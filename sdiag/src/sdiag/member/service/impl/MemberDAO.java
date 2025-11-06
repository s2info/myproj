package sdiag.member.service.impl;
import org.springframework.stereotype.Repository;

import sdiag.man.service.SearchVO;
import sdiag.member.service.MemberVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("memberDAO")
public class MemberDAO extends EgovAbstractDAO{
	/**
	 * 입력된 정보로 데이터베이스에 접근하여 회원 여부를 확인하고, 
	 * 회원일 경우 회원의 정보를 가져온다.
	 * @param vo 회원정보
	 * @return MemberVO 회원정보
	 */
	public MemberVO checkMember(MemberVO vo){
		return (MemberVO)select("common.getMember", vo);
	}
	/**
	 * ORG USER 정보 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public MemberVO getOrgUserInfo(String emp_no) throws Exception{
		return (MemberVO)select("user.getOrgUserInfo", emp_no);
	} 
}
