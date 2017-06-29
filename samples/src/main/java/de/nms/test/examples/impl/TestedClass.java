package de.nms.test.examples.impl;

import de.nms.test.annotation.GenerateNoTestStub;
import de.nms.test.annotation.GenerateTestStub;
import de.nms.test.examples.IClass;

@GenerateTestStub
public class TestedClass implements IClass {

    public TestedClass() {
        super();
    }

    @Override
    public void doSomething() {
    }

    @Override
    public void doSomethingElse() {
    }

    @GenerateNoTestStub
    @Override
    public void doNothing() {
    }
}
