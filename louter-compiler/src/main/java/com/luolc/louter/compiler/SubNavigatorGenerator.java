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
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * @author LuoLiangchen
 * @since 2017/1/11
 */
public class SubNavigatorGenerator {

  private static final String ABSTRACT_NAVIGATOR_PACKAGE_NAME = "com.luolc.louter.navigator";
  private static final String ABSTRACT_NAVIGATOR_CLASS_NAME = "AbstractNavigator";

  private final String mPath;

  private final List<NavigationParam> mParams;

  private final String mPackageName;

  public SubNavigatorGenerator(final Navigator annotation, final List<NavigationParam> params,
                               final String packageName) {
    mPath = annotation.value();
    mParams = params;
    mPackageName = packageName;
  }

  public JavaFile brewJava() {
    final ClassName abstractNavigator = ClassName.get(
        ABSTRACT_NAVIGATOR_PACKAGE_NAME, ABSTRACT_NAVIGATOR_CLASS_NAME);
    final TypeName thisClass = ClassName.get(mPackageName, generateSimpleClassName());
    final TypeName superClass = ParameterizedTypeName.get(abstractNavigator, thisClass);
    final TypeSpec.Builder navigatorTypeSpec = TypeSpec.classBuilder(generateSimpleClassName())
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(superClass);
    emitParamSetter(navigatorTypeSpec);
    return JavaFile.builder(mPackageName, navigatorTypeSpec.build()).build();
  }

  String generateSimpleClassName() {
    return Arrays.stream(mPath.split("/|_"))
        .filter(word -> !word.isEmpty())
        .map(this::capitalizeFully)
        .reduce("LouterNavigator_", (x, y) -> x + y);
  }

  String capitalizeFully(final String word) {
    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
  }

  private void emitParamSetter(final TypeSpec.Builder builder) {
    // TODO: 2017/1/12 emit method
  }
}
