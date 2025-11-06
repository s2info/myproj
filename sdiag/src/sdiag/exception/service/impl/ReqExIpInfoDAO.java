package sdiag.exception.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.board.service.NoticeVO;
import sdiag.exception.service.ExceptionVO;
import sdiag.exception.service.ReqExIpInfoVO;
import sdiag.exception.service.ReqExIpPolicyInfoVO;
import sdiag.exception.service.ReqSearchVO;
import sdiag.login.service.UserManageVO;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.man.service.SearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ReqExIpInfoDAO")
public class ReqExIpInfoDAO extends EgovAbstractDAO {

	/**
	 *  예외 신청 IP LIST
	 * @param searchVO 
	 */
	public List<ReqExIpInfoVO> getReqExIpInfoList(ReqSearchVO searchVO) throws Exception{
		List<ReqExIpInfoVO> list = (List<ReqExIpInfoVO>)list("reqExIpInfo.getReqExIpInfoList", searchVO);
		return list;
	}
	
	/**
	 *  예외 신청 IP totalCount
	 * @param searchVO 
	 */
	public int getReqExIpInfoListTotalCount(ReqSearchVO searchVO) throws Exception{
		return (int)select("reqExIpInfo.getReqExIpInfoListTotalCount", searchVO);
	}
	
	/**
	 *  예외 신청 IP 정보
	 */
	public List<ExceptionVO> getExceptionIpInfo(
			ExceptionVO exceptionVO) {
		List<ExceptionVO> list = (List<ExceptionVO>)list("reqExIpInfo.getExceptionIpInfo",exceptionVO );
		return list;
	}

	/**
	 *  예외 신청 IP 삭제
	 */
	public int exceptionIpDelete(ExceptionVO exceptionVO) {
		return delete("reqExIpInfo.exceptionIpDelete", exceptionVO);
		
	}

	/**
	 *  예외 신청 seq 조회
	 */
	public int getExSeqInfo() {
		return (int)select("reqExIpInfo.getExSeqInfo");
	}
	
	/**
	 *  예외 신청 IP 정보 저장
	 */
	public void reqExIpInfoSave(ReqExIpInfoVO reqExIpInfoVO) {
		insert("reqExIpInfo.reqExIpInfoSave", reqExIpInfoVO);
	}

	
	/**
	 *  예외 신청 IP 정보 저장(사용자)
	 */
	public void setReqExIpInfoUpdate(ReqExIpInfoVO reqExIpInfoVO) {
		update("reqExIpInfo.setReqExIpInfoUpdate", reqExIpInfoVO);
		
	}
	/**
	 *  예외 신청 IP 정책 리스트 삭제
	 */
	public void setReqExIpPolicyDelete(long reqSeq) {
		delete("reqExIpInfo.setReqExIpPolicyDelete", reqSeq);
	}
	/**
	 *  예외 신청 IP 정책 리스트 저장
	 */
	public void setReqExIpPolicyInsert(ReqExIpInfoVO reqExIpInfoVO) {
		insert("reqExIpInfo.setMailSendPolicyInsert", reqExIpInfoVO);
		
	}
	/**
	 *  예외 신청  사번 유효성 검사
	 */
	public int empNoCheck(ReqExIpInfoVO reqExIpInfoVO) {
		return (int)select("reqExIpInfo.empNoCheck", reqExIpInfoVO);
	}
	
	/**
	 *  예외 신청자 정보 조회
	 */
	public ReqExIpInfoVO getReqUserInfo(ReqExIpInfoVO reqExIpInfoVO) {
		return (ReqExIpInfoVO)select("reqExIpInfo.getReqUserInfo", reqExIpInfoVO);
	}

	/**
	 *  예외 신청자 정책 리스트
	 */
	public List<ReqExIpPolicyInfoVO> getReqExIpPolicyList(ReqExIpInfoVO reqExIpInfoVO) {
		return (List<ReqExIpPolicyInfoVO>)list("reqExIpInfo.getReqExIpPolicyList", reqExIpInfoVO);
	}

	/**
	 *  예외 신청 IP 삭제
	 */
	public void reqExIpInfoDelete(ReqExIpInfoVO reqExIpInfoVO) {
		delete("reqExIpInfo.reqExIpInfoDelete", reqExIpInfoVO);		
	}

	/**
	 *  예외 신청 IP 상세 정보 조회
	 */
	public ReqExIpInfoVO getReqExIpInfo(ReqExIpInfoVO reqExIpInfoVO) {
		return (ReqExIpInfoVO)select("reqExIpInfo.getReqExIpInfo", reqExIpInfoVO);
	}

	/**
	 *  예외 신청 IP 승인
	 * @return 
	 */
	public int reqExIpAppUpdate(ReqExIpInfoVO reqExIpInfoVO) {
		return update("reqExIpInfo.reqExIpAppUpdate", reqExIpInfoVO);
		
	}
	
	/**
	 *  승인 안내 발송 메일 주소 조죟
	 */
	public String getAppEmail(String empNo) {
		return (String)select("reqExIpInfo.getAppEmail", empNo);
	}

	/**
	 *  예외 테이블 중복 체크
	 */
	public int exceptionIpCnt(ReqExIpInfoVO reqExIpInfo) {
		return (int)select("reqExIpInfo.exceptionIpCnt", reqExIpInfo);
	}

	/**
	 *  예외 테이블에 저장
	 */
	public void exceptionIpSave(ReqExIpInfoVO reqExIpInfo) {
		insert("reqExIpInfo.exceptionIpSave", reqExIpInfo);
		
	}

	public void exceptionIpDelete(ReqExIpInfoVO reqExIpInfo) {
		delete("reqExIpInfo.exceptionIpDelete", reqExIpInfo);
	}

	public void exceptionEmpNoDelete(ReqExIpInfoVO reqExIpInfo) {
		delete("reqExIpInfo.exceptionEmpNoDelete", reqExIpInfo);
		
	}
	
	public List<ReqExIpInfoVO> getExInfo(ReqExIpPolicyInfoVO polInfo) {
		return (List<ReqExIpInfoVO>)list("reqExIpInfo.getExInfo", polInfo);
		
	}

	public void exceptionEmpNoSave(ReqExIpInfoVO empNoInfo) {
		insert("reqExIpInfo.exceptionEmpNoSave", empNoInfo);
	}
	
}