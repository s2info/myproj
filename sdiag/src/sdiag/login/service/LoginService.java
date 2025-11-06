package sdiag.login.service;

import java.util.List;

import sdiag.login.service.UserManageVO;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 인터페이스 클래스
 * @author 공통서비스 개발
 * @since 2015.10.12
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2015.10.12  CJLee          최초 생성 
 *  
 *  </pre>
 */
public interface LoginService {
	
	/**
	 * 관리자 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	UserManageVO actionLoginAdmin(UserManageVO vo) throws Exception;	
	
	/**
	 * 일반 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	UserManageVO actionLoginPersonal(UserManageVO vo) throws Exception;
    
}
