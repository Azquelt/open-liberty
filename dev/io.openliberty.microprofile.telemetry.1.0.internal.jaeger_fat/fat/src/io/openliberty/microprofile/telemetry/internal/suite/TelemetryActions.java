/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package io.openliberty.microprofile.telemetry.internal.suite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import componenttest.custom.junit.runner.Mode.TestMode;
import componenttest.rules.repeater.FeatureReplacementAction;
import componenttest.rules.repeater.FeatureSet;
import componenttest.rules.repeater.MicroProfileActions;
import componenttest.rules.repeater.RepeatActions;
import componenttest.rules.repeater.RepeatTests;
import componenttest.topology.impl.JavaInfo;

public class TelemetryActions {
    public static final String MP14_MPTEL11_ID = MicroProfileActions.MP14_ID + "_MPTEL11";
    public static final String MP41_MPTEL11_ID = MicroProfileActions.MP41_ID + "_MPTEL11";
    public static final String MP50_MPTEL11_ID = MicroProfileActions.MP50_ID + "_MPTEL11";

    public static final FeatureSet MP14_MPTEL11 = MicroProfileActions.MP14
                                                                          .addFeature("mpTelemetry-1.1")
                                                                          .build(MP14_MPTEL11_ID);

    public static final FeatureSet MP41_MPTEL11 = MicroProfileActions.MP41
                                                                          .addFeature("mpTelemetry-1.1")
                                                                          .build(MP41_MPTEL11_ID);

    public static final FeatureSet MP50_MPTEL11 = MicroProfileActions.MP50
                                                                          .addFeature("mpTelemetry-1.1")
                                                                          .build(MP50_MPTEL11_ID);

    //All MicroProfile Telemetry FeatureSets - must be descending order
    private static final FeatureSet[] ALL_MPTEL_SETS_ARRAY = { MicroProfileActions.MP61, MicroProfileActions.MP60, MP50_MPTEL11, MP41_MPTEL11, MP14_MPTEL11 };
    private static final List<FeatureSet> ALL_MPTEL_SETS_LIST = Arrays.asList(ALL_MPTEL_SETS_ARRAY);

    /**
     * Get a repeat action which runs the given feature set
     * <p>
     * The returned FeatureReplacementAction can then be configured further
     *
     * @param server the server to repeat on
     * @param featureSet the featureSet to repeat with
     * @return a FeatureReplacementAction
     */
    public static FeatureReplacementAction repeatFor(String server, FeatureSet featureSet) {
        return RepeatActions.forFeatureSet(ALL_MPTEL_SETS_LIST, featureSet, new String[] { server }, TestMode.FULL);
    }

    /**
     * Get a RepeatTests instance for the given FeatureSets. The first FeatureSet will be run in LITE mode. The others will be run in FULL.
     *
     * @param server The server to repeat on
     * @param firstFeatureSet The first FeatureSet
     * @param otherFeatureSets The other FeatureSets
     * @return a RepeatTests instance
     */
    public static RepeatTests repeat(String server, FeatureSet firstFeatureSet, FeatureSet... otherFeatureSets) {
        return repeat(server, TestMode.FULL, firstFeatureSet, otherFeatureSets);
    }

    /**
     * Get a RepeatTests instance for the given FeatureSets. The first FeatureSet will be run in LITE mode. The others will be run in the mode specified.
     *
     * @param server The server to repeat on
     * @param otherFeatureSetsTestMode The mode to run the other FeatureSets
     * @param firstFeatureSet The first FeatureSet
     * @param otherFeatureSets The other FeatureSets
     * @return a RepeatTests instance
     */
    public static RepeatTests repeat(String server, TestMode otherFeatureSetsTestMode, FeatureSet firstFeatureSet, FeatureSet... otherFeatureSets) {
        return RepeatActions.repeat(server, otherFeatureSetsTestMode, ALL_MPTEL_SETS_LIST, firstFeatureSet, Arrays.asList(otherFeatureSets));
    }

    public static RepeatTests repeatForFiles(String directory, FeatureSet firstFeatureSet, FeatureSet... otherFeatureSetsArray) {
        FeatureSet actualFirstFeatureSet = firstFeatureSet;
        List<FeatureSet> otherFeatureSets = Arrays.asList(otherFeatureSetsArray);
        List<FeatureSet> allFeatureSets = ALL_MPTEL_SETS_LIST;
        List<FeatureSet> actualOtherFeatureSets = new ArrayList<>(otherFeatureSets);

        // If the firstFeatureSet requires a Java level higher than the one we're running, try to find a suitable replacement so we don't end up not running the test at all in LITE mode
        int currentJavaLevel = JavaInfo.forCurrentVM().majorVersion();
        if (currentJavaLevel < firstFeatureSet.getMinJavaLevel().majorVersion()) {

            // Find the newest feature set that's in otherFeatureSets and is compatible with the current java version
            Optional<FeatureSet> newestSupportedSet = allFeatureSets.stream()
                                                                    .filter(s -> actualOtherFeatureSets.contains(s))
                                                                    .filter(s -> s.getMinJavaLevel().majorVersion() <= currentJavaLevel)
                                                                    .findFirst();

            if (newestSupportedSet.isPresent()) {
                actualFirstFeatureSet = newestSupportedSet.get();
                actualOtherFeatureSets.remove(actualFirstFeatureSet);
            }
        }
        RepeatTests r = RepeatTests.with(RepeatActions.forFeatureSet(allFeatureSets, actualFirstFeatureSet, null, TestMode.LITE).forServerConfigPaths(directory));
        for (FeatureSet other : actualOtherFeatureSets) {
            r = r.andWith(RepeatActions.forFeatureSet(allFeatureSets, other, null, TestMode.FULL).forServerConfigPaths(directory));
        }
        return r;

    }

}
