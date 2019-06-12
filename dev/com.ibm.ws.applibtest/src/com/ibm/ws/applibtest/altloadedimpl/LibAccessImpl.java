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
package com.ibm.ws.applibtest.altloadedimpl;

import javax.enterprise.context.ApplicationScoped;

import com.example.testlib.TheAdder;
import com.ibm.ws.applibtest.api.LibAccess;

/**
 *
 */
@ApplicationScoped
public class LibAccessImpl implements LibAccess {

    private final TheAdder adder = new TheAdder();

    @Override
    public int add(int a, int b) {
        return adder.add(a, b);
    }

}
