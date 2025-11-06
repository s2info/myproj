package sdiag.man.service;

import java.io.Serializable;

public class UserSearchInfoVO implements Serializable{

	
	// TB - "ORG_USER"
	private String empNo;  	// 사번
	private String empNm="";  	// 성명
	private String posnNm; 	// 포지션 명칭
	private String secPolId;  	// 수정 여부
	private String secPolDesc; 	// 호칭코드
	private String groupCode; 	// 호칭명
	private String groupNm; 	// 직책코드
	private String orgType;  	// 그룹 타입
	private String orgInfo;  	// 그룹 타입
	private String orgNm;  	// 그룹 타입
	
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public String getPosnNm() {
		return posnNm;
	}
	public void setPosnNm(String posnNm) {
		this.posnNm = posnNm;
	}
	public String getSecPolId() {
		return secPolId;
	}
	public void setSecPolId(String secPolId) {
		this.secPolId = secPolId;
	}
	public String getSecPolDesc() {
		return secPolDesc;
	}
	public void setSecPolDesc(String secPolDesc) {
		this.secPolDesc = secPolDesc;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupNm() {
		return groupNm;
	}
	public void setGroupNm(String groupNm) {
		this.groupNm = groupNm;
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
	
}
