package sdiag.main.service;

/**
 * 사용자 정책 별 점수 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.02.01
 * @version 1.0
 * @see
 *
 * 
 *
 * </pre>
 */
public class UserPolIdxInfoVO{

	
	
	// 사용자 사번
	private String emp_no;
	private String emp_nm;
	// 보안 정책 ID
	private String sec_pol_id;
	// 보안 솔루션 ID
	private String sec_sol_id;
	// 보안 정책 설명
	private String sec_pol_desc;
	// 지수화 등록일
	private String idx_rgdt_date;
	// 발생사유
	private String reason;
	// MAC
	private String mac;
	// 지수화 정책 ID
	private String pol_idx_id;
	// 사용자 ip
	private String ip;
	// 점수
	private String score;
	
	private String avg_score="";
	private String buseo_indc="";
	// 건수
	private String count;
	//상태
	private String critical;
	// 등록일
	private String rgdt_date;
	// 
	private String org_eventdate;
	// 조치방안 상세
	private String detail;
	// 소명신청 ID
	private String appr_id;
	private String org_code="";
	private String org_nm="";
	private String diag_majr_code="";
	private String diag_desc=""; 
	private String scorestat="";
	private String scorestat_name="";
	private String scorestat_color="";
	private String diag_icon_info="";
	private String is_sub_org="N";
	private String search_rgdt_date="";
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getScorestat_name() {
		return scorestat_name;
	}
	public void setScorestat_name(String scorestat_name) {
		this.scorestat_name = scorestat_name;
	}
	public String getScorestat_color() {
		return scorestat_color;
	}
	public void setScorestat_color(String scorestat_color) {
		this.scorestat_color = scorestat_color;
	}
	public String getAvg_score() {
		return avg_score;
	}
	public void setAvg_score(String avg_score) {
		this.avg_score = avg_score;
	}
	public String getBuseo_indc() {
		return buseo_indc;
	}
	public void setBuseo_indc(String buseo_indc) {
		this.buseo_indc = buseo_indc;
	}
	public String getDiag_majr_code() {
		return diag_majr_code;
	}
	public void setDiag_majr_code(String diag_majr_code) {
		this.diag_majr_code = diag_majr_code;
	}
	public String getDiag_desc() {
		return diag_desc;
	}
	public void setDiag_desc(String diag_desc) {
		this.diag_desc = diag_desc;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
	}
	public String getSec_sol_id() {
		return sec_sol_id;
	}
	public void setSec_sol_id(String sec_sol_id) {
		this.sec_sol_id = sec_sol_id;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCritical() {
		return critical;
	}
	public void setCritical(String critical) {
		this.critical = critical;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getOrg_eventdate() {
		return org_eventdate;
	}
	public void setOrg_eventdate(String org_eventdate) {
		this.org_eventdate = org_eventdate;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAppr_id() {
		return appr_id;
	}
	public void setAppr_id(String appr_id) {
		this.appr_id = appr_id;
	}
	public String getScorestat() {
		return scorestat;
	}
	public void setScorestat(String scorestat) {
		this.scorestat = scorestat;
	}
	public String getDiag_icon_info() {
		return diag_icon_info;
	}
	public void setDiag_icon_info(String diag_icon_info) {
		this.diag_icon_info = diag_icon_info;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getIs_sub_org() {
		return is_sub_org;
	}
	public void setIs_sub_org(String is_sub_org) {
		this.is_sub_org = is_sub_org;
	}
	public String getEmp_nm() {
		return emp_nm;
	}
	public void setEmp_nm(String emp_nm) {
		this.emp_nm = emp_nm;
	}
	public String getSearch_rgdt_date() {
		return search_rgdt_date;
	}
	public void setSearch_rgdt_date(String search_rgdt_date) {
		this.search_rgdt_date = search_rgdt_date;
	}
	
		
}
