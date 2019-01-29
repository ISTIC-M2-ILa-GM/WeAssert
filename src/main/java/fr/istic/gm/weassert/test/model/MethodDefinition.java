package fr.istic.gm.weassert.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MethodDefinition {

    private Class clazz;

    private String methodName;

    private String methodDesc;
}
