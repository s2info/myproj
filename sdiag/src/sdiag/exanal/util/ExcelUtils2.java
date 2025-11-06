package sdiag.exanal.util;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import sdiag.exanal.util.TestUploadDTO;

public class ExcelUtils2 {
	public static void doExecute(HttpServletRequest req, HttpServletResponse res) throws Exception {
	    try {     
	        //TestUploadDTO dto = new TestUploadDTO();
	       // dto = (TestUploadDTO)req.getAttribute("dto");
	       
	        int rv = 0;
	        String result = "[SUCESS_UPLOAD]";
	        String TestPath = "/";
	       
	        String fileName = null2Str(req.getParameter("fileName"));
	       
	        int col_num = 0;                           // 열수
	        int row_num = 0;                           // 행수     
	        
	        System.out.println("fileName:"+fileName);
	       
	        try{           
	            Workbook workbook = Workbook.getWorkbook(new File(TestPath + fileName));
	            
	            Sheet sheet = workbook.getSheet(0);
	           
	            col_num = sheet.getColumns();                  
	            row_num = sheet.getRows();  
	            
	            System.out.print("col_num:"+col_num);
	           
	            Hashtable ht = new Hashtable();
	            
	            int j = 0;
	           
	            for(int i = 1; i < row_num; i++){ 
	               
	                //dto.setRow(Integer.toString(i));
	                ht.put("Testnumber" ,sheet.getCell(0,i).getContents());                   
	                ht.put("aaaaa"      ,sheet.getCell(1,i).getContents());
	                ht.put("bbbbb"      ,sheet.getCell(2,i).getContents());
	                                   
	                try{
	                    //rv = dto.saveTestYn(ht,dto);          
	                    //if(rv==1){
	                    
	                    //}else{                             
	                    //    result="[FAIL_UPLOAD]";
	                    //}
	                }catch(Exception e){                      
	                    //int badrow = badrow + j;
	                    result="[FAIL_UPLOAD]";
	                }                              
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	            result="[BAD_FILE]";//
	            System.out.print("badfileread");
	        }
	        //3. 서버에 저장한 엑셀 파일 삭제 Start
	        //File delfile = new File(TestPath + fileName);
	       
	        //if (delfile.delete()) {
	           
	        //} else {
	           
	        //}

	        //3. 서버에 저장한 엑셀 파일 삭제 End

	        //req.setAttribute("result",result);           
	       
	    }catch(Exception e){
	        //e.printStackTrace();           
	        throw e;
	    }
	}

	private static String null2Str(String word) {
		// TODO Auto-generated method stub
		return word == null ? "" : word;
	}
}
