/*
* Copyright 2018 Nextworks s.r.l.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package it.nextworks.nfvmano.nfvodriver;



import it.nextworks.nfvmano.nfvodriver.file.DummyFileNfvoCatalogueDriver;
import it.nextworks.nfvmano.nfvodriver.file.NsdFileRegistryService;
import it.nextworks.nfvmano.nfvodriver.file.VnfdFileRegistryService;
import it.nextworks.nfvmano.nfvodriver.logging.NfvoCatalogueLoggingDriver;
import it.nextworks.nfvmano.nfvodriver.osm.OsmCatalogueDriver;
import it.nextworks.nfvmano.nfvodriver.sol.SolCatalogueDriver;
import it.nextworks.nfvmano.nfvodriver.timeo.TimeoCatalogueDriver;
import it.nextworks.nfvmano.nfvodriver.dummy.DummyNfvoCatalogueDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NfvoCatalogueServiceUtils {

    private static final Logger log = LoggerFactory.getLogger(NfvoCatalogueService.class);

    @Value("${nfvo.catalogue.type}")
    private String nfvoCatalogueType;

    @Value("${nfvo.catalogue.address}")
    private String nfvoCatalogueAddress;

    
    @Value("${nfvo.catalogue.username:admin}")
    private String nfvoCatalogueUsername;
    
    @Value("${nfvo.catalogue.password:admin}")
    private String nfvoCataloguePassword;
    
    @Value("${nfvo.catalogue.project:admin}")
    private String nfvoCatalogueProject;

    @Value("${nfvo.catalogue.id:5g-catalogue}")
    private String nfvoCatalogueId;

    @Value("${sebastian.localTmpDir:/tmp}")
    private String tmpDir;

    @Value("${nfvo.catalogue.nsdStorage.enabled:false}")
    private boolean nsdFileStorageEnabled;


    @Autowired
    NfvoCatalogueService nfvoCatalogueService;

    @Autowired
    NsdFileRegistryService nsdFileRegistryService;

    @Autowired
    VnfdFileRegistryService vnfdFileRegistryService;

    @PostConstruct
    public void initNfvoCatalogueDriver() {
        log.debug("Initializing NFVO CATALOGUE driver for type:" + nfvoCatalogueType);
        if (nfvoCatalogueType.equals("LOGGING")) {

            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new NfvoCatalogueLoggingDriver());
        } else if (nfvoCatalogueType.equals("TIMEO")) {
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new TimeoCatalogueDriver(nfvoCatalogueAddress, null));
        } else if(nfvoCatalogueType.equals("DUMMY")){
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new DummyNfvoCatalogueDriver(nfvoCatalogueAddress, tmpDir));
        } else if(nfvoCatalogueType.equals("OSM")){
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new OsmCatalogueDriver(nfvoCatalogueAddress, nfvoCatalogueUsername, nfvoCataloguePassword, nfvoCatalogueProject, null, nsdFileRegistryService, vnfdFileRegistryService));
        } else if(nfvoCatalogueType.equals("SOL_005")) {
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new SolCatalogueDriver(nfvoCatalogueAddress, nfvoCatalogueUsername, nfvoCataloguePassword, nfvoCatalogueProject, nfvoCatalogueId, null));
        }else if(nfvoCatalogueType.equals("DUMMY_FILE")){
            //only used for test purposes
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new DummyFileNfvoCatalogueDriver(nsdFileRegistryService, vnfdFileRegistryService, tmpDir));
        } else {
            log.error("NFVO not configured!");
        }
    }
}
