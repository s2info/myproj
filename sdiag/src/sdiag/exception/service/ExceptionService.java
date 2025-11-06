package sdiag.exception.service;
import java.util.HashMap;
import java.util.List;

import sdiag.man.service.SearchVO;

public interface ExceptionService {

	/**
	 *  예외처리 IP LIST
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getExceptionIpList()throws Exception;

	/**
	 *  예외처리 IP 정보 저장
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int exceptionIpSave(ExceptionVO exceptionVO)throws Exception;

	/**
	 *  정책 LIST
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPolIdxList() throws Exception;

	/**
	 *  예외처리 IP 정보 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ExceptionVO getExceptionIpInfo(ExceptionVO exceptionVO) throws Exception;
	
	/**
	 *  예외처리 IP 정보 
	 * @param 
	 * @return
	 * @throws Exception
	 */

	public int exceptionIpDelete(ExceptionVO exceptionVO) throws Exception;
	
	
	/**
	 *  예외처리 EmpNo LIST
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getExceptionEmpNoList()throws Exception;

	/**
	 *  예외처리 EmpNo 정보 저장
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int exceptionEmpNoSave(ExceptionVO exceptionVO)throws Exception;

	/**
	 *  예외처리 EmpNo 정보 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ExceptionVO getExceptionEmpNoInfo(ExceptionVO exceptionVO) throws Exception;
	
	/**
	 *  예외처리 EmpNo 삭제
	 * @param 
	 * @return
	 * @throws Exception
	 */

	public int exceptionEmpNoDelete(ExceptionVO exceptionVO) throws Exception;

	/**
	 *  메일발송 예외자 LIST 
	 * @param 
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> getExceptionMailList() throws Exception;

	/**
	 *  메일발송 예외자 정보
	 * @param SecurityDayVO
	 * @return ExceptionVO
	 * @throws Exception
	 */
	public ExceptionVO getExceptionMailInfo(ExceptionVO exceptionVO) throws Exception;

	/**
	 *  메일발송 예외자 정보 삭제
	 * @param SecurityDayVO
	 * @return int
	 * @throws Exception
	 */
	public int exceptionMailDelete(ExceptionVO exceptionVO) throws Exception;

	/**
	 *  메일발송 예외자 정보 저장
	 * @param SecurityDayVO
	 * @return int
	 * @throws Exception
	 */
	public int exceptionMailSave(ExceptionVO exceptionVO) throws Exception;



}
