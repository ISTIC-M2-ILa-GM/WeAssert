package fr.istic.gm.weassert.test;

import java.util.List;

/**
 * A Url Class Loader wrapper to retrieve classes
 */
public interface UrlClassLoaderWrapper {

    /**
     * Retrieve classes
     *
     * @return the classes
     */
    List<Class<?>> getClassList();
}
