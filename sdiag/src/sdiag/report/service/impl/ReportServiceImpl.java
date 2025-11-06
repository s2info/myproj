package sdiag.report.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.InputStream;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.report.service.OrgResultInfoVO;
import sdiag.report.service.ReportSearchResultVO;
import sdiag.report.service.ReportSearchVO;
import sdiag.report.service.ReportService;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {
	
	@Resource(name = "ReportDAO")
	private ReportDAO reportDAO;	
	
	@Override
	public List<EgovMap> getPersonalPolcount(DashboardSearchVO searchVO)
			throws Exception {
		return reportDAO.getPersonalPolcount(searchVO);
	}	
	
	public List getOrgPolItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception {
		if(searchVO.getBuseoType().equals("Y")){
			return reportDAO.getOrgPolItemCountScoreStatusBuseo(searchVO);
		}else{
			return reportDAO.getOrgPolItemCountScoreStatus(searchVO);
		}
		
	}
	
	/**
	 * 진단 보고서 정책별 건수 조회를 위한 추가쿼리
	 * @param searchVO
	 * @return
	 */
	public List getOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO) throws Exception{
		return reportDAO.getOrgPolItemCountScoreStatusFroPolicyList(searchVO);
	}
	/**
	 * 데이터 추출 보고서 조직별, 정책별 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO) throws Exception{
		return reportDAO.getDataReportOrgPolItemCountScoreStatusFroPolicyList(searchVO);
	}
	/**
	 * 데이트 추출 정책 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO) throws Exception{
		return reportDAO.getDataReportItemCountScoreStatusFroDistinctPolicyList(searchVO);
	}
	/**
	 * 진단 보고서 팀별 정책별 건수 조회
	 * @param searchVO
	 * @return
	 */
	public List getTeamPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO){
		return reportDAO.getTeamPolItemCountScoreStatusFroPolicyList(searchVO);
	}
	public List getOrgPolItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO) throws Exception{
		return reportDAO.getOrgPolItemCountScoreStatusFroDistinctPolicyList(searchVO);
	}
	
	@Override
	public List<EgovMap> getOrgDiagItemCountScoreStatus(DashboardSearchVO searchVO) {
		if(searchVO.getBuseoType().equals("Y")){
			return reportDAO.getOrgDiagItemCountScoreStatusBuseo(searchVO);
		}else{
			return reportDAO.getOrgDiagItemCountScoreStatus(searchVO);
		}
		
	}

	@Override
	public List<EgovMap> getOrgPolTotcountAvg(DashboardSearchVO searchVO) throws Exception {
		if(searchVO.getBuseoType().equals("Y")){
			return reportDAO.getOrgPolTotcountAvgBuseo(searchVO);
		}else{
			return reportDAO.getOrgPolTotcountAvg(searchVO);
		}
		
	}

	@Override
	public List<EgovMap> getPerCountScoreStatus(DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getPerCountScoreStatus(searchVO);
	}

	@Override
	public List<EgovMap> getPerPolItemCountScoreStatus(DashboardSearchVO searchVO)
			throws Exception {
		return reportDAO.getPerPolItemCountScoreStatus(searchVO);
	}

	@Override
	public String getOrgSelectName(DashboardSearchVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return (String)reportDAO.getOrgSelectName(searchVO);
	}

	@Override
	public List<HashMap<String, Object>> getOrgPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getOrgPolItemCountScoreStatusExportExcel(searchVO);
	}

	@Override
	public List<HashMap<String, Object>> getOrgDiagItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getOrgDiagItemCountScoreStatusExportExcel(searchVO);
	}

	@Override
	public List<HashMap<String, Object>> getPerCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getPerCountScoreStatusExportExcel(searchVO);
	}

	@Override
	public List<HashMap<String, Object>> getPerPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getPerPolItemCountScoreStatusExportExcel(searchVO);
	}

	/************************* 월간보고서 ***************************************/
	
	/**
	 * 조직정보 조회
	 * @param searchVO
	 * @return
	 */
	public ReportSearchVO getMonthOrgInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthOrgInfo(searchVO);
	}
	/**
	 * 조회 날짜 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchVO> getSearchDateList(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getSearchDateList(searchVO);
	}
	/**
	 * 하위조직 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportOrgListInfo(searchVO);
	}
	/**
	 * 하위조직 User 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportOrgUserListInfo(searchVO);
	}
	/**
	 * 하위조직 리스트 data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListDataInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportOrgListDataInfo(searchVO);
	}
	/**
	 * 하위조직 User 리스트 Data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListDataInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportOrgUserListDataInfo(searchVO);
	}
	/**
	 * 개인 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportUserTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportUserTotalScoreInfo(searchVO);
	}
	/**
	 * 조직 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportOrgTotalScoreInfo(searchVO);
	}
	/**
	 * 하위조직 미존재  전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportSubOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getMonthReportSubOrgTotalScoreInfo(searchVO);
	}
	/************************* 월간 보고서 END **********************************/

	/**
	 * 조직 평균 점수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getOrgPolAvg(DashboardSearchVO searchVO) throws Exception {
		return reportDAO.getOrgPolAvg(searchVO);
	}

	/**
	 * 정책 리스트 조회
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getDiagItemList() throws Exception {
		
		return reportDAO.getDiagItemList();
	}
	/**
	 * 보고서 데이터 추출 정책 기준 조회
	 * @param serchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForOrgPolicyList(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getDataReportForOrgPolicyList(searchVO);
	}
	/**
	 *  보고서 데이터 추출 정책 기준 사원별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForUserPolicyList(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getDataReportForUserPolicyList(searchVO);
	}
	/**
	 *  보고서 데이터 추출 사원 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportOrgUserList(ReportSearchVO searchVO) throws Exception{
		return reportDAO.getDataReportOrgUserList(searchVO);
	}
}
