package io.openliberty.microprofile.openapi20.fat.deployments.test1;

import javax.json.bind.annotation.JsonbProperty;

public class DeploymentTestResult {
    
    public String result;
    @JsonbProperty(nillable = true)
    public int error_code;

}
