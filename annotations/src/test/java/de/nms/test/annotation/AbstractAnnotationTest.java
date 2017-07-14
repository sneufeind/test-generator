package de.nms.test.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public abstract class AbstractAnnotationTest<A extends Annotation> {

	private final Class<A> annotationClass;

	protected AbstractAnnotationTest(final Class<A> annotationClass) {
		this.annotationClass = annotationClass;
	}

	protected <T> T getDefaultValue(final String methodName) throws NoSuchMethodException {
		final Method m = getAnnotationClass().getMethod(methodName);
		@SuppressWarnings("unchecked")
		final T defaultValue = (T) m.getDefaultValue();
		return defaultValue;
	}

	protected Class<A> getAnnotationClass() {
		return annotationClass;
	}

	@Test
	public void testNumberOfAnnotation() {
		// Arrange
		final Class<?>[] expectedAnnotations = { Inherited.class, Documented.class, Retention.class, Target.class };
		// Act
		final List<Annotation> annotations = Arrays.asList(getAnnotationClass().getAnnotations());
		// Assert
		assertEquals(expectedAnnotations.length, annotations.size());
	}

	@Test
	public void testAnnotatedWith_Inherited() {
		// Arrange
		final Class<? extends Annotation> annotationClass = Inherited.class;
		// Act
		final List<Annotation> annotations = Arrays.asList(getAnnotationClass().getAnnotations());
		// Assert
		assertAnnotation(annotations, annotationClass);
	}

	@Test
	public void testAnnotatedWith_Documented() {
		// Arrange
		final Class<? extends Annotation> annotationClass = Documented.class;
		// Act
		final List<Annotation> annotations = Arrays.asList(getAnnotationClass().getAnnotations());
		// Assert
		assertAnnotation(annotations, annotationClass);
	}

	@Test
	public void testAnnotatedWith_Retention() {
		// Arrange
		final Class<Retention> annotationClass = Retention.class;
		final RetentionPolicy expectedRetentionPolicy = RetentionPolicy.SOURCE;
		// Act
		final List<Annotation> annotations = Arrays.asList(getAnnotationClass().getAnnotations());
		// Assert
		final Retention annotation = annotationClass.cast(assertAnnotation(annotations, annotationClass));
		assertEquals(expectedRetentionPolicy, annotation.value());
	}

	@Test
	public void testAnnotatedWith_Target() {
		// Arrange
		final ElementType expectedTypeElement = ElementType.TYPE;
		final ElementType expectedMethodElement = ElementType.METHOD;
		final Class<Target> annotationClass = Target.class;
		// Act
		final List<Annotation> annotations = Arrays.asList(getAnnotationClass().getAnnotations());
		// Assert
		final Target annotation = annotationClass.cast(assertAnnotation(annotations, annotationClass));
		final ElementType[] elementTypes = annotation.value();
		assertEquals(2, elementTypes.length);
		assertEquals(expectedTypeElement, elementTypes[0]);
		assertEquals(expectedMethodElement, elementTypes[1]);
	}

	private static Annotation assertAnnotation(final List<? extends Annotation> annotations,
			final Class<? extends Annotation> annotationClass) {
		final List<? extends Annotation> resultSet = annotations.parallelStream()
				.filter(a -> annotationClass.isInstance(a)).collect(Collectors.toList());
		assertEquals(1, resultSet.size());
		final Annotation annotation = resultSet.get(0);
		assertTrue(annotationClass.isAssignableFrom(annotation.getClass()));
		return annotation;
	}
}
