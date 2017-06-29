package de.nms.test.examples.impl;

import de.nms.test.annotation.GenerateNoTestStub;
import de.nms.test.examples.IClass;

@GenerateNoTestStub
public class NotTestedClass implements IClass {

    public NotTestedClass() {
        super();
    }

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
