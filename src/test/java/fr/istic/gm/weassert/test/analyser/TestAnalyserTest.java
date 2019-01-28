package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.analyser.impl.TestAnalyserImpl;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.MethodAnalyser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
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

    private LocalVariableParsed fakeLocalVariableParsed;

    @Before
    public void setUp() {
        testAnalyser = new TestAnalyserImpl(mockLocalVariableParser, mockCodeWriter, mockCodeVisitor, getClass());

        fakeLocalVariableParsed = LocalVariableParsed.builder()
                .desc("a-desc")
                .name("a-name")
                .localVariables(asList("a-var", "a-var1"))
                .build();
    }

    @Test
    public void shouldAnalyse() {

        when(mockLocalVariableParser.parse()).thenReturn(Collections.singletonList(fakeLocalVariableParsed));

        List<MethodAnalyser> result = testAnalyser.analyse();

        verify(mockLocalVariableParser).parse();
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(a-var)");
        verify(mockCodeWriter).insertOne("a-name", "a-desc", "CodeVisitor.INSTANCE.visit(a-var1)");
        verify(mockCodeWriter)
    }
}
