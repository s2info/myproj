package sdiag.com.service;

import java.io.Serializable;

public class MailInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6004756510706780626L;

	private String toMailAddress="";
	private String subject = "";
	private String contents = "";
	private String emp_no = "";
	
	private String idx_rgdt_date="";
	private String mac="";
	private String pol_idx_id="";
	private String ip="";
	private String sancttype="";
	
	public String getSancttype() {
		return sancttype;
	}
	public void setSancttype(String sancttype) {
		this.sancttype = sancttype;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPol_idx_id() {
		return pol_idx_id;
	}
	public void setPol_idx_id(String pol_idx_id) {
		this.pol_idx_id = pol_idx_id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getToMailAddress() {
		return toMailAddress;
	}
	public void setToMailAddress(String toMailAddress) {
		this.toMailAddress = toMailAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	
	
}
