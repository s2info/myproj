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
public class SdTargetInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 그룹 seq
	private int clGroupNo =0;
	//점검표 seq
	private int sdCheckNo =0;
	//구분(대상, 예외)
	private String gubun;
	//대상 타입
	private String targetType;
	//대상 코드(사번 or 조직코드 or 그룹코드)
	private String targetCode;
	//대상명
	private String targetNm;
	//등록일           
	private String rgdtDate;
	//점검표명
	private String checklistNm;
	//점검표 seq
	private String sdCheckNoList;
	
	public int getClGroupNo() {
		return clGroupNo;
	}
	public void setClGroupNo(int clGroupNo) {
		this.clGroupNo = clGroupNo;
	}
	public int getSdCheckNo() {
		return sdCheckNo;
	}
	public void setSdCheckNo(int sdCheckNo) {
		this.sdCheckNo = sdCheckNo;
	}
	public String getSdCheckNoList() {
		return sdCheckNoList;
	}
	public void setSdCheckNoList(String sdCheckNoList) {
		this.sdCheckNoList = sdCheckNoList;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getRgdtDate() {
		return rgdtDate;
	}
	public void setRgdtDate(String rgdtDate) {
		this.rgdtDate = rgdtDate;
	}
	public String getTargetNm() {
		return targetNm;
	}
	public void setTargetNm(String targetNm) {
		this.targetNm = targetNm;
	}
	public String getChecklistNm() {
		return checklistNm;
	}
	public void setChecklistNm(String checklistNm) {
		this.checklistNm = checklistNm;
	}
	
}
