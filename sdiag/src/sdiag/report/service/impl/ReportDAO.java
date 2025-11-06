package sdiag.report.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.dash.service.DashboardSearchVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.SearchVO;
import sdiag.report.service.OrgResultInfoVO;
import sdiag.report.service.ReportSearchResultVO;
import sdiag.report.service.ReportSearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("ReportDAO")
public class ReportDAO extends EgovAbstractDAO{

	public List getPersonalPolcount(DashboardSearchVO searchVO) {
		return list("ReportDAO.getPersonalPolcount", searchVO);
	}	
	
	public List getOrgPolItemCountScoreStatus(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgPolItemCountScoreStatus",searchVO);
	}
	/**
	 * 진단 보고서 정책별 건수 조회를 위한 추가쿼리
	 * @param searchVO
	 * @return
	 */
	public List getOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO){
		return list("ReportDAO.getOrgPolItemCountScoreStatusFroPolicyList", searchVO);
	}
	/**
	 * 진단 보고서 팀별 정책별 건수 조회
	 * @param searchVO
	 * @return
	 */
	public List getTeamPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO){
		return list("ReportDAO.getTeamPolItemCountScoreStatusFroPolicyList", searchVO);
	}
	public List getOrgPolItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO){
		return list("ReportDAO.getOrgPolItemCountScoreStatusFroDistinctPolicyList", searchVO);
	}
	/**
	 * 데이터 추출 보고서 조직별, 정책별 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO){
		return list("ReportDAO.getDataReportOrgPolItemCountScoreStatusFroPolicyList", searchVO);
	}
	/**
	 * 데이트 추출 정책 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO){
		return list("ReportDAO.getDataReportItemCountScoreStatusFroDistinctPolicyList", searchVO);
	}
	public List getOrgPolItemCountScoreStatusBuseo(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgPolItemCountScoreStatusBuseo",searchVO);
	}

	public List getOrgDiagItemCountScoreStatus(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgDiagItemCountScoreStatus",searchVO);
	}
	public List getOrgDiagItemCountScoreStatusBuseo(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgDiagItemCountScoreStatusBuseo",searchVO);
	}

	/**
	 * 
	 * @param searchVO
	 * @return
	 */
	public List getOrgPolTotcountAvg(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgPolTotcountAvg",searchVO);
	}
	public List getOrgPolTotcountAvgBuseo(DashboardSearchVO searchVO) {
		return list("ReportDAO.getOrgPolTotcountAvgBuseo",searchVO);
	}

	public List getPerCountScoreStatus(DashboardSearchVO searchVO) {
		return list("ReportDAO.getPerCountScoreStatus",searchVO);
	}

	public List getPerPolItemCountScoreStatus(DashboardSearchVO searchVO) {
		return list("ReportDAO.getPerPolItemCountScoreStatus",searchVO);
	}

	public String getOrgSelectName(DashboardSearchVO searchVO) {
		// TODO Auto-generated method stub
		return (String) select("ReportDAO.getOrgSelectName",searchVO);
	}

	public List<HashMap<String, Object>> getOrgPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) {
		return (List<HashMap<String, Object>>)list("ReportDAO.getOrgPolItemCountScoreStatusExportExcel", searchVO);
	}

	public List<HashMap<String, Object>> getOrgDiagItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) {
		// TODO Auto-generated method stub
		return (List<HashMap<String, Object>>)list("ReportDAO.getOrgDiagItemCountScoreStatusExportExcel", searchVO);
	}

	public List<HashMap<String, Object>> getPerCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) {
		return (List<HashMap<String, Object>>)list("ReportDAO.getPerCountScoreStatusExportExcel", searchVO);
	}

	public List<HashMap<String, Object>> getPerPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) {
		return (List<HashMap<String, Object>>)list("ReportDAO.getPerPolItemCountScoreStatusExportExcel", searchVO);
	}

	/************************** 월간 보고서 **************************************/
	/**
	 * 조직정보 조회
	 * @param searchVO
	 * @return
	 */
	public ReportSearchVO getMonthOrgInfo(ReportSearchVO searchVO) throws Exception{
		return (ReportSearchVO)select("report.month.getOrgInfo", searchVO);
	}
	/**
	 * 조회 날짜 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchVO> getSearchDateList(ReportSearchVO searchVO) throws Exception{
		return (List<ReportSearchVO>)list("report.month.getSearchDateList", searchVO);
	}
	/**
	 * 하위조직 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListInfo(ReportSearchVO searchVO) throws Exception{
		return (List<OrgResultInfoVO>)list("report.month.getOrgListInfo", searchVO);
	}
	/**
	 * 하위조직 User 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListInfo(ReportSearchVO searchVO) throws Exception{
		return (List<OrgResultInfoVO>)list("report.month.getOrgUserListInfo", searchVO);
	}
	
	/**
	 * 하위조직 리스트 data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListDataInfo(ReportSearchVO searchVO) throws Exception{
		return (List<OrgResultInfoVO>)list("report.month.getOrgListDataInfo", searchVO);
	}
	/**
	 * 하위조직 User 리스트 Data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListDataInfo(ReportSearchVO searchVO) throws Exception{
		return (List<OrgResultInfoVO>)list("report.month.getOrgUserListDataInfo", searchVO);
	}
	/**
	 * 개인 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportUserTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return (OrgResultInfoVO)select("report.month.getUserTotalScoreInfo", searchVO);
	}
	/**
	 * 조직 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return (OrgResultInfoVO)select("report.month.getOrgTotalScoreInfo", searchVO);
	}
	/**
	 * 하위조직 미존재  전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportSubOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return (OrgResultInfoVO)select("report.month.getSubOrgTotalScoreInfo", searchVO);
	}
	/************************** 월간 보고서 END **************************************/

	/**
	 * 조직 평균 점수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getOrgPolAvg(DashboardSearchVO searchVO) throws Exception{
		int totval = 0;
		try{
			totval = (int)select("report.getOrgPolAvg", searchVO);
		}catch(Exception e){
			 totval = -999;
		}
		
		return totval;
	}
	/**
	 * 정책 리스트 조회 -
	 * @return
	 * @throws Exception
	 */
	public List<LinkedHashMap<String, Object>> getDiagItemList() throws Exception{
		return (List<LinkedHashMap<String, Object>>)list("report.getDiagItemList");
	}
	/**
	 * 보고서 데이터 추출 정책 기준 조직별 점수 조회
	 * @param serchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForOrgPolicyList(ReportSearchVO searchVO) throws Exception{
		return (List<ReportSearchResultVO>)list("report.getDataReportForOrgPolicyList", searchVO);
	}
	/**
	 *  보고서 데이터 추출 정책 기준 사원별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForUserPolicyList(ReportSearchVO searchVO) throws Exception{
		return (List<ReportSearchResultVO>)list("report.getDataReportForUserPolicyList", searchVO);
	}
	/**
	 *  보고서 데이터 추출 사원 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportOrgUserList(ReportSearchVO searchVO) throws Exception{
		return (List<ReportSearchResultVO>)list("report.getDataReportOrgUserList", searchVO);
	}
}
