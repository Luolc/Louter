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

import com.google.testing.compile.JavaFileObjects;

import com.luolc.louter.Navigator;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.LinkedList;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.luolc.louter.compiler.SubNavigatorGenerator.MSG_CHECK_PATH;
import static org.junit.Assert.assertEquals;

/**
 * @author LuoLiangchen
 * @since 2017/1/12
 */
public class SubNavigatorGeneratorTest extends BaseTest {

  private SubNavigatorGenerator mSubNavigatorGenerator;

  @Before
  public void setUp() {
    Navigator annotation = createNavigatorAnnotation("public_info/classroom/detail");
    mSubNavigatorGenerator = new SubNavigatorGenerator(annotation, new LinkedList<>(), "com.example");
  }

  @Test
  public void testIntegration() {
    JavaFileObject source = JavaFileObjects.forResource("com/luolc/louter/compiler/InputRouter.java");
    JavaFileObject expectedPublicInfoClassroomDetail = JavaFileObjects.forResource(
        "com/luolc/louter/compiler/ExpectedLouterNavigator_PublicInfoClassroomDetail.java");
    JavaFileObject expectedHoleList = JavaFileObjects.forResource(
        "com/luolc/louter/compiler/ExpectedLouterNavigator_HoleList.java");

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new LouterProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(expectedPublicInfoClassroomDetail, expectedHoleList);
  }

  @Test
  public void testCheckPath() {
    assertException(
        () -> new SubNavigatorGenerator(createNavigatorAnnotation("a$c"), new LinkedList<>(), ""),
        IllegalArgumentException.class,
        MSG_CHECK_PATH + "a$c");
    assertException(
        () -> new SubNavigatorGenerator(createNavigatorAnnotation("a//b"), new LinkedList<>(), ""),
        IllegalArgumentException.class,
        MSG_CHECK_PATH + "a//b");
    assertException(
        () -> new SubNavigatorGenerator(createNavigatorAnnotation("a/_b"), new LinkedList<>(), ""),
        IllegalArgumentException.class,
        MSG_CHECK_PATH + "a/_b");
    assertException(
        () -> new SubNavigatorGenerator(createNavigatorAnnotation("a_/b"), new LinkedList<>(), ""),
        IllegalArgumentException.class,
        MSG_CHECK_PATH + "a_/b");
    assertException(
        () -> new SubNavigatorGenerator(createNavigatorAnnotation("a__b"), new LinkedList<>(), ""),
        IllegalArgumentException.class,
        MSG_CHECK_PATH + "a__b");
  }

  private Navigator createNavigatorAnnotation(String value) {
    return new Navigator() {
      @Override
      public String value() {
        return value;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Navigator.class;
      }
    };
  }

  @Test
  public void testPackageName() {
    assertEquals("com.example", mSubNavigatorGenerator.getPackageName());
  }

  @Test
  public void testGenerateSimpleClassName() {
    assertEquals("LouterNavigator_PublicInfoClassroomDetail",
        mSubNavigatorGenerator.generateSimpleClassName());
  }

  @Test
  public void testCapitalizeFullyWithCommonWord() {
    assertEquals("Hello", SubNavigatorGenerator.capitalizeFully("hElLO"));
  }

  @Test
  public void testCapitalizeFullyWithSingleLetter() {
    assertEquals("A", SubNavigatorGenerator.capitalizeFully("a"));
  }

  @Test
  public void testCapitalizeFullyWithNonLetter() {
    assertEquals("1", SubNavigatorGenerator.capitalizeFully("1"));
  }
}
