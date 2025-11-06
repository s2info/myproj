package sdiag.com.service;
import java.io.Serializable;
public class FileVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 620811090234456482L;
	
	//idx_rgdt_date, emp_no, mac, pol_idx_id
	//evid_file_name, evid_file_loc
	private String idx_rgdt_date = "";
	private String emp_no = "";
	private String mac="";
	private String pol_idx_id="";
	private String evid_file_name = ""; //실제파일명
	private String evid_file_loc= ""; //저장 파일명
	private int evid_file_size=0;
	
	
	public int getEvid_file_size() {
		return evid_file_size;
	}
	public void setEvid_file_size(int evid_file_size) {
		this.evid_file_size = evid_file_size;
	}
	public String getIdx_rgdt_date() {
		return idx_rgdt_date;
	}
	public void setIdx_rgdt_date(String idx_rgdt_date) {
		this.idx_rgdt_date = idx_rgdt_date;
	}
	public String getEmp_no() {
		return emp_no;
	}
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPol_idx_id() {
		return pol_idx_id;
	}
	public void setPol_idx_id(String pol_idx_id) {
		this.pol_idx_id = pol_idx_id;
	}
	public String getEvid_file_name() {
		return evid_file_name;
	}
	public void setEvid_file_name(String evid_file_name) {
		this.evid_file_name = evid_file_name;
	}
	public String getEvid_file_loc() {
		return evid_file_loc;
	}
	public void setEvid_file_loc(String evid_file_loc) {
		this.evid_file_loc = evid_file_loc;
	}

	
}
