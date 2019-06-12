/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.applibtest.impl;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.ibm.ws.applibtest.api.LibAccess;

@ApplicationScoped
public class BeanProvider {

    private ClassLoader appLibLoader;

    @PostConstruct
    private void init() {
        appLibLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                List<Class<?>> interfaces = Arrays.asList(LibAccess.class);
                URL[] urls = new URL[] { BeanProvider.class.getProtectionDomain().getCodeSource().getLocation() }; // URL for this bundle
                return new AppLibraryClassLoader(urls, interfaces, Thread.currentThread().getContextClassLoader());
            }
        });
    }

    @Produces
    private LibAccess produceLibAccess() {
        LibAccess result;
        try {
            Class<?> appLibClass = appLibLoader.loadClass("com.ibm.ws.applibtest.altloadedimpl.LibAccessImpl");
            result = (LibAccess) appLibClass.newInstance();
            return result;
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
