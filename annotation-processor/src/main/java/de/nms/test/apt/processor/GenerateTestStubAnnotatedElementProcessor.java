package de.nms.test.apt.processor;

import de.nms.test.annotation.GenerateTestStub;
import de.nms.test.apt.model.AnnotatedTestClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;

class GenerateTestStubAnnotatedElementProcessor extends AbstractAnnotatedElementProcessor {

    GenerateTestStubAnnotatedElementProcessor() {
        super(GenerateTestStub.class);
    }

    @Override
    protected void processClassElement(final TypeElement classElement, final Map<String, AnnotatedTestClass> classMap) {
        final AnnotatedTestClass annotatedTestClass = createAnnotatedTestClass(classElement);
        final String canonicalTestClassName = annotatedTestClass.getCanonicalTestClassName();
        classMap.put(canonicalTestClassName, annotatedTestClass);

        // prepare
        final List<? extends Element> enclosedElements = classElement.getEnclosedElements();
        for (final Element enclosedElement : enclosedElements) {
            final ExecutableElement methodElement = ExecutableElement.class.cast(enclosedElement);
            if (isMethodElementForTestRelevant(methodElement)) {
                annotatedTestClass.addMethod(getTestMethodName(methodElement));
            }
        }
    }

    @Override
    protected void processMethodElement(final ExecutableElement methodElement, final TypeElement classElement, final Map<String, AnnotatedTestClass> classMap) {
        final String canonicalTestClassName = getCanonicalTestClassName(classElement);
        if (!classMap.containsKey(canonicalTestClassName)) {
            final AnnotatedTestClass newAnnotatedTestClass = createAnnotatedTestClass(classElement);
            classMap.put(newAnnotatedTestClass.getCanonicalTestClassName(), newAnnotatedTestClass);
        }
        final AnnotatedTestClass annotatedTestClass = classMap.get(canonicalTestClassName);
        final String methodName = getTestMethodName(methodElement);

        annotatedTestClass.addMethod(methodName);
    }

}
