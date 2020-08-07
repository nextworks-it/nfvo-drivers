package it.nextworks.nfvmano.nfvodriver.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VnfdFileRegistry {


    @Id
    @GeneratedValue
    private Long id;

    private String vnfdInfoId;
    private  String vnfdId;
    private  String vnfdVersion;
    private String packageName;
    private String packageProvider;
    private String packageVersion;

    private String relativePath;

    //JPA
    public VnfdFileRegistry() {
    }

    public VnfdFileRegistry(String vnfdId, String vnfdVersion, String relativePath, String vnfdInfoId, String packageName, String packageVersion, String packageProvider ) {
        this.vnfdId = vnfdId;
        this.vnfdVersion = vnfdVersion;

        this.relativePath = relativePath;
        this.vnfdInfoId = vnfdInfoId;
        this.packageName = packageName;
        this.packageProvider=packageProvider;
        this.packageVersion=packageVersion;
    }


    public String getPackageName() {
        return packageName;
    }

    public String getPackageProvider() {
        return packageProvider;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public String getVnfdId() {
        return vnfdId;
    }

    public String getVnfdVersion() {
        return vnfdVersion;
    }



    public String getRelativePath() {
        return relativePath;
    }

    public String getVnfdInfoId() {
        return vnfdInfoId;
    }
}
