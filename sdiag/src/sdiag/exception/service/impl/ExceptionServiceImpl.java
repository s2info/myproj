package sdiag.exception.service.impl;

import java.sql.SQLNonTransientException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import sdiag.exception.service.ExceptionService;
import sdiag.exception.service.ExceptionVO;

@Service("ExceptionService")
public class ExceptionServiceImpl implements ExceptionService{
	@Resource(name = "ExceptionDAO")
	private ExceptionDAO exceptionDAO;

	/**
	 *  예외처리 IP LIST
	 */
	public HashMap<String, Object> getExceptionIpList() throws Exception {
		List<ExceptionVO> List = exceptionDAO.getExceptionIpList();
		int TotalCount = exceptionDAO.getExceptionIpListTotalCount();
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  예외처리 IP 정보 저장
	 */
	public int exceptionIpSave(ExceptionVO exceptionVO) throws Exception {
		
		int result=0;
		
		try{
		
			String[] ipList =  exceptionVO.getIp().split("\n");
			
			for(String ip:ipList){
				ip = (ip.replace("\r", "")).replaceAll(" ", "");
				if(!ip.isEmpty() && ip !="" && ip !=null){
					exceptionVO.setIp(ip);
					
					//중복여부 체크 
					int cnt = exceptionDAO.exceptionIpCnt(exceptionVO);
					if(cnt == 0){
						exceptionDAO.exceptionIpSave(exceptionVO);
					}
					
						
				}
				
			}
			result=0;
			
		}catch(Exception e){
			e.printStackTrace();
			result=-1;
		}
		
		return result;
		
	}

	/**
	 *  정책 LIST
	 */
	public HashMap<String, Object> getPolIdxList() throws Exception {
		List<ExceptionVO> List = exceptionDAO.getPolIdxList();
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		return rMap;
	}

	/**
	 *  예외IP 정보
	 */
	public ExceptionVO getExceptionIpInfo(ExceptionVO exceptionVO) 
			throws Exception {
		
		List<ExceptionVO> List = exceptionDAO.getExceptionIpInfo(exceptionVO);
		ExceptionVO exceptionIpInfo = new ExceptionVO();
		
		String ip = "";
		
		for(ExceptionVO row:List){
			
			exceptionIpInfo.setSec_pol_id(row.getSec_pol_id());
			exceptionIpInfo.setException_desc(row.getException_desc());
			
			if(ip ==""){
				ip = row.getIp();
			}else{
				ip += "\n" + row.getIp();
			}
			
		}
		exceptionIpInfo.setIp(ip);
		
		return exceptionIpInfo;
	}

	/**
	 *  예외IP 정보
	 */
	public int exceptionIpDelete(ExceptionVO exceptionVO) throws Exception {
		int result=0;
		try{
			exceptionDAO.exceptionIpDelete(exceptionVO);
			result=0;
		}catch(Exception e){
			
			e.printStackTrace();
			result=-1;
		}
		return result;
	}
	
	
	/**
	 *  예외처리 EmpNo LIST
	 */
	public HashMap<String, Object> getExceptionEmpNoList() throws Exception {
		List<ExceptionVO> List = exceptionDAO.getExceptionEmpNoList();
		int TotalCount = exceptionDAO.getExceptionEmpNoListTotalCount();
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  예외처리 EmpNo 정보 저장
	 */
	public int exceptionEmpNoSave(ExceptionVO exceptionVO) throws Exception {
		
		int result=0;
		
		try{
		
			String[] empNoList =  exceptionVO.getEmp_no().split("\n");
			
			for(String empNo:empNoList){
				empNo = (empNo.replace("\r", "")).replaceAll(" ", "");;
				if(!empNo.isEmpty() && empNo !="" && empNo !=null){
					exceptionVO.setEmp_no(empNo);
					
					// 사번 유효 체크
					//int cnt = exceptionDAO.getOrgUserInfo(empNo);
					
					//if(cnt > 0){
					
					//중복여부 체크 
					int cnt = exceptionDAO.exceptionEmpNoCnt(exceptionVO);
					if(cnt == 0){
							exceptionDAO.exceptionEmpNoSave(exceptionVO);
					}
						
					//}
				}
				
			}
			result=0;
			
		}catch(Exception e){
			e.printStackTrace();
			result=-1;
		}
		
		return result;
		
	}

	/**
	 *  예외EmpNo 정보
	 */
	public ExceptionVO getExceptionEmpNoInfo(ExceptionVO exceptionVO) 
			throws Exception {
		
		List<ExceptionVO> List = exceptionDAO.getExceptionEmpNoInfo(exceptionVO);
		ExceptionVO exceptionEmpNoInfo = new ExceptionVO();
		
		String empNo = "";
		
		for(ExceptionVO row:List){
			
			exceptionEmpNoInfo.setSec_pol_id(row.getSec_pol_id());
			exceptionEmpNoInfo.setException_desc(row.getException_desc());
			
			if(empNo ==""){
				empNo = row.getEmp_no();
			}else{
				empNo += "\n" + row.getEmp_no();
			}
			
		}
		exceptionEmpNoInfo.setEmp_no(empNo);
		
		return exceptionEmpNoInfo;
	}

	/**
	 *  예외EmpNo 정보 삭제
	 */
	public int exceptionEmpNoDelete(ExceptionVO exceptionVO) throws Exception {
		int result=0;
		try{
			exceptionDAO.exceptionEmpNoDelete(exceptionVO);
			result=0;
		}catch(Exception e){
			
			e.printStackTrace();
			result=-1;
		}
		return result;
	}

	/**
	 *  메일 예외 EmpNo List
	 */
	public HashMap<String, Object> getExceptionMailList() throws Exception {
		List<ExceptionVO> List = exceptionDAO.getExceptionMailList();
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		return rMap;
	}

	/**
	 *  메일 예외 EmpNo 정보
	 */
	public ExceptionVO getExceptionMailInfo(ExceptionVO exceptionVO)
			throws Exception {
		List<ExceptionVO> List = exceptionDAO.getExceptionMailInfo(exceptionVO);
		ExceptionVO exceptionMailInfo = new ExceptionVO();
		
		String empNo = "";
		
		for(ExceptionVO row:List){
			
			exceptionMailInfo.setOp_indc(row.getOp_indc());
			
			if(empNo ==""){
				empNo = row.getEmp_no();
			}else{
				empNo += "\n" + row.getEmp_no();
			}
			
		}
		exceptionMailInfo.setEmp_no(empNo);
		
		return exceptionMailInfo;
	}

	/**
	 *  메일 예외 EmpNo 정보 삭제
	 */
	public int exceptionMailDelete(ExceptionVO exceptionVO) throws Exception {
		int result=0;
		try{
			exceptionDAO.exceptionMailDelete(exceptionVO);
			result=0;
		}catch(Exception e){
			
			e.printStackTrace();
			result=-1;
		}
		return result;
	}

	/**
	 *  메일 예외 EmpNo 정보 저장
	 */
	public int exceptionMailSave(ExceptionVO exceptionVO) throws Exception {
		int result=0;
		
		try{
		
			String[] empNoList =  exceptionVO.getEmp_no().split("\n");
			
			for(String empNo:empNoList){
				empNo = (empNo.replace("\r", "")).replaceAll(" ", "");;
				if(!empNo.isEmpty() && empNo !="" && empNo !=null){
					exceptionVO.setEmp_no(empNo);
					
					//중복여부 체크 
					int cnt = exceptionDAO.exceptionMailCnt(exceptionVO);
					if(cnt == 0){
							exceptionDAO.exceptionMailSave(exceptionVO);
					}
						
					//}
				}
				
			}
			result=0;
			
		}catch(Exception e){
			e.printStackTrace();
			result=-1;
		}
		
		return result;
	}
}