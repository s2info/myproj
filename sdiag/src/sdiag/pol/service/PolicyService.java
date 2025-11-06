package sdiag.pol.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface PolicyService {

	
	public List<OrgGroupVO> getOrgGroupList() throws Exception;
	public List<OrgGroupVO> getOrgGroupSelectList(PolicySearchVO searchVO) throws Exception;
	
	public Object noticeinserttest() throws Exception;
	
	public String getUserInfoLastDate(PolicySearchVO searchVO) throws Exception;
	
	public HashMap<String,Object> getUserIdxInfoList(PolicySearchVO searchVO) throws Exception;
	
	public List<EgovMap> getUserCurrentInfo(String userid) throws Exception;
	
	public List<EgovMap> getProxyUserInfo(String userid) throws Exception;
	
	public List<EgovMap> getOrgPolResultList(PolicySearchVO searchVO, int searchType) throws Exception;
	
	public List<EgovMap> getUserOrgPolResultList(PolicySearchVO searchVO) throws Exception;
	
	public List<EgovMap> getUserOrgPolDetailResultList(PolicySearchVO searchVO) throws Exception;
	
	public List<EgovMap> getUserSearchPolResultList(PolicySearchVO searchVO) throws Exception;
	
	/**
	 * 정책 모니터링 상세 Paging
	 */
	public HashMap<String, Object> getUserSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception;
	
	/**
	 * 정책 모니터링 상세 Paging 처리 - 하위 조직 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUserOrgSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception;
	
	
	public List<EgovMap> getUserTeamSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception;
	
	public List<HashMap<?,?>> getPolDetailLogForDateNUser(HashMap<String, String> hMap) throws Exception;
	
	public boolean setApprAllRegister(HashMap<String, Object> hMap) throws Exception;
	
	public HashMap<String, Object> getUserSearchPolDetailReaultListForPageing(PolicySearchVO searchVO) throws Exception;
	/**
	 * 수동제재/소명 검색결과 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchSanctiontListForExportExcel(PolicySearchVO searchVO) throws Exception;
	public int setUpdateApprFileInfo(HashMap<String, Object> hMap) throws Exception;
	
	/**
	 * 선택조직 하위 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgSubList(PolicySearchVO searchVO) throws Exception;
	public EgovMap getOrgResultInfo(PolicySearchVO searchVO) throws Exception;
	public EgovMap getApplInfo(String appr_id) throws Exception;
	/**
	 * 소명처리 시스템 소명정보 조회
	 * @param appr_id
	 * @return
	 * @throws Exception
	 */
	public EgovMap getPovApplInfo(String appr_id) throws Exception;
	public int setUpdateApprDesc(HashMap<String, Object> hMap) throws Exception;
	
	/**
	 * 지수정책 모니터링 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgPolResultListInfo(PolicySearchVO searchVO) throws Exception;
	/**
	 * 정책 모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception;
	/**
	 * 정책 모니터일 상세 Export Excel : 하위 직원리스트
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExportExcel(PolicySearchVO searchVO) throws Exception;
	/**
	 * 지수 정책 수집로그 조회 정보 테이블 / 컬럼...
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getPolDetailLogTableNColumns(HashMap<String, String> hMap) throws Exception;
	/**
	 * 부서진단 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBuseoSearchPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception;
	/**
	 * 부서진단 하위부서 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBuseoSubOrgPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception;
	/**
	 * 부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception;
	/**
	 * 부서진단 하위부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSubOrgPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception;
}
