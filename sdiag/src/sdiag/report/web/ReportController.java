package sdiag.report.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ibm.icu.util.Calendar;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.MenuItemVO;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.DashboardService;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.login.service.UserManageVO;
import sdiag.man.service.PolConVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.report.service.OrgResultInfoVO;
import sdiag.report.service.ReportSearchResultVO;
import sdiag.report.service.ReportSearchVO;
import sdiag.report.service.ReportService;
import sdiag.stat.service.StatisticSearchVO;
import sdiag.util.CommonUtil;
import sdiag.util.DateUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.ExcelUtilMulti;
import sdiag.util.LeftMenuInfo;
import sdiag.util.MajrCodeInfo;

@Controller
public class ReportController {
	
	@Resource(name = "ReportService")
    private ReportService reportService;
	
	@Resource(name = "DashboardService")
	private DashboardService dashboardService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "PolicyService")
	private PolicyService polService;	

	/**
	 * DATA GET TEST - 미사용
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/report/sheet.do")
	public String reportHandeler(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("searchVO") DashboardSearchVO searchVO,
			ModelMap model
			) throws Exception{
		
		try{
		
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			
			String dpflag = ""; // cap:조직, (cap_code:조직코드), super:관리자, personal:개인, replace:대무자
	 		/**
	 		 * 권한별 정보 조회용  
	 		 */
			String ktmp = request.getParameter("ktmp") != null && !request.getParameter("ktmp").toString().equals("") ? request.getParameter("ktmp") : "";
			if(ktmp.equals("")){
				ktmp = (String)request.getSession().getAttribute("loginType");
			}
			
			
			String orgcodeMode = "";
			Boolean orgcodeModeFlag = false;
			
			if( ktmp != null ){
				dpflag = ktmp;
				model.addAttribute("dpflag", dpflag);
				
				if(!dpflag.equals("personal") || !dpflag.equals("sa")){   //조직코드 체크
					orgcodeMode = dpflag;
				}
			}else{
				dpflag = "personal";   // 초기 개인모드 설정 추가
				model.addAttribute("dpflag", dpflag);
			}
			
			String soc_org_code = ""; // 담당 조직코드
			String org_level = "";	// 조직레벨
			String user_authnm = "";		
			
	//		String auth = loginInfo.getRole_code();
	//		String orgCode = loginInfo.getOrgcode();				
	//		String emp_no = loginInfo.getUserid();
			
			String auth = "1";
			String orgCode = "";				
			String emp_no = "10002935";
			
			boolean isUser = auth == "1" ? false : true;		
			
			if(Integer.parseInt(auth) == 1)
			{
				isUser = false;
				auth = "1";
				orgCode = "";
			}
			
	
			/**
			 * [1] 대무자를 조회
			 */	
			UserManageVO proxyUser = new UserManageVO();
			//해당 조직 조직장 사번 조회
			if(!dpflag.equals("personal")){
				String capNo =dashboardService.getOrgCapNo(dpflag); 
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("emp_no", emp_no);
				hmap.put("capNo", capNo);
				
				proxyUser = dashboardService.getProxyUser(hmap);
			}
			if (proxyUser != null){
				
				if(!dpflag.equals("personal")){
					emp_no = proxyUser.getEmp_no();  // 대무자이면 emp_no 재설정
				}
				user_authnm = "대무자:"+emp_no;
			}		
			
			/**
			 * [2] 조직장인지 
			 */
	//		List<OrgGroupVO> orgCapList = comService.getOrgCapInfoList(emp_no);
	//		if(orgCapList.size() > 0)
	//		{
	//			//조직장 
	//			OrgGroupVO capInfo = orgCapList.get(0);
	//			isUser = false;
	//			auth = "2";
	//			orgCode=capInfo.getOrg_code().toString();
	//			
	//			if(!orgCode.equals(dpflag)){
	//				emp_no = loginInfo.getUserid(); // getEmp_no(request);
	//				orgCode = dpflag;
	//			}
	//			
	//			System.out.println("조직장????!!!!");
	//		}
			
	//		
	//		/** [1-1] 담당부서 정보 조회 */
	//		HashMap<String,Object> rMapUinfo = new HashMap<String,Object>();
	//		List<UserIdxInfoCurrVO> list_uinfo = new ArrayList<UserIdxInfoCurrVO>();
	//		rMapUinfo = dashboardService.getUserInfo(emp_no);
	//		list_uinfo = (List<UserIdxInfoCurrVO>)rMapUinfo.get("list");
	//		model.addAttribute("resultList_uinfo", list_uinfo);	
	//		
	//		if(list_uinfo != null){
	//			if(list_uinfo.size() > 1){
	//				for(int u=0;u<list_uinfo.size();u++){
	//					System.out.println("#########"+list_uinfo.get(u).getUnder_code() + "(orglevel:" + list_uinfo.get(u).getOrg_level() + ")");
	//					soc_org_code = list_uinfo.get(u).getUnder_code();
	//					org_level = list_uinfo.get(u).getOrg_level();
	//					
	//					System.out.println("==========1=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
	//					
	//					if(orgcodeMode.equals(soc_org_code)){
	//						orgcodeModeFlag = true;
	//						System.out.println("==========2=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
	//						orgCode = orgcodeMode;
	//					}
	//				}
	//			}else{
	//				if(list_uinfo.get(0).getUnder_code() != null){
	//					System.out.println("#########" + list_uinfo.get(0).getUnder_code());
	//					soc_org_code = list_uinfo.get(0).getUnder_code();
	//					org_level = list_uinfo.get(0).getOrg_level();
	//				}else{ 
	//					System.out.println("#########" + "personal");
	//					
	//					soc_org_code = "";
	//					org_level = "";
	//				}
	//			}
	//		}else{
	//			System.out.println("=====================null");
	//		}
	//		
	//		if(orgcodeModeFlag){
	//			soc_org_code = orgcodeMode;
	//		}
	//		
	//		System.out.println("soc_org_code:" + soc_org_code + "//" + "org_level:" + org_level);
	//		
	//		/** 조직코드 */
	//		if(dpflag.equals("sa")){
	//			searchVO.setOrg_upper("000001"); 
	//		}else{
	//			searchVO.setOrg_upper(soc_org_code); 
	//		}
	//		/** 사번코드 */
	//		searchVO.setEmp_no(emp_no);			
	//		
	//		if (auth != null){
	//			switch (auth){
	//				case "1": 
	//					user_authnm = "관리자/운영자"; 
	//					break;
	//				case "2": 
	//					user_authnm = "조직장"; 
	//					break;
	//				default : 
	//					user_authnm = "일  반"; 
	//					break;
	//			}
	//		}
	//		
	//		System.out.println("==================::"+user_authnm);
	//		
	//		
	//		System.out.println("===============================================");
	//		/** parameter : emp_no */
	//		List<EgovMap> caporgList = dashboardService.getUserInfoCap(searchVO); //조직 정보조회
	//		System.out.println("===============================================caporgList.size=" + caporgList.size());	
	//	    
	//	    /** parameter : upper_org_code */	    
	//	    List<EgovMap> orgorgList = dashboardService.getOrgSubList(searchVO); // 하위조직 조회
	//	    System.out.println("===============================================orgorgList.size=" + orgorgList.size());		
	//		
	//		//조직장이고 하위조직 없으면 팀장
	//	    
	//	    if(auth.equals("2")){
	//		    if(caporgList.size() > 0 && orgorgList.size() < 1){
	//		    	System.out.println("============팀장");
	//		    }else if(caporgList.size() > 0 && orgorgList.size() > 0){
	//		    	System.out.println("============조직장");
	//		    }
	//	    }

			/**
			 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
			 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
			 */
			List<EgovMap> rstListPer = reportService.getPersonalPolcount(searchVO);
			
			String resultListPersonal = "";
			int avgScore = 0;
			int sumScore = 0;
			String tmpScore = "";
	
			for(EgovMap pow:rstListPer){
				
				resultListPersonal += pow.get("diagdesc");
				resultListPersonal += " : " + pow.get("count");
				resultListPersonal += " / " + pow.get("score");
				resultListPersonal += " | ";
				
				tmpScore = (String) pow.get("score").toString();
				sumScore += Integer.parseInt(tmpScore);
	
			}
			
			avgScore = sumScore / rstListPer.size();
			
			//System.out.println("==================:" + avgScore);		
			System.out.println("==================:" + resultListPersonal);
			
			model.addAttribute("tableTitleP1", "※---------※");	
			model.addAttribute("jindanAvg", avgScore);
			model.addAttribute("personalAvg", resultListPersonal);
			
			
			/**
			 * 조직 :: 진단점수 및 진단항목별 전체건수/점수
			 * map 	<!-- 팀 조직의 진단항목별 전체건수 및 점수 @org_code -->
			 * <select id="ReportDAO.OrgPolTotcountAvg" parameterClass="java.lang.String" resultClass="egovMap" remapResults="true">
			 */
			sumScore = 0;
			avgScore = 0;
			String rstListOrg = "";
			List<EgovMap> rstOrgPolTotcountAvg = reportService.getOrgPolTotcountAvg(searchVO);
			System.out.println("==================:" + rstOrgPolTotcountAvg);
			for(EgovMap qow:rstOrgPolTotcountAvg){
				
				rstListOrg += qow.get("diagdesc");
				rstListOrg += " : " + qow.get("count");
				rstListOrg += " / " + qow.get("avg");
				rstListOrg += " | ";
				
				tmpScore = (String) qow.get("avg").toString();
				sumScore += Integer.parseInt(tmpScore);
	
			}			
			
			avgScore = sumScore / rstOrgPolTotcountAvg.size();
			
			model.addAttribute("tableTitleO", "※ 조직 진단항목 건수/점수 ※");
			model.addAttribute("jindanOrgAvg", avgScore);
			model.addAttribute("OrgPolTotcountAvg", rstListOrg);
			
			/**
			 * 개인 지수화정책 항목별 건수 점수 진단상태
			 * 	<!-- 개인의 지수화정책 항목별 총건수 및 점수, 진단상태 -->
			 * 	<select id="ReportDAO.getPerPolItemCountScoreStatus" parameterClass="java.lang.String" resultClass="egovMap" remapResults="true">
			 */
			List<EgovMap> rstPerPolCount = reportService.getPerPolItemCountScoreStatus(searchVO);
			
			model.addAttribute("tableTitlePC", "※ 지수정책항목별 발생건수 및 점수 ※");
			model.addAttribute("PerPolCount", rstPerPolCount);			
			
			
			/**
			 * 팀원별 발생건수 점수
			 * 	<!-- 사용자별 발생건수 및 점수 = 팀원의 전체 건수 및 점수, 진단상태 => 날짜, 조직, 사번/이름, Mac/IP, 전체건수, 평균, 진단결과 -->
			 * 	<select id="ReportDAO.getPerCountScoreStatus" parameterClass="java.lang.String" resultClass="egovMap" remapResults="true">
			 */
					List<EgovMap> rstPerTeam = reportService.getPerCountScoreStatus(searchVO);
					
					model.addAttribute("tableTitleT", "※ 사용자별 발생건수 및 점수 ※");
					model.addAttribute("PerTeam", rstPerTeam);
			
			
			/**
			 * 조직별 발생건수 및 점수, 진단상태
			 * map <!-- 조직별 발생건수 및 점수 => 조직의 지수화정책 대분류 항목별(A01, B01, D01)  전체건수, 평균, 진단상태 -->
			 */
			List<EgovMap> resultList = reportService.getOrgPolItemCountScoreStatus(searchVO);
	
			for(EgovMap gow:resultList){
				System.out.println(gow.get("sumrgdt") + "::" + gow.get("orgnm"));
			}
			
			System.out.println(resultList);
			
			model.addAttribute("tableTitleP2", "※ 조직별 발생건수 및 점수 ※");
			model.addAttribute("OrgPolcount", resultList);
			
			
			/**
			 * 조직 :: 정책별 발생건수 및 점수, 진단상태
			 * <!-- [1]조직 정책별 발생건수 및 점수, 진단상태 => 진단날짜, 진단명, 점검명, 정책명, 건수, 점수, 진단결과 -->
			 */
			List<EgovMap> rstOrgDiagItemCountScoreStatus = reportService.getOrgDiagItemCountScoreStatus(searchVO);
			
			model.addAttribute("tableTitleP3", "※ 정책별 발생건수 및 점수 ※");
			model.addAttribute("OrgDiagItem", rstOrgDiagItemCountScoreStatus);


		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "/report/reportsheet";
	}
	/**
	 * 진단보고서 - 일간보고서
	 * @param request
	 * @param model
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/report/sheetStar.do")
	public String reportStar(HttpServletRequest request, 
			ModelMap model, 
			@ModelAttribute("PolicySearchVO") PolicySearchVO searchVO) throws Exception{
		

		/**
		 * 사용자 로그인 정보
		 * auth 값정의 : 관리자/운영자:1 , 대무자(조직장):2, 일반사용자:3
		 * 정책모니터링 조회시 사용하는 인증코드임
		 * 
		 * loginType : PERSONAL(개인), ADMIN(관리자), 조직코드(조직장:CAPTAIN, 대무자:PROXY ..)
		 */
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		
		String empno = loginInfo.getUserid();// getEmp_no(request); //
		String auth = loginInfo.getRole_code().equals("1") || loginInfo.getRole_code().equals("2") ? "1" : loginInfo.getRole_code(); //getUser_auth(request).equals("3") ? getUser_auth(request) : "1"; 
		String orgCode = loginInfo.getOrgcode();
		String requestType = (String)request.getSession().getAttribute("loginType");

		if(requestType.equals("personal")){
			auth = "3";
		}else if(requestType.equals("sa")){
			auth = "1";
		}else{
			auth = "2";
		}
		
		boolean isUser = auth == "1" ? false : true;		

		
		
		//오늘 날짜 셋팅
		Date date = new Date();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		
		model.addAttribute("nowDate", fm.format(date));
		
		
		//검색날짜 조회 - 최종날짜 조회
		String begin_Date = polService.getUserInfoLastDate(searchVO);
		searchVO.setBegin_date(begin_Date);
		
		model.addAttribute("getdate", begin_Date);
		
		
		
		if(Integer.parseInt(auth) == 1)
		{
			isUser = false;
			auth = "1";
			orgCode = "";
		}else if(Integer.parseInt(auth) == 2){
			isUser = false;
			orgCode=requestType;
		}		
		
		//협력사 지수 여부
		HashMap<String, String> codeInfo = new HashMap<String, String>();
		codeInfo.put("majCode", "PIX");
		codeInfo.put("minCode", "02");
		
		CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
		String isKpc = isKpcInfo.getAdd_info1();
		
		//조직도 조회
		CommonUtil.getCreateOrgTree(request, comService, orgCode, isUser, empno, isKpc);
		CommonUtil.topMenuToString(request, "report", comService);
		CommonUtil.reportLeftMenuToString(request, LeftMenuInfo.REPORT_DAY);	
		return "/report/reportStar";
	}
	
	/**
	 * 일별 보고서 조회결과
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/report/sheetAjax.do")
	public void reportHandelerAjax(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("searchVO") DashboardSearchVO searchVO,
			ModelMap model
			) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false;
	    
		try{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
		    String beginDate = searchVO.getBegin_date().replace("-", ""); //검색날짜
		    searchVO.setBegin_date(beginDate);
			
			String dpflag = ""; // cap:조직, (cap_code:조직코드), super:관리자, personal:개인, replace:대무자
			
	 		/**
	 		 * 권한별 정보 조회용  
	 		 */
			String ktmp = ""; // request.getParameter("ktmp") != null && !request.getParameter("ktmp").toString().equals("") ? request.getParameter("ktmp") : "";

			if(ktmp.equals("")){
				ktmp = (String)request.getSession().getAttribute("loginType");
			}
		
			//조직 조회 코드
			String orgUpper = request.getParameter("org_upper") != null && !request.getParameter("org_upper").toString().equals("") ? request.getParameter("org_upper") : "";
