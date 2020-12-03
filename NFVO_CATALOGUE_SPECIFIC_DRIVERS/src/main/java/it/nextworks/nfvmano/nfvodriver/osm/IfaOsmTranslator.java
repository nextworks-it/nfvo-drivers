package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.*;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.*;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingGroupDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingPolicy;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.*;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class IfaOsmTranslator {

    private static final Logger log = LoggerFactory.getLogger(IfaOsmTranslator.class);
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");


    //******************************** NSD tanslation ********************************//

    /**
     * Takes Nsd Ifa descriptor and generates the corrispondent Nsd to be onboarded in OSM
     * @param nsd
     * @return nsDf
     */
    private static NSDescriptor translateIfaToOsmNsd(Nsd nsd, NsDf nsDf) {

        NSDescriptor nsDescriptor = new NSDescriptor();

        // used to get the vnfd position of a vnfd within the nsd
        HashMap<String,Integer> vnfdIdToPosition = new HashMap<>();
        // map the sapd to the corresponding nsVirtualLinkDescId
        HashMap<String,Sapd> nsVirtuaLinkIdToSapd = new HashMap<>();
        // map the virtual link profileId to virtual link descId
        HashMap<String,String> vlProfileIdToVlDescId = new HashMap<>();
        // map the constituent_vnf_id to the constituent_vnf_id_df_profile_id
        HashMap<String,String> mappedVnfds = new HashMap<>();

        //set generic nsd info
        nsDescriptor.setId(nsd.getNsdIdentifier()+"_"+nsDf.getNsDfId()); // a combination of nsdId and nsdDfId
        nsDescriptor.setName(nsd.getNsdName());
        nsDescriptor.setShortName(nsd.getNsdName());
        nsDescriptor.setVendor(nsd.getDesigner());
        nsDescriptor.setVersion(nsd.getVersion());
        try {
            nsDescriptor.setDescription(nsDf.getDefaultInstantiationLevel().getDescription());
        } catch (NotExistingEntityException e) {
            log.debug("No description found for nsDescriptor: " + nsDescriptor.getId());
            e.printStackTrace();
            nsDescriptor.setDescription("No available description");
        }

        List<ConstituentVNFD> constituentVNFDList = new ArrayList<>();
        int indexOfVnf=1;
        for (String vnfdId : nsd.getVnfdId()){
            //mapping for conssituentVNFD
            ConstituentVNFD constituentVNFD = new ConstituentVNFD();
            //take the flavour id of this vnf, in order to know which vnf of the mappedVnfs to use
            String vnfdIdWithFlavour = null;
            try {
                vnfdIdWithFlavour = vnfdId + "_" + getFlavourFromVnfdId(nsDf,vnfdId);
                constituentVNFD.setVnfdIdentifierReference(vnfdIdWithFlavour);
                constituentVNFD.setMemberVNFIndex(indexOfVnf);
                constituentVNFDList.add(constituentVNFD);
                vnfdIdToPosition.put(vnfdIdWithFlavour,indexOfVnf);
                indexOfVnf++;
                mappedVnfds.put(vnfdId, vnfdIdWithFlavour);
            } catch (NotExistingEntityException e) {
                log.debug("No vnf profile id for vnfdId: " + vnfdId +" found in this DF");
                e.printStackTrace();
            }
        }
        nsDescriptor.setConstituentVNFDs(constituentVNFDList);

        List<it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConnectionPoint> connectionPointList = new ArrayList<>();
        for (Sapd sapd : nsd.getSapd()){
            //mapping for connection-point
            it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConnectionPoint connectionPoint
                    = new it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConnectionPoint();
            connectionPoint.setName(sapd.getCpdId());
            nsVirtuaLinkIdToSapd.put(sapd.getNsVirtualLinkDescId(), sapd);
            /*
             * In Sapd the attribute AddressData is a list.
             * For a NSD connection point in OSM the only other
             * entry is "floating-ip-required" that is by default false
             * Can we assume
             * that is always a list of one single element in order to get this info?
             * //TODO validate this
             */
            if(sapd.getAddressData() != null)
                connectionPoint.setFloatingIPRequired(sapd.getAddressData().get(0).isFloatingIpActivated());
            connectionPoint.setType("VPORT"); //the only supported by OSM
            connectionPoint.setVldIdRef(sapd.getNsVirtualLinkDescId());
            connectionPointList.add(connectionPoint);
        }
        nsDescriptor.setConnectionPoints(connectionPointList);

        // map the associations between vld and its list of cp
        HashMap<String,List<VNFDConnectionPointReference>> vldIdListHashMap = new HashMap<>();
        List<VLD> vldList = new ArrayList<>();
        for (NsVirtualLinkDesc nsVirtualLinkDesc : nsd.getVirtualLinkDesc()) {
            //mapping for vld
            VLD vld = new VLD();
            vld.setId(nsVirtualLinkDesc.getVirtualLinkDescId());
            vld.setName(nsVirtualLinkDesc.getVirtualLinkDescId());
            vld.setShortName(nsVirtualLinkDesc.getVirtualLinkDescId());
            vld.setDescription(nsVirtualLinkDesc.getDescription());
            vld.setType("ELAN"); //OSM support also ELINE

            boolean isVLMgmt = isVirtualLinkManagement(nsVirtuaLinkIdToSapd.get(vld.getId()), vld.getId());
            vld.setMgmtNetwork(isVLMgmt);
            if(isVLMgmt) vld.setVimNetworkName("VM_MGMT");
            else vld.setVimNetworkName(vld.getId());

            vldIdListHashMap.put(vld.getId(), new ArrayList<>());
            vldList.add(vld);
            vlProfileIdToVlDescId.put(getVirtualLinkProfileId(nsDf,vld.getId()), vld.getId());
        }
        // Now for each vld we need to populate the list of connection points
        for(VnfProfile vnfProfile: nsDf.getVnfProfile()) {
            for(NsVirtualLinkConnectivity nsVirtualLinkConnectivity : vnfProfile.getNsVirtualLinkConnectivity()) {
                String vlDescId = vlProfileIdToVlDescId.get(nsVirtualLinkConnectivity.getVirtualLinkProfileId());
                if(isVldPresent(vldList, vlDescId)){
                    for(String cpdId : nsVirtualLinkConnectivity.getCpdId()){
                        VNFDConnectionPointReference vnfdConnectionPointReference = new VNFDConnectionPointReference();
                        String vnfdIdWithFlavour = mappedVnfds.get(vnfProfile.getVnfdId());
                        vnfdConnectionPointReference.setVnfdIdReference(vnfdIdWithFlavour); // vnfdId
                        vnfdConnectionPointReference.setVnfdConnectionPointReference(cpdId); //cp of the vnf
                        vnfdConnectionPointReference.setIndexReference(vnfdIdToPosition.get(vnfdIdWithFlavour)); //position of vnf within the nsd
                        //this cp belongs to the vld referenced by vnfProfileId
                        vldIdListHashMap.get(vlDescId) //take the list of cps associated to vlDescvId
                                .add(vnfdConnectionPointReference);
                    }
                }
            }
        }
        //now we need to associate the list of cp to each vld
        for(VLD vld : vldList){
            vld.setVnfdConnectionPointReferences(vldIdListHashMap.get(vld.getId()));
        }
        //associate the vld list to nsd
        nsDescriptor.setVldList(vldList);
        return nsDescriptor;
    }

    private static String getFlavourFromVnfdId(NsDf nsDf, String vnfdId) throws NotExistingEntityException {
        for(VnfProfile vnfProfile : nsDf.getVnfProfile()){
            if(vnfProfile.getVnfdId().equals(vnfdId))
                return vnfProfile.getFlavourId();
        }
        throw new NotExistingEntityException();
    }

    /**
     * Get the virtual link profile id from the virtual link descriptor id
     * @param nsDf
     * @param nsVirtualLinkDescId
     * @return String
     */
    private static String getVirtualLinkProfileId(NsDf nsDf, String nsVirtualLinkDescId) {
        for(VirtualLinkProfile virtualLinkProfile : nsDf.getVirtualLinkProfile()){
            if(virtualLinkProfile.getVirtualLinkDescId().equals(nsVirtualLinkDescId)) {
                return virtualLinkProfile.getVirtualLinkProfileId();
            }
        }
        return null;
    }

    /**
     * Return true if the virtualLink is management
     */
    private static boolean isVirtualLinkManagement(Sapd sapd, String virtualLinkDescId ){
        if(sapd != null && virtualLinkDescId != null) {
            for (AddressData address : sapd.getAddressData()) {
                if (address.isManagement())
                    return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Return true if there is a vld with vlDescId in vldList
     */
    private static boolean isVldPresent(List<VLD> vldList, String vlDescId) {
        for(VLD vld: vldList){
            if(vld.getId().equals(vlDescId))
                return true;
        }
        return false;
    }

    //******************************** VNFD tanslation ********************************//

    private static VNFDescriptor translateIfaToOsmVnfd(Vnfd vnfd, VnfDf vnfDf) throws FailedOperationException {

        //managing the cloud init script: return error if there are multiple vdus
        if(vnfd.getVdu().size() > 1 && vnfd.getLifeCycleManagementScript().size() != 0){
            throw new FailedOperationException("A VNF configuration script is present, but there are multiple vdu");
        }

        //generating cloud_init file TODO how to manage this?
        for(LifeCycleManagementScript lifeCycleManagementScript : vnfd.getLifeCycleManagementScript()){
            String script = lifeCycleManagementScript.getScript();
            if(script.length()>0) {
                File scriptFile;
                if (script.contains("bin/bash"))
                    scriptFile = new File(TEMP_DIR, lifeCycleManagementScript.getEvent().get(0).name().toLowerCase() + ".sh");
                else
                    scriptFile = new File(TEMP_DIR, lifeCycleManagementScript.getEvent().get(0).name().toLowerCase() + ".txt");
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(scriptFile);
                    fileWriter.write(script);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        VNFDescriptor vnfDescriptor = new VNFDescriptor();

        //map the internalCpd of a vdu to its external Cpd
        HashMap<String,String> intCpdToExtCpd = new HashMap<>();

        // map the cp of the vdu to an internal vld (that connects vdus)
        HashMap<String,String> intVLDToIntCP = new HashMap<>();
        HashMap<String,String> intCPToIntVduInterface = new HashMap<>();

        //set generic info
        vnfDescriptor.setId(vnfd.getVnfdId()+"_"+vnfDf.getFlavourId());
        vnfDescriptor.setName(vnfd.getVnfProductName());
        vnfDescriptor.setShortName(vnfd.getVnfProductName());
        vnfDescriptor.setVendor(vnfd.getVnfProvider());
        vnfDescriptor.setVersion(vnfd.getVnfdVersion());

        List<it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ConnectionPoint> connectionPoints = new ArrayList<>();
        VnfExtCpd mgmtConnectionPoint = null; //taking the management ExtCp
        for(VnfExtCpd vnfExtCpd : vnfd.getVnfExtCpd()){
            it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ConnectionPoint connectionPoint
                    = new it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ConnectionPoint();
            connectionPoint.setId(vnfExtCpd.getCpdId());
            connectionPoint.setName(vnfExtCpd.getCpdId());
            connectionPoint.setType("VPORT"); // the only supported by OSM
            //connectionPoint.setInternalVldRef(); TODO how to set this?
            intCpdToExtCpd.put(vnfExtCpd.getIntCpd(),vnfExtCpd.getCpdId());
            connectionPoints.add(connectionPoint);
            if(mgmtConnectionPoint == null && checkIfIsManagement(vnfExtCpd.getAddressData())) mgmtConnectionPoint = vnfExtCpd;
        }
        vnfDescriptor.setConnectionPoints(connectionPoints);

        ManagementInterface managementInterface = new ManagementInterface();
        if(mgmtConnectionPoint != null){
            managementInterface.setCp(mgmtConnectionPoint.getCpdId());
            vnfDescriptor.setManagementInterface(managementInterface);
        }

        List<VDU> vduList = new ArrayList<>();
        for(Vdu ifaVDU : vnfd.getVdu()){
            VDU osmVDU = new VDU();
            //set generic info
            osmVDU.setId(ifaVDU.getVduId());
            osmVDU.setName(ifaVDU.getVduName());
            osmVDU.setDescription(ifaVDU.getDescription());
            //get the initial count from DF -> vduProfile(vduId) -> minNumberOfInstances //TODO validate
            osmVDU.setCount(getCountVduFromVduProfile(vnfDf.getVduProfile(),ifaVDU.getVduId()));
            //the name of sw image must be the same of the image on OpenStack (for example "ubuntu18.04")
            osmVDU.setImage(ifaVDU.getSwImageDesc().getName()); //TODO validate

            //osmVDU.setCloudInitFile(); //TODO how to set this?

            VirtualComputeDesc virtualComputeDesc = null;
            List<VirtualStorageDesc> virtualStorageDescs = new ArrayList<>();
            try {
                virtualComputeDesc = vnfd.getVirtualComputeDescriptorFromId(ifaVDU.getVirtualComputeDesc());
                for(String str : ifaVDU.getVirtualStorageDesc()){
                    virtualStorageDescs.add(vnfd.getVirtualStorageDescriptorFromId(str));
                }
            } catch (NotExistingEntityException e) {
                e.printStackTrace();
            }
            VMFlavor vmFlavor = new VMFlavor();
            vmFlavor.setStorageGb(getStorageFromSWImageId(virtualStorageDescs,ifaVDU.getSwImageDesc().getSwImageId()));
            if(virtualComputeDesc!= null){
                vmFlavor.setMemoryMb(virtualComputeDesc.getVirtualMemory().getVirtualMemSize()*1024);
                vmFlavor.setVcpuCount(virtualComputeDesc.getVirtualCpu().getNumVirtualCpu());
            }
            osmVDU.setVmFlavor(vmFlavor);

            //Setting the vdu interface
            List<Interface> interfaceList = new ArrayList<>();
            //setting internal cp
            List<InternalConnectionPoint> internalConnectionPoints = new ArrayList<>();
            int position = 1;
            for(VduCpd vduCpd : ifaVDU.getIntCpd()){
                Interface osmInterface = new Interface();
                if(intCpdToExtCpd.containsKey(vduCpd.getCpdId())){
                    //this means that this cp is connected to an external cp of the vnf
                    osmInterface.setType("EXTERNAL");
                    osmInterface.setExtConnPointRef(intCpdToExtCpd.get(vduCpd.getCpdId()));
                }
                else{
                    //this is a cp that allow connection to other vdu (through a vld)
                    osmInterface.setType("INTERNAL");
                    //TODO validate this
                    osmInterface.setIntConnPointRef(getIcpNameFromCpd(vduCpd.getCpdId()));
                    //vduCpd -> internal-connection-point-ref = vduCpd-internal

                    intVLDToIntCP.put(vduCpd.getIntVirtualLinkDesc(),vduCpd.getCpdId());
                    intCPToIntVduInterface.put(vduCpd.getCpdId(),osmInterface.getIntConnPointRef());
                }
                osmInterface.setName(vduCpd.getCpdId());
                osmInterface.setPosition(position);
                position++;
                osmInterface.setMgmtInterface(checkIfIsManagement(vduCpd.getAddressData()));
                VirtualInterface virtualInterface = new VirtualInterface();
                virtualInterface.setType("PARAVIRT"); //Note this is the default entry, but there are others
                //virtualInterface.setBandwidth(); aggregate bandwidth of the NIC
                //virtualInterface.setVpci(); specify the virtual PCI address
                osmInterface.setVirtualInterface(virtualInterface);
                interfaceList.add(osmInterface);
                if(osmInterface.getType().equals("INTERNAL")){
                    InternalConnectionPoint internalConnectionPoint = new InternalConnectionPoint();
                    internalConnectionPoint.setId(osmInterface.getIntConnPointRef());
                    internalConnectionPoint.setName(osmInterface.getIntConnPointRef());
                    internalConnectionPoint.setShortName(osmInterface.getIntConnPointRef());
                    internalConnectionPoint.setType("VPORT");
                    internalConnectionPoint.setInternalVldRef(vduCpd.getIntVirtualLinkDesc());
                    internalConnectionPoints.add(internalConnectionPoint);
                }
            }
            osmVDU.setInterfaces(interfaceList);
            osmVDU.setInternalConnectionPoints(internalConnectionPoints);
            vduList.add(osmVDU);
        }
        vnfDescriptor.setVduList(vduList);

        /*
        Not relevant in always one single vdu
        TODO why internalConnectionPoint is only one element?
        List<InternalVld> internalVlds = new ArrayList<>();
        for(VnfVirtualLinkDesc vnfVirtualLinkDesc : vnfd.getIntVirtualLinkDesc()){
            InternalVld vld = new InternalVld();
            vld.setId(vnfVirtualLinkDesc.getVirtualLinkDescId());
            vld.setName(vnfVirtualLinkDesc.getVirtualLinkDescId());
            vld.setType("ELAN"); //TODO or maybe ELINE?
            //vld.setRootBandwidth(); TODO How to set this? aggregate bandwidth
            //vld.setLeafBandwidth(); TODO How to set this? bandwidth of branches
            InternalConnectionPointVld internalConnectionPointVld = new InternalConnectionPointVld();
        }
        vnfDescriptor.setInternalVld(internalVlds);
        */

        //adding scaling rules
        if(vnfDf.getInstantiationLevel().size() > 1){
            InstantiationLevel defaultInstantiationLevel = null;
            try {
                //assumption the default instantiation level has always the min number of instances
                defaultInstantiationLevel = vnfDf.getDefaultInstantiationLevel();
            } catch (NotExistingEntityException e) {
                e.printStackTrace();
            }
            List<ScalingGroupDescriptor> scalingGroupDescriptorList = new ArrayList<>();
            for(InstantiationLevel instantiationLevel : vnfDf.getInstantiationLevel()){
                if(!instantiationLevel.getLevelId().equals(defaultInstantiationLevel.getLevelId())){
                    ScalingGroupDescriptor scalingGroupDescriptor = new ScalingGroupDescriptor();
                    scalingGroupDescriptor.setName(instantiationLevel.getLevelId());
                    scalingGroupDescriptor.setMinInstanceCount(0);
                    // assumption always one vdu in VduLevel List
                    scalingGroupDescriptor.setMaxInstanceCount(instantiationLevel.getVduLevel().get(0).getNumberOfInstances()+1);

                    List<ScalingPolicy> scalingPolicyList = new ArrayList<>();
                    ScalingPolicy scalingPolicy = new ScalingPolicy();
                    scalingPolicy.setName(instantiationLevel.getLevelId());
                    scalingPolicy.setScalingType("manual");
                    scalingPolicy.setEnabled(true);
                    scalingPolicy.setThresholdTime(1);
                    scalingPolicy.setCooldownTime(10);
                    scalingPolicyList.add(scalingPolicy);
                    scalingGroupDescriptor.setScalingPolicies(scalingPolicyList);

                    List<VduReference> vduReferenceList = new ArrayList<>();
                    VduReference vduReference = new VduReference();
                    vduReference.setVduIdRef(instantiationLevel.getVduLevel().get(0).getVduId());
                    vduReference.setCount(instantiationLevel.getVduLevel().get(0).getNumberOfInstances()-defaultInstantiationLevel.getVduLevel().get(0).getNumberOfInstances());
                    vduReferenceList.add(vduReference);
                    scalingGroupDescriptor.setVduList(vduReferenceList);

                    scalingGroupDescriptorList.add(scalingGroupDescriptor);
                }
            }
            vnfDescriptor.setScalingGroupDescriptor(scalingGroupDescriptorList);
        }

        return vnfDescriptor;
    }

    private static String getIcpNameFromCpd(String cpdId) {
        return cpdId+"-internal";
    }

    private static Integer getStorageFromSWImageId(List<VirtualStorageDesc> virtualStorageDescs, String swImageId) {
        for(VirtualStorageDesc vsd : virtualStorageDescs){
            if(vsd.getSwImageDesc().equals(swImageId)){
                return vsd.getSizeOfStorage();
            }
        }
        return 0;
    }

    private static Integer getCountVduFromVduProfile(List<VduProfile> vduProfile, String vduId) {
        for(VduProfile profile : vduProfile){
            if(profile.getVduId().equals(vduId)){
                return profile.getMinNumberOfInstances();
            }
        }
        return 0;
    }

    private static boolean checkIfIsManagement(List<AddressData> ad) {
        for(AddressData addressData : ad){
            if(addressData.isManagement())
                return true;
        }
        return false;
    }

    //******************************** Utility functions ********************************//

    /**
     * Create the Nsd zip package and return the path
     * @param nsd
     * @param nsDf
     * @return String
     */
    public static File createPackageForNsd(Nsd nsd, NsDf nsDf) {
        NSDescriptor nsdOsm = translateIfaToOsmNsd(nsd,nsDf);

        List<NSDescriptor> nsDescriptorList = new ArrayList<>();
        nsDescriptorList.add(nsdOsm);
        NSDCatalog nsdCatalog = new NSDCatalog();
        nsdCatalog.setNsds(nsDescriptorList);
        OsmNSPackage osmNSPackage = new OsmNSPackage();
        osmNSPackage.setNsdCatalog(nsdCatalog);

        //making folder that will contain nsd
        File nsdFolder = makeNsFolder(nsdOsm.getId());

        //making yaml file
        makeYml(osmNSPackage,nsdOsm.getId(),nsdFolder.getAbsolutePath());

        //creating zip archive
        File tarNsdFile = compress(nsdFolder);

        return tarNsdFile;
    }

    public static File createPackageForVnfd(Vnfd vnfd, VnfDf vnfdDf) {
        VNFDescriptor vnfdOsm = null;
        try {
            vnfdOsm = translateIfaToOsmVnfd(vnfd,vnfdDf);
        } catch (FailedOperationException e) {
            e.printStackTrace();
            return null;
        }
        List<VNFDescriptor> vnfDescriptorList = new ArrayList<>();
        vnfDescriptorList.add(vnfdOsm);
        VNFDCatalog vnfdCatalog = new VNFDCatalog();
        vnfdCatalog.setVnfd(vnfDescriptorList);

        OsmVNFPackage osmVNFPackage = new OsmVNFPackage();
        osmVNFPackage.setVnfdCatalog(vnfdCatalog);

        //making folder that will contain vnfd
        File vnfdFolder = makeVnfFolder(vnfdOsm.getId());
        File cloudInitFolder = makeSubFolder(vnfdFolder,"cloud_init");

        makeYml(osmVNFPackage, vnfdOsm.getId(), vnfdFolder.getAbsolutePath());

        File tarVnfdFile = compress(vnfdFolder);
        return tarVnfdFile;
    }

    private static File makeSubFolder(File folder, String subFolder) {
        File newFolder = new File(folder, subFolder);
        if (!newFolder.mkdir()) {
            throw new IllegalArgumentException(
                    String.format("Cannot create folder %s", newFolder.getAbsolutePath())
            );
        }
        return newFolder;
    }

    private static File makeVnfFolder(String vnfdId) {
        String folderPath = TEMP_DIR + File.separator + vnfdId + "_vnf";
        Path path = Paths.get(folderPath);
        if(Files.isDirectory(path)){
            log.debug("Temporary folder " + vnfdId + "_ns already existing. Overwriting");
            if (!rmRecursively(path.toFile())) {
                throw new IllegalStateException(
                        String.format("Could not delete folder %s", path.toFile().getAbsolutePath())
                );
            }
        }
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.toFile();
    }

    private static File makeNsFolder(String nsdId) {
        String folderPath = TEMP_DIR + File.separator + nsdId + "_ns";
        Path path = Paths.get(folderPath);
        if(Files.isDirectory(path)){
            log.debug("Temporary folder " + nsdId + "_ns already existing. Overwriting");
            if (!rmRecursively(path.toFile())) {
                throw new IllegalStateException(
                        String.format("Could not delete folder %s", path.toFile().getAbsolutePath())
                );
            }
        }
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.toFile();
    }

    private static boolean rmRecursively(File folder) {
        SimpleFileVisitor<Path> deleter = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        };
        try {
            Files.walkFileTree(folder.toPath(), deleter);
            return true;
        } catch (IOException e) {
            log.error(
                    "Could not recursively delete folder {}. Error: {}",
                    folder.getAbsolutePath(),
                    e.getMessage()
            );
            log.debug("Error details: ", e);
            return false;
        }
    } //same of ArchiveBuilder

    private static void makeYml(OsmNSPackage osmNSPackage, String nsdId, String nsdFolder) {
        String yamlFilePath = nsdFolder + File.separator + nsdId + ".yaml";

        ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());
        try{
            List<String> strings = Arrays.asList(ymlMapper.writeValueAsString(osmNSPackage).split("\n"));
            Files.write(Paths.get(yamlFilePath), strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeYml(OsmVNFPackage osmVNFPackage, String vnfdId, String vnfdFolder) {
        String yamlFilePath = vnfdFolder + File.separator + vnfdId + ".yaml";

        ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());
        try{
            List<String> strings = Arrays.asList(ymlMapper.writeValueAsString(osmVNFPackage).split("\n"));
            Files.write(Paths.get(yamlFilePath), strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File compress(File folder) {
        File rootDir = folder.getParentFile();
        File archive = new File(rootDir, folder.getName() + ".tar.gz");
        try (
                FileOutputStream fos = new FileOutputStream(archive);
                GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
                TarArchiveOutputStream tos = new TarArchiveOutputStream(gos)
        ) {
            SimpleFileVisitor<Path> archiver = new SimpleFileVisitor<Path>() {

                private File ROOT = folder.getParentFile();

                private String relPath(Path target) {
                    return ROOT.toPath().relativize(target).toString();
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    tos.putArchiveEntry(new TarArchiveEntry(file.toFile(), relPath(file)));
                    Files.copy(file, tos);
                    tos.closeArchiveEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes)
                        throws IOException {
                    tos.putArchiveEntry(new TarArchiveEntry(path.toFile(), relPath(path)));
                    tos.closeArchiveEntry();
                    return FileVisitResult.CONTINUE;
                }
            };
            Files.walkFileTree(folder.toPath(), archiver);
            return archive;
        } catch (IOException e) {
            throw new IllegalStateException(
                    String.format("Could not compress package. Error: %s", e.getMessage())
            );
        }
    } //same of ArchiveBuilder

    public static String getOsmNsdId(Nsd nsd, NsDf df) {
        return nsd.getNsdIdentifier()+"_"+ df.getNsDfId();
    }

}
