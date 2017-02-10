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
public class LouterGenerateTest {

  @Test
  public void generate() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Router", ""
        + "package test;\n"
        + "import com.luolc.louter.Navigator;\n"
        + "import android.support.annotation.Nullable;\n"
        + "public interface Router {\n"
        + "  @Navigator(\"public_info/classroom/detail\")\n"
        + "  void publicInfoClassroomDetail(int holeId, String from, @Nullable boolean hasStarred);\n"
        + "}"
    );

    JavaFileObject louter = JavaFileObjects.forSourceString("com/luolc/louter/Louter", ""
        + "package com.luolc.louter;\n"
        + "\n"
        + "import android.app.Activity;\n"
        + "import android.content.Context;\n"
        + "import android.support.annotation.NonNull;\n"
        + "import android.support.v4.app.Fragment;\n"
        + "import java.lang.String;\n"
        + "\n"
        + "public final class Louter {\n"
        + "\n"
        + "  private final String mBaseUrl;\n"
        + "\n"
        + "  Louter(String baseUrl) {\n"
        + "    mBaseUrl = baseUrl;\n"
        + "  }\n"
        + "\n"
        + "  public CentralNavigator with(@NonNull Context context) {\n"
        + "    return new CentralNavigator(context, mBaseUrl);\n"
        + "  }\n"
        + "\n"
        + "  public CentralNavigator with(@NonNull Activity activity) {\n"
        + "    return new CentralNavigator(activity, mBaseUrl);\n"
        + "  }\n"
        + "\n"
        + "  public CentralNavigator with(@NonNull Fragment fragment) {\n"
        + "    return new CentralNavigator(fragment, mBaseUrl);\n"
        + "  }\n"
        + "\n"
        + "  public CentralNavigator with(@NonNull android.app.Fragment fragment) {\n"
        + "    return new CentralNavigator(fragment, mBaseUrl);\n"
        + "  }\n"
        + "\n"
        + "  public static final class Builder {\n"
        + "\n"
        + "    private String mBaseUrl;\n"
        + "\n"
        + "    public Builder baseUrl(@NonNull String baseUrl) {\n"
        + "      mBaseUrl = baseUrl;\n"
        + "      return this;\n"
        + "    }\n"
        + "\n"
        + "    public Louter build() {\n"
        + "      return new Louter(mBaseUrl);\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new LouterProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(louter);
  }
}