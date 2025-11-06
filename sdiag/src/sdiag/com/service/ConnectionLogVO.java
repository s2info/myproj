package sdiag.com.service;

import java.io.Serializable;

public class ConnectionLogVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8863456660910297826L;
	
	private String login_time = "";
	private String emp_no = "";
	private String ip = "";
	private String mac = "";
	private String ui_id = "";
	private String logout_time = "";
	private String rgdt_date = "";
	
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getUi_id() {
		return ui_id;
	}
	public void setUi_id(String ui_id) {
		this.ui_id = ui_id;
	}
	public String getLogout_time() {
		return logout_time;
	}
	public void setLogout_time(String logout_time) {
		this.logout_time = logout_time;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	
}
