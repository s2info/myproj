package sdiag.util;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.ExcelInitVO;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDown {

	public void downExcel(HttpServletResponse response,
							Object wb,
							Object sheet,
							ExcelInitVO excelVo,
							List<EgovMap> val) throws Exception{			
		int _row = 0;
		List<String> head = excelVo.getHead();
		
		/***********************************
		* title
		***********************************/
		Row row = null;
		row = this.getSheetRow(sheet, row, _row); //sheet.createRow(_row);
		for (int i = 0; i < head.size(); i++){
			Cell cell = row.createCell(i);
			if (i == 0) cell.setCellValue(excelVo.getTitle());
			
			CellStyle cellStyle = this.getcellStyle(wb);
			cellStyle.setVerticalAlignment(cellStyle.VERTICAL_CENTER);
			cellStyle.setAlignment(cellStyle.ALIGN_CENTER);					// 글씨 정렬 센터 정렬
			Font _font = this.getFont(wb);									// font create
			_font.setBoldweight(Font.BOLDWEIGHT_BOLD);						// font bold
			_font.setFontName("굴림");									// font 글씨체
			cellStyle.setFont(_font);										// font set
			cellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);		// cell 배경색 지정
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);			// cell 배경색 패턴
			cell.setCellStyle(cellStyle);
		}
		
		this.setCellMerged(sheet,_row,head);
		_row++;
		
		/***********************************
		* head
		***********************************/
		row = this.getSheetRow(sheet, row, _row);
		for (int i = 0; i < head.size(); i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(head.get(i));
			if (sheet instanceof HSSFSheet){
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			}
			else if (sheet instanceof XSSFSheet){
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			} 
			CellStyle cellStyle = this.getcellStyle(wb);
			cellStyle.setWrapText(true);
			cellStyle.setVerticalAlignment(cellStyle.VERTICAL_CENTER);
			cellStyle.setAlignment(cellStyle.ALIGN_CENTER);					// 글씨 정렬 센터 정렬
			Font _font = this.getFont(wb);									// font create
			_font.setBoldweight(Font.BOLDWEIGHT_BOLD);						// font bold
			_font.setFontName("굴림");									// font 글씨체
			cellStyle.setFont(_font);										// font set
			cellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);		// cell 배경색 지정
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);			// cell 배경색 패턴
			cellStyle.setBorderLeft(cellStyle.BORDER_THIN);					// cell 왼쪽 테두리
			cellStyle.setBorderRight(cellStyle.BORDER_THIN);				// cell 오른쪽 테두리
			cellStyle.setBorderBottom(cellStyle.BORDER_THIN);				// cell 아래쪽 테두리
			cellStyle.setBorderTop(cellStyle.BORDER_THIN);					// cell 윗쪽 테두리
			cell.setCellStyle(cellStyle);
		}
		if(excelVo.isHeadMergeEqualValue()){
			this.setEqualCellMerged(sheet,_row, head);
		}
		_row++;	
		/***********************************
		* body
		***********************************/
		for (int i = 0; i < val.size(); i++){
			row = this.getSheetRow(sheet, row, _row);
			
			EgovMap map = val.get(i);
			List keyList = map.keyList();
			
			for (int j = 0; j < keyList.size(); j++){
				Cell cell = row.createCell(j);
				cell.setCellValue(String.valueOf((Object)map.get((String)keyList.get(j))));
								
				CellStyle cellStyle = this.getcellStyle(wb);
				
				Font _font = this.getFont(wb);									// font create
				_font.setFontName("굴림");									// font 글씨체
				cellStyle.setFont(_font);										// font set
				cellStyle.setVerticalAlignment(cellStyle.VERTICAL_CENTER);
				cellStyle.setBorderLeft(cellStyle.BORDER_THIN);					// cell 왼쪽 테두리
				cellStyle.setBorderRight(cellStyle.BORDER_THIN);				// cell 오른쪽 테두리
				cellStyle.setBorderBottom(cellStyle.BORDER_THIN);				// cell 아래쪽 테두리
				cellStyle.setBorderTop(cellStyle.BORDER_THIN);					// cell 윗쪽 테두리
				cell.setCellStyle(cellStyle);
			}
			_row++;
		}		
		/***********************************
		* cell size 
		***********************************/		
		/*
		for (int i = 0; i < 2; i++){
			
			this.setAutoSizeColumn(sheet, i);
			this.setColumnWidth(sheet, i);		// 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
		}	
		*/
		if(excelVo.isAutoSize()){
			for (int i = 0; i < 2; i++){
				
				this.setAutoSizeColumn(sheet, i);
				this.setColumnWidth(sheet, i);		// 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
			}	
		}
		/******************************************************************
		 * 다운로드를 위해 스트림 작업
		 ******************************************************************/
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		this.setWbWrite(wb, outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		OutputStream outStream = response.getOutputStream();
		outStream.write(outArray);
		outStream.flush();
		outStream.close();
		response.flushBuffer();
	}
	
	
	
	private Row getSheetRow(Object sheet, Row row, int _row) throws Exception{
		if (sheet instanceof HSSFSheet){
			row = ((HSSFSheet) sheet).createRow(_row);
		}
		else if (sheet instanceof XSSFSheet){
			row = ((XSSFSheet) sheet).createRow(_row);
		} 
		
		return row;
	}
	
	private CellStyle getcellStyle(Object wb) throws Exception{
		CellStyle cellStyle = null;
		if (wb instanceof HSSFWorkbook){
			cellStyle = ((HSSFWorkbook) wb).createCellStyle();
		}
		else if (wb instanceof XSSFWorkbook){
			cellStyle = ((XSSFWorkbook) wb).createCellStyle();
		}
		return cellStyle;
	}
	
	private Font getFont(Object wb) throws Exception{
		Font _font = null;
		if (wb instanceof HSSFWorkbook){
			_font = ((HSSFWorkbook) wb).createFont();
		}
		else if (wb instanceof XSSFWorkbook){
			_font = ((XSSFWorkbook) wb).createFont();
		}
		return _font;
	}
	
	private void setCellMerged(Object sheet, int _row, List<String> head) throws Exception{
		if (sheet instanceof HSSFSheet){
			((HSSFSheet) sheet).addMergedRegion(new CellRangeAddress(
					_row,_row,0,head.size() - 1
			));
		}
		else if (sheet instanceof XSSFSheet){
			((XSSFSheet) sheet).addMergedRegion(new CellRangeAddress(
					_row,_row,0,head.size() - 1
			));
		}
	}
	
	private void setEqualCellMerged(Object sheet, int _row, List<String> head)throws Exception{
		if (sheet instanceof HSSFSheet){
			
			for(int i =1 ;i<head.size();i++){
				if(i == head.size() - 1){
					break;
				}
				if(((HSSFSheet) sheet).getRow(_row).getCell(i).toString().equals(((HSSFSheet) sheet).getRow(_row).getCell(i+1).toString())){
					CellRangeAddress cellRangeAddress = new CellRangeAddress(_row, _row, i, i+1);
					((HSSFSheet) sheet).addMergedRegion(cellRangeAddress);
				}
			}
			
		}
		else if (sheet instanceof XSSFSheet){
			for(int i =1 ;i<head.size();i++){
				if(i == head.size() - 1){
					break;
				}
				if(((XSSFSheet) sheet).getRow(_row).getCell(i).toString().equals(((XSSFSheet) sheet).getRow(_row).getCell(i+1).toString())){
					CellRangeAddress cellRangeAddress = new CellRangeAddress(_row, _row, i, i+1);
					((XSSFSheet) sheet).addMergedRegion(cellRangeAddress);
				}
			}
			
		}
	}
	
	private void setAutoSizeColumn(Object sheet, int cnt) throws Exception{
		if (sheet instanceof HSSFSheet){
			((HSSFSheet) sheet).autoSizeColumn((int)cnt);
		}
		else if (sheet instanceof XSSFSheet){
			((XSSFSheet) sheet).autoSizeColumn((int)cnt);
		}
	}
	
	private void setColumnWidth(Object sheet, int cnt) throws Exception{
		if (sheet instanceof HSSFSheet){
			((HSSFSheet) sheet).setColumnWidth((int)cnt, (((HSSFSheet) sheet).getColumnWidth(cnt))+512);
		}
		else if (sheet instanceof XSSFSheet){
			((XSSFSheet) sheet).setColumnWidth((int)cnt, (((XSSFSheet) sheet).getColumnWidth(cnt))+512);
		}
	}
	
	private void setWbWrite(Object wb, ByteArrayOutputStream outByteStream) throws Exception{
		if (wb instanceof HSSFWorkbook){
			((HSSFWorkbook) wb).write(outByteStream);
		}
		else if (wb instanceof XSSFWorkbook){
			((XSSFWorkbook) wb).write(outByteStream);
		}
	}
}
