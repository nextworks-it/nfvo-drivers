package io.swagger.client.elma.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NetworkServiceInstance {

    @SerializedName("nsInstanceId")
    private List<String> nsInstanceId = new ArrayList<>();


    @SerializedName("nsDescriptorId")
    private String nsDescriptorId;

    @SerializedName("tenantId")
    private String tenantId;


    public NetworkServiceInstance(String nsInstanceId, String nsDescriptorId, String tenantId) {
        this.nsInstanceId.add(nsInstanceId);
        this.nsDescriptorId = nsDescriptorId;
        this.tenantId = tenantId;
    }

    public List<String> getNsInstanceId() {
        return nsInstanceId;
    }


    public String getNsDescriptorId() {
        return nsDescriptorId;
    }

    public void setNsDescriptorId(String nsDescriptorId) {
        this.nsDescriptorId = nsDescriptorId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}


