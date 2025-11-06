<%@ page import="nets.websso.ssoclient.authcheck.*" %>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.ui.ModelMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="sdiag.login.service.UserManageVO" %>
<%@ page import="sdiag.member.service.MemberVO"%>
<%@ page import="sdiag.util.CommonUtil"%>
<%@ page import="sdiag.util.RequestWrapperForSecurity"%>
<%@ page import="egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper"%>
<%@ page import="sdiag.com.service.CommonService" %>
<%@ page language="java" contentType="text/html; charset=EUC-KR" %>
<%

	SSOConfig.request = request;
    AuthCheck auth = new AuthCheck(request, response);
    AuthStatus status = auth.CheckLogon(AuthCheckLevel.Medium);
    
    // 인증이 안되었을때, 이동시킬 SSO 로그인 URL (추후, URL 변경될 수 있음)
    String loginUrl =  "http://kate.kt.com/?" + SSOConfig.ReturnURLTagName() + "=" + Util.URLEncode(auth.ThisURL(), "UTF8"); //개발
    //String loginUrl =  "http://kate.kt.com/?" + SSOConfig.ReturnURLTagName() + "=" + Util.URLEncode(auth.ThisURL(), "UTF8"); //운영
    if(status == AuthStatus.SSOFirstAccess) //SSO 인증검사 : 최초접근 (멀티도메인시에도 체크됨)
    {
        auth.TrySSO();
    }
    else if(status == AuthStatus.SSOFail)   // SSO 인증검사: 실패
    {
        if(auth.ErrorNumber() != ErrorCode.NO_ERR)
        {
            String errMsg = ErrorMessage.GetMessage(auth.ErrorNumber());
            System.out.println(" NSSO Error Message :: "  + errMsg);
            response.sendRedirect(loginUrl);
        }
        else
        {
            response.sendRedirect(loginUrl);
        }
    }
    else if(status == AuthStatus.SSOSuccess)    //SSO 인증검사 : 성공
    {
        // 인증된 사용자 ID 받기
        String struid = auth.UserID();
       // response.getWriter().write("사용자 ID: " + struid + "<br>");   // ID받기 테스트
        
        response.getWriter().write("<form name=\"frm\" id=\"frm\" method=\"post\" autocomplete=\"off\" action=\"/nossloginProcess.do\">");
        response.getWriter().write(String.format("<input type=\"hidden\" id=\"u1\" name=\"u1\" value=\"%s\" />",struid));
        response.getWriter().write("<input type=\"hidden\" id=\"p1\" name=\"p1\" value=\"12345\" />");
        response.getWriter().write("</form>");
        response.getWriter().write("<script type=\"text/javascript\">");
        response.getWriter().write("document.frm.submit();");
        response.getWriter().write("</script>");
        response.getWriter().write("");
      
    }
    else if(status == AuthStatus.SSOUnAvaliable)    //SSO 인증검사 : SSO서버 중지
    {
        System.out.println(" NSSO UnAvailable :: " + status);
        response.sendRedirect(loginUrl);
    }
%>