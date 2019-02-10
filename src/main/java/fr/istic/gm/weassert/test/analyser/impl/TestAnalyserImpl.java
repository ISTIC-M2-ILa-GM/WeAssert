package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.analyser.CodeVisitor;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.MethodDefinition;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import fr.istic.gm.weassert.test.model.VariableDefinition;
import fr.istic.gm.weassert.test.runner.TestRunner;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.istic.gm.weassert.test.utils.ClassResolverUtil.mapClassToClassPath;

@Slf4j
@AllArgsConstructor
public class TestAnalyserImpl implements TestAnalyser {

    private static final String VISITOR_CODE = "%s.INSTANCE.visit(getClass(), \"%s\", \"%s\", \"%s\", %s); // GENERATED VISITOR";

    private LocalVariableParser localVariableParser;

    private CodeWriter codeWriter;

    private CodeVisitor codeVisitor;

    private SourceCodeCompiler sourceCodeCompiler;

    private UrlClassLoaderWrapper urlClassLoaderWrapper;

    private TestRunner testRunner;

    @Override
    public List<TestAnalysed> analyse() {

        log.info("ANALYSE...");
        addVisitorToTests();
        Class<?> refreshedClass = retrieveClass();
        Map<VariableDefinition, Object> firstVariableValues = runTestsAndRetrieveFistVariableValues(refreshedClass);
        List<TestAnalysed> result = createAnalyseResult(firstVariableValues, codeVisitor.getVariableValues());

        log.info(String.format("ANALYSED: %s", result));
        return result;
    }

    private Class<?> retrieveClass() {
        return urlClassLoaderWrapper.getClassList().stream()
                .filter(c -> mapClassToClassPath(c).equals(mapClassToClassPath(localVariableParser.getClazz())))
                .findFirst()
                .orElseThrow(() -> new WeAssertException("Can't reload refreshed class"));
    }

    private void addVisitorToTests() {
        List<LocalVariableParsed> parse = localVariableParser.parse();
        parse.forEach(p ->
                p.getLocalVariables().forEach(v ->
                        {
                            String visitor = String.format(VISITOR_CODE, codeVisitor.getClass().getName(), p.getName(), p.getDesc(), v, v);
                            codeWriter.insertOne(p.getName(), p.getDesc(), visitor);
                        }
                ));
        codeWriter.writeAndCloseFile();
        sourceCodeCompiler.compileAndWait();
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
