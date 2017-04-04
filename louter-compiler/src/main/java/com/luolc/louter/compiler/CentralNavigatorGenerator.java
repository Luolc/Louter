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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

import static com.luolc.louter.compiler.Config.CENTRAL_NAVIGATOR_CLASS_NAME;
import static com.luolc.louter.compiler.Config.CENTRAL_NAVIGATOR_PACKAGE_NAME;

/**
 * @author LuoLiangchen
 * @since 2017/2/10
 */
final class CentralNavigatorGenerator {

  private final List<SubNavigatorGenerator> mSubNavigatorGenerators;

  CentralNavigatorGenerator(final List<SubNavigatorGenerator> subNavigatorGenerators) {
    mSubNavigatorGenerators = subNavigatorGenerators;
    Collections.sort(mSubNavigatorGenerators);
  }

  JavaFile brewJava() {
    final TypeSpec root = TypeSpec.classBuilder(CENTRAL_NAVIGATOR_CLASS_NAME)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(ClassName.OBJECT, "mStarter", Modifier.PRIVATE, Modifier.FINAL)
        .addField(ClassName.get(String.class), "mBaseUrl", Modifier.PRIVATE, Modifier.FINAL)
        .addMethod(createConstructor())
        .addMethods(createGetNavigatorMethods())
        .build();
    return JavaFile.builder(CENTRAL_NAVIGATOR_PACKAGE_NAME, root).build();
  }

  private static MethodSpec createConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ClassName.OBJECT, "starter")
        .addParameter(ClassName.get(String.class), "baseUrl")
        .addStatement("mStarter = starter")
        .addStatement("mBaseUrl = baseUrl")
        .build();
  }

  private Iterable<MethodSpec> createGetNavigatorMethods() {
    return mSubNavigatorGenerators.stream()
        .map(this::createGetNavigatorMethod)
        ::iterator;
  }

  private MethodSpec createGetNavigatorMethod(final SubNavigatorGenerator generator) {
    final String navigatorSimpleName = generator.generateSimpleClassName();
    final TypeName navigator = ClassName.get(generator.getPackageName(), navigatorSimpleName);
    return MethodSpec.methodBuilder("to" + generator.generateTargetName())
        .addModifiers(Modifier.PUBLIC)
        .returns(navigator)
        .addStatement("return new $L(mBaseUrl, mStarter)", navigatorSimpleName)
        .build();
  }
}
