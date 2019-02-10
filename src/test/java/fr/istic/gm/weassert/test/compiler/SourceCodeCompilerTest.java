package fr.istic.gm.weassert.test.compiler;

import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SourceCodeCompilerTest {

    private SourceCodeCompiler sourceCodeCompiler;

    @Mock
    private ProcessBuilderFactory mockProcessBuilderFactory;

    @Mock
    private ProcessBuilderWrapper mockProcessBuilderWrapper;

    @Mock
    private UrlClassLoaderWrapper mockUrlClassLoaderWrapper;

    @Mock
    private Process mockProcess;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {

        when(mockProcessBuilderFactory.create(any(), any())).thenReturn(mockProcessBuilderWrapper);

        sourceCodeCompiler = new SourceCodeCompilerImpl("fake", "mvn", mockProcessBuilderFactory, mockUrlClassLoaderWrapper);
    }

    @Test
    public void shouldThrownAnExceptionWhenInstanciateCompilerWithWrongPath() {

        thrown.expect(WeAssertException.class);
        new SourceCodeCompilerImpl("not_a_path", "mvn", mockProcessBuilderFactory, mockUrlClassLoaderWrapper);
    }

    @Test
    public void shouldCreateProcessBuilder() {
        verify(mockProcessBuilderFactory).create("fake", "mvn", "clean", "test", "-DskipTests");
    }

    @Test
    public void shouldCompile() throws FileNotFoundException {

        when(mockProcessBuilderWrapper.start()).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(new FileInputStream(".gitignore"));

        sourceCodeCompiler.compileAndWait();

        verify(mockProcessBuilderWrapper).start();
        verify(mockProcess).getInputStream();
        verify(mockUrlClassLoaderWrapper).refresh();
    }
}
