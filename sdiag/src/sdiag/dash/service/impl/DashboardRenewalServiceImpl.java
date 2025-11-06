package sdiag.dash.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.dash.service.DashboardRenewalService;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;

//DashboardRenewalService
@Service("DashboardRenewalService")
public class DashboardRenewalServiceImpl implements DashboardRenewalService{
	@Resource(name = "DashboardRenewalDAO")
	private DashboardRenewalDAO dao;
	
	/**
	 * 데시보드 마지막 수집 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public UserMainIdxInfoVO getLastUserCollectScore(DashboardSearchVO searchVO) throws Exception{
		return dao.getLastUserCollectScore(searchVO);
	}
	/**
	 * 개인 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastUserPolicyScore(DashboardSearchVO searchVO) throws Exception{
		return dao.getLastUserPolicyScore(searchVO);
	}
	/**
	 * 부서장 - 마지막 날짜 정책별 점수 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getLastOrgPolicyScore(DashboardSearchVO searchVO) throws Exception{
		return dao.getLastOrgPolicyScore(searchVO);
	}
	/**
	 * 개인 - 마지막 정책별 점수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserPolIdxInfoVO> getDetailScoreList(DashboardSearchVO searchVO) throws Exception{
		List<UserPolIdxInfoVO> result = new ArrayList<UserPolIdxInfoVO>();
		searchVO.setSearch_date(searchVO.getSearch_date().replace("-", ""));
		if(searchVO.getSearchCondition().equals("P")){
			//개인
			result = dao.getUserPolicyForScore(searchVO);
		}else if(searchVO.getSearchCondition().equals("R")){
			//정책 선택 없음
			if(searchVO.getIsSubOrg().equals("Y")){
				//하위 부서 존재
				result = dao.getOrgNonePolicyForScore(searchVO);
			}else{
				//하위 부서 없음
				result = dao.getTeamTotalForScore(searchVO);
			}
			
		}else{
			if(searchVO.getMajrCode().equals("")){
				if(searchVO.getIsSubOrg().equals("Y")){
					//하위 부서 존재
					result = dao.getOrgNonePolicyForScore(searchVO);
				}else{
					//하위 부서 없음
					result = dao.getTeamTotalForScore(searchVO);
				}
			}else{
				//정책 선택
				if(searchVO.getIsSubOrg().equals("Y")){
					result = dao.getOrgPolicyForScore(searchVO);
				}else{
					result = dao.getTeamPolicyForScore(searchVO);
				}
			}
			
		}
		return result;
	}
	/**
	 * 마지막 수집 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public String getLastData() throws Exception{
		return dao.getLastData();
	}
	/**
	 * 신규글 여부
	 * @param empno
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBoardReadCountInfo(String empno) throws Exception{
		return dao.getBoardReadCountInfo(empno);
	}
}
