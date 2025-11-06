package sdiag.util;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Node;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import webdecorder.CipherBPMUtil;

public class BPMUtil {
	/**
	 * BPM업무시작 전송
	 * @param emp_no
	 * @param appr_line_code
	 * @param appr_id
	 * @param comService
	 * @return
	 * @throws Exception
	 */
	public static boolean BPMSend(String emp_no, String appr_line_code, String appr_id, CommonService comService) throws Exception{
		 String codeInfo = "";
		 BPMInfoVO bpmLog = new BPMInfoVO();
		 String procId = "";
		 String errCode = "";
		 String url = "";
		 String sdiagparam = "";
		 String Exmsg = "OK";
		 String apprid = appr_id;
		try{
			HashMap<String, String> pMap = new HashMap<String, String>();
			pMap.put("empno", emp_no);
			pMap.put("apprlinecode", appr_line_code);
			
			EgovMap upperInfo = comService.getApprLineUserInfo(pMap);
			
			if(upperInfo.get("upper1").equals("") && upperInfo.get("upper2").equals("")){
				throw new Exception("처리실패 -> 소명 결재정보(상급자정보) 없음");
			}
			
			sdiagparam=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<APPR_INFO>"
					+ "	<APPR_ID>%s</APPR_ID>"
					+ "	<EMP_NO>%s</EMP_NO>"
					+ "	<SIGN_TYPE>%s</SIGN_TYPE>"
					+ "	<SIGN1>%s</SIGN1>"
					+ "	<SIGN2>%s</SIGN2>"
					+ "</APPR_INFO>"
					, apprid
					, emp_no 
					, appr_line_code
					, upperInfo.get("upper1")
					, upperInfo.get("upper2"));
			
			codeInfo = CipherBPMUtil.Encrypt(sdiagparam);
			CodeInfoVO param = new CodeInfoVO();
			param.setMajr_code(MajrCodeInfo.BPMURL);
			param.setMinr_code("SR1"); //운영 SR1
			CodeInfoVO bpmInfo = comService.getCodeInfoForOne(param);
			url = String.format("%s?sdiagparam=%s", bpmInfo.getCode_desc(), codeInfo);
			
			//url = String.format("%s?sdiagparam=%s", "http://localhost:8080/sample/bpmresponsetest.do", codeInfo);
			RestTemplate resttmp = new RestTemplate();
			String result1 = resttmp.getForObject(url, String.class);
		
			HashMap<String, String> xmlInfo = BPMUtil.ConvertBPMPrarmToHsahMap(result1, false);
			errCode = xmlInfo.get("ERRORCODE");
			procId = xmlInfo.get("PROCID");

			HashMap<String, String> pMap1 = new HashMap<String, String>();
			pMap1.put("procid", procId);
			pMap1.put("appr_id", apprid);
			int uret = comService.setUpdateApprBpmKey(pMap1);
			Exmsg += String.format(", BPMID UPDATE OK : %s, ProcID : %s, appr_id : %s, RESULT INFO: %s" , uret, procId, apprid, result1 );
			
		}catch(Exception e){
			Exmsg = e.toString();
			return false;
		}finally{
			bpmLog.setAppr_id(apprid);
			bpmLog.setBpm_ret_code(errCode);
			bpmLog.setBpm_id(procId);
			bpmLog.setStatus_type("S");
			bpmLog.setAppr_commment("처리결과 : " + Exmsg);
			bpmLog.setEmp_no(emp_no);
			comService.InsertBPMLog(bpmLog);
		}
		
		if(errCode.equals("0")){
			return true;
		}else{
			return false;
		}
		
	}

	public static HashMap<String, String> ConvertBPMPrarmToHsahMap(String params) throws Exception {
		return ConvertBPMPrarmToHsahMap(params, true);
	}
	
	public static HashMap<String, String> ConvertBPMPrarmToHsahMap(String params, boolean isCrypt) throws Exception {
		HashMap<String, String> xmlInfo = null;
		try {
			String result_xml;
			result_xml = isCrypt ? CipherBPMUtil.Decrypt(params) : params.trim();
			//XML를 HashMap으로 Convert...
	    	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xml =null;
			InputSource is = new InputSource(new StringReader(result_xml));
			xml = documentBuilder.parse(is);
			Element element = xml.getDocumentElement();
			NodeList list = element.getChildNodes();
			xmlInfo = new HashMap<String, String>();
			for(int i = 0;i<list.getLength();i++){
				if(Node.TEXT_NODE != list.item(i).getNodeType()){
					xmlInfo.put(list.item(i).getNodeName(), list.item(i).getTextContent());
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new Exception("XML 변환 에러...]["+params);
		}		
    	
		return xmlInfo;
		
	}
}
