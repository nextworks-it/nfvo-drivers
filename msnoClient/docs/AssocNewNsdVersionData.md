
# AssocNewNsdVersionData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**newNsdId** | **String** | Identifier of the new NSD version that is to be associated to the NS instance.  | 
**sync** | **Boolean** | Specify whether the NS instance shall be automatically synchronized to the new NSD by the NFVO (in case of true value) or the NFVO shall not do any action (in case of a false value) and wait for further guidance from OSS/BSS (i.e. waiting for OSS/BSS to issue NS lifecycle management operation to explicitly add/remove VNFs and modify information of VNF instances according to the new NSD). The synchronization to the new NSD means e.g. instantiating/adding those VNFs whose VNFD is referenced by the new NSD version but not referenced by the old one, terminating/removing those VNFs whose VNFD is referenced by the old NSD version but not referenced by the new NSD version, modifying information of VNF instances to the new applicable VNFD provided in the new NSD version. A cardinality of 0 indicates that synchronization shall not be done.  |  [optional]



