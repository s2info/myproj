package sdiag.dash.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sdiag.dash.service.DashboardService;
import sdiag.dash.service.DashboardSearchVO;
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
public class DashboardController  {
	
	@Resource(name = "DashboardService")
	private DashboardService dashboardService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;	
	
	@RequestMapping(value="/dash/dashboard-----.do")
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
		String auth = loginInfo.getRole_code();// getUser_auth(request);
		String orgCode = loginInfo.getOrgcode();// getOrg_code(request);
				
		String emp_no = loginInfo.getUserid();// getEmp_no(request);
		
		String mmEmp_no = loginInfo.getUserid();;//getEmp_no(request);;
		
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
		//UserManageVO proxyUser = dashboardService.getProxyUser(emp_no);
		/*if (proxyUser != null){
			
			if(!dpflag.equals("personal")){
				emp_no = proxyUser.getEmp_no();  // 대무자이면 emp_no 재설정
			}
			user_authnm = "대무자:"+emp_no;
		}		
		*/
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
			
			System.out.println("조직장!!!!");
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
					System.out.println("-------------------------------------------------------------");
					System.out.println("#########"+list_uinfo.get(u).getUnder_code() + "(orglevel:" + list_uinfo.get(u).getOrg_level() + ")");
					System.out.println("-------------------------------------------------------------");

					soc_org_code = list_uinfo.get(u).getUnder_code();
					org_level = list_uinfo.get(u).getOrg_level();
					
