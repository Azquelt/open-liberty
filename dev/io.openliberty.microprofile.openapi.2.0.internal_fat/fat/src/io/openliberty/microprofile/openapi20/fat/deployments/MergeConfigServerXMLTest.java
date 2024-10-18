/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package io.openliberty.microprofile.openapi20.fat.deployments;

import static com.ibm.websphere.simplicity.ShrinkHelper.DeployOptions.DISABLE_VALIDATION;
import static com.ibm.websphere.simplicity.ShrinkHelper.DeployOptions.SERVER_ONLY;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.websphere.simplicity.ShrinkHelper;
import com.ibm.websphere.simplicity.config.MpOpenAPIElement;
import com.ibm.websphere.simplicity.config.MpOpenAPIInfoElement;
import com.ibm.websphere.simplicity.config.ServerConfiguration;

import componenttest.annotation.Server;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.rules.repeater.RepeatTests;
import componenttest.topology.impl.LibertyServer;
import io.openliberty.microprofile.openapi20.fat.FATSuite;
import io.openliberty.microprofile.openapi20.fat.deployments.test1.DeploymentTestApp;
import io.openliberty.microprofile.openapi20.fat.deployments.test1.DeploymentTestResource;
import io.openliberty.microprofile.openapi20.fat.deployments.test2.DeploymentTestResource2;
import io.openliberty.microprofile.openapi20.fat.utils.OpenAPIConnection;
import io.openliberty.microprofile.openapi20.fat.utils.OpenAPITestUtil;

@RunWith(FATRunner.class)
public class MergeConfigServerXMLTest {

    private static final String SERVER_NAME = "OpenAPIMergeWithServerXMLTestServer";

    @Server(SERVER_NAME)
    public static LibertyServer server;

    @ClassRule
    public static RepeatTests r = FATSuite.repeatDefault(SERVER_NAME);

    @BeforeClass
    public static void startup() throws Exception {
        server.startServer();
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stopServer();
    }

    @After
    public void cleanup() throws Exception {
        server.deleteAllDropinApplications();
        server.removeAllInstalledAppsForValidation();
    }

    @Test
    public void testInfoConfigured() throws Exception {
        MpOpenAPIInfoElement info = new MpOpenAPIInfoElement();
        info.setTitle("test title");
        info.setVersion("3.7");
        info.setTermsOfService("http://example.org/tos");
        info.setContactName("John Smith");
        info.setContactUrl("http://example.org/contact");
        info.setLicenseName("Apache 2.0");
        info.setLicenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html");

        setMergeConfig(list("all"), null, info);

        WebArchive war1 = ShrinkWrap.create(WebArchive.class, "test1.war")
                                    .addClasses(DeploymentTestApp.class, DeploymentTestResource.class);
        deployApp(war1);

        WebArchive war2 = ShrinkWrap.create(WebArchive.class, "test2.war")
                                    .addClasses(DeploymentTestApp.class, DeploymentTestResource2.class);
        deployApp(war2);

        String doc = OpenAPIConnection.openAPIDocsConnection(server, false).download();
        JsonNode openapiNode = OpenAPITestUtil.readYamlTree(doc);

        JsonNode expectedInfo = new ObjectMapper().readTree("{"
                                                            + "\"title\": \"test title\","
                                                            + "\"termsOfService\": \"http://example.org/tos\","
                                                            + "\"contact\": {"
                                                            + "\"name\": \"John Smith\","
                                                            + "\"url\": \"http://example.org/contact\""
                                                            + "},"
                                                            + "\"license\": {"
                                                            + "\"name\": \"Apache 2.0\","
                                                            + "\"url\": \"https://www.apache.org/licenses/LICENSE-2.0.html\""
                                                            + "},"
                                                            + "\"version\": \"3.7\""
                                                            + "}");

        assertEquals(expectedInfo, openapiNode.path("info"));
    }

