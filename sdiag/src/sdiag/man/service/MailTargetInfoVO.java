package sdiag.man.service;

import java.io.Serializable;

public class MailTargetInfoVO implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 3677100097378374029L;
		private long mail_seq=0;
		private String target_type="";
		private String target_type_name="";
		private String target_code="";
		private String rgdt_date="";
		private String target_nm="";
		private String org_type="";
		private String targetTypeArr="";
		private String targetCodeArr="";
		private String targetNmArr="";
		private String emp_no="";
		private String emp_nm="";
		private String org_code="";
		private String org_nm="";
		private String email="";
		
		public String getEmp_no() {
			return emp_no;
		}
		public void setEmp_no(String emp_no) {
			this.emp_no = emp_no;
		}
		public String getEmp_nm() {
			return emp_nm;
		}
		public void setEmp_nm(String emp_nm) {
			this.emp_nm = emp_nm;
		}
		public String getOrg_code() {
			return org_code;
		}
		public void setOrg_code(String org_code) {
			this.org_code = org_code;
		}
		public String getOrg_nm() {
			return org_nm;
		}
		public void setOrg_nm(String org_nm) {
			this.org_nm = org_nm;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public long getMail_seq() {
			return mail_seq;
		}
		public void setMail_seq(long mail_seq) {
			this.mail_seq = mail_seq;
		}
		public String getTarget_type() {
			return target_type;
		}
		public void setTarget_type(String target_type) {
			this.target_type = target_type;
		}
		public String getTarget_code() {
			return target_code;
		}
		public void setTarget_code(String target_code) {
			this.target_code = target_code;
		}
		public String getRgdt_date() {
			return rgdt_date;
		}
		public void setRgdt_date(String rgdt_date) {
			this.rgdt_date = rgdt_date;
		}
		public String getTarget_nm() {
			return target_nm;
		}
		public void setTarget_nm(String target_nm) {
			this.target_nm = target_nm;
		}
		public String getOrg_type() {
			return org_type;
		}
		public void setOrg_type(String org_type) {
			this.org_type = org_type;
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
		public String getTarget_type_name() {
			return target_type_name;
		}
		public void setTarget_type_name(String target_type_name) {
			this.target_type_name = target_type_name;
		}
		
}
