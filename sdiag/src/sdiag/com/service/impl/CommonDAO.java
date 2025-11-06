package sdiag.com.service.impl;


import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;


















import sdiag.com.service.ApprInfoVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.ConnectionLogVO;
import sdiag.com.service.LoginUserTypeVO;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.MenuItemVO;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.pol.service.OrgGroupVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Repository("CommonDAO")
public class CommonDAO extends EgovAbstractDAO{
	/**
	 * 조직 상위 리스트 조회
	 * @param orgcode
	 * @return
	 * @throws Exception
	 */
	public String getUpperOrgNames(String orgcode) throws Exception{
		return (String)select("commmon.getUpperOrgNames", orgcode);
	}
	/**
	 * 메뉴구성을 위한 진단항목조회
	 * @return
	 * @throws Exception
	 */
	public List<MenuItemVO> getSolMenuList() throws Exception{
		List<MenuItemVO> list = (List<MenuItemVO>)list("commmon.getSolMenuList");
		return list;
	}
	/**
	 * 메뉴구성을 위한 진단항목조회 미사용포함
	 * @return
	 * @throws Exception
	 */
	public List<MenuItemVO> getSolAllMenuList() throws Exception{
		List<MenuItemVO> list = (List<MenuItemVO>)list("commmon.getSolAllMenuList");
		return list;
	}
	/**
	 * 메뉴구성을위한 정책조회(대진단/중진단)
	 * @param hmap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolMenuList(HashMap<String, String> hmap) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("commmon.getPolMenuList", hmap);
		return list;
	}
	/**
	 * 정책정보 조회
	 * @param hmap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getPolMenuNaviString(HashMap<String, String> hmap) throws Exception{
		EgovMap list = (EgovMap)select("common.getPolMenuNaviString", hmap);
		return list;
	}
	/**
	 * 코드 리스트 조회
	 * @param majCode
	 * @return
	 * @throws Exception
	 */
	public List<CodeInfoVO> getCodeInfoList(String majCode) throws Exception{
		List<CodeInfoVO> list = (List<CodeInfoVO>)list("commmon.getCodeInfoList", majCode);
		return list;
	}
	/**
	 *  코드 리스트 조회
	 * @param majCode
	 * @return
	 * @throws Exception
	 */
	public List<CodeInfoVO> getCodeInfoListNoTitle(String majCode) throws Exception{
		List<CodeInfoVO> list = (List<CodeInfoVO>)list("commmon.getCodeInfoListNoTitle", majCode);
		return list;
	}
	
