package it.nextworks.nfvmano.nfvodriver;


import it.nextworks.nfvmano.nfvodriver.elicensing.DummyElicenseManager;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicenseManagementDriver;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicenseManagementProviderInterface;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicensingService;
import it.nextworks.nfvmano.nfvodriver.logging.NfvoLcmLoggingDriver;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringManager;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringManagerProviderInterface;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.MdaDriver;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.PrometheusDriver;
import it.nextworks.nfvmano.nfvodriver.osm.OsmLcmDriver;
import it.nextworks.nfvmano.nfvodriver.osm10.Osm10LcmDriver;
import it.nextworks.nfvmano.nfvodriver.sol5.Sol5NfvoLcmDriver;
import it.nextworks.nfvmano.nfvodriver.test.AimlNfvoLcmDriver;
import it.nextworks.nfvmano.nfvodriver.test.EvsTestNfvoLcmDriver;
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

    @Value("${domain_id:local}")
    private String domainId;

    @Value("${nfvo.lcm.external_monitoring.enable:false}")
    private boolean enableExternalMonitoring;

    @Value("${nfvo.lcm.external_monitoring.type:PROMETHEUS}")
    private String externalMonitoringType;

    @Value("${nfvo.lcm.external_monitoring.address:http://localhost}")
    private String externalMonitoringAddress;

    @Value("${nfvo.lcm.elicensing.enable:false}")
    private boolean enableElicensing;

    @Value("${nfvo.lcm.elicensing.type:DUMMY}")
    private String elicensingType;

    @Value("${nfvo.lcm.elicensing.address:http://localhost:8085}")
    private String elicensingAddress;


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


    //Using default value from OkHttpClient
    @Value("${nfvo.lcm.timeout:10000}")
    private int nfvoLcmTimeout;

    @Value("${nfvo.lcm.notification.url}")
    private String nfvoLcmNotificationUrl;


    @Value("${nfvo.lcm.monitoring.url}")
    private String nfvoLcmMonitoringnUrl;
    @Autowired
    NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager;

    @Autowired
    NfvoLcmService nfvoLcmService;

    @Autowired
    NfvoCatalogueService nfvoCatalogueService;

    @Autowired
    private ElicensingService elicensingService;

    @PostConstruct
    public void initNfvoLcmDriver() {
        log.debug("Initializing NFVO LCM driver for type:"+ nfvoLcmType);
        ElicenseManagementProviderInterface eLicenseMgr = null;
        if(enableElicensing){
            log.debug("Configuring external licensing manager:"+elicensingType);
            if(elicensingType.equals("DUMMY")){
                eLicenseMgr = new DummyElicenseManager();
            }else if(elicensingType.equals("ELMA")){
                eLicenseMgr = new ElicenseManagementDriver(elicensingAddress, domainId, elicensingService);
            }else log.error("Unknown  elicensing type, not configured");

        }else  log.debug("NFVO elicensing disabled");

        MonitoringManager monitoringMgr = null;
        if(enableExternalMonitoring){
            log.debug("Configuring external monitoring:"+externalMonitoringType);
            if(externalMonitoringType.equals("PROMETHEUS")){

                monitoringMgr = new MonitoringManager(new PrometheusDriver(
                        externalMonitoringAddress+":8989/prom-manager",
                        externalMonitoringAddress+":3000",nfvoLcmAddress));
            }else if(externalMonitoringType.equals("MDA")){
                monitoringMgr = new MonitoringManager(new MdaDriver(
                        externalMonitoringAddress, domainId, nfvoLcmAddress, nfvoLcmMonitoringnUrl));
            }else log.error("Unknown  external monitoring type, not configured");
        }

        if (nfvoLcmType.equals("LOGGING")) {

            log.debug("Configured for NFVO LCM type:"+ nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new NfvoLcmLoggingDriver());

        }else if (nfvoLcmType.equals("TIMEO")) {
            log.debug("Configured for type:" + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new TimeoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager));
        }else if(nfvoLcmType.equals("DUMMY")){
            log.debug("Configured for type:" + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new DummyNfvoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager, eLicenseMgr, monitoringMgr, nfvoCatalogueService));

        }else if(nfvoLcmType.equals("SOL5")) {
            log.debug("Configured for type:" + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new Sol5NfvoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager, nfvoLcmNotificationUrl, nfvoLcmTimeout));
        }else if(nfvoLcmType.equals("OSM")){
            log.debug("Configured for type:" + nfvoLcmType);
            OsmLcmDriver osmLcmDriver = new OsmLcmDriver(this.nfvoLcmAddress, this.nfvoLcmUsername, this.nfvoLcmPassword,
                    this.nfvoLcmProject, this.nfvoLcmOperationPollingManager, null, UUID.fromString(this.nfvoLcmVim),
                    this.nfvoCatalogueService, monitoringMgr, eLicenseMgr);
            nfvoLcmService.setNfvoLcmDriver(osmLcmDriver);
        }else if(nfvoLcmType.equals("EVS_TEST")){
            log.debug("Configured for type:" + nfvoLcmType);
            nfvoLcmService.setNfvoLcmDriver(new EvsTestNfvoLcmDriver(nfvoLcmAddress, null, nfvoLcmOperationPollingManager));
	} else if(nfvoLcmType.equals("OSM10")){
                log.debug("Configured for type:" + nfvoLcmType);
                Osm10LcmDriver osmLcmDriver =
                        new Osm10LcmDriver(this.nfvoLcmAddress, null, this.nfvoLcmOperationPollingManager, nfvoLcmNotificationUrl,0,
                                true, nfvoLcmUsername, nfvoLcmPassword, nfvoLcmProject, nfvoLcmVim, nfvoCatalogueService, monitoringMgr, eLicenseMgr);
                nfvoLcmService.setNfvoLcmDriver(osmLcmDriver);
        } else {
            log.error("NFVO not configured!");
        }
    }
}
