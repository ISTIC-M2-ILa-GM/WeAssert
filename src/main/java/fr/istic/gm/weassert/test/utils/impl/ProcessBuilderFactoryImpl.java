package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;
import lombok.AllArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
public class ProcessBuilderFactoryImpl implements ProcessBuilderFactory {

    private static final String ERROR_DIRECTORY = "ProcessBuilderFactoryImpl: The directory needs to exists %s";
    private static final String ERROR_COMMAND = "ProcessBuilderFactoryImpl: No command";

    @Override
    public ProcessBuilderWrapper create(String directory, String... command) {
        if (directory == null || !Files.isDirectory(Paths.get(directory))) {
            throw new WeAssertException(String.format(ERROR_DIRECTORY, directory));
        }
        if (command == null || command.length == 0 || command[0].isEmpty()) {
            throw new WeAssertException(ERROR_COMMAND);
        }
        return new ProcessBuilderWrapperImpl(command)
                .directory(directory);
    }
}
