package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.elements.MonitoringParameter;
import it.nextworks.nfvmano.libs.ifa.common.enums.RelationalOperation;
import it.nextworks.nfvmano.libs.ifa.common.enums.ThresholdCrossingDirection;
import it.nextworks.nfvmano.libs.ifa.common.enums.ThresholdType;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.PerformanceManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.ScaleNsData;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.ScaleNsRequest;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.PrometheusDriver;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusTDetails;
import it.nextworks.nfvmano.nfvodriver.monitoring.elements.AutoscalingRulesWrapper;
import it.nextworks.nfvmano.nfvodriver.osm.OsmLcmDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


public class NsAlertManager implements PerformanceManagementConsumerInterface {

    private static Logger log = LoggerFactory.getLogger(NsAlertManager.class);

    private String nsiId;

    private AutoscalingRulesWrapper wrapper;

    private Map<String, String> thresholdId2criterionId = new HashMap<>();

    private PrometheusDriver driver;

    private List<MonitoredData> monitoredData;

    private MonitoringAlertDispatcher dispatcher;

    private OsmLcmDriver osmLcmDriver;

    private Map<String, String> previousIL = new HashMap<>();

    private Map<String, String> currentIL = new HashMap<>();

    public NsAlertManager(
            String nsiId,
            List<MonitoredData> monitoredData,
            List<NsAutoscalingRule> rules,
            PrometheusDriver driver,
            MonitoringAlertDispatcher dispatcher,
            OsmLcmDriver osmLcmDriver
    ) {
        if (null == rules) {
            throw new NullPointerException("rules must not be null.");
        }
        //this.engine = engine;
        this.nsiId = nsiId;
        this.wrapper = new AutoscalingRulesWrapper(rules);
        this.driver = driver;
        this.monitoredData = monitoredData;
        this.dispatcher = dispatcher;
        this.osmLcmDriver = osmLcmDriver;
    }

    @Override
    public void notify(PerformanceInformationAvailableNotification notification) {
        log.error("PerformanceInformationAvailableNotification not supported.");
    }

    @Override
    public void notify(ThresholdCrossedNotification notification) {
        String thresholdId = notification.getThresholdId();
        String criterionId = thresholdId2criterionId.get(thresholdId);
        if (criterionId == null) {
            throw new IllegalArgumentException(String.format(
                    "No threshold with id %s registered at this manager",
                    thresholdId
            ));
        }
        ThresholdCrossingDirection crossingDirection = notification.getCrossingDirection();
        if (crossingDirection.equals(ThresholdCrossingDirection.DOWN)) {
            // "Resolved" alert.
            wrapper.stopFiring(criterionId);
            ScaleNsRequest scaleNsRequest =  new ScaleNsRequest(
                    nsiId,
                    null,
                    new ScaleNsData(
                            null,
                            null,
                            null,
                            new ScaleNsToLevelData(previousIL.get(nsiId),null),
                            null,
                            null,
                            null
                    ),
                    null,
                    null);
            if(!currentIL.get(nsiId).equals(previousIL.get(nsiId))){
                try {
                    osmLcmDriver.scaleNs(scaleNsRequest);
                } catch (Exception e) {
                    log.debug("Fail to execute scaling-in of Ns Instance " + nsiId);
                }
            }
            return ;
        }
        List<AutoscalingAction> actions = wrapper.fireCriterion(criterionId);
        if (actions.isEmpty()) {
            log.debug("Autoscaling actions empty");
            return;
        }
        if (actions.size() > 1) {
            throw new IllegalArgumentException("More than one auto-scaling action not supported.");
        }
        //here trigger the update to sebastian.
        AutoscalingAction autoscalingAction = actions.get(0);
        ScaleNsRequest scaleNsRequest = new ScaleNsRequest(
                nsiId,
                autoscalingAction.getScaleType(),
                new ScaleNsData(
                        null,
                        null,
                        null,
                        autoscalingAction.getScaleNsToLevelData(),
                        null,
                        null,
                        null
                ),
                null,
                null);
        try {
            //String nsPreviousIL = osmLcmDriver.getCurrentIL(nsiId);
            String nsPreviousIL = "default";
            String nsNextIL = autoscalingAction.getScaleNsToLevelData().getNsInstantiationLevel();
            if(!nsPreviousIL.equals(nsNextIL)){
                previousIL.put(nsiId, nsPreviousIL);
                currentIL.put(nsiId,nsNextIL);
                osmLcmDriver.scaleNs(scaleNsRequest);
            }
            else log.debug("Cannot scale to the same Instantiation Level");
        } catch (Exception e) {
            log.debug("Fail to execute scaling of Ns Instance " + nsiId);
        }
        /*
        String objectInstanceId = notification.getObjectInstanceId();
        String performanceMetric = notification.getPerformanceMetric();
        int performanceValue = notification.getPerformanceValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        /*MessageDummyAlgorithm messageDummyAlgorithm = new MessageDummyAlgorithm(objectInstanceId,performanceMetric, String.valueOf(performanceValue));
        HttpEntity<?> postEntity = new HttpEntity<>(messageDummyAlgorithm, header);
        ResponseEntity<Void> scalingResponse = restTemplate.exchange("http://localhost:8080/dummy", HttpMethod.POST, postEntity, Void.class);
        if (scalingResponse.getStatusCode() == HttpStatus.OK)
            log.debug("Notification sent successfully");
        else
            log.debug("Errore while sending notification " + scalingResponse.getStatusCode());*/
    }

