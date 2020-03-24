package it.nextworks.nfvmano.nfvodriver.sol;

class NsdDfIlKey{


    private String nsdId;
    private String instantiationLevel;
    private String deploymentFlavor;

    public NsdDfIlKey(String nsdId, String instantiationLevel, String deploymentFlavor) {
        this.nsdId = nsdId;
        this.instantiationLevel = instantiationLevel;
        this.deploymentFlavor = deploymentFlavor;
    }

    public String getNsdId() {
        return nsdId;
    }

    public String getInstantiationLevel() {
        return instantiationLevel;
    }

    public String getDeploymentFlavor() {
        return deploymentFlavor;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof NsdDfIlKey){
            NsdDfIlKey key = (NsdDfIlKey)o;
            return nsdId.equals(key.getNsdId())&&instantiationLevel.equals(key.getInstantiationLevel())&&deploymentFlavor.equals(key.getDeploymentFlavor());
        }else return false;
    }
}