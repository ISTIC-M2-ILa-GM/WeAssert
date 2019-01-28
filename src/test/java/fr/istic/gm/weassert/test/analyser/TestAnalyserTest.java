package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.times;
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

        Map<String, Object> firstCall = new HashMap<>();
        firstCall.put("var1", 1);
        firstCall.put("var2", 2);
        firstCall.put("var3", 3);
        Map<String, Object> secondCall = new HashMap<>();
        firstCall.put("var1", 4);
        firstCall.put("var2", 2);
        firstCall.put("var3", 5);
        firstCall.put("var4", 6);

        when(mockLocalVariableParser.parse()).thenReturn(asList(fakeLocalVariableParsed, fakeLocalVariableParsed1));
        when(mockLocalVariableParser.getClazz()).thenReturn(getClass());
        when(mockCodeVisitor.getVariableValues()).thenReturn(firstCall, secondCall);

        List<TestAnalysed> result = testAnalyser.analyse();

        String expectedCompleteMethodName = getClass().getName() + "a-name" + "a-desc";

        verify(mockLocalVariableParser).parse();
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(\"" + expectedCompleteMethodName + " a-var\", a-var)");
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(\"" + expectedCompleteMethodName + " a-var1\", a-var1)");
        verify(mockCodeWriter).writeAndCloseFile();
        verify(mockTestRunner, times(2)).startTest(getClass());
        verify(mockCodeVisitor, times(2)).getVariableValues();
        verify(mockCodeVisitor).initVariableValues();

        Map<String, Object> variableValues = new HashMap<>();
        variableValues.put();
        List<TestAnalysed> expectedTestAnalyseds = asList(
                TestAnalysed.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableValues(variableValues).build()
        );

        assertThat(result, notNullValue());
        assertThat();
    }
}
