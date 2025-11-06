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
public class SdCheckListVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 seq
	private int sdCheckNo;
	//점검표명
	private String checklistNm;
	//점검표 사용여부              
	private String useYn;  
	//등록자 사번     
	private String rgdtEmpNo;
	//등록자 명
	private String rgdtEmpNm;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//삭제할 점검표 LIST
	private String sdCheckNoList;

	
		
	public int getSdCheckNo() {
		return sdCheckNo;
	}
	public void setSdCheckNo(int sdCheckNo) {
		this.sdCheckNo = sdCheckNo;
	}
	public String getChecklistNm() {
		return checklistNm;
	}
	public void setChecklistNm(String checklistNm) {
		this.checklistNm = checklistNm;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	public String getSdCheckNoList() {
		return sdCheckNoList;
	}
	public void setSdCheckNoList(String sdCheckNoList) {
		this.sdCheckNoList = sdCheckNoList;
	}

}
