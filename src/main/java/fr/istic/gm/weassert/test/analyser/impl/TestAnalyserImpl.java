package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.analyser.CodeVisitor;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.MethodDefinition;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.model.VariableDefinition;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TestAnalyserImpl implements TestAnalyser {

    private LocalVariableParser localVariableParser;

    private CodeWriter codeWriter;

    private CodeVisitor codeVisitor;

    private TestRunner testRunner;

    @Override
    public List<TestAnalysed> analyse() {
        Class clazz = localVariableParser.getClazz();
        addVisitorToTests(clazz);
        Map<VariableDefinition, Object> firstVariableValues = runTestsAndRetrieveFistVariableValues(clazz);
        return createAnalyseResult(firstVariableValues, codeVisitor.getVariableValues());
    }

    private void addVisitorToTests(Class clazz) {
        List<LocalVariableParsed> parse = localVariableParser.parse();
        parse.forEach(p ->
                p.getLocalVariables().forEach(v ->
                {
                    String completeMethodName = clazz.getName() + p.getName() + p.getDesc();
                    codeWriter.insertOne(p.getName(), p.getDesc(), String.format("CodeVisitor.INSTANCE.visit(\"%s\", %s)", completeMethodName + " " + v, v));
                }));
        codeWriter.writeAndCloseFile();
    }

    private Map<VariableDefinition, Object> runTestsAndRetrieveFistVariableValues(Class clazz) {
        testRunner.startTest(clazz);
        Map<VariableDefinition, Object> firstVariableValues = codeVisitor.getVariableValues();
        codeVisitor.initVariableValues();
        testRunner.startTest(clazz);
        return firstVariableValues;
    }

    private List<TestAnalysed> createAnalyseResult(Map<VariableDefinition, Object> firstVariableValues, Map<VariableDefinition, Object> secondsVariableValues) {
        Map<MethodDefinition, TestAnalysed> testAnalyseds = new HashMap<>();
        firstVariableValues.keySet().stream().filter(k -> secondsVariableValues.containsKey(k) && secondsVariableValues.get(k).equals(firstVariableValues.get(k))).forEach(k -> {
            MethodDefinition methodDefinition = MethodDefinition.builder().clazz(k.getClazz()).methodName(k.getMethodName()).methodDesc(k.getMethodDesc()).build();
            TestAnalysed testAnalysed;
            if (testAnalyseds.containsKey(methodDefinition)) {
                testAnalysed = testAnalyseds.get(methodDefinition);
            } else {
                testAnalysed = TestAnalysed.builder().clazz(k.getClazz()).methodName(k.getMethodName()).methodDesc(k.getMethodDesc()).build();
                testAnalyseds.put(methodDefinition, testAnalysed);
            }
            testAnalysed.getVariableValues().put(k.getVariableName(), firstVariableValues.get(k));
        });
        return new ArrayList<>(testAnalyseds.values());
    }
}
