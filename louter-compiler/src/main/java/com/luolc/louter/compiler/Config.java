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
import com.squareup.javapoet.TypeName;

/**
 * @author LuoLiangchen
 * @since 2017/2/10
 */
final class Config {

  @SuppressWarnings("WeakerAccess")
  static final String ROOT_PACKAGE = "com.luolc.louter";

  static final String LOUTER_PACKAGE_NAME = ROOT_PACKAGE;

  static final String LOUTER_CLASS_NAME = "Louter";

  static final String CENTRAL_NAVIGATOR_PACKAGE_NAME = ROOT_PACKAGE;

  static final String CENTRAL_NAVIGATOR_CLASS_NAME = "CentralNavigator";

  static final ClassName NON_NULL = ClassName.get("android.support.annotation", "NonNull");

  static final TypeName CONTEXT = ClassName.get("android.content", "Context");

  static final TypeName ACTIVITY = ClassName.get("android.app", "Activity");

  static final TypeName FRAGMENT = ClassName.get("android.app", "Fragment");

  static final TypeName SUPPORT_FRAGMENT = ClassName.get("android.support.v4.app", "Fragment");

  static final ClassName LOUTER = ClassName.get(LOUTER_PACKAGE_NAME, LOUTER_CLASS_NAME);

  static final TypeName LOUTER_BUILDER = LOUTER.nestedClass("Builder");

  static final TypeName CENTRAL_NAVIGATOR = ClassName.get(CENTRAL_NAVIGATOR_PACKAGE_NAME, "CentralNavigator");

  private Config() {}
}