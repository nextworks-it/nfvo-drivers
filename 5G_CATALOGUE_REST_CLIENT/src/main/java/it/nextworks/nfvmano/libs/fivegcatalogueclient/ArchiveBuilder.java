package it.nextworks.nfvmano.libs.fivegcatalogueclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.nextworks.nfvmano.libs.descriptors.templates.DescriptorTemplate;
import it.nextworks.nfvmano.libs.descriptors.templates.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveBuilder {

    public static String createNSCSAR(DescriptorTemplate template){

        String serviceName = "descriptor";
        String servicePackagePath;
        Map<String, Node> nodes = template.getTopologyTemplate().getNodeTemplates();
        for(Map.Entry<String, Node> node : nodes.entrySet()){
            if(node.getValue().getType().equals("tosca.nodes.nfv.NS"))
                serviceName = node.getKey();
        }

        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        List<String> strings = new ArrayList<>();

        try{
            //Create directories
            File root = makeFolder(serviceName);
            File definitions = makeSubFolder(root, "Definitions");
            File files = makeSubFolder(root, "Files");
            File licenses = makeSubFolder(files, "Licences");
            File scripts = makeSubFolder(files, "Scripts");
            File tests = makeSubFolder(files, "Tests");
            File metadata = makeSubFolder(root, "TOSCA-Metadata");

            //Create standard files
            File manifest = new File(root, serviceName + ".mf");
            strings.add("metadata:");
            strings.add("\tns_name: " + serviceName);
            strings.add("\tns_vendor_id: " + template.getMetadata().getVendor());
            strings.add("\tns_version: " + template.getMetadata().getVersion());
            strings.add(String.format("\tns_release_date_time: %1$TD %1$TT", ts));

            Files.write(manifest.toPath(), strings);
            strings.clear();
            File license = new File(licenses, "LICENSE");
            Files.write(license.toPath(), strings);
            File changeLog = new File(files, "ChangeLog.txt");
            strings.add(String.format("%1$TD %1$TT - New NS Package according to ETSI GS NFV-SOL004 v 2.5.1", ts));
            Files.write(changeLog.toPath(), strings);
            strings.clear();
            File certificate = new File(files, serviceName + ".cert");
            Files.write(certificate.toPath(), strings);
            File toscaMetadata = new File(metadata, "TOSCA.meta");
            strings.add("TOSCA-Meta-File-Version: 1.0");
            strings.add("CSAR-Version: 1.1");
            strings.add("CreatedBy: 5G Catalogue Client");
            strings.add("Entry-Definitions: Definitions/"+ serviceName + ".yaml");
            Files.write(toscaMetadata.toPath(), strings);
            strings.clear();

            //Create descriptor files
            File descriptorFile = new File(definitions, serviceName + ".yaml");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.writeValue(descriptorFile, template);

            servicePackagePath = compress(root.toPath().toString());
        } catch (IOException e) {
            throw new IllegalStateException(
                String.format("Could not write files. Error: %s", e.getMessage(), e)
            );
        }

        return servicePackagePath;
    }

    public static String createVNFCSAR(DescriptorTemplate template, String cloudInit){

        String functionName = "descriptor";
        String functionPackagePath;
        Map<String, Node> nodes = template.getTopologyTemplate().getNodeTemplates();
        for(Map.Entry<String, Node> node : nodes.entrySet()){
            if(node.getValue().getType().equals("tosca.nodes.nfv.VNF"))
                functionName = node.getKey();
        }

        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        List<String> strings = new ArrayList<>();

        try{
            //Create directories
            File root = makeFolder(functionName);
            File definitions = makeSubFolder(root, "Definitions");
            File files = makeSubFolder(root, "Files");
            File licenses = makeSubFolder(files, "Licences");
            File scripts = makeSubFolder(files, "Scripts");
            File tests = makeSubFolder(files, "Tests");
            File metadata = makeSubFolder(root, "TOSCA-Metadata");

            //Create standard files
            File manifest = new File(root, functionName + ".mf");
            strings.add("metadata:");
            strings.add("\tvnf_product_name: " + functionName);
            strings.add("\tvnf_provider_id: " + template.getMetadata().getVendor());
            strings.add("\tvnf_package_version: " + template.getMetadata().getVersion());
            strings.add(String.format("\tvnf_release_date_time: %1$TD %1$TT", ts));
            if(cloudInit != null) {
                strings.add("\nconfiguration:");
                strings.add("\tcloud_init:");
                strings.add("\t\tSource: Files/Scripts/cloud-init.txt");
            }
            Files.write(manifest.toPath(), strings);
            strings.clear();
            File license = new File(licenses, "LICENSE");
            Files.write(license.toPath(), strings);
            File changeLog = new File(files, "ChangeLog.txt");
            strings.add(String.format("%1$TD %1$TT - New VNF Package according to ETSI GS NFV-SOL004 v 2.5.1", ts));
            Files.write(changeLog.toPath(), strings);
            strings.clear();
            File certificate = new File(files, functionName + ".cert");
            Files.write(certificate.toPath(), strings);
            File toscaMetadata = new File(metadata, "TOSCA.meta");
            strings.add("TOSCA-Meta-File-Version: 1.0");
            strings.add("CSAR-Version: 1.1");
            strings.add("CreatedBy: 5G Catalogue Client");
            strings.add("Entry-Definitions: Definitions/"+ functionName + ".yaml");
            Files.write(toscaMetadata.toPath(), strings);
            strings.clear();

            //Create descriptor files
            File descriptorFile = new File(definitions, functionName + ".yaml");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            File cloudInitFile;
            if(cloudInit != null){
                cloudInitFile = new File(scripts, "cloud-init.txt");
                Files.write(cloudInitFile.toPath(), cloudInit.getBytes());
            }
            mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.writeValue(descriptorFile, template);

            functionPackagePath = compress(root.toPath().toString());
        } catch (IOException e) {
            throw new IllegalStateException(
                String.format("Could not write files. Error: %s", e.getMessage(), e)
            );
        }

        return functionPackagePath;
    }

    public static File makeFolder(String name) {
        File folder = new File("/tmp", name);
        if (folder.isDirectory()) {
            if (!rmRecursively(folder)) {
                throw new IllegalStateException(
                    String.format("Could not delete folder %s", folder.getAbsolutePath())
                );
            }
        }
        if (!folder.mkdir()) {
            throw new IllegalArgumentException(
                String.format("Cannot create folder %s", folder.getAbsolutePath())
            );
        }
        return folder;
    }

    public static boolean rmRecursively(File folder) {
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
            return false;
        }
    }

    public static File makeSubFolder(File folder, String subFolder) {
        File newFolder = new File(folder, subFolder);
        if (!newFolder.mkdir()) {
            throw new IllegalArgumentException(
                String.format("Cannot create folder %s", newFolder.getAbsolutePath())
            );
        }
        return newFolder;
    }

    public static void copyFile(File fileFolder, File file) {
        try {
            Files.copy(file.toPath(), new File(fileFolder, file.getName()).toPath());
        } catch (IOException e) {
            String msg = String.format(
                "Cannot copy icon file %s to folder %s",
                file.getAbsolutePath(),
                fileFolder.getAbsolutePath()
            );
            throw new IllegalArgumentException(msg, e);
        }
    }

    public static String compress(String dirPath) {
        final Path sourceDir = Paths.get(dirPath);
        String zipFileName = dirPath.concat(".zip");
        try {
            final ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        Path targetFile = sourceDir.relativize(file);
                        outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        outputStream.write(bytes, 0, bytes.length);
                        outputStream.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFileName;
    }
}
