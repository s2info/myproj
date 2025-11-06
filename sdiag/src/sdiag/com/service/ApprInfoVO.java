package sdiag.com.service;

import java.io.Serializable;

public class ApprInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1656335153960376022L;
	private String appr_id = "";
	private String emp_no = "";
	private String appr_line_code="";
	private String appr_stat_code="";
	private String summ_appl_desc="";
	private String summ_appl_detl_desc="";
	private String summ_appl_act_desc="";
	private String evid_file_name="";
	private String evid_file_loc="";
	private String rgdt_date="";
	private String updt_date="";
	private String appr_comment="";
	private String reqtype="0000";
	public String getAppr_id() {
		return appr_id;
	}
	public void setAppr_id(String appr_id) {
		this.appr_id = appr_id;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getAppr_line_code() {
		return appr_line_code;
	}
	public void setAppr_line_code(String appr_line_code) {
		this.appr_line_code = appr_line_code;
	}
	public String getAppr_stat_code() {
		return appr_stat_code;
	}
	public void setAppr_stat_code(String appr_stat_code) {
		this.appr_stat_code = appr_stat_code;
	}
	public String getSumm_appl_desc() {
		return summ_appl_desc;
	}
	public void setSumm_appl_desc(String summ_appl_desc) {
		this.summ_appl_desc = summ_appl_desc;
	}
	public String getSumm_appl_detl_desc() {
		return summ_appl_detl_desc;
	}
	public void setSumm_appl_detl_desc(String summ_appl_detl_desc) {
		this.summ_appl_detl_desc = summ_appl_detl_desc;
	}
	public String getSumm_appl_act_desc() {
		return summ_appl_act_desc;
	}
	public void setSumm_appl_act_desc(String summ_appl_act_desc) {
		this.summ_appl_act_desc = summ_appl_act_desc;
	}
	public String getEvid_file_name() {
		return evid_file_name;
	}
	public void setEvid_file_name(String evid_file_name) {
		this.evid_file_name = evid_file_name;
	}
	public String getEvid_file_loc() {
		return evid_file_loc;
	}
	public void setEvid_file_loc(String evid_file_loc) {
		this.evid_file_loc = evid_file_loc;
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
	public String getAppr_comment() {
		return appr_comment;
	}
	public void setAppr_comment(String appr_comment) {
		this.appr_comment = appr_comment;
	}
	public String getReqtype() {
		return reqtype;
	}
	public void setReqtype(String reqtype) {
		this.reqtype = reqtype;
	}
	
}
