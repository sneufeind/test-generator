package de.nms.test.apt.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.squareup.javapoet.JavaFile;

import de.nms.test.apt.model.AnnotatedTestClass;
import de.nms.test.apt.util.FileUtil;

public class TestCaseGenerator {

	synchronized public void generateTestClass(final StandardJavaFileManager fileManager, final Filer filer,
			final AnnotatedTestClass testClass) throws IOException, ClassNotFoundException, URISyntaxException {
		final String origClassName = testClass.getSimpleOriginalClassName();
		final String testClassName = testClass.getSimpleTestClassName();
		final String packageName = testClass.getPackageName();

		URI srcPath = FileUtil.findJavaClassPath(packageName, origClassName);
		if (srcPath == null) {
			final String message = String.format("Could not find java class '%s'!",
					origClassName != null ? origClassName : "null");
			throw new FileNotFoundException(message);
		}
		String testSrcPath = FileUtil.switchToTestSrcPath(srcPath.toString());

		// create directories
		final Path dirPath = Paths.get(new URI(testSrcPath)).getParent();
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		final File javaSrcFile = FileUtil.createJavaSrcFile(dirPath, testClassName);
		if (javaSrcFile.exists()) {
			// TODO check existing methods
		} else {
			// write file in test sources path
			writeJavaTestClass(fileManager, testClass, javaSrcFile);
		}
	}

	private void writeJavaTestClass(final StandardJavaFileManager fileManager, final AnnotatedTestClass testClass,
			final File javaSrcFile) throws IOException {
		final JavaFileObject javaFileObject = getJavaFileObject(fileManager, javaSrcFile);
		final Writer writer = javaFileObject.openWriter();
		final JavaFile javaFile = new JavaTestFileBuilder(testClass).build();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}

	protected JavaFileObject getJavaFileObject(final StandardJavaFileManager fileManager, final File javaSrcFile) {
		final Iterable<? extends JavaFileObject> units = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(javaSrcFile));
		final Iterator<? extends JavaFileObject> iterator = units.iterator();
		final JavaFileObject javaFileObject = iterator.next();
		return javaFileObject;
	}
}
