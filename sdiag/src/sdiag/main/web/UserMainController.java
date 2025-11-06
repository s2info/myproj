package sdiag.main.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.board.service.NoticeService;
import sdiag.board.service.NoticeVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.LoginUserTypeVO;
import sdiag.dash.service.GaugeItemValue;
import sdiag.getdata.service.GetDataService;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserMainService;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.SecPolService;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.PolicyService;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.pol.web.PolicyController;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.util.CommonUtil;
import sdiag.util.HTMLInputFilter;

@Controller
public class UserMainController {
	@Resource(name = "UserMainService")
	protected UserMainService userMainService;
	
	@Resource(name = "commonService")
	protected CommonService comService;
	
	@Resource(name = "PolicyService")
	private PolicyService polService;
	
	@Resource(name = "GetDataService")
	private GetDataService getDataService;
	
	@Resource(name = "SecPolService")
	private SecPolService secPolService;

	@Resource(name="propertiesService1")
	protected EgovPropertyService propertiesService1;
	
	@Resource(name = "NoticeService")
	private NoticeService noticeService;
	
	
	
	/**
	 * 메인화면 class
	 * @throws Exception 
	 */
	@RequestMapping("/main/userMain.do")
	private String userMain(HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws Exception{
		
		/**
		 * 로그인 사용자 정보  
		 * 
		 */
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		
		SearchVO searchVO = new SearchVO();
		
		//공지사항 최근 3건 List 조회
		searchVO.setSq_no(3);
		HashMap<String,Object> retMap = userMainService.getNoticeList(searchVO);
		
		List<NoticeVO> noticeList = (List<NoticeVO>)retMap.get("list");
		
		model.addAttribute("noticeList", noticeList);
		
		
		//faq 최근 3건 List 조회
		HashMap<String,Object> retMap2 = userMainService.getFaqList();
		List<NoticeVO> faqList = (List<NoticeVO>)retMap2.get("list");
		model.addAttribute("faqList", faqList);
		
		
		//공지사항 팝업 List 조회
		searchVO.setSq_no(0);
		searchVO.setSearchKeyword("Y");
		HashMap<String,Object> retMap3 = userMainService.getNoticeList(searchVO);
		List<NoticeVO> noticePopupList = (List<NoticeVO>)retMap3.get("list");
		
		model.addAttribute("noticePopupList", noticePopupList);
		
		
		List<CodeInfoVO> scoreStatus = comService.getCodeInfoListNoTitle("C04");
		GaugeItemValue gauagValue = new GaugeItemValue();
		gauagValue.setGood("100");
		for(CodeInfoVO row:scoreStatus){
			if(row.getMinr_code().equals("WAN")){
				gauagValue.setDanger(row.getCode_desc());
			}else if(row.getMinr_code().equals("GOD")){
				gauagValue.setWarning(row.getCode_desc());
			}
		}
		model.addAttribute("gauagValue", gauagValue);
		
		//테스트 
		//loginInfo.setUserid("10086969");
		
				
		// 전사평균
		UserMainIdxInfoVO totalAvg = userMainService.getTotalAvg();
		model.addAttribute("totalAvg", totalAvg);
		
		//사용자 , 사용자 팀, 사용자 상위 팀 보안점수 조회
		UserMainIdxInfoVO userIdxVo = userMainService.getUserIdxInfo(loginInfo.getUserid());
		
		model.addAttribute("userIdxVo", userIdxVo);
		
		
		
	 	//사용자 정책별 점수 
		HashMap<String,Object> resultMap = userMainService.getUserPolIdxInfoList(loginInfo.getUserid());
		List<UserPolIdxInfoVO> userPolIdxInfoList = (List<UserPolIdxInfoVO>)resultMap.get("list");
		model.addAttribute("userPolIdxInfoList", userPolIdxInfoList);
		
		model.addAttribute("userNm", loginInfo.getUsername());
		
		
		// 사용자 typeList 추가  2017.04.27
		List<LoginUserTypeVO> typeList = comService.getLoginUserTypeInfo(loginInfo.getUserid(), loginInfo.getRole_code());
		
		model.addAttribute("typeList", typeList);
		
		
		return "main/userMain";
		
	}
	
	
	/**
	 * 팝업
	 * @param request
	 * @param sanctno
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/main/userPolIdxInfoPopup.do")
	public String sanctconfigpopup(HttpServletRequest request, String sanctno, HttpServletResponse response,UserPolIdxInfoVO userPolIdxInfo,ModelMap model ) throws Exception {
		Gson gson = new Gson();
		
		
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map = (HashMap<String,Object>) gson.fromJson(param, map.getClass());
		userPolIdxInfo.setSec_pol_id(map.get("sec_pol_id").toString());
	    
	    /**
		 * 로그인 사용자 정보  
		 * 
		 */
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
	    
	    userPolIdxInfo.setEmp_no(loginInfo.getUserid());
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
		
		/** 해당 정책 상세정보 조회 **/
		UserPolIdxInfoVO userPolIdxInfoVO = userMainService.getuserPolIdxInfo(userPolIdxInfo);
		
		model.addAttribute("userPolIdxInfoVO",userPolIdxInfoVO);
		
		
		/** 상세로그 조회 START**/
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat dtforamt = new SimpleDateFormat("YYYYMMdd");
		
    	String polcode = userPolIdxInfo.getSec_pol_id();
		String empno = loginInfo.getUserid();
		String begindate = dtforamt.format(cal.getTime());
		String mac = "";
		
		HashMap<String, String> hMap2 = new HashMap<String, String>();
		hMap2.put("polcode", polcode);
		hMap2.put("empno", empno);
		hMap2.put("begindate", begindate);
		hMap2.put("mac", mac);
		int logCnt = 0;
		
		EgovMap tblInfo = polService.getPolDetailLogTableNColumns(hMap2);
		
		hMap2.put("tblname", String.format("\"public\".\"%s\"", tblInfo.get("tblname").toString()));
		hMap2.put("columnsname", tblInfo.get("columnsname").toString());
		
		String[] flag = tblInfo.get("columnsname").toString().split(",");
		List <String> list = new ArrayList<String>();
		Collections.addAll(list,flag);
		String str = "";
		
		String dFlag ="";
		
		for (int i=0; i<list.size(); i++ ){
			if(list.get(i).equals("d_flag")){
				dFlag = "AND d_flag != 'Y'";
				list.remove(i);
			}
		}
		
		for(int i=0; i<list.size(); i++){
			if(i > 0){
				str = str +"," + list.get(i);
			}else {
				str = list.get(i);
			}
		}
		//System.out.println(str);
		
		if(str !=""){
			hMap2.put("columnsname", str);
		}
		hMap2.put("dFlag", dFlag);
		
		/** 상세로그 조회 **/
		List<LinkedHashMap<String, Object>> Logs = getDataService.getPolDetailLogForDateNUser(hMap2);
		
		model.addAttribute("Logs",Logs);
		
		
		HashMap<String, String> colunm = new HashMap<String, String>();
		List <HashMap<String, String>> colunmList = new ArrayList();
		
		HashMap<String, String> value = new HashMap<String, String>();
		List <HashMap<String, String>> valueList = new ArrayList();
		String strValue= "";
		
		if(Logs.size() >0){
			
			List<EgovMap> columnsInfo = secPolService.getPolSourceLogTableColumns(tblInfo.get("tblname").toString());
			HashMap<?,?> log = (HashMap<?,?>)Logs.get(0);
			for(Object key:log.keySet())
			{
				colunm.put("column",  ConvertColumnsName(columnsInfo, key.toString()));
				colunmList.add(colunm);
				colunm = new HashMap<String, String>();
				
			}
			
			for(HashMap<?,?> row:Logs)
			{
				
				for(Object key:row.keySet()){
					strValue += row.get(key).toString().isEmpty() || row.get(key).toString() == null || row.get(key).toString() =="" ? " ," : row.get(key).toString().replace(",", "_")+",";
				}
				value.put("value", strValue);
				valueList.add(value);
				value = new HashMap<String, String>();
				strValue= "";
			}
		}
		
		model.addAttribute("colunmList",colunmList);
		model.addAttribute("valueList",valueList);
		
		return "main/userPolIdxInfoPopup";
	}
	
