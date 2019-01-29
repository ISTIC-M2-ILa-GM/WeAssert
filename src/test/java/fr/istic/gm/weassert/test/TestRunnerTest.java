package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.TestRunnerApp;
import fr.istic.gm.weassert.test.runner.impl.TestRunnerImpl;
import fr.istic.gm.weassert.test.runner.TestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestRunnerTest {

    private TestRunner testRunner;

    @Mock
    private JUnitCore mockJUnitCore;

    @Mock
    private RunListener mockRunListener;

    @Before
    public void setUp() {
        testRunner = new TestRunnerImpl(mockJUnitCore, mockRunListener);
    }

    @Test
    public void shouldAStartTestClass() {

        testRunner.startTest(getClass());

        verify(mockJUnitCore).addListener(mockRunListener);
        verify(mockJUnitCore).run(getClass());
    }

    @Test
    public void shouldAStartTestsClass() {

        testRunner.startTests(Arrays.asList(getClass(), TestRunnerApp.class));

        verify(mockJUnitCore).addListener(mockRunListener);
        verify(mockJUnitCore).run(getClass());
        verify(mockJUnitCore).run(TestRunnerApp.class);
    }
}
