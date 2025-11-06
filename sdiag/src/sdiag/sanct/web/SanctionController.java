package sdiag.sanct.web;


import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.MenuItemVO;
import sdiag.sanct.service.SanctSearchResultListVO;
import sdiag.sanct.service.SanctionService;
import sdiag.member.service.MemberVO;
import sdiag.member.service.SdiagMemberService;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.util.BPMUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.CommonUtil;
import sdiag.util.MailUtil;
import sdiag.util.MajrCodeInfo;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class SanctionController {
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "PolicyService")
	private PolicyService polService;
	
	@Resource(name = "SanctionService")
	private SanctionService sanctService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	@Resource(name="memberService")
	private SdiagMemberService memberService;
	
	/**
	 *  수동제재 List 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/sanctlist.do")
	public String sanctlist(HttpServletRequest request, @ModelAttribute("searchVO") PolicySearchVO searchVO, ModelMap model) throws Exception{	

		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SANCTION, comService);
		
		//오늘 날짜 셋팅
		Date date = new Date();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("nowDate", fm.format(date));

		//검색날짜 조회 - 최종날짜 조회
		String begin_Date = polService.getUserInfoLastDate(searchVO);
		if(searchVO.getBegin_date().equals("")){
			searchVO.setBegin_date(begin_Date);
			searchVO.setEnd_date(begin_Date);
		}
		
		//조직도 조회
		//CommonUtil.getCreateOrgTree(request, comService, "", false, "");
		
		model.addAttribute("getdate", searchVO.getBegin_date());
		//대진단 선택
		StringBuffer str = new StringBuffer();
		List<MenuItemVO> polList = comService.getSolMenuList();	
		List<MenuItemVO> selList = new ArrayList<MenuItemVO>();
		for(MenuItemVO row:polList){
			if(row.getBuseo_indc().equals("Y")){
				continue;
			}
			if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
				selList.add(row);
			}
		}
		model.addAttribute("diag_majr_select", selList);
		
		String root = MajrCodeInfo.RootOrgCode;
		OrgGroupVO rootInfo = comService.getOrgInfo(root);
		model.addAttribute("orgRootInfo", rootInfo);
		
		model.addAttribute("totalPage", 1);
		model.addAttribute("currentpage", 1);
		
		//소명승인상태
    	List<CodeInfoVO> apprStatusList = comService.getCodeInfoListNoTitle(MajrCodeInfo.ApprStat);
		model.addAttribute("apprStatusList", apprStatusList);

		return "man/sanct/sanctlist";

	}
	
	/**
	 * 사용안함
	 * @param request
	 * @param searchVO
	 * @return
	 */
	private String getPolSelectBoxCreate(HttpServletRequest request, PolicySearchVO searchVO){
		StringBuffer selectBoxString = new StringBuffer();
		  
		ServletContext servletContext = request.getServletContext();
		List<MenuItemVO> sollist = (List<MenuItemVO>) servletContext.getAttribute("SolMenuList");
		List<EgovMap> pollist = (List<EgovMap>) servletContext.getAttribute("polMenuList");
		
		selectBoxString.append("<li><select class='selected_sol' name='majCode' id='majCode' >");
		selectBoxString.append("<option value='' >대진단 전체</option>");
		for(MenuItemVO row:sollist){
			if(row.getBuseo_indc().equals("Y")){
				continue;
			}
			if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
				//대진단
				selectBoxString.append(String.format("<option value='%s' %s >%s</option>"
						, row.getDiag_majr_code()
						, row.getDiag_majr_code().equals(searchVO.getMajCode()) ? "selected" : ""
						, row.getDiag_desc()));
			}
		}
		selectBoxString.append("</select></li>");
		
		if(searchVO.getMajCode().equals("")){
			selectBoxString.append("<li><select class='selected_pol' name='minCode' id='minCode' >");
			selectBoxString.append("<option value='' >중진단 전체</option>");
			selectBoxString.append("</select></li>");
		}else{
			selectBoxString.append("<li><select class='selected_pol' name='minCode' id='minCode' >");
			selectBoxString.append("<option value='' >중진단 전체</option>");
			for(MenuItemVO row:sollist){
				if(row.getDiag_majr_code().equals(searchVO.getMajCode())){
					if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
						selectBoxString.append(String.format("<option value='%s' %s >%s</option>"
								, row.getDiag_minr_code()
								, row.getDiag_minr_code().equals(searchVO.getMinCode()) ? "selected" : ""
								, row.getDiag_desc()));
					}
				}
			}
			selectBoxString.append("</select></li>");
		}
		
		if(searchVO.getMinCode().equals("")){
			selectBoxString.append("<li><select name='polCode' id='polCode' >");
			selectBoxString.append("<option value='' >지수화정책 전체</option>");
			selectBoxString.append("</select></li>");
		}else{
			selectBoxString.append("<li><select name='polCode' id='polCode' >");
			selectBoxString.append("<option value='' >지수화정책 전체</option>");
			List<EgovMap> sList = CommonUtil.getListFromPolMenuForString(pollist, searchVO.getMajCode(), searchVO.getMinCode());
			if(sList.size() > 0){
				for(EgovMap srow:sList)
				{
					selectBoxString.append(String.format("<option value='%s' %s >%s</option>"
						, srow.get("secpolid")
						, srow.get("secpolid").equals(searchVO.getPolCode()) ? "selected" : ""
						, srow.get("poldesc")));
				}
			}
			selectBoxString.append("</select></li>");
		}
		  
		return selectBoxString.toString();
	}
	
	/**
	 * 제재조치 설정 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/sanctconfig.do")
	public String sanctconfig(HttpServletRequest request, @ModelAttribute("searchVO") PolicySearchVO searchVO, ModelMap model) throws Exception{	

		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SANC);


		List<EgovMap> list = sanctService.getSanctConfigList();
		model.addAttribute("resultList", list);

		return "man/sanct/sanctconfig";

	}

	/**
	 * 진단정책조회
	 * @param request
	 * @param selid
	 * @param majCode
	 * @param minCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getSelectedPollist.do")
	public void getSelectedPollist(HttpServletRequest request, String selid, String majCode, String minCode, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    //ServletContext servletContext = request.getServletContext();
		List<MenuItemVO> sollist = comService.getSolMenuList();		//(List<MenuItemVO>) servletContext.getAttribute("SolMenuList");
		List<EgovMap> pollist = comService.getPolMenuAllList();//(List<EgovMap>) servletContext.getAttribute("polMenuList");
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	List<HashMap<String, String>> polList = new ArrayList<HashMap<String, String>>();
	    	if(selid.equals("majCode")){
	    		//중진단 조회
	    		for(MenuItemVO row:sollist){
					if(row.getDiag_majr_code().equals(majCode)){
						if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
							HashMap<String, String> polInfo = new HashMap<String, String>();
							polInfo.put("code", row.getDiag_minr_code());
							polInfo.put("desc", row.getDiag_desc());
							polList.add(polInfo);
						}
					}
				}
	    	}else{
	    		//정책
	    		List<EgovMap> sList = CommonUtil.getListFromPolMenuForString(pollist, majCode, minCode);
				if(sList.size() > 0){
					for(EgovMap srow:sList)
					{
						HashMap<String, String> polInfo = new HashMap<String, String>();
						polInfo.put("code", srow.get("secpolid").toString());
						polInfo.put("desc", srow.get("poldesc").toString());
						polList.add(polInfo);
					}
				}
	    	}
	    	
	    	isOk = true;
	    	hMap.put("list", polList);
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
	 * 제재 설정 팝업
	 * @param request
	 * @param sanctno
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/sanctconfigpopup.do")
	public void sanctconfigpopup(HttpServletRequest request, String sanctno, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    StringBuffer popupBody = new StringBuffer();
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	EgovMap info = sanctService.getSanctConfigInfo(sanctno);	
	    	
	    	popupBody.append("<div class='ly_block1' style='width:670px;background:#fff;padding:10px 10px 10px 10px'>");
	    	popupBody.append(String.format("<div class='subTT' style='width:100%%;cursor:move;'><span>%s</span></div>", "제재조치 설정"));
	    	popupBody.append("<div class='pd10'></div>");
	    	popupBody.append("<table class='tblInfoType' cellpadding='0' cellspacing='0'>");
    		popupBody.append(" <colgroup><col style='width:20%;' /><col style='width:80%;' /></colgroup>");
	    	popupBody.append("	<tr>");
	    	popupBody.append("		<th>제제조치</th>");
	    	popupBody.append(String.format("	<td>%s<input type='hidden' id='sanctno' name='sanctno' value='%s' /></td>", info.get("sanctnm").toString(), info.get("sanctkind").toString()));
	    	popupBody.append("	</tr>");
	    	popupBody.append("	<tr>");
	    	popupBody.append("		<th>점수(FROM)</th>");
	    	popupBody.append(String.format("	<td><input type='text' style='width:200px' id='scor_from' name='scor_from' value='%s' maxlength='10' /></td>", info.get("scorfrom").toString()));
	    	popupBody.append("	</tr>");
	    	popupBody.append("	<tr>");
	    	popupBody.append("		<th>점수(TO)</th>");
	    	popupBody.append(String.format("	<td><input type='text' style='width:200px' id='scor_to' name='scor_to' value='%s' maxlength='10' /></td>", info.get("scoreto").toString()));
	    	popupBody.append("	</tr>");	
	    	popupBody.append("	<tr>");
	    	popupBody.append("		<th>제재 공지내용</th>");
	    	popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><textarea id='sanctnoti' name='sanctnoti' maxlength='2000' style='width:100%%' rows='7'>%s</textarea></td>", info.get("sanctnoti").toString()));
	    	popupBody.append("	</tr>");
	    	popupBody.append("	<tr>");
	    	popupBody.append("		<th>해제 공지내용</th>");
	    	popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><textarea id='sanctsolnoti' name='sanctsolnoti' maxlength='2000' style='width:100%%' rows='7'>%s</textarea></td>", info.get("sanctsolnoti").toString()));
	    	popupBody.append("	</tr>");
	    	popupBody.append("</table>");
	    	popupBody.append("<div class='btn_black2'>"
	    			+ "			<a class='btn_black btn_popup_save'><span style='color:#ffffff'>저장</span></a> "
    				+ "			<a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a>"
    				+ "		</div>");
	    	popupBody.append("</div>");
	    	
    	/*
	   
	    	popupBody.append("<div id='wrap_pop' style='background: #fff'>");
	    	popupBody.append("	<div class='WP_tit' style='cursor:move;'>");
	    	popupBody.append("		<ul>");
	    	popupBody.append("		<li class='WPT_ic'><img src='/img/ic_stit2.png' alt='아이콘'></li>");
	    	popupBody.append("		<li class='WPT_txt'>제재조치 설정</li>");
	    	popupBody.append("		<li class='WPT_btn'><div class='del2'><a class='btn_dialogbox_close' style='cursor:pointer;'><span class='blind'>닫기</span></a></div></li>");
	    	popupBody.append("		</ul>");
	    	popupBody.append("	</div>");
	    	popupBody.append("	<div class='WP_con'>");
	    	popupBody.append("	<div>");
	    	popupBody.append("		<div class='marT10'>");
	    	popupBody.append("		<table border='0' class='TBS4' cellpadding=0 cellspacing=0>");
	    	popupBody.append("			<colgroup>");
	    	popupBody.append("			<col style='width:20%'>");
	    	popupBody.append("			<col style='width:80%'>");
	    	popupBody.append("			</colgroup>");
	    	popupBody.append("			<tr>");
	    	popupBody.append("				<th>제제조치</th>");
	    	popupBody.append(String.format("	<td>%s<input type='hidden' id='sanctno' name='sanctno' value='%s' /></td>", info.get("sanctnm").toString(), info.get("sanctkind").toString()));
	    	popupBody.append("			</tr>");
	    	popupBody.append("			<tr>");
	    	popupBody.append("				<th>점수(FROM)</th>");
	    	popupBody.append(String.format("	<td><input type='text' style='width:200px' id='scor_from' name='scor_from' value='%s' maxlength='10' /></td>", info.get("scorfrom").toString()));
	    	popupBody.append("			</tr>");
	    	popupBody.append("			<tr>");
	    	popupBody.append("				<th>점수(TO)</th>");
	    	popupBody.append(String.format("	<td><input type='text' style='width:200px' id='scor_to' name='scor_to' value='%s' maxlength='10' /></td>", info.get("scoreto").toString()));
	    	popupBody.append("			</tr>");	
	    	popupBody.append("			<tr>");
	    	popupBody.append("				<th>공지 내용</th>");
	    	popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><textarea id='sanctnoti' name='sanctnoti' maxlength='500' style='width:400px' rows='5'>%s</textarea></td>", info.get("sanctnoti").toString()));
	    	popupBody.append("			</tr>");
	    	popupBody.append("		</table>");
	    	popupBody.append("		</div>");
	    	popupBody.append("		<div style='width:95%' class='marT10 disp'>");
	    	popupBody.append("			<div class='btn3 F_right'><a class='btn_dialogbox_close' style='cursor:pointer;'>닫기</a></div>");
	    	popupBody.append("			<div class='btn3 F_right'><a class='btn_popup_save' style='cursor:pointer;'>저장</a></div>");
	    	popupBody.append("		</div>");
	    	popupBody.append("	</div>");
	    	popupBody.append("	</div>");
	    	popupBody.append("</div>");
	    	*/
	    	StringBuffer script = new StringBuffer();
	    	script.append("<script type='text/javascript'>");
	    	script.append("$(function () {");
	    	script.append("	 $('.btn_popup_save').click(function(){ "
	    				 + "	if(!$('#scor_from').val().isNumber() || !$('#scor_to').val().isNumber()){"
	    				 + "		alert('점수는 숫자로 입력하여 주세요.');"
	    				 + "	  	return false;"
	    				 + "	}"
	    				 + "	if($('#sanctnoti').val() == ''){"
	    				 + "		alert('공지 내용을 입력하여 주세요.');return false;"
	    				 + "	}"
	    				 + "	if (!confirm('제재설정값 및 공지내용을 저장 하시겠습니까?')) { return false; }"
	    				 + "	var data = {sanctno:$('#sanctno').val(), scor_from:$('#scor_from').val(), scor_to:$('#scor_to').val(), sanctnoti:$('#sanctnoti').val(), sanctsolnoti:$('#sanctsolnoti').val()};"
	    				  + "	$.ajax({"
	    				 + "		data: data,"
	    				 + "		url: '/man/sanctconfigSave.do',"
	    				 + "		type: 'POST',"
	    				 + "		dataType: 'json',"
	    				 + "		error: function (jqXHR, textStatus, errorThrown) {"
	    				 + "			alert(textStatus + '\\r\\n' + errorThrown);"
	    				 + "		},"
	    				 + "		success: function (data) {"
	    				 + "			if (data.ISOK) {"
	    				 + "				$('.DialogBox').data('is_save', 'Y');"
	    				 + "				$('.DialogBox').dialog('close');"
	    				 + "			}else{alert(data.MSG); }"
	    				 + "		}"
	    				 + "	});"
	    				 + "  });");
	    	script.append("});");
	    	script.append("</script>");
	    	popupBody.append(script.toString());
	    	//System.out.println(popupBody.toString());
	    	isOk = true;
	       
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		    hMap.put("popup_body", popupBody.toString());
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
	 * 제재설정 정보 저장
	 * @param request
	 * @param sanctno
	 * @param scor_from
	 * @param scor_to
	 * @param sanctnoti
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/sanctconfigSave.do")
	public void sanctconfigSave(HttpServletRequest request
			, String sanctno
			, String scor_from
			, String scor_to
			, String sanctnoti
			, String sanctsolnoti
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	HashMap<String,Object> params = new HashMap<String,Object>();
	    	params.put("sanctno", sanctno);
	    	params.put("scor_from", Integer.parseInt(scor_from));
	    	params.put("scor_to", Integer.parseInt(scor_to));
	    	params.put("sanctnoti", sanctnoti);
	    	params.put("sanctsolnoti", sanctsolnoti);
	    	boolean ret = sanctService.setModifySanctConfigInfo(params);
		    
	        isOk = ret;
	        
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", "");
		    
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
	 * 수동제재를 위한 로그정보 조회
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getSanctionList.do")
	public void getSanctionList(HttpServletRequest request
			, @ModelAttribute("searchVO") PolicySearchVO searchVO
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		//인증확인
		
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
			
	    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
	 		//searchVO.setPageSize(propertiesService.getInt("pageSize"));

	 		PaginationInfo paginationInfo = new PaginationInfo();
	 		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());

	 		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());

	 		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	 		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	 		searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
	 		searchVO.setEnd_date(searchVO.getEnd_date().replace("-",  ""));
	    	
	 		//결재정보조회
	    	List<CodeInfoVO> agreeList = comService.getCodeInfoList(MajrCodeInfo.AgreeCode);
	 		//제재정보조회
	    	List<EgovMap> sanctList = sanctService.getSanctConfigList();
			int sanctMaxValue = 0;
			for(EgovMap row:sanctList){
				if(Integer.parseInt(row.get("scoreto").toString()) > sanctMaxValue){
					sanctMaxValue = Integer.parseInt(row.get("scoreto").toString());
				}
			}
	    	
	 		HashMap<String,Object> retMap = polService.getUserSearchPolDetailReaultListForPageing(searchVO);
			List<SanctSearchResultListVO> result = (List<SanctSearchResultListVO>)retMap.get("list");
			StringBuffer item_body = new StringBuffer();
		    if(result.size() > 0){
		    	for(SanctSearchResultListVO row:result){
		    		String uuid = CommonUtil.CreateUUID();
		    		
			    	item_body.append("<tr style='font-weight:bold;'>");
			    	item_body.append(String.format("<td>%s</td>", row.getIsexpn().equals("Y") ? "예외자" : row.getAppr_stat_code().equals("03") ? "소명완료" : String.format("<input type='checkbox' name='sel_box' value='%s' empno='%s' macno='%s' uip='%s' dt_date='%s' pol_id='%s' scancid='%s'/>"
			    				, uuid
			    				, row.getEmp_no()
			    				, row.getMac()
			    				, row.getIp()
			    				, row.getIdx_rgdt_date().replace("-", "")
			    				, row.getPol_idx_id()
			    				, row.getSanc_id())));
			    	item_body.append(String.format("<td>%s</td>", row.getIdx_rgdt_date()));
			    	item_body.append(String.format("<td><a class='show_upperorgnames' orgcode='%s'>%s</a></td>", row.getOrg_code(), row.getOrg_nm()));
			    	item_body.append(String.format("<td>%s<br>%s</td>", row.getEmp_no(), row.getEmp_nm()));
			    	//item_body.append(String.format("<td>%s<br />%s</td>", row.getMac(), row.getIp()));
			    	item_body.append(String.format("<td>%s</td>", row.getEventdate()));
			    	item_body.append(String.format("<td class='btn_log_view' polcd='%s' empno='%s' bdt='%s' style='cursor:pointer;'><a>%s</a></td>", row.getPol_idx_id(), row.getEmp_no(), row.getIdx_rgdt_date().replace("-", ""), row.getPol_idx_name()));
			    	item_body.append(String.format("<td>%s</td>", row.getCount()));
			    	item_body.append(String.format("<td>%s</td>", row.getScore()));
			    	item_body.append(String.format("<td>%s</td>", String.format("<span class='scondition1' style='background:%s;border:solid 1px %s'>%s</span>", row.getPol_stat_color(), row.getPol_stat_color(), row.getPol_stat_name())));// row.getPol_stat_code().equals("G") ? "<span class='scondition1'>양호</span>" : row.getPol_stat_code().equals("W") ? "<span class='scondition3'>주의</span>" : "<span class='scondition2'>취약</span>"));
			    	item_body.append(String.format("<td>%s</td>"
			    			, row.getIsexpn().equals("Y") ? "예외자" : row.getIs_bother_type().equals("NS") ? row.getIs_bother_txt() : String.format("<a title='제재일 : %s, 해제일 : %s'>%s</a>", row.getSanc_date(), row.getRe_sanc_date(), row.getIs_bother_txt())));
			    	//item_body.append(String.format("<td>%s</td>", row.getIsexpn().equals("Y") ? "예외자" : row.getIs_bother_txt()));
			    	//<span class="scondition2">취약</span>
			    	String sanctType = "<select name='sanct_type' style='width:80px;'>";
			    	for(EgovMap sanctInfo:sanctList){
			    		sanctType += String.format("<option value='%s'%s>%s</option>"
								, sanctInfo.get("sanctkind")
								, "" // Float.parseFloat(String.valueOf(row.get("score"))) >= Float.parseFloat(String.valueOf(sanctInfo.get("scorfrom"))) && Float.parseFloat(String.valueOf(row.get("score"))) < Float.parseFloat(String.valueOf(sanctInfo.get("scoreto"))) ? " selected" : ""
								, sanctInfo.get("sanctnm"));
			    	}
			    	sanctType += "</select>";
			    	item_body.append(String.format("<td>%s</td>", row.getIsexpn().equals("Y") ? "예외자" : sanctType));
			    	
				    item_body.append(String.format("<td>%s</td>"
				    		, row.getIsexpn().equals("Y") ? "예외자" : row.getAppr_cnt() > 0 ? row.getAppr_id().equals("") ? "소명처리" : row.getIs_apprstatus_type().equals("NA") ? row.getIs_apprstatus_txt() : String.format("<a title='소명처리일 : %s, 승인완료일 : %s'>%s</a>", row.getAppr_date(), row.getRe_appr_date(), row.getIs_apprstatus_txt()) : row.getIs_apprstatus_txt() ));

				    String agreeSelect = "<select name='app_target' style='width:80px;'>";
				    for(CodeInfoVO code:agreeList)
				    {
				    	if(code.getMinr_code().equals("00")){
				    		continue;
				    	}
				    	agreeSelect += String.format("<option value='%s'%s>%s</option>"
				    									, code.getMinr_code()
				    									, row.getAppr_line_code().equals(code.getMinr_code()) ? " selected" : ""
				    									, code.getCode_desc());
				    }
				    agreeSelect += "</select>";
				    item_body.append(String.format("<td>%s</td>", row.getIsexpn().equals("Y") ? "예외자" : row.getAppr_cnt() > 0 ? row.getAppr_id().equals("") ? "소명처리" : String.format("<a class='btn_scr3 btn_appr_info' apprid='%s'><span>소명정보</span></a>", row.getAppr_id()) : agreeSelect ));
			    	item_body.append(String.format("<td>%s</td>", row.getIsexpn().equals("Y") ? "예외자" : row.getMac().equals("") ? "미대상" : row.getExe_para().equals("") ? "-" : row.isPcg_indc() ? String.format("<a class='btn_scr3 btn_pcgact_info' mode='P' style='cursor:pointer;' pcgparam='%s' pcgactid='%s' empno='%s' macno='%s' uip='%s' dt_date='%s' pol_id='%s'><span>강제조치</span></a>"
				    		, row.getExe_para()
				    		, uuid
				    		, row.getEmp_no()
				    		, row.getMac()
				    		, row.getIp()
				    		, row.getIdx_rgdt_date().replace("-", "")
				    		, row.getPol_idx_id()) : "-" ));
				    item_body.append("</tr>");
			    	
			    }
		    }else{
		    	item_body.append("<tr style='font-weight:bold;height:200px;'>");
		    	item_body.append(String.format("<td colspan='14'>%s</td>", "진단정보가 없습니다."));
		    	item_body.append("</tr>");
		    }
			//list_body
		
			int totalCnt = (int)retMap.get("totalCount");
	    	int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			
	        isOk = true;
	        hMap.put("list_body", item_body);
	        hMap.put("totalPage", TotPage);
	        hMap.put("currentpage", searchVO.getPageIndex());
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", "");
		    hMap.put("totalCnt", totalCnt);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
	 * 소명, 제재, PC지키미 조치 실행 팝업
	 * @param request
	 * @param mode
	 * @param sanctlist
	 * @param apprlist
	 * @param pcgactinfo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setSanctAllPrecessPopup.do")
	public void setSanctAllPrecessPopup(HttpServletRequest request
			, String mode
			, String sanctlist
			, String apprlist
			, String pcgactinfo
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		//System.out.println(mode + "][" + sanctlist + "][" + apprlist + "][" + pcgactinfo);
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    StringBuffer popupBody = new StringBuffer();
	    String popupTitle = "소명요청 일괄 처리";
	    String btnCaption = "소명요청";
	    if(mode.equals("S")){
	    	popupTitle = "제재조치(소명요청) 일괄 처리";
	    	btnCaption ="제재(소명요청포함) 처리";
	    }else if(mode.equals("P")){
	    	popupTitle = "PC지키미 조치 처리";
	    	btnCaption = "조치저장";
	    }
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	popupBody.append("<div class='ly_block1' style='width:100%;'>");
	    	popupBody.append(String.format("<div class='subTT' style='width:100%%;cursor:move;'><span>%s</span></div>", popupTitle));
	    	popupBody.append("<div class='ly_list' style='height:310px;overflow-y:auto;'>");
	    	if(mode.equals("A") || mode.equals("S")){
		        if(mode.equals("S")){
		        	//제재정보조회
	    	    	List<EgovMap> sanctList = sanctService.getSanctConfigList();
	    	    	String sanctType = "<select name='sancttype' id='sancttype' disabled >";
			    	for(EgovMap sanctInfo:sanctList){
			    		sanctType += String.format("<option value='%s'%s>%s</option>"
								, sanctInfo.get("sanctkind")
								, ""
								, sanctInfo.get("sanctnm"));
			    	}
			    	sanctType += "</select>";

	        		popupBody.append(String.format("<div class='popTT'><img src='/img/icon_arw4.jpg' /> 제재 일괄처리여부<input type='checkbox' name='ischksanct' id='ischksanct' > %s</div>", sanctType));
	        		popupBody.append("		<table border='0' class='ly_tbl' cellpadding=0 cellspacing=0>");
	    	    	popupBody.append("			<colgroup>");
	    	    	popupBody.append("			<col style='width:20%'>");
	    	    	popupBody.append("			<col style='width:25%'>");
	    	    	popupBody.append("			<col style='width:25%'>");
	    	    	popupBody.append("			<col style='width:30%'>");
	    	    	popupBody.append("			</colgroup>");
	    	    	
	    	    	popupBody.append("			<tr>");
	    	    	popupBody.append("				<th>사번</th>");
	    	    	popupBody.append("				<th>MAC/IP</th>");
	    	    	popupBody.append("				<th>처리내용</th>");
	    	    	popupBody.append("				<th>제재(결재)구분</th>");
	    	    	popupBody.append("			</tr>");
	        		
	        		if(!sanctlist.trim().equals("")){
	        			List<String> slist = CommonUtil.SplitToString(sanctlist, ",");
	        			int i = 1;
		        		for(String str:slist){
		        			String[] sInfo = str.split("/");
		        			popupBody.append("<tr>");
		        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", sInfo[0]));
		        			popupBody.append(String.format("	<td class='%s'>%s<br>%s</td>", i % 2 == 0 ? "cell2" : "cell1", sInfo[2], sInfo[3]));
		        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", "제재"));
		        			String sType = "";
		        			for(EgovMap cInfo:sanctList){
		        				if(cInfo.get("sanctkind").equals(sInfo[6])){
		        					sType=cInfo.get("sanctnm").toString();
		        					break;
		        				}
		        			}
		        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", sType));
		        			i++;
		        		}
	        		}
	        		popupBody.append("	</table>");
		       	}
		        
		      //결재정보조회
    	    	List<CodeInfoVO> agreeList = comService.getCodeInfoList(MajrCodeInfo.AgreeCode);
    	    	String agreeSelect = "<select name='apptarget' id='apptarget' disabled>";
    	    	for(CodeInfoVO code:agreeList)
    	    	{
    	    		if(code.getMinr_code().equals("00")){
    	    			continue;
    	    		}
    	    		agreeSelect += String.format("<option value='%s'%s>%s</option>"
    	    				, code.getMinr_code()
    	    				, ""
    	    				, code.getCode_desc());
    	    	}
    	    	agreeSelect += "</select>";
    	    	popupBody.append(String.format("<div class='popTT'><img src='/img/icon_arw4.jpg' /> 소명 결재라인 일괄 적용여부<input type='checkbox' name='ischkapprline' id='ischkapprline' > %s</div>", agreeSelect));
    	    	
    	    	popupBody.append("		<table border='0' class='ly_tbl' cellpadding=0 cellspacing=0>");
    	    	popupBody.append("			<colgroup>");
    	    	popupBody.append("			<col style='width:20%'>");
    	    	popupBody.append("			<col style='width:25%'>");
    	    	popupBody.append("			<col style='width:25%'>");
    	    	popupBody.append("			<col style='width:30%'>");
    	    	popupBody.append("			</colgroup>");
    	    	
    	    	popupBody.append("			<tr>");
    	    	popupBody.append("				<th>사번</th>");
    	    	popupBody.append("				<th>MAC/IP</th>");
    	    	popupBody.append("				<th>처리내용</th>");
    	    	popupBody.append("				<th>제재(결재)구분</th>");
    	    	popupBody.append("			</tr>");
	        	if(!apprlist.trim().equals("")){
	        		List<String> alist = CommonUtil.SplitToString(apprlist, ",");
	        		int i=1;
	        		for(String str:alist){
	        			String[] sInfo = str.split("/");
	        			popupBody.append("<tr>");
	        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", sInfo[0]));
	        			popupBody.append(String.format("	<td class='%s'>%s<br>%s</td>", i % 2 == 0 ? "cell2" : "cell1", sInfo[2], sInfo[3]));
	        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", "소명"));
	        			String aline = "";
	        			for(CodeInfoVO cInfo:agreeList){
	        				if(cInfo.getMinr_code().equals(sInfo[6])){
	        					aline=cInfo.getCode_desc();
	        					break;
	        				}
	        			}
	        			popupBody.append(String.format("	<td class='%s'>%s</td>", i % 2 == 0 ? "cell2" : "cell1", aline));
	        			i++;
	        		}
	        	}
	        	popupBody.append("</table>");
	        	popupBody.append("</div>");
		        
		        
	    	}else{
	    		
	    		popupBody.append("<table class='tblInfoType' cellpadding='0' cellspacing='0'>");
	    		popupBody.append(" <colgroup><col style='width:20%;' /><col style='width:80%;' /></colgroup>");
    	    	String[] sInfo = pcgactinfo.split("/");
    	    	String paramString = sanctService.getPcgActParamInfo(sInfo[6]);
    	    	popupBody.append("			<tr>");
    	    	popupBody.append("				<th>사번</th>");
    	    	popupBody.append(String.format("<td>%s</td>", sInfo[0]));
    	    	popupBody.append("			</tr>");
    	    	popupBody.append("			<tr>");
    	    	popupBody.append("				<th>MAC</th>");
    	    	popupBody.append(String.format("<td>%s</td>", sInfo[2]));
    	    	popupBody.append("			</tr>");
    	    	popupBody.append("			<tr>");
    	    	popupBody.append("				<th>IP</th>");
    	    	popupBody.append(String.format("<td>%s</td>", sInfo[3]));
    	    	popupBody.append("			</tr>");
    	    	popupBody.append("			<tr>");
    	    	popupBody.append("				<th>PC지키미<br/>조치내용</th>");
    	    	popupBody.append(String.format("<td>%s</td>", paramString));
    	    	popupBody.append("			</tr>");
    	    	popupBody.append("		</table>");
	    	}
		    
	    	popupBody.append(String.format("<div class='btn_black2'>"
	    			+ "			<a class='btn_black btn_popup_save'><span style='color:#ffffff'>%s</span></a> "
	    			+ "			<a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a>"
	    			+ "		</div>", btnCaption));
	    	popupBody.append("</div>"
	    			+ "		<div id='poploading' style='display:none;top:170px;left:280px;position:absolute;' ><img style='margin:0 auto;' src='/img/loading.gif' /></div>");
	    	popupBody.append(String.format("<input type='hidden' name='mode' value='%s' />", mode));
	    	popupBody.append(String.format("<input type='hidden' name='sanctlist' value='%s' />", sanctlist));
	    	popupBody.append(String.format("<input type='hidden' name='apprlist' value='%s' />", apprlist));
	    	popupBody.append(String.format("<input type='hidden' name='pcgactinfo' value='%s' />", pcgactinfo));
	    	
	    	StringBuffer script = new StringBuffer();
	    	script.append("<script type='text/javascript'>");
	    	script.append("$(function () {");
	    	script.append("$('.btn_popup_save').click(function(){ "
	    			+ "			if($('input:hidden[name=mode]').val() == 'S'){"
	    				 + "		if($('input:checkbox[name=ischksanct]').is(':checked')){"
	    				 + "			if(!confirm('제재유형을 [' + $('#sancttype option:selected').text() + ']으로 일괄 처리 하시겠습니까?')){ return false; }"
	    				 + "		}"
	    				 + "	}"
	    				 + "	if($('input:hidden[name=mode]').val() != 'P'){"
	    				 + "		if($('input:checkbox[name=ischkapprline]').is(':checked')){"
	    				 + "			if(!confirm('소명 결재 라인을 [' + $('#apptarget option:selected').text() + ']으로 일괄 처리 하시겠습니까?')){ return false; }"
	    				 + "		}"
	    				 + "	}"
	    				 + "	var msg = '일괄처리를 진행 하시겠습니까?';"
	    				 + "	if($('input:hidden[name=mode]').val() == 'P'){ msg = 'PC지키미 조치를 저장 하시겠습니까?'; }"
	    				 + "	if (!confirm(msg)) { return false; }"
	    				 + "	var mode=$('input:hidden[name=mode]').val();"
	    				 + "	var sanctlist=$('input:hidden[name=sanctlist]').val();"
	    				 + "	var apprlist=$('input:hidden[name=apprlist]').val();"
	    				 + "	var pcgactinfo=$('input:hidden[name=pcgactinfo]').val();"
	    				 + "	var ischksanct = $('input:checkbox[name=ischksanct]').is(':checked') ? 'Y' : 'N';"
	    				 + "	var ischkapprline = $('input:checkbox[name=ischkapprline]').is(':checked') ? 'Y' : 'N';"
	    				 + "	var sancttype=$('#sancttype').val();"
	    				 + "	var apptarget=$('#apptarget').val();"
	    				
 						 + "	if($('input:hidden[name=mode]').val() == 'P'){ msg = 'PC지키미 조치를 저장 하시겠습니까?'; }"
	    				 + "	var data = {mode:mode, sanctlist:sanctlist, apprlist:apprlist, pcgactinfo:pcgactinfo, ischksanct:ischksanct, ischkapprline:ischkapprline, sancttype:sancttype, apptarget:apptarget};"
	    				 + "	$.ajax({"
	    				 + "		data: data,"
	    				 + "		url: '/man/setSanctAllRegister.do',"
	    				 + "		type: 'POST',"
	    				 + "		dataType: 'json',"
	    				 + "		error: function (jqXHR, textStatus, errorThrown) {"
	    				 + "			alert(textStatus + '\\r\\n' + errorThrown);"
	    				 + "		},"
	    				 + "		success: function (data) {"
	    				 + "			if (data.ISOK) {"
	    				 + "				var rmsg='처리유형  사      번  BPM연동  처리결과  메일연동 \\r\\n';"
	    				 + "				rmsg +='--------------------------------------------------------\\r\\n';"
	    				 + "				var result=data.RESULT;"
	    				 + "				for(var i=0;i<result.length;i++){"
	    				 + "					rmsg += result[i].PTYPE + '  ' + result[i].EMPNO + '  '+ result[i].BPM + '  ' + result[i].RESULT + '  ' + result[i].MAIL + '\\r\\n'"
	    				 + "				}"
	    				 + "				alert(rmsg);"
	    				 + "				$('.DialogBox').data('is_save', 'Y');"
	    				 + "				$('.DialogBox').dialog('close');"
	    				 + "			}else{alert(data.MSG); }"
	    				 + "		},"
	    				 + "		beforeSend: function () {"
	    				 + "			$('#poploading').show().fadeIn('fast');"
	    				 + "		},"
	    				 + "		complete: function () {"
	    				 + "			$('#poploading').fadeOut();"
	    				 + "		}"
	    				 + "	});"
	    				 
	    				 + "  });");
	    	script.append("$('#ischksanct').click(function(){"
	    			+ "  var checkval = $('#ischksanct').is(':checked');"
	    			+ "  $('#sancttype').attr('disabled', !checkval);"
	    			+ "});");
	    	script.append("$('#ischkapprline').click(function(){"
	    			+ "  var checkval = $('#ischkapprline').is(':checked');"
	    			+ "  $('#apptarget').attr('disabled', !checkval);"
	    			+ "});");
	    	script.append("});");
	    	script.append("</script>");
	    	popupBody.append(script.toString());
	    	//System.out.println(popupBody.toString());
	    	isOk = true;
	       
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		    hMap.put("popup_body", popupBody.toString());
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
	 * 수동 제재,소명요청, PC지키미 실행 처리
	 * @param request
	 * @param checkSanctlist
	 * @param checklist
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setSanctAllRegister.do")
	public void setApprAllRegister(HttpServletRequest request
			, String mode    //P : PC지키미, S : 제재, A : 소명처리
			, String sanctlist //제재 정보 
			, String apprlist //소명 정보
			, String pcgactinfo //PC지키미 정보
			, String ischksanct //제재유형 변경인가?
			, String ischkapprline //소명 결재라인 변경인가?
			, String sancttype  //제재 변경유형 
			, String apptarget  //소명라인 변경유형
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	//BPM 실행여부
	    	CodeInfoVO codeInfo = new CodeInfoVO();
	    	codeInfo.setMajr_code(MajrCodeInfo.ExecuteCode);
	    	codeInfo.setMinr_code("BPM");
	    	CodeInfoVO bpmInfo = comService.getCodeInfoForOne(codeInfo);
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO malInfo = comService.getCodeInfoForOne(codeInfo);
	    	
	    	codeInfo.setMajr_code(MajrCodeInfo.UsedCode);
	    	codeInfo.setMinr_code("BPM");
	    	CodeInfoVO isbpmInfo = comService.getCodeInfoForOne(codeInfo);
	    	codeInfo.setMinr_code("MAL");
	    	CodeInfoVO ismalInfo = comService.getCodeInfoForOne(codeInfo);
	    	
	    	boolean isBPM = bpmInfo.getCode_desc().equals("Y") ? true : false;
	    	boolean isMail = malInfo.getCode_desc().equals("Y") ? true : false;
	    	boolean isBPMCode = isbpmInfo.getCode_desc().equals("Y") ? true : false;
	    	boolean isMailCode = ismalInfo.getCode_desc().equals("Y") ? true : false;
	    	
	    	List<HashMap<String,String>> resultMap = new ArrayList<HashMap<String, String>>();
	    	
	    	/**************************************************************
	    	 * 모든처리를 건by건으로 처리함 - 처리중 메일 발송, BPM처리를 위하여
	    	 **************************************************************/
	    	if(!mode.equals("P")){
	    		//제재이거나 소명
	    		//소명 / 제재시 BPM처리후 소명정보 저장
	    		
	    		if(!apprlist.trim().equals("")){
        			List<String> slist = CommonUtil.SplitToString(apprlist, ",");
	        		for(String str:slist){
	        			HashMap<String, String> result = new HashMap<String, String>();
	        			
	        			
	        			String[] sInfo = str.split("/");
	        			HashMap<String, Object> param = new HashMap<String, Object>();
	        			String apprid=CommonUtil.CreateUUID();
	        			
	        			result.put("PTYPE", "소명요청");
	        			result.put("EMPNO", sInfo[0]);
	        			
	        			
	        			param.put("apprid", apprid);
	        			param.put("empno", sInfo[0]);
	        			String apprlineCode = sInfo[6];
	        			if(ischkapprline.equals("Y"))
	        			{
	        				apprlineCode = apptarget;
	        			}
	        			param.put("apprlinecode", apprlineCode);
	        			param.put("rgdtdate", sInfo[5].replace("-", ""));
	        			param.put("mac", sInfo[2]);
	        			param.put("ip", sInfo[3]);
	        			param.put("polidxid", sInfo[4]);
	        			param.put("mode", mode);
	        			
	        			/**
	        			 * BPM처리
	        			 *  
	        			 */
	        			/***************************/
	        			try
	        			{
	        				boolean bpm_ret = false;
	        				boolean appr_ret = false;
	        				boolean mail_ret = false;
	        				boolean appr_inst = false;
	        				if(isBPM){
	        					//BPM 처리성공시 소명정보 등록
	        					/**
	        					 * 소명정보 INSERT
	        					 */
	        					appr_inst = sanctService.setApprInfoInsert(param);
	        					/**
	        					 * 소명코드 업데이트
	        					 */
        						appr_ret = sanctService.setApprInfoRegister(param);
        						
        						if(appr_ret && appr_inst){
        							  
        							bpm_ret = isBPMCode ? BPMUtil.BPMSend(sInfo[0], apprlineCode, apprid, comService) : true;
    	        					if(bpm_ret){
    	        	        			result.put("RESULT", "처리완료");
    	        					}else{
    	        						/**
    	        						 * BPM 처리 실패시 소명정보 삭제 
    	        						 */
    	        						boolean rollback = sanctService.setApprInfoRollback(param);
    	        						result.put("RESULT", "BPM실패");
    	        					}
        						}else{
        							result.put("RESULT", "소명실패");
        						}
	        					
	        				}else{
	        					result.put("RESULT", "BPM차단");
	        				}
	        				
	        				result.put("BPM", isBPM ? bpm_ret ? "전송완료" : "전송실패" : "전송차단");
	        				/***************************/
		        			/**
		        			 * 메일전송 
		        			 * 메일은 BPM 전송 완료 + 소명처리 완료시 전송함
		        			 */
		        			if(isMail){
		        				
		        				if(bpm_ret && appr_ret){
		        					MailInfoVO InfoVO = new MailInfoVO();
		        					InfoVO.setIdx_rgdt_date(param.get("rgdtdate").toString());
		        					InfoVO.setEmp_no(param.get("empno").toString());
		        					InfoVO.setIp(param.get("ip").toString());
		        					InfoVO.setMac(param.get("mac").toString());
		        					InfoVO.setPol_idx_id(param.get("polidxid").toString());
		        					InfoVO.setSancttype("");
		        					
		        					MailSendLogVO mailLog = MailUtil.getMailMessage("A", InfoVO, param.get("empno").toString(), comService); 
		    	        			mail_ret = isMailCode ? MailUtil.SendMail(mailLog, comService) : true;
		    	        			result.put("MAIL", mail_ret ? "전송완료" : "전송실패");
		        				}else{
		        					result.put("MAIL", "처리실패");
		        				}
		        			}else{
		        				result.put("MAIL", "전송차단");
		        			}
		        				
	        			}catch(Exception e){
	        				e.printStackTrace();
	        			}finally{
	        				/**
							 * BPM 및 소명관련 로그저장
							 */
	        				BPMInfoVO bpmLog = new BPMInfoVO();
							bpmLog.setAppr_id(apprid);
							bpmLog.setBpm_ret_code("-999");
							bpmLog.setBpm_id("");
							bpmLog.setStatus_type("L");
							bpmLog.setAppr_commment(String.format("BPM 및 소명 처리 : 결재구분 : %s, 진단날짜 : %s, MAC : %s, IP : %s, POL_IDX_ID : %s, BPM결과 : %s, 소명처리결과 : %s, 메일처리결과 : %s"
									, param.get("apprlinecode")
									, param.get("rgdtdate")
									, param.get("mac")
									, param.get("ip")
									, param.get("polidxid")
									, result.get("BPM")
									, result.get("RESULT")
									, result.get("MAIL")));
							bpmLog.setEmp_no(sInfo[0]);
							comService.InsertBPMLog(bpmLog);
	        				
	        			}
	        			
	        			resultMap.add(result);
	        		}
	    		}
	    		
	    		if(mode.equals("S")){
	    			/**
	    			 * 제재처리
	    			 */
	    			/**
	    			 * 제재 Pass 사이트 및 아이피 조회
	    			 */
	    			codeInfo.setMajr_code(MajrCodeInfo.SanctPassIP);
	    	    	codeInfo.setMinr_code("1");
	    	    	CodeInfoVO passInfo = comService.getCodeInfoForOne(codeInfo);
	    			
	    	    	/**
	    	    	 * 제재조치 시간 조회
	    	    	 */
	    	    	codeInfo.setMajr_code(MajrCodeInfo.SanctScreenBlockTime);
	    	    	codeInfo.setMinr_code("SEC");
	    	    	CodeInfoVO screenblockInfo = comService.getCodeInfoForOne(codeInfo);
	    	    	
	    			if(!sanctlist.trim().equals("")){
	        			List<String> slist = CommonUtil.SplitToString(sanctlist, ",");
		        		for(String str:slist){
		        			HashMap<String, String> result = new HashMap<String, String>();
		        			
		        			String[] sInfo = str.split("/");
		        			
		        			
		        			HashMap<String, Object> param = new HashMap<String, Object>();
		        			String sanctid = sInfo[1].trim();
		        			
		        			result.put("PTYPE", "제재처리");
		        			result.put("EMPNO", sInfo[0]);
		        			result.put("BPM", "해당없음");
		        			
		        			param.put("empno", sInfo[0]);
		        			String stype = sInfo[6];
		        			if(ischksanct.equals("Y"))
		        			{
		        				stype = sancttype;
		        			}
		        			param.put("sancttype", stype);
		        			param.put("pcgactparam", "");
		        			param.put("passid", passInfo.getCode_desc());
		        			param.put("rgdtdate", sInfo[5].replace("-", ""));
		        			param.put("mac", sInfo[2]);
		        			param.put("ip", sInfo[3]);
		        			param.put("polidxid", sInfo[4]);
		        			param.put("mode", mode);
		        			param.put("screenblock", screenblockInfo.getCode_desc());
		        			//소명정보 있을경우 제재처리함 : 소명코드 조회 없을경우 소명정보 없음
		        			
		        			String apprcode = sanctService.getApprCodeInfo(param);
		        			if(apprcode.equals("")){
		        				result.put("RESULT", "소명없음");
		        				result.put("MAIL", isBPM ? "처리보류" : "전송차단");
		        				resultMap.add(result);
		        				continue;
		        			}
		        			if(sanctid.equals("")){
		        				sanctid = apprcode;
		        			}
		        			param.put("sanctid", sanctid);
		        			
		        			boolean scnc_ret = sanctService.setSanctInfoRegister(param, mode);
		        			result.put("RESULT", scnc_ret ? "처리완료" : "처리실패");
		        			/**
		        			 * 메일전송 
		        			 */
		        			boolean mail_ret = false;
		        			if(isMail){
		        				if(scnc_ret){
		        					MailInfoVO InfoVO = new MailInfoVO();
		        					InfoVO.setIdx_rgdt_date(param.get("rgdtdate").toString());
		        					InfoVO.setEmp_no(param.get("empno").toString());
		        					InfoVO.setIp(param.get("ip").toString());
		        					InfoVO.setMac(param.get("mac").toString());
		        					InfoVO.setPol_idx_id(param.get("polidxid").toString());
		        					InfoVO.setSancttype(param.get("sancttype").toString());
		        					
		        					MailSendLogVO mailLog = MailUtil.getMailMessage("S", InfoVO, param.get("empno").toString(), comService); 
		        					mail_ret = isMailCode ? MailUtil.SendMail(mailLog, comService) : true;
				        			result.put("MAIL", mail_ret ? "전송완료" : "전송실패");
			        			}else{
		        					result.put("MAIL", "처리실패");
		        				}
		        			}else{
		        				result.put("MAIL", "전송차단");
		        			}
		        			
		        			
		        			resultMap.add(result);
		        			
		        		}
		    		}
	    		}
	    	}else{
	    		//PC지키미
	    		
	    		if(!pcgactinfo.trim().equals("")){
	    			HashMap<String, String> result = new HashMap<String, String>();
	    			
	        		String[] sInfo = pcgactinfo.split("/");
	        		HashMap<String, Object> param = new HashMap<String, Object>();
	        		String sanctid=CommonUtil.CreateUUID();
	        		
	        		result.put("PTYPE", "PC지키미");
        			result.put("EMPNO", sInfo[0]);
        			result.put("BPM", "해당없음");	        		
	        		
	        		param.put("sanctid", sanctid);
	        		param.put("empno", sInfo[0]);
	        		
	        		param.put("sancttype", "2");
	        		param.put("pcgactparam", sInfo[6]);
	        		param.put("passid", "");
	        		param.put("rgdtdate", sInfo[5].replace("-", ""));
	        		param.put("mac", sInfo[2]);
	        		param.put("ip", sInfo[3]);
	        		param.put("polidxid", sInfo[4]);
	        		param.put("mode", mode);
	        		boolean pcact_ret = sanctService.setSanctInfoRegister(param, mode);
	        		result.put("RESULT", pcact_ret ? "처리완료" : "처리실패");
	        		/**
        			 * 메일전송 ? 
        			 */
	        		boolean mail_ret = false;
	        		/*
	        		if(isMail){
	        			if(pcact_ret){
	        				MailInfoVO InfoVO = new MailInfoVO();
        					InfoVO.setIdx_rgdt_date(param.get("rgdtdate").toString());
        					InfoVO.setEmp_no(param.get("empno").toString());
        					InfoVO.setIp(param.get("ip").toString());
        					InfoVO.setMac(param.get("mac").toString());
        					InfoVO.setPol_idx_id(param.get("polidxid").toString());
        					InfoVO.setSancttype(param.get("sancttype").toString());
        					
        					MailSendLogVO mailLog = MailUtil.getMailMessage("P", InfoVO, param.get("empno").toString(), comService); 
        					mail_ret = isMailCode ? MailUtil.SendMail(mailLog, comService) : true;
	        				result.put("MAIL", mail_ret ? "전송완료" : "전송실패");
	        			}else{
	        				result.put("MAIL", "처리실패");
	        			}
	        		}else{
	        			result.put("MAIL", "전송차단");
	        		}
	        		*/
	        		result.put("MAIL", "처리완료");
        			resultMap.add(result);
	    		}
	    	}
	    	
	    	
	    	isOk = true;	
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		    hMap.put("RESULT", resultMap);
		  
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.toString();
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		    hMap.put("RESULT", "");
			
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
	 * 수동제재 리스트 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/sanctionListexportexcel.do")
	public void sanctionListexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") PolicySearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		
		
		searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
 		searchVO.setEnd_date(searchVO.getEnd_date().replace("-",  ""));
 		
 		String fileName = String.format("%s_%s_소명제재_리스트", searchVO.getBegin_date(), searchVO.getEnd_date());
 		String FileName = URLEncoder.encode(fileName, "UTF-8").replace("\\", "%20");
 		
		List<HashMap<String, Object>> list = polService.getUserSearchSanctiontListForExportExcel(searchVO);
		
		List<String[]> contents = setCSVContents(list);
		
		ExcelUtil.hashMapToExportCsv(response, FileName, contents);
		//ExcelUtil.stringToExportCsv(response, setContent(list), FileName);
		
		/*
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("검색결과");
		excelVO.setTitle("검색결과");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		searchVO.setBegin_date(searchVO.getBegin_date().replace("-", ""));
 		searchVO.setEnd_date(searchVO.getEnd_date().replace("-",  ""));
 		
		List<HashMap<String, Object>> list = polService.getUserSearchSanctiontListForExportExcel(searchVO);
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
				map.put(key, row.get(key).toString());
			}
			excellist.add(map);
		}
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
		//ExcelUtil.htmlStirngToExportExcel(response, excelVO, excellist);
		 */
		
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "org_code" : ColumnName = "조직코드";break;
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "성명";break;
		case "org_nm" : ColumnName = "조직명";break;
		case "score" : ColumnName = "점수";break;
		case "count" : ColumnName = "건수";break;
		case "idx_rgdt_date" : ColumnName = "지수수집일";break;
		case "pol_idx_id" : ColumnName = "정책코드";break;
		case "mac" : ColumnName = "MAC";break;
		case "ip" : ColumnName = "IP";break;
		case "appr_id" : ColumnName = "소명코드";break;
		case "sanc_id" : ColumnName = "제재코드";break;
		
		case "isexpn" : ColumnName = "예외자여부";break;
		case "pol_stat_code" : ColumnName = "진단내역";break;
		
		case "is_pcgact" : ColumnName = "PC지키미실행여부";break;
		case "is_bother_txt" : ColumnName = "제재상태";break;
		case "is_apprstatus_txt" : ColumnName = "소명상태";break;
		case "pol_idx_name" : ColumnName = "정책명";break;
		case "sanc_date" : ColumnName="제재일";break;
		case "re_sanc_date" : ColumnName="제재해제일";break;
		case "appr_date" : ColumnName="소명처리일";break;
		case "re_appr_date" : ColumnName="소명완료일";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
}
