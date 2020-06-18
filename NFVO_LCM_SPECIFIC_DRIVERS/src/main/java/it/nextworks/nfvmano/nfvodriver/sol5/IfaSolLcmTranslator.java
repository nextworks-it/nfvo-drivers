package it.nextworks.nfvmano.nfvodriver.sol5;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.nextworks.nfvmano.libs.ifa.common.elements.AffinityRule;
import it.nextworks.nfvmano.libs.ifa.common.elements.ResourceHandle;
import it.nextworks.nfvmano.libs.ifa.common.enums.AffinityScope;
import it.nextworks.nfvmano.libs.ifa.common.enums.AffinityType;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsLinkPort;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfInfo;
import it.nextworks.nfvmano.nfvodriver.sol5.im.*;
import it.nextworks.openapi.msno.model.*;
import it.nextworks.openapi.msno.model.CpProtocolData.LayerProtocolEnum;
import it.nextworks.openapi.msno.model.AffinityOrAntiAffinityRule.AffinityOrAntiAffiintyEnum;
import it.nextworks.openapi.msno.model.AffinityOrAntiAffinityRule.ScopeEnum;
import it.nextworks.openapi.msno.model.IpOverEthernetAddressDataIpAddresses.TypeEnum;


public class IfaSolLcmTranslator {

