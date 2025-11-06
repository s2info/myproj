package sdiag.man.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.sun.corba.se.spi.orbutil.fsm.StateImpl;

import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.MenuItemVO;
import sdiag.exanal.web.ProcessSheetInterface;
import sdiag.exanal.web.ReadExcelFile;
import sdiag.exception.service.ReqExIpInfoService;
import sdiag.exception.service.ReqExIpPolicyInfoVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.man.service.OrgStatisticService;
import sdiag.man.vo.MailTargetInfoVO;
import sdiag.man.vo.OrgStatisticSearchVO;
import sdiag.man.vo.StatInfoVO;
import sdiag.member.service.MemberVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.securityDay.service.SdQuestionInfoVO;
import sdiag.securityDay.service.SdQuestionMcVO;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.securityDay.service.SdSearchVO;
import sdiag.util.CommonUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.MajrCodeInfo;
import sun.swing.plaf.synth.DefaultSynthStyle.StateInfo;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class OrgStatisticController {
	@Resource(name= "commonService")
	private CommonService comService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name="OrgStatisticService")
	private OrgStatisticService orgStatService;
	
	@Resource(name = "ReqExIpInfoService")
	protected ReqExIpInfoService reqExIpInfoService;
	
	/**
	 * 기관별 통계
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/statInfoList.do")
	public String statInfoList(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") OrgStatisticSearchVO searchVO
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STAT, comService);	
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		try
		{
			HashMap<String,Object> retMap = orgStatService.getStatInfoList(searchVO);
			List<EgovMap> statInfoList = (List<EgovMap>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", statInfoList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "man/orgStat/orgStatInfoList";
	}
	
	
	/**
	 * 기관별 통계 정책 설정 화면
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/orgStatPolForm.do")
	public String orgStatPolForm(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") OrgStatisticSearchVO searchVO
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STAT, comService);	
		
		
		List<EgovMap> polList = orgStatService.getPolList();
		
		model.addAttribute("polList", polList);
		
		int polCnt = 1;
		
		List<StatInfoVO> statPolList = new ArrayList<StatInfoVO>();
		StatInfoVO statInfo = new StatInfoVO();
		
		if(searchVO.getFormMod().equals("U")){
			statPolList = orgStatService.getStatPolList(searchVO);
			statInfo = orgStatService.getStatInfo(searchVO);
			polCnt = orgStatService.getUpPolCnt(searchVO);
		}
		//orgStat.getUpPolCnt
		
		model.addAttribute("polCnt", polCnt);
		model.addAttribute("statPolList", statPolList);
		model.addAttribute("statInfo", statInfo);
		
		
		return "man/orgStat/orgStatPolForm";
	}
	
	/**
	 * 기관별 통계 정책 설정 화면
	 * @param 통계 정보 업데이트
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/statInfoSave.do") 
	public void statInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") OrgStatisticSearchVO searchVO,
			StatInfoVO statInfoVo
			) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    Boolean isUp = false;
	    int result =0;
		
		try{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
			// 로그이한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			statInfoVo.setRgdtEmpNo(loginInfo.getUserid());
			
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			
			isUp = statInfoSave (statInfoVo, searchVO, multiRequest);
			if(!isUp)
				msg = "수동정책 점수 업로드 중 오류가 발생하였습니다.";
				
			if(searchVO.getFormMod().equals("I")){
				searchVO.setFormMod("U");
				searchVO.setStatSeq(statInfoVo.getStatSeq());
			}
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("ISUP", isUp);
			map.put("MSG", msg);
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
	 * 기관별 통계 정보 저장
	 * @param 통계 정보 업데이트
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/orgStatInfoSave.do") 
	public void orgStatInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") OrgStatisticSearchVO searchVO,
			StatInfoVO statInfoVo
			) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    Boolean isUp = false;
	    int result =0;
		
		try{
			
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			
			// 로그이한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			statInfoVo.setRgdtEmpNo(loginInfo.getUserid());
			
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			
			isUp = statInfoSave (statInfoVo, searchVO,multiRequest);
			if(!isUp)
				msg = "수동정책 점수 업로드 중 오류가 발생하였습니다.";
			
			orgStatService.setStatUploadScoreDelete(statInfoVo);
			
			orgStatService.setUploadScore(statInfoVo);
			
			List<StatInfoVO> polIdxList = new ArrayList<StatInfoVO>();
			List<StatInfoVO> upPolIdxList = new ArrayList<StatInfoVO>();
			
			polIdxList = orgStatService.getPolIdxList(statInfoVo);
			upPolIdxList = orgStatService.getUpPolIdxList(statInfoVo);
			
			
			
			if(polIdxList.size() == 0 && upPolIdxList.size() == 0){
				isOk = false;
				
				msg = "통계 산정에 대한 점수 데이터가 존재하지 않습니다.";
			}else{
				statInfoVo.setStatPolList(polIdxList);
				statInfoVo.setStatUpPolList(upPolIdxList);
				orgStatService.setTotalStatScore(statInfoVo);
				
				isOk = true;
				
				
			}
			
			
			
			
			map.put("ISOK", isOk);
			map.put("ISUP", isUp);
			map.put("MSG", msg);
			map.put("searchVO", searchVO);
			
			
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = "처리 도중 오류가 발생 하였습니다. \n" +e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	} 
	
	/**
	 * 기관별 통계 정책 설정 화면
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/orgStatView.do")
	public String orgStatView(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") OrgStatisticSearchVO searchVO
			,StatInfoVO statInfoVo
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STAT, comService);	
		
		List<StatInfoVO> polIdxList = new ArrayList<StatInfoVO>();
		List<StatInfoVO> upPolIdxList = new ArrayList<StatInfoVO>();
		
		polIdxList = orgStatService.getPolIdxList(statInfoVo);
		upPolIdxList = orgStatService.getUpPolIdxList(statInfoVo);
		
		StatInfoVO orgStat = new StatInfoVO();
		
		orgStat.setStatSeq(statInfoVo.getStatSeq());
		orgStat.setOrgLevel(statInfoVo.getOrgLevel());
		orgStat.setStatPolList(polIdxList);
		orgStat.setStatUpPolList(upPolIdxList);
		
		List<EgovMap> orgStatInfo = orgStatService.getOrgStatInfo(orgStat);
		
		StatInfoVO statInfo = new StatInfoVO();
		
		statInfo = orgStatService.getStatInfo(searchVO);
		statInfo.setOrgLevel(statInfoVo.getOrgLevel());
		
		model.addAttribute("orgStatInfo", orgStatInfo);
		model.addAttribute("statInfo", statInfo);
		model.addAttribute("polIdxList", polIdxList);
		model.addAttribute("upPolIdxList", upPolIdxList);
		
		
		
		String trList = "";
		StringBuffer str = new StringBuffer();
		
		int width = 20+(10*upPolIdxList.size())+(10*polIdxList.size());
		
		model.addAttribute("width", width);
        
        for(EgovMap pow:orgStatInfo){
        	str.append("<tr>");
        	str.append(String.format("<td style='text-align: left;'>%s</td>", pow.get("orgNm")));
        	str.append(String.format("<td>%s</td>", pow.get("totalScore")));
        	str.append(String.format("<td>%s</td>", pow.get("totalAvg")));
        	for(StatInfoVO statinfo:polIdxList){
        		String value = (String) pow.get(statinfo.getPolIdxId().toString().toLowerCase()+statinfo.getSumRgdtDate().toString().toLowerCase());
        		
        		if(value.equals("-")){
        			str.append(String.format("<td>%s</td>", value));
        		}else{
        			str.append(String.format("<td>%s</td>", value));
        		}
        	}
        	for(StatInfoVO statUpinfo:upPolIdxList){
        		String value = (String)pow.get(statUpinfo.getPolIdxId().toString().toLowerCase()+statUpinfo.getSumRgdtDate().toString().toLowerCase());
				if(value.equals("-")){
        			str.append(String.format("<td>%s</td>", value));
        		}else{
        			str.append(String.format("<td>%s</td>", value));
        		}
        	}
        

        	str.append("</tr>");
        }
        
        model.addAttribute("trList", str.toString());		
						
		
		return "man/orgStat/orgStatView";
	}
	
	/**
	 * 기관별 통계 정보 저장
	 * @param 통계 정보 업데이트
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/man/orgStatInfoDelete.do") 
	public void orgStatInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") OrgStatisticSearchVO searchVO,
			StatInfoVO statInfoVo
			) throws Exception{
	
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리 중 오류가 발생하였습니다.";
	    Boolean isOk = false; 
	    int result =0;
		
		try{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			String statSeqList = statInfoVo.getStatSeqList();
			for(String statSeq:statSeqList.split(",")){
				StatInfoVO statInfo = new StatInfoVO();
				
				
				statInfo.setStatSeq(Integer.parseInt(statSeq));
				
				orgStatService.orgStatInfoDelete(statInfo);
				
				
			}
			
			isOk = true;
			map.put("ISOK", isOk);
			
			map.put("MSG", msg);
			map.put("searchVO", searchVO);
			
			
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = "처리 중 오류가 발생하였습니다. \n";
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	}
	
	public Boolean statInfoSave(StatInfoVO statInfoVo, OrgStatisticSearchVO searchVO, MultipartHttpServletRequest multiRequest){
		Boolean isUp = false;
		
		List<StatInfoVO> statPolList = new ArrayList<StatInfoVO>();
		if(!statInfoVo.getPolIdxList().equals("")){
			String[] polList = statInfoVo.getPolIdxList().split(",");
			for(String polInfo:polList){
				StatInfoVO statPolInfo = new StatInfoVO();
				//targetCode + "|" +targetType + "|" +targetNm
				String[] polInfos = polInfo.split("\\|");
				statPolInfo.setPolIdxId(polInfos[0]);
				statPolInfo.setPolIdxNm(polInfos[1]);
				statPolInfo.setSumRgdtDate(polInfos[2].replaceAll("-", ""));
				statPolInfo.setGubun(polInfos[3]);
				statPolInfo.setPolWeightValue(Integer.parseInt(polInfos[4]));
				statPolList.add(statPolInfo);
			}
			statInfoVo.setStatPolList(statPolList);
		}
		
		List<StatInfoVO> statUpPolList = new ArrayList<StatInfoVO>();
		if(!statInfoVo.getUpPolList().equals("")){
			String[] upPolList = statInfoVo.getUpPolList().split(",");
			for(String upPolInfo:upPolList){
				StatInfoVO statUpPolInfo = new StatInfoVO();
				//targetCode + "|" +targetType + "|" +targetNm
				String[] upPolInfos = upPolInfo.split("\\|");
				statUpPolInfo.setPolIdxId(upPolInfos[0]);
				statUpPolInfo.setPolIdxNm(upPolInfos[1]);
				statUpPolInfo.setExcelFile(upPolInfos[2]);
				statUpPolInfo.setGubun(upPolInfos[3]);
				statUpPolInfo.setPolWeightValue(Integer.parseInt(upPolInfos[4]));
				statUpPolList.add(statUpPolInfo);
				statPolList.add(statUpPolInfo);
			}
			
			
		}
		
		statInfoVo.setStatPolList(statPolList);
		statInfoVo.setStatUpPolList(statUpPolList);
		
		try{
			if(searchVO.getFormMod().equals("U")){
				orgStatService.statInfoUpdate(statInfoVo);
				isUp = true;
			}else{
				//통계 seq 조회
				int statSeq = orgStatService.getStatSeq();
				
				statInfoVo.setStatSeq(statSeq);
				
				orgStatService.statInfoInsert(statInfoVo);
				isUp = true;
			}
			
			
			
			if(statInfoVo.getStatUpPolList() != null){
				for(StatInfoVO statUpPolInfo :  statInfoVo.getStatUpPolList()){
					HashMap<String,Object> rMap = new HashMap<String,Object>();
					rMap.put("polIdxId", statUpPolInfo.getPolIdxId());
					rMap.put("rgdtEmpNo", statInfoVo.getRgdtEmpNo());
					rMap.put("statSeq", statInfoVo.getStatSeq());
					
					orgStatService.statUploadScoreDelete(rMap);
					
					statUpPolInfo.setStatSeq(statInfoVo.getStatSeq());
					statUpPolInfo.setRgdtEmpNo(statInfoVo.getRgdtEmpNo());
					//statUpPolInfo.setExcelFile(statUpPolInfo.getExcelFile().replaceAll("\", "\\"));
					
						    	
					List<MultipartFile> files = multiRequest.getFiles("excelFile_u");
					for(MultipartFile file : files){
						
						String orginFileName = "";
						InputStream fis = null;
						int sheetNo = 1;
				    	ProcessSheetInterface handlerObject = null;
				    	PrintStream output = null;
				    	int maxColoumnCount = 2;
				    	orginFileName = file.getOriginalFilename();
				    	int upfileIdex = statUpPolInfo.getExcelFile().lastIndexOf("\\");
				    	String upfileNm = statUpPolInfo.getExcelFile().substring(upfileIdex+1, statUpPolInfo.getExcelFile().length());
				    	if(orginFileName.equals(upfileNm)){
				    			fis = file.getInputStream();
								
								statUpPolInfo.setMsg("");
								ReadUploadExcelFile.ProcessExcelSheet(fis, sheetNo, handlerObject, output, maxColoumnCount, orgStatService, statUpPolInfo.getExcelFile(), statUpPolInfo);
							}
				    	
						if(statUpPolInfo.getMsg().equals("")){
							isUp = true;
				    	}else{
				    		isUp = false;
				    	}
					}
				}
			}
			
		}catch(Exception e){
			isUp = false;
			e.printStackTrace();
		}
		return isUp;
		
	}
}
