package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.test.model.TestAnalysed;

import java.util.List;

public interface AssertionGenerator {
    void generate(List<TestAnalysed> testsAnalysed);
}
