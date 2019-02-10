package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.FileUtils;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UrlClassLoaderWrapperImpl implements UrlClassLoaderWrapper {

    private static final String LOAD_CLASS_ERROR = "Can't load class: ";
    private static final String PARSED_ERROR = "Url can't be parsed: ";

    private List<String> paths;

    @Getter
    private List<Class<?>> classList;

    private URLClassLoader urlClassLoader;

    public UrlClassLoaderWrapperImpl(List<String> paths) {
        this.paths = paths;
        refresh();
    }

    @Override
    public void refresh() {
        log.info("LOADING CLASS...");
        URL[] urls = paths.stream().map(this::mapToUrl).toArray(URL[]::new);
        urlClassLoader = URLClassLoader.newInstance(urls, getClass().getClassLoader());
        classList = mapPathsToClass(paths);
        log.info("CLASSLOADED: " + paths);
    }

    private List<Class<?>> mapPathsToClass(List<String> paths) {
        List<String> fileNames = mapPathsToFileNames(paths);
        List<String> classNames = new ArrayList<>();
        fileNames.forEach(f -> {
            for (String p : paths) {
                if (f.startsWith(p)) {
                    String className = f.replaceAll(String.format("^%s", p), "")
                            .replaceAll("\\.class$", "")
                            .replaceAll("^/", "")
                            .replace("/", ".");
                    classNames.add(className);
                    break;
                }
            }
        });
        return classNames.stream().map(this::mapToClass).collect(Collectors.toList());
    }

    private List<String> mapPathsToFileNames(List<String> paths) {
        return paths.stream()
                .map(File::new)
                .map(FileUtils::findFilesFromFolder)
                .flatMap(Collection::stream)
                .map(File::getPath)
                .filter(f -> f.endsWith(".class")).collect(Collectors.toList());
    }

    private URL mapToUrl(String a) {
        try {
            return new URL(String.format("file://%s/", a));
        } catch (MalformedURLException e) {
            String message = PARSED_ERROR + e.getCause();
            log.info(message);
            throw new WeAssertException(message, e);
        }
    }

    private Class<?> mapToClass(String className) {
        try {
            return urlClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            String message = LOAD_CLASS_ERROR + e.getCause();
            log.info(message);
            throw new WeAssertException(message, e);
        }
    }
}