    @Test
    public void testInfoNoTitle() throws Exception {
        MpOpenAPIInfoElement info = new MpOpenAPIInfoElement();
        info.setVersion("1.0");

        setMergeConfig(list("all"), null, info);

        WebArchive war1 = ShrinkWrap.create(WebArchive.class, "test1.war")
                                    .addClasses(DeploymentTestApp.class, DeploymentTestResource.class);
        deployApp(war1);

        WebArchive war2 = ShrinkWrap.create(WebArchive.class, "test2.war")
                                    .addClasses(DeploymentTestApp.class, DeploymentTestResource2.class);
        deployApp(war2);

        String doc = OpenAPIConnection.openAPIDocsConnection(server, false).download();
        JsonNode openapiNode = OpenAPITestUtil.readYamlTree(doc);

        server.findStringsInLogsUsingMark("CWWKO1664W:.*The title and version properties must be set.*1.0",
                                          server.getDefaultLogFile());
        assertEquals("Document title", "Generated API", openapiNode.path("info").path("title").asText());
        assertEquals("Version", "1.0", openapiNode.path("info").path("version").asText());
    }

    @Test
    public void testInfoConfiguredOneApp() throws Exception {
        MpOpenAPIInfoElement info = new MpOpenAPIInfoElement();
        info.setTitle("test title");
        info.setVersion("3.7");

        setMergeConfig(list("all"), null, info);

        WebArchive war1 = ShrinkWrap.create(WebArchive.class, "test1.war")
                                    .addClasses(DeploymentTestApp.class, DeploymentTestResource.class);
        deployApp(war1);

        String doc = OpenAPIConnection.openAPIDocsConnection(server, false).download();
        JsonNode openapiNode = OpenAPITestUtil.readYamlTree(doc);

        JsonNode expectedInfo = new ObjectMapper().readTree("{"
                                                            + "\"title\": \"test title\","
                                                            + "\"version\": \"3.7\""
                                                            + "}");

        assertEquals(expectedInfo, openapiNode.path("info"));
    }

    private void setMergeConfig(List<String> included, List<String> excluded, MpOpenAPIInfoElement info) throws Exception {
        ServerConfiguration config = server.getServerConfiguration();
        MpOpenAPIElement mpOpenAPI = config.getMpOpenAPIElement();

        List<String> includedApplications = mpOpenAPI.getIncludedApplications();
        includedApplications.clear();
        includedApplications.addAll(applications(included));

        List<String> includedModules = mpOpenAPI.getIncludedModules();
        includedModules.clear();
        includedModules.addAll(modules(included));

        List<String> excludedApplications = mpOpenAPI.getExcludedApplications();
        excludedApplications.clear();
        excludedApplications.addAll(applications(excluded));

        List<String> excludedModules = mpOpenAPI.getExcludedModules();
        excludedModules.clear();
        excludedModules.addAll(modules(excluded));

        mpOpenAPI.setInfo(info);

        server.setMarkToEndOfLog();
        server.updateServerConfiguration(config);
        server.waitForConfigUpdateInLogUsingMark(null);
    }

    private static List<String> list(String... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    private static List<String> applications(List<String> values) {
        if (values == null) {
            return Collections.emptyList();
        }
        return values.stream()
                     .filter(v -> !v.contains("/"))
                     .collect(Collectors.toList());
    }

    private static List<String> modules(List<String> values) {
        if (values == null) {
            return Collections.emptyList();
        }
        return values.stream()
                     .filter(v -> v.contains("/"))
                     .collect(Collectors.toList());
    }

    private void deployApp(Archive<?> app) throws Exception {
        deployApp(app, getInstalledName(app.getName()));
    }

    private void deployApp(Archive<?> archive, String installedName) throws Exception {
        // Manually add for validation with the correct installed name
        ShrinkHelper.exportDropinAppToServer(server, archive, SERVER_ONLY, DISABLE_VALIDATION);
        server.addInstalledAppForValidation(installedName);
    }

    private void removeApp(Archive<?> app) throws Exception {
        removeApp(app, getInstalledName(app.getName()));
    }

    private void removeApp(Archive<?> app, String installedName) throws Exception {
        server.setMarkToEndOfLog();
        server.deleteFileFromLibertyServerRoot("dropins/" + app.getName());
        server.removeInstalledAppForValidation(installedName);
    }

    private String getInstalledName(String archiveName) {
        return archiveName.endsWith(".war") || archiveName.endsWith(".ear") ? archiveName.substring(0, archiveName.length() - 4) : archiveName;
    }
}
