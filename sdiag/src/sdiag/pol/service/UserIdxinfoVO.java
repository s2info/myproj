package sdiag.pol.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class UserIdxinfoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388345580860975987L;

	private String idx_rgdt_date;
	private String emp_no;
	private String mac;
	private String pol_idx_id;
	private String ip;
	private String appr_stat_code;
	private String summ_appl_desc;
	private long score;
	private long count;
	private String sanc_date;
	private String sanc_id;
	private String sanc_indc;
	private String sol_id;
	private String evid_file_name;
	private String evid_file_loc;
	private String rgdt_date;
	private String updt_date;
	private String appr_id;
	private String org_nm;
	private String emp_nm;
	
	public String getAppr_id() {
		return appr_id;
	}
	public void setAppr_id(String appr_id) {
		this.appr_id = appr_id;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getEmp_nm() {
		return emp_nm;
	}
	public void setEmp_nm(String emp_nm) {
		this.emp_nm = emp_nm;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
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
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getSanc_date() {
		return sanc_date;
	}
	public void setSanc_date(String sanc_date) {
		this.sanc_date = sanc_date;
	}
	public String getSanc_id() {
		return sanc_id;
	}
	public void setSanc_id(String sanc_id) {
		this.sanc_id = sanc_id;
	}
	public String getSanc_indc() {
		return sanc_indc;
	}
	public void setSanc_indc(String sanc_indc) {
		this.sanc_indc = sanc_indc;
	}
	public String getSol_id() {
		return sol_id;
	}
	public void setSol_id(String sol_id) {
		this.sol_id = sol_id;
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
	
	
}
