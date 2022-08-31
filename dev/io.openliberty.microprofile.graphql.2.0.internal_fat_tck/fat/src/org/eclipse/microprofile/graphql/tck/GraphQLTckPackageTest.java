/*******************************************************************************
 * Copyright (c) 2021, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.microprofile.graphql.tck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import componenttest.annotation.AllowedFFDC;
import componenttest.annotation.Server;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.custom.junit.runner.Mode.TestMode;
import componenttest.rules.repeater.FeatureSet;
import componenttest.rules.repeater.MicroProfileActions;
import componenttest.rules.repeater.RepeatActions;
import componenttest.rules.repeater.RepeatTests;
import componenttest.topology.impl.LibertyServer;
import componenttest.topology.utils.MvnUtils;

/**
 * This is a test class that runs a whole Maven TCK as one test FAT test.
 * There is a detailed output on specific
 */
@RunWith(FATRunner.class)
public class GraphQLTckPackageTest {

    private static final String SERVER_NAME = "FATServer";

    public static final FeatureSet MP50_GRAPHQL = MicroProfileActions.MP50.addFeature("mpGraphQL-2.0")
            .build(MicroProfileActions.MP50_ID + "_GraphQL_20");

    public static final FeatureSet MP60_GRAPHQL = MicroProfileActions.MP60.addFeature("mpGraphQL-2.0")
            .build(MicroProfileActions.MP60_ID + "_GraphQL_20");

    public static final Set<FeatureSet> ALL;
    static {
        ALL = new HashSet<>(MicroProfileActions.ALL);
        ALL.add(MP50_GRAPHQL);
        ALL.add(MP60_GRAPHQL);
    }

    @ClassRule
    public static RepeatTests r = RepeatActions.repeat(SERVER_NAME, TestMode.LITE, ALL, MP50_GRAPHQL, MP60_GRAPHQL);

    @Server(SERVER_NAME)
    public static LibertyServer server;

    @BeforeClass
    public static void setUp() throws Exception {
        server.startServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (server != null) {
        	server.stopServer("CWNEN0047W", "CWNEN0049W", "CWWKZ0014W");
        }
    }

    @Test
    @AllowedFFDC // The tested deployment exceptions cause FFDC so we have to allow for this.
    public void testGraphQLTck() throws Exception {
        MvnUtils.runTCKMvnCmd(server, "io.openliberty.microprofile.graphql.2.0.internal_fat_tck", this.getClass() + ":testGraphQLTck");
        Map<String, String> resultInfo = MvnUtils.getResultInfo(server);
        resultInfo.put("results_type", "MicroProfile");
        resultInfo.put("feature_name", "GraphQL");
        resultInfo.put("feature_version", "2.0");
        MvnUtils.preparePublicationFile(resultInfo);
    }
}