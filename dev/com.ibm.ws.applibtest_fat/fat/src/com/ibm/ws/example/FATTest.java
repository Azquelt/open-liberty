/**
 *
 */
package com.ibm.ws.example;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.example.testlib.TheAdder;
import com.ibm.websphere.simplicity.ShrinkHelper;
import com.ibm.websphere.simplicity.ShrinkHelper.DeployOptions;

import componenttest.annotation.Server;
import componenttest.annotation.TestServlet;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.topology.impl.LibertyServer;

/**
 *
 */
@RunWith(FATRunner.class)
public class FATTest {

    private final static String APP_NAME = "appLibTest";

    @TestServlet(contextRoot = APP_NAME, servlet = AppLibTestServlet.class)
    @Server("FATServer")
    public static LibertyServer server;

    @BeforeClass
    public static void setup() throws Exception {
//        File jarFile = new File("files/com.ibm.ws.applibtest.testlib.jar");
//        JavaArchive jar = ShrinkWrap.createFromZipFile(JavaArchive.class, jarFile);
        WebArchive war = ShrinkWrap.create(WebArchive.class, APP_NAME + ".war")
                        .addClass(AppLibTestServlet.class)
                        .addClass(TheAdder.class);

        ShrinkHelper.exportDropinAppToServer(server, war, DeployOptions.SERVER_ONLY);

        server.startServer();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        server.stopServer();
    }

}
