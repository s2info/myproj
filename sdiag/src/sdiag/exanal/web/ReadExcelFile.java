package sdiag.exanal.web;  
  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.PrintStream;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;  

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  

import org.apache.poi.hssf.record.BOFRecord;  
import org.apache.poi.hssf.record.BoundSheetRecord;  
import org.apache.poi.hssf.record.LabelSSTRecord;  
import org.apache.poi.hssf.record.NumberRecord;  
import org.apache.poi.hssf.record.Record;  
import org.apache.poi.hssf.record.RowRecord;  
import org.apache.poi.hssf.record.SSTRecord;  
import org.apache.poi.hssf.eventusermodel.HSSFListener;  
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;  
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;  
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;  
import org.apache.poi.openxml4j.opc.OPCPackage;  
import org.apache.poi.openxml4j.opc.PackageAccess;  
import org.apache.poi.ss.usermodel.BuiltinFormats;  
import org.apache.poi.ss.usermodel.DataFormatter;  
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;  
import org.apache.poi.xssf.eventusermodel.XSSFReader;  
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;  
import org.apache.poi.xssf.usermodel.XSSFCellStyle;  
import org.apache.poi.xssf.usermodel.XSSFRichTextString;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;  
import org.xml.sax.ContentHandler;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
import org.xml.sax.XMLReader;  
import org.xml.sax.helpers.DefaultHandler;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.exanal.service.PolVO;
import sdiag.exanal.web.ProcessSheetInterface;  
import sdiag.exanal.service.ExanalManageService;
import sdiag.util.StringUtil;
  
public class ReadExcelFile {  
  
    enum xssfDataType {  
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
    }  
  
    int countrows = 0;  
    
    class XSSFSheetHandler extends DefaultHandler {  
  
  
        /** 
         * Table with styles 
         */  
        private StylesTable stylesTable;  
  
        /** 
         * Table with unique strings 
         */  
        private ReadOnlySharedStringsTable sharedStringsTable;  
  
        /** 
         * Destination for data 
         */  
        private final PrintStream output;  
  
        private List<?> list = new ArrayList();
  
        private Class clazz;  
  
        /** 
         * Number of columns to read starting with leftmost 
         */  
        private final int minColumnCount;  
        private int count=0;  
        String[] strValues = new String[3];  
  
        // Set when V start element is seen  
        private boolean vIsOpen;  
  
        // Set when cell start element is seen;  
        // used when cell close element is seen.  
        private xssfDataType nextDataType;  
  
        // Used to format numeric cell values.  
        private short formatIndex;  
        private String formatString;  
        private final DataFormatter formatter; 
        
        //exist table counts
        private int tableCount = 0;
  
        private int thisColumn = -1;  
        // The last column printed to the output stream  
        private int lastColumnNumber = -1;  
        
        private int totColumns = 0;
  
        // Gathers characters as they are seen.  
        private StringBuffer value;
        
        private String columnDescription = "";
        private String columnDescription_diag = "";
        
        private String column_result = "";
        private String columnList_rsv = "";
        private String columnList = "";
        private String columnList_diag = "";
        
        private String gubun = "";
        private String gubun2 = "";

        private String currRow = "";
        private int currCell = 0;
        
        private String cellDataList = "";
        private List rowDataList = new ArrayList();
        //PolVO polVO = new PolVO();
        PolVO polVO;
        
        private  int mCnt1 = 0;
        private int mCnt2 = 0;
        private int adCnt = 0;
        
        @Resource(name = "ExanalManageService")
        private ExanalManageService exanalManageService;        
  
