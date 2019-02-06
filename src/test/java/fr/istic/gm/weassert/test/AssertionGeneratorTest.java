package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.impl.AssertionGeneratorImpl;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class AssertionGeneratorTest {
    private AssertionGenerator assertionGenerator;

    @Mock
    private CodeWriter codeWriterMock;


    @Before
    public void setUp() {
        this.assertionGenerator = new AssertionGeneratorImpl(this.codeWriterMock);
    }

    @Test
    public void generate() {
        Map<String, Object> variableValues = new HashMap<>();

        variableValues.put("maVariable1", new Object());

        TestAnalysed testAnalysed = TestAnalysed.builder()
                .clazz(getClass())
                .methodName("generate")
                .methodDesc("V()")
                .variableValues(variableValues)
                .build();

        this.assertionGenerator.generate(Collections.singletonList(testAnalysed));

        Mockito.verify(this.codeWriterMock).insertMany(testAnalysed.getMethodName(), testAnalysed.getMethodDesc(), new ArrayList<>());
    }

    @Test
    public void generateWithStrings() {
        Map<String, Object> variableValues = new HashMap<>();

        variableValues.put("maVariable2", "Hey there !");

        TestAnalysed testAnalysed = TestAnalysed.builder()
                .clazz(getClass())
                .methodName("generateWithStrings")
                .methodDesc("V()")
                .variableValues(variableValues)
                .build();

        this.assertionGenerator.generate(Collections.singletonList(testAnalysed));

        List<String> generatedCodes = new ArrayList<>();
        generatedCodes.add("org.junit.Assert.assertEquals(maVariable2,\"Hey there !\"); // GENERATED ASSERT");
        Mockito.verify(this.codeWriterMock).insertMany(testAnalysed.getMethodName(), testAnalysed.getMethodDesc(), generatedCodes);
    }

    @Test
    public void generateWithIntegers() {
        Map<String, Object> variableValues = new HashMap<>();

        variableValues.put("maVariable3", 123);

        TestAnalysed testAnalysed = TestAnalysed.builder()
                .clazz(getClass())
                .methodName("generateWithIntegers")
                .methodDesc("V()")
                .variableValues(variableValues)
                .build();

        this.assertionGenerator.generate(Collections.singletonList(testAnalysed));

        List<String> generatedCodes = new ArrayList<>();
        generatedCodes.add("org.junit.Assert.assertEquals(maVariable3,123); // GENERATED ASSERT");
        Mockito.verify(this.codeWriterMock).insertMany(testAnalysed.getMethodName(), testAnalysed.getMethodDesc(), generatedCodes);
    }

    @Test
    public void generateWithFloats() {
        Map<String, Object> variableValues = new HashMap<>();

        variableValues.put("maVariable4", 123.0);

        TestAnalysed testAnalysed = TestAnalysed.builder()
                .clazz(getClass())
                .methodName("generateWithFloats")
                .methodDesc("V()")
                .variableValues(variableValues)
                .build();

        this.assertionGenerator.generate(Collections.singletonList(testAnalysed));

        List<String> generatedCodes = new ArrayList<>();
        generatedCodes.add("org.junit.Assert.assertEquals(maVariable4,123.0); // GENERATED ASSERT");
        Mockito.verify(this.codeWriterMock).insertMany(testAnalysed.getMethodName(), testAnalysed.getMethodDesc(), generatedCodes);
    }
}