package fr.istic.gm.weassert;

import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.impl.TestRunListener;
import fr.istic.gm.weassert.test.impl.TestRunnerImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestRunnerApp {

    public static void main(String... args) {

        TestRunner testRunner = new TestRunnerImpl(new TestRunListener());
        List<String> paths = new ArrayList<>();
        paths.add("file:///home/malah/Code/ISTIC/Projects/WeAssert/fake/target/test-classes/");
        paths.add("file:///home/malah/Code/ISTIC/Projects/WeAssert/fake/target/classes/");
        List<String> classNames = Collections.singletonList("fr.istic.gm.weassert.fake.PersonTest");

        testRunner.startTest(paths, classNames);
    }
}
