package com.ibm.ws.applibtest.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Allows a library to be loaded from the app classloader
 * <p>
 * This class should be used when you want to access a library provided by the
 * user in their application.
 * <p>
 * Users need to provide three things:
 * <p>
 * Firstly a set of interfaces which wrap the parts of the library which the
 * user wishes to use. These may not reference any classes from the library.
 * <p>
 * Secondly an implementation of those interfaces, which may reference the
 * classes from the library.
 * <p>
 * Thirdly the application classloader, from which the library classes may be
 * loaded.
 * <p>
 * The user should create an instance of this classloader, reflectively load
 * classes from their implementation, cast them to their interface and then use
 * them as normal.
 */
public class AppLibraryClassLoader extends URLClassLoader {

    private Map<String, Class<?>> interfaces;

    /**
     * @param implementationUrls URLs to jars which contain the implementation classes
     * @param interfaces a List of interface classes which should be shared with the caller
     * @param parent the parent classloader (usually the application classloader)
     */
    public AppLibraryClassLoader(URL[] implementationUrls, List<Class<?>> interfaces, ClassLoader parent) {
        super(implementationUrls, parent);
        this.interfaces = interfaces.stream()
                                    .collect(Collectors.toMap(c -> c.getCanonicalName(),
                                                              c -> c));
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // First check whether the requested class is an interface shared with the caller
        Class<?> result = interfaces.get(name);
        // Otherwise attempt to load from the implementation URLs
        if (result == null) {
            result = super.findClass(name);
        }
        return result;
    }

}
