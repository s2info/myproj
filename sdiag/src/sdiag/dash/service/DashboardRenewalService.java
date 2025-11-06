package sdiag.dash.service;

import java.util.HashMap;
import java.util.List;

import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;

public interface DashboardRenewalService {
	/**
	 * 데시보드 마지막 수집 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public UserMainIdxInfoVO getLastUserCollectScore(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 개인 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastUserPolicyScore(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 부서장 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastOrgPolicyScore(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 개인 - 마지막 정책별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getDetailScoreList(DashboardSearchVO searchVO) throws Exception;
	/**
	 * 마지막 수집 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getLastData() throws Exception;
	/**
	 * 신규글 여부
	 * @param empno
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBoardReadCountInfo(String empno) throws Exception;
}
