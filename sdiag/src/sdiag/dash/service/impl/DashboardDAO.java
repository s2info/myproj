package sdiag.dash.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.login.service.UserManageVO;
import sdiag.man.service.SearchVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.UserIdxInfoCurrVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("DashboardDAO")
public class DashboardDAO extends EgovAbstractDAO{
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurr_foremp(SearchVO searchVO) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurr_foremp", searchVO);
		return list;
	}	

	public UserIdxInfoCurrVO getUserIdxInfoCurrScore(String emp_no) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScore", emp_no);
	}

	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreTeam(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScoreTeam", org_code);
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreDamdang(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScoreDamdang", org_code);
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBonbu(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScoreBonbu", org_code);
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBumoon(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScoreBumoon", org_code);
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreChong(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserIdxInfoCurrScoreChong", org_code);
	}	
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCount(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCount", emp_no);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCountTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCountTeam", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCountDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCountDamdang", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCountBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCountBonbu", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCountBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCountBumoon", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCountChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCountChong", org_code);
		return list;
	}	

	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendAll(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendAll", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendPersonal(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendPersonal", emp_no);
		return list;
	}	
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendTeam", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendDamdang", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendBonbu", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendBumoon", org_code);
		return list;
	}	

	public List<UserIdxInfoCurrVO> getUserIdxInfoTrendChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoTrendChong", org_code);
		return list;
	}

	public UserIdxInfoCurrVO getUserInfoOrg(String emp_no) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getUserInfoOrg", emp_no);
	}	
	
	public List<UserIdxInfoCurrVO> getUserInfo(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserInfo", emp_no);
		return list;		
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurr(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurr", emp_no);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurrTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurrTeam", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurrDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurrDamdang", org_code);
		return list;
	}

	public List<UserIdxInfoCurrVO> getUserIdxInfoCurrBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurrBonbu", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurrBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurrBumoon", org_code);
		return list;
	}
	
	public List<UserIdxInfoCurrVO> getUserIdxInfoCurrChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getUserIdxInfoCurrChong", org_code);
		return list;
	}

	public UserManageVO getProxyUser(HashMap<String, String> hMap) throws Exception{
		return (UserManageVO)select("dashboard.getProxyUser", hMap);
	}
	
	public List<EgovMap> getUserIdxInfoCurrTong(HashMap<String, String> hMap) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("dashboard.getUserIdxInfoCurrTong", hMap);
		return list;
	}

	public List<EgovMap> getOrgSubList(DashboardSearchVO searchVO) throws Exception{
		// TODO Auto-generated method stub
		return (List<EgovMap>)list("dashboard.getOrgSubList", searchVO);
	}

	public EgovMap getOrgResultInfo(DashboardSearchVO searchVO) throws Exception{
		// TODO Auto-generated method stub
		try
		{
			EgovMap list = (EgovMap)select("dashboard.getOrgResultInfo", searchVO);
			
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public List<EgovMap> getUserOrgResultList(DashboardSearchVO searchVO) throws Exception{
		// TODO Auto-generated method stub
		return (List<EgovMap>)list("dashboard.getUserOrgResultList", searchVO);
	}

	public List<EgovMap> getUserInfoCap(DashboardSearchVO searchVO) {
		return (List<EgovMap>)list("dashboard.getUserInfoCap", searchVO);
	}

	public EgovMap getOrgResultTrendInfo(DashboardSearchVO searchVO) {
		try
		{
			EgovMap list = (EgovMap)select("dashboard.getOrgResultTrendInfo", searchVO);
			
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public List<UserIdxInfoCurrVO> getOrgCodeUpper(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getOrgCodeUpper", emp_no);
		return list;
	}

	public java.util.List<UserIdxInfoCurrVO> getOrgSumInfoTrend(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getOrgSumInfoTrend", org_code);
		return list;
	}
	
	public java.util.List<UserIdxInfoCurrVO> getOrgBuseoSumInfoTrend(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getOrgBuseoSumInfoTrend", org_code);
		return list;
	}	

	public java.util.List<UserIdxInfoCurrVO> getPerSumInfoTrend(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getPerSumInfoTrend", emp_no);
		return list;
	}

	public UserIdxInfoCurrVO getOrgCurScore(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getOrgCurScore", org_code);
	}
	
	public UserIdxInfoCurrVO getOrgCurBuseoScore(String org_code) throws Exception{
		return (UserIdxInfoCurrVO)select("dashboard.getOrgBuseoCurScore", org_code);
	}	

	public UserIdxInfoCurrVO getPerCurScore(String emp_no) {
		return (UserIdxInfoCurrVO)select("dashboard.getPerCurScore", emp_no);
	}

	public List<UserIdxInfoCurrVO> getOrgIdxCount(String orgCode) {
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getOrgIdxCount", orgCode);
		return list;		
	}
	
	public List<UserIdxInfoCurrVO> getOrgBuseoIdxCount(String orgCode) {
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getOrgBuseoIdxCount", orgCode);
		return list;		
	}	

	public List<UserIdxInfoCurrVO> getPerIdxCount(String emp_no) {
		List<UserIdxInfoCurrVO> list = (List<UserIdxInfoCurrVO>)list("dashboard.getPerIdxCount", emp_no);
		return list;	
	}

	public List perDiagMajrPolscore(DashboardSearchVO searchVO) {
		return list("dashboard.perDiagMajrPolscore",searchVO);
	}

	public List memberDiagMajrPolscore(DashboardSearchVO searchVO) {
		return list("dashboard.memberDiagMajrPolscore",searchVO);
	}

	public List orgDiagMajrPolscore(DashboardSearchVO searchVO) {
		return list("dashboard.orgDiagMajrPolscore",searchVO);
	}
	
	public List orgBuseoDiagMajrPolscore(DashboardSearchVO searchVO) {
		return list("dashboard.orgBuseoDiagMajrPolscore",searchVO);
	}	

	public HashMap<String, Object> perDiagPolsumcount(DashboardSearchVO searchVO) {
		return (HashMap<String, Object>) select("dashboard.perDiagPolsumcount",searchVO);
	}

	public HashMap<String, Object> minMaxdate(DashboardSearchVO searchVO) {
		return (HashMap<String, Object>) select("dashboard.minMaxdate",searchVO);
	}

	public UserIdxInfoCurrVO getTotalBuseoIdxCount(HashMap<String, String> sMap) {
		return (UserIdxInfoCurrVO)select("dashboard.getTotalBuseoIdxCount", sMap);
	}

	public List<EgovMap> perDiagMajrOrgPolscore(DashboardSearchVO searchVO) {
		List<EgovMap> list = (List<EgovMap>)list("dashboard.perDiagMajrOrgPolscore", searchVO);
		return list;
	}

	public List<EgovMap> perDiagMajrUserPolscore(DashboardSearchVO searchVO) {
		List<EgovMap> list = (List<EgovMap>)list("dashboard.perDiagMajrUserPolscore", searchVO);
		return list;
	}

	//조직장 사번 조회
	public String getOrgCapNo(String dpflag) {
		return (String) select("dashboard.getOrgCapNo",dpflag);
	}

	
}
