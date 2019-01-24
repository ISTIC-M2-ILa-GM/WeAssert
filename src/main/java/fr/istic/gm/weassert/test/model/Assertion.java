package fr.istic.gm.weassert.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Assertion {
    private String actualValue;

    private String expectedValue;
}