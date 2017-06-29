package de.nms.test.apt.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import de.nms.test.annotation.GenerateNoTestStub;
import de.nms.test.annotation.GenerateTestStub;
import de.nms.test.apt.generator.TestCaseGenerator;
import de.nms.test.apt.model.AnnotatedTestClass;

public class TestingAnnotationProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;
	private Elements elementUtils;
	private Types typeUtils;

	private static final Map<Class<?>, AnnotatedElementProcessor> ANNOTATIONS = new HashMap<>();

	static {
		ANNOTATIONS.put(GenerateTestStub.class, new GenerateTestStubAnnotatedElementProcessor());
		ANNOTATIONS.put(GenerateNoTestStub.class, new GenerateNoTestStubAnnotatedElementProcessor());
	}

	/**
	 * parameterless Constructor is needed
	 */
	public TestingAnnotationProcessor() {
		super();
	}

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.filer = processingEnv.getFiler();
		this.messager = processingEnv.getMessager();
		this.elementUtils = processingEnv.getElementUtils();
		this.typeUtils = processingEnv.getTypeUtils();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		printMessage(Kind.NOTE, "TestingAnnotationProcessor processing...", null); //$NON-NLS-1$
		final Map<String, AnnotatedTestClass> classMap = new HashMap<>();

		// collect all methods and classes annotated with @GenerateTestStub
		ANNOTATIONS.get(GenerateTestStub.class).process(elementUtils, roundEnv, classMap);

		// remove all methods and classes annotated with @GenerateNoTestStub
		ANNOTATIONS.get(GenerateNoTestStub.class).process(elementUtils, roundEnv, classMap);

		// generate all non-exisiting elements annotated with @GenerateTestStub
		for (final AnnotatedTestClass testClass : classMap.values()) {
			printMessage(Kind.NOTE, "generating testclass " + testClass.getCanonicalTestClassName(), null);
			try {
				TestCaseGenerator.generateTestClass(filer, testClass);
			} catch (IOException | ClassNotFoundException e) {
				printMessage(Kind.ERROR, "Failed to generate testclass " + testClass.getCanonicalTestClassName(), null);
			}
		}

		return true;
	}

	private void printMessage(final Kind messageType, final String message, final Element element) {
		if (element == null) {
			messager.printMessage(messageType, message);
		} else {
			messager.printMessage(messageType, message, element);
		}
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		final Set<String> annotations = new LinkedHashSet<>();
		for (final Class<?> clazz : ANNOTATIONS.keySet()) {
			annotations.add(clazz.getCanonicalName());
		}
		return annotations;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}
