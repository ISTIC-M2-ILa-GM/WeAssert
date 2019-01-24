package fr.istic.gm.weassert.test;

import java.util.List;

public interface CodeWriter {
    void insertOne(String methodName, String code);
    void insertMany(String methodName, List<String> codes);
}
