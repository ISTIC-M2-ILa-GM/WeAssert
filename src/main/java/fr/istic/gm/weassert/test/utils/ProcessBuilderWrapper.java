package fr.istic.gm.weassert.test.utils;

import java.io.File;
import java.util.List;

public interface ProcessBuilderWrapper {
    Process start();
    File directory();
    List<String> command();
    ProcessBuilderWrapper directory(String filePath);
}
