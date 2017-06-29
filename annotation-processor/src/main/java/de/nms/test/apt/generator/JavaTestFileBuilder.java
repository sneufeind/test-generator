package de.nms.test.apt.generator;

import java.util.Collection;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import org.junit.Test;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import de.nms.test.apt.model.AnnotatedTestClass;
import de.nms.test.apt.util.DateUtil;

public class JavaTestFileBuilder {

    private AnnotatedTestClass annotatedTestClass = null;

    public JavaTestFileBuilder(final AnnotatedTestClass annotatedTestClass) {
        super();
        this.annotatedTestClass = annotatedTestClass;
    }

    public JavaFile build() {
        final String packageName = annotatedTestClass.getPackageName();
        final JavaFile javaFile = JavaFile.builder(packageName, createClassSpec(annotatedTestClass)).build();
        return javaFile;
    }

    private TypeSpec createClassSpec(final AnnotatedTestClass testClass) {
        final Builder classBuilder = TypeSpec
                .classBuilder(testClass.getSimpleTestClassName())
                .addAnnotation(createGeneratedAnnotationSpec())
                .addModifiers(Modifier.PUBLIC);

        final Collection<String> methodNames = testClass.getMethods();
        if(methodNames != null){
	        for (final String methodName : methodNames) {
	            classBuilder.addMethod(createMethodSpec(methodName));
	        }
        }

        return classBuilder.build();
    }

    private MethodSpec createMethodSpec(final String methodName) {
        return MethodSpec
                .methodBuilder(methodName)
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .addCode("// Arrange\n\n")
                .addCode("// Act\n\n")
                .addCode("// Assert\n")
                .addCode(String.format("org.junit.Assert.fail(\"'%s' not implemented yet!\");\n", methodName))
                .build();
    }

    private static AnnotationSpec createGeneratedAnnotationSpec() {
        return AnnotationSpec
                .builder(Generated.class)
                .addMember("value", String.format("\"%s\"", TestCaseGenerator.class.getName()))
                .addMember("date", String.format("\"%s\"", DateUtil.createCurrentDateAsString()))
                .build();
    }
}
