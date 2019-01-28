package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.CodeWriterImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodeWriterITest {
    private CodeWriterImpl codeWriter;

    @Before
    public void setUp() {
        this.codeWriter = new CodeWriterImpl(getClass());

        assertEquals(this.codeWriter.getClassName(), getClass().getName());
    }

    @Test
    public void insertMany() {
        List<String> sourceCode = Arrays.asList(
                "System.out.println(\"Hello world\");",
                "System.out.println(\"Leonard de Vinci\");"
        );

        this.codeWriter.insertMany("insertMany", "()V", sourceCode);
        this.codeWriter.writeAndCloseFile();

        assertTrue(this.codeWriter.getClassContainer().isFrozen());
    }
}