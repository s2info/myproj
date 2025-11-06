package sdiag.member.service.impl;

import sdiag.member.service.impl.MemberDAO;

import javax.annotation.Resource;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sdiag.member.service.MemberVO;
import sdiag.member.service.SdiagMemberService;

@Service("memberService")
public class SdiagMemberServiceImpl  implements SdiagMemberService {
	/** MemberDAO */
	@Resource(name="memberDAO")
	private MemberDAO memberDAO;  //회원정보 관련 데이터베이스 접근 클래스
	
	/**
	 * 입력된 정보로 데이터베이스에 접근하여 회원 여부를 확인하고, 회원일 경우 로그인 후 메인화면으로 이동한다.
	 * @param MemberVO 회원번호
	 * @return MemberVO 회원번호
	 * @throws Exception 
	 */
	public MemberVO loginMember(MemberVO memberVO) throws Exception {
		
		// 1. 입력한 비밀번호를 암호화한다.
	    ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		encoder.setEncodeHashAsBase64(true);
		
	    String hashedPass = encoder.encodePassword(memberVO.getPassword(), null);
	    memberVO.setPassword(hashedPass);
	    
        MemberVO loginVO = memberDAO.checkMember(memberVO);
        
        if (loginVO != null && !loginVO.getUserid().equals("")
                && !loginVO.getPassword().equals("")) {
                return loginVO;
            } else {
                loginVO = new MemberVO();
          }
		return loginVO;
	}
	/**
	 * ORG USER 정보 조회
	 * @param emp_no
	 * @return
	 * @throws Exception
	 */
	public MemberVO getOrgUserInfo(String emp_no) throws Exception{
		return memberDAO.getOrgUserInfo(emp_no);
	}
}
