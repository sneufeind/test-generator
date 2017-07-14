package de.nms.test.apt.processor;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import de.nms.test.apt.model.AnnotatedTestClass;

abstract class AbstractAnnotatedElementProcessor implements AnnotatedElementProcessor {

	private final Class<? extends Annotation> annotationClass;
	private Elements elementUtils = null;

	AbstractAnnotatedElementProcessor(final Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	abstract protected void processClassElement(TypeElement classElement, Map<String, AnnotatedTestClass> classMap);

	abstract protected void processMethodElement(ExecutableElement methodElement, TypeElement classElement,
			Map<String, AnnotatedTestClass> classMap);

	@Override
	public final void process(final Elements elementUtils, final RoundEnvironment roundEnv,
			final Map<String, AnnotatedTestClass> classMap) {
		this.elementUtils = elementUtils;
		final Collection<? extends Element> testedAnnotatedElements = roundEnv
				.getElementsAnnotatedWith(annotationClass);
		if (testedAnnotatedElements != null) {
			for (Element element : testedAnnotatedElements) {
				// class
				if (TypeElement.class.isInstance(element)) {
					final TypeElement classElement = TypeElement.class.cast(element);
					if (isClassElementForTestRelevant(classElement)) {
						processClassElement(classElement, classMap);
					}
				} else if (ExecutableElement.class.isInstance(element)) {
					// methods
					final ExecutableElement methodElement = ExecutableElement.class.cast(element);
					if (isMethodElementForTestRelevant(methodElement)) {
						final Element enclosingElement = methodElement.getEnclosingElement();
						if (TypeElement.class.isInstance(enclosingElement)) {
							final TypeElement classElement = TypeElement.class.cast(enclosingElement);
							if (isClassElementForTestRelevant(classElement)) {
								processMethodElement(methodElement, classElement, classMap);
							}
						}
					}
				}
			}
		}
	}

	protected boolean isClassElementForTestRelevant(final TypeElement classElement) {
		if (ElementKind.CLASS == classElement.getKind()) {
			if (classElement.getModifiers().contains(Modifier.PUBLIC)
					|| classElement.getModifiers().contains(Modifier.PROTECTED)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isMethodElementForTestRelevant(ExecutableElement methodElement) {
		if (ElementKind.METHOD == methodElement.getKind()) {
			if (methodElement.getModifiers().contains(Modifier.PUBLIC)
					&& !methodElement.getModifiers().contains(Modifier.ABSTRACT)) {
				return true;
			}
		}
		return false;
	}

	protected AnnotatedTestClass createAnnotatedTestClass(final TypeElement classElement) {
		PackageElement packageElement = elementUtils.getPackageOf(classElement);
		Name packageName = packageElement.getQualifiedName();
		return new AnnotatedTestClass(packageName.toString(), classElement.getSimpleName().toString());
	}

	protected String getCanonicalTestClassName(final TypeElement classElement) {
		return createAnnotatedTestClass(classElement).getCanonicalTestClassName();
	}

	protected String getTestMethodName(final ExecutableElement methodElement) {
		return methodElement.getSimpleName().toString();
	}
}
