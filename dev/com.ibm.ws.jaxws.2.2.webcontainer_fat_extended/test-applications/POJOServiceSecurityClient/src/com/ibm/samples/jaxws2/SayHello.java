/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
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
package com.ibm.samples.jaxws2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 *
 */
@WebService(name = "SayHello", targetNamespace = "http://jaxws2.samples.ibm.com")
@XmlSeeAlso({
              ObjectFactory.class
})
public interface SayHello {

    /**
     *
     * @param arg0
     * @return
     *         returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://jaxws2.samples.ibm.com", className = "com.ibm.samples.jaxws2.SayHello_Type")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://jaxws2.samples.ibm.com", className = "com.ibm.samples.jaxws2.SayHelloResponse")
    public String sayHello(
                           @WebParam(name = "arg0", targetNamespace = "") String arg0);

}
