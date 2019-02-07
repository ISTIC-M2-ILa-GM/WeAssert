package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.model.VariableDefinition;
import fr.istic.gm.weassert.test.runner.TestRunner;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;

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
    private SourceCodeCompiler mockSourceCodeCompiler;

    @Mock
    private TestRunner mockTestRunner;

    @Mock
    private UrlClassLoaderWrapper mockUrlClassLoaderWrapper;

    private LocalVariableParsed fakeLocalVariableParsed;

    private LocalVariableParsed fakeLocalVariableParsed1;

    @Before
    public void setUp() {
        testAnalyser = new TestAnalyserImpl(mockLocalVariableParser, mockCodeWriter, mockCodeVisitor, mockSourceCodeCompiler, mockUrlClassLoaderWrapper, mockTestRunner);

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
    public void shouldCallAllNeedMethodsWhenAnalyse() {

        when(mockLocalVariableParser.parse()).thenReturn(asList(fakeLocalVariableParsed, fakeLocalVariableParsed1));
        when(mockLocalVariableParser.getClazz()).thenReturn(getClass());
        when(mockUrlClassLoaderWrapper.getClassList()).thenReturn(Collections.singletonList(getClass()));

        testAnalyser.analyse();

        verify(mockLocalVariableParser).parse();
        verify(mockLocalVariableParser).getClazz();
        verify(mockCodeWriter).insertOne("a-name", "a-desc", mockCodeVisitor.getClass().getName() + ".INSTANCE.visit(getClass(), \"a-name\", \"a-desc\", \"a-var\", a-var); // GENERATED VISITOR");
        verify(mockCodeWriter).insertOne("a-name", "a-desc", mockCodeVisitor.getClass().getName() + ".INSTANCE.visit(getClass(), \"a-name\", \"a-desc\", \"a-var1\", a-var1); // GENERATED VISITOR");
        verify(mockCodeWriter).writeAndCloseFile();
        verify(mockSourceCodeCompiler).compileAndWait();
        verify(mockUrlClassLoaderWrapper).getClassList();
        verify(mockTestRunner, times(2)).startTest(getClass());
        verify(mockCodeVisitor, times(2)).getVariableValues();
        verify(mockCodeVisitor).initVariableValues();
    }



    @Test
    public void shouldReturnAGoodResponseWhenAnalyse() {

        VariableDefinition var0 = VariableDefinition.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableName("var0").build();
        VariableDefinition var1 = VariableDefinition.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableName("var1").build();
        VariableDefinition var2 = VariableDefinition.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableName("var2").build();
        VariableDefinition var3 = VariableDefinition.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableName("var3").build();
        VariableDefinition var4 = VariableDefinition.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableName("var4").build();
        Map<VariableDefinition, Object> firstCall = new HashMap<>();
        firstCall.put(var0, 0);
        firstCall.put(var1, 1);
        firstCall.put(var2, 2);
        firstCall.put(var3, 3);
        Map<VariableDefinition, Object> secondCall = new HashMap<>();
        secondCall.put(var0, 0);
        secondCall.put(var1, 4);
        secondCall.put(var2, 2);
        secondCall.put(var3, 5);
        secondCall.put(var4, 6);

        when(mockLocalVariableParser.parse()).thenReturn(asList(fakeLocalVariableParsed, fakeLocalVariableParsed1));
        when(mockLocalVariableParser.getClazz()).thenReturn(getClass());
        when(mockCodeVisitor.getVariableValues()).thenReturn(firstCall, secondCall);
        when(mockUrlClassLoaderWrapper.getClassList()).thenReturn(Collections.singletonList(getClass()));

        List<TestAnalysed> result = testAnalyser.analyse();

        Map<String, Object> variableValues = new HashMap<>();
        variableValues.put("var0", 0);
        variableValues.put("var2", 2);
        List<TestAnalysed> expectedTestAnalyseds = Collections.singletonList(
                TestAnalysed.builder().clazz(getClass()).methodName("a-name").methodDesc("a-desc").variableValues(variableValues).build()
        );

        assertThat(result, notNullValue());
        assertThat(result, equalTo(expectedTestAnalyseds));
    }
}
