package fr.istic.gm.weassert.test.utils;

import fr.istic.gm.weassert.test.utils.impl.BackupUtilsImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BackupUtilsTest {

    private String absolutePath;
    private BackupUtilsImpl backupUtils;

    @Before
    public void setUp() {
        absolutePath = getAbsolutePath("fake/src/test/java/fr/istic/gm/weassert/fake/PersonTest.java");
        backupUtils = new BackupUtilsImpl(absolutePath);
    }

    @Test
    public void shouldStoreData() throws IOException {

        byte[] content = Files.readAllBytes(new File(absolutePath).toPath());

        assertThat(backupUtils.getContent(), equalTo(content));
    }
}
