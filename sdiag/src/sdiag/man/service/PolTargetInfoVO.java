package sdiag.man.service;
import java.io.Serializable;

public class PolTargetInfoVO implements Serializable{

	/**
	 * 정책 대상 VO
	 */
	private static final long serialVersionUID = 2488929481026705735L;
	
	// 보안 정책 ID
	private String sec_pol_id;
	// 대상 타입
	private String targetType;
	// 대상 코드
	private String targetCode;
	// 대상 이름
	private String targetNm;
	// 그룹 타입
	private String groupType;
	// 등록일
	private String rgdtDate;
	
	private String targetTypeArr;
	private String targetCodeArr;
	private String targetNmArr;
	
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
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
	public String getTargetNm() {
		return targetNm;
	}
	public void setTargetNm(String targetNm) {
		this.targetNm = targetNm;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getRgdtDate() {
		return rgdtDate;
	}
	public void setRgdtDate(String rgdtDate) {
		this.rgdtDate = rgdtDate;
	}
	public String getTargetTypeArr() {
		return targetTypeArr;
	}
	public void setTargetTypeArr(String targetTypeArr) {
		this.targetTypeArr = targetTypeArr;
	}
	public String getTargetCodeArr() {
		return targetCodeArr;
	}
	public void setTargetCodeArr(String targetCodeArr) {
		this.targetCodeArr = targetCodeArr;
	}
	public String getTargetNmArr() {
		return targetNmArr;
	}
	public void setTargetNmArr(String targetNmArr) {
		this.targetNmArr = targetNmArr;
	}
	
}
