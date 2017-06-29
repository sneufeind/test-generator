package de.nms.test.apt.util;

import org.apache.commons.lang3.text.WordUtils;

public class StringUtil {

	public static String replaceDotsWithSlashes(final String input) {
		String result = null;
		if (input != null) {
			result = input.replaceAll("\\.", "/");
		}
		return result;
	}

	public static String capitalize(String input) {
		return WordUtils.capitalize(input);
	}

}