        /** 
         * Accepts objects needed while parsing. 
         *  
         * @param styles 
         *            Table of styles 
         * @param strings 
         *            Table of shared strings 
         * @param cols 
         *            Minimum number of columns to show 
         * @param target 
         *            Sink for output 
         */  
        public XSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, int cols, PrintStream target, ExanalManageService exanalManageService, PolVO polVO) {

            this.stylesTable = styles;  
            this.sharedStringsTable = strings;  
            this.minColumnCount = cols;  
            this.output = target;  
            this.value = new StringBuffer();  
            this.nextDataType = xssfDataType.NUMBER;  
            this.formatter = new DataFormatter();
            
            this.columnDescription = "";
            this.columnDescription_diag = "";
            this.currRow = "";
            this.currCell = 0; 
            
            this.polVO = polVO;
            
            this.column_result = "";
            
            this.columnList_rsv = "";
            this.columnList = "";
            this.columnList_diag = "";
                        
            this.exanalManageService = exanalManageService;
            
            this.gubun = polVO.getGubun();
            this.gubun2 = polVO.getGubun2();
            /** 예약 키 컬럼리스트 생성 */
            columnList_rsv += "sldm_empno, ";
            columnList_rsv += "sldm_ip, ";
            columnList_rsv += "sldm_mac, ";
            columnList_rsv += "sldm_org_logdate, ";
            

            
            columnDescription += "sldm_empno character varying(20), ";
            columnDescription += "sldm_ip character varying(25), ";
            columnDescription += "sldm_mac character varying(34), ";
            columnDescription += "sldm_org_logdate timestamp without time zone, ";
            

            
            columnList_diag += columnList_rsv;
            columnList_diag += "sldm_policy_id, ";
            columnList_diag += "sldm_explan_flag, ";
            columnList_diag += "sldm_extract_value, ";
            columnList_diag += "sldm_eventdate, ";
            
            columnDescription_diag += columnDescription;
            columnDescription_diag += "sldm_policy_id character varying(5), ";
            columnDescription_diag += "sldm_explan_flag character(1), ";
            columnDescription_diag += "sldm_extract_value integer, "; 
            columnDescription_diag += "sldm_eventdate timestamp without time zone, ";
        }
        
        public void startDocument(){
        	System.out.println("Start document!!");
        	System.out.println("totColumns Start : " + totColumns);
        	
    		if(inMode.equals("NEW")){
				try {
											            	
	            	/** 테이블이 있는지 조회 */
					polVO.setTable_name(genTableName.toLowerCase());
	            	tableCount = exanalManageService.getCountTableName(polVO);
	            	
	            	if(tableCount > 0){
	            		polVO.setMsg(" 테이블명이 존재합니다.");
	            		return;
	            	}			    					
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}       	
        	
        }
        
        public void endDocument(){
        	System.out.println("End document!!"); 
        	
        	// Execute updateSelectUsermstr
        	
        	if( column_result.length() > 0 ){
        		return;
        	}else{
	        	try {
					exanalManageService.updateSelectUsermstr(polVO);
					
					if(gubun.equals("G")){
		           
		            	String wanValueList = "emp_no,'','',NOW(),emp_no,";
		            	
		            	for(String column :polVO.getColumnList().split(",")){
		            		column = column.replaceAll(" ", "");
		            		if(column.equals("d_flag")){
		            			wanValueList += "'N',";
		            		}else if(column.equals("ep_flag")){
		            			wanValueList += "'','N'";
		            			break;
		            		}else if(!column.equals("sldm_empno") && !column.equals("sldm_ip") && !column.equals("sldm_mac") && !column.equals("sldm_org_logdate") && !column.equals("empno") && !column.equals("log_yn")){
		            			wanValueList += "'',";
		            		}
		            	}
		            
		            	polVO.setValueList(wanValueList);
		            	
		            	exanalManageService.insertPolWanList(polVO);
		            }
					 if(inMode.equals("NEW") && tableCount == 0 ){
			            	
		            	exanalManageService.insertPolTableInfo(polVO);
		            }				
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        }
  
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {  
        	
        	//System.out.println("<----------------------------------startElement:name:-------------------------->" + name);
        	
        	if ("row".equals(name)) {
        		currRow = attributes.getValue(0);
        		currCell = 0;
        		cellDataList = "";
        		for(int i=0;i<=totColumns;i++){
    	    		rowDataList.add(i, "'',");
    	    	}
        	}
        	
            if ("inlineStr".equals(name) || "v".equals(name)) {  
                vIsOpen = true;  
                // Clear contents cache  
                value.setLength(0);  
                currCell++;
            }  
            // c => cell  
            else if ("c".equals(name)) {
            	            	
                // Get the cell reference 
                String r = attributes.getValue("r");
                int firstDigit = -1; 
                for (int c = 0; c < r.length(); ++c) {  
                    if (Character.isDigit(r.charAt(c))) {  
                        firstDigit = c;
                        break;  
                    }  
                }  
                thisColumn = nameToColumn(r.substring(0, firstDigit));
 
                // Set up defaults.  
                this.nextDataType = xssfDataType.NUMBER;  
                this.formatIndex = -1;  
                this.formatString = null;
                String cellType = attributes.getValue("t");  
                String cellStyleStr = attributes.getValue("s");
                
                if ("b".equals(cellType))  
                    nextDataType = xssfDataType.BOOL;  
                else if ("e".equals(cellType))  
                    nextDataType = xssfDataType.ERROR;  
                else if ("inlineStr".equals(cellType))  
                    nextDataType = xssfDataType.INLINESTR;  
                else if ("s".equals(cellType))  
                    nextDataType = xssfDataType.SSTINDEX;  
                else if ("str".equals(cellType))  
                    nextDataType = xssfDataType.FORMULA;  
               
                else if (cellStyleStr != null) {  
                    // It's a number, but almost certainly one  
                    // with a special style or format  
                    int styleIndex = Integer.parseInt(cellStyleStr);  
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);  
                    this.formatIndex = style.getDataFormat();  
                    this.formatString = style.getDataFormatString();  
                    if (this.formatString == null)  
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);  
                }
            }
        }  
          
        public void endElement(String uri, String localName, String name) throws SAXException { 
        	
        	try{
	        	//System.out.println("<----------------------------------name::endElement-------------------------->" + name);  

	            String thisStr = null;
	  
	            // v => contents of a cell  
	            if ("v".equals(name)) {
	                // Process the value contents as required.  
	                // Do now, as characters() may be called more than once  
	                switch (nextDataType) {  
	  
	                case BOOL:  
	                    char first = value.charAt(0);  
	                    thisStr = first == '0' ? "FALSE" : "TRUE";  
	                    break;  
	  
	                case ERROR:  
	                    thisStr = "ERROR:" + value.toString() + '"';  
	                    break;  
	  
	                case FORMULA:  
	                    // A formula could result in a string value,  
	                    // so always add double-quote characters.  
	                    thisStr = value.toString();  
	                    break;  
	  
	                case INLINESTR:  
	                    XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());  
	                    thisStr = rtsi.toString();  
	                    break;  
	  
	                case SSTINDEX:  
	                    String sstIndex = value.toString();  
	                    try {  
	                        int idx = Integer.parseInt(sstIndex);  
	                        XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));  
	                        thisStr = rtss.toString();  
	                    } catch (NumberFormatException ex) {  
	                        output.println("Failed to parse SST index '" + sstIndex  + "': " + ex.toString());  
	                    }  
	                    break;  
	  
	                case NUMBER:  
	                    String n = value.toString();  
	                    if (this.formatString != null)  
	                        thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);  
	                    else  
	                        thisStr = n;  
	                    break; 
	                    
	                default:  
	                    thisStr = "(TODO: Unexpected type: " + nextDataType + ")";  
	                    break;  
	                }  
	                
