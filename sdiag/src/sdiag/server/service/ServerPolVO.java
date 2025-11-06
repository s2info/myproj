package sdiag.server.service;

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

public class ServerPolVO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8230258064637277188L;

	/** 정책 ID */
	private String policyId = "";
	/** 정책 ID */
	private String sldmEmpNo = "";
	/** 정책 ID */
	private String sldmOrgLogDate = "";
	/** 정책 ID */
	private String seq = "";
	/** 정책 ID */
	private String actionDate = "";
	/** 정책 ID */
	private String actionYn = "";
	/** 정책 ID */
	private String bigo = "";
	/** 정책 명 */
	private String policyName = "";
	/** 컬럼리스트*/
	private String columnList = "";
	/** 정책로그테이블 */
	private String src_table = "";
	
  	private String columnDescription = "";
	
	private String columnDescription_diag = "";

	private String valueList = "";	
		
	private String 조치예정일  = "";
	
	private String 조치여부  = "";
	
	private String 비고  = "";
	
	private String role_cdoe = "";
	
	private String empNo = "";

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getSldmEmpNo() {
		return sldmEmpNo;
	}

	public void setSldmEmpNo(String sldmEmpNo) {
		this.sldmEmpNo = sldmEmpNo;
	}

	public String getSldmOrgLogDate() {
		return sldmOrgLogDate;
	}

	public void setSldmOrgLogDate(String sldmOrgLogDate) {
		this.sldmOrgLogDate = sldmOrgLogDate;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionYn() {
		return actionYn;
	}

	public void setActionYn(String actionYn) {
		this.actionYn = actionYn;
	}

	public String getBigo() {
		return bigo;
	}

	public void setBigo(String bigo) {
		this.bigo = bigo;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getColumnList() {
		return columnList;
	}

	public void setColumnList(String columnList) {
		this.columnList = columnList;
	}

	public String getSrc_table() {
		return src_table;
	}

	public void setSrc_table(String src_table) {
		this.src_table = src_table;
	}

	public String getColumnDescription() {
		return columnDescription;
	}

	public void setColumnDescription(String columnDescription) {
		this.columnDescription = columnDescription;
	}

	public String getColumnDescription_diag() {
		return columnDescription_diag;
	}

	public void setColumnDescription_diag(String columnDescription_diag) {
		this.columnDescription_diag = columnDescription_diag;
	}

	public String getValueList() {
		return valueList;
	}

	public void setValueList(String valueList) {
		this.valueList = valueList;
	}

	public String get조치예정일() {
		return 조치예정일;
	}

	public void set조치예정일(String 조치예정일) {
		this.조치예정일 = 조치예정일;
	}

	public String get조치여부() {
		return 조치여부;
	}

	public void set조치여부(String 조치여부) {
		this.조치여부 = 조치여부;
	}

	public String get비고() {
		return 비고;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public String getRole_cdoe() {
		return role_cdoe;
	}

	public void setRole_cdoe(String role_cdoe) {
		this.role_cdoe = role_cdoe;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	
	
	
}
