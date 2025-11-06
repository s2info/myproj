package sdiag.sanct.service;

import java.io.Serializable;

public class SanctSearchResultListVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5684753663259887382L;
	
	private String org_code = "";
	private String emp_no = "";
	private String emp_nm = "";
	private String org_nm = "";
	private int score = 0;
	private int count = 0;
	private String idx_rgdt_date = "";
	private String pol_idx_id = "";
	private String mac = "";
	private String ip = "";
	private String appr_id = "";
	private String sanc_id = "";
	private String pcg_act_id = "";
	private String is_pcg_act_id = "";
	private String isexpn = "";
	private String appr_stat_code = "";
	private String appr_stat_name = "";
	private String appr_line_code = "";
	private String pol_stat_code = "";
	private String pol_stat_color = "";
	private String pol_stat_name = "";
	private String is_sanct = "";
	private String sanct_kind = "";
	private String sanct_name = "";
	private String sanct_cate = "";
	private String is_pcgact = "";
	private String pcgact_kind = "";
	private String pcgact_name = "";
	private String exe_para = "";
	private boolean sanc_indc = false;
	private boolean pcg_indc = false;
	private String is_bother_txt = "";
	private String is_apprstatus_txt = "";
	private String pol_idx_name = "";
	private String is_bother_type = "";
	private String sanc_date = "";
	private String re_sanc_date = "";
	private String is_apprstatus_type = "";
	private String appr_date = "";
	private String re_appr_date = "";
	private String eventdate="";
	private int appr_cnt=0;
	
	public String getEventdate() {
		return eventdate;
	}
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}
	public String getIs_bother_type() {
		return is_bother_type;
	}
	public void setIs_bother_type(String is_bother_type) {
		this.is_bother_type = is_bother_type;
	}
	public String getSanc_date() {
		return sanc_date;
	}
	public void setSanc_date(String sanc_date) {
		this.sanc_date = sanc_date;
	}
	public String getRe_sanc_date() {
		return re_sanc_date;
	}
	public void setRe_sanc_date(String re_sanc_date) {
		this.re_sanc_date = re_sanc_date;
	}
	public String getIs_apprstatus_type() {
		return is_apprstatus_type;
	}
	public void setIs_apprstatus_type(String is_apprstatus_type) {
		this.is_apprstatus_type = is_apprstatus_type;
	}
	public String getAppr_date() {
		return appr_date;
	}
	public void setAppr_date(String appr_date) {
		this.appr_date = appr_date;
	}
	public String getRe_appr_date() {
		return re_appr_date;
	}
	public void setRe_appr_date(String re_appr_date) {
		this.re_appr_date = re_appr_date;
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
	public String getEmp_nm() {
		return emp_nm;
	}
	public void setEmp_nm(String emp_nm) {
		this.emp_nm = emp_nm;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
	}
	public String getPol_idx_id() {
		return pol_idx_id;
	}
	public void setPol_idx_id(String pol_idx_id) {
		this.pol_idx_id = pol_idx_id;
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
	public String getAppr_id() {
		return appr_id;
	}
	public void setAppr_id(String appr_id) {
		this.appr_id = appr_id;
	}
	public String getSanc_id() {
		return sanc_id;
	}
	public void setSanc_id(String sanc_id) {
		this.sanc_id = sanc_id;
	}
	public String getPcg_act_id() {
		return pcg_act_id;
	}
	public void setPcg_act_id(String pcg_act_id) {
		this.pcg_act_id = pcg_act_id;
	}
	public String getIs_pcg_act_id() {
		return is_pcg_act_id;
	}
	public void setIs_pcg_act_id(String is_pcg_act_id) {
		this.is_pcg_act_id = is_pcg_act_id;
	}
	public String getIsexpn() {
		return isexpn;
	}
	public void setIsexpn(String isexpn) {
		this.isexpn = isexpn;
	}
	public String getAppr_stat_code() {
		return appr_stat_code;
	}
	public void setAppr_stat_code(String appr_stat_code) {
		this.appr_stat_code = appr_stat_code;
	}
	public String getAppr_stat_name() {
		return appr_stat_name;
	}
	public void setAppr_stat_name(String appr_stat_name) {
		this.appr_stat_name = appr_stat_name;
	}
	public String getAppr_line_code() {
		return appr_line_code;
	}
	public void setAppr_line_code(String appr_line_code) {
		this.appr_line_code = appr_line_code;
	}
	public String getPol_stat_code() {
		return pol_stat_code;
	}
	public void setPol_stat_code(String pol_stat_code) {
		this.pol_stat_code = pol_stat_code;
	}
	public String getIs_sanct() {
		return is_sanct;
	}
	public void setIs_sanct(String is_sanct) {
		this.is_sanct = is_sanct;
	}
	public String getSanct_kind() {
		return sanct_kind;
	}
	public void setSanct_kind(String sanct_kind) {
		this.sanct_kind = sanct_kind;
	}
	public String getSanct_name() {
		return sanct_name;
	}
	public void setSanct_name(String sanct_name) {
		this.sanct_name = sanct_name;
	}
	public String getSanct_cate() {
		return sanct_cate;
	}
	public void setSanct_cate(String sanct_cate) {
		this.sanct_cate = sanct_cate;
	}
	public String getIs_pcgact() {
		return is_pcgact;
	}
	public void setIs_pcgact(String is_pcgact) {
		this.is_pcgact = is_pcgact;
	}
	public String getPcgact_kind() {
		return pcgact_kind;
	}
	public void setPcgact_kind(String pcgact_kind) {
		this.pcgact_kind = pcgact_kind;
	}
	public String getPcgact_name() {
		return pcgact_name;
	}
	public void setPcgact_name(String pcgact_name) {
		this.pcgact_name = pcgact_name;
	}
	public String getExe_para() {
		return exe_para;
	}
	public void setExe_para(String exe_para) {
		this.exe_para = exe_para;
	}

	public boolean isSanc_indc() {
		return sanc_indc;
	}
	public void setSanc_indc(boolean sanc_indc) {
		this.sanc_indc = sanc_indc;
	}
	public boolean isPcg_indc() {
		return pcg_indc;
	}
	public void setPcg_indc(boolean pcg_indc) {
		this.pcg_indc = pcg_indc;
	}
	public String getIs_bother_txt() {
		return is_bother_txt;
	}
	public void setIs_bother_txt(String is_bother_txt) {
		this.is_bother_txt = is_bother_txt;
	}
	public String getIs_apprstatus_txt() {
		return is_apprstatus_txt;
	}
	public void setIs_apprstatus_txt(String is_apprstatus_txt) {
		this.is_apprstatus_txt = is_apprstatus_txt;
	}
	public String getPol_idx_name() {
		return pol_idx_name;
	}
	public void setPol_idx_name(String pol_idx_name) {
		this.pol_idx_name = pol_idx_name;
	}
	public int getAppr_cnt() {
		return appr_cnt;
	}
	public void setAppr_cnt(int appr_cnt) {
		this.appr_cnt = appr_cnt;
	}
	public String getPol_stat_color() {
		return pol_stat_color;
	}
	public void setPol_stat_color(String pol_stat_color) {
		this.pol_stat_color = pol_stat_color;
	}
	public String getPol_stat_name() {
		return pol_stat_name;
	}
	public void setPol_stat_name(String pol_stat_name) {
		this.pol_stat_name = pol_stat_name;
	}
	
	

}
