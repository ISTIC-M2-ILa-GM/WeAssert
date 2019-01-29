package fr.istic.gm.weassert.test.runner.impl;

import fr.istic.gm.weassert.test.runner.TestRunner;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import java.util.List;

@Slf4j
public class TestRunnerImpl implements TestRunner {

    private JUnitCore jUnitCore;

    public TestRunnerImpl(JUnitCore jUnitCore, RunListener runListener) {
        this.jUnitCore = jUnitCore;
        this.jUnitCore.addListener(runListener);
    }

    @Override
    public void startTests(List<Class<?>> classes) {
        classes.forEach(this::startTest);
        log.info("TESTS RUNNED: " + classes);
    }

    @Override
    public void startTest(Class<?> clazz) {
        jUnitCore.run(clazz);
        log.info("TESTS RUNNED: " + clazz);
    }
}
