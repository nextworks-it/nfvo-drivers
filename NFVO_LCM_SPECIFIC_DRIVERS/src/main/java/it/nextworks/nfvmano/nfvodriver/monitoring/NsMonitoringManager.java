package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.elements.MonitoringParameter;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.MonitoredData;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.VnfIndicatorData;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.ObjectSelection;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.MonitoringObjectType;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.CreatePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobResponse;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.PrometheusDriver;
import it.nextworks.nfvmano.nfvodriver.monitoring.elements.MonitoringGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manage the monitoring for a specific NS instance. It talks with PrometheusDriver
 * in order to configure the monioring job and dashboard on Grafana.
 */

public class NsMonitoringManager {

    private static final Logger log = LoggerFactory.getLogger(NsMonitoringManager.class);

    private final String nsInstanceId;

    private final String tenantId;

    private final Nsd nsd;

    private final List<VnfInfo> vnfInfoList;

    private final MonitoringDriverProviderInterface prometheusDriver;

    //Key: pm job ID; Value: Monitoring Parameter ID - This is for pm jobs associated to the NSD monitoring parameters
    private final Map<String, String> pmJobIdToMpIdMap = new HashMap<>();

    //Key: Monitoring Parameter ID; Value: pm job ID - This is for pm jobs associated to the NSD monitoring parameters
    private final Map<String, String> mpIdToPmJobIdMap = new HashMap<>();

    //This list includes the pm jobs associated to NSD monitoring parameters or the ones requested out of the NS instance management
    private final List<String> pmJobIds = new ArrayList<>();

    private String monitoringGuiUrl = null;

    private MonitoringGui monitoringGui;

    public NsMonitoringManager(String nsInstanceId,
                               String tenantId,
                               Nsd nsd,
                               List<VnfInfo> vnfInfoList,
                               MonitoringDriverProviderInterface prometheusDriver){
        this.nsInstanceId = nsInstanceId;
        this.nsd = nsd;
        this.vnfInfoList = vnfInfoList;
        this.prometheusDriver = prometheusDriver;
        this.tenantId = tenantId;
    }

    public String createPmJob(CreatePmJobRequest request, VnfInfo vnfInfo)
            throws MethodNotImplementedException, FailedOperationException, MalformattedElementException {
        //VnfInfo will contains ip infos
        String pmJobId = prometheusDriver.createPmJob(request, vnfInfo);
        pmJobIds.add(pmJobId);
        return pmJobId;
    }

    public DeletePmJobResponse deletePmJob(DeletePmJobRequest request) throws MethodNotImplementedException,
            FailedOperationException, MalformattedElementException, NotExistingEntityException {
        DeletePmJobResponse response = prometheusDriver.deletePmJob(request);
        pmJobIds.remove(request.getPmJobId().get(0));
        return response;
    }

    public void activateNsMonitoring(NsInfo nsInfo) throws FailedOperationException {
        log.debug("Starting monitoring activation for NS instance " + nsInstanceId + ". Reading NSD.");
        if ((nsd.getMonitoredInfo() == null) || (nsd.getMonitoredInfo().isEmpty())) {
            log.debug("No monitored info specified in the NSD " + nsd.getNsdName() + " for NS instance " + nsInstanceId + ". Nothing to do.");
            return;
        }
        List<MonitoredData> monitoredData = nsd.getMonitoredInfo();
        for (MonitoredData md : monitoredData) {
            try {
                if (md.getVnfIndicatorInfo() != null)
                    startMonitoringJobForVnfIndicator(md.getVnfIndicatorInfo(), nsInfo);
                if (md.getMonitoringParameter() != null)
                    startMonitoringJobForMonitoringParameter(md, nsInfo);
            } catch (Exception e) {
                log.error("Error while starting a monitoring job: " + e.getMessage() + ". Skipping it.", e);
                log.error(e.getMessage());
            }
        }
        log.debug("Finished creation of monitoring jobs for NS instance " + nsInstanceId);

		log.debug("Starting building monitoring dashboard for NS instance " + nsInstanceId);
		buildMonitoringDashboard();
		if(monitoringGui!=null){
		    monitoringGuiUrl = monitoringGui.getUrl();
        }
		/*
		try {
			nsDbWrapper.setNsInfoMonitoringUrl(nsInstanceId, url);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set URL for NS instance " + nsInstanceId + e.getMessage());
		}*/
		log.debug("Finished creation of monitoring dashboard for NS instance " + nsInstanceId);
		/*
		ALERT functionalities
		try {
			alertManager.createThresholds(mpIdToPmJobIdMap);
		} catch (MalformattedElementException exc) {
			log.error("Malformatted element in creation of thresholds");
			log.debug("Details:", exc);
		}
        */
    }

