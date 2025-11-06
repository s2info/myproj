package sdiag.login.service;

/**
 * 사용자정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2015.10.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.10.12  CJLee          최초 생성
 *
 * </pre>
 */
public class UserManageVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String emp_no;
	private String emp_nm;
	private String org_code;
	private String title_code;
	private String title_nm;
	
	/**
	 * 사용자 ID
	 */
	private String userid;
	
	/**
	 * 권한
	 */
	private String user_auth;
	
	/**
	 * 이메일 수신여부
	 */
	private String email_indc;
	
	/**
	 * 임직원여부
	 */
	private String emp_indc;  
	
	private String mac;
	
	private String ip;
	
	/**
	 * 등록날짜
	 */
	private String rgdt_date;
	
	/**
	 * 수정날짜
	 */
	private String updt_date;
	
	/**
	 * 비번
	 */
	private String password;
	
	
	private String userName;
	private String deptCD;
	private String deptName;
	private String agencyCD;
	private String agencyName;
	private String positionCD;
	private String positionName;  	
	
	
	/* org_user */
	
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
	
	/* org_user */
		

	public String getuserid() {
		return userid;
	}
	
	public void setuserid(String userid) {
		this.userid = userid;
	}
	
	public String getuser_auth() {
		return user_auth;
	}
	
	public void setuser_auth(String user_auth) {
		this.user_auth = user_auth;
	}
	
	public String getemail_indc() {
		return email_indc;
	}
	
	public void setemail_indc(String email_indc) {
		this.email_indc = email_indc;
	}
	
	public String getemp_indc() {
		return emp_indc;
	}
	
	public void setemp_indc(String emp_indc) {
		this.emp_indc = emp_indc;
	}
	
	public String getmac() {
		return mac;
	}
	
	public void setmac(String mac) {
		this.mac = mac;
	}
	
	public String getip() {
		return ip;
	}
	
	public void setip(String ip) {
		this.ip = ip;
	}
	
	public String getrgdt_date() {
		return rgdt_date;
	}
	
	public void setrgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	
	public String getupdt_date() {
		return updt_date;
	}
	
	public void setupdt_date(String updt_date) {
		this.updt_date = updt_date;
	}
	
	public String getpassword() {
		return password;
	}
	
	public void setpassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}	
	public void setUserName(String userName) {
		this.userName = userName;
		
	}
	
	public String getDeptCD() {
		return deptCD;
	}	
	public void setDeptCD(String deptCD) {
		this.deptCD = deptCD;
		
	}
	
	public String getDeptName() {
		return deptName;
	}	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
		
	}
	
	public String getAgencyCD() {
		return agencyCD;
	}	
	public void setAgencyCD(String agencyCD) {
		this.agencyCD = agencyCD;
		
	}
	
	public String getAgencyName() {
		return agencyName;
	}	
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
		
	}
	
	public String getPositionCD() {
		return positionCD;
	}	
	public void setPositionCD(String positionCD) {
		this.positionCD = positionCD;
		
	}
	
	public String getPositionName() {
		return positionName;
	}	
	public void setPositionName(String positionName) {
		this.positionName = positionName;
		
	}	
	
}
