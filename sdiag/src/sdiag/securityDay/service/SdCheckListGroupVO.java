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
public class SdCheckListGroupVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 seq
	private int clGroupNo=0;
	//점검표명
	private String clGroupNm;
	//점검 시작일
	private String checkStartDay;
	//점검 종료일
	private String checkEndDay;
	//등록자 사번     
	private String rgdtEmpNo;
	//등록자 명
	private String rgdtEmpNm;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//지수화 시작일
	private String idxStartDay;
	//지수화 종료일
	private String idxEndDay;
	//점검표 seq
	private String m_sdCheckNo;
	//점검표 seq
	private String clGroupNoList;
	
	//안내문구
	private String infoText;
	
	
	
	public String getM_sdCheckNo() {
		return m_sdCheckNo;
	}
	public void setM_sdCheckNo(String m_sdCheckNo) {
		this.m_sdCheckNo = m_sdCheckNo;
	}
	public int getClGroupNo() {
		return clGroupNo;
	}
	public void setClGroupNo(int clGroupNo) {
		this.clGroupNo = clGroupNo;
	}
	public String getClGroupNm() {
		return clGroupNm;
	}
	public void setClGroupNm(String clGroupNm) {
		this.clGroupNm = clGroupNm;
	}
	public String getCheckStartDay() {
		return checkStartDay;
	}
	public void setCheckStartDay(String checkStartDay) {
		this.checkStartDay = checkStartDay;
	}
	public String getCheckEndDay() {
		return checkEndDay;
	}
	public void setCheckEndDay(String checkEndDay) {
		this.checkEndDay = checkEndDay;
	}
	public String getRgdtEmpNo() {
		return rgdtEmpNo;
	}
	public void setRgdtEmpNo(String rgdtEmpNo) {
		this.rgdtEmpNo = rgdtEmpNo;
	}
	public String getRgdtEmpNm() {
		return rgdtEmpNm;
	}
	public void setRgdtEmpNm(String rgdtEmpNm) {
		this.rgdtEmpNm = rgdtEmpNm;
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
	public String getIdxStartDay() {
		return idxStartDay;
	}
	public void setIdxStartDay(String idxStartDay) {
		this.idxStartDay = idxStartDay;
	}
	public String getIdxEndDay() {
		return idxEndDay;
	}
	public void setIdxEndDay(String idxEndDay) {
		this.idxEndDay = idxEndDay;
	}
	public String getClGroupNoList() {
		return clGroupNoList;
	}
	public void setClGroupNoList(String clGroupNoList) {
		this.clGroupNoList = clGroupNoList;
	}
	public String getInfoText() {
		return infoText;
	}
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
	
}
