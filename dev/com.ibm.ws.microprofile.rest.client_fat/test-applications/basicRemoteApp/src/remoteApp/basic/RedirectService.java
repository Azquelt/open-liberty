/**
 * 
 */
package remoteApp.basic;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/redirect")
public class RedirectService {

    @Path("/{status}")
    @GET
    public Response getRedirected(@PathParam("status") int status) {
        if (status < 300 || status >= 400) {
            throw new RuntimeException("Invalid redirect status code");
        }
        return Response.status(status)
                        .location(URI.create("/basicRemoteApp/redirect/target"))
                        .build();
    }

    @Path("target")
    public String getRedirectTarget() {
        return "target";
    }

}
