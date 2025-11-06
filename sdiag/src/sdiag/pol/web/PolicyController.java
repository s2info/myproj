package sdiag.pol.web;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sdiag.com.service.ApprInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.FileVO;
import sdiag.com.service.SdiagProperties;
import sdiag.getdata.service.GetDataService;
import sdiag.login.web.CommonController;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SecPolService;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.util.CommonUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.FileManager;
import sdiag.util.HTMLInputFilter;
import sdiag.util.MajrCodeInfo;
import webdecorder.CipherBPMUtil;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class PolicyController {

	@Resource(name = "PolicyService")
	private PolicyService polService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "SecPolService")
	private SecPolService secPolService;
	
	// 파일 처리 클래스 선언
	@Resource(name = "FileManager")
	private FileManager fileManager;
	
	@Resource(name = "GetDataService")
	private GetDataService getDataService;
	
	@RequestMapping(value="/pol/policystatus.do")
	public String policystatus(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("PolicySearchVO") PolicySearchVO searchVO					
			, ModelMap model) throws Exception{	
		
		CommonUtil.topMenuToString(request, searchVO.getMajCode(), comService, "P", searchVO.getBuseoType());	
		CommonUtil.polLeftMenuToString(request, searchVO.getMajCode(), searchVO.getMinCode(), searchVO.getPolCode(), comService);
		
		if(searchVO.getBuseoType().equals("Y")){
			searchVO.setSearchKeyword("");
		}
		
		
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
		
		 /*********************************************/
		boolean isUser = auth == "1" ? false : true;
		/*
		if(request.getParameter("majCode") != null && !request.getParameter("majCode").toString().equals("")){
			searchVO.setMajCode(request.getParameter("majCode"));
		}
		if(request.getParameter("minCode") != null && !request.getParameter("minCode").toString().equals("")){
			searchVO.setMinCode(request.getParameter("minCode"));
		}
		if(request.getParameter("polCode") != null && !request.getParameter("polCode").toString().equals("")){
			searchVO.setPolCode(request.getParameter("polCode"));
		}
		*/
		HashMap<String, String> codemap = new HashMap<String, String>();
		codemap.put("majcode", searchVO.getMajCode());
		codemap.put("mincode", searchVO.getMinCode());
		codemap.put("polcode", searchVO.getPolCode());
		
		model.addAttribute("polInfo", codemap);
		model.addAttribute("polStatString", CommonUtil.getPolString(request, codemap, comService));
		
		//오늘 날짜 셋팅
		
		Date date = new Date();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("nowDate", fm.format(date));
		String begin_Date = searchVO.getBegin_date();
		if(searchVO.getBegin_date().equals("")){
			
			begin_Date = polService.getUserInfoLastDate(searchVO);

			searchVO.setBegin_date(begin_Date);
			System.out.println(begin_Date + "][begin_Date");
		}
		//검색날짜 조회 - 최종날짜 조회
		model.addAttribute("getdate", begin_Date);
		
		
		StringBuffer org_body = new StringBuffer();
		if(Integer.parseInt(auth) == 1)
		{
			isUser = false;
			auth = "1";
			orgCode = "";
		}else if(Integer.parseInt(auth) == 2){
			isUser = false;
			orgCode=requestType;
		}
		
		HashMap<String, String> codeInfo = new HashMap<String, String>();
		codeInfo.put("majCode", "PIX");
		codeInfo.put("minCode", "02");
		
		
		//협력사 지수 여부
		CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
		String isKpc = isKpcInfo.getAdd_info1();
			
		CommonUtil.getCreateOrgTree(request, comService, orgCode, isUser, empno, isKpc);
		
		model.addAttribute("auth", auth);
		model.addAttribute("emp", auth.equals("3") ? empno : searchVO.getSearchKeyword());
		model.addAttribute("orgbody", org_body);
		model.addAttribute("istot", auth.equals("1") ? "Y" : "N");
		
		if(auth.equals("3")){
			searchVO.setSearchCondition("1");
		}
		
		return "pol/status";
	}
	
	
	
	private String CreateOrgCheckBoxHtml(List<OrgGroupVO> orgList, String orgType, String orgIndex, String orgLevel)
	{
		StringBuffer checkBox_body = new StringBuffer();
		boolean is_orgexists = false;
		if(Integer.parseInt(orgType) <= Integer.parseInt(orgLevel)){
			if(orgList != null)
			{
				for(OrgGroupVO srow:orgList){
					if(orgIndex.indexOf(srow.getOrg_code()) != -1)
					{
						is_orgexists = true;
						break;
					}
					
				}
			}
		}
	
		checkBox_body.append(String.format("<li><select id='org_%s' name='%s' class='selected_org' norg='%d' %s style='width:120px'>"
										   , orgType
										   , ConvertToOrgTitle(orgType, true)
										   , Integer.parseInt(orgType) + 1
										   , is_orgexists ? "disabled" : ""));
										   
		checkBox_body.append(String.format("<option value='%s' >%s</option>", "", ConvertToOrgTitle(orgType, false)));
		if(orgList != null)
		{
			for(OrgGroupVO srow:orgList){
				boolean is_selected = false;
				is_selected = orgIndex.indexOf(srow.getOrg_code()) != -1;
				
				checkBox_body.append(String.format("<option value='%s' %s>%s</option>"
													, srow.getOrg_code()
													, is_selected ? "selected" : ""
													, srow.getOrg_nm()));
			}
		}
		checkBox_body.append("</select></li>");
		
		return checkBox_body.toString();
	}
	
	private String CreateOrgCheckBoxHtml(List<OrgGroupVO> orgList, String orgType)
	{
		StringBuffer checkBox_body = new StringBuffer();
	
		checkBox_body.append(String.format("<li><select id='org_%s' name='%s' class='selected_org' norg='%d' style='width:120px'>"
										   , orgType
										   , ConvertToOrgTitle(orgType, true)
										   , Integer.parseInt(orgType) + 1));
		checkBox_body.append(String.format("<option value='%s' >%s</option>", "", ConvertToOrgTitle(orgType, false)));
		if(orgList != null)
		{
			for(OrgGroupVO srow:orgList){
				checkBox_body.append(String.format("<option value='%s' >%s</option>", srow.getOrg_code(), srow.getOrg_nm()));
			}
		}
		checkBox_body.append("</select></li>");
		
		return checkBox_body.toString();
	}
	
	private String ConvertToOrgTitle(String org_level, boolean isObejctName)
	{
		if(org_level.equals("1"))
			return isObejctName ? "org_bumon" : "부문전체";
		else if(org_level.equals("2"))
			return isObejctName ? "org_bonbu" : "본부전체";
		else if(org_level.equals("3"))
			return isObejctName ? "org_damdang" : "담당전체";
		else if(org_level.equals("4"))
			return isObejctName ? "org_team" : "팀전체";
		else 
			return "전체";
	}
	
	@RequestMapping(value="/pol/getSelectedOrglist.do")
	public void getSelectedOrglist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    String sel_org = "";
	    String sel_val = "";
	    if(request.getParameter("orgval") != null && request.getParameter("orgval") != "" )
	    	sel_org = request.getParameter("orgval");
	    if(request.getParameter("selval") != null && request.getParameter("selval") != "" )
	    	sel_val = request.getParameter("selval");
	   
	    try
		{
	    	
	    	PolicySearchVO searchVO = new PolicySearchVO();
	    	searchVO.setOrg_upper(sel_org);

	    	List<OrgGroupVO> orgList = polService.getOrgGroupSelectList(searchVO);
	    	
	    	
		    isOk = true;
				
		        
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("selTitle", ConvertToOrgTitle(sel_val, false));
		    map.put("list", orgList);
		}
		catch(Exception ex)
		{
			msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	
	
	/**
	 * 조직진단정보 조회 모니터링용 - 검색결과
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/getSelectResultList.do")
	public void getSelectResultList(HttpServletRequest request, 
									@ModelAttribute("PolicySearchVO") PolicySearchVO searchVO, 
									HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int isRowType = 1; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			HashMap<String, String> codeInfo = new HashMap<String, String>();
			codeInfo.put("majCode", "PIX");
			codeInfo.put("minCode", "02");
			
			
			//협력사 지수 여부
			CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
			String isKpc = isKpcInfo.getAdd_info1();
			
			searchVO.setIsKpc(isKpc);
			
	    	searchVO.setMajCode(searchVO.getmCode());
	    	searchVO.setMinCode(searchVO.getnCode());
	    	searchVO.setPolCode(searchVO.getpCode());
	    		
		    StringBuffer item_body = new StringBuffer();
		    String dateValue = searchVO.getBegin_date();
		    
		    searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
		    
		    List<EgovMap> resultList = polService.getOrgPolResultListInfo(searchVO);
	
		    int ItemCnt = 0;
		    int orgCnt = 0;
		    for(EgovMap record:resultList)
		    {
		    	
		    	if(searchVO.getIsexpn_zero().equals("Y")){
		    		if(Integer.parseInt(record.get("count").toString()) <= 0){
			    		continue;
			    	}
		    	}
		    	ItemCnt++;
		    	
		    	
		    	if(record.get("scoreyn").equals("N")){
		    		/*item_body.append(String.format("<div class='item %s'>", Float.parseFloat(String.valueOf(record.get("score"))) > 50 ? "STATUS_A" : record.get("listtype").equals("O") ? "STATUS_A" : "STATUS_B"));
			    	item_body.append(String.format("<li class='B1_a %s %s' style='cursor:pointer;' isRowType='%s' orgcode='%s' issub='%s' empno='%s' is_large='Y' dateValue='%s' listtype='%s'>"
			    			, "box3_s" 
			    			, "btn_detail_info"
			    			, record.get("issuborg").equals("Y") ? 1 : 2
							, record.get("orgcode")
							, record.get("issuborg")
							, record.get("empno")
							, dateValue
							, record.get("listtype")));
			    	item_body.append("<ul>");
			    	item_body.append(String.format("	<li class='boxcell1'><img class='img_orgtitle' style='width:26px;height:26px;' src='/img/%s.png' alt=''> <span class='orgname'>%s</span></li>"
			    			, record.get("listtype").equals("U") ? "icon_blue_1" : "icon_blue_2"
			    			, record.get("listtype").equals("U") ? record.get("empnm") : String.format("%s", record.get("orgnm"))));
			    	item_body.append("<li class='boxcell2'><p class='count' style='display:none;'></p><span></span></li>");
			    	item_body.append(String.format("	<li class='boxcell3'>%s</li>", dateValue));
			    	item_body.append("</ul>");
			    	item_body.append("</li>");
			    	
			    	
			    	item_body.append(String.format("<li class='B1_b %s' style='display:none;' is_large='Y'>"
			    			,"box3_l"));
			    	item_body.append("<ul class='btn_detail_info' style='cursor:pointer;'>");
			    	item_body.append(String.format("<li class='boxcell1'><img src='/img/%s.png' alt=''> <span class='orgname'>%s</span></li>"
			    			, record.get("listtype").equals("U") ? "icon_blue_1" : "icon_blue_2"
				    		, record.get("listtype").equals("U") ? record.get("empnm") : String.format("%s", record.get("orgnm"))));
			    	item_body.append("<li class='boxcell2'><p class='count' style='display:none;'></p><span></span></li>");
			    	item_body.append("</ul>");
			    	item_body.append(String.format("<div class='%s'>"
			    			, "box3_list"));
			    	item_body.append(String.format("<table cellpadding='0' cellspacing='0' class='%s'>"
			    			, "box_red"));			    	
			    	item_body.append("<colgroup><col style='width:*'><col style='width:25%'><col style='width:20%'><col style='width:15%'></colgroup>");
					item_body.append("<tbody class='box_content_list'><tr><td colspan='4' style='height: 154px; border-bottom-color: currentColor; border-bottom-width: 0px; border-bottom-style: none;'>정책 대상이 아닙니다.</td></tr></tbody>");
			    	item_body.append("</table>");
			    	item_body.append("</div>");
			    	item_body.append("</li>");
			    	
			    	item_body.append("</div>");*/
		    	}else{
		    		orgCnt ++;
		    		item_body.append(String.format("<div class='item %s'>", /*Float.parseFloat(String.valueOf(record.get("score"))) > 50 ? "STATUS_A" :*/ record.get("listtype").equals("O") ? "STATUS_A" : "STATUS_B"));
			    	item_body.append(String.format("<li class='B1_a %s %s' style='cursor:pointer;' isRowType='%s' orgcode='%s' issub='%s' empno='%s' is_large='N' dateValue='%s' listtype='%s'>"
			    			, record.get("isCollabor").equals("N")||record.get("collaborCode").equals("") ? record.get("listtype").equals("O")  ? "box2_s" : "box1_s" : record.get("collaborCode").toString().replaceAll(" ","").equals("01")  ? "box5_s" : record.get("collaborCode").toString().replaceAll(" ","").equals("02") ? "box6_s" : record.get("collaborCode").toString().replaceAll(" ","").equals("03") ? "box4_s" :"box2_s" 
			    			, "btn_detail_info"
			    			, record.get("issuborg").equals("Y") ? 1 : 2
							, record.get("orgcode")
							, record.get("issuborg")
							, record.get("empno")
							, dateValue
							, record.get("listtype")));
		    		
			    	item_body.append("<ul>");
			    	item_body.append(String.format("	<li class='boxcell1'><img class='img_orgtitle' style='width:26px;height:26px;' src='/img/%s.png' alt=''> <span class='orgname'>%s</span></li>"
			    			, record.get("listtype").equals("U") ? "icon_blue_1" : "icon_blue_2"
			    			, record.get("listtype").equals("U") ? record.get("empnm") : String.format("%s", record.get("orgnm"))));
			    	item_body.append(String.format("	<li class='boxcell2'><p class='count' style='display:none;'>%s</p><span>%s</span>건</li>"
			    			, String.valueOf(record.get("count"))
			    			, CommonUtil.convertStringToCommaString(Integer.parseInt(record.get("count").toString())) ));
			    	item_body.append(String.format("	<li class='boxcell3'>%s</li>", dateValue));
			    	item_body.append("</ul>");
			    	item_body.append("</li>");
			    	
			    	
			    	item_body.append(String.format("<li class='B1_b %s' style='display:none;' is_large='Y'>"
			    			, record.get("isCollabor").equals("N")||record.get("collaborCode").equals("") ? record.get("listtype").equals("O") ? "box2_l" : "box1_l" : record.get("collaborCode").toString().replaceAll(" ","").equals("01")  ? "box5_l" : record.get("collaborCode").toString().replaceAll(" ","").equals("02") ? "box6_l" : record.get("collaborCode").toString().replaceAll(" ","").equals("03") ? "box4_l" :"box2_l"));
			    	item_body.append("<ul class='btn_detail_info' style='cursor:pointer;'>");
			    	item_body.append(String.format("<li class='boxcell1'><img src='/img/%s.png' alt=''> <span class='orgname'>%s</span></li>"
			    			, record.get("listtype").equals("U") ? "icon_blue_1" : "icon_blue_2"
				    		, record.get("listtype").equals("U") ? record.get("empnm") : String.format("%s", record.get("orgnm"))));
			    	item_body.append(String.format("<li class='boxcell2'><p class='count' style='display:none;'>%s</p><span>%s</span>건</li>"
			    			, String.valueOf(record.get("count"))
			    			, CommonUtil.convertStringToCommaString(Integer.parseInt(record.get("count").toString())) ));
			    	item_body.append("</ul>");
			    	item_body.append(String.format("<div class='%s'>"
			    			, record.get("listtype").equals("O") ? "box2_list" : "box1_list"));
			    	item_body.append(String.format("<table cellpadding='0' cellspacing='0' class='%s'>"
			    			, record.get("listtype").equals("O") ? "box_red" : "box_blue"));
			    	
			    	item_body.append("<colgroup><col style='width:*'><col style='width:25%'><col style='width:20%'><col style='width:15%'></colgroup>");
					item_body.append(String.format("<tbody class='box_content_list'><tr><td colspan='4'>%s</td></tr></tbody>",record.get("orgnm")));
			    	item_body.append("</table>");
			    	item_body.append("</div>");
			    	item_body.append("</li>");
			    	
			    	item_body.append("</div>");
		    	}
		    	
		    	 
		    	/*
		    	
		    	
		    	item_body.append(String.format("<li class='B1_b %s' style='width:377px;display:none;' is_large='Y'>", /*Float.parseFloat(String.valueOf(record.get("score"))) > 50 ? "B1_bl" :* "B1_or"));
		    	item_body.append("<div class='B1_tit1 btn_detail_info' style='cursor:pointer;'>");
				item_body.append("<ul>");
				item_body.append("<li class='B1T1_ic'><a style='cursor:pointer;'><span class='blind'>검색(돋보기)</span></a></li>");
				item_body.append(String.format("<li class='orgname'>%s</li>",  record.get("listtype").equals("O") ? record.get("orgnm") : String.format("%s", record.get("empnm") )));
				item_body.append("</ul>");
				item_body.append("</div>");
				item_body.append(String.format("<div class='B1_score1 btn_detail_info' style='cursor:pointer;'><span class='count' style='display:none;'>%s</span><span class='number'>%s</span>건</div>"
												, String.valueOf(record.get("count"))
												, CommonUtil.convertStringToCommaString(Integer.parseInt(record.get("count").toString()))));
				item_body.append("<div class='B1_table1' style='overflow-y:auto;'>");
				//하위조직조회
				
				item_body.append("<table border='0' class='TBS1' cellpadding=0 cellspacing=0 >");					
				item_body.append("<colgroup><col style='width:*'><col style='width:25%'><col style='width:20%'><col style='width:15%'></colgroup>");
				item_body.append(String.format("<tbody class='box_content_list'><tr><td colspan='4'>%s</td></tr></tbody>",record.get("orgnm")));
				item_body.append("</table>");
				item_body.append("</div>");
				item_body.append(String.format("<div class='B1_date1'>%s</div>", dateValue));	
				
		    	item_body.append("</li>");
		    	 * */
		    	
		    	
		    }
		    
		    if(ItemCnt <= 0 || orgCnt <=0){
		    	item_body.append(String.format("<div style='width:100%%;height:200px;text-align:center;padding-top:200px;'><span style='font-size:30px;font-weight:bold;color:#7E7E80;font-family:돋움;'>%s</span></div>", "검색결과가 없습니다."));
		    }
		    
		    isOk = true;
		   // item_body.append(CreateTempData(searchVO));
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("items", item_body.toString());
		   // map.put("selTitle", ConvertToOrgTitle(sel_val, false));
		   // map.put("list", orgList);
		}
		catch(Exception ex)
		{
			msg = ex.getMessage();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	    finally{
	    	
	    }
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	
	/**
	 * 모니터링 박스 상세 정보 조회
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/getSelectResultDetailList.do")
	public void getSelectResultDetailList(HttpServletRequest request 
									, String isRowType
									, String orgCode
									, String isSub
									, String beginDate
									, String empno
									, String mCode
									, String nCode
									, String pCode
									, String listtype
									, String buseoType
									,HttpServletResponse response) throws Exception {
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
	    	
	    	PolicySearchVO searchVO= new PolicySearchVO();
	    	searchVO.setBegin_date(beginDate.replace("-", ""));
	    	searchVO.setMajCode(mCode);
	    	searchVO.setMinCode(nCode);
	    	searchVO.setPolCode(pCode);
	    	searchVO.setBuseoType(buseoType);
		    StringBuffer item_body = new StringBuffer();
		    
		    if(buseoType.equals("N")){
		    	//부서진단 아님
		    	if(isSub.equals("Y")){
		    		//하위조직 있음
		    		if(listtype.equals("U")){
		    			/**
						 * 하위 조직 팀원 정보 조회
						 */
						item_body.append("<tr>");
						item_body.append("<th>성명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						
						searchVO.setOrg_upper(orgCode);
						List<EgovMap> subList = polService.getUserOrgPolResultList(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' issub='%s' style='cursor:pointer;'>"
										, row.get("empno").toString()
										, row.get("issuborg")));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1",  row.get("empnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", row.get("empno").toString()));
							   	item_body.append("</tr>");
							   	i++;
						    }
							
						}
		    		}else{
		    			/**
						 * 하위조직 정보 조회
						 */
						item_body.append("<tr>");
						item_body.append("<th>조직명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						searchVO.setOrg_upper(orgCode);
						
						//List<EgovMap> subList = polService.getOrgSubList(searchVO);//polService.getOrgPolResultList(searchVO, searchType + 1);
						 List<EgovMap> subList = polService.getOrgPolResultListInfo(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='%s' sseq='%s' issub='%s' style='cursor:pointer;'>"
										, row.get("listtype").toString()
										, row.get("listtype").equals("U") ? row.get("empno").toString() : row.get("orgcode").toString()
										, row.get("issuborg")));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("listtype").equals("U") ? row.get("empnm").toString() : row.get("orgnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>"
							   			, i % 2 == 0 ? "cell2" : "cell1"
							   			, row.get("listtype").toString()						
							   			, row.get("orgcode").toString()));
							   	item_body.append("</tr>");
								
							   	i++;
						    }
						}
		    		}
		    	}else{
		    		//하위조직 없음
		    		if(listtype.equals("U")){
		    			/**
						 * 직원별 정보조회
						 */
		    			item_body.append("<tr>");
						item_body.append("<th>정책명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						
						searchVO.setOrg_upper(orgCode);
						searchVO.setEmp_no(empno);
						List<EgovMap> subList = polService.getUserOrgPolDetailResultList(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
						    	item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' issub='%s' style='cursor:pointer;'>"
						    					, empno
						    					, row.get("issuborg")));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("polidxname").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", empno));
							   	item_body.append("</tr>");
							   	i++;
						    }
						}
		    		}else{
		    			/**
						 * 하위 조직 팀원 정보 조회
						 */
						item_body.append("<tr>");
						item_body.append("<th>성명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						
						searchVO.setOrg_upper(orgCode);
						List<EgovMap> subList = polService.getUserOrgPolResultList(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' issub='%s' style='cursor:pointer;'>"
										, row.get("empno").toString()
										, row.get("issuborg")));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1",  row.get("empnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", row.get("empno").toString()));
							   	item_body.append("</tr>");
							   	i++;
						    }
						}
		    		}
		    		
		    		
		    		
		    	}
		    }else{
		    	//부서진단
		    	if(listtype.equals("B")){
		    		//하위조직 있음
		    		/**
					 * 하위조직 정보 조회
					 */
					item_body.append("<tr>");
					item_body.append("<th>조직명</th>");
					item_body.append("<th>건수</th>");
					item_body.append("<th>날짜</th>");
					item_body.append("<th>상세</th>");
					item_body.append("</tr>");
					searchVO.setOrg_upper(orgCode);
					
					//List<EgovMap> subList = polService.getOrgSubList(searchVO);//polService.getOrgPolResultList(searchVO, searchType + 1);
					 List<EgovMap> subList = polService.getOrgPolResultListInfo(searchVO);
					if(subList.size() > 0){
						int i = 1;
						for(EgovMap row:subList)
					    {
							if(Integer.parseInt(row.get("count").toString()) <=0 || !row.get("orgcode").equals(orgCode)){
					    		continue;
					    	}
							
							item_body.append(String.format("<tr class='btn_detail_user_info' st='%s' sseq='%s' issub='%s' style='cursor:pointer;'>"
									, row.get("listtype").toString()
									, row.get("listtype").equals("U") ? row.get("empno").toString() : row.get("orgcode").toString()
									, row.get("issuborg")));
						    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("listtype").equals("U") ? row.get("empnm").toString() : row.get("orgnm").toString()));
						   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
						   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
						   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>"
						   			, i % 2 == 0 ? "cell2" : "cell1"
						   			, row.get("listtype").toString()						
						   			, row.get("orgcode").toString()));
						   	item_body.append("</tr>");
							
						   	i++;
					    }
					}
		    	}else{
		    		//하위조직 있음
		    		/**
					 * 하위조직 정보 조회
					 */
					item_body.append("<tr>");
					item_body.append("<th>조직명</th>");
					item_body.append("<th>건수</th>");
					item_body.append("<th>날짜</th>");
					item_body.append("<th>상세</th>");
					item_body.append("</tr>");
					searchVO.setOrg_upper(orgCode);
					
					//List<EgovMap> subList = polService.getOrgSubList(searchVO);//polService.getOrgPolResultList(searchVO, searchType + 1);
					 List<EgovMap> subList = polService.getOrgPolResultListInfo(searchVO);
					if(subList.size() > 0){
						int i = 1;
						for(EgovMap row:subList)
					    {
							if(Integer.parseInt(row.get("count").toString()) <=0){
					    		continue;
					    	}
							item_body.append(String.format("<tr class='btn_detail_user_info' st='%s' sseq='%s' issub='%s' style='cursor:pointer;'>"
									, row.get("listtype").toString()
									, row.get("listtype").equals("U") ? row.get("empno").toString() : row.get("orgcode").toString()
									, row.get("issuborg")));
						    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("listtype").equals("U") ? row.get("empnm").toString() : row.get("orgnm").toString()));
						   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
						   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
						   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>"
						   			, i % 2 == 0 ? "cell2" : "cell1"
						   			, row.get("listtype").toString()						
						   			, row.get("orgcode").toString()));
						   	item_body.append("</tr>");
							
						   	i++;
					    }
					}
		    	}
		    }
		    
		    
		    /*
		    
		    //조직인지-1, 개인인지=2
		    if(Integer.parseInt(isRowType) == 1){
		    	//하위 조직이 있는가? 있으면 하위조직 조회
				if(isSub.equals("Y")){
					
					//System.out.println(listtype + ":listtype");
					if(listtype.equals("U")){
						/**
						 * 하위 조직 팀원 정보 조회
						
						item_body.append("<tr>");
						item_body.append("<th>성명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						
						searchVO.setOrg_upper(orgCode);
						List<EgovMap> subList = polService.getUserOrgPolResultList(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' style='cursor:pointer;'>", row.get("empno").toString()));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1",  row.get("empnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", row.get("empno").toString()));
							   	item_body.append("</tr>");
							   	i++;
						    }
							
						}else{
							
						}
					}else{
						/**
						 * 하위조직 정보 조회
						
						item_body.append("<tr>");
						item_body.append("<th>조직명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						searchVO.setOrg_upper(orgCode);
						
						//List<EgovMap> subList = polService.getOrgSubList(searchVO);//polService.getOrgPolResultList(searchVO, searchType + 1);
						 List<EgovMap> subList = polService.getOrgPolResultListInfo(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='%s' sseq='%s' style='cursor:pointer;'>"
										, row.get("listtype").toString()
										, row.get("listtype").equals("U") ? row.get("empno").toString() : row.get("orgcode").toString()));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("listtype").equals("U") ? row.get("empnm").toString() : row.get("orgnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>"
							   			, i % 2 == 0 ? "cell2" : "cell1"
							   			, row.get("listtype").toString()						
							   			, row.get("orgcode").toString()));
							   	item_body.append("</tr>");
								
							   	i++;
						    }
						}else{
							
						}
					}
					
					
				}else{
					if(listtype.equals("B")){
						/**
						 * 하위조직 정보 조회
						 
						item_body.append("<tr>");
						item_body.append("<th>조직명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						searchVO.setOrg_upper(orgCode);
						
						//List<EgovMap> subList = polService.getOrgSubList(searchVO);//polService.getOrgPolResultList(searchVO, searchType + 1);
						 List<EgovMap> subList = polService.getOrgPolResultListInfo(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='%s' sseq='%s' style='cursor:pointer;'>"
										, row.get("listtype").toString()
										, row.get("listtype").equals("U") ? row.get("empno").toString() : row.get("orgcode").toString()));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("listtype").equals("U") ? row.get("empnm").toString() : row.get("orgnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString()))));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>"
							   			, i % 2 == 0 ? "cell2" : "cell1"
							   			, row.get("listtype").toString()						
							   			, row.get("orgcode").toString()));
							   	item_body.append("</tr>");
								
							   	i++;
						    }
						}else{
							
						}
					}else{
						/**
						 * 하위 조직 팀원 정보 조회
						 
						item_body.append("<tr>");
						item_body.append("<th>성명</th>");
						item_body.append("<th>건수</th>");
						item_body.append("<th>날짜</th>");
						item_body.append("<th>상세</th>");
						item_body.append("</tr>");
						
						searchVO.setOrg_upper(orgCode);
						List<EgovMap> subList = polService.getUserOrgPolResultList(searchVO);
						if(subList.size() > 0){
							int i = 1;
							for(EgovMap row:subList)
						    {
								if(Integer.parseInt(row.get("count").toString()) <=0){
						    		continue;
						    	}
								item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' style='cursor:pointer;'>", row.get("empno").toString()));
							    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1",  row.get("empnm").toString()));
							   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
							   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
							   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", row.get("empno").toString()));
							   	item_body.append("</tr>");
							   	i++;
						    }
						}else{
							
						}
					}
					
				}
			}else{
				if(listtype.equals("U")){
					/**
					 * 직원별 정보조회
					 
					item_body.append("<tr>");
					item_body.append("<th>정책명</th>");
					item_body.append("<th>건수</th>");
					item_body.append("<th>날짜</th>");
					item_body.append("<th>상세</th>");
					item_body.append("</tr>");
					
					searchVO.setOrg_upper(orgCode);
					searchVO.setEmp_no(empno);
					List<EgovMap> subList = polService.getUserOrgPolDetailResultList(searchVO);
					if(subList.size() > 0){
						int i = 1;
						for(EgovMap row:subList)
					    {
					    	item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' style='cursor:pointer;'>", empno));
						    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", row.get("polidxname").toString()));
						   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
						   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
						   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", empno));
						   	item_body.append("</tr>");
						   	i++;
					    }
					}else{
						
					}
				}else{
					/**
					 * 하위조직 팀원 정보 조회
					 
					item_body.append("<tr>");
					item_body.append("<th>성명</th>");
					item_body.append("<th>건수</th>");
					item_body.append("<th>날짜</th>");
					item_body.append("<th>상세</th>");
					item_body.append("</tr>");
					
					searchVO.setOrg_upper(orgCode);
					List<EgovMap> subList = polService.getUserOrgPolResultList(searchVO);
					if(subList.size() > 0){
						int i = 1;
						for(EgovMap row:subList)
					    {
							if(Integer.parseInt(row.get("count").toString()) <=0){
					    		continue;
					    	}
							item_body.append(String.format("<tr class='btn_detail_user_info' st='U' sseq='%s' style='cursor:pointer;'>", row.get("empno").toString()));
						    item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1",  row.get("empnm").toString()));
						   	item_body.append(String.format("<td class='%s'>%s건</td>", i % 2 == 0 ? "cell2" : "cell1", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
						   	item_body.append(String.format("<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", beginDate));
						   	item_body.append(String.format("<td class='%s'><img src='/img/btn_view.png' alt='상세보기'></td>", i % 2 == 0 ? "cell2" : "cell1", row.get("empno").toString()));
						   	item_body.append("</tr>");
						   	i++;
					    }
					}else{
						
					}
				}
			}
		    */
		    isOk = true;	
		   
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("box_body", item_body.toString());
		  
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	
	/**
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/policystatusdetail.do")
	public String policystatusdetail(HttpServletRequest request
			, @ModelAttribute("PolicySearchVO") PolicySearchVO searchVO					
			, ModelMap model) throws Exception{	
		if(searchVO.getBegin_date().equals(""))
		{
			String begin_Date = polService.getUserInfoLastDate(searchVO);
			searchVO.setBegin_date(begin_Date);
		}
		model.addAttribute("getdate", searchVO.getBegin_date());
		
		/*auth, empno 임시코드 */
		String auth = request.getParameter("auth");
		String empno = request.getParameter("emp");
		
		model.addAttribute("auth", auth);
		model.addAttribute("emp", empno);
		/*********************** 임시코드 **************/
		//조회용 아이디/사번 - 대무자일경우 대무자 아이디 입력
		String UID = empno; //본부2 캡장 "3333"
		//auth = "1"; //관리자권한
		
		//auth = "3"; //일반사용자
		//org_code = ""
		
		String org_level = ""; //조직레벨 
		//String UPS = "4, 1, 2, 7, 9 "; //KT,부문,본부,담당,팀
		
	    //검색 - 조직코드인지 사번인지 
	    String dtType = searchVO.getDtType();
		String dtSeq = searchVO.getDtSeq();
		 /*********************************************/
		//모든 조직 그룹 조회
		//PolicySearchVO searchVO = new PolicySearchVO();
		
		//검색날짜 조회 - 최종날짜 조회
		String begin_Date = polService.getUserInfoLastDate(searchVO);
		searchVO.setBegin_date(begin_Date);
		
		model.addAttribute("getdate", begin_Date);
		model.addAttribute("dtType", dtType);
		model.addAttribute("dtSeq", dtSeq);
		
		StringBuffer org_body = new StringBuffer();
		
		if(Integer.parseInt(auth) <= 2)
		{
			searchVO.setOrg_upper(propertiesService.getString("orgRoot"));
			List<OrgGroupVO> orgGroup = polService.getOrgGroupSelectList(searchVO);
			
			org_body.append(CreateOrgCheckBoxHtml(orgGroup, "1"));
			org_body.append(CreateOrgCheckBoxHtml(null, "2"));
			org_body.append(CreateOrgCheckBoxHtml(null, "3"));
			org_body.append(CreateOrgCheckBoxHtml(null, "4"));
			
			
		}
		else if(Integer.parseInt(auth) == 3)
		{
			//사용자이면 조직레벨별 해당 조직만 바인딩 (상위조직은 바인딩 후 비활성 처리)
			// 1 : 조직장 / 대무자 인지 조회 -> 조직장이면 조직코드
			// USER 포지션 : 4, 1, 2, 7, 9 
			//조직장인지 / 대무자 인지 조회
			//일반사용자이면 자기 자신정보만 조회
			
			/*
			 * 1. 자신의 상위조직를 조회
			 * 2. 상위조직정보에 값이 있으면 선택박스에서 선택 후 비활성화 시킨다.
			 * */
			
			//대무자 조회
			List<EgovMap> proxyInfo = polService.getProxyUserInfo(UID);
			
			if(proxyInfo.size() > 0)
			{
				UID = ((EgovMap)proxyInfo.get(0)).get("empno").toString();
			}
			
			List<EgovMap> userInfo = polService.getUserCurrentInfo(UID);
		
			if(userInfo.size() <= 0)
			{
				return "redirect:/mainPage.do";
			}
			EgovMap uInfo = (EgovMap)userInfo.get(0);
			String OrgIndex = uInfo.get("orgindex").toString();
			org_level = uInfo.get("orglevel").toString();
			String rootOrg = uInfo.get("orgcode0").toString();
			//부문
			searchVO.setOrg_upper(rootOrg); //"4"는 KT대표코드 이다
			List<OrgGroupVO> orgGroup = polService.getOrgGroupSelectList(searchVO);
			org_body.append(CreateOrgCheckBoxHtml(orgGroup, "1", OrgIndex, org_level));
			
			//본부
			if(!uInfo.get("orgcode1").toString().equals("")){
				
				searchVO.setOrg_upper(uInfo.get("orgcode1").toString()); 
				List<OrgGroupVO> orgGroup2 = polService.getOrgGroupSelectList(searchVO);
				org_body.append(CreateOrgCheckBoxHtml(orgGroup2, "2", OrgIndex, org_level));
			}else{
				org_body.append(CreateOrgCheckBoxHtml(null, "2"));
			}
				
			//담당
			if(!uInfo.get("orgcode2").toString().equals("")){
				searchVO.setOrg_upper(uInfo.get("orgcode2").toString()); 
				List<OrgGroupVO> orgGroup3 = polService.getOrgGroupSelectList(searchVO);
					
				org_body.append(CreateOrgCheckBoxHtml(orgGroup3, "3", OrgIndex, org_level));
			}else{
				org_body.append(CreateOrgCheckBoxHtml(null, "3"));
			}
			
			//팀
			if(!uInfo.get("orgcode3").toString().equals(""))
			{
				searchVO.setOrg_upper(uInfo.get("orgcode3").toString()); 
				List<OrgGroupVO> orgGroup4 = polService.getOrgGroupSelectList(searchVO);
					
				org_body.append(CreateOrgCheckBoxHtml(orgGroup4, "4", OrgIndex, org_level));
			}
			else{
				org_body.append(CreateOrgCheckBoxHtml(null, "4"));
			}
				
			
				
		}
		
		model.addAttribute("orgbody", org_body);
		
		int TotPage =  10; //(totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
	
		return "pol/detail";
	}
	
	/**
	 * 지수정책모니터링 하위 조직-직원 결과 상세 조회
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/getploUserResultList.do")
	public void getploUserResultList(HttpServletRequest request, 
									@ModelAttribute("PolicySearchVO") PolicySearchVO searchVO, 
									HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    String auth = request.getParameter("auth");
	    MemberVO loginInfo = CommonUtil.getMemberInfo();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	searchVO.setMajCode(searchVO.getmCode());
	    	searchVO.setMinCode(searchVO.getnCode());
	    	searchVO.setPolCode(searchVO.getpCode());
	    	
	    	isOk = true;
	    	//System.out.println(searchVO.getPageIndex() + "][searchVO.getPageIndex()");
	    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
	 		searchVO.setPageSize(propertiesService.getInt("pageSize"));

	 		PaginationInfo paginationInfo = new PaginationInfo();
	 		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());

	 		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());

	 		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	 		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	    	
	    	searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
	    	//System.out.println(searchVO.getBegin_date() + ":begin_date");
	    	//System.out.println(searchVO.getRecordCountPerPage() + "][searchVO.getRecordCountPerPage()");
	    	//System.out.println(searchVO.getFirstIndex() + "][searchVO.getFirstIndex()");
	    	//결재정보조회
	    	List<CodeInfoVO> agreeList = comService.getCodeInfoList(MajrCodeInfo.AgreeCode);
	    	
	    	HashMap<String,Object> retMap = null;
	    
	    	if(searchVO.getBuseoType().equals("N")){
	    		if(searchVO.getDtType().equals("O")){
		    		searchVO.setOrg_upper(searchVO.getDtSeq());
		    		retMap = polService.getUserOrgSearchPolDetailResultList(searchVO);
		    	}else{
		    		if(auth.equals("3")){
		    			searchVO.setSearchKeyword(loginInfo.getUserid());
		    		}else{
		    			searchVO.setSearchKeyword(searchVO.getDtSeq());
		    		}
		    		
		    		searchVO.setSearchCondition("1");
		    		retMap = polService.getUserSearchPolDetailResultList(searchVO);
		    	}
	    	}else{
	    		//System.out.println(searchVO.getIssub() + ": issub;");
	    		if(searchVO.getIssub().equals("M") && searchVO.getDtType().equals("B")){
	    			searchVO.setOrg_upper(searchVO.getDtSeq());
	    			retMap = polService.getBuseoSubOrgPolDetailResultListForPaging(searchVO);
	    		}else{
	    			searchVO.setOrg_upper(searchVO.getDtSeq());
	    			retMap = polService.getBuseoSearchPolDetailResultListForPaging(searchVO);
	    		}
	    	}
	    	
	    	
		    
		   // HashMap<String,Object> retMap = polService.getUserSearchPolDetailReaultListForPageing(searchVO);
			List<EgovMap> result = (List<EgovMap>)retMap.get("list");
		    
		    //System.out.println(result.size() + "][");
		    StringBuffer item_body = new StringBuffer();
		    if(result.size() > 0){
		    	if(searchVO.getBuseoType().equals("N")){
			    	for(EgovMap row:result){
			    		String uuid = CommonUtil.CreateUUID();
				    	item_body.append("<tr style='font-weight:bold;'>");
				    	item_body.append(String.format("<td><a class='show_upperorgnames' orgcode='%s'>%s</a></td>", row.get("orgcode"), row.get("orgnm")));
				    	item_body.append(String.format("<td>%s</td>", row.get("empnm")));
				    	//item_body.append(String.format("<td>%s<br />%s</td>", row.get("mac"), row.get("ip")));
				    	item_body.append(String.format("<td>%s</td>", row.get("eventdate")));
				    	item_body.append(String.format("<td><a class='btn_log_view' polcd='%s' empno='%s' bdt='%s' mac='%s' style='cursor:pointer;'>%s</a></td>", row.get("polidxid"), row.get("empno"), searchVO.getBegin_date(), row.get("mac"), row.get("polidxname")));
				    	//item_body.append(String.format("<td>%s</td>", row.get("polidxname")));
				    	item_body.append(String.format("<td>%s</td>", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
				    	item_body.append(String.format("<td>%s</td>", row.get("score")));
				    	item_body.append(String.format("<td>%s</td>", String.format("<span class='scondition1' style='background:%s;border:solid 1px %s;'>%s</span>", row.get("scorestatcolor"), row.get("scorestatcolor"), row.get("scorestatname"))));//row.get("polstat").equals("양호") ? "<span class='scondition1'>양호</span>" :  row.get("polstat").equals("주의") ? "<span class='scondition3'>주의</span>" : "<span class='scondition2'>취약</span>"));
				    	/*if(auth.equals("1")){ //관리자 이면  
				    		if(Boolean.parseBoolean(row.get("pcgindc").toString())){ //PC지키미 정책이면 조치(PC 지키미 실행)
					    		
					    		item_body.append(String.format("<td>%s</td>", row.get("mac").equals("") ? "미대상" : row.get("pcgactkind").equals("") ? "PC지키미 조치" : row.get("pcgactname")));
						    	
						    	item_body.append(String.format("<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : row.get("mac").equals("") ? "미대상" : Boolean.parseBoolean(row.get("pcgindc").toString()) ? String.format("<a class='btn_scr3 btn_pcgact_info' mode='P' pcgparam='%s' pcgactid='%s' empno='%s' macno='%s' uip='%s' dt_date='%s' pol_id='%s'><span>강제조치</span></a>"
							    		, row.get("exepara")
							    		, uuid
							    		, row.get("empno")
							    		, row.get("mac")
							    		, row.get("ip")
							    		, row.get("regdate").toString().replace("-", "")
							    		, row.get("polidxid")) : "-" ));
				    		}else{
				    			item_body.append(String.format("<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : row.get("apprid").equals("") ? row.get("mac").equals("") ? "미대상" : String.format("<input type='checkbox' name='sel_box' value='%s' empno='%s' macno='%s' uip='%s' dt_date='%s' pol_id='%s' />", uuid, row.get("empno"), row.get("mac"), row.get("ip"), row.get("regdate").toString().replace("-", ""), row.get("polidxid")) : row.get("apprstatname")));
		
						    	String agreeSelect = "<select name='app_target' style='width:100px;'>";
						    	for(CodeInfoVO code:agreeList)
						    	{
						    		if(code.getMinr_code().equals("00")){
						    			continue;
						    		}
						    		agreeSelect += String.format("<option value='%s'%s>%s</option>"
						    										, code.getMinr_code()
						    										, row.get("apprlinecd").equals(code.getMinr_code()) ? " selected" : ""
						    										, code.getCode_desc());
						    	}
						    	agreeSelect += "</select>";
						    	
						    	item_body.append(String.format("<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : Integer.parseInt(row.get("apprcnt").toString()) > 0 ? row.get("apprid").equals("") ? "소명처리" : String.format("<a class='btn_scr3 btn_appr_info' apprid='%s'><span>소명정보</span></a>", row.get("apprid")) :  agreeSelect));
				    		}
				    	}else{ //일반이면
				    		
					    	if(Boolean.parseBoolean(row.get("pcgindc").toString())){ //PC지키미 정책이면 조치(PC 지키미 실행)
					    		item_body.append(String.format("<td>%s</td>", row.get("pcgactkind").equals("") ? "PC지키미 조치" : row.get("pcgactname")));
					    		item_body.append(String.format("<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : row.get("mac").equals("") ? "미대상" : Boolean.parseBoolean(row.get("pcgindc").toString()) ? String.format("<a class='btn_scr3 btn_pcgact_info' mode='P' pcgparam='%s' pcgactid='%s' empno='%s' macno='%s' uip='%s' dt_date='%s' pol_id='%s'><span>조치</span></a>"
							    		, row.get("exepara")
							    		, uuid
							    		, row.get("empno")
							    		, row.get("mac")
							    		, row.get("ip")
							    		, row.get("regdate").toString().replace("-", "")
							    		, row.get("polidxid")) : "-" ));
					    	}else{
					    		item_body.append(String.format("<td>%s</td>", row.get("issanct")));
						    	item_body.append(String.format("<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : Integer.parseInt(row.get("apprcnt").toString()) > 0 ? row.get("apprid").equals("") ? "소명처리" : String.format("<a class='btn_scr3 btn_appr_info' apprid='%s'><span>소명정보</span></a>", row.get("apprid")) : "-"));
					    	}
					    	
				    	}*/
				    	
				    	item_body.append("</tr>");
				    	
				    }
		    	}else{
		    		for(EgovMap row:result){
			    		item_body.append("<tr style='font-weight:bold;'>");
				    	item_body.append(String.format("<td><a class='show_upperorgnames' orgcode='%s'>%s</a></td>", row.get("orgcode"), row.get("orgnm")));
				    	item_body.append(String.format("<td>%s</td>", row.get("capnm")));
				    	item_body.append(String.format("<td><a class='btn_log_view' polcd='%s' empno='%s' bdt='%s' mac='%s' style='cursor:pointer;'>%s</a></td>", row.get("polidxid"), row.get("capno"), searchVO.getBegin_date(), "", row.get("poldesc")));
				    	item_body.append(String.format("<td>%s</td>", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("count").toString())) ));
				    	item_body.append(String.format("<td>%s</td>", CommonUtil.convertStringToCommaString(Integer.parseInt(row.get("score").toString())) ));
				    	item_body.append(String.format("<td>%s</td>", row.get("avg")));
				    	item_body.append(String.format("<td>%s</td>", row.get("polstat").equals("양호") ? "<span class='scondition1'>양호</span>" : row.get("polstat").equals("주의") ? "<span class='scondition3'>주의</span>" : "<span class='scondition2'>취약</span>"));
				    	item_body.append("</tr>");
		    		}
		    	}
		    		
		    }else{
		    	item_body.append("<tr style='font-weight:bold;height:300px;'>");
		    	item_body.append(String.format("<td colspan='9'>%s</td>", "진단정보가 없습니다."));
		    	item_body.append("</tr>");
		    }
		    
		    int totalCnt = (int)retMap.get("totalCount");
	    	int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 	
		   
	    	map.put("totalPage", TotPage);
	    	map.put("currentpage", searchVO.getPageIndex());
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("body", item_body.toString());
		    map.put("totalCnt", totalCnt);
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	   
	}
	/**
	 * 지수정책 수집로그 조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/polLogdetailview.do")
	public String polLogdetailview(HttpServletRequest request
			, ModelMap model) throws Exception{	
		String polcode = request.getParameter("polcd") == null ? "" : request.getParameter("polcd");
		String empno = request.getParameter("empno") == null ? "" : request.getParameter("empno");
		String begindate = request.getParameter("begindate") == null ? "" : request.getParameter("begindate");
		String mac = request.getParameter("mac") == null ? "" : request.getParameter("mac");
		int logCnt = 0;
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("polcode", polcode);
		hMap.put("empno", empno);
		hMap.put("begindate", begindate);
		hMap.put("mac", mac);
		
		model.addAttribute("searchVO", hMap);
		try{
		StringBuffer str = new StringBuffer();
		
		EgovMap tblInfo = polService.getPolDetailLogTableNColumns(hMap);
		
		hMap.put("tblname", String.format("\"public\".\"%s\"", tblInfo.get("tblname").toString()));
		hMap.put("columnsname", tblInfo.get("columnsname").toString());
		
		String[] flag = tblInfo.get("columnsname").toString().split(",");
		List <String> list = new ArrayList<String>();
		Collections.addAll(list,flag);
		String cStr = "";
		
		String dFlag ="";
		
		for (int i=0; i<list.size(); i++ ){
			if(list.get(i).equals("d_flag")){
				dFlag = "AND d_flag != 'Y'";
				list.remove(i);
			}
		}
		
		for(int i=0; i<list.size(); i++){
			if(i > 0){
				cStr = cStr +"," + list.get(i);
			}else {
				cStr = list.get(i);
			}
		}
		//System.out.println(str);
		
		if(cStr !=""){
			hMap.put("columnsname", cStr);
		}else {
			cStr = tblInfo.get("columnsname").toString();
		}
		
		
		
		hMap.put("dFlag", dFlag);
		
		List<LinkedHashMap<String, Object>> Logs = getDataService.getPolDetailLogForDateNUser(hMap);
		//List<HashMap<?,?>> Logs = polService.getPolDetailLogForDateNUser(hMap);
		if(Logs == null){
			str.append("<tr style='height:300px'><td>검색조건이 미설정 되었습니다.</td></tr>");
		}else{
			
			//EgovMap tblInfo = polService.getPolDetailLogTableNColumns(hMap);
			List<EgovMap> columnsInfo = secPolService.getPolSourceLogTableColumns(tblInfo.get("tblname").toString());
			
			if(Logs.size() > 0){
				logCnt = Logs.size();
				HashMap<?,?> log = (HashMap<?,?>)Logs.get(0);
				str.append("<tr>");
				for(Object key:log.keySet())
				{
					str.append(String.format("<th>%s</th>", ConvertColumnsName(columnsInfo, key.toString())));
				}
				str.append("</tr>");
				for(HashMap<?,?> row:Logs)
				{
					str.append("<tr>");
					for(Object key:row.keySet()){
						str.append(String.format("<td>%s</td>", row.get(key) == null ? "" : HTMLInputFilter.htmlSpecialChars(row.get(key).toString())));
					}
					str.append("</tr>");
				}
				
			}else{
				//EgovMap logInfo = polService.getPolDetailLogTableNColumns(hMap);
				List<String> columns = CommonUtil.SplitToString(cStr, ",");
				/*str.append("<tr>");
				for(String column:columns)
				{
					str.append(String.format("<th>%s</th>", ConvertColumnsName(columnsInfo, column)));
				}
				str.append("</tr>");*/
				str.append(String.format("<tr style='height:300px'><td colspan='%s'>검색결과가 없습니다.</td></tr>", columns.size()));
			}
			
		}
		model.addAttribute("logBody", str.toString());
		model.addAttribute("logCnt", logCnt);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "pol/logdetailview";
	}
	
	/**
	 * 지수화정책 상세로그 Export Excel
	 * @param request
	 * @param polcode
	 * @param empno
	 * @param begindate
	 * @param mac
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/logdetailviewexcel.do")
	public void logdetailviewexcel(HttpServletRequest request
								, String polcode
								, String empno
								, String begindate
								, String mac
								, HttpServletResponse response) throws Exception {
		
		String fileName = "";
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("polcode", polcode);
		hMap.put("empno", empno);
		hMap.put("begindate", begindate);
		hMap.put("mac", mac);
		EgovMap tblInfo = polService.getPolDetailLogTableNColumns(hMap);
		hMap.put("tblname", String.format("\"public\".\"%s\"", tblInfo.get("tblname").toString()));
		hMap.put("columnsname", tblInfo.get("columnsname").toString());
		
		String[] flag = tblInfo.get("columnsname").toString().split(",");
		List <String> list = new ArrayList<String>();
		Collections.addAll(list,flag);
		String cStr = "";
		
		String dFlag ="";
		
		for (int i=0; i<list.size(); i++ ){
			if(list.get(i).equals("d_flag")){
				dFlag = "AND d_flag != 'Y'";
				list.remove(i);
			}
		}
		
		for(int i=0; i<list.size(); i++){
			if(i > 0){
				cStr = cStr +"," + list.get(i);
			}else {
				cStr = list.get(i);
			}
		}
		//System.out.println(str);
		
		if(cStr !=""){
			hMap.put("columnsname", cStr);
		}
		hMap.put("dFlag", dFlag);
		
		//List<HashMap<?,?>> Logs = polService.getPolDetailLogForDateNUser(hMap);
		List<LinkedHashMap<String, Object>> Logs = getDataService.getPolDetailLogForDateNUser(hMap);
		if(Logs == null){
			response.flushBuffer();
		}else{

			List<EgovMap> columnsInfo = secPolService.getPolSourceLogTableColumns(tblInfo.get("tblname").toString());
			if(Logs.size() > 0){
				fileName = String.format("%s_%s_지수화정책진단상세로그", begindate, polcode);
				ExcelInitVO excelVO = new ExcelInitVO();
				excelVO.setFileName(fileName);
				excelVO.setSheetName("진단 상세로그");
				excelVO.setTitle("진단 상세로그");
				excelVO.setHeadVal("");
				List<String> head = new ArrayList<String>();
				HashMap<?,?> log = (HashMap<?,?>)Logs.get(0);
				
				for(Object key:log.keySet())
				{
					head.add(ConvertColumnsName(columnsInfo, key.toString()));
				}
				excelVO.setHead(head);
				
				List<EgovMap> excellist = new ArrayList<EgovMap>();
				for(HashMap<?,?> row:Logs)
				{
					EgovMap map = new EgovMap();
					for(Object key:row.keySet()){
						map.put(key, row.get(key) == null ? "" : row.get(key).toString());
					}
					excellist.add(map);
				}
				excelVO.setType("xlsx");
				ExcelUtil.xssExcelDown(response, excelVO, excellist);
				
			}else{
				response.flushBuffer();
			}
			
		}
		
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
	 * 소명일괄처리
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/setApprAllRegister.do")
	public void setApprAllRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    String checklist = "";
	     
	    if(request.getParameter("checklist") != null && request.getParameter("checklist") != "" )
	    	checklist = request.getParameter("checklist");
	   
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	
	    	HashMap<String,Object> hashMap = new HashMap<String,Object>();
	    	List<String> alist = CommonUtil.SplitToString(checklist, ",");
	    		    	
	    	hashMap.put("appridList", CommonUtil.SplitToString(checklist, ","));
	    	
	    	boolean retVal = polService.setApprAllRegister(hashMap);
	    	if(retVal){
	    		isOk = true;
	    	}else{
	    		msg = "소명일괄 처리중 오류가 발생 하였습니다.";
	    	}
	    	
	    	
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
	
	/**
	 * 소명정보 팝업 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/polapprdetailview.do")
	public String polapprdetailview(HttpServletRequest request
			, ModelMap model) throws Exception{	
		String apprid = request.getParameter("apprid") == null ? "" : request.getParameter("apprid");
		String ispgm = "0";
		ispgm = request.getParameter("ispgm") == null ? "0" : request.getParameter("ispgm");
		if(!apprid.equals(null)){
			EgovMap apprInfo = polService.getApplInfo(apprid);
			model.addAttribute("apprInfo", apprInfo);
		}
		model.addAttribute("ispgm", ispgm);
		model.addAttribute("apprid", apprid);
		return "pol/apprdetailview";
	}
	
	/**
	 * BPM iframe 상세정보
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/polapprbpmview.do")
	public String polapprbpmview(HttpServletRequest request
			, HttpServletResponse response
			, ModelMap model) throws Exception{	
		
		String apprid = request.getParameter("apprid") == null ? "" : request.getParameter("apprid");
		String issubmit = request.getParameter("issubmit") == null ? "0" : request.getParameter("issubmit");
		String bpmcall_url = "";
		String bpmidvlaue = "";
		
		if(!apprid.equals(null)){
			ApprInfoVO apprInfoVO = comService.getApprInfo(apprid);
			model.addAttribute("reqtype", apprInfoVO.getReqtype());
			EgovMap apprInfo = null;
			if(apprInfoVO.getReqtype().equals("0002")){
				apprInfo = polService.getPovApplInfo(apprid);
				model.addAttribute("apprInfo", apprInfo);
			}else{
				apprInfo = polService.getApplInfo(apprid);
				model.addAttribute("apprInfo", apprInfo);
			}
			
			
			
			CodeInfoVO param = new CodeInfoVO();
			param.setMajr_code(MajrCodeInfo.BPMURL);
			param.setMinr_code("SR2"); //운영 SR2
			CodeInfoVO bpmInfo = comService.getCodeInfoForOne(param);
			if(!apprInfo.get("bpmkey").equals("")){
				bpmcall_url = String.format("%s", bpmInfo.getCode_desc());
				bpmidvlaue = CipherBPMUtil.Encrypt(apprInfo.get("bpmkey").toString());
			}
			
			//파일업로드정보
			param.setMajr_code("F01");
			param.setMinr_code("EXT"); 
			CodeInfoVO extend = comService.getCodeInfoForOne(param);
			model.addAttribute("fileExtend", extend.getCode_desc());
			param.setMinr_code("SIZ"); 
			CodeInfoVO fileSize = comService.getCodeInfoForOne(param);
			model.addAttribute("fileSize", fileSize.getCode_desc());
			param.setMinr_code("CNT"); 
			CodeInfoVO fileCnt = comService.getCodeInfoForOne(param);
			model.addAttribute("fileCnt", fileCnt.getCode_desc());
			
			model.addAttribute("maxSize", (Integer.parseInt(fileSize.getCode_desc()) * 1024) * Integer.parseInt(fileCnt.getCode_desc()) );
			//procid
			model.addAttribute("bpmcall_url", bpmcall_url);
			model.addAttribute("bpmidvlaue", bpmidvlaue);
			model.addAttribute("polnotice", apprInfo.get("polnotice").toString().replaceAll("(\r\n|\n|\r)", "<br />"));
			model.addAttribute("appdesctext", apprInfo.get("appldesc").toString().replaceAll("(\r\n|\n|\r)", "<br />"));
			
		}
		model.addAttribute("issubmit", issubmit);
		model.addAttribute("apprid", apprid);
		return "pol/polapprbpmview";
	}	
	
	/**
	 * 소명정보 Ajex 저장 - 파일업로드 기능추가로 사용안함 
	 * @param request
	 * @param userIdxinfo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/setUpdateApprDesc.do")
	public void setUpdateApprDesc(HttpServletRequest request
								, @ModelAttribute("UserIdxinfoVO") UserIdxinfoVO userIdxinfo
								, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
	
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    	hashMap.put("appr_id", userIdxinfo.getAppr_id() );
	    	hashMap.put("appl_desc", userIdxinfo.getSumm_appl_desc());
	    	int retVal = polService.setUpdateApprDesc(hashMap);
	    	
	    	isOk = true;
	    	
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
	
	/**
	 * 소명정보 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/apprdetalreg.do")
	public String apprdetalreg(final MultipartHttpServletRequest request
			, ModelMap model) throws Exception{	
		
		String apprid = request.getParameter("apprid") == null ? "" : request.getParameter("apprid");
		String ispgm = "0";
		ispgm = request.getParameter("ispgm") == null ? "0" : request.getParameter("ispgm");
		
		String fName = "";
		String lName ="";
		fName = request.getParameter("fName") == null ? "" : request.getParameter("fName");
		lName = request.getParameter("lName") == null ? "" : request.getParameter("lName");
		
		try
		{
			
			final Map<String, MultipartFile> files = request.getFileMap();	
			List<FileVO> fileVo = new ArrayList<FileVO>();
			FileVO fInfo = new FileVO();
			
			
			if(files.size() > 0){
				List<FileVO> retvo = fileManager.fileUploadProcess(files, "APPR_", SdiagProperties.getProperty("Globals.apprfilePath"), fileVo);
				for(FileVO vo:retvo){
					System.out.println(vo.getEvid_file_name() + "][" + vo.getEvid_file_loc());
				}
				
				if(retvo.size() > 0){
					fInfo = retvo.get(0);
				}else{
					fInfo.setEvid_file_name("");
					fInfo.setEvid_file_loc("");
				}
			}else{
				fInfo.setEvid_file_name(fName);
				fInfo.setEvid_file_loc(lName);
			}
		
			
			
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    	hashMap.put("appr_id", apprid );
	    	hashMap.put("appl_desc", request.getParameter("appldesc"));
	    	hashMap.put("file_name", fInfo.getEvid_file_name() );
	    	hashMap.put("file_loc", fInfo.getEvid_file_loc());
	    	int retVal = polService.setUpdateApprDesc(hashMap);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return String.format("redirect:/pol/polapprbpmview.do?apprid=%s&issubmit=1", apprid);
	}	
	
	/*
	 * @RequestMapping("/sample/excelSample.do")
	public void excelSample(HttpServletRequest request,
								HttpServletResponse response) throws Exception{
		ExcelInitVO excelVo = new ExcelInitVO();			// 엑셀 기본 정보
		List<EgovMap> list = service.getExcel();			// 뿌려줄 내용
															
															// 표에 헤더값 셋팅
		excelVo.setHeadVal("날짜");
		excelVo.setHeadVal("로그 구분");
		excelVo.setHeadVal("아이디");
		excelVo.setHeadVal("이름");
		excelVo.setHeadVal("메뉴명");
		excelVo.setHeadVal("부서명");
		excelVo.setHeadVal("Client IP");
		excelVo.setHeadVal("접속권한");
		
		excelVo.setFileName("test");						// 다운로드 될 파일명
		excelVo.setSheetName("test sheet");					// 시트명
		excelVo.setTitle("접속이력");						// 표 타이틀
		excelVo.setType("xls");							// 엑셀 구분자 2007이전 : xls, 2007이후 : xlsx
		
		ExcelUtil.excelDown(response, excelVo, list);		// 다운로드 호출
	}
	 * */
	
	/**
	 * 조직도 하위 조직 조회
	 * @param request
	 * @param userIdxinfo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/setAddOrgSubfolder.do")
	public void setAddOrgSubfolder(HttpServletRequest request
								, String uorg_code
								, HttpServletResponse response) throws Exception {
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
	    	
			HashMap<String, String> codeInfo = new HashMap<String, String>();
			codeInfo.put("majCode", "PIX");
			codeInfo.put("minCode", "02");
			
	
			CodeInfoVO isKpcInfo = comService.getCodeInfo(codeInfo);
			String isKpc = isKpcInfo.getAdd_info1();
			
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("upper_org_code", uorg_code);
			map.put("isKpc", isKpc);
			
	    	
	    	List<OrgGroupVO> orgList = comService.getUnderOrgInfoListForManager(map);
	    	String subOrgString = CommonUtil.getCreateSubOrgNode(orgList, uorg_code, false);
	    	isOk = true;
	    	
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		    hMap.put("node_body", subOrgString);
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
	
	private String setContent(List<HashMap<String, Object>> list){
		
		StringBuffer data = new StringBuffer();
		HashMap<String, Object> log = (HashMap<String, Object>)list.get(0);
		
		for(Object key:log.keySet())
		{
			data.append(ConvertToColumnName(key.toString()) + ",");
		}
		data.append("\n");
		
		for(HashMap<String, Object> row:list)
		{
			for(Object key:row.keySet()){
				data.append(row.get(key).toString()+ ",");
			}
			data.append("\n");
		}
		
		return data.toString();
	}
	
	private List<String[]> setCSVContents(List<HashMap<String, Object>> list){
		List<String[]> csvList = new ArrayList<String[]>();
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
		
		return csvList;
	}
	
	/**
	 * 지수정책결과 Excel 저장 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pol/exportexcel.do")
	public void exportexcel(HttpServletRequest request
								, @ModelAttribute("PolicySearchVO") PolicySearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "";
		searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
		searchVO.setMajCode(searchVO.getmCode());
		searchVO.setMinCode(searchVO.getnCode());
		searchVO.setPolCode(searchVO.getpCode());
		
		List<HashMap<String, Object>> list = null;
		if(searchVO.getBuseoType().equals("N")){
			if(searchVO.getDtType().equals("O")){
				/**
				 * 조직이면
				 */
	    		searchVO.setOrg_upper(searchVO.getDtSeq());
	    		list = polService.getUserSearchPolDetailResultListForExcelList(searchVO);
	    		fileName = String.format("%s_%s_지수화정책진단결과", searchVO.getBegin_date(), searchVO.getOrg_upper());
	    	}else{
	    		searchVO.setSearchKeyword(searchVO.getDtSeq());
	    		searchVO.setSearchCondition("1");
	    		list = polService.getUserSearchPolDetailResultListForExportExcel(searchVO);
	    		fileName = String.format("%s_%s_지수화정책진단결과", searchVO.getBegin_date(), searchVO.getSearchKeyword());
	    	}
		}else{
			if(searchVO.getIssub().equals("M") && searchVO.getDtType().equals("B")){
				searchVO.setOrg_upper(searchVO.getDtSeq());
	    		list = polService.getBuseoSubOrgPolDetailResultListForExcelList(searchVO);
	    		fileName = String.format("%s_%s_지수화정책부서진단결과", searchVO.getBegin_date(), searchVO.getOrg_upper());
    		}else{
    			searchVO.setOrg_upper(searchVO.getDtSeq());
        		list = polService.getBuseoSearchPolDetailResultListForExcelList(searchVO);
        		fileName = String.format("%s_%s_지수화정책부서진단결과", searchVO.getBegin_date(), searchVO.getOrg_upper());
    		}
			
		}
		String FileName = URLEncoder.encode(fileName, "UTF-8").replace("\\", "%20");
		//ExcelUtil.stringToExportCsv(response, setContent(list), FileName);
		
		List<String[]> contents = setCSVContents(list);
		
		ExcelUtil.hashMapToExportCsv(response, FileName, contents);
		/*
		
		if(list != null){
			ExcelInitVO excelVO = new ExcelInitVO();
			excelVO.setFileName(fileName);
			excelVO.setSheetName("진단결과 현황");
			excelVO.setTitle("진단결과 현황");
			excelVO.setHeadVal("");
			List<String> head = new ArrayList<String>();
			HashMap<String, Object> log = (HashMap<String, Object>)list.get(0);
		
			for(Object key:log.keySet())
			{
				head.add(ConvertToColumnName(key.toString()));
			}
			excelVO.setHead(head);
			List<EgovMap> excellist = new ArrayList<EgovMap>();
			for(HashMap<?,?> row:list)
			{
				EgovMap map = new EgovMap();
				for(Object key:row.keySet()){
					map.put(key, row.get(key) == null ? "" : row.get(key).toString());
				}
				excellist.add(map);
			}
			excelVO.setType("xlsx");
			ExcelUtil.xssExcelDown(response, excelVO, excellist);
		}
		*/
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "org_code" : ColumnName = "부서코드";break;
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "이름";break;
		case "org_nm" : ColumnName = "조직명";break;
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
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	
}


