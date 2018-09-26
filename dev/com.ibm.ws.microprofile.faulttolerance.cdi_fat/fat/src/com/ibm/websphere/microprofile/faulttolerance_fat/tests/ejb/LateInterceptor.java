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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;

import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.CounterFactory.CounterId;
import com.ibm.websphere.microprofile.faulttolerance_fat.tests.ejb.LateInterceptor.InterceptLate;

/**
 * An interceptor that comes after the FaultToleranceInterceptor in the chain and counts invocations.
 */
@Interceptor
@InterceptLate
@Priority(Interceptor.Priority.LIBRARY_AFTER)
public class LateInterceptor {

    @Inject
    @CounterId("LateInterceptor")
    public AtomicInteger counter;

    /**
     * Interceptor binding for {@link LateInterceptor}
     */
    @Retention(RUNTIME)
    @Target({ TYPE, METHOD })
    @InterceptorBinding
    public static @interface InterceptLate {}

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        counter.incrementAndGet();
        return ctx.proceed();
    }
}
