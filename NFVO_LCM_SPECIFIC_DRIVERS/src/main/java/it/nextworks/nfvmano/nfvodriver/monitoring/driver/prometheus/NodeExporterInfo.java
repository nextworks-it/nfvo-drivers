package it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus;

public class NodeExporterInfo extends AbstractExporterInfo {

	public NodeExporterInfo() {
		super(ExporterType.NODE_EXPORTER, 9100);
	}

}
