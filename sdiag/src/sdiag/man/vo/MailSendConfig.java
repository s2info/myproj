package sdiag.man.vo;

import java.io.Serializable;

public class MailSendConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1826959863352807841L;
	private String send_date="";
	private String send_hour="";
	private String send_minutes="";
	private String send_day_type="";
	private String send_day_option="";
	private String send_day="";
	private String send_hour_reg="";
	private String send_minutes_reg="";
	private String send_hour_rep="";
	private String send_minutes_rep="";
	private String select_value="";
	private String select_text = "";
	
	public String getSelect_value() {
		return select_value;
	}
	public void setSelect_value(String select_value) {
		this.select_value = select_value;
	}
	public String getSelect_text() {
		return select_text;
	}
	public void setSelect_text(String select_text) {
		this.select_text = select_text;
	}
	public String getSend_hour_reg() {
		return send_hour_reg;
	}
	public void setSend_hour_reg(String send_hour_reg) {
		this.send_hour_reg = send_hour_reg;
	}
	public String getSend_minutes_reg() {
		return send_minutes_reg;
	}
	public void setSend_minutes_reg(String send_minutes_reg) {
		this.send_minutes_reg = send_minutes_reg;
	}
	public String getSend_hour_rep() {
		return send_hour_rep;
	}
	public void setSend_hour_rep(String send_hour_rep) {
		this.send_hour_rep = send_hour_rep;
	}
	public String getSend_minutes_rep() {
		return send_minutes_rep;
	}
	public void setSend_minutes_rep(String send_minutes_rep) {
		this.send_minutes_rep = send_minutes_rep;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getSend_hour() {
		return send_hour;
	}
	public void setSend_hour(String send_hour) {
		this.send_hour = send_hour;
	}
	public String getSend_minutes() {
		return send_minutes;
	}
	public void setSend_minutes(String send_minutes) {
		this.send_minutes = send_minutes;
	}
	public String getSend_day_type() {
		return send_day_type;
	}
	public void setSend_day_type(String send_day_type) {
		this.send_day_type = send_day_type;
	}
	public String getSend_day_option() {
		return send_day_option;
	}
	public void setSend_day_option(String send_day_option) {
		this.send_day_option = send_day_option;
	}
	public String getSend_day() {
		return send_day;
	}
	public void setSend_day(String send_day) {
		this.send_day = send_day;
	}
	
}
