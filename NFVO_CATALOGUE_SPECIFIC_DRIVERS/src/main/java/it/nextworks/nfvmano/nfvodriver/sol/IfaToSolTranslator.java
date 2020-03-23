package it.nextworks.nfvmano.nfvodriver.sol;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import it.nextworks.nfvmano.libs.descriptors.interfaces.LcmOperation;
import it.nextworks.nfvmano.libs.descriptors.interfaces.Nslcm;
import it.nextworks.nfvmano.libs.descriptors.nsd.nodes.NS.NSInterfaces;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.vnfpackagemanagement.elements.VnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.common.elements.KeyValuePair;
import it.nextworks.nfvmano.libs.ifa.common.enums.LcmEventType;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.LifeCycleManagementScript;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IfaToSolTranslator {

	private static final Logger log = LoggerFactory.getLogger(IfaToSolTranslator.class);
        //SOL : IFA 
	public static DescriptorTemplate translateIfaToSolNsd(Nsd nsd, NsDf nsDf, NsLevel nsIl) throws FailedOperationException {
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
        
    	NSRequirements nsRequirements = getNsRequirements(nsd, nsDf, nsIl, virtualLinkProfileIds, vlProfileToLinkDesc);
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
		NSInterfaces nsInterfaces = getNsInterfaces(nsd);
		nsNode.setInterfaces(nsInterfaces);
        nsNode.setRequirements(nsRequirements);


        nodeTemplates.put(( nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId() ), nsNode);
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
        			VNFNode vnfNode = getVNFNode(vnfProfile, vlProfileToLinkDesc, nsd);
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
			String vlProfileId = entry.getKey();
			String virtualLinkDescId = entry.getValue();
			NsVirtualLinkNode vlNode = getVLNode(nsd,nsDf, vlProfileId, virtualLinkDescId);
			nodeTemplates.put(virtualLinkDescId, vlNode);

		}





        topologyTemplate.setNodeTemplates(nodeTemplates);
        DescriptorTemplate descriptorTemplate = new DescriptorTemplate(toscaDefinitionsVersion, toscaDefaultNamespace, description, metadata, topologyTemplate);
        return descriptorTemplate;
	}



	private static VNFNode getVNFNode(VnfProfile vnfProfile, Map<String, String> vlProfileToLinkDesc, Nsd nsd){
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
		return vnfNode;
	}

	private static NsVirtualLinkNode getVLNode(Nsd nsd, NsDf nsDf, String virtualLinkProfileId, String virtualLinkDescriptorId) throws FailedOperationException {
		NsVirtualLinkDesc virtualLinkDesc = getNsVirtualLinkDesc(nsd, virtualLinkDescriptorId);
		VirtualLinkProfile virtualLinkProfile = null;
		try {
			virtualLinkProfile = nsDf.getVirtualLinkProfile(virtualLinkProfileId);
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
		return vlNode;
	}

	private static NSRequirements getNsRequirements(Nsd nsd, NsDf nsDf, NsLevel nsIl , List<String> virtualLinkProfileIds, Map<String, String> vlProfileToLinkDesc){
		List<String> nsVirtualLinks = new ArrayList<>();
		for (String vlProfileId : virtualLinkProfileIds) {
			nsVirtualLinks.add(vlProfileToLinkDesc.get(vlProfileId));
		}

		return new NSRequirements(nsVirtualLinks);

	}

	private static NSInterfaces getNsInterfaces (Nsd nsd) throws FailedOperationException {
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


	private static NsVirtualLinkDesc getNsVirtualLinkDesc(Nsd nsd, String virtualLinkDescId) throws FailedOperationException {

		Optional<NsVirtualLinkDesc> optionalNsVirtualLinkDesc = nsd.getVirtualLinkDesc().stream()
				.filter(linkDesc -> linkDesc.getVirtualLinkDescId().equals(virtualLinkDescId))
						.findFirst();
		if(optionalNsVirtualLinkDesc.isPresent()){
			return optionalNsVirtualLinkDesc.get();
		}else throw new FailedOperationException("Cannot find virtual link desc with id:"+virtualLinkDescId+" in NSD:"+nsd.getNsdIdentifier());


	}


	public static String createCsarPackageForNsdDfIl(Nsd nsd, NsDf nsDf, NsLevel nsIl) throws FailedOperationException {

		DescriptorTemplate descriptorTemplate = translateIfaToSolNsd(nsd, nsDf, nsIl );
		String nsDescriptorId = getNsDescriptorIdSeed(nsd, nsDf, nsIl);
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
		writer.write("\tvnf_product_name: " + getNsDescriptorIdSeed(nsd, nsDf, nsIl) +"\n");
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
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
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

    private static String getNsDescriptorId(Nsd nsd, NsDf nsDf, NsLevel nsIl){
        String seed = getNsDescriptorIdSeed(nsd, nsDf, nsIl);
        return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
    }

    private static String getNsDescriptorIdSeed(Nsd nsd, NsDf nsDf, NsLevel nsIl){
		String seed = nsd.getNsdIdentifier() + "_" + nsDf.getNsDfId() + "_" + nsIl.getNsLevelId();
		return seed;
	}
}
