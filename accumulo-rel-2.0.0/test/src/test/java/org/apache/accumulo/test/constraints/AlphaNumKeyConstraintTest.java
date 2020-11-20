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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.accumulo.core.constraints.Constraint;
import org.apache.accumulo.core.constraints.Constraint.Environment;
import org.apache.accumulo.core.data.ColumnUpdate;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;

public class AlphaNumKeyConstraintTest {

	private Constraint ankc;

	@Before
	public void setup() {
		final short NON_ALPHA_NUM_ROW = 1;
		final short NON_ALPHA_NUM_COLF = 2;
		final short NON_ALPHA_NUM_COLQ = 3;

		final String ROW_VIOLATION_MESSAGE = "Row was not alpha numeric";
		final String COLF_VIOLATION_MESSAGE = "Column family was not alpha numeric";
		final String COLQ_VIOLATION_MESSAGE = "Column qualifier was not alpha numeric";
		ankc = Mockito.mock(Constraint.class);
		Mockito.when(ankc.check(Mockito.any(), Mockito.any())).thenAnswer(invo -> {
			Environment env = invo.getArgument(0);
			Mutation mutation = invo.getArgument(1);
			Set<Short> violations = null;

			if (!isAlphaNum(mutation.getRow())) violations = addViolation(violations, NON_ALPHA_NUM_ROW);

			Collection<ColumnUpdate> updates = mutation.getUpdates();
			for (ColumnUpdate columnUpdate : updates) {
				if (!isAlphaNum(columnUpdate.getColumnFamily()))
					violations = addViolation(violations, NON_ALPHA_NUM_COLF);

				if (!isAlphaNum(columnUpdate.getColumnQualifier()))
					violations = addViolation(violations, NON_ALPHA_NUM_COLQ);
			}

			return violations == null ? null : new ArrayList<>(violations);
		});

		Mockito.when(ankc.getViolationDescription(Mockito.anyShort())).thenAnswer(invo -> {
			short violationCode = invo.getArgument(0);
			switch (violationCode) {
			case NON_ALPHA_NUM_ROW:
				return ROW_VIOLATION_MESSAGE;
			case NON_ALPHA_NUM_COLF:
				return COLF_VIOLATION_MESSAGE;
			case NON_ALPHA_NUM_COLQ:
				return COLQ_VIOLATION_MESSAGE;
			}

			return null;
		});

	}

	private boolean isAlphaNum(byte[] bytes) {
		for (byte b : bytes) {
			boolean ok = ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || (b >= '0' && b <= '9'));
			if (!ok) return false;
		}

		return true;
	}

	private Set<Short> addViolation(Set<Short> violations, short violation) {
		if (violations == null) {
			violations = new LinkedHashSet<>();
			violations.add(violation);
		} else if (!violations.contains(violation)) {
			violations.add(violation);
		}
		return violations;
	}

	@Test
	public void test() {
		Mutation goodMutation = new Mutation(new Text("Row1"));
		goodMutation.put(new Text("Colf2"), new Text("ColQ3"), new Value("value".getBytes()));
		assertNull(ankc.check(null, goodMutation));

		// Check that violations are in row, cf, cq order
		Mutation badMutation = new Mutation(new Text("Row#1"));
		badMutation.put(new Text("Colf$2"), new Text("Colq%3"), new Value("value".getBytes()));
		assertEquals(ImmutableList.of(AlphaNumKeyConstraint.NON_ALPHA_NUM_ROW, AlphaNumKeyConstraint.NON_ALPHA_NUM_COLF,
				AlphaNumKeyConstraint.NON_ALPHA_NUM_COLQ), ankc.check(null, badMutation));
	}

	@Test
	public void testGetViolationDescription() {
		assertEquals(AlphaNumKeyConstraint.ROW_VIOLATION_MESSAGE,
				ankc.getViolationDescription(AlphaNumKeyConstraint.NON_ALPHA_NUM_ROW));
		assertEquals(AlphaNumKeyConstraint.COLF_VIOLATION_MESSAGE,
				ankc.getViolationDescription(AlphaNumKeyConstraint.NON_ALPHA_NUM_COLF));
		assertEquals(AlphaNumKeyConstraint.COLQ_VIOLATION_MESSAGE,
				ankc.getViolationDescription(AlphaNumKeyConstraint.NON_ALPHA_NUM_COLQ));
		assertNull(ankc.getViolationDescription((short) 4));
	}
}
