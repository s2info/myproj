package sdiag.exception.service;

import java.util.List;

import sdiag.man.vo.MailPolicyInfoVO;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class ReqExIpInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 등로자 사번
	private String rgdtEmpNo;
	// 신청자명
	private String rgdtEmpNm;
	// 예외 정보
	private String exInfo;
	// 예외 신청 사유              
	private String reason;  
	// 등록 일자             
	private String rgdtDate;
	// 수정자 사번            
	private String updtEmpNO;
	// 수정자 명           
	private String updtEmpNm;
	// 수정일
	private String updtDate;
	// 신청 사번
    private long exSeq=0;
    private String exStartDate;
    
    private String exEndDate="";
	
    private String exDays;
    
    private String exSeqList = "";
    
    private String polSelectList = "";
    
    private List<ReqExIpPolicyInfoVO> reqPolicyList = null;
    
    private String secPolId = "";
    
    private int intExDays =0 ;
    
    private String gubun = "";
    
    private String  subject = "";
    
    private String formMod ="";
    
    private String exAction = "";

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

	public String getExInfo() {
		return exInfo;
	}

	public void setExInfo(String exInfo) {
		this.exInfo = exInfo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRgdtDate() {
		return rgdtDate;
	}

	public void setRgdtDate(String rgdtDate) {
		this.rgdtDate = rgdtDate;
	}

	public String getUpdtEmpNO() {
		return updtEmpNO;
	}

	public void setUpdtEmpNO(String updtEmpNO) {
		this.updtEmpNO = updtEmpNO;
	}

	public String getUpdtEmpNm() {
		return updtEmpNm;
	}

	public void setUpdtEmpNm(String updtEmpNm) {
		this.updtEmpNm = updtEmpNm;
	}

	public String getUpdtDate() {
		return updtDate;
	}

	public void setUpdtDate(String updtDate) {
		this.updtDate = updtDate;
	}

	public long getExSeq() {
		return exSeq;
	}

	public void setExSeq(long exSeq) {
		this.exSeq = exSeq;
	}

	public String getExStartDate() {
		return exStartDate;
	}

	public void setExStartDate(String exStartDate) {
		this.exStartDate = exStartDate;
	}

	public String getExEndDate() {
		return exEndDate;
	}

	public void setExEndDate(String exEndDate) {
		this.exEndDate = exEndDate;
	}

	public String getExDays() {
		return exDays;
	}

	public void setExDays(String exDays) {
		this.exDays = exDays;
	}

	public String getExSeqList() {
		return exSeqList;
	}

	public void setExSeqList(String exSeqList) {
		this.exSeqList = exSeqList;
	}

	public String getPolSelectList() {
		return polSelectList;
	}

	public void setPolSelectList(String polSelectList) {
		this.polSelectList = polSelectList;
	}

	public List<ReqExIpPolicyInfoVO> getReqPolicyList() {
		return reqPolicyList;
	}

	public void setReqPolicyList(List<ReqExIpPolicyInfoVO> reqPolicyList) {
		this.reqPolicyList = reqPolicyList;
	}

	public String getSecPolId() {
		return secPolId;
	}

	public void setSecPolId(String secPolId) {
		this.secPolId = secPolId;
	}

	public int getIntExDays() {
		return intExDays;
	}

	public void setIntExDays(int intExDays) {
		this.intExDays = intExDays;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFormMod() {
		return formMod;
	}
	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}

	public String getExAction() {
		return exAction;
	}

	public void setExAction(String exAction) {
		this.exAction = exAction;
	}
}
