package it.nextworks.nfvmano.nfvodriver.sol;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import it.nextworks.nfvmano.libs.descriptors.interfaces.LcmOperation;
import it.nextworks.nfvmano.libs.descriptors.interfaces.Nslcm;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NS.NSInterfaces;
import it.nextworks.nfvmano.libs.descriptors.pnfd.nodes.PNF.PNFNode;
import it.nextworks.nfvmano.libs.descriptors.pnfd.nodes.PNF.PNFProperties;
import it.nextworks.nfvmano.libs.descriptors.pnfd.nodes.PNF.PNFRequirements;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.vnfpackagemanagement.elements.PackageUsageStateType;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.vnfpackagemanagement.elements.VnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryNsdResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryOnBoardedVnfPkgInfoResponse;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.elements.KeyValuePair;
import it.nextworks.nfvmano.libs.ifa.common.enums.LcmEventType;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationalState;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.AddressData;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.LifeCycleManagementScript;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.VirtualLinkDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.VirtualLinkProfile;
import it.nextworks.nfvmano.libs.descriptors.elements.ConnectivityType;
import it.nextworks.nfvmano.libs.descriptors.elements.LinkBitrateRequirements;
import it.nextworks.nfvmano.libs.descriptors.elements.VlProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NS.NSNode;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NS.NSProperties;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NS.NSRequirements;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NsVirtualLink.NsVirtualLinkNode;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NsVirtualLink.NsVirtualLinkProperties;
import it.nextworks.nfvmano.libs.descriptors.templates.DataType;
import it.nextworks.nfvmano.libs.descriptors.templates.DescriptorTemplate;
import it.nextworks.nfvmano.libs.descriptors.templates.Metadata;
import it.nextworks.nfvmano.libs.descriptors.templates.Node;
import it.nextworks.nfvmano.libs.descriptors.templates.NodeType;
import it.nextworks.nfvmano.libs.descriptors.templates.SubstitutionMappings;
import it.nextworks.nfvmano.libs.descriptors.templates.SubstitutionMappingsRequirements;
import it.nextworks.nfvmano.libs.descriptors.templates.TopologyTemplate;
import it.nextworks.nfvmano.libs.descriptors.templates.VirtualLinkPair;
import it.nextworks.nfvmano.libs.descriptors.vnfd.nodes.VNF.VNFNode;
import it.nextworks.nfvmano.libs.descriptors.vnfd.nodes.VNF.VNFProperties;
import it.nextworks.nfvmano.libs.descriptors.vnfd.nodes.VNF.VNFRequirements;
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_NATIVE_OBJECT_ID;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_NATIVE_TYPE_ID;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

public class IfaToSolTranslator {

