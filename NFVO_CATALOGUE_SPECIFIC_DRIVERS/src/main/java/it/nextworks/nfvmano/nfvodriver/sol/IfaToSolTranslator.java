package it.nextworks.nfvmano.nfvodriver.sol;

import java.util.*;

import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.vnfpackagemanagement.elements.VnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.common.elements.KeyValuePair;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.VirtualLinkDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.VirtualLinkProfile;
import it.nextworks.nfvmano.libs.descriptors.elements.ConnectivityType;
import it.nextworks.nfvmano.libs.descriptors.elements.LinkBitrateRequirements;
import it.nextworks.nfvmano.libs.descriptors.elements.VlProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsVirtualLinkConnectivity;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsVirtualLinkDesc;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Sapd;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.VirtualLinkToLevelMapping;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.VnfProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.VnfToLevelMapping;
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

public class IfaToSolTranslator {
        //SOL : IFA 
	public static DescriptorTemplate translateIfaToSolNsd(Nsd nsd, NsDf nsDf, NsLevel nsIl, SolCatalogueDriver solCatalogueDriver) throws FailedOperationException {
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
		 *					virtualLink (list): nsDf > virtualLinkProfile > virtualLinkDescId 
         */
        Map<String, Node> nodeTemplates = new HashMap<>();    
        //Set NS Node
        NSNode nsNode = new NSNode("tosca.nodes.nfv.NS", null, null);

        NSProperties nsProperties = new NSProperties(nsDescriptorId, nsd.getDesigner(), nsd.getVersion(), ( nsd.getNsdIdentifier() + "_" + nsd.getNsDf().get(0).getNsDfId() + "_" + nsIl.getNsLevelId() ), ( nsd.getNsdInvariantId() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ));
        nsNode.setProperties(nsProperties);

        //List<String> nsVirtualLink = new ArrayList<>();
        List<String> virtualLinkProfileId = new ArrayList<>();
        for (VirtualLinkToLevelMapping virtualLinkToLevelMapping : nsIl.getVirtualLinkToLevelMapping() ) {
      		virtualLinkProfileId.add(virtualLinkToLevelMapping.getVirtualLinkProfileId());
        }

        Map<String, String> vlProfileToLinkDesc = new HashMap();
    	for (String vlProfileId : virtualLinkProfileId) { 
    		for (VirtualLinkProfile virtualLinkProfile : nsDf.getVirtualLinkProfile()) {
    			if (vlProfileId.equals(virtualLinkProfile.getVirtualLinkProfileId())) {
					//nsVirtualLink.add(virtualLinkProfile.getVirtualLinkDescId());
    				vlProfileToLinkDesc.put(vlProfileId,virtualLinkProfile.getVirtualLinkDescId() );
    			}
        	}
        }
        
    	NSRequirements nsRequirements = new NSRequirements(null);
        nsNode.setRequirements(nsRequirements);
        Node node = nsNode;

        nodeTemplates.put(( nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ), node);
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
        List<String> vnfProfileId = new ArrayList<>();
   		for (VnfToLevelMapping vnfToLevelMapping : nsIl.getVnfToLevelMapping() ) {
        	vnfProfileId.add(vnfToLevelMapping.getVnfProfileId());
        }
        for (String vProfileId : vnfProfileId) {

        	for (VnfProfile vnfProfile : nsDf.getVnfProfile()) {
        		if (vProfileId.equals(vnfProfile.getVnfProfileId())) {
        			VNFProperties vnfProperties = new VNFProperties();
        			vnfProperties.setDescriptorId(getVnfDescriptorId(vnfProfile));
        			vnfProperties.setDescriptorVersion(nsd.getVersion());
        			vnfProperties.setProvider(nsd.getDesigner());
        			vnfProperties.setProductName(vnfProfile.getVnfdId());
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

        			//TODO: Fix this with lorenzo
        			//vnfRequirements.setVirtualLink(vnfVirtualLink);
                    VNFRequirements vnfRequirements = new VNFRequirements(vnfVirtualLink);
        			VNFNode vnfNode = new VNFNode("tosca.nodes.nfv.VNF", null, null,null, null, null);
        			vnfNode.setProperties(vnfProperties);
        			vnfNode.setRequirements(vnfRequirements);
        			nodeTemplates.put(vnfProfile.getVnfdId(), vnfNode);
        		}
        	}
        }     
        /*
         * tolopogyTemplate
         * 		nodeTemplates
         * 			VL_NAME : virtualLinkDesc > virtualLinkDescriptorId
		 *				type : VLNode default
		 *				properties
		 *					descriptorId : nsDf > vnfProfile > vnfProfileId *TO BE CLARIFIED*
		 *					testAccess : ???
		 *					vlProfile
		 *						maxBitrateRequirements / only max = min in IFA
		 *							root : virtualLinkDesc>virtualLinkDf>bitrateRequirements>root
		 *							leaf : virtualLinkDesc>virtualLinkDf>bitrateRequirements>leaf
		 *					connectivityType
		 *						layerProtocols (list): virtualLinkDesc>connectivityType>layerProtocol
		 *						flowPattern : ???
         */


		for (Map.Entry<String, String> entry : vlProfileToLinkDesc.entrySet()) {
			NsVirtualLinkDesc virtualLinkDesc = getNsVirtualLinkDesc(nsd, entry.getValue());
			VirtualLinkProfile virtualLinkProfile = null;
			try {
				virtualLinkProfile = nsDf.getVirtualLinkProfile(entry.getKey());
			} catch (NotExistingEntityException e) {
				throw new FailedOperationException(e.getMessage());
			}
			NsVirtualLinkNode vlNode = new 	NsVirtualLinkNode("tosca.nodes.nfv.NsVirtualLink",null, null);
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
			vlNode.setProperties(nsVirtualLinkProperties);
			nodeTemplates.put(virtualLinkDesc.getVirtualLinkDescId(), vlNode);

		}


        topologyTemplate.setNodeTemplates(nodeTemplates);
        DescriptorTemplate descriptorTemplate = new DescriptorTemplate(toscaDefinitionsVersion, toscaDefaultNamespace, description, metadata, topologyTemplate);
        return descriptorTemplate;
	}




	private static NsVirtualLinkDesc getNsVirtualLinkDesc(Nsd nsd, String virtualLinkDescId) throws FailedOperationException {

		Optional<NsVirtualLinkDesc> optionalNsVirtualLinkDesc = nsd.getVirtualLinkDesc().stream()
				.filter(linkDesc -> linkDesc.getVirtualLinkDescId().equals(virtualLinkDescId))
						.findFirst();
		if(optionalNsVirtualLinkDesc.isPresent()){
			return optionalNsVirtualLinkDesc.get();
		}else throw new FailedOperationException("Cannot find virtual link desc with id:"+virtualLinkDescId+" in NSD:"+nsd.getNsdIdentifier());


	}

	private static String getVnfDescriptorId(VnfProfile vnfProfile){
	    //String seed = vnfProfile.getVnfdId();
        //return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
		return vnfProfile.getVnfdId();

    }

    private static String getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
        String seed = nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId();
        return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
    }
}
