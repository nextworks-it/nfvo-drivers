package it.nextworks.nfvmano.nfvodriver;


import edu.upc.gco.slcnt.orch.nmro.driver.lcm.NmroLcmDriver;
import it.nextworks.nfvmano.nfvodriver.logging.NfvoLcmLoggingDriver;
import it.nextworks.nfvmano.nfvodriver.osm.OsmLcmDriver;
import it.nextworks.nfvmano.nfvodriver.sol5.Sol5NfvoLcmDriver;
import it.nextworks.nfvmano.nfvodriver.timeo.TimeoLcmDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class NfvoLcmServiceUtils {

    private static final Logger log = LoggerFactory.getLogger(NfvoLcmServiceUtils.class);

    @Value("${nfvo.lcm.type}")
    private String nfvoLcmType;

    @Value("${nfvo.lcm.address}")
    private String nfvoLcmAddress;

    @Value("${nfvo.lcm.username:}")
    private String nfvoLcmUsername;

    @Value("${nfvo.lcm.password:}")
    private String nfvoLcmPassword;

    @Value("${nfvo.lcm.project:}}")
    private String nfvoLcmProject;

    @Value("${nfvo.lcm.vim:}")
    private String nfvoLcmVim;


    @Value("${nfvo.lcm.notification.url}")
    private String nfvoLcmNotificationUrl;

    @Autowired
    NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager;

    @Autowired
    NfvoLcmService nfvoLcmService;

    @PostConstruct
    public void initNfvoLcmDriver() {
        log.debug("Initializing NFVO LCM driver for type: "+ nfvoLcmType);
        if (nfvoLcmType.equals("LOGGING")) {
            log.debug("Configured for NFVO LCM type: "+ nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new NfvoLcmLoggingDriver());

        }else if (nfvoLcmType.equals("TIMEO")) {
            log.debug("Configured for type: " + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new TimeoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager));
        }else if(nfvoLcmType.equals("DUMMY")){
            log.debug("Configured for type: " + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new DummyNfvoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager));

        }else if(nfvoLcmType.equals("SOL5")) {
            log.debug("Configured for type: " + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new Sol5NfvoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager, nfvoLcmNotificationUrl));
        }else if(nfvoLcmType.equals("OSM")){
            log.debug("Configured for type:" + nfvoLcmType);
            OsmLcmDriver osmLcmDriver = new OsmLcmDriver(this.nfvoLcmAddress, this.nfvoLcmUsername, this.nfvoLcmPassword,
                    this.nfvoLcmProject, this.nfvoLcmOperationPollingManager, null, UUID.fromString(this.nfvoLcmVim));
            nfvoLcmService.setNfvoLcmDriver(osmLcmDriver);
        }
        else if(nfvoLcmType.equals("NMRO")){
            log.debug("Configured for type: " + nfvoLcmType);
            NmroLcmDriver nmroLcmDriver= new NmroLcmDriver(NfvoLcmDriverType.OSM, this.nfvoLcmAddress, null, nfvoLcmOperationPollingManager);
            nfvoLcmService.setNfvoLcmDriver(nmroLcmDriver);
        }else {
            log.error("NFVO not configured!");
        }
    }
}