	private static final Logger log = LoggerFactory.getLogger(IfaToSolTranslator.class);
        //SOL : IFA 
	public static DescriptorTemplate translateIfaToSolNsd(Nsd nsd, NsDf nsDf, NsLevel nsIl, SolCatalogueDriver driver) throws FailedOperationException, MalformattedElementException {
		log.debug("Received request to Transalte NSD IFA IL  to SOL:" + nsd.getNsdIdentifier() + " " + nsDf.getNsDfId() + " " + nsIl.getNsLevelId());

		if (nsd.getNestedNsdId() == null || nsd.getNestedNsdId().isEmpty()) {
			log.debug("Translating Nested NSD");
			return translateIfaToSolNsdNested(nsd, nsDf, nsIl, driver);
		}else{
			log.debug("Translating Composite NSD");

			return translateIfaToSolNsdComposite(nsd, nsDf, nsIl, driver);
		}
	}
	private static DescriptorTemplate translateIfaToSolNsdNested(Nsd nsd, NsDf nsDf, NsLevel nsIl, SolCatalogueDriver driver) throws FailedOperationException{
		/*
		 * toscaDefinitionVersion : - (constant)
		 * toscaDefaultNamespace  : - (constant)
		 * description : nsDf > nsInstantiationLevel > description 
		*/
		String toscaDefinitionsVersion =  "tosca_simple_yaml_1_2" ;		
		String toscaDefaultNamespace = "toscanfv";
		String description = nsIl.getDescription();
		
		/*
		 * metadata
		 * 		descriptorId : nsdInvariantId + nsDf > nsDfId + nsDf > nsInstantiationLevel > nsLevelId *TO BE CLARIFIED*
		 * 		vendor : designer
		 * 		version : version
		 */
        String nsDescriptorId = getNsDescriptorId(nsd, nsDf, nsIl);
        Metadata metadata = new Metadata( nsDescriptorId , nsd.getDesigner(), nsd.getVersion());
        
        /*
         * dataTypes : -
         * nodeTypes : -
         * imports : -
         */
        Map<String, DataType> dataTypes;
        Map<String, NodeType> nodeTypes;
        List<String> imports;

        /*
         * topologyTemplate
         */
        
        TopologyTemplate topologyTemplate = new TopologyTemplate();
        
        /*
         * topologyTemplate
         * 		sobstitutionMappings
         * 			nodeType : - (constant)
         * 			requirements
         * 				virtualLink (map): ( sapd > cpdId - sapd > nsVirtualLinkDescId) *TO BE CLARIFIED*
         */
        SubstitutionMappings substituitionMappings = new SubstitutionMappings();
        substituitionMappings.setNodeType("tosca.nodes.nfv.NS");
        SubstitutionMappingsRequirements requirements = new SubstitutionMappingsRequirements();
        List<VirtualLinkPair> virtualLink = new ArrayList<>();
        for (Sapd sapd : nsd.getSapd()) {
            VirtualLinkPair virtualLinkPair = new VirtualLinkPair(sapd.getCpdId(), sapd.getNsVirtualLinkDescId());
            virtualLink.add(virtualLinkPair);
        }
        requirements.setVirtualLink(virtualLink);
        substituitionMappings.setRequirements(requirements);
        topologyTemplate.setSubstituitionMappings(substituitionMappings);
      
        /*
         * tolopogyTemplate
         * 		nodeTemplates
         * 			NSNode_NAME : nsdId_nsDf> nsDfId_nsDf>nsInstantiationLevel>nsLevelId *TO BE CLARIFIED*
         * 				type : NSNode default
         * 				properties 
         * 					descriptorId : nsdInvariantId + nsDf > nsDfId + nsDf > nsInstantiationLevel > nsLevelId *TO BE CLARIFIED* *TO BE CLARIFIED*
         * 					designer : designer
		 * 					version : version
		 * 					name : nsDf > nsProfile > nsdId
		 * 					invariantId : nsdInvariantId + nsDf > nsDfId + nsDf > nsInstantiationLevel > nsLevelId *TO BE CLARIFIED* *TO BE CLARIFIED*
		 *				requirements
		 *					virtualLink (list): nsDf > virtualLinkProfile > virtualLinkDescId  //OVERSEED: ONLY NEEDED TO MAP VIRTUAL LINKS OF COMPOSED NSDS
         */
        Map<String, Node> nodeTemplates = new HashMap<>();    
        //Set NS Node
        NSNode nsNode = new NSNode(null, null, null);

        NSProperties nsProperties = new NSProperties(nsDescriptorId, nsd.getDesigner(), nsd.getVersion(), ( nsd.getNsdIdentifier() + "_" + nsd.getNsDf().get(0).getNsDfId() + "_" + nsIl.getNsLevelId() ), ( nsd.getNsdInvariantId() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ));
        nsNode.setProperties(nsProperties);

		List<String> virtualLinkProfileIds = new ArrayList<>();
		for (VirtualLinkToLevelMapping virtualLinkToLevelMapping : nsIl.getVirtualLinkToLevelMapping() ) {
			virtualLinkProfileIds.add(virtualLinkToLevelMapping.getVirtualLinkProfileId());
		}
		Map<String, String> vlProfileToLinkDesc = new HashMap();
		for (String vlProfileId : virtualLinkProfileIds) {
			for (VirtualLinkProfile virtualLinkProfile : nsDf.getVirtualLinkProfile()) {
				if (vlProfileId.equals(virtualLinkProfile.getVirtualLinkProfileId())) {
					vlProfileToLinkDesc.put(vlProfileId,virtualLinkProfile.getVirtualLinkDescId() );

				}
			}
		}
        
    	//NSRequirements nsRequirements = getNsRequirements(nsd, nsDf, nsIl, virtualLinkProfileIds, vlProfileToLinkDesc);
        NSRequirements nsRequirements = null;
		NSInterfaces nsInterfaces = getNsInterfaces(nsd);
		nsNode.setInterfaces(nsInterfaces);
        nsNode.setRequirements(nsRequirements);


        nodeTemplates.put(( nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ), nsNode);

        List<String> vnfProfileId = new ArrayList<>();
   		for (VnfToLevelMapping vnfToLevelMapping : nsIl.getVnfToLevelMapping() ) {
        	vnfProfileId.add(vnfToLevelMapping.getVnfProfileId());
        }



        for (String vProfileId : vnfProfileId) {

        	for (VnfProfile vnfProfile : nsDf.getVnfProfile()) {
        		if (vProfileId.equals(vnfProfile.getVnfProfileId())) {
        			VNFNode vnfNode = getVNFNode(vnfProfile, vlProfileToLinkDesc, nsd, driver);
        			nodeTemplates.put(vnfProfile.getVnfdId(), vnfNode);
        		}
        	}
        }

        for(String pnfdId : nsd.getPnfdId()){
        	PNFNode pnfNode = getPnfNode(nsd, nsDf, nsIl, pnfdId, vlProfileToLinkDesc);
        	nodeTemplates.put(pnfdId, pnfNode);
		}



		for (Map.Entry<String, String> entry : vlProfileToLinkDesc.entrySet()) {
			String vlProfileId = entry.getKey();
			String virtualLinkDescId = entry.getValue();
			NsVirtualLinkNode vlNode = getVLNode(nsd,nsDf, vlProfileId, virtualLinkDescId);
			nodeTemplates.put(virtualLinkDescId, vlNode);

		}

       	topologyTemplate.setNodeTemplates(nodeTemplates);
        DescriptorTemplate descriptorTemplate = new DescriptorTemplate(toscaDefinitionsVersion, toscaDefaultNamespace, description, metadata, topologyTemplate);
        return descriptorTemplate;
	}



