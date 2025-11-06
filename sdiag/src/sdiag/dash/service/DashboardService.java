package sdiag.dash.service;

import java.util.HashMap;
import java.util.List;

import sdiag.login.service.UserManageVO;
import sdiag.man.service.SearchVO;
import sdiag.pol.service.PolicySearchVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface DashboardService {

	public HashMap<String,Object> getUserIdxInfoCurr_foremp(SearchVO searchVO) throws Exception;
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScore(String emp_no) throws Exception;
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreTeam(String org_code) throws Exception;
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreDamdang(String org_code) throws Exception;
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBonbu(String org_code) throws Exception;
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBumoon(String org_code) throws Exception;
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreChong(String org_code) throws Exception;
	
	public HashMap<String,Object> getUserIdxInfoCount(String emp_no) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCountTeam(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCountDamdang(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCountBonbu(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCountBumoon(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCountChong(String org_code) throws Exception;	
	
	public HashMap<String,Object> getUserIdxInfoTrendPersonal(String emp_no) throws Exception;
	public HashMap<String,Object> getUserIdxInfoTrendAll(String org_code) throws Exception;	
	public HashMap<String,Object> getUserIdxInfoTrendTeam(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoTrendDamdang(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoTrendBonbu(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoTrendBumoon(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoTrendChong(String org_code) throws Exception;	
	
	public UserIdxInfoCurrVO getUserInfoOrg(String emp_no) throws Exception;
	public HashMap<String,Object> getUserInfo(String emp_no) throws Exception;
	
	public HashMap<String,Object> getUserIdxInfoCurr(String emp_no) throws Exception;	
	public HashMap<String,Object> getUserIdxInfoCurrTeam(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCurrDamdang(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCurrBonbu(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCurrBumoon(String org_code) throws Exception;
	public HashMap<String,Object> getUserIdxInfoCurrChong(String org_code) throws Exception;
	
	public List<EgovMap> getUserIdxInfoCurrTong(HashMap<String, String> hmap) throws Exception;

	public UserManageVO getProxyUser(HashMap<String, String> hmap) throws Exception;

	public List<EgovMap> getOrgSubList(DashboardSearchVO searchVO) throws Exception;
	
	public List<EgovMap> getUserInfoCap(DashboardSearchVO searchVO) throws Exception;	

	public EgovMap getOrgResultInfo(DashboardSearchVO searchVO) throws Exception;

	public List<EgovMap> getUserOrgResultList(DashboardSearchVO searchVO) throws Exception;

	public EgovMap getOrgResultTrendInfo(DashboardSearchVO searchVO)  throws Exception;
	
	/*V2.0 보안점수 추이*/
	public HashMap<String, Object> getOrgCodeUpper(String emp_no) throws Exception;

	public HashMap<String, Object> getOrgSumInfoTrend(String org_code) throws Exception;
	
	public HashMap<String, Object> getOrgBuseoSumInfoTrend(String org_code) throws Exception;

	public HashMap<String, Object> getPerSumInfoTrend(String emp_no) throws Exception;

	/*V2.0 현재 보안점수*/
	public UserIdxInfoCurrVO getOrgCurScore(String org_code) throws Exception;
	
	/*V2.0 현재 보안점수 + 부서 */
	public UserIdxInfoCurrVO getOrgBuseoCurScore(String org_code) throws Exception;	

	public UserIdxInfoCurrVO getPerCurScore(String emp_no) throws Exception;
	
	/*V2.0 진단항목발생 건수*/
	public HashMap<String, Object> getOrgIdxCount(String orgCode) throws Exception;
	
	/*V2.0 진단항목발생 건수 + 부서*/
	public HashMap<String, Object> getOrgBuseoIdxCount(String orgCode) throws Exception;	
	
	public HashMap<String, Object> getPerIdxCount(String emp_no) throws Exception;

	public List<EgovMap> perDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception;

	public List<EgovMap> memberDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception;

	public List<EgovMap> orgDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception;
	
	public List<EgovMap> orgBuseoDiagMajrPolscore(DashboardSearchVO searchVO) throws Exception;	

	public HashMap<String, Object> perDiagPolsumcount(DashboardSearchVO searchVO) throws Exception;

	public HashMap<String, Object> minMaxdate(DashboardSearchVO searchVO) throws Exception;

	public List<EgovMap> perDiagMajrOrgPolscore(DashboardSearchVO searchVO) throws Exception;

	public UserIdxInfoCurrVO getTotalOrgBuseoIdxCount(HashMap<String, String> sMap) throws Exception;

	public List<EgovMap> perDiagMajrUserPolscore(DashboardSearchVO searchVO) throws Exception;

	//조지장 사번 조회
	public String getOrgCapNo(String dpflag);
	
		
}
