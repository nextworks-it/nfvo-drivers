package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.PrometheusDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class takes care of activate/deactive the monitoring for the ns instances.
 */
public class MonitoringManager implements MonitoringManagerProviderInterface {

    private static final Logger log = LoggerFactory.getLogger(MonitoringManager.class);

    private final Map<String, NsMonitoringManager> nsMonitoringManagers = new HashMap<>(); //Key: NS_instance_ID

    //private final String monitoringUrl=null;

    //private final String grafanaUrl=null;

    //notification address
    private String manoDomain;

    private  MonitoringDriverProviderInterface prometheusDriver;

    public MonitoringManager(
            MonitoringDriverProviderInterface driver){
        //this.monitoringUrl = monitoringUrl;
        //this.grafanaUrl = grafanaUrl;
        //this.manoDomain = manoDomain;
        prometheusDriver =driver;
    }

    public void activateNsMonitoring(NsInfo nsInfo, Nsd nsd, List<VnfInfo> vnfInfoList, Map<String, String> metadata)
            throws MethodNotImplementedException, AlreadyExistingEntityException, NotExistingEntityException, FailedOperationException,
            MalformattedElementException {
        if ((nsInfo == null) || (nsd==null)) throw new MalformattedElementException("Received activate NS request with null parameters");
        String nsInstanceId = nsInfo.getNsInstanceId();
        log.debug("Activating NS monitoring for NS instance " + nsInstanceId);
        if (this.nsMonitoringManagers.containsKey(nsInstanceId))
            throw new AlreadyExistingEntityException("Monitoring already active for NS instance " + nsInstanceId + ". ");
        /*NsInfo nsInfo = nsDbWrapper.getNsInfo(nsInstanceId);
        NsMonitoringManager nsMonitoringManager = new NsMonitoringManager(
                nsInstanceId,
                nsd,
                this.nsDbWrapper,
                this.monitoringDriver,
                this.vnfmHandler,
                new NsAlertManager(
                        engine,
                        nsInstanceId,
                        nsd.getMonitoredInfo(),
                        nsd.getAutoScalingRule(),
                        monitoringDriver,
                        dispatcher
                )
        );*/
        NsMonitoringManager nsMonitoringManager = new NsMonitoringManager(
                nsInstanceId,
                nsInfo.getTenantId(),
                nsd,
                vnfInfoList,
                prometheusDriver,
                metadata);
        log.debug("Instantiated new NS Monitoring Manager for NS instance " + nsInstanceId);
        nsMonitoringManager.activateNsMonitoring(nsInfo);
        this.nsMonitoringManagers.put(nsInstanceId,nsMonitoringManager);
        log.debug("Activated monitoring for NS instance " + nsInstanceId);
    }

    public void deactivateNsMonitoring(String nsInstanceId)
            throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException,
            MalformattedElementException {
        if (nsMonitoringManagers.containsKey(nsInstanceId)) {
            log.debug("Disactivating NS monitoring for NS instance " + nsInstanceId);
            nsMonitoringManagers.get(nsInstanceId).deactivateNsMonitoring();
            log.debug("NS monitoring disactivated for NS instance " + nsInstanceId);
        } else {
            log.debug("NS monitoring not active for NS instance " + nsInstanceId + ". Nothing to do");
            return;
        }
        nsMonitoringManagers.remove(nsInstanceId);
        log.debug("Monitoring manager for NS instance " + nsInstanceId + " removed.");
    }
}
