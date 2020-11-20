/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.core.file.blockfile.cache;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.apache.accumulo.core.file.blockfile.cache.lru.CachedBlock;
import org.apache.accumulo.core.file.blockfile.cache.lru.CachedBlockQueue;
import org.junit.Test;
import org.mockito.Mockito;

public class TestCachedBlockQueue {

	@Test
	public void testQueue() {
		CachedBlock cb1 = Mockito
				.spy(new CachedBlock("cb1", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 1, false));
		CachedBlock cb2 = Mockito
				.spy(new CachedBlock("cb2", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 2, false));
		CachedBlock cb3 = Mockito
				.spy(new CachedBlock("cb3", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 3, false));
		CachedBlock cb4 = Mockito
				.spy(new CachedBlock("cb4", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 4, false));
		CachedBlock cb5 = Mockito
				.spy(new CachedBlock("cb5", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 5, false));
		CachedBlock cb6 = Mockito
				.spy(new CachedBlock("cb6", new byte[(int) (1750 - CachedBlock.PER_BLOCK_OVERHEAD)], 6, false));
		CachedBlock cb7 = Mockito
				.spy(new CachedBlock("cb7", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 7, false));
		CachedBlock cb8 = Mockito
				.spy(new CachedBlock("cb8", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 8, false));
		CachedBlock cb9 = Mockito
				.spy(new CachedBlock("cb9", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 9, false));
		CachedBlock cb10 = Mockito
				.spy(new CachedBlock("cb10", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 10, false));

		CachedBlockQueue queue = new CachedBlockQueue(10000, 1000);

		queue.add(cb1);
		queue.add(cb2);
		queue.add(cb3);
		queue.add(cb4);
		queue.add(cb5);
		queue.add(cb6);
		queue.add(cb7);
		queue.add(cb8);
		queue.add(cb9);
		queue.add(cb10);

		// We expect cb1 through cb8 to be in the queue
		long expectedSize = cb1.heapSize() + cb2.heapSize() + cb3.heapSize() + cb4.heapSize() + cb5.heapSize()
				+ cb6.heapSize() + cb7.heapSize() + cb8.heapSize();

		assertEquals(queue.heapSize(), expectedSize);

		LinkedList<CachedBlock> blocks = queue.getList();
		assertEquals(blocks.poll().getName(), "cb1");
		assertEquals(blocks.poll().getName(), "cb2");
		assertEquals(blocks.poll().getName(), "cb3");
		assertEquals(blocks.poll().getName(), "cb4");
		assertEquals(blocks.poll().getName(), "cb5");
		assertEquals(blocks.poll().getName(), "cb6");
		assertEquals(blocks.poll().getName(), "cb7");
		assertEquals(blocks.poll().getName(), "cb8");

	}

	@Test
	public void testQueueSmallBlockEdgeCase() {

		CachedBlock cb1 = Mockito
				.spy(new CachedBlock("cb1", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 1, false));
		CachedBlock cb2 = Mockito
				.spy(new CachedBlock("cb2", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 2, false));
		CachedBlock cb3 = Mockito
				.spy(new CachedBlock("cb3", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 3, false));
		CachedBlock cb4 = Mockito
				.spy(new CachedBlock("cb4", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 4, false));
		CachedBlock cb5 = Mockito
				.spy(new CachedBlock("cb5", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 5, false));
		CachedBlock cb6 = Mockito
				.spy(new CachedBlock("cb6", new byte[(int) (1750 - CachedBlock.PER_BLOCK_OVERHEAD)], 6, false));
		CachedBlock cb7 = Mockito
				.spy(new CachedBlock("cb7", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 7, false));
		CachedBlock cb8 = Mockito
				.spy(new CachedBlock("cb8", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 8, false));
		CachedBlock cb9 = Mockito
				.spy(new CachedBlock("cb9", new byte[(int) (1000 - CachedBlock.PER_BLOCK_OVERHEAD)], 9, false));
		CachedBlock cb10 = Mockito
				.spy(new CachedBlock("cb10", new byte[(int) (1500 - CachedBlock.PER_BLOCK_OVERHEAD)], 10, false));

		CachedBlockQueue queue = new CachedBlockQueue(10000, 1000);

		queue.add(cb1);
		queue.add(cb2);
		queue.add(cb3);
		queue.add(cb4);
		queue.add(cb5);
		queue.add(cb6);
		queue.add(cb7);
		queue.add(cb8);
		queue.add(cb9);
		queue.add(cb10);
		CachedBlock cb0 = Mockito.spy(new CachedBlock("cb0", new byte[(int) 10], 0, false));
		queue.add(cb0);

		// This is older so we must include it, but it will not end up kicking
		// anything out because (heapSize - cb8.heapSize + cb0.heapSize <
		// maxSize)
		// and we must always maintain heapSize >= maxSize once we achieve it.

		// We expect cb0 through cb8 to be in the queue
		long expectedSize = cb1.heapSize() + cb2.heapSize() + cb3.heapSize() + cb4.heapSize() + cb5.heapSize()
				+ cb6.heapSize() + cb7.heapSize() + cb8.heapSize() + cb0.heapSize();

		assertEquals(queue.heapSize(), expectedSize);

		LinkedList<CachedBlock> blocks = queue.getList();
		assertEquals(blocks.poll().getName(), "cb0");
		assertEquals(blocks.poll().getName(), "cb1");
		assertEquals(blocks.poll().getName(), "cb2");
		assertEquals(blocks.poll().getName(), "cb3");
		assertEquals(blocks.poll().getName(), "cb4");
		assertEquals(blocks.poll().getName(), "cb5");
		assertEquals(blocks.poll().getName(), "cb6");
		assertEquals(blocks.poll().getName(), "cb7");
		assertEquals(blocks.poll().getName(), "cb8");

	}

}
