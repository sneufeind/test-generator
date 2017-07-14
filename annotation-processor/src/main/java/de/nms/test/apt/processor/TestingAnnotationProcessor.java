package de.nms.test.apt.processor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import de.nms.test.annotation.GenerateNoTestStub;
import de.nms.test.annotation.GenerateTestStub;
import de.nms.test.apt.generator.TestCaseGenerator;
import de.nms.test.apt.model.AnnotatedTestClass;

public class TestingAnnotationProcessor extends AbstractProcessor {

	private static final Map<Class<?>, AnnotatedElementProcessor> ANNOTATIONS = new HashMap<>();
	static {
		ANNOTATIONS.put(GenerateTestStub.class, new GenerateTestStubAnnotatedElementProcessor());
		ANNOTATIONS.put(GenerateNoTestStub.class, new GenerateNoTestStubAnnotatedElementProcessor());
	}

	private Messager messager;
	private Elements elementUtils;

	/**
	 * parameterless Constructor is needed
	 */
	public TestingAnnotationProcessor() {
		super();
	}

	private static StandardJavaFileManager getJavaFileManager() {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final Locale locale = Locale.getDefault();
		final Charset encoding = Charset.defaultCharset();
		return compiler.getStandardFileManager(null, locale, encoding);
	}

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
		this.elementUtils = processingEnv.getElementUtils();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		printMessage(Kind.NOTE, "TestingAnnotationProcessor processing...", null); //$NON-NLS-1$
		final Map<String, AnnotatedTestClass> classMap = new HashMap<>();

		// COLLECT all methods and classes annotated with @GenerateTestStub
		ANNOTATIONS.get(GenerateTestStub.class).process(elementUtils, roundEnv, classMap);

		// REMOVE all methods and classes annotated with @GenerateNoTestStub
		ANNOTATIONS.get(GenerateNoTestStub.class).process(elementUtils, roundEnv, classMap);

		// GENERATE all non-exisiting elements annotated with @GenerateTestStub
		final StandardJavaFileManager fileManager = getJavaFileManager();
		final TestCaseGenerator generator = new TestCaseGenerator();
		for (final AnnotatedTestClass testClass : classMap.values()) {
			printMessage(Kind.NOTE, "generating testclass " + testClass.getCanonicalTestClassName(), null);
			try {
				generator.generateTestClass(fileManager, testClass);
			} catch (IOException | ClassNotFoundException | URISyntaxException e) {
				printMessage(Kind.ERROR, e.getMessage(), null);
				printMessage(Kind.ERROR, "Failed to generate testclass " + testClass.getCanonicalTestClassName(), null);
			}
		}

		try {
			fileManager.close();
		} catch (IOException e) {
			printMessage(Kind.ERROR, "Failed to close file manager!", null);
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