	public static CreateNsRequest buildSolCreateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest request) {
		CreateNsRequest body = new CreateNsRequest();
		body.setNsDescription(request.getNsDescription());
		body.setNsdId(request.getNsdId());
		body.setNsName(request.getNsName());
		return body;
	}
	
	public static it.nextworks.openapi.msno.model.InstantiateNsRequest buildSolInstantiateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest request) {
		it.nextworks.openapi.msno.model.InstantiateNsRequest body = new it.nextworks.openapi.msno.model.InstantiateNsRequest();
		
		List<AffinityOrAntiAffinityRule> additionalAffinityOrAntiAffinityRule = new ArrayList<AffinityOrAntiAffinityRule>();
		List<AffinityRule> ars = request.getAdditionalAffinityOrAntiAffinityRule();
		for (AffinityRule ar : ars) additionalAffinityOrAntiAffinityRule.add(translateAffinityRule(ar));
		if (!additionalAffinityOrAntiAffinityRule.isEmpty()) body.setAdditionalAffinityOrAntiAffinityRule(additionalAffinityOrAntiAffinityRule);
		
		List<ParamsForNestedNs> additionalParamForNestedNs = new ArrayList<>();
		//This is not available in IFA
		if (!additionalParamForNestedNs.isEmpty()) body.setAdditionalParamForNestedNs(additionalParamForNestedNs);
		
		Map<String,String> apn = request.getAdditionalParamForNs();
		KeyValuePairsString additionalParamsForNs = translateStringMap(apn);
		if (!additionalParamsForNs.isEmpty()) body.setAdditionalParamsForNs(additionalParamsForNs);
		
		List<it.nextworks.openapi.msno.model.ParamsForVnf> additionalParamsForVnf = new ArrayList<>();
		List<it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.ParamsForVnf> inputPfvs = request.getAdditionalParamForVnf();
		for (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.ParamsForVnf inputPfv : inputPfvs) {
			additionalParamsForVnf.add(translateParamsForVnf(inputPfv));
		}
		if (!additionalParamsForVnf.isEmpty()) body.setAdditionalParamsForVnf(additionalParamsForVnf);
		
		
		List<AddPnfData> addpnfData = new ArrayList<>();
		List<PnfInfo> pnfInfos = request.getPnfInfo();
		for (PnfInfo pnfInfo : pnfInfos) {
			addpnfData.add(translatePnfInfo(pnfInfo));
		}
		if (!addpnfData.isEmpty()) body.setAddpnfData(addpnfData);
		
		List<it.nextworks.openapi.msno.model.VnfLocationConstraint> localizationLanguage = new ArrayList<>();
		List<it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfLocationConstraints> locationConstraints = request.getLocationConstraints();
		for (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfLocationConstraints lc : locationConstraints)
			localizationLanguage.add(translateVnfLocationConstraints(lc));
		if (!localizationLanguage.isEmpty()) body.setLocalizationLanguage(localizationLanguage);
		
		List<it.nextworks.openapi.msno.model.NestedNsInstanceData> nestedNsInstanceData = new ArrayList<>();
		List<String> nestedNsInstanceIds = request.getNestedNsInstanceId();
		for (String id : nestedNsInstanceIds) {
			NestedNsInstanceData nestedData = new NestedNsInstanceData();
			nestedData.setNestedNsInstanceId(id);
			nestedData.setNsProfileId(null);
			nestedNsInstanceData.add(nestedData);
		}
		if (!nestedNsInstanceData.isEmpty()) body.setNestedNsInstanceData(nestedNsInstanceData);
		
		String nsFlavourId = request.getFlavourId();
		body.setNsFlavourId(nsFlavourId);
		
		String nsInstantiationLevelId = request.getNsInstantiationLevelId();
		body.setNsInstantiationLevelId(nsInstantiationLevelId);
		
		List<it.nextworks.openapi.msno.model.SapData> sapData = new ArrayList<>(); 
		List<it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.SapData> inSapData = request.getSapData();
		for (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.SapData sap : inSapData) {
			sapData.add(translateSapData(sap));
		}
		if (!sapData.isEmpty()) body.setSapData(sapData);
		
		if (request.getStartTime() != null)
			body.setStartTime(request.getStartTime().toString());
		
		List<it.nextworks.openapi.msno.model.VnfInstanceData> vnfInstanceData = new ArrayList<>();
		List<it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfInstanceData> inVnfInstanceData = request.getVnfInstanceData();
		for (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfInstanceData inVnfInstance : inVnfInstanceData) {
			vnfInstanceData.add(translateVnfInstanceData(inVnfInstance));
		}
		if (!vnfInstanceData.isEmpty()) body.setVnfInstanceData(vnfInstanceData);
		return body;
	}
	
	public static it.nextworks.openapi.msno.model.TerminateNsRequest buildSolTerminateNsRequest(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest request) {
		it.nextworks.openapi.msno.model.TerminateNsRequest body = new it.nextworks.openapi.msno.model.TerminateNsRequest();
		if (request.getTerminateTime() != null)
			body.setTerminationTime(request.getTerminateTime().toString());
		return body;
	}
	
	public static NsInfo buildIfaNsInfo(it.nextworks.openapi.msno.model.NsInstance nsInstance) {
		String nsInstanceId = nsInstance.getId();
		String nsName = nsInstance.getNsInstanceName();
		String description  = nsInstance.getNsInstanceDescription();
		String nsdId = nsInstance.getNsdId();
		Map<String, String> configurationParameters = new HashMap<String, String>();	//not available in SOL
		String flavourId = nsInstance.getFlavourId();
		List<String> vnfInfoId = new ArrayList<>();
		List<VnfInstance> vnfis = nsInstance.getVnfInstance();
		for (VnfInstance vnfi : vnfis) {
			vnfInfoId.add(vnfi.getId());					//TODO: shall we keep this aligned with SOL instead of IFA? Otherwise no way to get info about VNFs
		}
		List<PnfInfo> pnfInfos = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.PnfInfo> inPnfInfo = nsInstance.getPnfInfo();
		if(inPnfInfo!=null &&!inPnfInfo.isEmpty()){
            for (it.nextworks.openapi.msno.model.PnfInfo inp : inPnfInfo) {
                List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfExtCpInfo> cpInfos = new ArrayList<>();
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfExtCpInfo cpInfo = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfExtCpInfo(inp.getCpInfo().getCpdId(),
                        inp.getCpInfo().getCpProtocolData().get(0).getIpOverEthernet().getIpAddresses().get(0).getFixedAddresses().get(0));
                cpInfos.add(cpInfo);
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfInfo pnfInfo = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfInfo(null,
                        inp.getPnfId(), inp.getPnfName(), inp.getPnfdId(), inp.getPnfdInfoId(), inp.getPnfProfileId(), cpInfos);
                pnfInfos.add(pnfInfo);
            }

        }

		List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsVirtualLinkInfo> virtualLinkInfo = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.NsVirtualLinkInfo> vlis = nsInstance.getVirtualLinkInfo();


		if(vlis != null && !vlis.isEmpty()){
            for (it.nextworks.openapi.msno.model.NsVirtualLinkInfo vli : vlis) {
                List<it.nextworks.nfvmano.libs.ifa.common.elements.ResourceHandle> resourceHandles = new ArrayList<ResourceHandle>();
                List<it.nextworks.openapi.msno.model.ResourceHandle> rhs = vli.getResourceHandle();
                for (it.nextworks.openapi.msno.model.ResourceHandle rh : rhs) {
                    it.nextworks.nfvmano.libs.ifa.common.elements.ResourceHandle rHandle = new it.nextworks.nfvmano.libs.ifa.common.elements.ResourceHandle(rh.getVimId(),
                            rh.getResourceProviderId(), rh.getResourceId(), rh.getVimLevelResourceType());
                    resourceHandles.add(rHandle);
                }
                List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsLinkPort> linkPorts = new ArrayList<NsLinkPort>();
                List<it.nextworks.openapi.msno.model.NsLinkPortInfo> lps = vli.getLinkPort();
                for (it.nextworks.openapi.msno.model.NsLinkPortInfo lp : lps) {
                    it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsLinkPort lPort = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsLinkPort(null,
                            new it.nextworks.nfvmano.libs.ifa.common.elements.ResourceHandle(lp.getResourceHandle().getVimId(),
                                    lp.getResourceHandle().getResourceProviderId(), lp.getResourceHandle().getResourceId(), lp.getResourceHandle().getVimLevelResourceType()),
                            readCpIdFromNsCpHandle(lp.getNsCpHandle().get(0)),
                            false);
                    linkPorts.add(lPort);
                }
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsVirtualLinkInfo vlInfo = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsVirtualLinkInfo(null, vli.getId(),
                        vli.getNsVirtualLinkDescId(),
                        resourceHandles,
                        linkPorts);
            }

        }


		List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.VnffgInfo> vnffgInfos = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.VnffgInfo> vfgis = nsInstance.getVnffgInfo();
		if(vfgis!=null && !vfgis.isEmpty()){
            for (it.nextworks.openapi.msno.model.VnffgInfo vfgi : vfgis) {
                List<String> cpId = new ArrayList<String>();
                List<NsCpHandle> cpHandles = vfgi.getNsCpHandle();
                for (NsCpHandle ch : cpHandles) cpId.add(readCpIdFromNsCpHandle(ch));
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.VnffgInfo vnffgi = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.VnffgInfo(null,
                        vfgi.getId(),
                        vfgi.getVnffgdId(),
                        vfgi.getVnfInstanceId(),
                        vfgi.getPnfdInfoId(),
                        vfgi.getNsVirtualLinkInfoId(),
                        cpId);
                vnffgInfos.add(vnffgi);
            }
        }

		
		List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo> sapInfos = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.SapInfo> sis = nsInstance.getSapInfo();

		if(sis != null && !sis.isEmpty()){
            for (it.nextworks.openapi.msno.model.SapInfo si : sis) {
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo sapInfo = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo(null,
                        si.getId(),
                        si.getSapdId(),
                        si.getSapName(),
                        si.getDescription(),
                        readIpAddressFromCpProtocolInfo(si.getSapProtocolInfo()),
                        null);
                sapInfos.add(sapInfo);
            }
        }else{
		    sapInfos= getSapInfoFromVnfInstance(vnfis);
        }

		
		List<String> nestedNsInfoId = nsInstance.getNestedNsInstanceId();
		
		it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState nsState = translateNsState(nsInstance.getNsState());
		
		List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsScaleInfo> nsScaleStatus = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.NsScaleInfo> nsScaleInfos = nsInstance.getNsScaleStatus();
		if(nsScaleInfos!=null && !nsScaleInfos.isEmpty()){
            for (it.nextworks.openapi.msno.model.NsScaleInfo nsScaleInfo : nsScaleInfos) {
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsScaleInfo nsi = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsScaleInfo(nsScaleInfo.getNsScalingAspectId(),
                        nsScaleInfo.getNsScaleLevelId());
                nsScaleStatus.add(nsi);
            }
        }

		
		List<it.nextworks.nfvmano.libs.ifa.common.elements.AffinityRule> additionalAffinityOrAntiAffinityRule = new ArrayList<>();
		List<it.nextworks.openapi.msno.model.AffinityOrAntiAffinityRule> aars = nsInstance.getAdditionalAffinityOrAntiAffinityRule();
		if(aars!=null && !aars.isEmpty()){
            for (it.nextworks.openapi.msno.model.AffinityOrAntiAffinityRule aar : aars) {
                it.nextworks.nfvmano.libs.ifa.common.elements.AffinityRule rule = translateAffinityRule(aar);
                additionalAffinityOrAntiAffinityRule.add(rule);
            }
        }

		
		String monitoringDashboardUrl = null;	//this is not standard
		
		NsInfo nsInfo = new NsInfo(nsInstanceId, nsName, description, nsdId, null,
				configurationParameters, flavourId, vnfInfoId, pnfInfos,
				virtualLinkInfo, vnffgInfos, sapInfos,
				nestedNsInfoId, nsState, nsScaleStatus,
				additionalAffinityOrAntiAffinityRule, monitoringDashboardUrl);
		return nsInfo;
	}

	private static List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo> getSapInfoFromVnfInstance(List<VnfInstance> vnfis){
        List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo> sapInfos = new ArrayList<>();
        for(VnfInstance vnfI: vnfis){
            VnfInstanceInstantiatedVnfInfo instanceInfo = vnfI.getInstantiatedVnfInfo();
            List<VnfExtCpInfo> extCpInfos = instanceInfo.getExtCpInfo();
            for(VnfExtCpInfo cpInfo: extCpInfos){
                List<CpProtocolInfo> cpProtocolInfos = cpInfo.getCpProtocolInfo();
                String address = cpProtocolInfos.get(0).getIpOverEthernet().getIpAddresses().get(0).getAddresses().get(0);
                String sapName = vnfI.getVnfdId()+"-"+cpInfo.getCpdId();
                it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo sapInfo
                        = new it.nextworks.nfvmano.libs.ifa.records.nsinfo.SapInfo(null, cpInfo.getId(), cpInfo.getCpdId(),sapName, sapName,address, null );
                sapInfos.add(sapInfo);




            }

        }
        return sapInfos;

    }
	
	private static String readCpIdFromNsCpHandle(NsCpHandle nsCpHandle) {
		if (nsCpHandle == null) return null;
		if (nsCpHandle.getNsSapInstanceId() != null) return nsCpHandle.getNsSapInstanceId();
		if (nsCpHandle.getPnfExtCpInstanceId() != null) return nsCpHandle.getPnfExtCpInstanceId();
		if (nsCpHandle.getVnfExtCpInstanceId() != null) return nsCpHandle.getVnfExtCpInstanceId();
		return null;
	}
	
	private static String readIpAddressFromCpProtocolInfo(List<it.nextworks.openapi.msno.model.CpProtocolInfo> infos) {
		if ((infos == null) || infos.isEmpty()) return null;
		IpOverEthernetAddressInfo addressInfo = infos.get(0).getIpOverEthernet();
		if (addressInfo != null) {
			List<String> ips = addressInfo.getAddresses();
			if (ips.isEmpty()) return null;
			else return ips.get(0);
		}
		return null;
	}
	
	private static it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState translateNsState(it.nextworks.openapi.msno.model.NsInstance.NsStateEnum input) {
		it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState nsState = it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState.NOT_INSTANTIATED;
		if (input.equals(it.nextworks.openapi.msno.model.NsInstance.NsStateEnum.INSTANTIATED)) nsState = it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState.INSTANTIATED;
		return nsState;	
	}
	
	private static AffinityOrAntiAffinityRule translateAffinityRule(AffinityRule ar) {
		AffinityOrAntiAffinityRule aaRule = new AffinityOrAntiAffinityRule();
		AffinityType at = ar.getAffinityType();
		if (at.equals(AffinityType.AFFINITY))
			aaRule.setAffinityOrAntiAffiinty(AffinityOrAntiAffiintyEnum.AFFINITY);
		else if (at.equals(AffinityType.ANTI_AFFINITY))
			aaRule.setAffinityOrAntiAffiinty(AffinityOrAntiAffiintyEnum.ANTI_AFFINITY);
		AffinityScope ac = ar.getAffinityScope();
		if (ac.equals(AffinityScope.NFVI_NODE))
			aaRule.setScope(ScopeEnum.NFVI_NODE);
		else if (ac.equals(AffinityScope.NFVI_POP))
			aaRule.setScope(ScopeEnum.NFVI_POP);
		else if (ac.equals(AffinityScope.ZONE))
			aaRule.setScope(ScopeEnum.ZONE);
		else if (ac.equals(AffinityScope.ZONE_GROUP))
			aaRule.setScope(ScopeEnum.ZONE_GROUP);
		
		aaRule.setVnfdId(null);
		aaRule.setVnfInstanceId(null);
		aaRule.setVnfProfileId(null);
		return aaRule;
	}
	
	private static AffinityRule translateAffinityRule(AffinityOrAntiAffinityRule ar) {
		AffinityType affinityType = AffinityType.AFFINITY;
		AffinityScope affinityScope = AffinityScope.NFVI_NODE;
		if (ar.getAffinityOrAntiAffiinty().equals(AffinityOrAntiAffiintyEnum.ANTI_AFFINITY)) affinityType = AffinityType.ANTI_AFFINITY;
		if (ar.getScope().equals(ScopeEnum.NFVI_POP)) affinityScope = AffinityScope.NFVI_POP;
		else if (ar.getScope().equals(ScopeEnum.ZONE)) affinityScope = AffinityScope.ZONE;
		else if (ar.getScope().equals(ScopeEnum.ZONE_GROUP)) affinityScope = AffinityScope.ZONE_GROUP;
		AffinityRule aaRule = new AffinityRule(affinityType, affinityScope);
		return aaRule;
	}
	
	private static it.nextworks.openapi.msno.model.VnfInstanceData translateVnfInstanceData (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfInstanceData input) {
		it.nextworks.openapi.msno.model.VnfInstanceData output = new it.nextworks.openapi.msno.model.VnfInstanceData();
		output.setVnfInstanceId(input.getVnfInstanceId());
		output.setVnfProfileId(input.getVnfProfileId());
		return output;
	}
	
	private static it.nextworks.openapi.msno.model.SapData translateSapData(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.SapData input) {
		it.nextworks.openapi.msno.model.SapData output = new it.nextworks.openapi.msno.model.SapData();
		output.setDescription(input.getDescription());
		output.setSapdId(input.getSapdId());
		output.setSapName(input.getSapName());
		List<CpProtocolData> sapProtocolData = new ArrayList<>();
		CpProtocolData x = new CpProtocolData();
		IpOverEthernetAddressData ipOverEthernet = new IpOverEthernetAddressData();
		IpOverEthernetAddressDataIpAddresses ip = new IpOverEthernetAddressDataIpAddresses();
		ip.setType(TypeEnum.IPV4);
		ip.addFixedAddressesItem(input.getAddress());
		ipOverEthernet.addIpAddressesItem(ip);
		x.setIpOverEthernet(ipOverEthernet);
		x.setLayerProtocol(LayerProtocolEnum.IP_OVER_ETHERNET);
		sapProtocolData.add(x);
		output.setSapProtocolData(sapProtocolData);
		return output;
	}
	
	private static it.nextworks.openapi.msno.model.ParamsForVnf translateParamsForVnf(it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.ParamsForVnf pfv) {
		it.nextworks.openapi.msno.model.ParamsForVnf output = new it.nextworks.openapi.msno.model.ParamsForVnf();
		output.setAdditionalParams(translateStringMap(pfv.getAdditionalParam()));
		output.setVnfProfileId(pfv.getVnfProfileId());
		return output;
	}
	
	private static it.nextworks.openapi.msno.model.VnfLocationConstraint translateVnfLocationConstraints (it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.VnfLocationConstraints input) {
		it.nextworks.openapi.msno.model.VnfLocationConstraint output = new it.nextworks.openapi.msno.model.VnfLocationConstraint();
		LocationConstraints lc = new LocationConstraints();
		//lc format is not compatible at the moment
		output.setLocationConstraints(lc);
		output.setVnfProfileId(input.getVnfProfileId());
		return output;
	}
	
	private static AddPnfData translatePnfInfo(PnfInfo pnfInfo) {
		AddPnfData output = new AddPnfData();
		output.setPnfdId(pnfInfo.getPnfdId());
		output.setPnfId(pnfInfo.getPnfId());
		output.setPnfName(pnfInfo.getPnfName());
		output.setPnfProfileId(pnfInfo.getPnfProfileId());
		List<it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfExtCpInfo> cpInfos = pnfInfo.getCpInfo();
		for (it.nextworks.nfvmano.libs.ifa.records.nsinfo.PnfExtCpInfo cpInfo : cpInfos) {
			PnfExtCpData cpdi = new PnfExtCpData();
			cpdi.setCpdId(cpInfo.getCpdId());
			cpdi.setCpInstanceI16(cpInfo.getAddress());
			List<CpProtocolData> cpProtocolData = new ArrayList<>();
			CpProtocolData cpd = new CpProtocolData();
			cpd.setLayerProtocol(LayerProtocolEnum.IP_OVER_ETHERNET);
			cpProtocolData.add(cpd);
			cpdi.setCpProtocolData(cpProtocolData);
			output.addCpDataItem(cpdi);
		}
		return output;
	}
 
	private static KeyValuePairsString translateStringMap(Map<String, String> input) {
		KeyValuePairsString output = new KeyValuePairsString();
		for (Map.Entry<String, String> entry : input.entrySet()) 
			output.add(entry.getKey(), entry.getValue());
		return output;
	}
	
}
