package sdiag.report.service;

import java.io.Serializable;

import com.ibm.icu.util.Calendar;

import sdiag.util.DateUtil;

public class ReportSearchVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7994632687696876805L;
	private String begin_date = "";
	private String end_date = "";
	private String org_code="";
	private String emp_no = "";
	private String now_date = DateUtil.getDateAdd(Calendar.DATE, -1);
	private String auth="3";
	private String orgCode="";
	private String orgName="";
	private String org_upper="";
	private String requestType="";
	private int subCount=0;
	private String isSubOrgCount = "N";
	private String content_body1="";
	private String polcheckedlist="";
	private String policy = "";
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getNow_date() {
		return now_date;
	}
	public void setNow_date(String now_date) {
		this.now_date = now_date;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrg_upper() {
		return org_upper;
	}
	public void setOrg_upper(String org_upper) {
		this.org_upper = org_upper;
	}
	public int getSubCount() {
		return subCount;
	}
	public void setSubCount(int subCount) {
		this.subCount = subCount;
	}
	public String getIsSubOrgCount() {
		return isSubOrgCount;
	}
	public void setIsSubOrgCount(String isSubOrgCount) {
		this.isSubOrgCount = isSubOrgCount;
	}
	public String getContent_body1() {
		return content_body1;
	}
	public void setContent_body1(String content_body1) {
		this.content_body1 = content_body1;
	}
	public String getPolcheckedlist() {
		return polcheckedlist;
	}
	public void setPolcheckedlist(String polcheckedlist) {
		this.polcheckedlist = polcheckedlist;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
}
