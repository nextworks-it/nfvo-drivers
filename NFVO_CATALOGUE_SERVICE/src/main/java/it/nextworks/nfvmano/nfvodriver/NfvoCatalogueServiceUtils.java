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



import it.nextworks.nfvmano.nfvodriver.logging.NfvoCatalogueLoggingDriver;
import it.nextworks.nfvmano.nfvodriver.timeo.TimeoCatalogueDriver;
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


    @Autowired
    NfvoCatalogueService nfvoCatalogueService;

    @PostConstruct
    public void initNfvoCatalogueDriver() {
        log.debug("Initializing NFVO CATALOGUE driver for type:" + nfvoCatalogueType);
        if (nfvoCatalogueType.equals("LOGGING")) {

            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new NfvoCatalogueLoggingDriver());
        }else if (nfvoCatalogueType.equals("TIMEO")) {
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new TimeoCatalogueDriver(nfvoCatalogueAddress, null));
        }else if(nfvoCatalogueType.equals("DUMMY")){
            log.debug("Configured for type:" + nfvoCatalogueType);
            nfvoCatalogueService.setNfvoCatalogueDriver(new DummyNfvoCatalogueDriver(nfvoCatalogueAddress));
        }else {
            log.error("NFVO not configured!");
        }
    }
}
