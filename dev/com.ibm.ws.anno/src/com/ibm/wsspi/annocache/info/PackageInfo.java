/*******************************************************************************
 * Copyright (c) 2017, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wsspi.annocache.info;

/**
 * <p>Info object type representing a java package.</p>
 * 
 * <p>The name and qualified name of a package info object are the same.</p>
 */
public interface PackageInfo extends Info, com.ibm.wsspi.anno.info.PackageInfo {
    // jakarta review: No longer used.
    // See com.ibm.ws.annocache.info.internal.ClassInfoImpl.isJavaClass(String)
    /**
     * <p>Naming constant: The prefix for the <code>java</code> package,
     * (with a trailing ".").</p>
     */
    String JAVA_CLASS_PREFIX = "java.";

    // jakarta review: no current uses
    /**
     * <p>Naming constant: The prefix for the <code>javax</code> package,
     * (with a trailing ".").</p>
     */
    String JAVAX_CLASS_PREFIX = "javax.";

    // jakarta review: No longer used.
    // See com.ibm.ws.annocache.info.internal.ClassInfoImpl.isJavaClass(String)
    /**
     * <p>Naming constant: The prefix for the <code>javax.ejb</code> package,
     * (with a trailing ".").</p>
     */
    String JAVAX_EJB_CLASS_PREFIX = "javax.ejb.";

    // jakarta review: No longer used.
    // See com.ibm.ws.annocache.info.internal.ClassInfoImpl.isJavaClass(String)
    /**
     * <p>Naming constant: The prefix for the <code>javax.servlet</code> package,
     * (with a trailing ".").</p>
     */
    String JAVAX_SERVLET_CLASS_PREFIX = "javax.servlet.";

    /**
     * <p>Tell if this package info was created to represent a package which
     * either could not be loaded, or which has no declared package information.</p>
     * 
     * @return True if this package object is artificial. Otherwise, false.
     */
    boolean getIsArtificial();

    /**
     * <p>Tell if this package info represents a failed load. This is a special
     * case for artificial packages, and represents the case when declared package
     * information is available, but could not be loaded.</p>
     * 
     * @return True if this package object is for a failed load. Otherwise, false.
     */
    boolean getForFailedLoad();
}
