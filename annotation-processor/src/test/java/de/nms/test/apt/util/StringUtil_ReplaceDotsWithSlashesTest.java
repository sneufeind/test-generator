package de.nms.test.apt.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class StringUtil_ReplaceDotsWithSlashesTest {

	private final String input;
	private final String expectedResult;

	public StringUtil_ReplaceDotsWithSlashesTest(final String input, final String expectedResult) {
		this.input = input;
		this.expectedResult = expectedResult;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] { //
				{ null, null }//
				, { "", "" }//
				, { ".", "/" }//
				, { "de.nms.test.apt.util", "de/nms/test/apt/util" }//
				, { "/", "/" }//
				, { "\\", "\\" }//
		});
	}

	@Test
	public void testReplaceDotsWithSlashes() throws Exception {
		// Arrange
		// Act
		final String result = StringUtil.replaceDotsWithSlashes(input);
		// Assert
		assertEquals(expectedResult, result);
	}
}
