package fr.istic.gm.weassert.test.analyser;

import java.util.Map;

public interface CodeVisitor {

    Map<String, Object> getVariableValues();
    void initVariableValues();

    void visit(String localVariable);
}
