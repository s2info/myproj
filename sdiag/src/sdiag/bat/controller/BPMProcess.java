package sdiag.bat.controller;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Node;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.bat.service.AutoSanctService;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.util.MajrCodeInfo;
import webdecorder.CipherBPMUtil;

public class BPMProcess {
	
	/**
	 * BPM업무시작 전송
	 * @param emp_no
	 * @param appr_line_code
	 * @param appr_id
	 * @param comService
	 * @return
	 * @throws Exception
	 */
	public static boolean BPMSend(String emp_no, String appr_line_code, String appr_id, AutoSanctService autoSanctService) throws Exception{
		 String codeInfo = "";
		 BPMInfoVO bpmLog = new BPMInfoVO();
		 String procId = "";
		 String errCode = "";
		 String url = "";
		 String sdiagparam = "";
		 String Exmsg = "";
		try{
						
			HashMap<String, String> pMap = new HashMap<String, String>();
			pMap.put("empno", emp_no);
			pMap.put("apprlinecode", appr_line_code);
			
			//EgovMap upperInfo = comService.getApprLineUserInfo(pMap);
			EgovMap upperInfo = autoSanctService.getApprLineUserInfo(pMap);
			
			sdiagparam=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<APPR_INFO>"
					+ "	<APPR_ID>%s</APPR_ID>"
					+ "	<EMP_NO>%s</EMP_NO>"
					+ "	<SIGN_TYPE>%s</SIGN_TYPE>"
					+ "	<SIGN1>%s</SIGN1>"
					+ "	<SIGN2>%s</SIGN2>"
					+ "</APPR_INFO>"
					, appr_id
					, emp_no 
					, appr_line_code
					, upperInfo.get("upper1")
					, upperInfo.get("upper2"));
			
			codeInfo = CipherBPMUtil.Encrypt(sdiagparam);
			
			CodeInfoVO param = new CodeInfoVO();
			param.setMajr_code(MajrCodeInfo.BPMURL);
			param.setMinr_code("SR1"); //운영 SR1 개발 TS1
			CodeInfoVO bpmInfo = autoSanctService.getCodeInfoForOne(param);
			url = String.format("%s?sdiagparam=%s", bpmInfo.getCode_desc(), codeInfo);
			//System.out.println("URL:" + url);			
			
			RestTemplate resttmp = new RestTemplate();
			String result1 = resttmp.getForObject(url, String.class);
			System.out.println("RESULT : " + result1); //
			HashMap<String, String> xmlInfo = BPMProcess.ConvertBPMPrarmToHsahMap(result1, false);
			errCode = xmlInfo.get("ERRORCODE");
			procId = xmlInfo.get("PROCID");
			
			pMap.put("procid", procId);
			pMap.put("appr_id", appr_id);
			autoSanctService.setUpdateApprBpmKey(pMap);
			
			if(errCode.equals("0")){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			Exmsg = e.toString();
			return false;
		}finally{
			bpmLog.setAppr_id(appr_id);
			bpmLog.setBpm_ret_code(errCode);
			bpmLog.setBpm_id(procId);
			bpmLog.setStatus_type("S");
			bpmLog.setAppr_commment("처리결과 : " + Exmsg);
			autoSanctService.InsertBPMLog(bpmLog);

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
