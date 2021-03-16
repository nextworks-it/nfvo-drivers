package it.nextworks.nfvmano.nfvodriver.elicensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;

import java.util.Map;

public class DummyElicenseManager implements ElicenseManagementProviderInterface {
    private static final Logger log = LoggerFactory.getLogger(DummyElicenseManager.class);


    @Override
    public void activateElicenseManagement(Map<String, String> metadata) throws FailedOperationException {
        log.debug("Requesting elicense management creation."+metadata);
    }

    @Override
    public void terminateElicenseManagement() throws FailedOperationException {
        log.debug("Requesting elicense management termination.");
    }
}