//			if(!orgUpper.equals("") && !ktmp.equals("personal")){
//				ktmp = orgUpper;
//			}

			String orgcodeMode = "";
			Boolean orgcodeModeFlag = false;
			
			if( ktmp != null ){
				dpflag = ktmp;
				model.addAttribute("dpflag", dpflag);
				
				if(!dpflag.equals("personal") || !dpflag.equals("sa")){   //조직코드 체크
					orgcodeMode = dpflag;
				}
			}else{
				dpflag = "personal";   // 초기 개인모드 설정 추가
				model.addAttribute("dpflag", dpflag);
			}
			
			String soc_org_code = ""; // 담당 조직코드
			String org_level = "";	// 조직레벨
			String user_authnm = "";		
			
			/**
			 * 사용자 로그인 정보
			 * auth 값정의 : 관리자/운영자:1 , 대무자(조직장):2, 일반사용자:3
			 * 정책모니터링 조회시 사용하는 인증코드임
			 */
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			String auth = loginInfo.getRole_code();// getUser_auth(request);
			String orgCode = loginInfo.getOrgcode();// getOrg_code(request);		
			String emp_no = loginInfo.getUserid();// getEmp_no(request);
			
			String currOrgName = "";
			
			boolean isUser = auth == "1" ? false : true;		
			
			if(Integer.parseInt(auth) == 1)
			{
				isUser = false;
				auth = "1";
			}
			
	
			/**
			 * [1] 대무자를 조회
			 */	
			UserManageVO proxyUser = new UserManageVO();
			//해당 조직 조직장 사번 조회
			if(!dpflag.equals("personal")){
				String capNo =dashboardService.getOrgCapNo(dpflag); 
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("emp_no", emp_no);
				hmap.put("capNo", capNo);
				
				proxyUser = dashboardService.getProxyUser(hmap);
			}
			if (proxyUser != null){
				
				if(!dpflag.equals("personal")){
					emp_no = proxyUser.getEmp_no();  // 대무자이면 emp_no 재설정
				}
				user_authnm = "대무자:"+emp_no;
			}		
			
			/**
			 * [2] 조직장인지 
			 */
			List<OrgGroupVO> orgCapList = comService.getOrgCapInfoList(emp_no);
			if(orgCapList.size() > 0)
			{
				//조직장 
				OrgGroupVO capInfo = orgCapList.get(0);
				isUser = false;
				auth = "2";
				orgCode=capInfo.getOrg_code().toString();
				
				if(!orgCode.equals(dpflag)){
					emp_no = loginInfo.getUserid();
					orgCode = dpflag;
				}
				
				System.out.println("조직장????!!!!");
			}
			
			
			/** [1-1] 담당부서 정보 조회 */
			HashMap<String,Object> rMapUinfo = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> list_uinfo = new ArrayList<UserIdxInfoCurrVO>();
			rMapUinfo = dashboardService.getUserInfo(emp_no);
			list_uinfo = (List<UserIdxInfoCurrVO>)rMapUinfo.get("list");
			model.addAttribute("resultList_uinfo", list_uinfo);	
						
			if(list_uinfo != null && list_uinfo.size() > 0){
				if(list_uinfo.size() > 1){
					for(int u=0;u<list_uinfo.size();u++){
						soc_org_code = list_uinfo.get(u).getUnder_code();
						org_level = list_uinfo.get(u).getOrg_level();
						
						
						if(orgcodeMode.equals(soc_org_code)){
							orgcodeModeFlag = true;
							orgCode = orgcodeMode;
						}
					}
				}else{
					if(list_uinfo.get(0).getUnder_code() != null){
						soc_org_code = list_uinfo.get(0).getUnder_code();
						org_level = list_uinfo.get(0).getOrg_level();
					}else{ 
						soc_org_code = "";
						org_level = "";
					}
				}
			}
			
			if(orgcodeModeFlag){
				soc_org_code = orgcodeMode;
			}
			
			
			/** 조직코드 */
			if(dpflag.equals("sa")){
				if(!orgUpper.equals("")){
					orgCode = orgUpper;
				}else{
					orgCode = "000001";
					searchVO.setOrg_upper("000001");
				}				
			}else{
				searchVO.setOrg_upper(soc_org_code); 
				if(!orgUpper.equals("")){
					orgCode = orgUpper;
					searchVO.setOrg_upper(orgUpper);
				}else{
					searchVO.setOrg_upper(soc_org_code);
				}
			}
		
			/** 사번코드 */
			searchVO.setEmp_no(emp_no);	
			
			
			if (auth != null){
				switch (auth){
					case "1": 
						user_authnm = "관리자/운영자"; 
						break;
					case "2": 
						user_authnm = "조직장"; 
						break;
					default : 
						user_authnm = "일  반"; 
						break;
				}
			}			
			
			/** parameter : emp_no */
			List<EgovMap> caporgList = dashboardService.getUserInfoCap(searchVO); //조직 정보조회
		    
		    /** parameter : upper_org_code */	    
		    List<EgovMap> orgorgList = dashboardService.getOrgSubList(searchVO); // 하위조직 조회
			
			//조직장이고 하위조직 없으면 팀장
		    
		    if(auth.equals("2")){
			    if(caporgList.size() > 0 && orgorgList.size() < 1){
			    }else if(caporgList.size() > 0 && orgorgList.size() > 0){
			    }
		    }
		    
		  
			String rtnStr = "";			
			int avgScore = 0;
			int sumScore = 0;
			String tmpScore = "";
			
			isOk = true;
			
			/** 
			 * case controll [A] 개인
			 * [1] - [3]

			 * case controll [B] 팀장
			 * [2] - [4] - [6]

			 * case controll [C] 조직장
			 * [2] - [5] - [6]

			 * case controll [D] 관리자
			 * default 000001
			 * tree org code -> XXX -> 팀 [2] - [4] or [5] - [6]

			 * case controll [E] 대무자
			 * 
			 */
			
			/* 현재 조직 이름 조회 */
			currOrgName = reportService.getOrgSelectName(searchVO);
						
			if(ktmp.equals("personal")){
				rtnStr = personalPolcount(searchVO); //[1]
				rtnStr += perPolItemCountScoreStatus(searchVO); //[3]
			}else{
				
				/* 하위조직 있는지 */
				
				rtnStr += orgPolTotcountAvg(searchVO); //[2]
				
				if(orgorgList.size() < 1 ){
					/* 하위조직 없으면 */
					rtnStr += perCountScoreStatus(searchVO); //[4]
				}else{
					/* 하위조직 있으면 */
					rtnStr += orgPolItemCountScoreStatus(searchVO); //[5]
				}
				
				rtnStr += orgDiagItemCountScoreStatus(searchVO); //[6]				
				
			}
			/*
			 * 동일코드 제거
			 * else if(ktmp.equals("sa")){
				rtnStr += orgPolTotcountAvg(searchVO); //[2]
				
				* 하위조직 있는지 *
				System.out.println("============::orgorgList::"+orgorgList.size());
				
				if(orgorgList.size() < 1 ){
					* 하위조직 없으면 *
					rtnStr += perCountScoreStatus(searchVO); //[4]
				}else{
					* 하위조직 있으면 *
					rtnStr += orgPolItemCountScoreStatus(searchVO); //[5]
				}
				
				rtnStr += orgDiagItemCountScoreStatus(searchVO); //[6]
			}
			 * */

		
			/**
			 * 부서진단 추가 
			 * 관리자 이거나 상무 이상이거나 대무자직급이 상무 이상일때.....
			 */
			String rtnBuseoStr = "";
			String isViewTab = "N";
			if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || !loginInfo.getRole_code().equals("3") || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
				if(!ktmp.equals("personal")){
					System.out.println("관리자 이거나 상무 이상이거나 대무자직급이 상무 이상일때....."+ ktmp + "][" + searchVO.getOrg_upper());
					searchVO.setBuseoType("Y");
					isViewTab = "Y";
					
					/* 하위조직 있는지 */
					rtnBuseoStr += orgPolTotcountAvg(searchVO); //[2]
					
					if(orgorgList.size() < 1 ){
						/* 하위조직 없으면 */
						//rtnBuseoStr += perCountScoreStatus(searchVO); //[4]
						searchVO.setIsSubOrg("N");
						rtnBuseoStr += orgPolItemCountScoreStatus(searchVO);
					}else{
						/* 하위조직 있으면 */
						searchVO.setIsSubOrg("Y");
						rtnBuseoStr += orgPolItemCountScoreStatus(searchVO); //[5]
					}
					
					rtnBuseoStr += orgDiagItemCountScoreStatus(searchVO); //[6]	
				}
				
			}
	       
	        msg = "Data count : 0";
		    map.put("ISOK", isOk);
		    map.put("MSG", "");
		    map.put("strList", rtnStr);
		    map.put("strBuseoList", rtnBuseoStr);
		    map.put("currOrgName", currOrgName);
		    map.put("orgCode", orgCode);
		    map.put("isviewtab", isViewTab);
		}catch(Exception e){
			e.printStackTrace();
	        map.put("totalPage", 0);
	        map.put("currentpage", 1);			    
			
	        msg = "Data count : 0";
		    map.put("ISOK", isOk);
		    map.put("MSG", "");
		    map.put("strList", "");
		    map.put("currOrgName", "");
		    map.put("orgCode", "");
		}			
		
    
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	
	}	
	
	/**
	 * 월간 보고서
	 * @param request
	 * @param model
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/report/monthSheetStar.do")
	public String monthSheetStar(HttpServletRequest request, 
			ModelMap model, 
			@ModelAttribute("searchVO") ReportSearchVO searchVO) throws Exception{
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		
		String empno = loginInfo.getUserid();// getEmp_no(request); //
		String auth = loginInfo.getRole_code().equals("1") || loginInfo.getRole_code().equals("2") ? "1" : loginInfo.getRole_code(); //getUser_auth(request).equals("3") ? getUser_auth(request) : "1"; 
		String orgCode = loginInfo.getOrgcode();
		String requestType = (String)request.getSession().getAttribute("loginType");

		if(requestType.equals("personal")){
			auth = "3";
		}else if(requestType.equals("sa")){
			auth = "1";
		}else{
			auth = "2";
		}
		
		boolean isUser = auth == "1" ? false : true;		
		if(Integer.parseInt(auth) == 1)
		{
			isUser = false;
			auth = "1";
			orgCode = MajrCodeInfo.RootOrgCode;
		}else if(Integer.parseInt(auth) == 2){
			isUser = false;
			orgCode=requestType;
		}		
		//협력사 지수 여부
		HashMap<String, String> codeInfo = new HashMap<String, String>();
		codeInfo.put("majCode", "PIX");
		codeInfo.put("minCode", "02");
		
		CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
		String isKpc = isKpcInfo.getAdd_info1();
		
		//조직도 조회
		CommonUtil.getCreateOrgTree(request, comService, orgCode, isUser, empno, isKpc);
		
		/**
		 * 검색조건 셋팅
		 */
		if(searchVO.getBegin_date().equals("")){
			String begin_Date = DateUtil.getDateAdd(Calendar.MONTH, -1);
			searchVO.setBegin_date(String.format("%s-01", begin_Date.substring(0, 7)));
		}
		if(searchVO.getEnd_date().equals("")){
			String end_Date = DateUtil.getDateAdd(Calendar.MONTH, -1);
			searchVO.setEnd_date(String.format("%s-%s", end_Date.substring(0, 7), DateUtil.getLastDayOfMonth(end_Date)));
		}
		searchVO.setAuth(auth);
		searchVO.setRequestType(requestType);
		searchVO.setOrgCode(orgCode);
		
		ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
		searchVO.setOrgName(orgInfo.getOrgName());
		searchVO.setSubCount(orgInfo.getSubCount());
		searchVO.setIsSubOrgCount(orgInfo.getSubCount() > 0 ? "Y" : "N");
		
		CommonUtil.topMenuToString(request, "monthreport", comService);
		CommonUtil.reportLeftMenuToString(request, LeftMenuInfo.REPORT_MONTH);	
		
		return "/report/monthSheetStar";
	}
	/**
	 * 월간 보고서 검색결과 Ajax
	 * @param request
	 * @param interval
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/getmonthreportcontents.do")
	public void getmonthreportcontents(HttpServletRequest request
				, HttpServletResponse response
				, @ModelAttribute("searchVO") ReportSearchVO searchVO) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date bdate = fm.parse(searchVO.getBegin_date());
			Date edate = fm.parse(searchVO.getEnd_date());
			if(edate.getTime() - bdate.getTime() < 0){
				throw new Exception("시작일은 종료일 보다 클 수 없습니다.");
			}
			//개인 ? 개인점수 조회 : 하위조직 존재여부 ? 하위조직 점수 조회 : 부서명 점수 조회 
			searchVO.setOrgCode(searchVO.getOrg_upper());
			//날짜 리스트 조회
			List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
			
			StringBuffer tableBody = new StringBuffer();
			StringBuffer totalscoreBody = new StringBuffer();
			
			tableBody.append("<br><br>");
			tableBody.append("<div class='list_title'><h3>기간별 발생 건수 및 점수</h3></div>");
			tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
			tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\" style='width:%spx;' cellpadding=0 cellspacing=0>", 200 + 100 + (100 * dateList.size())));
			/*
			tableBody.append("<colgroup>");
			tableBody.append("	<col style=\"width:200px;\">");
			tableBody.append("	<col style=\"width:150px;\">");
			for(ReportSearchVO dateInfo:dateList){
				tableBody.append("	<col style=\"width:150px;\">");
			}
			tableBody.append("</colgroup>");
			*/
			tableBody.append("<tr>");
			tableBody.append("	<th style='width:200px;'>조직명(성명)</th>");
			tableBody.append("	<th style='width:150px;border-left:solid 1px #CDCDCD;' >누적평균</th>");
			for(ReportSearchVO dateInfo:dateList){
				tableBody.append(String.format("<th style='width:150px;border-left:solid 1px #CDCDCD;' >%s</th>", dateInfo.getBegin_date()));
			}
			tableBody.append("</tr>");
			
			if(searchVO.getAuth().equals("3")){
				ReportSearchVO dataSearchVO = new ReportSearchVO();
				dataSearchVO.setBegin_date(searchVO.getBegin_date());
				dataSearchVO.setEnd_date(searchVO.getEnd_date());
				//로그인 유저
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				searchVO.setEmp_no(loginInfo.getUserid());
				
				OrgResultInfoVO totalScoreInfo = reportService.getMonthReportUserTotalScoreInfo(searchVO);
				totalscoreBody.append("<div class='list_title'><h3>기간별 진단 전체 건수 및 평균 점수</h3></div>");
				totalscoreBody.append("<div class=\"sch_block3 title1_1\">");
				totalscoreBody.append(String.format("<li><p>진단 전체 건수 :</p> %s 건</li>", totalScoreInfo == null ? 0 : totalScoreInfo.getTot_count()));
				totalscoreBody.append("<br><br>");
				totalscoreBody.append(String.format("<li><p>진단 전체 평균 :</p> %s 점</li>", totalScoreInfo == null ? 0 : totalScoreInfo.getAvg()));
				totalscoreBody.append("</div>");
				
				List<OrgResultInfoVO> userResult = reportService.getMonthReportOrgUserListInfo(searchVO);
				if(userResult.size() > 0){
					for(OrgResultInfoVO user:userResult){
						tableBody.append("<tr>");
						tableBody.append(String.format("<td style='width:200px;'>%s</td>",user.getOrg_nm()));
						tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", user.getAvg().equals("-") ? "-" : String.format("%s건 / %s점", user.getTot_count(), user.getAvg()) ));
						dataSearchVO.setOrgCode(user.getOrg_code());
						List<OrgResultInfoVO> userResultData = reportService.getMonthReportOrgUserListDataInfo(dataSearchVO);
						for(OrgResultInfoVO userData:userResultData){
							//DATA Insert....
							tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", userData.getAvg().equals("-") ? "-" : String.format("%s건 / %s점", userData.getTot_count(), userData.getAvg()) ));
						}
						tableBody.append("</tr>");
					}
				}else{
					tableBody.append("<tr style='height:200px;'>");
					tableBody.append(String.format("<td colspan='%s' >%s</td>", 3 + (dateList.size() * 2), "검색결과가 없습니다."));
					tableBody.append("</tr>");
				}
				
			}else{
				//대무자/관리자/팀 선택 
				ReportSearchVO dataSearchVO = new ReportSearchVO();
				dataSearchVO.setBegin_date(searchVO.getBegin_date());
				dataSearchVO.setEnd_date(searchVO.getEnd_date());
				
				ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
				if(orgInfo.getSubCount() > 0){
					
					OrgResultInfoVO totalScoreInfo = reportService.getMonthReportOrgTotalScoreInfo(searchVO);
					totalscoreBody.append("<div class='list_title'><h3>기간별 진단 전체 건수 및 평균 점수</h3></div>");
					totalscoreBody.append("<div class=\"sch_block3 title1_1\">");
					totalscoreBody.append(String.format("<li><p>진단 전체 건수 :</p> %s 건</li>", totalScoreInfo == null ? 0 : totalScoreInfo.getTot_count()));
					totalscoreBody.append("<br><br>");
					totalscoreBody.append(String.format("<li><p>진단 전체 평균 :</p> %s 점</li>", totalScoreInfo == null ? 0 : totalScoreInfo.getAvg()));
					totalscoreBody.append("</div>");
					
					//하위 조직리스트 존재 할 경우
					List<OrgResultInfoVO> orgResult = reportService.getMonthReportOrgListInfo(searchVO);
					if(orgResult.size() > 0){
						for(OrgResultInfoVO orginfo:orgResult){
							tableBody.append("<tr>");
							tableBody.append(String.format("<td style='width:200px;'>%s</td>",orginfo.getOrg_nm()));
							tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", orginfo.getAvg().equals("-") ? "-" : String.format("%s건 / %s점", orginfo.getTot_count(), orginfo.getAvg())));
							dataSearchVO.setOrgCode(orginfo.getOrg_code());
							List<OrgResultInfoVO> orgResultData = reportService.getMonthReportOrgListDataInfo(dataSearchVO);
							for(OrgResultInfoVO orgData:orgResultData){
								//Data Insert...
								tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", orgData.getAvg().equals("-") ? "-" : String.format("%s건 / %s점",  orgData.getTot_count(), orgData.getAvg())));
							}
							tableBody.append("</tr>");
						}
					}else{
						tableBody.append("<tr style='height:200px;'>");
						tableBody.append(String.format("<td colspan='%s' >%s</td>", 3 + (dateList.size() * 2), "검색결과가 없습니다."));
						tableBody.append("</tr>");
					}
					
				}else{
					//하위조직 리스트 가 없는 경우 -> 조직원 정보 리스트 조회
					List<OrgResultInfoVO> orgResult = reportService.getMonthReportOrgUserListInfo(searchVO);
					
					OrgResultInfoVO totalScoreInfo = reportService.getMonthReportSubOrgTotalScoreInfo(searchVO);
					totalscoreBody.append("<div class='list_title'><h3>기간별 진단 전체 건수 및 평균 점수</h3></div>");
					totalscoreBody.append("<div class=\"sch_block3 title1_1\">");
					totalscoreBody.append(String.format("<li><p>진단 전체 건수 :</p> %s 건</li>", orgResult.size() > 0 ? totalScoreInfo.getTot_count() : "-"));
					totalscoreBody.append("<br><br>");
					totalscoreBody.append(String.format("<li><p>진단 전체 평균 :</p> %s 점</li>", orgResult.size() > 0 ? totalScoreInfo.getAvg() : "-"));
					totalscoreBody.append("</div>");
					
					if(orgResult.size() > 0){
						for(OrgResultInfoVO orginfo:orgResult){
							tableBody.append("<tr>");
							tableBody.append(String.format("<td style='width:200px;'>%s</td>",orginfo.getOrg_nm()));
							tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", orginfo.getAvg().equals("-") ? "-" : String.format("%s건 / %s점", orginfo.getTot_count(), orginfo.getAvg())));
							dataSearchVO.setOrgCode(orginfo.getOrg_code());
							List<OrgResultInfoVO> orgResultData = reportService.getMonthReportOrgUserListDataInfo(dataSearchVO);
							for(OrgResultInfoVO orgData:orgResultData){
								//Data Insert...
								tableBody.append(String.format("<td style='width:75px;border-left:solid 1px #eeeeee;'>%s</td>", orgData.getAvg().equals("-") ? "-" : String.format("%s건 / %s점",  orgData.getTot_count(), orgData.getAvg())));
							}
							tableBody.append("</tr>");
						}
					}else{
						tableBody.append("<tr style='height:200px;'>");
						tableBody.append(String.format("<td colspan='%s' >%s</td>", 3 + (dateList.size() * 2), "검색결과가 없습니다."));
						tableBody.append("</tr>");
					}
					
				}
			}
			tableBody.append("</table></div>");
			
			
			hMap.put("tableBody", totalscoreBody.toString() +  tableBody.toString());
			isOk = true;
			
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();	
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}

		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));

	}
	/**
	 * 월간보고서 Excel 출력
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/reportMonthexportExcel.do")
	public void reportMonthexportExcel(HttpServletRequest request
								, @ModelAttribute("searchVO") ReportSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
		String fileName = String.format("기간별보고서_%s_%s", searchVO.getBegin_date(), searchVO.getEnd_date());
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("기간별보고서");
		excelVO.setTitle(String.format("기간별 보고서 [ %s ~ %s ]", searchVO.getBegin_date(), searchVO.getEnd_date()));
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		List<String> rowData = CommonUtil.SplitToString(searchVO.getContent_body1(), ",");
		List<String> head = new ArrayList<String>();
		List<String> colData = CommonUtil.SplitToString(rowData.get(0), "\\|");
		for(int i=0 ; i < colData.size(); i++){
			head.add(colData.get(i));
		}
		excelVO.setHead(head);
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		for(int i=1 ; i < rowData.size() ; i++){
			List<String> columnData = CommonUtil.SplitToString(rowData.get(i), "\\|");
			EgovMap map = new EgovMap();
			for(int j=0 ; j < columnData.size(); j++){
				map.put(head.get(j), columnData.get(j));
			}
			excellist.add(map);
		}
		
		excelVO.setAutoSize(true);
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
		
		/*
		List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
		
		List<String> rowData = CommonUtil.SplitToString(searchVO.getContent_body1(), ",");
		List<String> head = new ArrayList<String>();
		head.add("조직명(성명)");
		head.add("누적평균\r\n건수 | 평균");
		head.add("누적평균\r\n건수 | 평균");
		for(ReportSearchVO dateInfo:dateList){
			head.add(String.format("%s\r\n건수 | 평균",dateInfo.getBegin_date()));
			head.add(String.format("%s\r\n건수 | 평균",dateInfo.getBegin_date()));
		}
		
		//List<String> colData = CommonUtil.SplitToString(rowData.get(1), "\\|");
		//for(int i=1 ; i < colData.size(); i++){
		//	head.add("col" + i);
		//}
		excelVO.setHead(head);
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		for(int i=1 ; i < rowData.size() ; i++){
			//if(rowData.get(i).trim().length() <= 0){
			//	continue;
			//}
			List<String> columnData = CommonUtil.SplitToString(rowData.get(i), "\\|");
			EgovMap map = new EgovMap();
			for(int j=0 ; j < columnData.size(); j++){
				System.out.println();
				map.put("column" +j, columnData.get(j));
			}
			excellist.add(map);
		}
		//excelVO.setHeadMergeEqualValue(true);
		excelVO.setAutoSize(true);
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
		*/
		}catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * 진단점수 평균 및 대분류 항목별 건수 평균
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String personalPolcount(DashboardSearchVO searchVO) throws Exception{
		/**
		 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
		 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
		 */	
		String resultListPersonal = "";
		int avgScore = 0;
		int sumScore = 0;
		String tmpScore = "";
		
		StringBuffer str = new StringBuffer();
		List<EgovMap> rstListPer = reportService.getPersonalPolcount(searchVO);
		int polCnt = 0;
		for(EgovMap pow:rstListPer){
			
			resultListPersonal += "<p>";
			resultListPersonal += pow.get("diagdesc");
			resultListPersonal += " : </p>";
			resultListPersonal += pow.get("count");
			resultListPersonal += " / " + pow.get("score") + "점&nbsp;";
			
			//2019-01-07 수정
			tmpScore = (String) pow.get("score").toString();
			if(!tmpScore.equals("-")){
				polCnt++;
				sumScore += Integer.parseInt(tmpScore);
			}

		}
		
		if(rstListPer.size() > 0 && polCnt >0){
			//2019-01-07 수정
			//avgScore = sumScore / rstListPer.size();
			avgScore = sumScore / polCnt;
		}
		
		
		//str.append("<br><br>");		
		str.append("<div class='list_title title1'> ※진단항목 건수/점수</div>");					

    	str.append("<div class=\"sch_block3 title1_1\">");
		str.append("<li>");
		str.append("<p>진단 점수 (평균) : </p>");
		if(avgScore > 0){
		str.append(avgScore + "점&nbsp;");
		}else{
			str.append("-점&nbsp;");
		}
		str.append("</li>");
		str.append("<br><br>");
		str.append("<li>");
		str.append(resultListPersonal);
		str.append("</li>");
    	str.append("</div>");
		return str.toString();
	}
	
	private String personalPolcountExportExcel(DashboardSearchVO searchVO) throws Exception{
		/**
		 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
		 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
		 */	
		String resultListPersonal = "";
		int avgScore = 0;
		int sumScore = 0;
		String tmpScore = "";
		int polCnt = 0;

		StringBuffer str = new StringBuffer();
		List<EgovMap> rstListPer = reportService.getPersonalPolcount(searchVO);
		
		for(EgovMap pow:rstListPer){
			
			resultListPersonal += "  ";
			resultListPersonal += pow.get("diagdesc");
			resultListPersonal += " : ";
			resultListPersonal += pow.get("count");
			resultListPersonal += " / " + pow.get("score") + "점&nbsp;";
			
			tmpScore = (String) pow.get("score").toString();
			if(!tmpScore.equals("-")){
				sumScore += Integer.parseInt(tmpScore);
			}

		}
			
		if(rstListPer.size() > 0 && polCnt >0){
			//2019-01-07 수정
			//avgScore = sumScore / rstListPer.size();
			avgScore = sumScore / polCnt;
		}

		str.append("진단 점수 (평균) :");
		if(avgScore > 0){
			str.append(avgScore + "점&nbsp;");
		}else{
			str.append("-점&nbsp;");
		}
		

		str.append(resultListPersonal);

		return str.toString();
	}	
	
	/**
	 * 진단점수 및 진단항목별 전체건수/점수
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	private String orgPolTotcountAvg(DashboardSearchVO searchVO) throws Exception{

		String resultListOrg = "";
		int avgScore = 0;
		int sumScore = 0;
		String tmpScore = "";
		
		StringBuffer str = new StringBuffer();
		List<EgovMap> rstList = reportService.getOrgPolTotcountAvg(searchVO);
		
		for(EgovMap pow:rstList){
			
			resultListOrg += "<p>";
			resultListOrg += pow.get("diagdesc");
			resultListOrg += " : </p>";
			resultListOrg += pow.get("count");
			resultListOrg += " / " + pow.get("avg") + "점&nbsp;";
			
			tmpScore = (String) pow.get("avg").toString();
			if(!tmpScore.equals("-")){
				sumScore += Integer.parseInt(tmpScore);
			}

		}
		
		avgScore = reportService.getOrgPolAvg(searchVO);
		//평균
		/*if(rstList.size() > 0){
			avgScore = sumScore / rstList.size();
		}*/
		
		
		//str.append("<br><br>");		
		str.append(String.format("<div class='%s'>※ %s 진단항목 건수/점수</div>"
				, searchVO.getBuseoType().equals("Y") ? "buseotitle1" : "title1"
				, searchVO.getBuseoType().equals("Y") ? "부서" : "조직"));		

    	str.append(String.format("<div class=\"sch_block %s\">"
    			, searchVO.getBuseoType().equals("Y") ? "buseotitle1_1" : "title1_1"));
		str.append("<li>");
		str.append("<p>진단 점수 (평균) : </p>");
		str.append(avgScore == -999 ? "- 점&nbsp;" :avgScore  + "점&nbsp;");
		str.append("</li>");
		str.append("<br><br>");
		str.append("<li>");
		str.append(resultListOrg);
		str.append("</li>");
    	str.append("</div>");

		return str.toString();
	}
	
	private String orgPolTotcountAvgExportExcel(DashboardSearchVO searchVO) throws Exception{

		String resultListOrg = "";
		int avgScore = 0;
		int sumScore = 0;
		String tmpScore = "";
		
		StringBuffer str = new StringBuffer();
		List<EgovMap> rstList = reportService.getOrgPolTotcountAvg(searchVO);
		
		for(EgovMap pow:rstList){
			
			resultListOrg += " ";
			resultListOrg += pow.get("diagdesc");
			resultListOrg += " : ";
			resultListOrg += pow.get("count");
			resultListOrg += " / " + pow.get("avg") + "점&nbsp;";
			
			tmpScore = (String) pow.get("avg").toString();
			if(!tmpScore.equals("-")){
				sumScore += Integer.parseInt(tmpScore);
			}

		}
		avgScore = reportService.getOrgPolAvg(searchVO);
		//avgScore = sumScore / rstList.size();
		
		str.append("진단 점수 (평균) :");
		str.append(avgScore + "점&nbsp;");
		
		str.append(resultListOrg);

		return str.toString();
	}		
	
	/**
	 * 지수정책항목별 발생건수 및 점수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	private String perPolItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception{
		
		StringBuffer str = new StringBuffer();
		
		List<EgovMap> rstPerPolCount = reportService.getPerPolItemCountScoreStatus(searchVO);
		
		str.append("<br><br>");		
		str.append("<div class='list_title title2'> ※ 지수정책항목별 발생건수 및 점수</div>");
		str.append("<table border='0' class=\"tbl_list1 contents1\" cellpadding=0 cellspacing=0>");
		
		str.append(""
				+ "     <colgroup>"
				+ "			<col style=\"width:10%\">"
				+ "			<col style=\"width:20%\">"
				+ "			<col style=\"width:15%\">"
				//+ "			<col style=\"width:20%\">"
				+ "			<col style=\"width:*\">"
				//+ "			<col style=\"width:7\">"
				+ "			<col style=\"width:15%\">"
				+ "			<col style=\"width:20%\">"
				+ "		</colgroup>"
				+ "		<tr>"
				+ "			<th>날짜</th>"
				+ "			<th>조직</th>"
				+ "			<th>사번/이름</th>"
				//+ "			<th>MAC/IP</th>"
				+ "			<th>지수화정책</th>"				
				//+ "			<th>전체건수</th>"
				+ "			<th>점수</th>"
				+ "			<th>진단결과</th>"
				+ "		</tr>"
				+ "		<tbody class='list_contents_body'>"
				);	
		
		for(EgovMap row:rstPerPolCount){
			str.append("<tr>");
			str.append("<td>");
			str.append(row.get("idxrgdtdate"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("orgnm"));
			str.append("</td>");			
			
			str.append("<td>");
			str.append(row.get("empno"));
			str.append(" / ");			
			str.append(row.get("empnm"));
			str.append("</td>");

			str.append("<td>");
			str.append(row.get("descname"));
			str.append("</td>");
			
			/*str.append("<td>");
			str.append(row.get("mac"));
			str.append("<br>");
			str.append(row.get("ip"));
			str.append("</td>");
			
			
			
			str.append("<td>");
			str.append(row.get("count"));
			str.append("</td>");*/
			
			str.append("<td>");
			str.append(row.get("score"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("idxstatus"));
			str.append("</td>");
			str.append("</tr>");
		}
		str.append("</tbody>");
		str.append("</table>");
		return str.toString();
	}	
	
	/**
	 * 사용자별=팀원별 전체건수 평균 진단결과
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	private String perCountScoreStatus(DashboardSearchVO searchVO) throws Exception{
		StringBuffer str = new StringBuffer();
		
		List<EgovMap> rstPerTeam = reportService.getPerCountScoreStatus(searchVO);
		
		str.append("<br><br>");		
		str.append("<div class='list_title title2'>※ 사용자별 발생건수 및 점수</div>");
		str.append("<table border='0' class=\"tbl_list1 contents1\" cellpadding=0 cellspacing=0>");
		
		str.append(""
				+ "     <colgroup>"
				+ "			<col style=\"width:15%\">"
				+ "			<col style=\"width:20%\">"
				+ "			<col style=\"width:15%\">"
				//+ "			<col style=\"width:*\">"
				+ "			<col style=\"width:15%\">"
				+ "			<col style=\"width:15%\">"
				+ "			<col style=\"width:20%\">"
				+ "		</colgroup>"
				+ "		<tr>"
				+ "			<th>날짜</th>"
				+ "			<th>조직</th>"
				+ "			<th>사번</th>"
				//+ "			<th>MAC/IP</th>"
				+ "			<th>성명</th>"
				+ "			<th>점수</th>"
				+ "			<th>진단결과</th>"
				+ "		</tr>"
				+ "		<tbody class='list_contents_body'>"
				); 		
		
		for(EgovMap row:rstPerTeam){
			str.append("<tr>");
			str.append("<td>");
			str.append(row.get("regdate"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("orgnm"));
			str.append("</td>");			
			
			str.append("<td>");
			str.append(row.get("empno"));
			str.append("</td>");

			/*str.append("<td>");
			str.append(row.get("mac"));
			str.append("<br>");
			str.append(row.get("ip"));
			str.append("</td>");
			*/
			str.append("<td>");
			str.append(row.get("empnm"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("score"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("idxstatus"));
			str.append("</td>");
			str.append("</tr>");
		}
		str.append("</tbody>");
		str.append("</table>");		
		
		return str.toString();
	}
	
	/**
	 * 조직별 발생건수 및 점수, 진단상태
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String orgPolItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception{
		StringBuffer str = new StringBuffer();

		/**
		 * 조직별 발생건수 및 점수, 진단상태
		 * map <!-- 조직별 발생건수 및 점수 => 조직의 지수화정책 대분류 항목별(A01, B01, D01)  전체건수, 평균, 진단상태 -->
		 */
		List<EgovMap> resultList = reportService.getOrgPolItemCountScoreStatus(searchVO);


		str.append("<br><br>");		
		str.append(String.format("<div class='%s'>※ 조직별 발생건수 및 점수</div>"
				, searchVO.getBuseoType().equals("Y") ? "buseotitle2" : "title2" ));
		str.append(String.format("<table border='0' class=\"tbl_list1 %s\" cellpadding=0 cellspacing=0>"
				, searchVO.getBuseoType().equals("Y") ? "buseocontents1" : "contents1"));
		if(searchVO.getBuseoType().equals("Y")){
			str.append(""
					+ "     <colgroup>"
					+ "			<col style=\"width:10%\">"
					+ "			<col style=\"width:*\">"
					+ "			<col style=\"width:20%\">"
					+ "			<col style=\"width:20%\">"
					+ "			<col style=\"width:20%\">"
					//+ "			<col style=\"width:10%\">"
					+ "		</colgroup>"
					+ "		<tr>"
					+ "			<th>날짜</th>"
					+ "			<th>조직</th>"
					+ "			<th>부서진단</th>"
					+ "			<th>전체건수</th>"
					+ "			<th>점수</th>"
					//+ "			<th>진단결과</th>"
					+ "		</tr>"
					+ "		<tbody class='list_contents_body'>"
					);	
			
			for(EgovMap row:resultList){
				str.append("<tr>");
				str.append("<td>");
				str.append(row.get("sumrgdt"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(row.get("orgnm"));
				str.append("</td>");			
				
				str.append("<td>");
				str.append(row.get("diagdesc1"));
				str.append("</td>");
			
				str.append("<td>");
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(row.get("avg"));
				str.append("</td>");
				
				/*str.append("<td>");
				str.append(row.get("polstat"));
				str.append("</td>");
				str.append("</tr>");*/
			}
		}else{
			List<EgovMap> policyScoreList = reportService.getOrgPolItemCountScoreStatusFroPolicyList(searchVO);
			List<EgovMap> distinctPolicyList = reportService.getOrgPolItemCountScoreStatusFroDistinctPolicyList(searchVO);
			str.append(""
					
					+ "		<tr>"
					+ "			<th>날짜</th>"
					+ "			<th>조직</th>");
			for(EgovMap row:distinctPolicyList){
				str.append(String.format("<th>%s</th>", row.get("diagdesc")));			
			}
					/*+ "			<th>PC 취약성 진단</th>"
					+ "			<th>악성코드 감염진단</th>"
					+ "			<th>개인 수준 진단</th>"	
					+ "			<th>PC 유해  S/W 진단</th>"	*/			
			str.append("			<th>전체건수</th>"
					+ "			<th>점수</th>"
					+ "			<th>진단결과</th>"
					+ "		</tr>"
					+ "		<tbody class='list_contents_body'>"
					);	
			
			for(EgovMap row:resultList){
				str.append("<tr>");
				str.append("<td>");
				str.append(row.get("sumrgdt"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(row.get("orgnm"));
				str.append("</td>");			
			
				for(EgovMap row_pol:distinctPolicyList){
					if(policyScoreList.size() <=0){
						str.append("<td>-</td>");
					}else{
						str.append(String.format("<td>%s</td>", getPolicySumCount(policyScoreList, row.get("orgcode").toString(), row_pol.get("diagmajrcode").toString())));
					}
					
								
				}	
				/*str.append("<td>");
				str.append(row.get("diagdesc1"));
				str.append("</td>");

				str.append("<td>");
				str.append(row.get("diagdesc2"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(row.get("diagdesc3"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(row.get("diagdesc5"));
				str.append("</td>");			
				*/
				str.append("<td>");
				str.append(Integer.parseInt(row.get("count").toString()) ==-999 ? "-" :row.get("count"));
				str.append("</td>");
				
				str.append("<td>");
				str.append(Integer.parseInt(row.get("avg").toString()) ==-999 ? "-" : row.get("avg") );
				str.append("</td>");
				
				str.append("<td>");
				str.append(Integer.parseInt(row.get("avg").toString()) ==-999 ? "-" : row.get("polstat") );
				str.append("</td>");
				str.append("</tr>");
			}
		}
		
		str.append("</tbody>");
		str.append("</table>");		
		
		return str.toString();
	}
	
	private String getPolicySumCount(List<EgovMap> policyScoreList, String orgcode, String diagmajrcode){
		int cnt = 0;
		int matchCnt = 0;
		for(EgovMap row:policyScoreList){
			//System.out.println(row.get("orgcode") + "][" + row.get("diagmajrcode") + "][" + row.get("count"));
			if(row.get("orgcode").equals(orgcode) && row.get("diagmajrcode").equals(diagmajrcode)){
				cnt = cnt + Integer.parseInt(row.get("count").toString());
				matchCnt++;
			}
		}
		
		String resultCnt = "";
		
		
		if(matchCnt == 0){
			resultCnt = "-"; 
		}else{
			resultCnt = String.valueOf(cnt);
		}
		
		return resultCnt;
	}
	
	private String getPolicySumCount(List<EgovMap> policyScoreList, String orgcode, String diagmajrcode, String indStr, String column){
		String retvalue = "-";
		for(EgovMap row:policyScoreList){
			if(row.get("orgcode").equals(orgcode) && row.get(indStr).equals(diagmajrcode)){
				retvalue = row.get(column).toString();
				break;
			}
		}
		return retvalue;
	}
	private String getPolicyUserSumCount(List<EgovMap> policyScoreList, String empno, String diagmajrcode, String indStr, String column){
		String retvalue = "-";
		for(EgovMap row:policyScoreList){
			if(row.get("empno").equals(empno) && row.get(indStr).equals(diagmajrcode)){
				retvalue = row.get(column).toString();
				break;
			}
		}
		return retvalue;
	}
	
	/**
	 * 정책별 발생건수 및 점수, 진단상태
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	private String orgDiagItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception{
		
		StringBuffer str = new StringBuffer();

		/**
		 * 조직 :: 정책별 발생건수 및 점수, 진단상태
		 * <!-- [1]조직 정책별 발생건수 및 점수, 진단상태 => 진단날짜, 진단명, 점검명, 정책명, 건수, 점수, 진단결과 -->
		 */
		List<EgovMap> resultList = reportService.getOrgDiagItemCountScoreStatus(searchVO);


		str.append("<br><br>");		
		str.append(String.format("<div class='%s'>※ 정책별 발생건수 및 점수</div>"
				, searchVO.getBuseoType().equals("Y") ? "buseotitle3" : "title3" ));
		str.append(String.format("<table border='0' class=\"tbl_list1 %s\" cellpadding=0 cellspacing=0>"
				, searchVO.getBuseoType().equals("Y") ? "buseocontents2" : "contents2"));
		
		str.append(""
				+ "     <colgroup>"
				+ "			<col style=\"width:10%\">"
				+ "			<col style=\"width:20%\">"
				+ "			<col style=\"width:20%\">"
				+ "			<col style=\"width:*\">"
				+ "			<col style=\"width:10%\">"
				+ "			<col style=\"width:10%\">");
		if(searchVO.getBuseoType().equals("N")){
			str.append("		<col style=\"width:10%\">");
		}
		str.append("		</colgroup>"
				+ "		<tr>"
				+ "			<th>날짜</th>"
				+ "			<th>진단명</th>"
				+ "			<th>점검명</th>"
				+ "			<th>정책명</th>"				
				+ "			<th>건수</th>"
				+ "			<th>점수</th>");
		if(searchVO.getBuseoType().equals("N")){
			str.append("		<th>진단결과</th>");
		}
		str.append("	</tr>"
				+ "		<tbody class='list_contents_body'>"
				);
		
		for(EgovMap row:resultList){
			str.append("<tr>");
			str.append("<td>");
			str.append(row.get("sumrgdtdate"));
			str.append("</td>");			
			
			str.append("<td>");
			str.append(row.get("majrdiagdesc"));
			str.append("</td>");

			str.append("<td>");
			str.append(row.get("minrdiagdesc"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("secpoldesc"));
			str.append("</td>");			
			
			str.append("<td>");
			str.append(row.get("totcount"));
			str.append("</td>");
			
			str.append("<td>");
			str.append(row.get("avg"));
			str.append("</td>");
			if(searchVO.getBuseoType().equals("N")){	
				str.append("<td>");
				str.append(row.get("idxstatus"));
				str.append("</td>");
				str.append("</tr>");
			}
		}
		str.append("</tbody>");
		str.append("</table>");			
		
		return str.toString();
	}
	
	@RequestMapping(value="/report/polReportexportexcelex.do")
	public void polReportexportexcelex(HttpServletRequest request
								, String begin_date /*조회날짜*/
								, String tit1 /*일반진단 첫번째 타이틀*/
								, String tit1_1 /*일반진단 Summary 내용*/
								, String tit2 /*일반진단 두번재 타이틀*/
								, String conts1 /*일반진단 첫번째 리스트 내용*/
								, String tit3 /*일반진단 세번째 타이틀*/
								, String conts2 /*일반진단 두번째 리스트 내용*/
								, String btit1 /*부서진단 첫번째 타이틀*/
								, String btit1_1 /*부서진단 Summary 내용*/
								, String btit2 /*부서진단 두번째 타이틀*/
								, String bconts1 /*부서진단 첫번째 리스트 내용*/
								, String btit3 /*부서진단 세번째 타이틀*/
								, String bconts2 /*부서진단 두번재 리스트 내용*/
								, HttpServletResponse response) throws Exception {
		
		String fileName = String.format("진단보고서_%s", begin_date);
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("진단보고서 "+ begin_date);
		excelVO.setTitle("진단보고서");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		List<ExcelInitVO> contentList = new ArrayList<ExcelInitVO>();
		contentList.add(getExcelContents(tit2, conts1, tit1, tit1_1, "N"));
		contentList.add(getExcelContents(tit3, conts2, tit2, tit1_1, "N"));
		//contentList.add(getExcelContents(btit2, bconts1, btit1, btit1_1, "B"));
		//contentList.add(getExcelContents(btit3, bconts2, btit1, btit1_1, "B"));
		
		List<String> maxhead = new ArrayList<String>();
		for(ExcelInitVO rows:contentList){
			if(maxhead.size() < rows.getHead().size()){
				maxhead = rows.getHead();
			}
		}
		excelVO.setHead(maxhead);
		
		ExcelUtilMulti.xssExcelDownEx(response, excelVO, contentList);
	}
	
	private ExcelInitVO getExcelContents(String title, String contents, String headline, String headlinestr, String contentType){
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setSubTitleAA(headline);
		excelVO.setTitleValue(headlinestr);
		excelVO.setType(contentType);
		excelVO.setTitle(title);
		
		List<String> rowData = CommonUtil.SplitToString(contents, ",");
		List<String> headList = new ArrayList<String>();
		
		List<String> colData = CommonUtil.SplitToString(rowData.get(0), "\\|");
		for(int i=0 ; i < colData.size(); i++){
			headList.add(colData.get(i));
		}
		excelVO.setHead(headList);
		
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		for(int i=1 ; i < rowData.size() ; i++){
			List<String> columnData = CommonUtil.SplitToString(rowData.get(i), "\\|");
			EgovMap hashmap = new EgovMap();
			for(int j=0 ; j < columnData.size(); j++){
				hashmap.put(headList.get(j), columnData.get(j));
			}
			excellist.add(hashmap);
		}
		excelVO.setContents(excellist);
		
		return excelVO;
	}
	/**
	 * 일별 보고서 엑셀저장
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/polReportexportexcel.do")
	public void diagReportexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") DashboardSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "";
		System.out.println("------------------------------------------------->>>>>>1");		
		fileName = String.format("%s_%s_진단보고서", searchVO.getBegin_date(), searchVO.getOrg_upper());
		
		System.out.println("::"+ searchVO.getBegin_date());
		if(!searchVO.getOrg_upper().equals("")){
			System.out.println("::"+ searchVO.getOrg_upper());
		}
		System.out.println("------------------------------------------------->>>>>>2");	
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("진단보고서 "+ searchVO.getBegin_date());
		excelVO.setTitle("진단보고서");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
 		searchVO.setEnd_date(searchVO.getEnd_date().replace("-",  ""));
 		
 		
 		String rtnStr = "";
 		
 		System.out.println("------------------------------------------------->>>>>>3");	
 		
		String dpflag = ""; // cap:조직, (cap_code:조직코드), super:관리자, personal:개인, replace:대무자
		
 		/**
 		 * 권한별 정보 조회용  
 		 */
		String ktmp = request.getParameter("ktmp") != null && !request.getParameter("ktmp").toString().equals("") ? request.getParameter("ktmp") : "";

		if(ktmp.equals("")){
			ktmp = (String)request.getSession().getAttribute("loginType");
		}
	
		//조직 조회 코드
		String orgUpper = request.getParameter("org_upper") != null && !request.getParameter("org_upper").toString().equals("") ? request.getParameter("org_upper") : "";

		String orgcodeMode = "";
		Boolean orgcodeModeFlag = false;
	
		
		String soc_org_code = ""; // 담당 조직코드
		String org_level = "";	// 조직레벨
		String user_authnm = "";		
		
		/**
		 * 사용자 로그인 정보
		 * auth 값정의 : 관리자/운영자:1 , 대무자(조직장):2, 일반사용자:3
		 * 정책모니터링 조회시 사용하는 인증코드임
		 */
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		String auth = loginInfo.getRole_code();// getUser_auth(request);
		String orgCode = loginInfo.getOrgcode();// getOrg_code(request);		
		String emp_no = loginInfo.getUserid();// getEmp_no(request);
		
		String currOrgName = "";
		
		boolean isUser = auth == "1" ? false : true;		
		
		if(Integer.parseInt(auth) == 1)
		{
			isUser = false;
			auth = "1";
		}
		

		/**
		 * [1] 대무자를 조회
		 */	
		UserManageVO proxyUser = new UserManageVO();
		//해당 조직 조직장 사번 조회
		if(!dpflag.equals("personal")){
			String capNo =dashboardService.getOrgCapNo(dpflag); 
			HashMap<String, String> hmap = new HashMap<String, String>();
			hmap.put("emp_no", emp_no);
			hmap.put("capNo", capNo);
			
			proxyUser = dashboardService.getProxyUser(hmap);
		}
		if (proxyUser != null){
			
			if(!dpflag.equals("personal")){
				emp_no = proxyUser.getEmp_no();  // 대무자이면 emp_no 재설정
			}
			user_authnm = "대무자:"+emp_no;
		}		
		
		/**
		 * [2] 조직장인지 
		 */
		List<OrgGroupVO> orgCapList = comService.getOrgCapInfoList(emp_no);
		if(orgCapList.size() > 0)
		{
			//조직장 
			OrgGroupVO capInfo = orgCapList.get(0);
			isUser = false;
			auth = "2";
			orgCode=capInfo.getOrg_code().toString();
			
			if(!orgCode.equals(dpflag)){
				emp_no = loginInfo.getUserid();
				orgCode = dpflag;
			}
			
			System.out.println("조직장????!!!!");
		}
		
		
		/** [1-1] 담당부서 정보 조회 */
		HashMap<String,Object> rMapUinfo = new HashMap<String,Object>();
		List<UserIdxInfoCurrVO> list_uinfo = new ArrayList<UserIdxInfoCurrVO>();
		rMapUinfo = dashboardService.getUserInfo(emp_no);
		list_uinfo = (List<UserIdxInfoCurrVO>)rMapUinfo.get("list");
					
		if(list_uinfo != null && list_uinfo.size() > 0){
			if(list_uinfo.size() > 1){
				for(int u=0;u<list_uinfo.size();u++){
					System.out.println("#########"+list_uinfo.get(u).getUnder_code() + "(orglevel:" + list_uinfo.get(u).getOrg_level() + ")");
					soc_org_code = list_uinfo.get(u).getUnder_code();
					org_level = list_uinfo.get(u).getOrg_level();
					
					System.out.println("==========1=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
					
					if(orgcodeMode.equals(soc_org_code)){
						orgcodeModeFlag = true;
						System.out.println("==========2=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
						orgCode = orgcodeMode;
					}
				}
			}else{
				if(list_uinfo.get(0).getUnder_code() != null){
					System.out.println("#########" + list_uinfo.get(0).getUnder_code());
					soc_org_code = list_uinfo.get(0).getUnder_code();
					org_level = list_uinfo.get(0).getOrg_level();
				}else{ 
					System.out.println("#########" + "personal");
					
					soc_org_code = "";
					org_level = "";
				}
			}
		}else{
			System.out.println("=====================null");
		}
		
		if(orgcodeModeFlag){
			soc_org_code = orgcodeMode;
		}
		
		System.out.println("soc_org_code:" + soc_org_code + "//" + "org_level:" + org_level);
		
		/** 조직코드 */
		if(dpflag.equals("sa")){
			if(!orgUpper.equals("")){
				orgCode = orgUpper;
			}else{
				orgCode = "000001";
				searchVO.setOrg_upper("000001");
			}				
		}else{
			searchVO.setOrg_upper(soc_org_code); 
			if(!orgUpper.equals("")){
				orgCode = orgUpper;
				searchVO.setOrg_upper(orgUpper);
			}else{
				searchVO.setOrg_upper(soc_org_code);
			}
		}
	
		/** 사번코드 */
		searchVO.setEmp_no(emp_no);	
		
		System.out.println("------------------------------------------------->>>>>>" + searchVO.getBegin_date());
		System.out.println("------------------------------------------------->>>>>>" + searchVO.getEmp_no());
		System.out.println("------------------------------------------------->>>>>>" + searchVO.getOrg_upper());			
		
		if (auth != null){
			switch (auth){
				case "1": 
					user_authnm = "관리자/운영자"; 
					break;
				case "2": 
					user_authnm = "조직장"; 
					break;
				default : 
					user_authnm = "일  반"; 
					break;
			}
		}			
		
		/** parameter : emp_no */
		List<EgovMap> caporgList = dashboardService.getUserInfoCap(searchVO); //조직 정보조회
	    
	    /** parameter : upper_org_code */	    
	    List<EgovMap> orgorgList = dashboardService.getOrgSubList(searchVO); // 하위조직 조회
		
		//조직장이고 하위조직 없으면 팀장
	    
	    if(auth.equals("2")){
		    if(caporgList.size() > 0 && orgorgList.size() < 1){
		    	System.out.println("============팀장");
		    }else if(caporgList.size() > 0 && orgorgList.size() > 0){
		    	System.out.println("============조직장");
		    }
	    }
	    
	    
	
	    System.out.println("===============================================");
	    System.out.println("권한코드::"+auth);
	    System.out.println("권한상태::"+user_authnm);
	    System.out.println("화면모드::"+ktmp);
	    System.out.println("조직코드::"+orgUpper + "||" + dpflag);
	    System.out.println("소속조직코드::"+orgCode);
	    System.out.println("==============================================="); 	
	    
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		
		List<EgovMap> excellistOrg = new ArrayList<EgovMap>();
		
		try{
	    
			if(ktmp.equals("personal")){
		 		excelVO.setSubTitleAA("※ 진단항목 건수/점수");
		 		rtnStr = personalPolcountExportExcel(searchVO); //[1]
		 		excelVO.setExcelListAA(rtnStr);
		 			 			 		
				excelVO.setSubTitleBB("※ 지수정책 항목별 발생건수 및 점수");
				
				List<HashMap<String, Object>> rstOrg = reportService.getPerPolItemCountScoreStatusExportExcel(searchVO); //[3]
				
				System.out.println("------------------------------------------------->>>>>>" + rstOrg.size());
				
				if(rstOrg.size() > 0){
					List<String> head = new ArrayList<String>();
					HashMap<String, Object> logOrg = (HashMap<String, Object>)rstOrg.get(0);
					
					for(Object key:logOrg.keySet())
					{
						head.add(ConvertToColumnName(key.toString()));		
					}
					
					excelVO.setHead(head);
					
					EgovMap mapOrg = new EgovMap();
			
					for(HashMap<?,?> row:rstOrg)
					{
						//EgovMap mapOrg = new EgovMap();
						for(Object key:row.keySet()){
							mapOrg.put(key, row.get(key).toString());
						}
						excellist.add(mapOrg);
					}	
					
					//excellistOrg.add(mapOrg);
					
				}else{
					
					List<String> head = new ArrayList<String>();
					head.add(ConvertToColumnName("idxrgdtdate"));
					head.add(ConvertToColumnName("orgnm"));
					head.add(ConvertToColumnName("empnm"));
					head.add(ConvertToColumnName("mac"));
					head.add(ConvertToColumnName("ip"));
					head.add(ConvertToColumnName("descname"));
					head.add(ConvertToColumnName("count"));
					head.add(ConvertToColumnName("score"));
					head.add(ConvertToColumnName("idxstatus"));
					excelVO.setHead(head);					
				}
		 		
			}else if(ktmp.equals("sa")){
				
		 		excelVO.setSubTitleAA("※ 진단항목 건수/점수");
		 		rtnStr = orgPolTotcountAvgExportExcel(searchVO); //[2]
		 		excelVO.setExcelListAA(rtnStr);
		 					
				/* 하위조직 있는지 */
				System.out.println("============::orgorgList::"+orgorgList.size());
				
				if(orgorgList.size() < 1 ){
					/* 하위조직 없으면 */				
					excelVO.setSubTitleBB("※ 사용자별 발생건수 및 점수");
					
					try{
			 		
				 		List<HashMap<String, Object>> resultList = reportService.getPerCountScoreStatusExportExcel(searchVO);	 //4
						
						if(resultList.size() > 0){
							
							List<String> head = new ArrayList<String>();
							HashMap<String, Object> log = (HashMap<String, Object>)resultList.get(0);
						
							for(Object key:log.keySet())
							{
								head.add(ConvertToColumnName(key.toString()));
							}
							
							excelVO.setHead(head);		
							
							for(HashMap<?,?> row:resultList)
							{
								EgovMap map = new EgovMap();
								for(Object key:row.keySet()){
									map.put(key, row.get(key).toString());
								}
								excellist.add(map);
							}
						}else{
							List<String> head = new ArrayList<String>();
							head.add(ConvertToColumnName("idxrgdtdate"));
							head.add(ConvertToColumnName("orgnm"));
							head.add(ConvertToColumnName("empnm"));
							head.add(ConvertToColumnName("mac"));
							head.add(ConvertToColumnName("ip"));
							head.add(ConvertToColumnName("descname"));
							head.add(ConvertToColumnName("count"));
							head.add(ConvertToColumnName("score"));
							head.add(ConvertToColumnName("idxstatus"));
							excelVO.setHead(head);							
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					/* 하위조직 있으면 */
					excelVO.setSubTitleBB("※ 조직별 발생건수 및 점수");
			 		
			 		List<HashMap<String, Object>> resultList = reportService.getOrgPolItemCountScoreStatusExportExcel(searchVO);	 //5
					
					List<String> head = new ArrayList<String>();
					HashMap<String, Object> log = (HashMap<String, Object>)resultList.get(0);
				
					for(Object key:log.keySet())
					{
						head.add(ConvertToColumnName(key.toString()));
					}
					
					excelVO.setHead(head);		
					
					for(HashMap<?,?> row:resultList)
					{
						EgovMap map = new EgovMap();
						for(Object key:row.keySet()){
							map.put(key, row.get(key).toString());
						}
						excellist.add(map);
					}
				}
				
				excelVO.setSubTitleCC("※ 정책별 발생건수 및 점수");
				
				List<HashMap<String, Object>> rstOrg = reportService.getOrgDiagItemCountScoreStatusExportExcel(searchVO); //6
				
				if(rstOrg.size() > 0 ){
					
					List<String> headOrg = new ArrayList<String>();
					HashMap<String, Object> logOrg = (HashMap<String, Object>)rstOrg.get(0);
					
					for(Object key:logOrg.keySet())
					{
						headOrg.add(ConvertToColumnName(key.toString()));		
					}
					
					excelVO.setHeadOrg(headOrg);
			
					for(HashMap<?,?> row:rstOrg)
					{
						EgovMap mapOrg = new EgovMap();
						for(Object key:row.keySet()){
							mapOrg.put(key, row.get(key).toString());
						}
						excellistOrg.add(mapOrg);
					}
				}else{
					
					List<String> headOrg = new ArrayList<String>();
					headOrg.add(ConvertToColumnName("sumrgdtdate"));
					headOrg.add(ConvertToColumnName("orgnm"));
					headOrg.add(ConvertToColumnName("majrdiagdesc"));
					headOrg.add(ConvertToColumnName("minrdiagdesc"));
					headOrg.add(ConvertToColumnName("secpoldesc"));
					headOrg.add(ConvertToColumnName("totcount"));
					headOrg.add(ConvertToColumnName("avg"));
					headOrg.add(ConvertToColumnName("score"));
					headOrg.add(ConvertToColumnName("idxstatus"));
					excelVO.setHeadOrg(headOrg);				
				}
				
			}else{
				
				/* 하위조직 있는지 */
				System.out.println("============::orgorgList::"+orgorgList.size());
				
		 		excelVO.setSubTitleAA("※ 진단항목 건수/점수");
		 		rtnStr = orgPolTotcountAvgExportExcel(searchVO); //[2]
		 		excelVO.setExcelListAA(rtnStr);			
				
				if(orgorgList.size() < 1 ){
					/* 하위조직 없으면 */
					excelVO.setSubTitleBB("※ 사용자별 발생건수 및 점수");
			 		
			 		List<HashMap<String, Object>> resultList = reportService.getPerCountScoreStatusExportExcel(searchVO);	 //4
					
					List<String> head = new ArrayList<String>();
					HashMap<String, Object> log = (HashMap<String, Object>)resultList.get(0);
				
					for(Object key:log.keySet())
					{
						head.add(ConvertToColumnName(key.toString()));
					}
					
					excelVO.setHead(head);		
					
					for(HashMap<?,?> row:resultList)
					{
						EgovMap map = new EgovMap();
						for(Object key:row.keySet()){
							map.put(key, row.get(key).toString());
						}
						excellist.add(map);
					}
				}else{
					/* 하위조직 있으면 */				
					excelVO.setSubTitleBB("※ 조직별 발생건수 및 점수");
			 		
			 		List<HashMap<String, Object>> resultList = reportService.getOrgPolItemCountScoreStatusExportExcel(searchVO);	 //5
					
					List<String> head = new ArrayList<String>();
					HashMap<String, Object> log = (HashMap<String, Object>)resultList.get(0);
				
					for(Object key:log.keySet())
					{
						head.add(ConvertToColumnName(key.toString()));
					}
					
					excelVO.setHead(head);		
					
					for(HashMap<?,?> row:resultList)
					{
						EgovMap map = new EgovMap();
						for(Object key:row.keySet()){
							map.put(key, row.get(key).toString());
						}
						excellist.add(map);
					}				
				}
				
				excelVO.setSubTitleCC("※ 정책별 발생건수 및 점수");
				
				List<HashMap<String, Object>> rstOrg = reportService.getOrgDiagItemCountScoreStatusExportExcel(searchVO); //6
		
				List<String> headOrg = new ArrayList<String>();
				HashMap<String, Object> logOrg = (HashMap<String, Object>)rstOrg.get(0);
				
				for(Object key:logOrg.keySet())
				{
					headOrg.add(ConvertToColumnName(key.toString()));		
				}
				
				excelVO.setHeadOrg(headOrg);
		
				for(HashMap<?,?> row:rstOrg)
				{
					EgovMap mapOrg = new EgovMap();
					for(Object key:row.keySet()){
						mapOrg.put(key, row.get(key).toString());
					}
					excellistOrg.add(mapOrg);
				}			
				
			}	    
 		
			ExcelUtilMulti.xssExcelDown(response, excelVO, excellist, excellistOrg);
		}catch(Exception e){
			e.printStackTrace();
		}
				
	}
	
	
	/**
	 * 기간별 데이터 추출 보고서
	 * @param request
	 * @param model
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/report/dataSheetStar.do")
	public String dataSheetStar(HttpServletRequest request, 
			ModelMap model, 
			@ModelAttribute("searchVO") ReportSearchVO searchVO) throws Exception{
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		
		String empno = loginInfo.getUserid();// getEmp_no(request); //
		String auth = loginInfo.getRole_code().equals("1") || loginInfo.getRole_code().equals("2") ? "1" : loginInfo.getRole_code(); //getUser_auth(request).equals("3") ? getUser_auth(request) : "1"; 
		String orgCode = loginInfo.getOrgcode();
		String requestType = (String)request.getSession().getAttribute("loginType");

		if(requestType.equals("personal")){
			auth = "3";
		}else if(requestType.equals("sa")){
			auth = "1";
		}else{
			auth = "2";
		}
		
		boolean isUser = auth == "1" ? false : true;		
		if(Integer.parseInt(auth) == 1)
		{
			isUser = false;
			auth = "1";
			orgCode = MajrCodeInfo.RootOrgCode;
		}else if(Integer.parseInt(auth) == 2){
			isUser = false;
			orgCode=requestType;
		}		
		//협력사 지수 여부
		
		HashMap<String, String> codeInfo = new HashMap<String, String>();
		codeInfo.put("majCode", "PIX");
		codeInfo.put("minCode", "02");
		
		
		CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
		String isKpc = isKpcInfo.getAdd_info1();
				
				
		//조직도 조회
		CommonUtil.getCreateOrgTree(request, comService, orgCode, isUser, empno, isKpc);
		
		/**
		 * 검색조건 셋팅
		 */
		if(searchVO.getBegin_date().equals("")){
			String begin_Date = DateUtil.getDateAdd(Calendar.DATE, -8);
			searchVO.setBegin_date(String.format("%s", begin_Date));
		}
		if(searchVO.getEnd_date().equals("")){
			String end_Date = DateUtil.getDateAdd(Calendar.DATE, -1);
			searchVO.setEnd_date(String.format("%s", end_Date));
		}
		searchVO.setAuth(auth);
		searchVO.setRequestType(requestType);
		searchVO.setOrgCode(orgCode);
		
		ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
		searchVO.setOrgName(orgInfo.getOrgName());
		searchVO.setSubCount(orgInfo.getSubCount());
		searchVO.setIsSubOrgCount(orgInfo.getSubCount() > 0 ? "Y" : "N");
		
		//List<EgovMap> distinctPolicyList = reportService.getDataReportItemCountScoreStatusFroDistinctPolicyList(dashSearchVO);
		List<LinkedHashMap<String, Object>> diagItemList = reportService.getDiagItemList();
		model.addAttribute("diagItemList", diagItemList);
		
		CommonUtil.topMenuToString(request, "monthreport", comService);
		CommonUtil.reportLeftMenuToString(request, LeftMenuInfo.REPORT_DATA);	
		
		return "/report/dataSheetStar";
	}
	
	/**
	 * 기간별 데이터 추출 검색결과 Ajax
	 * @param request
	 * @param interval
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/getdatareportcontents.do")
	public void getdatareportcontents(HttpServletRequest request
				, HttpServletResponse response
				, @ModelAttribute("searchVO") ReportSearchVO searchVO) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date bdate = fm.parse(searchVO.getBegin_date());
			Date edate = fm.parse(searchVO.getEnd_date());
			if(edate.getTime() - bdate.getTime() < 0){
				throw new Exception("시작일은 종료일 보다 클 수 없습니다.");
			}
			String[] polCheckList = searchVO.getPolcheckedlist().split(",");
			//개인 ? 개인점수 조회 : 하위조직 존재여부 ? 하위조직 점수 조회 : 부서명 점수 조회 
			searchVO.setOrgCode(searchVO.getOrg_upper());
			//날짜 리스트 조회
			List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
			StringBuffer tableBody = new StringBuffer();
			//조직 정보 조회 하위 조직 존재 여부
			ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
			
			
			for(ReportSearchVO dateSearchVO:dateList){
				//날짜
				
				dateSearchVO.setOrg_code(searchVO.getOrg_upper());
				dateSearchVO.setOrg_upper(searchVO.getOrg_upper());
				dateSearchVO.setEnd_date(dateSearchVO.getBegin_date());
				dateSearchVO.setOrgCode(searchVO.getOrg_upper());
				
				if(orgInfo.getSubCount() > 0){
					//하위 조직 존재시
					//System.out.println(dateSearchVO.getOrg_upper() + "][" + dateSearchVO.getBegin_date() + "][" + dateSearchVO.getEnd_date());
					List<OrgResultInfoVO> orgResultList = reportService.getMonthReportOrgListInfo(dateSearchVO);
					
						//조직 리스트 
					DashboardSearchVO dashSearchVO = new DashboardSearchVO();
					dashSearchVO.setOrg_upper(dateSearchVO.getOrg_upper());
					dashSearchVO.setBegin_date(dateSearchVO.getBegin_date().replace("-", ""));
					//정책별 결과 리스트
					List<EgovMap> policyScoreList = reportService.getDataReportOrgPolItemCountScoreStatusFroPolicyList(dashSearchVO);
					//정책 리스트
					List<EgovMap> distinctPolicyList = reportService.getDataReportItemCountScoreStatusFroDistinctPolicyList(dashSearchVO);
					
					tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
					tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\" style=\"width:%spx;\" cellpadding=0 cellspacing=0 collect_date='%s'>", ((120*5) + (160*polCheckList.length)), dateSearchVO.getBegin_date()));
					
					//border-top:solid 2px #3c7dbe;border-bottom:solid 1px #dcdcdc;
					tableBody.append(""
							+ "		<tr>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>날짜</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>조직</th>");
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								tableBody.append(String.format("<th style='width:160px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' colspan=2>%s</th>", row.get("poldesc")));	
								continue;
							}
						}
								
					}
							
					tableBody.append("	<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>전체건수</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>점수(가중치적용)</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;' rowspan=2>진단결과</th>"
							+ "		</tr>"
					);	
					tableBody.append("<tr>");
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-left:solid 1px #CECAC5;border-right:solid 1px #CECAC5;'>건수</th><th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>점수</th>"));	
								continue;
							}
						}
								
					}
							
					tableBody.append("</tr>"
							+ "		<tbody class='list_contents_body'>"
					);	
					for(OrgResultInfoVO orgResult:orgResultList){
						tableBody.append("<tr>");
						tableBody.append("<td>");
						tableBody.append(dateSearchVO.getBegin_date());
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(orgResult.getOrg_nm());
						tableBody.append("</td>");			
						for(EgovMap row_pol:distinctPolicyList){
							for(String policy:polCheckList){
								if(row_pol.get("polidxid").equals(policy)){
									tableBody.append(String.format("<td>%s</td>", getPolicySumCount(policyScoreList, orgResult.getOrg_code(), row_pol.get("polidxid").toString(), "polidx", "count")));		
									tableBody.append(String.format("<td>%s</td>", getPolicySumCount(policyScoreList, orgResult.getOrg_code(), row_pol.get("polidxid").toString(), "polidx", "score")));	
									continue;
								}
							}
							
						}	
						tableBody.append("<td>");
						tableBody.append(orgResult.getTot_count());
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(orgResult.getAvg());
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(orgResult.getIdxstatus());
						tableBody.append("</td>");
						tableBody.append("</tr>");
					}
					
				}else{
					//하위 조직 미존재시
					DashboardSearchVO dashSearchVO = new DashboardSearchVO();
					dashSearchVO.setOrg_upper(dateSearchVO.getOrg_upper());
					dashSearchVO.setBegin_date(dateSearchVO.getBegin_date().replace("-", ""));
					List<EgovMap> rstPerTeam = reportService.getPerCountScoreStatus(dashSearchVO);
					
					List<EgovMap> policyScoreList = reportService.getTeamPolItemCountScoreStatusFroPolicyList(dashSearchVO);
					List<EgovMap> distinctPolicyList = reportService.getDataReportItemCountScoreStatusFroDistinctPolicyList(dashSearchVO);
					
					tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
					tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\" style=\"width:%spx;\" cellpadding=0 cellspacing=0 collect_date='%s'>", ((120*6) + (160*polCheckList.length)), dateSearchVO.getBegin_date()));
					
					
					tableBody.append(""
							+ "		<tr>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>날짜</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>조직</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>성명</th>");
					
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								tableBody.append(String.format("<th style='width:160px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' colspan=2>%s</th>", row.get("poldesc")));	
								continue;
							}
						}
					}
							
					tableBody.append("	<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>전체건수</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>점수(가중치적용)</th>"
							+ "			<th style='width:120px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>진단결과</th>"
							+ "		</tr>"
					);
					tableBody.append("<tr>");
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-left:solid 1px #CECAC5;border-right:solid 1px #CECAC5;'>건수</th><th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>점수</th>"));	
								continue;
							}
						}
					}
							
					tableBody.append("</tr>"
							+ "<tbody class='list_contents_body'>"
					);	
					for(EgovMap perResult:rstPerTeam){
						tableBody.append("<tr>");
						tableBody.append("<td>");
						tableBody.append(dateSearchVO.getBegin_date());
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(perResult.get("orgnm"));
						tableBody.append("</td>");		
						tableBody.append("<td>");
						tableBody.append(perResult.get("empnm"));
						tableBody.append("</td>");	
						for(EgovMap row_pol:distinctPolicyList){
							for(String policy:polCheckList){
								if(row_pol.get("polidxid").equals(policy)){
									tableBody.append(String.format("<td>%s</td>", getPolicyUserSumCount(policyScoreList, perResult.get("empno").toString(), row_pol.get("polidxid").toString(), "polidx", "count")));		
									tableBody.append(String.format("<td>%s</td>", getPolicyUserSumCount(policyScoreList, perResult.get("empno").toString(), row_pol.get("polidxid").toString(), "polidx", "score")));	
									continue;
								}
							}
								
						}	
						tableBody.append("<td>");
						tableBody.append(perResult.get("count"));
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(perResult.get("score"));
						tableBody.append("</td>");
						tableBody.append("<td>");
						tableBody.append(perResult.get("idxstatus"));
						tableBody.append("</td>");
						tableBody.append("</tr>");
					}
					tableBody.append("</tbody>");
				}
				
				tableBody.append("</table></div>");
				tableBody.append("<div style='height:20px;'></div>");
			}
			
			 
			//tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
			//tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\"  cellpadding=0 cellspacing=0>"));
			/*String bodyString = String.format("%s%s%s"
					, "<div style='border:0px solid blue;width:100%;overflow:auto;'>"
					, String.format("<table border='0' class=\"tbl_list1 contents1\" style='width:%spx;'  cellpadding=0 cellspacing=0>", ((120 * 6) + (polCount * 150)))
					, tableBody.toString());*/
			hMap.put("tableBody", tableBody.toString());
			isOk = true;
			
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();	
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));

	}
	
	private List<ReportSearchResultVO> getPolicyDateList(String policy, List<ReportSearchResultVO> orgResultList){
		List<ReportSearchResultVO> resultlist = new ArrayList<ReportSearchResultVO>();
		for(ReportSearchResultVO resultItem:orgResultList){
			if(resultItem.getPol_idx_id().equals(policy)){
				resultlist.add(resultItem);
			}
		}
		return resultlist;
	}
	
	private ReportSearchResultVO getPolicyDateOrgInfo(String policy, String org_code, String rgdt_date, List<ReportSearchResultVO> resultList){
		ReportSearchResultVO resultinto = null;// new ReportSearchResultVO();
		for(ReportSearchResultVO resultItem:resultList){
			if(resultItem.getPol_idx_id().equals(policy) && resultItem.getOrg_code().equals(org_code) && resultItem.getRgdt_date().equals(rgdt_date)){
				resultinto = resultItem;
			}
		}
		return resultinto;
	}
	
	private ReportSearchResultVO getPolicyDateUserInfo(String policy, String emp_no, String rgdt_date, List<ReportSearchResultVO> resultList){
		ReportSearchResultVO resultinto = null;// new ReportSearchResultVO();
		for(ReportSearchResultVO resultItem:resultList){
			if(resultItem.getPol_idx_id().equals(policy) && resultItem.getEmp_no().equals(emp_no) && resultItem.getRgdt_date().equals(rgdt_date)){
				resultinto = resultItem;
			}
		}
		return resultinto;
	}
	
	private ReportSearchResultVO getPolicyInfo(String policycode, List<ReportSearchResultVO> orgResultList){
		ReportSearchResultVO resultinto = null;
		for(ReportSearchResultVO resultItem:orgResultList){
			if(resultItem.getPol_idx_id().equals(policycode)){
				resultinto = resultItem;
			}
		}
		return resultinto;
	}
	/**
	 * 기간별 데이터 추출 검색결과 Ajax
	 * @param request
	 * @param interval
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/getdatareportcontentsex.do")
	public void getdatareportcontentsex(HttpServletRequest request
				, HttpServletResponse response
				, @ModelAttribute("searchVO") ReportSearchVO searchVO) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date bdate = fm.parse(searchVO.getBegin_date());
			Date edate = fm.parse(searchVO.getEnd_date());
			if(edate.getTime() - bdate.getTime() < 0){
				throw new Exception("시작일은 종료일 보다 클 수 없습니다.");
			}
			String[] polCheckList = searchVO.getPolcheckedlist().split(",");
			//개인 ? 개인점수 조회 : 하위조직 존재여부 ? 하위조직 점수 조회 : 부서명 점수 조회 
			searchVO.setOrgCode(searchVO.getOrg_upper());
			//날짜 리스트 조회
			List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
			StringBuffer tableBody = new StringBuffer();
			//조직 정보 조회 하위 조직 존재 여부
			ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
			
			if(orgInfo.getSubCount() > 0){
				
				//searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
				//searchVO.setEnd_date(searchVO.getEnd_date().replace("-", ""));
				//하위 조직 존재시
				List<ReportSearchResultVO> orgResultList = reportService.getDataReportForOrgPolicyList(searchVO);
				List<OrgResultInfoVO> orgTotalResultList = reportService.getMonthReportOrgListInfo(searchVO);
				for(String policy:polCheckList){
					
					ReportSearchResultVO policyInfo = getPolicyInfo(policy, orgResultList);
					if(policyInfo != null){
						tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
						tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\" style=\"width:%spx;\" cellpadding=0 cellspacing=0 >", ((180*2) + (240*dateList.size()))));
						List<ReportSearchResultVO> policyList = getPolicyDateList(policy, orgResultList);
						tableBody.append("<tr>");
						tableBody.append("<th style='width:180px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>정책명</th>");
						tableBody.append("<th style='width:180px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>부서명</th>");
						for(ReportSearchVO dateInfo:dateList){
							tableBody.append(String.format("<th style='width:240px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' colspan=3>%s</th>", dateInfo.getBegin_date()));	
						}
						tableBody.append("</tr>");
						tableBody.append("<tr>");
						for(ReportSearchVO dateInfo:dateList){
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-left:solid 1px #CECAC5;border-right:solid 1px #CECAC5;'>건수</th>"));	
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>점수</th>"));	
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>진단결과</th>"));	
						}
						tableBody.append("</tr>");
						boolean isFirst = true;
						for(OrgResultInfoVO totInfo:orgTotalResultList){
							tableBody.append("<tr>");
							if(isFirst){
								tableBody.append(String.format("<td rowspan=%s style='border-right:solid 1px #CECAC5;'>%s</td>", orgTotalResultList.size(), policyInfo.getSec_pol_desc()));
								isFirst = false;
							}
							
							tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", totInfo.getOrg_nm()));
							for(ReportSearchVO dateInfo:dateList){
								ReportSearchResultVO resultItem = getPolicyDateOrgInfo(policy, totInfo.getOrg_code(), dateInfo.getBegin_date(), policyList);
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getTot_count()));
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getAvg()));
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getIdxstatus()));
							}
							tableBody.append("</tr>");
						}
						
						tableBody.append("</table></div>");
						tableBody.append("<div style='height:20px;'></div>");
					}
					
					
				}
				
			}else{
				//하위 조직 미존재
				List<ReportSearchResultVO> userResultList = reportService.getDataReportForUserPolicyList(searchVO);
				List<ReportSearchResultVO> orgUserList = reportService.getDataReportOrgUserList(searchVO);
				for(String policy:polCheckList){
					//searchVO.setPolicy(policy);
					ReportSearchResultVO policyInfo = getPolicyInfo(policy, userResultList);
					if(policyInfo != null){
						tableBody.append("<div style='border:0px solid blue;width:100%;overflow:auto;'>");
						tableBody.append(String.format("<table border='0' class=\"tbl_list1 contents1\" style=\"width:%spx;\" cellpadding=0 cellspacing=0 >", ((180*2) + (240*dateList.size()))));
						List<ReportSearchResultVO> policyList = getPolicyDateList(policy, userResultList);
						tableBody.append("<tr>");
						tableBody.append("<th style='width:180px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>정책명</th>");
						tableBody.append("<th style='width:180px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' rowspan=2>성명</th>");
						for(ReportSearchVO dateInfo:dateList){
							tableBody.append(String.format("<th style='width:240px;border-bottom:solid 1px #CECAC5;border-right:solid 1px #CECAC5;' colspan=3>%s</th>", dateInfo.getBegin_date()));	
						}
						tableBody.append("</tr>");
						tableBody.append("<tr>");
						for(ReportSearchVO dateInfo:dateList){
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-left:solid 1px #CECAC5;border-right:solid 1px #CECAC5;'>건수</th>"));	
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>점수</th>"));	
							tableBody.append(String.format("<th style='width:80px;border-bottom:solid 1px #CECAC5;border-top:solid 0px #CECAC5;border-right:solid 1px #CECAC5;'>진단결과</th>"));	
						}
						tableBody.append("</tr>");
						boolean isFirst = true;
						for(ReportSearchResultVO totInfo:orgUserList){
							tableBody.append("<tr>");
							if(isFirst){
								tableBody.append(String.format("<td rowspan=%s style='border-right:solid 1px #CECAC5;'>%s</td>", orgUserList.size(), policyInfo.getSec_pol_desc()));
								isFirst = false;
							}
							
							tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", totInfo.getEmp_nm()));
							for(ReportSearchVO dateInfo:dateList){
								ReportSearchResultVO resultItem = getPolicyDateUserInfo(policy, totInfo.getEmp_no(), dateInfo.getBegin_date(), policyList);
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getCount()));
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getScore()));
								tableBody.append(String.format("<td style='border-right:solid 1px #CECAC5;'>%s</td>", resultItem == null ? "-" : resultItem.getIdxstatus()));
							}
							tableBody.append("</tr>");
						}
						
						tableBody.append("</table></div>");
						tableBody.append("<div style='height:20px;'></div>");
					}
				}
				
			
			}
			
			
			hMap.put("tableBody", tableBody.toString());
			isOk = true;
			
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.getMessage();	
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));

	}
	
	private String getPerPolicySumCount(List<EgovMap> policyScoreList, String empno, String diagmajrcode){
		int cnt = 0;
		for(EgovMap row:policyScoreList){
			//System.out.println(row.get("orgcode") + "][" + row.get("diagmajrcode") + "][" + row.get("count"));
			if(row.get("empno").equals(empno) && row.get("diagmajrcode").equals(diagmajrcode)){
				cnt = cnt + Integer.parseInt(row.get("count").toString());
			}
		}
		
		return String.valueOf(cnt);
	}
	/**
	 * 데이터 추출 Excel 저장
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/datareportexportexcel.do")
	public void statisttotalexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") ReportSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
			
			String[] polCheckList = searchVO.getPolcheckedlist().split(",");
			
		String fileName = String.format("%s_%s_데이터추출_보고서", searchVO.getBegin_date(), searchVO.getEnd_date());
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("데이터추출결과");
		excelVO.setTitle("데이터추출결과");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		List<String> head = new ArrayList<String>();
		
		
		//excelVO.setHead(head);
		
		
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		
		searchVO.setOrgCode(searchVO.getOrg_upper());
		//날짜 리스트 조회
		List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
		StringBuffer tableBody = new StringBuffer();
		//조직 정보 조회 하위 조직 존재 여부
		ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
		
		boolean isheadcreate = false;
		for(ReportSearchVO dateSearchVO:dateList){
			//날짜
			
			dateSearchVO.setOrg_code(searchVO.getOrg_upper());
			dateSearchVO.setOrg_upper(searchVO.getOrg_upper());
			dateSearchVO.setEnd_date(dateSearchVO.getBegin_date());
			dateSearchVO.setOrgCode(searchVO.getOrg_upper());
			
			if(orgInfo.getSubCount() > 0){
				//하위 조직 존재시
				List<OrgResultInfoVO> orgResultList = reportService.getMonthReportOrgListInfo(dateSearchVO);
				
					//조직 리스트 
				DashboardSearchVO dashSearchVO = new DashboardSearchVO();
				dashSearchVO.setOrg_upper(dateSearchVO.getOrg_upper());
				dashSearchVO.setBegin_date(dateSearchVO.getBegin_date().replace("-", ""));
				//정책별 결과 리스트
				List<EgovMap> policyScoreList = reportService.getDataReportOrgPolItemCountScoreStatusFroPolicyList(dashSearchVO);
				//정책 리스트
				List<EgovMap> distinctPolicyList = reportService.getDataReportItemCountScoreStatusFroDistinctPolicyList(dashSearchVO);
				if(!isheadcreate){
					head.add("날짜");
					head.add("조직");
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								head.add(String.format("%s (건수)", row.get("poldesc")));
								head.add(String.format("%s (점수)", row.get("poldesc")));
								continue;
							}
						}
						
					}
					head.add("전체건수");
					head.add("점수(가중치적용)");
					head.add("진단결과");
					excelVO.setHead(head);
					isheadcreate = true;
				}
				
				
				for(OrgResultInfoVO orgResult:orgResultList){
					EgovMap map = new EgovMap();
					map.put("날짜", dateSearchVO.getBegin_date());
					map.put("조직", orgResult.getOrg_nm());
					for(EgovMap row_pol:distinctPolicyList){
						for(String policy:polCheckList){
							if(row_pol.get("polidxid").equals(policy)){
								map.put(String.format("%s (건수)", row_pol.get("poldesc")), getPolicySumCount(policyScoreList, orgResult.getOrg_code(), row_pol.get("polidxid").toString(), "polidx", "count"));
								map.put(String.format("%s (점수)", row_pol.get("poldesc")), getPolicySumCount(policyScoreList, orgResult.getOrg_code(), row_pol.get("polidxid").toString(), "polidx", "score"));
								continue;
							}
						}
						
					}
					map.put("전체건수", orgResult.getTot_count());
					map.put("점수(가중치적용)", orgResult.getAvg());
					map.put("진단결과", orgResult.getIdxstatus());
					excellist.add(map);
				}
				excellist.add(new EgovMap());
			}else{
				//하위 조직 미존재시
				DashboardSearchVO dashSearchVO = new DashboardSearchVO();
				dashSearchVO.setOrg_upper(dateSearchVO.getOrg_upper());
				dashSearchVO.setBegin_date(dateSearchVO.getBegin_date().replace("-", ""));
				List<EgovMap> rstPerTeam = reportService.getPerCountScoreStatus(dashSearchVO);
				
				List<EgovMap> policyScoreList = reportService.getTeamPolItemCountScoreStatusFroPolicyList(dashSearchVO);
				List<EgovMap> distinctPolicyList = reportService.getDataReportItemCountScoreStatusFroDistinctPolicyList(dashSearchVO);
				
				if(!isheadcreate){
					head.add("날짜");
					head.add("조직");
					head.add("성명");
					for(EgovMap row:distinctPolicyList){
						for(String policy:polCheckList){
							if(row.get("polidxid").equals(policy)){
								head.add(String.format("%s (건수)", row.get("poldesc")));
								head.add(String.format("%s (점수)", row.get("poldesc")));
								continue;
							}
						}
						
					}
					head.add("전체건수");
					head.add("점수(가중치적용)");
					head.add("진단결과");
					excelVO.setHead(head);
					isheadcreate = true;
				}
				
				
				for(EgovMap perResult:rstPerTeam){
					EgovMap map = new EgovMap();
					map.put("날짜", dateSearchVO.getBegin_date());
					map.put("조직", perResult.get("orgnm"));
					map.put("성명", perResult.get("empnm"));
					for(EgovMap row_pol:distinctPolicyList){
						for(String policy:polCheckList){
							if(row_pol.get("polidxid").equals(policy)){
								map.put(String.format("%s (건수)", row_pol.get("poldesc")), getPolicyUserSumCount(policyScoreList, perResult.get("empno").toString(), row_pol.get("polidxid").toString(), "polidx", "count"));
								map.put(String.format("%s (점수)", row_pol.get("poldesc")), getPolicyUserSumCount(policyScoreList, perResult.get("empno").toString(), row_pol.get("polidxid").toString(), "polidx", "score"));
								continue;
							}
						}
					}
					map.put("전체건수", perResult.get("count"));
					map.put("점수(가중치적용)", perResult.get("score"));
					map.put("진단결과", perResult.get("idxstatus"));
					excellist.add(map);
				}
				excellist.add(new EgovMap());
			}
		}
		
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * 데이터 추출 Excel 저장
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/report/datareportexportexcelex.do")
	public void datareportexportexcelex(HttpServletRequest request
								, @ModelAttribute("searchVO") ReportSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
			
			String[] polCheckList = searchVO.getPolcheckedlist().split(",");
			
			String fileName = String.format("%s_%s_데이터추출_보고서", searchVO.getBegin_date(), searchVO.getEnd_date());
			
			ExcelInitVO excelVO = new ExcelInitVO();
			excelVO.setFileName(fileName);
			excelVO.setSheetName("데이터추출결과");
			excelVO.setTitle("데이터추출결과");
			excelVO.setHeadVal("");
			excelVO.setType("xlsx");
			
			List<String> head = new ArrayList<String>();
			
			
			//excelVO.setHead(head);
			
			
			List<EgovMap> excellist = new ArrayList<EgovMap>();
			
			searchVO.setOrgCode(searchVO.getOrg_upper());
			//날짜 리스트 조회
			List<ReportSearchVO> dateList = reportService.getSearchDateList(searchVO);
			//조직 정보 조회 하위 조직 존재 여부
			ReportSearchVO orgInfo = reportService.getMonthOrgInfo(searchVO);
			
			boolean isheadcreate = false;
			
			//개인 ? 개인점수 조회 : 하위조직 존재여부 ? 하위조직 점수 조회 : 부서명 점수 조회 
			searchVO.setOrgCode(searchVO.getOrg_upper());
			//날짜 리스트 조회
			//조직 정보 조회 하위 조직 존재 여부
			
			if(orgInfo.getSubCount() > 0){
				
				//searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
				//searchVO.setEnd_date(searchVO.getEnd_date().replace("-", ""));
				//하위 조직 존재시
				List<ReportSearchResultVO> orgResultList = reportService.getDataReportForOrgPolicyList(searchVO);
				List<OrgResultInfoVO> orgTotalResultList = reportService.getMonthReportOrgListInfo(searchVO);
				
				if(!isheadcreate){
					head.add("정책명");
					head.add("부서명");
					for(ReportSearchVO dateInfo:dateList){
						head.add(String.format("%s (건수)", dateInfo.getBegin_date()));
						head.add(String.format("%s (점수)", dateInfo.getBegin_date()));
						head.add(String.format("%s (진단결과)", dateInfo.getBegin_date()));
					}
					excelVO.setHead(head);
					isheadcreate = true;
				}
				
				for(String policy:polCheckList){
					
					ReportSearchResultVO policyInfo = getPolicyInfo(policy, orgResultList);
					if(policyInfo != null){
						List<ReportSearchResultVO> policyList = getPolicyDateList(policy, orgResultList);
						for(OrgResultInfoVO totInfo:orgTotalResultList){
							EgovMap map = new EgovMap();
							map.put("정책명", policyInfo.getSec_pol_desc());
							map.put("부서명", totInfo.getOrg_nm());
							for(ReportSearchVO dateInfo:dateList){
								ReportSearchResultVO resultItem = getPolicyDateOrgInfo(policy, totInfo.getOrg_code(), dateInfo.getBegin_date(), policyList);
								map.put(String.format("%s (건수)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getTot_count());
								map.put(String.format("%s (점수)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getAvg());
								map.put(String.format("%s (진단결과)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getIdxstatus());
							}
							excellist.add(map);
						}
						excellist.add(new EgovMap());
					}
				}
				
			}else{
				//하위 조직 미존재
				List<ReportSearchResultVO> userResultList = reportService.getDataReportForUserPolicyList(searchVO);
				List<ReportSearchResultVO> orgUserList = reportService.getDataReportOrgUserList(searchVO);
				
				if(!isheadcreate){
					head.add("정책명");
					head.add("성명");
					for(ReportSearchVO dateInfo:dateList){
						head.add(String.format("%s (건수)", dateInfo.getBegin_date()));
						head.add(String.format("%s (점수)", dateInfo.getBegin_date()));
						head.add(String.format("%s (진단결과)", dateInfo.getBegin_date()));
					}
					excelVO.setHead(head);
					isheadcreate = true;
				}
				
				for(String policy:polCheckList){
					//searchVO.setPolicy(policy);
					ReportSearchResultVO policyInfo = getPolicyInfo(policy, userResultList);
					if(policyInfo != null){
						List<ReportSearchResultVO> policyList = getPolicyDateList(policy, userResultList);
						
						for(ReportSearchResultVO totInfo:orgUserList){
							EgovMap map = new EgovMap();
							map.put("정책명", policyInfo.getSec_pol_desc());
							map.put("성명", totInfo.getEmp_nm());
							for(ReportSearchVO dateInfo:dateList){
								ReportSearchResultVO resultItem = getPolicyDateOrgInfo(policy, totInfo.getOrg_code(), dateInfo.getBegin_date(), policyList);
								map.put(String.format("%s (건수)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getCount());
								map.put(String.format("%s (점수)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getScore());
								map.put(String.format("%s (진단결과)", dateInfo.getBegin_date()), resultItem == null ? "-" : resultItem.getIdxstatus());
							}
							excellist.add(map);
						}
						excellist.add(new EgovMap());
					}
				}
				
			
			}
		
			ExcelUtil.xssExcelDown(response, excelVO, excellist);
		
		}catch(Exception e){e.printStackTrace();}
	}
		
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "sumrgdt" : ColumnName = "날 짜";break;
		case "sumrgdtdate" : ColumnName = "날 짜";break;
		case "idxrgdtdate" : ColumnName = "날 짜";break;
		case "regdate" : ColumnName = "날 짜";break;
		case "empno" : ColumnName = "사 번";break;
		case "empnm" : ColumnName = "성 명";break;
		case "descname" : ColumnName = "지수화정책";break;
		case "majrdiagdesc" : ColumnName = "진단명";break;
		case "minrdiagdesc" : ColumnName = "점검명";break;
		case "secpoldesc" : ColumnName = "정책명";break;
		case "totcount" : ColumnName = "건수";break;
		case "idxstatus" : ColumnName = "진단결과";break;		
		case "orgcode" : ColumnName = "조직코드";break;
		case "orgnm" : ColumnName = "조직명";break;
		case "diagdesc1" : ColumnName = "PC 취약성 진단";break;
		case "diagdesc2" : ColumnName = "악성코드 감염진단";break;
		case "diagdesc3" : ColumnName = "정보 유출 진단";break;
		case "org_code" : ColumnName = "조직코드";break;
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "성명";break;
		case "org_nm" : ColumnName = "조직명";break;
		case "score" : ColumnName = "점수";break;
		case "count" : ColumnName = "건수";break;
		case "avg" : ColumnName = "평균";break;
		case "polstat" : ColumnName = "진단결과";break;
		case "idx_rgdt_date" : ColumnName = "지수수집일";break;
		case "pol_idx_id" : ColumnName = "정책코드";break;
		case "mac" : ColumnName = "MAC";break;
		case "ip" : ColumnName = "IP";break;	
		case "pol_stat_code" : ColumnName = "진단내역";break;
		case "pol_idx_name" : ColumnName = "정책명";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}	
}