	private static DescriptorTemplate translateIfaToSolNsdComposite(Nsd nsd, NsDf nsDf, NsLevel nsIl, SolCatalogueDriver driver) throws MalformattedElementException, FailedOperationException {
		/*
		 * toscaDefinitionVersion : - (constant)
		 * toscaDefaultNamespace  : - (constant)
		 * description : nsDf > nsInstantiationLevel > description
		 */
		String toscaDefinitionsVersion =  "tosca_simple_yaml_1_2" ;
		String toscaDefaultNamespace = "toscanfv";
		String description = nsIl.getDescription();

		/*
		 * metadata
		 * 		descriptorId : nsdInvariantId + nsDf > nsDfId + nsDf > nsInstantiationLevel > nsLevelId *TO BE CLARIFIED*
		 * 		vendor : designer
		 * 		version : version
		 */
		String nsDescriptorId = getNsDescriptorId(nsd, nsDf, nsIl);
		Metadata metadata = new Metadata( nsDescriptorId , nsd.getDesigner(), nsd.getVersion());

		TopologyTemplate topologyTemplate = new TopologyTemplate();
		SubstitutionMappings substituitionMappings = new SubstitutionMappings();
		substituitionMappings.setNodeType("tosca.nodes.nfv.NS");
		SubstitutionMappingsRequirements requirements = new SubstitutionMappingsRequirements();
		Map<String, Node> nodeTemplates = new HashMap<>();

		for( NsToLevelMapping nsToLevelMapping : nsIl.getNsToLevelMapping()){
			String nsProfileId = nsToLevelMapping.getNsProfileId();
			Optional<NsProfile> nsProfile = nsDf.getNsProfile().stream()
					.filter(p -> p.getNsProfileId().equals(nsProfileId))
					.findFirst();
			if(nsProfile.isPresent()){
				String nestedNsdId = nsProfile.get().getNsdId();
				String nestedNsDf = nsProfile.get().getNsDeploymentFlavourId();
				String nestedNsIl = nsProfile.get().getNsInstantiationLevelId();
				String nestedNsdIdSol = getNsDescriptorId(nestedNsdId, nestedNsDf, nestedNsIl);
				Map<String, String> filterParams = new HashMap<>();
				log.debug("Retrieving correspondent NSD for:"+nestedNsdId+" "+nestedNsDf+" "+nestedNsIl+" -"+nestedNsdIdSol);
				filterParams.put("NSD_ID", nestedNsdId);
				filterParams.put("NS_DF", nestedNsDf);
				filterParams.put("NS_IL", nestedNsIl);
				GeneralizedQueryRequest query = new GeneralizedQueryRequest(new Filter(filterParams), new ArrayList<>());
				try {
					QueryNsdResponse response = driver.queryNsd(query);
					if(response!=null && !response.getQueryResult().isEmpty()){
						String nestedNsdDesigner = response.getQueryResult().get(0).getDesigner();
						String nestedNsdName = response.getQueryResult().get(0).getName();
						String nestedNsdInvariantId = nestedNsdIdSol;
						float nestedNsdVersion = Float.parseFloat(response.getQueryResult().get(0).getVersion());
						for(NsdInfo nsdInfo: response.getQueryResult()){
							if(nestedNsdVersion<Float.parseFloat(nsdInfo.getVersion())){
								nestedNsdVersion=Float.parseFloat(nsdInfo.getVersion());
								nestedNsdDesigner = nsdInfo.getDesigner();
								nestedNsdName = nsdInfo.getName();
							}
						}
						log.debug("Retrieved version for NSD:"+nestedNsdVersion);
						NSProperties nsProperties=new NSProperties(nestedNsdIdSol, nestedNsdDesigner, Float.toString(nestedNsdVersion), nestedNsdName, nestedNsdInvariantId);

						//Set NS Node
						NSNode nsNode = new NSNode(null, nsProperties, null);
						nodeTemplates.put(( nestedNsdId + "_" + nestedNsDf + "_" + nestedNsIl ), nsNode);
					}else throw new FailedOperationException("Error while retrieving correspondent NSD from catalogue. Empty query return");
				} catch (MethodNotImplementedException e) {
					log.error("", e);
				} catch (NotExistingEntityException e) {
					log.error("",e);
					throw new MalformattedElementException("Error while retrieving correspondent NSD from catalogue", e);
				} catch (FailedOperationException e) {
					throw new FailedOperationException("Error while retrieving correspondent NSD from catalogue", e);
				}
			}else throw new MalformattedElementException("NS Profile NOT FOUND:"+nsProfileId);

		}

		log.debug("Generating end-to-end NS Node");
		NSProperties nsProperties=new NSProperties(nsDescriptorId, nsd.getDesigner(), nsd.getVersion(), nsd.getNsdName(), nsd.getNsdInvariantId());

		//Set NS Node
		NSNode nsNode = new NSNode(null, nsProperties, null);
		nodeTemplates.put(( nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ), nsNode);
		topologyTemplate.setNodeTemplates(nodeTemplates);
		DescriptorTemplate descriptorTemplate = new DescriptorTemplate(toscaDefinitionsVersion, toscaDefaultNamespace, description, metadata, topologyTemplate);
		return descriptorTemplate;
	}




