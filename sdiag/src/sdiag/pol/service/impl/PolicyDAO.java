package sdiag.pol.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.man.service.SearchVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.sanct.service.SanctSearchResultListVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Repository("PolicyDAO")
public class PolicyDAO extends EgovAbstractDAO{
	
	public List<OrgGroupVO> getOrgGroupList() throws Exception{
		List<OrgGroupVO> list = (List<OrgGroupVO>)list("pol.service.getOrgGroupList");
		return list;
	}
	
	public List<OrgGroupVO> getOrgGroupSelectList(PolicySearchVO searchVO) throws Exception{
		List<OrgGroupVO> list = (List<OrgGroupVO>)list("pol.service.getOrgGroupSelectList", searchVO);
		return list;
	}
	
	//TEST
	public Object noticeinserttest() {
		try
		{
			
			
			int nseq = (int)select("pol.service.noticemaxseq");
			insert("pol.service.noticeinserttest", nseq);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	
	}
	
	public String getUserInfoLastDate(PolicySearchVO searchVO) throws Exception{
		return (String)select("pol.service.getUserInfoLastDate", searchVO);
	}
	
	public List<UserIdxinfoVO> getUserIdxInfoList(PolicySearchVO searchVO) throws Exception{
		List<UserIdxinfoVO> list = (List<UserIdxinfoVO>)list("pol.service.getUserIdxInfoList", searchVO);
		return list;
	}
	
	public int getUserIdxInfoTotalCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getUserIdxInfoTotalCount", searchVO);
	}
	
