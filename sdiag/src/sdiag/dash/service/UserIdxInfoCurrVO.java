package sdiag.dash.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserIdxInfoCurrVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7723819547206178750L;

	String emp_no = "";
	String mac = "";
	String ip = "";
	int score = 0;
	String rgdt_date = "";
	String updt_date = "";

	String emp_nm = "";
	String org_code = "";
	String title_code = "";
	String title_nm = "";
	String level_code = "";
	String level_nm = "";
	String org_nm = "";
	
	String msg = "";
	String msg_code = "";
	
	String pol_idx_id = "";
	String count = "";
	String sec_pol_desc = "";
	
	String diag_desc = "";
	String diag_majr_code = "";
	
	String idx_status = "";
	String idxstatus = "";
	
	String month_g = "";
	
	String posn_nm = "";
	String under_code = "";
	String lv = "";
	String upper_org_code = "";
	
	String org_level = "";
	
	String descname = "";
	
	String buseo_indc = "";
	String avg_score = "";

	public String getAvg_score() {
		return avg_score;
	}
	public void setAvg_score(String avg_score) {
		this.avg_score = avg_score;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getRpdt_date() {
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

//	String emp_nm;

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
	
	public String getTitle_code() {
		return title_code;
	}
	public void setTitle_code(String title_code) {
		this.title_code = title_code;
	}	
	public String getTitle_nm() {
		return title_nm;
	}
	public void setTitle_nm(String title_nm) {
		this.title_nm = title_nm;
	}	
	public String getLevel_code() {
		return level_code;
	}
	public void setLevel_code(String level_code) {
		this.level_code = level_code;
	}	
	public String getLevel_nm() {
		return level_nm;
	}
	public void setLevel_nm(String level_nm) {
		this.level_nm = level_nm;
	}	
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}	
	public String getMsg_code() {
		return msg_code;
	}
	public void setMsg_code(String msg_code) {
		this.msg_code = msg_code;
	}
	
	/*
	String pol_idx_id = "";
	int count = 0;
	String sec_pol_desc = "";
	*/
	
	public String getPol_idx_id() {
		return pol_idx_id;
	}
	public void setPol_idx_id(String pol_idx_id) {
		this.pol_idx_id = pol_idx_id;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	
	public String getDiag_desc() {
		return diag_desc;
	}
	public void setDiag_desc(String diag_desc) {
		this.diag_desc = diag_desc;
	}
		
	public String getDiag_majr_code() {
		return diag_majr_code;
	}
	public void setDiag_majr_code(String diag_majr_code) {
		this.diag_majr_code = diag_majr_code;
	}	
	
	public String getIdx_status() {
		return idx_status;
	}
	public void setIdx_status(String idx_status) {
		this.idx_status = idx_status;
	}	

	public String getIdxstatus() {
		return idxstatus;
	}
	public void setIdxstatus(String idxstatus) {
		this.idxstatus = idxstatus;
	}		
	
	public String getMonth_g() {
		return month_g;
	}
	public void setMonth_g(String month_g) {
		this.month_g = month_g;
	}

	public String getPosn_nm() {
		return posn_nm;
	}	
	
	public void setPosn_nm(String posn_nm) {
		this.posn_nm = posn_nm;
	}
	
	public String getUnder_code() {
		return under_code;
	}
	public void setUnder_code(String under_code) {
		this.under_code = under_code;
	}	

	public String getLv() {
		return lv;
	}
	public void setLv(String lv) {
		this.lv = lv;
	}		
	
	public String getUpper_org_code() {
		return upper_org_code;
	}
	public void setUpper_org_code(String upper_org_code) {
		this.upper_org_code = upper_org_code;
	}
		
	public String getOrg_level() {
		// TODO Auto-generated method stub
		return org_level;
	}
	public void setOrg_level(String org_level) {
		this.org_level = org_level;
	}
	
	public String getDescname() {
		return descname;
	}
	
	public void setDescname(String descname) {
		this.descname = descname;
	}
	
	public String getBuseo_indc() {
		return buseo_indc;
	}
	public void setBuseo_indc(String buseo_indc) {
		this.buseo_indc = buseo_indc;
	}	
}
