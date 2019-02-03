package fr.istic.gm.weassert.test.compiler;

import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {

        when(mockProcessBuilderFactory.create(any(), any())).thenReturn(mockProcessBuilderWrapper);

        sourceCodeCompiler = new SourceCodeCompilerImpl("fake", "mvn", mockProcessBuilderFactory);
    }

    @Test
    public void shouldThrownAnExceptionWhenInstanciateCompilerWithWrongPath() {

        thrown.expect(WeAssertException.class);
        new SourceCodeCompilerImpl("not_a_path", "mvn", mockProcessBuilderFactory);
    }

    @Test
    public void shouldCreateProcessBuilder() {
        verify(mockProcessBuilderFactory).create("fake", "mvn", "compile");
    }

    @Test
    public void shouldCompile() {

        sourceCodeCompiler.compile();

        verify(mockProcessBuilderWrapper).start();
    }
}
