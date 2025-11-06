package sdiag.com.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class MenuItemVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4537766979706559529L;

	private String diag_majr_code = "";
	private String diag_minr_code = "";
	private String diag_desc = "";
	private String ordr = "";
	private String use_indc = "";
	private String diag_notice = "";
	private String buseo_indc = "";
	private String pol_diag_majr_code= "";
	private String sec_pol_desc="";
	
	public String getPol_diag_majr_code() {
		return pol_diag_majr_code;
	}
	public void setPol_diag_majr_code(String pol_diag_majr_code) {
		this.pol_diag_majr_code = pol_diag_majr_code;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
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
	
	
}
