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

import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.man.service.DignosisItemVO;
import sdiag.board.service.NoticeService;
import sdiag.man.service.SearchVO;
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
public class CodeController {
	@Resource(name = "NoticeService")
	private NoticeService noticeService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	@RequestMapping(value="/man/codelist.do")
	public String policystatus(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.CODE);
	
		if(searchVO.getSearchKeyword().equals("")){
			searchVO.setSelectedRowId("0");
		}
		System.out.println(searchVO.getSearchKeyword().toString());
		List<CodeInfoVO> list = noticeService.getCodeItemList(searchVO);
		
		StringBuffer str = new StringBuffer();
		String hCode = "";
		int rownum=0;
		for(CodeInfoVO row:list){
			if(row.getMinr_code().equals("00")){
				if(searchVO.getSearchKeyword().equals(row.getMajr_code())){
					searchVO.setSelectedRowId(String.valueOf(rownum));
				}
				rownum++;
				str.append(String.format("<ul class='row top row_%s' style='border-bottom:#eee solid 1px;'>", row.getMajr_code()));
				str.append(String.format("<li class='col co1'>%s</li>",row.getMajr_code()));
				str.append(String.format("<li class='col co2'>%s</li>",row.getMinr_code()));
				str.append(String.format("<li class='col co3'>%s</li>",row.getCode_desc()));
				str.append(String.format("<li class='col co4'>%s</li>",row.getUse_indc()));
				str.append(String.format("<li class='col co5'>%s</li>",row.getRgdt_date()));
				str.append("<li class='col co6'><a class='btn_modify' mode='2' style='cursor:pointer;'><img src='/img/btn_modify.png' alt='modify' title='modify' /></a></li>");
				str.append("<li class='col co7'><a class='btn_add' mode='1' style='cursor:pointer;'><img src='/img/btn_plus2.png' alt='plus' title='plus' /></a></li>");
				str.append("<li class='col co8'><a class='btn_delete' mode='5' style='cursor:pointer;'><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></li>");
				str.append("</ul>");
				List<CodeInfoVO> subList = CreateCodeSubList(list, row.getMajr_code());
				str.append("<div class='codeHolder'>");
				int i=0;
				for(CodeInfoVO srow:subList){
					str.append(String.format("<ul id='%s' class='row %s srow_%s' style='border-bottom:#eee solid 1px;'>"
								, String.format("%s_%s", srow.getMajr_code(), srow.getMinr_code())
								, i%2 == 0 ? "odd" : "evn"
								, String.format("%s_%s", srow.getMajr_code(), srow.getMinr_code()) ));
					str.append(String.format("<li class='col co1'>%s</li>",srow.getMajr_code()));
					str.append(String.format("<li class='col co2'>%s</li>",srow.getMinr_code()));
					str.append(String.format("<li class='col co3'>%s</li>",srow.getCode_desc()));
					str.append(String.format("<li class='col co4'>%s</li>",srow.getUse_indc()));
					
					str.append(String.format("<li class='col co5'>%s</li>",srow.getRgdt_date()));
					str.append("<li class='col co6'><a class='btn_modify' mode='2' style='cursor:pointer;'><img src='/img/btn_modify.png' alt='modify' title='modify' /></a></li>");
					str.append("<li class='col co7'><a class='btn_delete' mode='5' style='cursor:pointer;'><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></li>");
					str.append("<li class='col co8'></li>");
					str.append("</ul>");
					i++;
				}
				str.append("</div>");
			}
		}
	
		model.addAttribute("tblbody", str.toString());
		
