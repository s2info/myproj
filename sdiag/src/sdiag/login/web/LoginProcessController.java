package sdiag.login.web;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.login.service.LoginService;
import sdiag.login.service.UserManageVO;
import sdiag.board.service.NoticeVO;
import sdiag.member.service.MemberVO;
import sdiag.util.CommonUtil;
import sdiag.util.LDAPUtil;
import sdiag.util.MajrCodeInfo;
import sdiag.util.RequestWrapperForSecurity;
import nets.ldap.ADUtilSSL; //

@Controller
public class LoginProcessController {
	/** LoginService */
	@Resource(name = "loginService")
    private LoginService loginService;
	
	@Resource(name= "commonService")
	private CommonService comService;

	private Logger log = Logger.getLogger(LoginController.class);
	
	@RequestMapping("/loginPage.do")
	public String loginPage(HttpServletRequest request,
							HttpServletResponse response,
							ModelMap model) throws Exception{
		//response.addHeader("X-Frame-Options", "SAMEORIGIN");
		String returnUrl = request.getParameter("returl") == null ? "" : request.getParameter("returl"); 
		
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		
		NoticeVO topNotice = comService.getMainNoticeInfo();
		model.addAttribute("topNotice", topNotice);
		model.addAttribute("returnUrl", returnUrl);
		if (isAuthenticated) {
			if(!returnUrl.equals("")){
				return "redirect:"+ returnUrl;
			}else{
				//return "redirect:/dash/home.do";
				//return "redirect:/main/userMain.do";
				/**
				 * RENEWAL 수정
				 */
				return "redirect:/dash/dashboard.do";
			}
			
		}else{
			return "loginPage";
		}
	}
	
	@RequestMapping("/nossloginProcess.do")
	public String nossloginProcess(HttpServletRequest request
							, HttpServletResponse response,
							ModelMap model) throws Exception{
		//response.addHeader("X-Frame-Options", "SAMEORIGIN");
		String rUrl = "";
		NoticeVO topNotice = comService.getMainNoticeInfo();
		model.addAttribute("topNotice", topNotice);
		HashMap<String,String> map = null;
		try{
			map = comService.loginProcess(request);
			//HashMap<String,String> map = comService.loginProcess(request);
		
			/**********************************************************************************************/
		
			switch (Integer.parseInt((String)map.get("result"))) {
			//switch (result) {
			case 1:
				model.addAttribute("rMsg", "아이디를 입력하세요");
				rUrl = "loginPage";
				break;
			case 2:
				model.addAttribute("rMsg", "비밀번호를 입력하세요");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				rUrl = "loginPage";
				break;
			case 3:
				model.addAttribute("rMsg", "아이디 또는 비밀번호(kate비밀번호)가 일치하지 않습니다.");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				model.addAttribute("password", "");
				rUrl = "loginPage";
				break;
			case 4:
				model.addAttribute("rMsg", "비밀번호(kate비밀번호)가 일치하지 않습니다");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				model.addAttribute("password", "");			
				rUrl = "loginPage";
				break;
			case 5:
			
				UsernamePasswordAuthenticationFilter springSecurity = null;
				 
				ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
				@SuppressWarnings("rawtypes")
				Map beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
				if (beans.size() > 0) {
					springSecurity = (UsernamePasswordAuthenticationFilter)beans.values().toArray()[0];
				} else {
					throw new IllegalStateException("No AuthenticationProcessingFilter");
				}
			 
				springSecurity.setContinueChainBeforeSuccessfulAuthentication(false);	// false 이면 chain 처리 되지 않음.. (filter가 아닌 경우 false로...)
				
				String uid = (String)request.getParameter("u1");
				//인증암호 - Ldap통한 암호 확인 불가로 기본값 입력함 
				//String passwd= (String)request.getParameter("pd");
				request.getSession().invalidate();
				HttpSession session = request.getSession(true);
				springSecurity.doFilter(new RequestWrapperForSecurity(request, uid , "121212"), response, null);
				
				/*****************************************************************
				 * 접속 로그  필요시 적용
				 *****************************************************************/
				comService.setUserLoginLogoutLog(request, "LOGIN");
				request.setCharacterEncoding("euc-kr");
				request.getSession().setAttribute("returnURL", "");
				rUrl = "forward:/member/actionmain.do";
				
				break;
			}
			}catch(Exception e){
				e.printStackTrace();
			}
				
			return rUrl;
	}
	