	/**
	 * 팝업
	 * @param request
	 * @param sanctno
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/main/userPolIdxInfoPopupEx.do")
	public String userPolIdxInfoPopupEx(HttpServletRequest request, String sanctno, HttpServletResponse response,UserPolIdxInfoVO userPolIdxInfo,ModelMap model ) throws Exception {
		Gson gson = new Gson();
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map = (HashMap<String,Object>) gson.fromJson(param, map.getClass());
		userPolIdxInfo.setSec_pol_id(map.get("sec_pol_id").toString());
		String empno = map.get("emp_no").toString();
		String begindate =  map.get("searchDate").toString();
	    /**
		 * 로그인 사용자 정보  
		 * 
		 */
		
		
	    if(empno == null || empno.equals("")){
	    	userPolIdxInfo.setEmp_no(loginInfo.getUserid());
	    	empno = loginInfo.getUserid();
	    }else{
	    	userPolIdxInfo.setEmp_no(empno);
	    }
	    
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
		
		/** 해당 정책 상세정보 조회 **/
		UserPolIdxInfoVO userPolIdxInfoVO = userMainService.getuserPolIdxInfo(userPolIdxInfo);
		
		model.addAttribute("userPolIdxInfoVO",userPolIdxInfoVO);
		
		
		/** 상세로그 조회 START**/
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat dtforamt = new SimpleDateFormat("YYYYMMdd");
		
