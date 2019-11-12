
# OperateVnfData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceId** | **String** | Identifier of the VNF instance.  | 
**changeStateTo** | [**OperationalStates**](OperationalStates.md) | The desired operational state (i.e. started or stopped) to change the VNF to.  | 
**stopType** | [**StopType**](StopType.md) | It signals whether forceful or graceful stop is requested.  |  [optional]
**gracefulStopTimeout** | **Integer** | The time interval (in seconds) to wait for the VNF to be taken out of service during graceful stop, before stopping the VNF.  |  [optional]



