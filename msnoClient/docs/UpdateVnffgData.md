
# UpdateVnffgData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnffgInfoId** | **String** | Identifier of an existing VNFFG to be updated for the NS Instance.  | 
**nfp** | [**List&lt;NfpData&gt;**](NfpData.md) | Indicate the desired new NFP(s) for a given VNFFG after the operations of addition/removal of NS components (e.g. VNFs, VLs, etc.) have been completed, or indicate the updated or newly created NFP classification and selection rule which applied to an existing NFP.  |  [optional]
**nfpInfoId** | **List&lt;String&gt;** | Identifier(s) of the NFP to be deleted from a given VNFFG.  |  [optional]



