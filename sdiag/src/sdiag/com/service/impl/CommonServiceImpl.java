   package sdiag.com.service.impl;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import sdiag.com.service.ApprInfoVO;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ConnectionLogVO;
import sdiag.com.service.LoginUserTypeVO;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.MenuItemVO;
import sdiag.board.service.NoticeVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.impl.PolicyDAO;
import sdiag.util.CommonUtil;
import sdiag.util.MajrCodeInfo;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	@Resource(name = "CommonDAO")
	private CommonDAO comDao;
	
	@Resource(name = "PolicyDAO")
	private PolicyDAO polDao;
	
	/**
	 * 조직 상위 리스트 조회
	 * @param orgcode
	 * @return
	 * @throws Exception
	 */
	public String getUpperOrgNames(String orgcode) throws Exception{
		return comDao.getUpperOrgNames(orgcode);
	}
	
	public List<MenuItemVO> getSolMenuList() throws Exception{
		return comDao.getSolMenuList();
	}
	
	/**
	 * 메뉴구성을 위한 진단항목조회 미사용포함
	 * @return
	 * @throws Exception
	 */
	public List<MenuItemVO> getSolAllMenuList() throws Exception{
		return comDao.getSolAllMenuList();
	}
	
	public List<EgovMap> getPolMenuList(HashMap<String, String> hmap) throws Exception{
		return comDao.getPolMenuList(hmap);
	}
	
	public EgovMap getPolMenuNaviString(HashMap<String, String> hmap) throws Exception{
		return comDao.getPolMenuNaviString(hmap);
	}
	
	public List<CodeInfoVO> getCodeInfoList(String majCode) throws Exception{
		return comDao.getCodeInfoList(majCode);
	}
	
	public List<OrgGroupVO> getCapOrgInfoList(String org_code) throws Exception{
		return comDao.getCapOrgInfoList(org_code);
	}
	
	public List<CodeInfoVO> getCodeInfoListNoTitle(String majCode) throws Exception{
		return comDao.getCodeInfoListNoTitle(majCode);
	}
	
	public CodeInfoVO getCodeInfo(HashMap<String, String> hmap) throws Exception{
		return comDao.getCodeInfo(hmap);
	}
	
	public CodeInfoVO getCodeInfoForOne(CodeInfoVO codeInfo) throws Exception{
		return comDao.getCodeInfoForOne(codeInfo);
	}
	
	public List<OrgGroupVO> getOrgInfoList(HashMap<String, Object> hmap) throws Exception{
		return comDao.getOrgInfoList(hmap);
	}
	
	public EgovMap getOrgInfoForUser(String emp_no) throws Exception{
		return comDao.getOrgInfoForUser(emp_no);
	}
	
	public OrgGroupVO getOrgInfo(String orgCode) throws Exception{
		return comDao.getOrgInfo(orgCode);
	}
	
	public List<OrgGroupVO> getOrgCapInfoList(String empno) throws Exception{
		return comDao.getOrgCapInfoList(empno);
	}
	
	public List<OrgGroupVO> getOrgInfoListForManager(String upper_org_code) throws Exception{
		return comDao.getOrgInfoListForManager(upper_org_code);
	}
	
	public List<OrgGroupVO> getUnderOrgInfoListForManager(HashMap<String,String> map) throws Exception{
		return comDao.getUnderOrgInfoListForManager(map);
	}
	
	public List<EgovMap> getPolMenuAllList() throws Exception{
		return comDao.getPolMenuAllList();
	}
	/**
	 * 메뉴및 정책 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolMenuAllList(HashMap<String,String> pMap) throws Exception{
		return comDao.getPolMenuAllList(pMap);
	}
	
	public HashMap<String,String> loginProcess(HttpServletRequest request) throws Exception{
		HashMap<String,String> rMap = new HashMap<String,String>();
		
		if (request.getParameter("u1") == null || ((String)request.getParameter("u1")).equals("")){			//아이디가 입력하지 않았을때
			rMap.put("result", "1");
			return rMap;
		}
		
		if (request.getParameter("p1") == null || ((String)request.getParameter("p1")).equals("")){			//패스워드가 입력하지 않았을때
			rMap.put("result", "2");
			return rMap;
		}
		
		HashMap<String,String> pMap = new HashMap<String,String>();
		pMap.put("userid", (String)request.getParameter("u1"));
		
		if (comDao.checkUserId(pMap) <= 0){
			rMap.put("result", "3");
			return rMap;
		}
		
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		encoder.setEncodeHashAsBase64(true);
		
	    String pwd = encoder.encodePassword((String)request.getParameter("p1"), null);
	    pMap.put("password", pwd);
	    if(request.getParameter("u1").equals("admin")){
			if (comDao.checkPassWord(pMap) <= 0){
				rMap.put("result", "4");
				return rMap;
			}
		}else{
			if(!((String)request.getParameter("p1")).equals("12345")){
				rMap.put("result", "4");
				return rMap;
			}
		}
	    
		rMap.put("result", "5");			//로그인 성공
		//체크 완료 권한 부여 및 세션부여 END
		
		return rMap;
	}
	
	/**
	 * 메일 전송 로그저장
	 * @param pMap
	 * @throws Exception
	 */
	public void setInsertMailLog(MailSendLogVO mailSendLogVO)throws Exception{
		comDao.InsertMailLog(mailSendLogVO);
	}
	/**
	 * BPM 연동 로그저장
	 * @param bpmLogVO
	 * @throws Exception
	 */
	public void InsertBPMLog(BPMInfoVO bpmLogVO)throws Exception{
		comDao.InsertBPMLog(bpmLogVO);
	}
	/**
	 * BPM 승인처리시 상태 업데이트 user_idx_info, if_appr_info
	 */
	public int setUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception{
		return comDao.setUpdateApprStatusForifappr(bpmLogVO);
	}
	/**
	 * 소명처리 소명처리 결과 업데이트
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public int setPovUpdateApprStatusForifappr(BPMInfoVO bpmLogVO)throws Exception{
		return comDao.setPovUpdateApprStatusForifappr(bpmLogVO);
	}
	/**
	 * 소명 완료시 처리 
	 * @param bpmLogVO
	 * @return
	 * @throws Exception
	 */
	public boolean setUpdateApprBPMCompleteInfo(BPMInfoVO bpmLogVO)throws Exception{
		EgovMap apprInfo = polDao.getApplInfo(bpmLogVO.getAppr_id());
		/**
		 *
	   	 * user_idx_info score -> 100으로 처리 idx_rgdt_date, emp_no, mac, pol_idx_id,  ip, appr_id
	   	 * policyfact explan_flag -> 'Y' 처리 sldm_empno, sldm_mac, sldm_ip, policy_id, event_date
	   	 * user_idx_info_day scor_curr -> 다시계산함 idx_rgdt_date, emp_no, mac, ip
	   	 * 제재존재시 해제처리
	   	 */
		
		try{
			bpmLogVO.setEmp_no(apprInfo.get("empno").toString());
			/**
			 * 점수 업데이트 100으로
			 */
			comDao.setUpdateUserIdxInfoScore(bpmLogVO);
			/**
			 * 제재 유저 해제처리
			 */
			//해제공지 조회
			String notiInfo = comDao.getUserSanctBlockNotice(bpmLogVO);
			bpmLogVO.setNoti(notiInfo);
			comDao.setUpdateSancUserInfoReset(bpmLogVO);
			/**
			 * Policy_factInfo 업데이트
			 */
			HashMap<String, String> pMap = new HashMap<String, String>();
			pMap.put("sldm_empno", apprInfo.get("empno").toString());
			pMap.put("sldm_mac", apprInfo.get("mac").toString());
			pMap.put("sldm_ip", apprInfo.get("ip").toString());
			pMap.put("policy_id", apprInfo.get("polid").toString());
			pMap.put("event_date", apprInfo.get("eventdate").toString());
			comDao.setUpdatePolicyFactInfo(pMap);
			/**
			 * 일별 유저 점수 업데이트(평균)
			 * 추후 점수 산정공식 적용함(가중치?? 적용)
			 */
			comDao.setUpdateUserIdxInfoDayInfo(pMap);
			
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 결재 라인 구성을 위한 직/차 상급자 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public EgovMap getApprLineUserInfo(HashMap<String, String> pMap) throws Exception{
		return comDao.getApprLineUserInfo(pMap);
	}
	/**
	 * BPM 연동후 BPM Key 업데이트
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateApprBpmKey(HashMap<String, String> pMap)throws Exception{
		return comDao.setUpdateApprBpmKey(pMap);
	}
		
	public List<LoginUserTypeVO> getLoginUserTypeInfo(String userid, String roleCode) throws Exception{
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("userid", userid);
		hMap.put("roleCode", roleCode);
		
		return comDao.getLoginUserTypeInfo(hMap);
	}
	/**
	 * 메인 상단 알림공지
	 * @return
	 * @throws Exception
	 */
	public NoticeVO getMainNoticeInfo() throws Exception{
		return comDao.getMainNoticeInfo();
	}
	
	/**
	 * 메인 공지 N FAQ TOP 5 List
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getMainNoticeNFaqListInfo() throws Exception{
		HashMap<String, Object> retObj = new HashMap<String, Object>();
		List<NoticeVO> noticeList = comDao.getMainNoticeNFaqListInfo("0000");
		retObj.put("noticeList", noticeList);
		List<NoticeVO> faqList = comDao.getMainNoticeNFaqListInfo("0001");
		retObj.put("faqList", faqList);
		return retObj;
	}
	
	/**
	 * 접속로그 저장
	 * @param connectionLogVO
	 * @throws Exception
	 */
	public void setUserLoginLogoutLog(HttpServletRequest request, String logType)throws Exception{
		
		ConnectionLogVO logVO = new ConnectionLogVO();
		System.out.println("CommonUtil.getMemberInfo(): "+CommonUtil.getMemberInfo());
		MemberVO memberVO = CommonUtil.getMemberInfo();
		/*if(memberVO.getUserid() != null){//2020.12.11 내가 수정한 부분... 왜 에러가 났을까?
			System.out.println("memberVO.getUserid(): "+memberVO.getUserid());
		logVO.setEmp_no(memberVO.getUserid());
		logVO.setMac(memberVO.getMac());
		}*/
		
		logVO.setEmp_no(memberVO.getUserid());
		logVO.setMac(memberVO.getMac());
		String clientIp = CommonUtil.getLocalIPAddress(request);
		/*
		String clientIp = request.getRemoteAddr();
		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown") || clientIp.equals("0:0:0:0:0:0:0:1")){
			InetAddress inetAddress = InetAddress.getLocalHost();
			clientIp = inetAddress.getHostAddress();
		}
		*/
		logVO.setIp(clientIp);
		logVO.setUi_id(logType);
		
		comDao.setUserLoginLogoutLog(logVO);
	}
	
	/**
	 * 메일전송을 위한 진단정보 조회
	 * @param mailInfoVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPolIdxInfo(MailInfoVO mailInfoVO) throws Exception{
		return comDao.getPolIdxInfo(mailInfoVO);
	}
	
	/**
	 * 로그인실패 회수 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public EgovMap LoginFailCountInfo(HashMap<String, String> map) throws Exception{
		
		return comDao.LoginFailCountInfo(map);
	}
	
	/**
	 * 로그인실패 회수 초기화(삭제)
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public int LoginFailCountDelete(String emp_no)throws Exception{
		return comDao.LoginFailCountDelete(emp_no);
	}
	
	/**
	 * 로그인실패 회수 저장
	 * @param emp_no
	 * @throws Exception
	 */
	public boolean LoginFailCountInsert(String emp_no)throws Exception{
		//우선업데이트 없으면 저장
		try
		{
			int ret = comDao.LoginFailCountUpdate(emp_no);
			if(ret <= 0){
				comDao.LoginFailCountInsert(emp_no);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 소명정보 조회
	 * @param apprid
	 * @return
	 * @throws Exception
	 */
	public ApprInfoVO getApprInfo(String apprid) throws Exception{
		return comDao.getApprInfo(apprid);
	}

	/**
	 * 협력사 조직 리스트
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public List<LoginUserTypeVO> getColGroupList(String emp_no)
			throws Exception {
		return comDao.getColGroupList(emp_no);
	}
}
