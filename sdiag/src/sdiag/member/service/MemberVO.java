package sdiag.member.service;

import java.io.Serializable;

public class MemberVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3607623073672746884L;

	private String userid;
	private String password;
	private String username;
	private String email;
	private String telno;
	private String last_pwd_date;
	private String role_info;
	private String role_code;
	private String ip;
	private String titlecode;
	private String orgcode;
	private String titlename;
	private String levelcode;
	private String levelname;
	private String ismail;
	private String mac;
	private String isEmp;
	private String isProxy = "N";
	private String isProxyDirector = "N";
	
	public String getIsProxyDirector() {
		return isProxyDirector;
	}
	public void setIsProxyDirector(String isProxyDirector) {
		this.isProxyDirector = isProxyDirector;
	}
	public String getIsProxy() {
		return isProxy;
	}
	public void setIsProxy(String isProxy) {
		this.isProxy = isProxy;
	}
	public String getIsEmp() {
		return isEmp;
	}
	public void setIsEmp(String isEmp) {
		this.isEmp = isEmp;
	}
	public String getTitlename() {
		return titlename;
	}
	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}
	public String getLevelcode() {
		return levelcode;
	}
	public void setLevelcode(String levelcode) {
		this.levelcode = levelcode;
	}
	public String getLevelname() {
		return levelname;
	}
	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}
	public String getIsmail() {
		return ismail;
	}
	public void setIsmail(String ismail) {
		this.ismail = ismail;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getOrgcode() {
		return orgcode;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	public String getTitlecode() {
		return titlecode;
	}
	public void setTitlecode(String titlecode) {
		this.titlecode = titlecode;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
	}
	public String getLast_pwd_date() {
		return last_pwd_date;
	}
	public void setLast_pwd_date(String last_pwd_date) {
		this.last_pwd_date = last_pwd_date;
	}
	public String getRole_info() {
		return role_info;
	}
	public void setRole_info(String role_info) {
		this.role_info = role_info;
	}
	public String getRole_code() {
		return role_code;
	}
	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
