package sdiag.cmm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.mvc.WebContentInterceptor;

import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;

//import fpm.cmm.service.MenuVO;

public class Interceptor extends WebContentInterceptor {
	
	@Resource(name= "commonService")
	private CommonService comService;

	/** 메뉴 정렬 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		
		try{
			
			/*****************************************************************
			 * 로그인 여부 체크
			 *****************************************************************/
			/*Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			
			if (!isAuthenticated) {
				response.sendRedirect("/");
				return false;
			}*/
			
			
			/*****************************************************************
			 * IP 차단
			 *****************************************************************/			
			String remotIp = request.getRemoteAddr();
			
			int cnt = 0;
			
			List<CodeInfoVO> accessIpInfo = new ArrayList<CodeInfoVO>();
			
			accessIpInfo = comService.getCodeInfoListNoTitle("AIP");
			if(accessIpInfo.size() > 0){
				String accessIp = accessIpInfo.get(0).getAdd_info1();
				
				if(!accessIp.isEmpty() && !accessIp.equals("") && accessIp != null){
				
					String[] accessIpList = accessIpInfo.get(0).getAdd_info1().split(",");
					
						for(String aIp : accessIpList){
							if(aIp.equals(remotIp)){
								cnt++;
							}
						}
						
						if(cnt == 0){
							response.sendRedirect("/common/notice.jsp");
							return false;
						}
				}
			}
			
			/*String url = request.getRequestURL().toString();
			if(url.startsWith("http://") && url.indexOf("localhost") <0){
				url = url.replaceAll("http://", "https://");
				response.sendRedirect(url);
			}
			*/
			
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return true;
	}
}
