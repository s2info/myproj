package sdiag.com.service;

import java.io.Serializable;

public class LoginUserTypeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -154912560360622303L;

	private String emp_no = "";
	private String emp_nm = "";
	private String org_code="";
	private String org_nm = "";
	private String logintype="";
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getEmp_nm() {
		return emp_nm;
	}
	public void setEmp_nm(String emp_nm) {
		this.emp_nm = emp_nm;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getLogintype() {
		return logintype;
	}
	public void setLogintype(String logintype) {
		this.logintype = logintype;
	}
	
	
}
