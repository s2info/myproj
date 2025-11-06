package sdiag.man.vo;

import java.io.Serializable;
import java.util.List;

public class MailSendInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1452601112033992331L;
	private long mail_seq=0;
	private String gubun="";
	private String gubun_name="";
	private String pol_type="06";
	private String pol_type_name="";
	private String send_schedule="";
	private String send_schedule_name="";
	private String subject="";
	private String contents="";
	private String contents_top="";
	private String contents_bottom="";
	private String mail_ask = "";
	private String is_used="";
	private String is_used_name="";
	private String upd_user="";
	private String rgdt_date="";
	private String upd_date="";
	private String send_type="";
	private String cronExpression="";
	private String schedule_expression="";
	private MailSendConfig sendConfig = null;
	private String polSelectList = "";
	private String targetCodeList = "";
	private String send_date = "";
	private String send_hour_reg = "";
	private String send_minutes_reg = "";
	private String send_day_type = "";
	private String send_day_option = "";
	private String send_day = "";
	private String send_hour_rep = "";
	private String send_minutes_rep = "";
	private List<MailTargetInfoVO> mailTargetList = null;
	private List<MailPolicyInfoVO> mailPolicyList = null;
	private List<MailSendConfig> dayOptionList = null;
	private long targetUserList=0;
	private String batchjobname="";
	private int sendcount=0;
	private int sendsuccesscount=0;
	private String is_apply="";
	private String is_apply_name="";
	private String is_cap_send="N";
	public List<MailTargetInfoVO> getMailTargetList() {
		return mailTargetList;
	}
	public void setMailTargetList(List<MailTargetInfoVO> mailTargetList) {
		this.mailTargetList = mailTargetList;
	}
	public List<MailPolicyInfoVO> getMailPolicyList() {
		return mailPolicyList;
	}
	public void setMailPolicyList(List<MailPolicyInfoVO> mailPolicyList) {
		this.mailPolicyList = mailPolicyList;
	}
	public String getPolSelectList() {
		return polSelectList;
	}
	public void setPolSelectList(String polSelectList) {
		this.polSelectList = polSelectList;
	}
	public String getTargetCodeList() {
		return targetCodeList;
	}
	public void setTargetCodeList(String targetCodeList) {
		this.targetCodeList = targetCodeList;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
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
	public String getGubun_name() {
		return gubun_name;
	}
	public void setGubun_name(String gubun_name) {
		this.gubun_name = gubun_name;
	}
	public String getSend_schedule_name() {
		return send_schedule_name;
	}
	public void setSend_schedule_name(String send_schedule_name) {
		this.send_schedule_name = send_schedule_name;
	}
	public long getMail_seq() {
		return mail_seq;
	}
	public void setMail_seq(long mail_seq) {
		this.mail_seq = mail_seq;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getPol_type() {
		return pol_type;
	}
	public void setPol_type(String pol_type) {
		this.pol_type = pol_type;
	}
	public String getSend_schedule() {
		return send_schedule;
	}
	public void setSend_schedule(String send_schedule) {
		this.send_schedule = send_schedule;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getContents_top() {
		return contents_top;
	}
	public void setContents_top(String contents_top) {
		this.contents_top = contents_top;
	}
	public String getContents_bottom() {
		return contents_bottom;
	}
	public void setContents_bottom(String contents_bottom) {
		this.contents_bottom = contents_bottom;
	}
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public String getUpd_user() {
		return upd_user;
	}
	public void setUpd_user(String upd_user) {
		this.upd_user = upd_user;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getUpd_date() {
		return upd_date;
	}
	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getSchedule_expression() {
		return schedule_expression;
	}
	public void setSchedule_expression(String schedule_expression) {
		this.schedule_expression = schedule_expression;
	}
	public MailSendConfig getSendConfig() {
		return sendConfig;
	}
	public void setSendConfig(MailSendConfig sendConfig) {
		this.sendConfig = sendConfig;
	}
	public List<MailSendConfig> getDayOptionList() {
		return dayOptionList;
	}
	public void setDayOptionList(List<MailSendConfig> dayOptionList) {
		this.dayOptionList = dayOptionList;
	}
	public String getIs_used_name() {
		return is_used_name;
	}
	public void setIs_used_name(String is_used_name) {
		this.is_used_name = is_used_name;
	}
	public long getTargetUserList() {
		return targetUserList;
	}
	public void setTargetUserList(long targetUserList) {
		this.targetUserList = targetUserList;
	}
	public String getMail_ask() {
		return mail_ask;
	}
	public void setMail_ask(String mail_ask) {
		this.mail_ask = mail_ask;
	}
	public String getPol_type_name() {
		return pol_type_name;
	}
	public void setPol_type_name(String pol_type_name) {
		this.pol_type_name = pol_type_name;
	}
	public String getBatchjobname() {
		return batchjobname;
	}
	public void setBatchjobname(String batchjobname) {
		this.batchjobname = batchjobname;
	}
	public int getSendcount() {
		return sendcount;
	}
	public void setSendcount(int sendcount) {
		this.sendcount = sendcount;
	}
	public int getSendsuccesscount() {
		return sendsuccesscount;
	}
	public void setSendsuccesscount(int sendsuccesscount) {
		this.sendsuccesscount = sendsuccesscount;
	}
	public String getIs_apply() {
		return is_apply;
	}
	public void setIs_apply(String is_apply) {
		this.is_apply = is_apply;
	}
	public String getIs_apply_name() {
		return is_apply_name;
	}
	public void setIs_apply_name(String is_apply_name) {
		this.is_apply_name = is_apply_name;
	}
	public String getIs_cap_send() {
		return is_cap_send;
	}
	public void setIs_cap_send(String is_cap_send) {
		this.is_cap_send = is_cap_send;
	}
	
	
}
