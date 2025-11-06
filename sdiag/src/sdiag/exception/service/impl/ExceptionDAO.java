package sdiag.exception.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.board.service.NoticeVO;
import sdiag.exception.service.ExceptionVO;
import sdiag.login.service.UserManageVO;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.man.service.SearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ExceptionDAO")
public class ExceptionDAO extends EgovAbstractDAO {

	/**
	 *  예외처리 IP LIST
	 */
	public List<ExceptionVO> getExceptionIpList() throws Exception{
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionIpList");
		return list;
	}
	
	/**
	 *  예외처리 IP totalCount
	 */
	public int getExceptionIpListTotalCount() throws Exception{
		return (int)select("exception.getExceptionIpListTotalCount");
	}
	
	/**
	 *  예외처리 IP 정보 저장
	 */
	public Object exceptionIpSave(ExceptionVO exceptionVO) throws Exception{
		return insert("exception.exceptionIpSave", exceptionVO);
	}

	public List<ExceptionVO> getPolIdxList() {
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getPolIdxList");
		return list;
	}

	/**
	 *  예외처리 IP 정보
	 */
	public List<ExceptionVO> getExceptionIpInfo(
			ExceptionVO exceptionVO) {
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionIpInfo",exceptionVO );
		return list;
	}

	/**
	 *  예외처리 IP 삭제
	 */
	public int exceptionIpDelete(ExceptionVO exceptionVO) {
		return delete("exception.exceptionIpDelete", exceptionVO);
		
	}
	
	
	/**
	 *  예외처리 EmpNo LIST
	 */
	public List<ExceptionVO> getExceptionEmpNoList() throws Exception{
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionEmpNoList");
		return list;
	}
	
	/**
	 *  예외처리 EmpNo totalCount
	 */
	public int getExceptionEmpNoListTotalCount() throws Exception{
		return (int)select("exception.getExceptionEmpNoListTotalCount");
	}
	
	/**
	 *  예외처리 EmpNo 정보 저장
	 */
	public Object exceptionEmpNoSave(ExceptionVO exceptionVO) throws Exception{
		return insert("exception.exceptionEmpNoSave", exceptionVO);
	}

	/**
	 *  예외처리 EmpNo 정보
	 */
	public List<ExceptionVO> getExceptionEmpNoInfo(
			ExceptionVO exceptionVO) {
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionEmpNoInfo",exceptionVO );
		return list;
	}

	/**
	 *  예외처리 EmpNo 삭제
	 */
	public int exceptionEmpNoDelete(ExceptionVO exceptionVO) {
		return delete("exception.exceptionEmpNoDelete", exceptionVO);
		
	}

	/**
	 *  사번 유효 체크
	 */
	public int getOrgUserInfo(String empNo) {
		return (int)select("exception.getOrgUserInfo", empNo);
	}

	/**
	 *  예외처리 Ip 중복 체크
	 */
	public int exceptionIpCnt(ExceptionVO exceptionVO) {
		return (int)select("exception.exceptionIpCnt", exceptionVO);
	}
	
	/**
	 *  예외처리 사번 중복 체크
	 */
	public int exceptionEmpNoCnt(ExceptionVO exceptionVO) {
		return (int)select("exception.exceptionEmpNoCnt", exceptionVO);
	}
	
	/**
	 *  메일예외처리 EmpNo LIST
	 */
	public List<ExceptionVO> getExceptionMailList() throws Exception{
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionMailList");
		return list;
	}
	
	
	/**
	 *  메일예외처리 EmpNo 정보 저장
	 */
	public Object exceptionMailSave(ExceptionVO exceptionVO) throws Exception{
		return insert("exception.exceptionMailSave", exceptionVO);
	}

	/**
	 *  메일예외처리 EmpNo 정보
	 */
	public List<ExceptionVO> getExceptionMailInfo(
			ExceptionVO exceptionVO) {
		List<ExceptionVO> list = (List<ExceptionVO>)list("exception.getExceptionMailInfo",exceptionVO );
		return list;
	}

	/**
	 *  메일예외처리 EmpNo 삭제
	 */
	public int exceptionMailDelete(ExceptionVO exceptionVO) {
		return delete("exception.exceptionMailDelete", exceptionVO);
		
	}


	/**
	 *  메일예외처리 사번 중복 체크
	 */
	public int exceptionMailCnt(ExceptionVO exceptionVO) {
		return (int)select("exception.exceptionMailCnt", exceptionVO);
	}
}