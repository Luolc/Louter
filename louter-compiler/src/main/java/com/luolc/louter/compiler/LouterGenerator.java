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
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static com.luolc.louter.compiler.Config.ACTIVITY;
import static com.luolc.louter.compiler.Config.CENTRAL_NAVIGATOR;
import static com.luolc.louter.compiler.Config.CENTRAL_NAVIGATOR_CLASS_NAME;
import static com.luolc.louter.compiler.Config.CONTEXT;
import static com.luolc.louter.compiler.Config.FRAGMENT;
import static com.luolc.louter.compiler.Config.LOUTER;
import static com.luolc.louter.compiler.Config.LOUTER_BUILDER;
import static com.luolc.louter.compiler.Config.LOUTER_CLASS_NAME;
import static com.luolc.louter.compiler.Config.LOUTER_PACKAGE_NAME;
import static com.luolc.louter.compiler.Config.NON_NULL;
import static com.luolc.louter.compiler.Config.SUPPORT_FRAGMENT;

/**
 * @author LuoLiangchen
 * @since 2017/2/10
 */
class LouterGenerator {

  static JavaFile brewJava() {
    final TypeSpec root = TypeSpec.classBuilder(LOUTER_CLASS_NAME)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(ClassName.get(String.class), "mBaseUrl", Modifier.PRIVATE, Modifier.FINAL)
        .addMethod(createConstructor())
        .addMethod(createWithMethod(CONTEXT, "context"))
        .addMethod(createWithMethod(ACTIVITY, "activity"))
        .addMethod(createWithMethod(SUPPORT_FRAGMENT, "fragment"))
        .addMethod(createWithMethod(FRAGMENT, "fragment"))
        .addType(createBuilderClass())
        .build();
    return JavaFile.builder(LOUTER_PACKAGE_NAME, root).build();
  }

  private static MethodSpec createConstructor() {
    return MethodSpec.constructorBuilder()
        .addParameter(ClassName.get(String.class), "baseUrl")
        .addStatement("mBaseUrl = baseUrl")
        .build();
  }

  private static MethodSpec createWithMethod(final TypeName typeName, final String name) {
    return MethodSpec.methodBuilder("with")
        .addModifiers(Modifier.PUBLIC)
        .returns(CENTRAL_NAVIGATOR)
        .addParameter(ParameterSpec.builder(typeName, name).addAnnotation(NON_NULL).build())
        .addStatement("return new $L($L, mBaseUrl)", CENTRAL_NAVIGATOR_CLASS_NAME, name)
        .build();
  }

  private static TypeSpec createBuilderClass() {
    return TypeSpec.classBuilder("Builder")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
        .addField(ClassName.get(String.class), "mBaseUrl", Modifier.PRIVATE)
        .addMethod(createBuilderBaseUrlMethod())
        .addMethod(createBuilderBuildMethod())
        .build();
  }

  private static MethodSpec createBuilderBaseUrlMethod() {
    return MethodSpec.methodBuilder("baseUrl")
        .addModifiers(Modifier.PUBLIC)
        .returns(LOUTER_BUILDER)
        .addParameter(ParameterSpec.builder(ClassName.get(String.class), "baseUrl")
            .addAnnotation(NON_NULL)
            .build())
        .addStatement("mBaseUrl = baseUrl")
        .addStatement("return this")
        .build();
  }

  private static MethodSpec createBuilderBuildMethod() {
    return MethodSpec.methodBuilder("build")
        .addModifiers(Modifier.PUBLIC)
        .returns(LOUTER)
        .addStatement("return new $L(mBaseUrl)", LOUTER_CLASS_NAME)
        .build();
  }
}
