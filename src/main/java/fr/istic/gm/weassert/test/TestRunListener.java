package fr.istic.gm.weassert.test;

import lombok.extern.java.Log;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

@Log
public class TestRunListener extends RunListener {
    @Override
    public void testRunStarted(Description description) throws Exception {
        log.info("RUN STARTED");
    }

    @Override
    public void testStarted(Description description) throws Exception {
        log.info("TEST STARTED: " +  description.getDisplayName());
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        log.info("FAILURE: " + failure.getMessage());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        log.info("TEST IGNORED: " + description.getDisplayName());
    }

    @Override
    public void testFinished(Description description) throws Exception {
        log.info("TEST FINISHED: " + description.getDisplayName());
    }


    @Override
    public void testRunFinished(Result result) throws Exception {
        log.info("RUN FINISHED");
        log.info(String.format("| IGNORED: %d", result.getIgnoreCount()));
        log.info(String.format("| FAILURES: %d", result.getFailureCount()));
        log.info(String.format("| RUN: %d", result.getRunCount()));
    }
}
