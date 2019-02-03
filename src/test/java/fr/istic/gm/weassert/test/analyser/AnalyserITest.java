package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.analyser.impl.CodeVisitorImpl;
import fr.istic.gm.weassert.test.analyser.impl.LocalVariableParserImpl;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.impl.CodeWriterImpl;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.runner.TestRunner;
import fr.istic.gm.weassert.test.runner.TestRunnerListener;
import fr.istic.gm.weassert.test.runner.impl.TestRunnerImpl;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.utils.impl.ClassReaderFactoryImpl;
import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static java.util.Arrays.asList;

@Slf4j
public class AnalyserITest {

    private ClassReaderFactory classReaderFactory;

    private TestRunner testRunner;

    @Before
    public void setUp() {

        classReaderFactory = new ClassReaderFactoryImpl();
        testRunner = new TestRunnerImpl(new JUnitCore(), new TestRunnerListener());
    }

    @Test
    public void shouldAnalyseAFakeClass() {
        List<TestAnalysed> testAnalyseds = new ArrayList<>();
        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(asList(getAbsolutePath("fake/target/classes/"), getAbsolutePath("fake/target/test-classes/")));
        urlClassLoaderWrapper.getClassList().forEach(c -> {
            LocalVariableParser localVariableParser = new LocalVariableParserImpl(classReaderFactory, c, new ClassNode());
            CodeWriter codeWriter = new CodeWriterImpl(c);
            TestAnalyser testAnalyser = new TestAnalyserImpl(localVariableParser, codeWriter, CodeVisitorImpl.INSTANCE, testRunner);
            testAnalyseds.addAll(testAnalyser.analyse());
        });
        testAnalyseds.forEach(t -> log.info("TestAnalysed:" + t));
    }
}