	/**
	 * 코드 정보조회
	 * @param hmap
	 * @return
	 * @throws Exception
	 */
	public CodeInfoVO getCodeInfo(HashMap<String, String> hmap) throws Exception{
		CodeInfoVO list = (CodeInfoVO)select("commmon.getCodeInfo", hmap);
		return list;
	}
	/**
	 * 코드 정보 조회
	 * @param codeInfo
	 * @return
	 * @throws Exception
	 */
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception{
		CodeInfoVO list = (CodeInfoVO)select("commmon.getCodeInfoForOne", codeInfo);
		return list;
	}
	/**
	 * 조직 정보 조회
	 * @param hmap
	 * @return
	 * @throws Exception
	 */
	public List<OrgGroupVO> getOrgInfoList(HashMap<String, Object> hmap) throws Exception{
		try
		{
			List<OrgGroupVO> list = (List<OrgGroupVO>)list("common.getOrgInfoList", hmap);
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 조직장 용 조직트리 조회
	 * @param hmap
	 * @return
	 * @throws Exception
	 */
	public List<OrgGroupVO> getCapOrgInfoList(String org_code) throws Exception{
		try
		{
			List<OrgGroupVO> list = (List<OrgGroupVO>)list("common.getCapOrgInfoList", org_code);
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<OrgGroupVO> getOrgInfoListForManager(String upper_org_code) throws Exception{
		try
		{
			List<OrgGroupVO> list = (List<OrgGroupVO>)list("common.getOrgInfoListForManager", upper_org_code);
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<OrgGroupVO> getUnderOrgInfoListForManager(HashMap<String,String> map) throws Exception{
		List<OrgGroupVO> list = (List<OrgGroupVO>)list("common.getUnderOrgInfoListForManager", map);
		return list;
	}
	
	public OrgGroupVO getOrgInfo(String orgCode) throws Exception{
		try
		{
			OrgGroupVO list = (OrgGroupVO)select("common.getOrgInfo", orgCode);
			return list;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public EgovMap getOrgInfoForUser(String emp_no) throws Exception{
		return (EgovMap)select("common.getOrgInfoForUser", emp_no);
	}
	
	public List<OrgGroupVO> getOrgCapInfoList(String empno) throws Exception{
		List<OrgGroupVO> list = (List<OrgGroupVO>)list("common.getOrgCapInfoList", empno);
		return list;
	}
	
	public List<EgovMap> getPolMenuAllList() throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("commmon.getPolMenuAllList");
		return list;
	}
	/**
	 * 메뉴및 정책 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolMenuAllList(HashMap<String,String> pMap) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("commmon.getPolMenuAllList", pMap);
		return list;
	}
	
	public int checkUserId(HashMap<String,String> pMap)throws Exception{
		return (int)select("login.checkUserId", pMap);
	}
	
	public int checkPassWord(HashMap<String,String> pMap)throws Exception{
		return (int)select("login.checkPassWord", pMap);
	}
	
	public void InsertMailLog(MailSendLogVO mailSendLogVO)throws Exception{
		insert("log.InsertMailLog", mailSendLogVO);
	}
	
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception{
		insert("log.InsertBPMLog", bpmLogVO);
	}
	
	public int setUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception{
		return (int)update("bpm.setUpdateApprStatusForifappr", bpmLogVO);
	}
	/**
	 * 소명처리 소명처리 결과 업데이트
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public int setPovUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception{
		return (int)update("bpm.setPovUpdateApprStatusForifappr", bpmLogVO);
	}
	
	public int setUpdateUserIdxInfoScore(BPMInfoVO bpmLogVO)throws Exception{
		return (int)update("bpm.setUpdateUserIdxInfoScore", bpmLogVO);
	}
	
	public int setUpdatePolicyFactInfo(HashMap<String, String> pMap)throws Exception{
		return (int)update("bpm.setUpdatePolicyFactInfo", pMap);
	}
	
	public int setUpdateUserIdxInfoDayInfo(HashMap<String, String> pMap)throws Exception{
		return (int)update("bpm.setUpdateUserIdxInfoDayInfo", pMap);
	}
	
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception{
		return (EgovMap)select("common.getApprLineUserInfo", pMap);
	}
	
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception{
		return (int)update("bpm.setUpdateApprBpmKey", pMap);
	}
	
	public List<LoginUserTypeVO> getLoginUserTypeInfo(HashMap<String, String> pMap) throws Exception{
		List<LoginUserTypeVO> list = (List<LoginUserTypeVO>)list("common.getLoginUserTypeInfo", pMap);
		return list;
	}
	/**
	 * 메인 상단 알림공지
	 * @return
	 * @throws Exception
	 */
	public NoticeVO getMainNoticeInfo() throws Exception{
		return (NoticeVO)select("common.getMainNoticeInfo");
	}
	/**
	 * 메인 공지 N FAQ TOP 5 List
	 * @return
	 * @throws Exception
	 */
	public List<NoticeVO> getMainNoticeNFaqListInfo(String n_type) throws Exception{
		List<NoticeVO> list = (List<NoticeVO>)list("dash.getMainNoticeNFaqListInfo", n_type);
		return list;
	}
	/**
	 * 소명완료시 제재정보 해제처리
	 * 해제시 해제 공지 업데이트
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public int setUpdateSancUserInfoReset(BPMInfoVO bpmLogVO)throws Exception{
		return (int)update("bpm.setUpdateSancUserInfoReset", bpmLogVO);
	}
	/**
	 * 제재해제처리를 위한 해제공지 조회
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public String getUserSanctBlockNotice(BPMInfoVO bpmLogVO)throws Exception{
		return (String)select("bpm.getUserSanctBlockNotice", bpmLogVO);
	}
	/**
	 * 접속로그 저장
	 * @param connectionLogVO
	 * @throws Exception
	 */
	public void setUserLoginLogoutLog(ConnectionLogVO connectionLogVO)throws Exception{
		insert("log.setUserLoginLogoutLog", connectionLogVO);
	}
	
	/**
	 * 메일전송을 위한 진단정보 조회
	 * @param mailInfoVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPolIdxInfo(MailInfoVO mailInfoVO) throws Exception{
		HashMap<String, Object> info = (HashMap<String, Object>)select("mail.getPolIdxInfo", mailInfoVO);
		return info;
	}
	
	/**
	 * 로그인실패 회수 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public EgovMap LoginFailCountInfo(HashMap<String, String> map) throws Exception{
		return (EgovMap)select("log.LoginFailCountInfo", map);
	}
	/**
	 * 로그인실패 회수 업데이트
	 * @param emp_no
	 * @return
	 */
	public int LoginFailCountUpdate(String emp_no) {
		return (int)update("log.LoginFailCountUpdate", emp_no);
	}
	/**
	 * 로그인실패 회수 저장
	 * @param emp_no
	 * @throws Exception
	 */
	public void LoginFailCountInsert(String emp_no)throws Exception{
		insert("log.LoginFailCountInsert", emp_no);
	}
	/**
	 * 로그인실패 회수 초기화(삭제)
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public int LoginFailCountDelete(String emp_no)throws Exception{
		return (int)delete("log.LoginFailCountDelete", emp_no);
	}
	/**
	 * 소명정보 조회
	 * @param apprid
	 * @return
	 * @throws Exception
	 */
	public ApprInfoVO getApprInfo(String apprid) throws Exception{
		return (ApprInfoVO)select("commmon.ApprInfo", apprid);
	}
	
	/**
	 * 협력사 조직 리스트
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public List<LoginUserTypeVO> getColGroupList(String emp_no) {
		List<LoginUserTypeVO> list = (List<LoginUserTypeVO>)list("commmon.getColGroupList", emp_no);
		return list;
	}
}
