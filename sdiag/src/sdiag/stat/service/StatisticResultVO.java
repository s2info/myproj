package sdiag.stat.service;

import java.io.Serializable;

public class StatisticResultVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -737839487254802542L;

	private String period = "";
	private String policy_id = "";
	private String sec_pol_desc = "";
	private String org_nm_1 = "";
	private String org_nm_2 = "";
	private String org_nm_3 = "";
	private String org_nm_4 = "";
	private String org_nm_5 = "";
	private String org_nm_6 = "";
	private String emp_nm = "";
	private String emp_no = "";
	private long tot_event = 0;
	
	public String getEmp_nm() {
		return emp_nm;
	}
	public void setEmp_nm(String emp_nm) {
		this.emp_nm = emp_nm;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPolicy_id() {
		return policy_id;
	}
	public void setPolicy_id(String policy_id) {
		this.policy_id = policy_id;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	public String getOrg_nm_1() {
		return org_nm_1;
	}
	public void setOrg_nm_1(String org_nm_1) {
		this.org_nm_1 = org_nm_1;
	}
	public String getOrg_nm_2() {
		return org_nm_2;
	}
	public void setOrg_nm_2(String org_nm_2) {
		this.org_nm_2 = org_nm_2;
	}
	public String getOrg_nm_3() {
		return org_nm_3;
	}
	public void setOrg_nm_3(String org_nm_3) {
		this.org_nm_3 = org_nm_3;
	}
	public String getOrg_nm_4() {
		return org_nm_4;
	}
	public void setOrg_nm_4(String org_nm_4) {
		this.org_nm_4 = org_nm_4;
	}
	public String getOrg_nm_5() {
		return org_nm_5;
	}
	public void setOrg_nm_5(String org_nm_5) {
		this.org_nm_5 = org_nm_5;
	}
	public String getOrg_nm_6() {
		return org_nm_6;
	}
	public void setOrg_nm_6(String org_nm_6) {
		this.org_nm_6 = org_nm_6;
	}
	public long getTot_event() {
		return tot_event;
	}
	public void setTot_event(long tot_event) {
		this.tot_event = tot_event;
	}
	
	
	
}
