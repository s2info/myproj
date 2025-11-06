package sdiag.com.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public class ExcelInitVO {

	private String fileName;			// 파일 이름
	private String sheetName;			// 시트 이름
	private String title;				// 타이틀
	private String type;				// xls, xlsx
	private List<String> head = new ArrayList<String>();		// 헤더값
	private List<String> headOrg = new ArrayList<String>();		// 헤더값
	
	private String subTitleAA;
	private String subTitleBB;
	private String subTitleCC;

	private String excelListAA;
	private List<EgovMap> contents = new ArrayList<EgovMap>();
	private String titleValue = "";
	private boolean AutoSize = false;
	private boolean HeadMergeEqualValue = false;
	public String getTitleValue() {
		return titleValue;
	}
	public void setTitleValue(String titleValue) {
		this.titleValue = titleValue;
	}
	public List<EgovMap> getContents() {
		return contents;
	}
	public void setContents(List<EgovMap> contents) {
		this.contents = contents;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getHead() {
		return head;
	}
	public void setHead(List<String> head) {
		this.head = head;
	}	
	
	public void setHeadVal(String val){
		if (val != null && !val.equals("")){
			this.head.add(val);
		}
	}
	
	public List<String> getHeadOrg() {
		return headOrg;
	}
	public void setHeadOrg(List<String> headOrg) {
		this.headOrg = headOrg;
	}
	
	public String getSubTitleAA() {
		return subTitleAA;
	}
	public void setSubTitleAA(String subTitleAA) {
		this.subTitleAA = subTitleAA;
	}
	
	public String getSubTitleBB() {
		return subTitleBB;
	}
	public void setSubTitleBB(String subTitleBB) {
		this.subTitleBB = subTitleBB;
	}
	
	public String getSubTitleCC() {
		return subTitleCC;
	}
	public void setSubTitleCC(String subTitleCC) {
		this.subTitleCC = subTitleCC;
	}
	
	public String getExcelListAA() {
		return excelListAA;
	}
	public void setExcelListAA(String excelListAA) {
		this.excelListAA = excelListAA;
	}
	public boolean isAutoSize() {
		return AutoSize;
	}
	public void setAutoSize(boolean autoSize) {
		AutoSize = autoSize;
	}
	public boolean isHeadMergeEqualValue() {
		return HeadMergeEqualValue;
	}
	public void setHeadMergeEqualValue(boolean headMergeEqualValue) {
		HeadMergeEqualValue = headMergeEqualValue;
	}
	

}
