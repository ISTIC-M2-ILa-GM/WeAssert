package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.model.Assertion;

import java.util.List;

public interface AssertionWriter {
    void insert(String clazz, String methodName, List<Assertion> assertions);

    String generate(Assertion assertion);
}
