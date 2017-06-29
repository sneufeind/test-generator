package de.nms.test.apt.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import de.nms.test.apt.util.StringUtil;

public class AnnotatedTestClass {

	private static final String TESTCLASS_PREFIX = "";
	private static final String TESTCLASS_SUFFIX = "Test";
	private static final String TESTMETHOD_PREFIX = "test";
	private static final String TESTMETHOD_SUFFIX = "";

	private final Collection<String> methods = new LinkedList<>();
	private final String packageName;
	private final String simpleClassName;

	public AnnotatedTestClass(final String packageName, final String simpleClassName) {
		if (packageName == null) {
			throw new IllegalArgumentException("The packagename must be set!");
		}
		if (simpleClassName == null) {
			throw new IllegalArgumentException("The classname must be set!");
		}
		this.packageName = packageName;
		this.simpleClassName = simpleClassName;
	}

	public void addMethod(final String methodName) {
		methods.add(methodName);
	}

	public void removeMethod(final String methodName) {
		methods.remove(methodName);
	}

	public Collection<String> getMethods() {
		final Collection<String> testMethods = new LinkedList<>();
		for (final String method : methods) {
			testMethods.add(getTestMethodName(method));
		}
		return Collections.unmodifiableCollection(testMethods);
	}

	public String getCanonicalTestClassName() {
		return getPackageName() + "." + getSimpleTestClassName();
	}

	public String getCanonicalOriginalClassName() {
		return getPackageName() + "." + getSimpleOriginalClassName();
	}

	public String getSimpleOriginalClassName() {
		return simpleClassName;
	}

	public String getSimpleTestClassName() {
		String result = TESTCLASS_PREFIX;
		result += simpleClassName;
		result += TESTCLASS_SUFFIX;
		return result;
	}

	private String getTestMethodName(final String methodName) {
		String result = TESTMETHOD_PREFIX;
		result += TESTMETHOD_PREFIX.isEmpty() ? methodName : StringUtil.capitalize(methodName);
		result += TESTMETHOD_SUFFIX;
		return result;
	}

	public String getPackageName() {
		return packageName;
	}
}
