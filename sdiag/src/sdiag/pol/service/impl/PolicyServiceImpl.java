package sdiag.pol.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.board.service.NoticeVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.pol.service.UserIdxinfoVO;
import sdiag.sanct.service.SanctSearchResultListVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("PolicyService")
public class PolicyServiceImpl implements PolicyService {

	@Resource(name = "PolicyDAO")
	private PolicyDAO polDao;
	
	public List<OrgGroupVO> getOrgGroupList() throws Exception{
		return polDao.getOrgGroupList();
	}
	
	public List<OrgGroupVO> getOrgGroupSelectList(PolicySearchVO searchVO) throws Exception{
		return polDao.getOrgGroupSelectList(searchVO);
	}
	
	public Object noticeinserttest() throws Exception{
	   Object ret =	polDao.noticeinserttest();

		return ret;
	}
	
	public String getUserInfoLastDate(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserInfoLastDate(searchVO);
	}
	
	public HashMap<String,Object> getUserIdxInfoList(PolicySearchVO searchVO) throws Exception{
		List<UserIdxinfoVO> List = polDao.getUserIdxInfoList(searchVO);
		int TotalCount = polDao.getUserIdxInfoTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
		
	}
	
	//조직선택박스 구성을 위한 일반사용자 정보조회
	public List<EgovMap> getUserCurrentInfo(String userid) throws Exception{
		return polDao.getUserCurrentInfo(userid);
	}
	
	public List<EgovMap> getProxyUserInfo(String userid) throws Exception{
		return polDao.getProxyUserInfo(userid);
	}
	
	//지수화정책조회
	public List<EgovMap> getOrgPolResultList(PolicySearchVO searchVO, int searchType) throws Exception{
		List<EgovMap> list = null;
		/*
		if(searchType == 1){
			list = polDao.getBumonOrgPolResultList(searchVO);
		}else if(searchType == 2){
			list = polDao.getBonbuOrgPolResultList(searchVO);
		}else if(searchType == 3){
			list = polDao.getDamDangOrgPolResultList(searchVO);
		}else if(searchType == 4){
			list = polDao.getTeamOrgPolResultList(searchVO);
		}
		*/
		
		return list;
	}
	/**
	 * 팀원 결과 조회 - 일반진단 / 부서진단 분기
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserOrgPolResultList(PolicySearchVO searchVO) throws Exception{
		if(searchVO.getBuseoType().equals("N")){
			return polDao.getUserOrgPolResultList(searchVO);
		}else{
			return polDao.getBuseoUserOrgPolResultList(searchVO);
		}
	}
	/**
	 * 개인별 지수정책 진단결과 조회 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getUserOrgPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		if(searchVO.getBuseoType().equals("N")){
			return polDao.getUserOrgPolDetailResultList(searchVO);
		}else{
			return polDao.getBuseoUserOrgPolDetailResultList(searchVO);
		}
		
	}
	
	public List<EgovMap> getUserSearchPolResultList(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserSearchPolResultList(searchVO);
	}
	
	/**
	 * 정책 모니터링 상세 Paging
	 */
	public HashMap<String, Object> getUserSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			List<EgovMap> dataList = polDao.getUserSearchPolDetailResultListForPaging(searchVO);
			result.put("list", dataList);
			
			int totalData = polDao.getUserSearchPolDetailResultListForPagingCount(searchVO);
			result.put("totalCount", totalData);
			
