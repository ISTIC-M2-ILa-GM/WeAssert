package fr.istic.gm.weassert.test.compiler;

import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.impl.ProcessBuilderFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SourceCodeCompilerITest {

    private SourceCodeCompiler sourceCodeCompiler;

    private ProcessBuilderFactory processBuilderFactory;

    @Before
    public void setUp() {
        processBuilderFactory = new ProcessBuilderFactoryImpl();
        sourceCodeCompiler = new SourceCodeCompilerImpl("fake", "mvn", processBuilderFactory);
    }

    @Test
    public void shouldCompile() throws InterruptedException {

        processBuilderFactory.create("fake", "rm", "-rf", "target/").start().waitFor();

        assertThat(Files.exists(Paths.get("fake/target")), equalTo(false));

        sourceCodeCompiler.compile().waitFor();

        assertThat(Files.exists(Paths.get("fake/target")), equalTo(true));
    }
}
