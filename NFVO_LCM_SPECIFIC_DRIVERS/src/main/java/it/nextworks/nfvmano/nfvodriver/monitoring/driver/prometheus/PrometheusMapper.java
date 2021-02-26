package it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus;

import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.MonitoringObjectType;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to retrieve information about Prometheus exporters and queries for predefined metric types.
 * 
 * 
 * @author nextworks
 *
 */
public class PrometheusMapper {

	private static final Map<MonitoringObjectType, Map<String, AbstractExporterInfo>> prometheusExporterMapper;
	static {
		prometheusExporterMapper = new HashMap<>();
		MonitoringObjectType motExpVnf = MonitoringObjectType.VNF;
		AbstractExporterInfo nodeExporterInfo = new NodeExporterInfo();
		Map<String, AbstractExporterInfo> vnfMetricsExporterMap = new HashMap<>();
		vnfMetricsExporterMap.put("VcpuUsageMean", nodeExporterInfo);
		vnfMetricsExporterMap.put("VmemoryUsageMean", nodeExporterInfo);
		vnfMetricsExporterMap.put("VdiskUsageMean", nodeExporterInfo);
		vnfMetricsExporterMap.put("ByteIncoming", nodeExporterInfo);
		AbstractExporterInfo telegrafExporterInfo = new TelegrafExporterInfo();
		vnfMetricsExporterMap.put("CurrentClientConnections", telegrafExporterInfo);
		vnfMetricsExporterMap.put("CurrentActiveClientConnections", telegrafExporterInfo);
		vnfMetricsExporterMap.put("UserAgentCurrentConnectionsCount", telegrafExporterInfo);
		vnfMetricsExporterMap.put("CompletedRequests", telegrafExporterInfo);
		vnfMetricsExporterMap.put("TotalHits", telegrafExporterInfo);
		vnfMetricsExporterMap.put("CacheRamUsed", telegrafExporterInfo);
		prometheusExporterMapper.put(motExpVnf, vnfMetricsExporterMap);
	}
	
	private static final Map<MonitoringObjectType, Map<String, String>> prometheusQueriesMapper;
	static {
		prometheusQueriesMapper = new HashMap<>();
		MonitoringObjectType motQueryVnf = MonitoringObjectType.VNF;
		Map<String, String> vnfMetricsQueriesMap = new HashMap<>();
		vnfMetricsQueriesMap.put("VcpuUsageMean", "(node_memory_MemAvailable_bytes{job=\"$$configExp\"}/ node_memory_MemTotal_bytes{job=\"$$configExp\"}) + 100");
		vnfMetricsQueriesMap.put("VmemoryUsageMean", "sum without (mode, cpu) (rate(node_cpu_seconds_total{mode != \"idle\", job=\"$$configExp\"}[1m])) * 50");
		vnfMetricsQueriesMap.put("VdiskUsageMean", "node_filesystem_avail_bytes{mountpoint=\"/\", job=\"$$configExp\"} / node_filesystem_size_bytes{mountpoint=\"/\", job=\"$$configExp\"} * 100");
		//TODO: to be fixed how to pass parameters
		vnfMetricsQueriesMap.put("ByteIncoming", "rate(node_network_receive_bytes_total{device=\"<NIC_name>\", job=\"$$configExp\"}[1m]");
		vnfMetricsQueriesMap.put("CurrentClientConnections", "trafficserver_stats_http_current_client_connections{job=\"$$configExp\"}");
		vnfMetricsQueriesMap.put("CurrentActiveClientConnections", "trafficserver_stats_http_current_active_client_connections{job=\"$$configExp\"}");
		vnfMetricsQueriesMap.put("UserAgentCurrentConnectionsCount", "trafficserver_stats_http_user_agent_current_connections_count{job=\"$$configExp\"}");
		vnfMetricsQueriesMap.put("CompletedRequests", "trafficserver_stats_http_completed_requests{job=\"$$configExp\"}");
		vnfMetricsQueriesMap.put("TotalHits", "trafficserver_stats_hostdb_cache_total_hits{job=\"$$configExp\"}");
		vnfMetricsQueriesMap.put("CacheRamUsed", "trafficserver_stats_cache_ram_cache_bytes_used{job=\"$$configExp\"}");
		prometheusQueriesMapper.put(motQueryVnf, vnfMetricsQueriesMap);
	}
	
	public static AbstractExporterInfo readPrometheusExporterInfo(MonitoringObjectType monitoringObjectType, String metricType) {
		return prometheusExporterMapper.get(monitoringObjectType).get(metricType);
	}
	
	public static String readPrometheusQuery(MonitoringObjectType monitoringObjectType, String metricType, String exporterId) {
		String origString = prometheusQueriesMapper.get(monitoringObjectType).get(metricType);
		return modifyParameter(origString, "$$configExp", exporterId);
	}
	
	public static int getPrometheusExporterPort(ExporterType type) {
		switch (type) {
		case TELEGRAF_EXPORTER:
			return 9273;

		case NODE_EXPORTER:
			return 9100;
			
		default:
			//TODO: put error
			return 0;
		}
	}
	
	/**
	 * Method to substitute a parameter with its value. It returns the modified script.
	 * 
	 * @param originalString script to be modified
	 * @param parameter type of parameter to be substituted
	 * @param parameterValue value of the parameter
	 * @return the modified script
	 */
	private static String modifyParameter(String originalString, String parameter, String parameterValue) {
		String p = createRegex(parameter);
		String result = originalString.replaceAll(p, parameterValue);
		return result;
	}

	/**
	 * Method to create a string compliant with the regular expression format
	 * 
	 * @param s	string to be modified
	 * @return	modified string
	 */
	private static String createRegex(String s) {
		StringBuilder b = new StringBuilder();
		for(int i=0; i<s.length(); ++i) {
			char ch = s.charAt(i);
			if ("\\.^$|?*+[]{}()".indexOf(ch) != -1)
				b.append('\\').append(ch);
			else if (Character.isLetter(ch))
				//b.append("[A-Za-z]");
				b.append(ch);
			else if (Character.isDigit(ch))
				//b.append("\\d");
				b.append(ch);
			else
				b.append(ch);
		}
		return b.toString();
	}
}
