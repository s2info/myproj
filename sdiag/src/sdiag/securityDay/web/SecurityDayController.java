     package sdiag.securityDay.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.FileUploadDto;
import sdiag.com.service.FileVO;
import sdiag.com.service.SdiagProperties;
import sdiag.login.service.UserManageVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.securityDay.service.SdCheckListGroupVO;
import sdiag.securityDay.service.SdQuestionInfoVO;
import sdiag.securityDay.service.SdQuestionMcVO;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.securityDay.service.SdSearchVO;
import sdiag.securityDay.service.SdTargetInfoVO;
import sdiag.securityDay.service.SecurityDayService;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.util.CommonUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.FileManager;
import sdiag.util.LeftMenuInfo;

@Controller
public class SecurityDayController {
	@Resource(name = "SecurityDayService")
	protected SecurityDayService securityDayService;
	
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "propertiesService1")
	protected EgovPropertyService propertiesService1;
	
	// 파일 처리 클래스 선언
	@Resource(name = "FileManager")
	private FileManager fileManager;
	
	/**
	 * 점검표 LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdCheckList.do")
	private String sdCheckList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
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
			HashMap<String,Object> retMap = securityDayService.getSdCheckList(searchVO);
			List<SdCheckListVO> sdCheckList = (List<SdCheckListVO>)retMap.get("list");
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
		return "securityDay/sdCheckList";
		
	}
	
	/**
	 * 점검표 정보 상세보기
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdCheckListForm.do")
	private String sdCheckListForm(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListVO sdCheckListVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		//System.out.println("dfff");
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		
		// 점검표 상세 정보
		SdCheckListVO sdCheckListInfo = new SdCheckListVO();
		
		//문항 정보 LIST
		List<SdQuestionInfoVO> qusetionList = new ArrayList<SdQuestionInfoVO>();
		
		if(searchVO.getFormMod().equals("U")){
			
			sdCheckListInfo = securityDayService.getSdCheckListInfo(sdCheckListVO);
			qusetionList = securityDayService.getQustionList(sdCheckListVO);
		}
		
		
		model.addAttribute("sdCheckListInfo", sdCheckListInfo);
		model.addAttribute("resultList", qusetionList);
		
		
		return "securityDay/sdCheckListForm";
		
	}
	
	
	/**
	 * 점검표 문항 정보 상세보기
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdQuestionForm.do")
	private String sdQuestionForm(HttpServletRequest request,
			HttpServletResponse response,
			SdQuestionInfoVO sdQuestionInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		
		
		SdQuestionInfoVO sdQuestionInfo = new SdQuestionInfoVO();
		List<SdQuestionMcVO> sdQuestionMcList = new ArrayList<SdQuestionMcVO>();
		
		if(searchVO.getqFormMod().equals("U")){
			sdQuestionInfo = securityDayService.getQuestionInfo(sdQuestionInfoVO);
			sdQuestionMcList = securityDayService.getQuestionMcList(sdQuestionInfoVO);
		}else{
			//int questionNum = securityDayService.getQuestionNum();
			//sdQuestionInfo.setQuestionNum(questionNum);
			sdQuestionInfo = sdQuestionInfoVO;
		}
		
		model.addAttribute("sdQuestionInfo", sdQuestionInfo);
		model.addAttribute("sdQuestionMcList", sdQuestionMcList);
		model.addAttribute("exNumCnt", Integer.toString(sdQuestionMcList.size()));
		
		//model.addAttribute("sdCheckListInfo", sdCheckListVO);
		
		return "securityDay/sdQuestionForm";
		
	}
	
	/**
	 * 점검표 정보 저장
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdCheckListInfoSave.do") 
	public void sdCheckListInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListVO sdCheckListVO,
			SdTargetInfoVO sdTargetInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
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
				securityDayService.setSdCheckListInfoUpdate(sdCheckListVO, sdTargetInfoVO);
			}else{
				
				int sdCheckNo = securityDayService.getSdCheckNo();
				sdCheckListVO.setSdCheckNo(sdCheckNo);
				sdTargetInfoVO.setSdCheckNo(sdCheckNo);
				
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				
				sdCheckListVO.setRgdtEmpNo(loginInfo.getUserid());
				
				
				securityDayService.setSdCheckListInfoInsert(sdCheckListVO, sdTargetInfoVO);
			}
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			map.put("sdCheckNo", sdCheckListVO.getSdCheckNo());
			
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
	 * 점검표 문항 정보 저장
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdQuestionInfoSave.do") 
	public void sdQuestionInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListVO sdCheckListVO,
			SdQuestionInfoVO sdQuestionInfoVO,
			SdQuestionMcVO sdQuestionMcVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
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
			
		
			if(searchVO.getqFormMod().equals("U")){
				securityDayService.setSdQuestionInfoUpdate(sdQuestionInfoVO, sdQuestionMcVO);
			}else{
				int questionNum = securityDayService.getQuestionNum(sdCheckListVO.getSdCheckNo());
				sdQuestionInfoVO.setQuestionNum(questionNum);
				
				// 로그이한 사용자 정보
				MemberVO loginInfo = CommonUtil.getMemberInfo();
				sdQuestionInfoVO.setRgdtEmpNo(loginInfo.getUserid());
				
				securityDayService.setSdQuestionInfoInsert(sdQuestionInfoVO, sdQuestionMcVO);
			}
			
			isOk =  true;
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
	 * 문항표 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdQuestionInfoDelete.do") 
	public void sdQuestionInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			//SdCheckListVO sdCheckListVO,
			SdQuestionInfoVO sdQuestionInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
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
			
			//점검표 문항 정보 삭제
			securityDayService.sdQuestionInfoDelete(sdQuestionInfoVO);
			
			
			//점검표 문항 보기 정보 삭제
			SdQuestionMcVO sdQuestionMcVO = new SdQuestionMcVO();
			sdQuestionMcVO.setSdCheckNo(sdQuestionInfoVO.getSdCheckNo());
			sdQuestionMcVO.setQuestionNum(sdQuestionInfoVO.getQuestionNum());
			securityDayService.questionMcDelete(sdQuestionMcVO);
			
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
	 * 점검표 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdCheckListInfoDelete.do") 
	public void sdCheckListInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListVO sdCheckListVO,
			//SdQuestionInfoVO sdQuestionInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    
	    try{
	    	
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
		    String sdCheckNoList = sdCheckListVO.getSdCheckNoList();
			for(String sdCheckNo:sdCheckNoList.split(",")){
				sdCheckListVO.setSdCheckNo(Integer.parseInt(sdCheckNo));
					
					//점검표 정보 삭제
					securityDayService.sdCheckListInfoDelete(sdCheckListVO);
	
					//점검표 문항 정보 삭제
					SdQuestionInfoVO sdQuestionInfoVO = new SdQuestionInfoVO();
					sdQuestionInfoVO.setSdCheckNo(sdCheckListVO.getSdCheckNo());
					securityDayService.sdQuestionInfoDelete(sdQuestionInfoVO);
					
					//점검표 문항 보기 정보 삭제
					SdQuestionMcVO sdQuestionMcVO = new SdQuestionMcVO();
					sdQuestionMcVO.setSdCheckNo(sdCheckListVO.getSdCheckNo());
					securityDayService.questionMcDelete(sdQuestionMcVO);
					
					SdTargetInfoVO sdTargetInfo = new SdTargetInfoVO();
					sdTargetInfo.setSdCheckNo(sdCheckListVO.getSdCheckNo());
					
					//점검표 그룹 매핑 정보 삭제
					securityDayService.sdclGroupMappingInfoDelete(sdTargetInfo);
					
					//점검표 대상 정보 삭제
					securityDayService.sdTargetInfoDelete(sdTargetInfo);
					
					//점검표 결과 정보 삭제
					securityDayService.sdTargetDatailInfoDelete(sdTargetInfo);
					
					//점검표 상세대상 정보 삭제
					securityDayService.sdResultInfoDelete(sdTargetInfo);
					
					//점검표 상세대상 정보 삭제
					securityDayService.sdResultDatailInfoDelete(sdTargetInfo);
					
					
					
					isOk = true;
					
					map.put("ISOK", isOk);
					map.put("MSG", msg);
					
				
			}
	    } catch (Exception e) { 
			e.printStackTrace();
			msg = "삭제 도중 오류가 발생 하였습니다. \n" +e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	/**
	 * EDITOR 파일 업로드 처리
	 * @param request
	 * @param dto
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/securityDay/editorImageUpload.do")
	public String editorImageUpload(HttpServletRequest request, FileUploadDto dto, ModelMap model, HttpServletResponse response) throws Exception{
		
		//System.out.println("파일 업로드][");
		//System.out.println(dto.getCKEditorFuncNum() + "][ CKEditorFuncNum");
		//System.out.println(dto.getImageUrl() + "][ ImageUrl");
		//System.out.println(dto.getNewFilename() + "][ NewFilename");
		//System.out.println(dto.getUpload().getName() + "][ UPload filename");
		String filePath = propertiesService1.getString("SD.FILE.PATH");
		//System.out.println(filePath);
		String contextPath = request.getSession().getServletContext().getContextPath();
		//String contextPath = "http://10.215.47.207:8080";
		//String contextPath = "http://localhost:8080";
		
		String regEx = "(jpg|jpeg|png|gif|bmp|JPG|JPEG|PNG|GIF|BPM)";
		
		File dir = new File(filePath); 
		if(!dir.isDirectory()) { 
			dir.mkdirs(); 
		}
		try
		{
		Map<String, MultipartFile> files = new HashMap<String, MultipartFile>();//request.getFileMap();
		files.put("ckupload", dto.getUpload());
		List<FileVO> fileVo = new ArrayList<FileVO>();
		FileVO fInfo = new FileVO();
		
		
		if(files.size() > 0){
			List<FileVO> retvo = fileManager.fileUploadProcess(files, "", filePath, fileVo);
			
			for(FileVO vo:retvo){
				System.out.println(vo.getEvid_file_name() + "][" + vo.getEvid_file_loc());
			}
			int index =retvo.get(0).getEvid_file_name().lastIndexOf(".");
			String fileExt = retvo.get(0).getEvid_file_name().substring(index + 1);
			
			if(fileExt.matches(regEx)){
				if(retvo.size() > 0){
					fInfo = retvo.get(0);
					
					dto.setNewFilename(fInfo.getEvid_file_name());
					
					//나중에 수정하기(개발용)
					//dto.setImageUrl(contextPath + "//"+"attachfile//question//" + fInfo.getEvid_file_loc());
					//나중에 수정하기(운영용)
					dto.setImageUrl(contextPath + "/"+"attachfile/question/" + fInfo.getEvid_file_loc());
					dto.setResult("true");
				}else{
					fInfo.setEvid_file_name("");
					fInfo.setEvid_file_loc("");
				}
			}else{
				dto.setResult("false");
			}
		
		}
		
		//PrintWriter printWriter = response.getWriter();
		//printWriter.println("<script type='text/javascript'>window.parent.CHEDITOR.tools.callFunction("+ dto.getCKEditorFuncNum() + "," + dto.getImageUrl() + "," + "'이미지업로드  완료....'" +")</script>");
		//printWriter.flush();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(dto.getNewFilename());
		//System.out.println(dto.getImageUrl());
		
		model.addAttribute("dto", dto);
		
		return "cmm/editorImageUpload";
	}
	
	
	/**
	 * 문항 보기 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdQuestionMcDelete.do") 
	public void sdQuestionMcDelete(HttpServletRequest request,
			HttpServletResponse response,
			//SdCheckListVO sdCheckListVO,
			SdQuestionMcVO sdQuestionMcVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
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
			
			//점검표 문항 정보 삭제
			securityDayService.questionMcDelete(sdQuestionMcVO);
			
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
	 * 점검표 대상 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdTargetInfoDelete.do") 
	public void sdTargetInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
		    SdTargetInfoVO sdTargetInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
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
			//점검표 대상 정보 삭제
			securityDayService.sdTargetInfoDelete(sdTargetInfoVO);
			
			//점검표 결과 정보 삭제
			securityDayService.sdTargetDatailInfoDelete(sdTargetInfoVO);
			
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
	 * 점검표 LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdCheckListGroupList.do")
	private String sdCheckListGroupList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLISTGROUP);
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
			HashMap<String,Object> retMap = securityDayService.getSdCheckListGroupList(searchVO);
			List<SdCheckListGroupVO> sdCheckList = (List<SdCheckListGroupVO>)retMap.get("list");
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
		return "securityDay/sdCheckListGroupList";
		
	}
	
	/**
	 * 점검표 정보 상세보기
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdCheckListGroupForm.do")
	private String sdCheckListGroupForm(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListGroupVO sdCheckListGroupVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		//System.out.println("dfff");
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLISTGROUP);
		
		
		// 점검표 상세 정보
		SdCheckListGroupVO sdCheckListGroupInfo = new SdCheckListGroupVO();
		
		//점검표 LIST
		List<SdCheckListVO> sdCheckList = new ArrayList<SdCheckListVO>();
		
		//점검 대상 LIST
		List<SdTargetInfoVO> targetList = new ArrayList<SdTargetInfoVO>();
		
		//그룹 대상 점검표  LIST
		List<SdTargetInfoVO> sdClGroupMappingList = new ArrayList<SdTargetInfoVO>();
		
		
		
		if(searchVO.getFormMod().equals("U")){
			
			sdCheckListGroupInfo = securityDayService.getSdCheckListGroupInfo(sdCheckListGroupVO);
			
			targetList = securityDayService.getSdTargetList(sdCheckListGroupVO);
			
			sdClGroupMappingList = securityDayService.getSdClGroupMappingList(sdCheckListGroupVO);
		}
		sdCheckList = securityDayService.getClList();
		
		
		
		model.addAttribute("sdCheckListGroupInfo", sdCheckListGroupInfo);
		model.addAttribute("sdCheckList", sdCheckList);
		model.addAttribute("targetList", targetList);
		model.addAttribute("sdClGroupMappingList", sdClGroupMappingList);
		
		
		return "securityDay/sdCheckListGroupForm";
		
	}
	
	/**
	 * 점검표 정보 저장
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdCheckListGroupInfoSave.do") 
	public void sdCheckListGroupInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListGroupVO sdCheckListGroupVO,
			SdTargetInfoVO sdTargetInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int clGroupNo = 0;
	    
	    sdCheckListGroupVO.setCheckStartDay(sdCheckListGroupVO.getCheckStartDay().replaceAll("-", ""));
	    sdCheckListGroupVO.setCheckEndDay(sdCheckListGroupVO.getCheckEndDay().replaceAll("-", ""));
	    sdCheckListGroupVO.setIdxStartDay(sdCheckListGroupVO.getIdxStartDay().replaceAll("-", ""));
	    sdCheckListGroupVO.setIdxEndDay(sdCheckListGroupVO.getIdxEndDay().replaceAll("-", ""));
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			// 지수화일 중복 체크
			int idxDayCnt = 0;
			
			idxDayCnt = securityDayService.getIdxDayCnt(sdCheckListGroupVO);
			
			if(idxDayCnt > 0){
				isOk = false;
				msg = "활성화 또는 지수화 기간이  중복됩니다.\n다시 한번 확인 해주세요.";
				map.put("ISOK", isOk);
				map.put("clGroupNo", clGroupNo);
				map.put("MSG", msg);
			}else {
				if(searchVO.getFormMod().equals("U")){
					securityDayService.setSdCheckListGroupInfoUpdate(sdCheckListGroupVO, sdTargetInfoVO);
				}else{
					
					clGroupNo = securityDayService.getClGroupNo();
					sdCheckListGroupVO.setClGroupNo(clGroupNo);
					sdTargetInfoVO.setClGroupNo(clGroupNo);
					
					// 로그이한 사용자 정보
					MemberVO loginInfo = CommonUtil.getMemberInfo();
					
					sdCheckListGroupVO.setRgdtEmpNo(loginInfo.getUserid());
					
					
					securityDayService.setSdCheckListInfoGroupInsert(sdCheckListGroupVO, sdTargetInfoVO);
				}
				isOk = true;
				map.put("ISOK", isOk);
				map.put("clGroupNo", clGroupNo);
				map.put("MSG", msg);
			}
			
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
	 * 점검표 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdCheckListGroupInfoDelete.do") 
	public void sdCheckListGroupInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListGroupVO sdCheckListGroupVO,
			SdTargetInfoVO sdTargetInfoVO,
			//SdQuestionInfoVO sdQuestionInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    
	    String clGroupNoList = sdCheckListGroupVO.getClGroupNoList();
		for(String clGroupNo:clGroupNoList.split(",")){
			SdTargetInfoVO sdTargetInfo = new SdTargetInfoVO();
			sdCheckListGroupVO.setClGroupNo(Integer.parseInt(clGroupNo));
			sdTargetInfo.setClGroupNo(Integer.parseInt(clGroupNo));
		
			try {
				//점검표 그룹 정보 삭제
				securityDayService.sdCheckListGroupInfoDelete(sdCheckListGroupVO);
				
				//점검표 그룹 매핑 정보 삭제
				securityDayService.sdclGroupMappingInfoDelete(sdTargetInfo);
				
				//점검표 대상 정보 삭제
				securityDayService.sdTargetInfoDelete(sdTargetInfo);
				
				//점검표 결과 정보 삭제
				securityDayService.sdTargetDatailInfoDelete(sdTargetInfo);
				
				
				//점검표 상세대상 정보 삭제
				securityDayService.sdResultInfoDelete(sdTargetInfo);
				
				
				//점검표 상세대상 정보 삭제
				securityDayService.sdResultDatailInfoDelete(sdTargetInfo);
				
				
				
				
				// 점검 증적파일 삭제
				File file = new File(propertiesService1.getString("SD.RFILE.PATH")+Integer.toString(sdCheckListGroupVO.getClGroupNo()));
				if(file.exists()){
					if(file.isDirectory()){
						File[] files = file.listFiles();
						for(int i=0; i<files.length; i++){
							if(files[i].delete()){
								isOk = true;
							}else{
								isOk = false;
								msg ="파일 삭제 중 오류가 발생 하였습니다.";
							}
						}
					}
					file.delete();
				}
				
				
				
				isOk = true;
				
				map.put("ISOK", isOk);
				map.put("MSG", msg);
				
			} catch (Exception e) { 
				isOk = false;
				e.printStackTrace();
				msg = "삭제 도중 오류가 발생 하였습니다. \n" +e.toString();
				map.put("ISOK", isOk);
				map.put("MSG", msg);
			}
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	/**
	 * 그룹 대상 점검표 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/GroupClInfoDelete.do") 
	public void GroupClInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			SdTargetInfoVO sdTargetInfoVO,
			//SdQuestionInfoVO sdQuestionInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
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
			
			//그룹 대상 점검표 정보 삭제
			securityDayService.sdclGroupMappingInfoDelete(sdTargetInfoVO);
			
			//점검표 대상 정보 삭제
			securityDayService.sdTargetInfoDelete(sdTargetInfoVO);
			
			//점검표 결과 정보 삭제
			securityDayService.sdTargetDatailInfoDelete(sdTargetInfoVO);

			isOk = true;
			
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = "삭제 도중 오류가 발생 하였습니다. \n" +e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	/**
	 * securityDay 점검 팝업
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/popSecurityDay.do")
	private String popSecurityDay(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		// 로그이한 사용자 정보
		MemberVO loginInfo = CommonUtil.getMemberInfo();
		SdResultInfoVO sdResultInfoVO = new SdResultInfoVO();
		List<SdQuestionMcVO> mcList = new ArrayList<SdQuestionMcVO>();
		SdQuestionInfoVO sdQuestionInfoVO = new SdQuestionInfoVO();
		searchVO.setReqCode("1");
		sdResultInfoVO.setEmpNo(loginInfo.getUserid());
		sdResultInfoVO.setRowNum(1);
		sdResultInfoVO = securityDayService.getSdInfo(sdResultInfoVO);
			
		/*if(sdResultInfoVO.getAnswerType().equals("2")){
			sdQuestionInfoVO.setSdCheckNo(sdResultInfoVO.getSdCheckNo());
			sdQuestionInfoVO.setQuestionNum(sdResultInfoVO.getQuestionNum());
			
			mcList = securityDayService.getQuestionMcList(sdQuestionInfoVO);
		}*/
		
		model.addAttribute("sdResultInfo", sdResultInfoVO);
		model.addAttribute("mcList", mcList);
		
		//if(searchVO.getReqCode().equals("2"))
			return "securityDay/popSecurityDay";
		//else
			//return "securityDay/popSecurityDaySE";
		
	}
	
	
	/**
	 * 점검표 문항 점검 결과 정보 저장
	 * @throws Exception 
	 */
	/*@RequestMapping(value = "/securityDay/sdResultInfoSave.do") 
	public String sdQuestionInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			SdResultInfoVO sdResultInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
			HashMap<String, Object> map = new HashMap<String,Object>();
			String msg = "Error!!";
		    Boolean isOk = false; 
		    int result =0;
			
		    String saveFileName="";
		    String origName = ""; 
		    
			//파일이 저장될 path 설정
			String path = propertiesService1.getString("SD.RFILE.PATH")+Integer.toString(sdResultInfoVO.getClGroupNo());
			Map returnObject = new	HashMap(); 
			
			try { // MultipartHttpServletRequest생성 
				MultipartHttpServletRequest mhsr =	(MultipartHttpServletRequest) request; 
				Iterator iter =	mhsr.getFileNames(); 
				MultipartFile mfile = null;	
				String fieldName = ""; 
				List resultList = new ArrayList();
				
				// 디레토리가 없다면 생성
				File dir = new File(path); 
				if(!dir.isDirectory()) { 
					dir.mkdirs(); 
				} // 값이 나올때까지
				
				while (iter.hasNext()) { 
					fieldName = (String) iter.next();
					//내용을 가져와서 
					mfile = mhsr.getFile(fieldName);
					
					origName = mfile.getOriginalFilename(); //한글꺠짐 방지 
					// 파일명이 없다면
					
					if("".equals(origName)) { continue; } 
					// 파일 명	변경(uuid로 암호화) 
					
					int idx =origName.lastIndexOf('.'); 
					String fileNm =origName.substring(0, idx);
					String ext = origName.substring(origName.lastIndexOf('.'));
					
	//				확장자 String 
					saveFileName = sdResultInfoVO.getEmpNo()+"_"+Integer.toString(sdResultInfoVO.getSdCheckNo())+"_"+Integer.toString(sdResultInfoVO.getQuestionNum())+"_"+fileNm+ext;
					
					
					//설정한 path에 파일저장 
					File serverFile = new File(path	+ File.separator + saveFileName);
					mfile.transferTo(serverFile); 
					Map file = new	HashMap(); 
					file.put("origName", origName);
					file.put("sfile", serverFile); resultList.add(file); 
				}
				
				
				sdResultInfoVO.setFileNm(saveFileName);
				
				securityDayService.setSdResultInfoSave(sdResultInfoVO);
				
				SdResultInfoVO sdResultInfo = new SdResultInfoVO();
				
				sdResultInfo = securityDayService.getSdInfo(sdResultInfoVO.getEmpNo());
				
				if(sdResultInfo == null){
					securityDayService.setSdResultSave(sdResultInfoVO);
					searchVO.setReqCode("3");
				}
				
				
				
			} catch (UnsupportedEncodingException e) { 
				e.printStackTrace();
			}catch (IllegalStateException e) {
				e.printStackTrace(); 
			} catch (IOException e) { 
				e.printStackTrace();
			} 
		
		return "redirect:/securityDay/popSecurityDay.do?reqCode="+searchVO.getReqCode();
	} */
	
	
	
	/**
	 * 그룹 대상 점검표 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdResultInfoSave.do") 
	public void sdResultInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			SdResultInfoVO sdResultInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
		
	    String saveFileName="";
	    String origName = ""; 
	    
		//파일이 저장될 path 설정
		String path = propertiesService1.getString("SD.RFILE.PATH")+Integer.toString(sdResultInfoVO.getClGroupNo());
		Map returnObject = new	HashMap();
			
		try {
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
			if(searchVO.getReqCode().equals("2")){
				
			
				MultipartHttpServletRequest mhsr =	(MultipartHttpServletRequest) request; 
				Iterator iter =	mhsr.getFileNames(); 
				MultipartFile mfile = null;	
				String fieldName = ""; 
				List resultList = new ArrayList();
				
				// 디레토리가 없다면 생성
				File dir = new File(path); 
				if(!dir.isDirectory()) { 
					dir.mkdirs(); 
				} // 값이 나올때까지
				
				while (iter.hasNext()) { 
					fieldName = (String) iter.next();
					//내용을 가져와서 
					mfile = mhsr.getFile(fieldName);
					
					origName = mfile.getOriginalFilename().replaceAll(" ", ""); //한글꺠짐 방지 
					// 파일명이 없다면
					
					if("".equals(origName)) { continue; } 
					// 파일 명	변경(uuid로 암호화) 
					
					int idx =origName.lastIndexOf('.'); 
					String fileNm =origName.substring(0, idx);
					String ext = origName.substring(origName.lastIndexOf('.'));
					
	//				확장자 String 
					saveFileName = sdResultInfoVO.getEmpNo()+"_"+Integer.toString(sdResultInfoVO.getSdCheckNo())+"_"+Integer.toString(sdResultInfoVO.getQuestionNum())+"_"+fileNm+ext;
					
					
					//설정한 path에 파일저장 
					File serverFile = new File(path	+ File.separator + saveFileName);
					mfile.transferTo(serverFile); 
					Map file = new	HashMap(); 
					file.put("origName", origName);
					file.put("sfile", serverFile); resultList.add(file); 
				}
				
				
				sdResultInfoVO.setFileNm(saveFileName);
				
				//if(sdResultInfoVO.getResultYn().equals("Y"))
					securityDayService.deleteSdResultDatailInfo(sdResultInfoVO);
				//else
					securityDayService.setSdResultInfoSave(sdResultInfoVO);
			
			}
			
			SdResultInfoVO sdResultInfo = new SdResultInfoVO();
			SdResultInfoVO sdResultDetailInfo = new SdResultInfoVO();
			int sdInfoTotalCnt = securityDayService.getSdInfoTotalCnt(sdResultInfoVO);
			
			
			if(sdResultInfoVO.getRowNum() == sdInfoTotalCnt+1){
				securityDayService.setSdResultSave(sdResultInfoVO);
				searchVO.setReqCode("3");
			}else{
				
				sdResultInfo = securityDayService.getSdInfo(sdResultInfoVO);
				sdResultDetailInfo = securityDayService.getSdRDetailInfo(sdResultInfo);
				
				
				if(sdResultDetailInfo == null){
					sdResultDetailInfo = new SdResultInfoVO();
					sdResultDetailInfo.setResultYn("N");
				}
				searchVO.setReqCode("2");
				
				if(sdResultInfo.getAnswerType().equals("2")){
					SdQuestionInfoVO sdQuestionInfoVO = new SdQuestionInfoVO();
					sdQuestionInfoVO.setSdCheckNo(sdResultInfo.getSdCheckNo());
					sdQuestionInfoVO.setQuestionNum(sdResultInfo.getQuestionNum());
					
					List<SdQuestionMcVO> mcList = new ArrayList<SdQuestionMcVO>();
					mcList = securityDayService.getQuestionMcList(sdQuestionInfoVO);
					
					map.put("mcList", mcList);
				}
			}
			
			
			
			
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			map.put("sdResultInfo", sdResultInfo);
			map.put("sdResultDetailInfo", sdResultDetailInfo);
			map.put("searchVO", searchVO);
			
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = "삭제 도중 오류가 발생 하였습니다. \n" +e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	

	/**
	 * 그룹 대상 점검표 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/securityDay/sdBackInfo.do") 
	public void sdResultInfo(HttpServletRequest request,
			HttpServletResponse response,
			SdResultInfoVO sdResultInfoVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
	    SdResultInfoVO sdResultInfo = new SdResultInfoVO();
		SdResultInfoVO sdResultDetailInfo = new SdResultInfoVO();
		
		sdResultInfo = securityDayService.getSdInfo(sdResultInfoVO);
		sdResultDetailInfo = securityDayService.getSdRDetailInfo(sdResultInfo);
		

		if(sdResultDetailInfo == null){
			sdResultDetailInfo = new SdResultInfoVO();
			sdResultDetailInfo.setResultYn("N");
		}
		
		searchVO.setReqCode("2");
		
		if(sdResultInfo.getAnswerType().equals("2")){
			SdQuestionInfoVO sdQuestionInfoVO = new SdQuestionInfoVO();
			sdQuestionInfoVO.setSdCheckNo(sdResultInfo.getSdCheckNo());
			sdQuestionInfoVO.setQuestionNum(sdResultInfo.getQuestionNum());
			
			List<SdQuestionMcVO> mcList = new ArrayList<SdQuestionMcVO>();
			mcList = securityDayService.getQuestionMcList(sdQuestionInfoVO);
			
			map.put("mcList", mcList);
		}
			
		isOk = true;
		map.put("ISOK", isOk);
		map.put("MSG", msg);
		map.put("sdResultInfo", sdResultInfo);
		map.put("sdResultDetailInfo", sdResultDetailInfo);
		map.put("searchVO", searchVO);
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	
	/**
	 * securityDay 점검 팝업
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/popSdPreView.do")
	private String popSdPreView(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		SdResultInfoVO sdResultInfoVO = new SdResultInfoVO();
		List<SdQuestionMcVO> mcList = new ArrayList<SdQuestionMcVO>();
		SdQuestionInfoVO sdQuestionInfoVO = new SdQuestionInfoVO();
			
		
		if(searchVO.getRowNum() == 0)
			searchVO.setRowNum(1);
		
		sdResultInfoVO = securityDayService.getSdPreView(searchVO);
		
		
		int sdInfoTotalCnt = securityDayService.getSdPreViewTotalCnt(searchVO);
		
		if(sdResultInfoVO == null  && !searchVO.getReqCode().isEmpty()){
			searchVO.setReqCode("3");
		}else if(sdResultInfoVO == null && searchVO.getReqCode().isEmpty()){
			searchVO.setReqCode("1");
		}else{
			searchVO.setReqCode("2");
			if(sdResultInfoVO.getAnswerType().equals("2")){
				sdQuestionInfoVO.setSdCheckNo(sdResultInfoVO.getSdCheckNo());
				sdQuestionInfoVO.setQuestionNum(sdResultInfoVO.getQuestionNum());
				
				mcList = securityDayService.getQuestionMcList(sdQuestionInfoVO);
			}
		}
		
		
		model.addAttribute("sdResultInfo", sdResultInfoVO);
		model.addAttribute("mcList", mcList);
		
		//if(searchVO.getReqCode().equals("2"))
			return "securityDay/popSdPreView";
		//else
			//return "securityDay/popSecurityDaySE";
		
	}
	
	/**
	 * 점검표 정보 상세보기
	 * @throws Exception 
	 */
	@RequestMapping("/securityDay/sdResultInfo.do")
	private String sdResultInfo(HttpServletRequest request,
			HttpServletResponse response,
			SdCheckListGroupVO sdCheckListGroupVO,
			@ModelAttribute("searchVO") SdSearchVO searchVO,
			ModelMap model) throws Exception{
		
		//System.out.println("dfff");
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLISTGROUP);
		// 점검표 상세 정보
		SdCheckListGroupVO sdCheckListGroupInfo = new SdCheckListGroupVO();
			
		//점검 대상 LIST
		List<EgovMap> sdResultList = new ArrayList<EgovMap>();
		
		
		if(searchVO.getsUseYn() != null && searchVO.getsUseYn() != "" && searchVO.getSearchKeyword() != null && searchVO.getSearchKeyword() !=""){
				
			//그룹 대상 점검표  LIST
			//List<SdTargetInfoVO> sdClGroupMappingList = new ArrayList<SdTargetInfoVO>();
			
			
			sdCheckListGroupInfo = securityDayService.getSdCheckListGroupInfo(sdCheckListGroupVO);
			
			sdResultList = securityDayService.getSdResultInfo(searchVO);
		}
			
			//sdClGroupMappingList = securityDayService.getSdClGroupMappingList(sdCheckListGroupVO);
		
		
		
		model.addAttribute("sdCheckListGroupInfo", sdCheckListGroupInfo);
		model.addAttribute("sdResultList", sdResultList);
		//model.addAttribute("sdClGroupMappingList", sdClGroupMappingList);
		
		
		return "securityDay/sdResultInfo";
		
	}
	
	
	/**
	 * <pre>
	 * 파일 다운로드
	 * </pre>
	 * 
	 */
	@RequestMapping("/securityDay/fileDown.do")
    public void fileDown(HttpServletRequest request,	HttpServletResponse response, SdResultInfoVO sdResultInfoVO) throws IOException {
		
		
		
		//String fileDispName =  new String (request.getParameter("fileNm").getBytes("utf-8"), "ISO-8859-1");
		
		String fileDispName = securityDayService.getFileNm(sdResultInfoVO);
		String path = propertiesService1.getString("SD.RFILE.PATH")+Integer.toString(sdResultInfoVO.getClGroupNo())+ File.separator +fileDispName;
		
		File file = new File(path);
    	
		byte[] b = new byte[1024]; //buffer size
				
		String header = getBrowser(request);
		
		if (header.contains("MSIE")) {
	       String docName = URLEncoder.encode(fileDispName,"UTF-8").replaceAll("\\+", " ");
	       response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
		} else if (header.contains("Firefox")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Opera")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Chrome")) {
	       String docName = new String(fileDispName.getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		}
		
		response.setHeader("Content-Type", "application/octet-stream");
		//response.setContentLength(Integer.parseInt(vo.getFileSize()));
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
	
		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
		    outs = new BufferedOutputStream(response.getOutputStream());
		    int read = 0;
	
			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
		    if (outs != null) {
				try {
					outs.flush();
				    outs.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
		    if (fin != null) {
				try {
				    fin.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
		}
    }
	
	 /**
     * 브라우저 알아내기
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String header =request.getHeader("User-Agent");
                
        if (header.contains("Firefox")) {
               return "Firefox";
        } else if(header.contains("Chrome")) {
               return "Chrome";
        } else if(header.contains("Opera")) {
               return "Opera";
        } else if(header.contains("Safari")) {
            return "Chrome";
        }
        return "MSIE";
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
				try{
				contents += row.get(key).toString()+ ",";
				}catch(NullPointerException ex){
					contents += "   ,";
				}
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
	@RequestMapping(value="/securityDay/exportexcel.do")
	public void exportexcel(HttpServletRequest request,
			SdCheckListGroupVO sdCheckListGroupVO
								, @ModelAttribute("searchVO") SdSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "";
		
		List<HashMap<String, Object>> list = null;
		
		// 점검표 상세 정보
		SdCheckListGroupVO sdCheckListGroupInfo = new SdCheckListGroupVO();
			
		sdCheckListGroupInfo = securityDayService.getSdCheckListGroupInfo(sdCheckListGroupVO);
		
		list = securityDayService.getExcelSdResultInfo(searchVO);
		fileName = String.format("%s_점검결과", sdCheckListGroupInfo.getClGroupNm());
		String FileName = URLEncoder.encode(fileName, "UTF-8").replace("\\", "%20");
		//ExcelUtil.stringToExportCsv(response, setContent(list), FileName);
		
		List<String[]> contents = setCSVContents(list);
		
		ExcelUtil.hashMapToExportCsv(response, FileName, contents);
		
		/*try{
			String fileName = "";
			
			List<HashMap<String, Object>> list = null;
			
			// 점검표 상세 정보
			SdCheckListGroupVO sdCheckListGroupInfo = new SdCheckListGroupVO();
				
			sdCheckListGroupInfo = securityDayService.getSdCheckListGroupInfo(sdCheckListGroupVO);
			
			
			fileName = String.format("%s_점검결과", sdCheckListGroupInfo.getClGroupNm());
			String FileName = URLEncoder.encode(fileName, "UTF-8").replace("\\", "%20");
			
			ExcelInitVO excelVO = new ExcelInitVO();
			excelVO.setFileName(fileName);
			excelVO.setSheetName("점검결과");
			excelVO.setTitle("점검결과");
			excelVO.setHeadVal("");
			excelVO.setType("csv");
			
			List<String> rowData = CommonUtil.SplitToString(searchVO.getBodyData(), ",");
			List<String> head = new ArrayList<String>();
			//head.add("조직 리스트");
			
			List<String> colData = CommonUtil.SplitToString(rowData.get(0), "\\|");
			for(int i=0 ; i < colData.size(); i++){
				head.add(colData.get(i));
			}
			excelVO.setHead(head);
			List<EgovMap> excellist = new ArrayList<EgovMap>();
			for(int i=1 ; i < rowData.size() ; i++){
				List<String> columnData = CommonUtil.SplitToString(rowData.get(i), "\\|");
				EgovMap map = new EgovMap();
				for(int j=0 ; j < columnData.size(); j++){
					map.put(head.get(j), columnData.get(j));
				}
				excellist.add(map);
			}
			ExcelUtil.xssExcelDown(response, excelVO, excellist);
			
			}catch(Exception e){e.printStackTrace();}*/

	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "이름";break;
		case "posn_nm" : ColumnName = "부서";break;
		case "checklist_nm" :  ColumnName = "점검표명";break;
		case "question_num" : ColumnName = "문항 번호";break;
		case "answer" : ColumnName = "답변";break;
		case "file_nm" : ColumnName = "파일 명";break;
		case "check_yn" : ColumnName = "점검여부";break;
		case "check_date" : ColumnName = "점검일";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	
	
	
}
