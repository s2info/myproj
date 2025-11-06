package sdiag.exanal.web;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import sdiag.util.CommonUtil;
import sdiag.util.StringUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.com.service.CommonService;
import sdiag.exanal.service.ExanalManageService;
import sdiag.exanal.service.PolVO;
import egovframework.rte.fdl.property.EgovPropertyService;
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
public class ExanalMultiController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "ExanalManageService")
    private ExanalManageService exanalManageService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
	@Resource(name= "commonService")
	private CommonService comService;     
    
	public void setAutoSanctService(ExanalManageService exanalManageService){
		this.exanalManageService = exanalManageService;
	}
	

    /**
	 * 정책로그 데이터 목록을 조회한다.
     * @param loginVO
     * @param searchVO
     * @param model
     * @return "/handy/ExanalPolList"
     * @throws Exception
     */
    @RequestMapping("/handy/ExanalPolList.do")
	public String selectPolList (@ModelAttribute("searchVO") PolVO polVO
			, HttpServletRequest request
			, HttpServletResponse response
			, ModelMap model
			) throws Exception {
    	/** EgovPropertyService.sample */
    	polVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	polVO.setPageSize(propertiesService.getInt("handyPageSize"));

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(polVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(polVO.getPageUnit());
		paginationInfo.setPageSize(polVO.getPageSize());
		
		polVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		polVO.setLastIndex(paginationInfo.getLastRecordIndex());
		polVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		String genTableName = ""; // set table name temporary 
		
		/** top menu call */
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.HANDY);
		
		String sqno = request.getParameter("sqno") == null ? "0" : (String)request.getParameter("sqno");
		
		String table_name = request.getParameter("tbnm") == "" ? request.getParameter("fileName") : request.getParameter("tbnm");
		
		table_name = request.getParameter("fileName") == null ? request.getParameter("tbnm"): request.getParameter("fileName");
		
		int sn = Integer.parseInt(sqno);
		String gubun = "";
		
		if(sn > 0){
			polVO.setSn(sn);
			polVO.setTable_name(table_name);
			
			model.addAttribute("sqno", sn);
			model.addAttribute("tableName", table_name); 
			
			System.out.println("=====cmd:"+request.getParameter("cmd")+"=====table_name" + request.getParameter("fileName") + "====sn:"+sn);
	        
		    List<EgovMap> resultList = exanalManageService.selectPolList(polVO);
		    gubun = exanalManageService.getGubunInfo(table_name);
	    	
	    	model.addAttribute("resultList", resultList);
	    	model.addAttribute("gubun", gubun);
	        
	        PolVO file_name = exanalManageService.selectFileName(sn);
	        
	        model.addAttribute("filename", file_name.getOrgfile_name());
	        
	        int totCnt = exanalManageService.selectPolListTotCnt(polVO);
	        
			model.addAttribute("totCnt", totCnt);
	        model.addAttribute("paginationInfo", paginationInfo);
	        
			model.addAttribute("totalPage", 1);
			model.addAttribute("currentpage", 1);
			
		}else{
			
			model.addAttribute("sqno", "0");
			model.addAttribute("tableName", "");   			
			
			model.addAttribute("filename", "");
			model.addAttribute("totCnt", 0);
	        model.addAttribute("paginationInfo", "");
	        
			model.addAttribute("totalPage", 0);
			model.addAttribute("currentpage", 0);	
			model.addAttribute("gubun", gubun);
			
		}
        
        return "/handy/ExanalPolList";
    }
    
    
   /**
	 * 정책관리 테이블 목록 조회.
     * @param loginVO
     * @param searchVO
     * @param model
     * @return "/handy/ExanalTableList"
     * @throws Exception
     */	
	@RequestMapping(value = "/handy/ExanalTableList.do")
	public String tableList(@ModelAttribute("loginVO") UserManageVO loginVO  
			, final HttpServletRequest request
			, @ModelAttribute("searchVO") PolVO polVO
			, Map commandMap
			, Model model) throws Exception {

		model.addAttribute("resultList", exanalManageService.selectTableList(polVO));
		
		return "/handy/ExanalTableList";
	
	}
	
	   /**
		 * 정책로그 데이터 Grid 생성.
	     * @param loginVO
	     * @param searchVO
	     * @param model
	     * @return void
	     * @throws Exception
	     */
	    @RequestMapping(value="/handy/ExanalPolGrid.do")
		public void selectPolGrid (@ModelAttribute("loginVO") UserManageVO loginVO
				, @ModelAttribute("searchVO") PolVO polVO
				, final HttpServletRequest request
				, final HttpServletResponse response
				, ModelMap model
				) throws Exception {
	    	
	    	/** EgovPropertyService.sample */
	    	polVO.setPageUnit(propertiesService.getInt("pageUnit"));
	    	polVO.setPageSize(propertiesService.getInt("pageSize"));

	    	/** paging */
	    	PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(polVO.getPageIndex());
			paginationInfo.setRecordCountPerPage(polVO.getPageUnit());
			paginationInfo.setPageSize(polVO.getPageSize());
			
			polVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
			polVO.setLastIndex(paginationInfo.getLastRecordIndex());
			polVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());	    	
	    				
			String genTableName = "_hart_dlp_usb"; // set table name temporary 
			polVO.setGenTableName(genTableName);
			
			polVO.setBegin_date(polVO.getBegin_date().replace("-", ""));
			
			String sqno = request.getParameter("sqno") == null ? "1" : (String)request.getParameter("sqno");
			int sn = Integer.parseInt(sqno);

			PolVO table_name2 = exanalManageService.selectFileName(sn);			
			
			polVO.setSn(sn);
			
			polVO.setTable_name(table_name2.getTable_name());
			model.addAttribute("tableName", table_name2.getTable_name());
				        
		    List<EgovMap> resultList = exanalManageService.selectPolList(polVO);
			
			System.out.println("/* 컬럼리스트 */");		
	    	
	    	List<EgovMap> columnAllList = exanalManageService.getColumnList(polVO);
	    	
			Gson gson = new Gson();
			HashMap<String, Object> map = new HashMap<String,Object>();
			String msg = "Error!!";
		    Boolean isOk = false; 
		    
			StringBuilder str = new StringBuilder();
			
			if(resultList.size() > 0){
			
			    try
				{
	                str.append("	<tr style=\"cursor:pointer;\"> ");		    	
			    	
			    	for(EgovMap col:columnAllList){
			    		str.append(String.format("<th class=\"lt_text3\" nowrap=\"nowrap\">%s</th>", col.get("columnname")));
			    	}
	
			    	str.append("</tr>");
	
				
			    	for(EgovMap row:resultList){
			    		
			    		str.append("<tr>");
			    		
				    	for(EgovMap col:columnAllList){
				    		str.append(String.format("<td class='col co3 btn_modify' mode='2'>%s</td>",row.get(CamelUtil.convert2CamelCase((String) col.get("columnname")))));			    		
				    	}		    		
			    		str.append("</tr>");
			    	}
			    	
				    isOk = true;
				    
					int totalCnt = exanalManageService.selectPolListTotCnt(polVO);
			    	int TotPage =  (totalCnt -1) / polVO.getPageSize() + 1; 
					
			        map.put("totalPage", TotPage);
			        map.put("currentpage", polVO.getPageIndex());			    
					
				    map.put("ISOK", isOk);
				    map.put("MSG", msg);
				    map.put("strList", str.toString());
				}
				catch(Exception ex)
				{
					msg = ex.toString();
					map.put("ISOK", isOk);
					map.put("MSG", msg);
					
				}
			}else{
				
		        map.put("totalPage", 0);
		        map.put("currentpage", 1);			    
				
		        msg = "Data count : 0";
			    map.put("ISOK", isOk);
			    map.put("MSG", msg);
			    map.put("strList", "");				
				
			}
		    
			response.setContentType("application/json");
			response.setContentType("text/xml;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(gson.toJson(map));

	    }   

	    
		/**
		 * 엑셀파일을 업로드하여 신규테이블 생성하거나 기존 테이블에 데이터를 등록한다.
		 * @param loginVO
		 * @param request
		 * @param model
		 * @return "/handy/ExanalPolRegist"
		 * @throws Exception
		 */
		@RequestMapping(value = "/handy/ExanalPolRegist.do")
		public String ExanalPolRegist(@ModelAttribute("loginVO") UserManageVO loginVO
				, @ModelAttribute("searchVO") PolVO polVO
				, HttpServletRequest request
				, HttpServletResponse response
				, MultipartHttpServletRequest req
				, Model model) throws Exception {

			String sCmd = request.getParameter("cmd") == null ? "" : (String)request.getParameter("cmd");
	    	if (sCmd.equals("")) {
	    		return "/handy/ExanalPolList";
	    	}
	    	
	    	final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;	    	
			final Map<String, MultipartFile> files = multiRequest.getFileMap();
			
			Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();			
			
			MultipartFile file = null;
			InputStream fis = null;
			
			String orginFileName = "";
			
			String preFix = "_hart_";
			String preFix_diag = "sldm_";
			
			String sn = request.getParameter("sqno");
			String fileName = request.getParameter("fileName");
			
			String gubun = request.getParameter("gubun");
			
			String inMode = "";
			String column_result;
			
			String genTableName = "";
			String genTableName_diag = "";
			
			if(sn.equals("0") && fileName.equals("")){
				inMode = "NEW";
			}else{
				inMode = "EXT";
			}
			
			System.out.println("sn:"+sn +" fileName:"+fileName);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>inMode>>>>>>>>>>>>>>"+inMode);
			
			
			while (itr.hasNext()) {
				Entry<String, MultipartFile> entry = itr.next();
			    try{
			    
					file = entry.getValue();
					fis = file.getInputStream();
					
					orginFileName = file.getOriginalFilename();
					
					if(inMode.equals("NEW")){
						fileName = orginFileName;
					}					
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		    int index = orginFileName.lastIndexOf(".");
		    fileName = orginFileName.substring(0, index);
		    String fileExt = orginFileName.substring(index + 1);
		    
		    if(inMode.equals("NEW")){
		    							    	
		    	/* 파일명으로 부터 테이블 이름생성 처리
		    	 * Remove space
		    	 * Remove minus(-)
		    	 * Add preFix
		    	 */
		    	fileName = StringUtil.removeWhitespace(fileName);
		    	
		    	int indexOfFile = StringUtil.indexOf(fileName, "-");
		    	
		    	if(indexOfFile > -1){
		    		fileName = fileName.substring(0, indexOfFile);
		    	}
    
		    	genTableName = preFix + fileName;
		    	genTableName_diag = preFix_diag + preFix + fileName;
		    	
		    }else{
		    	
		    	genTableName = request.getParameter("fileName");
		    	genTableName_diag = request.getParameter("fileName");
		    }		    
		    
		    System.out.println(">>>>>>>>>>>>>genTableName>>>>>>>>>>>>>"+genTableName);			
			
			if(file.getOriginalFilename().toLowerCase().endsWith(".xls")){
				System.out.println("-----------------------------------------> xls file");
				
	            String columnList = "";
	            String columnDescription = "";
	            
	            String columnList_diag = "";
	            String columnDescription_diag = "";	
	            
	            /** 예약 키 컬럼리스트 생성 */
	            columnList += "sldm_empno, ";
	            columnList += "sldm_ip, ";
	            columnList += "sldm_mac, ";
	            columnList += "sldm_org_logdate, ";
	            
	           
	            
	            columnDescription += "sldm_empno character varying(20), ";
	            columnDescription += "sldm_ip character varying(25), ";
	            columnDescription += "sldm_mac character varying(34), ";
	            columnDescription += "sldm_org_logdate timestamp without time zone, ";
	            
	     
	            
	            columnList_diag += columnList;
	            columnList_diag += "sldm_policy_id, ";
	            columnList_diag += "sldm_explan_flag, ";
	            columnList_diag += "sldm_extract_value, ";
	            columnList_diag += "sldm_eventdate, ";
	            
	            columnDescription_diag += columnDescription;
	            columnDescription_diag += "sldm_policy_id character varying(5), ";
	            columnDescription_diag += "sldm_explan_flag character(1), ";
	            columnDescription_diag += "sldm_extract_value integer, ";	            
	            columnDescription_diag += "sldm_eventdate timestamp without time zone, ";
				try {
					
					System.out.println("T:xls===========================================================");
					HSSFWorkbook wb = new HSSFWorkbook(fis);
					HSSFSheet sheet = null;
					HSSFRow row = null;
					HSSFCell cell = null;
					System.out.println("S:xls===========================================================");					
				
//						System.out.println("T:xlsx===========================================================");
//						XSSFWorkbook wb = new XSSFWorkbook(fileIn.getInputStream());
//						XSSFSheet sheet = null;
//						XSSFRow row = null;
//						XSSFCell cell = null;
//						System.out.println("S:xlsx===========================================================");
				
					int sheetNum = wb.getNumberOfSheets();
					sheetNum = 1;  // sheetNum 시트개수 1로 설정 첫번째 시트처리
										
					for(int i = 0; i < sheetNum; i++){
						sheet = wb.getSheetAt(i);
						int rows = sheet.getPhysicalNumberOfRows();
						int empNoCell = 0; // emp_no의 셀넘버
												
						if (rows > 1){
						
							HSSFRow frow = sheet.getRow(0);
							int fcells = frow.getPhysicalNumberOfCells();
							
							for(int fc = 0; fc < fcells; fc++){ // 첫번째 row로 부터 헤드컬럼을 생성하고 테이블을 생성한다.
								
								cell = frow.getCell(fc);
								
								if(cell == null){
									continue;
								}
								
								switch(cell.getCellType()){
								
								case 0:
									//System.out.println(cell.getNumericCellValue());
									columnList += Integer.toString((int) cell.getNumericCellValue());
									columnDescription += Integer.toString((int) cell.getNumericCellValue()) + " character varying(500)";
									columnList_diag += Integer.toString((int) cell.getNumericCellValue());
									columnDescription_diag += Integer.toString((int) cell.getNumericCellValue()) + " character varying(500)";										
									break;
								case 1:
									//System.out.println(cell.getStringCellValue());
									columnList += cell.getStringCellValue();
									columnDescription += cell.getStringCellValue() + " character varying(500)";
									columnList_diag += cell.getStringCellValue();
									columnDescription_diag += cell.getStringCellValue() + " character varying(500)";	
									break;
								case Cell.CELL_TYPE_FORMULA :
									//System.out.println(cell.getCellFormula());
									columnList += cell.getCellFormula();
									columnDescription += cell.getCellFormula() + " character varying(500)";
									columnList_diag += cell.getCellFormula();
									columnDescription_diag += cell.getCellFormula() + " character varying(500)";
									break;
									
								default:
									//System.out.println("");
										
								}								
											            	
				            	if(fc < fcells - 1){
				            		columnList += ", ";
				            		columnDescription += ", ";
				            		columnList_diag += ", ";
				            		columnDescription_diag += ", ";
				            	}
				            	
				                // Catch cell number for emp_no column 
				                // It is used to head column for first row. One of columns must be "emp_no"
				                if(columnList.equals("emp_no")){ 	
				                	
				                	//set empNoCell to currCell number
				                	empNoCell = fc;
				                	System.out.println("=========> emp_no's currCell number = " + fc);
				                } 					            	
								
							}							
							
							/* S:Create Table */
							
							int mCnt1 = 0;
			            	int mCnt2 = 0;

				            polVO.setGenTableName(genTableName.toLowerCase());
				            polVO.setTable_name(genTableName.toLowerCase());
				            if(gubun.equals("G")){
				            	polVO.setColumnList(columnList+ ", ep_flag, log_yn");
				            	polVO.setColumnDescription(columnDescription+ ", ep_flag character varying(500), log_yn character varying(500)");
				            	polVO.setColumnList_diag(columnList_diag+ ", ep_flag, log_yn");
				            	polVO.setColumnDescription_diag(columnDescription_diag+ ", ep_flag character varying(500), log_yn character varying(500)");		
				            }else if(gubun.equals("S")){
				            	String [] cArr = columnList.split(",");
				            	for(String column : cArr){
				            		column = column.replaceAll(" ", "");
				            		if(column.equals("조치예정일") ||column == "조치예정일"){
				            			mCnt1++;
				            		}else if(column.equals("조치여부") ||column == "조치여부"){
				            			mCnt2++;
				            		}
				            	}
				            	
				            	if(mCnt1 == 0 ){
				            		columnList = columnList+ ",  조치예정일";
				            		columnDescription = columnDescription+ ", 조치예정일 character varying(500)";
				            		columnList_diag = columnList_diag+ ",  조치예정일";
				            		columnDescription_diag = columnDescription_diag+", 조치예정일 character varying(500)";	
				            	}
				            	
				            	if(mCnt2 == 0){
				            		columnList = columnList+ ",  조치여부";
				            		columnDescription = columnDescription+ ", 조치여부 character varying(500)";
				            		columnList_diag = columnList_diag+ ",  조치여부";
				            		columnDescription_diag = columnDescription_diag+", 조치여부 character varying(500)";	
				            	}
				            	
				            	polVO.setColumnList(columnList+ ", ep_flag, 비고, seq");
				            	polVO.setColumnDescription(columnDescription+ ", ep_flag character varying(500),  비고 text, seq character varying(500)");
				            	polVO.setColumnList_diag(columnList_diag+ ", ep_flag,  비고, seq");
				            	polVO.setColumnDescription_diag(columnDescription_diag+", ep_flag character varying(500), 비고 text,  seq character varying(500)");	
				            		
				            }else{
				            	polVO.setColumnList(columnList+ ", ep_flag");
				            	polVO.setColumnDescription(columnDescription+ ", ep_flag character varying(500)");
				            	polVO.setColumnList_diag(columnList_diag+ ", ep_flag");
				            	polVO.setColumnDescription_diag(columnDescription_diag+ ", ep_flag character varying(500)");					            
				            }
				            
				            
				            polVO.setOrgfile_name(orginFileName);
				            
				            polVO.setGenTableName_diag(genTableName_diag.toLowerCase());
				            polVO.setTable_name_diag(genTableName_diag.toLowerCase());
				           
				           
				            
				            if(inMode.equals("NEW")){
				            	
				            	/** 테이블이 있는지 조회 */
				            	int tableCount = exanalManageService.getCountTableName(polVO);
				            	
				            	if(tableCount > 0){
				            		polVO.setMsg(" 테이블명이 존재합니다.");
				            		model.addAttribute("MSG", "테이블명이 존재합니다.");
				            		//model.addAttribute("MSG_DETAIL", column_result);
				            		return "/handy/ExanalPolList";
				            	}else{
				            		exanalManageService.createPolTable(polVO);  // 테이블 생성					            		
				            	}
				            	
				            }else{
				            	/**
				            	 * 기존 테이블에 인서트인경우
				            	 * 수집한 컬럼과 기존 테이블의 컬럼을 비교
				            	 * @param columnList
				            	 * @param table column
				            	 */
				            	column_result = columnCompare(polVO);
				            	
				            	if(column_result.length() > 0){
				            		polVO.setMsg(column_result + " 컬럼이 일치하지 않습니다.");
				            		model.addAttribute("MSG", "컬럼이 일치하지 않습니다.");
				            		model.addAttribute("MSG_DETAIL", column_result);
				            		return "forward:/handy/ExanalPolList.do";
				            		//return "/handy/ExanalPolList";
				            	}
				            	
				            }								
							
							/* E:Create Table */
				            
				            
				            /* S:Insert Data in Create Table */					            
				            
				            for(int j = 1; j < rows; j++){	// Data row Extract	            						            	
				            	String valueList = "";
				            	
				            	row = sheet.getRow(j);					            	
		            	
				            	valueList = "'','','', NOW(),";
				            	
				            	
				            	
				            	for(int x = 0; x < fcells; x++){ // Data cell Extract
				            		
				            		cell = row.getCell(x);
				            		
									switch(cell.getCellType()){
									
									case 0:
										//System.out.println(cell.getNumericCellValue());
										valueList += "'" + Integer.toString((int) cell.getNumericCellValue()) + "'";										
										break;
									case 1:
										//System.out.println(cell.getStringCellValue());
										valueList += "'" + cell.getStringCellValue() + "'";
										break;
									case Cell.CELL_TYPE_FORMULA :
										//System.out.println(cell.getCellFormula());
										valueList += "'" + cell.getCellFormula() + "'";
										break;
										
									default:
										//System.out.println("");
										valueList += "''";
											
									}					            		

				            		if(x < fcells - 1){
					            		valueList += ",";
					            	}
				            		
				            		
				            	}
				            	
				            	
				            	if(gubun.equals("G")){
				            		valueList = valueList + ",'', 'Y'";		
					            }else if(gubun.equals("S")){
					            	
					            	if(mCnt1 == 0){
					            		valueList = valueList + ",''";		
					            	}
					            	if(mCnt2 == 0){
					            		valueList = valueList + ",''";
					            	}
					            	
					            	valueList = valueList + ",'','', '"+j+"'";		
					            }else{
					            	valueList = valueList + ",''";					            
					            }
				            	
				            	//valueList = valueList + ",''";
				            	
				            	polVO.setValueList(valueList);
				                
				                exanalManageService.insertPolList(polVO);
				                
				            }
				            
				            /** emp_no user_mstr과 비교하여 업데이트 한다 */
				            exanalManageService.updateSelectUsermstr(polVO);
				            
				            if(gubun.equals("G")){
						           
				            	PolVO polInfo = new PolVO();
				            	
				            	String wanValueList = "emp_no,'','',NOW(),emp_no,";
				            	
				            	for(String column :polVO.getColumnList().split(",")){
				            		column = column.replaceAll(" ", "");
				            		if(column.equals("d_flag")){
				            			wanValueList += "'N',";
				            		}else if(column.equals("ep_flag")){
				            			wanValueList += "'', 'N'";
				            			break;
				            		}else if(!column.equals("sldm_empno") && !column.equals("sldm_ip") && !column.equals("sldm_mac") && !column.equals("sldm_org_logdate") && !column.equals("empno") && !column.equals("log_yn")){
				            			wanValueList += "'',";
				            		}
				            	}
				            	
				            	polInfo.setColumnList(polVO.getColumnList());
				            	polInfo.setGenTableName(polVO.getGenTableName());
				            	polInfo.setValueList(wanValueList);
				            	polInfo.setGubun2(polVO.getGubun2());
				            	
				            	exanalManageService.insertPolWanList(polInfo);
				            }
				            
				            
				            if(inMode.equals("NEW")){
				            	exanalManageService.insertPolTableInfo(polVO);
				            }					            
				            
				            /* E:Insert Data in Create Table */
				            
						
						} //row
						
					} //sheet
					model.addAttribute("MSG", "수동업로드 처리를 완료 하였습니다.");
				}catch(Exception e){
					polVO.setMsg("수동업로드 처리 중 오류가 발생하였습니다.");
					e.printStackTrace();
				}	
				
			}else if(file.getOriginalFilename().toLowerCase().endsWith(".xlsx")){			

				/* S:SAX ================================================================================================= Parser */
		    	int sheetNo = 1;
		    	ProcessSheetInterface handlerObject = null;
		    	PrintStream output = null;
		    	int maxColoumnCount = 15;
		    	polVO.setMsg("");
		    	ReadExcelFile.ProcessExcelSheet(fis, sheetNo, handlerObject, output, maxColoumnCount, genTableName, exanalManageService, inMode, orginFileName, polVO); 			
				/* E:SAX ================================================================================================== Parser */
		    	
		    	//System.out.println("======####=======polVO.getMsg():" + polVO.getMsg());
		    	if(polVO.getMsg().equals("")){
		    		polVO.setMsg("수동업로드 처리를 완료 하였습니다.");
		    	}
        		model.addAttribute("MSG", polVO.getMsg());
        		//model.addAttribute("MSG_DETAIL", column_result);
				
			}else{
				System.out.println("-----------------------------------------> not available file");
			}

			model.addAttribute("MSG", polVO.getMsg());
	        //return "/handy/ExanalPolList";
			return "forward:/handy/ExanalPolList.do";
		}
		
		
		/**
		 * 컬럼리스트 비교, 테이블컬럼 대상
		 * @param polVO
		 * @return
		 * @throws Exception
		 */
		public String columnCompare(PolVO polVO) throws Exception {
			
			String compareResult = "";
			String[] excelColumnList;
			String allDbColumnList = "";
			
			excelColumnList = StringUtil.split(StringUtil.removeWhitespace(polVO.getColumnList()), ",");
			
			List<EgovMap> columnAllList = exanalManageService.getColumnList(polVO);
			
			Boolean matchFlag = false;
	    	
	    	for(int i=0; i<excelColumnList.length; i++){

	    		matchFlag = false;
	    		
	    		for(EgovMap col:columnAllList){
	    			if(col.get("columnname").equals(excelColumnList[i].toLowerCase())){
	    				matchFlag = true;
	    			}
	    			allDbColumnList += " " + col.get("columnname");
	    		}
	    		if(!matchFlag){
	    			compareResult += " " + excelColumnList[i];
	    		}
	    	}
	
			return compareResult;
			
		}
		
		
	    /**
		 * 테이블의 Drop처리와 테이블정보의 레코드 삭제를 수행한다.
	     * @param loginVO
	     * @param searchVO polVO
	     * @param request response
	     * @param model
	     * @return void
	     * @throws Exception
	     */			
		@RequestMapping("/handy/ExanalDelTable.do")
		public void exanalDelTable(@ModelAttribute("loginVO") UserManageVO loginVO
				, @ModelAttribute("searchVO") PolVO polVO
				, HttpServletRequest request
				, HttpServletResponse response
				, Model model) throws Exception {
			
				Gson gson = new Gson();
				HashMap<String, Object> map = new HashMap<String,Object>();
				String msg = "Error!!";
			    Boolean isOk = false; 			

				String sqno = "";
				String cmd = "";
				String tableName = "";
				String tableName_diag = "";
				
				sqno = request.getParameter("sqno");
				cmd  = request.getParameter("cmd");
				tableName = request.getParameter("fileName");
				tableName_diag = "sldm_" + tableName;			
				
				if(!tableName.isEmpty()){
					polVO.setTable_name(tableName);
					polVO.setTable_name_diag(tableName_diag);	
					exanalManageService.deleteTable(polVO);
					
			        map.put("totalPage", 0);
			        map.put("currentpage", 1);			    
					
			        msg = "Data count : 0";
				    map.put("ISOK", isOk);
				    map.put("MSG", polVO.getMsg());
				    map.put("strList", "");					
					
				}else{				
			        map.put("totalPage", 0);
			        map.put("currentpage", 1);			    
					
			        msg = "Data count : 0";
				    map.put("ISOK", isOk);
				    map.put("MSG", "테이블이 삭제처리되지 않았습니다.");
				    map.put("strList", "");					
				}				
				
		    
				response.setContentType("application/json");
				response.setContentType("text/xml;charset=utf-8");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(gson.toJson(map));				
        
		}
		
		
	    /**
		 * 테이블의 '전체데이터 || 검색날짜' 데이터를 Delete처리 한다.
	     * @param loginVO
	     * @param searchVO polVO
	     * @param request response
	     * @param model
	     * @return void
	     * @throws Exception
	     */		
		@RequestMapping("/handy/ExanalDelRecode.do")
		public void exanalDelRecode(@ModelAttribute("loginVO") UserManageVO loginVO
				, @ModelAttribute("searchVO") PolVO polVO
				, HttpServletRequest request
				, HttpServletResponse response
				, Model model) throws Exception {
			
				Gson gson = new Gson();
				HashMap<String, Object> map = new HashMap<String,Object>();
				String msg = "Error!!";
			    Boolean isOk = false; 			

				String sqno = "";
				String cmd = "";
				String tableName = "";
				String beginDate = "";
				
				int totCnt = 0;
				
				sqno = request.getParameter("sqno");
				cmd  = request.getParameter("cmd");
				tableName = request.getParameter("fileName");
				beginDate = request.getParameter("beginDate");
				
				polVO.setBegin_date(polVO.getBegin_date().replace("-", ""));
				
				if(cmd.equals("DelRecode")){
					exanalManageService.deleteRecode(polVO);
					isOk = true; 
					
					totCnt = exanalManageService.selectPolListTotCnt(polVO);
					 
			    	int TotPage =  (totCnt -1) / polVO.getPageSize() + 1; 
					
			        isOk = true;
			        map.put("totalPage", TotPage);					 
				}
				
		        map.put("currentpage", 1);			    
				
		        msg = "Data count : 0";
			    map.put("ISOK", isOk);
			    map.put("MSG", polVO.getMsg());
			    map.put("strList", "");
			    map.put("totCnt", totCnt);
				
				response.setContentType("application/json");
				response.setContentType("text/xml;charset=utf-8");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(gson.toJson(map));				
      
		}
		
		/*
		
		
		@RequestMapping("/handy/exanaltest.do")
		public void exanaltest() throws Exception {
			
	        Connection con = null;
	        PreparedStatement pstmt = null ;
	         
	        String sql = "Insert Into _hart_test(uid, name, age) Values(?, ?, ?)" ;
	         
	         
	        try{
	            Class.forName("core.log.jdbc.driver.PostgresqlDriver");
	            con = DriverManager.getConnection("jdbc:postgresql://10.225.148.195:5432/diagdb", "diag", "diag");
	            con.setAutoCommit(false);
	             
	             
	            pstmt = con.prepareStatement(sql) ;
	             
	             
	            for(int i=0; i < 100000; i++){
	                int uid = 10000 + i ;
	                String name = "홍길동_" + Integer.toString(i) ;
	                int age = i ;
	             
	                pstmt.setInt(1, uid);
	                pstmt.setString(2, name) ;
	                pstmt.setInt(3, age);
	                 
	                // addBatch에 담기
	                pstmt.addBatch();
	                 
	                // 파라미터 Clear
	                pstmt.clearParameters() ;
	                 
	                 
	                // OutOfMemory를 고려하여 만건 단위로 커밋
	                if( (i % 10000) == 0){
	                     
	                    // Batch 실행
	                    pstmt.executeBatch() ;
	                     
	                    // Batch 초기화
	                    pstmt.clearBatch();
	                     
	                    // 커밋
	                    con.commit() ;
	                     
	                }
	            }
	             
	             
	            // 커밋되지 못한 나머지 구문에 대하여 커밋
	            pstmt.executeBatch() ;
	            con.commit() ;
	             
	        }catch(Exception e){
	            e.printStackTrace();
	             
	            try {
	                con.rollback() ;
	            } catch (SQLException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
	             
	        }finally{
	            if (pstmt != null) try {pstmt.close();pstmt = null;} catch(SQLException ex){}
	            if (con != null) try {con.close();con = null;} catch(SQLException ex){}
	        }
	 
	    }			
		
*/
}


