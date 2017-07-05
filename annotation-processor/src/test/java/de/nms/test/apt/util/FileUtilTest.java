package de.nms.test.apt.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testGetRootPath() throws Exception {
		// Arrange
		URL location = FileUtilTest.class.getProtectionDomain().getCodeSource().getLocation(); // points
																								// to
																								// file://<root>/target/test-classes
		String expectedPath = Paths.get(location.toURI()).getParent().getParent().toString();
		// Act
		String rootPath = FileUtil.getRootPath();
		// Assert
		assertEquals(expectedPath, rootPath);
	}

	@Test
	public void testCreateJavaSrcFile() throws Exception {
		// Arrange
		String javaClassName = "MyTestClass";
		String rootPath = FileUtil.getRootPath();
		String expectedFilePath = rootPath + File.separator + javaClassName + ".java";
		// Act
		File file = FileUtil.createJavaSrcFile(Paths.get(rootPath), javaClassName);
		// Assert
		assertEquals(expectedFilePath, file.toString());
	}

	@Test
	public void testSwitchToTestSrcPath() throws Exception {
		// Arrange
		String rootPath = FileUtil.getRootPath();
		String sourcePath = rootPath + File.separator + FileUtil.SOURCE_PATH;
		String expectedTestPath = rootPath + File.separator + FileUtil.TEST_PATH;

		// Act
		String testPath = FileUtil.switchToTestSrcPath(sourcePath);
		// Assert
		assertEquals(expectedTestPath, testPath);
	}

	@Test
	public void testFindJavaClassPath() throws Exception {
		// Arrange
		String packageName = FileUtil.class.getPackage().getName();
		String origClassName = FileUtil.class.getSimpleName();

		String rootPath = FileUtil.getRootPath();
		Path path = Paths.get(rootPath, FileUtil.SOURCE_PATH, StringUtil.replaceDotsWithSlashes(packageName + "."));
		URI expectedSrcPath = FileUtil.createJavaSrcFile(path, origClassName).toURI();
		// Act
		URI testPath = FileUtil.findJavaClassPath(packageName, origClassName, true);
		// Assert
		assertEquals(expectedSrcPath.getPath(), testPath.getPath());
	}
}
