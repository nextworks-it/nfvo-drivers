package it.nextworks.nfvmano.nfvodriver.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;

import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationalState;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VnfdFileRegistryService {

    private static final Logger log = LoggerFactory.getLogger(VnfdFileRegistryService.class);

    @Autowired
    private VnfdFileRegistryRepository vnfdFileRegistryRepository;

    @Value("${nfvo.catalogue.vnfdStorage.folder:/tmp/vnfd/}")
    private String vnfdStorageFolder;


    public String storeVnfd(OnBoardVnfPackageRequest request, Vnfd vnfd) throws FailedOperationException, AlreadyExistingEntityException {
        log.debug("Storing Vnfd: "+vnfd.getVnfdId()+" in internal catalogue");
        boolean vnfPresent =   vnfdFileRegistryRepository.findByVnfdIdAndVnfdVersion(vnfd.getVnfdId(), vnfd.getVnfdVersion()).isPresent()
                ||  vnfdFileRegistryRepository.findByPackageNameAndPackageProviderAndPackageVersion(request.getName(), request.getProvider(), request.getVersion()).isPresent();;
        if(vnfPresent){
            throw new AlreadyExistingEntityException("Vnfd with vnfdId:"+vnfd.getVnfdId()+" already present in DB");
        }



            String vnfdInfoId = getVnfdInfoId(vnfd.getVnfdId(), vnfd.getVnfdVersion());
            String vnfdRelativePath = getVnfdRelativePath(vnfd.getVnfdId(), vnfd.getVnfdVersion());
            String vnfdPath = getVnfdFullPath(vnfd.getVnfdId(), vnfd.getVnfdVersion());
            log.debug("Storing Vnfd:"+vnfd.getVnfdId()+" in:"+vnfdPath);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(vnfdPath), vnfd);
                VnfdFileRegistry vnfdFileRegistry = new VnfdFileRegistry(vnfd.getVnfdId(), vnfd.getVnfdVersion(), vnfdRelativePath, vnfdInfoId, request.getName(), request.getVersion(), request.getProvider() );
                vnfdFileRegistryRepository.saveAndFlush(vnfdFileRegistry);
                log.debug("Successfully stored Vnfd:"+vnfd.getVnfdId()+" in internal file repository. VnfdInfoId:"+vnfdInfoId);
                return vnfdInfoId;
            } catch (IOException e) {
                log.error("Error storing VNFD in internal catalogue:"+vnfd.getVnfdId(), e);
                throw new FailedOperationException(e.getMessage());
            }



    }


    public OnboardedVnfPkgInfo queryVnf(String vnfdId) throws NotExistingEntityException {
        log.debug("Retrieving VnfPkgInfo for vnf vnfdId:"+vnfdId);
        List<VnfdFileRegistry> vnfdFileRegistries = vnfdFileRegistryRepository.findAllByVnfdId(vnfdId);
        if(vnfdFileRegistries.isEmpty()){
            throw new NotExistingEntityException("VnfPackage not found in the catalogue");
        }
        return buildVnfPkgInfo(vnfdFileRegistries.get(0));
    }

    public OnboardedVnfPkgInfo queryVnf(String name, String provider, String version) throws NotExistingEntityException {
        log.debug("Retrieving VnfPkgInfo for vnf name:"+name+ " provider:"+provider+" version:"+version);
        Optional<VnfdFileRegistry> vnfdFileRegistry = vnfdFileRegistryRepository.findByPackageNameAndPackageProviderAndPackageVersion(name, provider, version);
        if(!vnfdFileRegistry.isPresent()){
            throw new NotExistingEntityException("VnfPackage not found in the catalogue");
        }
        return buildVnfPkgInfo(vnfdFileRegistry.get());
    }

    private OnboardedVnfPkgInfo buildVnfPkgInfo(VnfdFileRegistry vnfdFileRegistry) throws NotExistingEntityException {

        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = getVnfdFullPath(vnfdFileRegistry.getVnfdId(), vnfdFileRegistry.getVnfdVersion());
        try {

            Vnfd storedVnfd = objectMapper.readValue(new File(filePath), Vnfd.class);
            OnboardedVnfPkgInfo vnfPkgInfo = new OnboardedVnfPkgInfo(vnfdFileRegistry.getVnfdInfoId(), vnfdFileRegistry.getVnfdId(),vnfdFileRegistry.getPackageProvider(),
                    vnfdFileRegistry.getPackageName(), vnfdFileRegistry.getPackageVersion(), storedVnfd.getVnfdVersion(), null, storedVnfd,null, null, OperationalState.ENABLED, UsageState.NOT_IN_USE,
                    false, new HashMap<>());
            return vnfPkgInfo;
        } catch (IOException e) {
            log.error("Error reading Vnfd:"+vnfdFileRegistry.getVnfdId()+" from file:"+filePath,e);
            throw new NotExistingEntityException(e.getMessage());
        }

    }


    private String getVnfdFullPath(String vnfdId, String vnfdVersion){

        return vnfdStorageFolder+getVnfdRelativePath(vnfdId, vnfdVersion);
    }

    private String getVnfdRelativePath(String vnfdId, String vnfdVersion){
        return getVnfdInfoId(vnfdId, vnfdVersion);
    }

    private String getVnfdInfoId(String vnfdId, String vnfdVersion){
        return UUID.nameUUIDFromBytes((vnfdId+"_"+vnfdVersion).getBytes()).toString();
    }

}
