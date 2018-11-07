package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.TestRunner;
import lombok.extern.java.Log;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Log
public class TestRunnerImpl implements TestRunner {

    private static final String LOAD_CLASS_ERROR = "Can't load class: %s";
    private static final String PARSED_ERROR = "Url can't be parsed: %s";

    @Override
    public void startTest(List<String> paths, List<String> classNames) {
        URL[] urls = paths.stream().map(this::mapToUrl).toArray(URL[]::new);
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
        classNames.forEach(c -> startTest(c, urlClassLoader));
    }

    private void startTest(String className, URLClassLoader urlClassLoader) {
        try {
            Class<?> clazz = urlClassLoader.loadClass(className);
            JUnitCore core = new JUnitCore();
            Result result = core.run(clazz);
            log.info("FINISHED");
            log.info(String.format("| IGNORED: %d", result.getIgnoreCount()));
            log.info(String.format("| FAILURES: %d", result.getFailureCount()));
            log.info(String.format("| RUN: %d", result.getRunCount()));
        } catch (ClassNotFoundException e) {
            String message = String.format(LOAD_CLASS_ERROR, e.getCause());
            log.info(message);
            throw new RuntimeException(message, e);
        }
    }


    private URL mapToUrl(String a) {
        try {
            return new URL(a);
        } catch (MalformedURLException e) {
            String message = String.format(PARSED_ERROR, e.getCause());
            log.info(message);
            throw new RuntimeException(message, e);
        }
    }
}
