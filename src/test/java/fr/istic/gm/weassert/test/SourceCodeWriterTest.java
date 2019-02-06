package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.SourceCodeWriter;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import fr.istic.gm.weassert.test.utils.ClassReaderFactoryTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static org.junit.Assert.*;


public class SourceCodeWriterTest {
    private SourceCodeWriter sourceCodeWriter;
    private String classPath;

    @Before
    public void setUp() {
        this.classPath = getAbsolutePath("fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java");

        this.sourceCodeWriter = new SourceCodeWriter(this.classPath);
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
    public void writeAndCloseFile() {
        String code = "System.out.println(\"Hello World !\");";
        this.sourceCodeWriter.insertOne("testAge", "()V", code);
        this.sourceCodeWriter.writeAndCloseFile();
        
        try {
            String readFile = new String(Files.readAllBytes(Paths.get(this.classPath)));
            assertEquals(this.sourceCodeWriter.getSourceCode(), readFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}