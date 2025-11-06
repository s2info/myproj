package sdiag.bat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sdiag.bat.service.AutoSanctService;
import sdiag.bat.service.ExplainDataVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.sanct.service.SanctionService;
import sdiag.util.BPMUtil;
import sdiag.util.CommonUtil;
import sdiag.util.MailUtil;
import sdiag.util.MajrCodeInfo;
import sdiag.util.Monitor;

public class AutoExplainController extends QuartzJobBean{
	private AutoSanctService autoSanctService;
	private CommonService commonService;
	private SanctionService sanctionService;
	public void setAutoSanctService(AutoSanctService autoSanctService){
		this.autoSanctService = autoSanctService;
	}
	public void setSanctionService(SanctionService sanctionService) {
		this.sanctionService = sanctionService;
	}
	public void setCommonService(CommonService commonService){
		this.commonService = commonService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		boolean isExec =false;
		String isAutoAppr = "N";
		String checkPort = "19951";
		try {
			CodeInfoVO codeInfoParam = new CodeInfoVO();
			codeInfoParam.setMajr_code("RPT");
			codeInfoParam.setMinr_code("PRT");
			CodeInfoVO portInfo = commonService.getCodeInfoForOne(codeInfoParam);
			checkPort = portInfo.getAdd_info1();
			codeInfoParam.setMinr_code("EXP");
			CodeInfoVO batchExecInfo = commonService.getCodeInfoForOne(codeInfoParam);
			isAutoAppr = batchExecInfo.getAdd_info1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//배치 중복 실행 체크
		isExec = Monitor.monitoring(Integer.parseInt(checkPort));
		if(!isExec){
			System.out.println("이전배치 미완료로 인하여 배치처리 예외..");
		}else{
			System.out.println("배치 처리 시작..");
			if(isAutoAppr.equals("Y")){
				try {
					setExplainProcess();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Monitor.close();
		}
		
	}
	
	private void setExplainProcess() throws Exception{
		boolean isBPM = false;
		boolean isMail = false;
		boolean isBPMCode = false;
		boolean isMailCode = false;
		boolean isTest = true;
		String test_emp_no = "";
		String test_mail = "";
		int testCnt = 0;
		try {
			//BPM 실행여부
	    	CodeInfoVO codeInfo = new CodeInfoVO();
	    	codeInfo.setMajr_code(MajrCodeInfo.ExecuteCode);
	    	//BPM 실행여부
	    	codeInfo.setMinr_code("BPM");
	    	CodeInfoVO bpmInfo = commonService.getCodeInfoForOne(codeInfo);
	    	isBPM = bpmInfo.getCode_desc().equals("Y") ? true : false;
	    	
	    	//메일 실행 여부
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO malInfo = commonService.getCodeInfoForOne(codeInfo);
	    	isMail = malInfo.getCode_desc().equals("Y") ? true : false;
	    	
	    	//BPM 실행우회 여부
	    	codeInfo.setMajr_code(MajrCodeInfo.UsedCode);
	    	codeInfo.setMinr_code("BPM");
	    	CodeInfoVO isbpmInfo = commonService.getCodeInfoForOne(codeInfo);
	    	isBPMCode = isbpmInfo.getCode_desc().equals("Y") ? true : false;
	    	//메일 실행 우회 여부
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO ismalInfo = commonService.getCodeInfoForOne(codeInfo);
	    	isMailCode = ismalInfo.getCode_desc().equals("Y") ? true : false;
	    	
	    	/*TEST 관련 정보*/
	    	codeInfo.setMajr_code("RPT");
	    	//TEST 여부
	    	codeInfo.setMinr_code("EXP");
	    	CodeInfoVO testInfo = commonService.getCodeInfoForOne(codeInfo);
	    	isTest = !testInfo.getAdd_info1().equals("Y") ? false : true;
	    	
	    	if(isTest){
	    		//test 계정 - BPM및 메일 테스트 계정으로 전송됨
	    		codeInfo.setMinr_code("EUR");
		    	CodeInfoVO testempInfo = commonService.getCodeInfoForOne(codeInfo);
		    	String[] tUser = testempInfo.getAdd_info1().toString().split("\\|");
		    	
		    	test_emp_no = tUser[0];
		    	test_mail = tUser[1];
		    	//test 모드 일대 처리 건수 
		    	codeInfo.setMinr_code("CNT");
		    	CodeInfoVO testCntInfo = commonService.getCodeInfoForOne(codeInfo);
		    	testCnt = Integer.parseInt(testCntInfo.getAdd_info1().toString());
	    	}
	    	
			int totalCnt = 0;
			
			List<ExplainDataVO> apprList = autoSanctService.getExplainDataList();
			totalCnt = apprList.size();
			System.out.println(totalCnt + ":전체 처리 건수");
			int successCnt = 0;
			for(ExplainDataVO apprInfo:apprList){
				if(!apprInfo.getAppr_id().equals("")){
					continue;
				}
				
				if(isTest){
					if(successCnt >= testCnt){
						break;
					}
					
				}
				
				apprInfo.setAppr_id(CommonUtil.CreateUUID());

				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("apprid", apprInfo.getAppr_id());
				param.put("empno", apprInfo.getEmp_no());
				param.put("apprlinecode", apprInfo.getAppr_line_code());
				param.put("rgdtdate", apprInfo.getIdx_rgdt_date());
				param.put("mac", apprInfo.getMac());
				param.put("ip", apprInfo.getIp());
				param.put("polidxid", apprInfo.getPol_idx_id());
				param.put("mode", "A" ); // A:소명처리 S:제재

				//BPM처리
				HashMap<String, String> result = new HashMap<String, String>();
				boolean bpm_ret = false;
				boolean appr_ret = false;
				boolean mail_ret = false;
				boolean appr_inst = false;
				if (isBPM) {
					//소명정보 INSERT
					appr_inst = sanctionService.setApprInfoInsert(param);
					//소명정보 처리
					appr_ret = sanctionService.setApprInfoRegister(param);
					if (appr_ret && appr_inst) {
						String bpm_Emp_no = apprInfo.getEmp_no();
						if(isTest){
							bpm_Emp_no = test_emp_no;
						}
						
						bpm_ret = isBPMCode ? BPMUtil.BPMSend(bpm_Emp_no, apprInfo.getAppr_line_code(), apprInfo.getAppr_id(), commonService) : true;
						if (bpm_ret) {
							result.put("RESULT", "처리완료");
						} else {
							/**
							 * BPM 처리 실패시 소명정보 삭제
							 */
							boolean rollback = sanctionService.setApprInfoRollback(param);
							result.put("RESULT", "BPM실패");
						}
					} else {
						result.put("RESULT", "소명실패");
					}
				} else {
					result.put("RESULT", "BPM차단");
				}
				/**
				 * 메일전송 메일은 BPM 전송 완료 + 소명처리 완료시 전송함
				 */
				if (isMail) {
					if (bpm_ret && appr_ret) {
						MailInfoVO InfoVO = new MailInfoVO();
						InfoVO.setIdx_rgdt_date(apprInfo.getIdx_rgdt_date());
						InfoVO.setEmp_no(apprInfo.getEmp_no());
						InfoVO.setIp(apprInfo.getIp());
						InfoVO.setMac(apprInfo.getMac());
						InfoVO.setPol_idx_id(apprInfo.getPol_idx_id());
						InfoVO.setSancttype("");

						MailSendLogVO mailLog = MailUtil.getMailMessage("A", InfoVO, apprInfo.getEmp_no(),commonService);
						String bpm_Emp_no = apprInfo.getEmp_no();
						if(isTest){
							mailLog.setTo_email(test_mail);
						}
						mail_ret = isMailCode ? MailUtil.SendMail(mailLog, commonService) : true;
						result.put("MAIL", mail_ret ? "전송완료": "전송실패");
					} else {
						result.put("MAIL", "처리실패");
					}
				} else {
					result.put("MAIL", "전송차단");
				}
				result.put("RESULT", String.valueOf(bpm_ret && appr_ret && mail_ret));
				if(bpm_ret && appr_ret){
					successCnt++;
				}
				
				/**
				 * BPM 및 소명 로그저장
				 */
				if (isBPM) {
					BPMInfoVO bpmLog = new BPMInfoVO();
					bpmLog.setAppr_id(apprInfo.getAppr_id());
					bpmLog.setBpm_ret_code("-999");
					bpmLog.setBpm_id("");
					bpmLog.setStatus_type("L");
					bpmLog.setAppr_commment(String
							.format("BPM 및 소명 처리 : 결재구분 : %s, 진단날짜 : %s, MAC : %s, IP : %s, POL_IDX_ID : %s, BPM결과 : %s, 소명처리결과 : %s, 메일처리결과 : %s",
									apprInfo.getAppr_line_code(),
									apprInfo.getIdx_rgdt_date(),
									apprInfo.getMac(),
									apprInfo.getIp(),
									apprInfo.getPol_idx_id(),
									result.get("BPM"),
									result.get("RESULT"),
									result.get("MAIL")));
					bpmLog.setEmp_no(apprInfo.getEmp_no());
					autoSanctService.InsertBPMLog(bpmLog);
				}
				
			}//for
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
