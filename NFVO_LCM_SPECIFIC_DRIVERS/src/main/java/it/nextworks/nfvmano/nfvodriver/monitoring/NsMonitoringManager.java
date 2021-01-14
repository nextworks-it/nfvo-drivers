package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.elements.MonitoringParameter;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.MonitoredData;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.VnfIndicatorData;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.PrometheusDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NsMonitoringManager {

    private static final Logger log = LoggerFactory.getLogger(NsMonitoringManager.class);

    private String nsInstanceId;
    private VnfInfo vnfInfo;
    private Nsd nsd;
    private PrometheusDriver prometheusDriver;

    public NsMonitoringManager(String nsInstanceId,Nsd nsd, PrometheusDriver prometheusDriver){
        this.nsInstanceId = nsInstanceId;
        this.nsd = nsd;
        this.prometheusDriver = prometheusDriver;
    }

    public void activateNsMonitoring(NsInfo nsInfo) {
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
                    startMonitoringJobForMonitoringParameter(md.getMonitoringParameter(), nsInfo);
            } catch (Exception e) {
                log.error("Error while starting a monitoring job: " + e.getMessage() + ". Skipping it.");
                log.error(e.getMessage());
            }
        }
        log.debug("Finished creation of monitoring jobs for NS instance " + nsInstanceId);
        /*
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
					startMonitoringJobForMonitoringParameter(md.getMonitoringParameter(), nsInfo);
			} catch (Exception e) {
				log.error("Error while starting a monitoring job: " + e.getMessage() + ". Skipping it.");
				log.error(e.getMessage());
			}
		}
		log.debug("Finished creation of monitoring jobs for NS instance " + nsInstanceId);
		log.debug("Starting building monitoring dashboard for NS instance " + nsInstanceId);
		buildMonitoringDashboard();
		String url = monitoringGui.getUrl();
		try {
			nsDbWrapper.setNsInfoMonitoringUrl(nsInstanceId, url);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set URL for NS instance " + nsInstanceId + e.getMessage());
		}
		log.debug("Finished creation of monitoring dashboard for NS instance " + nsInstanceId);
		try {
			alertManager.createThresholds(mpIdToPmJobIdMap);
		} catch (MalformattedElementException exc) {
			log.error("Malformatted element in creation of thresholds");
			log.debug("Details:", exc);
		}
         */
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
    private void startMonitoringJobForMonitoringParameter (MonitoringParameter mp, NsInfo nsInfo)
            throws MethodNotImplementedException, MalformattedElementException, NotExistingEntityException, FailedOperationException {
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

        List<String> vnfInfoIds = nsInfo.getVnfInfoIdFromVnfdId(vnfdId);
        if (vnfInfoIds.size() == 0) throw new NotExistingEntityException("VNF info for VNFD " + vnfdId + " not found.");
        if (vnfInfoIds.size() > 1) throw new MethodNotImplementedException("Found multiple VNF infos for the given VNFD in the NS info. Monitoring for multiple VNFs with same type not yet supported.");
        String vnfInfoId = vnfInfoIds.get(0);

        /*ObjectSelection vnfSelector = new ObjectSelection();
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
        */
        List<String> performanceMetric = new ArrayList<>();
        performanceMetric.add(metricType);
        //workaround to provide NSD name and NS ID
        List<String> performanceMetricGroup = new ArrayList<>();
        performanceMetricGroup.add(vnfInfo.getVnfdId());
        performanceMetricGroup.add(nsInstanceId);
        /*CreatePmJobRequest pmJobRequest = new CreatePmJobRequest(null,	//NS selector
                null, 													//resource selector
                vnfSelector,											//VNF selector
                performanceMetric, 										//performance metric
                performanceMetricGroup,					 				//performance metric group
                0, 														//collection period
                0, 														//reporting period
                null);													//reporting boundary
        String pmJobId = createPmJob(pmJobRequest);
        log.debug("Created PM job with ID " + pmJobId + " for monitoring parameter " + mpId);*/

        //this.pmJobIdToMpIdMap.put(pmJobId, mpId);
        //this.mpIdToPmJobIdMap.put(mpId, pmJobId);
        log.debug("Updated internal maps with PM job and MP IDs");
    }
}