	private static VNFNode getVNFNode(VnfProfile vnfProfile, Map<String, String> vlProfileToLinkDesc, Nsd nsd, SolCatalogueDriver driver) throws FailedOperationException {

		/*
		 * tolopogyTemplate
		 * 		nodeTemplates
		 * 			VNF_NAME : nsDf > vnfProfile > vnfdId
		 * 				type : VNFNode default
		 * 				properties
		 * 					descriptorId : nsDf > vnfProfile > vnfProfileId *TO BE CLARIFIED*
		 * 					provider : designer ??? (not always the same of the NS)
		 * 					DescriptorVersion : version  ??? (not always the same of the NS)
		 * 					productName : nsDf > vnfProfile > vnfdId
		 * 					softwareVersion : version  ??? (not always the same of the NS)
		 * 					productInfoName : ???
		 * 					productInfoDescription : ???
		 * 					vnfmInfo (list) : ???
		 * 					localizationLanguages (list) : ???
		 * 					defaultLocalizationLanaguage : ???
		 * 					configurableProperties : ???
		 * 					flavourId : nsDf > vnfProfile > flavourId
		 * 					flavourDescription : ???
		 *				requirements
		 *					virtualLink (list): nsDf > virtualLinkProfile > virtualLinkDescId (when nsDf > virtualLinkProfile > virtualLinkProfileId == nsDf > vnfProfile > nsVirtualLinkConnectivity > virtualLinkProfileId)
		 */

		String descriptorId = getVnfDescriptorId(vnfProfile);
		String version = getVnfPackageVersion(vnfProfile.getVnfdId(), driver);
		String provider = nsd.getDesigner();
		String productName = vnfProfile.getVnfdId();
		String flavourId = vnfProfile.getFlavourId();



		Map<String, String> vnfVirtualLink= new HashMap<>();
		//VnfPkgInfo currentPackage =  solCatalogueDriver.getVnfdIdPackageInfo(vnfProfile.getVnfdId());
		//DescriptorTemplate vnfTemplate = solCatalogueDriver.getVNFD(currentPackage.getId().toString());
		//List<VirtualLinkPair> vnfLinks = vnfTemplate.getTopologyTemplate().getSubstituitionMappings().getRequirements().getVirtualLink();
                    /*for(VirtualLinkPair vPair : vnfLinks){
                        vnfVirtualLink.put(vPair.getVl(), vPair.getCp());
                    }*/

		for (NsVirtualLinkConnectivity nsVirtualLinkConnectivity:vnfProfile.getNsVirtualLinkConnectivity()){

			String vlDesc = vlProfileToLinkDesc.get(nsVirtualLinkConnectivity.getVirtualLinkProfileId());
			for (String cpId : nsVirtualLinkConnectivity.getCpdId()){

				vnfVirtualLink.put(cpId, vlDesc);

			}
		}

		it.nextworks.nfvmano.libs.descriptors.elements.VnfProfile solVnfProfile = null;

		solVnfProfile = new it.nextworks.nfvmano.libs.descriptors.elements.VnfProfile(vnfProfile.getInstantiationLevel(),
				vnfProfile.getMinNumberOfInstances(),
				vnfProfile.getMaxNumberOfInstances()
		) ;




		VNFProperties vnfProperties = new VNFProperties(descriptorId, version, provider, productName, "", productName, "", null, null, null,
				null, null, null, null, flavourId, "", solVnfProfile);

		VNFRequirements vnfRequirements = new VNFRequirements(vnfVirtualLink);
		VNFNode vnfNode = new VNFNode(null, null, null,null, null, null);
		vnfNode.setProperties(vnfProperties);
		vnfNode.setRequirements(vnfRequirements);
		return vnfNode;
	}


	private static String getVnfPackageVersion(String vnfdId, SolCatalogueDriver driver) throws FailedOperationException {
		Map<String, String> filterParams = new HashMap<>();
		filterParams.put("VNFD_ID", vnfdId);
		GeneralizedQueryRequest query = new GeneralizedQueryRequest(new Filter(filterParams), new ArrayList<>());
		try {
			QueryOnBoardedVnfPkgInfoResponse response = driver.queryVnfPackageInfo(query);
			if(!response.getQueryResult().isEmpty()){
				double maxVersion = 0.0;
				for(OnboardedVnfPkgInfo pkgInfo : response.getQueryResult()){
					double currentVersion = Double.parseDouble(pkgInfo.getVnfdVersion());
					if(maxVersion<currentVersion){
						maxVersion=currentVersion;
					}
				}
				return Double.toString(maxVersion);
			}else throw new FailedOperationException("Empty VnfPackage query result");

		} catch (MethodNotImplementedException e) {
			log.error("Failed to retrieve VNF Package version:"+vnfdId, e);
			throw new FailedOperationException(e.getMessage());
		} catch (NotExistingEntityException e) {
			log.error("Failed to retrieve VNF Package version:"+vnfdId, e);
			throw new FailedOperationException(e.getMessage());
		} catch (MalformattedElementException e) {
			log.error("Failed to retrieve VNF Package version:"+vnfdId, e);
			throw new FailedOperationException(e.getMessage());
		}

	}

