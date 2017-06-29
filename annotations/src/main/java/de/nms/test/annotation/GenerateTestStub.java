package de.nms.test.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface GenerateTestStub {
    String methodPrefix() default "test";

    String methodSuffix() default "";

    String classPrefix() default "";

    String classSuffix() default "Test";
}
