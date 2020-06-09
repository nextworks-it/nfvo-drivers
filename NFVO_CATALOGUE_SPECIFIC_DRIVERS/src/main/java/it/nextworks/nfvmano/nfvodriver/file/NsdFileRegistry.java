package it.nextworks.nfvmano.nfvodriver.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NsdFileRegistry {


    @Id
    @GeneratedValue
    private Long id;

    private String nsdInfoId;
    private  String nsdIdentifier;
    private  String nsdVersion;
    private String nsdName;
    private String relativePath;

    //JPA
    public NsdFileRegistry() {
    }

    public NsdFileRegistry(String nsdIdentifier, String nsdVersion, String nsdName, String relativePath, String nsdInfoId) {
        this.nsdIdentifier = nsdIdentifier;
        this.nsdVersion = nsdVersion;
        this.nsdName = nsdName;
        this.relativePath = relativePath;
        this.nsdInfoId = nsdInfoId;
    }




    public String getNsdIdentifier() {
        return nsdIdentifier;
    }

    public String getNsdVersion() {
        return nsdVersion;
    }

    public String getNsdName() {
        return nsdName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getNsdInfoId() {
        return nsdInfoId;
    }
}