    	String polcode = userPolIdxInfo.getSec_pol_id();
		if(begindate == null || begindate.equals("")){
			begindate = dtforamt.format(cal.getTime());
		}
		String mac = "";
		
		HashMap<String, String> hMap2 = new HashMap<String, String>();
		hMap2.put("polcode", polcode);
		hMap2.put("empno", empno);
		hMap2.put("begindate", begindate);
		hMap2.put("mac", mac);
		int logCnt = 0;
		
		EgovMap tblInfo = polService.getPolDetailLogTableNColumns(hMap2);
		
		hMap2.put("tblname", String.format("\"public\".\"%s\"", tblInfo.get("tblname").toString()));
		hMap2.put("columnsname", tblInfo.get("columnsname").toString());
		
		String[] flag = tblInfo.get("columnsname").toString().split(",");
		List <String> list = new ArrayList<String>();
		Collections.addAll(list,flag);
		String str = "";
		
		String dFlag ="";
		
		for (int i=0; i<list.size(); i++ ){
			if(list.get(i).equals("d_flag")){
				dFlag = "AND d_flag != 'Y'";
				list.remove(i);
			}
		}
		
		for(int i=0; i<list.size(); i++){
			if(i > 0){
				str = str +"," + list.get(i);
			}else {
				str = list.get(i);
			}
		}
		//System.out.println(str);
		
		if(str !=""){
			hMap2.put("columnsname", str);
		}
		hMap2.put("dFlag", dFlag);
		
		/** 상세로그 조회 **/
		List<LinkedHashMap<String, Object>> Logs = getDataService.getPolDetailLogForDateNUser(hMap2);
		
		model.addAttribute("Logs",Logs);
		
		
		HashMap<String, String> colunm = new HashMap<String, String>();
		List <HashMap<String, String>> colunmList = new ArrayList();
		
		HashMap<String, String> value = new HashMap<String, String>();
		List <HashMap<String, String>> valueList = new ArrayList();
		String strValue= "";
		
		if(Logs.size() >0){
			
			List<EgovMap> columnsInfo = secPolService.getPolSourceLogTableColumns(tblInfo.get("tblname").toString());
			HashMap<?,?> log = (HashMap<?,?>)Logs.get(0);
			for(Object key:log.keySet())
			{
				colunm.put("column",  ConvertColumnsName(columnsInfo, key.toString()));
				colunmList.add(colunm);
				colunm = new HashMap<String, String>();
				
			}
			
			for(HashMap<?,?> row:Logs)
			{
				
				for(Object key:row.keySet()){
					try{
						strValue += row.get(key).toString().isEmpty() || row.get(key).toString() == null || row.get(key).toString() =="" ? " ," : row.get(key).toString().replace(",", "_")+",";
					}catch(NullPointerException e){
						strValue += " ";
					}
						
				}
				value.put("value", strValue);
				valueList.add(value);
				value = new HashMap<String, String>();
				strValue= "";
			}
		}
		
