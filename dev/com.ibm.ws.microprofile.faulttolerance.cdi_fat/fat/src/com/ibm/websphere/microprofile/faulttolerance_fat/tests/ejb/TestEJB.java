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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Retry;

import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.CounterFactory.CounterId;
import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.EarlyInterceptor.InterceptEarly;
import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.LateInterceptor.InterceptLate;

@Stateless
public class TestEJB {

    @Inject
    @CounterId("retryGetString")
    private AtomicInteger retryGetStringCounter;

    @Asynchronous
    public Future<String> asyncGetString() {
        return CompletableFuture.completedFuture("OK");
    }

    @InterceptEarly
    @InterceptLate
    @Retry(maxRetries = 5)
    public String retryGetString() {
        retryGetStringCounter.incrementAndGet();
        throw new TestException("retryGetString failed");
    }

}
