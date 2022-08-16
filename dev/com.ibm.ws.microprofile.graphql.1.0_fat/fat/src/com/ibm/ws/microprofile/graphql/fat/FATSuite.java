/*******************************************************************************
 * Copyright (c) 2019, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.graphql.fat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import componenttest.custom.junit.runner.Mode.TestMode;
import componenttest.rules.repeater.FeatureSet;
import componenttest.rules.repeater.MicroProfileActions;
import componenttest.rules.repeater.RepeatActions;
import componenttest.rules.repeater.RepeatTests;


@RunWith(Suite.class)
@SuiteClasses({
                BasicMutationTest.class,
                BasicQueryTest.class,
                BasicQueryWithConfigTest.class,
                DefaultValueTest.class,
                //DeprecationTest.class, // Deprecation behavior was removed from the 1.0 spec
                GraphQLInterfaceTest.class,
                IfaceTest.class,
                IgnoreTest.class,
                InputFieldsTest.class,
                JarInWarTest.class,
                MetricsTest.class,
                OutputFieldsTest.class,
                RolesAuthTest.class,
                TypesTest.class,
                UITest.class,
                VoidQueryTest.class
})
public class FATSuite {

    public static final FeatureSet MP33_GRAPHQL = MicroProfileActions.MP33
                    .addFeature("mpGraphQL-1.0")
                    .build(MicroProfileActions.MP33_ID + "_GraphQL_10");
    
    public static final FeatureSet MP40_GRAPHQL = MicroProfileActions.MP40
                    .addFeature("mpGraphQL-1.0")
                    .build(MicroProfileActions.MP40_ID + "_GraphQL_10");
    
    public static final FeatureSet MP50_GRAPHQL = MicroProfileActions.MP50
                    .addFeature("mpGraphQL-2.0")
                    .build(MicroProfileActions.MP50_ID + "_GraphQL_20");
    
    public static final FeatureSet MP60_GRAPHQL = MicroProfileActions.MP60
                    .addFeature("mpGraphQL-2.0")
                    .build(MicroProfileActions.MP60_ID + "_GraphQL_20");
    
    public static final Set<FeatureSet> ALL;
    static {
        ALL = new HashSet<>(MicroProfileActions.ALL);
        ALL.add(MP33_GRAPHQL);
        ALL.add(MP40_GRAPHQL);
        ALL.add(MP50_GRAPHQL);
        ALL.add(MP60_GRAPHQL);
    }
    
    @ClassRule
    public static RepeatTests r = RepeatActions.repeat(null, TestMode.LITE, ALL,
                                                             MP33_GRAPHQL,
                                                             MP40_GRAPHQL,
                                                             MP50_GRAPHQL,
                                                             MP60_GRAPHQL);
    
    public static void addSmallRyeGraphQLClientLibraries(WebArchive webArchive) {
        File libs = new File("publish/shared/resources/smallryeGraphQLClient/");
        webArchive.addAsLibraries(libs.listFiles());
    }
}