    /**
     * This method de-activates all the existing Performance Monitoring jobs
     *
     * @throws MethodNotImplementedException if the method is not implemented
     * @throws FailedOperationException if the operation fails
     */
    public void deactivateNsMonitoring() {
        log.debug("Disactivating NS monitoring for NS instance " + nsInstanceId);
        //alertManager.deleteThresholds();
        log.debug("Removing monitoring GUI");
        if (monitoringGui != null) {
            try {
                removeMonitoringDashboard();
                monitoringGui = null;
            } catch (FailedOperationException e) {
                log.debug("Monitoring dashboard removal failed. Proceeding with removing pm jobs.");
            }
            //nsDbWrapper.setNsInfoMonitoringUrl(nsInstanceId, null);
            monitoringGuiUrl = null;
            log.debug("Monitoring URL removed from NS info for NS instance " + nsInstanceId);
            log.debug("Monitoring GUI removed.");
        } else {
            log.debug("Monitoring GUI not available. Nothing to remove.");
        }
        //deleting job
        List<String> toBeRemoved = new ArrayList<>();
        for (String pmJobId : pmJobIds) toBeRemoved.add(pmJobId);

        for (String pmJobId : toBeRemoved) {
            log.debug("Removing pm job with ID " + pmJobId);
            List<String> pms = new ArrayList<>();
            pms.add(pmJobId);
            DeletePmJobRequest deleteRequest = new DeletePmJobRequest(pms);
            try {
                DeletePmJobResponse response = deletePmJob(deleteRequest);
                if (response.getDeletedPmJobId().get(0).equals(pmJobId))
                    log.error("Impossible to delete PM job " + pmJobId + ". Skipping it.");
                if (pmJobIdToMpIdMap.containsKey(pmJobId)) {
                    log.debug("PM job associated to NS monitoring parameter. Removing from internal maps.");
                    String mpId = pmJobIdToMpIdMap.get(pmJobId);
                    mpIdToPmJobIdMap.remove(mpId);
                    pmJobIdToMpIdMap.remove(pmJobId);
                    log.debug("PM job removed from internal maps.");
                }
                log.debug("PM job " + pmJobId + " deleted");
            } catch (Exception e) {
                log.debug("Failed to delete PM job " + pmJobId);
            }
        }
    }

    /**
     * This method creates a monitoring job to collect a VNF indicator
     *
     * @param vnfIndicator VNF indicator to be collected
     * @param nsInfo information about the NS instance for which the VNF indicator must be collected
     * @throws MethodNotImplementedException if the method is not implemented
     */
    private void startMonitoringJobForVnfIndicator(VnfIndicatorData vnfIndicator, NsInfo nsInfo)
            throws MethodNotImplementedException {
        log.error("VNF indicators not yet supported.");
        throw new MethodNotImplementedException("Support for monitoring VNF indicators not yet available");
    }

    /**
     * This method creates a monitoring job to collect a monitoring parameter
     *
     * @param mp monitoring parameter to be collected
     * @param nsInfo information about the NS instance for which the monitoring parameter must be collected
     * @throws MethodNotImplementedException if the method is not implemented
     */
    private void startMonitoringJobForMonitoringParameter (MonitoredData md, NsInfo nsInfo)
            throws MethodNotImplementedException, MalformattedElementException, NotExistingEntityException, FailedOperationException {
        MonitoringParameter mp = md.getMonitoringParameter();
        String mpId = mp.getMonitoringParameterId();
        String mpName = mp.getName();
        String mpMetric = mp.getPerformanceMetric();
        log.debug("Starting monitoring job for monitoring parameter " + mpName + " with ID " + mpId + " for metric " + mpMetric);

        String [] splits = mpMetric.split("\\.");
        //TODO: This covers just a limited range of cases. To be extended.
        //Supported items:
        //VcpuUsageMean.<vnfdId>
        //VmemoryUsageMean.<vnfdId>
        //VdiskUsageMean.<vnfdId>
        //ByteIncoming.<vnfdId>.<vnfExtCpdId> --> not yet supported
        if (splits.length != 2) throw new MalformattedElementException("Wrong metric format. Performance metric in monitoring parameter should be in format <metricType>.<vnfdId>");
        String metricType = splits[0];
        String vnfdId = splits[1];
        log.debug("Monitoring metric successfully parsed. Metric type: " + metricType + " - VNFD ID: " + vnfdId);
        Map<String,String> vnfInfoVnfdIdMap = nsInfo.getVnfInfoVnfdIdMap();
        vnfdId = getEffectiveVnfdId(vnfInfoVnfdIdMap,vnfdId);
        List<String> vnfInfoIds = nsInfo.getVnfInfoIdFromVnfdId(vnfdId);
        if (vnfInfoIds.size() == 0) throw new NotExistingEntityException("VNF info for VNFD " + vnfdId + " not found.");
        if (vnfInfoIds.size() > 1) throw new MethodNotImplementedException("Found multiple VNF infos for the given VNFD in the NS info. Monitoring for multiple VNFs with same type not yet supported.");
        //this is the uuid of the vnf instance for which are specified monitoring parameters
        String vnfInfoId = vnfInfoIds.get(0);

        ObjectSelection vnfSelector = new ObjectSelection();
        //TODO: this must be updated
        if ( (metricType.equals("VcpuUsageMean")) || (metricType.equals("VmemoryUsageMean")) || (metricType.equals("VdiskUsageMean"))
                || (metricType.equals("CurrentClientConnections")) || (metricType.equals("CurrentActiveClientConnections")) || (metricType.equals("UserAgentCurrentConnectionsCount"))
                || (metricType.equals("CompletedRequests")) || (metricType.equals("TotalHits")) || (metricType.equals("CacheRamUsed"))) {
            List<MonitoringObjectType> objectType = new ArrayList<>();
            List<String> objectInstanceId = new ArrayList<>();
            objectType.add(MonitoringObjectType.VNF);
            objectInstanceId.add(vnfInfoId);
            vnfSelector = new ObjectSelection(objectType, null, objectInstanceId);
        } else if (metricType.equals("ByteIncoming")) {
            throw new MethodNotImplementedException("ByteIncoming metric not yet supported");
        } else throw new MalformattedElementException(metricType + " not supported");
        log.debug("Built target object to be monitored.");

        List<String> performanceMetric = new ArrayList<>();
        performanceMetric.add(metricType);
        //workaround to provide NSD name and NS ID
        List<String> performanceMetricGroup = new ArrayList<>();
        //TODO validate this
        VnfInfo vnfInfo = getVnfInfoByInfoId(vnfInfoList,vnfInfoId);
        if(vnfInfo != null) performanceMetricGroup.add(vnfInfo.getVnfdId());
        performanceMetricGroup.add(nsInstanceId);
        if(nsInfo.getConfigurationParameters()!=null && nsInfo.getConfigurationParameters().containsKey("product_id")){
            performanceMetricGroup.add(nsInfo.getConfigurationParameters().get("product_id"));
        }
        CreatePmJobRequest pmJobRequest = new CreatePmJobRequest(null,	//NS selector
                null, 													//resource selector
                vnfSelector,											//VNF selector
                performanceMetric, 										//performance metric
                performanceMetricGroup,					 				//performance metric group
                0, 														//collection period
                0, 														//reporting period
                null,                                   //reporting boundary
                mp.getParams());
        String pmJobId = createPmJob(pmJobRequest,vnfInfo);
        log.debug("Created PM job with ID " + pmJobId + " for monitoring parameter " + mpId);
        this.pmJobIdToMpIdMap.put(pmJobId, mpId);
        this.mpIdToPmJobIdMap.put(mpId, pmJobId);
        log.debug("Updated internal maps with PM job and MP IDs");
    }

