package sdiag.groupInfo.service;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class GroupInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//그룹 seq
	private int groupCode;
	// 그룹 명
	private String groupNm;
	//그룹 설명
	private String groupExt;
	//등록자 사번     
	private String rgdtEmpNo;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//등록자 명
	private String rgdtEmpNm;
	//그룹코드 리스트
	private String groupCodeList;
	//그룹 타입
	private String groupType;
	
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupNm() {
		return groupNm;
	}
	public void setGroupNm(String groupNm) {
		this.groupNm = groupNm;
	}
	public String getGroupExt() {
		return groupExt;
	}
	public void setGroupExt(String groupExt) {
		this.groupExt = groupExt;
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
	public String getGroupCodeList() {
		return groupCodeList;
	}
	public void setGroupCodeList(String groupCodeList) {
		this.groupCodeList = groupCodeList;
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
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	
	
}
