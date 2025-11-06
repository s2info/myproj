package sdiag.stat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.stat.service.StatisticResultVO;
import sdiag.stat.service.StatisticSearchVO;
import sdiag.stat.service.StatisticService;

@Service("StatisticService")
public class StatisticServiceImpl implements StatisticService{

	@Resource(name = "StatisticDAO")
	private StatisticDAO statDAO;
	
	/**
	 * 정책리스트 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getPolicyTitleList() throws Exception{
		return statDAO.getPolicyTitleList();
	}
	/**
	 * Policy fact 마직막 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getPolicyfactLastDate() throws Exception{
		return statDAO.getPolicyfactLastDate();
	}
	/**
	 * 조직별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getPolictfactInfoOrgResult(StatisticSearchVO statSearchVO) throws Exception{
		
		List<StatisticResultVO> List = statDAO.getPolictfactInfoOrgResult(statSearchVO);
		int TotalCount = statDAO.getPolictfactInfoOrgTotalCount(statSearchVO);
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("List", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
		
		//return statDAO.getPolictfactInfoOrgResult(statSearchVO);
	}
	/**
	 * 직원별 통계 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getPolictfactInfoUserResult(StatisticSearchVO statSearchVO) throws Exception{
		
		List<StatisticResultVO> List = statDAO.getPolictfactInfoUserResult(statSearchVO);
		int TotalCount = statDAO.getPolictfactInfoUserTotalCount(statSearchVO);
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("List", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
		
	}
	/**
	 * 조직별 / 직원별 통계 Export Excel
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getStatisticOrgUserListForExportExcel(StatisticSearchVO statSearchVO) throws Exception{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		if(statSearchVO.getSearchType().equals("O")){
			list = statDAO.getPolictfactInfoOrgForExportExcel(statSearchVO);
		}else{
			list = statDAO.getPolictfactInfoUserForExportExcel(statSearchVO);
		}
		return list;
	}
	
	/**
	 * 진단항목 및 정책 리스트 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDiagItemAndPolList(String majrCode) throws Exception{
		return statDAO.getDiagItemAndPolList(majrCode);
	}
	/**
	 * 하위 조직 조회
	 * @param upperOrgCode
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSubOrgList(String upperOrgCode) throws Exception{
		return statDAO.getSubOrgList(upperOrgCode);
	}
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicyTotalCount(StatisticSearchVO statSearchVO) throws Exception{
		return statDAO.getUpperOrgPolicyTotalCount(statSearchVO);
	}
	
	/**
	 * 조직별 전체 정책 건수 조회
	 * @param statSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUpperOrgPolicySumCount(StatisticSearchVO statSearchVO) throws Exception{
		statSearchVO.setBegindate(statSearchVO.getBegindate().replace("-", ""));
		statSearchVO.setEnddate(statSearchVO.getEnddate().replace("-", ""));
		return statDAO.getUpperOrgPolicySumCount(statSearchVO);
	}
}
