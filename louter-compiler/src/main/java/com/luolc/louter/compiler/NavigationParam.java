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

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

/**
 * @author LuoLiangchen
 * @since 2017/1/11
 */
final class NavigationParam {

  private final String mKey;

  private final boolean mRequired;

  private final TypeName mParamType;

  NavigationParam(final ExecutableElement executableElement) {
    final VariableElement paramElement = executableElement.getParameters().get(0);
    mKey = executableElement.getSimpleName().toString();
    mParamType = TypeName.get(paramElement.asType());
    mRequired = isRequiredParam(paramElement);
  }

  public String getKey() {
    return mKey;
  }

  public boolean isRequired() {
    return mRequired;
  }

  public TypeName getParamType() {
    return mParamType;
  }

  private static boolean isRequiredParam(final Element element) {
    return !hasAnnotationWithName(element, "Nullable");
  }

  private static boolean hasAnnotationWithName(final Element element, final String simpleName) {
    return element.getAnnotationMirrors().stream()
        .map(AnnotationMirror::getAnnotationType)
        .map(DeclaredType::asElement)
        .map(Element::getSimpleName)
        .map(Name::toString)
        .anyMatch(simpleName::equals);
  }
}