	@RequestMapping("/loginProcess.do")
	public String loginProcess(HttpServletRequest request
							, HttpServletResponse response,
							ModelMap model) throws Exception{
		//response.addHeader("X-Frame-Options", "SAMEORIGIN");
		//System.out.println(request.getParameter("p1"));
		String rUrl = "loginPage";
		NoticeVO topNotice = comService.getMainNoticeInfo();
		model.addAttribute("topNotice", topNotice);
		HashMap<String,String> map = null;
		try{
		
			CodeInfoVO codeInfo = new CodeInfoVO();
			codeInfo.setMajr_code(MajrCodeInfo.ExecuteCode);
			codeInfo.setMinr_code("LDP");
			CodeInfoVO ldapInfo = comService.getCodeInfoForOne(codeInfo);
			if(request.getParameter("u1").equals("admin")){
				//관리를 위한 사용자이면 Ldap 인증없이 바로 로그인 처리 함
				//System.out.println("ADMIN 로그인처리");
				map = comService.loginProcess(request);
			}else{
				boolean isLdap = false;
				if(ldapInfo.getCode_desc() != null){
					isLdap = ldapInfo.getCode_desc().equals("Y");
					//TEST 용
					//isLdap = false;
				}
				int result = 1;
				
				if(isLdap){
					/**
					 * LDAP check
					 * 로그인 실패 로그 확인
					 */
					codeInfo.setMajr_code(MajrCodeInfo.LoginFaildBlockHour);
					map = new HashMap<String, String>();
					String loginID = request.getParameter("u1");
					
					codeInfo.setMinr_code("01"); //차단시간 조회
					CodeInfoVO loginBlockTime= comService.getCodeInfoForOne(codeInfo);
					HashMap<String, String> smap = new HashMap<String, String>();
					smap.put("emp_no", loginID);
					smap.put("interval_val", String.format("%s hour", loginBlockTime.getCode_desc()));
					
					EgovMap failLog = comService.LoginFailCountInfo(smap);
					int failcount = 0;
					int blockcount = 0;
					if(failLog != null){
						failcount = (Integer)failLog.get("failcnt");
						codeInfo.setMinr_code("02");
						CodeInfoVO loginBlockInfo= comService.getCodeInfoForOne(codeInfo);
						
						blockcount = Integer.parseInt(loginBlockInfo.getCode_desc());
								
						if(failcount >= blockcount){
							/**
							 * 로그인실패 회수가 제안회수 이상일때 로그인 차단함
							 */
							if(failLog.get("isblock").toString().equals("Y")){
								//블럭상태임
								map.put("result", "100");
								map.put("passDate", failLog.get("rdate").toString());
								map.put("blockcount", blockcount + "");
								result = 0;
							}else{
								//초기화
								comService.LoginFailCountDelete(loginID);
							}
						}
					}else{
						map.put("result", "101");
						result = 0;
					}
					
					if(result==1){
						boolean isLogin = false;
						isLogin = LDAPUtil.ldapLoginCheck(request, null, model);
						
						if(isLogin){
							map.put("result", "5");
							//System.out.println("Login OK!");
							result = 5;
							/**
							 * 로그인성공이면 로그인실패회수 초기화
							 */
							comService.LoginFailCountDelete(loginID);
						}else{
							map.put("result", "102");
							map.put("failcnt", ++failcount + "");
							map.put("blockcount", blockcount + "");
							//System.out.println("Login Fail");
							result =3;
							comService.LoginFailCountInsert(loginID);
						}		
					}
					
				}else{
					map = comService.loginProcess(request);
				}
			}
			//HashMap<String,String> map = comService.loginProcess(request);
		
			/**********************************************************************************************/
		
			switch (Integer.parseInt((String)map.get("result"))) {
			//switch (result) {
			case 1:
				model.addAttribute("rMsg", "사번을 입력하세요");
				rUrl = "loginPage";
				break;
			case 2:
				model.addAttribute("rMsg", "비밀번호를 입력하세요");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				rUrl = "loginPage";
				break;
			case 3:
				model.addAttribute("rMsg", "사번 또는 비밀번호(kate비밀번호)가 일치하지 않습니다.");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				//model.addAttribute("password", (String)request.getParameter("password"));
				rUrl = "loginPage";
				break;
			case 4:
				model.addAttribute("rMsg", "비밀번호(kate비밀번호)가 일치하지 않습니다.");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				//model.addAttribute("password", (String)request.getParameter("password"));			
				rUrl = "loginPage";
				break;
			case 100:
				model.addAttribute("rMsg", String.format("비밀번호 %s회 오류로 로그인 차단 상태 입니다.\\r\\n\\r\\n로그인 차단 해제 시간 : %s", map.get("blockcount"), map.get("passDate") ));
				model.addAttribute("u1", (String)request.getParameter("u1"));
				//model.addAttribute("password", (String)request.getParameter("password"));			
				rUrl = "loginPage";
				break;	
			case 101:
				model.addAttribute("rMsg", "사번 또는 비밀번호(kate비밀번호)가 일치하지 않습니다. ");
				model.addAttribute("u1", (String)request.getParameter("u1"));
				//model.addAttribute("password", (String)request.getParameter("password"));			
				rUrl = "loginPage";
				break;
			case 102:
				model.addAttribute("rMsg", String.format("사번 또는 비밀번호(kate비밀번호)가 일치하지 않습니다.(비밀번호 [%s]회 오류)\\r\\n\\r\\n%s회 이상 오류시 로그인이 제한됩니다.", map.get("failcnt"), map.get("blockcount")));
				model.addAttribute("u1", (String)request.getParameter("u1"));
				//model.addAttribute("password", (String)request.getParameter("password"));
				rUrl = "loginPage";
				break;
			case 5:
			
				UsernamePasswordAuthenticationFilter springSecurity = null;
				 
				ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
				@SuppressWarnings("rawtypes")
				Map beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
				if (beans.size() > 0) {
					springSecurity = (UsernamePasswordAuthenticationFilter)beans.values().toArray()[0];
				} else {
					throw new IllegalStateException("No AuthenticationProcessingFilter");
				}
			 
				springSecurity.setContinueChainBeforeSuccessfulAuthentication(false);	// false 이면 chain 처리 되지 않음.. (filter가 아닌 경우 false로...)
				
				String uid = (String)request.getParameter("u1");
				//인증암호 - Ldap통한 암호 확인 불가로 기본값 입력함 
				//String passwd= (String)request.getParameter("password");
				request.getSession().invalidate();
				HttpSession session = request.getSession(true);
				springSecurity.doFilter(new RequestWrapperForSecurity(request, uid , "121212"), response, null);
				
				/*****************************************************************
				 * 접속 로그  필요시 적용
				 *****************************************************************/
				comService.setUserLoginLogoutLog(request, "LOGIN");
				
				request.setCharacterEncoding("euc-kr");
				request.getSession().setAttribute("returnURL", request.getParameter("returl"));
				/**
				 * 접속자 관리자일경우 아이피 체크 일단 주석
				 */
				/*
				if(request.getParameter("userid").equals("admin")){
					MemberVO memberVO = CommonUtil.getMemberInfo(request);
				}
				*/
				
				/**
				 * 접속정보 세션처리
				  */
				/*
				MemberVO memberVO = CommonUtil.getMemberInfo();
				
				UserManageVO loginVO = new UserManageVO();
				loginVO.setuserid(memberVO.getUserid());
				loginVO.setEmp_no(memberVO.getUserid());
				loginVO.setEmp_nm(memberVO.getUsername());
				loginVO.setOrg_code(memberVO.getOrgcode());
				loginVO.setuser_auth(memberVO.getRole_code());
				loginVO.setTitle_code(memberVO.getTitlecode());
				loginVO.setTitle_nm(memberVO.getTitlename());
				loginVO.setip(memberVO.getIp());
				loginVO.setmac(memberVO.getMac());
				loginVO.setemail_indc(memberVO.getIsmail());
				loginVO.setemp_indc(memberVO.getIsEmp());
				request.getSession().setAttribute("LoginVO", loginVO);
				
				System.out.println("Auth UserID : " + memberVO.getUserid());
				*/
				rUrl = "forward:/member/actionmain.do";
				
				break;
			}
			}catch(Exception e){
				e.printStackTrace();
			}
				
			return rUrl;
	}
	
