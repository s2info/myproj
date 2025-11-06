package sdiag.server.web;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

import sdiag.login.service.UserManageVO;
import sdiag.member.service.MemberVO;
import sdiag.securityDay.service.SdCheckListGroupVO;
import sdiag.securityDay.service.SdSearchVO;
import sdiag.securityDay.service.SdTargetInfoVO;
import sdiag.server.service.ServerPolSearchVO;
import sdiag.server.service.ServerPolService;
import sdiag.server.service.ServerPolVO;
import sdiag.util.CommonUtil;
import sdiag.util.StringUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.exanal.service.ExanalManageService;
import sdiag.exanal.service.PolVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.CamelUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.rte.psl.dataaccess.util.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * 상세로그 엑셀파일을 업로드하고 테이블 생성한다.
 * DB테이블과 컬럼명을 정의하고 서비스 클래스로 요청을 전달한다.
 * 서비스클래스에서 처리한 결과를 웹 화면으로 전달을 위한 Controller를 정의한다
 * @author LEE CHANG JAE
 * @since 2015.10.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.10.27  CJLee          최초 생성
 *
 * </pre>
 */

@Controller
public class ServerPolController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "ServerPolService")
    private ServerPolService serverPolService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
	@Resource(name= "commonService")
	private CommonService comService;     
    
	

    /**
	 * 정책로그 데이터 목록을 조회한다.
     * @param loginVO
     * @param searchVO
     * @param model
     * @return "/handy/ExanalPolList"
     * @throws Exception
     */
    @RequestMapping("/server/serverPolLogList.do")
	public String serverPolLogList ( ServerPolSearchVO searchVO
			, HttpServletRequest request
			, HttpServletResponse response
			, ModelMap model
			) throws Exception {
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("handyPageSize"));

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		String genTableName = ""; // set table name temporary 
		
		/** top menu call **/
		CommonUtil.topMenuToString(request, "server", comService);
		CommonUtil.serverLeftMenuToString(request, LeftMenuInfo.SERVER);
		
		// 로그이한 사용자 정보
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		if(!loginInfo.getRole_code().equals("1")){
			searchVO.setS_EmpNo(loginInfo.getUserid());
		}
		searchVO.setRole_code(loginInfo.getRole_code());
		
		List<EgovMap> polIdList = new ArrayList<>();
		polIdList = serverPolService.getPolIdList();
		
		
		
		model.addAttribute("serverPolIdList", polIdList); 
		
		List<EgovMap> dateList = new ArrayList<>();
		

		
		List<EgovMap> columnList = new ArrayList<EgovMap>();
		List<EgovMap> logList = new ArrayList<EgovMap>();
		StringBuilder str = new StringBuilder();
		if(searchVO.getPolId() != null && !searchVO.getPolId().equals("")){
			ServerPolSearchVO serverPolInfo = new ServerPolSearchVO();
			
			
			serverPolInfo = serverPolService.getServerPolList(searchVO);
			searchVO.setTbnm(serverPolInfo.getTbnm());
			if(!loginInfo.getRole_code().equals("1")){
				serverPolInfo.setS_EmpNo(loginInfo.getUserid());
				serverPolInfo.setS_EmpNm("");
				serverPolInfo.setS_actionYn(searchVO.getS_actionYn());
			}else {
				serverPolInfo.setS_EmpNo(searchVO.getS_EmpNo());
				serverPolInfo.setS_EmpNm(searchVO.getS_EmpNm());
				serverPolInfo.setS_actionYn(searchVO.getS_actionYn());
			}
			
			
			
			
			
			dateList = serverPolService.getDateList(serverPolInfo);
			
			
			
			columnList =  serverPolService.getColumnList(serverPolInfo);
			
			serverPolInfo.setColumnList(columnList);
			logList = serverPolService.getlogList(serverPolInfo);
			str.append("<tr>");
			str.append("<th class='ck_button'><label><input type='checkbox' name='selAll_checkbox' id='selAll_checkbox' /><span></span></label></th>");
			for(EgovMap col:columnList){
	    		str.append(String.format("<th>%s</th>", col.get("columnName")));
	    	}
			
			str.append("<th>조치예정일</th>");
			str.append("<th>조치여부</th>");
			str.append("<th>비고</th>");
			str.append("<th>저장</th>");
	    	str.append("</tr>");

		    int cnt = 1;
		    
	    	for(EgovMap row:logList){
	    		
	    		str.append("<tr>");
	    		str.append(String.format("<td class='ck_button'><label><input type='checkbox' name='c_seq' value='%s'><span></span></label></td>", String.valueOf(cnt)));
		    	for(EgovMap col:columnList){
		    		if(loginInfo.getRole_code().equals("1") && col.get("columnName").equals("sldm_empno")){
		    			str.append(String.format("<td><input type='text' class='srh' value='%s' onKeyup='this.value=this.value.replace(/[^0-9]/g,\"\");' name='sldm_empno' id='sldm_empno%s' maxlength='8'/> </td>",row.get(CamelUtil.convert2CamelCase("sldm_empno")),String.valueOf(cnt)));
		    		}else{
		    			str.append(String.format("<td >%s</td>",row.get(CamelUtil.convert2CamelCase((String) col.get("columnName")))));	
		    		}
		    				    		
		    	}	
		    	str.append(String.format("<td><input type='text' class='srh' value='%s' onKeyup='this.value=this.value.replace(/[^0-9]/g,\"\");' name='actionDate' id='actionDate%s' maxlength='8'/> </td>",row.get(CamelUtil.convert2CamelCase("조치예정일")),String.valueOf(cnt)));
		    	str.append("<td>");
		    	str.append(String.format("<select  name='actionYn' id='actionYn%s' >",String.valueOf(cnt)));
		    	str.append(String.format("<option value='' %s >선택하세요.</option>", row.get(CamelUtil.convert2CamelCase("조치여부")).equals("") ? "selected":""));
		    	str.append(String.format("<option value='미완료' %s >미완료</option>", row.get(CamelUtil.convert2CamelCase("조치여부")).equals("미완료") ? "selected":""));
		    	str.append(String.format("<option value='조치중' %s >조치중</option>", row.get(CamelUtil.convert2CamelCase("조치여부")).equals("조치중") ? "selected":""));
		    	str.append(String.format("<option value='완료' %s >완료</option>", row.get(CamelUtil.convert2CamelCase("조치여부")).equals("완료") ? "selected":""));
            	str.append("</select>");
            	str.append("</td>");
		    	str.append(String.format("<td><textarea  type='text' name='bigo' id='bigo%s'>%s</textarea> </td>", String.valueOf(cnt),row.get(CamelUtil.convert2CamelCase("비고"))));
		    	str.append(String.format("<td><a class='btn_black' onclick=\"$.setLogInfo('%s')\"><span >저장</span></a></td>",String.valueOf(cnt)));
		    	str.append(String.format("<input  type='hidden' name='seq' id='seq%s' value='%s'/>", String.valueOf(cnt),row.get(CamelUtil.convert2CamelCase("seq"))));
		    	str.append(String.format("<input  type='hidden' name='empno' id='empno%s' value='%s'/>", String.valueOf(cnt),row.get(CamelUtil.convert2CamelCase("empno"))));
		    	str.append(String.format("<input  type='hidden' name='rgdtDate' id='rgdtDate%s' value='%s'/>", String.valueOf(cnt),row.get(CamelUtil.convert2CamelCase("rgdtDate"))));
	    		str.append("</tr>");
	    		
	    		cnt++;
	    	}
			
			
		}
		
		StringBuffer script = new StringBuffer();
		script.append("<script type='text/javascript'>");
		script.append("$(function () {"
				+ "$('#selAll_checkbox').click(function(){"
				+ "		var ischeck = $(this).is(':checked');"
				+ "		$('input:checkbox[name=c_seq]').prop('checked',ischeck);"
				+ "	});");
		script.append("});");
		script.append("</script>");
		str.append(script.toString());
		model.addAttribute("columnList", columnList);
		model.addAttribute("logList", logList);
		model.addAttribute("strList", str.toString());
    	
		model.addAttribute("dateList", dateList); 
		model.addAttribute("totalPage", 1);
		model.addAttribute("currentpage", 1);	  
		model.addAttribute("searchVO", searchVO);	  
		
        return "/server/serverPolLogList";
    }
    
    /**
	 * 로그 정보 업데이트
	 * @throws Exception 
	 */
	@RequestMapping(value = "/server/setPolLogUpdate.do") 
	public void setPolLogUpdate(HttpServletRequest request,
			HttpServletResponse response,
			ServerPolVO serverPolVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
	    
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			// 로그인한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			
			if(loginInfo.getRole_code().equals("1")){
				serverPolService.setPolLogUpdateAdmin(serverPolVO);
			}else{
				serverPolVO.setSldmEmpNo(loginInfo.getUserid());
				
				serverPolService.setPolLogUpdate(serverPolVO);
			}
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		} 
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	
	 /**
	 * 로그 날짜 리스트 조회
	 * @throws Exception 
	 */
	@RequestMapping(value = "/server/getDateList.do") 
	public void getDateList(HttpServletRequest request,
			HttpServletResponse response,
			ServerPolSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
	    
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			// 로그이한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			searchVO.setS_EmpNo(loginInfo.getUserid());
			
			List<EgovMap> dateList = new ArrayList<>();
			
			dateList = serverPolService.getDateList(searchVO); 
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			map.put("dateList", dateList);
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		} 
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	/**
	 * 로그 정보 업데이트
	 * @throws Exception 
	 */
	@RequestMapping(value = "/server/setAllLogUpdate.do") 
	public void setAllLogUpdate(HttpServletRequest request,
			HttpServletResponse response,
			ServerPolVO serverPolVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
	    
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!"); 
		    }
			
			
			// 로그인한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			
			
			String[] sldmOrgLogDateArr =  serverPolVO.getSldmOrgLogDate().split(",");
			String[] seqArr =  serverPolVO.getSeq().split(",");
			String[] actionDateArr =  serverPolVO.getActionDate().split(",");
			String[] actionYnArr =  serverPolVO.getActionYn().split(",");
			String[] bigoArr =  serverPolVO.getBigo().split(",");
			String[] sldmEmpNo =  serverPolVO.getSldmEmpNo().split(",");
			String[] empNo =  serverPolVO.getEmpNo().split(",");
			

			for(int i =0; i<seqArr.length; i++){
				ServerPolVO serverPolInfo = new ServerPolVO();
				
				serverPolInfo.setSrc_table(serverPolVO.getSrc_table());
				serverPolInfo.setRole_cdoe(serverPolVO.getRole_cdoe());
				serverPolInfo.setSldmOrgLogDate(sldmOrgLogDateArr[i].equals("null")?"":sldmOrgLogDateArr[i]);
				serverPolInfo.setSeq(seqArr[i].equals("null")?"":seqArr[i]);
				serverPolInfo.setActionDate(actionDateArr[i].equals("null")?"":actionDateArr[i]);
				serverPolInfo.setActionYn(actionYnArr[i].equals("null")?"":actionYnArr[i]);
				serverPolInfo.setBigo(bigoArr[i].equals("null")?"":bigoArr[i]);
				
				if(loginInfo.getRole_code().equals("1")){
					serverPolInfo.setSldmEmpNo(sldmEmpNo[i].equals("null")?"":sldmEmpNo[i]);
					serverPolInfo.setEmpNo(empNo[i].equals("null")?"":empNo[i]);
					
					serverPolService.setPolLogUpdateAdmin(serverPolInfo);
				}else{
					serverPolInfo.setSldmEmpNo(loginInfo.getUserid());
					
					serverPolService.setPolLogUpdate(serverPolInfo);
				}
								
				
			}
			
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		} 
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
}


