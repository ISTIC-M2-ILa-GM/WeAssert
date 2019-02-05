package fr.istic.gm.weassert.test.utils;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.impl.ProcessBuilderFactoryImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class ProcessBuilderFactoryTest {

    private ProcessBuilderFactory processBuilderFactory;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        processBuilderFactory = new ProcessBuilderFactoryImpl();
    }

    @Test
    public void shouldThrowExceptionWhenCreateAProcessBuilderWithNullDirectory() {

        thrown.expect(WeAssertException.class);

        processBuilderFactory.create(null, "a-command");
    }

    @Test
    public void shouldThrowExceptionWhenCreateAProcessBuilderWithAWongDirectory() {

        thrown.expect(WeAssertException.class);

        processBuilderFactory.create("not-a-directory", "a-command");
    }

    @Test
    public void shouldThrowExceptionWhenCreateAProcessBuilderWithNullCommand() {

        thrown.expect(WeAssertException.class);

        processBuilderFactory.create("/tmp", null);
    }

    @Test
    public void shouldThrowExceptionWhenCreateAProcessBuilderWithEmptyCommand() {

        thrown.expect(WeAssertException.class);

        processBuilderFactory.create("/tmp", "");
    }

    @Test
    public void shouldCreateAProcessBuilder() {

        ProcessBuilderWrapper processBuilder = processBuilderFactory.create("/tmp", "ls", "test");

        assertThat(processBuilder, notNullValue());
        assertThat(processBuilder.directory().getPath(), equalTo("/tmp"));
        assertThat(processBuilder.command().get(0), equalTo("ls"));
        assertThat(processBuilder.command().get(1), equalTo("test"));
    }
}
