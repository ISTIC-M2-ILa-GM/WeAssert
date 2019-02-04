package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.analyser.impl.LocalVariableParserImpl;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocalVariableParserTest {

    private LocalVariableParser localVariableParser;

    @Mock
    private ClassReaderFactory mockClassReaderFactory;

    @Before
    public void setUp() throws Exception {
        when(mockClassReaderFactory.create(any())).thenReturn(new ClassReader(TestAnalyserTest.class.getName()));

        localVariableParser = new LocalVariableParserImpl(mockClassReaderFactory, TestAnalyserTest.class, new ClassNode());
    }

    @Test
    public void shouldInstanceClassReader() {

        verify(mockClassReaderFactory).create(TestAnalyserTest.class);
    }

    @Test
    public void shouldParseClassReader() {

        List<LocalVariableParsed> result = localVariableParser.parse();

        assertThat(result, notNullValue());
        assertThat(result, hasSize(3));
        assertThat(result.get(0).getName(), equalTo("setUp"));
        assertThat(result.get(1).getName(), equalTo("shouldCallAllNeedMethodsWhenAnalyse"));
        assertThat(result.get(2).getName(), equalTo("shouldReturnAGoodResponseWhenAnalyse"));
        assertThat(result.get(2).getLocalVariables(), hasItem("var0.getMethodName()"));
    }
}
