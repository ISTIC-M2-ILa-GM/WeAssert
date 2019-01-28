package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.TestRunner;
import fr.istic.gm.weassert.test.analyser.CodeVisitor;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.TestAnalysed;
import lombok.AllArgsConstructor;

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
        List<LocalVariableParsed> parse = localVariableParser.parse();
        parse.forEach(p ->
                p.getLocalVariables().forEach(v ->
                {
                    String completeMethodName = localVariableParser.getClazz().getName() + p.getName() + p.getDesc();
                    codeWriter.insertOne(p.getName(), p.getDesc(), String.format("CodeVisitor.INSTANCE.visit(\"%s\", %s)", completeMethodName + " " + v, v));
                }));
        codeWriter.writeAndCloseFile();
        testRunner.startTest(localVariableParser.getClazz());
        Map<String, Object> variableValues = codeVisitor.getVariableValues();
        codeVisitor.initVariableValues();
        testRunner.startTest(localVariableParser.getClazz());
        return null;
    }
}
