/**
 * 
 */
package mpRestClient10.handleresponses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.hamcrest.Matchers;

/**
 * Dummy rest resource
 */
@Path("/")
@ApplicationPath("/HandleResponses")
public class HandleResponsesResource extends Application {
    
    private RestClientBuilder builder;
    
    @PostConstruct
    public void init() {
        String baseUrlStr = "https://localhost:" + System.getProperty("bvt.prop.HTTP_secondary.secure") + "/basicRemoteApp";
        URL baseUrl;
        try {
            baseUrl = new URL(baseUrlStr);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        builder = RestClientBuilder.newBuilder()
                        .register(UnknownWidgetExceptionMapper.class)
                        .property("com.ibm.ws.jaxrs.client.ssl.config", "mySSLConfig")
                        .property("com.ibm.ws.jaxrs.client.receive.timeout", "120000")
                        .property("com.ibm.ws.jaxrs.client.connection.timeout", "120000")
                        .baseUrl(baseUrl);
    }
    
    @GET
    public String runTest() {
        try {
            Response r = builder.build(RedirectClient.class).getRedirected(307);
            assertEquals(307, r.getStatus());
            assertThat(r.getHeaderString("Location"), Matchers.endsWith("/redirect/target"));
        } catch (Throwable t) {
            return "Exception: " + t.toString();
        }
        return "OK";
    }
}
