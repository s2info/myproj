package sdiag.exception.service.impl;

import java.sql.SQLNonTransientException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import sdiag.exception.service.ExceptionVO;
import sdiag.exception.service.ReqExIpInfoService;
import sdiag.exception.service.ReqExIpInfoVO;
import sdiag.exception.service.ReqExIpPolicyInfoVO;
import sdiag.exception.service.ReqSearchVO;

@Service("ReqExIpInfoService")
public class ReqExIpInfoServiceImpl implements ReqExIpInfoService{
	@Resource(name = "ReqExIpInfoDAO")
	private ReqExIpInfoDAO reqExIpInfoDAO;
	
	@Resource(name = "ExceptionDAO")
	private ExceptionDAO exceptionDAO;

	/**
	 *  예외 신청 IP LIST
	 */
	public HashMap<String, Object> getReqExIpInfoList(ReqSearchVO searchVO) throws Exception {
		List<ReqExIpInfoVO> List = reqExIpInfoDAO.getReqExIpInfoList(searchVO);
		int TotalCount = reqExIpInfoDAO.getReqExIpInfoListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}
	
	/**
	 *  예외 IP 신청 사번 저장
	 */
	public void reqExIpInfoSave(ReqExIpInfoVO reqExIpInfoVO)
			throws Exception {
		if(reqExIpInfoVO.getFormMod().equals("U")){
			reqExIpInfoDAO.setReqExIpInfoUpdate(reqExIpInfoVO);
		}else{
			reqExIpInfoDAO.reqExIpInfoSave(reqExIpInfoVO);
		}
		
		//정책 정보 삭제
		reqExIpInfoDAO.setReqExIpPolicyDelete(reqExIpInfoVO.getExSeq());
		if(reqExIpInfoVO.getReqPolicyList() != null){
			// 정책 정보 저장
			reqExIpInfoDAO.setReqExIpPolicyInsert(reqExIpInfoVO);
		}
		
	}

	/**
	 *  예외 신청 IP 상세 정보 조회
	 */
	public ReqExIpInfoVO getReqExIpInfo(ReqExIpInfoVO reqExIpInfoVO)
			throws Exception {
		return reqExIpInfoDAO.getReqExIpInfo(reqExIpInfoVO);
	}

	/**
	 *  예외 신청 IP 정보 삭제
	 */
	public void reqExIpInfoDelete(ReqExIpInfoVO reqExIpInfoVO)
			throws Exception {
		reqExIpInfoDAO.reqExIpInfoDelete(reqExIpInfoVO);
		reqExIpInfoDAO.setReqExIpPolicyDelete(reqExIpInfoVO.getExSeq());
	}

	/**
	 *  예외 신청 IP LIST
	 */
	public int getExSeqInfo() throws Exception {
		return reqExIpInfoDAO.getExSeqInfo();
	}

	/**
	 *  예외 신청 IP 정보 저장(사용자)
	 */
	public void setReqExIpInfoUpdate(ReqExIpInfoVO reqExIpInfoVO) {
		
		//예외 신청 IP 정보 저장
		reqExIpInfoDAO.setReqExIpInfoUpdate(reqExIpInfoVO);
		
		//정책 정보 삭제
		reqExIpInfoDAO.setReqExIpPolicyDelete(reqExIpInfoVO.getExSeq());
		if(reqExIpInfoVO.getReqPolicyList() != null){
			// 정책 정보 저장
			reqExIpInfoDAO.setReqExIpPolicyInsert(reqExIpInfoVO);
		}
	}

	/**
	 *  예외 신청 IP 유효성 검사
	 */
	public int empNoCheck(ReqExIpInfoVO reqExIpInfoVO) throws Exception {
		return reqExIpInfoDAO.empNoCheck(reqExIpInfoVO);
	}

	/**
	 *  예외 신청자 정보
	 */
	public ReqExIpInfoVO getReqUserInfo(ReqExIpInfoVO reqExIpInfoVO)
			throws Exception {
		return reqExIpInfoDAO.getReqUserInfo(reqExIpInfoVO);
	}

	/**
	 *  예외 신청 정책 리스트
	 */
	public List<ReqExIpPolicyInfoVO> getReqExIpPolicyList(ReqExIpInfoVO reqExIpInfoVO)
			throws Exception {
		return reqExIpInfoDAO.getReqExIpPolicyList(reqExIpInfoVO);
	}

	/**
	 *  예외 신청 IP 승인
	 */
	public int reqExIpAppUpdate(ReqExIpInfoVO reqExIpInfoVO) throws Exception {
		return reqExIpInfoDAO.reqExIpAppUpdate(reqExIpInfoVO);
	}

	/**
	 *  승인 안내 발송 메일 주소 조죟
	 */
	public String getAppEmail(String empNo) {
		return reqExIpInfoDAO.getAppEmail(empNo);
	}

