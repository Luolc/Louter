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

import com.google.auto.service.AutoService;

import com.luolc.louter.Navigator;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static com.luolc.louter.compiler.LouterGenerator.brewJava;

/**
 * @author LuoLiangchen
 * @since 2017/1/10
 */
@SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
@AutoService(Processor.class)
public final class LouterProcessor extends AbstractProcessor {

  private Elements mElementUtils;

  //private Types mTypeUtils;

  private Filer mFiler;

  @Override
  public synchronized void init(final ProcessingEnvironment env) {
    super.init(env);
    mElementUtils = env.getElementUtils();
    //mTypeUtils = env.getTypeUtils();
    mFiler = env.getFiler();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_8;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return getSupportedAnnotations().stream()
        .map(Class::getCanonicalName)
        .collect(Collectors.toSet());
  }

  private Set<Class<? extends Annotation>> getSupportedAnnotations() {
    return Stream
        .<Class<? extends Annotation>>of(
            Navigator.class)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment env) {
    final Map<Element, SubNavigatorGenerator> generators = findAndParseTargets(env);
    generators.forEach((element, generator) -> {
      try {
        generator.brewJava().writeTo(mFiler);
      } catch (IOException e) {
        error(element, "Unable to write navigator for type %s: %s", element, e.getMessage());
      }
    });
    if (!generators.isEmpty()) {
      try {
        final List<SubNavigatorGenerator> orderedGenerators = new ArrayList<>(generators.values());
        Collections.sort(orderedGenerators);
        new CentralNavigatorGenerator(orderedGenerators).brewJava().writeTo(mFiler);
      } catch (IOException e) {
        error(null, "Unable to write CentralNavigator class");
      }
      try {
        brewJava().writeTo(mFiler);
      } catch (IOException e) {
        error(null, "Unable to write Louter class");
      }
    }
    return true;
  }

  private Map<Element, SubNavigatorGenerator> findAndParseTargets(final RoundEnvironment env) {
    return env.getElementsAnnotatedWith(Navigator.class).stream()
        .collect(Collectors.toMap(e -> e, this::parseNavigator));
  }

  private SubNavigatorGenerator parseNavigator(final Element element) {
    final TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
    if (enclosingElement.getKind() != ElementKind.INTERFACE) {
      error(element, "@%s holder class type must be 'interface'. (%s.%s)",
          Navigator.class.getSimpleName(), enclosingElement.getQualifiedName(),
          element.getSimpleName());
      return null;
    }
    if (element.getKind() != ElementKind.METHOD) {
      error(element, "@%s must annotates a method. (%s.%s)",
          Navigator.class.getSimpleName(), enclosingElement.getQualifiedName(),
          element.getSimpleName());
      return null;
    }
    final List<NavigationParam> params = ((ExecutableElement) element).getParameters().stream()
        .map(this::convertToNavigationParam)
        .collect(Collectors.toList());
    final Navigator annotation = element.getAnnotation(Navigator.class);
    return new SubNavigatorGenerator(annotation, params, getPackageName(enclosingElement));
  }

  private NavigationParam convertToNavigationParam(final VariableElement paramDefinition) {
    final String key = paramDefinition.getSimpleName().toString();
    final TypeName paramType = TypeName.get(paramDefinition.asType());
    final boolean required = NavigationParam.isRequiredParam(paramDefinition);
    return new NavigationParam(key, paramType, required);
  }

  private String getPackageName(final TypeElement type) {
    return mElementUtils.getPackageOf(type).getQualifiedName().toString();
  }

  private void error(final Element element, final String message, final Object... args) {
    printMessage(Diagnostic.Kind.ERROR, element, message, args);
  }

  private void printMessage(final Diagnostic.Kind kind, final Element element,
                            final String message, final Object... args) {
    final String rawMessage = args.length > 0 ? String.format(message, args) : message;
    processingEnv.getMessager().printMessage(kind, rawMessage, element);
  }
}
