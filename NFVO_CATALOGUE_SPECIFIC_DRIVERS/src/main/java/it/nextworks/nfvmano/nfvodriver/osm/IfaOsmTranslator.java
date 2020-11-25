package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.ArchiveBuilder;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.AddressData;
import it.nextworks.nfvmano.libs.ifa.descriptors.common.elements.VirtualLinkProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.*;
import it.nextworks.osm.ApiException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class IfaOsmTranslator {

    private static final Logger log = LoggerFactory.getLogger(IfaOsmTranslator.class);

    /**
     * Takes Nsd Ifa descriptor and generates the corrispondent Nsd to be onboarded in OSM
     * @param nsd
     * @return nsDf
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public static NSDescriptor translateIfaOsmNsd(Nsd nsd, NsDf nsDf) throws FailedOperationException {

        NSDescriptor nsDescriptor = new NSDescriptor();

        // used to get the vnfd position of a vnfd within the nsd
        HashMap<String,Integer> vnfdIdToPosition = new HashMap<>();
        // map the sapd to the corresponding nsVirtualLinkDescId
        HashMap<String,Sapd> nsVirtuaLinkIdToSapd = new HashMap<>();
        // map the virtual link profileId to virtual link descId
        HashMap<String,String> vlProfileIdToVlDescId = new HashMap<>();

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
            nsDescriptor.setDescription("Empty");
        }

        List<ConstituentVNFD> constituentVNFDList = new ArrayList<>();
        int indexOfVnf=1;
        for (String vnfdId : nsd.getVnfdId()){
            //mapping for conssituentVNFD
            ConstituentVNFD constituentVNFD = new ConstituentVNFD();
            constituentVNFD.setVnfdIdentifierReference(vnfdId);
            constituentVNFD.setMemberVNFIndex(indexOfVnf);
            constituentVNFDList.add(constituentVNFD);
            vnfdIdToPosition.put(vnfdId,indexOfVnf);
            indexOfVnf++;
        }
        nsDescriptor.setConstituentVNFDs(constituentVNFDList);

        List<ConnectionPoint> connectionPointList = new ArrayList<>();
        for (Sapd sapd : nsd.getSapd()){
            //mapping for connection-point
            ConnectionPoint connectionPoint = new ConnectionPoint();
            connectionPoint.setName(sapd.getCpdId());
            nsVirtuaLinkIdToSapd.put(sapd.getNsVirtualLinkDescId(), sapd);
            /*
             * In Sapd the attribute AddressData is a list. Can we assume
             * that is always a list of one single element?
             */
            connectionPoint.setFloatingIPRequired(sapd.getAddressData().get(0).isFloatingIpActivated());
            connectionPoint.setType("VPORT"); //the only supported
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

            vld.setMgmtNetwork(isVirtualLinkManagement(nsVirtuaLinkIdToSapd.get(vld.getId()), vld.getId()));
            vld.setVimNetworkName(vld.getId());
            vldIdListHashMap.put(vld.getId(), new ArrayList<>());
            vldList.add(vld);
            vlProfileIdToVlDescId.put(getVirtualLinkProfileId(nsDf,vld.getId()), vld.getId());
        }
        // Now for each vld we need to populate the list of connection points
        for(VnfProfile vnfProfile: nsDf.getVnfProfile()) {
            for(NsVirtualLinkConnectivity nsVirtualLinkConnectivity : vnfProfile.getNsVirtualLinkConnectivity()) {
                String vlDescId = vlProfileIdToVlDescId.get(nsVirtualLinkConnectivity.getVirtualLinkProfileId());
                if(isVldPresent(vldList, vlDescId)){
                    VNFDConnectionPointReference vnfdConnectionPointReference = new VNFDConnectionPointReference();
                    vnfdConnectionPointReference.setVnfdIdReference(vnfProfile.getVnfdId()); // vnfdId
                    //assume one single cpd. TODO check for this assumption
                    vnfdConnectionPointReference.setVnfdConnectionPointReference(nsVirtualLinkConnectivity.getCpdId().get(0)); //cp of the vnf
                    vnfdConnectionPointReference.setIndexReference(vnfdIdToPosition.get(vnfProfile.getVnfdId())); //position of vnf within the nsd
                    //this cp belogs to the vld referenced by vnfProfileId
                    vldIdListHashMap.get(vlDescId) //take the list of cps associated to vlDescvId
                            .add(vnfdConnectionPointReference);
                }
            }
        }
        //associate the list of cp to each vld
        for(VLD vld : vldList){
            vld.setVnfdConnectionPointReferences(vldIdListHashMap.get(vld.getId()));
        }
        //associate the vld list to nsd
        nsDescriptor.setVldList(vldList);
        return nsDescriptor;
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

    /**
     * Create the Nsd zip package and return the path
     * @param nsd
     * @param nsDf
     * @return String
     */
    public static String createPackage(Nsd nsd, NsDf nsDf) throws FailedOperationException {
        NSDescriptor nsdOsm = translateIfaOsmNsd(nsd,nsDf);
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
        String tarNsdFile = compress(nsdFolder);

        return tarNsdFile;
    }

    private static File makeNsFolder(String nsdId) {
        String folderPath = System.getProperty("java.io.tmpdir") + File.separator + nsdId + "_ns";
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
        String yamlFilePath = nsdFolder + File.separator + nsdId + "_nsd.yaml";

        File nsdFile = new File(yamlFilePath);
        ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());
        try{
            List<String> strings = Arrays.asList(ymlMapper.writeValueAsString(osmNSPackage).split("\n"));
            Files.write(Paths.get(yamlFilePath), strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String compress(File folder) {
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
            return archive.getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(
                    String.format("Could not compress package. Error: %s", e.getMessage())
            );
        }
    } //same of ArchiveBuilder
}
