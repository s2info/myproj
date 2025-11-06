package sdiag.exception.service;
import java.util.HashMap;
import java.util.List;

import sdiag.man.service.SearchVO;

public interface ReqExIpInfoService {

	/**
	 *  예외 신청 IP LIST
	 * @param searchVO 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getReqExIpInfoList(ReqSearchVO searchVO)throws Exception;

	/**
	 *  예외 신청 IP 정보 저장
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void reqExIpInfoSave(ReqExIpInfoVO reqExIpInfoVO)throws Exception;

	/**
	 *  예외 신청 IP 정보 조회
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ReqExIpInfoVO getReqExIpInfo(ReqExIpInfoVO reqExIpInfoVO) throws Exception;
	
	/**
	 *  예외 신청 IP 정보 삭제
	 * @param 
	 * @return
	 * @throws Exception
	 */

	public void reqExIpInfoDelete(ReqExIpInfoVO reqExIpInfoVO) throws Exception;
	/**
	 *  예외 신청 IP 정보 삭제
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getExSeqInfo() throws Exception;
	
	/**
	 *  예외 신청 IP 정보 수정
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void setReqExIpInfoUpdate(ReqExIpInfoVO reqExIpInfoVO) throws Exception;

	/**
	 *  예외 신청 IP 유효성 검사
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int empNoCheck(ReqExIpInfoVO reqExIpInfoVO) throws Exception;

	/**
	 *  예외 신청 IP 유효성 검사
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ReqExIpInfoVO getReqUserInfo(ReqExIpInfoVO reqExIpInfoVO) throws Exception;

	/**
	 *  예외 신청 정책 리스트
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<ReqExIpPolicyInfoVO> getReqExIpPolicyList(ReqExIpInfoVO reqExIpInfoVO) throws Exception;

	/**
	 *  예외 신청 승인
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int reqExIpAppUpdate(ReqExIpInfoVO reqExIpInfoVO) throws Exception;

	/**
	 * 예외 승인 안내 메일 주소
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public String getAppEmail(String empNo);


	/**
	 * 예외 테이블에  저장
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void exceptionIpSave(ReqExIpPolicyInfoVO polInfo) throws Exception;

	public void exceptionIpDelete(ReqExIpInfoVO reqExIpInfo) throws Exception;

	public void exceptionEmpnoDelete(ReqExIpInfoVO reqExIpInfo) throws Exception;
	
	/**
	 * 예외 테이블에  저장
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void exceptionEmpNoSave(ReqExIpPolicyInfoVO polInfo) throws Exception;

}
