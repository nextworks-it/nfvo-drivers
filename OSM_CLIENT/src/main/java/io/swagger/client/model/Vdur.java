package io.swagger.client.model;

import java.util.List;

//not standard
public class Vdur {

    String vduIdRef;

    String ipAddress;

    List<VdurInterface> interfaces;

    String vimId;

    String name;

    String status;

    public String getVduIdRef() {
        return vduIdRef;
    }

    public void setVduIdRef(String vduIdRef) {
        this.vduIdRef = vduIdRef;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<VdurInterface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<VdurInterface> interfaces) {
        this.interfaces = interfaces;
    }

    public String getVimId() {
        return vimId;
    }

    public void setVimId(String vimId) {
        this.vimId = vimId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
