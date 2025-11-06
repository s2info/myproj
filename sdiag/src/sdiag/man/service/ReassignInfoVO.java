package sdiag.man.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReassignInfoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8268660506034761897L;
	

	/** 협력사 사번 리스트 */                      
	private String kpEmpNoList;
	/** 협력사 사번 */                      
	private String kpEmpNo;
	/** 협력사 이름 */                 
	private String kpEmpNm;
	/** KT 담당자 사번 */                 
	private String ktEmpNo;
	/** KT 담당자 이름*/                 
	private String ktEmpNm;
	/** 재지정 담당자 이름 */                 
	private String reEmpNm;
	/** 재지정 담당자 사번*/
	private String reEmpNo;
	/** 협력사 회사명 */                 
	private String userBizNm;
	/** 협력사 유형 */                 
	private String cType;
	/** 등록자 사번 */                 
	private String rgdtEmpNo;
	/** 등록일 */                 
	private String rgdtDate;
	/** 수정자 사번 */                 
	private String updtEmpNo;
	/** 수정일 */                 
	private String updtDate;
	/** 협력사 유형 코드 */                 
	private String collaborCode;
	/** KT 담당자 조직  */                 
	private String ktPosnNm;
	/** 재지정 담당자 조직 */                 
	private String rePosnNm;
	private String infoList;
	
	
	public String getKpEmpNoList() {
		return kpEmpNoList;
	}
	public void setKpEmpNoList(String kpEmpNoList) {
		this.kpEmpNoList = kpEmpNoList;
	}
	public String getKpEmpNo() {
		return kpEmpNo;
	}
	public void setKpEmpNo(String kpEmpNo) {
		this.kpEmpNo = kpEmpNo;
	}
	public String getKpEmpNm() {
		return kpEmpNm;
	}
	public void setKpEmpNm(String kpEmpNm) {
		this.kpEmpNm = kpEmpNm;
	}
	public String getKtEmpNo() {
		return ktEmpNo;
	}
	public void setKtEmpNo(String ktEmpNo) {
		this.ktEmpNo = ktEmpNo;
	}
	public String getKtEmpNm() {
		return ktEmpNm;
	}
	public void setKtEmpNm(String ktEmpNm) {
		this.ktEmpNm = ktEmpNm;
	}

	public String getReEmpNm() {
		return reEmpNm;
	}
	public void setReEmpNm(String reEmpNm) {
		this.reEmpNm = reEmpNm;
	}
	public String getReEmpNo() {
		return reEmpNo;
	}
	public void setReEmpNo(String reEmpNo) {
		this.reEmpNo = reEmpNo;
	}
	public String getUserBizNm() {
		return userBizNm;
	}
	public void setUserBizNm(String userBizNm) {
		this.userBizNm = userBizNm;
	}
	public String getcType() {
		return cType;
	}
	public void setcType(String cType) {
		this.cType = cType;
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
	public String getUpdtEmpNo() {
		return updtEmpNo;
	}
	public void setUpdtEmpNo(String updtEmpNo) {
		this.updtEmpNo = updtEmpNo;
	}
	public String getUpdtDate() {
		return updtDate;
	}
	public void setUpdtDate(String updtDate) {
		this.updtDate = updtDate;
	}
	public String getCollaborCode() {
		return collaborCode;
	}
	public void setCollaborCode(String collaborCode) {
		this.collaborCode = collaborCode;
	}
	public String getKtPosnNm() {
		return ktPosnNm;
	}
	public void setKtPosnNm(String ktPosnNm) {
		this.ktPosnNm = ktPosnNm;
	}
	public String getRePosnNm() {
		return rePosnNm;
	}
	public void setRePosnNm(String rePosnNm) {
		this.rePosnNm = rePosnNm;
	}
	public String getInfoList() {
		return infoList;
	}
	public void setInfoList(String infoList) {
		this.infoList = infoList;
	}
	
}
