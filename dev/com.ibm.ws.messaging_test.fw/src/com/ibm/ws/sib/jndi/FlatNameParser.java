/*
 * @start_no_prolog@
 * 
 * 
 * No IBM proprietary protection statement required
 * @end_no_prolog@
 */

/*******************************************************************************
 * Copyright (c) 1997, 1999 IBM Corporation and others.
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

//package examples;
package com.ibm.ws.sib.jndi;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;


class FlatNameParser implements NameParser {

    private static final Properties syntax = new Properties();
    static {
        syntax.put("jndi.syntax.direction", "flat");
        syntax.put("jndi.syntax.ignorecase", "false");
    }
    public Name parse(String name) throws NamingException {
        return new CompoundName(name, syntax);
    }
}
