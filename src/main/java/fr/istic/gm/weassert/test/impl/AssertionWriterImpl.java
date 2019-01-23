package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.AssertionWriter;
import fr.istic.gm.weassert.test.model.Assertion;

import java.util.List;

public class AssertionWriterImpl implements AssertionWriter {
    @Override
    public void insert(String clazz, String methodName, List<Assertion> assertions) {
        // TODO: 1. look for class named 'clazz'
        // TODO: 2. look for class method named 'methodName'
        // TODO: 3. generate and insert assertions
    }

    @Override
    public String generate(Assertion assertion) {
        return String.format("assertEquals(%s,%s);", assertion.getActualValue(), assertion.getExpectedValue());
    }
}
