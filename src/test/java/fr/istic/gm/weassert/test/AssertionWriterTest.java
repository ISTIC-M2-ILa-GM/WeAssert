package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.AssertionWriterImpl;
import fr.istic.gm.weassert.test.model.Assertion;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssertionWriterTest {
    private Assertion assertion;

    private AssertionWriter assertionWriter;

    @Before
    public void setUp() {
        String actualValue = "monObjet.getTruc()";
        String expectedValue = "true";


        this.assertion = new Assertion(actualValue, expectedValue);

        this.assertionWriter = new AssertionWriterImpl();
    }

    @Test
    public void insert() {

    }

    @Test
    public void generate() {
        String expected = "assertEquals(" + assertion.getActualValue() + "," + assertion.getExpectedValue() + ");";

        assertEquals(assertionWriter.generate(assertion), expected);
    }
}