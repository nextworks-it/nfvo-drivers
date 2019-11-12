
# ScaleNsData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceToBeAdded** | [**List&lt;VnfInstanceData&gt;**](VnfInstanceData.md) | An existing VNF instance to be added to the NS instance as part of the scaling operation. If needed, the VNF Profile to be used for this VNF instance may also be provided.  |  [optional]
**vnfInstanceToBeRemoved** | **List&lt;String&gt;** | The VNF instance to be removed from the NS instance as part of the scaling operation.  |  [optional]
**scaleNsByStepsData** | [**ScaleNsByStepsData**](ScaleNsByStepsData.md) | The information used to scale an NS instance by one or more scaling steps.  |  [optional]
**scaleNsToLevelData** | [**ScaleNsToLevelData**](ScaleNsToLevelData.md) | The information used to scale an NS instance to a target size.  |  [optional]
**additionalParamsForNs** | [**ParamsForVnf**](ParamsForVnf.md) | Allows the OSS/BSS to provide additional parameter(s) at the NS level necessary for the NS scaling (as opposed to the VNF level, which is covered in additionalParamForVnf).  |  [optional]
**additionalParamsForVnf** | [**List&lt;ParamsForVnf&gt;**](ParamsForVnf.md) | Allows the OSS/BSS to provide additional parameter(s) per VNF instance (as opposed to the NS level, which is covered in additionalParamforNs). This is for VNFs that are to be created by the NFVO as part of the NS scaling and not for existing VNF that are covered by the scaleVnfData.  |  [optional]
**locationConstraints** | [**List&lt;VnfLocationConstraint&gt;**](VnfLocationConstraint.md) | The location constraints for the VNF to be instantiated as part of the NS scaling. An example can be a constraint for the VNF to be in a specific geographic location.  |  [optional]



