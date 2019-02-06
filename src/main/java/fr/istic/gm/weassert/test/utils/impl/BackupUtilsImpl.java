package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.BackupUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BackupUtilsImpl implements BackupUtils {

    private String fileName;

    @Getter
    private byte[] content;

    public BackupUtilsImpl(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);
        try {
            content = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new WeAssertException("Can't store file: " + fileName, e);
        }
    }

    @Override
    public void restore() {
        File file = new File(fileName);
        try {
            Files.write(file.toPath(), content);
        } catch (IOException e) {
            throw new WeAssertException("Can't restore file: " + fileName, e);
        }
    }
}
