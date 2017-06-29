package de.nms.test.apt.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AnnotatedTestClassTest {

	private static final String PACKAGENAME = "de.nms.test";
	private static final String SIMPLE_CLASSNAME = "MyTestClass";
	private AnnotatedTestClass atc;

	@Before
	public void setUp() {
		this.atc = new AnnotatedTestClass(PACKAGENAME, SIMPLE_CLASSNAME);
	}

	@Test(expected = IllegalArgumentException.class) // Assert
	public void testConstruct_packageNameNotSet() throws Exception {
		// Arrange
		// Act
		new AnnotatedTestClass(null, SIMPLE_CLASSNAME);
	}

	@Test(expected = IllegalArgumentException.class) // Assert
	public void testConstruct_simpleClassNameNotSet() throws Exception {
		// Arrange
		// Act
		new AnnotatedTestClass(PACKAGENAME, null);
	}

	@Test
	public void testGetPackageName() throws Exception {
		// Arrange
		String expectedValue = PACKAGENAME;
		// Act
		String packageName = atc.getPackageName();
		// Assert
		assertEquals(expectedValue, packageName);
	}

	@Test
	public void testGetCanonicalOriginalClassName() throws Exception {
		// Arrange
		String expectedValue = PACKAGENAME + "." + SIMPLE_CLASSNAME;
		// Act
		String className = atc.getCanonicalOriginalClassName();
		// Assert
		assertEquals(expectedValue, className);
	}

	@Test
	public void testGetSimpleOriginalClassName() throws Exception {
		// Arrange
		String expectedValue = SIMPLE_CLASSNAME;
		// Act
		String className = atc.getSimpleOriginalClassName();
		// Assert
		assertEquals(expectedValue, className);
	}

	@Test
	public void testGetCanonicalTestClassName() throws Exception {
		// Arrange
		String expectedValue = PACKAGENAME + "." + SIMPLE_CLASSNAME + "Test";
		// Act
		String className = atc.getCanonicalTestClassName();
		// Assert
		assertEquals(expectedValue, className);
	}

	@Test
	public void testGetSimpleTestClassName() throws Exception {
		// Arrange
		String expectedValue = SIMPLE_CLASSNAME + "Test";
		// Act
		String className = atc.getSimpleTestClassName();
		// Assert
		assertEquals(expectedValue, className);
	}

	@Test
	public void testGetMethods_noMethods() throws Exception {
		// Arrange
		// Act
		Collection<String> testMethods = atc.getMethods();
		// Assert
		assertTrue(testMethods.isEmpty());
	}

	@Test
	public void testAddMethod() throws Exception {
		// Arrange
		String methodName = "method1";
		// Act
		atc.addMethod(methodName);
		// Assert
		Collection<String> testMethods = atc.getMethods();
		assertEquals(1, testMethods.size());
		assertTrue(testMethods.contains("testMethod1"));
	}

	@Test
	public void testAddMethod_multipleMethods() throws Exception {
		// Arrange
		String methodName1 = "method1";
		String methodName2 = "method2";
		String methodName3 = "method3";
		// Act
		atc.addMethod(methodName1);
		atc.addMethod(methodName2);
		atc.addMethod(methodName3);
		// Assert
		Collection<String> testMethods = atc.getMethods();
		assertEquals(3, testMethods.size());
		assertTrue(testMethods.contains("testMethod1"));
		assertTrue(testMethods.contains("testMethod2"));
		assertTrue(testMethods.contains("testMethod3"));
	}

	@Test
	public void testRemoveMethod() throws Exception {
		// Arrange
		String methodName1 = "method1";
		String methodName2 = "method2";
		atc.addMethod(methodName1);
		atc.addMethod(methodName2);
		// Act
		atc.removeMethod(methodName1);
		// Assert
		Collection<String> testMethods = atc.getMethods();
		assertEquals(1, testMethods.size());
		assertTrue(testMethods.contains("testMethod2"));
	}
}
