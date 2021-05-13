package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import io.swagger.client.mda.ApiClient;
import io.swagger.client.mda.ApiException;
import io.swagger.client.mda.api.DefaultApi;
import io.swagger.client.mda.model.ConfigModel;
import io.swagger.client.mda.model.MetricModel;
import io.swagger.client.mda.model.ResponseConfigModel;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.enums.MonitoringObjectType;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.CreatePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobResponse;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringDriverProviderInterface;
import it.nextworks.nfvmano.nfvodriver.monitoring.elements.MonitoringGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdaDriver implements MonitoringDriverProviderInterface {
    private static final Logger log = LoggerFactory.getLogger(MdaDriver.class);
    private final String domain;
    private DefaultApi defaultApi;


    public MdaDriver(String baseAddress, String domain){
        this.domain = domain;
        defaultApi = new DefaultApi();
        defaultApi.setApiClient(new ApiClient().setDebugging(true).setBasePath(baseAddress));

    }
    @Override
    public String createPmJob(CreatePmJobRequest request, VnfInfo vnfInfo) throws MethodNotImplementedException, FailedOperationException, MalformattedElementException {
        log.debug("CreatePmJob"+request+" "+vnfInfo);
        String metricType = request.getPerformanceMetric().get(0);

        MonitoringObjectType mot = request.getVnfSelector().getObjectType().get(0);

        String vnfInstanceId = request.getVnfSelector().getObjectInstanceId().get(0);


        String vnfdId = request.getPerformanceMetricGroup().get(0);

        String nsInstanceId = request.getPerformanceMetricGroup().get(1);
        String productId = null;
        if(request.getPerformanceMetricGroup().size()>=3){
            productId = request.getPerformanceMetricGroup().get(2);
        }


        //Config Model:
        //  bussinessId : product_id provided during VS instantation Request
        //  networkId : ??
        //  topic: domain
        //  referenceID: "Unique ID of entity (e.g., SLA) for whom we are collecting monitoring data"
        //  resourceID: "Unique ID of resource for which we are collecting monitoring data."
        //  tenantID:
        //  metricModel:
        //      metricType: from the monitoringParameterId in the NSD
        //      name: nsInstanceId.vnfdId.metricType
        //      aggregationMethod/timestampStep: from NSD monitoring parameter params.
        //
        log.debug("PM job parameters - Metric type: " + metricType + " - Monitoring object type: " + mot + " - VNF ID: " + vnfInstanceId + " - VNFD ID: " + vnfdId + " - NS Instance ID: " + nsInstanceId);
        ConfigModel body = new ConfigModel();
        body.setTopic(domain);
        body.setBusinessID(Integer.parseInt(productId));
        body.setNetworkID(0);

        //TODO
        body.setReferenceID("referenceID");
        body.setResourceID("resourceID");
        body.setTenantID("tenantID");
        List<MetricModel> metrics = new ArrayList<>();
        MetricModel metricModel = new MetricModel();
        //metricModel.setMetricType(mot.toString());
        metricModel.setMetricType(metricType);
        metricModel.setMetricName(metricType);
        Map<String, String> params = request.getPmJobParams();
        log.debug("PmJobParams:"+params);
        if(params.containsKey("aggregationMethod")){
            metricModel.setAggregationMethod(params.get("aggregationMethod"));
        }
        if(params.containsKey("timestampStep")){
            metricModel.setTimestampStep(params.get("timestampStep"));
        }

        metrics.add(metricModel);
        body.setMetrics(metrics);
        try {
            ResponseConfigModel response= defaultApi.setParamSettingsPost(body);
            return response.getId().toString();

        } catch (ApiException e) {
            log.error("ERROR DURING MDA CONFIGURATION REQUEST", e);
            throw new FailedOperationException(e);
        }

    }

    @Override
    public DeletePmJobResponse deletePmJob(DeletePmJobRequest request) throws MethodNotImplementedException, FailedOperationException, MalformattedElementException, NotExistingEntityException {
        return null;
    }

    @Override
    public MonitoringGui buildMonitoringGui(List<String> pmJobIds, String tenantId, Map<String, String> metadata) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
        return null;
    }

    @Override
    public void removeMonitoringGui(String guiId) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {

    }
}
