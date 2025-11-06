package sdiag.board.web;


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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.MimeBodyPartVO;
import sdiag.exception.service.ReqExIpInfoVO;
import sdiag.exception.service.ReqExIpPolicyInfoVO;
import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.ExcePtionRulerVO;
import sdiag.board.service.NoticeService;
import sdiag.board.service.NoticeVO;
import sdiag.board.service.QnaVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.CommonUtil;
import sdiag.util.MailUtil;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class NoticeController {
	
	@Resource(name = "NoticeService")
	private NoticeService noticeService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	/**
	 *  공지사항 List 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/noticelist.do")
	public String noticelist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
	
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.NOTICE);
		//searchVO.setPageIndex(2);
		MemberVO memVO = CommonUtil.getMemberInfo();
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		searchVO.setEmp_no(memVO.getUserid());
		
		HashMap<String,Object> retMap = noticeService.getNoticeList(searchVO);
		List<NoticeVO> list = (List<NoticeVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		
		
		model.addAttribute("btn_is_show", memVO.getRole_code().equals("1") | memVO.getRole_code().equals("2") ? "T" : "F");
		return "man/notice/noticelist";
	}
	
	/**
	 * 공지사항 페이지이동
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/noticeRegisterPage.do")
	public String noticeRegisterPage(HttpServletRequest request, 
								@ModelAttribute("noticeVO")NoticeVO noticeVO, 
								ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.NOTICE);
		NoticeVO borderInfo = new NoticeVO();
		if((int)noticeVO.getSq_no() > 0){
			//수정
			borderInfo = noticeService.getNoticeInfo((int)noticeVO.getSq_no());
			
		}
		model.addAttribute("borderInfo", borderInfo);
		return "man/notice/noticemodify";
	}
	
	/**
	 *  공지사항 (insert/update)
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/noticeRegister.do")
	public String noticeRegister(HttpServletRequest request, 
								@ModelAttribute("noticeVO") NoticeVO noticeVO, 
								ModelMap model) throws Exception{	
		MemberVO memInfo = CommonUtil.getMemberInfo();
		if(!noticeVO.getTitle().equals("") && !noticeVO.getContents().equals("")){
			if((int)noticeVO.getSq_no() > 0)
			{			
				noticeVO.setUpd_user(memInfo.getUserid());
				noticeService.NoticeUpdate(noticeVO);
			}else{
				noticeVO.setUpd_user(memInfo.getUserid());
				noticeService.setNoitceInsert(noticeVO);
			}
		}
		
		
		return "redirect:/man/noticelist.do";
	}
	
	/**
	 * 공지 상세보기
	 * @param response
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/noticeview.do")
	public String noticeview(HttpServletResponse response, 
			HttpServletRequest request, 
			@ModelAttribute("searchVO") SearchVO searchVO, 
			ModelMap model) throws Exception{
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.NOTICE);
		// 조회수 쿠키이용
		boolean isReadcnt = CommonUtil.setCommunityReadCnt(response, request, searchVO.getSq_no(), "CommunityReadCnt");
		if(isReadcnt)
		{
			noticeService.setCommunityReadCount(searchVO.getSq_no());
		}
		NoticeVO borderInfo = noticeService.getNoticeInfo(searchVO.getSqno());
		
		borderInfo.setContents(borderInfo.getContents().replace("\r\n", "<br />"));
		model.addAttribute("borderInfo", borderInfo);
		
		MemberVO memVO = CommonUtil.getMemberInfo();
		model.addAttribute("btn_is_show", memVO.getRole_code().equals("1") | memVO.getRole_code().equals("2") ? "T" : "F");
		
		HashMap<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("board_type", "0000");
		hMap.put("seq_no", searchVO.getSqno());
		hMap.put("emp_no", memVO.getUserid());
		noticeService.boardReadLogUpdate(hMap);
		
		return "man/notice/noticeview";
	}

	/**
	 *  공지 삭제
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/noticelistDelete.do")
	public String NoticelistDelete(HttpServletRequest request, @ModelAttribute("noticeVO") NoticeVO noticeVO, ModelMap model) throws Exception{
		System.out.println(noticeVO.getSq_no() + "][");
		noticeService.NoticelistDelete(noticeVO);
		
		return "redirect:/man/noticelist.do";
	}
	
	// 예외자팝업 추가관리 LIST
//	@RequestMapping(value="/pop/expnListPopUplist.do")
//	public String expnListPopUplist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
//		
//		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
//		searchVO.setPageSize(propertiesService.getInt("pageSize"));
//
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
//		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
//		 
//		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
//		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//		
//		HashMap<String,Object> retMap = noticeService.expnListPopUplist(searchVO);
//		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)retMap.get("list");
//		int totalCnt = (int)retMap.get("totalCount");
//		
//		model.addAttribute("resultList", list);
//		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
//		model.addAttribute("totalPage", TotPage);
//		model.addAttribute("currentpage", searchVO.getPageIndex());
//		
//		return "pop/expnListPopUp";
//	}

	/**
	 *  예외자팝업 추가조회
	 * @param request
	 * @param searchCondition
	 * @param searchKeyword
	 * @param levelcondit
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/pop/getexpnListPopUplist.do")
	public void getexpnListPopUplist(HttpServletRequest request
//			, String capno
			, String searchCondition
			, String searchKeyword
			, String levelcondit
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		System.out.println("searchCondition    " + searchCondition);
		System.out.println("searchKeyword    " + searchKeyword);
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	HashMap<String, String> hSearch = new HashMap<String, String>();
//	    	hSearch.put("capno", capno);
	    	hSearch.put("searchCondition", searchCondition);
	    	hSearch.put("searchKeyword", searchKeyword);
	    	hSearch.put("levelcondit", levelcondit);
	    	List<EgovMap> list = noticeService.expnListPopUplist(hSearch);
	    	//EgovMap info = noticeService.getproxyAddSelectOrgCapInfo(orgCode);
	    	StringBuffer item_body = new StringBuffer();
	    	
	    	System.out.println("list.size    " + list.size());
	    	for(EgovMap row:list){
	    		item_body.append("<tr>");
	    		item_body.append(String.format("	<td>%s</td>", row.get("orgnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("levelnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("titlenm")));
	    		item_body.append(String.format("	<td>%s / %s</td>", row.get("empno"), row.get("empnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("isexpn").equals("Y") ? "예외자" : String.format("<input type='checkbox' name='chkproxy' value='%s' />", row.get("empno"))));
	    		item_body.append("</tr>");
	    	}
	    	
	    	isOk = true;
	    	hMap.put("body", item_body.toString());
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
	
	
	// 예외자추가 (insert)
//	@RequestMapping(value="/user/expnInsert.do")
//	public String expnInsert(HttpServletRequest request, 
//								@ModelAttribute("excePtionRulerVO") ExcePtionRulerVO excePtionRulerVO, 
//								ModelMap model) throws Exception{	
		
//		if((int)noticeVO.getSq_no() > 0)
//		{
//			noticeService.NoticeUpdate(noticeVO);
//		}else{
//			noticeService.setExpnInsert(excePtionRulerVO);
//		}
		
		
		//	noticeService.NoticeUpdate(noticeVO);
		//}else {
//				boolean ret = noticeService.setNoitceInsert(noticeVO);
		//	rNoticeVO = noticeService.setNoitceInsert(noticeVO);
	//		noticeVO = rNoticeVO;
			//System.out.println("ret     " + ret);
		//}
//		return "redirect:/user/exceptlist.do";
//	}
	
	/**
	 *  예외자 관리 LIST
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/exceptlist.do")
	public String excePtionRuler(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPT);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		HashMap<String,Object> retMap = noticeService.getExcePtionRulerList(searchVO);
		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalCnt", totalCnt);
		return "user/exceptlist";
	}

	/**
	 *  예외자 Insert
	 * @param request
	 * @param chklist
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/setexpnUserInsert.do")
	public void setexpnUserInsert(HttpServletRequest request
			, String chklist
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
			
	    	HashMap<String,Object> hashMap = new HashMap<String,Object>();
	    	List<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
	    	
	    	for(String empno:CommonUtil.SplitToString(chklist, ",")){
	    		HashMap<String, String> userInfo = new HashMap<String, String>();
	    		userInfo.put("empno", empno);
	    		userList.add(userInfo);
	    	}
	    	hashMap.put("userList", userList);
	    
	    	boolean retVal = noticeService.setexpnUserInsert(hashMap);
	    	
	    	isOk = retVal;
	    	if(retVal){
	    		msg = "";
	    	}else{
	    		msg = "저장중 알수 없는 오류가 발생  하였습니다.";
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
 * 예외자 추가팝업
 * @param request
 * @param response
 * @throws Exception
 */
@RequestMapping(value="/user/getExpnaddPopup.do")
public void getExpnaddPopup(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
    	List<EgovMap> titleList = noticeService.expnListPopUpOrgUserTitleCodelist();
    	
    	
    	popupBody.append(String.format("<div class='ly_block2' style='width:%s;background:#fff;padding:10px 15px 10px 10px;'>", "620px" ));
    	popupBody.append(String.format("<div class='subTT' style='cursor:move;'><span>%s</span></div>","예외자 추가"));
    
    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' /> 예외자 검색</div>");
    	popupBody.append("			<div class='sch_block3'>"
	    			+ "					<li>");
    	popupBody.append("					<p>직책</p>");		
    	popupBody.append("					<select  name='levelcondit' id='levelcondit' >");
    	popupBody.append("						<option value='000'>전체</option>");
    	for(EgovMap row:titleList){
    		popupBody.append(String.format("					<option value='%s'>%s</option>", row.get("titlecode"), row.get("titlenm")));	
    	}
    	popupBody.append("						</select>");
	    popupBody.append("						<p>검색어</p> "
	    			+ "						<select  name='popcondit' id='popcondit' >"
					+ "							<option value='1'>사번</option>"
					+ "							<option value='2' selected>성명</option>"
					+ "						</select>"
	    			+ "						<input type='text' name='popkeywrd' id='popkeywrd' value='' style='width:100px' class='srh'>"
	    			+ "						<a class='btn_black btn_expn_usersearch' style='color:#fff;'><span>검색</span></a>"
	    			+ "					</li>"
	    			+ "				</div>"
	    			+ "				<div class='pd10'></div>"
	    		
					+ "				<div style='width:100%;height:270px;overflow-y:auto;border-bottom:1px solid #9e9e9e;'>"
					+ "						<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
					+ "						<colgroup>"
					+ "						<col style='width:*'>"
					+ "						<col style='width:15%'>"
					+ "						<col style='width:15%'>"
					+ "						<col style='width:20%'>"
					+ "						<col style='width:15%'>"
					+ "						</colgroup>"
					+ "						<tr>"
					+ "							<th>조직</th>"
					+ "							<th>직책</th>"
					+ "							<th>호칭</th>"
					+ "							<th>사번/성명</th>"
					+ "							<th>선택</th>"
					+ "						</tr>"
					+ "						<tbody class='popup_content_body'>"
					+ "						</tbody>"
					+ "						</table>"
					+ "				</div>");

    	popupBody.append("	<div class='btn_black2'><a class='btn_black btn_popup_save'><span style='color:#ffffff'>저장</span></a> <a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a></div>");
    	popupBody.append("</div>");
    	
    	
    	/*
    	popupBody.append("<div id='wrap_pop' style='background: #fff'>");
    	popupBody.append("	<div class='WP_tit' style='cursor:move;'>");
    	popupBody.append("		<ul>");
    	popupBody.append("		<li class='WPT_ic'><img src='/img/ic_stit2.png' alt='아이콘'></li>");
    	popupBody.append("		<li class='WPT_txt'>예외자 추가</li>");
    	popupBody.append("		<li class='WPT_btn'><div class='del2'><a class='btn_dialogbox_close' style='cursor:pointer;'><span class='blind'>닫기</span></a></div></li>");
    	popupBody.append("		</ul>");
    	popupBody.append("	</div>");
    	popupBody.append("	<div class='WP_con'>");
    	popupBody.append("	<div>");
    			
  		popupBody.append("		<div class='WC_part2' style='width:300px'>"
  				+ "					<div class='S_tit2 marT10'>"
    			+ "						<ul>"
    			+ "						<li><img src='/img/dot3.png' alt='타이틀'></li>"
    			+ "						<li class='ST_txt'>예외자 검색</li>"
    			+ "						</ul>"
    			+ "					</div>"
    			+ "					<div class='search1' style='width:490px;'>"
    			+ "						<ul>"
    			+ "						<li><img src='/img/dot1.png'></li>"
    			+ "						<li><span>예외자 검색</span></li>"
    			+ "						<li>"
    			+ "						<select  name='popcondit' id='popcondit' >"
    			+ "							<option value='1'>사번</option>"
    			+ "							<option value='2' selected>이름</option>"
    			+ "						</select>"
    			+ "						</li>"
    			+ "						<li><input type='text' name='popkeywrd' id='popkeywrd' style='width:90px' /></li>"
    			+ "						<li><div class='btn1 btn_proxy_usersearch'><a style='cursor:pointer;'>검색</a></div></li>"
    			+ "						</ul>"
    			+ "					</div>"
    			+ "					<div style='width:530px;height:260px;overflow-y:auto;' class='marT10'>"
    			+ "						<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
    			+ "						<colgroup>"
    			+ "						<col style='width:*'>"
    			+ "						<col style='width:30%'>"
    			+ "						<col style='width:15%'>"
    			+ "						</colgroup>"
    			+ "						<tr>"
    			+ "							<th>조직</th>"
    			+ "							<th>사번/이름</th>"
    			+ "							<th>선택</th>"
    			+ "						</tr>"
    			+ "						<tbody class='popup_content_body'>"
    			+ "						</tbody>"
    			+ "						</table>"
    			+ "					</div>"
    			+ ""
    			+ "				</div>"
    			+ ""
    			+ "");
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
    	script.append("function expn_usersearch(){"
    			+ "			var popcondit=$('#popcondit').val();"
    			+ "			var popkeywrd=$('#popkeywrd').val();"
    			+ "			var levelcondit=$('#levelcondit').val();"
    			+ "			if(levelcondit=='000'){"
    			+ "				if(popkeywrd == ''){"
    			+ "					alert('사번또는 이름을 입력하여주세요.');"
    			+ "					return false;"
    			+ "				}"
    			+ "			}"
    			+ "			$.ajax({"
   				+ "			data: {searchCondition:popcondit, searchKeyword:popkeywrd, levelcondit:levelcondit},"
   				 + "			url: '/pop/getexpnListPopUplist.do',"
   				 + "			type: 'POST',"
   				 + "			dataType: 'json',"
   				 + "			error: function (jqXHR, textStatus, errorThrown) {"
   				+ "					if(jqXHR.status == 401){"
				+ "						alert('인증정보가 만료되었습니다.');"
				+ "						location.href='/';"
				+ "					}else{"
				+ "						alert(textStatus + '\\r\\n' + errorThrown);"
				+ "					}"
   				 + "			},"
   				 + "			success: function (data) {"
   				 + "				if (data.ISOK) {"
   				 + "					$('.popup_content_body').empty().append(data.body);"
   				 + "				}else{alert(data.MSG); }"
   				 + "			}"
   				 + "		});"
    			+ "}");
    	script.append("$('.btn_expn_usersearch').click(function(){"
    			+ "		expn_usersearch();"
   				 + "});");
    	script.append("$('#popkeywrd').keyup(function(e){"
				+ "			if (e.keyCode == '13'){"
				+ "				expn_usersearch();"
				+ "			}"
				+ "		});");
    	script.append("$('#levelcondit').change(function(e){"
    			+ "			var levelcondit=$(this).val();"
    			+ "	 		if(levelcondit != '000'){"
    			+ "				$('#popkeywrd').val('');"
    			+ "				expn_usersearch();"
    			+ "			}else{"
    			+ "				$('.popup_content_body').empty();"
    			+ "			}"
				+ "		});");
    	script.append("$('.btn_popup_save').click(function(){"
    			+ "			var emplist = [];"
    			+ "			$('.popup_content_body').find('tr').each(function(){"
    			+ "				$(this).find('input:checkbox[name=chkproxy]:checked').each(function () {"
    			+ "					emplist.push($(this).val());"
    			+ "				});"
    			+ "			});"
    			+ "			if(emplist.toString() == ''){"
    			+ "				return false;"
    			+ "			}"
    			+ "			if(!confirm('선택한 예외자를 저장 하시겠습니까?')){"
    			+ "				return false;"
    			+ "			}"
    			+ "			var data={chklist:emplist.toString()};"
    			+ "			$.ajax({"
   				 + "			data: data,"
				 + "			url: '/user/setexpnUserInsert.do',"
   				 + "			type: 'POST',"
   				 + "			dataType: 'json',"
   				 + "			error: function (jqXHR, textStatus, errorThrown) {"
   				+ "					if(jqXHR.status == 401){"
				+ "						alert('인증정보가 만료되었습니다.');"
				+ "						location.href='/';"
				+ "					}else{"
				+ "						alert(textStatus + '\\r\\n' + errorThrown);"
				+ "					}"
   				 + "			},"
   				 + "			success: function (data) {"
   				 + "				if (data.ISOK) {"
   				+ "						$('.DialogBox').data('is_save', 'Y');"
   				+ "						$('.DialogBox').dialog('close');"
   				 + "				}else{alert(data.MSG); }"
   				 + "			}"
   				 + "		});"
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
 * 예외자 삭제
 * @param request
 * @param checklist
 * @param response
 * @throws Exception
 */
@RequestMapping(value="/user/setExpnDeleteUser.do")
public void setExpnDeleteUser(HttpServletRequest request, String checklist, HttpServletResponse response) throws Exception {
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
		
    	HashMap<String,Object> hashMap = new HashMap<String,Object>();
    	List<HashMap<String, Object>> empList = new ArrayList<HashMap<String, Object>>();
    	for(String empno:CommonUtil.SplitToString(checklist, ",")){
    		HashMap<String, Object> empInfo = new HashMap<String, Object>();
    		empInfo.put("emp_no", empno);
    		empList.add(empInfo);
    	}
    	hashMap.put("empList", empList);
    	boolean retVal = noticeService.expnDateDelete(hashMap);
    	
    	isOk = retVal;
    	if(retVal){
    		msg = "";
    	}else{
    		msg = "삭제 처리중 알수없는 오류가 발생하였습니다.";
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

/*
// 예외자 삭제관리
@RequestMapping("/service.expnDateDelete.do")
public String expnDateDelete(HttpServletRequest request,
							   @ModelAttribute("ExcePtionRulerVO") ExcePtionRulerVO excePtionRulerVO, 
							   ModelMap model) throws Exception{
	String[] expnno = request.getParameterValues("EXPNNO");
	for( int i = 0 ; i < expnno.length; i++){
		noticeService.expnDateDelete(expnno[i]);
	}
	return "redirect:/user/exceptlist.do";
}
*/		
	/**
	 *  대무자 관리 LIST
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/proxylist.do")
	public String proxyRuler(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.PROXY);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		HashMap<String,Object> retMap = noticeService.getProxyRulerList(searchVO);
		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)retMap.get("list");
	
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalCnt", totalCnt);
		return "user/proxylist";
	}
	
	// 대무자 삭제관리
	/*
	@RequestMapping("/service.proxyDateDelete.do")
	public String proxyDateDelete(HttpServletRequest request,
								   @ModelAttribute("ExcePtionRulerVO") ExcePtionRulerVO excePtionRulerVO, 
								   ModelMap model) throws Exception{
		String[] expnno = request.getParameterValues("EXPNNO");
		for( int i = 0 ; i < expnno.length; i++){
			noticeService.proxyDateDelete(expnno[i]);
		}
		return "redirect:/user/proxylist.do";
	}
	*/
	
	/**
	 * 대무자 추가 팝업
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/getProxyaddPopup.do")
	public void getProxyaddPopup(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	    	
	    	popupBody.append(String.format("<div class='ly_block2' style='width:%s;background:#fff;padding:10px 15px 10px 10px;'>", "870px" ));
	    	popupBody.append(String.format(" 	<div class='subTT' style='cursor:move;'><span>%s</span></div>","부서 담당자 추가"));
	    
	    	popupBody.append("	<div class='ly_left' style='width:300px'>");
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' />조직 및 조직장를 선택하세요.</div>");
	    	popupBody.append("		 <div style='height:450px;border:1px solid #9f9f9f;overflow-y:scroll;overflow-x:scroll;'>"
	    			+ "					<ul id='orgbrowser1' class='filetree chart_list'>");
	    	//조직트리
	    	popupBody.append(CommonUtil.CreateOrgTree(comService));
	    	
	    	popupBody.append("			</ul>"
					+ "				</div>");
	    	popupBody.append("	</div>");
	    
	    	popupBody.append(String.format("<div class='ly_right' style='width:550px'>"));
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' /> 조직장정보</div>");
	    	popupBody.append("			<table border='0' class='TBS4' cellpadding=0 cellspacing=0>"
					+ "					<colgroup>"
					+ "					<col style='width:20%'>"
					+ "					<col style='width:80%'>"
					+ "					</colgroup>"
					+ "					<tr>"
					+ "						<th>선택조직</th>"
					+ "						<td><span class='selorg'></span><input type='hidden' name='orgcode' id='orgcode' /></td>"
					+ "					</tr>"
					+ "					<tr>"
					+ "						<th>조직장명</th>"
					+ "						<td><span class='selcapinto'></span><input type='hidden' name='capno' id='capno' /></td>"
					+ "					</tr>"
					+ "					</table>");
					
			popupBody.append("			<div class='sch_block3'>"
		    			+ "					<li>"
		    			+ "						<p>검색어</p> "
		    			+ "						<select  name='popcondit' id='popcondit' >"
						+ "							<option value='1'>사번</option>"
						+ "							<option value='2' selected>성명</option>"
						+ "						</select>"
		    			+ "						<input type='text' name='popkeywrd' id='popkeywrd' value='' style='width:100px' class='srh'>"
		    			+ "						<a class='btn_black btn_proxy_usersearch' style='color:#fff;'><span>검색</span></a>"
		    			+ "					</li>"
		    			+ "				</div>"
		    			+ "				<div class='popTT'><img src='/img/icon_arw4.jpg' /> 부서담당자 검색</div>"

						+ "				<div style='width:550px;height:270px;overflow-y:auto;border-bottom:1px solid #9e9e9e;'>"
						+ "						<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
						+ "						<colgroup>"
						+ "						<col style='width:*'>"
						+ "						<col style='width:20%'>"
						+ "						<col style='width:20%'>"
						+ "						<col style='width:25%'>"
						+ "						<col style='width:15%'>"
						+ "						</colgroup>"
						+ "						<tr>"
						+ "							<th>조직</th>"
						+ "							<th>직책</th>"
						+ "							<th>호칭</th>"
						+ "							<th>사번/성명</th>"
						+ "							<th>선택</th>"
						+ "						</tr>"
						+ "						<tbody class='popup_content_body'>"
						+ "						</tbody>"
						+ "						</table>"
						+ "					</div>");
	    	popupBody.append("	</div>");
	    	popupBody.append("	<div class='btn_black2'><a class='btn_black btn_popup_save'><span style='color:#ffffff'>저장</span></a> <a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a></div>");
	    	popupBody.append("</div>");
	    	
	    	
	    	
	    	StringBuffer script = new StringBuffer();
	    	script.append("<script type='text/javascript'>");
	    	script.append("$(function () {"
	    			+ "			 $('#orgbrowser1').on('click', '.sel_folder', function(){"
	    			+ "				if(!$(this).hasClass('add_ON')){"
	    			+ "					var _this = $(this);"
	    			+ "					var pNode = $(this).find('ul');"
	    			+ "					var uCode = $(this).attr('uorg_code');"
	    			+ "					$.ajax({"
	    			+ "						url : '/pol/setAddOrgSubfolder.do',"
	    			+ "						data : {uorg_code:uCode},"
	    			+ "						type : 'POST',"
	    			+ "						dataType : 'json',"
	    			+ "						error : function(jqXHR, textStatus, errorThrown) {"
	    			+ "							if(jqXHR.status == 401){"
					+ "								alert('인증정보가 만료되었습니다.');"
					+ "								location.href='/';"
					+ "							}else{"
					+ "								alert(textStatus + '\\r\\n' + errorThrown);"
					+ "							}"
	    			+ "						},"
	    			+ "						success : function(data) {"
	    			+ "							if (data.ISOK) {"
	    			+ "								var node_html = data.node_body;"
	    			+ "								var child_node = $(node_html).appendTo(pNode);"
	    			+ "								$(pNode).treeview({add:child_node});"
	    			+ "								_this.addClass('add_ON');"
	    			+ "							} else {"
	    			+ "								alert(data.MSG);"
	    			+ "							}"
	    			+ "						}"
	    			+ "					});"
	    			+ "				}"
	    			+ "			});"
	    			+ "			$('#orgbrowser1').treeview({"
	    			+ "				collapsed: false,"
	    			+ "				animated: 'fast',"
	    			+ "				unique: true,"
	    			+ "				persist: 'location'"
	    			+ "			});");
	    	script.append("function proxyusersearch(){"
	    			+ "			var capno=$('#capno').val();"
	    			+ "			var popcondit=$('#popcondit').val();"
	    			+ "			var popkeywrd=$('#popkeywrd').val();"
	    			+ "			if($('#orgcode').val() == ''){"
	    			+ "				alert('부서담당자 지정을 위한 조직을 선택하여 주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			if(popkeywrd == ''){"
	    			+ "				alert('사번또는 이름을 입력하여주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			$.ajax({"
	    			+ "			data: {capno:capno,searchCondition:popcondit, searchKeyword:popkeywrd},"
	    			+ "			url: '/user/getproxySearchUserList.do',"
	    			+ "			type: 'POST',"
	    			+ "			dataType: 'json',"
	    			+ "			error: function (jqXHR, textStatus, errorThrown) {"
	    			+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
	    			+ "			},"
	    			+ "			success: function (data) {"
	    			+ "				if (data.ISOK) {"
	    			+ "					$('.popup_content_body').empty().append(data.body);"
	    			+ "				}else{alert(data.MSG); }"
	    			+ "			}"
	    			+ "		});"
	    			+ "	}");
	    	script.append("$('.btn_proxy_usersearch').click(function(){"
	    			+ "			proxyusersearch();"
	    			+ "});");
	    	script.append("$('#popkeywrd').keyup(function(e){"
					+ "			if (e.keyCode == '13'){"
					+ "				proxyusersearch()"
					+ "			}"
					+ "		});");
	    	script.append("$('.btn_popup_save').click(function(){"
	    			+ "			var capno=$('#capno').val();"
	    			+ "			var emplist = [];"
	    			+ "			$('.popup_content_body').find('tr').each(function(){"
	    			+ "				$(this).find('input:checkbox[name=chkproxy]:checked').each(function () {"
	    			+ "					emplist.push($(this).val());"
	    			+ "				});"
	    			+ "			});"
	    			+ "			if(capno == ''){"
	    			+ "				alert('부서담당자 지정을 위한 조직장을 선택하여 주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			if(emplist.toString() == ''){"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			if(!confirm('선택한 사원을 '+ $('.selcapinto').text() +'님의 부서담당자로 지정 하시겠습니까?')){"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			var data={chklist:emplist.toString(), capno:capno};"
	    			+ "			$.ajax({"
	   				 + "			data: data,"
	   				 + "			url: '/user/setproxyUserInsert.do',"
	   				 + "			type: 'POST',"
	   				 + "			dataType: 'json',"
	   				 + "			error: function (jqXHR, textStatus, errorThrown) {"
	   				+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
	   				 + "			},"
	   				 + "			success: function (data) {"
	   				 + "				if (data.ISOK) {"
	   				+ "						$('.DialogBox').data('is_save', 'Y');"
	   				+ "						$('.DialogBox').dialog('close');"
	   				 + "				}else{alert(data.MSG); }"
	   				 + "			}"
	   				 + "		});"
	    			+ "});");
	    	
	    	script.append("$('#orgbrowser1').on('click', '.sel_text', function(){"
	    			+ "			var orgcode = $(this).attr('orgcode');"
	    			+ "			$('#orgbrowser1').find('a').each(function(){"
	    			+ "				if($(this).hasClass('ON')){"
	    			+ "					$(this).removeClass('ON');"
	    			+ "				}"
	    			+ "			});"
	    			+ "			$(this).find('a').addClass('ON');"
	    			+ "			$.ajax({"
	   				 + "			data: {orgCode:orgcode},"
	   				 + "			url: '/user/getproxySelectOrgCapinfo.do',"
	   				 + "			type: 'POST',"
	   				 + "			dataType: 'json',"
	   				 + "			error: function (jqXHR, textStatus, errorThrown) {"
	   				+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
	   				 + "			},"
	   				 + "			success: function (data) {"
	   				 + "				if (data.ISOK) {"
	   				 + "					$('.selorg').text(data.info.orgnm);$('#orgcode').val(data.info.orgcode);"
	   				 + "					$('.selcapinto').text(data.info.empnm);$('#capno').val(data.info.capno);"
	   				 + "				}else{alert(data.MSG); }"
	   				 + "			}"
	   				 + "		});"
	    			+ "		});");

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
	
	@RequestMapping(value="/user/setproxyUserInsert.do")
	public void setproxyUserInsert(HttpServletRequest request
			, String capno
			, String chklist
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
			
	    	HashMap<String,Object> hashMap = new HashMap<String,Object>();
	    	List<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
	    	
	    	for(String empno:CommonUtil.SplitToString(chklist, ",")){
	    		HashMap<String, String> userInfo = new HashMap<String, String>();
	    		userInfo.put("empno", empno);
	    		userInfo.put("capno", capno);
	    		userList.add(userInfo);
	    	}
	    	hashMap.put("userList", userList);
	    
	    	boolean retVal = noticeService.setProxyUserInsert(hashMap);
	    	
	    	isOk = retVal;
	    	if(retVal){
	    		msg = "";
	    	}else{
	    		msg = "저장중 알수 없는 오류가 발생  하였습니다.";
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
	
	@RequestMapping(value="/user/getproxySearchUserList.do")
	public void getproxySearchUserList(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
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
			
	    	HashMap<String, String> hSearch = new HashMap<String, String>();
	    	hSearch.put("capno", capno);
	    	hSearch.put("searchCondition", searchCondition);
	    	hSearch.put("searchKeyword", searchKeyword);
	    	List<EgovMap> list = noticeService.getproxyAddSearchUserList(hSearch);
	    	//EgovMap info = noticeService.getproxyAddSelectOrgCapInfo(orgCode);
	    	StringBuffer item_body = new StringBuffer();
	    	
	    	for(EgovMap row:list){
	    		item_body.append("<tr>");
	    		item_body.append(String.format("	<td>%s</td>", row.get("orgnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("levelnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("titlenm")));
	    		item_body.append(String.format("	<td>%s<br />%s</td>", row.get("empno"), row.get("empnm")));
	    		item_body.append(String.format("	<td>%s</td>", row.get("isproxy").equals("Y") ? "부서담당자" : String.format("<input type='checkbox' name='chkproxy' value='%s' />", row.get("empno"))));
	    		item_body.append("</tr>");
	    	}
	    	
	    	isOk = true;
	    	hMap.put("body", item_body.toString());
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
	
	@RequestMapping(value="/user/getproxySelectOrgCapinfo.do")
	public void getproxySelectOrgCapinfo(HttpServletRequest request, String orgCode, HttpServletResponse response) throws Exception {
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
			
	    	EgovMap info = noticeService.getproxyAddSelectOrgCapInfo(orgCode);
	    	isOk = true;
	    	HashMap<String, String> orgInfo = new HashMap<String, String>();
	    	orgInfo.put("orgcode", info.get("orgcode").toString());
	    	orgInfo.put("capno", info.get("capno").toString());
	    	orgInfo.put("orgnm", info.get("orgnm").toString());
	    	orgInfo.put("empnm", info.get("empnm").toString());
	    	hMap.put("info", orgInfo);
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
	
	@RequestMapping(value="/user/setProxyDeleteUser.do")
	public void setApprAllRegister(HttpServletRequest request, String checklist, HttpServletResponse response) throws Exception {
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
	    	
	    	HashMap<String,Object> hashMap = new HashMap<String,Object>();
	    		    	
	    	hashMap.put("proxyList", CommonUtil.SplitToString(checklist, ","));
	    	
	    	boolean retVal = noticeService.proxyDateDelete(hashMap);
	    	
	    	isOk = retVal;
	    	if(retVal){
	    		msg = "";
	    	}else{
	    		msg = "삭제 처리중 알수없는 오류가 발생하였습니다.";
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
	
	private List<DignosisItemVO> CreateSubList(List<DignosisItemVO> List, String majCode){
		List<DignosisItemVO> list = new ArrayList<DignosisItemVO>();
		for(DignosisItemVO row:List){
			if(row.getDiag_majr_code().equals(majCode)){
				if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
					list.add(row);
				}
			}
		}
		
		return list;
	}
	
	
	
	/**
	 * faq List 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/faqlist.do")
	public String faqlist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.FAQ);
		//searchVO.setPageIndex(2);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		// faq List 조회
		HashMap<String,Object> retMap = noticeService.getFaqList(searchVO);
		List<NoticeVO> list = (List<NoticeVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		
		MemberVO memVO = CommonUtil.getMemberInfo();
		model.addAttribute("btn_is_show", memVO.getRole_code().equals("1") | memVO.getRole_code().equals("2") ? "T" : "F");
		
		return "man/notice/faqlist";
	}
	
	/**
	 *  FAQ 페이지이동
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/faqRegisterPage.do")
	public String faqRegisterPage(HttpServletRequest request, 
								@ModelAttribute("noticeVO")NoticeVO noticeVO, 
								ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.FAQ);
		if((int)noticeVO.getSq_no() > 0){
			//수정
			NoticeVO borderInfo = noticeService.getFaqInfo((int)noticeVO.getSq_no());
			model.addAttribute("borderInfo", borderInfo);
		
		}
		
		return "man/notice/faqmodify";
	}
	/**
	 *  FAQ (insert/update)
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/faqRegister.do")
	public String faqRegister(HttpServletRequest request, 
								@ModelAttribute("noticeVO") NoticeVO noticeVO, 
								ModelMap model) throws Exception{	
		if(!noticeVO.getTitle().equals("") && !noticeVO.getContents().equals("")){
			if((int)noticeVO.getSq_no() > 0)
			{
				noticeService.FaqUpdate(noticeVO);
			}else{
				noticeService.setFaqInsert(noticeVO);
			}
		}
		
		
		
		return "redirect:/man/faqlist.do";
	}
	
	/**
	 *  FAQ 상세보기
	 * @param response
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/faqview.do")
	public String faqView(HttpServletResponse response, 
			HttpServletRequest request, 
			@ModelAttribute("searchVO") SearchVO searchVO, 
			ModelMap model) throws Exception{
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.FAQ);
		
		// 조회수 쿠키이용
		boolean isReadcnt = CommonUtil.setCommunityReadCnt(response, request, searchVO.getSq_no(), "CommunityReadCnt");
		if(isReadcnt)
		{
			noticeService.setCommunityReadCount(searchVO.getSq_no());
		}
		NoticeVO borderInfo = noticeService.getFaqInfo(searchVO.getSqno());
		
		borderInfo.setContents(borderInfo.getContents().replace("\r\n", "<br />"));
		model.addAttribute("borderInfo", borderInfo);
		
		MemberVO memVO = CommonUtil.getMemberInfo();
		model.addAttribute("btn_is_show", memVO.getRole_code().equals("1") | memVO.getRole_code().equals("2") ? "T" : "F");
		
		HashMap<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("board_type", "0001");
		hMap.put("seq_no", searchVO.getSq_no());
		hMap.put("emp_no", memVO.getUserid());
		noticeService.boardReadLogUpdate(hMap);
		
		return "man/notice/faqview";
	}
	
	/**
	 *  FAQ 삭제
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/faqlistDelete.do")
	public String FaqlistDelete(HttpServletRequest request, @ModelAttribute("noticeVO") NoticeVO noticeVO, ModelMap model) throws Exception{
		System.out.println(noticeVO.getSq_no() + "][");
		noticeService.FaqlistDelete(noticeVO);
		
		return "redirect:/man/faqlist.do";
	}
	/**
	 *  사용자 권한등록
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pop/userListPopUplist.do")
	public String userListPopUplist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		
		HashMap<String, Object> retMap = noticeService.userListPopUplist(searchVO);
		List<UserinfoVO> list = (List<UserinfoVO>)retMap.get("list");
		
		model.addAttribute("resultList", list);
		
		return "pop/userListPopUp";
	}
	// 사용자관리 List 조회
//	@RequestMapping(value="/man/userlist.do")
//	public String userList(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
//		
//		CommonUtil.topMenuToString(request, "notice");
//		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.USER);
//		//searchVO.setPageIndex(2);
//		
//		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
//		searchVO.setPageSize(propertiesService.getInt("pageSize"));
//
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
//		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
//		 
//		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
//		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//		
//		// 사용자관리 List 조회
//		HashMap<String,Object> retMap = noticeService.getUserList(searchVO);
//		List<UserinfoVO> list = (List<UserinfoVO>)retMap.get("list");
//		int totalCnt = (int)retMap.get("totalCount");
//		
//		model.addAttribute("resultList", list);
//		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
//		model.addAttribute("totalPage", TotPage);
//		model.addAttribute("currentpage", searchVO.getPageIndex());
//		
//		return "man/users/userlist";
//	}
	/**
	 * 대무자 리스트 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/proxylistexportexcel.do")
	public void proxylistexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") SearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "부서담당자_리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("부서담당자 현황");
		excelVO.setTitle("부서담당자 현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		List<HashMap<String, Object>> list = noticeService.getProxyRulerListForExportExcel(searchVO);
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
		
		ExcelUtil.hssExcelDown(response, excelVO, excellist);
		
	}
	
	/**
	 * 예외자 리스트 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/expnlistexportexcel.do")
	public void expnlistexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") SearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "소명예외자_리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("예외자현황");
		excelVO.setTitle("예외자현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		List<HashMap<String, Object>> list = noticeService.getExpnRulerListForExportExcel(searchVO);
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
		
		ExcelUtil.hssExcelDown(response, excelVO, excellist);
		
	}
	
	
	
	/**
	 *  QnA List 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaList.do")
	public String qnaList(HttpServletResponse response,HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
	
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.QNA);
		//searchVO.setPageIndex(2);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		MemberVO memVO = CommonUtil.getMemberInfo();
		if(!memVO.getRole_code().equals("1")){
			searchVO.setSelectedRowId(memVO.getUserid());
		}
		searchVO.setEmp_no(memVO.getUserid());
		
		HashMap<String,Object> retMap = noticeService.getQnaList(searchVO);
		List<QnaVO> list = (List<QnaVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		
		return "man/notice/qnaList";
	}
	
	/**
	 *  QnA 등록 페이지
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaView.do")
	public String qnaView(HttpServletResponse response,HttpServletRequest request, ModelMap model, QnaVO qnaVO) throws Exception{	
	
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.QNA);
		//searchVO.setPageIndex(2);
		
		MemberVO memInfo = CommonUtil.getMemberInfo();
		QnaVO qnaInfo =noticeService.getQnaInfo((int)qnaVO.getSq_no());
	
		HashMap<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("board_type", "0002");
		hMap.put("seq_no", qnaVO.getSq_no());
		hMap.put("emp_no", memInfo.getUserid());
		noticeService.boardReadLogUpdate(hMap);
		
		model.addAttribute("qnaInfo", qnaInfo);
		model.addAttribute("btn_answer_show", memInfo.getRole_code().equals("1") ? "T" : "F");
		model.addAttribute("btn_is_show", memInfo.getUserid().equals(qnaInfo.getUpd_user()) ? "T" : "F");
		
		
		return "man/notice/qnaView";
	}

	/**
	 *  QnA 등록 페이지
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaModify.do")
	public String qnaModify(HttpServletResponse response,HttpServletRequest request, ModelMap model, QnaVO qnaVO) throws Exception{	
	
		
		
		CommonUtil.topMenuToString(request, "notice", comService);
		CommonUtil.noticeLeftMenuToString(request, LeftMenuInfo.QNA);
		//searchVO.setPageIndex(2);
		
		QnaVO qnaInfo = new QnaVO();
		if((int)qnaVO.getSq_no() > 0){
			//수정
			qnaInfo = noticeService.getQnaInfo((int)qnaVO.getSq_no());
			
		}
		model.addAttribute("qnaInfo", qnaInfo);
		
		
		return "man/notice/qnaModify";
	}
	
	/**
	 *  QnA 등록
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaInfoSave.do")
	public String qnaInfoInsert(HttpServletResponse response,HttpServletRequest request, ModelMap model, QnaVO qnaVO) throws Exception{	
	
		MemberVO memInfo = CommonUtil.getMemberInfo();
		if(!qnaVO.getTitle().equals("") && !qnaVO.getContents().equals("")){
			if((int)qnaVO.getSq_no() > 0)
			{			
				qnaVO.setUpd_user(memInfo.getUserid());
				noticeService.qnaInfoUpdate(qnaVO);
			}else{
				qnaVO.setUpd_user(memInfo.getUserid());
				noticeService.qnaInfoInsert(qnaVO);
				qnaSendMailInfo(qnaVO);
			}
		}
		
		return "redirect:/man/qnaList.do";
	}
	
	
	/**
	 *  QnA 답변등록
	 * @param request
	 * @param qnaVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaAnswerInfoUpdate.do")
	public String qnaAnswerInfoUpdate(HttpServletResponse response,HttpServletRequest request, ModelMap model, QnaVO qnaVO) throws Exception{	
	
		MemberVO memInfo = CommonUtil.getMemberInfo();
		qnaVO.setAnswer_user(memInfo.getUserid());
		noticeService.qnaAnswerInfoUpdate(qnaVO);
		
		return "redirect:/man/qnaList.do";
	}
	

	/**
	 *  QnA 삭제
	 * @param request
	 * @param noticeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/qnaListDelete.do")
	public String qnaListDelete(HttpServletResponse response,HttpServletRequest request, QnaVO qnaVO, ModelMap model) throws Exception{
		System.out.println(qnaVO.getSq_no() + "][");
		noticeService.qnaListDelete(qnaVO);
		
		return "redirect:/man/qnaList.do";
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "성명";break;
		case "org_nm_2" : ColumnName = "부서명";break;
		case "org_nm" : ColumnName = "IP";break;
		case "rgdt_date" : ColumnName = "등록일";break;
		case "title_nm" : ColumnName = "직책";break;
		case "proxy_emp_no" : ColumnName = "부서담당자 사번";break;
		case "proxy_emp_nm" : ColumnName = "부서담당자 성명";break;
		case "proxy_org_nm" : ColumnName = "부서담당자 부서명";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	
	private boolean qnaSendMailInfo(QnaVO qnaVO) throws Exception{
		boolean ret = true;
		StringBuffer mailBody = new StringBuffer();
		MemberVO memInfo = CommonUtil.getMemberInfo();
		HashMap<String, String> codeInfo = new HashMap<String, String>();
		codeInfo.put("majCode", "RPT");
		codeInfo.put("minCode", "AEA");
		
		CodeInfoVO mailTestAddr = comService.getCodeInfo(codeInfo);
		
		String toEmail =  mailTestAddr.getAdd_info1();
		
		mailBody.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		mailBody.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
		mailBody.append("<head>");
		mailBody.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		mailBody.append("<title>Untitled Document</title>");
		mailBody.append("</head>");
        mailBody.append("");
		mailBody.append("<body style='padding:0;margin:0 auto;'>");
		mailBody.append("<table cellpadding='0' cellspacing='0' style='width:700px;background:#efefef;color:#393939;'>");
		mailBody.append("	<tr>");
		mailBody.append("    	<td style='padding:10px;'><img src=\"cid:toplogo\"  alt='KT임직원 보안수준진단' title='KT임직원 보안수준진단' /><span style='float:right;font:bold;font-family:\"돋움\";font-size:12px;padding-top:20px'>Q&A 등록 알림</span></td>");
		mailBody.append("    </tr>");
		mailBody.append("    <tr>");
		mailBody.append("    	<td style='padding:30px 20px;background:#ffffff;font:normal;font-family:\"돋움\";font-size:12px;border-top:solid 2px #5499de;line-height:25px;'>");
		mailBody.append(String.format("    <strong>%s님 안녕하세요.</strong><br/><br/>", "임직원 보안수준진단 담당자"));
		mailBody.append("        ");
		mailBody.append("        [임직원 보안수준진단] Q&A 등록 알림 메일 입니다.<br/>");
		mailBody.append("		아래의 내용 확인 부탁 드립니다.<br/>");
		mailBody.append("        ");
		mailBody.append("        <strong>감사합니다.</strong>   ");
		mailBody.append("        </td>");
		mailBody.append("    </tr>");
		mailBody.append("    <tr>");
		mailBody.append("    	<td style='padding:30px 20px 80px 20px;background:#ffffff;font:normal;font-family:\"돋움\";font-size:12px;border-bottom:solid 1px #cfcfcf;border-collapse:collapse;'>");
		mailBody.append("        	<table cellpadding='0' cellspacing='0' style='width:680px;border:solid 1px #cfcfcf;'>");
		mailBody.append("            	<tr>");
		mailBody.append("                	<td colspan='2' style='color:#ffffff;font:bold;font-family:\"돋움\";font-size:12px;text-align:center;padding:10px;background:#5499de;border-bottom:solid 1px #cfcfcf;'>IP 예외 상세정보</td>");
		mailBody.append("                </tr>");
		mailBody.append("                <tr style=''>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>Q&A 제목</th>");
		mailBody.append(String.format("					<td style='padding:10px;border:solid 1px #cfcfcf;'> %s</td>", qnaVO.getTitle()));
		mailBody.append("                </tr>");
		mailBody.append("                <tr>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>등록자</th>");
		mailBody.append(String.format("     <td style='padding:10px;border:solid 1px #cfcfcf;'>%s</td>", memInfo.getUsername()));
		mailBody.append("                </tr>");
		mailBody.append("            </table>");
		mailBody.append("        </td>");
		mailBody.append("    </tr> ");
		mailBody.append("	<tr>");
		mailBody.append("    	<td style='padding:15px 0px 0px 0px;text-align:center;font:normal 11px '돋움';'>");
		mailBody.append("        *본 메일은 회신할 수 없는 발신전용 메일입니다.");
		mailBody.append("        <p style='color:#b8b8b8;'>Copyright <span style='font:bold 11px '돋움';color:#e71e25'>KT</span> All right reserverd.</p>");
		mailBody.append("        </td>");
		mailBody.append("    </tr>       ");
		mailBody.append("</table>");
		mailBody.append("</body>");
		mailBody.append("</html>");

		List<MimeBodyPartVO> imageList = new ArrayList<MimeBodyPartVO>();
		MimeBodyPartVO logo = new MimeBodyPartVO();
		logo.setImageName("top_logo.png");
		logo.setContentID("<toplogo>");
		logo.setFileName("toplogo.png");
		imageList.add(logo);
		
		//boolean ret = true;
		ret = MailUtil.SendMail(memInfo.getUserid(), "[임직원 보안수준진단] Q&A 등록 알림", mailBody.toString(), toEmail, comService, imageList);
		
		return ret;
		
	}
}
