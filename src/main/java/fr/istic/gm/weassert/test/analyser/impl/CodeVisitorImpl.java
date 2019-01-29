package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.analyser.CodeVisitor;
import fr.istic.gm.weassert.test.model.VariableDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CodeVisitorImpl implements CodeVisitor {

    private Map<VariableDefinition, Object> variableValues;

    @Override
    public Map<VariableDefinition, Object> getVariableValues() {
        if (variableValues == null) {
            variableValues = new HashMap<>();
        }
        return variableValues;
    }

    @Override
    public void initVariableValues() {
        variableValues = new HashMap<>();
    }

    @Override
    public void visit(Class clazz, String methodName, String methodDesc, String variableName, Object variableValue) {

        VariableDefinition variableDefinition = VariableDefinition.builder()
                .clazz(clazz)
                .methodName(methodName)
                .methodDesc(methodDesc)
                .variableName(variableName)
                .build();
        getVariableValues().put(variableDefinition, variableValue);
    }
}
