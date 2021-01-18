package it.nextworks.openapi.msno.model;

public class RadioSliceProfile {

    private String sST;

    private String coverageArea;

    private int latency;

    private int uLThptPerSlice;

    private int dLThptPerSlice;


    public RadioSliceProfile(String sST, String coverageArea, int latency, int uLThptPerSlice, int dLThptPerSlice) {
        this.sST = sST;
        this.coverageArea = coverageArea;
        this.latency = latency;
        this.uLThptPerSlice = uLThptPerSlice;
        this.dLThptPerSlice = dLThptPerSlice;
    }

    public String getsST() {
        return sST;
    }

    public void setsST(String sST) {
        this.sST = sST;
    }

    public String getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(String coverageArea) {
        this.coverageArea = coverageArea;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getuLThptPerSlice() {
        return uLThptPerSlice;
    }

    public void setuLThptPerSlice(int uLThptPerSlice) {
        this.uLThptPerSlice = uLThptPerSlice;
    }

    public int getdLThptPerSlice() {
        return dLThptPerSlice;
    }

    public void setdLThptPerSlice(int dLThptPerSlice) {
        this.dLThptPerSlice = dLThptPerSlice;
    }
}
