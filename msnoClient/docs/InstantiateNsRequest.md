
# InstantiateNsRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**nsFlavourId** | **String** | Identifier of the NS deployment flavor to be instantiated.  | 
**sapData** | [**List&lt;SapData&gt;**](SapData.md) | Create data concerning the SAPs of this NS.  |  [optional]
**addpnfData** | [**List&lt;AddPnfData&gt;**](AddPnfData.md) | Information on the PNF(s) that are part of this NS.  |  [optional]
**vnfInstanceData** | [**List&lt;VnfInstanceData&gt;**](VnfInstanceData.md) | Specify an existing VNF instance to be used in the NS. If needed, the VNF Profile to be used for this VNF instance is also provided. The DF of the VNF instance shall match the VNF DF present in the associated VNF Profile.  |  [optional]
**nestedNsInstanceData** | [**List&lt;NestedNsInstanceData&gt;**](NestedNsInstanceData.md) | Specify an existing NS instance to be used as a nested NS within the NS. If needed, the NS Profile to be used for this nested NS instance is also provided. NOTE 2: The NS DF of each nested NS shall be one of the allowed flavours in the associated NSD (as referenced in the nestedNsd attribute of the NSD of the NS to be instantiated). NOTE 3: The NSD of each referenced NSs (i.e. each nestedInstanceId) shall match the one of the nested NSD in the composite NSD.  |  [optional]
**localizationLanguage** | [**List&lt;VnfLocationConstraint&gt;**](VnfLocationConstraint.md) | Defines the location constraints for the VNF to be instantiated as part of the NS instantiation. An example can be a constraint for the VNF to be in a specific geographic location..  |  [optional]
**additionalParamsForNs** | [**KeyValuePairs**](KeyValuePairs.md) | Allows the OSS/BSS to provide additional parameter(s) at the composite NS level (as opposed to the VNF level, which is covered in additionalParamsForVnf), and as opposed to the nested NS level, which is covered in additionalParamForNestedNs.  |  [optional]
**additionalParamForNestedNs** | [**List&lt;ParamsForNestedNs&gt;**](ParamsForNestedNs.md) | Allows the OSS/BSS to provide additional parameter(s) per nested NS instance (as opposed to the composite NS level, which is covered in additionalParamForNs, and as opposed to the VNF level, which is covered in additionalParamForVnf). This is for nested NS instances that are to be created by the NFVO as part of the NS instantiation and not for existing nested NS instances that are referenced for reuse.  |  [optional]
**additionalParamsForVnf** | [**List&lt;ParamsForVnf&gt;**](ParamsForVnf.md) | Allows the OSS/BSS to provide additional parameter(s) per VNF instance (as opposed to the composite NS level, which is covered in additionalParamsForNs and as opposed to the nested NS level, which is covered in additionalParamForNestedNs). This is for VNFs that are to be created by the NFVO as part of the NS instantiation and not for existing VNF that are referenced for reuse.  |  [optional]
**startTime** | **String** | Timestamp indicating the earliest time to instantiate the NS. Cardinality \&quot;0\&quot; indicates the NS instantiation takes place immediately.  |  [optional]
**nsInstantiationLevelId** | **String** | Identifies one of the NS instantiation levels declared in the DF applicable to this NS instance. If not present, the default NS instantiation level as declared in the NSD shall be used.  |  [optional]
**additionalAffinityOrAntiAffinityRule** | [**List&lt;AffinityOrAntiAffinityRule&gt;**](AffinityOrAntiAffinityRule.md) | Specifies additional affinity or anti-affinity constraint for the VNF instances to be instantiated as part of the NS instantiation. Shall not conflict with rules already specified in the NSD.  |  [optional]



