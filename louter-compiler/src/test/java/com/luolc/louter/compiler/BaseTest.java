/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Luolc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.luolc.louter.compiler;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseTest {

  protected BaseTest() {}

  protected static void fail(String... messages) {
    if (messages.length == 0) {
      throw new AssertionError();
    } else if (messages.length == 1) {
      throw new AssertionError(messages[0]);
    } else {
      Arrays.stream(messages).reduce((x, y) -> x + ", " + y).ifPresent(msg -> {
        throw new AssertionError(msg);
      });
    }
  }

  protected static void assertException(
      Action action, Class<? extends Throwable> expectedThrowable, String expectedMsg) {
    try {
      action.call();
      fail("expect exception " + expectedThrowable.getCanonicalName() + " with msg: " + expectedMsg);
    } catch (Throwable actual) {
      assertTrue(expectedThrowable.isAssignableFrom(actual.getClass()));
      assertEquals(expectedMsg, actual.getMessage());
    }
  }

  protected interface Action {
    void call();
  }
}
