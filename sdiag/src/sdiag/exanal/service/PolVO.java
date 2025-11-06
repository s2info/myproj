package sdiag.exanal.service;

import java.io.Serializable;

/**
 * 
 * 정책항목 VO 클래스
 * @author LEE CHANG JAE
 * @since 2015.10.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.10.27  CJLee          최초 생성
 *
 * </pre>
 */

public class PolVO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8230258064637277188L;

	/** 생성테이블 이름 */
	private String genTableName = "";
	
	/** 생성테이블 이름 */
	private String genTableName_diag = "";
	
	private String columnList = "";
	
	private String columnList_diag = "";
	
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

	private String columnDescription = "";
	
	private String columnDescription_diag = "";

	private String valueList = "";	
		
	private String emp_no  = "";
	private String name = "";
	private String gubun = "";
	private String gubun2 = "";
	private String prv_drm = "";
	private String prv_nondrm = "";
	private String nonprv_drm = "";
	private String nonprn_nondrm = "";
	private String total = "";
	private String etc	 = "";
	
	private String pol_id = "";
	private String table_name = "";
	private String table_name_diag = "";
	private int sn;
	private int sqno;
	private String orgfile_name;
	private String idx_used = "";
	
	private String column_name = "";

	private String begin_date = "";
	
	private String sldm_mac = "";
	private String sldm_ip = "";

	private String msg  = "";
	
	public String getIdx_used(){
		return idx_used;
	}

	public void setIdx_used(String idx_used) {
		this.idx_used = idx_used;		
	}		
	
	public String getOrgfile_name(){
		return orgfile_name;
	}

	public void setOrgfile_name(String orgfile_name) {
		this.orgfile_name = orgfile_name;		
	}	
    
    /**
     * 테이블 이름을 리턴한다.
     * @return
     */
	public String getGenTableName() {
		return genTableName;
	}

	/**
	 * genTableName attribute 값을 설정한다.
	 * @param genTableName String
	 */
	public void setGenTableName(String genTableName) {
		this.genTableName = genTableName;
	}
    
	/**
	 * pageIndex attribute 를 리턴한다.
	 * @return int
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * pageIndex attribute 값을 설정한다.
	 * @param pageIndex int
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * pageUnit attribute 를 리턴한다.
	 * @return int
	 */
	public int getPageUnit() {
		return pageUnit;
	}

	/**
	 * pageUnit attribute 값을 설정한다.
	 * @param pageUnit int
	 */
	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	/**
	 * pageSize attribute 를 리턴한다.
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * pageSize attribute 값을 설정한다.
	 * @param pageSize int
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * firstIndex attribute 를 리턴한다.
	 * @return int
	 */
	public int getFirstIndex() {
		return firstIndex;
	}

	/**
	 * firstIndex attribute 값을 설정한다.
	 * @param firstIndex int
	 */
	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	/**
	 * lastIndex attribute 를 리턴한다.
	 * @return int
	 */
	public int getLastIndex() {
		return lastIndex;
	}

	/**
	 * lastIndex attribute 값을 설정한다.
	 * @param lastIndex int
	 */
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	/**
	 * recordCountPerPage attribute 를 리턴한다.
	 * @return int
	 */
	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	/**
	 * recordCountPerPage attribute 값을 설정한다.
	 * @param recordCountPerPage int
	 */
	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}
	
	/**
	 * 컬럼리스트를 리턴한다.
	 * @return
	 */
	public String getColumnList(){
		return columnList;
	}
	
	/**
	 * 컬럼리스트를 설정한다.
	 * @param columnList
	 */
	public void setColumnList(String columnList) {
		this.columnList = columnList;
		
	}

	public String getColumnDescription(){
		return columnDescription;
	}
	
	public void setColumnDescription(String columnDescription) {
		this.columnDescription  = columnDescription;
		
	}

	public String getValueList(){
		return valueList;
	}	
	
	public void setValueList(String valueList) {
		this.valueList  = valueList;
		
	}
	
	public String getPol_id(){
		return pol_id;
	}	
	
	public void setPol_id(String pol_id) {
		this.pol_id  = pol_id;
	}
	
	public String getTable_name(){
		return table_name;
	}	
	
	public void setTable_name(String table_name) {
		this.table_name  = table_name;
	}
	
	public int getSn(){
		return sn;
	}	
	
	public void setSn(int sn) {
		this.sn  = sn;
	}
	
	public int getSqno(){
		return sqno;
	}	
	
	public void setSqno(int sqno) {
		this.sqno  = sqno;
	}		
	
			
	public String getEmp_no(){
		return emp_no;
	}	
	
	public void setEmp_no(String emp_no) {
		this.emp_no  = emp_no;
	}
	
	public String getName(){
		return name;
	}	
	
	public void setName(String name) {
		this.name  = name;
	}
	
	public String getGubun(){
		return gubun;
	}	
	
	public void setGubun(String gubun) {
		this.gubun  = gubun;
	}
	
	public String getGubun2(){
		return gubun2;
	}	
	
	public void setGubun2(String gubun2) {
		this.gubun2  = gubun2;
	}
	
	public String getPrv_drm(){
		return prv_drm;
	}	
	
	public void setPrv_drm(String prv_drm) {
		this.prv_drm  = prv_drm;
	}
	
	public String getPrv_nondrm(){
		return prv_nondrm;
	}	
	
	public void setPrv_nondrm(String prv_nondrm) {
		this.prv_nondrm  = prv_nondrm;
	}
	
	public String getNonprv_drm(){
		return nonprv_drm;
	}	
	
	public void setNonprv_drm(String nonprv_drm) {
		this.nonprv_drm  = nonprv_drm;
	}
	
	public String getNonprn_nondrm(){
		return nonprn_nondrm;
	}	
	
	public void setNonprn_nondrm(String nonprn_nondrm) {
		this.nonprn_nondrm  = nonprn_nondrm;
	}
	
	public String getTotal(){
		return total;
	}	
	
	public void setTotal(String total) {
		this.total  = total;
	}
	
	public String getEtc(){
		return etc;
	}	
	
	public void setEtc(String etc) {
		this.etc  = etc;
	}
	
	public String getColumn_name(){
		return column_name;
	}	
	
	public void setColumn_name(String column_name) {
		this.column_name  = column_name;
	}
	
	public String getBegin_date(){
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date  = begin_date;	
	}
	
	public String getSldm_mac(){
		return sldm_mac;
	}

	public void setSldm_mac(String sldm_mac) {
		this.sldm_mac  = sldm_mac;	
	}
	
	public String getSldm_ip(){
		return sldm_ip;
	}

	public void setSldm_ip(String sldm_ip) {
		this.sldm_ip  = sldm_ip;	
	}
	
	public String getMsg(){
		return msg;
	}

	public void setMsg(String msg) {
		this.msg  = msg;		
	}
    
	public String getGenTableName_diag(){
		return genTableName_diag;
	}

	public void setGenTableName_diag(String genTableName_diag) {
		this.genTableName_diag  = genTableName_diag;	
	}
	
	public String getTable_name_diag(){
		return table_name_diag;
	}

	public void setTable_name_diag(String table_name_diag) {
		this.table_name_diag  = table_name_diag;	
	}
	
	public String getColumnList_diag(){
		return columnList_diag;
	}

	public void setColumnList_diag(String columnList_diag) {
		this.columnList_diag  = columnList_diag;	
	}

	public String getColumnDescription_diag(){
		return columnDescription_diag;
	}

	public void setColumnDescription_diag(String columnDescription_diag) {
		this.columnDescription_diag  = columnDescription_diag;	
	}

}
