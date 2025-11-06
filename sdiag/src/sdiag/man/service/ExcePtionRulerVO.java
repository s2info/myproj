package sdiag.man.service;

public class ExcePtionRulerVO {
	
	// 예외자 EXPN
	String emp_no;				// 사번
	String emp_nm;				// 성명
	String org_code;			// 조직코드
	String rgdt_date;			// 등록일
	String updt_date;			// 수정일
	
	String proxyEmpNo;			// 사번/이름
	
	// ORG_USER_HIER
	String org_code_0;          // 조직코드 KT
	String org_nm_0;            // 조직명
	String org_code_1;          // 조직코드 부문
	String org_nm_1;            // 조직명
	String org_code_2;          // 조직코드 본부
	String org_nm_2;            // 조직명
	String org_code_3;          // 조직코드 담당
	String org_nm_3;            // 조직명
	String org_code_4;          // 조직코드 팀
	String org_nm_4;            // 조직명
	String org_level;           // 조직레벨
	String level_code;			// 직책코드
	String level_nm;			// 직책명
	
	// 대무자 PROXY
	String proxy_emp_no;
	
	
	public String getEmp_no() {
		return emp_no;
	}

	public void setEmp_no(String expnDelete) {
		this.emp_no = expnDelete;
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

	public String getProxy_emp_no() {
		return proxy_emp_no;
	}

	public void setProxy_emp_no(String proxy_emp_no) {
		this.proxy_emp_no = proxy_emp_no;
	}

	public String getProxyEmpNo() {
		return proxyEmpNo;
	}

	public void setProxyEmpNo(String proxyEmpNo) {
		this.proxyEmpNo = proxyEmpNo;
	}

	public String getOrg_code_0() {
		return org_code_0;
	}

	public void setOrg_code_0(String org_code_0) {
		this.org_code_0 = org_code_0;
	}

	public String getOrg_nm_0() {
		return org_nm_0;
	}

	public void setOrg_nm_0(String org_nm_0) {
		this.org_nm_0 = org_nm_0;
	}

	public String getOrg_code_1() {
		return org_code_1;
	}

	public void setOrg_code_1(String org_code_1) {
		this.org_code_1 = org_code_1;
	}

	public String getOrg_nm_1() {
		return org_nm_1;
	}

	public void setOrg_nm_1(String org_nm_1) {
		this.org_nm_1 = org_nm_1;
	}

	public String getOrg_code_2() {
		return org_code_2;
	}

	public void setOrg_code_2(String org_code_2) {
		this.org_code_2 = org_code_2;
	}

	public String getOrg_nm_2() {
		return org_nm_2;
	}

	public void setOrg_nm_2(String org_nm_2) {
		this.org_nm_2 = org_nm_2;
	}

	public String getOrg_code_3() {
		return org_code_3;
	}

	public void setOrg_code_3(String org_code_3) {
		this.org_code_3 = org_code_3;
	}

	public String getOrg_nm_3() {
		return org_nm_3;
	}

	public void setOrg_nm_3(String org_nm_3) {
		this.org_nm_3 = org_nm_3;
	}

	public String getOrg_code_4() {
		return org_code_4;
	}

	public void setOrg_code_4(String org_code_4) {
		this.org_code_4 = org_code_4;
	}

	public String getOrg_nm_4() {
		return org_nm_4;
	}

	public void setOrg_nm_4(String org_nm_4) {
		this.org_nm_4 = org_nm_4;
	}

	public String getOrg_level() {
		return org_level;
	}

	public void setOrg_level(String org_level) {
		this.org_level = org_level;
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
