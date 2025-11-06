package sdiag.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapperForSecurity extends HttpServletRequestWrapper{
	private String u1 = null;
	private String p1 = null;
 
	public RequestWrapperForSecurity(HttpServletRequest request, String un, String pw) {
		super(request);
 
		this.u1 = un;
		this.p1 = pw;
	}
 
	@Override
	public String getRequestURI() {
		
		//String returl = ((HttpServletRequest)super.getRequest()).getParameter("returl");
		//System.out.println(returl + "][^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		//return ((HttpServletRequest)super.getRequest()).getContextPath() + String.format("/j_spring_security_check%s", returl.equals("") ? "" : "?spring-security-redirect="+ returl);
		return ((HttpServletRequest)super.getRequest()).getContextPath() + "/j_spring_security_check";
	}
 
	@Override
	public String getParameter(String name) {
        if (name.equals("j_username")) {
        	return u1;
        }
 
        if (name.equals("j_password")) {
        	return p1;
        }
 
        return super.getParameter(name);
    }
}
