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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Creates counters that can be injected.
 * <p>
 * Usage:
 * {@code @Inject @CounterId("foo") AtomicInteger fooCounter;}
 * <p>
 * Injection points which specify the same {@code @CounterId} will receive the same counter instance.
 */
@ApplicationScoped
public class CounterFactory {

    private final Map<String, AtomicInteger> counters = new HashMap<>();

    @Qualifier
    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, PARAMETER })
    public static @interface CounterId {
        @Nonbinding
        String value();
    }

    @Produces
    @Dependent
    @CounterId("")
    private synchronized AtomicInteger produce(InjectionPoint injectionPoint) {
        String id = null;
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier.annotationType() == CounterId.class) {
                id = ((CounterId) qualifier).value();
            }
        }

        if (id == null) {
            // We shouldn't get this since @CounterId is a required qualifier
            throw new IllegalStateException("No counter id for injection point: " + injectionPoint.getMember());
        }

        AtomicInteger counter = counters.get(id);
        if (counter == null) {
            counter = new AtomicInteger();
            counters.put(id, counter);
        }

        return counter;
    }
}
