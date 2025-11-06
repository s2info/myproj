package sdiag.exanal.util;

import java.util.*;
import java.io.File;
import jxl.*;
 
import org.apache.log4j.Logger;

public class ExcelUtils {

	/**
	 * @param args
	 */

	  private static Logger log = Logger.getLogger(ExcelUtils.class);
	  
	  public List xlsForList(String file_path) throws Exception {
	    File file = new File(file_path);
	 
	    if (!file.exists()) {
	      throw new Exception("파일이 존재하지 않습니다.");
	    }
	 
	    Workbook workbook = null;
	    Sheet sheet = null;
	    List list = new ArrayList();
	 
	    try {
	 
	      workbook = Workbook.getWorkbook(file);
	      sheet = workbook.getSheet(0);
	 
	      int row = sheet.getRows();
	      int col = sheet.getColumns();
	 
	      if(row <= 0) { throw new Exception("내용이 없습니다."); }
	 
	      for(int i = 0; i < row ; i++) {
	        HashMap hm = new HashMap();
	        for(int o = 0; o < col ; o++) {
	          hm.put("COL"+o,sheet.getCell(o,i).getContents());
	        }
	 
	        list.add(i,hm);
	      }
	 
	    } catch (Exception e) {
	      e.printStackTrace();
	      throw e;
	    } finally {
	      try {
	        if(workbook != null) { workbook.close(); }
	      } catch (Exception e) { }
	    }
	 
	    return list;
	  }
	

}

 
