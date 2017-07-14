package de.nms.test.annotation;

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractAnnotationTest<A extends Annotation> {

    private final Class<A> annotationClass;

    protected AbstractAnnotationTest(final Class<A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    protected <T> T getDefaultValue(final String methodName) throws NoSuchMethodException {
        final Method m = getAnnotationClass().getMethod(methodName);
        final T defaultValue = (T) m.getDefaultValue();
        return defaultValue;
    }

    protected Class<A> getAnnotationClass() {
        return annotationClass;
    }

    @Test
    public void testNumberOfAnnotation() {
        // Arrange
        final Class<?>[] expectedAnnotations = {Inherited.class, Documented.class, Retention.class, Target.class};
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

    private static <A extends Annotation> Annotation assertAnnotation(final List<? extends Annotation> annotations,
                                                                      final Class<A> annotationClass) {
        A result = null;
        if (annotations != null) {
            for (final Annotation annotation : annotations) {
                if (annotationClass.isInstance(annotation)) {
                    result = annotationClass.cast(annotation);
                    break;
                }
            }
        }
        assertNotNull(annotationClass);
        return result;
    }
}
