package sdiag.exception.service;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class ReqSearchVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	//formMod
	private String formMod;
	
	/**승인여부  */
    private String appYn;
    
    /**승인여부  */
    private long exSeq=0;
	

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

	public String getFormMod() {
		return formMod;
	}

	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}

	public String getAppYn() {
		return appYn;
	}

	public void setAppYn(String appYn) {
		this.appYn = appYn;
	}

	public long getExSeq() {
		return exSeq;
	}

	public void setExSeq(long exSeq) {
		this.exSeq = exSeq;
	}

}
