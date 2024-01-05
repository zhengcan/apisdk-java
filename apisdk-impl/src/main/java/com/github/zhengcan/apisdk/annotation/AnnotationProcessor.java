package com.github.zhengcan.apisdk.annotation;

import com.github.zhengcan.apisdk.ApiService;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

public class AnnotationProcessor extends javax.annotation.processing.AbstractProcessor {
  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_11;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(ApiService.class.getName());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("annotations: %s", annotations));
    Set<? extends Element> apiServiceElements = roundEnv.getElementsAnnotatedWith(ApiService.class);
    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("elements: %s", apiServiceElements));
    return false;
  }
}
