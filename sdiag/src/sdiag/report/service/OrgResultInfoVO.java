package sdiag.report.service;

import java.io.Serializable;

public class OrgResultInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4913541245115292268L;
	private String org_code="";
	private String org_nm="";
	private String sum_rgdt_date="";
	private String avg="";
	private String tot_count="";
	private String idxstatus="";
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
	
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
	public String getTot_count() {
		return tot_count;
	}
	public void setTot_count(String tot_count) {
		this.tot_count = tot_count;
	}
	/*	public int getAvg() {
		return avg;
	}
	public void setAvg(int avg) {
		this.avg = avg;
	}
	public int getTot_count() {
		return tot_count;
	}
	public void setTot_count(int tot_count) {
		this.tot_count = tot_count;
	}*/
	public String getSum_rgdt_date() {
		return sum_rgdt_date;
	}
	public void setSum_rgdt_date(String sum_rgdt_date) {
		this.sum_rgdt_date = sum_rgdt_date;
	}
	public String getIdxstatus() {
		return idxstatus;
	}
	public void setIdxstatus(String idxstatus) {
		this.idxstatus = idxstatus;
	}
	
}
