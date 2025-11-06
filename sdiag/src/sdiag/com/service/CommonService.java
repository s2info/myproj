package sdiag.com.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import sdiag.board.service.NoticeVO;
import sdiag.pol.service.OrgGroupVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface CommonService {
	
	/**
	 * 조직 상위 리스트 조회
	 * @param orgcode
	 * @return
	 * @throws Exception
	 */
	public String getUpperOrgNames(String orgcode) throws Exception;
	
	public List<MenuItemVO> getSolMenuList() throws Exception;
	/**
	 * 메뉴구성을 위한 진단항목조회 미사용포함
	 * @return
	 * @throws Exception
	 */
	public List<MenuItemVO> getSolAllMenuList() throws Exception;
	public List<EgovMap> getPolMenuList(HashMap<String, String> hmap) throws Exception;
	public EgovMap getPolMenuNaviString(HashMap<String, String> hmap) throws Exception;
	public List<CodeInfoVO> getCodeInfoList(String majCode) throws Exception;
	public List<CodeInfoVO> getCodeInfoListNoTitle(String majCode) throws Exception;
	/**
	 * 코드정보 조회
	 * @param hmap : majCode, minCode
	 * @return
	 * @throws Exception
	 */
	public CodeInfoVO getCodeInfo(HashMap<String, String> hmap) throws Exception;
	/**
	 * 코드정보조회
	 * @param codeInfo
	 * @return
	 * @throws Exception
	 */
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception;
	public List<OrgGroupVO> getOrgInfoList(HashMap<String, Object> hmap) throws Exception;
	/**
	 * 조직장 용 조직트리 조회
	 * @param org_code
	 * @return
	 * @throws Exception
	 */
	public List<OrgGroupVO> getCapOrgInfoList(String org_code) throws Exception;
	public List<OrgGroupVO> getOrgInfoListForManager(String upper_org_code) throws Exception;
	public List<OrgGroupVO> getUnderOrgInfoListForManager(HashMap<String, String> map) throws Exception;
	public EgovMap getOrgInfoForUser(String emp_no) throws Exception;
	public OrgGroupVO getOrgInfo(String orgCode) throws Exception;
	public List<OrgGroupVO> getOrgCapInfoList(String empno) throws Exception;
	public List<EgovMap> getPolMenuAllList() throws Exception;
	/**
	 * 메뉴및 정책 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolMenuAllList(HashMap<String,String> pMap) throws Exception;
	/**
	 * 로그인 Process
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> loginProcess(HttpServletRequest request) throws Exception;
	/**
	 * 메일 전송 로그 저장
	 * @param mailSendLogVO
	 * @throws Exception
	 */
	public void setInsertMailLog(MailSendLogVO mailSendLogVO)throws Exception;
	/**
	 * BPM 연동로그 저장
	 * @param bpmLogVO
	 * @throws Exception
	 */
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception;
	/**
	 * BPM 결재 정보 업데이트
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public int setUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception;
	/**
	 * 소명처리 소명처리 결과 업데이트
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public int setPovUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception;
	/**
	 * BPM 결재 승인 완료 처리
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public boolean setUpdateApprBPMCompleteInfo(BPMInfoVO bpmLogVO)throws Exception;
	/**
	 * 결재 라인 구성을 위한 직/차 상급자 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception;
	/**
	 * BPM Key 업데이트 
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception;
	
	/**
	 * 로그인 사용자 권한별 조회 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public List<LoginUserTypeVO> getLoginUserTypeInfo(String userid, String roleCode) throws Exception;
	/**
	 * 메인 상단 알림공지
	 * @return
	 * @throws Exception
	 */
	public NoticeVO getMainNoticeInfo() throws Exception;
	/**
	 * 메인 공지 N FAQ TOP 5 List
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getMainNoticeNFaqListInfo() throws Exception;
	/**
	 * 접속로그 저장
	 * @param connectionLogVO
	 * @throws Exception
	 */
	public void setUserLoginLogoutLog(HttpServletRequest request, String logType)throws Exception;
	/**
	 * 메일전송을 위한 진단정보 조회
	 * @param mailInfoVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPolIdxInfo(MailInfoVO mailInfoVO) throws Exception;
	/**
	 * 로그인실패 회수 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public EgovMap LoginFailCountInfo(HashMap<String, String> map) throws Exception;
	/**
	 * 로그인실패 회수 초기화(삭제)
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public int LoginFailCountDelete(String emp_no)throws Exception;
	/**
	 * 로그인실패 회수 저장
	 * @param emp_no
	 * @throws Exception
	 */
	public boolean LoginFailCountInsert(String emp_no)throws Exception;
	/**
	 * 소명정보 조회
	 * @param apprid
	 * @return
	 * @throws Exception
	 */
	public ApprInfoVO getApprInfo(String apprid) throws Exception;

	/**
	 * 협력사 조직 리스트
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public List<LoginUserTypeVO> getColGroupList(String emp_no) throws Exception;
}
