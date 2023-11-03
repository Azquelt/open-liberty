/*******************************************************************************
 * Copyright (c) 2022, 2023 IBM Corporation and others.
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
package io.openliberty.microprofile.telemetry.internal.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import componenttest.rules.repeater.FeatureReplacementAction;
import componenttest.rules.repeater.MicroProfileActions;
import componenttest.rules.repeater.RepeatTests;

import componenttest.annotation.MinimumJavaLevel;
import componenttest.containers.TestContainerSuite;
import componenttest.custom.junit.runner.AlwaysPassesTest;
import io.openliberty.microprofile.telemetry.internal.tests.AgentConfigTest;
import io.openliberty.microprofile.telemetry.internal.tests.AgentTest;
import io.openliberty.microprofile.telemetry.internal.tests.CrossFeatureJaegerTest;
import io.openliberty.microprofile.telemetry.internal.tests.CrossFeatureZipkinTest;
import io.openliberty.microprofile.telemetry.internal.tests.JaegerLegacyTest;
import io.openliberty.microprofile.telemetry.internal.tests.JaegerOtelCollectorTest;
import io.openliberty.microprofile.telemetry.internal.tests.JaegerOtlpTest;
import io.openliberty.microprofile.telemetry.internal.tests.JaegerSecureOtelCollectorTest;
import io.openliberty.microprofile.telemetry.internal.tests.JaegerSecureOtlpTest;
import io.openliberty.microprofile.telemetry.internal.tests.TracingNotEnabledTest;
import io.openliberty.microprofile.telemetry.internal.tests.ZipkinOtelCollectorTest;
import io.openliberty.microprofile.telemetry.internal.tests.ZipkinTest;

@RunWith(Suite.class)
@SuiteClasses({
                AlwaysPassesTest.class, //Must keep this test to run something in the Java 6 builds.
                AgentTest.class,
                AgentConfigTest.class,
                CrossFeatureJaegerTest.class,
                CrossFeatureZipkinTest.class,
                JaegerSecureOtelCollectorTest.class,
                JaegerSecureOtlpTest.class,
                JaegerOtlpTest.class,
                JaegerOtelCollectorTest.class,
                JaegerLegacyTest.class,
                TracingNotEnabledTest.class,
                ZipkinOtelCollectorTest.class,
                ZipkinTest.class,
})

@MinimumJavaLevel(javaLevel = 11)
/**
 * Purpose: This suite collects and runs all known good test suites.
 */
public class FATSuite extends TestContainerSuite {

    public static final String MP60_BETA_ID = MicroProfileActions.MP60_ID + "_BETA";
    public static final String MP61_BETA_ID = MicroProfileActions.MP61_ID + "_BETA";

    public static RepeatTests allMPRepeats(String serverName) {
        return TelemetryActions
                        .repeat(serverName, MicroProfileActions.MP61, MicroProfileActions.MP60)
                        .andWith(TelemetryActions.repeatFor(serverName, MicroProfileActions.MP61).fullFATOnly().withBeta().withID(MP61_BETA_ID))
                        .andWith(TelemetryActions.repeatFor(serverName, MicroProfileActions.MP60).fullFATOnly().withBeta().withID(MP60_BETA_ID));
    }

}
