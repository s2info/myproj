package sdiag.report.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import sdiag.dash.service.DashboardSearchVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ReportService {
	List<EgovMap> getPersonalPolcount(DashboardSearchVO searchVO) throws Exception;
	
	List<EgovMap> getOrgPolItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception;

	List<EgovMap> getOrgDiagItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception;

	List<EgovMap> getOrgPolTotcountAvg(DashboardSearchVO searchVO) throws Exception;

	List<EgovMap> getPerCountScoreStatus(DashboardSearchVO searchVO) throws Exception;

	List<EgovMap> getPerPolItemCountScoreStatus(DashboardSearchVO searchVO) throws Exception;

	String getOrgSelectName(DashboardSearchVO searchVO) throws Exception;

	List<HashMap<String, Object>> getOrgPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception;

	List<HashMap<String, Object>> getOrgDiagItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception;

	List<HashMap<String, Object>> getPerCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception;

	List<HashMap<String, Object>> getPerPolItemCountScoreStatusExportExcel(
			DashboardSearchVO searchVO) throws Exception;
	/**
	 * 진단 보고서 정책별 건수 조회를 위한 추가쿼리
	 * @param searchVO
	 * @return
	 */
	List<EgovMap> getOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 데이터 추출 보고서 조직별, 정책별 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportOrgPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 데이트 추출 정책 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List getDataReportItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 진단 보고서 팀별 정책별 건수 조회
	 * @param searchVO
	 * @return
	 */
	public List getTeamPolItemCountScoreStatusFroPolicyList(DashboardSearchVO searchVO);
	public List getOrgPolItemCountScoreStatusFroDistinctPolicyList(DashboardSearchVO searchVO) throws Exception;
	/********************************** 월간보고서 *******************************/
	/**
	 * 조직정보 조회
	 * @param searchVO
	 * @return
	 */
	public ReportSearchVO getMonthOrgInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 조회 날짜 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchVO> getSearchDateList(ReportSearchVO searchVO) throws Exception;
	/**
	 * 하위조직 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 하위조직 User 리스트 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 하위조직 리스트 data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgListDataInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 하위조직 User 리스트 Data 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<OrgResultInfoVO> getMonthReportOrgUserListDataInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 개인 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportUserTotalScoreInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 조직 전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception;
	/**
	 * 하위조직 미존재  전체 평균 및 건수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public OrgResultInfoVO getMonthReportSubOrgTotalScoreInfo(ReportSearchVO searchVO) throws Exception;

	
	
	/********************************** 월간 보고서 END ****************************/
	/**
	 * 조직 평균 점수
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getOrgPolAvg(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 정책 리스트 조회 -
	 * @return
	 * @throws Exception
	 */
	public List<LinkedHashMap<String, Object>> getDiagItemList() throws Exception;
	/**
	 * 보고서 데이터 추출 정책 기준 조회
	 * @param serchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForOrgPolicyList(ReportSearchVO searchVO) throws Exception;
	/**
	 *  보고서 데이터 추출 정책 기준 사원별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportForUserPolicyList(ReportSearchVO searchVO) throws Exception;
	/**
	 *  보고서 데이터 추출 사원 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<ReportSearchResultVO> getDataReportOrgUserList(ReportSearchVO searchVO) throws Exception;
}
