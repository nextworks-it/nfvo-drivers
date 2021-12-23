package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.monit.interfaces.PerformanceManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.PerformanceInformationAvailableNotification;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.ThresholdCrossedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MonitoringAlertDispatcher implements PerformanceManagementConsumerInterface {

    private static Logger log = LoggerFactory.getLogger(MonitoringAlertDispatcher.class);

    private Map<String, PerformanceManagementConsumerInterface> listenerMap = new HashMap<>();

    @Override
    public void notify(PerformanceInformationAvailableNotification notification) {
        log.error("PerformanceInformationAvailableNotification not supported.");
    }

    @Override
    public void notify(ThresholdCrossedNotification notification) {
        String thresholdId = notification.getThresholdId();
        log.debug("Received alert for threshold ID {}", thresholdId);
        PerformanceManagementConsumerInterface listener = listenerMap.get(thresholdId);
        if (listener == null) {
            log.error("No listener subscribed for threshold ID {}, aborting", thresholdId);
            return;
        }
        log.debug("Notifying listener {}", listener);
        listener.notify(notification);
    }

    public void register(String thresholdId, PerformanceManagementConsumerInterface listener) {
        log.debug("Adding listener {} to threshold ID {}", listener, thresholdId);
        if (listenerMap.containsKey(thresholdId)) {
            log.error("Threshold ID {} already has a registered listened", thresholdId);
            throw new IllegalArgumentException(String.format(
                    "Threshold ID %s already has a registered listened", thresholdId
            ));
        }
        listenerMap.put(thresholdId, listener);
    }

    public void deregister(String thresholdId) {
        log.debug("Removing registration to threshold ID {}", thresholdId);
        listenerMap.remove(thresholdId);
    }
}