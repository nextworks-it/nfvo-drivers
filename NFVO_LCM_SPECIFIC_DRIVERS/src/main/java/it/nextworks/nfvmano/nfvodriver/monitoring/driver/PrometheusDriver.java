package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrometheusDriver {

    private static final Logger log = LoggerFactory.getLogger(PrometheusDriver.class);

    private static final String MON_PATH = "timeo/alerts";
    /*
    private ExporterApi exporterApi;

	private DashboardApi dashboardApi;

	private String grafanaUrl;

	private String timeoDomain;

	private AlertApi alertApi;

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
	private Map<String, MonitoringGui> monitoringGui = new HashMap<>();
     */
    public PrometheusDriver(){}

}
