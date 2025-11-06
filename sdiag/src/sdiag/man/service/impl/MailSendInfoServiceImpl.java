package sdiag.man.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.man.service.MailSendInfoService;
import sdiag.man.vo.MailPolicyInfoVO;
import sdiag.man.vo.MailSearchVO;
import sdiag.man.vo.MailSendInfoVO;
import sdiag.man.vo.MailTargetInfoVO;

@Service("MailSendInfoService")
public class MailSendInfoServiceImpl implements MailSendInfoService {
	
	@Resource(name = "MailSendInfoDAO")
	private MailSendInfoDAO dao;
	
	/**
	 * 메일 전송 정보 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getMailSendInfoList(MailSearchVO searchVO) throws Exception{
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		List<MailSendInfoVO> list = dao.getMailSendInfoList(searchVO);
		rMap.put("list", list);
		int totalCount = dao.getMailSendInfoListTotalCount(searchVO);
		rMap.put("totalCount", totalCount);
		return rMap;
		
	}
	/**
	 * 메앨 전송 정보 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public MailSendInfoVO getMailSendInfo(MailSearchVO searchVO) throws Exception{
		return dao.getMailSendInfo(searchVO);
	}
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailSendTargetList(MailSearchVO searchVO) throws Exception{
		return dao.getMailSendTargetList(searchVO);
	}
	/**
	 * 메일 발송 정책 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailPolicyInfoVO> getMailSendPolicyList(MailSearchVO searchVO) throws Exception{
		return dao.getMailSendPolicyList(searchVO);
	}
	/**
	 * 메일 전송 정보 저장
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public long setMailSendInfoModify(MailSendInfoVO mailSendInfoVO) throws Exception{
		try{
			long mail_seq = mailSendInfoVO.getMail_seq();
			int upCnt = dao.setMailSendInfoUpdate(mailSendInfoVO);
			if(upCnt <= 0){
				mail_seq = dao.getMailSendInfoMaxSeq();
				mailSendInfoVO.setMail_seq(mail_seq);
				dao.setMailSendInfoInsert(mailSendInfoVO);
			}
			dao.setMailSendTargetDelete(mail_seq);
			if(mailSendInfoVO.getMailTargetList() != null){
				dao.setMailSendTargetInsert(mailSendInfoVO);
			}
			dao.setMailSendPolicyDelete(mail_seq);
			if(mailSendInfoVO.getMailPolicyList() != null){
				dao.setMailSendPolicyInsert(mailSendInfoVO);
			}
			return mail_seq;
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}
	}
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailTargetUserList(MailSearchVO searchVO) throws Exception{
		return dao.getMailTargetUserList(searchVO);
	}
	/**
	 * 메일 발송 대상 전체 Count
	 * @return
	 * @throws Exception
	 */
	public long getMailTargetUserListTotalCount(MailSearchVO searchVO)throws Exception{
		return dao.getMailTargetUserListTotalCount(searchVO);
	}
	/**
	 * 메일 첨부파일 다운로드
	 * @return
	 * @throws Exception
	 */
	public String getFileName(String type) throws Exception {
		return dao.getFileName(type);
	}
}
