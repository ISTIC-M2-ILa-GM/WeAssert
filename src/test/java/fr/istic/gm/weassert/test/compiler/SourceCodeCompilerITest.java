package fr.istic.gm.weassert.test.compiler;

import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.utils.impl.ProcessBuilderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SourceCodeCompilerITest {

    private SourceCodeCompiler sourceCodeCompiler;

    private ProcessBuilderFactory processBuilderFactory;

    private UrlClassLoaderWrapper urlClassLoaderWrapper;

    @Before
    public void setUp() {
        processBuilderFactory = new ProcessBuilderFactoryImpl();
        urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(new ArrayList<>());
        sourceCodeCompiler = new SourceCodeCompilerImpl("fake", "mvn", processBuilderFactory, urlClassLoaderWrapper);
    }

    @Test
    public void shouldCompile() throws InterruptedException {

        processBuilderFactory.create("fake", "rm", "-rf", "target/classes").start().waitFor();

        assertThat(Files.exists(Paths.get("fake/target/classes")), equalTo(false));

        sourceCodeCompiler.compileAndWait();

        assertThat(Files.exists(Paths.get("fake/target/classes")), equalTo(true));
    }
}
