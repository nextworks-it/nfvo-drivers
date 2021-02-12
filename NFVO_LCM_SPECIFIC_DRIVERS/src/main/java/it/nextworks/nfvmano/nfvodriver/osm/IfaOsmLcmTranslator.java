package it.nextworks.nfvmano.nfvodriver.osm;

import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest;

import java.util.UUID;

public class IfaOsmLcmTranslator {



    /*public static CreateNsRequest getCreateNsRequest(CreateNsIdentifierRequest request, UUID vimId, UUID nsdInfoId){


        CreateNsRequest translation = new CreateNsRequest();
        translation.setNsName(request.getNsName());
        translation.setNsdId(nsdInfoId);
        translation.setVimAccountId(vimId);

        return translation;
    }*/

    public static CreateNsRequest getCreateNsRequest(CreateNsIdentifierRequest request, UUID vimId, UUID nsdId){

        CreateNsRequest translation = new CreateNsRequest();
        translation.setNsName(request.getNsName());
        translation.setNsdId(nsdId);
        translation.setVimAccountId(vimId);
        translation.setNsDescription(request.getNsDescription());

        return translation;

    }

    private static UUID getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
        String seed = nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId();
        return UUID.nameUUIDFromBytes(seed.getBytes());
    }

    /*public static InstantiateNsRequest getInstantiateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest request, UUID nsdInfoId, UUID nsdId, UUID vimId) {
        InstantiateNsRequest translation = new InstantiateNsRequest();
        //TODO: Should we add again the nsdId? would need to be saved in a map
        translation.setVimAccountId(vimId);
        translation.setNsName(request.getNsInstanceId());
        translation.setNsdId(nsdId);
        return translation;

    }*/

    public static InstantiateNsRequest getInstantiateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest request, NsdInfo nsdOsm, UUID vimId) {
        InstantiateNsRequest translation = new InstantiateNsRequest();

        translation.setNsName(nsdOsm.getName());
        translation.setNsdId(nsdOsm.getIdentifier());
        translation.setVimAccountId(vimId);
        return translation;

    }

    public static ScaleNsRequest getScaleOutNsRequest(String indexConstituentVnf, String scalingGroup){

        ScaleNsRequest scaleNsRequest = new ScaleNsRequest();

        scaleNsRequest.put("scaleType","SCALE_VNF");
        ScaleNsRequestScaleVnfData scaleNsRequestScaleVnfData = new ScaleNsRequestScaleVnfData();
        ScaleNsRequestScaleVnfDataScaleByStepData scaleNsRequestScaleVnfDataScaleByStepData = new ScaleNsRequestScaleVnfDataScaleByStepData();

        scaleNsRequestScaleVnfDataScaleByStepData.setMemberVnfIndex(indexConstituentVnf);
        scaleNsRequestScaleVnfDataScaleByStepData.setScalingGroupDescriptor(scalingGroup);

        scaleNsRequestScaleVnfData.setScaleByStepData(scaleNsRequestScaleVnfDataScaleByStepData);
        scaleNsRequestScaleVnfData.setScaleVnfType(ScaleNsRequestScaleVnfData.ScaleVnfTypeEnum.OUT);


        scaleNsRequest.put("scaleVnfData",scaleNsRequestScaleVnfData);

        return scaleNsRequest;
    }

    public static ScaleNsRequest getScaleInNsRequest(String indexConstituentVnf, String scalingGroup) {
        ScaleNsRequest scaleNsRequest = new ScaleNsRequest();

        scaleNsRequest.put("scaleType","SCALE_VNF");
        ScaleNsRequestScaleVnfData scaleNsRequestScaleVnfData = new ScaleNsRequestScaleVnfData();
        ScaleNsRequestScaleVnfDataScaleByStepData scaleNsRequestScaleVnfDataScaleByStepData = new ScaleNsRequestScaleVnfDataScaleByStepData();

        scaleNsRequestScaleVnfDataScaleByStepData.setMemberVnfIndex(indexConstituentVnf);
        scaleNsRequestScaleVnfDataScaleByStepData.setScalingGroupDescriptor(scalingGroup);

        scaleNsRequestScaleVnfData.setScaleByStepData(scaleNsRequestScaleVnfDataScaleByStepData);
        scaleNsRequestScaleVnfData.setScaleVnfType(ScaleNsRequestScaleVnfData.ScaleVnfTypeEnum.IN);

        scaleNsRequest.put("scaleVnfData",scaleNsRequestScaleVnfData);

        return scaleNsRequest;
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
