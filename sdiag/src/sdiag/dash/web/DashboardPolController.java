package sdiag.dash.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import sdiag.dash.service.DashboardService;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.GaugeItemValue;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.login.service.UserManageVO;
import sdiag.login.web.CommonController;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.util.CommonUtil;
import sdiag.util.LeftMenuInfo;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.util.LDAPUtil;

@Controller
public class DashboardPolController  {
	
	@Resource(name = "DashboardService")
	private DashboardService dashboardService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	static final String MAJRCODE = "A01";
	
	@RequestMapping(value="/dash/home.do")
	public String dashboard(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("searchVO") DashboardSearchVO searchVO, ModelMap model) throws Exception{	
		//response.addHeader("X-Frame-Options", "SAMEORIGIN");
		/**
		 * 로그인 사용자 정보  
		 * 인증세션 동기화를 위하여 수정함* 
		 */
		
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		
		CommonUtil.topMenuToString(request, "home", comService);
		
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
				
		
		/**
		 * 사용자 로그인 정보
		 * auth 값정의 : 관리자/운영자:1 , 대무자(조직장):2, 일반사용자:3
		 * 정책모니터링 조회시 사용하는 인증코드임
		 */
		String auth = loginInfo.getRole_code();
		String orgCode = loginInfo.getOrgcode();
				
		String emp_no = loginInfo.getUserid();
		
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
		List<OrgGroupVO> orgCapList = comService.getOrgCapInfoList(emp_no);
		if(orgCapList.size() > 0)
		{
			//조직장 
			OrgGroupVO capInfo = orgCapList.get(0);
			isUser = false;
			auth = "2";
			orgCode=capInfo.getOrg_code().toString();
			
			if(!orgCode.equals(dpflag)){
				emp_no = loginInfo.getUserid(); // getEmp_no(request);
				orgCode = dpflag;
			}
			
			//System.out.println("조직장!!!!");
		}

		/**
		 * [3] 사용자 정보 조회 
		 */		
		UserIdxInfoCurrVO userInfo = dashboardService.getUserInfoOrg(emp_no);
		model.addAttribute("userInfo", userInfo);
		
		model.addAttribute("authvalue", auth);
		
		
		/** [1-1] 담당부서 정보 조회 */
		HashMap<String,Object> rMapUinfo = new HashMap<String,Object>();
		List<UserIdxInfoCurrVO> list_uinfo = new ArrayList<UserIdxInfoCurrVO>();
		rMapUinfo = dashboardService.getUserInfo(emp_no);
		list_uinfo = (List<UserIdxInfoCurrVO>)rMapUinfo.get("list");
		model.addAttribute("resultList_uinfo", list_uinfo);	
		
		if(list_uinfo != null){
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
			searchVO.setOrg_upper("000001"); 
		}else{
			searchVO.setOrg_upper(soc_org_code); 
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
				
	    int isRowType;
		
	    List<EgovMap> resultList = null;
		/** parameter : emp_no */
		List<EgovMap> caporgList = dashboardService.getUserInfoCap(searchVO); //조직 정보조회
	    /** parameter : upper_org_code */	    
	    List<EgovMap> orgorgList = dashboardService.getOrgSubList(searchVO); // 하위조직 조회
	
	    /* 관리자인경우 조직코드 설정 */
		if(dpflag.equals("sa")){
			orgCode = "000001";
		}	    
	    
		
		
		/**
		 * V2.0 
		 * 현재점수 조회 [1]-[2]
		 * 현재조직 & 개인
		 */
		try{
			int curScore = 0;
			String curScoreUserInfo = "";
			String curScoreUserOrgName = "";
			String curScoreUserStat = "";
			
			UserIdxInfoCurrVO rmapOrgCurScore = new UserIdxInfoCurrVO();			
			UserIdxInfoCurrVO rmapPerCurScore = new UserIdxInfoCurrVO();
			
			if( (caporgList.size() > 1 && !dpflag.equals("personal")) || dpflag.equals("sa") || (auth.equals("2") && !dpflag.equals("personal"))){
				
				/* 상무이상, 대무자가 상무인지, 관리자인, 대무관리자인지 여부에 따라 부서진단 점수 표시 */
				if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || !loginInfo.getRole_code().equals("3") || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
					if( (orgorgList.size() > 0 && !dpflag.equals("personal")) || dpflag.equals("sa") ){
						rmapOrgCurScore = dashboardService.getOrgBuseoCurScore(orgCode);
					}else{
						rmapOrgCurScore = dashboardService.getOrgCurScore(orgCode);
					}
				}else{
					rmapOrgCurScore = dashboardService.getOrgCurScore(orgCode);
				}				
							
				/** 조직점수 */
				if (rmapOrgCurScore != null){
					curScore = rmapOrgCurScore.getScore();
					curScoreUserOrgName = rmapOrgCurScore.getOrg_nm();   //userInfo.getOrg_nm();
				}
			
			}else{
				
				rmapPerCurScore = dashboardService.getPerCurScore(emp_no);
				
				/** 개인점수 */
				if (rmapPerCurScore != null){
					curScore = rmapPerCurScore.getScore();
					curScoreUserOrgName = rmapPerCurScore.getOrg_nm();   //userInfo.getOrg_nm();
				}				
			}
			
			curScoreUserInfo = userInfo.getEmp_nm();
			
			/** 직책코드 */			
			if(auth.equals("2")){
				curScoreUserInfo += "&nbsp;" + userInfo.getLevel_nm();
			}else{
				curScoreUserInfo += "&nbsp;" + userInfo.getTitle_nm();
			}
			
			/**
			 * 현재 보안점수에 대한 상태 표시 
			 * 100 ~ 80 이상 : 양호, 80 ~ 40 : 주의, 40 ~ 0 : 위험
			 */
			List<CodeInfoVO> scoreStatus = comService.getCodeInfoListNoTitle("C04");
			GaugeItemValue gauagValue = new GaugeItemValue();
			curScoreUserStat = "위험";
			gauagValue.setGood("100");
			for(CodeInfoVO row:scoreStatus){
				if(row.getMinr_code().equals("WAN")){
					gauagValue.setDanger(row.getCode_desc());
				}else if(row.getMinr_code().equals("GOD")){
					gauagValue.setWarning(row.getCode_desc());
				}
			}
			
			if(curScore <=  Integer.parseInt(gauagValue.getGood().toString())  && curScore >= Integer.parseInt(gauagValue.getWarning().toString()) ){
				curScoreUserStat = "양호";
			}else if(curScore < Integer.parseInt(gauagValue.getWarning().toString()) && curScore >= Integer.parseInt(gauagValue.getDanger().toString())){
				curScoreUserStat ="주의";
			}else{
				curScoreUserStat = "위험";
			}
			/*
			if (curScore <= 100 && curScore >= 80){
				curScoreUserStat = "양호";
			}else if (curScore < 80 && curScore >= 40){
				curScoreUserStat = "주의";
			}else{
				curScoreUserStat = "위험";
			}			
			*/
			model.addAttribute("curScore", curScore);
			model.addAttribute("curScoreUserStat", curScoreUserStat);
			model.addAttribute("curScoreUserInfo", curScoreUserInfo);
			model.addAttribute("curScoreUserOrgName", curScoreUserOrgName);
			model.addAttribute("gaugeValue", gauagValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/**
		 * V2.0 
		 * 보안수준진단 점수 추이 리스트 [1]-[2]-[3]
		 * 상위 소속조직 & 개인
		 */
		try{
			HashMap<String,Object> rmapOrgCodeUpper = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> lstOrgCodeUpperd = new ArrayList<UserIdxInfoCurrVO>();	
			
			HashMap<String,Object> rmapOrgSumInfoTrend = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> lstOrgSumInfoTrend = new ArrayList<UserIdxInfoCurrVO>();
			
			//getPerSumInfoTrend
			HashMap<String,Object> rmapPerSumInfoTrend = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> lstPerSumInfoTrend = new ArrayList<UserIdxInfoCurrVO>();
			
			/**
			 * 보안수준진단 추이구간 MINDATE MAXDATE
			 */
			
			HashMap<String,Object> retMap = new HashMap<String,Object>();
			
			retMap = dashboardService.minMaxdate(searchVO);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			Date beginDate = dateFormat.parse(retMap.get("minday").toString());
			Date endDate = dateFormat.parse(retMap.get("maxday").toString()); 
			long diff = endDate.getTime() - beginDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			String tickIntervalString = "1 weeks";
			if(diffDays < 30){
				tickIntervalString = "2 days";
			}
			if(diffDays < 14){
				tickIntervalString = "1 days";
			}
			model.addAttribute("minday", retMap.get("minday"));
			model.addAttribute("maxday", retMap.get("maxday"));
			model.addAttribute("mindate", retMap.get("mindate"));
			model.addAttribute("maxdate", retMap.get("maxdate"));
			model.addAttribute("basepoint", retMap.get("basepoint"));
			model.addAttribute("tickInterval", tickIntervalString);
			/**
			 * [1] 상위조직 코드 조회
			 * parameter : emp_no, org_code
			 */
			
			rmapOrgCodeUpper = dashboardService.getOrgCodeUpper(emp_no);
			lstOrgCodeUpperd = (List<UserIdxInfoCurrVO>)rmapOrgCodeUpper.get("list");
			
			/**
			 * 개인
			 */
			rmapPerSumInfoTrend = dashboardService.getPerSumInfoTrend(emp_no);
			lstPerSumInfoTrend = (List<UserIdxInfoCurrVO>)rmapPerSumInfoTrend.get("list");			
			
			String chartList0 = "";
			String chartList1 = "";
			String chartList2 = "";
			String chartList3 = "";
			String chartList4 = "";
			String chartList5 = "";
			String chartList_per = "''"; //개인
			String plotLines = ""; // 차트라인을 결정한다.
			String labelList = ""; // label 표시용 리스트 (Example) { label: '${label0}' }, { label: '${label1}' }, { label: '${label2}' } ..
			String seriesLegendText = "";
			List<String> colorList = Arrays.asList(new String[]{"'#3B83C4'", "'rgba(1, 141, 53, 1)'", "'rgba(189, 13, 253, 1)'", "'rgba(0, 0, 0, 1)'", "'#17BDB8'", "'#48CB01'", "'rgba(0, 0, 0, 1)'", "'#fa5d58'"});
			
			/**
			 * [2] 조직별 score 조회
			 * parameter : org_code return: average score
			 */
			if(lstOrgCodeUpperd.size() > 0){
				
				chartList0 = "[";
				chartList1 = "[";
				chartList2 = "[";
				chartList3 = "[";
				chartList4 = "[";
				chartList5 = "[";
	
				for(int i = 0; i < lstOrgCodeUpperd.size(); i++){
					
					/* 상무이상, 대무자가 상무인지, 관리자인지 여부에 따라 부서진단 점수 표시 */
					if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || !loginInfo.getRole_code().equals("3") || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
						
						if( (orgorgList.size() > 0 && !dpflag.equals("personal")) || dpflag.equals("sa") ){
							rmapOrgSumInfoTrend = dashboardService.getOrgBuseoSumInfoTrend(lstOrgCodeUpperd.get(i).getOrg_code());
						}else{
							rmapOrgSumInfoTrend = dashboardService.getOrgSumInfoTrend(lstOrgCodeUpperd.get(i).getOrg_code());
						}
					}else{
						rmapOrgSumInfoTrend = dashboardService.getOrgSumInfoTrend(lstOrgCodeUpperd.get(i).getOrg_code());
					}
					lstOrgSumInfoTrend = (List<UserIdxInfoCurrVO>)rmapOrgSumInfoTrend.get("list");
					
					for(int j = 0; j < lstOrgSumInfoTrend.size(); j++){
						
						if(i==0){
							chartList0 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList0 += ',';
							}
						}
						if(i==1){
							chartList1 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList1 += ',';
							}
						}
						if(i==2){
							chartList2 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList2 += ',';
							}
						}
						if(i==3){
							chartList3 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList3 += ',';
							}
						}
						if(i==4){
							chartList4 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList4 += ',';
							}
						}
						if(i==5){
							chartList5 += "['" +lstOrgSumInfoTrend.get(j).getMonth_g() + "',"+lstOrgSumInfoTrend.get(j).getScore()+"]";
							if (j != lstOrgSumInfoTrend.size()-1){
								chartList5 += ',';
							}
						}
					}
					