	//조직선택박스 구성을 위한 일반사용자 정보조회
	public List<EgovMap> getUserCurrentInfo(String userid) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserCurrentInfo", userid);
	}
	
	public List<EgovMap> getProxyUserInfo(String userid) throws Exception{
		return (List<EgovMap>)list("pol.service.getProxyUserInfo", userid);
	}
	
	/*지수화정책 조회*/
	public List<EgovMap> getBumonOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBumonOrgPolResultList", searchVO);
	}
	public List<EgovMap> getBonbuOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBonbuOrgPolResultList", searchVO);
	}
	public List<EgovMap> getDamDangOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getDamDangOrgPolResultList", searchVO);
	}
	public List<EgovMap> getTeamOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getTeamOrgPolResultList", searchVO);
	}
	/**
	 * 팀원 결과 조회 - 일반진단
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserOrgPolResultList", searchVO);
	}
	/**
	 * 팀원 결과 조회 - 부서진단
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getBuseoUserOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBuseoUserOrgPolResultList", searchVO);
	}
	
	/**
	 * 개인별 지수정책 진단결과 조회 - 일반진단
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserOrgPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserOrgPolDetailResultList", searchVO);
	}
	
	/**
	 * 개인별 지수정책 진단결과 조회 - 부서진단
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getBuseoUserOrgPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBuseoUserOrgPolDetailResultList", searchVO);
	}
	
	public List<EgovMap> getUserSearchPolResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserSearchPolResultList", searchVO);
	}
	
	public List<EgovMap> getUserSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserSearchPolDetailResultList", searchVO);
	}
	/**
	 * 정책 모니터링 상세 Paging: 검색 사용자 정책결과 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserSearchPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserSearchPolDetailResultListForPaging", searchVO);
	}
	/**
	 * 정책 모니터링 상세 Paging Count: 검색 사용자 정책결과 조회 count
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getUserSearchPolDetailResultListForPagingCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getUserSearchPolDetailResultListForPagingCount", searchVO);
	}
	
	/**
	 * 정책 모니터링 상세 Paging: 검색 사용자 정책결과 하위조직 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserOrgSearchPolDetailResultListForpaging(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserOrgSearchPolDetailResultListForpaging", searchVO);
	}
	/**
	 * 정책 모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("pol.service.getUserSearchPolDetailResultListForExcelList", searchVO);
	}
	/**
	 * 정책 모니터링 상세 Paging Count: 검색 사용자 정책결과 하위조직 조회 count
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getUserOrgSearchPolDetailResultListForPagingCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getUserOrgSearchPolDetailResultListForPagingCount", searchVO);
	}
	
	/**
	 * 정책 모니터일 상세 Export Excel : 하위 직원리스트
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExportExcel(PolicySearchVO searchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("pol.service.getUserSearchPolDetailResultListForExportExcel", searchVO);
	}
	
	public List<EgovMap> getUserTeamSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getUserTeamSearchPolDetailResultList", searchVO);
	}
	
	public List<HashMap<?,?>> getPolDetailLogForDateNUser(HashMap<String, String> hMap) throws Exception{
		return (List<HashMap<?,?>>)list("pol.service.getPolDetailLogForDateNUser", hMap);
	}
	
	public EgovMap getPolDetailLogTableNColumns(HashMap<String, String> hMap) throws Exception{
		return (EgovMap)select("pol.service.getPolDetailLogTableNColumns", hMap);
	}
	/*지수화정책 조회끝*/
	/*소명신청*/
	public int setDeleteApprAllList(HashMap<String, Object> hMap) throws Exception{
		return delete("pol.setDeleteApprAllList", hMap);
	}
	
	public int setUpdatePolApprAllList(HashMap<String, Object> hMap) throws Exception{
		return update("pol.setUpdatePolApprAllList", hMap);
	}
	
	public Object setInsertApprInfoAllList(HashMap<String, Object> hMap) throws Exception{
		return insert("pol.setInsertApprInfoAllList", hMap);
	}
	/*소명신청끝*/
	
	/**선택조직 하위 조회*/
	public List<EgovMap> getOrgSubList(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.getOrgSubList", searchVO);
	}
	
	/**조직별점수 건수 조회*/
	public EgovMap getOrgResultInfo(PolicySearchVO searchVO) throws Exception{
		try
		{
			EgovMap list = (EgovMap)select("pol.getOrgResultInfo", searchVO);
			
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public EgovMap getApplInfo(String appr_id) throws Exception{
		return (EgovMap)select("pol.getApplInfo", appr_id);
	}
	/**
	 * 소명처리 시스템 소명정보 조회
	 * @param appr_id
	 * @return
	 * @throws Exception
	 */
	public EgovMap getPovApplInfo(String appr_id) throws Exception{
		return (EgovMap)select("pol.getPovApplInfo", appr_id);
	}
	
	public int setUpdateApprDesc(HashMap<String, Object> hMap) throws Exception{
		return update("pol.setUpdateApprDesc", hMap);
	}
	
	public List<SanctSearchResultListVO> getUserSearchSanctiontList(PolicySearchVO searchVO) throws Exception{
		return (List<SanctSearchResultListVO>)list("pol.service.getUserSearchSanctiontList", searchVO);
	}
	
	public int getUserSearchSanctiontListTotalCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getUserSearchSanctiontListTotalCount", searchVO);
	}
	/**
	 * 수동제재/소명 검색결과 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchSanctiontListForExportExcel(PolicySearchVO searchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("pol.service.getUserSearchSanctiontListForExportExcel", searchVO);
	}
	
	public int setUpdateApprFileInfo(HashMap<String, Object> hMap) throws Exception{
		return update("pol.setUpdateApprFileInfo", hMap);
	}
	/**
	 * 지수정책 모니터링 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgPolResultListInfo(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.getOrgPolResultListInfo", searchVO);
	}
	/**
	 * 부서진단 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getBuseoSearchPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBuseoSearchPolDetailResultListForPaging", searchVO);
	}
	/**
	 * 부서진단 상세정보 총카운트 조회(페이징처리)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getBuseoSearchPolDetailResultListForPagingCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getBuseoSearchPolDetailResultListForPagingCount", searchVO);
	}
	/**
	 * 부서진단 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getBuseoSubOrgPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception{
		return (List<EgovMap>)list("pol.service.getBuseoSubOrgPolDetailResultListForPaging", searchVO);
	}
	/**
	 * 부서진단 상세정보 총카운트 조회(페이징처리)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getBuseoSubOrgPolDetailResultListForPagingCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getBuseoSubOrgPolDetailResultListForPagingCount", searchVO);
	}
	/**
	 * 부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("pol.service.getBuseoSearchPolDetailResultListForExcelList", searchVO);
	}
	/**
	 * 부서진단 하위부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSubOrgPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return (List<HashMap<String, Object>>)list("pol.service.getBuseoSubOrgPolDetailResultListForExcelList", searchVO);
	}
}


