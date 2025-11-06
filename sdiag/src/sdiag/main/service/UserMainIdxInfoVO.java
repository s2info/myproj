package sdiag.main.service;

/**
 * 사용자, 사용자 팀, 사용자 상위 팀 보안점수 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.02.01
 * @version 1.0
 * @see
 *
 * 
 */
public class UserMainIdxInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 사번
	private String emp_no;
	// 사용자 이름
	private String emp_nm;
	// 등록 일자             
	private String rgdt_date;
	// 수정된 점수                
	private String scor_curr;  
	// 점수                
	private String score;  
	// 등록 연도                
	private String year_val;  
	// 등록 일자               
	private String day_val;  
	// 등록 월              
	private String month_val;  
	// 이메일                
	private String email;
	//사용자 팀 코드               
	private String u_org_code; 
	//사용자 팀 명                
	private String u_org_nm;  
	//사용자  팀 사원수                
	private String u_tot_emp;  
	//사용자 팀 점수 평균                
	private String u_avg;  
	//사용자 상위 팀 코드               
	private String upper_org_code;  
	//사용자 상위 팀 명                
	private String upper_org_nm;  
	//사용자 상위 팀 전체 사원수           
	private String upper_org_tot_emp;  
	//사용자 상위 팀 평균              
	private String upper_avg;
	//전사 평균             
	private String avg_val;
	private String scorestat="";
	private String scorestat_name="";
	private String scorestat_color="";
	private String u_scorestat_name="";
	private String u_scorestat_color="";
	private String upper_scorestat_name="";
	private String upper_scorestat_color="";
	private String emp_scorestat_name="";
	private String emp_scorestat_color="";
	private String graph_panel_width = "900px";
	private String collabor_org_code="";
	private String iscollabor_cap="";
	private String collabo_scorestat_name="";
	private String collabor_org_nm="";
	private String collabor_org_tot_emp="";
	private String collabo_avg_val="";
	private String collabo_scorestat_color="";
	private String scorelist_panel_height="400";
	private String iscollabor;
	public String getCollabor_org_code() {
		return collabor_org_code;
	}
	public void setCollabor_org_code(String collabor_org_code) {
		this.collabor_org_code = collabor_org_code;
	}
	public String getIscollabor_cap() {
		return iscollabor_cap;
	}
	public void setIscollabor_cap(String iscollabor_cap) {
		this.iscollabor_cap = iscollabor_cap;
	}
	public String getCollabo_scorestat_name() {
		return collabo_scorestat_name;
	}
	public void setCollabo_scorestat_name(String collabo_scorestat_name) {
		this.collabo_scorestat_name = collabo_scorestat_name;
	}
	public String getCollabor_org_nm() {
		return collabor_org_nm;
	}
	public void setCollabor_org_nm(String collabor_org_nm) {
		this.collabor_org_nm = collabor_org_nm;
	}
	public String getCollabor_org_tot_emp() {
		return collabor_org_tot_emp;
	}
	public void setCollabor_org_tot_emp(String collabor_org_tot_emp) {
		this.collabor_org_tot_emp = collabor_org_tot_emp;
	}
	public String getCollabo_avg_val() {
		return collabo_avg_val;
	}
	public void setCollabo_avg_val(String collabo_avg_val) {
		this.collabo_avg_val = collabo_avg_val;
	}
	public String getCollabo_scorestat_color() {
		return collabo_scorestat_color;
	}
	public void setCollabo_scorestat_color(String collabo_scorestat_color) {
		this.collabo_scorestat_color = collabo_scorestat_color;
	}
	public String getEmp_scorestat_name() {
		return emp_scorestat_name;
	}
	public void setEmp_scorestat_name(String emp_scorestat_name) {
		this.emp_scorestat_name = emp_scorestat_name;
	}
	public String getEmp_scorestat_color() {
		return emp_scorestat_color;
	}
	public void setEmp_scorestat_color(String emp_scorestat_color) {
		this.emp_scorestat_color = emp_scorestat_color;
	}
	public String getU_scorestat_name() {
		return u_scorestat_name;
	}
	public void setU_scorestat_name(String u_scorestat_name) {
		this.u_scorestat_name = u_scorestat_name;
	}
	public String getU_scorestat_color() {
		return u_scorestat_color;
	}
	public void setU_scorestat_color(String u_scorestat_color) {
		this.u_scorestat_color = u_scorestat_color;
	}
	public String getUpper_scorestat_name() {
		return upper_scorestat_name;
	}
	public void setUpper_scorestat_name(String upper_scorestat_name) {
		this.upper_scorestat_name = upper_scorestat_name;
	}
	public String getUpper_scorestat_color() {
		return upper_scorestat_color;
	}
	public void setUpper_scorestat_color(String upper_scorestat_color) {
		this.upper_scorestat_color = upper_scorestat_color;
	}
	public String getScorestat() {
		return scorestat;
	}
	public void setScorestat(String scorestat) {
		this.scorestat = scorestat;
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
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getScor_curr() {
		return scor_curr;
	}
	public void setScor_curr(String scor_curr) {
		this.scor_curr = scor_curr;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getYear_val() {
		return year_val;
	}
	public void setYear_val(String year_val) {
		this.year_val = year_val;
	}
	public String getDay_val() {
		return day_val;
	}
	public void setDay_val(String day_val) {
		this.day_val = day_val;
	}
	public String getMonth_val() {
		return month_val;
	}
	public void setMonth_val(String month_val) {
		this.month_val = month_val;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getU_org_code() {
		return u_org_code;
	}
	public void setU_org_code(String u_org_code) {
		this.u_org_code = u_org_code;
	}
	public String getU_org_nm() {
		return u_org_nm;
	}
	public void setU_org_nm(String u_org_nm) {
		this.u_org_nm = u_org_nm;
	}
	public String getU_tot_emp() {
		return u_tot_emp;
	}
	public void setU_tot_emp(String u_tot_emp) {
		this.u_tot_emp = u_tot_emp;
	}
	public String getU_avg() {
		return u_avg;
	}
	public void setU_avg(String u_avg) {
		this.u_avg = u_avg;
	}
	public String getUpper_org_code() {
		return upper_org_code;
	}
	public void setUpper_org_code(String upper_org_code) {
		this.upper_org_code = upper_org_code;
	}
	public String getUpper_org_nm() {
		return upper_org_nm;
	}
	public void setUpper_org_nm(String upper_org_nm) {
		this.upper_org_nm = upper_org_nm;
	}
	public String getUpper_org_tot_emp() {
		return upper_org_tot_emp;
	}
	public void setUpper_org_tot_emp(String upper_org_tot_emp) {
		this.upper_org_tot_emp = upper_org_tot_emp;
	}
	public String getUpper_avg() {
		return upper_avg;
	}
	public void setUpper_avg(String upper_avg) {
		this.upper_avg = upper_avg;
	}
	public String getAvg_val() {
		return avg_val;
	}
	public void setAvg_val(String avg_val) {
		this.avg_val = avg_val;
	}
	public String getGraph_panel_width() {
		return graph_panel_width;
	}
	public void setGraph_panel_width(String graph_panel_width) {
		this.graph_panel_width = graph_panel_width;
	}
	public String getScorelist_panel_height() {
		return scorelist_panel_height;
	}
	public void setScorelist_panel_height(String scorelist_panel_height) {
		this.scorelist_panel_height = scorelist_panel_height;
	}
	public String getIscollabor() {
		return iscollabor;
	}
	public void setIscollabor(String iscollabor) {
		this.iscollabor = iscollabor;
	}  
	
	
	
}
