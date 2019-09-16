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
package it.nextworks.nfvmano.nfvodriver.guidrivers;

import java.util.Optional;

/**
 * Created by Marco Capitani on 21/03/19.
 *
 * @author Marco Capitani <m.capitani AT nextworks.it>
 */
public class TimeoGUIConnector implements NfvoGuiConnector {

    private String address;
    private int port;

    public TimeoGUIConnector(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public TimeoGUIConnector(String address) {
        this.address = address;
        this.port = 80;
    }

    @Override
    public Optional<String> makeNfvNsUrl(String nfvNsId) {
        return Optional.of(String.format(
                "http://%s:%s/timeo_web_gui/pages/ns/nsi_details.html?nsiId=%s",
                address,
                port,
                nfvNsId
        ));
    }
}
