package sdiag.com.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CodeInfoVO implements Serializable{

	/**
	 * 코드정보 
	 */
	private static final long serialVersionUID = -4937191924807182793L;
	
	private String majr_code = "";
	private String minr_code = "";
	private String code_desc = "";
 	private String use_indc = "";
 	private String rgdt_date = "";
 	private String add_info1 = "";
 	
	public String getMajr_code() {
		return majr_code;
	}
	public void setMajr_code(String majr_code) {
		this.majr_code = majr_code;
	}
	public String getMinr_code() {
		return minr_code;
	}
	public String getAdd_info1() {
		return add_info1;
	}
	public void setAdd_info1(String add_info1) {
		this.add_info1 = add_info1;
	}
	public void setMinr_code(String minr_code) {
		this.minr_code = minr_code;
	}
	public String getCode_desc() {
		return code_desc;
	}
	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
	}
	public String getUse_indc() {
		return use_indc;
	}
	public void setUse_indc(String use_indc) {
		this.use_indc = use_indc;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
 	
 	

}
