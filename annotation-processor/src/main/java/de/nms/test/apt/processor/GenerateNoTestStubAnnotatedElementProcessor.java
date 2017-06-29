package de.nms.test.apt.processor;

import de.nms.test.annotation.GenerateNoTestStub;
import de.nms.test.apt.model.AnnotatedTestClass;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Map;

class GenerateNoTestStubAnnotatedElementProcessor extends AbstractAnnotatedElementProcessor {

    GenerateNoTestStubAnnotatedElementProcessor() {
        super(GenerateNoTestStub.class);
    }

    @Override
    protected void processClassElement(final TypeElement classElement, final Map<String, AnnotatedTestClass> classMap) {
        final String canonicalTestClassName = getCanonicalTestClassName(classElement);
        classMap.remove(canonicalTestClassName);
    }

    @Override
    protected void processMethodElement(final ExecutableElement methodElement, final TypeElement classElement, final Map<String, AnnotatedTestClass> classMap) {
        final String canonicalTestClassName = getCanonicalTestClassName(classElement);
        if (classMap.containsKey(canonicalTestClassName)) {
            final AnnotatedTestClass annotatedTestClass = classMap.get(canonicalTestClassName);
            final String methodName = getTestMethodName(methodElement);
            annotatedTestClass.removeMethod(methodName);
        }
    }

}
