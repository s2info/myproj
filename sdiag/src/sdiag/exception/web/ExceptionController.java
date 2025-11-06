package sdiag.exception.web;

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

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import sdiag.board.service.NoticeVO;
import sdiag.com.service.CommonService;
import sdiag.exception.service.ExceptionService;
import sdiag.exception.service.ExceptionVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import sdiag.util.CommonUtil;
import sdiag.util.LeftMenuInfo;

@Controller
public class ExceptionController {
	@Resource(name = "ExceptionService")
	protected ExceptionService exceptionService;
	
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	/**
	 * 예외처리IP LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionIpList.do")
	private String exceptionIpList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTIP);
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
			HashMap<String,Object> retMap = exceptionService.getExceptionIpList();
			List<ExceptionVO> exceptionIpList = (List<ExceptionVO>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", exceptionIpList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "exception/exceptionIpList";
		
	}
	
	/**
	 * 메인화면 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionIpForm.do")
	private String exceptionIpForm(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTIP, comService);
		
		HashMap<String,Object> retMap = exceptionService.getPolIdxList();
		List<ExceptionVO> polIdxList = (List<ExceptionVO>)retMap.get("list");
		model.addAttribute("polIdxList", polIdxList);
		
		
		ExceptionVO exceptionIpInfo = new ExceptionVO();
		
		if(exceptionVO.getFormMod().equals("U")){
			exceptionIpInfo = exceptionService.getExceptionIpInfo(exceptionVO);
			exceptionIpInfo.setFormMod(exceptionVO.getFormMod());
		}
		
		model.addAttribute("exceptionIpInfo", exceptionIpInfo);
		
		
		return "exception/exceptionIpForm";
		
	}
	
	/**
	 * 예외IP 등록 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionIpSave.do")
	private void exceptionIpSave(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
	    ExceptionVO exceptionIpInfo = new ExceptionVO();
	    
	    
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
			if(exceptionVO.getFormMod().equals("U")){
				result = exceptionService.exceptionIpDelete(exceptionVO);
			
			}else{
				
				exceptionIpInfo = exceptionService.getExceptionIpInfo(exceptionVO);
				
				if(!exceptionIpInfo.getSec_pol_id().equals(null) && exceptionIpInfo.getSec_pol_id() != "" ){
					isOk = false;
					msg = "이미 존재하는 정책 입니다.";
					result = -1;
				}
			}
			
			if(result == 0 && (exceptionIpInfo.getSec_pol_id().equals(null) || exceptionIpInfo.getSec_pol_id() == "")){
				result = exceptionService.exceptionIpSave(exceptionVO);
			
				if(result == -1){
					isOk = false;
					msg = "저장중 오류가 발생하였습니다.";
					
				}else{
					isOk = true;
				}
				
			}else{
				isOk = false;
				msg = "저장중 오류가 발생하였습니다.";
			}
				
			map.put("ISOK", isOk);
			map.put("MSG", msg);
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
	 * 예외처리EmpNo LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionEmpNoList.do")
	private String exceptionEmpNoList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTEMPNO);
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
			HashMap<String,Object> retMap = exceptionService.getExceptionEmpNoList();
			List<ExceptionVO> exceptionEmpNoList = (List<ExceptionVO>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", exceptionEmpNoList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "exception/exceptionEmpNoList";
		
	}
	
	/**
	 * 메인화면 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionEmpNoForm.do")
	private String exceptionEmpNoForm(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTEMPNO);
		
		HashMap<String,Object> retMap = exceptionService.getPolIdxList();
		List<ExceptionVO> polIdxList = (List<ExceptionVO>)retMap.get("list");
		model.addAttribute("polIdxList", polIdxList);
		
		
		ExceptionVO exceptionEmpNoInfo = new ExceptionVO();
		
		if(exceptionVO.getFormMod().equals("U")){
			exceptionEmpNoInfo = exceptionService.getExceptionEmpNoInfo(exceptionVO);
			exceptionEmpNoInfo.setFormMod(exceptionVO.getFormMod());
		}
		
		model.addAttribute("exceptionEmpNoInfo", exceptionEmpNoInfo);
		
		
		return "exception/exceptionEmpNoForm";
		
	}
	
	/**
	 * 예외EmpNo 등록 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionEmpNoSave.do")
	private void exceptionEmpNoSave(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
	    ExceptionVO exceptionEmpNoInfo = new ExceptionVO();
		try
		{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			if(exceptionVO.getFormMod().equals("U")){
				result = exceptionService.exceptionEmpNoDelete(exceptionVO);
			
			}else{
				
				
				exceptionEmpNoInfo = exceptionService.getExceptionEmpNoInfo(exceptionVO);
				if(!exceptionEmpNoInfo.getSec_pol_id().equals(null) && exceptionEmpNoInfo.getSec_pol_id() != "" ){
					isOk = false;
					msg = "이미 존재하는 정책 입니다.";
					result = -1;
					
				}
			}
			
			if(result == 0 && (exceptionEmpNoInfo.getSec_pol_id().equals(null) || exceptionEmpNoInfo.getSec_pol_id() == "")){
				result = exceptionService.exceptionEmpNoSave(exceptionVO);
			
				if(result == -1){
					isOk = false;
					msg = "저장중 오류가 발생하였습니다.";
					
				}else{
					isOk = true;
				}
				
			}else{
				isOk = false;
				msg = "저장중 오류가 발생하였습니다.";
			}
				
			map.put("ISOK", isOk);
			map.put("MSG", msg);
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
	 * 예외 IP 삭제
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionIpDelete.do")
	private void exceptionIpDelete(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false;
	    int result =0;
	    
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	ExceptionVO exceptionInfo = new ExceptionVO();
    		String secPolIdList = exceptionVO.getSecPolIdList();
    		for(String secPolId:secPolIdList.split(",")){
    			exceptionInfo.setSec_pol_id(secPolId);
    			result  = exceptionService.exceptionIpDelete(exceptionInfo);
    			
    			if(result != 0){
    				isOk = false;
    				break;
    			}else{
    				isOk = true;
    			}
    			
	    	}
	    		
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
	 * 예외EmpNo 삭제
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionEmpNoDelete.do")
	private void exceptionEmpNoDelete(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false;
	    int result =0;
	    
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	ExceptionVO exceptionInfo = new ExceptionVO();
    		String secPolIdList = exceptionVO.getSecPolIdList();
    		for(String secPolId:secPolIdList.split(",")){
    			exceptionInfo.setSec_pol_id(secPolId);
    			result  = exceptionService.exceptionEmpNoDelete(exceptionInfo);
    			
    			if(result != 0){
    				isOk = false;
    				break;
    			}else{
    				isOk = true;
    			}
    			
	    	}
	    		
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
	 * 예외IP 정보 조회
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionIpInfo.do")
	private void exceptionIpInfo(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = true;
	    
	    Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			throw new Exception("AUTH ERROR!");
	    }
	    
	    ExceptionVO exceptionIpInfo = exceptionService.getExceptionIpInfo(exceptionVO);
	    
	    map.put("ISOK", isOk);
	    map.put("ipInfo", exceptionIpInfo.getIp());
		map.put("MSG", msg);
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	
	/**
	 * 예외empNo 정보 조회
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionEmpNoInfo.do")
	private void exceptionEmpNoInfo(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = true;
	    
	    Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			throw new Exception("AUTH ERROR!");
	    }
	    
	    ExceptionVO exceptionEmpNoInfo = exceptionService.getExceptionEmpNoInfo(exceptionVO);
	    
	    map.put("ISOK", isOk);
	    map.put("empNoInfo", exceptionEmpNoInfo.getEmp_no());
		map.put("MSG", msg);
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	
	/**
	 * 메일발송 예외자 LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionMailList.do")
	private String exceptionMailList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTMAIL);
		//searchVO.setPageIndex(2);
		
		
		try
		{
			HashMap<String,Object> retMap = exceptionService.getExceptionMailList();
			List<ExceptionVO> exceptionMailList = (List<ExceptionVO>)retMap.get("list");
		
			model.addAttribute("resultList", exceptionMailList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "exception/exceptionMailList";
		
	}
	
	/**
	 * 메일발송 예외자 수정 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionMailForm.do")
	private String exceptionMailForm(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.EXCEPTMAIL);
		

		
		ExceptionVO exceptionMailInfo = new ExceptionVO();
		
		exceptionMailInfo = exceptionService.getExceptionMailInfo(exceptionVO);
		
		model.addAttribute("exceptionMailInfo", exceptionMailInfo);
		
		
		return "exception/exceptionMailForm";
		
	}
	
	/**
	 * 메일발송 예외자  등록 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/exceptionMailSave.do")
	private void exceptionMailSave(HttpServletRequest request,
			HttpServletResponse response,
			ExceptionVO exceptionVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
		try
		{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			result = exceptionService.exceptionMailDelete(exceptionVO);
			
			if(result == 0){
				result = exceptionService.exceptionMailSave(exceptionVO);
			
				if(result == -1){
					isOk = false;
					msg = "저장중 오류가 발생하였습니다.";
					
				}else{
					isOk = true;
					msg = "저장 완료하였습니다.";
				}
				
			}else{
				isOk = false;
				msg = "저장중 오류가 발생하였습니다.";
			}
				
			map.put("ISOK", isOk);
			map.put("MSG", msg);
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
}
