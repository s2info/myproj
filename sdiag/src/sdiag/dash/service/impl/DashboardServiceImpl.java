package sdiag.dash.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.dash.service.DashboardSearchVO;
import sdiag.dash.service.DashboardService;
import sdiag.login.service.UserManageVO;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.dash.service.UserIdxInfoCurrVO;

@Service("DashboardService")
public class DashboardServiceImpl implements DashboardService{
	@Resource(name = "DashboardDAO")
	private DashboardDAO dashboardDao;
	
	public HashMap<String,Object> getUserIdxInfoCurr_foremp(SearchVO searchVO) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurr_foremp(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScore(String emp_no) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScore(emp_no);			
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreTeam(String org_code) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScoreTeam(org_code);			
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreDamdang(String org_code) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScoreDamdang(org_code);			
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBonbu(String org_code) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScoreBonbu(org_code);			
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreBumoon(String org_code) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScoreBumoon(org_code);			
	}
	
	public UserIdxInfoCurrVO getUserIdxInfoCurrScoreChong(String org_code) throws Exception{	
		return dashboardDao.getUserIdxInfoCurrScoreChong(org_code);			
	}		
	
	public HashMap<String,Object> getUserIdxInfoCount(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCount(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCountTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCountTeam(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCountDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCountDamdang(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCountBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCountBonbu(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCountBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCountBumoon(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCountChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCountChong(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}	
	

	public HashMap<String,Object> getUserIdxInfoTrendAll(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendAll(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoTrendPersonal(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendPersonal(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}	
	
	public HashMap<String,Object> getUserIdxInfoTrendTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendTeam(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoTrendDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendDamdang(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoTrendBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendBonbu(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}

	public HashMap<String,Object> getUserIdxInfoTrendBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendBumoon(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoTrendChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoTrendChong(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public UserIdxInfoCurrVO getUserInfoOrg(String emp_no) throws Exception{	
		return dashboardDao.getUserInfoOrg(emp_no);			
	}	
	
	public HashMap<String, Object> getUserInfo(String emp_no) throws Exception{	
		
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserInfo(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
		
	}
	
	public HashMap<String,Object> getUserIdxInfoCurr(String emp_no) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurr(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCurrTeam(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurrTeam(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	

	public HashMap<String,Object> getUserIdxInfoCurrDamdang(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurrDamdang(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCurrBonbu(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurrBonbu(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCurrBumoon(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurrBumoon(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public HashMap<String,Object> getUserIdxInfoCurrChong(String org_code) throws Exception{
		List<UserIdxInfoCurrVO> List = dashboardDao.getUserIdxInfoCurrChong(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public UserManageVO getProxyUser(HashMap<String, String> hMap) throws Exception{
		return dashboardDao.getProxyUser(hMap);
	}
	
	public List<EgovMap> getUserIdxInfoCurrTong(HashMap<String, String> hMap) throws Exception{
		
		return dashboardDao.getUserIdxInfoCurrTong(hMap);
		
	}

	@Override
	public List<EgovMap> getOrgSubList(DashboardSearchVO searchVO)
			throws Exception {
		// TODO Auto-generated method stub
		return dashboardDao.getOrgSubList(searchVO);
	}

	@Override
	public EgovMap getOrgResultInfo(DashboardSearchVO searchVO)
			throws Exception {
		// TODO Auto-generated method stub
		return dashboardDao.getOrgResultInfo(searchVO);
	}

	@Override
	public List<EgovMap> getUserOrgResultList(DashboardSearchVO searchVO)
			throws Exception {
		// TODO Auto-generated method stub
		return dashboardDao.getUserOrgResultList(searchVO);
	}

	@Override
	public List<EgovMap> getUserInfoCap(DashboardSearchVO searchVO)
			throws Exception {
		// TODO Auto-generated method stub
		return dashboardDao.getUserInfoCap(searchVO);
	}

	@Override
	public EgovMap getOrgResultTrendInfo(DashboardSearchVO searchVO)
			throws Exception {
		// TODO Auto-generated method stub
		return dashboardDao.getOrgResultTrendInfo(searchVO);
	}

	/*V2.0*/
	@Override
	public HashMap<String, Object> getOrgCodeUpper(String emp_no)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getOrgCodeUpper(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}

	@Override
	public HashMap<String, Object> getOrgSumInfoTrend(String org_code)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getOrgSumInfoTrend(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	@Override
	public HashMap<String, Object> getOrgBuseoSumInfoTrend(String org_code)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getOrgBuseoSumInfoTrend(org_code);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}	

	@Override
	public HashMap<String, Object> getPerSumInfoTrend(String emp_no)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getPerSumInfoTrend(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}

	@Override
	public UserIdxInfoCurrVO getOrgCurScore(String org_code) throws Exception {
		return dashboardDao.getOrgCurScore(org_code);	
	}
	
	@Override
	public UserIdxInfoCurrVO getOrgBuseoCurScore(String org_code) throws Exception {
		return dashboardDao.getOrgCurBuseoScore(org_code);	
	}	

	@Override
	public UserIdxInfoCurrVO getPerCurScore(String emp_no) throws Exception {
		return dashboardDao.getPerCurScore(emp_no);
	}

	@Override
	public HashMap<String, Object> getOrgIdxCount(String orgCode)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getOrgIdxCount(orgCode);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;		
	}
	
	@Override
	public HashMap<String, Object> getOrgBuseoIdxCount(String orgCode)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getOrgBuseoIdxCount(orgCode);
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;		
	}	

	@Override
	public HashMap<String, Object> getPerIdxCount(String emp_no)
			throws Exception {
		List<UserIdxInfoCurrVO> List = dashboardDao.getPerIdxCount(emp_no);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;	
	}

	@Override
	public List<EgovMap> perDiagMajrPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.perDiagMajrPolscore(searchVO);
	}

	@Override
	public List<EgovMap> memberDiagMajrPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.memberDiagMajrPolscore(searchVO);
	}

	@Override
	public List<EgovMap> orgDiagMajrPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.orgDiagMajrPolscore(searchVO);
	}
	
	@Override
	public List<EgovMap> orgBuseoDiagMajrPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.orgBuseoDiagMajrPolscore(searchVO);
	}	

	@Override
	public HashMap<String, Object> perDiagPolsumcount(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.perDiagPolsumcount(searchVO);
	}

	@Override
	public HashMap<String, Object> minMaxdate(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.minMaxdate(searchVO);
	}

	@Override
	public List<EgovMap> perDiagMajrOrgPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.perDiagMajrOrgPolscore(searchVO);
	}

	@Override
	public UserIdxInfoCurrVO getTotalOrgBuseoIdxCount(HashMap<String, String> sMap)
			throws Exception {
		UserIdxInfoCurrVO total = dashboardDao.getTotalBuseoIdxCount(sMap);
		return total;
	}

	@Override
	public List<EgovMap> perDiagMajrUserPolscore(DashboardSearchVO searchVO)
			throws Exception {
		return dashboardDao.perDiagMajrUserPolscore(searchVO);
	}

	//조징상 사번 조회
	public String getOrgCapNo(String dpflag) {
		return dashboardDao.getOrgCapNo(dpflag);
	}
	

}
