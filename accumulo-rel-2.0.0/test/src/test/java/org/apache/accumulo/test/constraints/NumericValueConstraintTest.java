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
package org.apache.accumulo.test.constraints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.accumulo.core.constraints.Constraint;
import org.apache.accumulo.core.data.ColumnUpdate;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Iterables;

public class NumericValueConstraintTest {

	private Constraint nvc;

	@Before
	public void setup() {
		nvc = Mockito.mock(Constraint.class);
		final short[] NON_NUMERIC_VALUE = new short[] { 1 };
		String[] VIOLATION_MESSAGE = new String[] { "Value is not numeric" };
		List<Short> VIOLATION_LIST = Collections.unmodifiableList(Arrays.asList(NON_NUMERIC_VALUE[0]));
		Mockito.when(nvc.check(Mockito.any(), Mockito.any(Mutation.class))).thenAnswer(invo -> {
			System.out.println("?");
			Mutation mutation = invo.getArgument(1);
			Collection<ColumnUpdate> updates = mutation.getUpdates();
			for (ColumnUpdate columnUpdate : updates) {
				if (!isNumeric(columnUpdate.getValue())) return VIOLATION_LIST;
			}

			return null;
		});
		Mockito.when(nvc.getViolationDescription(Mockito.anyShort())).thenAnswer(invo -> {
			short violationCode = invo.getArgument(0);
			switch (violationCode) {
			case 1:
				return "Value is not numeric";
			}
			return null;
		});
	}

	private boolean isNumeric(byte[] bytes) {
		for (byte b : bytes) {
			boolean ok = (b >= '0' && b <= '9');
			if (!ok) return false;
		}

		return true;
	}

	@Test
	public void testCheck() {
		Mutation goodMutation = new Mutation(new Text("r"));
		goodMutation.put(new Text("cf"), new Text("cq"), new Value("1234".getBytes()));
		assertNull(nvc.check(null, goodMutation));

		// Check that multiple bad mutations result in one violation only
		Mutation badMutation = new Mutation(new Text("r"));
		badMutation.put(new Text("cf"), new Text("cq"), new Value("foo1234".getBytes()));
		badMutation.put(new Text("cf2"), new Text("cq2"), new Value("foo1234".getBytes()));
		assertEquals(NumericValueConstraint.NON_NUMERIC_VALUE,
				Iterables.getOnlyElement(nvc.check(null, badMutation)).shortValue());
	}

	@Test
	public void testGetViolationDescription() {
		assertEquals(NumericValueConstraint.VIOLATION_MESSAGE,
				nvc.getViolationDescription(NumericValueConstraint.NON_NUMERIC_VALUE));
		assertNull(nvc.getViolationDescription((short) 2));
	}
}
