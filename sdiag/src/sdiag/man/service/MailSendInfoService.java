package sdiag.man.service;

import java.util.HashMap;
import java.util.List;

import sdiag.man.vo.MailPolicyInfoVO;
import sdiag.man.vo.MailSearchVO;
import sdiag.man.vo.MailSendInfoVO;
import sdiag.man.vo.MailTargetInfoVO;

public interface MailSendInfoService {
	/**
	 * 메일 전송 정보 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getMailSendInfoList(MailSearchVO searchVO) throws Exception;
	/**
	 * 메앨 전송 정보 
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public MailSendInfoVO getMailSendInfo(MailSearchVO searchVO) throws Exception;
	/**
	 * 메일 전송 정보 저장
	 * @param mailSendInfoVO
	 * @return
	 * @throws Exception
	 */
	public long setMailSendInfoModify(MailSendInfoVO mailSendInfoVO) throws Exception;
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailSendTargetList(MailSearchVO searchVO) throws Exception;
	/**
	 * 메일 발송 정책 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailPolicyInfoVO> getMailSendPolicyList(MailSearchVO searchVO) throws Exception;
	/**
	 * 메일 발송 대상 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<MailTargetInfoVO> getMailTargetUserList(MailSearchVO searchVO) throws Exception;
	/**
	 * 메일 발송 대상 전체 Count
	 * @return
	 * @throws Exception
	 */
	public long getMailTargetUserListTotalCount(MailSearchVO searchVO)throws Exception;
	
	/**
	 * 메일 첨부 파일 다운로드
	 * @return
	 * @throws Exception
	 */
	public String getFileName(String type) throws Exception;
}
