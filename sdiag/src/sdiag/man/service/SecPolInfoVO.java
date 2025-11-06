package sdiag.man.service;
import java.io.Serializable;

public class SecPolInfoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2488929481026705735L;
	
	private String sec_pol_id="";
	private String sec_sol_id="";
	private String idx_indc="";
	private String majr_desc="";
	private String minr_desc="";
	private String sec_pol_desc="";
	private String sec_pol_cat=""; //정책구분 (기간 /건수)
	private String sec_pol_cat_desc = "";
	private int pol_critical_vlaue=0;
	private int pol_weight_value=0;
	private String use_indc="";
	private String appr_line_code="";
	private String appr_cate="";
	private String sanc_cate="";
	
    private String pol_critical_value_type=""; //임계치 구분(월/일/기간)
    private String pol_critical_value_data_type="";	//임계치데이터 타입(카운트/O,X, 날짜)
	private String diag_majr_code="";
	private String diag_minr_code="";
	private String appr_attach_indc="";
	private String sec_pol_sql="";
	private String sec_pol_patn_con="";
	private String sec_pol_table="";
	private String sec_pol_table_col="";
	private String appr_note="";
	private String rgdt_date="";
	private String ispcgact="";
	private String exe_para="";
	private boolean sanc_indc=false;
	private boolean pcg_indc=false;
	private String cond_field1="";
	private String sec_pol_notice="";
	private String buseo_indc="";
	private String gajeom_indc="";
	private String sec_pol_notice_detail="";
	private String reason="";
	// 순서 2017.04.27
	private String ordr = "";
	// 활성화 시작일 2018.09.20
	private String startDay="";
	
	// 활성화 종료일 2018.09.20
	private String endDay="";
		
	private String collabor_indc="N";
	
	private String bigo="";
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSec_pol_notice_detail() {
		return sec_pol_notice_detail;
	}
	public void setSec_pol_notice_detail(String sec_pol_notice_detail) {
		this.sec_pol_notice_detail = sec_pol_notice_detail;
	}
	public String getBuseo_indc() {
		return buseo_indc;
	}
	public void setBuseo_indc(String buseo_indc) {
		this.buseo_indc = buseo_indc;
	}
	public String getGajeom_indc() {
		return gajeom_indc;
	}
	public void setGajeom_indc(String gajeom_indc) {
		this.gajeom_indc = gajeom_indc;
	}
	public String getSec_pol_notice() {
		return sec_pol_notice;
	}
	public void setSec_pol_notice(String sec_pol_notice) {
		this.sec_pol_notice = sec_pol_notice;
	}
	public String getSec_pol_cat_desc() {
		return sec_pol_cat_desc;
	}
	public void setSec_pol_cat_desc(String sec_pol_cat_desc) {
		this.sec_pol_cat_desc = sec_pol_cat_desc;
	}
	public boolean isSanc_indc() {
		return sanc_indc;
	}
	public void setSanc_indc(boolean sanc_indc) {
		this.sanc_indc = sanc_indc;
	}
	public boolean isPcg_indc() {
		return pcg_indc;
	}
	public void setPcg_indc(boolean pcg_indc) {
		this.pcg_indc = pcg_indc;
	}
	public String getCond_field1() {
		return cond_field1;
	}
	public void setCond_field1(String cond_field1) {
		this.cond_field1 = cond_field1;
	}
	
	public String getExe_para() {
		return exe_para;
	}
	public void setExe_para(String exe_para) {
		this.exe_para = exe_para;
	}
	
	public String getIspcgact() {
		return ispcgact;
	}
	public void setIspcgact(String ispcgact) {
		this.ispcgact = ispcgact;
	}
	public String getPol_critical_value_type() {
		return pol_critical_value_type;
	}
	public void setPol_critical_value_type(String pol_critical_value_type) {
		this.pol_critical_value_type = pol_critical_value_type;
	}
	public String getPol_critical_value_data_type() {
		return pol_critical_value_data_type;
	}
	public void setPol_critical_value_data_type(String pol_critical_value_data_type) {
		this.pol_critical_value_data_type = pol_critical_value_data_type;
	}
	public String getDiag_majr_code() {
		return diag_majr_code;
	}
	public void setDiag_majr_code(String diag_majr_code) {
		this.diag_majr_code = diag_majr_code;
	}
	public String getDiag_minr_code() {
		return diag_minr_code;
	}
	public void setDiag_minr_code(String diag_minr_code) {
		this.diag_minr_code = diag_minr_code;
	}
	public String getAppr_attach_indc() {
		return appr_attach_indc;
	}
	public void setAppr_attach_indc(String appr_attach_indc) {
		this.appr_attach_indc = appr_attach_indc;
	}
	public String getSec_pol_sql() {
		return sec_pol_sql;
	}
	public void setSec_pol_sql(String sec_pol_sql) {
		this.sec_pol_sql = sec_pol_sql;
	}
	public String getSec_pol_patn_con() {
		return sec_pol_patn_con;
	}
	public void setSec_pol_patn_con(String sec_pol_patn_con) {
		this.sec_pol_patn_con = sec_pol_patn_con;
	}
	public String getSec_pol_table() {
		return sec_pol_table;
	}
	public void setSec_pol_table(String sec_pol_table) {
		this.sec_pol_table = sec_pol_table;
	}
	public String getSec_pol_table_col() {
		return sec_pol_table_col;
	}
	public void setSec_pol_table_col(String sec_pol_table_col) {
		this.sec_pol_table_col = sec_pol_table_col;
	}
	public String getAppr_note() {
		return appr_note;
	}
	public void setAppr_note(String appr_note) {
		this.appr_note = appr_note;
	}
	public String getRgdt_date() {
		return rgdt_date;
	}
	public void setRgdt_date(String rgdt_date) {
		this.rgdt_date = rgdt_date;
	}
	public String getSec_pol_id() {
		return sec_pol_id;
	}
	public void setSec_pol_id(String sec_pol_id) {
		this.sec_pol_id = sec_pol_id;
	}
	public String getSec_sol_id() {
		return sec_sol_id;
	}
	public void setSec_sol_id(String sec_sol_id) {
		this.sec_sol_id = sec_sol_id;
	}
	public String getIdx_indc() {
		return idx_indc;
	}
	public void setIdx_indc(String idx_indc) {
		this.idx_indc = idx_indc;
	}
	public String getMajr_desc() {
		return majr_desc;
	}
	public void setMajr_desc(String majr_desc) {
		this.majr_desc = majr_desc;
	}
	public String getMinr_desc() {
		return minr_desc;
	}
	public void setMinr_desc(String minr_desc) {
		this.minr_desc = minr_desc;
	}
	public String getSec_pol_desc() {
		return sec_pol_desc;
	}
	public void setSec_pol_desc(String sec_pol_desc) {
		this.sec_pol_desc = sec_pol_desc;
	}
	public String getSec_pol_cat() {
		return sec_pol_cat;
	}
	public void setSec_pol_cat(String sec_pol_cat) {
		this.sec_pol_cat = sec_pol_cat;
	}
	public int getPol_critical_vlaue() {
		return pol_critical_vlaue;
	}
	public void setPol_critical_vlaue(int pol_critical_vlaue) {
		this.pol_critical_vlaue = pol_critical_vlaue;
	}
	public String getOrdr() {
		return ordr;
	}
	public void setOrdr(String ordr) {
		this.ordr = ordr;
	}
	public int getPol_weight_value() {
		return pol_weight_value;
	}
	public void setPol_weight_value(int pol_weight_value) {
		this.pol_weight_value = pol_weight_value;
	}
	public String getUse_indc() {
		return use_indc;
	}
	public void setUse_indc(String use_indc) {
		this.use_indc = use_indc;
	}
	public String getAppr_line_code() {
		return appr_line_code;
	}
	public void setAppr_line_code(String appr_line_code) {
		this.appr_line_code = appr_line_code;
	}
	public String getAppr_cate() {
		return appr_cate;
	}
	public void setAppr_cate(String appr_cate) {
		this.appr_cate = appr_cate;
	}
	public String getSanc_cate() {
		return sanc_cate;
	}
	public void setSanc_cate(String sanc_cate) {
		this.sanc_cate = sanc_cate;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getCollabor_indc() {
		return collabor_indc;
	}
	public void setCollabor_indc(String collabor_indc) {
		this.collabor_indc = collabor_indc;
	}
	public String getBigo() {
		return bigo;
	}
	public void setBigo(String bigo) {
		this.bigo = bigo;
	}
	
	
}
