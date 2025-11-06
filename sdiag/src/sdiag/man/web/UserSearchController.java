package sdiag.man.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.board.service.NoticeService;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserSearchService;
import sdiag.man.service.UserSearchInfoVO;
import sdiag.man.service.UserService;
import sdiag.man.service.UserinfoVO;
import sdiag.member.service.MemberVO;
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
public class UserSearchController {

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "UserSearchService")
	private UserSearchService userSearchService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	

	/**
	 *  사용자 정보 조회 조회
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/userSearchList.do")
	public String userSearchList(HttpServletRequest request, 
			@ModelAttribute("searchVO") SearchVO searchVO, 
			ModelMap model) throws Exception{	
	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.USERSEARCH, comService);
		//searchVO.setPageIndex(2);
		
		List<UserSearchInfoVO>  userList = new ArrayList<UserSearchInfoVO>();
		List<UserSearchInfoVO> userPolList = new ArrayList<UserSearchInfoVO>();
		List<UserSearchInfoVO> userExPolList = new ArrayList<UserSearchInfoVO>();
		List<UserSearchInfoVO> userGroupList = new ArrayList<UserSearchInfoVO>();
		List<String> orgGroupList = new ArrayList<String>();
		
		if(!searchVO.getSearchKeyword().isEmpty() && searchVO.getSearchKeyword() != null && searchVO.getSearchKeyword() !=""){
			if(searchVO.getSearchCondition().equals("2")){
				userList = userSearchService.getUserList(searchVO);
			}else{
				userPolList = userSearchService.getUserPolList(searchVO);
				userExPolList = userSearchService.getUserExPolList(searchVO);
				
				EgovMap userGroup = userSearchService.getUserGroup(searchVO);
				
				orgGroupList.add(searchVO.getSearchKeyword());
				
				if(userGroup != null){
					for(int i = 0; i<8; i++){
						String orgCode = (String) userGroup.get("orgCode"+i);
						if(!orgCode.isEmpty()){
							orgGroupList.add(orgCode);
						}
					}
				}
				
				Map<String, Object> pMap = new HashMap<String, Object>();
				pMap.put("orgGroupList", orgGroupList);
				
				userGroupList = userSearchService.getUserGroupList(pMap);
			}
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("userPolList", userPolList);
		model.addAttribute("userExPolList", userExPolList);
		model.addAttribute("userGroupList", userGroupList);
		
		
		return "man/userSearch/userSearchList";
	}
	
	
	/**
	 *  사용자 정보 조회 조회
	 * @param request
	 * @param userSeachInfoVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/polEx.do") 
	public void polEx(HttpServletRequest request,
			HttpServletResponse response,
			UserSearchInfoVO userSeachInfoVO) throws Exception{
	
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
			
			userSearchService.setPolExInsert(userSeachInfoVO);
			
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
	 *  사용자 정보 조회 조회
	 * @param request
	 * @param userSeachInfoVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/polExDelete.do") 
	public void polExDelete(HttpServletRequest request,
			HttpServletResponse response,
			UserSearchInfoVO userSeachInfoVO) throws Exception{
	
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
			
			userSearchService.setPolExDelete(userSeachInfoVO);
			
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
	 *  사용자 정보 조회 조회
	 * @param request
	 * @param userSeachInfoVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/groupDelete.do") 
	public void groupDelete(HttpServletRequest request,
			HttpServletResponse response,
			UserSearchInfoVO userSeachInfoVO) throws Exception{
	
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
			
			userSearchService.setGroupDelete(userSeachInfoVO);
			
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
}
