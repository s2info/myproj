package sdiag.exception.service;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class ExceptionVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//보안 정책 ID
	private String sec_pol_id ="" ;
	//보안 정책 명
	private String sec_pol_desc;
	// 사번
	private String emp_no;
	// ip
	private String ip;
	// 설명              
	private String exception_desc;  
	// 등록 일자             
	private String rgdt_date;
	// 화면모드            
	private String formMod;
	// 삭제 할 정책 ID
	private String secPolIdList;
	// 갯수
	private String cnt;
	// 갯수
	private String ex_seq;
	
	// 메일전송 구분
	private String op_indc;
	
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getException_desc() {
		return exception_desc;
	}
	public void setException_desc(String exception_desc) {
		this.exception_desc = exception_desc;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	public String getFormMod() {
		return formMod;
	}
	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}
	public String getSecPolIdList() {
		return secPolIdList;
	}
	public void setSecPolIdList(String secPolIdList) {
		this.secPolIdList = secPolIdList;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getOp_indc() {
		return op_indc;
	}
	public void setOp_indc(String op_indc) {
		this.op_indc = op_indc;
	}
	
	
}