					labelList += "{";
					labelList += "label: '"+lstOrgCodeUpperd.get(i).getOrg_nm()+"'";
					labelList += "}";					
					
					plotLines += "line"+i;
					seriesLegendText += colorList.get(i) + ",";
					
					if (i != lstOrgCodeUpperd.size()-1){
						plotLines += ',';
						
						labelList += ",";
					}						
					
				}
				
				chartList0 = chartList0 + "]";
				chartList1 = chartList1 + "]";
				chartList2 = chartList2 + "]";
				chartList3 = chartList3 + "]";
				chartList4 = chartList4 + "]";
				chartList5 = chartList5 + "]";
				
				
				model.addAttribute("chartList0", chartList0);
				model.addAttribute("chartList1", chartList1);
				model.addAttribute("chartList2", chartList2);
				model.addAttribute("chartList3", chartList3);
				model.addAttribute("chartList4", chartList4);
				model.addAttribute("chartList5", chartList5);				
			}
			
			/**
			 * [3] 개인 score 조회
			 * parameter : emp_no return : average sore
			 */
			
			if(lstPerSumInfoTrend.size() > 0){
				chartList_per = "[";
				
				for(int i = 0; i < lstPerSumInfoTrend.size(); i++){				
					chartList_per += "['" +lstPerSumInfoTrend.get(i).getMonth_g() + "',"+lstPerSumInfoTrend.get(i).getScore()+"]";
					if (i != lstPerSumInfoTrend.size()-1){
						chartList_per += ',';
					}
				}
				chartList_per = chartList_per + "]";
								
				if (lstOrgCodeUpperd.size() > 0){
					plotLines += ',';
					
					labelList += ",";
				}
				plotLines += "line_per";
				
				labelList += "{";
				labelList += "label:'"+ lstPerSumInfoTrend.get(0).getEmp_nm() +"'";
				labelList += "}";				
				seriesLegendText += colorList.get(6) + ",";
			}
			plotLines += ",[]";
			labelList += ",{label:'전사 목표점수'}";
			seriesLegendText += colorList.get(7);
			model.addAttribute("plotLines", plotLines);
			model.addAttribute("labelList", labelList);
			model.addAttribute("chartList_per", chartList_per);
			model.addAttribute("seriesLegendText", seriesLegendText);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		/**
		 * V2.0 
		 * 진단 항목별 발생건수 [1] [2]
		 * 조직 & 개인  [1]-getOrgIdxCount [2]-getPerIdxCount
		 */
		try{
			
			HashMap<String,Object> retMap_Orgidxcnt = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> list_Orgidxcnt = new ArrayList<UserIdxInfoCurrVO>();
			
			HashMap<String,Object> retMap_Peridxcnt = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> list_Peridxcnt = new ArrayList<UserIdxInfoCurrVO>();
			UserIdxInfoCurrVO total = new UserIdxInfoCurrVO();
			HashMap<String, String> sMap = new HashMap<String, String>();
			/* 상무이상, 대무자가 상무인지, 관리자인, 대무관리자인지 여부에 따라 부서진단 점수 표시 */
			if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || !loginInfo.getRole_code().equals("3") || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
				
				if( (orgorgList.size() > 0 && !dpflag.equals("personal")) || dpflag.equals("sa") ){
					retMap_Orgidxcnt = dashboardService.getOrgBuseoIdxCount(orgCode);
					
					
				}else{
					retMap_Orgidxcnt = dashboardService.getOrgIdxCount(orgCode);
				}
			}else{
				retMap_Orgidxcnt = dashboardService.getOrgIdxCount(orgCode);
			}
			
			if((auth.equals("2") && !dpflag.equals("personal") && orgorgList.size() > 0) || auth.equals("1")){
				sMap.put("orgCode", orgCode);
				sMap.put("type", "B");
				total = dashboardService.getTotalOrgBuseoIdxCount(sMap);
			}else if(auth.equals("2") && !dpflag.equals("personal") && orgorgList.size() <= 0){
				sMap = new HashMap<String, String>();
				sMap.put("orgCode", orgCode);
				sMap.put("type", "P");
				total = dashboardService.getTotalOrgBuseoIdxCount(sMap);
			}
			
			
			
			retMap_Peridxcnt = dashboardService.getPerIdxCount(emp_no);
			
			String stx_data = ""; // 지수별 점수 리스트
			String stx_tick = ""; // 지수별 항목 리스트
			
			int sumscore = 0;
			//int avgscore = 0;
			float avgscore = 0;
				
			if( (orgorgList.size() > 0 && !dpflag.equals("personal")) || dpflag.equals("sa") || (auth.equals("2") && !dpflag.equals("personal"))){
				
				/** 조직건수 */
				if (retMap_Orgidxcnt != null){
					list_Orgidxcnt = (List<UserIdxInfoCurrVO>)retMap_Orgidxcnt.get("list");
					int avgcnt = 0;
					for(int i = 0; i < list_Orgidxcnt.size(); i++){
						stx_data += list_Orgidxcnt.get(i).getCount().equals("-") ? "0" : list_Orgidxcnt.get(i).getCount();
						
						//if(Integer.parseInt((list_Orgidxcnt.get(i).getCount())) > 0){
							stx_tick += "'" + list_Orgidxcnt.get(i).getDiag_desc() + "'";
						//}else{
						//	stx_tick += "'" + list_Orgidxcnt.get(i).getDiag_desc() + "'";
						//}
						
						if (i != list_Orgidxcnt.size()-1){
							stx_data += ',';
							stx_tick += ',';
						}
						
						//sumscore += list_Orgidxcnt.get(i).getScore(); //점수 합계						
						if(!list_Orgidxcnt.get(i).getAvg_score().equals("-")){
							sumscore += list_Orgidxcnt.get(i).getScore(); //점수 합계
							avgcnt++;
						}
					}
					avgscore = (float) sumscore / avgcnt;	
					
					double avgscore_d = Math.floor(Math.round(avgscore/1000.0));
					String startClass = "";
					String endClass = "";					

					if(list_Orgidxcnt.size() == 4){
						
						startClass = "graph4_date3";
						endClass = "graph4_date4";
						
					}else if(list_Orgidxcnt.size() == 5){

						startClass = "graph4_date5";
						endClass = "graph4_date6";
						
					}else if(list_Orgidxcnt.size() == 3){
						
						startClass = "graph4_date1";
						endClass = "graph4_date2";						
						
					}else{
						startClass = "graph4_date1";
						endClass = "graph4_date2";
					}

					model.addAttribute("avgscore", (int)avgscore_d);
					model.addAttribute("polcount", list_Orgidxcnt);
					model.addAttribute("startClass", startClass);
					model.addAttribute("endClass", endClass);
					model.addAttribute("listsize", list_Orgidxcnt.size());
					model.addAttribute("total", total);
				}
			
			}else{
	
				/** 개인건수 */
				if (retMap_Peridxcnt != null){
					
					list_Peridxcnt = (List<UserIdxInfoCurrVO>)retMap_Peridxcnt.get("list");
					int avgcnt = 0;
					for(int i = 0; i < list_Peridxcnt.size(); i++){
						stx_data += list_Peridxcnt.get(i).getCount();
						stx_tick += "'"+list_Peridxcnt.get(i).getDiag_desc()+"'";
						if (i != list_Peridxcnt.size()-1){
							stx_data += ',';
							stx_tick += ',';
						}
						if(!list_Peridxcnt.get(i).getAvg_score().equals("-")){
							sumscore += list_Peridxcnt.get(i).getScore(); //점수 합계
							avgcnt++;
						}
						
						
					}
					
					avgscore = (float) sumscore / avgcnt;		
					
					double avgscore_d = Math.floor(Math.round(avgscore/1000.0));
					String startClass = "";
					String endClass = "";
					
					if(list_Peridxcnt.size() == 4){
						
						startClass = "graph4_date3";
						endClass = "graph4_date4";
						
					}else if(list_Peridxcnt.size() == 5){

						startClass = "graph4_date5";
						endClass = "graph4_date6";
						
					}else if(list_Peridxcnt.size() == 3){
						
						startClass = "graph4_date1";
						endClass = "graph4_date2";						
						
					}

					model.addAttribute("avgscore", (int)avgscore_d);							
					model.addAttribute("polcount", list_Peridxcnt);
					model.addAttribute("startClass", startClass);
					model.addAttribute("endClass", endClass);
					model.addAttribute("listsize", list_Peridxcnt.size());
				}				
			}
						
			model.addAttribute("stx_data", stx_data);  // bar data list
			model.addAttribute("stx_tick", stx_tick);	// axes tick list			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		/**
		 * 지수점수 테이블헤더 정의 START
		 */
		
		String jisuTitle = "";
		String jisuTableHead = "";
		
		if(auth.equals("3") || dpflag.equals("personal")){
			jisuTitle = "정책별";
			jisuTableHead = "정책";
		}else{
			
			if(dpflag.equals("sa")){
					jisuTitle = "조직별";
					jisuTableHead = "조직";
			}else{
				if(orgorgList.size() > 0){
					jisuTitle = "조직별";
					jisuTableHead = "조직";	
				}else{
					if(auth.equals("2")){ // 하위조직 없고 조직장이면
						jisuTitle = "팀원별";
						jisuTableHead = "팀원";							
					}else{ 
						jisuTitle = "정책별";
						jisuTableHead = "정책";							
					}	
				}
			}
		}
		
		/**
		 * 지수점수 테이블헤더 정의 END
		 */
	
		model.addAttribute("jisuTitle", jisuTitle);
		model.addAttribute("jisuTableHead", jisuTableHead);		
		
		/**
		 * 공지사항, FAQ Top 5 List			 * 
		 */
		HashMap<String, Object> noticeMap = comService.getMainNoticeNFaqListInfo();
		List<NoticeVO> noticeList = (List<NoticeVO>)noticeMap.get("noticeList");
		List<NoticeVO> faqList = (List<NoticeVO>)noticeMap.get("faqList");
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("faqList", faqList);

		return "/dash/dashboardpol";
	}
	
	
	@RequestMapping(value = "/report/diagMajrAjax.do")
	public void reportHandelerAjax(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("searchVO") DashboardSearchVO searchVO,
			ModelMap model
			) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!";
	    Boolean isOk = false;
	    String buseoIndc = "";
	    
		try{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			if(request.getParameter("majrCode") != null && !request.getParameter("majrCode").equals("")){
				//System.out.println("--------------------------------------------majrCode:"+request.getParameter("majrCode"));
				searchVO.setMajCode(request.getParameter("majrCode"));
				// 정책명 추가 2017.04.27
				searchVO.setMajName(request.getParameter("majrName"));
			}else{
				//System.out.println("--------------------------------------------majrCode:"+MAJRCODE);
				searchVO.setMajCode(MAJRCODE);
			}
			
			if(request.getParameter("buseoIndc") != null && !request.getParameter("buseoIndc").equals("")){
				
				buseoIndc = request.getParameter("buseoIndc");
				
			}
			
		    String beginDate = searchVO.getBegin_date().replace("-", ""); //검색날짜
		    searchVO.setBegin_date(beginDate);
			
			String dpflag = ""; // cap:조직, (cap_code:조직코드), super:관리자, personal:개인, replace:대무자
			
			String rtnStr = "";
			if(buseoIndc.equals("ALL")){
				rtnStr = perDiagMajrOrgPolscore(searchVO);
				isOk = true;
			}else if(buseoIndc.equals("P")){
				rtnStr = perDiagMajrUserPolscore(searchVO);
				isOk = true;
			}
			else{
			
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
						
			boolean isUser = auth == "1" ? false : true;		
			
			if(Integer.parseInt(auth) == 1)
			{
				isUser = false;
				auth = "1";
			}
			
	
			/**
			 * [1] 대무자를 조회
			 */	
			//해당 조직 조직장 사번 조회
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
				
				//System.out.println("조직장????!!!!");
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
						//System.out.println("-----------------------------------------"+list_uinfo.get(u).getUnder_code() + "(orglevel:" + list_uinfo.get(u).getOrg_level() + ")");
						soc_org_code = list_uinfo.get(u).getUnder_code();
						org_level = list_uinfo.get(u).getOrg_level();
						
						//System.out.println("1--------------------------------orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
						
						if(orgcodeMode.equals(soc_org_code)){
							orgcodeModeFlag = true;
							System.out.println("2-----------------------------------orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
							orgCode = orgcodeMode;
						}
					}
				}else{
					if(list_uinfo.get(0).getUnder_code() != null){
						//System.out.println("-------------------------------------------------------------" + list_uinfo.get(0).getUnder_code());
						soc_org_code = list_uinfo.get(0).getUnder_code();
						org_level = list_uinfo.get(0).getOrg_level();
					}else{ 
						//System.out.println("-------------------------------------------------------------" + "personal");
						
						soc_org_code = "";
						org_level = "";
					}
				}
			}else{
				//System.out.println("-------------------------------------------------------------null");
			}
			
			if(orgcodeModeFlag){
				soc_org_code = orgcodeMode;
			}
			
			//System.out.println("soc_org_code:" + soc_org_code + "//" + "org_level:" + org_level);
			
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
			
			//System.out.println("------------------------------------------------->>>>>>" + searchVO.getBegin_date());
			//System.out.println("------------------------------------------------->>>>>>" + searchVO.getEmp_no());
			//System.out.println("------------------------------------------------->>>>>>" + searchVO.getOrg_upper());			
			
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
			    	System.out.println("-------------------------------------------------------------팀장");
			    }else if(caporgList.size() > 0 && orgorgList.size() > 0){
			    	System.out.println("-------------------------------------------------------------조직장");
			    }
		    }
		    
		    /*
		    System.out.println("-------------------------------------------------------------");
		    System.out.println("권한코드::"+auth);
		    System.out.println("권한상태::"+user_authnm);
		    System.out.println("화면모드::"+ktmp);
		    System.out.println("조직코드::"+orgUpper + "||" + dpflag);
		    System.out.println("소속조직코드::"+orgCode);
		    System.out.println("-------------------------------------------------------------");
		    */
			
			
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
			

						
			if(ktmp.equals("personal")){
				rtnStr = perDiagMajrPolscore(searchVO); //[1]
			//	rtnStr += perPolItemCountScoreStatus(searchVO); //[3]
			}else if(ktmp.equals("sa")){
				//rtnStr += orgPolTotcountAvg(searchVO); //[2]
				
				/* 하위조직 있는지 */
				System.out.println("-------------------------------------------------------------orgorgList::"+orgorgList.size());
				
				if(orgorgList.size() < 1 ){
					/* 하위조직 없으면 */
					rtnStr += memberDiagMajrPolscore(searchVO); //[4]
				}else{
					/* 하위조직 있으면 */
					if(buseoIndc.equals("Y")){
						rtnStr += orgBuseoDiagMajrPolscore(searchVO); //[5]
					}else{
						rtnStr += orgDiagMajrPolscore(searchVO); //[5]
					}
				}
				
				//rtnStr += orgDiagItemCountScoreStatus(searchVO); //[6]
			}else{
				
				/* 하위조직 있는지 */
				System.out.println("-------------------------------------------------------------orgorgList::"+orgorgList.size());				
				
				//rtnStr += orgPolTotcountAvg(searchVO); //[2]
				
				if(orgorgList.size() < 1 ){
					/* 하위조직 없으면 */
					rtnStr += memberDiagMajrPolscore(searchVO); //[4]
				}else{
					/* 하위조직 있으면 */
					/* 상무이상, 대무자가 상무인지, 관리자인, 대무관리자인지 여부에 따라 부서진단 점수 표시 */
					if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
						if( buseoIndc.equals("Y") ){
							rtnStr += orgBuseoDiagMajrPolscore(searchVO); //[5]
						}else{
							rtnStr += orgDiagMajrPolscore(searchVO); //[5]
						}
					}else{
						rtnStr += orgDiagMajrPolscore(searchVO); //[5]
					}
				}
				
				//rtnStr += orgDiagItemCountScoreStatus(searchVO); //[6]				
				
			}
			

			/**
			 * [1]개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
			 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
			 */
			//rtnStr = personalPolcount(emp_no);
			
			
			/**
			 * [2]조직 :: 진단점수 및 진단항목별 전체건수/점수
			 * map 	<!-- 팀 조직의 진단항목별 전체건수 및 점수 @org_code -->
			 * <select id="ReportDAO.OrgPolTotcountAvg" parameterClass="java.lang.String" resultClass="egovMap" remapResults="true">
			 */
			//rtnStr += orgPolTotcountAvg(orgCode);
			
			
			/**
			 * [3]개인 지수화정책 항목별 건수 점수 진단상태
			 * 	<!-- 개인의 지수화정책 항목별 총건수 및 점수, 진단상태 -->
			 * 	<select id="ReportDAO.getPerPolItemCountScoreStatus" parameterClass="java.lang.String" resultClass="egovMap" remapResults="true">
			 */
			
			//rtnStr += perPolItemCountScoreStatus(emp_no);
			}
			
			
	        map.put("totalPage", 0);
	        map.put("currentpage", 1);			    
			
	        msg = "Data count : 0";
		    map.put("ISOK", isOk);
		    map.put("MSG", "");
		    map.put("strList", rtnStr);

		}catch(Exception e){
			e.printStackTrace();
	        map.put("totalPage", 0);
	        map.put("currentpage", 1);			    
			
	        msg = "Data count : 0";
		    map.put("ISOK", isOk);
		    map.put("MSG", "");
		    map.put("strList", "");
		}			
		
    
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	
	private String convertGaugeTitle(String Value){
		String retValue = "위험";
		if(Value.equals("WAN")){
			retValue = "주의";
		}else if(Value.equals("GOD")){
			retValue = "양호";
		}
		return retValue;
	}
	/**
	 * ver3.0 개인 : 진단항목 정책별지수 점수
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String perDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception{
		/**
		 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
		 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
		 */
		
		StringBuffer str = new StringBuffer();
		
		//perDiagPolsumcount
				
		HashMap<String,Object> retMap = new HashMap<String,Object>();
		
		retMap = dashboardService.perDiagPolsumcount(searchVO);/*개인*/
		
		List<EgovMap> rstListPer = dashboardService.perDiagMajrPolscore(searchVO);		
			//majCode='A01' minCode='A02' polCode='PCG03'  loginType='personal' menutype='P' buseoindc='N'	
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />정책별 지수점수 - "+searchVO.getMajName()+"</li> ");
		str.append(String.format("        <li class=\"cell2\">전체 %s개 정책 <span class=\"txcell1\">양호 %s개</span><span class=\"txcell2\">취약 %s개</span></li> ",retMap.get("tot"), retMap.get("goodness"), retMap.get("weekness") ));
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append(" 				<tr> ");
		str.append("	            	<th width=\"40%\">정책</th> ");
		str.append("	                <th width=\"15%\">건수</th> ");
		str.append("	                <th width=\"15%\">점수</th> ");
		str.append("	                <th width=\"15%\">상태</th> ");
		str.append("	                <th width=\"15%\">소명대상</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");

		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				
				str.append(String.format("<tr>"));			
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(String.format("<a  style='cursor:pointer;' class='btn_pol_status' majCode='%s' minCode='%s' polCode='%s' buseoType='%s'>%s</a>", row.get("majcode"), row.get("mincode"), row.get("polcode"), "N", row.get("descname")));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("idxstatus"));
				str.append("</td>");
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("isapprstatustxt"));
				str.append("</td>");
				str.append("</tr>");
				
				e = e + 1;
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='5'>정책 미설정 진단입니다.</td>");
			str.append("</tr>");
		}
		
                                                                
		str.append("	        </tbody> ");
		str.append("	    </table> ");

		return str.toString();
	}
	
	
	/**
	 * ver3.0 팀원 : 진단항목 대분류 정책 건수  점수
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String memberDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception{
		
		StringBuffer str = new StringBuffer();
		List<EgovMap>	rstListPer = dashboardService.memberDiagMajrPolscore(searchVO);
		
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />진단항목 세부 내역 - "+searchVO.getMajName()+"</li> ");
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append("	        	<tr> ");
		str.append("	            	<th width=\"55%\">팀원</th> ");
		str.append("	                <th width=\"15%\">건수</th> ");
		str.append("	                <th width=\"15%\">점수</th> ");
		str.append("	                <th width=\"15%\">상태</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");

		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				str.append("<tr>");			
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(String.format("<a style='cursor:pointer;' class='btn_pol_status' majCode='%s' minCode='%s' polCode='%s' buseoType='%s'>%s</a>", searchVO.getMajCode(), "", "", "N", row.get("empnm")));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("idxstatus"));
				str.append("</td>");
				str.append("</tr>");
				
				e = e + 1;			
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='5'>정책 미설정 진단입니다.</td>");
			str.append("</tr>");
		}
		
		
		str.append("	        </tbody> ");
		str.append("	    </table> ");		
		return str.toString();
	}
	
	
	/**
	 * ver3.0 조직 : 진단항목 대분류 정책 건수  점수
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String orgDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception{
		
		StringBuffer str = new StringBuffer();
		List<EgovMap>	rstListPer = dashboardService.orgDiagMajrPolscore(searchVO);
		
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />진단항목 세부 내역- "+searchVO.getMajName()+"</li> ");
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append("	        	<tr> ");
		str.append("	            	<th width=\"55%\">조직</th> ");
		str.append("	                <th width=\"15%\">건수</th> ");
		str.append("	                <th width=\"15%\">점수</th> ");
		str.append("	                <th width=\"15%\">상태</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");
		
		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				
				str.append("<tr>");			
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(String.format("<a style='cursor:pointer;' class='btn_pol_status' majCode='%s' minCode='%s' polCode='%s' buseoType='%s'>%s</a>", searchVO.getMajCode(), "", "", "N", row.get("orgnm")));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("idxstatus"));
				str.append("</td>");
				str.append("</tr>");
				
				e = e + 1;
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='5'>정책 미설정 진단입니다.</td>");
			str.append("</tr>");
		}
		
		
		str.append("	        </tbody> ");
		str.append("	    </table> ");
		str.append("        </div> ");
		return str.toString();
	}
	
	/**
	 * ver3.5 부서 : 진단항목 대분류 정책 건수  점수
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String orgBuseoDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception{
		
		StringBuffer str = new StringBuffer();
		/* 상무이상 */
		List<EgovMap>	rstListPer = dashboardService.orgBuseoDiagMajrPolscore(searchVO);
		
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />진단항목 세부 내역</li> ");
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append("	        	<tr> ");
		str.append("	            	<th width=\"55%\">조직</th> ");
		str.append("	                <th width=\"15%\">건수</th> ");
		str.append("	                <th width=\"15%\">점수</th> ");
		//str.append("	                <th width=\"15%\">상태</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");
		
		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				
				str.append("<tr>");			
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(String.format("<a style='cursor:pointer;' class='btn_pol_status' majCode='%s' minCode='%s' polCode='%s' buseoType='%s'>%s</a>", searchVO.getMajCode(), "", "", "Y", row.get("orgnm")));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");

				str.append("</tr>");
				
				e = e + 1;
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='5'>정책 미설정 진단입니다.</td>");
			str.append("</tr>");
		}
		
		
		str.append("	        </tbody> ");
		str.append("	    </table> ");
		str.append("        </div> ");
		return str.toString();
	}
	
	
	/**
	 * ver3.0 부서장 : 하위 조직 총 검수/총 점수 
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String perDiagMajrOrgPolscore(DashboardSearchVO searchVO) throws Exception{
		/**
		 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
		 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
		 */
		
		StringBuffer str = new StringBuffer();
		
		//perDiagPolsumcount
				
		HashMap<String,Object> retMap = new HashMap<String,Object>();
		
		//retMap = dashboardService.perDiagMajrOrgPolscore(searchVO);/*개인*/
		
		List<EgovMap> rstListPer = dashboardService.perDiagMajrOrgPolscore(searchVO);		
			//majCode='A01' minCode='A02' polCode='PCG03'  loginType='personal' menutype='P' buseoindc='N'	
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />조직별 지수점수 - "+searchVO.getMajName()+"</li> ");
		//str.append(String.format("        <li class=\"cell2\">전체 %s개 정책 <span class=\"txcell1\">양호 %s개</span><span class=\"txcell2\">취약 %s개</span></li> ",retMap.get("tot"), retMap.get("goodness"), retMap.get("weekness") ));
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"  style=\"height: 408px;\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append(" 				<tr> ");
		str.append("	            	<th width=\"50%\">조직</th> ");
		str.append("	                <th width=\"25%\">건수</th> ");
		str.append("	                <th width=\"25%\">점수</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");

		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				
				str.append("<tr>");	
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("diagDesc"));
				str.append("</td>");
				
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");
				str.append("<tr>");	
				
				
				e = e + 1;
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='3'>하위 조직이 없습니다.</td>");
			str.append("</tr>");
		}
		
                                                                
		str.append("	        </tbody> ");
		str.append("	    </table> ");

		return str.toString();
	}
	/**
	 * ver3.0 부서장 : 하위 조직 총 검수/총 점수 
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	private String perDiagMajrUserPolscore(DashboardSearchVO searchVO) throws Exception{
		/**
		 * 개인 :: 진단점수 평균 및 대분류 항목별 건수 평균
		 * map <!-- 개인의 진단항목별 전체건수 및 점수 @emp_no -->
		 */
		
		StringBuffer str = new StringBuffer();
		
		//perDiagPolsumcount
				
		HashMap<String,Object> retMap = new HashMap<String,Object>();
		
		//retMap = dashboardService.perDiagMajrOrgPolscore(searchVO);/*개인*/
		
		List<EgovMap> rstListPer = dashboardService.perDiagMajrUserPolscore(searchVO);		
			//majCode='A01' minCode='A02' polCode='PCG03'  loginType='personal' menutype='P' buseoindc='N'	
		str.append("");
		str.append("        <div class=\"index_block3cell\"> ");
		str.append("    	<li class=\"cell1\"><img src=\"/img/icon_index.png\" />개인별 지수점수 - "+searchVO.getMajName()+"</li> ");
		//str.append(String.format("        <li class=\"cell2\">전체 %s개 정책 <span class=\"txcell1\">양호 %s개</span><span class=\"txcell2\">취약 %s개</span></li> ",retMap.get("tot"), retMap.get("goodness"), retMap.get("weekness") ));
		str.append("	    </div> ");
		str.append("		<div class=\"boxpol_list\"  style=\"height: 408px;\"> ");
		str.append("	    <table cellpadding=\"0\" cellspacing=\"0\" class=\"tblpol\"> ");
		str.append("	    	<thead> ");
		str.append(" 				<tr> ");
		str.append("	            	<th width=\"50%\">팀원</th> ");
		//str.append("	                <th width=\"25%\">건수</th> ");
		str.append("	                <th width=\"25%\">점수</th> ");
		str.append("	            </tr> ");
		str.append("	        </thead> ");
		str.append("	        <tbody> ");

		int e = 0;
		int o = 0;
		if(rstListPer.size() > 0){
			for(EgovMap row:rstListPer){
				
				o = e % 2 + 1;
				
				str.append("<tr>");	
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("diagDesc"));
				str.append("</td>");
				
				
				/*str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("count"));
				str.append("</td>");*/
				
				str.append(String.format("<td class=\"cell%s\">", o));
				str.append(row.get("score"));
				str.append("</td>");
				str.append("<tr>");	
				
				
				e = e + 1;
			}
		}else{
			str.append("<tr style='height:270px;'>");			
			str.append("<td class='cell1' style='border-bottom:solid 0px #fff;font-weight:bold;font:bold 18px '돋움';color:#616161;' colspan='3'>하위 조직이 없습니다.</td>");
			str.append("</tr>");
		}
		
                                                                
		str.append("	        </tbody> ");
		str.append("	    </table> ");

		return str.toString();
	}

}
