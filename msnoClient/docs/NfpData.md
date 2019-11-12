
# NfpData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**nfpInfoId** | **String** | Identifier of the NFP to be modified. It shall be present for modified NFPs and shall be absent for the new NFP. It shall be present for modified NFPs and shall be absent for the new NFP.  |  [optional]
**nfpName** | **String** | Human readable name for the NFP. It shall be present for the new NFP, and it may be present otherwise. It shall be present for the new NFP, and it may be present otherwise.  |  [optional]
**description** | **String** | Human readable description for the NFP. It shall be present for the new NFP, and it may be present otherwise. It shall be present for the new NFP, and it may be present otherwise.  |  [optional]
**cpGroup** | [**List&lt;CpGroupInfo&gt;**](CpGroupInfo.md) | Group(s) of CPs and/or SAPs which the NFP passes by. Cardinality can be 0 if only updated or newly created NFP classification and selection rule which applied to an existing NFP is provided. At least a CP or an nfpRule shall be present. When multiple identifiers are included, the position of the identifier in the cpGroup value specifies the position of the group in the path.  |  [optional]
**nfpRule** | [**NfpRule**](NfpRule.md) | NFP classification and selection rule. See note 1.  |  [optional]



