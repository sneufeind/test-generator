package de.nms.test.apt.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.squareup.javapoet.JavaFile;

import de.nms.test.apt.model.AnnotatedTestClass;
import de.nms.test.apt.util.DateUtil;

@RunWith(Parameterized.class)
public class JavaTestFileBuilderTest {
	
	private static final String PACKAGE_NAME = "de.nms.test";
	private static final String SIMPLE_TESTCLASS_NAME = "MySimpleClassNameTest";
	
    private final Collection<String> methods;
	private AnnotatedTestClass anntotatedClazz;
	private JavaTestFileBuilder builder;
	
	public JavaTestFileBuilderTest(String testName, Collection<String> methods) {
		this.methods = methods;
	}

	@Before
	public void setUp(){
		this.anntotatedClazz = mock(AnnotatedTestClass.class);
		when(anntotatedClazz.getPackageName()).thenReturn(PACKAGE_NAME);
		when(anntotatedClazz.getSimpleTestClassName()).thenReturn(SIMPLE_TESTCLASS_NAME);
		when(anntotatedClazz.getMethods()).thenReturn(methods);
		this.builder = new JavaTestFileBuilder(anntotatedClazz);
	}
	
	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> getParameters(){
		return Arrays.asList(new Object[][]{ //
			{"Simple Class Stub", null}//
			,{"Single Method", createMethodList("testMethod")}//
			,{"Multi Methods", createMethodList("testMethod1", "testMethod2", "testMethod3")}//
			,{"Same Methods", createMethodList("testMethod1", "testMethod1")}//
		});
	}
	
	private static Collection<String> createMethodList(String... names){
		final Collection<String> methods = new LinkedList<>();
		if(names != null){
			for (String name : names) {
				methods.add(name);
			}
		}
		return methods;
	}
 	
	private static String createTestClassStub(Collection<String> methodNames){
		final StringBuilder sb = new StringBuilder();
		if(methodNames != null){
			boolean isFirst = true;
			for (String methodName : methodNames) {
				sb.append(isFirst ? "" : "\n").append(createTestMethodStub(methodName));
				if(isFirst){
					isFirst = false;
				}
			}
		}
		String content = sb.toString();
		return new StringBuilder() //
				.append("package de.nms.test;\n") // 
				.append("\n") // 
				.append(content.isEmpty() ? "" : "import java.lang.Exception;\n") // imported when method added
				.append("import javax.annotation.Generated;\n") //
				.append(content.isEmpty() ? "" : "import org.junit.Test;\n") // imported when method added
				.append("\n") //
				.append("@Generated(\n") //
				.append("    value = \"de.nms.test.apt.generator.TestCaseGenerator\",\n") //
				.append("    date = \"%s\"\n") //
				.append(")\n") //
				.append("public class MySimpleClassNameTest {\n") //
				.append(content) //
				.append("}\n") //
				.toString();
	}
	

	private static String createTestMethodStub(String name){
		return new StringBuilder() //
				.append("  @Test\n") // 
				.append("  public void ").append(name).append("() throws Exception {\n") //
				.append("    // Arrange\n") // 
				.append("\n") //
				.append("    // Act\n") // 
				.append("\n") //
				.append("    // Assert\n") // 
				.append("    org.junit.Assert.fail(\"'").append(name).append("' not implemented yet!\");\n") //
				.append("  }\n") //
				.toString();
	}
	
	@Test
	public void testBuild() throws Exception{
		// Arrange
		String expectedResult = createTestClassStub(methods);
		// Act
		String dateAsString = DateUtil.createCurrentDateAsString();
		JavaFile javaFile = builder.build();
		// Assert
		assertNotNull(javaFile);
		String javaFileStr = javaFile.toString();
		assertNotNull(javaFileStr);
		assertEquals(String.format(expectedResult, dateAsString), javaFileStr);
	}
}
