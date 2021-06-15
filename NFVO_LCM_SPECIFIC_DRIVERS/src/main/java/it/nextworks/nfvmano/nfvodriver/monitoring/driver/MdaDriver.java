package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import io.swagger.client.mda.ApiClient;
import io.swagger.client.mda.ApiException;
import io.swagger.client.mda.api.DefaultApi;
import io.swagger.client.mda.model.*;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdaDriver implements MonitoringDriverProviderInterface {
    private static final Logger log = LoggerFactory.getLogger(MdaDriver.class);
    private final String domain;
    private DefaultApi defaultApi;
    private String nfvoAddress;


    public MdaDriver(String baseAddress, String domain, String nfvoAddress){
        this.domain = domain;
        defaultApi = new DefaultApi();
        defaultApi.setApiClient(new ApiClient().setDebugging(true).setBasePath(baseAddress));
        this.nfvoAddress=nfvoAddress;

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
        String transactionId = null;
        String networkSliceId = null;
        String tenantId = null;
        // assuming productId and transactionId are always set in this order
        log.debug("PerformanceMetricGroups:"+request.getPerformanceMetricGroup());

        productId = request.getMetricMetadata().get("product_id");
        transactionId = request.getMetricMetadata().get("transaction_id");
        networkSliceId = request.getMetricMetadata().get("nsi_id");
        tenantId = request.getMetricMetadata().get("tenant_id");
        String instanceId = request.getMetricMetadata().get("vsi_id");
        /*if(request.getPerformanceMetricGroup().size()>=6){
            productId = request.getPerformanceMetricGroup().get(2);
            transactionId = request.getPerformanceMetricGroup().get(3);
            networkSliceId = request.getPerformanceMetricGroup().get(4);
            tenantId = request.getPerformanceMetricGroup().get(5);
        }*/

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
        body.setTenantId(tenantId);
        body.setTopic(domain + "-in-0");
        body.setTransactionId(transactionId);
        body.setProductId(productId);
        body.setInstanceId(instanceId);
        body.setDataSourceType(DataSourceType.OSM);
        ContextModel cm = new ContextModel();
        cm.setNetworkSliceId(networkSliceId);
        cm.setResourceId(productId);
        List<ContextModel> cms = new ArrayList<>();
        cms.add(cm);
        body.setContextIds(cms);




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
        if(params.containsKey("step")){
            metricModel.setStep(params.get("step"));
        }

        metrics.add(metricModel);
        body.setMetrics(metrics);
        if(body.getDataSourceType().equals(DataSourceType.OSM)){
            try {
                URL url = new URL(nfvoAddress);
                URL newurl = new URL(url.getProtocol(), url.getHost(), 9091, url.getFile());
                body.setMonitoringEndpoint(newurl.toString());
            } catch (MalformedURLException e) {
                throw  new FailedOperationException(e);
            }

        }
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
       log.debug("Triggering delete MDA configuration items:"+request.getPmJobId());
        try {
            List<String> ids = new ArrayList<>();
            for(String id: request.getPmJobId()){
                log.debug("Triggering delete MDA configuration item:"+id);
                defaultApi.deleteConfigIdSettingsConfigIdDelete(id);
                ids.add(id);
            }


            return new DeletePmJobResponse(ids);
        } catch (ApiException e) {
            log.error("Error while deleting PM job", e);
           throw new FailedOperationException(e);
        }
    }

    @Override
    public MonitoringGui buildMonitoringGui(List<String> pmJobIds, String tenantId, Map<String, String> metadata) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
        return null;
    }

    @Override
    public void removeMonitoringGui(String guiId) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {

    }
}
