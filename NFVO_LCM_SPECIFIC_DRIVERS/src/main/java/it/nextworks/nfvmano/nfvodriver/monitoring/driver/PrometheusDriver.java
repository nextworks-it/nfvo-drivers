package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import io.swagger.client.ApiException;
import io.swagger.client.api.ExporterApi;
import io.swagger.client.model.Endpoint;
import io.swagger.client.model.Exporter;
import io.swagger.client.model.ExporterDescription;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.ObjectSelection;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.PmJob;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.MonitoringObjectType;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.CreatePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.AbstractExporterInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.ExporterType;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PrometheusDriver {

    private static final Logger log = LoggerFactory.getLogger(PrometheusDriver.class);

    //private static final String MON_PATH = "timeo/alerts";

    private ExporterApi exporterApi;

	//private DashboardApi dashboardApi;

	//private String grafanaUrl;

	//private String timeoDomain;

	//private AlertApi alertApi;

	//key: ID of the VNF instance; Value: ID of the node exporter created for that VNF instance
	private Map<String, String> vnfInstanceToNodeExporterMap = new HashMap<>();

	//key: ID of the VNF instance; Value: ID of the Telegraf exporter created for that VNF instance
	private Map<String, String> vnfInstanceToTelegrahExporterMap = new HashMap<>();

	//key: ID of the pm job; Value: ID of the exporter associated to that pm job
	private Map<String, String> pmJobIdToExporterId = new HashMap<>();

	//key: ID of the exporter; Value: List of pm job IDs mapped on the exporter
	private Map<String, List<String>> exporterIdToPmJob = new HashMap<>();

	//key: ID of the exporter; Value: ID of the VNF
	private Map<String, String> exporterIdToVnfId = new HashMap<>();

	//key: ID of the PM job
	private Map<String, PmJob> pmJobs = new HashMap<>();

	//key: ID of the dashboard; Value: List of pm job IDs shown on the dashboard
	private Map<String, List<String>> dashboardIdToPmJobId = new HashMap<>();

	//key: ID of the pm job; Value: ID of the dashboard where the pm job is shown.
	//At the moment a pm job ID is shown on a single dashboard. Check if we need to evolve this.
	private Map<String, String> pmJobIdToDashboardId = new HashMap<>();

	//key: ID of the dashboard; Value: details of the dashboard
	//private Map<String, MonitoringGui> monitoringGui = new HashMap<>();

    public PrometheusDriver(){}


	public String createPmJob(CreatePmJobRequest request, VnfInfo vnfInfo)
			throws MethodNotImplementedException, FailedOperationException, MalformattedElementException {
		log.debug("Creating PM job in Prometheus");

		if ((request.getNsSelector() != null) || (request.getResourceSelector() != null)) {
			throw new MethodNotImplementedException("PM jobs for NS and resources not yet supported.");
		}

		ObjectSelection os = request.getVnfSelector();
		if (os == null) throw new MalformattedElementException("PM job request without object selection.");

		String metricType = request.getPerformanceMetric().get(0);

		MonitoringObjectType mot = request.getVnfSelector().getObjectType().get(0);

		String vnfInstanceId = request.getVnfSelector().getObjectInstanceId().get(0);


		String vnfdId = request.getPerformanceMetricGroup().get(0);

		String nsInstanceId = request.getPerformanceMetricGroup().get(1);


		log.debug("PM job parameters - Metric type: " + metricType + " - Monitoring object type: " + mot + " - VNF ID: " + vnfInstanceId + " - VNFD ID: " + vnfdId + " - NS Instance ID: " + nsInstanceId);

		try {
			AbstractExporterInfo exporterInfo = PrometheusMapper.readPrometheusExporterInfo(mot, metricType);

			if (exporterInfo == null) throw new FailedOperationException("Impossible to determine Prometheus exporter");

			if (exporterInfo.getType() == ExporterType.NODE_EXPORTER) {
				log.debug("The PM job requires a node exporter");
				if (vnfInstanceToNodeExporterMap.containsKey(vnfInstanceId)) {
					String exporterId = vnfInstanceToNodeExporterMap.get(vnfInstanceId);
					log.debug("Node exporter for VNF " + vnfInstanceId + " already existing. No need to create a new one. Exporter ID: " + exporterId);
					PmJob pmJob = buildPmJob(os, metricType, request.getCollectionPeriod(), request.getReportingPeriod());
					storePmJobInfo(pmJob, exporterId, null);
					return pmJob.getPmJobId();
				} else {
					log.debug("A new node exporter is required.");
					String vnfIpAddress = vnfInfo.getMetadata().get("IP");
					String exporterId = instantiateExporter(ExporterType.NODE_EXPORTER, vnfInstanceId, nsInstanceId, vnfdId, vnfIpAddress);
					vnfInstanceToNodeExporterMap.put(vnfInstanceId, exporterId);
					PmJob pmJob = buildPmJob(os, metricType, request.getCollectionPeriod(), request.getReportingPeriod());
					storePmJobInfo(pmJob, exporterId, vnfInstanceId);
					return pmJob.getPmJobId();
				}
			} else if (exporterInfo.getType() == ExporterType.TELEGRAF_EXPORTER) {
				log.debug("The PM job requires a telegraf exporter");
				if (vnfInstanceToTelegrahExporterMap.containsKey(vnfInstanceId)) {
					String exporterId = vnfInstanceToTelegrahExporterMap.get(vnfInstanceId);
					log.debug("Telegraf exporter for VNF " + vnfInstanceId + " already existing. No need to create a new one. Exporter ID: " + exporterId);
					PmJob pmJob = buildPmJob(os, metricType, request.getCollectionPeriod(), request.getReportingPeriod());
					storePmJobInfo(pmJob, exporterId, null);
					return pmJob.getPmJobId();
				} else {
					log.debug("A new telegraf exporter is required.");
					String vnfIpAddress = vnfInfo.getMetadata().get("IP");
					String exporterId = instantiateExporter(ExporterType.TELEGRAF_EXPORTER, vnfInstanceId, nsInstanceId, vnfdId, vnfIpAddress);
					vnfInstanceToTelegrahExporterMap.put(vnfInstanceId, exporterId);

					PmJob pmJob = buildPmJob(os, metricType, request.getCollectionPeriod(), request.getReportingPeriod());
					storePmJobInfo(pmJob, exporterId, vnfInstanceId);
					return pmJob.getPmJobId();
				}
			} else {
				throw new FailedOperationException("Unsupported Prometheus exporter type");
			}
		} catch (Exception e) {
			throw new FailedOperationException("Generic exception while determining Prometheus exporter" + e.getMessage());
		}
	}

	private PmJob buildPmJob(ObjectSelection os, String performanceMetric, int collectionPeriod, int reportingPeriod) {
		String pmJobId = UUID.randomUUID().toString();
		List<String> performanceMetrics  = new ArrayList<>();
		performanceMetrics.add(performanceMetric);
		log.debug("Created new pm job ID: " + pmJobId);
		return new PmJob(pmJobId, os, performanceMetrics, new ArrayList<>(), collectionPeriod, reportingPeriod, null);
	}

	void storePmJobInfo(PmJob pmJob, String exporterId, String vnfInstanceId) {
		String pmJobId = pmJob.getPmJobId();
		pmJobIdToExporterId.put(pmJobId, exporterId);
		List<String> exporterPMJobList =  exporterIdToPmJob.get(exporterId);
		if(!exporterIdToPmJob.containsKey(exporterId)){
			exporterPMJobList = new ArrayList<String>();
		}
		exporterPMJobList.add(pmJobId);
		exporterIdToPmJob.put(exporterId,exporterPMJobList);
		pmJobs.put(pmJobId, pmJob);
		if (vnfInstanceId != null) exporterIdToVnfId.put(exporterId, vnfInstanceId);
		log.debug("Stored info about pm job: " + pmJobId+" exporter: "+exporterId);
	}

	private String instantiateExporter(ExporterType type, String vnfInstanceId, String nsId, String vnfdId, String vnfIpAddress)
			throws NotExistingEntityException, FailedOperationException {
		log.debug("Instantiating exporter of type " + type + " for VNF instance " + vnfInstanceId);
		//String vnfIpAddress = vnfDbWrapper.getVnfManagementIpAddress(vnfInstanceId);
		int port = PrometheusMapper.getPrometheusExporterPort(type);
		ExporterDescription exporterDescription = new ExporterDescription();
		List<Endpoint> eps = new ArrayList<>();
		Endpoint ep = new Endpoint();
		ep.setAddress(vnfIpAddress);
		ep.setPort(port);
		eps.add(ep);
		exporterDescription.setName(type.toString()+vnfInstanceId+"_"+vnfdId);
		exporterDescription.setEndpoint(eps);
		exporterDescription.setNsId(nsId);
		exporterDescription.setVnfdId(vnfdId);
		exporterDescription.setCollectionPeriod(1);
		try {
			Exporter exporter = exporterApi.postExporter(exporterDescription);
			log.debug("Returned exporter: " + exporter.toString());
			String exporterId = exporter.getExporterId();
			log.debug("Created exporter " + exporterId + " with type " + type + " for VNF instance " + vnfInstanceId);
			return exporterId;
		} catch (ApiException e) {
			log.error("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
			throw new FailedOperationException("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
		}
	}
}
