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

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author LuoLiangchen
 * @since 2017/1/10
 */
public final class LouterProcessor extends AbstractProcessor {

  private Elements mElementUtils;

  private Types mTypeUtils;

  private Filer mFiler;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    mElementUtils = env.getElementUtils();
    mTypeUtils = env.getTypeUtils();
    mFiler = env.getFiler();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> types = new LinkedHashSet<>();
    for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
      types.add(annotation.getCanonicalName());
    }
    return types;
  }

  private Set<Class<? extends Annotation>> getSupportedAnnotations() {
    Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

    annotations.add(Navigator.class);

    return annotations;
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {

    return false;
  }

  private void findAndParseTargets(RoundEnvironment env) {
    for (Element element : env.getElementsAnnotatedWith(Navigator.class)) {

    }
  }

  private void parseNavigator(Element element) {
    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
    if (element.getKind() != ElementKind.INTERFACE) {
      error(element, "@%s class type must be 'interface'. (%s.%s)",
          Navigator.class.getSimpleName(), enclosingElement.getQualifiedName(),
          element.getSimpleName());
      return;
    }
    element.getEnclosedElements().stream()
        .filter(e -> e.getKind() == ElementKind.METHOD)
        .map(e -> (ExecutableElement) e)
        .map(e -> {

        })


    for (Element e : element.getEnclosedElements()) {
      if (e.getKind() == ElementKind.METHOD) {
        ExecutableElement executableElement = (ExecutableElement) e;
        executableElement.getSimpleName()
      }
    }
  }

  private void error(Element element, String message, Object... args) {
    printMessage(Diagnostic.Kind.ERROR, element, message, args);
  }

  private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }

    processingEnv.getMessager().printMessage(kind, message, element);
  }
}