					System.out.println("-------------------------------------------------------------");
					System.out.println("==========1=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
					System.out.println("-------------------------------------------------------------");
					
					if(orgcodeMode.equals(soc_org_code)){
						//soc_org_code = orgcodeMode;
						orgcodeModeFlag = true;
						System.out.println("-------------------------------------------------------------");
						System.out.println("==========2=========orgcodeMode==soc_org_code" + orgcodeMode + "==" + soc_org_code);
						System.out.println("-------------------------------------------------------------");
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
		}
		
		if(orgcodeModeFlag){
			soc_org_code = orgcodeMode;
		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("soc_org_code:" + soc_org_code + "//" + "org_level:" + org_level);
		System.out.println("-------------------------------------------------------------");

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
				
		String dpUserinfo = userInfo.getOrg_nm() + " : " + userInfo.getEmp_nm();
		dpUserinfo += " " + userInfo.getTitle_nm();
				
	    int isRowType;
		
	    List<EgovMap> resultList = null;
	    
		System.out.println("-------------------------------------------------------------");
		/** parameter : emp_no */
		List<EgovMap> caporgList = dashboardService.getUserInfoCap(searchVO); //조직 정보조회
		System.out.println("===============================================caporgList.size=" + caporgList.size());	
	    
	    /** parameter : upper_org_code */	    
	    List<EgovMap> orgorgList = dashboardService.getOrgSubList(searchVO); // 하위조직 조회
	    System.out.println("===============================================orgorgList.size=" + orgorgList.size());
	    
	    /* 관리자인경우 조직코드 설정 */
		if(dpflag.equals("sa")){
			orgCode = "000001";
		}	    
	    
		/**
		 * 정책 지수점수 리스트 처리부분
		 * */
		try{
			HashMap<String,Object> retMap = new HashMap<String,Object>();
			List<UserIdxInfoCurrVO> list = new ArrayList<UserIdxInfoCurrVO>();	
			
			System.out.println("caporglistsize:" + caporgList.size());
			System.out.println("하위조직??:" + orgorgList.size());
			
			if ( (caporgList.size() > 1 && !dpflag.equals("personal")) || dpflag.equals("sa") ){  // 조직장 직책 겸임 관리자
		    
		    	if(dpflag.equals("sa")){ // 관리자이면, 하위조직이 있으면
		    		resultList = new ArrayList<EgovMap>();
		    		
			    	for(EgovMap row:orgorgList){
			    		searchVO.setOrg_upper(row.get("orgcode").toString());
			    		EgovMap rList = dashboardService.getOrgResultInfo(searchVO);
			    				    		
			    		if(rList != null){
			    			resultList.add(rList);			  // 지수점수 결과리스트
			    		}
			    	}
			    	model.addAttribute("resultListJisuBody", resultList);
			    	isRowType = 2;
			    	
		    	}else{
		    		if(orgorgList.size() > 0){
			    		resultList = new ArrayList<EgovMap>();
				    	for(EgovMap row:caporgList){
				    		searchVO.setOrg_upper(row.get("orgcode").toString());
				    		
							System.out.println("-------------------------------------------------------------");
				    		System.out.println("=============orgcode:================="+row.get("orgcode").toString());
							System.out.println("-------------------------------------------------------------");

				    		EgovMap rList = dashboardService.getOrgResultInfo(searchVO);
				    		if(rList != null){
				    			resultList.add(rList);
				    		}
				    	}
				    	model.addAttribute("resultListJisuBody", resultList);
				    	isRowType = 2;
		    		}else{			    	
		        		resultList = dashboardService.getUserOrgResultList(searchVO);
		        		model.addAttribute("resultListJisuBody", resultList);
		        		isRowType = 1; 
		    		}
		    	}
			}else if(orgorgList.size() > 0 && !dpflag.equals("personal")){  // 대무자일때 caporgList가 1개 이며 orgList가 존재
				
	    		resultList = new ArrayList<EgovMap>();    		
		    	for(EgovMap row:orgorgList){
		    		searchVO.setOrg_upper(row.get("orgcode").toString());
		    		EgovMap rList = dashboardService.getOrgResultInfo(searchVO);
			    		
		    		if(rList != null){
		    			resultList.add(rList);			  // 지수점수 결과리스트
		    		}
		    	}
		    	model.addAttribute("resultListJisuBody", resultList);			
		    	isRowType = 2;	
				
				
	    	}else{//하위조직이 없으면 팀원/정책조회
	    		
	    		if (!auth.equals("3") && !dpflag.equals("personal")){
	        		resultList = dashboardService.getUserOrgResultList(searchVO);
	        		model.addAttribute("resultListJisuBody", resultList);
	        		isRowType = 1;    			
	    		}else{
	    			/* 정책 지수별 점수 리스트 */
	    			retMap = dashboardService.getUserIdxInfoCurr(emp_no);
	    			list = (List<UserIdxInfoCurrVO>)retMap.get("list");		
	    			model.addAttribute("resultListJisuBody", list);
	    			isRowType = 0;
	    		}
	
	    	}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/**
		 * V2.0 
		 * 현재점수 조회 [1]-[2]
		 * 현재조직 & 개인
		 */
		try{
			int curScore = 1;
			String curScoreUserInfo = "";
			String curScoreUserOrgName = "";
			String curScoreUserStat = "";
			
			UserIdxInfoCurrVO rmapOrgCurScore = new UserIdxInfoCurrVO();			
			UserIdxInfoCurrVO rmapPerCurScore = new UserIdxInfoCurrVO();
			
			System.out.println("-------------------------------------------------------------");
			System.out.println("-보안현재점수 조회-------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			
			rmapOrgCurScore = dashboardService.getOrgCurScore(orgCode);
			rmapPerCurScore = dashboardService.getPerCurScore(emp_no);
			
			System.out.println("===========caporgList.size()=========>>"+caporgList.size());
			System.out.println("===========orgList.size()=========>>"+caporgList.size());
			
			if( (caporgList.size() > 1 && !dpflag.equals("personal")) || dpflag.equals("sa") || (auth.equals("2") && !dpflag.equals("personal"))){
				
				System.out.println("-------------------------------------------------------------");
				System.out.println("조직점수=====================================================");
				System.out.println("-------------------------------------------------------------");
							
				/** 조직점수 */
				if (rmapOrgCurScore != null){
					curScore = rmapOrgCurScore.getScore();
					curScoreUserOrgName = rmapOrgCurScore.getOrg_nm();   //userInfo.getOrg_nm();
				}
			
			}else{
				System.out.println("-------------------------------------------------------------");
				System.out.println("개인점수=====================================================");
				System.out.println("-------------------------------------------------------------");
				
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
			if (curScore <= 100 && curScore >= 80){
				curScoreUserStat = "양호";
			}else if (curScore < 80 && curScore >= 40){
				curScoreUserStat = "주의";
			}else{
				curScoreUserStat = "위험";
			}			
			
			model.addAttribute("curScore", curScore);
			model.addAttribute("curScoreUserStat", curScoreUserStat);
			model.addAttribute("curScoreUserInfo", curScoreUserInfo);
			model.addAttribute("curScoreUserOrgName", curScoreUserOrgName);
			
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
					
					rmapOrgSumInfoTrend = dashboardService.getOrgSumInfoTrend(lstOrgCodeUpperd.get(i).getOrg_code());
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
				
			}
			
			model.addAttribute("plotLines", plotLines);
			
			model.addAttribute("labelList", labelList);
			
			model.addAttribute("chartList_per", chartList_per);
			
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
			
			retMap_Orgidxcnt = dashboardService.getOrgIdxCount(orgCode);			
			retMap_Peridxcnt = dashboardService.getPerIdxCount(emp_no);
			
			String stx_data = ""; // 지수별 점수 리스트
			String stx_tick = ""; // 지수별 항목 리스트
			
			System.out.println("-------------------------------------------------------------");
			System.out.println("진단항목별 발생건수============================>>>>>>>>>>>>>>>");
			System.out.println("-------------------------------------------------------------");

			
			if( (orgorgList.size() > 0 && !dpflag.equals("personal")) || dpflag.equals("sa") || (auth.equals("2") && !dpflag.equals("personal"))){
				
				/** 조직건수 */
				if (retMap_Orgidxcnt != null){
					list_Orgidxcnt = (List<UserIdxInfoCurrVO>)retMap_Orgidxcnt.get("list");
					
					for(int i = 0; i < list_Orgidxcnt.size(); i++){
						stx_data += list_Orgidxcnt.get(i).getCount();
						
						if(Integer.parseInt((list_Orgidxcnt.get(i).getCount())) > 0){
							/*건수제거*/
							//stx_tick += "'" + list_Orgidxcnt.get(i).getDiag_desc() + "<br>" + list_Orgidxcnt.get(i).getCount() + "건'";
							stx_tick += "'" + list_Orgidxcnt.get(i).getDiag_desc() + "'";
						}else{
							stx_tick += "'" + list_Orgidxcnt.get(i).getDiag_desc() + "'";
						}
						
						if (i != list_Orgidxcnt.size()-1){
							stx_data += ',';
							stx_tick += ',';
						}						
						
					}
				}
			
			}else{
				System.out.println("-------------------------------------------------------------");
				System.out.println("진단항목별 발생건수 개인============================retMap_Peridxcnt.size>>>>>>>>>>>>>>>" + retMap_Peridxcnt.size());
				System.out.println("-------------------------------------------------------------");

				/** 개인건수 */
				if (retMap_Peridxcnt != null){
					
					System.out.println("진단항목별 발생건수 개인============================list_Peridxcnt Start>>>>>>>>>>>>>>>");
					list_Peridxcnt = (List<UserIdxInfoCurrVO>)retMap_Peridxcnt.get("list");
					System.out.println("진단항목별 발생건수 개인============================list_Peridxcnt.size() End>>>>>>>>>>>>>>>" + list_Peridxcnt.size());
					
					for(int i = 0; i < list_Peridxcnt.size(); i++){
						stx_data += list_Peridxcnt.get(i).getCount();
						stx_tick += "'"+list_Peridxcnt.get(i).getDiag_desc()+"'";
						if (i != list_Peridxcnt.size()-1){
							stx_data += ',';
							stx_tick += ',';
						}						
						
					}					
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

		return "/dash/dashboard";
	}	

}
