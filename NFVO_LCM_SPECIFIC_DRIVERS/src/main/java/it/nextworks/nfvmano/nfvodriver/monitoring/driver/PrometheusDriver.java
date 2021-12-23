package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.AlertApi;
import io.swagger.client.api.DashboardApi;
import io.swagger.client.api.ExporterApi;
import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.common.enums.RelationalOperation;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.ObjectSelection;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.PmJob;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.elements.ThresholdDetails;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.MonitoringObjectType;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.ThresholdFormat;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringDriverProviderInterface;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.AbstractExporterInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.ExporterType;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusMapper;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusTDetails;
import it.nextworks.nfvmano.nfvodriver.monitoring.elements.MonitoringGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This is the monitoring driver that collect metrics from the NS instances.
 */
public class PrometheusDriver implements MonitoringDriverProviderInterface {

    private static final Logger log = LoggerFactory.getLogger(PrometheusDriver.class);

    //private static final String MON_PATH = "timeo/alerts";

    private final ExporterApi exporterApi;

	private final DashboardApi dashboardApi;

	private String grafanaUrl;

	private String manoDomain;

	private final String dummyAlgorithmEndPoint;

	private final AlertApi alertApi;

	//key: ID of the VNF instance; Value: ID of the node exporter created for that VNF instance
	private final Map<String, String> vnfInstanceToNodeExporterMap = new HashMap<>();

	//key: ID of the VNF instance; Value: ID of the Telegraf exporter created for that VNF instance
	private final Map<String, String> vnfInstanceToTelegrahExporterMap = new HashMap<>();

	//key: ID of the pm job; Value: ID of the exporter associated to that pm job
	private final Map<String, String> pmJobIdToExporterId = new HashMap<>();

	//key: ID of the exporter; Value: List of pm job IDs mapped on the exporter
	private final Map<String, List<String>> exporterIdToPmJob = new HashMap<>();

	//key: ID of the exporter; Value: ID of the VNF
	private final Map<String, String> exporterIdToVnfId = new HashMap<>();

	//key: ID of the PM job
	private final Map<String, PmJob> pmJobs = new HashMap<>();

	//key: ID of the dashboard; Value: List of pm job IDs shown on the dashboard
	private final Map<String, List<String>> dashboardIdToPmJobId = new HashMap<>();

	//key: ID of the pm job; Value: ID of the dashboard where the pm job is shown.
	//At the moment a pm job ID is shown on a single dashboard. Check if we need to evolve this.
	private final Map<String, String> pmJobIdToDashboardId = new HashMap<>();

	//key: ID of the dashboard; Value: details of the dashboard
	private final Map<String, MonitoringGui> monitoringGui = new HashMap<>();

    public PrometheusDriver(String monitoringPlatformUrl,
							String grafanaUrl,
							String manoDomain){
    	exporterApi = new ExporterApi();
		dashboardApi = new DashboardApi();
		alertApi = new AlertApi();

		ApiClient apiClient = new ApiClient();
		// url of the monitoring platform http://<ADDRESS>:8989/prom-manager/
		apiClient.setBasePath(monitoringPlatformUrl);
		exporterApi.setApiClient(apiClient);

		dashboardApi.setApiClient(apiClient);
		alertApi.setApiClient(apiClient);
		this.grafanaUrl=grafanaUrl;

		// timeo.domain=http://localhost:8081/
		this.manoDomain = manoDomain;
		//Maybe in application properties need to configure the output of the alert
		dummyAlgorithmEndPoint = "http://localhost:8083/nfvodriver/alerts";
	}

