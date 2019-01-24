package fr.istic.gm.weassert.test;

import java.util.List;

public interface CodeWriter {
    void insertOne(String code);
    void insertMany(List<String> codes);
}
