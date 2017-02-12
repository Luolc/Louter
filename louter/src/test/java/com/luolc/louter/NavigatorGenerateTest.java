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

package com.luolc.louter;

import com.google.testing.compile.JavaFileObjects;

import com.luolc.louter.compiler.LouterProcessor;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author LuoLiangchen
 * @since 2017/2/10
 */
public class NavigatorGenerateTest {

  @Test
  public void singleNavigatorWithoutParams() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Router", ""
        + "package test;\n"
        + "import com.luolc.louter.Navigator;\n"
        + "public interface Router {\n"
        + "  @Navigator(\"public_info/classroom/detail\")\n"
        + "  void publicInfoClassroomDetail();\n"
        + "}"
    );

    JavaFileObject generatedNavigatorSource = JavaFileObjects.forSourceString("test/LouterNavigator_PublicInfoClassroomDetail", ""
        + "package test;\n"
        + "\n"
        + "import android.content.Intent;\n"
        + "import android.support.annotation.AnimRes;\n"
        + "import com.luolc.louter.navigator.AbstractNavigator;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "\n"
        + "public final class LouterNavigator_PublicInfoClassroomDetail extends AbstractNavigator<LouterNavigator_PublicInfoClassroomDetail> {\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail(String baseUrl, Object starter) {\n"
        + "    super(baseUrl, \"public_info/classroom/detail\", starter);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public void start() {\n"
        + "    super.start();\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail addFlags(int flags) {\n"
        + "    return super.addFlags(flags);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail forResult(int requestCode) {\n"
        + "    return super.forResult(requestCode);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail withAnim(@AnimRes int enterAnim, @AnimRes int exitAnim) {\n"
        + "    return super.withAnim(enterAnim, exitAnim);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  protected boolean isAllRequiredParamsExist() {\n"
        + "    final Intent intent = getIntent();\n"
        + "    boolean result = true;\n"
        + "    return result;\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new LouterProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(generatedNavigatorSource);
  }

  @Test
  public void singleNavigatorWithNullableParam() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Router", ""
        + "package test;\n"
        + "import com.luolc.louter.Navigator;\n"
        + "import android.support.annotation.Nullable;\n"
        + "public interface Router {\n"
        + "  @Navigator(\"public_info/classroom/detail\")\n"
        + "  void publicInfoClassroomDetail(int holeId, String from, @Nullable boolean hasStarred);\n"
        + "}"
    );

    JavaFileObject generatedNavigatorSource = JavaFileObjects.forSourceString("test/LouterNavigator_PublicInfoClassroomDetail", ""
        + "package test;\n"
        + "\n"
        + "import android.content.Intent;\n"
        + "import android.support.annotation.AnimRes;\n"
        + "import com.luolc.louter.navigator.AbstractNavigator;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "\n"
        + "public final class LouterNavigator_PublicInfoClassroomDetail extends AbstractNavigator<LouterNavigator_PublicInfoClassroomDetail> {\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail(String baseUrl, Object starter) {\n"
        + "    super(baseUrl, \"public_info/classroom/detail\", starter);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public void start() {\n"
        + "    super.start();\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail addFlags(int flags) {\n"
        + "    return super.addFlags(flags);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail forResult(int requestCode) {\n"
        + "    return super.forResult(requestCode);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail withAnim(@AnimRes int enterAnim, @AnimRes int exitAnim) {\n"
        + "    return super.withAnim(enterAnim, exitAnim);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  protected boolean isAllRequiredParamsExist() {\n"
        + "    final Intent intent = getIntent();\n"
        + "    boolean result = true;\n"
        + "    result &= intent.hasExtra(\"holeId\");\n"
        + "    result &= intent.hasExtra(\"from\");\n"
        + "    return result;\n"
        + "  }\n"
        + "\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail holeId(int holeId) {\n"
        + "    getIntent().putExtra(\"holeId\", holeId);\n"
        + "    return this;\n"
        + "  }\n"
        + "\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail from(String from) {\n"
        + "    getIntent().putExtra(\"from\", from);\n"
        + "    return this;\n"
        + "  }\n"
        + "\n"
        + "  public LouterNavigator_PublicInfoClassroomDetail hasStarred(boolean hasStarred) {\n"
        + "    getIntent().putExtra(\"hasStarred\", hasStarred);\n"
        + "    return this;\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new LouterProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(generatedNavigatorSource);
  }
}
