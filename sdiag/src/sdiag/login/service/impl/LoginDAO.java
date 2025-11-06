package sdiag.login.service.impl;

import java.util.List;

import sdiag.login.service.UserManageVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 DAO 클래스
 * @author 공통서비스 보안수준진단 개발팀
 * @since 2015.10.12
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2015.10.12   CJLee          최초 생성 
 *  
 *  </pre>
 */
@Repository("loginDAO")
public class LoginDAO extends EgovAbstractDAO{
	
	/** log */
    protected static final Log LOG = LogFactory.getLog(LoginDAO.class);
    
	/**
	 * 관리자/운영자 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public UserManageVO actionLoginAdmin(UserManageVO vo) throws Exception {
    	return (UserManageVO)select("loginDAO.actionLogin.admin", vo);
    } 
    
	/**
	 * 일반 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public UserManageVO actionLoginPersonal(UserManageVO vo) throws Exception {
    	UserManageVO user = (UserManageVO)select("loginDAO.actionLogin.personal", vo);
    	System.out.println(user.getEmp_no() + ":empno");
    	return user;//(UserManageVO)select("loginDAO.actionLogin.personal", vo);
    }     
	
}
