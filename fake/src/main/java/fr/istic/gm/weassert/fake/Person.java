package fr.istic.gm.weassert.fake;

import lombok.Data;

/**
 * A fake class Person to test the projet
 */
@Data
public class Person {

    private String name;
    private int age;

    public boolean isAdult() {
        return age >= 18;
    }
}