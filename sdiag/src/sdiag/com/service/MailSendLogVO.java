package sdiag.com.service;

import java.io.Serializable;
import java.util.List;

import sdiag.com.service.MimeBodyPartVO;

public class MailSendLogVO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 7006545502541242988L;

	private String emp_no="";
	private String to_email = "";
	private String subj_email="";
	private String body_email="";
	private String evid_file_name="";
	
	private List<MimeBodyPartVO> imageArray = null;
	
	private boolean result = false;
	private String result_message = "";
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getResult_message() {
		return result_message;
	}
	public void setResult_message(String result_message) {
		this.result_message = result_message;
	}
	
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getTo_email() {
		return to_email;
	}
	public void setTo_email(String to_email) {
		this.to_email = to_email;
	}
	public String getSubj_email() {
		return subj_email;
	}
	public void setSubj_email(String subj_email) {
		this.subj_email = subj_email;
	}
	public String getBody_email() {
		return body_email;
	}
	public void setBody_email(String body_email) {
		this.body_email = body_email;
	}
	public String getEvid_file_name() {
		return evid_file_name;
	}
	public void setEvid_file_name(String evid_file_name) {
		this.evid_file_name = evid_file_name;
	}
	public List<MimeBodyPartVO> getImageArray() {
		return imageArray;
	}
	public void setImageArray(List<MimeBodyPartVO> imageArray) {
		this.imageArray = imageArray;
	}
}
