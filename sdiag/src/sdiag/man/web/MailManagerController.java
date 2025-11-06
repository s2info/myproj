package sdiag.man.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.SdiagProperties;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoService;
import sdiag.groupInfo.service.GroupSearchVO;
import sdiag.man.service.MailSendInfoService;
import sdiag.man.service.PolConVO;
import sdiag.man.service.PolTargetInfoVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.SecPolInfoVO;
import sdiag.man.service.UserinfoVO;
import sdiag.man.vo.MailPolicyInfoVO;
import sdiag.man.vo.MailSearchVO;
import sdiag.man.vo.MailSendConfig;
import sdiag.man.vo.MailSendInfoVO;
import sdiag.man.vo.MailTargetInfoVO;
import sdiag.stat.service.StatisticService;
import sdiag.util.CommonUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.StringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class MailManagerController {
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	@Resource(name= "MailSendInfoService")
	private MailSendInfoService mailSendService;
	
	@Resource(name="StatisticService")
	private StatisticService statService;
	
	@Resource(name="GroupInfoService")
	private GroupInfoService groupInfoService;
	
	/**
	 *  메일 전송 리스트 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/mailmanagerlist.do")
	public String mailmanagerlist(HttpServletRequest request, @ModelAttribute("searchVO") MailSearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.MAILINFO);
		//searchVO.setPageIndex(2);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		try
		{
		HashMap<String,Object> retMap = mailSendService.getMailSendInfoList(searchVO);
		List<MailSendInfoVO> list = (List<MailSendInfoVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("resultList", list);
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/mail/mailmanagerlist";
	}
	/**
	 * 메일 전송 정보 등록 및 수정
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/mailmanagermodify.do")
	public String mailmanagermodify(HttpServletRequest request
			, @ModelAttribute("searchVO") MailSearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.MAILINFO);
		try{
			MailSendInfoVO mailSendInfo = new MailSendInfoVO();
			mailSendInfo.setGubun("N");
			mailSendInfo.setIs_used("Y");
			mailSendInfo.setSend_type("REG");
			//mailSendInfo.setPol_type("04");
			List<MailTargetInfoVO> mailTargetList = new ArrayList<MailTargetInfoVO>();
			List<MailPolicyInfoVO> mailPolicyList = mailSendService.getMailSendPolicyList(searchVO);
			//MailSendConfig sendConfig = new MailSendConfig();
			//mailSendInfo.setSendConfig(sendConfig);
			List<MailSendConfig> selectList = new ArrayList<MailSendConfig>();
			if(searchVO.getMail_seq() > 0){
				mailSendInfo = mailSendService.getMailSendInfo(searchVO);
				mailTargetList = mailSendService.getMailSendTargetList(searchVO);
				//mailPolicyList = mailSendService.getMailSendPolicyList(searchVO);
				
				String[] sendSchedule = mailSendInfo.getSend_schedule().split("\\|");
				if(mailSendInfo.getSend_type().equals("REG")){
					mailSendInfo.setSend_date(sendSchedule[0]);
					mailSendInfo.setSend_hour_reg(sendSchedule[1]);
					mailSendInfo.setSend_minutes_reg(sendSchedule[2]);
				}else if(mailSendInfo.getSend_type().equals("REP")){
					mailSendInfo.setSend_day_type(sendSchedule[0]);
					mailSendInfo.setSend_day_option(sendSchedule[1]);
					mailSendInfo.setSend_day(sendSchedule[2]);
					mailSendInfo.setSend_hour_rep(sendSchedule[3]);
					mailSendInfo.setSend_minutes_rep(sendSchedule[4]);
					if(mailSendInfo.getSend_day_type().equals("WEEK")){
						for(int i=1;i<=7;i++){
							MailSendConfig option = new MailSendConfig();
							option.setSelect_value(connvertToWeekName(i, false));
							option.setSelect_text(connvertToWeekName(i, true));
							selectList.add(option);
						}
					}else{
						if(mailSendInfo.getSend_day_option().equals("D")){
							//날짜 이면
							for(int i=1;i<=31;i++){
								MailSendConfig option = new MailSendConfig();
								option.setSelect_value(String.valueOf(i));
								option.setSelect_text(String.format("%02d", i));
								selectList.add(option);
							}
						}else{
							for(int i=1;i<=7;i++){
								MailSendConfig option = new MailSendConfig();
								option.setSelect_value(connvertToWeekName(i, false));
								option.setSelect_text(connvertToWeekName(i, true));
								selectList.add(option);
							}
						}
					}
					mailSendInfo.setDayOptionList(selectList);
				}
				//searchVO.setOp_indc(mailSendInfo.getPol_type());
				//long targetUserList = mailSendService.getMailTargetUserListTotalCount(searchVO);
				//mailSendInfo.setTargetUserList(targetUserList);
			}else{
				for(int i=1;i<=31;i++){
					MailSendConfig option = new MailSendConfig();
					option.setSelect_value(String.valueOf(i));
					option.setSelect_text(String.format("%02d", i));
					selectList.add(option);
				}
				mailSendInfo.setContents_bottom("<div><span style=\"color:rgb(105, 105, 105)\"><span style=\"font-size:12px\">※ 정책별 평균점수는 정책별 가중치로 인해서 합산점수의 평균과 다를수 있습니다.</span></span></div>");
				mailSendInfo.setDayOptionList(selectList);
			}
			/*CodeInfoVO codeInfo = new CodeInfoVO();
			codeInfo.setMajr_code("RPT");
			codeInfo.setMinr_code("T01");
			CodeInfoVO weekTemp = comService.getCodeInfoForOne(codeInfo);
			codeInfo.setMinr_code("M01");
			CodeInfoVO monthTemp = comService.getCodeInfoForOne(codeInfo);
			model.addAttribute("weekTemp", StringUtil.getHtmlStrCnvr(weekTemp.getAdd_info1()));
			model.addAttribute("monthTemp", StringUtil.getHtmlStrCnvr(monthTemp.getAdd_info1()));*/
			
			//model.addAttribute("contents_temp", contents_temp);
			model.addAttribute("mailSendInfo", mailSendInfo);
			model.addAttribute("mailTargetList", mailTargetList);
			model.addAttribute("mailPolicyList", mailPolicyList);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "man/mail/mailmanagermodify";
	}
	
	private String connvertToWeekName(int day, boolean iskor){
		String day_value = iskor ? "월요일" : "MON";
		switch(day){
		case 1 : 
			day_value = iskor ? "월요일" : "MON";
			break;
		case 2 : 
			day_value = iskor ? "화요일" : "TUE";
			break;
		case 3 : 
			day_value = iskor ? "수요일" : "WED";
			break;
		case 4 : 
			day_value = iskor ? "목요일" : "THU";
			break;
		case 5 : 
			day_value = iskor ? "금요일" : "FRI";
			break;
		case 6 : 
			day_value = iskor ? "토요일" : "SAT";
			break;
		case 7 : 
			day_value = iskor ? "일요일" : "SUN";
			break;
		}
		return day_value;
	}
	/**
	 * 메일 전송 정보 저장
	 * @param request
	 * @param response
	 * @param mailSendInfoVO
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setmailmanagerinfo.do")
	public void setmailmanagerinfo(HttpServletRequest request
			, HttpServletResponse response
			, MailSendInfoVO mailSendInfoVO) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			String cronExpression = CommonUtil.generateCronExpression("*", "*", "*", "*", "*", "*", "*");
	    	String sendSchedule = "";
			if(mailSendInfoVO.getSend_type().equals("REG")){
				sendSchedule = String.format("%s|%s|%s"
						, mailSendInfoVO.getSend_date()
						, mailSendInfoVO.getSend_hour_reg()
						, mailSendInfoVO.getSend_minutes_reg());
				
				String[] dateSplit = mailSendInfoVO.getSend_date().split("-");
				/**
				 * seconds mandatory = yes. allowed values = 0-59 * / , -
					minutes mandatory = yes. allowed values = 0-59 * / , -
					hours mandatory = yes. allowed values = 0-23 * / , -
					dayOfMonth mandatory = yes. allowed values = 1-31 * / , - ? L W
					month mandatory = yes. allowed values = 1-12 or JAN-DEC * / , -
					dayOfWeek mandatory = yes. allowed values = 1-7 or SUN-SAT * / , - ? L #
					year mandatory = no. allowed values = 1970?2099 * / , -

				 */
				//System.out.println(dateSplit[0] + "][" + dateSplit[1] + "][" + dateSplit[2]);
				cronExpression = CommonUtil.generateCronExpression("0"
						, mailSendInfoVO.getSend_minutes_reg()
						, mailSendInfoVO.getSend_hour_reg()
						, dateSplit[2]
						, dateSplit[1]
						, "?"
						, dateSplit[0]);
				mailSendInfoVO.setSchedule_expression(cronExpression);
			}else if(mailSendInfoVO.getSend_type().equals("REP")){
				sendSchedule = String.format("%s|%s|%s|%s|%s"
						, mailSendInfoVO.getSend_day_type()
						, mailSendInfoVO.getSend_day_option()
						, mailSendInfoVO.getSend_day()
						, mailSendInfoVO.getSend_hour_rep()
						, mailSendInfoVO.getSend_minutes_rep());
				cronExpression = CommonUtil.generateCronExpression("0"
						, mailSendInfoVO.getSend_minutes_rep()
						, mailSendInfoVO.getSend_hour_rep()
						, mailSendInfoVO.getSend_day_type().equals("WEEK") ? "?" : mailSendInfoVO.getSend_day_option().equals("D") ? mailSendInfoVO.getSend_day() : "?"
						, "*"
						, mailSendInfoVO.getSend_day_type().equals("WEEK") ? mailSendInfoVO.getSend_day() : mailSendInfoVO.getSend_day_option().equals("D") ? "?" : String.format("%s#%s", mailSendInfoVO.getSend_day(), mailSendInfoVO.getSend_day_option().substring(1, 2))
						, "*");
				mailSendInfoVO.setSchedule_expression(cronExpression);
				
			}else{
				//mailSendInfoVO.setSchedule_expression(mailSendInfoVO.getSchedule_expression());
			}
			mailSendInfoVO.setSend_schedule(sendSchedule);
			
			List<MailTargetInfoVO> mailTargetList = new ArrayList<MailTargetInfoVO>();
			if(!mailSendInfoVO.getTargetCodeList().equals("")){
				String[] targetStrList = mailSendInfoVO.getTargetCodeList().split(",");
				for(String target:targetStrList){
					MailTargetInfoVO targetVO = new MailTargetInfoVO();
					//targetCode + "|" +targetType + "|" +targetNm
					String[] targetInfos = target.split("\\|");
					targetVO.setTarget_code(targetInfos[0]);
					targetVO.setTarget_type(targetInfos[1]);
					targetVO.setTarget_nm(targetInfos[2]);
					mailTargetList.add(targetVO);
				}
				mailSendInfoVO.setMailTargetList(mailTargetList);
			}
			
			List<MailPolicyInfoVO> mailPolicyList = new ArrayList<MailPolicyInfoVO>();
			if(!mailSendInfoVO.getPolSelectList().equals("")){
				String[] polStrList = mailSendInfoVO.getPolSelectList().split(",");
				for(String pol:polStrList){
					MailPolicyInfoVO polVO = new MailPolicyInfoVO();
					polVO.setSec_pol_id(pol);
					mailPolicyList.add(polVO);
				}
				mailSendInfoVO.setMailPolicyList(mailPolicyList);
			}
			
			if(mailSendInfoVO.getGubun().equals("N")){
				mailSendInfoVO.setBatchjobname("sldmNoticeMailsend.jar");
			}else{
				if(mailSendInfoVO.getPol_type().equals("04")){
					mailSendInfoVO.setBatchjobname("sldmWeeklyMailsend.jar");
				}else if(mailSendInfoVO.getPol_type().equals("05")){
					mailSendInfoVO.setBatchjobname("sldmMonthlyMailsend.jar");
				}else {
					mailSendInfoVO.setBatchjobname("sldmWanMailsend.jar");
				}
			}
			
	    	long ret = mailSendService.setMailSendInfoModify(mailSendInfoVO);
		    
			if(ret <= 0){
				msg = "저장중 오류가 발생하였습니다.";
			}
			map.put("mailSeq", ret);
		    map.put("ISOK", ret > 0 ? true : false);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();
			map.put("mailSeq", 0);
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	/**
	 * 메일 전송 대상자 조회 팝업
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/man/mailTargetSearchPopup.do")
	private String mailTargetSearchPopup(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		//searchVO.setPageIndex(2);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		try
		{	
			HashMap<String,Object> retMap = null;
			List<GroupDetailInfoVO> orgInfoList = new ArrayList<GroupDetailInfoVO>();
			int totalCnt = 0;
			
			if(!searchVO.getSearchCondition().isEmpty() || searchVO.getSearchCondition() !=""){
				if(searchVO.getSearchCondition().equals("1") || searchVO.getSearchCondition().equals("2"))
					retMap = groupInfoService.getOrgUserInfoList(searchVO);
				else if(searchVO.getSearchCondition().equals("3") || searchVO.getSearchCondition().equals("4"))
					retMap = groupInfoService.getOrgGroupInfoList(searchVO);
				else
					retMap = groupInfoService.getPopGroupInfoList(searchVO);
				
				orgInfoList = (List<GroupDetailInfoVO>)retMap.get("list");
				totalCnt = (int)retMap.get("totalCount");
			}
			
			model.addAttribute("resultList", orgInfoList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/mail/mailTargetSearchPopup";
		
	}
	/**
	 * mail 정송 대상 전체 Count
	 * @param request
	 * @param response
	 * @param mailSendInfoVO
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getmailtargetuserlistcount.do")
	public void getmailtargetuserlistcount(HttpServletRequest request
			, HttpServletResponse response
			, MailSearchVO searchVO) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			long totalTargetList = mailSendService.getMailTargetUserListTotalCount(searchVO);
		    
			isOk = true;
			map.put("TargetTotalCount", totalTargetList);
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			map.put("TargetTotalCount", 0);
			msg = ex.getMessage();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	
	///man/mainfreeviewPopup.do
	@RequestMapping(value="/man/mainfreeviewPopup.do")
	private String mainfreeviewPopup(HttpServletRequest request,
			HttpServletResponse response,
			//String param,
			MailSearchVO searchVO,
		    ModelMap model) throws Exception{
		try
		{	
			//System.out.println(msendInfo.getContents());
			//String param = request.getParameter("param").toString();
			/*
			System.out.println(URLDecoder.decode(param, "UTF-8"));
			String mailSendInfo = URLDecoder.decode(param, "UTF-8");
			Gson gson = new Gson();
			MailSendInfoVO sendInfo =  gson.fromJson(mailSendInfo, MailSendInfoVO.class);
			System.out.println(sendInfo.getGubun() + "][" + sendInfo.getContents_top());
			*/
			MailSendInfoVO mailSendInfo = mailSendService.getMailSendInfo(searchVO);
			
			CodeInfoVO codeInfo = new CodeInfoVO();
			codeInfo.setMajr_code("RPT");
			codeInfo.setMinr_code("ASK");
			CodeInfoVO askInfo = comService.getCodeInfoForOne(codeInfo);
			mailSendInfo.setMail_ask(StringUtil.getHtmlStrCnvr(askInfo.getAdd_info1()));
			mailSendInfo.setContents(StringUtil.getHtmlStrCnvr(mailSendInfo.getContents()));
			mailSendInfo.setContents_top(StringUtil.getHtmlStrCnvr(mailSendInfo.getContents_top()));
			mailSendInfo.setContents_bottom(StringUtil.getHtmlStrCnvr(mailSendInfo.getContents_bottom()));
			
			model.addAttribute("sendInfo", mailSendInfo);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/mail/mainfreeviewPopup";
		
	}
	/**
	 * 메일 상단 템플릿 조회
	 * @param request
	 * @param response
	 * @param searchVO
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getmailtempstr.do")
	public void getmailtempstr(HttpServletRequest request
			, HttpServletResponse response
			, MailSearchVO searchVO) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			CodeInfoVO codeInfo = new CodeInfoVO();
			codeInfo.setMajr_code("RPT");
			codeInfo.setMinr_code(searchVO.getTarget_type());
			CodeInfoVO contentTemp = comService.getCodeInfoForOne(codeInfo);
		    
			isOk = true;
			map.put("contentTemp", contentTemp.getAdd_info1() == null ? "" : StringUtil.getHtmlStrCnvr(contentTemp.getAdd_info1()));
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	/**
	 * 메일 전송정보 저장
	 * @param request
	 * @param searchVO
	 * @param mailInfoVO
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/man/mailmanagersave.do")
	public String mailmanagersave(HttpServletRequest request
			, @ModelAttribute("searchVO") MailSearchVO searchVO
			, MailSendInfoVO mailInfoVO) throws Exception{	
		System.out.println("**********************************************");
		try{
			//System.out.println("Mail Info Save ........");
			//System.out.println(mailInfoVO.getContents());
			//System.out.println(URLDecoder.decode(mailInfoVO.getContents(), "UTF-8"));
			
			//Thread.sleep(2000);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "forward:/man/mailmanagerlist.do";
	}*/
	
	/**
	 * 메일 수동 발송 처리
	 * @param request
	 * @param response
	 * @param searchVO
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setmailsend.do")
	public void setmailsend(HttpServletRequest request
			, HttpServletResponse response
			, MailSearchVO searchVO) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			//Runtime rt = Runtime.getRuntime();
			//Process pc = null;
			MailSendInfoVO mailSendInfo = mailSendService.getMailSendInfo(searchVO);
			String jobScript = String.format("java -jar %s %s %s"
					, String.format("%s%s", SdiagProperties.getProperty("Globals.mail.batch_agent_path"), mailSendInfo.getBatchjobname())
					, SdiagProperties.getProperty("Globals.mail.properties_path")
					, mailSendInfo.getMail_seq());
			
			try {
				DefaultExecutor excutor = new DefaultExecutor();
				CommandLine cmdLine = CommandLine.parse(jobScript);
				excutor.execute(cmdLine);
				//pc = rt.exec(jobScript);
				//System.out.println(jobScript);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("메일 전송 Agent 실행 오류 입니다.");
			}finally{
				isOk = true;
			}
			/*try {
				
				//pc = rt.exec(jobScript);
				//System.out.println(jobScript);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("메일 전송 Agent 실행 오류 입니다.");
			}finally{
				pc.waitFor();
				pc.destroy();
				isOk = true;
			}*/
			
			
			
			map.put("ISOK", isOk);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	/**
	 * 메일 발송 Agent Reset
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setmailsendagentreset.do")
	public void setmailsendagentreset(HttpServletRequest request
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			String osversion = System.getProperty("os.name").toLowerCase();
			if(osversion.indexOf("nix") >= 0 || osversion.indexOf("nux") >= 0 || osversion.indexOf("aix") > 0){
				Runtime rt = Runtime.getRuntime();
				Process pc = null;
				
				//String jobScript = String.format("java -jar %s %s %s"
				//		, String.format("%s%s", SdiagProperties.getProperty("Globals.mail.batch_agent_path"), "")
				//		, SdiagProperties.getProperty("Globals.mail.properties_path")
				//		, "");
				try {
					//./sldmAgentRestart.sh >/dev/null 2>&1 &
					String jobScript = String.format("%s%s", SdiagProperties.getProperty("Globals.mail.batch_agent_path"), "sldmAgentRestart.sh");
					pc = rt.exec(jobScript);
					//System.out.println(jobScript);
					
					isOk = true;
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("배치 Agent Reset 오류 입니다.");
				}finally{
					pc.waitFor();
					pc.destroy();
				}
			}else{
				throw new Exception("OS 버전이 맞지 않습니다.");
			}
			map.put("ISOK", isOk);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	
	/**
	 * <pre>
	 * 파일 다운로드
	 * </pre>
	 * 
	 */
	@RequestMapping("/man/fileDown.do")
    public void fileDown(HttpServletRequest request,	HttpServletResponse response, String type) throws Exception {
		
		
		CodeInfoVO codeInfo = new CodeInfoVO();
		
		codeInfo.setMajr_code("MFN");
		codeInfo.setMinr_code(type);
		
		CodeInfoVO fileInfo = comService.getCodeInfoForOne(codeInfo);
    	
		String path = SdiagProperties.getProperty("Globals.mail.batch_agent_path")+fileInfo.getCode_desc();
		
		String fileDispName =  fileInfo.getCode_desc();
		
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
}
