
# NsCpHandle2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceId** | **String** | Identifier of the VNF instance associated to the CP instance. This attribute shall be present if the CP instance is VNF external CP.  |  [optional]
**vnfExtCpInstanceId** | **String** | Identifier of the VNF external CP instance in the scope of the VNF instance. This attribute shall be present if the CP instance is VNF external CP. See notes 1 and 4.  |  [optional]
**pnfInfoId** | **String** | Identifier of the PNF instance associated to the CP instance. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4.  |  [optional]
**pnfExtCpInstanceId** | **String** | Identifier of the PNF external CP instance in the scope of the PNF. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4.  |  [optional]
**nsInstanceId** | **String** | Identifier of the NS instance associated to the SAP instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4.  |  [optional]
**nsSapInstanceId** | **String** | Identifier of the SAP instance in the scope of the NS instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4.  |  [optional]



