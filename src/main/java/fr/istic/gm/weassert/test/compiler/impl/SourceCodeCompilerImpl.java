package fr.istic.gm.weassert.test.compiler.impl;

import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class SourceCodeCompilerImpl implements SourceCodeCompiler {

    private static final String POM_FILE = "/pom.xml";
    private static final String ERROR_NO_POM = "SourceCodeCompilerImpl: No pom file on the maven project %s";

    private String mavenProjectPath;

    private ProcessBuilderWrapper processBuilder;

    private UrlClassLoaderWrapper urlClassLoaderWrapper;

    public SourceCodeCompilerImpl(String mavenProjectPath, String mavenCommand, ProcessBuilderFactory processBuilderFactory, UrlClassLoaderWrapper urlClassLoaderWrapper) {
        this.mavenProjectPath = mavenProjectPath + POM_FILE;
        this.urlClassLoaderWrapper = urlClassLoaderWrapper;
        if (!Paths.get(mavenProjectPath).toFile().exists()) {
            throw new WeAssertException(String.format(ERROR_NO_POM, this.mavenProjectPath));
        }
        processBuilder = processBuilderFactory.create(mavenProjectPath, mavenCommand, "clean", "test", "-DskipTests");
    }

    @Override
    public void compileAndWait() {
        try {
            Process start = processBuilder.start();
            BufferedReader buff = new BufferedReader(new InputStreamReader(start.getInputStream()));
            while (start.isAlive()) {
                String line;
                while ((line = buff.readLine()) != null) {
                    log.info(line);
                }
            }
            urlClassLoaderWrapper.refresh();
        } catch (Exception e) {
            throw new WeAssertException("SourceCodeCompilerImpl: Compile error", e);
        }
    }
}
