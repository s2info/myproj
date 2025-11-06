package sdiag.stat.service;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class StatisticSearchVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6272771445561945456L;

	private int pageIndex = 1;
    /** 검색조건 */
	private String searchCondition = "";
	
	/** 검색키워드 */
	private String searchKeyword = "";
    
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
    /**
     * 검색시작일
     */
    private String begindate = "";
    /**
     * 검색종료일
     */
    private String enddate = "";
    /**
     * 정책 검색조건
     */
    private String policyid = "";
    /**
     * 대분류 검색조건
     */
    private String majrCode = "";
    /**
     * 다중 정책 검색조건
     */
    private List<String> policyidList = null;
    /**
     * 검색구분 - O : org, U : user
     */
    private String searchType = "O";
    /**
     * 상위 조직코드
     */
    private String upperOrgCode = "";
    /**
     * 상위 조직명
     */
    private String upperOrgName = "";
    /**
     * 검색조직
     */
    private String searchOrgCode= "";
    /**
     * 통계분석 결과 Data
     */
    private String bodyData = "";
    /**
     * 부서진단여부
     */
    private String buseoindc = "N";
    
	public String getBuseoindc() {
		return buseoindc;
	}
	public void setBuseoindc(String buseoindc) {
		this.buseoindc = buseoindc;
	}
	public String getBodyData() {
		return bodyData;
	}
	public void setBodyData(String bodyData) {
		this.bodyData = bodyData;
	}
	public String getSearchOrgCode() {
		return searchOrgCode;
	}
	public void setSearchOrgCode(String searchOrgCode) {
		this.searchOrgCode = searchOrgCode;
	}
	public String getUpperOrgName() {
		return upperOrgName;
	}
	public void setUpperOrgName(String upperOrgName) {
		this.upperOrgName = upperOrgName;
	}
	public String getUpperOrgCode() {
		return upperOrgCode;
	}
	public void setUpperOrgCode(String upperOrgCode) {
		this.upperOrgCode = upperOrgCode;
	}
	public String getMajrCode() {
		return majrCode;
	}
	public void setMajrCode(String majrCode) {
		this.majrCode = majrCode;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
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
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getPolicyid() {
		return policyid;
	}
	public void setPolicyid(String policyid) {
		this.policyid = policyid;
	}
	public List<String> getPolicyidList() {
		return policyidList;
	}
	public void setPolicyidList(List<String> policyidList) {
		this.policyidList = policyidList;
	}
    		
    
}
