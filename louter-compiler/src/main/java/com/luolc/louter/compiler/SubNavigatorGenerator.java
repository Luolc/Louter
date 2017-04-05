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
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.MethodSpec.methodBuilder;

/**
 * @author LuoLiangchen
 * @since 2017/1/11
 */
final class SubNavigatorGenerator implements Comparable<SubNavigatorGenerator> {

  private static final String ABSTRACT_NAVIGATOR_PACKAGE_NAME = "com.luolc.louter";

  private static final String ABSTRACT_NAVIGATOR_CLASS_NAME = "AbstractNavigator";

  private final String mPath;

  private final List<NavigationParam> mParams;

  private final String mPackageName;

  private final String mSimpleClassName;

  private final ClassName mThisClass;

  SubNavigatorGenerator(final Navigator annotation, final List<NavigationParam> params,
                        final String packageName) {
    mPath = annotation.value();
    mParams = params;
    mPackageName = packageName;
    mSimpleClassName = generateSimpleClassName();
    mThisClass = ClassName.get(mPackageName, generateSimpleClassName());
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public int compareTo(final SubNavigatorGenerator generator) {
    return mSimpleClassName.compareTo(generator.generateSimpleClassName());
  }

  JavaFile brewJava() {
    final ClassName abstractNavigator = ClassName.get(
        ABSTRACT_NAVIGATOR_PACKAGE_NAME, ABSTRACT_NAVIGATOR_CLASS_NAME);
    final TypeName superClass = ParameterizedTypeName.get(abstractNavigator, mThisClass);
    final TypeSpec root = TypeSpec.classBuilder(mSimpleClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(superClass)
        .addMethod(createConstructor())
        .addMethod(createStartMethod())
        .addMethod(createAddFlagsMethod())
        .addMethod(createForResultMethod())
        .addMethod(createWithAnimMethod())
        .addMethod(createRequiredParamsCheckMethod())
        .addMethods(createParamSetters())
        .build();
    return JavaFile.builder(mPackageName, root).build();
  }

  String getPackageName() {
    return mPackageName;
  }

  String generateSimpleClassName() {
    return "LouterNavigator_" + generateTargetName();
  }

  String generateTargetName() {
    return Arrays.stream(mPath.split("/|_"))
        .filter(word -> !word.isEmpty())
        .map(SubNavigatorGenerator::capitalizeFully)
        .reduce("", (x, y) -> x + y);
  }

  static String capitalizeFully(final String word) {
    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
  }

  private MethodSpec createConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ClassName.get(String.class), "baseUrl")
        .addParameter(ClassName.OBJECT, "starter")
        .addStatement("super(baseUrl, $S, starter)", mPath)
        .build();
  }

  private static MethodSpec createStartMethod() {
    return methodBuilder("start")
        .addModifiers(Modifier.PUBLIC)
        .returns(TypeName.VOID)
        .addAnnotation(Override.class)
        .addStatement("super.start()")
        .build();
  }

  private MethodSpec createAddFlagsMethod() {
    return methodBuilder("addFlags")
        .addModifiers(Modifier.PUBLIC)
        .returns(mThisClass)
        .addAnnotation(Override.class)
        .addParameter(TypeName.INT, "flags")
        .addStatement("return super.addFlags(flags)")
        .build();
  }

  private MethodSpec createForResultMethod() {
    return methodBuilder("forResult")
        .addModifiers(Modifier.PUBLIC)
        .returns(mThisClass)
        .addAnnotation(Override.class)
        .addParameter(TypeName.INT, "requestCode")
        .addStatement("return super.forResult(requestCode)")
        .build();
  }

  private MethodSpec createWithAnimMethod() {
    final ClassName animRes = ClassName.get("android.support.annotation", "AnimRes");
    return methodBuilder("withAnim")
        .addModifiers(Modifier.PUBLIC)
        .returns(mThisClass)
        .addAnnotation(Override.class)
        .addParameter(ParameterSpec.builder(TypeName.INT, "enterAnim").addAnnotation(animRes).build())
        .addParameter(ParameterSpec.builder(TypeName.INT, "exitAnim").addAnnotation(animRes).build())
        .addStatement("return super.withAnim(enterAnim, exitAnim)")
        .build();
  }

  private MethodSpec createRequiredParamsCheckMethod() {
    final ClassName intent = ClassName.get("android.content", "Intent");
    final MethodSpec.Builder builder = methodBuilder("isAllRequiredParamsExist")
        .addModifiers(Modifier.PROTECTED)
        .returns(TypeName.BOOLEAN)
        .addAnnotation(Override.class)
        .addStatement("final $T intent = getIntent()", intent)
        .addStatement("boolean result = true");
    mParams.stream()
        .filter(NavigationParam::isRequired)
        .forEach(param -> builder.addStatement("result &= intent.hasExtra($S)", param.getKey()));
    builder.addStatement("return result");
    return builder.build();
  }

  private Iterable<MethodSpec> createParamSetters() {
    return mParams.stream()
        .map(this::createParamSetter)
        ::iterator;
  }

  private MethodSpec createParamSetter(final NavigationParam param) {
    return methodBuilder(param.getKey())
        .addModifiers(Modifier.PUBLIC)
        .returns(mThisClass)
        .addParameter(param.getParamType(), param.getKey())
        .addStatement("getIntent().putExtra($S, $L)", param.getKey(), param.getKey())
        .addStatement("return this")
        .build();
  }
}