	public MonitoringGui buildMonitoringGui(List<String> pmJobIds, String tenantId, Map<String, String> metadata) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		log.debug("Building monitoring dashboard");
		DashboardDescription dd = new DashboardDescription();
		String nsdId = metadata.get("NSD_ID");
		String nsId = metadata.get("NS_ID");
		String name = "Monitoring for network service " + nsdId + " with instance ID " + nsId;
		dd.setName(name);
		//Plotted time: size of the time window in minutes
		int plottedTime = 60;
		dd.setPlottedTime(plottedTime);
		dd.setRefreshTime(DashboardDescription.RefreshTimeEnum._5S);
		List<String> users = new ArrayList<>();
		users.add(tenantId);
		dd.setUsers(users);
		for (String pmJobId : pmJobIds) {
			if (!(pmJobs.containsKey(pmJobId))) throw new NotExistingEntityException("Failed to build dashboard: pm job ID " + pmJobId + " not found");
			PmJob pmJob = pmJobs.get(pmJobId);
			if (pmJob == null) throw new NotExistingEntityException("Failed to build dashboard: pm job ID " + pmJobId + " not found");

			MonitoringObjectType monitoringObjectType = pmJob.getObjectSelector().getObjectType().get(0);
			String metricType = pmJob.getPerformanceMetric().get(0);

			DashboardPanel dp = new DashboardPanel();
			String exporterId = pmJobIdToExporterId.get(pmJobId);

			try {
				String query = PrometheusMapper.readPrometheusQuery(monitoringObjectType, metricType, exporterId);
				dp.setQuery(query);
				String title = "VNF instance " + exporterIdToVnfId.get(exporterId) + ": " + pmJob.getPerformanceMetric().get(0);
				dp.setTitle(title);
				dd.addPanelsItem(dp);
				log.debug("Added query " + query + " with title " + title + " to monitoring dashboard.");
			} catch (Exception e) {
				log.warn("Error while generating query for pm job " + pmJobId);
			}
		}
		try {
			Dashboard dashboard = dashboardApi.postDashboard(dd);
			String dashboardId = dashboard.getDashboardId();
			String url = dashboard.getUrl();
			if(!url.startsWith("http")){
				log.debug("Appending grafana url to dashboard url");
				url=grafanaUrl+url;
			}
			log.debug("Created dashboard with ID " + dashboardId + " with URL: " + url);
			MonitoringGui mg = new MonitoringGui(dashboardId, url);
			dashboardIdToPmJobId.put(dashboardId, pmJobIds);
			for (String pmJobId : pmJobIds) {
				pmJobIdToDashboardId.put(pmJobId, dashboardId);
			}
			monitoringGui.put(dashboardId, mg);
			log.debug("Stored info about monitoring GUI");
			return mg;
		} catch (ApiException e) {
			log.error("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
			throw new FailedOperationException("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
		}
	}

	public void removeMonitoringGui(String guiId) throws MethodNotImplementedException, NotExistingEntityException,
			FailedOperationException, MalformattedElementException {
		log.debug("Removing monitoring GUI with ID " + guiId);
		try {
			dashboardApi.deleteDashboard(guiId);
			log.debug("Dashboard removed from Prometheus");
			monitoringGui.remove(guiId);
			List<String> pmJobIdsInGui = dashboardIdToPmJobId.get(guiId);
			for (String pmJobId : pmJobIdsInGui) {
				pmJobIdToDashboardId.remove(pmJobId);
			}
			dashboardIdToPmJobId.remove(guiId);
			log.debug("Dashboard removed from internal maps.");
		} catch (ApiException e) {
			log.error("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
			throw new FailedOperationException("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
		}
	}

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

	/**
	 * It associates the Prometheus Job Id to the correspondent exporter
	 * @param pmJob
	 * @param exporterId
	 * @param vnfInstanceId
	 */
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
		//port 9100: node exporter
		ep.setPort(port);
		eps.add(ep);
		exporterDescription.setName(type.toString()+vnfInstanceId+"_"+vnfdId);
		exporterDescription.setEndpoint(eps);
		exporterDescription.setNsId(nsId);
		exporterDescription.setVnfdId(vnfdId);
		exporterDescription.setCollectionPeriod(5);
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

	public DeletePmJobResponse deletePmJob(DeletePmJobRequest request) throws MethodNotImplementedException,
			FailedOperationException, MalformattedElementException, NotExistingEntityException {
		log.debug("Removing pm jobs.");
		List<String> removed = new ArrayList<>();
		List<String> toBeRemoved = request.getPmJobId();
		for (String pmJobId : toBeRemoved) {
			log.debug("Removing pm job " + pmJobId);
			String exporterId = pmJobIdToExporterId.get(pmJobId);
			log.debug("Exporter associated to pm job " + pmJobId + ": " + exporterId);
			List<String> pmJobsInExp = exporterIdToPmJob.get(exporterId);
			pmJobsInExp.remove(pmJobId);
			pmJobIdToExporterId.remove(pmJobId);
			PmJob pmJob = pmJobs.get(pmJobId);
			ExporterType exporterType = ExporterType.UNDEFINED;
			try {
				exporterType = PrometheusMapper.readPrometheusExporterInfo(pmJob.getObjectSelector().getObjectType().get(0), pmJob.getPerformanceMetric().get(0)).getType();
			} catch (Exception e) {
				log.error("Impossible to determine exporter type: " + e.getMessage());
			}
			pmJobs.remove(pmJobId);
			log.debug("PM job removed from internal structures.");
			if (pmJobIdToDashboardId.containsKey(pmJobId)) {
				//TODO: check if we need to update dashboard at this stage.
				log.debug("PM job " + pmJobId + " associated to dashboard.");
				String guiId = pmJobIdToDashboardId.get(pmJobId);
				List<String> pmJobsInGui = dashboardIdToPmJobId.get(guiId);
				pmJobsInGui.remove(pmJobId);
				dashboardIdToPmJobId.replace(guiId, pmJobsInGui);
				pmJobIdToDashboardId.remove(pmJobId);
				log.debug("Removed association PM job - dashboard from internal structure.");
			}
			if (pmJobsInExp.isEmpty()) {
				log.debug("Exporter " + exporterId + " no more in use. It can be removed.");
				try {
					exporterApi.deleteExporter(exporterId);
					log.debug("Exporter " + exporterId + " removed from Prometheus.");
				} catch (ApiException e) {
					log.error("Failed to remove exporter " + exporterId + " from Prometheus. Continuing to remove internally.");
				}
				exporterIdToPmJob.remove(exporterId);
				String vnfId = exporterIdToVnfId.get(exporterId);
				if (exporterType.equals(ExporterType.NODE_EXPORTER)) vnfInstanceToNodeExporterMap.remove(vnfId);
				else if (exporterType.equals(ExporterType.TELEGRAF_EXPORTER)) vnfInstanceToTelegrahExporterMap.remove(vnfId);
				log.debug("Removed exporter from internal maps.");
			} else {
				log.debug("Exporter " + exporterId + " still serving " + pmJobsInExp.size() + " pm jobs. It cannot be removed.");
				exporterIdToPmJob.replace(exporterId, pmJobsInExp);
				log.debug("Exporter map updated.");
			}
			removed.add(pmJobId);
		}
		return new DeletePmJobResponse(removed);
	}

	private static AlertDescription.KindEnum translateRelation(RelationalOperation op) {
		switch (op) {
			case LT:
				return AlertDescription.KindEnum.L;
			case LE:
				return AlertDescription.KindEnum.LEQ;
			case GT:
				return AlertDescription.KindEnum.G;
			case GE:
				return AlertDescription.KindEnum.GEQ;
			case EQ:
				return AlertDescription.KindEnum.EQ;
			default:
				throw new IllegalArgumentException(String.format(
						"Unknown relational operation %s",
						op
				));
		}
	}

	String makeAlertQuery(String pm, PrometheusTDetails details) {
		String expId = pmJobIdToExporterId.get(details.getPmJobId());
		return PrometheusMapper.readPrometheusQuery(
				MonitoringObjectType.VNF, // The only type supported right now
				pm,
				expId
		);
	}

	AlertDescription makeAlertDescription(CreateThresholdRequest request) {
		ThresholdDetails details = request.getThresholdDetails();
		if (!details.getFormat().equals(ThresholdFormat.PROMETHEUS)) {
			throw new IllegalArgumentException(String.format(
					"Unsupported threshold format %s",
					details.getFormat()
			));
		}
		KVP kvp = new KVP();
		//in order to get the value of the metric in the alarm
		kvp.setKey("Description");
		kvp.setValue("{{ $value }}");
		List<KVP> kvpList = new ArrayList<>();
		kvpList.add(kvp);
		PrometheusTDetails promDetails = (PrometheusTDetails) details;
		return new AlertDescription()
				.alertName(UUID.randomUUID().toString())  // TODO more meaningful name?
				._for(promDetails.getThresholdTime() + "s") // javascript concatenation of int and string
				.kind(translateRelation(promDetails.getRelationalOperation()))
				.labels(kvpList)
				.query(makeAlertQuery(request.getPerformanceMetric(), promDetails))
				.severity(AlertDescription.SeverityEnum.WARNING)
				.target(dummyAlgorithmEndPoint)
				.value(promDetails.getValue());
	}

	public String createThreshold(CreateThresholdRequest request)
			throws MethodNotImplementedException, FailedOperationException, MalformattedElementException {

		Alert response;
		try {
			response = alertApi.postAlert(makeAlertDescription(request));
		} catch (ApiException e) {
			log.error("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
			throw new FailedOperationException("API exception while invoking Monitoring Config Manager client: " + e.getMessage());
		}
		return response.getAlertId();
	}

	public DeleteThresholdsResponse deleteThreshold(DeleteThresholdsRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException,
			MalformattedElementException {
		log.info("Deleting thresholds: {}", request.getThresholdId());
		List<String> deleted = new ArrayList<>();
		for (String thresholdId : request.getThresholdId()) {
			try {
				alertApi.deleteAlert(thresholdId);
				deleted.add(thresholdId);
			} catch (ApiException exc) {
				log.error("Could not delete threshold with id {}. Skipping", thresholdId);
				log.debug("Details:", exc);
			}
		}
		return new DeleteThresholdsResponse(
				deleted
		);
	}
}
