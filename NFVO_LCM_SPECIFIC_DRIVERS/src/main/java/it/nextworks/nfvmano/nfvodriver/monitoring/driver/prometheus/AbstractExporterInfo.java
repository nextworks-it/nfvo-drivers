package it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus;


public class AbstractExporterInfo {

	private ExporterType type;
	private int port;
	
	public AbstractExporterInfo(ExporterType type, int port) {
		this.type = type;
		this.port = port;
	}

	/**
	 * @return the type
	 */
	public ExporterType getType() {
		return type;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	
	
}
