package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.SourceCodeWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;


public class SourceCodeWriterTest {
    private SourceCodeWriter sourceCodeWriter;

    @Before
    public void setUp() {
        this.sourceCodeWriter = new SourceCodeWriter("/home/gautier/IdeaProjects/WeAssert/fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java");
    }

    @Test
    public void shouldConstruct() {
        assertNotNull(this.sourceCodeWriter.getClassName());
    }

    @Test
    public void insertOne() {
        String code = "System.out.println(\"Hello World !\");";
        this.sourceCodeWriter.insertOne("testAge", "()V", code);
        assertTrue(this.sourceCodeWriter.getSourceCode().contains(code));
    }

    @Test
    public void insertMany() {
    }

    @Test
    public void writeAndCloseFile() {
    }
}