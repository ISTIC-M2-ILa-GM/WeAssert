package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.MethodAnalysed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestAnalyserTest {

    private TestAnalyser testAnalyser;

    @Mock
    private LocalVariableParser mockLocalVariableParser;

    @Mock
    private CodeWriter mockCodeWriter;

    @Mock
    private CodeVisitor mockCodeVisitor;

    @Mock
    private TestRunner mockTestRunner;

    private LocalVariableParsed fakeLocalVariableParsed;

    private LocalVariableParsed fakeLocalVariableParsed1;

    @Before
    public void setUp() {
        testAnalyser = new TestAnalyserImpl(mockLocalVariableParser, mockCodeWriter, mockCodeVisitor, mockTestRunner);

        fakeLocalVariableParsed = LocalVariableParsed.builder()
                .desc("a-desc")
                .name("a-name")
                .localVariables(asList("a-var", "a-var1"))
                .build();

        fakeLocalVariableParsed1 = LocalVariableParsed.builder()
                .desc("a-desc1")
                .name("a-name1")
                .localVariables(asList("a-var1", "a-var2"))
                .build();
    }

    @Test
    public void shouldAnalyse() {

        when(mockLocalVariableParser.parse()).thenReturn(asList(fakeLocalVariableParsed, fakeLocalVariableParsed1));
        when(mockLocalVariableParser.getClazz()).thenReturn(getClass());

        List<MethodAnalysed> result = testAnalyser.analyse();

        String expectedCompleteMethodName = getClass().getName() + "a-name" + "a-desc";

        verify(mockLocalVariableParser).parse();
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(\"" + expectedCompleteMethodName + " a-var\", a-var)");
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(\"" + expectedCompleteMethodName + " a-var1\", a-var1)");
        verify(mockCodeWriter).writeAndCloseFile();
        verify(mockTestRunner).startTest(getClass());
    }
}
