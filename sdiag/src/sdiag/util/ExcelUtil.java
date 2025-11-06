package sdiag.util;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.ExcelInitVO;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVWriter;



public class ExcelUtil {

	public static void hashMapToExportCsv(HttpServletResponse response, String fileName, List<String[]> contents) throws IOException{
		CSVWriter writer = null;
		try{
			response.setContentType("text/csv;charset=MS949");
			response.setCharacterEncoding("MS949");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
			
			writer = new CSVWriter(response.getWriter(), ',');
			
			for(String[] str:contents){
				writer.writeNext(str);
			}
			
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
	}
	
	public static void stringToExportCsv(HttpServletResponse response,
			String stringbody,
			String fileName) throws Exception{
		
		
		response.setContentType("text/csv;charset=MS949");
		response.setCharacterEncoding("MS949");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
		
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(0xEF);
		outputStream.write(0xBB);
		outputStream.write(0xBF);
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "euc-kr"));
		
		outputStream.write(stringbody.getBytes() );
		
		outputStream.flush();
		outputStream.close();
		writer.flush();
		writer.close();
	}
	
	public static void htmlStirngToExportExcel(HttpServletResponse response,
			ExcelInitVO excelVo,
			List<EgovMap> val) throws Exception{
		
		/*
		 * FileName = HttpUtility.UrlEncode(FileName, System.Text.Encoding.UTF8).Replace("+", "%20");
        HttpResponse response = HttpContext.Current.Response;
        response.Clear();
        response.Buffer = true;
        response.ContentType = "application/vnd.ms-excel";
        response.Charset = "utf-8";
        response.ContentEncoding = System.Text.Encoding.GetEncoding("utf-8");
        response.Write("<meta http-equiv=Content-Type content=''text/html; charset=utf-8''>");
        response.AddHeader("content-disposition", "attachment;filename=\"" + FileName + "\"");

        response.Write(html_string);
       // response.Flush();
        response.End();
		 * */
		StringBuffer HtmlString = new StringBuffer();
		
		List<String> head = excelVo.getHead();
		HtmlString.append("<meta http-equiv=Content-Type content=''application/vnd.ms-excel;charset=euc-kr''>");
		HtmlString.append("<table cellpadding='0' cellspacing='0'>");
		HtmlString.append("<tr>");
		for(String str:head)
		{
			HtmlString.append(String.format("<th>%s</th>", str));
		}
		HtmlString.append("</tr>");
		
		for(EgovMap row:val)
		{
			HtmlString.append("<tr>");
			for(Object key:row.keySet()){
				HtmlString.append(String.format("<td>%s</td>", row.get(key)));
			}
			HtmlString.append("</tr>");
		}
		HtmlString.append("</table>");
		String style = "<style>.textmode {mso-number-format:\\@;}</style>";
		PrintWriter writer = response.getWriter();
		
		String FileName = URLEncoder.encode(excelVo.getFileName(), "UTF-8").replace("\\", "%20");
		
		response.setContentType("application/vnd.ms-excel;charset=euc-kr");
		response.setCharacterEncoding("euc-kr");
		response.setHeader("Content-Disposition", "attachment; filename=" + FileName + ".xls");
		
		writer.println(style);
		writer.println(HtmlString.toString());
		
		response.flushBuffer();
		
	}
	
	/**
	 * 2007버전 이하
	 * @param response
	 * @param fName
	 * @throws Exception
	 */
	public static void xssExcelDown(HttpServletResponse response,
								ExcelInitVO excelVo,
								List<EgovMap> val) throws Exception{
		
		/******************************************************************
		 * response header setting
		 ******************************************************************/
		String FileName = URLEncoder.encode(excelVo.getFileName(), "UTF-8").replace("\\", "%20");
		response.setContentType("application/ms-excel");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + FileName + "."+excelVo.getType());

		
		/******************************************************************
		 * 엑셀 만들기
		 ******************************************************************/
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(excelVo.getSheetName());
		
		ExcelDown eic = new ExcelDown();
		eic.downExcel(response, wb, sheet, excelVo, val);
	}
	
	/**
	 * 2007버전 이상
	 * @param response
	 * @param fName
	 * @throws Exception
	 */
	public static void hssExcelDown(HttpServletResponse response,
								ExcelInitVO excelVo,
								List<EgovMap> val) throws Exception{
		
		/******************************************************************
		 * response header setting
		 ******************************************************************/
		String FileName = URLEncoder.encode(excelVo.getFileName(), "UTF-8").replace("\\", "%20");
		response.setContentType("application/ms-excel");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + FileName + ".xls");

		
		/******************************************************************
		 * 엑셀 만들기
		 ******************************************************************/
		HSSFWorkbook wb = new HSSFWorkbook();		
		HSSFSheet sheet = wb.createSheet(excelVo.getSheetName());		// sheet 생성
		
		ExcelDown eic = new ExcelDown();
		eic.downExcel(response, wb, sheet, excelVo, val);
	}
	
	
	
	/**
	 * 엑셀 다운로드 외부 호출
	 * @param response
	 * @param excelVo
	 * @param val
	 * @throws Exception
	 */
	public static void excelDown(HttpServletResponse response, ExcelInitVO excelVo, List<EgovMap> val) throws Exception{
		/**
		 * 오류 체크 로직
		 * 1. type이 있다 없다
		 * 2. header 값이랑 val 값 길이가 같은지 
		 */
		
		if (excelVo.getType().equals("xls")){
			ExcelUtil.hssExcelDown(response, excelVo, val);
		}
		else if (excelVo.getType().equals("xlsx")){
			ExcelUtil.xssExcelDown(response, excelVo, val);
		}
		
		
	}
}
