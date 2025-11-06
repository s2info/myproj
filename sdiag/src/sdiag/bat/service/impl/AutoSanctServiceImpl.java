package sdiag.bat.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.bat.service.AutoSanctService;
import sdiag.bat.service.ExplainDataVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.member.service.MemberVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.member.service.MemberVO;

@Service("AutoSanctService")
public class AutoSanctServiceImpl implements AutoSanctService {
	@Resource(name = "AutoSanctDAO")
	private AutoSanctDAO autoSanctDao;

	@Override
	public List<EgovMap> selectSanctList() throws Exception {
		// TODO Auto-generated method stub
		return autoSanctDao.selectSanctList();
	}

//	@Override
//	public List<EgovMap> getCodeInfoForOne(HashMap<String, Object> baram)
//			throws Exception {
//		return autoSanctDao.getCodeInfoForOne(baram);
//	}
	
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception{
		return autoSanctDao.getCodeInfoForOne(codeInfo);
	}	

	/**
	 * BPM 연동후 BPM Key 업데이트
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception{
		return autoSanctDao.setUpdateApprBpmKey(pMap);
	}
	
	/**
	 * 결재 라인 구성을 위한 직/차 상급자 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception{
		System.out.println("===============================================>>>>>>>>>>>>>");
		return autoSanctDao.getApprLineUserInfo(pMap);
	}
	
	/**
	 * BPM 연동 로그저장
	 * @param bpmLogVO
	 * @throws Exception
	 */
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception{
		autoSanctDao.InsertBPMLog(bpmLogVO);
	}	
	
	/**
	 * 메일 전송 로그저장
	 * @param pMap
	 * @throws Exception
	 */
	public void setInsertMailLog(MailSendLogVO mailLog) throws Exception {
		autoSanctDao.InsertMailLog(mailLog);	
	}

	/**
	 * ORG USER 정보 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public MemberVO getOrgUserInfo(String emp_no) throws Exception {
		return autoSanctDao.getOrgUserInfo(emp_no);
	}
	
	/**
	 * 자동제재 실행 로그
	 * @param pMap
	 * @throws Exception
	 */
	public void InsertAutobatLog(HashMap<String, String> pMap)throws Exception{
		autoSanctDao.InsertAutobatLog(pMap);
	}

	/**
	 * 자동메일발송용 메일리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendList() throws Exception{
		return autoSanctDao.getAutoBatMailSendList();
		
	}
	/**
	 * 자동메일발송용 메일리스트 - 테스트리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendListTest() throws Exception{
		return autoSanctDao.getAutoBatMailSendListTest();
	}
	
	/**
	 * 자동소명  발송용 리스트 조회 
	 * @return
	 * @throws Exception
	 */
	public List<ExplainDataVO> getExplainDataList() throws Exception{
		return autoSanctDao.getExplainDataList();
	}
}