	/**
	 *  예외 IP 테이블에 저장
	 */
	public void exceptionIpSave(ReqExIpPolicyInfoVO polInfo) throws Exception {
		polInfo.setIs_selected("I");
		
		List<ReqExIpInfoVO> reqExIpInfoList = reqExIpInfoDAO.getExInfo(polInfo);
		
		
		//reqExIpInfoVO = reqExIpInfoDAO.getReqExIpInfo(reqExIpInfoVO);
		
		//String[] polList = reqExIpInfoVO.getPolSelectList().split(",");
		
		
		for(ReqExIpInfoVO reqExIpInfo : reqExIpInfoList){
			ReqExIpInfoVO ipInfo = new ReqExIpInfoVO();
			String[] ipList =  reqExIpInfo.getExInfo().split("\n");
			
			for(String ip:ipList){
				ip = ip.replaceAll("\r", "");
				if(!ip.isEmpty() && ip !="" && ip !=null){
					ipInfo.setExInfo(ip);
					ipInfo.setSecPolId(polInfo.getSec_pol_id());

					/*//중복여부 체크 
					int cnt = exceptionDAO.exceptionIpCnt(ipInfo);
					if(cnt == 0){*/
					reqExIpInfoDAO.exceptionIpSave(ipInfo);
					/*}*/
						
				}
				
			}
		}
		
	}
	
	/**
	 *  예외 테이블에 저장
	 */
	public void exceptionIpDelete(ReqExIpInfoVO reqExIpInfoVO) throws Exception {
		
		List<ReqExIpPolicyInfoVO> reqExIpPolicyList = reqExIpInfoDAO.getReqExIpPolicyList(reqExIpInfoVO);
		
		
		reqExIpInfoVO = reqExIpInfoDAO.getReqExIpInfo(reqExIpInfoVO);
		
		//String[] polList = reqExIpInfoVO.getPolSelectList().split(",");
		String[] ipList =  reqExIpInfoVO.getExInfo().split("\n");
		
		for(ReqExIpPolicyInfoVO reqExIpPolicyInfo : reqExIpPolicyList){
			ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
			
			if(reqExIpPolicyInfo.getIs_selected().equals("Y"))
				reqExIpInfo.setSecPolId(reqExIpPolicyInfo.getSec_pol_id());
			else
				continue;
			
			for(String ip:ipList){
				ip = ip.replaceAll("\r", "");
				if(!ip.isEmpty() && ip !="" && ip !=null){
					reqExIpInfo.setExInfo(ip);
					reqExIpInfoDAO.exceptionIpDelete(reqExIpInfo);
				}
				
			}
		}
		
	}
	
	/**
	 *  예외 테이블에 저장
	 */
	public void exceptionEmpnoDelete(ReqExIpInfoVO reqExIpInfoVO) throws Exception {
		
		
		List<ReqExIpPolicyInfoVO> reqExIpPolicyList = reqExIpInfoDAO.getReqExIpPolicyList(reqExIpInfoVO);
		
		
		reqExIpInfoVO = reqExIpInfoDAO.getReqExIpInfo(reqExIpInfoVO);
		
		//String[] polList = reqExIpInfoVO.getPolSelectList().split(",");
		String[] exInfoList =  reqExIpInfoVO.getExInfo().split("\n");
		
		for(ReqExIpPolicyInfoVO reqExIpPolicyInfo : reqExIpPolicyList){
			ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
			
			if(reqExIpPolicyInfo.getIs_selected().equals("Y"))
				reqExIpInfo.setSecPolId(reqExIpPolicyInfo.getSec_pol_id());
			else
				continue;
			
			for(String exInfo:exInfoList){
				exInfo = exInfo.replaceAll("\r", "");
				if(!exInfo.isEmpty() && exInfo !="" && exInfo !=null){
					reqExIpInfo.setExInfo(exInfo);
					reqExIpInfoDAO.exceptionEmpNoDelete(reqExIpInfo);
					
				}
				
			}
		}
		
	}
	
	/**
	 *  예외 사번 테이블에 저장
	 */
	public void exceptionEmpNoSave(ReqExIpPolicyInfoVO polInfo) throws Exception {
		polInfo.setIs_selected("E");
		List<ReqExIpInfoVO> reqExIpInfoList = reqExIpInfoDAO.getExInfo(polInfo);
		
		
		//reqExIpInfoVO = reqExIpInfoDAO.getReqExIpInfo(reqExIpInfoVO);
		
		//String[] polList = reqExIpInfoVO.getPolSelectList().split(",");
		
		
		for(ReqExIpInfoVO reqExEmpNoInfo : reqExIpInfoList){
			ReqExIpInfoVO empNoInfo = new ReqExIpInfoVO();
			String[] empNoList =  reqExEmpNoInfo.getExInfo().split("\n");
			
			for(String empNo:empNoList){
				empNo = empNo.replaceAll("\r", "");
				if(!empNo.isEmpty() && empNo !="" && empNo !=null){
					empNoInfo.setExInfo(empNo);
					empNoInfo.setSecPolId(polInfo.getSec_pol_id());

					/*//중복여부 체크 
					int cnt = exceptionDAO.exceptionEmpNoCnt(empNoInfo);
					if(cnt == 0){*/
					reqExIpInfoDAO.exceptionEmpNoSave(empNoInfo);
					/*}*/
						
				}
				
			}
		}
		
	}

}