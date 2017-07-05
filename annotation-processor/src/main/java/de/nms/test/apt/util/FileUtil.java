package de.nms.test.apt.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtil {

	static final String SOURCE_PATH = "/src/main/java/";
	static final String TEST_PATH = "/src/test/java/";

	public static String getRootPath() {
		String rootPath = Paths.get(new File(".").getAbsolutePath()).getParent().toFile().getAbsolutePath();
		return rootPath;
	}

	public static File createJavaSrcFile(final Path dirPath, final String javaClassName) {
		return Paths.get(dirPath.toString(), String.format("%s.java", javaClassName)).toFile();
	}

	public static String switchToTestSrcPath(String sourcePath) {
		String testPath = sourcePath.replace(SOURCE_PATH, TEST_PATH);
		return testPath;
	}

	public static URI findJavaClassPath(String packageName, String origClassName, boolean isInSrcPath)
			throws IOException {
		final URI[] helper = new URI[1];

		String target = StringUtil.replaceDotsWithSlashes(packageName + ".") + origClassName + ".java";
		String searchPath = getRootPath() + (isInSrcPath ? SOURCE_PATH : TEST_PATH);
		Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				final URI absolutePath = file.toUri();
				if (absolutePath.toString().contains(target)) {
					helper[0] = absolutePath;
					return FileVisitResult.TERMINATE;
				} else {
					return super.visitFile(file, attrs);
				}
			}
		});
		return helper[0];
	}
}
