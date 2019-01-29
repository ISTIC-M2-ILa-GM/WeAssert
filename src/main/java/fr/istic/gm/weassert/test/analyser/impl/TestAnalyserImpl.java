package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.runner.TestRunner;
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

    private static final String CODE_VISITOR_INSTANCE = "INSTANCE";

    private Class<CodeVisitor> codeVisitorClass;

    private LocalVariableParser localVariableParser;

    private CodeWriter codeWriter;

    private CodeVisitor codeVisitor;

    private TestRunner testRunner;

    @Override
    public List<TestAnalysed> analyse() {
        addVisitorToTests();
        Class clazz = localVariableParser.getClazz();
        Map<VariableDefinition, Object> firstVariableValues = runTestsAndRetrieveFistVariableValues(clazz);
        return createAnalyseResult(firstVariableValues, codeVisitor.getVariableValues());
    }

    private void addVisitorToTests() {
        List<LocalVariableParsed> parse = localVariableParser.parse();
        parse.forEach(p ->
                p.getLocalVariables().forEach(v ->
                {
                    codeWriter.insertOne(p.getName(), p.getDesc(), String.format("%s.%s.visit(getClass(), \"%s\", \"%s\", \"%s\", %s)", codeVisitorClass.getName(), CODE_VISITOR_INSTANCE, p.getName(), p.getDesc(), v, v));
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