	private static NsVirtualLinkNode getVLNode(Nsd nsd, NsDf nsDf, String virtualLinkProfileId, String virtualLinkDescriptorId) throws FailedOperationException {
		NsVirtualLinkDesc virtualLinkDesc = getNsVirtualLinkDesc(nsd, virtualLinkDescriptorId);
		VirtualLinkProfile virtualLinkProfile = null;
		try {
			virtualLinkProfile = nsDf.getVirtualLinkProfile(virtualLinkProfileId);
		} catch (NotExistingEntityException e) {
			throw new FailedOperationException(e.getMessage());
		}
		NsVirtualLinkNode vlNode = new 	NsVirtualLinkNode(null,null, null);
		NsVirtualLinkProperties nsVirtualLinkProperties = new NsVirtualLinkProperties();
		nsVirtualLinkProperties.setDescription(virtualLinkDesc.getDescription());
		VlProfile vlProfile = new VlProfile();
		for (VirtualLinkDf virtualLinkDf : virtualLinkDesc.getVirtualLinkDf()) {
			if (virtualLinkDf.getFlavourId().equals(virtualLinkProfile.getFlavourId())) {
				//LinkBitrateRequirements maxBitrateRequirements = new LinkBitrateRequirements();
				//LinkBitrateRequirements minBitrateRequirements = new LinkBitrateRequirements();
			}
		}
		ConnectivityType connectivityType = new ConnectivityType();
		nsVirtualLinkProperties.setVlProfile(vlProfile);
		nsVirtualLinkProperties.setConnectivityType(connectivityType);
		nsVirtualLinkProperties.setExternalNet(isVirtualLinkExternal(nsd, virtualLinkDescriptorId));
		nsVirtualLinkProperties.setMgmtNet(isVirtualLinkManagement(nsd, virtualLinkDescriptorId));
		vlNode.setProperties(nsVirtualLinkProperties);
		return vlNode;
	}

	/**
	 * returns true if there is one Sap connected ot the virtual link
	 * @param nsd
	 * @return
	 */
	private static boolean isVirtualLinkManagement(Nsd nsd, String virtualLinkDescId ){
		log.debug("Determining if link is managmenet");
		List<Sapd> saps = nsd.getSapd();
		if(saps!=null&& !saps.isEmpty()){
			for(Sapd sap: saps){
				if(sap.getNsVirtualLinkDescId().equals(virtualLinkDescId))
					for(AddressData address: sap.getAddressData()){
						if(address.isManagement())
							return true;
					}

			}
		}
		return false;
	}

	private static NSRequirements getNsRequirements(Nsd nsd, NsDf nsDf, NsLevel nsIl , List<String> virtualLinkProfileIds, Map<String, String> vlProfileToLinkDesc){

        /**
         * This is only needed for composite nsd to map the links of the composed to the ones
         * of the non-composed ones
         *
	    List<String> nsVirtualLinks = new ArrayList<>();
		for (String vlProfileId : virtualLinkProfileIds) {
			nsVirtualLinks.add(vlProfileToLinkDesc.get(vlProfileId));
		}

         */
		return new NSRequirements(new HashMap<>());

	}

