package fr.istic.gm.weassert;

import java.nio.file.Paths;

public class TestUtils {

    public static String getAbsolutePath(String relativePath) {
        return Paths.get(relativePath).toAbsolutePath().toString();
    }
}
