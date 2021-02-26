package it.nextworks.nfvmano.nfvodriver.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryNsdResponse;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationalState;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NsdFileRegistryService {

    private static final Logger log = LoggerFactory.getLogger(NsdFileRegistryService.class);
    @Autowired
    NsdFileRegistryRepository nsdFileRegistryRepository;

    @Value("${nfvo.catalogue.nsdStorage.folder:/tmp/nsd/}")
    private String nsdStorageFolder;


    public String storeNsd(Nsd nsd) throws AlreadyExistingEntityException, FailedOperationException {
        log.debug("Storing Nsd: "+nsd.getNsdIdentifier()+" in internal catalogue");
        Optional<NsdFileRegistry> nsdFileRegistryOptional =   nsdFileRegistryRepository.findByNsdIdentifierAndNsdVersion(nsd.getNsdIdentifier(), nsd.getVersion());
        if(!nsdFileRegistryOptional.isPresent()){
            String nsdInfoId = getNsdInfoId(nsd.getNsdIdentifier(), nsd.getVersion());
            String nsdRelativePath = getNsdRelativePath(nsd.getNsdIdentifier(), nsd.getVersion());
            String nsdPath = getNsdFullPath(nsd.getNsdIdentifier(), nsd.getVersion());
            log.debug("Storing Nsd:"+nsd.getNsdIdentifier()+" in:"+nsdPath);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(nsdPath), nsd);
                NsdFileRegistry nsdFileRegistry = new NsdFileRegistry(nsd.getNsdIdentifier(), nsd.getVersion(), nsd.getNsdName(), nsdRelativePath, nsdInfoId );
                nsdFileRegistryRepository.saveAndFlush(nsdFileRegistry);
                log.debug("Successfully stored Nsd:"+nsd.getNsdIdentifier()+" in internal file repository. NsdInfoId:"+nsdInfoId);
                return nsdInfoId;
            } catch (IOException e) {
                log.error("Error storing NSD in internal catalogue:"+nsd.getNsdIdentifier(), e);
                throw new FailedOperationException(e.getMessage());
            }

        }else throw new AlreadyExistingEntityException("Nsd with nsdIdentifier:"+nsd.getNsdIdentifier()+" already present in DB");

    }


    public NsdInfo queryNsd(String nsdIdentifier, String nsdVersion) throws NotExistingEntityException, FailedOperationException {
        log.debug("Querying Nsd internal repository");
        Optional<NsdFileRegistry> nsdFileRegistryOptional =   nsdFileRegistryRepository.findByNsdIdentifierAndNsdVersion(nsdIdentifier, nsdVersion);
        if(!nsdFileRegistryOptional.isPresent())
            throw  new NotExistingEntityException("No Nsd with id:"+nsdIdentifier+" found on the internal catalogue");

        return buildNsdInfo(nsdFileRegistryOptional.get());


    }


    public NsdInfo queryNsd(String nsdInfoId) throws NotExistingEntityException, FailedOperationException {
        log.debug("Querying Nsd internal repository");
        Optional<NsdFileRegistry> nsdFileRegistryOptional =   nsdFileRegistryRepository.findByNsdInfoId(nsdInfoId);
        if(!nsdFileRegistryOptional.isPresent())
            throw new NotExistingEntityException("NSD with nsdInfoId:"+nsdInfoId+" not found in DB");

        return buildNsdInfo(nsdFileRegistryOptional.get());

    }

    private NsdInfo buildNsdInfo(NsdFileRegistry nsdFileRegistry) throws FailedOperationException {

            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = getNsdFullPath(nsdFileRegistry.getNsdIdentifier(), nsdFileRegistry.getNsdVersion());
            try {

                Nsd storedNsd = objectMapper.readValue(new File(filePath), Nsd.class);
                NsdInfo nsdInfo = new NsdInfo(nsdFileRegistry.getNsdInfoId(),
                        nsdFileRegistry.getNsdIdentifier(),
                        nsdFileRegistry.getNsdName(),
                        nsdFileRegistry.getNsdVersion(),
                        storedNsd.getDesigner(),
                        storedNsd,
                        null,
                        null,
                        null,
                        OperationalState.ENABLED,
                        UsageState.IN_USE,
                        false,
                        null);
                return nsdInfo;

            } catch (IOException e) {
                log.error("Error reading Nsd:"+nsdFileRegistry.getNsdIdentifier()+" from file:"+filePath,e);
                throw new FailedOperationException(e.getMessage());
            }


    }





    private String getNsdFullPath(String nsdIdentifier, String nsdVersion){

        return nsdStorageFolder+getNsdRelativePath(nsdIdentifier, nsdVersion);
    }


    private String getNsdRelativePath(String nsdIdentifier, String nsdVersion){

        return getNsdInfoId(nsdIdentifier,nsdVersion);
    }


    private String getNsdInfoId(String nsdIdentifier, String nsdVersion){
        return UUID.nameUUIDFromBytes((nsdIdentifier+"_"+nsdVersion).getBytes()).toString();
    }
}
