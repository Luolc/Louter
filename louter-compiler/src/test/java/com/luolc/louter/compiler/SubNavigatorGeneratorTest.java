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

import com.luolc.louter.Navigator;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static junit.framework.TestCase.assertEquals;

/**
 * @author LuoLiangchen
 * @since 2017/1/12
 */
public class SubNavigatorGeneratorTest {

  private SubNavigatorGenerator mSubNavigatorGenerator;

  @Before
  public void setUp() {
    Navigator annotation = new Navigator() {
      @Override
      public String value() {
        return "public_info/classroom/detail";
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Navigator.class;
      }
    };
    mSubNavigatorGenerator = new SubNavigatorGenerator(annotation, null, "com.example");
  }

  @Test
  public void generateSimpleClassName() {
    assertEquals("LouterNavigator_PublicInfoClassroomDetail",
        mSubNavigatorGenerator.generateSimpleClassName());
  }

  @Test
  public void capitalizeFullyWithCommonWord() {
    assertEquals("Hello", mSubNavigatorGenerator.capitalizeFully("hElLO"));
  }

  @Test
  public void capitalizeFullyWithSingleLetter() {
    assertEquals("A", mSubNavigatorGenerator.capitalizeFully("a"));
  }

  @Test
  public void capitalizeFullyWithNonLetter() {
    assertEquals("1", mSubNavigatorGenerator.capitalizeFully("1"));
  }
}
