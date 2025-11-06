package sdiag.securityDay.service;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class SdQuestionMcVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 seq
	private int sdCheckNo;
	//문항번호
	private int questionNum;
	//보기 번호
	private String exNum;
	//보기 내용
	private String exText;
	//등록자 사번     
	private String rgdtEmpNo;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//등록자 명
	private String rgdtEmpNm;
	
	//보기 번호(수정할)
	private String u_exNum;
	//보기 내용(수정할)
	private String u_exText;
	
	public int getSdCheckNo() {
		return sdCheckNo;
	}
	public void setSdCheckNo(int sdCheckNo) {
		this.sdCheckNo = sdCheckNo;
	}
	public int getQuestionNum() {
		return questionNum;
	}
	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}
	public String getExNum() {
		return exNum;
	}
	public void setExNum(String exNum) {
		this.exNum = exNum;
	}
	public String getExText() {
		return exText;
	}
	public void setExText(String exText) {
		this.exText = exText;
	}
	public String getRgdtEmpNo() {
		return rgdtEmpNo;
	}
	public void setRgdtEmpNo(String rgdtEmpNo) {
		this.rgdtEmpNo = rgdtEmpNo;
	}
	public String getRgdtDate() {
		return rgdtDate;
	}
	public void setRgdtDate(String rgdtDate) {
		this.rgdtDate = rgdtDate;
	}
	public String getUpdtDate() {
		return updtDate;
	}
	public void setUpdtDate(String updtDate) {
		this.updtDate = updtDate;
	}
	public String getRgdtEmpNm() {
		return rgdtEmpNm;
	}
	public void setRgdtEmpNm(String rgdtEmpNm) {
		this.rgdtEmpNm = rgdtEmpNm;
	}
	public String getU_exNum() {
		return u_exNum;
	}
	public void setU_exNum(String u_exNum) {
		this.u_exNum = u_exNum;
	}
	public String getU_exText() {
		return u_exText;
	}
	public void setU_exText(String u_exText) {
		this.u_exText = u_exText;
	}
	
}
