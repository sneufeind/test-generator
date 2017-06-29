package de.nms.test.apt.processor;

import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;

import de.nms.test.apt.model.AnnotatedTestClass;

interface AnnotatedElementProcessor {

    void process(Elements elementUtils, RoundEnvironment roundEnv, Map<String, AnnotatedTestClass> classMap);

}
