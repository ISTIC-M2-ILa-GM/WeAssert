package fr.istic.gm.weassert.test.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<File> findFilesFromFolder(File folder) {
        List<File> files = new ArrayList<>();
        File[] listFiles = folder.listFiles();
        if (listFiles == null) {
            return files;
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                files.addAll(findFilesFromFolder(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }
}
