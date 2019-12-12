
# UpdateNsRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**updateType** | [**UpdateTypeEnum**](#UpdateTypeEnum) | The type of update. It determines also which one of the following parameters is present in the operation. Possible values include: * ADD_VNF: Adding existing VNF instance(s) * REMOVE_VNF: Removing VNF instance(s) * INSTANTIATE_VNF: Instantiating new VNF(s) * CHANGE_VNF_DF: Changing VNF DF * OPERATE_VNF: Changing VNF state, * MODIFY_VNF_INFORMATION: Modifying VNF information and/or the configurable properties of VNF instance(s) * CHANGE_EXTERNAL_VNF_CONNECTIVITY: Changing the external connectivity of VNF instance(s)ADD_SAP: Adding SAP(s) * REMOVE_SAP: Removing SAP(s) * ADD_NESTED_NS: Adding existing NS instance(s) as nested NS(s) * REMOVE_NESTED_NS: Removing existing nested NS instance(s) * ASSOC_NEW_NSD_VERSION: Associating a new NSD version to the NS instance * MOVE_VNF: Moving VNF instance(s) from one origin NS instance to another target NS instance * ADD_VNFFG: Adding VNFFG(s) * REMOVE_VNFFG: Removing VNFFG(s) * UPDATE_VNFFG: Updating VNFFG(s) * CHANGE_NS_DF: Changing NS DF * ADD_PNF: Adding PNF * MODIFY_PNF: Modifying PNF * REMOVE_PNF: Removing PNF  | 
**addVnfIstance** | [**List&lt;VnfInstanceData&gt;**](VnfInstanceData.md) | Identifies an existing VNF instance to be added to the NS instance. It shall be present only if updateType &#x3D; \&quot;ADD_VNF\&quot;.  |  [optional]
**removeVnfInstanceId** | **List&lt;String&gt;** | Identifies an existing VNF instance to be removed from the NS instance. It contains the identifier(s) of the VNF instances to be removed. It shall be present only if updateType &#x3D; \&quot;REMOVE_VNF.\&quot; Note: If a VNF instance is removed from a NS and this NS was the last one for which this VNF instance was a part, the VNF instance is terminated by the NFVO.  |  [optional]
**instantiateVnfData** | [**List&lt;InstantiateVnfData&gt;**](InstantiateVnfData.md) | Identifies the new VNF to be instantiated. It can be used e.g. for the bottom-up NS creation. It shall be present only if updateType &#x3D; \&quot;INSTANTIATE_VNF\&quot;.  |  [optional]
**changeVnfFlavourData** | [**List&lt;ChangeVnfFlavourData&gt;**](ChangeVnfFlavourData.md) | Identifies the new DF of the VNF instance to be changed to. It shall be present only if updateType &#x3D; \&quot;CHANGE_VNF_DF\&quot;.  |  [optional]
**operateVnfData** | [**List&lt;OperateVnfData&gt;**](OperateVnfData.md) | Identifies the state of the VNF instance to be changed. It shall be present only if updateType &#x3D; \&quot;OPERATE_VNF\&quot;.  |  [optional]
**modifyVnfInfoData** | [**List&lt;ModifyVnfInfoData&gt;**](ModifyVnfInfoData.md) | Identifies the VNF information parameters and/or the configurable properties of VNF instance to be modified. It shall be present only if updateType &#x3D; \&quot;MODIFY_VNF_INFORMATION\&quot;.  |  [optional]
**changeExtVnfConnectivityData** | [**List&lt;ChangeExtVnfConnectivityData&gt;**](ChangeExtVnfConnectivityData.md) | Specifies the new external connectivity data of the VNF instance to be changed. It shall be present only if updateType &#x3D; \&quot;CHANGE_EXTERNAL_VNF_CONNECTIVITY\&quot;.  |  [optional]
**addSap** | [**List&lt;SapData&gt;**](SapData.md) | Identifies a new SAP to be added to the NS instance. It shall be present only if updateType &#x3D; \&quot;ADD_SAP.\&quot;  |  [optional]
**removeSapId** | **List&lt;String&gt;** | The identifier an existing SAP to be removed from the NS instance. It shall be present only if updateType &#x3D; \&quot;REMOVE_SAP.\&quot;  |  [optional]
**addNestedNsData** | [**List&lt;NestedNsInstanceData&gt;**](NestedNsInstanceData.md) | The identifier of an existing nested NS instance to be added to (nested within) the NS instance. It shall be present only if updateType &#x3D; \&quot;ADD_NESTED_NS\&quot;.  |  [optional]
**removeNestedNsId** | **List&lt;String&gt;** | The identifier of an existing nested NS instance to be removed from the NS instance. It shall be present only if updateType &#x3D; \&quot;REMOVE_NESTED_NS\&quot;.  |  [optional]
**assocNewNsdVersionData** | [**AssocNewNsdVersionData**](AssocNewNsdVersionData.md) | Specify the new NSD to be used for the NS instance. It shall be present only if updateType &#x3D; ASSOC_NEW_NSD_VERSION\&quot;.  |  [optional]
**moveVnfInstanceData** | [**List&lt;MoveVnfInstanceData&gt;**](MoveVnfInstanceData.md) | Specify existing VNF instance to be moved from one NS instance to another NS instance. It shall be present only if updateType &#x3D; MOVE_VNF\&quot;.  |  [optional]
**addVnffg** | [**List&lt;AddVnffgData&gt;**](AddVnffgData.md) | Specify the new VNFFG to be created to the NS Instance. It shall be present only if updateType &#x3D; \&quot;ADD_VNFFG\&quot;.  |  [optional]
**removeVnffgId** | **List&lt;String&gt;** | Identifier of an existing VNFFG to be removed from the NS Instance. It shall be present only if updateType &#x3D; \&quot;REMOVE_VNFFG\&quot;.  |  [optional]
**updateVnffg** | [**List&lt;UpdateVnffgData&gt;**](UpdateVnffgData.md) | Specify the new VNFFG Information data to be updated for a VNFFG of the NS Instance. It shall be present only if updateType &#x3D; \&quot;UPDATE_VNFFG\&quot;.  |  [optional]
**changeNsFlavourData** | [**ChangeNsFlavourData**](ChangeNsFlavourData.md) | Specifies the new DF to be applied to the NS instance. It shall be present only if updateType &#x3D; \&quot;CHANGE_NS_DF\&quot;.  |  [optional]
**addPnfData** | [**List&lt;AddPnfData&gt;**](AddPnfData.md) | specifies the PNF to be added into the NS instance. It shall be present only if updateType &#x3D; \&quot;ADD_PNF\&quot;.  |  [optional]
**modifyPnfData** | [**List&lt;ModifyPnfData&gt;**](ModifyPnfData.md) | Specifies the PNF to be modified in the NS instance. It shall be present only if updateType &#x3D; \&quot;MODIFY_PNF\&quot;.  |  [optional]
**removePnfId** | **List&lt;String&gt;** | Identifier of the PNF to be deleted from the NS instance. It shall be present only if updateType &#x3D; \&quot;REMOVE_PNF\&quot;.  |  [optional]
**updateTime** | **String** | Timestamp indicating the update time of the NS, i.e. the NS will be updated at this timestamp. Cardinality \&quot;0\&quot; indicates the NS update takes place immediately.  |  [optional]


