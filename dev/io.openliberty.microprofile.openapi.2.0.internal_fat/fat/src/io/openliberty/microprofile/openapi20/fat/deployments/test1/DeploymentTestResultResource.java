package io.openliberty.microprofile.openapi20.fat.deployments.test1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/")
public class DeploymentTestResultResource {

    @GET
    @Path("/test")
    @Operation(summary = "test method")
    @APIResponse(responseCode = "200", description = "constant \"OK\" response")
    @Produces(value = MediaType.APPLICATION_JSON)
    public DeploymentTestResult test() {
        DeploymentTestResult result = new DeploymentTestResult();
        result.result = "OK";
        result.error_code = 0;
        return result;
    }

}