	@RequestMapping(value="/member/actionmain.do")
	public String ActionMain(HttpServletRequest request, ModelMap model) throws Exception{
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
	    	return "/loginPage";
	    }else{
	    	request.getSession().setAttribute("loginType", "personal");
	    	String returnUrl = (String)request.getSession().getAttribute("returnURL");
	    	if(returnUrl.equals("")){
	    		//return "redirect:/dash/home.do";
	    		//return "redirect:/main/userMain.do";
	    		/**
	    		 * RENEWAL 수정
	    		 */
	    		return "redirect:/dash/dashboard.do";
	    	}else{
	    		return "redirect:"+ returnUrl;
	    	}
	    		
	    	
	    }
	}
	
	@RequestMapping(value="/logout.do")
	public String logout(HttpServletRequest request
						, HttpServletResponse response
						, ModelMap model) throws Exception{
		/*****************************************************************
		 * 접속 종료 로그  필요시 적용
		 *****************************************************************/
		//response.addHeader("X-Frame-Options", "SAMEORIGIN");
		if(!EgovUserDetailsHelper.isAuthenticated()){
			return "redirect:/loginPage.do";
		}else{
			comService.setUserLoginLogoutLog(request, "LOGOUT"); 
			return "redirect:/j_spring_security_logout";
		}
		
	}	
	
	
	
}

/*
class RequestWrapperForSecurity extends HttpServletRequestWrapper {	
	private String username = null;
	private String password = null;
 
	public RequestWrapperForSecurity(HttpServletRequest request, String username, String password) {
		super(request);
 
		this.username = username;
		this.password = password;
	}
 
	@Override
	public String getRequestURI() {
		return ((HttpServletRequest)super.getRequest()).getContextPath() + "/j_spring_security_check";
	}
 
	@Override
	public String getParameter(String name) {
        if (name.equals("j_username")) {
        	return username;
        }
 
        if (name.equals("j_password")) {
        	return password;
        }
 
        return super.getParameter(name);
    }
}
*/
