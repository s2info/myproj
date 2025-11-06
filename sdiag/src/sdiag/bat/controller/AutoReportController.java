package sdiag.bat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sdiag.bat.service.AutoSanctService;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.member.service.MemberVO;
import sdiag.member.service.SdiagMemberService;
import sdiag.util.CommonUtil;
import sdiag.util.MailUtil;
import sdiag.util.MajrCodeInfo;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AutoReportController extends QuartzJobBean{
	private AutoSanctService autoSanctService;
	private CommonService commonService;
	
	public void setAutoSanctService(AutoSanctService autoSanctService){
		this.autoSanctService = autoSanctService;
	}
	
	public void setCommonService(CommonService commonService){
		this.commonService = commonService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		HashMap<String, String> logMap = new  HashMap<String, String>();
		try{
			
	    	CodeInfoVO codeInfo = new CodeInfoVO();
	    	codeInfo.setMajr_code(MajrCodeInfo.ExecuteCode); //EXC
	    	codeInfo.setMinr_code("AGR");
	    	CodeInfoVO agrInfo = autoSanctService.getCodeInfoForOne(codeInfo);
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO malInfo = autoSanctService.getCodeInfoForOne(codeInfo);
	    	
	    	codeInfo.setMajr_code(MajrCodeInfo.UsedCode);
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO ismalInfo = autoSanctService.getCodeInfoForOne(codeInfo);
	    	codeInfo.setMinr_code("AML");
	    	CodeInfoVO ismailTargetInfo = autoSanctService.getCodeInfoForOne(codeInfo);
	    	
	    	boolean isAGR = agrInfo.getCode_desc().equals("Y") ? true : false;
	    	boolean isMail = malInfo.getCode_desc().equals("Y") ? true : false;	
	    	boolean isMailCode = ismalInfo.getCode_desc().equals("Y") ? true : false;
	    	/**
	    	 * Y : 상무이상 전송, N:TEST 계정 전송 
	    	 */
	    	boolean isTargetCode = ismailTargetInfo.getCode_desc().equals("Y") ? true : false; 
	    	int sendCount = 0;
	    	int failCount = 0;
	    	if(isAGR){
	    		//상무이상(대무자상무이상) 조회
	    		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
	    		
	    		if(isTargetCode){
	    			resultList = autoSanctService.getAutoBatMailSendList();
	    		}else{
	    			resultList = autoSanctService.getAutoBatMailSendListTest();
	    		}
	    		
	    		for(HashMap<String, Object> param:resultList){
	    			HashMap<String, String> result = new HashMap<String, String>();
	    			
	    			boolean mail_ret = false;
	    			if(isMail){
    					MailSendLogVO mailLog = MailUtil.getMailMessage(param.get("emp_no").toString(), param.get("emp_nm").toString(), param.get("email").toString(), param.get("call_addr").toString());
    					mail_ret = isMailCode ? MailUtil.SendMail(mailLog, commonService) : true;
	        			result.put("MAIL", mail_ret ? "전송완료" : "전송실패");
	        			if(mail_ret){
	        				sendCount++; 
	        			}else{ 
	        				failCount++;
	        			}
	    			}else{
	    				result.put("MAIL", "전송차단");
		    		}
	    		
	    		}
	    	}
	    	logMap.put("emp_no", "Agent");
	    	logMap.put("comment", isAGR ? String.format("자동메일발송 실행 -> 메일발송수 : %s, 메일발송실패수: %s", sendCount, failCount) : "자동메일발송 시스템 차단");
		}catch(Exception e){
			logMap.put("emp_no", "Agent");
			logMap.put("comment", e.getMessage());
			e.printStackTrace();	
		}finally{
			try {
				logMap.put("log_type", "M");
				autoSanctService.InsertAutobatLog(logMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
