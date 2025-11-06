package sdiag.sample.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.aspectj.util.FileUtil;
import org.dom4j.Node;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.FileVO;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.SdiagProperties;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.sample.service.SampleService;
import sdiag.util.BPMUtil;
import sdiag.util.CommonUtil;
import sdiag.util.HTMLInputFilter;
import sdiag.util.MailUtil;
import sdiag.util.MajrCodeInfo;
import webdecorder.CipherBPMUtil;


@Controller
public class SampleController {

	@Resource(name="propertiesService1")
	protected EgovPropertyService propertyService;
	
	@Resource(name="sampleService")
	private SampleService sampleService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	@RequestMapping("/sample/samplePage.do")
	public String samplePage(HttpServletRequest request,
							HttpServletResponse response,
							ModelMap model){
		//String apprid = request.getParameter("apprid") == null ? "" : request.getParameter("apprid");
		//String bpmid = request.getParameter("procid") == null ? "" : request.getParameter("procid");
		//System.out.println(apprid + "]apprid[" + bpmid + "][");
		try{
			System.out.println(propertyService.getString("Globals.fileStorePath") + "][....");
			System.out.println(SdiagProperties.getProperty("Globals.SMPT_HOST"));
			System.out.println(CipherBPMUtil.Decrypt("SwzX9qXCQVeQjQxgkhM0yJT2i2wFYapUYCFPbMYXzcnTMtW9SSJXf+EgzZmk3Vih"));
		/*	String procId = "13801066";
			String appr_id="7f6307fc9aaa4ee8a77f36b80322de3d";
			HashMap<String, String> pMap1 = new HashMap<String, String>();
		
			pMap1.put("empno", "10029359");
			pMap1.put("apprlinecode", "01");
			
			EgovMap upperInfo = comService.getApprLineUserInfo(pMap1);
			
			
			pMap1.put("procid", procId);
			pMap1.put("appr_id", appr_id);
			int uret = comService.setUpdateApprBpmKey(pMap1);
			System.out.println(uret + "][");
			*/
		//	ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
	//		encoder.setEncodeHashAsBase64(true);
			
		//    String pwd = encoder.encodePassword("121212", null);
		//	System.out.println(pwd);
			//sampleService.test();
			/*
			
			String test_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<APPR_INFO>"
					+ "	<APPR_ID>소명신청ID</APPR_ID>"
					+ "	<EMP_NO>요청자 사번</EMP_NO>"
					+ "	<SIGN_TYPE>결재구분</SIGN_TYPE>"
					+ "	<SIGN1>직상급자 사번</SIGN1>"
					+ "	<SIGN2>차상급자 사번</SIGN2>"
					+ "</APPR_INFO>";
		
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xml =null;
			InputSource is = new InputSource(new StringReader(test_xml));
			xml = documentBuilder.parse(is);
			Element element = xml.getDocumentElement();
			NodeList list = element.getChildNodes();
			HashMap<String, String> xmlInfo = new HashMap<String, String>();
			for(int i = 0;i<list.getLength();i++){
				if(Node.TEXT_NODE != list.item(i).getNodeType()){
			//		System.out.println(String.format("%s - %s",list.item(i).getNodeName(), list.item(i).getTextContent()));	
					xmlInfo.put(list.item(i).getNodeName(), list.item(i).getTextContent());
				}
			}
			
			System.out.println(xmlInfo.get("APPR_ID") + "][ TEST");
			*/
			
			
			/*
			System.out.println(list.item(0).getNodeName() + ":0 NodeName");
			System.out.println(list.item(1).getNodeName() + ":1 NodeName");
			System.out.println(list.item(2).getNodeName() + ":2 NodeName");
			System.out.println(list.item(3).getNodeName() + ":3 NodeName");
			System.out.println(list.item(4).getNodeName() + ":4 NodeName");
		*/
	
			/**
			 * BPM TEST
			 */
			String codeInfo = "";
			try
			{
			/*
			String sdiagparam=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<APPR_INFO>"
					+ "	<APPR_ID>%s</APPR_ID>"
					+ "	<EMP_NO>%s</EMP_NO>"
					+ "	<SIGN_TYPE>%s</SIGN_TYPE>"
					+ "	<SIGN1>%s</SIGN1>"
					+ "	<SIGN2>%s</SIGN2>"
					+ "</APPR_INFO>"
					, "7a9d4037f52048b1a1100b899b5c424a"
					, "10087604"
					, "01"
					, "10138234"
					, "");
			codeInfo = CipherBPMUtil.Encrypt(sdiagparam);
			CodeInfoVO param = new CodeInfoVO();
			param.setMajr_code(MajrCodeInfo.BPMURL);
			param.setMinr_code(SdiagProperties.getProperty("Globals.BPMTYPE")); //운영 SVR
			CodeInfoVO bpmInfo = comService.getCodeInfoForOne(param);
			String url = String.format("%s?sdiagparam=%s", bpmInfo.getCode_desc(), codeInfo);
			model.addAttribute("bpmurl", url);
			System.out.println(url);
			
			RestTemplate resttmp = new RestTemplate();
			String result1 = resttmp.getForObject(url, String.class);
			System.out.println(result1 + ": RESULT CODE ");
			*/
		//http://bpdev.kt.com:8103/bizflow/KTBPM/adapter/adapter_calling.jsp?sdiagparam=BPaFJndz41jvbCHtfC3GKXnXJ+KtPlhp0CwnluBvmv6OSashtBePlQKGzFzyfM4E661Hl2ansAQHyc0Rz1oNHkC6ZF/2nqfSyKUFBGWCh4s2GzAsB0u+9D5e3X3RccNW4aJDjZJqdOuojC0r/kk7kwj8X8rK1DDj+Gb3j8v4j3ytXsza++G+Q1V+QHZ3v5InyZSSbjlbwithoXiGX68UHUqL0xgHPoOdAyjXqnR8/TP+DymoXjLk/GIRQXOsT/AY9p+expa9hIqTyxpRnJksBZMU+NMgpEqwPyJr44FwAc4=
	/*			String bpmurl = "http://bpdev.kt.com:8103/bizflow/KTBPM/adapter/adapter_calling.jsp?sdiagparam=BPaFJndz41jvbCHtfC3GKXnXJ+KtPlhp0CwnluBvmv6OSashtBePlQKGzFzyfM4E9o+C3kEAPzU0cwwQnObAoTblva8Bn9kyAqJ9BDCkXNrNGJTpSYO+UmL03ENsBgePOQH/HPD8QkZ6eT38uX2QfKuSH6p4qq3YOpTHf32sPQ1yja33pNOcxFgBR8IMDazpPUqjFrWHxxxQjzaXD6D4zccj3i1OSw3Y2DvV+AFbL5Sqdzcg5SKFCo1B9L5p1LED2tZQKnMIXST97o7N4jeNbe5XU4OxPB0j6S+SN47y9AM=";
				RestTemplate resttmp = new RestTemplate();
				String result1 = resttmp.getForObject(bpmurl, String.class);
				System.out.println(result1 + ": RESULT CODE ");	
		*/
				//<?xml version='1.0' encoding='utf-8'?><RESPONSE><APPR_ID>5ba2dd8df2734625be25d7d764879824</APPR_ID><PROCID>3604974</PROCID><ERRORCODE>0</ERRORCODE><ERRORMSG><![CDATA[프로세스 정상 생성.]]></ERRORMSG></RESPONSE>
			//	String result = "<?xml version='1.0' encoding='utf-8'?><RESPONSE><APPR_ID>5ba2dd8df2734625be25d7d764879824</APPR_ID><PROCID>3604974</PROCID><ERRORCODE>0</ERRORCODE><ERRORMSG><![CDATA[프로세스 정상 생성.]]></ERRORMSG></RESPONSE>";
			//	String ret = "nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXJVuBsOn4ZGsruoAeOcJlV86Zqg4+j9nmPr/ue3JLW+SEkRhtiXDpmRpTQaWNhLptwcWOcQknoAxJV6cqGpzp4tO3rhzsU7zcYtJhtmLocM+MPymGL/WeKnRvUVmTb31FglRV3mc3ZjiqUlq6/YXToeqmfLfQ4KnXk/XfpGMnW1wA==";
			//	HashMap<String, String> xmlInfo = BPMUtil.ConvertBPMPrarmToHsahMap(ret);
			//	System.out.println(xmlInfo.toString());
				//System.out.println(xmlInfo.get("ERRORCODE"));
				//System.out.println(xmlInfo.get("PROCID"));
				
				
				
		//BPM 처리 결과 업데이트
			
			//String bpmtest="http://10.215.47.210/bpm/apprbpmresult.do?bpmparam=nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA+1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ==";
			//String bpmtest="http://localhost:8080/bpm/apprbpmresult.do?bpmparam=nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA+1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ==";
			//nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA 1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ==
			//nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA+1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ==
			//nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA+1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ=="
		//		String url="http://localhost:8080/bpm/apprbpmresult.do";
				//String params = "nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5sBgPJe0csVIAW5OPHCoqNyQNfqlcivgFbj4iuWwm3Yxq49LVuaeOlYApVuS6DtM42QWss62vhK4K8Hu4yGk05I9V0pA+1FwYjeiBeSyMbDJBJL7MCWqqazxUDqxuqSelN25kgID/UrMxyVfIMXT5ZQ==";
	//			String params = "nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5Dwv+VTj2ZhDw7SM/9GIMKNEpbLukMwkakOXwplbrJ0ilcrvls5ZECjaH/bSWaaFIXhogSMiHOn/h+y2F4H+qTaLNALAOWpfhZGaYlfA6fhQ=";
		//		RestTemplate resttmp = new RestTemplate();
			//	String result1 = resttmp.postForObject(url, params, String.class);
			//String result1 = resttmp.getForObject(bpmtest, String.class);
			//System.out.println(result1 + ": RESULT CODE ");
				
				/*
			String bpmparam = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<REQUEST>"
					+ "<APPR_ID>7a9d4037f52048b1a1100b899b5c424a</APPR_ID>"
					+ "<PROCID>20151104111114</PROCID>"
					+ "<STATUS>F01</STATUS>"
					+ "</REQUEST>");
				
			System.out.println("bpmparam : " + CipherBPMUtil.Encrypt(bpmparam));			
			*/
			 /* <RESPONSE>
				<APPR_ID>소명신청ID</APPR_ID>
				<PROCID>bpm고유key<PROCID>
				<ERRORCODE>0</ERRORCODE>
				<ERRORMSG><![CDATA[프로세스 정상 생성.]]></ERRORMSG>
				</RESPONSE>
				ERRORCODE: 0 성공, -1: 실패
			 * */
			
			/*
			 * bpm 완료시 호출 파라미터 정보
			bpm 완료시 :  파라메터명은 bpmparam
			<?xml version="1.0" encoding="UTF-8"?>
			<REQUEST>
			 <APPR_ID>소명신청ID</APPR_ID>
			 <PROCID>bpm고유key<PROCID>
			 <STATUS>A02</STATUS> 
			</REQUEST>
			 
			상태 값정의
			A01 : 기안단계, A02 : 직상급자단계, A03: 차상급자단계, A04: 담당자단계, F01: 완료
			 

			 * */
			
			//model.addAttribute("returnbpm", result1);
			}catch(Exception e){
				e.printStackTrace();
			}
			//[UkXPStJKHBCqwqsENH+ZsQ==]:DBName/[KBr7qu1r9Aw6SsXbTj/Wwg==]:DBPwd/[SwzX9qXCQVeQjQxgkhM0yEXmA1BqaAM4swAhX0zu96ClOSVZGoOBMndjUR8qtHw1]:DBURl

			/*
			StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
	        
	        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
	        pbeEnc.setPassword("sdiag!"); // PBE 값(XML PASSWORD설정)
	        
	        String url = pbeEnc.encrypt("jdbc:postgresql://10.215.47.209:5432/diagdb");
	        String username = pbeEnc.encrypt("diag");
	        String password = pbeEnc.encrypt("tnwnswlseks!");
	        
	        System.out.println(url);
	        System.out.println(username);
	        System.out.println(password);
			*/
		  /*	String test = "aaa";
		  	String DBName= "anal";
		  	String DBPwd="tnwnswlseks!";
		  	String DBURL = "jdbc:postgresql://10.215.47.207:5432/analdb";
		  	System.out.println(String.format("[%s]:DBName/[%s]:DBPwd/[%s]:DBURl"
		  			,CipherBPMUtil.Encrypt(DBName)
		  			,CipherBPMUtil.Encrypt(DBPwd)
		  			,CipherBPMUtil.Encrypt(DBURL)));
		  			*/
		  	//TEST : [UkXPStJKHBCqwqsENH+ZsQ==]:DBName/[UkXPStJKHBCqwqsENH+ZsQ==]:DBPwd/[SwzX9qXCQVeQjQxgkhM0yJT2i2wFYapUYCFPbMYXzcnTMtW9SSJXf+EgzZmk3Vih]:DBURl

		  	//운영 : [UkXPStJKHBCqwqsENH+ZsQ==]:DBName/[KBr7qu1r9Aw6SsXbTj/Wwg==]:DBPwd/[SwzX9qXCQVeQjQxgkhM0yEXmA1BqaAM4swAhX0zu96ClOSVZGoOBMndjUR8qtHw1]:DBURl

			//test = CipherBPMUtil.Encrypt(test);		//암호화
			//System.out.println("test::"+test);
			//test = CipherBPMUtil.Decrypt("nJTDOaZsl1dREgnQf7aiCKQ09iztd8CEnZxbPH4vpXLlguvoV/7KphKUvpQjj6p5Dwv+VTj2ZhDw7SM/9GIMKNEpbLukMwkakOXwplbrJ0ilcrvls5ZECjaH/bSWaaFIXhogSMiHOn/h+y2F4H+qTaLNALAOWpfhZGaYlfA6fhQ=");		//복호화
			//System.out.println("test::"+test);
			
		  	//String APPR_ID= cols.item(0).getAttributes().item(0).getTextContent();
		  	//System.out.println(APPR_ID + "][APPRID");
		  	/*
		  	for(int idx=0;idx<cols.getLength();idx++){
		  		
		  	}
		  	*/
			//System.out.println(jsonxml);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "sample/samplePage";
	}
	
	@RequestMapping("/sample/mailsend.do")
	public String mailsend(HttpServletRequest request,
							HttpServletResponse response,
							@ModelAttribute("mailInfoVO") MailInfoVO mailInfoVO,
							ModelMap model) throws Exception{
		
		
		
		return "sample/mailsend";
	}
	
	@RequestMapping(value="/sample/sendmailprocess.do")
	public String sendmailprocess(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("mailInfoVO") MailInfoVO mailInfoVO,
			ModelMap model) throws Exception{	
		
		try
		{
			
			MailSendLogVO mailLog = new MailSendLogVO();
			//mailLog.setTo_email("91117655@ktfriend.com"); //mailInfoVO.getToMailAddress();
			//mailLog.setSubj_email(HTMLInputFilter.dehtmlSpecialChars(mailInfoVO.getSubject()));
			//mailLog.setBody_email(mailBody.toString());
			//mailLog.setEmp_no(mailInfoVO.getEmp_no());
			boolean mail_ret = false;
			
			MailInfoVO InfoVO = new MailInfoVO();
			InfoVO.setIdx_rgdt_date("20160216");
			InfoVO.setEmp_no("10029359");
			InfoVO.setIp("10.213.33.191");
			InfoVO.setMac("70-18-8B-44-BF-E3");
			InfoVO.setPol_idx_id("HAN05");
			InfoVO.setSancttype("1");
			if(!mailInfoVO.getToMailAddress().equals("")){
				mailLog = MailUtil.getMailMessage("A", InfoVO, "10029359", comService);
				mailLog.setTo_email(mailInfoVO.getToMailAddress());
				mail_ret = MailUtil.SendMail(mailLog, comService);
				
				mailLog = MailUtil.getMailMessage("S", InfoVO, "10029359", comService);
				mailLog.setTo_email(mailInfoVO.getToMailAddress());
				mail_ret = MailUtil.SendMail(mailLog, comService);
				
				mailLog = MailUtil.getMailMessage("P", InfoVO, "10029359", comService);
				mailLog.setTo_email(mailInfoVO.getToMailAddress());
				mail_ret = MailUtil.SendMail(mailLog, comService);
			}
			
			if(mail_ret){
				System.out.println("MAIL OK....");
			}else{
				System.out.println("MAIL FAILD....");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/sample/mailsend.do";
	}	
	
	@RequestMapping(value="/sample/bpmtestview.do")
	public String bpmtestview(HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws Exception{
		
			//response.addHeader("X-FRAME-OPTIONS", "ALLOWALL");
		
		return "sample/bpmtestview";
	}	
	
	@RequestMapping(value="/sample/bpmresponsetest.do")
	public void bpmresponsetest(HttpServletRequest request
								, HttpServletResponse response) throws Exception {
		String msg = "Error!!";
		
		msg = "<RESPONSE>";
		msg += "<APPR_ID>abcdefg00001</APPR_ID>";
		msg += "<PROCID>1234567</PROCID>";
		msg += "<ERRORCODE>0</ERRORCODE>";
		msg += "<ERRORMSG><![CDATA[프로세스 정상 생성.]]></ERRORMSG>";
		msg += "</RESPONSE>";
	
		response.setContentType("application/text");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(msg);
	   
		
		
	}

	
	@RequestMapping(value="/image/uploadsample.do")
	public String uploadsample(HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws Exception{
		
			//response.addHeader("X-FRAME-OPTIONS", "ALLOWALL");
		
		return "sample/ajaxfileupload";
	}	
	
	@RequestMapping("/image/upload.do")
	public void doUploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request
								, HttpServletResponse response)throws Exception {
		
		String filename = file.getOriginalFilename();
		System.out.println(filename);
		String sRootPath = "D:\\Project\\workspace\\sdiag\\WebContent";// uploadPathResource.getpath();
		String sSvrFilePath="EditorUploadImages";
		int index = filename.lastIndexOf(".");
		String fileExt = filename.substring(index + 1);	
		String sSvrFileName = CommonUtil.CreateUUID() + "." + fileExt;// FileUtil.getFileName(filename);
		
		InputStream inStream = null;
		OutputStream outStream = null;
		//Ajax 관련 변수 선언
		PrintWriter outWriter = null;
		
		try{
			//파일업로드 변수 설정
			inStream = file.getInputStream();
			System.out.println("path : " + sRootPath + "/" + sSvrFilePath + "/" );
			
			File newfile = new File(sRootPath + "/" + sSvrFilePath, sSvrFileName);
			if(!newfile.exists()){
				newfile.createNewFile();
			}
			outStream = new FileOutputStream(newfile);
			
			//파일업로드 진행
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = inStream.read(bytes)) != -1){
				outStream.write(bytes, 0, read);
			}
			
			//업로드 파일 정보
			Map<String, Object> mapResult = new HashMap<String, Object>();
			mapResult.put("file_name", filename);
			mapResult.put("file_svr_name", sSvrFileName);
			mapResult.put("file_svr_path", sSvrFilePath);
			mapResult.put("file_size", file.getSize());
			mapResult.put("content_type", file.getContentType());
			
			response.setContentType("application/text");
			response.setContentType("text/xml;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(mapResult));
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(inStream != null){
				inStream.close();
			}
			if(outStream != null){
				outStream.close();
			}
		}
		
		
		//
	   
	}
	
	
}
