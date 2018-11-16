package fr.istic.gm.weassert;

import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.impl.TestRunListener;
import fr.istic.gm.weassert.test.impl.TestRunnerImpl;
import fr.istic.gm.weassert.test.impl.UrlClassLoaderWrapperImpl;
import org.junit.runner.JUnitCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestRunnerApp {

    public static void main(String... args) {

        TestRunner testRunner = new TestRunnerImpl(new JUnitCore(), new TestRunListener());
        List<String> paths = new ArrayList<>();
        paths.add("file:///home/malah/Code/ISTIC/Projects/WeAssert/fake/target/test-classes/");
        paths.add("file:///home/malah/Code/ISTIC/Projects/WeAssert/fake/target/classes/");
        List<String> classNames = Collections.singletonList("fr.istic.gm.weassert.fake.PersonTest");

        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(paths, classNames);

        testRunner.startTests(urlClassLoaderWrapper.getClassList());
    }
}
