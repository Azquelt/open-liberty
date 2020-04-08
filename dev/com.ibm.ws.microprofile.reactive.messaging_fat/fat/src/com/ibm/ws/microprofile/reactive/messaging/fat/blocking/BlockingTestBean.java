/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.reactive.messaging.fat.blocking;

import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 *
 */
@ApplicationScoped
public class BlockingTestBean {

    Supplier<String> appNameSupplier;
    Supplier<String> contextualAppNameSupplier;

    @Outgoing("raw")
    public PublisherBuilder<String> generator() {
        return ReactiveStreams.of("abc", "def", "xyz");
    }

    @Incoming("raw")
    @Outgoing("upstream")
    public String upstream(String input) {
        System.out.println("In upstream");
        System.out.println("AppName: " + appNameSupplier.get());
        System.out.println("CtxAppName: " + contextualAppNameSupplier.get());
        System.out.println("Thread: " + Thread.currentThread().getName());
        return input;
    }

    @Blocking
    @Incoming("upstream")
    @Outgoing("processed")
    public String process(String input) throws NamingException {

        StringBuilder sb = new StringBuilder();
        sb.append("Input:        ").append(input).append("\n");
        sb.append("Thread:       ").append(Thread.currentThread().getName()).append("\n");
        sb.append("Thread Class: ").append(Thread.currentThread().getClass().getName()).append("\n");
//        sb.append("App name:     ").append(appName).append("\n");
        sb.append("\n");
        System.out.println("In blocking:");
        System.out.println("AppName: " + appNameSupplier.get());
        System.out.println("CtxAppName: " + contextualAppNameSupplier.get());
        System.out.println("Thread: " + Thread.currentThread().getName());

        return sb.toString();
    }

    @Incoming("processed")
    @Outgoing("downstream")
    public String downstream(String input) {
        System.out.println("In downstream:");
        System.out.println("AppName: " + appNameSupplier.get());
        System.out.println("CtxAppName: " + contextualAppNameSupplier.get());
        System.out.println("Thread: " + Thread.currentThread().getName());
        return input;
    }

    private String getAppName() {
        try {
            InitialContext c = new InitialContext();
            return (String) c.lookup("java:app/AppName");
        } catch (NamingException e) {
            return "Unknown";
        }
    }

    @PostConstruct
    private void init() {

        ThreadContext ctx = ThreadContext.builder()
                        .propagated(ThreadContext.APPLICATION, ThreadContext.SECURITY)
                        .build();
        appNameSupplier = this::getAppName;
        contextualAppNameSupplier = ctx.contextualSupplier(this::getAppName);
    }

}
