package fr.istic.gm.weassert.test.utils;

import java.util.List;

public interface ProcessBuilderFactory {

    ProcessBuilderWrapper create(String directory, String... command);
}
