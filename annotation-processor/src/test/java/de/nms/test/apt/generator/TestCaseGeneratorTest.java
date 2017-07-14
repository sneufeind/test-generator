package de.nms.test.apt.generator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.nms.test.apt.model.AnnotatedTestClass;
import de.nms.test.apt.util.FileUtil;

public class TestCaseGeneratorTest {

	private static final String PACKAGE_NAME = TestCaseGenerator.class.getPackage().getName();
	private static final String SIMPLE_NAME = TestCaseGenerator.class.getSimpleName();
	private static final String TESTCLASS_NAME = "MyTestClass";

	StandardJavaFileManager fileManager;
	AnnotatedTestClass testClass;

	@Before
	public void setUp() {
		this.testClass = new AnnotatedTestClass(PACKAGE_NAME, SIMPLE_NAME) {
			@Override
			public String getSimpleTestClassName() {
				return TESTCLASS_NAME;
			}
		};
		this.fileManager = mock(StandardJavaFileManager.class, Mockito.CALLS_REAL_METHODS);
	}

	@Test
	public void testGenerateTestClass() throws Exception {
		// Arrange
		String expectedTestSrcFile = Paths.get(getTestSrcPath(PACKAGE_NAME, SIMPLE_NAME), TESTCLASS_NAME + ".java")
				.toFile().getAbsolutePath();
		SimpleJavaFileObject javaFileObject = mock(SimpleJavaFileObject.class);
		FileWriter writer = mock(FileWriter.class);
		when(javaFileObject.openWriter()).thenReturn(writer);
		InternalTestCaseGenerator generator = new InternalTestCaseGenerator(javaFileObject);
		// Act
		generator.generateTestClass(this.fileManager, this.testClass);
		// Assert
		assertEquals(this.fileManager, generator.fileManager);
		assertEquals(expectedTestSrcFile, generator.javaSrcFile.getAbsolutePath());
	}

	private String getTestSrcPath(String packageName, String simpleName) throws IOException, URISyntaxException {
		URI javaClassPath = FileUtil.findJavaClassPath(packageName, simpleName);
		String testSrcFile = FileUtil.switchToTestSrcPath(Paths.get(javaClassPath).getParent().toUri().toString());
		return new File(new URI(testSrcFile)).getAbsolutePath();
	}

	private class InternalTestCaseGenerator extends TestCaseGenerator {

		private final JavaFileObject javaFileObject;
		private StandardJavaFileManager fileManager;
		private File javaSrcFile;

		InternalTestCaseGenerator(final JavaFileObject javaFileObject) {
			this.javaFileObject = javaFileObject;
		}

		@Override
		protected JavaFileObject getJavaFileObject(StandardJavaFileManager fileManager, File javaSrcFile) {
			this.fileManager = fileManager;
			this.javaSrcFile = javaSrcFile;
			return this.javaFileObject;
		}
	}
}