    public Optional<ScaleNsRequest> makeRequest(ThresholdCrossedNotification notification) {
        String thresholdId = notification.getThresholdId();
        String criterionId = thresholdId2criterionId.get(thresholdId);
        if (criterionId == null) {
            throw new IllegalArgumentException(String.format(
                    "No threshold with id %s registered at this manager",
                    thresholdId
            ));
        }
        if (notification.getCrossingDirection().equals(ThresholdCrossingDirection.DOWN)) {
            // "Resolved" alert
            wrapper.stopFiring(criterionId);
            return Optional.empty();
        }
        // else: "firing" alert
        List<AutoscalingAction> actions = wrapper.fireCriterion(criterionId);
        if (actions.isEmpty()) {
            return Optional.empty();
        }
        if (actions.size() > 1) {
            throw new IllegalArgumentException("More than one auto-scaling action not supported.");
        }
        AutoscalingAction action = actions.get(0);
        return Optional.of(new ScaleNsRequest(
                nsiId,
                action.getScaleType(),
                new ScaleNsData(
                        null,
                        null,
                        null,
                        action.getScaleNsToLevelData(),
                        null,
                        null,
                        null
                ),
                null,
                null
        ));
    }

    @Override
    public String toString() {
        return String.format("Alert listener for NSI ID %s", nsiId);
    }

    private PrometheusTDetails makeDetails(Map.Entry<AutoscalingRuleCriteria, Integer> entry, String pmJobId) {
        AutoscalingRuleCriteria criterion = entry.getKey();
        Integer thresholdTime = entry.getValue();
        RelationalOperation rel;
        int value;
        if (criterion.getScaleOutRelationalOperation() != null) {
            if (criterion.getScaleOutThreshold() == null || criterion.getScaleInRelationalOperation() != null) {
                throw new IllegalArgumentException(String.format("Malformed criterion %s", criterion.getName()));
            }
            rel = criterion.getScaleOutRelationalOperation();
            value = criterion.getScaleOutThreshold();
        } else {
            if (criterion.getScaleInThreshold() == null) {
                throw new IllegalArgumentException(String.format("Malformed criterion %s", criterion.getName()));
            }
            rel = criterion.getScaleInRelationalOperation();
            value = criterion.getScaleInThreshold();
        }
        return new PrometheusTDetails(
                value,
                rel,
                thresholdTime,
                pmJobId
        );
    }

    void createThresholds(Map<String, String> mp2job)
            throws MethodNotImplementedException, FailedOperationException, MalformattedElementException {
        log.info("Creating thresholds for NSI {}", nsiId);
        for (Map.Entry<AutoscalingRuleCriteria, Integer> entry : wrapper.getCriteriaAndThresholdTimes().entrySet()) {
            AutoscalingRuleCriteria criterion = entry.getKey();
            log.debug("Creating threshold for criterion {}", criterion.getName());
            String mpId = criterion.getNsMonitoringParamRef();
            Optional<String> optMetric = monitoredData.stream()
                    .filter(mi -> mi.getMonitoringParameter() != null)
                    .map(MonitoredData::getMonitoringParameter)
                    .filter(mp -> mp.getMonitoringParameterId().equals(mpId))
                    .map(MonitoringParameter::getPerformanceMetric)
                    .findAny();
            if (!optMetric.isPresent()) {
                log.warn(
                        "Monitoring parameter {} not available for nsi {}, aborting threshold",
                        mpId,
                        nsiId
                );
                continue;
            }
            String metric = optMetric.get().split("\\.")[0];
            CreateThresholdRequest request = new CreateThresholdRequest(
                    null,
                    null,
                    null,
                    metric,
                    ThresholdType.SINGLE_VALUE,
                    makeDetails(entry, mp2job.get(mpId))
            );
            //this create alarm and associations in alertmanager
            String thresholdId = driver.createThreshold(request);
            log.debug("Criterion {} is assigned threshold {}", criterion.getName(), thresholdId);
            thresholdId2criterionId.put(thresholdId, criterion.getName());
            dispatcher.register(thresholdId, this);
        }
        log.info("Threshold creation successful");
    }

    void deleteThresholds() {
        log.info("Stopping thresholds for NSI {}", nsiId);
        DeleteThresholdsRequest request = new DeleteThresholdsRequest(
                new ArrayList<>(thresholdId2criterionId.keySet())
        );
        DeleteThresholdsResponse response;
        try {
            response = driver.deleteThreshold(request);
        } catch (Exception exc) {
            log.error("Could not delete thresholds");
            log.debug("Details:", exc);
            return;
        }
        log.info("Deleted the following thresholds: {}", response.getDeletedThresholdId());
        response.getDeletedThresholdId().forEach(thresholdId2criterionId::remove);
    }
}