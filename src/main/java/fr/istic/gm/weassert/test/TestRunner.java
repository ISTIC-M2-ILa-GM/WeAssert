package fr.istic.gm.weassert.test;

import java.util.List;

public interface TestRunner {
    void startTests(List<Class<?>> classes);
    void startTest(Class<?> classes);
}
