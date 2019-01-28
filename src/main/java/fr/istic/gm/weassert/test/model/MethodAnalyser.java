package fr.istic.gm.weassert.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MethodAnalyser {

    private Class clazz;

    private String methodName;

    Map<String, Object> variableValues = new HashMap<>();
}
