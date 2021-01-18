package it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by Marco Capitani on 19/06/19.
 *
 * @author Marco Capitani <m.capitani AT nextworks.it>
 */
public class PrometheusAlertMessage {

    private String receiver;

    private String status;

    @NotNull
    private List<PrometheusAlert> alerts;

    private  Map<String, String> groupLabels;

    private  Map<String, String> commonLabels;

    private  Map<String, String> commonAnnotations;

    private  String externalURL;

    private String version;

    private String groupKey;

    public String getGroupKey() {
        return groupKey;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getStatus() {
        return status;
    }

    public List<PrometheusAlert> getAlerts() {
        return alerts;
    }

    public Map<String, String> getGroupLabels() {
        return groupLabels;
    }

    public Map<String, String> getCommonLabels() {
        return commonLabels;
    }

    public Map<String, String> getCommonAnnotations() {
        return commonAnnotations;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public String getVersion() {
        return version;
    }
}
