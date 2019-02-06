package fr.istic.gm.weassert.test.utils;

public interface ProcessBuilderFactory {

    ProcessBuilderWrapper create(String directory, String... command);
}