	                // Output after we've seen the string contents  
	                // Emit commas for any fields that were missing on this row  
	                if (lastColumnNumber == -1) {  
	                    lastColumnNumber = 0;  
	                }  
	                /*   for (int i = lastColumnNumber; i < thisColumn; ++i) 
	                    output.print(',');*/  
	                
	  
	                // Might be the empty string.  
	                // handlerInterface.processEachRecord(thisColumn,countrows,thisStr);
	               
	                if(thisColumn == 1 || thisColumn ==0 || thisColumn==2) 
	                        strValues[thisColumn] = thisStr; 
	                
	                // Update column  
	                if (thisColumn > -1)  
	                    lastColumnNumber = thisColumn; 
	                
	                // Catch cell number for emp_no column 
	                // It is used to head column for first row. One of columns must be "emp_no"
//	                if(currRow.equals("1") && thisStr.equals("emp_no")){ 	
//	                	set empNoCell to currCell number
//	                	empNoCell = currCell;
//	                }
	                
//	                System.out.println(">>>>>>>>>>>>>>>>>>>>>lastColumnNumber:::"+lastColumnNumber);
	                System.out.println(">>>>>>>>>>>>>>>>>>>>>thisColumnNumber:::"+thisColumn);
	                System.out.println(">>>>>>>>>>>>>>>>>>>>>totColumns:::"+totColumns);

