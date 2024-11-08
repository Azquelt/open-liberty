/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package io.openliberty.microprofile.telemetry.internal.utils;

import java.util.Objects;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.ImageNameSubstitutor;

import com.ibm.websphere.simplicity.log.Log;

import componenttest.custom.junit.runner.RepeatTestFilter;

/**
 *
 */
public class TcpDumpContainer extends GenericContainer<TcpDumpContainer> {

    private static final Class<?> c = TcpDumpContainer.class;

    private String dumpFileDestination;
    private String testName;
    private final Container<?> containerToMonitor;

    public TcpDumpContainer(Container<?> containerToMonitor) {
        this(containerToMonitor, null);
    }

    public TcpDumpContainer(Container<?> containerToMonitor, String testName) {
        // Use Alpine image with tcpdump installed
        super(new ImageFromDockerfile().withDockerfileFromBuilder(builder -> builder.from(
                                                                                          ImageNameSubstitutor.instance().apply(DockerImageName.parse("alpine:3.17"))
                                                                                                              .asCanonicalNameString())
                                                                                    .run("apk add --no-cache tcpdump")
                                                                                    .cmd("tcpdump -w tcpdump.dump")
                                                                                    .build()));
        Objects.requireNonNull(containerToMonitor, "containerToMontior must not be null");
        this.containerToMonitor = containerToMonitor;
        this.testName = testName;
    }

    public TcpDumpContainer withDumpFileDestination(String destination) {
        dumpFileDestination = destination;
        return this;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        // Snaffle out the test class name
        if (testName == null) {
            testName = description.getClassName();
        }
        return super.apply(base, description);
    }

    @Override
    public void start() {
        if (dumpFileDestination == null && testName == null) {
            throw new RuntimeException("Either dump file destination or test name must be set");
        }
        if (!containerToMonitor.isRunning()) {
            throw new RuntimeException("The container to monitor must be started before the tcpdump container");
        }
        // Wait until start time to grab the containerId of the container we're monitoring because it's not available until that container has started.
        withNetworkMode("container:" + containerToMonitor.getContainerId());
        super.start();
    }

    @Override
    public void stop() {
        String destination = dumpFileDestination;
        if (destination == null) {
            destination = "results/tcpdump-" + testName + RepeatTestFilter.getRepeatActionsAsString();
        }

        // Before we stop, attempt to extract the dump file
        try {
            // Signal tcpdump to flush to file
            execInContainer("kill", "-s", "USR2", "1");
            Thread.sleep(500);
            copyFileFromContainer("tcpdump.dump", destination);
        } catch (Exception e) {
            Log.error(c, "stop", e, "Failed to extract dump file from container");
        }
        super.stop();
    }

}
