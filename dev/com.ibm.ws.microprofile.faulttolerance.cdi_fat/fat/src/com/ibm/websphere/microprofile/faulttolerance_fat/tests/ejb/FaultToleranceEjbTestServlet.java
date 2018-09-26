/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

import org.junit.Test;

import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.CounterFactory.CounterId;

import componenttest.app.FATServlet;

@WebServlet("/EJBTest")
public class FaultToleranceEjbTestServlet extends FATServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    @CounterId("EarlyInterceptor")
    private AtomicInteger earlyInterceptorCounter;

    @Inject
    @CounterId("LateInterceptor")
    private AtomicInteger lateInterceptorCounter;

    @Inject
    @CounterId("retryGetString")
    private AtomicInteger methodCounter;

    @EJB
    private TestEJB testEjb;

    @Test
    public void testEjbAsync() throws InterruptedException, ExecutionException {
        Future<String> result = testEjb.asyncGetString();
        assertThat(result.get(), is("OK"));
    }

    @Test
    public void testEjbRetryInterceptors() {
        try {
            testEjb.retryGetString();
            fail("Exception not thrown");
        } catch (EJBException e) {
        } // Expected

        assertThat("methodCounter", methodCounter.get(), is(6)); // Method called six times (1 + 5 retries)
        assertThat("earlyInterceptorCounter", earlyInterceptorCounter.get(), is(1));
        assertThat("lateInterceptorCounter", lateInterceptorCounter.get(), is(6));
    }
}
