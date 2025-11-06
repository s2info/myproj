package sdiag.man.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8268660506034761897L;
	/** 글번호* */
	private int sqno = 0;
    
	private long sq_no = 0;
    /**구분**/
    private String n_type = "";
    /** 현재 페이지 */
	private int pageIndex = 1;
	//시작일
	private String beginDate = "";
	//종료링
	private String endDate = "";
	private String emp_no="";
	/** 검색조건 */
	private String searchCondition = "";
	
	/** 검색키워드 */
	private String searchKeyword = "";
    
	/** 페이지 갯수 */
    private int pageUnit; // properties에서 설정
    
    private String selectedRowId="";
	
	public String getSelectedRowId() {
		return selectedRowId;
	}
	public void setSelectedRowId(String selectedRowId) {
		this.selectedRowId = selectedRowId;
	}
    
    public long getSq_no() {
		return sq_no;
	}

	public void setSq_no(long sq_no) {
		this.sq_no = sq_no;
	}

	public String getN_type() {
		return n_type;
	}

	public void setN_type(String n_type) {
		this.n_type = n_type;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	/** 페이지 사이즈 */
    private int pageSize; // properties에서 설정
    
    /** 시작 인덱스 */
    private int firstIndex = 1;
    

	/** 끝 인덱스 */
    private int lastIndex = 1;
    
    /**페이지 별 레코드 갯수  */
    private int recordCountPerPage = 10;
    
    public int getSqno() {
		return sqno;
	}

	public void setSqno(int sqno) {
		this.sqno = sqno;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}

    
}