package sdiag.board.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class NoticeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8230258064637277188L;

	int sq_no = 0;
	String n_type;
	String contents;;
	String title;
	int read_cnt=0;
	String status_cd;
	String upd_user;
	String upd_date;
	String is_popup="N";
	private String is_new="N";
	private int read_count=0;
	
	public String getIs_new() {
		return is_new;
	}
	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}
	public int getRead_count() {
		return read_count;
	}
	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}
	public int getSq_no() {
		return sq_no;
	}
	public void setSq_no(int sq_no) {
		this.sq_no = sq_no;
	}
	public String getN_type() {
		return n_type;
	}
	public void setN_type(String n_type) {
		this.n_type = n_type;
	}
	
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getRead_cnt() {
		return read_cnt;
	}
	public void setRead_cnt(int read_cnt) {
		this.read_cnt = read_cnt;
	}
	public String getStatus_cd() {
		return status_cd;
	}
	public void setStatus_cd(String status_cd) {
		this.status_cd = status_cd;
	}
	public String getUpd_user() {
		return upd_user;
	}
	public void setUpd_user(String upd_user) {
		this.upd_user = upd_user;
	}
	public String getUpd_date() {
		return upd_date;
	}
	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}
	public String getIs_popup() {
		return is_popup;
	}
	public void setIs_popup(String is_popup) {
		this.is_popup = is_popup;
	}
	
	
}
