package it.nextworks.nfvmano.nfvodriver.monitoring;

import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;

import java.util.List;

public interface MonitoringManagerProviderInterface {


    void activateNsMonitoring(NsInfo nsInfo, Nsd nsd, List<VnfInfo> vnfInfoList)
            throws MethodNotImplementedException, AlreadyExistingEntityException, NotExistingEntityException, FailedOperationException,
            MalformattedElementException;

    void deactivateNsMonitoring(String nsInstanceId)
            throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException,
            MalformattedElementException;
}
