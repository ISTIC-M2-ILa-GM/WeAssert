package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.CodeWriterImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodeWriterTest {
    private CodeWriterImpl codeWriter;

    @Before
    public void setUp() {
        this.codeWriter = new CodeWriterImpl(getClass());

        assertEquals(this.codeWriter.getClassName(), getClass().getName());
    }

    @Test
    public void insertMany() {
        List<String> sourceCode = Arrays.asList(
                "System.out.println(\"Loco loco\");",
                "System.out.println(\"Loca loca\");"
        );

        this.codeWriter.insertMany("truc", sourceCode);

        assertTrue(this.codeWriter.getClassContainer().isFrozen());
    }

    public void truc() {

    }
}