			return result;
		}catch(Exception e){
			//e.printStackTrace();
			result.put("totalCount", 0);
			return result;
		}
	}
	
	/**
	 *  정책 모니터링 상세 페이징 처리 - 하위 조직 조회
	 */
	public HashMap<String, Object> getUserOrgSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		//return polDao.getUserOrgSearchPolDetailResultList(searchVO);
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			List<EgovMap> dataList = polDao.getUserOrgSearchPolDetailResultListForpaging(searchVO);
			result.put("list", dataList);//getUserOrgSearchPolDetailResultListForpaging
			
			int totalData = polDao.getUserOrgSearchPolDetailResultListForPagingCount(searchVO);
			result.put("totalCount", totalData);
			
			return result;
		}catch(Exception e){
			//e.printStackTrace();
			result.put("totalCount", 0);
			return result;
		}
	}	
	/**
	 * 정책 모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserSearchPolDetailResultListForExcelList(searchVO);
	}
	/**
	 * 
	 */
	public HashMap<String, Object> getUserSearchPolDetailReaultListForPageing(PolicySearchVO searchVO) throws Exception{
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			List<SanctSearchResultListVO> dataList = polDao.getUserSearchSanctiontList(searchVO);
			result.put("list", dataList);
			
			int totalData = polDao.getUserSearchSanctiontListTotalCount(searchVO);
			result.put("totalCount", totalData);
			
			return result;
		}catch(Exception e){
			//e.printStackTrace();
			result.put("totalCount", 0);
			return result;
		}
	}
	/**
	 * 수동제재/소명 검색결과 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchSanctiontListForExportExcel(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserSearchSanctiontListForExportExcel(searchVO);
	}
	/**
	 * 정책 모니터일 상세 Export Excel : 하위 직원리스트
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getUserSearchPolDetailResultListForExportExcel(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserSearchPolDetailResultListForExportExcel(searchVO);
	}
	
	public List<EgovMap> getUserTeamSearchPolDetailResultList(PolicySearchVO searchVO) throws Exception{
		return polDao.getUserTeamSearchPolDetailResultList(searchVO);
	}
	
	public EgovMap getPolDetailLogTableNColumns(HashMap<String, String> hMap) throws Exception{
		return  polDao.getPolDetailLogTableNColumns(hMap);
	}
	
	public List<HashMap<?,?>> getPolDetailLogForDateNUser(HashMap<String, String> hMap) throws Exception{
		try
		{
			EgovMap tblInfo = polDao.getPolDetailLogTableNColumns(hMap);
		
			hMap.put("tblname", String.format("\"public\".\"%s\"", tblInfo.get("tblname").toString()));
			hMap.put("columnsname", tblInfo.get("columnsname").toString());
			return polDao.getPolDetailLogForDateNUser(hMap);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 소명일괄처리 -> 건by건으로 변경 사용안함
	 */
	public boolean setApprAllRegister(HashMap<String, Object> hMap) throws Exception{
		List<HashMap<String, String>> retVal = new ArrayList<HashMap<String, String>>();
		try
		{
			/*****************************************************
			 * BPM연동시 일관 처리 된다는 가정하에 내부처리함
			 * 신규 신청이므로 이전값존재시 모두 삭제 후 저장
			 * 
			 * ***************************************************/
			//BPM 소명신청 처리역역
			//.........
			
			
			List<String> apprList = (List<String>)hMap.get("appridList");
			List<String> appridList =  new ArrayList<String>();
			List<HashMap<String, String>> apprInfo = new ArrayList<HashMap<String, String>>();
			for(String appr:apprList){
				//사번 | appr_id 분리
				System.out.println(appr + "][");
				String[] appInfo = appr.split("/");
				
				appridList.add(appInfo[1]);
				
				System.out.println(appInfo[0] + "][" + appInfo[1] + "][" + appInfo[2]);
				
				HashMap<String, String> appr_Info = new HashMap<String, String>();
				appr_Info.put("apprid", appInfo[1]);
				appr_Info.put("empno", appInfo[0]);
				appr_Info.put("apprlinecode", appInfo[2]);
				apprInfo.add(appr_Info); 
			}
			
			//전체 삭제후 신규저장
			hMap.put("apprlist", appridList);
			hMap.put("apprInfo", apprInfo);
			
			polDao.setDeleteApprAllList(hMap);
			polDao.setUpdatePolApprAllList(hMap);
			polDao.setInsertApprInfoAllList(hMap);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public List<EgovMap> getOrgSubList(PolicySearchVO searchVO) throws Exception{
		return polDao.getOrgSubList(searchVO);
	}
	
	public EgovMap getOrgResultInfo(PolicySearchVO searchVO) throws Exception{
		return polDao.getOrgResultInfo(searchVO);
	}
	
	public EgovMap getApplInfo(String appr_id) throws Exception{
		return polDao.getApplInfo(appr_id);
	}
	/**
	 * 소명처리 시스템 소명정보 조회
	 * @param appr_id
	 * @return
	 * @throws Exception
	 */
	public EgovMap getPovApplInfo(String appr_id) throws Exception{
		return polDao.getPovApplInfo(appr_id);
	}
	
	public int setUpdateApprDesc(HashMap<String, Object> hMap) throws Exception{
		return polDao.setUpdateApprDesc(hMap);
	}
	
	public int setUpdateApprFileInfo(HashMap<String, Object> hMap) throws Exception{
		return polDao.setUpdateApprFileInfo(hMap);
	}
		
	/**
	 * 지수정책 모니터링 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgPolResultListInfo(PolicySearchVO searchVO) throws Exception{
		return polDao.getOrgPolResultListInfo(searchVO);
	}
	
	/**
	 * 부서진단 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBuseoSearchPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception{
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			List<EgovMap> dataList = polDao.getBuseoSearchPolDetailResultListForPaging(searchVO);
			result.put("list", dataList);
			
			int totalData = polDao.getBuseoSearchPolDetailResultListForPagingCount(searchVO);
			result.put("totalCount", totalData);
			
			return result;
		}catch(Exception e){
			//e.printStackTrace();
			result.put("totalCount", 0);
			return result;
		}
	}
	/**
	 * 부서진단 하위부서 상세정보 조회(페이징)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getBuseoSubOrgPolDetailResultListForPaging(PolicySearchVO searchVO) throws Exception{
		HashMap<String, Object> result = new HashMap<String, Object>();
		try{
			List<EgovMap> dataList = polDao.getBuseoSubOrgPolDetailResultListForPaging(searchVO);
			result.put("list", dataList);
			
			int totalData = polDao.getBuseoSubOrgPolDetailResultListForPagingCount(searchVO);
			result.put("totalCount", totalData);
			
			return result;
		}catch(Exception e){
			//e.printStackTrace();
			result.put("totalCount", 0);
			return result;
		}
	}
	/**
	 * 부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSearchPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return polDao.getBuseoSearchPolDetailResultListForExcelList(searchVO);
	}
	/**
	 * 부서진단 하위부서진단  모니터링 상세 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getBuseoSubOrgPolDetailResultListForExcelList(PolicySearchVO searchVO) throws Exception{
		return polDao.getBuseoSubOrgPolDetailResultListForExcelList(searchVO);
	}
}
