package it.nextworks.nfvmano.nfvodriver.elicensing;

import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;

import java.util.Map;

public interface ElicenseManagementProviderInterface {


    public void activateElicenseManagement(Map<String, String> metadata) throws FailedOperationException;
    public void terminateElicenseManagement() throws FailedOperationException;
}
