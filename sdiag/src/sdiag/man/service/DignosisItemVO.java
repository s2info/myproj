package sdiag.man.service;

import java.io.Serializable;

public class DignosisItemVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 396142846505370618L;
	// 진단항목코드 TB - "DIAG_ITEM_CODE"
	private String diag_majr_code;  // 진단 대분류코드
	private String diag_minr_code;	// 진단 중분류코드
    private String diag_desc;		// 진단 설명
    private String ordr;			// 순서
	private String use_indc;		// 사용여부
	private String rgdt_date;		// 등록일
	private String updt_date;		// 수정일
	private String diag_notice;
	private String buseo_indc="N";
	
	public String getBuseo_indc() {
		return buseo_indc;
	}
	public void setBuseo_indc(String buseo_indc) {
		this.buseo_indc = buseo_indc;
	}
	public String getDiag_notice() {
		return diag_notice;
	}
	public void setDiag_notice(String diag_notice) {
		this.diag_notice = diag_notice;
	}
	public String getDiag_majr_code() {
		return diag_majr_code;
	}
	public void setDiag_majr_code(String diag_majr_code) {
		this.diag_majr_code = diag_majr_code;
	}
	public String getDiag_minr_code() {
		return diag_minr_code;
	}
	public void setDiag_minr_code(String diag_minr_code) {
		this.diag_minr_code = diag_minr_code;
	}
	public String getDiag_desc() {
		return diag_desc;
	}
	public void setDiag_desc(String diag_desc) {
		this.diag_desc = diag_desc;
	}
	public String getOrdr() {
		return ordr;
	}
	public void setOrdr(String ordr) {
		this.ordr = ordr;
	}
	public String getUse_indc() {
		return use_indc;
	}
	public void setUse_indc(String use_indc) {
		this.use_indc = use_indc;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getUpdt_date() {
		return updt_date;
	}
	public void setUpdt_date(String updt_date) {
		this.updt_date = updt_date;
	}

}
