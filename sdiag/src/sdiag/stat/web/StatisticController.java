package sdiag.stat.web;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.MenuItemVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.stat.service.StatisticResultVO;
import sdiag.stat.service.StatisticSearchVO;
import sdiag.stat.service.StatisticService;
import sdiag.util.CommonUtil;
import sdiag.util.ExcelUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.MajrCodeInfo;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class StatisticController {
	@Resource(name= "commonService")
	private CommonService comService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name="StatisticService")
	private StatisticService statService;
	/**
	 * 조직별 통계
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/statistorglist.do")
	public String statistorglist(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STATIC, comService);	
		
		List<HashMap<String, Object>> polList = statService.getPolicyTitleList();
		model.addAttribute("polList", polList);
		
		if(searchVO.getUpperOrgCode().equals("")){
			String root = MajrCodeInfo.RootOrgCode;
			OrgGroupVO rootInfo = comService.getOrgInfo(root);
			searchVO.setUpperOrgCode(rootInfo.getOrg_code());
			searchVO.setUpperOrgName(rootInfo.getOrg_nm());
		}
		
		//검색날짜 조회 - 최종날짜 조회
		if(searchVO.getBegindate().equals("")){
			String begin_Date = statService.getPolicyfactLastDate();
			searchVO.setBegindate(begin_Date);
			searchVO.setEnddate(begin_Date);
		}
		
		model.addAttribute("totalPage", 1);
		model.addAttribute("currentpage", 1);
		
		/*
		//검색날짜 조회 - 최종날짜 조회
		String begin_Date = statService.getPolicyfactLastDate();
				
		model.addAttribute("getdate", begin_Date);
		
		List<StatisticResultVO> resultList = null;
		/** 통계 검색 
		if(polList.size() > 0){
			if(searchVO.getBegindate().equals("")){
				searchVO.setBegindate(begin_Date);
			}
			if(searchVO.getEnddate().equals("")){
				searchVO.setEnddate(begin_Date);
			}
			if(searchVO.getPolicyid().equals("")){
				EgovMap polInfo = (EgovMap)polList.get(0);
				searchVO.setPolicyid(polInfo.get("policyid").toString());
			}
			
			resultList = statService.getPolictfactInfoOrgResult(searchVO);
			
		}
		
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultTotalCnt", resultList == null ? 0 : resultList.size());
		*/
		return "man/stat/statistorg";
	}
	/**
	 * 조직별 통계 조회
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/getstatistpolicyfactInfoorgdata.do")
	public void getstatistpolicyfactInfoorgdata(HttpServletRequest request
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
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
	    	
			PaginationInfo paginationInfo = new PaginationInfo();
	 		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());

	 		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());

	 		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	 		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
			
		    StringBuffer data = new StringBuffer();
		    //List<StatisticResultVO> resultList = statService.getPolictfactInfoOrgResult(searchVO);
		    
		    HashMap<String,Object> retMap =  statService.getPolictfactInfoOrgResult(searchVO);
		    List<StatisticResultVO> resultList = (List<StatisticResultVO>)retMap.get("List");
		    
		    if(resultList.size() > 0){
		    	for(StatisticResultVO row:resultList){
			    	data.append("<tr style='font-weight:bold;'>");
			    	data.append(String.format("<td>%s</td>", row.getPeriod()));
			    	data.append(String.format("<td>%s</td>", row.getSec_pol_desc()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_1()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_2()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_3()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_4()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_5()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_6()));
			    	data.append(String.format("<td>%s</td>", row.getTot_event()));
			    	data.append("</tr>");
			    }
		    }else{
		    	data.append("<tr style='font-weight:bold;height:300px;'>");
		    	data.append("<td colspan='9'>검색결과가 없습니다.</td>");
		    	data.append("</tr>");
		    }
		
		    int totalCnt = (int)retMap.get("totalCount");
	    	int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		
	    	 isOk = true;
	    	
		    hMap.put("list_body", data.toString());
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);
			hMap.put("ISDATA", resultList.size() > 0 ? "Y" : "N");
			hMap.put("totalPage", TotPage);
		    hMap.put("currentpage", searchVO.getPageIndex());
	    }catch(Exception ex){
	    	msg = ex.toString();
	    	hMap.put("ISOK", isOk);
	    	hMap.put("MSG", msg);
	    	hMap.put("totalPage", 1);
		    hMap.put("currentpage", 1);
	    }
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
	   
	}	
	/**
	 * 직원별 통계
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/statistuserlist.do")
	public String statistuserlist(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
			, ModelMap model) throws Exception{	
				
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STATIC, comService);
		
		List<HashMap<String, Object>> polList = statService.getPolicyTitleList();
		model.addAttribute("polList", polList);
		
		if(searchVO.getUpperOrgCode().equals("")){
			String root = MajrCodeInfo.RootOrgCode;
			OrgGroupVO rootInfo = comService.getOrgInfo(root);
			searchVO.setUpperOrgCode(rootInfo.getOrg_code());
			searchVO.setUpperOrgName(rootInfo.getOrg_nm());
		}
		
		//검색날짜 조회 - 최종날짜 조회
		if(searchVO.getBegindate().equals("")){
			String begin_Date = statService.getPolicyfactLastDate();
			searchVO.setBegindate(begin_Date);
			searchVO.setEnddate(begin_Date);
		}
		
		model.addAttribute("totalPage", 1);
		model.addAttribute("currentpage", 1);
				
		/*
		List<StatisticResultVO> resultList = null;
		/** 통계 검색 
		if(polList.size() > 0){
			if(searchVO.getBegindate().equals("")){
				searchVO.setBegindate(begin_Date);
			}
			if(searchVO.getEnddate().equals("")){
				searchVO.setEnddate(begin_Date);
			}
			if(searchVO.getPolicyid().equals("")){
				EgovMap polInfo = (EgovMap)polList.get(0);
				searchVO.setPolicyid(polInfo.get("policyid").toString());
			}
				
			resultList = statService.getPolictfactInfoUserResult(searchVO);
					
		}
				
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultTotalCnt", resultList == null ? 0 : resultList.size());
		*/		
		return "man/stat/statistuser";
	}
	/**
	 * 직원별 통계 조회
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/getstatistpolicyfactInfouserdata.do")
	public void getstatistpolicyfactInfouserdata(HttpServletRequest request
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
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
			PaginationInfo paginationInfo = new PaginationInfo();
	 		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());

	 		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());

	 		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	 		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	 		
		    StringBuffer data = new StringBuffer();
		    //List<StatisticResultVO> resultList = statService.getPolictfactInfoUserResult(searchVO);
		    HashMap<String,Object> retMap =  statService.getPolictfactInfoUserResult(searchVO);
		    
		    List<StatisticResultVO> resultList = (List<StatisticResultVO>)retMap.get("List");
		    
		    if(resultList.size() > 0){
			    for(StatisticResultVO row:resultList){
			    	data.append("<tr style='font-weight:bold;'>");
			    	data.append(String.format("<td>%s</td>", row.getPeriod()));
			    	data.append(String.format("<td>%s</td>", row.getSec_pol_desc()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_1()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_2()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_3()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_4()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_5()));
			    	data.append(String.format("<td>%s</td>", row.getOrg_nm_6()));
			    	data.append(String.format("<td>%s<br />%s</td>", row.getEmp_no(), row.getEmp_nm()));
			    	data.append(String.format("<td>%s</td>", row.getTot_event()));
			    	data.append("</tr>");
			    }
		    }else{
		    	data.append("<tr style='font-weight:bold;height:300px;'>");
		    	data.append("<td colspan='10'>검색결과가 없습니다.</td>");
		    	data.append("</tr>");
		    }
		    
		    int totalCnt = (int)retMap.get("totalCount");
	    	int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		
		    isOk = true;
		    
		    hMap.put("list_body", data.toString());
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);
			hMap.put("ISDATA", resultList.size() > 0 ? "Y" : "N");
			hMap.put("totalPage", TotPage);
		    hMap.put("currentpage", searchVO.getPageIndex());
	    }catch(Exception ex){
	    
	    	msg = ex.toString();
	    	hMap.put("ISOK", isOk);
	    	hMap.put("MSG", msg);
	    	hMap.put("totalPage", 1);
		    hMap.put("currentpage", 1);
	    }
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
	   
	}
	
	
	/**
	 * 정책별 조직별 종합 통계
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/statistpoltotallist.do")
	public String statistpoltotallist(HttpServletRequest request
			, HttpServletResponse response
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
			, ModelMap model) throws Exception{	
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.STATIC, comService);
		
		List<EgovMap> menuAllList = comService.getPolMenuAllList();
		List<EgovMap> majrCodeList = new ArrayList<EgovMap>();
		for(EgovMap row:menuAllList){
			if(row.get("majcode").equals(row.get("mincode"))){
				EgovMap mMap = new EgovMap();
				mMap.put("majcode", row.get("majcode"));
				mMap.put("diagdesc", row.get("diagdesc"));
				mMap.put("buseoindc", row.get("buseoindc"));
				majrCodeList.add(mMap);
			}
		}
		model.addAttribute("majrCodeList", majrCodeList);
		
		if(searchVO.getMajrCode().equals("")){
			EgovMap firstCode = majrCodeList.get(0);
			searchVO.setMajrCode(firstCode.get("majcode").toString());
			searchVO.setBuseoindc(firstCode.get("buseoindc").toString());
		}
		
		if(searchVO.getUpperOrgCode().equals("")){
			String root = MajrCodeInfo.RootOrgCode;
			OrgGroupVO rootInfo = comService.getOrgInfo(root);
			searchVO.setUpperOrgCode(rootInfo.getOrg_code());
			searchVO.setUpperOrgName(rootInfo.getOrg_nm());
		}
		
		//검색날짜 조회 - 최종날짜 조회
		if(searchVO.getBegindate().equals("")){
			String begin_Date = statService.getPolicyfactLastDate();
			searchVO.setBegindate(begin_Date);
			searchVO.setEnddate(begin_Date);
		}
		
		return "man/stat/statistpoltotal";
	}
	/**
	 * 종합 통계분석 검색결과
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/getstatistpoltotaldata.do")
	public void getstatistpoltotaldata(HttpServletRequest request
			, @ModelAttribute("searchVO") StatisticSearchVO searchVO
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
			
		    /**
			 * 테이블 상단 정책 테이블 구성
			 */
			StringBuffer thead = new StringBuffer();
			StringBuffer thead0 = new StringBuffer();
			StringBuffer thead1 = new StringBuffer();
			List<HashMap<String, Object>> PolicyList = statService.getDiagItemAndPolList(searchVO.getMajrCode());
			//중진단 설정
			
			String comcode = "";
			String comname= "";
			int rowcnt = 0;
			int totcnt = 0;
			for(HashMap<String, Object> row:PolicyList){
				totcnt++;
				if(comcode.equals("")){
					comcode = row.get("diag_minr_code").toString();
					comname = row.get("minr_name").toString();
				}
				if(PolicyList.size() == totcnt){
					rowcnt++;
					thead0.append(String.format("<th colspan='%s' class='%s'>%s</th>", rowcnt, row.get("diag_minr_code").toString(), row.get("minr_name").toString()));
				}else{
					if(comcode.equals(row.get("diag_minr_code").toString())){
						rowcnt++;
					}else{
						thead0.append(String.format("<th colspan='%s' class='%s'>%s</th>", rowcnt, comcode, comname));
						comcode = row.get("diag_minr_code").toString();
						comname = row.get("minr_name").toString();
						rowcnt = 1;
					}
				}
				thead1.append(String.format("<th style='width:150px;' class='%s'>%s</th>", row.get("sec_pol_id").toString(), row.get("sec_pol_desc").toString()));
				
			}
			
			thead.append("<tr>");
			thead.append("<th rowspan='2' style='width:400px;'>조직 리스트</th>");
			thead.append(thead0.toString());
			thead.append("</tr>");
			//정책 설정
			thead.append("<tr>");
			thead.append(thead1.toString());
			thead.append("</tr>");
			hMap.put("list_head", thead.toString());
			hMap.put("list_width", PolicyList.size() * 150 + 400);
			
		    StringBuffer data = new StringBuffer();
			List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
			
			//정책별 건수 조회
			List<HashMap<String, Object>> PolCntList = statService.getUpperOrgPolicySumCount(searchVO);
			//하위조직 검색
			List<HashMap<String, Object>> SubOrgList = statService.getSubOrgList(searchVO.getUpperOrgCode());
			
			for(HashMap<String, Object> row:SubOrgList){
				data.append(String.format("<tr class='org_sel_area' name='%s' is_suborg='%s' style='height:23px;'>"
						, searchVO.getUpperOrgCode()
						, row.get("issuborg").toString()));
				String blankArea = "";
				for(int i=0 ; i<Integer.parseInt(row.get("org_level").toString()) ;i++){
					blankArea += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				data.append(String.format("<td org_code='%s' isgetdata='N' style='text-align:left;padding:0;'>%s</td>"
						, row.get("org_code").toString()
						, String.format("%s<img %s is_open='N' org_code='%s' src='%s' style='margin-right:3px;%s'/>%s"
								, blankArea
								, row.get("issuborg").equals("Y") ? "class='btn_org'" : ""
								, row.get("org_code").toString()
								, row.get("issuborg").equals("Y") ? "/img/folder-closed.gif" : "/img/file.gif"
								, row.get("issuborg").equals("Y") ? "cursor:pointer;" : ""
								, row.get("org_nm").toString())));
				searchVO.setSearchOrgCode(row.get("org_code").toString());
				
				for(HashMap<String, Object> polrow:PolicyList){
					String cnt = getOrgPolCount(PolCntList, row.get("org_code").toString(), polrow.get("sec_pol_id").toString());
					String score = getOrgPolAvgScore(PolCntList, row.get("org_code").toString(), polrow.get("sec_pol_id").toString());
					data.append(String.format("<td class='cnt' pol_code='%s'>%s (%s)</td>", polrow.get("sec_pol_id").toString(), cnt, score));
					/*
					for(HashMap<String, Object> polData:PolicyFactData){
						if(polrow.get("sec_pol_id").equals(polData.get("polid"))){
							//val.put(polData.get("polid").toString(), polData.get("eventcnt").toString());
							data.append(String.format("<td class='cnt' pol_code='%s'>%s</td>", polData.get("polid").toString(), polData.get("eventcnt").toString()));
						}
					}
					*/
				}
				
			}
					
		    isOk = true;
		    //hMap.put("listvalue", dataList);
		    hMap.put("list_body", data.toString());
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);
			hMap.put("list_rowcount", SubOrgList.size() + 2);
	    }catch(Exception ex){
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
	
	private String getOrgPolCount(List<HashMap<String, Object>> list, String org_code,String pol_id){
		String ret = "0";
		for(HashMap<String, Object> row:list){
			if(row.get("org_code").equals(org_code) && row.get("pol_idx_id").equals(pol_id)){
				ret = row.get("cnt").toString();
				break;
			}
		}
		return ret;
	}
	
	private String getOrgPolAvgScore(List<HashMap<String, Object>> list, String org_code,String pol_id){
		String ret = "100";
		for(HashMap<String, Object> row:list){
			if(row.get("org_code").equals(org_code) && row.get("pol_idx_id").equals(pol_id)){
				ret = row.get("score").toString();
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 통계분석 종합정보 Excel 출력
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/stat/statisttotalexportexcel.do")
	public void statisttotalexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") StatisticSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
		String fileName = "정책및조직별_통계분석";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("통계분석결과");
		excelVO.setTitle("통계분석결과");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		List<String> rowData = CommonUtil.SplitToString(searchVO.getBodyData(), ",");
		List<String> head = new ArrayList<String>();
		head.add("조직 리스트");
		
		List<String> colData = CommonUtil.SplitToString(rowData.get(1), "\\|");
		for(int i=0 ; i < colData.size(); i++){
			head.add(colData.get(i));
		}
		excelVO.setHead(head);
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		for(int i=2 ; i < rowData.size() ; i++){
			List<String> columnData = CommonUtil.SplitToString(rowData.get(i), "\\|");
			EgovMap map = new EgovMap();
			for(int j=0 ; j < columnData.size(); j++){
				map.put(head.get(j), columnData.get(j));
			}
			excellist.add(map);
		}
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String setContent(List<HashMap<String, Object>> list){
		
		StringBuffer data = new StringBuffer();
		HashMap<String, Object> log = (HashMap<String, Object>)list.get(0);
		
		for(Object key:log.keySet())
		{
			data.append(ConvertToColumnName(key.toString()) + ",");
		}
		data.append("\n");
		
		for(HashMap<String, Object> row:list)
		{
			for(Object key:row.keySet()){
				data.append(row.get(key).toString()+ ",");
			}
			data.append("\n");
		}
		
		return data.toString();
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
				contents += row.get(key).toString()+ ",";
			}
			csvList.add(contents.split(","));
		}
		
		return csvList;
	}
	
	/**
	 * 조직별/직원별 통계 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	
	@RequestMapping(value="/stat/statistorguserexportexcel.do")
	public void sanctionListexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") StatisticSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
		String fileName = searchVO.getSearchType().equals("O") ? "조직별_통계" : "직원별_통계";
	
		String FileName = URLEncoder.encode(String.format("%s_%s_%s", searchVO.getBegindate().replaceAll("-",  ""), searchVO.getEnddate().replaceAll("-", ""), fileName), "UTF-8").replace("\\", "%20");
		
		List<HashMap<String, Object>> list = statService.getStatisticOrgUserListForExportExcel(searchVO);
		
		//ExcelUtil.stringToExportCsv(response, setContent(list), FileName);
		List<String[]> contents = setCSVContents(list);
		
		ExcelUtil.hashMapToExportCsv(response, FileName, contents);
		
		//response.setContentType("application/ms-excel:UTF-8;");
		//response.setCharacterEncoding("UTF-8");//MS949
		//response.setHeader("Content-Disposition", "attachment; filename=" + FileName + ".csv");
		
		
		
		//List<String> head = new ArrayList<String>();
		/*
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(0xEF);
		outputStream.write(0xBB);
		outputStream.write(0xBF);
		String outResult = setContent(list);
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		
		outputStream.write(outResult.getBytes() );
		
		outputStream.flush();
		outputStream.close();
		writer.flush();
		writer.close();
		*/
		
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	@RequestMapping(value="/stat/statistorguserexportexcel.do")
	public ResponseEntity<String> sanctionListexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") StatisticSearchVO searchVO
								, HttpServletResponse response) throws Exception {
		try{
		String fileName = searchVO.getSearchType().equals("O") ? "조직별_통계" : "직원별_통계";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("검색결과");
		excelVO.setTitle("검색결과");
		excelVO.setHeadVal("");
		excelVO.setType("xlsx");
		
		
		List<HashMap<String, Object>> list = statService.getStatisticOrgUserListForExportExcel(searchVO);
		//List<String> head = new ArrayList<String>();
	
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv;charset=MS949");
		header.add("Content-Disposition", "attachment;filename=\""+ "result.csv" + "\"");
		
		return new ResponseEntity<String>(setContent(list), header, HttpStatus.CREATED);
		
		
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
		ExcelUtil.xssExcelDown(response, excelVO, excellist);
	
		//ExcelUtil.htmlStirngToExportExcel(response, excelVO, excellist);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	*/
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "period" : ColumnName = "진단기간";break;
		case "policy_id" : ColumnName = "정책ID";break;
		case "sec_pol_desc" : ColumnName = "정책명";break;
		case "org_nm_1" : ColumnName = "조직1";break;
		case "org_nm_2" : ColumnName = "조직2";break;
		case "org_nm_3" : ColumnName = "조직3";break;
		case "org_nm_4" : ColumnName = "조직4";break;
		case "org_nm_5" : ColumnName = "조직5";break;
		case "org_nm_6" : ColumnName = "조직6";break;
		case "emp_no" : ColumnName = "사번";break;
		case "emp_nm" : ColumnName = "성명";break;
		case "tot_event" : ColumnName = "건수";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
		
}
