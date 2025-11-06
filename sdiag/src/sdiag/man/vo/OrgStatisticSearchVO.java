package sdiag.man.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class OrgStatisticSearchVO implements Serializable{

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
    private int pageUnit=0; // properties에서 설정
    /** 페이지 사이즈 */
    private int pageSize=10; // properties에서 설정
    
    /** 시작 인덱스 */
    private int firstIndex = 1;
    
    private String formMod = "";
    
	/** 통계 순번 */    
	private long statSeq=0;
	
	
    
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

	/** 끝 인덱스 */
    private int lastIndex = 1;
    
    /**페이지 별 레코드 갯수  */
    private int recordCountPerPage = 10;

	public String getFormMod() {
		return formMod;
	}

	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}

	public long getStatSeq() {
		return statSeq;
	}

	public void setStatSeq(long statSeq) {
		this.statSeq = statSeq;
	}


    
    
}
