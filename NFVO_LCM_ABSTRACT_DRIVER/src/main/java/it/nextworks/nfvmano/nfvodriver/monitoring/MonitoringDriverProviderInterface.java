package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;

import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.CreatePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobRequest;
import it.nextworks.nfvmano.libs.ifa.monit.interfaces.messages.DeletePmJobResponse;
import it.nextworks.nfvmano.nfvodriver.monitoring.elements.MonitoringGui;

import java.util.List;
import java.util.Map;

public interface MonitoringDriverProviderInterface {

    String createPmJob(CreatePmJobRequest request, VnfInfo vnfInfo)
            throws MethodNotImplementedException, FailedOperationException, MalformattedElementException;

    DeletePmJobResponse deletePmJob(DeletePmJobRequest request) throws MethodNotImplementedException,
            FailedOperationException, MalformattedElementException, NotExistingEntityException ;

    MonitoringGui buildMonitoringGui(List<String> pmJobIds, String tenantId, Map<String, String> metadata) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException;

    void removeMonitoringGui(String guiId) throws MethodNotImplementedException, NotExistingEntityException,
            FailedOperationException, MalformattedElementException;
}