	                if(currRow.equals("1")){
	                	columnList += thisStr + ",";
	                	columnDescription += thisStr + " character varying(500) " + ",";
	                	
	                	columnList_diag += thisStr + ", ";
	                	columnDescription_diag += thisStr + " character varying(500) " + ",";
	                }else{	                	
	                	cellDataList += "'" + thisStr.replace("'", "") + "' ";
	                	
	                	//if(thisColumn < totColumns){ 
	                		cellDataList += " , ";
	                //	}
	                	
//	                	if(empNoCell == currCell){
//	                		//Extract current Row emp_no
//	                		empNoRowValue = thisStr;
//	                    }
	                		
	                	rowDataList.add(thisColumn, "'" + thisStr.replace("'", "") + "',");
	                }
	                	  
	            } else if ("row".equals(name)) {  
	            		            	
	                // Print out any missing commas if needed  
	                if (minColumns > 0) {  
	                    // Columns are 0 based  
	                    if (lastColumnNumber == -1) {  
	                        lastColumnNumber = 0;  
	                    }  
	                  /*  for (int i = lastColumnNumber; i < (this.minColumnCount); i++) { 
	                        output.print(','); 
	                    }*/
	                } 
	                
	  
	                //handlerInterface.rowCompleted();  
	                // We're onto a new row  
	                if(strValues[1].equals("Y")) {                    
	                	output.println("found : "+strValues[0]+"       "+strValues[2]); 
	                	count++; 
	                }  
	                countrows++;  
	                lastColumnNumber = -1;  

	                
	            	
	                if(tableCount > 0){
	            		polVO.setMsg(" 테이블명이 존재합니다.");
	            		return;                	
	                }else{
		                // If end row number is 1 then execute create table
		                if(currRow.equals("1")){
		                	
		                	totColumns = thisColumn;
		                	
		                	
		                	if(gubun.equals("G")){
		                		polVO.setColumnList(columnList_rsv + columnList + "ep_flag, log_yn");	
		                		polVO.setColumnDescription(columnDescription + "ep_flag character varying(500), log_yn character varying(500)");	
		                		polVO.setColumnList_diag(columnList_diag + "ep_flag, log_yn");
						        polVO.setColumnDescription_diag(columnDescription_diag + "ep_flag character varying(500), log_yn character varying(500)");	
				            }else if(gubun.equals("S")){
				            	String [] cArr = columnList.split(",");
				            	int rowCnt = 0;
				            	for(String column : cArr){
				            		column = column.replaceAll(" ", "");
				            		if(column.equals("조치예정일")||column == "조치예정일" ){
				            			mCnt1++;
				            			adCnt = rowCnt;
				            		}else if(column.equals("조치여부") ||column == "조치여부"){
				            			mCnt2++;
				            		}
				            		rowCnt++;
				            	} 
				            	
				            	if(mCnt1 == 0 ){
				            		columnList = columnList + " 조치예정일,";	
				            		columnDescription = columnDescription + " 조치예정일 character varying(500),";	
				            		columnList_diag = columnList_diag + " 조치예정일,";
				            		columnDescription_diag = columnDescription_diag + " 조치예정일 character varying(500), ";
				            	}
				            	if(mCnt2 == 0){
				            		columnList = columnList + " 조치여부, ";	
				            		columnDescription =columnDescription + " 조치여부 character varying(500), ";	
				            		columnList_diag = columnList_diag + " 조치여부, ";
				            		columnDescription_diag = columnDescription_diag + " 조치여부 character varying(500), ";
				            	}
				            	
				            	polVO.setColumnList(columnList_rsv + columnList + "ep_flag,  비고, seq");	
		                		polVO.setColumnDescription(columnDescription + "ep_flag character varying(500), 비고 text, seq character varying(500)");	
		                		polVO.setColumnList_diag(columnList_diag + "ep_flag,비고, seq");
						        polVO.setColumnDescription_diag(columnDescription_diag + "ep_flag character varying(500),  비고 text, seq character varying(500)");
				            }else{
				            	polVO.setColumnList(columnList_rsv + columnList + "ep_flag");	
		                		polVO.setColumnDescription(columnDescription + "ep_flag character varying(500)");	
		                		polVO.setColumnList_diag(columnList_diag + "ep_flag");
						        polVO.setColumnDescription_diag(columnDescription_diag + "ep_flag character varying(500)");				            
				            }
		                	
		                	polVO.setGenTableName(genTableName.toLowerCase());
		                	polVO.setTable_name(genTableName.toLowerCase());
		                	
		                	if(!inMode.equals("NEW")){
		                	
			                	/* 컬럼비교 */
			                	column_result = columnCompare(polVO);
		                	
		                	}
		                	
				            		            
				            polVO.setGenTableName_diag("sldm_" + genTableName.toLowerCase());
				           		            
				            polVO.setOrgfile_name(orginFileName);
				            polVO.setGubun(gubun);
				            polVO.setGubun2(gubun2);
				            
				            
		                	
		                	if(column_result.length() > 0){
		                		polVO.setMsg(column_result + " 컬럼이 일치하지 않습니다.");
		                		return;
		                	}else{
			                    
		                		if(inMode.equals("NEW")){
				    				try {
						            		exanalManageService.createPolTable(polVO);  // 테이블 생성					            					    					
				    				} catch (Exception e) {
				    					e.printStackTrace();
				    				}
		                		}
		                	}
		                }else{ //currRow.equals("1") end
		                	
		                	if( column_result.length() > 0 ){
		                		return;
		                	}else{
			                	// insert data query			           		            	
		                		// insert data query			           		            	
				            	String valueList = "";
				            	
				            	/* user_mstr 정보처리는 입력 완료 후 처리, Go endDocument() */
				            	valueList = "'','','', NOW(),";
				            	
				            					            	
				            	//valueList = valueList + cellDataList;
				            	
				            	
				            	for(int i=0;i<=totColumns;i++){
				            		if(mCnt1 > 0 && adCnt==i){
				            			String ad = (String)rowDataList.get(i);
				            			if(ad.length() >11){
				            				polVO.setMsg("조치예정일 값을 확인 하여주시기 바랍니다. \n ex)20190701 유형으로 등록하여 주시기 바랍니다.");
					                		return;
				            			}
				            		}
				            		valueList = valueList + (String)rowDataList.get(i);
				            	}
				            	
				            	
				            	
				            	if(gubun.equals("G")){
				            		valueList = valueList + "'', 'Y'";		
					            }else if(gubun.equals("S")){
					            	int cnt = Integer.parseInt(currRow)-1;
					            	
					            	if(mCnt1 == 0 ){
					            		valueList = valueList + "'', ";	
					            	}
					            	if(mCnt2 == 0 ){
					            		valueList = valueList + "'', ";	
					            	}
					            	
					            	valueList = valueList + "'','','"+cnt+"'";		
					            }else{
					            	valueList = valueList + "''";					            
					            }
				            	
				            	polVO.setValueList(valueList);
				                exanalManageService.insertPolList(polVO);	                		
		                	}
				                
		                } // currRow.equals("1") else end
	                }// tableCount > 0 end

	            } 
      
      
        	}catch(Exception e){
        		e.printStackTrace();
        	}
  
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
			
			excelColumnList = StringUtil.split(StringUtil.removeWhitespace(columnList), ",");
						
			List<EgovMap> columnAllList = exanalManageService.getColumnList(polVO);
			
			Boolean matchFlag = false;
	    	
	    	for(int i=0; i<excelColumnList.length - 1; i++){
	    		
	    		matchFlag = false;
	    			    		
	    		for(EgovMap col:columnAllList){
	    				    			
	    			if(col.get("columnname").equals(excelColumnList[i].toLowerCase())){
	    				matchFlag = true;
	    			}
	    			
	    			allDbColumnList += col.get("columnname");
	    		}
	    		if(!matchFlag){
	    			compareResult += " || " + excelColumnList[i];
	    		}
	    			    		
	    	}
	    		
			return compareResult;
			
		}

		/** 
         * Captures characters only if a suitable element is open. Originally 
         * was just "v"; extended for inlineStr also. 
         */  
        public void characters(char[] ch, int start, int length) throws SAXException { 
        	
            if (vIsOpen)  
                value.append(ch, start, length);  
        }  
  
        /** 
         * Converts an Excel column name like "C" to a zero-based index. 
         *  
         * @param name 
         * @return Index corresponding to the specified name 
         */  
        private int nameToColumn(String name) {  
            int column = -1;  
            for (int i = 0; i < name.length(); ++i) {  
                int c = name.charAt(i);  
                column = (column + 1) * 26 + c - 'A';  
            }  
            return column;  
        }
        
        public StringBuffer getValuex(){
        	return value;
        }
  
    }  
  
    //
    //
  
    private OPCPackage xlsxPackage;  
    private int minColumns;  
    private PrintStream output;
	private String genTableName;
	private String inMode;
	private String orginFileName;
	private String gubun="";
	private String gubun2="";
     
  
    /** 
     * Creates a new XLSX -> CSV converter 
     *  
     * @param pkg 
     *            The XLSX package to process 
     * @param output 
     *            The PrintStream to output the CSV to 
     * @param minColumns 
     *            The minimum number of columns to output, or -1 for no minimum 
     * @param exanalManageService 
     */  
    private ReadExcelFile(OPCPackage pkg, PrintStream output, int minColumns,ProcessSheetInterface handlerInterface,
    		int sheetNo, String genTableName, ExanalManageService exanalManageService,
    		String inMode, String orginFileName, PolVO polVO) {  
		this.xlsxPackage = pkg;  
		this.output = output;  
		this.minColumns = minColumns;  
		this.genTableName = genTableName;
		this.inMode = inMode;
		this.orginFileName = orginFileName;
		this.gubun = polVO.getGubun();
		this.gubun2 = polVO.getGubun2();

		try{  
			process(sheetNo, exanalManageService, polVO); 
		}catch(Exception ex){  
			polVO.setMsg("수동업로드 처리 중 오류가 발생하였습니다.");
			output.println(ex);  
		}  
    }  

	/** 
     * Parses and shows the content of one sheet using the specified styles and 
     * shared-strings tables. 
     *  
     * @param styles 
     * @param strings 
     * @param sheetInputStream 
     */  
    public void processSheet(StylesTable styles,  
            ReadOnlySharedStringsTable strings, InputStream sheetInputStream, ExanalManageService exanalManageService, PolVO polVO)  
            throws IOException, ParserConfigurationException, SAXException {
		
        InputSource sheetSource = new InputSource(sheetInputStream); 
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        ContentHandler handler = new XSSFSheetHandler(styles, strings, this.minColumns, this.output, exanalManageService, polVO);
        sheetParser.setContentHandler(handler);
        sheetSource.getByteStream();
        sheetParser.parse(sheetSource);
    }  
  
    /** 
     * Initiates the processing of the XLS workbook file to CSV. 
     * @throws Exception 
     */  
    public void process(int sheetNo, ExanalManageService exanalManageService, PolVO polVO) throws Exception {  
  
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);  
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
  
        StylesTable styles = xssfReader.getStylesTable();  
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData(); 

        int index = 1;

        while (iter.hasNext()) {  
            InputStream stream = iter.next();            

            if(index==sheetNo)  
            {  
	            String sheetName = iter.getSheetName();
	            System.out.println("<------------------------sheetName----------------------->" + sheetName);
	            processSheet(styles, strings, stream, exanalManageService, polVO);  
            }  
            stream.close();  
            ++index;  
           
        } 
        
        
    } 

