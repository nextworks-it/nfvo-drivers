package it.nextworks.nfvmano.nfvodriver.osm;

import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;

public class MonitoringInfo {

    private final String nsInstanceId;
    private final String ifaNsdId;
    private final String ifaNsdVersion;
    private final NsInfo nsInfo;
    private final String operationId;

    public MonitoringInfo(String nsInstanceId,
                          String ifaNsdId,
                          String ifaNsdVersion,
                          NsInfo nsInfo,
                          String operationId){
        this.nsInstanceId = nsInstanceId;
        this.ifaNsdId = ifaNsdId;
        this.ifaNsdVersion = ifaNsdVersion;
        this.nsInfo = nsInfo;
        this.operationId = operationId;
    }

    public String getNsInstanceId() {
        return nsInstanceId;
    }

    public String getIfaNsdId() {
        return ifaNsdId;
    }

    public String getIfaNsdVersion() {
        return ifaNsdVersion;
    }

    public NsInfo getNsInfo() {
        return nsInfo;
    }

    public String getOperationId() {
        return operationId;
    }
}
