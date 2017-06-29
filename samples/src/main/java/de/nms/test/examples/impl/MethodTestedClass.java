package de.nms.test.examples.impl;

import de.nms.test.annotation.GenerateTestStub;
import de.nms.test.examples.IClass;

public class MethodTestedClass implements IClass {

    @GenerateTestStub
    @Override
    public void doSomething() {
    }

    @Override
    public void doSomethingElse() {
    }

    @Override
    public void doNothing() {
    }
}
