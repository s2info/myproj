package sdiag.bat.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.bat.service.ExplainDataVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.exanal.service.PolVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("AutoSanctDAO")
public class AutoSanctDAO extends EgovAbstractDAO {

	public List<EgovMap> selectSanctList() {
		try{
			List<EgovMap> list = (List<EgovMap>)list("auto.selectsanctList");
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}

	public List<EgovMap> getCodeInfoForOne(HashMap<String, Object> baram) {
		try{
			List<EgovMap> list = (List<EgovMap>)list("auto.getCodeInfoForOne", baram);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception{
		return (int)update("auto.setUpdateApprBpmKey", pMap);
	}
	
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception{
		try{
		return (EgovMap)select("auto.getApprLineUserInfo", pMap);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception{
		insert("log.InsertBPMLog", bpmLogVO);
	}
	
	public void InsertMailLog(MailSendLogVO mailLog)throws Exception{
		insert("auto.InsertMailLog", mailLog);
	}	
	
	/**
	 * ORG USER 정보 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */	
	public MemberVO getOrgUserInfo(String emp_no) throws Exception{
		return (MemberVO)select("auto.getOrgUserInfo", emp_no);
	}
	
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception{
		CodeInfoVO list = (CodeInfoVO)select("commmon.getCodeInfoForOne", codeInfo);
		return list;
	}	

	/**
	 * 자동제재 실행 로그
	 * @param pMap
	 * @throws Exception
	 */
	public void InsertAutobatLog(HashMap<String, String> pMap)throws Exception{
		insert("auto.InsertAutobatLog", pMap);
	}
	/**
	 * 자동메일발송용 메일리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendList() throws Exception{
		return (List<HashMap<String, Object>>)list("auto.getAutoBatMailSendList");
		
	}
	/**
	 * 자동메일발송용 메일리스트 - 테스트리스트
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getAutoBatMailSendListTest() throws Exception{
		return (List<HashMap<String, Object>>)list("auto.getAutoBatMailSendListTest");
		
	}
	/**
	 * 자동소명  발송용 리스트 조회 
	 * @return
	 * @throws Exception
	 */
	public List<ExplainDataVO> getExplainDataList() throws Exception{
		return (List<ExplainDataVO>)list("auto.getExplainDataList");
		
	}
}
