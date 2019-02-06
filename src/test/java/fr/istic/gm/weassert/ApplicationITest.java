package fr.istic.gm.weassert;

import fr.istic.gm.weassert.test.AssertionGenerator;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.analyser.impl.CodeVisitorImpl;
import fr.istic.gm.weassert.test.analyser.impl.LocalVariableParserImpl;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.impl.AssertionGeneratorImpl;
import fr.istic.gm.weassert.test.impl.SourceCodeWriter;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.runner.TestRunner;
import fr.istic.gm.weassert.test.runner.TestRunnerListener;
import fr.istic.gm.weassert.test.runner.impl.TestRunnerImpl;
import fr.istic.gm.weassert.test.utils.BackupUtils;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.utils.impl.BackupUtilsImpl;
import fr.istic.gm.weassert.test.utils.impl.ClassReaderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.ProcessBuilderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static fr.istic.gm.weassert.test.utils.ClassResolverUtil.mapClassToSourcePath;
import static java.util.Arrays.asList;

@Slf4j
public class ApplicationITest {

    private ClassReaderFactory classReaderFactory;

    private TestRunner testRunner;
    private BackupUtils backupUtils;

    @Before
    public void setUp() {
        backupUtils = new BackupUtilsImpl(getAbsolutePath("fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java"));
        classReaderFactory = new ClassReaderFactoryImpl();
        testRunner = new TestRunnerImpl(new JUnitCore(), new TestRunnerListener());
    }

    @After
    public void tearDown() {
        backupUtils.restore();
    }

    @Test
    public void shouldAnalyseAFakeClass() {
        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(asList(getAbsolutePath("fake/target/classes/"), getAbsolutePath("fake/target/test-classes/")));
        urlClassLoaderWrapper.getClassList().stream().filter(c -> c.getName().endsWith("Test")).forEach(c -> {
            LocalVariableParser localVariableParser = new LocalVariableParserImpl(classReaderFactory, c, new ClassNode());
            SourceCodeCompiler codeCompiler = new SourceCodeCompilerImpl("fake", "/usr/bin/mvn", new ProcessBuilderFactoryImpl(), urlClassLoaderWrapper);
            String sourcePath = mapClassToSourcePath(c);
            BackupUtils backupUtils = new BackupUtilsImpl(sourcePath);
            TestAnalyser testAnalyser = new TestAnalyserImpl(localVariableParser, new SourceCodeWriter(sourcePath), CodeVisitorImpl.INSTANCE, codeCompiler, urlClassLoaderWrapper, testRunner);
            List<TestAnalysed> testAnalyseds = testAnalyser.analyse();
            backupUtils.restore();
            AssertionGenerator assertionGenerator = new AssertionGeneratorImpl(new SourceCodeWriter(sourcePath));
            assertionGenerator.generate(testAnalyseds);
        });
    }
}
