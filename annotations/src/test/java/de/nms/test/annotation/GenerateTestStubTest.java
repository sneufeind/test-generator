package de.nms.test.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenerateTestStubTest extends AbstractAnnotationTest<GenerateTestStub> {

    public GenerateTestStubTest() {
        super(GenerateTestStub.class);
    }

    @Test
    public void testClassPrefix_default() throws Exception {
        // Arrange
        final String expectedValue = "";
        // Act
        final String value = getDefaultValue("classPrefix");
        // Assert
        assertEquals(expectedValue, value);
    }

    @Test
    public void testClassSuffix_default() throws Exception {
        // Arrange
        final String expectedValue = "Test";
        // Act
        final String value = getDefaultValue("classSuffix");
        // Assert
        assertEquals(expectedValue, value);
    }

    @Test
    public void testMethodPrefix_default() throws Exception {
        // Arrange
        final String expectedValue = "test";
        // Act
        final String value = getDefaultValue("methodPrefix");
        // Assert
        assertEquals(expectedValue, value);
    }

    @Test
    public void testMethodSuffix_default() throws Exception {
        // Arrange
        final String expectedValue = "";
        // Act
        final String value = getDefaultValue("methodSuffix");
        // Assert
        assertEquals(expectedValue, value);
    }
}
