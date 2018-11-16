package fr.istic.gm.weassert.test;

import java.util.List;

/**
 * The test runner
 */
public interface TestRunner {

    /**
     * Start all tests from the classes
     *
     * @param classes the classes to start test
     */
    void startTests(List<Class<?>> classes);

    /**
     * Start all tests from the class
     *
     * @param clazz the class to start test
     */
    void startTest(Class<?> clazz);
}
