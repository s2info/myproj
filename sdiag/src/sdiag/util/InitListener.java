package sdiag.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.CommonService;
import sdiag.com.service.MenuItemVO;
import sdiag.pol.service.OrgGroupVO;

public class InitListener implements ServletContextListener{

	private static ApplicationContext listnerContext = null;
	private CommonService commonService = null;
	public void init(){
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContext) {
		// TODO Auto-generated method stub
		
	}
	
	private static List<OrgGroupVO> getOrgListForUpperCode(List<OrgGroupVO> oList, String upperCode){
		List<OrgGroupVO> OrgList = new ArrayList<OrgGroupVO>();
		for(OrgGroupVO org:oList){
			if(org.getUpper_org_code().equals(upperCode)){
				OrgList.add(org);
			}
		}
		
		return OrgList;
	}
	
	private static void setOrgCodeInfo(List<OrgGroupVO> oList, String upperCode, StringBuffer str, boolean isUser){
		for(OrgGroupVO org:getOrgListForUpperCode(oList, upperCode)){
	    	str.append(String.format("<li class='%s'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
	    									, isUser ? "open" : "closed"
	    									, org.getIs_suborg().equals("Y") ? "folder" : "file"
	    									, org.getIs_suborg().equals("Y") ? "1" : "2"
	    									, org.getOrg_code()
	    									, isUser ? org.getIs_suborg().equals("Y") ? "" : "class='ON'" : ""
	    									, org.getOrg_nm()));
	    	if(org.getIs_suborg().equals("Y")){
	    		str.append("<ul>\r\n");
		    	setOrgCodeInfo(oList, org.getOrg_code(), str, isUser);
		    	str.append("</ul>\r\n");
	    	}
	    	str.append("</li>\r\n");
	    }
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContext ) {
		// TODO Auto-generated method stub
		try{
		//	ServletContext context = servletContext.getServletContext();
		//	listnerContext = new ClassPathXmlApplicationContext("sdiag.context/spring/context-*.xml");
			
		//	commonService = (CommonService)listnerContext.getBean("commonService");
		//	List<MenuItemVO> list = commonService.getSolMenuList();		
		//	context.setAttribute("SolMenuList", list);
			
			
			/*
			StringBuffer str = new StringBuffer();
			HashMap<String, Object> orgInfo = null;
			String root = MajrCodeInfo.RootOrgCode;
			OrgGroupVO rootInfo = commonService.getOrgInfo(root);
			str.append(String.format("<li class='open'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
										, rootInfo.getIs_suborg().equals("Y") ? "folder" : "file"
										, rootInfo.getIs_suborg().equals("Y") ? "1" : "2"
										, rootInfo.getOrg_code()
										, "class='ON'"
										, rootInfo.getOrg_nm()));
			str.append("<ul>\r\n");
			List<OrgGroupVO> orgList = commonService.getOrgInfoList(orgInfo);
			//최상위코드 조회
		    for(OrgGroupVO org:getOrgListForUpperCode(orgList, root)){
		    	str.append(String.format("<li class='%s'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
		    									, "closed"
		    									, org.getIs_suborg().equals("Y") ? "folder" : "file"
		    									, org.getIs_suborg().equals("Y") ? "1" : "2"
		    									, org.getOrg_code()
		    									, ""
		    									, org.getOrg_nm()));
		    	if(org.getIs_suborg().equals("Y")){
		    		str.append("<ul>\r\n");
			    	setOrgCodeInfo(orgList, org.getOrg_code(), str, false);
			    	str.append("</ul>\r\n");
		    	}
		    	str.append("</li>\r\n");
		    }
		    str.append("</ul>\r\n");
	    	str.append("</li>\r\n");
			
	    	context.setAttribute("treeList", str.toString());
	    	*/
			//List<EgovMap> polMenuList = commonService.getPolMenuAllList();
			//context.setAttribute("polMenuList", polMenuList);
			/*
			HashMap<String, MenuItemVO> menuMap = null;
			if (list != null && list.size() > 0){
				int listSize = list.size();
				menuMap = new HashMap<String,MenuItemVO>();
				for (int i = 0; i < listSize; i++){
					MenuItemVO vo = list.get(i);
					menuMap.put(vo.getMajrCode(), vo);
				}
			}
			context.setAttribute("menuMap", menuMap);
			*/
			

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
