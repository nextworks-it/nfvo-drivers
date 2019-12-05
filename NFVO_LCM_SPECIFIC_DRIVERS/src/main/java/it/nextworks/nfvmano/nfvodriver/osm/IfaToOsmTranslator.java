package it.nextworks.nfvmano.nfvodriver.osm;

import io.swagger.client.model.CreateNSinstanceContentRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest;

import java.util.UUID;

public class IfaToOsmTranslator {



    public static CreateNSinstanceContentRequest getCreateNSinstanceContentRequest(CreateNsIdentifierRequest request){

        Nsd nsd = request.get
        CreateNSinstanceContentRequest translation = new CreateNSinstanceContentRequest(request.getNsdId());
    }







    private static UUID getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
        String seed = nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId();
        return UUID.nameUUIDFromBytes(seed.getBytes());
    }
}
