package sdiag.man.service;

import java.io.Serializable;

public class UserinfoVO implements Serializable{

	// TB - "user_info"
	private String id="";  		// ID
	private String user_auth="";	// 권한 : 1: 관리자, 2 : 운영자
    private String email_indc="Y";	// 이메일수신 여부
    private String emp_indc="";	// 임직원 여부서
	private String pswd="";		// 비밀번호
	private String mac="";			// MAC
	private String ip="";			// IP
	private String rgdt_date;	// 등록일
	private String updt_date;	// 수정일
	private String stat_indc="";	// 상태코드 여부
	private String isused="Y";		// 
	
	
	// TB - "ORG_USER"
	private String emp_no;  	// 사번
	private String emp_nm="";  	// 성명
	private String email;  		// email
	private String org_code;  	// 조직코드
	private String stat; 		// 상태
	private String posn_nm; 	// 포지션 명칭
	private String updt_indc;  	// 수정 여부
	private String title_code; 	// 호칭코드
	private String title_nm; 	// 호칭명
	private String level_code; 	// 직책코드
	private String level_nm;  	// 직책명
	/**
	 * 조직명추가 - 김재락
	 */
	private String org_nm; //조직명
	
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_auth() {
		return user_auth;
	}
	public void setUser_auth(String user_auth) {
		this.user_auth = user_auth;
	}
	public String getEmail_indc() {
		return email_indc;
	}
	public void setEmail_indc(String email_indc) {
		this.email_indc = email_indc;
	}
	public String getEmp_indc() {
		return emp_indc;
	}
	public void setEmp_indc(String emp_indc) {
		this.emp_indc = emp_indc;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
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
	public String getStat_indc() {
		return stat_indc;
	}
	public void setStat_indc(String stat_indc) {
		this.stat_indc = stat_indc;
	}
	public String getIsused() {
		return isused;
	}
	public void setIsused(String isused) {
		this.isused = isused;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getPosn_nm() {
		return posn_nm;
	}
	public void setPosn_nm(String posn_nm) {
		this.posn_nm = posn_nm;
	}
	public String getUpdt_indc() {
		return updt_indc;
	}
	public void setUpdt_indc(String updt_indc) {
		this.updt_indc = updt_indc;
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
	
	
}