		model.addAttribute("colunmList",colunmList);
		model.addAttribute("valueList",valueList);
		List<SdResultInfoVO> sdResultInfo = new ArrayList<SdResultInfoVO>();
		
		if(userPolIdxInfo.getSec_pol_id().equals("SD01")){
			sdResultInfo = userMainService.getSdResultInfo(empno);
		}
		
		model.addAttribute("sdResultInfo",sdResultInfo);
		
		return "main/userPolIdxInfoPopup";
	}
	
	private String ConvertColumnsName(List<EgovMap> columnInfo, String key){
		String columnName = key;
		try
		{
			if(key.equals("sldm_empno")){
				return "사번";
			}else if(key.equals("sldm_ip")){
				return "아이피";
			}else if(key.equals("emp_nm")){
				return "성명";
			}else if(key.equals("org_nm")){
				return "조직명";
			}else if(key.equals("sldm_mac")){
				return "MAC";
			}else if(key.equals("empno")){
				return "사번";
			}else if(key.equals("path")){
				return "파일 경로";
			}else if(key.equals("username")){
				return "성명";
			}else if(key.equals("department")){
				return "부서";
			}
			
			for(EgovMap row:columnInfo){
				if(row.get("columnname").equals(key)){
					columnName = row.get("columndesc").toString().trim().equals("") ? key : row.get("columndesc").toString();
					break;
				}
			}
			
			return columnName;
		}catch(Exception e){
			return columnName;
		}
	}
	
	/**
	 * <pre>
	 * 파일 다운로드
	 * </pre>
	 * 
	 */
	@RequestMapping("/main/fileDown.do")
    public void fileDown(HttpServletRequest request,	HttpServletResponse response) throws IOException {
		
    	
		String path = propertiesService1.getString("manual.FILE.PATH")+propertiesService1.getString("manual.FILE.NAME");
		
		String fileDispName =  propertiesService1.getString("manual.FILE.NAME");
		
		File file = new File(path);
    	
		byte[] b = new byte[1024]; //buffer size
				
		String header = getBrowser(request);
		
		if (header.contains("MSIE")) {
	       String docName = URLEncoder.encode(fileDispName,"UTF-8").replaceAll("\\+", " ");
	       response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
		} else if (header.contains("Firefox")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Opera")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Chrome")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		}
		
		response.setHeader("Content-Type", "application/octet-stream");
		//response.setContentLength(Integer.parseInt(vo.getFileSize()));
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
	
		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
		    outs = new BufferedOutputStream(response.getOutputStream());
		    int read = 0;
	
			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
		    if (outs != null) {
				try {
					outs.flush();
				    outs.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
		    if (fin != null) {
				try {
				    fin.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
		}
    }
		
    /**
     * 브라우저 알아내기
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String header =request.getHeader("User-Agent");
                
        if (header.contains("Firefox")) {
               return "Firefox";
        } else if(header.contains("Chrome")) {
               return "Chrome";
        } else if(header.contains("Opera")) {
               return "Opera";
        } else if(header.contains("Safari")) {
            return "Chrome";
        }
        return "MSIE";
    }
    
    /**
	 * 메인 팝업
	 * @param request
	 * @param sanctno
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/main/noticePopup.do")
	public String noticePopup(HttpServletRequest request, String sanctno, HttpServletResponse response,SearchVO searchVO,ModelMap model ) throws Exception {
		Gson gson = new Gson();
		
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map = (HashMap<String,Object>) gson.fromJson(param, map.getClass());
		searchVO.setSqno(Integer.parseInt(map.get("sq_no").toString()));
	    
	    /**
		 * 로그인 사용자 정보  
		 * 
		 */
		
		NoticeVO borderInfo = noticeService.getNoticeInfo(searchVO.getSqno());
		
		model.addAttribute("borderInfo",borderInfo);
		
		return "main/noticePopup";
	}
	
}