		return "man/code/codelist";
		
	}

	/**
	 * 코드정보 팝업
	 * @param request
	 * @param mode
	 * @param majcd
	 * @param mincd
	 * @param desc
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/code/setCodeItemModifyPopup.do")
	public void setCodeItemModifyPopup(HttpServletRequest request
				, String mode
				, String majcd
				, String mincd
				, String desc
				, HttpServletResponse response) throws Exception {
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
			
			CodeInfoVO searchVO = new CodeInfoVO();
			searchVO.setMajr_code(majcd);
			searchVO.setMinr_code(mincd);
			if(Integer.parseInt(mode) == 5){
				//삭제이면
				CodeInfoVO paramInfo = new CodeInfoVO();
				paramInfo.setMajr_code(majcd);
				paramInfo.setMinr_code(mincd);;
				
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("mode", mode);
				item.put("item", paramInfo);
			    boolean	ret = noticeService.setCodeItemModify(item);
			}else{
				CodeInfoVO itemInfo = new CodeInfoVO();
				if(Integer.parseInt(mode) == 2)//수정
				{
					itemInfo = noticeService.getCodeItemInfo(searchVO);
				}
				
				popupBody.append("<div class='ly_block1' style='width:600px;background:#fff;padding:10px 10px 10px 10px'>");
		    	popupBody.append(String.format("<div class='subTT' style='width:100%%;cursor:move;'><span>%s</span></div>", Integer.parseInt(mode) == 2 ? "코드정보 수정" : Integer.parseInt(mode) == 3 ? "대분류 항목 추가" : "중분류 항목 추가"));
		    	popupBody.append("<div class='pd10'></div>");
		    	popupBody.append("<table class='tblInfoType_1 tblInfoType' cellpadding='0' cellspacing='0'>");
	    		popupBody.append(" <colgroup><col style='width:20%;' /><col style='width:80%;' /></colgroup>");
	    		popupBody.append("			<tr>");
	    		if(Integer.parseInt(mode) == 3)
				{
					popupBody.append("				<th>대분류 코드</th>");
					popupBody.append(String.format("	<td><input type='text' class='srh'  id='majCode' name='majCode' value='' /><input type='hidden' id='mode' name='mode' value='%s' /><input type='hidden' id='majCode' name='majCode' value='00' /></td>", mode));
					popupBody.append("			</tr>");
				}
				else{
					popupBody.append(String.format("	<th>대분류</th>", Integer.parseInt(mode) == 2 ? "대분류정보" : "대분류"));
					popupBody.append(String.format("	<td>%s - %s<input type='hidden' id='majCode' name='majCode' value='%s' /><input type='hidden' id='mode' name='mode' value='%s' /></td>", majcd, desc, majcd, mode));
					popupBody.append("			</tr>");
		
					if(Integer.parseInt(mode) == 2)//수정
					{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중분류 코드</th>");
						popupBody.append(String.format("	<td>%s<input type='hidden' id='minCode' name='minCode' value='%s' /></td>", itemInfo.getMinr_code(), itemInfo.getMinr_code()  ));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}else{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중분류 코드</th>");
						popupBody.append(String.format("	<td><input type='text' class='srh' style='width:200px' id='minCode' name='minCode' maxlength='3' value='' /></td>", ""));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}
				}
				popupBody.append("				<th>코드명</th>");
				popupBody.append(String.format("	<td><input type='text' class='srh' style='width:400px' id='desc' name='desc' value='%s' /></td>", Integer.parseInt(mode) == 2 ? itemInfo.getCode_desc() : ""));
				popupBody.append("			</tr>");
				popupBody.append("			<tr>");
				popupBody.append("				<th>추가정보1</th>");
				popupBody.append(String.format("<td><textarea  id='add_info1' rows='5' name='add_info1' style='width:99%%;' class='inputarea'>%s</textarea></td>", itemInfo.getAdd_info1()));
				popupBody.append("			</tr>");
				popupBody.append("			<tr>");
				popupBody.append("				<th>사용여부</th>");
				popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><select id='isused' name='idused'><option value='Y' %s>사용</option><option value='N' %s>사용중지</option></select></td>"
						, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("Y") ? "selected" : "" : ""
							, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("N") ? "selected" : "" : ""));
				popupBody.append("			</tr>");
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
				popupBody.append(String.format("		<li class='WPT_txt'>%s</li>", Integer.parseInt(mode) == 2 ? "코드정보 수정" : Integer.parseInt(mode) == 3 ? "대분류 항목 추가" : "중분류 항목 추가"));
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
				if(Integer.parseInt(mode) == 3)
				{
					popupBody.append("				<th>대분류 코드</th>");
					popupBody.append(String.format("	<td><input type='text' class='srh' id='majCode' name='majCode' value='' /><input type='hidden' id='mode' name='mode' value='%s' /><input type='hidden' id='majCode' name='majCode' value='00' /></td>", mode));
					popupBody.append("			</tr>");
				}
				else{
					popupBody.append(String.format("	<th>대분류</th>", Integer.parseInt(mode) == 2 ? "대분류정보" : "대분류"));
					popupBody.append(String.format("	<td>%s - %s<input type='hidden' id='majCode' name='majCode' value='%s' /><input type='hidden' id='mode' name='mode' value='%s' /></td>", majcd, desc, majcd, mode));
					popupBody.append("			</tr>");
		
					if(Integer.parseInt(mode) == 2)//수정
					{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중분류 코드</th>");
						popupBody.append(String.format("	<td>%s<input type='hidden' id='minCode' name='minCode' value='%s' /></td>", itemInfo.getMinr_code(), itemInfo.getMinr_code()  ));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}else{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중분류 코드</th>");
						popupBody.append(String.format("	<td><input type='text' class='srh' style='width:200px' id='minCode' name='minCode' maxlength='3' value='' /></td>", ""));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}
				}
				popupBody.append("				<th>코드명</th>");
				popupBody.append(String.format("	<td><input type='text' class='srh' style='width:400px' id='desc' name='desc' value='%s' /></td>", Integer.parseInt(mode) == 2 ? itemInfo.getCode_desc() : ""));
				popupBody.append("			</tr>");	
				popupBody.append("			<tr>");
				popupBody.append("				<th>사용여부</th>");
				popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><select id='isused' name='idused'><option value='Y' %s>사용</option><option value='N' %s>사용중지</option></select></td>"
						, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("Y") ? "selected" : "" : ""
							, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("N") ? "selected" : "" : ""));
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
						+ "	if($('#minCode').val() == ''){"
						+ "		alert('중분류 코드를 입력하여 주세요.');return false;"
						+ "	}"
						+ "	if($('#desc').val() == ''){"
						+ "		alert('코드명을 입력하여 주세요.');return false;"
						+ "	}"
						+ "	if (!confirm('코드 정보를 저장 하시겠습니까?')) { return false; }"
						+ "	var data = {mode:$('#mode').val(),majCode:$('#majCode').val(), minCode:$('#minCode').val(), desc:$('#desc').val(), isused:$('#isused').val(), add_info1:$('#add_info1').val()};"
						+ "	$.ajax({"
						+ "		data: data,"
						+ "		url: '/code/setCodeItemModifySave.do',"
						+ "		type: 'POST',"
						+ "		dataType: 'json',"
						+ "		error: function (jqXHR, textStatus, errorThrown) {"
						+ "			if(jqXHR.status == 401){"
						+ "				alert('인증정보가 만료되었습니다.');"
						+ "				location.href='/';"
						+ "			}else{"
						+ "				alert(textStatus + '\\r\\n' + errorThrown);"
						+ "			}"
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
			}
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
	 * 코드항목 저장
	 * @param request
	 * @param mode
	 * @param majCode
	 * @param minCode
	 * @param desc
	 * @param isused
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/code/setCodeItemModifySave.do")
	public void setCodeItemModifySave(HttpServletRequest request
				, String mode
				, String majCode
				, String minCode
				, String desc
				, String isused
				, String add_info1
				, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		boolean ret = false;
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			CodeInfoVO paramInfo = new CodeInfoVO();
			paramInfo.setMajr_code(majCode);
			paramInfo.setMinr_code(minCode);;
			paramInfo.setCode_desc(desc);
			paramInfo.setUse_indc(isused);
			paramInfo.setAdd_info1(add_info1);
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("mode", mode);
			item.put("item", paramInfo);
			if(mode.equals("1"))
			{
				int existCnt = noticeService.setCodeItemCheckExist(paramInfo);
				if(existCnt > 0){
					msg = "동일한 중분류 코드가 존재 합니다.";
				}else{
					ret = noticeService.setCodeItemModify(item);
				}
			}else if(mode.equals("3")){
				//대분류 추가
				paramInfo.setMinr_code("00");
				
				int existCnt = noticeService.setCodeItemCheckExist(paramInfo);
				if(existCnt > 0){
					msg = "동일한 대분류 코드가 존재 합니다.";
				}else{
					ret = noticeService.setCodeItemModify(item);
				}
			}else{
				ret = noticeService.setCodeItemModify(item);
			}



			isOk = ret;

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
	 * 코드정보 Export Excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/code/codeexportexcel.do")
	public void codeexportexcel(HttpServletRequest request
								, HttpServletResponse response) throws Exception {
		
		String fileName = "코드리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("코드현황");
		excelVO.setTitle("코드현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		
		List<HashMap<String, Object>> list = noticeService.getCodeItemListForExcelExport();
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
	 * 진단항목 Export Excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/diagitemexportexcel.do")
	public void diagitemexportexcel(HttpServletRequest request
								, HttpServletResponse response) throws Exception {
		
		String fileName = "진단항목리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("진단항목현황");
		excelVO.setTitle("진단항목현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		
		List<HashMap<String, Object>> list = noticeService.getDignosisItemListForExcelExport();
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
		
		ExcelUtil.hssExcelDown(response, excelVO, excellist);
		
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "maj_code" : ColumnName = "대분류";break;
		case "majr_desc" : ColumnName = "대분류명";break;
		case "min_code" : ColumnName = "중분류";break;
		case "min_desc" : ColumnName = "코드명";break;
		case "use_indc" : ColumnName = "사용여부";break;
		case "diagmaj_code" : ColumnName = "대진단";break;
		case "diagmaj_desc" : ColumnName = "대진단명";break;
		case "diagmin_code" : ColumnName = "중진단";break;
		case "diagmin_desc" : ColumnName = "중진단명";break;
		case "ordr" : ColumnName = "정렬순서";break;
		case "diag_notice" : ColumnName = "진단항목 설명";break;
		case "buseo_indc" : ColumnName = "부서진단여부";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	
	
	
	/**
	 *  진단항목 관리
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/dignosisItemlist.do")
	public String dignosisItemlist(HttpServletRequest request, @ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception{	
			
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.DIGNOSIS);

		HashMap<String,Object> retMap = noticeService.getDignosisItemList(searchVO);
		List<DignosisItemVO> list = (List<DignosisItemVO>)retMap.get("list");
		StringBuffer str = new StringBuffer();
		String hCode = "";
		for(DignosisItemVO row:list){
			if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
				str.append("<ul class='row top' style='border-bottom:#eee solid 1px;'>");
				str.append(String.format("<li class='col co1'>%s</li>",row.getDiag_majr_code()));
				str.append(String.format("<li class='col co2'>%s</li>",row.getDiag_minr_code()));
				str.append(String.format("<li class='col co3'>%s</li>",row.getDiag_desc()));
				str.append(String.format("<li class='col co4'>%s</li>",row.getUse_indc()));
				str.append(String.format("<li class='col co5'>%s</li>",row.getOrdr()));
				str.append(String.format("<li class='col co6'>%s</li>",row.getRgdt_date()));
				str.append("<li class='col co7'><a class='btn_modify' mode='2' style='cursor:pointer;'><img src='/img/btn_modify.png' alt='modify' title='modify' /></a></li>");
				str.append("<li class='col co8'><a class='btn_add' mode='1' style='cursor:pointer;'><img src='/img/btn_plus2.png' alt='plus' title='plus' /></a></li>");
				str.append("<li class='col co9'><a class='btn_delete' mode='5' style='cursor:pointer;'><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></li>");
				str.append("</ul>");
				List<DignosisItemVO> subList = CreateSubList(list, row.getDiag_majr_code());
				str.append("<div class='codeHolder'>");
				int i=0;
				for(DignosisItemVO srow:subList){
					str.append(String.format("<ul id='%s' class='row %s' style='cursor:move;border-bottom:#eee solid 1px;'>", String.format("%s_%s", srow.getDiag_majr_code(), srow.getDiag_minr_code()), i%2 == 0 ? "odd" : "evn" ));
					str.append(String.format("<li class='col co1'>%s</li>",srow.getDiag_majr_code()));
					str.append(String.format("<li class='col co2'>%s</li>",srow.getDiag_minr_code()));
					str.append(String.format("<li class='col co3'>%s</li>",srow.getDiag_desc()));
					str.append(String.format("<li class='col co4'>%s</li>",srow.getUse_indc()));
					str.append(String.format("<li class='col co5'>%s</li>",srow.getOrdr()));
					str.append(String.format("<li class='col co6'>%s</li>",srow.getRgdt_date()));
					str.append("<li class='col co7'><a class='btn_modify' mode='2' style='cursor:pointer;'><img src='/img/btn_modify.png' alt='modify' title='modify' /></a></li>");
					str.append("<li class='col co8'><a class='btn_delete' mode='5' style='cursor:pointer;'><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></li>");
					str.append("<li class='col co9'></li>");
					str.append("</ul>");
					i++;
				}
				str.append("</div>");
			}
		}

		model.addAttribute("tblbody", str.toString());
		return "man/code/diagnosisItemList";
	}
		
	@RequestMapping(value="/code/setDiagnosisItemUpdate.do")
	public void setDiagnosisItemUpdate(HttpServletRequest request
				, String mode
				, String maj_cd
				, String min_cd
				, String name
				, String active
				, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		boolean ret = false;

		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			if(Integer.parseInt(mode) == 4){
				//Order 수정
				List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
				List<String> cds = CommonUtil.SplitToString(min_cd, ",");

				for(String str:cds){
					String[] min_cds = str.split("/");
					HashMap<String, String> info = new HashMap<String, String>();
					info.put("majCode", maj_cd);
					info.put("minCode", min_cds[0]);
					info.put("orderval", min_cds[1]);
					dataList.add(info);
				}
				hMap.put("dataList", dataList);

				ret = noticeService.setDignosisItemOrderUpdate(hMap);
			}

			isOk = ret;
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

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
	 * 진단항목 수정팝업
	 * @param request
	 * @param mode
	 * @param majcd
	 * @param mincd
	 * @param desc
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/code/setDiagnosisItemModifyPopup.do")
	public void setDiagnosisItemModifyPopup(HttpServletRequest request
				, String mode
				, String majcd
				, String mincd
				, String desc
				, HttpServletResponse response) throws Exception {
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
			
			DignosisItemVO searchVO = new DignosisItemVO();
			searchVO.setDiag_majr_code(majcd);
			searchVO.setDiag_minr_code(mincd);
			boolean isSubItem = false;
			if(Integer.parseInt(mode) == 5){
				//삭제이면
				
			    boolean	ret = noticeService.setDignosisItemDelete(searchVO);
			}else{
			
			
				DignosisItemVO itemInfo = new DignosisItemVO();
				if(Integer.parseInt(mode) == 2){
					itemInfo = noticeService.getDignosisItemInfo(searchVO);
					isSubItem = true;
				}else if(Integer.parseInt(mode) == 1){//추가
					DignosisItemVO pItemInfo = noticeService.getDignosisItemInfo(searchVO);
					itemInfo.setBuseo_indc(pItemInfo.getBuseo_indc());
					isSubItem = true;
				}
				
				popupBody.append("<div class='ly_block1' style='width:600px;background:#fff;padding:10px 10px 10px 10px'>");
		    	popupBody.append(String.format("<div class='subTT' style='width:100%%;cursor:move;'><span>%s</span></div>", Integer.parseInt(mode) == 2 ? "진단항목 수정" : Integer.parseInt(mode) == 3 ? "대진단 항목 추가" : "중진단 항목 추가"));
		    	popupBody.append("<div class='pd10'></div>");
		    	popupBody.append("<table class='tblInfoType_1 tblInfoType' cellpadding='0' cellspacing='0'>");
	    		popupBody.append(" <colgroup><col style='width:20%;' /><col style='width:80%;' /></colgroup>");
	    		popupBody.append("			<tr>");
				if(Integer.parseInt(mode) == 3)//대진단 추가
				{
					popupBody.append("				<th>대진단 코드</th>");
					popupBody.append(String.format("	<td><input type='text' class='srh'  id='majCode' name='majCode' value='' /><input type='hidden' id='mode' name='mode' value='%s' /><input type='hidden' id='majCode' name='majCode' value='00' /></td>", mode));
					popupBody.append("			</tr>");
				}
				else{
					popupBody.append(String.format("	<th>대진단</th>", Integer.parseInt(mode) == 2 ? "진단정보" : "대진단"));
					popupBody.append(String.format("	<td>%s - %s<input type='hidden' id='majCode' name='majCode' value='%s' /><input type='hidden' id='mode' name='mode' value='%s' /></td>", majcd, desc, majcd, mode));
					popupBody.append("			</tr>");
		
					if(Integer.parseInt(mode) == 2)//수정
					{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중진단 코드</th>");
						popupBody.append(String.format("	<td>%s<input type='hidden' id='minCode' name='minCode' value='%s' /></td>", itemInfo.getDiag_minr_code(), itemInfo.getDiag_minr_code()  ));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}else{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중진단 코드</th>");
						popupBody.append(String.format("	<td><input type='text' class='srh'  style='width:200px' id='minCode' name='minCode' maxlength='3' value='' /></td>", ""));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}
				}
				popupBody.append("				<th>명칭</th>");
				popupBody.append(String.format("	<td><input type='text' class='srh'  style='width:400px' id='desc' name='desc' value='%s' /></td>", Integer.parseInt(mode) == 2 ? itemInfo.getDiag_desc() : ""));
				popupBody.append("			</tr>");	
				popupBody.append("				<th>부서진단여부</th>");
				popupBody.append(String.format("	<td><input type='radio' id='buseoindc' name='buseoindc' value='Y' %s %s /> 예 &nbsp;&nbsp; "
						+ "								<input type='radio' id='buseoindc' name='buseoindc' value='N' %s %s /> 아니오</td>"
													, itemInfo.getBuseo_indc().equals("Y") ? "checked" : ""
													, isSubItem ? "disabled" : ""
													, itemInfo.getBuseo_indc().equals("N") ? "checked" : ""
													, isSubItem ? "disabled" : ""));
				popupBody.append("			</tr>");
				popupBody.append("				<th>진단항목설명</th>");
				popupBody.append(String.format("<td style='padding:5px 0 5px 10px;'><textarea id='diagnoti' name='diagnoti' maxlength='2000' style='width:100%%' rows='7'>%s</textarea></td>", Integer.parseInt(mode) == 2 ? itemInfo.getDiag_notice() : ""));
				popupBody.append("			</tr>");	
				popupBody.append("			<tr>");
				popupBody.append("				<th>사용여부</th>");
				popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><select id='isused' name='idused'><option value='Y' %s>사용</option><option value='N' %s>사용중지</option></select></td>"
						, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("Y") ? "selected" : "" : ""
							, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("N") ? "selected" : "" : ""));
				popupBody.append("			</tr>");
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
				popupBody.append(String.format("		<li class='WPT_txt'>%s</li>", Integer.parseInt(mode) == 2 ? "진단항목 수정" : Integer.parseInt(mode) == 3 ? "대진단 항목 추가" : "중진단 항목 추가"));
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
				if(Integer.parseInt(mode) == 3)//대진단 추가
				{
					popupBody.append("				<th>대진단 코드</th>");
					popupBody.append(String.format("	<td><input type='text' class='srh' id='majCode' name='majCode' value='' /><input type='hidden' id='mode' name='mode' value='%s' /><input type='hidden' id='majCode' name='majCode' value='00' /></td>", mode));
					popupBody.append("			</tr>");
				}
				else{
					popupBody.append(String.format("	<th>대진단</th>", Integer.parseInt(mode) == 2 ? "진단정보" : "대진단"));
					popupBody.append(String.format("	<td>%s - %s<input type='hidden' id='majCode' name='majCode' value='%s' /><input type='hidden' id='mode' name='mode' value='%s' /></td>", majcd, desc, majcd, mode));
					popupBody.append("			</tr>");
		
					if(Integer.parseInt(mode) == 2)//수정
					{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중진단 코드</th>");
						popupBody.append(String.format("	<td>%s<input type='hidden' id='minCode' name='minCode' value='%s' /></td>", itemInfo.getDiag_minr_code(), itemInfo.getDiag_minr_code()  ));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}else{
						popupBody.append("			<tr>");
						popupBody.append("				<th>중진단 코드</th>");
						popupBody.append(String.format("	<td><input type='text' class='srh' style='width:200px' id='minCode' name='minCode' maxlength='3' value='' /></td>", ""));
						popupBody.append("			</tr>");
						popupBody.append("			<tr>");
					}
				}
				popupBody.append("				<th>명칭</th>");
				popupBody.append(String.format("	<td><input type='text' class='srh' style='width:400px' id='desc' name='desc' value='%s' /></td>", Integer.parseInt(mode) == 2 ? itemInfo.getDiag_desc() : ""));
				popupBody.append("			</tr>");	
				popupBody.append("			<tr>");
				popupBody.append("				<th>사용여부</th>");
				popupBody.append(String.format("	<td style='padding:5px 0 5px 10px;'><select id='isused' name='idused'><option value='Y' %s>사용</option><option value='N' %s>사용중지</option></select></td>"
						, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("Y") ? "selected" : "" : ""
							, Integer.parseInt(mode) == 2 ? itemInfo.getUse_indc().equals("N") ? "selected" : "" : ""));
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
						+ "	if($('#minCode').val() == ''){"
						+ "		alert('중진단 항목코드를 입력하여 주세요.');return false;"
						+ "	}"
						+ "	if($('#desc').val() == ''){"
						+ "		alert('진단항목 명칭을 입력하여 주세요.');return false;"
						+ "	}"
						+ "	if (!confirm('진단항목 정보를 저장 하시겠습니까?')) { return false; }"
						+ "	var buseoindc = $('input:radio[name=buseoindc]:checked').val();"
						+ "	var data = {mode:$('#mode').val(),majCode:$('#majCode').val(), minCode:$('#minCode').val(), desc:$('#desc').val(), isused:$('#isused').val(), notice:$('#diagnoti').val(), buseoindc:buseoindc};"
						+ "	$.ajax({"
						+ "		data: data,"
						+ "		url: '/code/setDiagnosisItemModifySave.do',"
						+ "		type: 'POST',"
						+ "		dataType: 'json',"
						+ "		error: function (jqXHR, textStatus, errorThrown) {"
						+ "			if(jqXHR.status == 401){"
						+ "				alert('인증정보가 만료되었습니다.');"
						+ "				location.href='/';"
						+ "			}else{"
						+ "				alert(textStatus + '\\r\\n' + errorThrown);"
						+ "			}"
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
			}
				
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
		
	@RequestMapping(value="/code/setDiagnosisItemModifySave.do")
	public void setDiagnosisItemModifySave(HttpServletRequest request
				, String mode
				, String majCode
				, String minCode
				, String desc
				, String isused
				, String notice
				, String buseoindc
				, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();

		String msg = "Error!!";
		Boolean isOk = false; 
		boolean ret = false;
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			DignosisItemVO paramInfo = new DignosisItemVO();
			paramInfo.setDiag_majr_code(majCode);
			paramInfo.setDiag_minr_code(minCode);;
			paramInfo.setDiag_desc(desc);
			paramInfo.setUse_indc(isused);
			paramInfo.setDiag_notice(notice);
			paramInfo.setBuseo_indc(buseoindc);
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("mode", mode);
			item.put("item", paramInfo);
			if(mode.equals("1"))
			{
				int existCnt = noticeService.setDignosisItemCheckExist(paramInfo);
				if(existCnt > 0){
					msg = "동일한 중진단 코드가 존재 합니다.";
				}else{
					ret = noticeService.setDignosisItemModify(item);
				}
			}else if(mode.equals("3")){
				//대진단 추가
				paramInfo.setDiag_minr_code(paramInfo.getDiag_majr_code());
				paramInfo.setOrdr("0");
				
				int existCnt = noticeService.setDignosisItemCheckExist(paramInfo);
				if(existCnt > 0){
					msg = "동일한 대진단 코드가 존재 합니다.";
				}else{
					ret = noticeService.setDignosisItemModify(item);
				}
			}else{
				ret = noticeService.setDignosisItemModify(item);
			}



			isOk = ret;

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
	
	private List<CodeInfoVO> CreateCodeSubList(List<CodeInfoVO> List, String majCode){
		List<CodeInfoVO> list = new ArrayList<CodeInfoVO>();
		for(CodeInfoVO row:List){
			if(row.getMajr_code().equals(majCode)){
				if(!row.getMinr_code().equals("00")){
					list.add(row);
				}
			}
		}

		return list;
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
	
}