//	int sheetNo = 1;
//	ProcessSheetInterface handlerObject = null;
//	PrintStream output = null;
//	int maxColoumnCount = 15;
//	
//	ReadExcelFile.ProcessExcelSheet(filePath, sheetNo, handlerObject, output, maxColoumnCount);     
    
    public static String ProcessExcelSheet(InputStream ins,int sheetNo,ProcessSheetInterface handlerObject,PrintStream output,
    		int maxColoumnCount, String genTableName, ExanalManageService exanalManageService, String inMode, String orginFileName, PolVO polVO) 
    		throws IOException, OpenXML4JException, ParserConfigurationException, SAXException  {  
    	
		Class clazz = ReadExcelFile.class;  
		PrintStream out;  
		if(output!=null)  
			out = output;  
		else  
			out= System.out;  
		try{
			
			//OPCPackage excelPackage = OPCPackage.open(filePath);
			OPCPackage excelPackage = OPCPackage.open(ins);
			
			//genTableName = "";
			new ReadExcelFile(excelPackage,System.out,maxColoumnCount,handlerObject,sheetNo, genTableName, exanalManageService, inMode, orginFileName, polVO);	
			
			//excelPackage.close();
			
		} catch (InvalidOperationException exe) {  
			org.apache.poi.openxml4j.opc.OPCPackage pkg = org.apache.poi.openxml4j.opc.OPCPackage.open(ins);  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			polVO.setMsg("수동업로드 처리 중 오류가 발생하였습니다.");
			e.printStackTrace();
		}
		return orginFileName;  
         
       
     }

    
   
}  
