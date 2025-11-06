package sdiag.com.service;

import java.io.Serializable;

public class BPMInfoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6791715149455232023L;

	private String appr_id = "";
	private String emp_no = "";
	private String bpm_id = "";
	private String bpm_ret_code = "";
	private String appr_commment = "";
	private String status_type = "";
	private String noti = "";
	private String reqtype="0001";
	private String appr_proc="0000";
	
	public String getAppr_proc() {
		return appr_proc;
	}
	public void setAppr_proc(String appr_proc) {
		this.appr_proc = appr_proc;
	}
	public String getReqtype() {
		return reqtype;
	}
	public void setReqtype(String reqtype) {
		this.reqtype = reqtype;
	}
	public String getNoti() {
		return noti;
	}
	public void setNoti(String noti) {
		this.noti = noti;
	}
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
	public String getBpm_id() {
		return bpm_id;
	}
	public void setBpm_id(String bpm_id) {
		this.bpm_id = bpm_id;
	}
	public String getBpm_ret_code() {
		return bpm_ret_code;
	}
	public void setBpm_ret_code(String bpm_ret_code) {
		this.bpm_ret_code = bpm_ret_code;
	}
	public String getAppr_commment() {
		return appr_commment;
	}
	public void setAppr_commment(String appr_commment) {
		this.appr_commment = appr_commment;
	}
	public String getStatus_type() {
		return status_type;
	}
	public void setStatus_type(String status_type) {
		this.status_type = status_type;
	}
	
}
