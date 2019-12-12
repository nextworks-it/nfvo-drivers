package it.nextworks.nfvmano.nfvodriver.osm;

import io.swagger.client.model.CreateNSinstanceContentRequest;
import io.swagger.client.model.CreateNsRequest;
import io.swagger.client.model.InstantiateNsRequest;
import io.swagger.client.model.TerminateNsRequest;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest;

import java.util.UUID;

public class IfaOsmLcmTranslator {



    public static CreateNsRequest getCreateNsRequest(CreateNsIdentifierRequest request, UUID vimId, UUID nsdInfoId){


        CreateNsRequest translation = new CreateNsRequest();
        translation.setNsName(request.getNsName());
        translation.setNsdId(nsdInfoId);
        translation.setVimAccountId(vimId);

        return translation;
    }

    private static UUID getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
        String seed = nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId();
        return UUID.nameUUIDFromBytes(seed.getBytes());
    }

    public static InstantiateNsRequest getInstantiateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest request, UUID nsdInfoId, UUID nsdId, UUID vimId) {
        InstantiateNsRequest translation = new InstantiateNsRequest();
        //TODO: Should we add again the nsdId? would need to be saved in a map
        translation.setVimAccountId(vimId);
        translation.setNsName(request.getNsInstanceId());
        translation.setNsdId(nsdId);
        return translation;

    }

    public static OperationStatus getOperationSatus(OsmNsLcmOperationStatus osmOperationStatus) throws FailedOperationException {
        //Retrived from SOL005v020401
        if (OsmNsLcmOperationStatus.FAILED == osmOperationStatus || OsmNsLcmOperationStatus.FAILED_TEMP == osmOperationStatus) {
            return OperationStatus.FAILED;
        } else if (OsmNsLcmOperationStatus.COMPLETED==osmOperationStatus || osmOperationStatus==OsmNsLcmOperationStatus.PARTIALLY_COMPLETED) {
            return OperationStatus.SUCCESSFULLY_DONE;
        } else if (osmOperationStatus==OsmNsLcmOperationStatus.ROLLING_BACK || osmOperationStatus==OsmNsLcmOperationStatus.ROLLED_BACK) {
            //TODO: See implications
            return OperationStatus.FAILED;
        } else if (osmOperationStatus==OsmNsLcmOperationStatus.PROCESSING) {
            return OperationStatus.PROCESSING;
        }else throw new FailedOperationException("UNKOWN OperationalStatus: "+osmOperationStatus);
    }

    public static TerminateNsRequest getTerminateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest request) {
        TerminateNsRequest translation = new TerminateNsRequest();
        return translation;
    }


}
