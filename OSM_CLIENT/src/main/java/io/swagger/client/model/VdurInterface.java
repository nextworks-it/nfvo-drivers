package io.swagger.client.model;

public class VdurInterface {

    String name;
    String nsVldId;
    String ipAddress;
    String macAddress;
    boolean mgmtVnf;
    boolean mgmtInterface;

    public VdurInterface(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNsVldId() {
        return nsVldId;
    }

    public void setNsVldId(String nsVldId) {
        this.nsVldId = nsVldId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean isMgmtVnf() {
        return mgmtVnf;
    }

    public void setMgmtVnf(boolean mgmtVnf) {
        this.mgmtVnf = mgmtVnf;
    }

    public boolean isMgmtInterface() {
        return mgmtInterface;
    }

    public void setMgmtInterface(boolean mgmtInterface) {
        this.mgmtInterface = mgmtInterface;
    }
}
