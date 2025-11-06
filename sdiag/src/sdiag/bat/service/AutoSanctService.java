package sdiag.bat.service;

import java.util.HashMap;
import java.util.List;

import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.member.service.MemberVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface AutoSanctService {

	public List<EgovMap> selectSanctList() throws Exception;

	//public List<EgovMap> getCodeInfoForOne(HashMap<String, Object> baram) throws Exception;
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception;

	
	/**
	 * BPM Key 업데이트 
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception;	

	/**
	 * 결재 라인 구성을 위한 직/차 상급자 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception;
	
	/**
	 * BPM 연동로그 저장
	 * @param bpmLogVO
	 * @throws Exception
	 */
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception;	
	
	/**
	 * 메일 전송 로그 저장
	 * @param mailSendLogVO
	 * @throws Exception
	 */
	//public void setInsertMailLog(HashMap<String, String> pMap)throws Exception;
	
	public void setInsertMailLog(MailSendLogVO mailLog)throws Exception;

	public MemberVO getOrgUserInfo(String empno) throws Exception;	
	/**
	 * 자동제재 실행 로그
	 * @param pMap
	 * @throws Exception
	 */
	public void InsertAutobatLog(HashMap<String, String> pMap)throws Exception;
	/**
	 * 자동메일발송용 메일리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendList() throws Exception;
	/**
	 * 자동메일발송용 메일리스트 - 테스트리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendListTest() throws Exception;
	/**
	 * 자동소명  발송용 리스트 조회 
	 * @return
	 * @throws Exception
	 */
	public List<ExplainDataVO> getExplainDataList() throws Exception;
	
}
