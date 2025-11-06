package sdiag.board.service;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class QnaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8230258064637277188L;

	int sq_no = 0;
	String n_type;
	String contents;
	String title;
	String upd_user;
	String upd_date;
	String answer_contents;
	String answer_user;
	String answer_date;
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
	public String getAnswer_contents() {
		return answer_contents;
	}
	public void setAnswer_contents(String answer_contents) {
		this.answer_contents = answer_contents;
	}
	public String getAnswer_user() {
		return answer_user;
	}
	public void setAnswer_user(String answer_user) {
		this.answer_user = answer_user;
	}
	public String getAnswer_date() {
		return answer_date;
	}
	public void setAnswer_date(String answer_date) {
		this.answer_date = answer_date;
	}

	
}
