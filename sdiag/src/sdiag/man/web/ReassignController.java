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

import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.board.service.NoticeService;
import sdiag.man.service.ReassignInfoVO;
import sdiag.man.service.ReassignSearchVO;
import sdiag.man.service.ReassignService;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserService;
import sdiag.man.service.UserinfoVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.CommonUtil;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class ReassignController {

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name= "commonService")
	private CommonService comService;	
	
	
	@Resource(name= "ReassignService")
	private ReassignService reassignService;	
	
	/**
	 *  유형별 담당자 재정의 리스트
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/typeReassignList.do")
	public String typeReassignList(HttpServletRequest request, @ModelAttribute("searchVO") ReassignSearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.REASSIGN);
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
			HashMap<String,Object> retMap = reassignService.getTypeReassignList(searchVO);
			List<ReassignInfoVO> reassignList = (List<ReassignInfoVO>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", reassignList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return "man/reassign/typeReassignList";
	}
	
	
	/**
	 *  개일벌 담당자 재정의 리스트
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/psReassignList.do")
	public String psReassignList(HttpServletRequest request, @ModelAttribute("searchVO") ReassignSearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.REASSIGN);
		//searchVO.setPageIndex(2);
		
		
		try
		{
			//협력사 리스트
			List<ReassignInfoVO> resultList = new ArrayList<ReassignInfoVO>();
			
			//협력사 유형리스트
			List<CodeInfoVO> cTypeList = new ArrayList<CodeInfoVO>();
			cTypeList = comService.getCodeInfoList("KPC");
			
			if(searchVO.getFormMod().equals("S")){
				resultList = reassignService.getKpSerachList(searchVO);
			}
			
			model.addAttribute("resultList", resultList);
					model.addAttribute("cTypeList", cTypeList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/reassign/psReassignList";
	}
	
	
	/**
	 * 담당자 정보 조회
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getSearchPopup.do")
	public void getProxyaddPopup(HttpServletRequest request, HttpServletResponse response, ReassignSearchVO searchVO) throws Exception {
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
	    	popupBody.append(String.format(" 	<div class='subTT' style='cursor:move;'><span>%s</span></div>","담당자 조회"));
	    	popupBody.append("<div class='ly_left'  style='width:100%;'>");
	        popupBody.append("<div class='popTT'><img src='/img/icon_arw4.jpg' /> 담당자 검색</div>");
	        popupBody.append("<!-- S :search --> ");
	        popupBody.append("<div class='sch_block4'>");
	        popupBody.append("    <li>   ");
	        popupBody.append("    <p>검색어</p> ");
	        popupBody.append("        <select  name='popcondit' id='popcondit' style='width:100px'>   ");
	        popupBody.append("            <option value='1'>사번</option>  ");
	        popupBody.append("            <option value='2' selected>성명</option> ");
	        popupBody.append("        </select>                            ");
	        popupBody.append("        <input name='popkeywrd' id='popkeywrd' value='' type='text' style='width:200px' class='srh'>   ");
	        popupBody.append("        <a class='btn_black btn_usersearch' style='color:#fff;'><span>검색</span></a>");
	        popupBody.append("    </li> ");
	        popupBody.append("</div>  ");
	        popupBody.append("<!-- E :search --> ");
	        popupBody.append("<div class='ly_list'>  ");
	        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl'>    ");
	        popupBody.append("        <colgroup>         ");
	        popupBody.append("            <col style='width:15%;' />  ");
	        popupBody.append("            <col style='width:15%;' /> ");
	        popupBody.append("            <col style='width:70%;' /> ");
	        popupBody.append("        </colgroup>  ");                  
	        popupBody.append("        <tr>     ");
	        popupBody.append("            <th>서명</th> ");
	        popupBody.append("            <th>사번</th> ");
	        popupBody.append("            <th>조직</th>");
	        popupBody.append("        </tr> "
	        		+  "			  <tbody class='popup_content_body'>"
					+ "				  </tbody>"
					+ "			  </table>"
					+ "		  </div>");
			popupBody.append("</div>");
			popupBody.append("<div class='btn_black2'> <a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a></div>");
			popupBody.append("</div>");

	    	StringBuffer script = new StringBuffer();
	    	script.append("<script type='text/javascript'>");
	    	script.append("$(function () {"
	    			+ "			function userSearch(){"
	    			+ "			var popcondit=$('#popcondit').val();"
	    			+ "			var popkeywrd=$('#popkeywrd').val();"
	    			+ "			if(popkeywrd == ''){"
	    			+ "				alert('사번또는 이름을 입력하여주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			$.ajax({"
	    			+ "			data: {searchCondition:popcondit, searchKeyword:popkeywrd, type:''},"
	    			+ "			url: '/man/getSearchUserList.do',"
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
	    			//+ "alert(data.body);"
	    			+ "					$('.popup_content_body').empty().append(data.body);"
	    			+ "				}else{alert(data.MSG); }"
	    			+ "			}"
	    			+ "		});"
	    			+ "	}");
	    	script.append("$('.btn_usersearch').click(function(){"
	    			+ "			userSearch();"
	    			+ "});");
	    	script.append("$('#popkeywrd').keyup(function(e){"
					+ "			if (e.keyCode == '13'){"
					+ "				userSearch();"
					+ "			}"
					+ "		});");
	    	script.append("$.empInfo = function(empNo, empNm, type){"
	    			//+ "			alert($('#formMod').val());"
	    			+ "			if($('#formMod').val() == '1'){"
	    			+ "				$('#kt_searchKeyword').val(empNo);"
	    			//+ "alert($('#kt_searchKeyword').val());"
	    			+ "			}else if($('#formMod').val() == '3'){"
	    			+ "				$('#re_searchKeyword').val(empNo);"
	    			+ "         }else{"
	    			+ "				$('#re_empNo').val(empNo);"
	    			+ "				$('#re_empNm').val(empNm);"
	    			//+ "alert($('#rs_empNo').val());"
	    			+ "			}"
	    			//+ "			alert($('#kt_searchKeyword').val());"
	    			+ "			$('.DialogBox').dialog('close');"	
	    			+ "		};");
	    	
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
	 * 담당자 리스트 조회
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getSearchUserList.do")
	public void getproxySearchUserList(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, String type
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
	    	hSearch.put("searchCondition", searchCondition);
	    	hSearch.put("searchKeyword", searchKeyword);
	    	List<EgovMap> list = reassignService.getSearchUserList(hSearch);
	    	//EgovMap info = noticeService.getproxyAddSelectOrgCapInfo(orgCode);
	    	StringBuffer item_body = new StringBuffer();
	    	
	    	int rowCnt =0;
	    	for(EgovMap row:list){
	    		
	    		
	    		item_body.append(String.format("<tr onclick=\"$.empInfo('%s','%s','%s');\" style='cursor:pointer;' >", row.get("empno"), row.get("empnm"), type ));
	    		item_body.append(String.format("	<td class='%s'>%s</td>", rowCnt%2 == 0 ? "cell1":"cell2" ,row.get("empno")));
	    		item_body.append(String.format("	<td class='%s'>%s</td>", rowCnt%2 == 0 ? "cell1":"cell2" , row.get("empnm")));
	    		item_body.append(String.format("	<td class='%s'>%s</td>", rowCnt%2 == 0 ? "cell1":"cell2" , row.get("posnm")));
	    		item_body.append("</tr>");
	    		
	    		rowCnt++;
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
	 * 개인별 담당자 재정의 정보 저장
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	@RequestMapping(value="/man/setProxyEmpNoSave.do")
	public void setProxyEmpNoSave(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, HttpServletResponse response
			, ReassignInfoVO reassignInfoVO) throws Exception {
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
			
			
			String kpEmpNoList = reassignInfoVO.getKpEmpNoList();
			for(String kpEmpNo:kpEmpNoList.split(",")){
				ReassignInfoVO reassignInfo = new ReassignInfoVO();
				
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				reassignInfo.setUpdtEmpNo(loginInfo.getUserid());
				
				reassignInfo.setKpEmpNo(kpEmpNo);
				reassignInfo.setReEmpNo(reassignInfoVO.getReEmpNo());
				
				reassignService.setProxyEmpNoSave(reassignInfo);
			}
	    	
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
	 * 개인별 담당자 정보 초기화
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setProxyEmpNoDelete.do")
	public void setProxyEmpNoDelete(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, HttpServletResponse response
			, ReassignInfoVO reassignInfoVO) throws Exception {
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
			
			
			String kpEmpNoList = reassignInfoVO.getKpEmpNoList();
			for(String kpEmpNo:kpEmpNoList.split(",")){
				ReassignInfoVO reassignInfo = new ReassignInfoVO();
				
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				reassignInfo.setUpdtEmpNo(loginInfo.getUserid());
				
				reassignInfo.setKpEmpNo(kpEmpNo);
				
				reassignService.setProxyEmpNoDelete(reassignInfo);
			}
	    	
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
	 * 유형별 담당자 재정의 화면
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getTypeReassignForm.do")
	public void getTypeReassignForm(HttpServletRequest request, HttpServletResponse response, ReassignSearchVO searchVO, ReassignInfoVO reassignInfoVO) throws Exception {
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
	    	
	    	popupBody.append(String.format("<div class='ly_block5' style='height:auto;'>", "980px" ));
	    	popupBody.append(String.format(" 	<div class='subTT' style='cursor:move;'><span>%s</span></div>","유형별 담당자 재 지정"));
	    	popupBody.append("<div class='ly_content'  style='width:100%;'>");
	        popupBody.append("<!-- S :search --> ");
	       
	        if(searchVO.getFormMod().equals("U")){
	        popupBody.append("<div class='sch_block4'>");
	        popupBody.append("    <li>   ");
	        popupBody.append("    <p style='margin-left: 5px;'> 재 지정 담당자 검색</p> ");
	        popupBody.append("        <select  name='popcondit2' id='popcondit2' style='width:100px'>   ");
	        popupBody.append("  <option value='4' %s>재 지정 담당자 명</option> ");
	        popupBody.append("                <option value='3' >재 지정 담당자 사번</option> ");
	        popupBody.append("        </select>                            ");
	        popupBody.append("        <input name='popkeywrd2' id='popkeywrd2' value='' type='text' style='width:200px' class='srh'>   ");
	        popupBody.append("        <a class='btn_black btn_usersearch2' style='color:#fff;'><span>검색</span></a>");
	        popupBody.append("    </li> ");
	        popupBody.append("</div>  ");
	        popupBody.append("<!-- E :search --> ");
	        popupBody.append("<div class='ly_list'>  ");
	        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl'>    ");
	        popupBody.append("        <colgroup>         ");
	        popupBody.append("            <col style='width:15%;' />  ");
	        popupBody.append("            <col style='width:15%;' /> ");
	        popupBody.append("            <col style='width:70%;' /> ");
	        popupBody.append("        </colgroup>  ");                  
	        popupBody.append("        <tr>     ");
	        popupBody.append("            <th>서명</th> ");
	        popupBody.append("            <th>사번</th> ");
	        popupBody.append("            <th>조직</th>");
	        popupBody.append("        </tr> "
	        		+  "			  <tbody class='popup_content_body_right'>"
					+ "				  </tbody>"
					+ "			  </table>"
					+ "		  </div>");
	       
	        }else{
	        	        	
	        popupBody.append("	<div class='ly_left2'>");
	        popupBody.append("    <!-- S :search --> ");
	        popupBody.append("    <div class='sch_block4'>  ");
	        popupBody.append("        <li>          ");
	        popupBody.append("        <p style='margin-left: 5px;'>KT 담당자 검색</p>    ");
	        popupBody.append("            <select  name='popcondit1' id='popcondit1' style='width:150px'>    ");
	        popupBody.append("                <option value='2' selected>KT 담당자 명</option>"); 
	        popupBody.append("                <option  value='1'>KT 담당자  사번</option>  ");
	        popupBody.append("            </select>                        ");
	        popupBody.append("            <input type='text' style='width:100px' class='srh'  name='popkeywrd1' id='popkeywrd1' > ");
	        popupBody.append("            <a class='btn_black btn_usersearch1' style='color:#fff;'><span style='padding-left: 10px;'>검색</span></a> ");
	        popupBody.append("        </li>  ");
	        popupBody.append("    </div>");
	        popupBody.append("    <!-- E :search -->  "); 
	        popupBody.append("	<div style='height: 300px; overflow: auto;'>");
	        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl1' style='margin:10px 0;'>   ");
	        popupBody.append("        <colgroup>     ");
	        popupBody.append("            <col style='width:15%;' />    ");
	        popupBody.append("            <col style='width:15%;' />  ");
	        popupBody.append("            <col style='width:70%;' />  ");
	        popupBody.append("        </colgroup>   ");                 
	        popupBody.append("        <tr>    ");
	        popupBody.append("            <th>성명</th> ");
	        popupBody.append("            <th>사번</th>  ");
	        popupBody.append("            <th>조직명</th> ");
	        popupBody.append("        </tr> "
	        		+  "			  <tbody class='popup_content_body_left'>"
					+ "				  </tbody>"
					+ "			  </table>"
					+ "			</div>"	
					+ "		  </div>");
	        popupBody.append("	<div class='ly_right2'>");
	        popupBody.append("    <!-- S :search --> ");
	        popupBody.append("    <div class='sch_block4'>  ");
	        popupBody.append("        <li>          ");
	        popupBody.append("        <p style='margin-left: 5px;'> 재 지정 담당자 검색</p>    ");
	        popupBody.append("            <select  name='popcondit2' id='popcondit2' style='width:150px'>    ");
	        popupBody.append("  				<option value='4' selected>재 지정 담당자 명</option> ");
	        popupBody.append("                <option value='3' >재 지정 담당자 사번</option> ");
	        popupBody.append("            </select>                        ");
	        popupBody.append("            <input type='text' style='width:100px' class='srh'  name='popkeywrd2' id='popkeywrd2' > ");
	        popupBody.append("            <a class='btn_black btn_usersearch2' style='color:#fff;'><span style='padding-left: 10px;'>검색</span></a> ");
	        popupBody.append("        </li>  ");
	        popupBody.append("    </div>");
	        popupBody.append("    <!-- E :search -->  ");   
	        popupBody.append("	<div style='height: 300px; overflow: auto;'>");
	        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl1' style='margin:10px 0;'>   ");
	        popupBody.append("        <colgroup>     ");
	        popupBody.append("            <col style='width:15%;' />    ");
	        popupBody.append("            <col style='width:15%;' />  ");
	        popupBody.append("            <col style='width:70%;' />  ");
	        popupBody.append("        </colgroup>   ");                 
	        popupBody.append("        <tr>    ");
	        popupBody.append("            <th>성명</th> ");
	        popupBody.append("            <th>사번</th>  ");
	        popupBody.append("            <th>조직명</th> ");
	        popupBody.append("        </tr> "
	        		+  "			  <tbody class='popup_content_body_right'>"
					+ "				  </tbody>"
					+ "			  </table>"
					+ "			</div>"
					+ "		  </div>");
    		/*	popupBody.append("<div class='ly_left2'>");
    			popupBody.append("	<div class='sch_block4'>");
    			popupBody.append("    <li>   ");
		        popupBody.append("    <p>검색어</p> ");
		        popupBody.append("        <select  name='popcondit' id='popcondit' style='width:120px'>   ");
	        	popupBody.append("            <option value='2' selected>KT 담당자 명</option>  ");
	        	popupBody.append("            <option value='1'>KT 담당자  사번</option> ");
		        popupBody.append("        </select>                            ");
		        popupBody.append("        <input name='popkeywrd' id='popkeywrd' value='' type='text' style='width:120px' class='srh'>   ");
		        popupBody.append("        <a class='btn_black btn_usersearch' style='color:#fff;'><span>검색</span></a>");
		        popupBody.append("    </li> ");
		        popupBody.append("	</div>  ");
		        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl1'>    ");
		        popupBody.append("        <colgroup>         ");
		        popupBody.append("            <col style='width:20%;' />  ");
		        popupBody.append("            <col style='width:20%;' /> ");
		        popupBody.append("            <col style='width:60%;' /> ");
		        popupBody.append("        </colgroup>  ");                  
		        popupBody.append("        <tr>     ");
		        popupBody.append("            <th>서명</th> ");
		        popupBody.append("            <th>사번</th> ");
		        popupBody.append("            <th>조직</th>");
		        popupBody.append("        </tr> "
		        		+  "			  <tbody class='popup_content_body_left'>"
						+ "				  </tbody>"
						+ "			  </table>"
						+ "		  </div>");
		        popupBody.append("<div class='ly_right2'>");
		        popupBody.append("	<div class='sch_block4'>");
    			popupBody.append("    <li>   ");
		        popupBody.append("    <p>검색어</p> ");
		        popupBody.append("        <select  name='popcondit' id='popcondit' style='width:100px'>   ");
		        popupBody.append("  <option value='4' selected>재 저의 담당자 명</option> ");
		        popupBody.append("                <option value='3' >재 지정 담당자 사번</option> ");
		        popupBody.append("        </select>                            ");
		        popupBody.append("        <input name='popkeywrd' id='popkeywrd' value='' type='text' style='width:120px' class='srh'>   ");
		        popupBody.append("        <a class='btn_black btn_usersearch' style='color:#fff;'><span>검색</span></a>");
		        popupBody.append("    </li> ");
		        popupBody.append("	</div>  ");
		        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl1'>    ");
		        popupBody.append("        <colgroup>         ");
		        popupBody.append("            <col style='width:20%;' />  ");
		        popupBody.append("            <col style='width:20%;' /> ");
		        popupBody.append("            <col style='width:60%;' /> ");
		        popupBody.append("        </colgroup>  ");                  
		        popupBody.append("        <tr>     ");
		        popupBody.append("            <th>서명</th> ");
		        popupBody.append("            <th>사번</th> ");
		        popupBody.append("            <th>조직</th>");
		        popupBody.append("        </tr> "
		        		+  "			  <tbody class='popup_content_body_left'>"
						+ "				  </tbody>"
						+ "			  </table>"
						+ "		  </div>");*/
    		        
	        }
	        
			//협력사 유형리스트
			List<CodeInfoVO> cTypeList = new ArrayList<CodeInfoVO>();
			cTypeList = comService.getCodeInfoList("KPC");
			
			
			popupBody.append("<div'>  ");
	        popupBody.append("    <table cellpadding='0' cellspacing='0' class='ly_tbl2'>    ");
	        popupBody.append("        <colgroup>         ");
	        popupBody.append("            <col style='width:20%;' />  ");
	        popupBody.append("            <col style='width:20%;' /> ");
	        popupBody.append("            <col style='width:20%;' /> ");
	        popupBody.append("            <col style='width:20%;' /> ");
	        popupBody.append("            <col style='width:20%;' /> ");
	        popupBody.append("        </colgroup>  ");
	        popupBody.append("        <tr>");
        	popupBody.append("        <th colspan='2'>KT 담당자 정보</th>");
            popupBody.append("        <th>협력사 유형 정보</th>");
            popupBody.append("        <th colspan='2'>재 지정 담당자 정보</th>");
            popupBody.append("        </tr> ");
	        popupBody.append("        <tr>     ");
	        popupBody.append("            <td class='cell1'>KT담당자 성명</td> ");
	        popupBody.append("            <td class='cell1'>KT당당자 사번</td> ");
	        popupBody.append("            <td class='cell1'>협력사 유형</td>");
	        popupBody.append("            <td class='cell1'>재 지정 담당자 성명</td>");
	        popupBody.append("            <td class='cell1'>재 지정 담당자 사번</td>");
	        popupBody.append("        </tr> ");
	        popupBody.append("        <tr>     ");
	        ReassignInfoVO reassignInfo = new ReassignInfoVO();
	        if(searchVO.getFormMod().equals("U")){
	        	reassignInfo = reassignService.getReassignInfo(reassignInfoVO);
	        }
	        popupBody.append(String.format("       <td id='ktEmpNm' >%s</td>", searchVO.getFormMod().equals("U") && reassignInfo !=null?reassignInfo.getKtEmpNm() :""));
    		popupBody.append(String.format("       <td id='ktEmpNo'>%s</td>" ,searchVO.getFormMod().equals("U") && reassignInfo !=null?reassignInfo.getKtEmpNo() :""));
			popupBody.append("       <td>");
			popupBody.append("           <select  name='cType' id='cType' style='width:100%'>");
			if(searchVO.getFormMod().equals("I")){
				popupBody.append("<option value='' selected>선택하세요</option>");
			}
			for(CodeInfoVO row:cTypeList){
				popupBody.append(String.format("<option value='%s' %s>%s</option>", row.getMinr_code(),
						          searchVO.getFormMod().equals("U") && reassignInfo !=null? reassignInfo.getcType().equals(row.getMinr_code())? "selected":"disabled":""
						        	  , row.getCode_desc()));
			}
			popupBody.append(String.format("       <td id='reEmpNm'>%s</td>",searchVO.getFormMod().equals("U") && reassignInfo !=null?reassignInfo.getReEmpNm() :""));
    		popupBody.append(String.format("       <td id='reEmpNo'>%s</td>",searchVO.getFormMod().equals("U") && reassignInfo !=null?reassignInfo.getReEmpNo() :""));
    		popupBody.append("        </tr> ");
			popupBody.append("			  </table>"
					+ "		  </div>");
			
			popupBody.append("<div class='btn_black2'> <a class='btn_black btn_dialogbox_save'><span style='color:#ffffff'>저장</span></a>"
					+ " <a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a></div>");
			popupBody.append("</div>");

	    	StringBuffer script = new StringBuffer();
	    	script.append("<script type='text/javascript'>");
	    	script.append("$(function () {");
	    	script.append("$('.btn_usersearch1').click(function(){"
	    			+ "			var popcondit=$('#popcondit1').val();"
	    			+ "			var popkeywrd=$('#popkeywrd1').val();"
	    			+ "         var type = '1';"
	    			+ "			if(popkeywrd == ''){"
	    			+ "				alert('사번또는 이름을 입력하여주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			$.ajax({"
	    			+ "			data: {searchCondition:popcondit, searchKeyword:popkeywrd, type:type},"
	    			+ "			url: '/man/getSearchUserList.do',"
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
	    			//+ "alert(data.body);"
	    			+ "					$('.popup_content_body_left').empty().append(data.body);"
	    			+ "				}else{alert(data.MSG); }"
	    			+ "			}"
	    			+ "		});"
	    			+ "});");
	    	script.append("$('.btn_usersearch2').click(function(){"
	    			+ "			var popcondit=$('#popcondit2').val();"
	    			+ "			var popkeywrd=$('#popkeywrd2').val();"
	    			+ "			var type = '2';"
	    			+ "			if(popkeywrd == ''){"
	    			+ "				alert('사번또는 이름을 입력하여주세요.');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			$.ajax({"
	    			+ "			data: {searchCondition:popcondit, searchKeyword:popkeywrd, type:type},"
	    			+ "			url: '/man/getSearchUserList.do',"
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
	    			//+ "alert(data.body);"
	    			+ "					$('.popup_content_body_right').empty().append(data.body);"
	    			+ "				}else{alert(data.MSG); }"
	    			+ "			}"
	    			+ "		});"
	    			+ "});");
	    	script.append("$('#popkeywrd').keyup(function(e){"
					+ "			if (e.keyCode == '13'){"
					+ "				userSearch();"
					+ "			}"
					+ "		});");
	    	script.append("$.empInfo = function(empNo, empNm, type){"
	    			+ "			if(type == '1'){"
	    			+ "				$('#ktEmpNo').text(empNo);"
	    			+ "				$('#ktEmpNm').text(empNm);"
	    			+ "			}else{"
	    			+ "				if($('#ktEmpNo').text() == empNo){"
	    			+ "					alert('KT담당자와 동일한 사원 정보 입니다. 다시 한번 확인해주세요.');"
	    			+ "					return false;"
	    			+ "				}else{ "
	    			+ "					$('#reEmpNo').text(empNo);"
	    			+ "					$('#reEmpNm').text(empNm);"
	    			+ "				}"
	    			+ "			}"
	    			+ "		};");
	    	
	    	script.append("$('.btn_dialogbox_save').click(function(){"
	    			+ "			var ktEmpNo=$('#ktEmpNo').text();"
	    			+ "			var reEmpNo=$('#reEmpNo').text();"
	    			+ "			var cType=$('#cType').val();"
	    			
					+ "			if(ktEmpNo == '' && $('#formMod').val()=='I'){"
	    	    	+ "				alert('KT 담당자를 선택하세요');"
	    	    	+ "				return false;"
	    	    	+ "			}"
	    			+ "			if(cType == ''){"
	    			+ "				alert('협력사 유형을 선택하세요');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			if(reEmpNo == ''){"
	    			+ "				alert('재 지정 담당자를 선택하세요');"
	    			+ "				return false;"
	    			+ "			}"
	    			+ "			var url ='';"
	    			+ "			if($('#formMod').val() == 'U'){"
	    			+ "				url = '/man/setTypeReassignUpdate.do';"
	    			+ "			} else{"
	    			+ "				url = '/man/setTypeReassignInsert.do';"
	    			+ "			}"
	    			//+ "			alert(url);"
	    						
	    			+ "			$.ajax({"
	    			+ "			data: {ktEmpNo:ktEmpNo, reEmpNo:reEmpNo,cType:cType },"
	    			+ "			url:  url,"
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
	    			+ "					alert('정상적으로 저장되었습니다.');"
	    			+ "					$('.DialogBox').dialog('close');"
	    			+ "$('#formMod').val('S');"
						
+ "document.listForm.action = '/man/typeReassignList.do';"
	    			+ "document.listForm.submit();"
	    			
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
	 * 유형별 담당자 재정의 정보 저장
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setTypeReassignInsert.do")
	public void setTypeReassignInsert(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, HttpServletResponse response
			, ReassignInfoVO reassignInfoVO) throws Exception {
		Gson gson = new Gson();
		
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			int cnt =0;
			ReassignInfoVO reassignInfo = new ReassignInfoVO();
			reassignInfo.setKpEmpNo(reassignInfoVO.getKtEmpNo());
			reassignInfo.setcType(reassignInfoVO.getcType());
			
			cnt = reassignService.getTypeReassignInfoCount(reassignInfo);
			
			
			if(cnt>0){
				msg = "이미 등록된 정보 입니다. \n 화인 후 등록하세요.";
				isOk =false;
			}
			else{
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				reassignInfoVO.setRgdtEmpNo(loginInfo.getUserid());
				reassignInfoVO.setUpdtEmpNo(loginInfo.getUserid());
				
				
				reassignService.setTypeReassignInsert(reassignInfoVO);
		    	
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
	
	/**
	 * 유형별 담당자 재정의 정보 저장
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setTypeReassignUpdate.do")
	public void setTypeReassignUpdate(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, HttpServletResponse response
			, ReassignInfoVO reassignInfoVO) throws Exception {
		Gson gson = new Gson();
		
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			int cnt =0;
			cnt = reassignService.getTypeReassignInfoCount(reassignInfoVO);
			
			
			if(cnt>0){
				msg = "이미 등록된 정보 입니다. \n 화인 후 등록하세요.";
				isOk =false;
			}
			else{
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				reassignInfoVO.setRgdtEmpNo(loginInfo.getUserid());
				reassignInfoVO.setUpdtEmpNo(loginInfo.getUserid());
				
				
				reassignService.setTypeReassignUpdate(reassignInfoVO);
		    	
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

	/**
	 * 유형별 담당자 재정의 정보 저장
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setTypeReassignDelete.do")
	public void setTypeReassignDelete(HttpServletRequest request
			, String capno
			, String searchCondition
			, String searchKeyword
			, HttpServletResponse response
			, ReassignInfoVO reassignInfoVO) throws Exception {
		Gson gson = new Gson();
		
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
		
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
			String InfoList = reassignInfoVO.getInfoList();
			for(String row:InfoList.split(",")){
				
				
				ReassignInfoVO reassignInfo = new ReassignInfoVO();
				
				String[] info = row.split("/");
				
								
				reassignInfo.setKtEmpNo(info[0]);
				reassignInfo.setcType(info[1]);
				reassignInfo.setReEmpNo(info[2]);
				
				reassignService.setTypeReassignDelete(reassignInfo);
			}
	    	
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

}
