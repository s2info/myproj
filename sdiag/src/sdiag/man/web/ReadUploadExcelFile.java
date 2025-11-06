package sdiag.man.web;  
  
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
import sdiag.man.service.OrgStatisticService;
import sdiag.man.vo.StatInfoVO;
import sdiag.util.StringUtil;
  
public class ReadUploadExcelFile {  
  
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
        private String column_result = "";
        // Gathers characters as they are seen.  
        private StringBuffer value;
        
        
        private String gubun = "";
        private int columnCnt = 0;
        private String gubun2 = "";

        private String currRow = "";
        private int currCell = 0;
        
        private String cellDataList = "";
        private List rowDataList = new ArrayList();
        //PolVO polVO = new PolVO();
        HashMap<String,Object> rMap = new HashMap<String,Object>();
        List<HashMap<String,Object>> scoreList = new ArrayList<HashMap<String,Object>>(); 
        
        HashMap<String,Object> sMap = new HashMap<String,Object>();
        
    	@Resource(name="OrgStatisticService")
    	private OrgStatisticService orgStatService;

		private StatInfoVO statInfoVo;      
  
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
        public XSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, int cols, PrintStream target, OrgStatisticService orgStatService, StatInfoVO statInfoVo) {

            this.stylesTable = styles;  
            this.sharedStringsTable = strings;  
            this.minColumnCount = cols;  
            this.output = target;  
            this.value = new StringBuffer();  
            this.nextDataType = xssfDataType.NUMBER;  
            this.formatter = new DataFormatter();
            
            this.currRow = "";
            this.currCell = 0; 
            this.rMap=rMap;
            this.statInfoVo = statInfoVo;
            this.rMap = rMap;
            this.sMap = sMap;
            this.scoreList = scoreList;
            this.orgStatService = orgStatService;
            
        }
        
        public void startDocument(){
        	System.out.println("Start document!!");
        	System.out.println("totColumns Start : " + totColumns);
        	
        	
        	rMap.put("polIdxId", statInfoVo.getPolIdxId());
			rMap.put("rgdtEmpNo", statInfoVo.getRgdtEmpNo());
			rMap.put("statSeq", statInfoVo.getStatSeq());
        	
        }
        
        public void endDocument(){
        	System.out.println("End document!!"); 
        	
        	// Execute updateSelectUsermstr
        	
        	if( column_result.length() > 0 ){
        		return;
        	}else{
	        	try {
	        		
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

	                if(Integer.parseInt(currRow) > 1){
	                	cellDataList += "'" + thisStr.replace("'", "") + "' ";
	                	
	                	//if(thisColumn < totColumns){ 
	                		cellDataList += " , ";
	                //	}
	                	
//	                	if(empNoCell == currCell){
//	                		//Extract current Row emp_no
//	                		empNoRowValue = thisStr;
//	                    }
	                		
	                	rowDataList.add(thisColumn, thisStr.replace("'", "") );
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

	                
	            	
	              
	                // If end row number is 1 then execute create table
	                if(Integer.parseInt(currRow) > 1){
	                	
	                	totColumns = thisColumn;
	                	
	                	if( column_result.length() > 0 ){
	                		return;
	                	}else{
		                	// insert data query			           		            	
	                		// insert data query			           		            	
			            	String valueList = "";
			            	
			            	/* user_mstr 정보처리는 입력 완료 후 처리, Go endDocument() */
			            	valueList = "";
			            	
			            					            	
			            	//valueList = valueList + cellDataList;
			            	
			            	
			            	for(int i=0;i<=1; i++){
			            		scoreList.clear();
			            		if(i==0){
			            			sMap.put("empno", String.valueOf(rowDataList.get(0)));
			            		}else{
			            			sMap.put("score", Integer.parseInt(rowDataList.get(1).toString()));
			            		}
			            		
			            	}
			            	scoreList.add(sMap);                		
	                	}
	                	rMap.put("scoreList", scoreList);
		    			
		        		orgStatService.statUploadScoreInsert(rMap);    
	                } // currRow.equals("1") else end
                }// tableCount > 0 end

      
      
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
     * @param orgStatService 
     */  
    private ReadUploadExcelFile(OPCPackage pkg, PrintStream output, int minColumns,ProcessSheetInterface handlerInterface,
    		int sheetNo,  OrgStatisticService orgStatService,
    		 String orginFileName, StatInfoVO statInfoVo) {  
		this.xlsxPackage = pkg;  
		this.output = output;  
		this.minColumns = minColumns;  
		this.orginFileName = orginFileName;


		try{  
			System.out.println("여기");
			process(sheetNo, orgStatService, statInfoVo); 
		}catch(Exception ex){  
			statInfoVo.setMsg("수동업로드 처리 중 오류가 발생하였습니다.");
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
            ReadOnlySharedStringsTable strings, InputStream sheetInputStream, OrgStatisticService orgStatService, StatInfoVO statInfoVo)  
            throws IOException, ParserConfigurationException, SAXException {
		
        InputSource sheetSource = new InputSource(sheetInputStream); 
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        ContentHandler handler = new XSSFSheetHandler(styles, strings, this.minColumns, this.output, orgStatService, statInfoVo);
        sheetParser.setContentHandler(handler);
        sheetSource.getByteStream();
        sheetParser.parse(sheetSource);
    }  
  
    /** 
     * Initiates the processing of the XLS workbook file to CSV. 
     * @throws Exception 
     */  
    public void process(int sheetNo, OrgStatisticService orgStatService, StatInfoVO statInfoVo) throws Exception {  
  
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
	            processSheet(styles, strings, stream, orgStatService, statInfoVo);  
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
    		int maxColoumnCount, OrgStatisticService orgStatService, String orginFileName, StatInfoVO statInfoVo) 
    		throws IOException, OpenXML4JException, ParserConfigurationException, SAXException  {  
    	
		Class clazz = ReadUploadExcelFile.class;  
		PrintStream out;  
		if(output!=null)  
			out = output;  
		else  
			out= System.out;  
		try{
			
			//OPCPackage excelPackage = OPCPackage.open(filePath);
			OPCPackage excelPackage = OPCPackage.open(ins);
			
			//genTableName = "";
			new ReadUploadExcelFile(excelPackage,System.out,maxColoumnCount,handlerObject,sheetNo, orgStatService, orginFileName, statInfoVo);	
			System.out.println("여기");
			//excelPackage.close();
			
		} catch (InvalidOperationException exe) {  
			org.apache.poi.openxml4j.opc.OPCPackage pkg = org.apache.poi.openxml4j.opc.OPCPackage.open(ins);  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statInfoVo.setMsg("수동업로드 처리 중 오류가 발생하였습니다.");
			e.printStackTrace();
		}
		return orginFileName;  
         
       
     }

    
   
}  
