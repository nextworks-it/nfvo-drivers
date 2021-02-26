package it.nextworks.nfvmano.libs.fivegcatalogueclient;

public abstract class CatalogueClient implements CatalogueProviderInterface {


    CatalogueType type;
    Catalogue catalogue;

    public CatalogueClient(CatalogueType type, Catalogue catalogue) {
        this.type = type;
        this.catalogue = catalogue;
    }

    public CatalogueType getType() {
        return type;
    }

    public void setType(CatalogueType type) {
        this.type = type;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }


}
