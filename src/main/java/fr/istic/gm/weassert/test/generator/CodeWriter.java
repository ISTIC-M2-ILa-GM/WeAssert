package fr.istic.gm.weassert.test.generator;

import java.util.List;

public interface CodeWriter {
    void insertOne(String methodName, String desc, String code);
    void insertMany(String methodName, String desc, List<String> codes);
    void writeAndCloseFile();
}
