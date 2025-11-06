package sdiag.report.service;

import java.io.Serializable;

public class ReportSearchResultVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3116738768365556336L;
	private String sum_rgdt_date = "";
	private String idx_rgdt_date="";
	private String rgdt_date = "";
	private String org_code = "";
	private String org_nm = "";
	private String pol_idx_id = "";
	private int tot_emp = 0;
	private int tot_score = 0;
	private int tot_count = 0;
	private int tot_org_emp = 0;
	private int avg = 0;
	private float rat_avg = 0;
	private String sec_pol_id = "";
	private String sec_pol_desc = "";
	private String idxstatus="";
	private String emp_no = "";
	private String emp_nm = "";
	private int count = 0;
	private int score = 0;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getSum_rgdt_date() {
		return sum_rgdt_date;
	}
	public void setSum_rgdt_date(String sum_rgdt_date) {
		this.sum_rgdt_date = sum_rgdt_date;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
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
	public String getPol_idx_id() {
		return pol_idx_id;
	}
	public void setPol_idx_id(String pol_idx_id) {
		this.pol_idx_id = pol_idx_id;
	}
	public int getTot_emp() {
		return tot_emp;
	}
	public void setTot_emp(int tot_emp) {
		this.tot_emp = tot_emp;
	}
	public int getTot_score() {
		return tot_score;
	}
	public void setTot_score(int tot_score) {
		this.tot_score = tot_score;
	}
	public int getTot_count() {
		return tot_count;
	}
	public void setTot_count(int tot_count) {
		this.tot_count = tot_count;
	}
	public int getTot_org_emp() {
		return tot_org_emp;
	}
	public void setTot_org_emp(int tot_org_emp) {
		this.tot_org_emp = tot_org_emp;
	}
	public int getAvg() {
		return avg;
	}
	public void setAvg(int avg) {
		this.avg = avg;
	}
	public float getRat_avg() {
		return rat_avg;
	}
	public void setRat_avg(float rat_avg) {
		this.rat_avg = rat_avg;
	}
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	public String getIdxstatus() {
		return idxstatus;
	}
	public void setIdxstatus(String idxstatus) {
		this.idxstatus = idxstatus;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
	}
	
}
