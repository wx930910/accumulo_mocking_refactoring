/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.fate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.accumulo.fate.AgeOffStore.TimeSource;
import org.apache.accumulo.fate.ReadOnlyTStore.TStatus;
import org.junit.Test;
import org.mockito.Mockito;

public class AgeOffStoreTest {

	private static class TestTimeSource implements TimeSource {
		long time = 0;

		@Override
		public long currentTimeMillis() {
			return time;
		}

	}

	@Test
	public void testBasic() {

		TimeSource tts = Mockito.mock(TimeSource.class);
		long[] time = { 0 };
		Mockito.when(tts.currentTimeMillis()).thenAnswer(invo -> {
			return time[0];
		});
		TStore<String> sstore = mockTStore(String.class);
		AgeOffStore<String> aoStore = new AgeOffStore<>(sstore, 10, tts);

		aoStore.ageOff();

		long txid1 = aoStore.create();
		aoStore.reserve(txid1);
		aoStore.setStatus(txid1, TStatus.IN_PROGRESS);
		aoStore.unreserve(txid1, 0);

		aoStore.ageOff();

		long txid2 = aoStore.create();
		aoStore.reserve(txid2);
		aoStore.setStatus(txid2, TStatus.IN_PROGRESS);
		aoStore.setStatus(txid2, TStatus.FAILED);
		aoStore.unreserve(txid2, 0);

		time[0] = 6;

		long txid3 = aoStore.create();
		aoStore.reserve(txid3);
		aoStore.setStatus(txid3, TStatus.IN_PROGRESS);
		aoStore.setStatus(txid3, TStatus.SUCCESSFUL);
		aoStore.unreserve(txid3, 0);

		Long txid4 = aoStore.create();

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1, txid2, txid3, txid4)), new HashSet<>(aoStore.list()));
		assertEquals(4, new HashSet<>(aoStore.list()).size());

		time[0] = 15;

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1, txid3, txid4)), new HashSet<>(aoStore.list()));
		assertEquals(3, new HashSet<>(aoStore.list()).size());

		time[0] = 30;

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1)), new HashSet<>(aoStore.list()));
		assertEquals(1, new HashSet<>(aoStore.list()).size());
	}

	private <T> TStore<T> mockTStore(Class<?> T) {
		long[] nextId = { 1 };
		Map<Long, TStatus> statuses = new HashMap<>();
		Set<Long> reserved = new HashSet<>();
		TStore<T> res = Mockito.mock(TStore.class);
		Mockito.when(res.create()).thenAnswer(invo -> {
			statuses.put(nextId[0], TStatus.NEW);
			return nextId[0]++;
		});
		Mockito.when(res.reserve()).thenThrow(new UnsupportedOperationException());
		Mockito.doAnswer(invo -> {
			long tid = invo.getArgument(0);
			if (reserved.contains(tid)) throw new IllegalStateException(); // zoo
																			// store
																			// would
																			// wait,
																			// but
																			// do
																			// not
																			// expect
																			// test
																			// to
																			// reserve
																			// twice...
																			// if
																			// test
																			// change,
																			// then
																			// change
																			// this
			reserved.add(tid);
			return null;
		}).when(res).reserve(Mockito.anyLong());
		Mockito.doAnswer(invo -> {
			long tid = invo.getArgument(0);
			long deferTime = invo.getArgument(1);
			if (!reserved.remove(tid)) {
				throw new IllegalStateException();
			}
			return null;
		}).when(res).unreserve(Mockito.anyLong(), Mockito.anyLong());
		Mockito.when(res.top(Mockito.anyLong())).thenThrow(new UnsupportedOperationException());
		try {
			Mockito.doThrow(new UnsupportedOperationException()).when(res).push(Mockito.anyLong(), Mockito.any());
			Mockito.doThrow(new UnsupportedOperationException()).when(res).pop(Mockito.anyLong());

		} catch (StackOverflowException e) {
			e.printStackTrace();
		}
		Mockito.when(res.getStatus(Mockito.anyLong())).thenAnswer(invo -> {
			long tid = invo.getArgument(0);
			if (!reserved.contains(tid)) throw new IllegalStateException();

			TStatus status = statuses.get(tid);
			if (status == null) return TStatus.UNKNOWN;
			return status;
		});
		Mockito.doAnswer(invo -> {
			long tid = invo.getArgument(0);
			org.apache.accumulo.fate.TStore.TStatus status = invo.getArgument(1);
			if (!reserved.contains(tid)) throw new IllegalStateException();
			if (!statuses.containsKey(tid)) throw new IllegalStateException();
			statuses.put(tid, status);
			return null;
		}).when(res).setStatus(Mockito.anyLong(), Mockito.any());
		Mockito.when(res.waitForStatusChange(Mockito.anyLong(), Mockito.any()))
				.thenThrow(new UnsupportedOperationException());
		Mockito.doThrow(new UnsupportedOperationException()).when(res).setProperty(Mockito.anyLong(),
				Mockito.anyString(), Mockito.any());
		Mockito.when(res.getProperty(Mockito.anyLong(), Mockito.any())).thenThrow(new UnsupportedOperationException());
		Mockito.doAnswer(invo -> {
			long tid = invo.getArgument(0);
			if (!reserved.contains(tid)) throw new IllegalStateException();
			statuses.remove(tid);
			return null;
		}).when(res).delete(Mockito.anyLong());
		Mockito.when(res.list()).thenAnswer(invo -> {
			return new ArrayList<>(statuses.keySet());
		});
		Mockito.when(res.getStack(Mockito.anyLong())).thenThrow(new UnsupportedOperationException());
		return res;
	}

	@Test
	public void testNonEmpty() {
		// test age off when source store starts off non empty

		TestTimeSource tts = new TestTimeSource();
		TStore<String> sstore = new SimpleStore<>();
		long txid1 = sstore.create();
		sstore.reserve(txid1);
		sstore.setStatus(txid1, TStatus.IN_PROGRESS);
		sstore.unreserve(txid1, 0);

		long txid2 = sstore.create();
		sstore.reserve(txid2);
		sstore.setStatus(txid2, TStatus.IN_PROGRESS);
		sstore.setStatus(txid2, TStatus.FAILED);
		sstore.unreserve(txid2, 0);

		long txid3 = sstore.create();
		sstore.reserve(txid3);
		sstore.setStatus(txid3, TStatus.IN_PROGRESS);
		sstore.setStatus(txid3, TStatus.SUCCESSFUL);
		sstore.unreserve(txid3, 0);

		Long txid4 = sstore.create();

		AgeOffStore<String> aoStore = new AgeOffStore<>(sstore, 10, tts);

		assertEquals(new HashSet<>(Arrays.asList(txid1, txid2, txid3, txid4)), new HashSet<>(aoStore.list()));
		assertEquals(4, new HashSet<>(aoStore.list()).size());

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1, txid2, txid3, txid4)), new HashSet<>(aoStore.list()));
		assertEquals(4, new HashSet<>(aoStore.list()).size());

		tts.time = 15;

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1)), new HashSet<>(aoStore.list()));
		assertEquals(1, new HashSet<>(aoStore.list()).size());

		aoStore.reserve(txid1);
		aoStore.setStatus(txid1, TStatus.FAILED_IN_PROGRESS);
		aoStore.unreserve(txid1, 0);

		tts.time = 30;

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1)), new HashSet<>(aoStore.list()));
		assertEquals(1, new HashSet<>(aoStore.list()).size());

		aoStore.reserve(txid1);
		aoStore.setStatus(txid1, TStatus.FAILED);
		aoStore.unreserve(txid1, 0);

		aoStore.ageOff();

		assertEquals(new HashSet<>(Arrays.asList(txid1)), new HashSet<>(aoStore.list()));
		assertEquals(1, new HashSet<>(aoStore.list()).size());

		tts.time = 42;

		aoStore.ageOff();

		assertEquals(0, new HashSet<>(aoStore.list()).size());
	}
}
