package fr.istic.gm.weassert.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "variableValues")
public class TestAnalysed {

    private Class clazz;

    private String methodName;

    private String methodDesc;

    Map<String, Object> variableValues = new HashMap<>();

    public Map<String, Object> getVariableValues() {
        if (variableValues == null) {
            variableValues = new HashMap<>();
        }
        return variableValues;
    }
}
