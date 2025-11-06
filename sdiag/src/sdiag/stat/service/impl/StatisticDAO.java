package sdiag.stat.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.pol.service.PolicySearchVO;
import sdiag.stat.service.StatisticResultVO;
import sdiag.stat.service.StatisticSearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("StatisticDAO")
public class StatisticDAO extends EgovAbstractDAO{

	/**
	 * 정책리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getPolicyTitleList() throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getPolicyTitleList");
	}
	/**
	 * Policy fact 마직막 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getPolicyfactLastDate() throws Exception{
		return (String)select("statistic.getPolicyfactLastDate");
	}
	/**
	 * 조직별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<StatisticResultVO> getPolictfactInfoOrgResult(StatisticSearchVO statSearchVO) throws Exception{
		return (List<StatisticResultVO>)list("statistic.getPolictfactInfoOrgResult", statSearchVO);
	}
	/**
	 * 조직별 통계 조회(전체조회수)
	 */
	public int getPolictfactInfoOrgTotalCount(StatisticSearchVO statSearchVO) throws Exception{
		return (int)select("statistic.getPolictfactInfoOrgTotalCount", statSearchVO);
	}
	
	
	public List<HashMap<String, Object>> getPolictfactInfoOrgForExportExcel(StatisticSearchVO statSearchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getPolictfactInfoOrgForExportExcel", statSearchVO);
	}
	/**
	 * 직원별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<StatisticResultVO> getPolictfactInfoUserResult(StatisticSearchVO statSearchVO) throws Exception{
		return (List<StatisticResultVO>)list("statistic.getPolictfactInfoUserResult", statSearchVO);
	}
	/**
	 * 직원별 통계 조회(전체조회수)
	 */
	public int getPolictfactInfoUserTotalCount(StatisticSearchVO statSearchVO) throws Exception{
		return (int)select("statistic.getPolictfactInfoUserTotalCount", statSearchVO);
	}
	
	public List<HashMap<String, Object>> getPolictfactInfoUserForExportExcel(StatisticSearchVO statSearchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getPolictfactInfoUserForExportExcel", statSearchVO);
	}
	/**
	 * 진단항목 및 정책 리스트 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDiagItemAndPolList(String majrCode) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getDiagItemAndPolList", majrCode);
	}
	/**
	 * 하위 조직 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSubOrgList(String upperOrgCode) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getSubOrgList", upperOrgCode);
	}
	
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicyTotalCount(StatisticSearchVO statSearchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getUpperOrgPolicyTotalCount", statSearchVO);
	}
	
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicySumCount(StatisticSearchVO statSearchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("statistic.getUpperOrgPolicySumCount", statSearchVO);
	}
}
