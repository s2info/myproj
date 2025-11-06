package sdiag.man.vo;

import sdiag.man.service.SearchVO;

public class MailSearchVO extends SearchVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3667911097596170719L;
	private String search_gubun = "";
	private String search_is_used = "";
	private String search_subject="";
	private String org_type="";
	private String target_type="";
	private String op_indc="";
	private long mail_seq=0;
	
	public String getSearch_gubun() {
		return search_gubun;
	}
	public void setSearch_gubun(String search_gubun) {
		this.search_gubun = search_gubun;
	}
	public String getSearch_is_used() {
		return search_is_used;
	}
	public void setSearch_is_used(String search_is_used) {
		this.search_is_used = search_is_used;
	}
	public String getSearch_subject() {
		return search_subject;
	}
	public void setSearch_subject(String search_subject) {
		this.search_subject = search_subject;
	}
	
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
	public String getTarget_type() {
		return target_type;
	}
	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
	public String getOp_indc() {
		return op_indc;
	}
	public void setOp_indc(String op_indc) {
		this.op_indc = op_indc;
	}
	public long getMail_seq() {
		return mail_seq;
	}
	public void setMail_seq(long mail_seq) {
		this.mail_seq = mail_seq;
	}
	
}
