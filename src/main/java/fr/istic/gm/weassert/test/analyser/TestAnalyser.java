package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.model.MethodAnalyser;

import java.util.List;

public interface TestAnalyser {
    List<MethodAnalyser> analyse();
}
