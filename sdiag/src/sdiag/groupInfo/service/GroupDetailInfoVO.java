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
public class GroupDetailInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//그룹 코드
	private int groupCode;
	//구분
	private String orgType;
	//org 정보(사번 또는 조직 코드)
	private String orgInfo;
	//org 이름(이름 또는 조직 명)
	private String orgNm;
	//구분
	private String[] orgTypeArr;
	//org 정보(사번 또는 조직 코드)
	private String[] orgInfoArr;
	//org 이름(이름 또는 조직 명)
	private String[] orgNmArr;
	//등록자 사번     
	private String rgdtEmpNo;
	//등록자 명
	private String rgdtEmpNm;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//부서
	private String orgPosition;
	
	//부서
	private String groupInfo;
	//부서
	private String groupInfoNm;
	//empNo 리스트
	private String empNoList;
	
	//수동 쿼리
	private String queryStr;


	
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getOrgInfo() {
		return orgInfo;
	}
	public void setOrgInfo(String orgInfo) {
		this.orgInfo = orgInfo;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String[] getOrgTypeArr() {
		return orgTypeArr;
	}
	public void setOrgTypeArr(String[] orgTypeArr) {
		this.orgTypeArr = orgTypeArr;
	}
	public String[] getOrgInfoArr() {
		return orgInfoArr;
	}
	public String getGroupInfo() {
		return groupInfo;
	}
	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
	}
	public String getGroupInfoNm() {
		return groupInfoNm;
	}
	public void setGroupInfoNm(String groupInfoNm) {
		this.groupInfoNm = groupInfoNm;
	}
	public void setOrgInfoArr(String[] orgInfoArr) {
		this.orgInfoArr = orgInfoArr;
	}
	public String[] getOrgNmArr() {
		return orgNmArr;
	}
	public void setOrgNmArr(String[] orgNmArr) {
		this.orgNmArr = orgNmArr;
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
	public String getOrgPosition() {
		return orgPosition;
	}
	public void setOrgPosition(String orgPosition) {
		this.orgPosition = orgPosition;
	}
	public String getEmpNoList() {
		return empNoList;
	}
	public void setEmpNoList(String empNoList) {
		this.empNoList = empNoList;
	}
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
}
