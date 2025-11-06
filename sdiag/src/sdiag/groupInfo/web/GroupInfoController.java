package sdiag.groupInfo.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;



















import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.exception.service.ExceptionService;
import sdiag.exception.service.ExceptionVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoService;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.groupInfo.service.GroupSearchVO;
import sdiag.login.service.UserManageVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.util.CommonUtil;
import sdiag.util.LeftMenuInfo;

@Controller
public class GroupInfoController {
	@Resource(name = "GroupInfoService")
	protected GroupInfoService groupInfoService;
	
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	@Resource(name = "ExceptionService")
	protected ExceptionService exceptionService;
	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "propertiesService1")
	protected EgovPropertyService propertiesService1;
	
	
	/**
	 * 그룹과리 LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/group/groupInfoList.do")
	private String groupInfoList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.GROUPINFO);
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
			HashMap<String,Object> retMap = groupInfoService.getGroupInfoList(searchVO);
			List<GroupInfoVO> sdCheckList = (List<GroupInfoVO>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", sdCheckList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "group/groupInfoList";
		
	}
	
	/**
	 * 그룹 정보 상세보기
	 * @throws Exception 
	 */
	@RequestMapping("/group/groupInfoForm.do")
	private String sdCheckListForm(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			GroupInfoVO groupInfoVO,
			GroupDetailInfoVO groupDetailInfoVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.GROUPINFO);
		
		GroupInfoVO groupInfo = new GroupInfoVO();
		List<GroupDetailInfoVO> groupDetailInfoList = new ArrayList<GroupDetailInfoVO>();
		
		List<CodeInfoVO> selecBoxList = new ArrayList<CodeInfoVO>();
		
		if(searchVO.getFormMod().equals("U")){
			groupInfo = groupInfoService.getGroupInfo(groupInfoVO);
			groupDetailInfoList = groupInfoService.getGroupDetailInfoList(groupInfoVO);
			
			if(groupInfo.getGroupType().equals("2"))
				selecBoxList =  groupInfoService.getPolIdxList();
			else if(groupInfo.getGroupType().equals("3"))
				selecBoxList = comService.getCodeInfoListNoTitle("KPC");
			else if(groupInfo.getGroupType().equals("4"))
				selecBoxList = comService.getCodeInfoListNoTitle("PCI");
			
		}
		
		model.addAttribute("selecBoxList", selecBoxList);
		
		model.addAttribute("groupInfo", groupInfo);
		model.addAttribute("resultList", groupDetailInfoList);
		
		return "group/groupInfoForm";
		
	}
	
	/**
	 * 조직/개인 검색 팝업
	 * @throws Exception 
	 */
	@RequestMapping("/group/popOrgInfoSearch.do")
	private String popOrgInfoSearch(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
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
			HashMap<String,Object> retMap = null;
			List<GroupDetailInfoVO> orgInfoList = new ArrayList<GroupDetailInfoVO>();
			int totalCnt = 0;
			
			if(!searchVO.getSearchCondition().isEmpty() && searchVO.getSearchCondition() !=""){
				if(searchVO.getSearchCondition().equals("1") || searchVO.getSearchCondition().equals("2"))
					retMap = groupInfoService.getOrgUserInfoList(searchVO);
				else if(searchVO.getSearchCondition().equals("3") || searchVO.getSearchCondition().equals("4"))
					retMap = groupInfoService.getOrgGroupInfoList(searchVO);
				else if(searchVO.getSearchCondition().equals("5"))
					retMap = groupInfoService.getPopGroupInfoList(searchVO);
				
				orgInfoList = (List<GroupDetailInfoVO>)retMap.get("list");
				totalCnt = (int)retMap.get("totalCount");
			}
			
			model.addAttribute("resultList", orgInfoList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "group/popOrgInfoSearch";
		
	}
	
	/**
	 * 그룹 코드 중복 체크
	 * @throws Exception 
	 */
	@RequestMapping(value = "/group/groupCodeCheck.do") 
	public void sdQuestionInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			GroupInfoVO groupInfoVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = true; 
	    Boolean result = true;
	    
	    //sdQuestionInfoVO = groupInfoService.getQustionInfo(sdQuestionInfoVO);
		
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			int check = groupInfoService.groupCodeCheck(groupInfoVO);
			
			if(check == 0){
				result = true;
			}else{
				result = false;
			}
			
			map.put("result", result);
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		} catch (Exception e) { 
			isOk = false;
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
	 * 그룹 정보 저장
	 * @throws Exception 
	 */
	@RequestMapping(value = "/group/groupInfoSave.do") 
	public void groupInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			GroupInfoVO groupInfoVO,
			GroupDetailInfoVO groupDetailInfoVO,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
		
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			if(searchVO.getFormMod().equals("U")){
				
				groupInfoService.setGroupInfoUpdate(groupInfoVO);
				
			}else{
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				groupInfoVO.setRgdtEmpNo(loginInfo.getUserid());
				groupDetailInfoVO.setRgdtEmpNo(loginInfo.getUserid());
				
				int groupCode = groupInfoService.getGroupCode();
				
				groupInfoVO.setGroupCode(groupCode);
				groupDetailInfoVO.setGroupCode(groupCode);
				groupInfoService.setGroupInfoInsert(groupInfoVO);
			}
			
			if(groupDetailInfoVO.getOrgInfo() != null && groupDetailInfoVO.getOrgNm() != null && groupDetailInfoVO.getOrgType() != null){
				groupInfoService.setGroupDetailInfoInsert(groupDetailInfoVO);
			}
			
			if(groupInfoVO.getGroupType().equals("5") && groupDetailInfoVO.getEmpNoList() !=null)
				groupInfoService.setPassiveGroupInsert(groupDetailInfoVO);
			
			if(groupInfoVO.getGroupType().equals("7") && groupDetailInfoVO.getQueryStr() !=null)
				groupInfoService.setQueryStrInsert(groupDetailInfoVO);
			
			
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
	 * 그룹 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/group/groupInfoDelete.do") 
	public void groupInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			GroupInfoVO groupInfoVO,
			GroupDetailInfoVO groupDetailInfoVO,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
		
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			String groupCodeList = groupInfoVO.getGroupCodeList();
			for(String groupCode:groupCodeList.split(",")){
				GroupInfoVO groupInfo = new GroupInfoVO();
				GroupDetailInfoVO groupDetailInfo = new GroupDetailInfoVO();
				
				groupInfo.setGroupCode(Integer.parseInt(groupCode));
				groupDetailInfo.setGroupCode(Integer.parseInt(groupCode));
				
				groupInfoService.groupInfoDelete(groupInfo);
				groupInfoService.groupDetailInfoDelete(groupDetailInfo);
				
				
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
	 * 그룹 상세 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/group/groupDetailInfoDelete.do") 
	public void groupDetailInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			GroupDetailInfoVO groupDetailInfoVO,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
		
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			groupInfoService.groupDetailInfoDelete(groupDetailInfoVO);
			
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
	 * 그룹 상세 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/group/getSelectBoxList.do") 
	public void getSelectBoxList(HttpServletRequest request,
			HttpServletResponse response,
			GroupInfoVO groupInfoVO,
			@ModelAttribute("searchVO") GroupSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
		
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			List<CodeInfoVO> selecBoxList = new ArrayList<CodeInfoVO>();
			
			if(groupInfoVO.getGroupType().equals("2"))
				selecBoxList =  groupInfoService.getPolIdxList();
			else if(groupInfoVO.getGroupType().equals("3"))
				selecBoxList = comService.getCodeInfoListNoTitle("KPC");
			else if(groupInfoVO.getGroupType().equals("4"))
				selecBoxList = comService.getCodeInfoListNoTitle("PCI");
			
			isOk = true;
			
			
			map.put("selecBoxList", selecBoxList);
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
