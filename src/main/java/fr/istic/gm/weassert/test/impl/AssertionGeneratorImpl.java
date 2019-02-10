package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.AssertionGenerator;
import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.model.TestAnalysed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssertionGeneratorImpl implements AssertionGenerator {

    private static final List<Class> PRIMITIVES = Arrays.asList(Integer.class, Long.class, Float.class, Double.class, Boolean.class);
    private CodeWriter codeWriter;

    public AssertionGeneratorImpl(CodeWriter codeWriter) {
        this.codeWriter = codeWriter;
    }

    @Override
    public void generate(List<TestAnalysed> testsAnalysed) {
        testsAnalysed.forEach(testAnalysed -> {
            List<String> generatedCodes = new ArrayList<>();

            testAnalysed.getVariableValues().keySet().forEach(key -> {
                Object o = testAnalysed.getVariableValues().get(key);
                if (PRIMITIVES.contains(o.getClass())) {
                    generatedCodes.add("org.junit.Assert.assertEquals(" + key + "," + o.toString() + "); // GENERATED ASSERT");
                } else if (o.getClass() == String.class) {
                    generatedCodes.add("org.junit.Assert.assertEquals(" + key + ",\"" + o.toString() + "\"); // GENERATED ASSERT");
                }
            });

            this.codeWriter.insertMany(testAnalysed.getMethodName(), testAnalysed.getMethodDesc(), generatedCodes);
        });

        this.codeWriter.writeAndCloseFile();
    }
}
