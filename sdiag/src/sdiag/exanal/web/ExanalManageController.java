package sdiag.exanal.web;

import java.io.InputStream;
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

import jxl.Sheet;
import jxl.Workbook;

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
public class ExanalManageController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "ExanalManageService")
    private ExanalManageService exanalManageService;
    
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
    @RequestMapping("/handy/ExanalPolListHugel.do")
	public String selectPolList (@ModelAttribute("loginVO") UserManageVO loginVO
			, @ModelAttribute("searchVO") PolVO polVO
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
		String genTableName_diag = ""; // for diag sldm_hart_(+filename+) for anal _hart_(+file_name+)
		
		/** top menu call */
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.HANDY);
		
		String sqno = request.getParameter("sqno") == null ? "0" : (String)request.getParameter("sqno");
		
		String table_name = request.getParameter("tbnm") == "" ? request.getParameter("fileName") : request.getParameter("tbnm");
		
		table_name = request.getParameter("fileName") == null ? request.getParameter("tbnm"): request.getParameter("fileName");
		
		int sn = Integer.parseInt(sqno);
		
		if(sn > 0){
			polVO.setSn(sn);
			polVO.setTable_name(table_name);
			
			model.addAttribute("sqno", sn);
			model.addAttribute("tableName", table_name); 
			
			System.out.println("=====cmd:"+request.getParameter("cmd")+"=====table_name" + request.getParameter("fileName") + "====sn:"+sn);
	        
		    List<EgovMap> resultList = exanalManageService.selectPolList(polVO);
	    	
	    	for(int i=0;i<resultList.size();i++){
	    		System.out.println(resultList.get(i));
	    	}
	    	
	    	model.addAttribute("resultList", resultList);
	        
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
	@RequestMapping(value = "/handy/ExanalTableListHugel.do")
	public String tableList(@ModelAttribute("loginVO") UserManageVO loginVO  
			, final HttpServletRequest request
			, @ModelAttribute("searchVO") PolVO polVO
			, Map commandMap
			, Model model) throws Exception {

		model.addAttribute("resultList", exanalManageService.selectTableList(polVO));
		
		return "/handy/ExanalTableListHugel";
	
	}
	
	   /**
		 * 정책로그 데이터 Grid 생성.
	     * @param loginVO
	     * @param searchVO
	     * @param model
	     * @return void
	     * @throws Exception
	     */
	    @RequestMapping(value="/handy/ExanalPolGridHugel.do")
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
	    				
			String genTableName = "_HART_DLP_USB"; // set table name temporary 
			polVO.setGenTableName(genTableName);
			
			polVO.setBegin_date(polVO.getBegin_date().replace("-", ""));
			
			String sqno = request.getParameter("sqno") == null ? "1" : (String)request.getParameter("sqno");
			int sn = Integer.parseInt(sqno);

			//String begin_date = request.getParameter("begin_date");
			//if(begin_date != null){
				//polVO.setBegin_date(begin_date);
			//}

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
					
			        isOk = true;
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
		@RequestMapping("/handy/ExanalPolRegistHugel.do")
		public String ExanalPolRegist(@ModelAttribute("searchVO") PolVO polVO
				, HttpServletRequest request
				, HttpServletResponse response
				, Model model) throws Exception {
			
			System.out.println("----------------------------------------------------------------------");

			String sCmd = request.getParameter("cmd") == null ? "" : (String)request.getParameter("cmd");
	    	if (sCmd.equals("")) {
	    		return "/handy/ExanalPolListHugel";
	    	}

	    	final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			final Map<String, MultipartFile> files = multiRequest.getFileMap();

			Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
			MultipartFile file = null;
			InputStream fis = null;	
			
			String sn = request.getParameter("sqno");
			String fileName = request.getParameter("fileName");
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
			
			/* POI========================-verbosegc -Xnoclassgc================================POI */
			
//			MultipartFile fileIn = req.getFile("fileNm");
//			
//			System.out.println("fileIn =========>"+fileIn.getSize());
//			
//			if (fileIn != null && fileIn.getSize() > 0) {
//				try {
//					OPCPackage opcPackage = OPCPackage.open(new File(""));
//					
//					
//					System.out.println("T===========================================================");
//					XSSFWorkbook wb = new XSSFWorkbook(fileIn.getInputStream());
//					System.out.println("S===========================================================");
//					XSSFSheet sheet = wb.getSheetAt(0);
//					System.out.println("P===========================================================");
//					
//					int last = sheet.getLastRowNum();
//					System.out.println("Last : " + last);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
			/* POI===========================================================POI */
			
			
			while (itr.hasNext()) {
				Entry<String, MultipartFile> entry = itr.next();
			    try{
			    
					file = entry.getValue();
					fis = file.getInputStream();
					
					String orginFileName = file.getOriginalFilename();
					
					if(inMode.equals("NEW")){
						fileName = orginFileName;
					}
										
					
					if (!"".equals(file.getOriginalFilename())) {
						// 업로드 파일에 대한 확장자를 체크
						if (file.getOriginalFilename().toLowerCase().endsWith(".xls")
								|| file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
							
					        String result = "[SUCESS_UPLOAD]";
					        				       
					        int col_num = 0;                           // 열수
					        int row_num = 0;
						    
					        /*
					         * 테이블을 생성한다.
					         * @param : filename
					         * @prefix HART_@param: filename
					         */
					        String preFix = "_hart_";
					        String preFix_diag = "sldm_";
					        
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
						    System.out.println(">>>>>>>>>>>>>genTableName_diag>>>>>>>>>>>>>"+genTableName_diag);
													
					        try{           
					            Workbook workbook = Workbook.getWorkbook(fis);
					            
					            Sheet sheet = workbook.getSheet(0);
					           
					            col_num = sheet.getColumns();                  
					            row_num = sheet.getRows();  
					            
					            System.out.println("col_num:"+col_num);
					            System.out.println("row_num:"+row_num);

					            int hx = 0;
					            int hy = 0;
					            int i = 1;
					            int j = 0;				            
					            /*
					             * column name extract
					             */
					            String columnList = "";
					            String columnDescription = "";
					            
					            String columnList_diag = "";
					            String columnDescription_diag = "";
					            
					            String constAndPrimary = "";
					            
					            
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
					            
					            
					            /** 파일 1컬럼으로 부터 컬럼 리스트를 생성 */
					            for(hx = 0; hx < col_num; hx++){
					            	columnList += sheet.getCell(hx,hy).getContents();
					            	columnDescription += sheet.getCell(hx,hy).getContents() + " character varying(500) ";
					            	columnList_diag += sheet.getCell(hx,hy).getContents();
					            	columnDescription_diag += sheet.getCell(hx,hy).getContents() + " character varying(500) ";
					            	
					            	if(hx < col_num - 1){
					            		columnList += ",";
					            		columnDescription += ",";
					            		columnList_diag += ",";
					            		columnDescription_diag += ",";
					            	}
					            }

					            
					            //constAndPrimary = " CONSTRAINT pk_"+genTableName+" PRIMARY KEY("+ sheet.getCell(0,0).getContents() +")";
					            constAndPrimary = " ";
					            
					            columnDescription = columnDescription + constAndPrimary;
					            
					            System.out.println("columnList:" + columnList);
					            System.out.println("columnDescription:" + columnDescription);
					            
					            polVO.setGenTableName(genTableName.toLowerCase());
					            polVO.setTable_name(genTableName.toLowerCase());
					            polVO.setColumnList(columnList);
					            polVO.setColumnDescription(columnDescription);
					            polVO.setOrgfile_name(orginFileName);
					            
					            polVO.setGenTableName_diag(genTableName_diag.toLowerCase());
					            polVO.setTable_name_diag(genTableName_diag.toLowerCase());
					            polVO.setColumnList_diag(columnList_diag);
					            polVO.setColumnDescription_diag(columnDescription_diag);					            
					            
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
					            
				            	/**
				            	 * user_info로 부터 emp_no 조회 하여 ip, mac 값 조회하여 가져오기
				            	 * 첫번째 컬럼이 emp_no인가 조사
				            	 */
					            
					            String emp_no = "";
					            PolVO userMstr = new PolVO();
					            				           
					            for(i = 1; i < row_num; i++){
					            	
					            	HashMap ht = new HashMap();
					            	
					            	String valueList = "";
					            	
					            	/**
					            	 * user_info로 부터 emp_no 조회 하여 ip, mac 가져오기
					            	 * param : 첫번째 컬럽 emp_no
					            	 */					            	
					            	emp_no = sheet.getCell(0,i).getContents();
					            	
					            	userMstr = exanalManageService.selectUsermstr(emp_no);
					            	
					            	if(userMstr != null){
					            		//System.out.println(userMstr.getEmp_no() + "::" +userMstr.getSldm_mac() + "::" + userMstr.getSldm_ip());
					            		valueList = "'"+userMstr.getEmp_no()+"','"+userMstr.getSldm_mac()+"','"+userMstr.getSldm_ip()+"', NOW(),";
					            	}else{				            	
					            		valueList = "'','','', NOW(),";
					            	}
					            	
					            	for(hx = 0; hx < col_num; hx++){
					            		ht.put(sheet.getCell(hx,hy).getContents()+i, sheet.getCell(hx,i).getContents());
					            		//System.out.println(sheet.getCell(hx,i).getContents());
					            		valueList += "'" + sheet.getCell(hx,i).getContents() + "'";
					            		if(hx < col_num - 1){
						            		valueList += ",";
						            	}
					            		
					            	}
					            	
					            	polVO.setValueList(valueList);
					            	
					                //System.out.print(sheet.getCell(0,i).getContents());
					                //System.out.print(sheet.getCell(1,i).getContents());
					                //System.out.println(sheet.getCell(2,i).getContents());
					                
					                exanalManageService.insertPolList(polVO);
					                
					            }
					            
					            if(inMode.equals("NEW")){
					            	exanalManageService.insertPolTableInfo(polVO);
					            }
					        }catch(Exception e){
					        	e.printStackTrace();
					            result="[BAD_FILE]";//
					            System.out.print("badfileread");
					        }						
														
						}else{
							//log.info("xls, xlsx 파일 타입만 등록이 가능합니다.");

							return "forward:/handy/ExanalPolListHugel.do";
						}
						// *********** 끝 ***********
					}
				
			    }catch(Exception e){
			    	log.debug(e);
			    }finally{
			    	try{
				    	if(fis!=null){
							fis.close();
				        }	
			    	}catch(Exception ee){
			    		log.debug(ee);
			    	}finally{
			    		fis.close();
			    	}
			    }
			    
				
			}

	        //return "/handy/ExanalPolList";
			return "forward:/handy/ExanalPolListHugel.do";
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
			
//			System.out.println("==============================columnCompare========================");
//			System.out.println(polVO.getColumnList());
//			System.out.println(polVO.getGenTableName());
//			System.out.println("==============================columnCompare========================");
			
			excelColumnList = StringUtil.split(StringUtil.removeWhitespace(polVO.getColumnList()), ",");
			
			List<EgovMap> columnAllList = exanalManageService.getColumnList(polVO);
			
			Boolean matchFlag = false;
	    	
	    	for(int i=0; i<excelColumnList.length; i++){

	    		matchFlag = false;
	    		
	    		for(EgovMap col:columnAllList){
	    			if(col.get("columnname").equals(excelColumnList[i].toLowerCase())){
	    				matchFlag = true;
	    			}else{
	    			}
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
		@RequestMapping("/handy/ExanalDelTableHugel.do")
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
				tableName_diag = "sldm_" + request.getParameter("fileName");
								
				System.out.println("cmd:============"+cmd+"======"+sqno+"=========="+tableName);
				System.out.println("cmd:============"+cmd+"======"+sqno+"=========="+tableName_diag);
				
				polVO.setTable_name(tableName);
				polVO.setTable_name_diag(tableName_diag);
				
				exanalManageService.deleteTable(polVO);
				
				
		        map.put("totalPage", 0);
		        map.put("currentpage", 1);			    
				
		        msg = "Data count : 0";
			    map.put("ISOK", isOk);
			    map.put("MSG", polVO.getMsg());
			    map.put("strList", "");				
				
		    
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
		@RequestMapping("/handy/ExanalDelRecodeHugel.do")
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
				
				sqno = request.getParameter("sqno");
				cmd  = request.getParameter("cmd");
				tableName = request.getParameter("fileName");
				beginDate = request.getParameter("beginDate");
				
				polVO.setBegin_date(polVO.getBegin_date().replace("-", ""));
				
				System.out.println(polVO.getSqno() + "===" + polVO.getTable_name() + "===" + polVO.getBegin_date());
				
				if(cmd.equals("DelRecode")){
					exanalManageService.deleteRecode(polVO);
					isOk = true;
				}
				
				
		        map.put("totalPage", 0);
		        map.put("currentpage", 1);			    
				
		        msg = "Data count : 0";
			    map.put("ISOK", isOk);
			    map.put("MSG", polVO.getMsg());
			    map.put("strList", "");				
				
		    
				response.setContentType("application/json");
				response.setContentType("text/xml;charset=utf-8");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(gson.toJson(map));				
      
		}
		
		/*
		
		
		@RequestMapping("/handy/exanaltestHugel.do")
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


