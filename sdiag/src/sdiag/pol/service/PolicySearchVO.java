package sdiag.pol.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PolicySearchVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3584388385743777555L;

    private	String org_bumon = "";
    private	String org_bonbu = "";
    private	String org_damdang = "";
    private	String org_team = "";
    private	String org_upper = "";
    private String end_date = "";
    private String begin_date = "";
	private String emp_no = "";
    private String searchType = "";
    private String loginType = "personal";
    private String buseoType = "N";
    private String polId = "";
    private String majrCode="";
    private String org_code="";
    private String searchDate="";
    private String isKpc="";
	public String getPolId() {
		return polId;
	}
	public void setPolId(String polId) {
		this.polId = polId;
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
    private String issub = "";
    
    public String getBuseoType() {
		return buseoType;
	}
	public void setBuseoType(String buseoType) {
		this.buseoType = buseoType;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
    
    public String getIssub() {
		return issub;
	}
	public void setIssub(String issub) {
		this.issub = issub;
	}
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
	public String getIsKpc() {
		return isKpc;
	}
	public void setIsKpc(String isKpc) {
		this.isKpc = isKpc;
	}
	
	/*****************************************/
    
	

	/************* 정책 조회 ********************/
	private String majCode = "";
	private String minCode = "";
	private String polCode = "";
	
	private String selectedRowId="";
	private String isexpn_zero="Y";
	private String sanctStatus = "";
	private String apprStatus = "";
	
	private String mCode = "";
	private String nCode = "";
	private String pCode = "";
	
	public String getmCode() {
		return mCode;
	}
	public void setmCode(String mCode) {
		this.mCode = mCode;
	}
	public String getnCode() {
		return nCode;
	}
	public void setnCode(String nCode) {
		this.nCode = nCode;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getSanctStatus() {
		return sanctStatus;
	}
	public void setSanctStatus(String sanctStatus) {
		this.sanctStatus = sanctStatus;
	}
	public String getApprStatus() {
		return apprStatus;
	}
	public void setApprStatus(String apprStatus) {
		this.apprStatus = apprStatus;
	}
	public String getIsexpn_zero() {
		return isexpn_zero;
	}
	public void setIsexpn_zero(String isexpn_zero) {
		this.isexpn_zero = isexpn_zero;
	}
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
	public String getMajrCode() {
		return majrCode;
	}
	public void setMajrCode(String majrCode) {
		this.majrCode = majrCode;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	
	
	
}
