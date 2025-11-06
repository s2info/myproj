package sdiag.man.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.man.vo.MailPolicyInfoVO;
import sdiag.man.vo.MailSearchVO;
import sdiag.man.vo.MailSendInfoVO;
import sdiag.man.vo.MailTargetInfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("MailSendInfoDAO")
public class MailSendInfoDAO extends EgovAbstractDAO{
	/**
	 * 메일 전송 정보 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailSendInfoVO> getMailSendInfoList(MailSearchVO searchVO) throws Exception{
		List<MailSendInfoVO> list = (List<MailSendInfoVO>)list("mail.getMailSendInfoList", searchVO);
		return list;
	}
	/**
	 * 메일 전송 정보 리스트 Count 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getMailSendInfoListTotalCount(MailSearchVO searchVO) throws Exception{
		return (int)select("mail.getMailSendInfoListTotalCount", searchVO);
	}
	/**
	 * 메앨 전송 정보 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public MailSendInfoVO getMailSendInfo(MailSearchVO searchVO) throws Exception{
		MailSendInfoVO info = (MailSendInfoVO)select("mail.getMailSendInfo", searchVO);
		return info;
	}
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailSendTargetList(MailSearchVO searchVO) throws Exception{
		List<MailTargetInfoVO> list = (List<MailTargetInfoVO>)list("mail.getMailSendTargetList", searchVO);
		return list;
	}
	/**
	 * 메일 발송 정책 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailPolicyInfoVO> getMailSendPolicyList(MailSearchVO searchVO) throws Exception{
		List<MailPolicyInfoVO> list = (List<MailPolicyInfoVO>)list("mail.getMailSendPolicyList", searchVO);
		return list;
	}
	/**
	 * 메일 발송 Seq
	 * @return
	 * @throws Exception
	 */
	public long getMailSendInfoMaxSeq()throws Exception{
		return (long)select("mail.getMailSendInfoMaxSeq");
	}
	/**
	 * 메일 발송 정보 Insert
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public Object setMailSendInfoInsert(MailSendInfoVO mailSendInfoVO)throws Exception{
		return insert("mail.setMailSendInfoInsert", mailSendInfoVO);
	}
	/**
	 * 메일 발송 정보 UPdate
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public int setMailSendInfoUpdate(MailSendInfoVO mailSendInfoVO)throws Exception{
		return (int)update("mail.setMailSendInfoUpdate", mailSendInfoVO);
	}
	/**
	 * 메일 발송 대상 리스트 삭제
	 * @param searchVO 
	 * @return
	 * @throws Exception
	 */
	public int setMailSendTargetDelete(long mail_seq) throws Exception{
		return (int)delete("mail.setMailSendTargetDelete", mail_seq);
	}
	/**
	 * 메일 발송 대상 저장
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public Object setMailSendTargetInsert(MailSendInfoVO mailSendInfoVO) throws Exception{
		return insert("mail.setMailSendTargetInsert", mailSendInfoVO);
	}
	/**
	 * 메일 발송 정책 리스트 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int setMailSendPolicyDelete(long mail_seq) throws Exception{
		return (int)delete("mail.setMailSendPolicyDelete", mail_seq);
	}
	/**
	 * 메일 발송 정책 저장
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public Object setMailSendPolicyInsert(MailSendInfoVO mailSendInfoVO) throws Exception{
		return insert("mail.setMailSendPolicyInsert", mailSendInfoVO);
	}
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailTargetUserList(MailSearchVO searchVO) throws Exception{
		List<MailTargetInfoVO> list = (List<MailTargetInfoVO>)list("mail.getMailTargetUserList", searchVO);
		return list;
	}
	/**
	 * 메일 발송 대상 전체 Count
	 * @return
	 * @throws Exception
	 */
	public long getMailTargetUserListTotalCount(MailSearchVO searchVO) throws Exception{
		return (long)select("mail.getMailTargetUserListTotalCount", searchVO);
	}
	/**
	 * 메일 첨부파일 다운로드
	 * @return
	 * @throws Exception
	 */
	public String getFileName(String type) throws Exception{
		return (String)select("mail.getFileName", type);
	}
}