    /**
     * Scan over the Vnf Info List in order to obtain the Vnf Info
     * associated to this vnfInfoId
     * @param vnfInfoList
     * @param vnfInfoId
     * @return VnfInfo
     */
    private VnfInfo getVnfInfoByInfoId(List<VnfInfo> vnfInfoList, String vnfInfoId) {
        for(VnfInfo vnfInfo : vnfInfoList){
            if(vnfInfo.getVnfInstanceId().equals(vnfInfoId))
                return vnfInfo;
        }
        return null;
    }

    /**
     * This method returns the effective osm vnfd package id
     * used by this ns. This is needed because the vnfdId parameter
     * taken from ifa nsd doesn't contain the information on which
     * osm vnfd package (that is in the form vnfdId_DfId) constitutes this ns.
     *
     * @param vnfInfoVnfdIdMap
     * @param vnfdId
     * @return String
     */
    private String getEffectiveVnfdId(Map<String, String> vnfInfoVnfdIdMap, String vnfdId) {
        for (Map.Entry<String, String> e : vnfInfoVnfdIdMap.entrySet()) {
            if (e.getValue().contains(vnfdId)) return e.getValue();
        }
        return null;
    }

    private void buildMonitoringDashboard() throws FailedOperationException {
        //reminder this nsInstanceId is the nsInstanceId of osm
        log.debug("Building monitoring dashboard for NS " + nsInstanceId);
        try {
            //String tenantId = nsDbWrapper.getNsInfo(nsInstanceId).getTenantId();
            Map<String, String> metadata = new HashMap<>();
            metadata.put("NSD_ID", nsd.getNsdName());
            metadata.put("NS_ID", nsInstanceId);
            //MonitoringGui mg = monitoringDriver.buildMonitoringGui(pmJobIds, new Tenant(tenantId, null), metadata);
            MonitoringGui mg = prometheusDriver.buildMonitoringGui(pmJobIds, tenantId, metadata);
            log.debug("Built monitoring GUI");
            monitoringGui = mg;
            log.debug("Stored info about monitoring GUI");
        } catch (Exception e) {
            log.error("Error while building monitoring dashboard: " + e.getMessage());
            throw new FailedOperationException("Error while building monitoring dashboard: " + e.getMessage());
        }
    }

    private void removeMonitoringDashboard() throws FailedOperationException {
        log.debug("Removing monitoring dashboard for NS " + nsInstanceId);
        String mgId = monitoringGui.getGuiId();
        log.debug("Removing monitoring dashboard with ID " + mgId);
        try {
            prometheusDriver.removeMonitoringGui(mgId);
        } catch (Exception e) {
            log.debug("Failed monitoring GUI removal on monitoring driver: " + e.getMessage());
            throw new FailedOperationException("Failed monitoring GUI removal on monitoring driver: " + e.getMessage());
        }
    }
}
