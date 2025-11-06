package sdiag.man.vo;

import java.io.Serializable;
import java.util.List;

public class StatInfoVO implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 3677100097378374029L;
	
	/** 통계 순번 */    
	private long statSeq=0;
	
	/** 통계 순번 */    
	private String statSeqList="";
	/** 제목 */
	private String subject="";
	/** 둥록자 사번 */
	private String rgdtEmpNo="";
	/** 등록일 */
	private String rgdtDate="";
	/** 수정자 사번 */
	private String updtEmpNo="";
	/** 수정자 사번 */
	private String updtEmpNm="";
	/** 수정일 */
	private String updtDate="";
	/** 정책 ID */
	private String polIdxId="";
	/** 정책 명 */
	private String polIdxNm="";
	/** 지수화일 */
	private String sumRgdtDate="";
	/** 가중치 */
	private int polWeightValue=100;
	
	
	private String polIdxList = "";
	private String upPolList = "";
	
	private List<StatInfoVO> statPolList = null;
	private List<StatInfoVO> statUpPolList = null;

	private String gubun="";
	private String formMod="";
	
	private String excelFile="";
	
	private long polCnt = 0 ;
	
	private String msg = "";
	
	private int orgLevel =3;
	
	public long getStatSeq() {
		return statSeq;
	}
	public void setStatSeq(long statSeq) {
		this.statSeq = statSeq;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public String getPolIdxId() {
		return polIdxId;
	}
	public void setPolIdxId(String polIdxId) {
		this.polIdxId = polIdxId;
	}
	public String getPolIdxNm() {
		return polIdxNm;
	}
	public void setPolIdxNm(String polIdxNm) {
		this.polIdxNm = polIdxNm;
	}
	public String getSumRgdtDate() {
		return sumRgdtDate;
	}
	public void setSumRgdtDate(String sumRgdtDate) {
		this.sumRgdtDate = sumRgdtDate;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getFormMod() {
		return formMod;
	}
	public void setFormMod(String formMod) {
		this.formMod = formMod;
	}
	public String getPolIdxList() {
		return polIdxList;
	}
	public void setPolIdxList(String polIdxList) {
		this.polIdxList = polIdxList;
	}
	public String getUpPolList() {
		return upPolList;
	}
	public void setUpPolList(String upPolList) {
		this.upPolList = upPolList;
	}
	public List<StatInfoVO> getStatPolList() {
		return statPolList;
	}
	public void setStatPolList(List<StatInfoVO> statPolList) {
		this.statPolList = statPolList;
	}
	public List<StatInfoVO> getStatUpPolList() {
		return statUpPolList;
	}
	public void setStatUpPolList(List<StatInfoVO> statUpPolList) {
		this.statUpPolList = statUpPolList;
	}
	public String getExcelFile() {
		return excelFile;
	}
	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}
	public long getPolCnt() {
		return polCnt;
	}
	public void setPolCnt(long polCnt) {
		this.polCnt = polCnt;
	}
	public String getStatSeqList() {
		return statSeqList;
	}
	public void setStatSeqList(String statSeqList) {
		this.statSeqList = statSeqList;
	}
	public int getPolWeightValue() {
		return polWeightValue;
	}
	public void setPolWeightValue(int polWeightValue) {
		this.polWeightValue = polWeightValue;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(int orgLevel) {
		this.orgLevel = orgLevel;
	}
	
}
