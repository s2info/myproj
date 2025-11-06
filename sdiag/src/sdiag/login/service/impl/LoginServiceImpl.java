package sdiag.login.service.impl;

import java.util.List;
import javax.annotation.Resource;

import sdiag.login.service.LoginService;
import sdiag.login.service.UserManageVO;

import org.springframework.stereotype.Service;

//import egovframework.let.utl.fcc.service.EgovNumberUtil;
//import egovframework.let.utl.fcc.service.EgovStringUtil;
//import egovframework.let.utl.sim.service.EgovFileScrty;
//import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
//import egovframework.let.ems.service.EgovSndngMailRegistService;
//import egovframework.let.ems.service.SndngMailVO;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 구현 클래스
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
 *  2009.03.06  CJLEE          최초 생성 
 *  
 *  </pre>
 */
@Service("loginService")
public class LoginServiceImpl implements
        LoginService {

    @Resource(name="loginDAO")
    private LoginDAO loginDAO;
    
    /**
	 * 관리자/운영자 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public UserManageVO actionLoginAdmin(UserManageVO vo) throws Exception {
    	
    	// 1. 입력한 비밀번호를 암호화한다.
    	//String enpassword = EgovFileScrty.encryptPassword(vo.getPasswd());
    	//vo.setPasswd(enpassword);
    	
    	// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
    	UserManageVO loginVO = loginDAO.actionLoginAdmin(vo);
    	
    	// 3. 결과를 리턴한다.
    	if (loginVO != null && !loginVO.getuserid().equals("") && !loginVO.getpassword().equals("")) {
    		return loginVO;
    	} else {
    		loginVO = new UserManageVO();
    	}
    	
    	return loginVO;
    }
    
    /**
	 * 일반 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public UserManageVO actionLoginPersonal(UserManageVO vo) throws Exception {
    	
    	// 1. 입력한 비밀번호를 암호화한다.
    	//String enpassword = EgovFileScrty.encryptPassword(vo.getPasswd());
    	//vo.setPasswd(enpassword);
    	
    	// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
    	UserManageVO loginVO = loginDAO.actionLoginPersonal(vo);
    	
    	// 3. 결과를 리턴한다.
    	if (loginVO != null && !loginVO.getEmp_no().equals("")) {
    		return loginVO;
    	} else {
    		loginVO = new UserManageVO();
    	}
    	
    	return loginVO;
    }    
    
    
}
