package sdiag.man.vo;

import java.io.Serializable;

public class MailPolicyInfoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868237610468130822L;
	private long mail_seq=0;
	private String sec_pol_id = "";
	private String majcode="";
	private String majdesc="";
	private String mincode="";
	private String mindesc="";
	private String policyname="";
	private String is_selected="N";
	
	public String getMajcode() {
		return majcode;
	}
	public void setMajcode(String majcode) {
		this.majcode = majcode;
	}
	public String getMajdesc() {
		return majdesc;
	}
	public void setMajdesc(String majdesc) {
		this.majdesc = majdesc;
	}
	public String getMincode() {
		return mincode;
	}
	public void setMincode(String mincode) {
		this.mincode = mincode;
	}
	public String getMindesc() {
		return mindesc;
	}
	public void setMindesc(String mindesc) {
		this.mindesc = mindesc;
	}
	public String getPolicyname() {
		return policyname;
	}
	public void setPolicyname(String policyname) {
		this.policyname = policyname;
	}
	public String getIs_selected() {
		return is_selected;
	}
	public void setIs_selected(String is_selected) {
		this.is_selected = is_selected;
	}
	private String rgdt_date = "";
	public long getMail_seq() {
		return mail_seq;
	}
	public void setMail_seq(long mail_seq) {
		this.mail_seq = mail_seq;
	}
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	
}
