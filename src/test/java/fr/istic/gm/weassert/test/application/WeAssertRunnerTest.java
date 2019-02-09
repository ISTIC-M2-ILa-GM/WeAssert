package fr.istic.gm.weassert.test.application;

import fr.istic.gm.weassert.test.application.impl.WeAssertRunnerImpl;
import fr.istic.gm.weassert.test.runner.TestRunnerListener;
import fr.istic.gm.weassert.test.utils.BackupUtils;
import fr.istic.gm.weassert.test.utils.impl.BackupUtilsImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@Slf4j
public class WeAssertRunnerTest {

    private WeAssertRunner weAssertRunner;

    private BackupUtils backupUtils;

    private boolean testRunned;

    @Before
    public void setUp() {
        testRunned = false;
        backupUtils = new BackupUtilsImpl(getAbsolutePath("fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java"));
        weAssertRunner = new WeAssertRunnerImpl(getAbsolutePath("fake"), "mvn", new TestRunnerListener() {
            @Override
            public void testRunFinished(Result result) {
                testRunned = true;
            }
        });
    }

    @After
    public void tearDown() {
        backupUtils.restore();
    }

    @Test
    public void shouldGenerate() throws IOException {

        weAssertRunner.generate();

        List<String> lines = Files.readAllLines(new File(getAbsolutePath("fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java")).toPath());
        assertThat(lines, hasItem("    org.junit.Assert.assertEquals(p.getName(),\"name\"); // GENERATED ASSERT"));
        assertThat(lines, hasItem("org.junit.Assert.assertEquals(p.getAge(),13); // GENERATED ASSERT"));
    }

    @Test
    public void shouldRunTests() {

        weAssertRunner.runTests();

        assertThat(testRunned, equalTo(true));

    }
}
