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
package org.apache.accumulo.server.util.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

public class BaseRelativeTimeTest {

	static class BogusTime implements ProvidesTime {
		public long value = 0;

		@Override
		public long currentTime() {
			return value;
		}
	}

	@Test
	public void testMatchesTime() {
		long[] btValue = new long[1];
		long[] nowValue = new long[1];

		ProvidesTime bt = Mockito.mock(ProvidesTime.class);
		Mockito.when(bt.currentTime()).thenAnswer(invo -> {
			return btValue[0];
		});
		ProvidesTime now = Mockito.mock(ProvidesTime.class);
		Mockito.when(now.currentTime()).thenAnswer(invo -> {
			return nowValue[0];
		});
		nowValue[0] = btValue[0] = System.currentTimeMillis();

		BaseRelativeTime brt = new BaseRelativeTime(now);
		assertEquals(brt.currentTime(), nowValue[0]);
		brt.updateTime(nowValue[0]);
		assertEquals(brt.currentTime(), nowValue[0]);
	}

	@Test
	public void testFutureTime() {
		BogusTime advice = new BogusTime();
		BogusTime local = new BogusTime();
		local.value = advice.value = System.currentTimeMillis();
		// Ten seconds into the future
		advice.value += 10000;

		BaseRelativeTime brt = new BaseRelativeTime(local);
		assertEquals(brt.currentTime(), local.value);
		brt.updateTime(advice.value);
		long once = brt.currentTime();
		assertTrue(once < advice.value);
		assertTrue(once > local.value);

		for (int i = 0; i < 100; i++) {
			brt.updateTime(advice.value);
		}
		long many = brt.currentTime();
		assertTrue(many > once);
		assertTrue("after much advice, relative time is still closer to local time",
				(advice.value - many) < (once - local.value));
	}

	@Test
	public void testPastTime() {
		BogusTime advice = new BogusTime();
		BogusTime local = new BogusTime();
		local.value = advice.value = System.currentTimeMillis();
		// Ten seconds into the past
		advice.value -= 10000;

		BaseRelativeTime brt = new BaseRelativeTime(local);
		brt.updateTime(advice.value);
		long once = brt.currentTime();
		assertTrue(once < local.value);
		brt.updateTime(advice.value);
		long twice = brt.currentTime();
		assertTrue("Time cannot go backwards", once <= twice);
		brt.updateTime(advice.value - 10000);
		assertTrue("Time cannot go backwards", once <= twice);
	}

}
