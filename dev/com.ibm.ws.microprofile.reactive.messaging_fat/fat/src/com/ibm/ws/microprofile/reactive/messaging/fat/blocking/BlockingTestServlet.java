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

import java.time.Duration;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.Test;

import componenttest.app.FATServlet;

@SuppressWarnings("serial")
@ApplicationScoped
@WebServlet("/blockingTest")
public class BlockingTestServlet extends FATServlet {

    @Inject
    private BlockingTestReceptionBean receptionBean;

    @Test
    public void testBlocking() throws InterruptedException {
        List<Message<String>> messages = receptionBean.assertReceivedMessages(3, Duration.ofSeconds(10));

        messages.stream().map(Message::getPayload)
                        .map(x -> "Output: " + x)
                        .forEach(System.out::println);
    }

}
