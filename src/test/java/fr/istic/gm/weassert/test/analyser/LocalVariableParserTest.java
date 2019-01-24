package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.analyser.impl.LocalVariableParserImpl;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

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
        when(mockClassReaderFactory.create(any())).thenReturn(new ClassReader(getClass().getName()));

        localVariableParser = new LocalVariableParserImpl(mockClassReaderFactory, getClass(), new ClassNode());
    }

    @Test
    public void shouldInstanceClassReader() {

        verify(mockClassReaderFactory).create(getClass());
    }

    @Test
    public void shouldParseClassReader() {

        Map<String, List<String>> result = localVariableParser.parse();

        assertThat(result, notNullValue());
        assertThat(result.keySet(), hasSize(3));
        assertThat(result.keySet(), hasItem("setUp"));
        assertThat(result.keySet(), hasItem("shouldInstanceClassReader"));
        assertThat(result.keySet(), hasItem("shouldParseClassReader"));
        assertThat(result.get("shouldParseClassReader"), hasItem("result"));
    }
}
