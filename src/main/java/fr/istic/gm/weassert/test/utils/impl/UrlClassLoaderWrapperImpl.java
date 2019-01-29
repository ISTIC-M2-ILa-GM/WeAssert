package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import lombok.Getter;
import lombok.extern.java.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Log
public class UrlClassLoaderWrapperImpl implements UrlClassLoaderWrapper {

    public static final String LOAD_CLASS_ERROR = "Can't load class: ";
    public static final String PARSED_ERROR = "Url can't be parsed: ";

    @Getter
    private List<Class<?>> classList;

    private URLClassLoader urlClassLoader;

    public UrlClassLoaderWrapperImpl(List<String> paths, List<String> classNames) {
        this.classList = new ArrayList<>();
        URL[] urls = paths.stream().map(this::mapToUrl).toArray(URL[]::new);
        this.urlClassLoader = URLClassLoader.newInstance(urls);
        classNames.forEach(this::mapToClass);
    }

    private URL mapToUrl(String a) {
        try {
            return new URL(a);
        } catch (MalformedURLException e) {
            String message = PARSED_ERROR + e.getCause();
            log.info(message);
            throw new WeAssertException(message, e);
        }
    }

    private void mapToClass(String className) {
        try {
            classList.add(urlClassLoader.loadClass(className));
        } catch (ClassNotFoundException e) {
            String message = LOAD_CLASS_ERROR + e.getCause();
            log.info(message);
            throw new WeAssertException(message, e);
        }
    }
}