<a name="UpdateTypeEnum"></a>
## Enum: UpdateTypeEnum
Name | Value
---- | -----
ADD_VNF | &quot;ADD_VNF&quot;
REMOVE_VNF | &quot;REMOVE_VNF&quot;
INSTANTIATE_VNF | &quot;INSTANTIATE_VNF&quot;
CHANGE_VNF_DF | &quot;CHANGE_VNF_DF&quot;
OPERATE_VNF | &quot;OPERATE_VNF&quot;
MODIFY_VNF_INFORMATION | &quot;MODIFY_VNF_INFORMATION&quot;
CHANGE_EXTERNAL_VNF_CONNECTIVITY | &quot;CHANGE_EXTERNAL_VNF_CONNECTIVITY&quot;
REMOVE_SAP | &quot;REMOVE_SAP&quot;
ADD_NESTED_NS | &quot;ADD_NESTED_NS&quot;
REMOVE_NESTED_NS | &quot;REMOVE_NESTED_NS&quot;
ASSOC_NEW_NSD_VERSION | &quot;ASSOC_NEW_NSD_VERSION&quot;
MOVE_VNF | &quot;MOVE_VNF&quot;
ADD_VNFFG | &quot;ADD_VNFFG&quot;
REMOVE_VNFFG | &quot;REMOVE_VNFFG&quot;
UPDATE_VNFFG | &quot;UPDATE_VNFFG&quot;
CHANGE_NS_DF | &quot;CHANGE_NS_DF&quot;
ADD_PNF | &quot;ADD_PNF&quot;
MODIFY_PNF | &quot;MODIFY_PNF&quot;
REMOVE_PNF | &quot;REMOVE_PNF&quot;



