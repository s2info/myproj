package sdiag.login.web;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import sdiag.login.service.UserManageVO;
import sdiag.login.service.LoginService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;

import nets.ldap.ADUtilSSL; //

/**
 * 일반 로그인, 인증서 로그인을 처리하는 컨트롤러 클래스
 * @author  이창재
 * @since 2015.10.12
 * @version 1.0
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
@Controller
public class LoginController extends CommonController {
	
	
    /** LoginService */
	@Resource(name = "loginService")
    private LoginService loginService;

	/*
	@RequestMapping(value="/loginPage.do")
	public String loginPage(HttpServletRequest request, ModelMap model) throws Exception{	
		
		/*Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (isAuthenticated) {
	        return "redirect:/main/main.do";
		}else{
			return "loginPage";
		}
		return "loginPage";
	}
	*/
	/*
	@RequestMapping(value="/mainPage.do")
	public String mianPage(HttpServletRequest request, ModelMap model) throws Exception{	
		
		return "main/main";
	}
	*/
    /**
	 * 일반(스프링 시큐리티) 로그인을 처리한다
	 * @param vo - 아이디, 비밀번호가 담긴 LoginVO
	 * @param request - 세션처리를 위한 HttpServletRequest
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	/*
    @RequestMapping(value="/main/LoginSecurity.do")
    public String actionSecurityLogin(@ModelAttribute("loginVO") UserManageVO loginVO, 
    		                   HttpServletRequest request,
    		                   ModelMap model)
            throws Exception {

    	try {
			// 접속IP
			//String userIp = getUserIp(request);
			//log.debug("userIp : " + userIp);
    		
    		String userid = request.getParameter("userid");
    		String password = request.getParameter("password");
    		loginVO.setuserid(userid);
    		loginVO.setpassword(password);
    		
    		boolean loginPolicyYn = false;
    		
    		UserManageVO resultVO = new UserManageVO();
    		
    		// 0. 관리자 로그인 처리
    		resultVO = loginService.actionLoginAdmin(loginVO);

			// 1. 일반 로그인 처리 & Ldap check
    		if (resultVO.getEmp_no() == null){
    			resultVO = loginService.actionLoginPersonal(loginVO);
    		}
			
			if (resultVO.getEmp_no() != null ){ //&& !"".equals(resultVO.getip())) {
				loginPolicyYn = true;
			}else{
				loginPolicyYn = false;
			}
			
			if (resultVO != null && loginPolicyYn) {
				//2. Session 연동
				request.getSession().setAttribute("LoginVO", resultVO);
			    return "forward:/main/actionMain.do";	
			} else {
				return "loginPage";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
        
        
    }
    */
    /**
	 * 로그인 후 메인화면으로 들어간다
	 * @param 
	 * @return 로그인 페이지
	 * @exception Exception
	 */
    /*
    @RequestMapping(value="/main/actionMain.do")
	public String actionMain(ModelMap model,
    								HttpServletRequest request)
			throws Exception {
    	String returnUrl = "";
    	// 1. Spring Security 사용자권한 처리

    	returnUrl = "redirect:/dash/dashboard.do";
    	
		// 2. 메인 페이지 이동

    	 return returnUrl;
	}
    */
    /**
 	 * 로그아웃한다.
 	 * @return String
 	 * @exception Exception
 	 */
    /*
     @RequestMapping(value="/logout.do")
 	public String actionLogout(HttpServletRequest request, ModelMap model) 
 			throws Exception {
     	try{
     	    request.getSession().setAttribute("LoginVO", null);
     	    String userId = getUserId(request);
     	    if(!"".equals(userId)){
     	    	log.debug("=====================loginVO.getuserId : "+userId);
     	    }else {
     	    	log.debug("=====================loginVO is null ");
     	    }
 		} catch (Exception e) {
 			// 1. Security 연동
 			//leaveaTrace.trace("fail.common.msg", this.getClass());
 		}
 	    return "forward:/loginPage.do";
     }   
     */
}