	private static NSInterfaces getNsInterfaces (Nsd nsd) throws FailedOperationException {


		//Extract NS LCM scripts
		/*
		 * tolopogyTemplate
		 * 		nodeTemplates
		 * 			<NS_NODE>
		 * 				interfaces:
		 * 					NsLcm:
		 * 						<event>:
		 * 							implementation:<implementation> <- <event>-script
		 * 							inputs:
		 * 								<NOT_SUPPORTED>
		 *
		 */
		LcmOperation instantiate, instantiateStart, instantiateEnd, terminate,
			terminateStart, terminateEnd, update, updateStart, updateEnd, heal,
			healStart, healEnd, scale, scaleStart, scaleEnd;
		instantiate=instantiateStart=instantiateEnd=terminate=terminateStart=terminateEnd=
				update=updateStart=updateEnd=heal=healStart=healEnd=scale=scaleStart=scaleEnd=null;

		for(LifeCycleManagementScript lcmScript : nsd.getLifeCycleManagementScript()){
			for(LcmEventType eventType : lcmScript.getEvent()){
				String implementation = eventType.toString()+"-script";
				switch (eventType){
					case START_NS_INSTANTIATION:
						if(instantiateStart==null){

							instantiateStart= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case END_NS_INSTANTIATION:
						if(instantiateEnd==null){

							instantiateEnd= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case START_NS_TERMINATION:
						if(terminateStart==null){

							terminateStart= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case END_NS_TERMINATION:
						if(terminateEnd==null){

							terminateEnd= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case START_NS_UPDATE:
						if(updateStart==null){

							updateStart= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case END_NS_UPDATE:
						if(updateEnd==null){
							updateEnd= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case START_NS_HEALING:
						if(healStart==null){
							healStart= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case END_NS_HEALING:
						if(healEnd==null){
							healEnd= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case START_NS_SCALING:
						if(scaleStart==null){
							scaleStart= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					case END_NS_SCALING:
						if(scaleEnd==null){
							scaleEnd= new LcmOperation(implementation);
						}else throw new FailedOperationException("Duplicate definition of LCM:"+eventType+" NOT SUPPORTED");
						break;
					default:
						throw  new FailedOperationException("NS LCM event mapping not supported: "+eventType);

				}

			}


		}

		Nslcm nslcm = new Nslcm(instantiate, instantiateStart, instantiateEnd, terminate, terminateStart, terminateEnd,
				update, updateStart, updateEnd, heal, healStart, healEnd, scale, scaleStart, scaleEnd);
		NSInterfaces nsInterfaces = new NSInterfaces(null, nslcm);
		return nsInterfaces;
	}



	private static PNFNode getPnfNode(Nsd nsd, NsDf nsDf, NsLevel nsIl, String pnfdId, Map<String, String> vlProfileToLinkDesc) throws FailedOperationException {

		/*
		 * tolopogyTemplate
		 * 		nodeTemplates
		 * 			PNF_NAME : nsd.pnfdId
		 * 				type : PNFNode default
		 * 				properties
		 * 					descriptorId : pnfdId
		 * 					provider : nsd.designer
		 * 					DescriptorVersion : nsd.version
		 * 					productName : pnfdId
		 * 					softwareVersion : version  ??? (not always the same of the NS)
		 * 					productInfoName : ???
		 * 					productInfoDescription : ???
		 * 					localizationLanguages (list) : ???
		 * 					defaultLocalizationLanaguage : ???
		 * 					configurableProperties : ???
		 * 					flavourId : ??
		 * 					flavourDescription : ???
		 *				requirements
		 *					virtualLink (list): nsDf > virtualLinkProfile > virtualLinkDescId (when nsDf > virtualLinkProfile > virtualLinkProfileId == nsDf > pnfProfile > nsVirtualLinkConnectivity > virtualLinkProfileId)
		 */


		log.debug("Computing PNFNodes");
		ArrayList<PNFNode> pnfNodes = new ArrayList<>();

		String descriptorId = pnfdId;


		String provider = nsd.getDesigner();
		String version =nsd.getVersion();
		String descriptorInvariantId = descriptorId;
		String name = pnfdId;

		PNFProperties pnfProperties = new PNFProperties(null, descriptorId, null,provider, version,  descriptorInvariantId, name,null );
		PnfProfile pnfProfile = getPnfProfile(pnfdId, nsDf);
		Map<String, String> pnfVirtualLink = new HashMap<>();
		for (NsVirtualLinkConnectivity nsVirtualLinkConnectivity:pnfProfile.getNsVirtualLinkConnectivity()){

			String vlDesc = vlProfileToLinkDesc.get(nsVirtualLinkConnectivity.getVirtualLinkProfileId());
			for (String cpId : nsVirtualLinkConnectivity.getCpdId()){

				pnfVirtualLink.put(cpId, vlDesc);

			}
		}

		PNFRequirements pnfRequirements = new PNFRequirements(pnfVirtualLink);
		PNFNode pnfNode = new PNFNode(null, pnfProperties, pnfRequirements);
		return pnfNode;

	}

	private static PnfProfile getPnfProfile(String pnfdId, NsDf nsDf){

		for(PnfProfile profile : nsDf.getPnfProfile()){
			if(profile.getPnfdId().equals(pnfdId)){
				return profile;
			}
		}
		return null;
	}
	private static NsVirtualLinkDesc getNsVirtualLinkDesc(Nsd nsd, String virtualLinkDescId) throws FailedOperationException {

		Optional<NsVirtualLinkDesc> optionalNsVirtualLinkDesc = nsd.getVirtualLinkDesc().stream()
				.filter(linkDesc -> linkDesc.getVirtualLinkDescId().equals(virtualLinkDescId))
						.findFirst();
		if(optionalNsVirtualLinkDesc.isPresent()){
			return optionalNsVirtualLinkDesc.get();
		}else throw new FailedOperationException("Cannot find virtual link desc with id:"+virtualLinkDescId+" in NSD:"+nsd.getNsdIdentifier());


	}


	public static String createCsarPackageForNsdDfIl(Nsd nsd, NsDf nsDf, NsLevel nsIl, SolCatalogueDriver driver) throws FailedOperationException, MalformattedElementException {

		DescriptorTemplate descriptorTemplate = translateIfaToSolNsd(nsd, nsDf, nsIl, driver);
		String nsDescriptorId = getNsDescriptorId(nsd.getNsdIdentifier(), nsDf.getNsDfId(), nsIl.getNsLevelId());
		File csarFolder = null;

		try {
			csarFolder = File.createTempFile(nsDescriptorId, "");
			csarFolder.delete();
		} catch (IOException e) {
			log.error("Error during CSAR folder creation!", e);
			throw new FailedOperationException(e.getMessage());

		}
		boolean createdCsarFolder = csarFolder.mkdir();
		if(!createdCsarFolder) throw new FailedOperationException("Failed to create CSAR Folder");
		else {
			log.debug("Created CSAR Folder:"+csarFolder.getAbsolutePath());
		}
		File definitionFolder = new File(csarFolder.getAbsolutePath()+File.separator+"Definitions");
		boolean createdDefFolder = definitionFolder.mkdir();
		String definitionFileRelativePath="Definitions"+ File.separator+nsDescriptorId+".yaml";
		if(!createdDefFolder) throw new FailedOperationException("Failed to create Definitions Folder");
		try {
			createNsdFileDefinition(descriptorTemplate, csarFolder.getAbsolutePath()+File.separator+definitionFileRelativePath);
		} catch (IOException e) {
			log.error("Error during NSD definition  creation!", e);
			throw new FailedOperationException(e.getMessage());
		}

		File scriptsFolder = new File(csarFolder.getAbsolutePath()+File.separator+"Files"+File.separator+"Scripts"+File.separator);
		scriptsFolder.mkdirs();

		List<String> fileList;
		try {


			fileList=createNsdLcmScripts(nsd, scriptsFolder.getAbsolutePath());
		} catch (IOException e) {
			log.error("Error during NSD LCM script definition  creation!", e);
			throw new FailedOperationException(e.getMessage());
		}

		File toscaMetadata = new File(csarFolder.getAbsolutePath()+File.separator+"TOSCA-Metadata");
		toscaMetadata.mkdir();
		try {
			createToscaMetadata(toscaMetadata.getAbsolutePath(), nsd, definitionFileRelativePath);
		} catch (IOException e) {
			log.error("Error during Tosca METADATA definition  creation!", e);
			throw new FailedOperationException(e.getMessage());
		}

		try {
			createToscaServiceTemplate(csarFolder.getAbsolutePath(), nsd, nsDf, nsIl, "", fileList);
		} catch (IOException e) {
			log.error("Error creating Tosca Service template!",e);
			throw new FailedOperationException(e.getMessage());
		}
		String zipFile="";
		try {
			zipFile = compressFolder(csarFolder.getAbsolutePath());
		} catch (IOException e) {
			log.error("Error compressing CSAR Folder",e);
			throw new FailedOperationException(e.getMessage());
		}
		log.debug("Deleting CSAR folder");
		csarFolder.delete();
		return zipFile;

	}

	private static void createToscaServiceTemplate(String csarFolderPath, Nsd nsd, NsDf nsDf, NsLevel nsIl, String cloudInitFile, List<String> lcmScripts ) throws IOException {
		String fileName = (new File(csarFolderPath)).getName();
		File toscaServiceTemplate = new File(csarFolderPath+File.separator+fileName+".mf");

		BufferedWriter writer = new BufferedWriter(new FileWriter(toscaServiceTemplate));
		writer.write("metadata:\n");
		writer.write("\tvnf_product_name: " + getNsDescriptorId(nsd.getNsdIdentifier(), nsDf.getNsDfId(), nsIl.getNsLevelId()) +"\n");
		writer.write("\tvnf_provider: " + nsd.getDesigner() +"\n");
		writer.write("\tvnf_package_version: " + nsd.getVersion() +"\n");

		writer.write("\tvnf_release_date :" + Instant.now().toString()  +"\n");
		writer.write("configuration:\n");
		writer.write("\tscripts:\n");
		for(String lcmScript : lcmScripts){
			String lcmPath = "Files"+File.separator+"Scripts"+File.separator+lcmScript;
			writer.write("\t\tSource: "+lcmPath+"\n");

		}
		writer.close();

	}

	private static void createToscaMetadata(String path, Nsd nsd, String definitionsFile) throws IOException {
		File toscaMetaFile = new File(path+File.separator+"TOSCA.meta");

		BufferedWriter writer = new BufferedWriter(new FileWriter(toscaMetaFile));
		writer.write("TOSCA-Meta-File-Version: 1.0\n");
		writer.write("CSAR-Version: 1.1\n");
		writer.write("CreatedBy: "+nsd.getDesigner()+"\n");
		writer.write("Entry-Definitions: "+definitionsFile+"\n");
		writer.close();

	}

	private static void createNsdFileDefinition (DescriptorTemplate template, String nsDefinitionPath) throws IOException {
		File nsdFile = new File(nsDefinitionPath);
		log.debug("Using file: "+nsdFile.getPath()+" to store NSD: "+template.getMetadata());
		final YAMLFactory yamlFactory = new YAMLFactory()
				.configure(USE_NATIVE_TYPE_ID, false)
				.configure(USE_NATIVE_OBJECT_ID, false)
				.configure(WRITE_DOC_START_MARKER, false);


		ObjectMapper mapper = new ObjectMapper(yamlFactory);

		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String obtainedNsd = mapper.writeValueAsString(template);
		log.debug("Obtained NSD:"+obtainedNsd);
		BufferedWriter writer = new BufferedWriter(new FileWriter(nsdFile));
		writer.write(obtainedNsd);
		writer.close();


	}

	private static List<String> createNsdLcmScripts(Nsd nsd, String scriptsPath) throws IOException {
		ArrayList<String> fileList = new ArrayList<>();
		for(LifeCycleManagementScript lcmScript : nsd.getLifeCycleManagementScript()){
			String command = lcmScript.getScript();
			for(LcmEventType eventType : lcmScript.getEvent()){
				String scriptName = eventType.toString()+"-script";
				File currentScript = new File(scriptsPath+File.separator+scriptName);
				BufferedWriter writer = new BufferedWriter(new FileWriter(currentScript));
				writer.write(command);

				writer.close();
				fileList.add(scriptName);

			}
		}
		return fileList;
	}


	private static String compressFolder(String dirPath) throws IOException {
			log.debug("Compressing CSAR Folder");

			final Path sourceDir = Paths.get(dirPath);
			String zipFileName = dirPath.concat(".zip");

			final ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
			Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

						Path targetFile = sourceDir.relativize(file);
						outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
						byte[] bytes = Files.readAllBytes(file);
						outputStream.write(bytes, 0, bytes.length);
						outputStream.closeEntry();

					return FileVisitResult.CONTINUE;
				}
			});
			outputStream.close();

			return zipFileName;

	}

	private static String getVnfDescriptorId(VnfProfile vnfProfile){
	    //String seed = vnfProfile.getVnfdId();
        //return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
		return vnfProfile.getVnfdId();

    }

    public static String getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
     	return getNsDescriptorId(nsd.getNsdIdentifier(), nsDf.getNsDfId(), nsIl.getNsLevelId());

    }


    private static String getNsDescriptorId(String nsdId, String  nsDfId, String nsIlId){
		String seed = nsdId + "_" + nsDfId + "_" + nsIlId;
		return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
	}

	public static QueryOnBoardedVnfPkgInfoResponse translateQueryVnfPackageInfo(GeneralizedQueryRequest request, List<VnfPkgInfo> vnfPkgInfos) {
		ArrayList<OnboardedVnfPkgInfo> pkgInfos = new ArrayList<>();
		for(VnfPkgInfo vnfPkgInfo : vnfPkgInfos){
			UsageState state;
			if(vnfPkgInfo.getUsageState()== PackageUsageStateType.IN_USE){
				state=UsageState.IN_USE;
			}else{
				state = UsageState.NOT_IN_USE;
			}

			pkgInfos.add(new OnboardedVnfPkgInfo(vnfPkgInfo.getId().toString(),vnfPkgInfo.getVnfdId().toString(),
					vnfPkgInfo.getVnfProvider(), vnfPkgInfo.getVnfProductName(), vnfPkgInfo.getVnfSoftwareVersion(), vnfPkgInfo.getVnfdVersion(),"",null, null,
					 null, OperationalState.ENABLED, state, false, null ));
		}
		QueryOnBoardedVnfPkgInfoResponse response = new QueryOnBoardedVnfPkgInfoResponse(pkgInfos);
		return response;
	}


	public static QueryNsdResponse translateQueryNsdInfoResponse(GeneralizedQueryRequest request, List<it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements.NsdInfo> nsdInfos){
		List<NsdInfo> nsdInfoList = new ArrayList<>();
		Map<String, String> requestParams = request.getFilter().getParameters();
		for(it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements.NsdInfo nsdInfo : nsdInfos){
			NsdInfo ifaNsdInfo = new NsdInfo(nsdInfo.getId().toString(),requestParams.get("NSD_ID"),
					nsdInfo.getNsdName(),
					nsdInfo.getNsdVersion(),
					nsdInfo.getNsdDesigner(),
					null,
					null,
					null,
					null,
					OperationalState.ENABLED,
					UsageState.IN_USE,
					false,
					new HashMap<>());
			nsdInfoList.add(ifaNsdInfo);
		}

		QueryNsdResponse response = new QueryNsdResponse(nsdInfoList);
		return response;
	}


	public static GeneralizedQueryRequest translateQueryNsdRequest(GeneralizedQueryRequest request) throws MethodNotImplementedException {
		Map<String, String> parameters = request.getFilter().getParameters();
		if(parameters.containsKey("NSD_ID")&& parameters.containsKey("NS_DF")&& parameters.containsKey("NS_IL")){
			String solNsdId = getNsDescriptorId(parameters.get("NSD_ID"),parameters.get("NS_DF"), parameters.get("NS_IL") );
			Map<String, String> solParams = new HashMap<>();
			solParams.put("NSD_ID", solNsdId);
			return new GeneralizedQueryRequest(new Filter(solParams), null);


		}else throw new MethodNotImplementedException("Unsupported query filter. SolCatalogueDriver requires the query NSD to include the DF and IL due to the IFA-SOL mapping");
	}


    /**
     * returns true if there is one Sap connected ot the virtual link
     * @param nsd
     * @return
     */
	private static boolean isVirtualLinkExternal(Nsd nsd, String virtualLinkDescId ){
	    log.debug("Determining if link is external");
	    List<Sapd> saps = nsd.getSapd();
	    if(saps!=null&& !saps.isEmpty()){
	        for(Sapd sap: saps){
	            if(sap.getNsVirtualLinkDescId().equals(virtualLinkDescId))
	                return true;
            }
        }
	    return false;
    }
}
