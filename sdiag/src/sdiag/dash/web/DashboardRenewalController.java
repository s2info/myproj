package sdiag.dash.web;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sdiag.util.DateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import sdiag.board.service.NoticeVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.MenuItemVO;
import sdiag.dash.service.DashboardRenewalService;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.DashboardService;
import sdiag.dash.service.GaugeItemValue;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserMainService;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.man.service.SearchVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.securityDay.service.SecurityDayService;
import sdiag.util.CommonUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.MajrCodeInfo;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class DashboardRenewalController {

	@Resource(name = "DashboardService")
	private DashboardService dashboardService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name= "DashboardRenewalService")
	private DashboardRenewalService dashrenewalService;
	
	@Resource(name = "PolicyService")
	private PolicyService polService;
	
	@Resource(name = "SecurityDayService")
	protected SecurityDayService securityDayService;
	
	@Resource(name = "UserMainService")
	protected UserMainService userMainService;
	
	@RequestMapping(value="/dash/dashboard.do")
	public String dashboard(HttpServletRequest request
			, HttpServletResponse response
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "home", comService);
		
		
		//로그인 사용자 정보 VO
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
		
		
		// security Day 점검 대상자 여부 확인 
		SdResultInfoVO sdUseYn = new SdResultInfoVO();
		
		sdUseYn = securityDayService.getSdYn(loginInfo.getUserid());
		
		model.addAttribute("sdUseYn", sdUseYn);
		

		//공지사항 팝업 List 조회
		SearchVO noticeSearchVO = new SearchVO();
		noticeSearchVO.setSq_no(0);
		noticeSearchVO.setSearchKeyword("Y");
		HashMap<String,Object> retMap3 = userMainService.getNoticeList(noticeSearchVO);
		List<NoticeVO> noticePopupList = (List<NoticeVO>)retMap3.get("list");
		
		model.addAttribute("noticePopupList", noticePopupList);
				
		/*
		 * 1. 공지사항 , FAQ 상위 5건 조회
		 */
		HashMap<String, Object> noticeMap = comService.getMainNoticeNFaqListInfo();
		List<NoticeVO> noticeList = (List<NoticeVO>)noticeMap.get("noticeList");
		List<NoticeVO> faqList = (List<NoticeVO>)noticeMap.get("faqList");
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("faqList", faqList);
		
		/**
		 * 1. 전사 점수 (공통)
		 * 2. 개인(부서장), 팀, 상위팀 점수
		 * 3. 정책별 결과 
		 */
		DashboardSearchVO searchVO = new DashboardSearchVO();
		searchVO.setOrg_root_code(MajrCodeInfo.RootOrgCode);
		List<UserPolIdxInfoVO> policyScoreList = new ArrayList<UserPolIdxInfoVO>();
		try{
			if(auth.equals("1")){
				//관리자
				searchVO.setEmp_no(empno);
				searchVO.setOrg_code(searchVO.getOrg_root_code());
				
				policyScoreList = dashrenewalService.getLastOrgPolicyScore(searchVO);
			}else if(auth.equals("2")){
				/**
				 * 조직장 또는 대무자
				 * 대무자의 경우 대무자 부서장 권한 으로 실행
				 */
				if(orgCode.equals(requestType)){
					//조직장
					searchVO.setEmp_no(empno);
					searchVO.setOrg_code(orgCode);
				}else{
					// 대무자 일때 해당 조직장의 사번으로 조회
					OrgGroupVO orgInfo = comService.getOrgInfo(requestType);
					searchVO.setEmp_no(orgInfo.getOrg_cap_no());
					searchVO.setOrg_code(requestType);
				}
				policyScoreList = dashrenewalService.getLastOrgPolicyScore(searchVO);
			}else{
				/**
				 * 개인 
				 */
				searchVO.setEmp_no(empno);
				searchVO.setOrg_code(orgCode);
				policyScoreList = dashrenewalService.getLastUserPolicyScore(searchVO);
			}
		
			//전사점수 및 개인, 팀, 상위 팀 점수 조회(협력사의 경우 협력사만 표시)
		
			UserMainIdxInfoVO totalUserInfo = dashrenewalService.getLastUserCollectScore(searchVO);
			totalUserInfo.setGraph_panel_width("30%");
			if(totalUserInfo.getIscollabor_cap().equals("Y")){
				totalUserInfo.setGraph_panel_width("20%");
			}
			
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
			
			totalUserInfo.setScorelist_panel_height(policyScoreList.size() * 80 + "px");
			
			List<CodeInfoVO> scoreStat = comService.getCodeInfoListNoTitle("C05");
			// getCodeInfo(scoreStat, "GOD", "ADDINFO")
			HashMap<String, String> scoreColor = new HashMap<String, String>();
			scoreColor.put("GOOD", getCodeInfo(scoreStat, "GOD", "ADDINFO"));
			scoreColor.put("CAUTION", getCodeInfo(scoreStat, "CUT", "ADDINFO"));
			scoreColor.put("WARNING", getCodeInfo(scoreStat, "WAN", "ADDINFO"));
			model.addAttribute("scoreColor", scoreColor);
			model.addAttribute("totalUserInfo", totalUserInfo);
			model.addAttribute("policyScoreList", policyScoreList);
			model.addAttribute("auth", auth);
			model.addAttribute("searchVO", searchVO);
			model.addAttribute("nowDate", DateUtil.getDateAdd(Calendar.DATE, -1));
			
			HashMap<String, Object> readcntInfo = dashrenewalService.getBoardReadCountInfo(empno);
			model.addAttribute("readcntInfo", readcntInfo);
			
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return "dash/dashboardrenewal";
	}
	/**
	 * 정책별 상세 정보 조회
	 * @param request
	 * @param response
	 * @param searchVO
	 * @throws Exception
	 */
	@RequestMapping(value="/dash/policyResultForAjax.do")
	public void policyResultForAjax(HttpServletRequest request
			, HttpServletResponse response
			, DashboardSearchVO searchVO) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	   
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	//System.out.println(searchVO.getEmp_no() + "][" + searchVO.getMajrCode() + "][" + searchVO.getOrg_code() + "][" + searchVO.getSearchType());
	    	System.out.println(searchVO.getSearch_date() + ":Searech Date");
	    	StringBuffer tablebody = new StringBuffer();
	    	tablebody.append("<table class=\"scroll_list\" id=\"table1\" style=\"width:100%;\">");
	    	
	    	String requestType = (String)request.getSession().getAttribute("loginType");
	    	if(requestType.equals("personal")){
	    		searchVO.setSearchType("3");
			}else if(requestType.equals("sa")){
				searchVO.setSearchType("1");
			}else{
				searchVO.setSearchType("2");
			}
	    	
	    	
	    	
	    	if(searchVO.getSearchType().equals("3")){
	    		//개인 이면 
	    		searchVO.setSearchCondition("P");
	    		tablebody.append("<thead><tr>");
	    		tablebody.append("<th width=\"15%\">수집날짜</th>");
	    		tablebody.append("<th width=\"25%\">정책</th>");
		    	tablebody.append("<th width=\"15%\">건수</th>");
		    	tablebody.append("<th width=\"15%\">평균점수</th>");
		    	tablebody.append("<th width=\"15%\">상태</th>");
		    	tablebody.append("<th width=\"15%\">상세</th>");
		    	tablebody.append("</tr></thead>");
	    	}else{
	    		if(searchVO.getMajrCode().equals("") && searchVO.getSearchType().equals("1")){
	    			//관리자 이면
	    			searchVO.setSearchCondition("R");
	    			tablebody.append("<thead><tr>");
	    			tablebody.append("<th width=\"15%\">수집날짜</th>");
		    		tablebody.append("<th width=\"25%\">부서</th>");
			    	tablebody.append("<th width=\"15%\">건수</th>");
			    	tablebody.append("<th width=\"15%\">평균점수</th>");
			    	tablebody.append("<th width=\"15%\">상태</th>");
			    	tablebody.append("<th width=\"15%\">상세</th>");
			    	tablebody.append("</tr></thead>");
	    		}else if(!requestType.equals("personal")){
	    			//부서장 이면
	    			searchVO.setSearchCondition("L");
	    			if(searchVO.getIsSubOrg().equals("Y")){
	    				//하위부서가 있으면 부서점수
	    				tablebody.append("<thead><tr>");
	    				tablebody.append("<th width=\"15%\">수집날짜</th>");
	    	    		tablebody.append("<th width=\"25%\">부서</th>");
	    		    	tablebody.append("<th width=\"15%\">건수</th>");
	    		    	tablebody.append("<th width=\"15%\">평균점수</th>");
	    		    	tablebody.append("<th width=\"15%\">상태</th>");
	    		    	tablebody.append("<th width=\"15%\">상세</th>");
	    		    	tablebody.append("</tr></thead>");
	    			}else{
	    				//팀장 이면 하위 팀원 점수
	    				tablebody.append("<thead><tr>");
	    				tablebody.append("<th width=\"13%\">수집날짜</th>");
	    	    		tablebody.append("<th width=\"21%\">부서</th>");
	    	    		tablebody.append("<th width=\"13%\">성명</th>");
	    		    	tablebody.append("<th width=\"13%\">건수</th>");
	    		    	tablebody.append("<th width=\"13%\">평균점수</th>");
	    		    	tablebody.append("<th width=\"13%\">상태</th>");
	    		    	tablebody.append("<th width=\"13%\">상세</th>");
	    		    	tablebody.append("</tr></thead>");
	    			}
	    		}
	    		
	    	}
	    	List<UserPolIdxInfoVO> resultList = dashrenewalService.getDetailScoreList(searchVO);
	    	tablebody.append("<tbody result_detail_body>");
	    	int goodCnt = 0;
	    	int cautionCnt = 0;
	    	int warningCnt = 0;
	    	if(resultList != null){
	    		if(resultList.size() > 0){
	    			for(UserPolIdxInfoVO row:resultList){
	    				if(row.getScore().equals("-999")){
	    					continue;
	    				}
	    				
	    				tablebody.append("<tr>");
	    				tablebody.append(String.format("<td class=\"cell1\">%s</td>", row.getRgdt_date()));
	    				tablebody.append(String.format("<td class=\"cell1\">%s</td>", searchVO.getSearchType().equals("3") ? row.getDiag_desc() : row.getOrg_nm() ));
	    				if(!searchVO.getIsSubOrg().equals("Y") && !searchVO.getSearchType().equals("3")){
	    					tablebody.append(String.format("<td class=\"cell1\">%s</td>", row.getEmp_nm()));
	    				}
	    				tablebody.append(String.format("<td class=\"cell1\">%s</td>", row.getCount().equals("-999") ? "-" : row.getCount()));
	    				tablebody.append(String.format("<td class=\"cell1\">%s점</td>", row.getScore().equals("-999") ? "-" : row.getScore()));
	    				if(row.getScore().equals("-999")){
	    					tablebody.append(String.format("<td class=\"cell1\"><span class=\"scondition1\" style='background:%s;border:1px solid %s;'>%s</span></td>", "#f1f1f1", "#f1f1f1", "-" ));
	    				}else{
	    					tablebody.append(String.format("<td class=\"cell1\"><span class=\"scondition1\" style='background:%s;border:1px solid %s;'>%s</span></td>", row.getScorestat_color(), row.getScorestat_color(), row.getScorestat_name() ));
	    				}
	    				
	    				if(searchVO.getSearchType().equals("3")){
	    					//개인 이면 정책별 결과 팝업 실행
	    					tablebody.append(String.format("<td class=\"cell1\"><a style='cursor:pointer;' class=\"btn_details btn_policy_view\" secPolId='%s' emp_no='%s' searchDate='%s' >상세보기</a></td>"
	    							, row.getDiag_majr_code()
	    							, row.getEmp_no()
	    							, searchVO.getSearch_date() /*row.getSearch_rgdt_date()*/));
	    				}else{
	    					//개인이 아닐때 정책별 전체 로그 출력 팝업 실행
	    					if(searchVO.getIsSubOrg().equals("Y")){
		    					tablebody.append(String.format("<td class=\"cell1\"><a style='cursor:pointer;' class=\"btn_details btn_totallog_view\" diagMajrCode='%s' org_code='%s' emp_no='' searchDate='%s' is_sub_org='%s'>상세보기</a></td>"
		    							, searchVO.getMajrCode()
		    							, row.getOrg_code()
		    							, searchVO.getSearch_date() /*row.getSearch_rgdt_date()*/
		    							, searchVO.getIsSubOrg()));
	    					}else{
	    						tablebody.append(String.format("<td class=\"cell1\"><a style='cursor:pointer;' class=\"btn_details btn_totallog_view\" diagMajrCode='%s' org_code='%s' emp_no='%s' searchDate='%s' is_sub_org='%s'>상세보기</a></td>"
		    							, searchVO.getMajrCode()
		    							, row.getOrg_code()
		    							, row.getEmp_no()
		    							, searchVO.getSearch_date() /*row.getSearch_rgdt_date()*/
		    							, searchVO.getIsSubOrg()));
	    					}
	    				}
	    				
	    				tablebody.append("</tr>");
	    				if(!row.getScore().equals("-999")){
	    					if(row.getScorestat().equals("GOD")){
		    					goodCnt++;
		    				}else if(row.getScorestat().equals("CUT")){
		    					cautionCnt++;
		    				}else if(row.getScorestat().equals("WAN")){
		    					warningCnt++;
		    				}
	    				}
	    				
	    			}
	    		}else{
	    			tablebody.append("<tr style=\"height:300px;\"><td colspan=\"7\" class=\"cell1\">결과값이 없습니다.</td>");
	    		}
	    	}else{
	    		tablebody.append("<tr style=\"height:300px;\"><td colspan=\"7\" class=\"cell1\">결과값이 없습니다.</td>");
	    	}
	    	tablebody.append("</tbody>");
	    	tablebody.append("</table>");
	    	String lastDate = searchVO.getSearch_date().replace("-", ""); //dashrenewalService.getLastData();
	    	List<CodeInfoVO> scoreStat = comService.getCodeInfoListNoTitle("C05");
	    	String result_title = String.format("<img src=\"/img/icon_index.png\" />%s 지수점수 > %s", searchVO.getSearchType().equals("3") ? "정책별" : searchVO.getIsSubOrg().equals("Y") ? "하위 부서별" : "직원별", searchVO.getSearchType().equals("3") ? "전체보기" :searchVO.getMajrName());
	    	String result_status = String.format("전체 %s %s %s %s %s %s"
	    			, resultList.size()
	    			, searchVO.getSearchType().equals("3") ? "개 정책" : searchVO.getIsSubOrg().equals("Y") ? "개 부서" :"명"
	    			, String.format("<span class=\"txcell1\" style='background:%s;border:1px solid %s;'>%s %s개</span>", getCodeInfo(scoreStat, "GOD", "ADDINFO"), getCodeInfo(scoreStat, "GOD", "ADDINFO"), getCodeInfo(scoreStat, "GOD", "DESC"), goodCnt)
	    			, String.format("<span class=\"txcell1\" style='background:%s;border:1px solid %s;'>%s %s개</span>", getCodeInfo(scoreStat, "CUT", "ADDINFO"), getCodeInfo(scoreStat, "CUT", "ADDINFO"), getCodeInfo(scoreStat, "CUT", "DESC"), cautionCnt)
	    			, String.format("<span class=\"txcell1\" style='background:%s;border:1px solid %s;'>%s %s개</span>", getCodeInfo(scoreStat, "WAN", "ADDINFO"), getCodeInfo(scoreStat, "WAN", "ADDINFO"), getCodeInfo(scoreStat, "WAN", "DESC"), warningCnt)
	    			, String.format("<span><a class=\"txcell3 btn_totallog_excel\" style='cursor:pointer;' majrCode='%s' org_code='%s' searchDate='%s' isSubOrg='%s' emp_no='%s'><img src=\"/img/icon_excel.png\" /> EXCEL</a></span>", searchVO.getMajrCode(), searchVO.getOrg_code(), lastDate, searchVO.getSearchType().equals("3") ? "N" : "Y", searchVO.getEmp_no()));
	    	
	    	isOk = true;
	    	hMap.put("tablebody", tablebody.toString());
	    	hMap.put("result_title", result_title);
	    	hMap.put("result_status", result_status);
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		  
		}
		catch(Exception ex)
		{
			msg = ex.toString();
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
			
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
	   
	}	
	
	private String getCodeInfo(List<CodeInfoVO> codeList, String minrCode, String retDiv){
		String retValue = "";
		for(CodeInfoVO row:codeList){
			if(row.getMinr_code().equals(minrCode)){
				if(retDiv.equals("DESC")){
					retValue = row.getCode_desc();
				}else{
					retValue = row.getAdd_info1();
				}
			}
		}
		return retValue;
	}
	
	/**
	 * 지수화 상세 정보 조회
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dash/detailLogViewPopup.do")
	public String detailLogViewPopup(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("PolicySearchVO") PolicySearchVO searchVO					
			, ModelMap model) throws Exception{	
		try{
		Gson gson = new Gson();
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map = (HashMap<String,Object>) gson.fromJson(param, map.getClass());
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		searchVO.setBuseoType(map.get("buseoType").toString());
		searchVO.setBegin_date(map.get("searchDate").toString());
		searchVO.setOrg_upper(map.get("org_code").toString());
		searchVO.setMajCode(map.get("majrCode").toString());
		HashMap<String,Object> retMap = null;
		
		if(searchVO.getBuseoType().equals("N")){ //하위 부서 존재 여부
			searchVO.setSearchCondition("1");
			searchVO.setSearchKeyword(map.get("emp_no").toString());
			retMap = polService.getUserSearchPolDetailResultList(searchVO);
    	}else{
    		//하위 부서 존재 함
    		retMap = polService.getUserOrgSearchPolDetailResultList(searchVO);
    	}
		List<EgovMap> result = (List<EgovMap>)retMap.get("list");
		
		int totalCnt = (int)retMap.get("totalCount");
    	int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 	
    	//System.out.println(TotPage + " Page][" + map.get("majrCode") + "][" + map.get("org_code") + "][" + map.get("emp_no") + "][" + map.get("searchDate") + "][" + map.get("buseoType"));
    	model.addAttribute("searchVO", searchVO);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("resultList",result);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "dash/detailLogViewPopup";
	}
	
	/**
	 * 지수화 상세 정보 엑셀저장
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dash/detailLogViewPopupExportExcel.do")
	public String detailLogViewPopupExportExcel(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("PolicySearchVO") PolicySearchVO searchVO					
			, ModelMap model) throws Exception{	
		try{
		Gson gson = new Gson();
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map = (HashMap<String,Object>) gson.fromJson(param, map.getClass());
		
		searchVO.setBuseoType(map.get("buseoType").toString());
		searchVO.setBegin_date(map.get("searchDate").toString());
		searchVO.setOrg_upper(map.get("org_code").toString());
		searchVO.setMajCode(map.get("majrCode").toString());
		List<HashMap<String,Object>> list = null;
		String fileName = String.format("지수화정책진단결과_상세_%s", searchVO.getBegin_date());
		if(searchVO.getBuseoType().equals("N")){ //하위 부서 존재 여부
			searchVO.setSearchCondition("1");
			searchVO.setSearchKeyword(map.get("emp_no").toString());
			//retMap = polService.getUserSearchPolDetailResultList(searchVO);
			list = polService.getUserSearchPolDetailResultListForExportExcel(searchVO);
    	}else{
    		//하위 부서 존재 함
    		list = polService.getUserSearchPolDetailResultListForExcelList(searchVO);
    	}
		String FileName = URLEncoder.encode(fileName, "UTF-8").replace("\\", "%20");
		//ExcelUtil.stringToExportCsv(response, setContent(list), FileName);
		
		List<String[]> contents = setCSVContents(list);
		
		ExcelUtil.hashMapToExportCsv(response, FileName, contents);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "dash/detailLogViewPopup";
	}
	
	private List<String[]> setCSVContents(List<HashMap<String, Object>> list){
		List<String[]> csvList = new ArrayList<String[]>();
		if(list.size() > 0){
			HashMap<String, Object> log = (HashMap<String, Object>)list.get(0);
			String heard = "";
			for(Object key:log.keySet())
			{
				//csvList.add(new String[]{"", ""});// .append(ConvertToColumnName(key.toString()) + ",");
				heard += ConvertToColumnName(key.toString()) + ",";
			}
			csvList.add(heard.split(","));
			
			for(HashMap<String, Object> row:list)
			{
				String contents = "";
				for(Object key:row.keySet()){
					//data.append(row.get(key).toString()+ ",");
					contents += row.get(key).toString()+ ",";
				}
				csvList.add(contents.split(","));
			}
		}
		return csvList;
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "org_code" : ColumnName = "부서코드";break;
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "이름";break;
		case "org_nm" : ColumnName = "조직명";break;
		case "org_position" : ColumnName = "조직 계위";break;
		case "capnm" :  ColumnName = "조직장명";break;
		case "capno" : ColumnName = "조직장사번";break;
		case "score" : ColumnName = "점수";break;
		case "count" : ColumnName = "건수";break;
		case "avg" : ColumnName = "평균";break;
		case "idx_rgdt_date" : ColumnName = "진단일";break;
		case "mac" : ColumnName = "MAC";break;
		case "ip" : ColumnName = "IP";break;
		case "sec_pol_desc" : ColumnName = "지수화 정책명";break;
		case "is_expn" : ColumnName = "예외자 여부";break;
		case "is_appr" : ColumnName = "소명 여부";break;
		case "appr_desc" : ColumnName = "소명 상태";break;
		case "score_type" : ColumnName = "진단 상태";break;
		case "is_sanct" : ColumnName = "제재 여부";break;
		case "sanct_type" : ColumnName = "제재 유형";break;
		case "is_pcg_act" : ColumnName = "PC지키미 실행여부";break;
		case "pcg_act_desc" : ColumnName = "PC지키미 실행명";break;
		case "pcg_act_para" : ColumnName = "PC지키미 실행코드";break;
		case "pol_idx_id" : ColumnName = "지수화정책 코드";break;
		case "empno" : ColumnName = "사번";break;
		case "path" : ColumnName = "파일 경로";break;
		case "username" : ColumnName = "성명";break;
		case "department" : ColumnName = "부서";break;
		case "userbizname" : ColumnName = "회사명";break;
		case "userbizno" : ColumnName = "사업자 번호";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
}