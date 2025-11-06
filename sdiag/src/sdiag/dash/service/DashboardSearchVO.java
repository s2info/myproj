package sdiag.dash.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DashboardSearchVO implements Serializable{
	
	private static final long serialVersionUID = 3584388385743777555L;

    private	String org_bumon = "";
    private	String org_bonbu = "";
    private	String org_damdang = "";
    private	String org_team = "";
    private	String org_upper = "";
    private String end_date = "";
    private String begin_date = "";
	private String emp_no = "";
	private String org_code="";
    private String searchType = "";
    private String idx_status = "";
    private String buseoType = "";
    private String isSubOrg = "Y";
    private String org_root_code = "00001";
    private String majrCode="";
    private String buseoIndc="";
    private String majrName="";
    private String search_date="";
    
    public String getMajrCode() {
		return majrCode;
	}
	public void setMajrCode(String majrCode) {
		this.majrCode = majrCode;
	}
	public String getBuseoIndc() {
		return buseoIndc;
	}
	public void setBuseoIndc(String buseoIndc) {
		this.buseoIndc = buseoIndc;
	}
	public String getMajrName() {
		return majrName;
	}
	public void setMajrName(String majrName) {
		this.majrName = majrName;
	}
	public String getIsSubOrg() {
		return isSubOrg;
	}
	public void setIsSubOrg(String isSubOrg) {
		this.isSubOrg = isSubOrg;
	}
	public String getBuseoType() {
		return buseoType;
	}
	public void setBuseoType(String buseoType) {
		this.buseoType = buseoType;
	}

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
    
    /*****************************************/
    private String dtType="";
    private String dtSeq = "";
    
    
    public String getDtType() {
		return dtType;
	}
	public void setDtType(String dtType) {
		this.dtType = dtType;
	}
	public String getDtSeq() {
		return dtSeq;
	}
	public void setDtSeq(String dtSeq) {
		this.dtSeq = dtSeq;
	}
	/*****************************************/
    
	/************* 정책 조회 ********************/
	private String majCode = "";
	private String majName = "";
	private String minCode = "";
	private String polCode = "";
	
	/************* 정책 전체건수 정책 양호/취약 건수 조회 ********************/	
	private int tot = 0;
	private int goodness = 0;
	private int weekness = 0;	
	
	private String selectedRowId="";
	
	public String getSelectedRowId() {
		return selectedRowId;
	}
	public void setSelectedRowId(String selectedRowId) {
		this.selectedRowId = selectedRowId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getMajCode() {
		return majCode;
	}
	public void setMajCode(String majCode) {
		this.majCode = majCode;
	}
	public String getMinCode() {
		return minCode;
	}
	public void setMinCode(String minCode) {
		this.minCode = minCode;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	/*****************************************/
	
    
    public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
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
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	
	public String getOrg_upper() {
		return org_upper;
	}
	public void setOrg_upper(String org_upper) {
		this.org_upper = org_upper;
	}
	public String getOrg_bumon() {
		return org_bumon;
	}
	public void setOrg_bumon(String org_bumon) {
		this.org_bumon = org_bumon;
	}
	public String getOrg_bonbu() {
		return org_bonbu;
	}
	public void setOrg_bonbu(String org_bonbu) {
		this.org_bonbu = org_bonbu;
	}
	public String getOrg_damdang() {
		return org_damdang;
	}
	public void setOrg_damdang(String org_damdang) {
		this.org_damdang = org_damdang;
	}
	public String getOrg_team() {
		return org_team;
	}
	public void setOrg_team(String org_team) {
		this.org_team = org_team;
	}
	public String getIdx_status() {
		return idx_status;
	}
	public void setIdx_status(String idx_status) {
		this.idx_status = idx_status;
	}
	

	public int getTot() {
		return tot;
	}
	public void setTot(int tot) {
		this.tot = tot;
	}
	
	
	public int getGoodness() {
		return goodness;
	}
	public void setGoodness(int goodness) {
		this.goodness = goodness;
	}
	
	
	public int getWeekness() {
		return weekness;
	}
	public void setWeekness(int weekness) {
		this.weekness = weekness;
	}
	public String getMajName() {
		return majName;
	}
	public void setMajName(String majName) {
		this.majName = majName;
	}
	public String getOrg_root_code() {
		return org_root_code;
	}
	public void setOrg_root_code(String org_root_code) {
		this.org_root_code = org_root_code;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getSearch_date() {
		return search_date;
	}
	public void setSearch_date(String search_date) {
		this.search_date = search_date;
	}
	
}
