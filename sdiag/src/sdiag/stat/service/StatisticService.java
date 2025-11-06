package sdiag.stat.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface StatisticService {

	/**
	 * 정책리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getPolicyTitleList() throws Exception;
	/**
	 * Policy fact 마직막 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getPolicyfactLastDate() throws Exception;
	/**
	 * 조직별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getPolictfactInfoOrgResult(StatisticSearchVO statSearchVO) throws Exception;
	/**
	 * 직원별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getPolictfactInfoUserResult(StatisticSearchVO statSearchVO) throws Exception;
	/**
	 * 조직별 / 직원별 통계 Export Excel
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getStatisticOrgUserListForExportExcel(StatisticSearchVO statSearchVO) throws Exception;
	/**
	 * 진단항목 및 정책 리스트 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDiagItemAndPolList(String majrCode) throws Exception;
	/**
	 * 하위 조직 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSubOrgList(String upperOrgCode) throws Exception;
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicyTotalCount(StatisticSearchVO statSearchVO) throws Exception;
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicySumCount(StatisticSearchVO statSearchVO) throws Exception;
}
