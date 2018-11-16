package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.TestRunner;
import lombok.extern.java.Log;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import java.util.List;

@Log
public class TestRunnerImpl implements TestRunner {

    private JUnitCore jUnitCore;

    public TestRunnerImpl(JUnitCore jUnitCore, RunListener runListener) {
        this.jUnitCore = jUnitCore;
        this.jUnitCore.addListener(runListener);
    }

    @Override
    public void startTests(List<Class<?>> classes) {
        classes.forEach(this::startTest);
    }

    @Override
    public void startTest(Class<?> clazz) {
        jUnitCore.run(clazz);
    }
}
