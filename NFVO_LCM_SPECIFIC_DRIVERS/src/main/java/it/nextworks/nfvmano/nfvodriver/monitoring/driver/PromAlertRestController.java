package it.nextworks.nfvmano.nfvodriver.monitoring.driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nextworks.nfvmano.libs.ifa.common.enums.ThresholdCrossingDirection;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.ThresholdCrossedNotification;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringAlertDispatcher;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusAlert;
import it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus.PrometheusAlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/nfvodriver/alerts")
public class PromAlertRestController {

    private final static Logger log = LoggerFactory.getLogger(PromAlertRestController.class);

    private Map<String,String> alertStatus = new HashMap<>();

    private final MonitoringAlertDispatcher dispatcher;

    private final ObjectMapper mapper;

    @Autowired
    public PromAlertRestController(MonitoringAlertDispatcher dispatcher, ObjectMapper mapper) {
        this.dispatcher = dispatcher;
        this.mapper = mapper;
    }

    private void printData(Object data) throws JsonProcessingException {
        log.debug("Alert content:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
    }

    private ThresholdCrossedNotification parseAlert(PrometheusAlert alert) throws JsonProcessingException, FailedOperationException {
        String alertname = alert.getLabels().get("alertname");
        if (alertname == null) {
            log.error("Malformed alert: no alertname label");
            printData(alert);
            throw new FailedOperationException();
        }
        String status = alert.getStatus();
        if (status == null || !(status.equals("firing") || status.equals("resolved"))) {
            log.error("Illegal alert status: {}", status);
            throw new FailedOperationException();
        }
        if(!alertStatus.containsKey(alertname)){
            alertStatus.put(alertname,status);
        }
        printData(alert);
        ThresholdCrossingDirection direction;
        // TODO: This is used as "firing", "resolved" as opposed to "growing" and "decreasing"...
        if (status.equals("firing")) {
            if(alertStatus.get(alertname).equals("resolved"))
                alertStatus.replace(alertname,status);
            direction = ThresholdCrossingDirection.UP;
        } else {
            direction = ThresholdCrossingDirection.DOWN;
        }
        return new ThresholdCrossedNotification(
                alertname,
                direction,
                null,  // TODO: values here? What? They are unused...
                null,
                0
        );
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void notify(@RequestBody PrometheusAlertMessage notification) throws FailedOperationException {
        log.info("Received alert");
        try {
            // debug info
            printData(notification);
            // Parse, validate and convert
            for (PrometheusAlert alert : notification.getAlerts()) {
                ThresholdCrossedNotification ifaAlert = parseAlert(alert);
                dispatcher.notify(ifaAlert);
            }
        } catch (JsonProcessingException exc) {
            log.error("Cannot process JSON payload");
            log.debug("Details:", exc);
            throw new FailedOperationException();
        }
    }

}
