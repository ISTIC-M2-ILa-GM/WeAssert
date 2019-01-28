package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.analyser.CodeVisitor;
import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.analyser.TestAnalyser;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.model.MethodAnalyser;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TestAnalyserImpl implements TestAnalyser {

    private LocalVariableParser localVariableParser;

    private CodeWriter codeWriter;

    private CodeVisitor codeVisitor;

    private Class clazz;

    @Override
    public List<MethodAnalyser> analyse() {
        List<LocalVariableParsed> parse = localVariableParser.parse();
        parse.forEach(v -> {
            codeWriter.insertOne(v.getName(), v.getDesc(), "CodeVisitor.INSTANCE.visit(");
        });
        return null;
    }
}
