#!/bin/bash

if [ ! -d  /var/log/5GCatalogueClient/ ]; then
	sudo mkdir  /var/log/5GCatalogueClient/
    sudo chmod a+rw /var/log/5GCatalogueClient/
fi

mvn clean install
