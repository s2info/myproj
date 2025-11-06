package sdiag.dash.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("DashboardRenewalDAO")
public class DashboardRenewalDAO extends EgovAbstractDAO{

	/**
	 * 데시보드 마지막 수집 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public UserMainIdxInfoVO getLastUserCollectScore(DashboardSearchVO dashboardSearchVO) throws Exception{
		return (UserMainIdxInfoVO)select("dashboard.getLastUserCollectScore", dashboardSearchVO);
	}
	/**
	 * 개인 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastUserPolicyScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getLastUserPolicyScore", searchVO);
	}
	/**
	 * 부서장 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastOrgPolicyScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getLastOrgPolicyScore", searchVO);
	}
	/**
	 * 개인 - 마지막 정책별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getUserPolicyForScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getUserPolicyForScore", searchVO);
	}
	/**
	 * 부서장 - 정책 미선택시 하위 부서 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getOrgNonePolicyForScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getOrgNonePolicyForScore", searchVO);
	}
	/**
	 * 부서장 - 정책 선택시 하위 부서 점수  조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getOrgPolicyForScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getOrgPolicyForScore", searchVO);
	}
	/**
	 * 부서장 하위부서 없음 즉 팀장 일경우 전체 정책 팀원별 점수 조회 [가중치적용]
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getTeamTotalForScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getTeamTotalForScore", searchVO);
	}
	/**
	 *  부서장 하위부서 없음 즉 팀장 일경우 정책별 팀원 점수 조회 [가중치 미적용]
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getTeamPolicyForScore(DashboardSearchVO searchVO) throws Exception{
		return (List<UserPolIdxInfoVO>)list("dashboard.getTeamPolicyForScore", searchVO);
	}
	/**
	 * 마지막 수집 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getLastData() throws Exception{
		return (String)select("dashboard.getLastData");
	}
	/**
	 * 신규글 여부
	 * @param empno
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBoardReadCountInfo(String empno) throws Exception{
		return (HashMap<String, Object>)select("dashboard.getBoardReadCountInfo", empno);
	}
}
