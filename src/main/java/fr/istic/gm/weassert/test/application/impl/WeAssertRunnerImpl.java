package fr.istic.gm.weassert.test.application.impl;

import fr.istic.gm.weassert.test.AssertionGenerator;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.analyser.impl.CodeVisitorImpl;
import fr.istic.gm.weassert.test.analyser.impl.LocalVariableParserImpl;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.application.WeAssertRunner;
import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.compiler.impl.SourceCodeCompilerImpl;
import fr.istic.gm.weassert.test.impl.AssertionGeneratorImpl;
import fr.istic.gm.weassert.test.impl.SourceCodeWriter;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.runner.TestRunner;
import fr.istic.gm.weassert.test.runner.TestRunnerListener;
import fr.istic.gm.weassert.test.runner.impl.TestRunnerImpl;
import fr.istic.gm.weassert.test.utils.BackupUtils;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.utils.impl.BackupUtilsImpl;
import fr.istic.gm.weassert.test.utils.impl.ClassReaderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.ProcessBuilderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import org.junit.runner.JUnitCore;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.stream.Collectors;

import static fr.istic.gm.weassert.test.utils.ClassResolverUtil.mapClassToSourcePath;
import static java.util.Arrays.asList;

public class WeAssertRunnerImpl implements WeAssertRunner {

    private static final String TEST_END_FILENAME = "Test";
    private static final List<String> DEFAULT_MAVEN_FILE_PATH = asList("%s/target/classes/", "%s/target/test-classes/");

    private String mavenProjectPath;

    private String mavenCommand;

    private TestRunnerListener testRunnerListener;

    public WeAssertRunnerImpl(String mavenProjectPath, String mavenCommand, TestRunnerListener testRunnerListener) {
        this.mavenProjectPath = mavenProjectPath;
        this.mavenCommand = mavenCommand;
        this.testRunnerListener = testRunnerListener;
    }

    @Override
    public void generate() {
        List<String> filePaths = getMavenFilePath();
        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(filePaths);
        urlClassLoaderWrapper.getClassList().stream().filter(c -> c.getName().endsWith(TEST_END_FILENAME)).forEach(c -> {
            LocalVariableParser localVariableParser = new LocalVariableParserImpl(new ClassReaderFactoryImpl(), c, new ClassNode(), urlClassLoaderWrapper);
            SourceCodeCompiler codeCompiler = new SourceCodeCompilerImpl(mavenProjectPath, mavenCommand, new ProcessBuilderFactoryImpl(), urlClassLoaderWrapper);
            String sourcePath = mapClassToSourcePath(c);
            BackupUtils backupUtils = new BackupUtilsImpl(sourcePath);
            TestAnalyser testAnalyser = new TestAnalyserImpl(localVariableParser, new SourceCodeWriter(sourcePath), CodeVisitorImpl.INSTANCE, codeCompiler, urlClassLoaderWrapper, new TestRunnerImpl(new JUnitCore(), testRunnerListener));
            List<TestAnalysed> testAnalyseds = testAnalyser.analyse();
            backupUtils.restore();
            AssertionGenerator assertionGenerator = new AssertionGeneratorImpl(new SourceCodeWriter(sourcePath));
            assertionGenerator.generate(testAnalyseds);
        });
    }

    @Override
    public void runTests() {
        List<String> filePaths = getMavenFilePath();
        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(filePaths);
        urlClassLoaderWrapper.getClassList().stream().filter(c -> c.getName().endsWith(TEST_END_FILENAME)).forEach(c -> {
            TestRunner testRunner = new TestRunnerImpl(new JUnitCore(), testRunnerListener);
            testRunner.startTest(c);
        });
    }

    private List<String> getMavenFilePath() {
        return DEFAULT_MAVEN_FILE_PATH.stream().map(s -> String.format(s, mavenProjectPath)).collect(Collectors.toList());
    }
}
