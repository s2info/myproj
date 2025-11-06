package sdiag.com.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jxl.write.DateTime;

import org.apache.log4j.Logger;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

import sdiag.com.service.ApprInfoVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.FileVO;
import sdiag.util.BPMUtil;
import sdiag.util.CommonUtil;
import sdiag.util.StringUtil;
import sun.security.ssl.*;
import sun.security.ssl.Debug;
import webdecorder.CipherBPMUtil;

@Controller
public class BpmRestController {

	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "commonService")
	private CommonService comService;

	@RequestMapping(value = "/bpmrest/test", method = RequestMethod.GET)
	public String apprbpmstatusprocess(@PathVariable("apprid") String apprid)
			throws Exception {
		String ret = "FALSE";
		try {
			System.out.println(apprid + ": APPR ID");
			System.out.println("UPDATE.................");

			ret = "TRUE";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/*
	 * bpm 완료시 호출 파라미터 정보 bpm 완료시 : 파라메터명은 bpmparam <?xml version="1.0"
	 * encoding="UTF-8"?> <REQUEST> <APPR_ID>소명신청ID</APPR_ID>
	 * <PROCID>bpm고유key<PROCID> <STATUS>A02</STATUS> </REQUEST>
	 * 
	 * 상태 값정의 A01 : 기안단계, A02 : 직상급자단계, A03: 차상급자단계, A04: 담당자단계, F01: 완료
	 */
	/**
	 * BPM 결재시 응답 URl
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/apprbpmresult.do")
	public void apprbpmresult(HttpServletRequest request
			,HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		String msg = "";
		String params = "";
		String reqType = "0001";
		Date now = new Date();
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try{
			InputStream inputStream = request.getInputStream();
			if(inputStream != null){
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while((bytesRead = bufferedReader.read(charBuffer)) > 0){
					stringBuffer.append(charBuffer, 0, bytesRead);
					
				}
			}
			
			
			params = stringBuffer.toString();
			
			HashMap<String, Object> hMap = new HashMap<String, Object>();
			String bpm_key = "";
			String appr_id = "";
			String status = "";
			
			BPMInfoVO bpmLog = new BPMInfoVO();
			HashMap<String, String> xmlInfo = BPMUtil.ConvertBPMPrarmToHsahMap(params);
			appr_id = xmlInfo.get("APPR_ID");
			bpm_key = xmlInfo.get("PROCID");
			status = xmlInfo.get("STATUS");

			ApprInfoVO apprInfo = new ApprInfoVO();
			if(!appr_id.equals("")){
				//소명정보 조회
				apprInfo = comService.getApprInfo(appr_id);
				reqType=apprInfo.getReqtype();
			}
			
			bpmLog.setAppr_id(appr_id);
			bpmLog.setBpm_ret_code(status);
			bpmLog.setBpm_id(bpm_key);
			bpmLog.setStatus_type("R");
			bpmLog.setAppr_commment(params);
			bpmLog.setReqtype(apprInfo.getReqtype());
			comService.InsertBPMLog(bpmLog);
			if(appr_id.equals("") || bpm_key.equals("") || status.equals("")){
				String errorMsg = appr_id.equals("") ? "소명코드없음" : bpm_key.equals("") ? "BPM Key 없음" : status.equals("") ? "상태코드 없음" : "기타 상태 오류";
				
				BPMInfoVO bpmerrLog = new BPMInfoVO();
				bpmerrLog.setAppr_id(String.valueOf(now.getTime()));
				bpmerrLog.setBpm_ret_code("-99");
				bpmerrLog.setBpm_id(bpm_key);
				bpmerrLog.setStatus_type("R");
				bpmerrLog.setReqtype(apprInfo.getReqtype());
				bpmerrLog.setAppr_commment(errorMsg);
				comService.InsertBPMLog(bpmerrLog);

				
				msg = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<RESPONSE>" 
						+ "<ERRORCODE>%s</ERRORCODE>"
						+ "<ERRORMSG><![CDATA[%s]]></ERRORMSG>" 
						+ "</RESPONSE>"
						,"-1"
						, errorMsg);
			}else{
				
				// BPM로그 저장
				// 완료시 완료처리
				BPMInfoVO bpmProcLog = new BPMInfoVO();
				if (status.equals("F01")) {
					bpmProcLog.setAppr_id(appr_id);
					bpmProcLog.setBpm_ret_code("03");
					bpmProcLog.setAppr_proc("1010");
					/*******************************
					 * 소명완료시 user_idx_info appr_stat_code : 03으로 user_idx_info score
					 * -> 100으로 처리 idx_rgdt_date, emp_no, mac, pol_idx_id, ip,
					 * appr_id policyfact explan_flag -> 'Y' 처리 sldm_empno,
					 * sldm_mac, sldm_ip, policy_id, event_date user_idx_info_day
					 * scor_curr -> 다시계산함 idx_rgdt_date, emp_no, mac, ip
					 * 제재존재시 해제처리
					 * ****************************/
					if(apprInfo.getReqtype().equals("0001")){
						boolean ret = comService.setUpdateApprBPMCompleteInfo(bpmProcLog);
					}
					
				} else if (status.equals("A01")) {
					bpmProcLog.setAppr_id(appr_id);
					bpmProcLog.setBpm_ret_code("01");
					bpmProcLog.setAppr_proc("1001");
				} else {
					bpmProcLog.setAppr_id(appr_id);
					bpmProcLog.setBpm_ret_code("02");
					bpmProcLog.setAppr_proc("1005");
				}
				// 로그 저장
				if(apprInfo.getReqtype().equals("0002")){
					int ret = comService.setPovUpdateApprStatusForifappr(bpmProcLog);
				}else{
					int ret = comService.setUpdateApprStatusForifappr(bpmProcLog);
				}
				
				msg = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<RESPONSE>" 
						+ "<ERRORCODE>%s</ERRORCODE>"
						+ "<ERRORMSG><![CDATA[처리완료.]]></ERRORMSG>" 
						+ "</RESPONSE>"
						,"0");
			}
		}catch(Exception e){
			//new Exception("InputStream 처리 에러 - " + e.getMessage());
			BPMInfoVO bpmerrLog = new BPMInfoVO();
			bpmerrLog.setAppr_id(String.valueOf(now.getTime()));
			bpmerrLog.setBpm_ret_code("-99");
			bpmerrLog.setBpm_id("");
			bpmerrLog.setStatus_type("R");
			bpmerrLog.setReqtype(reqType);
			bpmerrLog.setAppr_commment(e.getMessage() + ">>" + stringBuffer.toString());
			comService.InsertBPMLog(bpmerrLog);
			e.printStackTrace();
			String errorMsg = e.getMessage();
			msg = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<RESPONSE>" 
					+ "<ERRORCODE>%s</ERRORCODE>"
					+ "<ERRORMSG><![CDATA[%s]]></ERRORMSG>" 
					+ "</RESPONSE>"
					,"-1"
					, errorMsg);
		}finally{
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch(Exception e){
					new Exception(e.getMessage());
				}
			}
		}
		
		response.setContentType("application/text");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(msg);

	}
}
