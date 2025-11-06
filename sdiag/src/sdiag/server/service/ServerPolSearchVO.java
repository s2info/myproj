package sdiag.server.service;

import java.io.Serializable;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public class ServerPolSearchVO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8230258064637277188L;

	/** 정책 ID */
	private String tbnm = "";
	/** 사번 */
	private String s_EmpNo = "";
	private String s_EmpNm = "";
	private String s_rgdtDate = "";
	private String s_actionYn = "";
	private String role_code = "";
	
	
    /** 현재페이지 */
    private int pageIndex = 1;
    
    /** 페이지갯수 */
    private int pageUnit = 10;
    
    /** 페이지사이즈 */
    private int pageSize = 10;

    /** firstIndex */
    private int firstIndex = 1;

    /** lastIndex */
    private int lastIndex = 1;

    /** recordCountPerPage */
    private int recordCountPerPage = 10;
    
    private List<EgovMap> columnList = null;
    
    private int extractTerm = 90;
    
    private String polId="";

	public String getTbnm() {
		return tbnm;
	}

	public void setTbnm(String tbnm) {
		this.tbnm = tbnm;
	}

	public String getS_EmpNo() {
		return s_EmpNo;
	}

	public void setS_EmpNo(String s_EmpNo) {
		this.s_EmpNo = s_EmpNo;
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

	public List<EgovMap> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<EgovMap> columnList) {
		this.columnList = columnList;
	}
	
	public String getS_rgdtDate() {
		return s_rgdtDate;
	}

	public void setS_rgdtDate(String s_rgdtDate) {
		this.s_rgdtDate = s_rgdtDate;
	}

	public String getS_actionYn() {
		return s_actionYn;
	}

	public void setS_actionYn(String s_actionYn) {
		this.s_actionYn = s_actionYn;
	}

	public int getExtractTerm() {
		return extractTerm;
	}

	public void setExtractTerm(int extractTerm) {
		this.extractTerm = extractTerm;
	}

	public String getPolId() {
		return polId;
	}

	public void setPolId(String polId) {
		this.polId = polId;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

	public String getS_EmpNm() {
		return s_EmpNm;
	}

	public void setS_EmpNm(String s_EmpNm) {
		this.s_EmpNm = s_EmpNm;
	}
	
}
