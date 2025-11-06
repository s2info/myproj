package sdiag.member.service;
import sdiag.member.service.MemberVO;

public interface SdiagMemberService {
	public MemberVO loginMember(MemberVO memberVO) throws Exception;
	/**
	 * ORG USER 정보 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public MemberVO getOrgUserInfo(String emp_no) throws Exception;
}
