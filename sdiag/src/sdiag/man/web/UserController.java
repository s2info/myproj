package sdiag.man.web;

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

import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.board.service.NoticeService;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserService;
import sdiag.man.service.UserinfoVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.CommonUtil;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class UserController {

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "UserService")
	private UserService userService;
	
	@Resource(name = "NoticeService")
	private NoticeService noticeService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	/**
	 *  사용자관리
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/userlist.do")
	public String policystatus(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.USER);
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
		HashMap<String,Object> retMap = userService.getManUserList(searchVO);
		List<UserinfoVO> list = (List<UserinfoVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/users/userlist";
	}
	
	
	/**
	 * 관리자(운영자) 추가 - 사번으로저장
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/getManUseraddPopup.do")
	public void getManUseraddPopup(HttpServletRequest request, String mode, String uid, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
			
		String msg = "Error!!";
		Boolean isOk = false; 

		StringBuffer popupBody = new StringBuffer();
		System.out.println(uid + ":uid");
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		UserinfoVO userInfo = new UserinfoVO();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			if(mode.equals("M")){
				userInfo = userService.getManUserInfo(uid);
			}
			
			popupBody.append(String.format("<div class='ly_block2' style='width:%s;background:#fff;padding:10px 15px 10px 10px;'>", mode.equals("A") ? "820px" : "450px" ));
	    	popupBody.append(String.format(" 	<div class='subTT' style='cursor:move;'><span>%s</span></div>",mode.equals("A") ? "사용자 추가" : "사용자 수정"));
	    	if(mode.equals("A")){
	    	popupBody.append("	<div class='ly_left' style='width:420px'>");
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' />추가하고자하는 직원을 검색하세요.</div>");
	    	popupBody.append("			<div class='sch_block4'>"
	    			+ "					<li>"
	    			+ "						<p>검색어</p> "
	    			+ "						<select  name='popcondit' id='popcondit' >"
					+ "							<option value='UP1'>사번</option>"
					+ "							<option value='UP2' selected>성명</option>"
					+ "						</select>"
	    			+ "						<input type='text' name='popkeywrd' id='popkeywrd' value='' style='width:100px' class='srh'>"
	    			+ "						<a class='btn_black btn_man_usersearch' style='color:#fff;'><span>검색</span></a>"
	    			+ "					</li>"
	    			+ "					</div>"
	    			+ "					<div class='pd10'></div>"
	    			+ "");
	    	popupBody.append("		 <div class='ly_list' style='width:100%;height:200px;overflow-y:auto;border-bottom:1px solid #9e9e9e'>"
	    			+ "					<table border='0' class='TBS3 ly_tbl' cellpadding=0 cellspacing=0>"
					+ "						<colgroup>"
					+ "						<col style='width:*'>"
					+ "						<col style='width:30%'>"
					+ "						<col style='width:15%'>"
					+ "						</colgroup>"
					+ "						<tr>"
					+ "							<th>조직</th>"
					+ "							<th>사번/성명</th>"
					+ "							<th>선택</th>"
					+ "						</tr>"
					+ "						<tbody class='popup_content_body'>"
					+ "						</tbody>"
					+ "					</table>"
					+ "				</div>");
	    	popupBody.append("	</div>");
	    	}
	    	popupBody.append(String.format("<div class='ly_right' style='width:%s'>", uid.equals("") ? "380px" : "440px"));
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' /> 사용자 정보</div>");
	    	popupBody.append("		<table border='0' class='TBS4 tblInfoType' cellpadding=0 cellspacing=0>"
					+ "					<colgroup>"
					+ "					<col style='width:30%'>"
					+ "					<col style='width:70%'>"
					+ "					</colgroup>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>ID(사번)</th>"
					+ String.format("		<td><span class='empnotxt'>%s</span><input type='hidden' name='empno' id='empno' value='%s' /></td>", userInfo.getId(), userInfo.getId())
					+ "					</tr>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>성명</th>"
					+ String.format("		<td><span class='empnmtxt'>%s</span><input type='hidden' name='empnm' id='empnm' value='%s' /></td>", userInfo.getEmp_nm(), userInfo.getEmp_nm())
					+ "					</tr>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>권한</th>"
					+ String.format("		<td><select name='userauth' id='userauth' ><option value=''>권한선택</option><option value='1' %s>관리자</option><option value='2' %s>운영자</option></select></td>", userInfo.getUser_auth().equals("1") ? "selected" : "", userInfo.getUser_auth().equals("2") ? "selected" : "")
					+ "					</tr>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>메일 수신여부</th>"
					+ String.format("		<td><input type='radio' name='emailindc' value='Y' %s>예 <input type='radio' name='emailindc' value='N' %s>아니오 </td>", userInfo.getEmail_indc().equals("Y") ? "checked" : "", userInfo.getEmail_indc().equals("N") ? "checked" : "")
					+ "					</tr>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>IP</th>"
					+ String.format("		<td><input type='text' name='uip' id='uip' style='width:200px;' maxlangth='20' value='%s'/></td>", userInfo.getIp())
					+ "					</tr>"
					+ "					<tr style='height:35px;'>"
					+ "						<th>MAC</th>"
					+ String.format("		<td><input type='text' name='mac' id='mac' style='width:200px;' maxlangth='30' value='%s' /></td>", userInfo.getMac())
					+ "					</tr>"
					/*+ "					<tr>"
					+ "						<th>상태</th>"
					+ String.format("		<td><input type='text' name='statindc' id='statindc' style='width:200px;' maxlangth='1' /></td>", userInfo.getStat_indc())
					+ "					</tr>"
					+ "					<tr>"*/
					+ "						<th>사용여부</th>"
					+ String.format("		<td><select name='isused' id='isused' ><option value='Y' %s>사용</option><option value='N' %s>사용중지</option></select></td>", userInfo.getIsused().equals("Y") ? "selected" : "", userInfo.getIsused().equals("N") ? "selected" : "")
					+ "					</tr>"
					+ "					</table><input type='hidden' name='statindc' id='statindc' value='Y' />");
	    	popupBody.append("	</div>");
	    	popupBody.append("	<div class='btn_black2'><a class='btn_black btn_popup_save'><span style='color:#ffffff'>저장</span></a> "
	    			+ "									<a class='btn_black btn_popup_delete'><span style='color:#ffffff'>삭제</span></a> "
	    			+ "									<a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a>"
	    			+ "			</div>");
	    	popupBody.append("</div>");
	    	popupBody.append(String.format("</div><input type='hidden' name='mode' id='mode' value='%s'/>", mode));
			
			
			
			StringBuffer script = new StringBuffer();
			script.append("<script type='text/javascript'>");
			script.append("$(function () {");
			script.append("function popusersearch(){"
					+ "			var capno=$('#capno').val();"
					+ "			var popcondit=$('#popcondit').val();"
					+ "			var popkeywrd=$('#popkeywrd').val();"
					+ "			if(popkeywrd == ''){"
					+ "				alert('사번또는 성명을 입력하여주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			$.ajax({"
					+ "			data: {capno:capno,searchCondition:popcondit, searchKeyword:popkeywrd},"
					+ "			url: '/user/getManSearchUserList.do',"
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
			script.append("$('.btn_man_usersearch').click(function(){"
					+ "			popusersearch()"
					+ "});");
			script.append("$('#popkeywrd').keyup(function(e){"
					+ "			if (e.keyCode == '13'){"
					+ "				popusersearch()"
					+ "			}"
					+ "		});");
			script.append("$('.popup_content_body').on('click', 'input:radio[name=radioUser]', function(){"
					+ "			var empno=$(this).val();"
					+ "			var empnm=$(this).attr('uname');"
	    			+ "			$('.empnotxt').text(empno);$('#empno').val(empno);"
	    			+ "			$('.empnmtxt').text(empnm);$('#empnm').val(empnm);"
	    			+ "		});");
			script.append("$('.btn_popup_delete').click(function(){"
					+ "			if(!confirm('사용자를 삭제 하시겠습니까?')){"
					+ "				return false;"
					+ "			}"
					+ "			var data={stat:$('#mode').val(),id:$('#empno').val()};"
					+ "			$.ajax({"
					+ "			data: data,"
					+ "			url: '/user/setManUserDelete.do',"
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
			script.append("$('.btn_popup_save').click(function(){"
					+ "			if($('#empno').val() == ''){"
					+ "				alert('사용자추가를 위한 직원을 검색 및 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if($('#userauth').val() == ''){"
					+ "				alert('사용자 권한을 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if($('#uip').val() != ''){"
					+ "				if(!$('#uip').val().isNumber()){"
					+ "					alert('IP는 숫자로 입력하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "			}"
					+ "			var emailindc = $('input:radio[name=emailindc]:checked').val();"
					+ "			if(!confirm('사용자를 저장 하시겠습니까?')){"
					+ "				return false;"
					+ "			}"
					+ "			var data={stat:$('#mode').val(),id:$('#empno').val(), user_auth:$('#userauth').val(), email_indc:emailindc, ip:$('#uip').val(), mac:$('#mac').val(), stat_indc:$('#statindc').val(), isused:$('#isused').val()};"
					+ "			$.ajax({"
					+ "			data: data,"
					+ "			url: '/user/setManUserInsert.do',"
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

	@RequestMapping(value="/user/setManUserInsert.do")
	public void setManUserInsert(HttpServletRequest request
			,  @ModelAttribute("userinfoVO") UserinfoVO  userinfoVO 
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "저장중 알수 없는 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	int ret = userService.setManUserInsert(userinfoVO);
		    if(ret == 1){
		    	msg = "동인한 ID(사번)이 존재 합니다.";
		    }else if(ret == 0){
		    	isOk = true;
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
	
	@RequestMapping(value="/user/setManUserDelete.do")
	public void setManUserDelete(HttpServletRequest request
			,  @ModelAttribute("userinfoVO") UserinfoVO  userinfoVO 
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "삭제중 알수 없는 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	int ret = userService.setManUserDelete(userinfoVO);
		    if(ret == 1){
		    	isOk = true;
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

	@RequestMapping(value="/user/getManSearchUserList.do")
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
	    	SearchVO searchVO = new SearchVO();
	    	searchVO.setSearchCondition(searchCondition);
	    	searchVO.setSearchKeyword(searchKeyword);
	    	List<UserinfoVO> list = userService.getManAddUserSearchUserList(searchVO);
	    	
	    	StringBuffer item_body = new StringBuffer();
	    	
	    	for(UserinfoVO row:list){
	    		item_body.append("<tr>");
	    		item_body.append(String.format("	<td class='cell2'>%s</td>", row.getOrg_nm()));
	    		item_body.append(String.format("	<td class='cell1'>%s <br /> %s</td>", row.getEmp_no(), row.getEmp_nm()));
	    		item_body.append(String.format("	<td class='cell3 ck_button'>%s</td>", String.format("<label><input type='radio' name='radioUser' value='%s' uname='%s' /><span></span></label>", row.getEmp_no(), row.getEmp_nm())));
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
	/**
	 * 관리자정보 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/userlistexportexcel.do")
	public void userlistexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") SearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "관리권한사용자_리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("관리자현황");
		excelVO.setTitle("관리자현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		List<HashMap<String, Object>> list = userService.getManUserListForExportExcel(searchVO);
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
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "id" : ColumnName = "아이디";break;
		case "user_auth" : ColumnName = "사용권한";break;
		case "email" : ColumnName = "이메일";break;
		case "email_indc" : ColumnName = "이메일수신여부";break;
		case "emp_indc" : ColumnName = "사번사용여부";break;
		case "emp_nm" : ColumnName = "성명";break;
		case "mac" : ColumnName = "MAC";break;
		case "ip" : ColumnName = "IP";break;
		case "rgdt_date" : ColumnName = "등록일";break;
		case "emp_no" : ColumnName = "사번";break;
		case "posn_nm" : ColumnName = "소속조직";break;
		case "isused" : ColumnName = "사용여부";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	/**
	 * Block User List
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/blockuserlist.do")
	public String blockuserlist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.BLOCK);
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
		HashMap<String,Object> retMap = userService.getManBlockuserList(searchVO);
		List<HashMap<String, String>> list = (List<HashMap<String, String>>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/block/blockuserlist";
	}
	/**
	 * Block User Delete
	 * @param request
	 * @param uid
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/setBlockuserDelete.do")
	public void setBlockuserDelete(HttpServletRequest request
			, String uid  
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "삭제중 알수 없는 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	int ret = userService.setManBlockUserDelete(uid);
		    if(ret >= 1){
		    	isOk = true;
		    }
	    		
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
	 * Block User Delete
	 * @param request
	 * @param uid
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/user/setBlockuserDeleteAll.do")
	public void setBlockuserDeleteAll(HttpServletRequest request
			, String uidlist  
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "삭제중 알수 없는 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
			List<String> slist = CommonUtil.SplitToString(uidlist, ",");
    		for(String str:slist){
    			userService.setManBlockUserDelete(str);
    		}
	    	
		    isOk = true;
		    	
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

}
