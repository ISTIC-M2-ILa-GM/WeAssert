package fr.istic.gm.weassert;

import com.sun.jndi.toolkit.url.UrlUtil;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.stream.Collectors;

public class App {

    public static void main(String... args) {
        if(args.length < 1) {
            System.err.println("Missing argument ! Please provide an absolute path to a Maven project.");

            throw new MissingFormatArgumentException("[path]");
        }
        String path = args[0];
        System.out.println("Selected absolute path: " + path);

        JUnitCore core = new JUnitCore();

        List<Path> files;

        try {
            files = Files.walk(Paths.get(path + "/target/test-classes/"))
                    .filter(f -> f.toString().endsWith("Test.class"))
                    .collect(Collectors.toList());

            System.out.println("# of files: " + files.size());
            files.forEach(file -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
