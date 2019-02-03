package fr.istic.gm.weassert.test.compiler.impl;

import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceCodeCompilerImpl implements SourceCodeCompiler {

    private static final String POM_FILE = "/pom.xml";
    private static final String ERROR_NO_POM = "SourceCodeCompilerImpl: No pom file on the maven project %s";
    private static final String COMPILE = "compile";

    private String mavenProjectPath;

    private ProcessBuilderWrapper processBuilder;

    public SourceCodeCompilerImpl(String mavenProjectPath, String mavenCommand, ProcessBuilderFactory processBuilderFactory) {
        this.mavenProjectPath = mavenProjectPath + POM_FILE;
        if (!Files.exists(Paths.get(mavenProjectPath))) {
            throw new WeAssertException(String.format(ERROR_NO_POM, this.mavenProjectPath));
        }
        processBuilder = processBuilderFactory.create(mavenProjectPath, mavenCommand, COMPILE);
    }

    @Override
    public Process compile() {
        return processBuilder.start();
    }
}
