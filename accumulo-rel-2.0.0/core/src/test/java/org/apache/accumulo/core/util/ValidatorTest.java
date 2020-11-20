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
package org.apache.accumulo.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ValidatorTest {
	private static class TestValidator extends Validator<String> {
		private final String s;

		TestValidator(String s) {
			this.s = s;
		}

		@Override
		public boolean test(String argument) {
			return s.equals(argument);
		}
	}

	private static class Test2Validator extends Validator<String> {
		private final String ps;

		Test2Validator(String s) {
			ps = s;
		}

		@Override
		public boolean test(String argument) {
			return (argument != null && argument.matches(ps));
		}
	}

	private Validator<String> v, v2, v3;

	private Validator<String> mockTestValidator(String s) {
		Validator<String> res = Mockito.mock(Validator.class, Mockito.CALLS_REAL_METHODS);
		Mockito.when(res.test(Mockito.anyString())).thenAnswer(invo -> {
			String arg = invo.getArgument(0);
			return s.equals(arg);
		});
		return res;
	}

	private Validator<String> mockTest2Validator(String ps) {
		Validator<String> res = Mockito.mock(Validator.class, Mockito.CALLS_REAL_METHODS);
		Mockito.when(res.test(Mockito.anyString())).thenAnswer(invo -> {
			String argument = invo.getArgument(0);
			return (argument != null && argument.matches(ps));
		});
		return res;
	}

	@Before
	public void setUp() {

		v = mockTestValidator("correct");
		v2 = mockTestValidator("righto");
		v3 = mockTest2Validator("c.*");
	}

	@Test
	public void testValidate_Success() {
		assertEquals("correct", v.validate("correct"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidate_Failure() {
		v.validate("incorrect");
	}

	@Test
	public void testInvalidMessage() {
		assertEquals("Invalid argument incorrect", v.invalidMessage("incorrect"));
	}

	@Test
	public void testAnd() {
		Validator<String> vand = v3.and(v);
		assertTrue(vand.test("correct"));
		assertFalse(vand.test("righto"));
		assertFalse(vand.test("coriander"));
	}

	@Test
	public void testOr() {
		Validator<String> vor = v.or(v2);
		assertTrue(vor.test("correct"));
		assertTrue(vor.test("righto"));
		assertFalse(vor.test("coriander"));
	}

	@Test
	public void testNot() {
		Validator<String> vnot = v3.not();
		assertFalse(vnot.test("correct"));
		assertFalse(vnot.test("coriander"));
		assertTrue(vnot.test("righto"));
	}
}
