package de.nms.test.apt.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.processing.Filer;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.squareup.javapoet.JavaFile;

import de.nms.test.apt.model.AnnotatedTestClass;
import de.nms.test.apt.util.FileUtil;

public class TestCaseGenerator {

	synchronized public static void generateTestClass(final Filer filer, final AnnotatedTestClass testClass)
			throws IOException, ClassNotFoundException {
		final String origClassName = testClass.getSimpleOriginalClassName();
		final String testClassName = testClass.getSimpleTestClassName();
		final String packageName = testClass.getPackageName();

		URI srcPath = FileUtil.findJavaClass(packageName, origClassName, true);
		if (srcPath == null) {
			final String message = String.format("Could not find java class '%s'!",
					origClassName != null ? origClassName : "null");
			throw new FileNotFoundException(message);
		}
		String testSrcPath = FileUtil.switchToTestSrcPath(srcPath.toString());

		// create directories
		final Path dirPath = Paths.get(testSrcPath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		final File javaSrcFile = FileUtil.createJavaSrcFile(dirPath, testClassName);
		if (javaSrcFile.exists()) {
			// TODO check existing methods
		} else {
			// write file in test sources path
			writeJavaTestClass(testClass, javaSrcFile);
		}
	}

	private static void writeJavaTestClass(final AnnotatedTestClass testClass, final File javaSrcFile)
			throws IOException {
		final StandardJavaFileManager fileManager = getJavaFileManager();
		final JavaFileObject javaFileObject = getJavaFileObject(fileManager, javaSrcFile);
		final Writer writer = javaFileObject.openWriter();
		final JavaFile javaFile = new JavaTestFileBuilder(testClass).build();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
		fileManager.close();
	}

	private static JavaFileObject getJavaFileObject(final StandardJavaFileManager fileManager, final File javaSrcFile) {
		final Iterable<? extends JavaFileObject> units = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(javaSrcFile));
		final JavaFileObject javaFileObject = units.iterator().next();
		return javaFileObject;
	}

	private static StandardJavaFileManager getJavaFileManager() {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final Locale locale = Locale.getDefault();
		final Charset encoding = Charset.defaultCharset();
		return compiler.getStandardFileManager(null, locale, encoding);
	}

}
