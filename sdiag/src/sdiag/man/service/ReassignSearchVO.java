package sdiag.man.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReassignSearchVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8268660506034761897L;
	/** formMod*/
	private String formMod = "";
	/** 협력사 정보 검색 조건*/
	private String c_condition="";
	/** 협력사 정보 검색어*/
	private String c_searchKeyword="";
	/** KT 정보 검색어*/
	private String kt_searchKeyword="";
	/** 협력사 유형*/
	private String  s_cType ="";
	/** 검색조건 */
	private String searchCondition = "";
	/** 검색키워드 */
	private String searchKeyword = "";
	
	
	private int pageIndex = 1;
    
	/** 페이지 갯수 */
    private int pageUnit; // properties에서 설정
    /** 페이지 사이즈 */
    private int pageSize; // properties에서 설정
    
    /** 시작 인덱스 */
    private int firstIndex = 1;
    

	/** 끝 인덱스 */
    private int lastIndex = 1;
    
    /**페이지 별 레코드 갯수  */
    private int recordCountPerPage = 10;
    
    
    private String re_searchKeyword="";
	
	public String getFormMod() {
		return formMod;
	}
	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}
	public String getC_condition() {
		return c_condition;
	}
	public void setC_condition(String c_condition) {
		this.c_condition = c_condition;
	}
	public String getC_searchKeyword() {
		return c_searchKeyword;
	}
	public void setC_searchKeyword(String c_searchKeyword) {
		this.c_searchKeyword = c_searchKeyword;
	}
	public String getKt_searchKeyword() {
		return kt_searchKeyword;
	}
	public void setKt_searchKeyword(String kt_searchKeyword) {
		this.kt_searchKeyword = kt_searchKeyword;
	}
	public String getS_cType() {
		return s_cType;
	}
	public void setS_cType(String s_cType) {
		this.s_cType = s_cType;
	}
	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageUnit() {
		return pageUnit;
	}
	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getFirstIndex() {
		return firstIndex;
	}
	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}
	public int getLastIndex() {
		return lastIndex;
	}
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}
	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}
	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}
	public String getRe_searchKeyword() {
		return re_searchKeyword;
	}
	public void setRe_searchKeyword(String re_searchKeyword) {
		this.re_searchKeyword = re_searchKeyword;
	}
	
	
    
    
}
