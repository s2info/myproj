package sdiag.pol.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class OrgGroupVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7723819547206178750L;

	private String org_code = "";
	private String org_cap_no = "";
	private String upper_org_code = "";
	private String org_level = "";
	private String org_order = "";
	private String use_indc = "";
	private String updt_indc = "";
	private String rgdt_date = "";
	private String updt_date = "";
	private String org_nm = "";
	private String comp_code = "";
	private String is_suborg = "";
	private String is_orgmember = "Y";
	private String is_orgloctype = "";
	
	public String getIs_orgloctype() {
		return is_orgloctype;
	}
	public void setIs_orgloctype(String is_orgloctype) {
		this.is_orgloctype = is_orgloctype;
	}
	public String getIs_orgmember() {
		return is_orgmember;
	}
	public void setIs_orgmember(String is_orgmember) {
		this.is_orgmember = is_orgmember;
	}
	public String getIs_suborg() {
		return is_suborg;
	}
	public void setIs_suborg(String is_suborg) {
		this.is_suborg = is_suborg;
	}
	public String getComp_code() {
		return comp_code;
	}
	public void setComp_code(String comp_code) {
		this.comp_code = comp_code;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getOrg_cap_no() {
		return org_cap_no;
	}
	public void setOrg_cap_no(String org_cap_no) {
		this.org_cap_no = org_cap_no;
	}
	public String getUpper_org_code() {
		return upper_org_code;
	}
	public void setUpper_org_code(String upper_org_code) {
		this.upper_org_code = upper_org_code;
	}
	public String getOrg_level() {
		return org_level;
	}
	public void setOrg_level(String org_level) {
		this.org_level = org_level;
	}
	public String getOrg_order() {
		return org_order;
	}
	public void setOrg_order(String org_order) {
		this.org_order = org_order;
	}
	public String getUse_indc() {
		return use_indc;
	}
	public void setUse_indc(String use_indc) {
		this.use_indc = use_indc;
	}
	public String getUpdt_indc() {
		return updt_indc;
	}
	public void setUpdt_indc(String updt_indc) {
		this.updt_indc = updt_indc;
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
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}

	
	
}
