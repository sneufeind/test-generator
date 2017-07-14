# test-generator
Generating Unit-Test-Stubs for Java using Annotation Processing

### Intention
The idea is to improve unit test coverage without implementing to much boilerplate code. 
The delevoper doesn't care anymore, whether to write a unit test or not. 
Now he explicitly decides against writing an unit test, if it doesn't make sense.

### Getting started with Maven
1. Add the `annotation-processor` to your `maven-compiler-plugin` in your pom.xml.
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>de.nms.test</groupId>
            <artifactId>annotation-processor</artifactId>
            <version>${latest.annotation-processor.version}</version>
          </path>
        </annotationProcessorPaths>
        <annotationProcessors>
          <proc>de.nms.test.apt.processor.TestingAnnotationProcessor</proc>
        </annotationProcessors>
      </plugin>
    </plugins>
</build>
```
2. Add the `annotations`-dependency to your project:
```xml
<dependencies>
  <dependency>
    <groupId>de.nms.test</groupId>
    <artifactId>annotations</artifactId>
  </dependency>
</dependencies>
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>de.nms.test</groupId>
      <artifactId>annotations</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```
3. Create a java class annotated with `@GenerateTestStub`. If you're looking for examples, please have closer look to [samples](https://github.com/sneufeind/test-generator/tree/master/samples).
4. There will be an unit test class automatically generate for every `public` and non-`abstract` method implemented in that class.
```
shell> mvn clean compile
```
5. To avoid generating a test for a single method you'll be able to add the `@GenerateNoTestStub` to the method itself.
