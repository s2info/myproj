package sdiag.util;

import java.util.HashMap;
import java.util.Map;

import nets.ldap.ADUtilSSL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import sdiag.login.service.UserManageVO;
import sdiag.member.service.MemberVO;

public class LDAPUtil {
	
	
	static final Boolean ldapDevMode = false;
	
	
	/**
	 * 개발 LDAP Host 설정
	 */
	static final String host_dev = "tbldap01.ldapktlab.dev";
	//static final String host_dev = "10.217.40.53";
	static final String port_dev = "389"; //or 389, 636(SSL Port)
	static final String baseDN_dev = "OU=Employee,DC=ldap,DC=ktlab,DC=dev";

	/**
	 * 운영 LDAP Host 설정
	 */
	static final String host_prod = "ldap.kt.com";
	static final String port_prod = "389"; //or 389, 636(SSL Port)
	static final String baseDN_prod = "OU=Employee,DC=ktldap,DC=ktad,DC=kt,DC=net";

	
	/**
	 * 개발 테스트 계정 (개발계LDAP 테스트시에만 사용 가능, 운용테스트는 실사용자 계정으로 수행)
	 * 
	 * [서비스 계정 / 서비스 비밀번호]
	 * LDAP 개발계(Dev)  sldm_ldapdevuser/Sldm_LdapDevUser : 인증결과,사용자 조회
	 * LDAP 개발계(Prod) sldm_ldapuser/SLDM_Ld@pUser! : 인증결과,사용자 조회
	 */	
		
	/** ldap 개발 */
	static final String setloginID = "";
	//static final String setpasswd = "";
	
	static final String connID_dev = "sldm_ldapdevuser";
	static final String connPwd_dev = "Sldm_LdapDevUser";
	
	/** ldap 운영 */
	static final String connID_prod = "sldm_ldapuser";
	static final String connPwd_prod = "SLDM_Ld@pUser!";

	
	public static boolean ldapLoginCheck(HttpServletRequest request, 
			HttpServletResponse response, ModelMap model) throws Exception{

		try
		{
			String loginID = request.getParameter("u1");  // USER_EMPN
			String loginpwd = request.getParameter("p1");	 //USER_PWD
		
			loginpwd = HTMLInputFilter.dehtmlSpecialChars(loginpwd);
			//if(ldapDevMode){
		//		loginID = setloginID;
		//		loginpwd = setpasswd;
	//		}
						
		//	System.out.println(":::::::::::::::::::::::::::"+loginID);
		//	System.out.println(":::::::::::::::::::::::::::"+loginpwd);			
			
			Boolean bLogin = false;

			/**
			 * 	[Method 파라미터 설명]
			 *
			 * ADUtilSSL.auth_basic_try(host, port, baseDN, 사번(8자리), 비밀번호, 서비스계정ID, 서비스계정PWD, SSL여부)
			 * 389 포트를 사용할 경우 false , 636 포트를 사용할 경우 true
			 */
			
			//if (ldapDevMode){  //개발
			//	bLogin = ADUtilSSL.auth_basic_try(host_dev, port_dev, baseDN_dev, loginID, loginpwd, connID_dev, connPwd_dev, false);				
			//}else{  //운영
			bLogin = ADUtilSSL.auth_basic_try(host_prod, port_prod, baseDN_prod, loginID, loginpwd, connID_prod, connPwd_prod, false);
			//}
						
			if(bLogin){
				//queryUserInfo(request, null);
				return true;				
			}else{
				return false;
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			
			if(e1.getMessage().indexOf("data 0005") > 0)
			{
				//인증실패 처리
				//comment: 인증에 실패하였습니다. data 0005
				model.addAttribute("rMsg", "인증에 실패하였습니다.");
			}else if(e1.getMessage().indexOf("data 0003") > 0){
				//comment: 서비스 계정이 잘못되었습니다. data 0003
				model.addAttribute("rMsg", "서비스 계정이 잘못되었습니다.");				
			}else{
				
			}
		}
		
		return false;

	}
	
	public static String queryUserInfo(HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		
		String loginID = request.getParameter("u1");  // USER_EMPN
		String rUrl = "";
		/*
		if(ldapDevMode){
			loginID = setloginID;
		}	
		*/
		Map map = new HashMap();

		try 
		{
			//if (ldapDevMode){
			//	map = ADUtilSSL.query_userinfo(host_dev, port_dev, baseDN_dev, loginID, connID_dev, connPwd_dev, false);
			//}else{
			map = ADUtilSSL.query_userinfo(host_prod, port_prod, baseDN_prod, loginID, connID_prod, connPwd_prod, false);
			//}
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}

		if(map.size() > 0)
		{
			String userName = (String) map.get("userName");		//이름
			String deptCD = (String) map.get("deptCD");		//소속 팀 코드
			String deptName = (String) map.get("deptName");		//소속 팀 명
			String agencyCD = (String) map.get("agencyCD");		//소속 본부 코드
			String agencyName = (String) map.get("agencyName");	//소속 본부 명
			String positionCD = (String) map.get("positionCD");	//그룹사 코드 | 본부 코드 | 팀 코드
			String positionName = (String) map.get("positionName");	//그룹사 명 | 본부 명 | 팀 명	
		  
			/**
			 * 접속정보 세션처리
			  */		  
			UserManageVO loginVO = new UserManageVO();
			loginVO.setUserName(userName);
			loginVO.setDeptCD(deptCD);
			loginVO.setDeptName(deptName);
			loginVO.setAgencyCD(agencyCD);
			loginVO.setAgencyName(agencyName);
			loginVO.setPositionCD(positionCD);
			loginVO.setPositionName(positionName);
			request.getSession().setAttribute("LoginVO3", loginVO);
			
			//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>userName:" + userName);
			//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>deptName:" + deptName);			
		}
		
		rUrl = "forward:/handy/ExanalList.do";
		
		
		
		return rUrl;
		
	}

}
