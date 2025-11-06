package sdiag.util;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.util.UrlPathHelper;

import sdiag.com.service.CommonService;
import sdiag.com.service.LoginUserTypeVO;
import sdiag.com.service.MenuItemVO;
import sdiag.board.service.NoticeVO;
import sdiag.member.service.MemberVO;
import sdiag.pol.service.OrgGroupVO;

import com.google.gson.Gson;

import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public class CommonUtil {
	/**
	 * Generate a CRON expression is a string comprising 6 or 7 fields separated by white space.
	 *
	 * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
	 * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
	 * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
	 * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
	 * @param year       mandatory = no. allowed values = {@code 1970?2099    * / , -}
	 * @return a CRON Formatted String.
	 */
	public static String generateCronExpression(final String seconds, final String minutes, final String hours,
	                                             final String dayOfMonth,
	                                             final String month, final String dayOfWeek, final String year){
	  return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
	}
	/**
	 * 접속자 로컬 아이피 조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getLocalIPAddress(HttpServletRequest request) throws Exception
	{
		String clientIp = request.getRemoteAddr();
		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown") || clientIp.equals("0:0:0:0:0:0:0:1")){
			InetAddress inetAddress = InetAddress.getLocalHost();
			clientIp = inetAddress.getHostAddress();
		}	
		return clientIp;
	}
	
	/**
	 * 고유값 생성
	 * @return
	 */
	public static String CreateUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	//문자열을 숫자배열로 변환
	public static List<Integer> SplitToInteger(String str, String separator)
	{
		List<Integer> list = new ArrayList<Integer>();

		if(str.split(separator).length == 0 && str.length() > 0)
			list.add(Integer.parseInt(str));
		else
		{
			String[] split_str = str.split(separator);
			for(int i = 0 ; i < split_str.length ; i++)
			{
				list.add(Integer.parseInt(split_str[i]));
			}

		}

		return list;
	}
	
	public static List<String> SplitToString(String str, String separator)
	{
		List<String> list = new ArrayList<String>();

		if(str.split(separator).length == 0 && str.length() > 0)
			list.add(str);
		else
		{
			String[] split_str = str.split(separator);
			for(int i = 0 ; i < split_str.length ; i++)
			{
				list.add(split_str[i]);
			}

		}

		return list;
	}
	
	//Json String To HashMap<String, String>
	public static HashMap<String, String> convertJsonStringToHashMap(String jsonString)
	{
		Gson gson = new Gson();
		HashMap<String, String> paramMap = new HashMap<String,String>();
		paramMap = (HashMap<String, String>)gson.fromJson(jsonString, paramMap.getClass());

		return paramMap;
	}
	
	public static boolean setCommunityReadCnt(HttpServletResponse response, HttpServletRequest request, long cno, String CookieName) throws Exception
	{
		boolean retValue = false;
		//조회수 쿠키 이용
		Cookie cookies[] = request.getCookies();
		Map mapCookie = new HashMap();
		if(request.getCookies() != null){
			for (int i = 0; i < cookies.length; i++) {
				Cookie obj = cookies[i];
				mapCookie.put(obj.getName(),obj.getValue());
			}
		}

		String cookie_ReadCnt = (String) mapCookie.get(CookieName);
		String cookie_ReadCnt_New = "|" + cno;

		// 저장된 쿠키에 새로운 쿠키값이 존재하는 지 검사
		if ( StringUtils.indexOfIgnoreCase(cookie_ReadCnt, cookie_ReadCnt_New) == -1 ) {
			// 없을 경우 쿠키 생성
			Cookie cookie = new Cookie(CookieName, cookie_ReadCnt + cookie_ReadCnt_New);
			//cookie.setMaxAge(1000); // 초단위
			response.addCookie(cookie);
			retValue = true;
		}

		return retValue;
	}
	
	/**
	 * 메뉴를 String으로 만들어서 request 속성값에 넣는다 속성이름은 topMenu
	 * @param request
	 * @param MenuInfo
	 */
	//public static void topMenuToString(HttpServletRequest request, String MenuInfo){
//		topMenuToString(request, MenuInfo, null);
//	}
	public static void topMenuToString(HttpServletRequest request, String MenuInfo, CommonService comService){
		topMenuToString(request, MenuInfo, comService, "H");
	}
	public static void topMenuToString(HttpServletRequest request, String MenuInfo, CommonService comService, String pageType){
		topMenuToString(request, MenuInfo, comService, pageType, "N");
	}
	public static void topMenuToString(HttpServletRequest request, String MenuInfo, CommonService comService, String pageType, String buseoType){
		
		try{
			
			StringBuffer str = new StringBuffer();
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			//MemberVO memberVO = CommonUtil.getMemberInfo();	
			String subMenu = "";
			String requestType = (String)request.getSession().getAttribute("loginType");
			
			List<MenuItemVO> menulist = null; 
			
			if(comService == null){
				ServletContext servletContext = request.getServletContext();
				menulist = (List<MenuItemVO>) servletContext.getAttribute("SolMenuList");
			}else{
				menulist = comService.getSolMenuList();		
			}
			
			List<LoginUserTypeVO> TypeList = comService.getLoginUserTypeInfo(loginInfo.getUserid(), loginInfo.getRole_code());
			StringBuffer btnLoginType = new StringBuffer();
			/**
			 * HOME | 진단보고서 메뉴 일때
			 */
			if(MenuInfo == "home" || MenuInfo == "report" || MenuInfo == "monthreport"){
				if(TypeList.size() > 1){
					//String ktmp = request.getParameter("ktmp");
					//String requestType = (String)request.getSession().getAttribute("loginType");
					for(LoginUserTypeVO row:TypeList){
						HashMap<String, String> typeInfo = getConvertMainparamString(row.getLogintype(), row.getOrg_code(), row.getOrg_nm(), row.getEmp_nm());
						//<a style='cursor:pointer;margin-right: 7px;' loginType='%s'  menutype='T' class='btn_pol_status %s'>%s</a>\r\n
						btnLoginType.append(String.format("<a style='cursor:pointer;margin-right: 7px;' class='btn_main_reload %s' ktmp='%s' url='%s'>%s</a>\r\n" //href='/dash/home.do?ktmp=%s
								, typeInfo.get("type").equals(requestType) ? "on" : ""
								, typeInfo.get("type")
								//, MenuInfo == "home" ? "/dash/home.do" : MenuInfo == "report" ? "/report/sheetStar.do" : "/report/monthSheetStar.do"
								, MenuInfo == "home" ? "/dash/dashboard.do" : MenuInfo == "report" ? "/report/sheetStar.do" : "/report/monthSheetStar.do"
								, typeInfo.get("typeName")));
						
						if(row.getLogintype().equals("CAPTAIN")){
							List<LoginUserTypeVO> ColGroupList = comService.getColGroupList(row.getEmp_no());
							if(ColGroupList.size() > 0){
								for(LoginUserTypeVO info:ColGroupList){
									HashMap<String, String> colGroupInfo = getConvertMainparamString(info.getLogintype(), info.getOrg_code(), info.getOrg_nm(), info.getEmp_nm());
									//<a style='cursor:pointer;margin-right: 7px;' loginType='%s'  menutype='T' class='btn_pol_status %s'>%s</a>\r\n
									btnLoginType.append(String.format("<a style='cursor:pointer;margin-right: 7px;' class='btn_main_reload %s' ktmp='%s' url='%s'>%s</a>\r\n" //href='/dash/home.do?ktmp=%s
											, colGroupInfo.get("type").equals(requestType) ? "on" : ""
											, colGroupInfo.get("type")
											//, MenuInfo == "home" ? "/dash/home.do" : MenuInfo == "report" ? "/report/sheetStar.do" : "/report/monthSheetStar.do"
											, MenuInfo == "home" ? "/dash/dashboard.do" : MenuInfo == "report" ? "/report/sheetStar.do" : "/report/monthSheetStar.do"
											, colGroupInfo.get("typeName")));
								}
							}
						}
					}
				}
			}
			
			
			
				/**
				 * 지수화 정책 모니터링 메뉴일때
				 */
				if(pageType.equals("P")){
					//List<LoginUserTypeVO> TypeList = comService.getLoginUserTypeInfo(loginInfo.getUserid(), loginInfo.getRole_code()); 
					if(TypeList.size() > 1){
						/**
						 * 부서진단일때 - 관리자 및 상무이상 메뉴 활성화 (상무중 조직장이 아닌경유 비활성화)
						 * 부서진단일때 - 대무자 직급이 상무 이상일때 메뉴 활성화(대무자가 조직장이 아닌경우 비활성화)
						 */
						if(buseoType.equals("Y")){
							if(requestType.equals("personal")){
								for(LoginUserTypeVO row:TypeList){
									if(row.getLogintype().equals("CAPTAIN")){
										requestType = row.getOrg_code();
										request.getSession().setAttribute("loginType", requestType);
										break;
									}else if(row.getLogintype().equals("ADMIN")){
										requestType = "sa";
										request.getSession().setAttribute("loginType", requestType);
										break;
									}else if(row.getLogintype().equals("PROXY")){
										requestType = row.getOrg_code();
										request.getSession().setAttribute("loginType", requestType);
										break;
									}
								}
							}
						}
						for(LoginUserTypeVO row:TypeList){
							if(buseoType.equals("Y")){
								if(!row.getLogintype().equals("PERSONAL")){
									HashMap<String, String> typeInfo = getConvertMainparamString(row.getLogintype(), row.getOrg_code(), row.getOrg_nm(), row.getEmp_nm());
									btnLoginType.append(String.format("<a style='cursor:pointer;margin-right: 7px;' loginType='%s'  menutype='T' class='btn_pol_status %s'>%s</a>\r\n"
											, typeInfo.get("type")
											, typeInfo.get("type").equals(requestType) ? "on" : ""
											, typeInfo.get("typeName")));
								}
									
							}else{
								HashMap<String, String> typeInfo = getConvertMainparamString(row.getLogintype(), row.getOrg_code(), row.getOrg_nm(), row.getEmp_nm());
								btnLoginType.append(String.format("<a style='cursor:pointer;margin-right: 7px;' loginType='%s'  menutype='T' class='btn_pol_status %s'>%s</a>\r\n"
										, typeInfo.get("type")
										, typeInfo.get("type").equals(requestType) ? "on" : ""
										, typeInfo.get("typeName")));
							}
							
						}
					}
				}
				request.setAttribute("BtnLoginUserType", btnLoginType.toString());
				
				
				List<EgovMap> polMenulist = comService.getPolMenuAllList();
				
				str.append(String.format("<li><a class='topmenu%s' href='/dash/dashboard.do' subMenu=''><span>Home</span></a></li>\r\n", MenuInfo == "home" ? " topmenuSel" : ""));
				
				
				
				if(!requestType.equals("personal")){
				
				for(MenuItemVO row:menulist)
				{
					if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
						if(row.getBuseo_indc().equals("N")){
							subMenu = "<div class=\"menuwarp\"><ul>";
							for(MenuItemVO submenu:menulist){
								if(submenu.getDiag_majr_code().equals(row.getDiag_majr_code()) && !submenu.getDiag_majr_code().equals(submenu.getDiag_minr_code())){
									subMenu += String.format("<li><span><a class=\"btn_pol_status\" style=\"cursor:pointer;\" %s>%s</a></span></li>"
											, String.format("majCode=\"%s\" minCode=\"%s\" polCode=\"\"  loginType=\"%s\" menutype=\"P\" buseoindc=\"%s\""
													, submenu.getDiag_majr_code()
													, submenu.getDiag_minr_code()
													, requestType
													, submenu.getBuseo_indc())
											, submenu.getDiag_desc());
									
									List<EgovMap> polList = getPolMenuList(submenu.getDiag_majr_code(), submenu.getDiag_minr_code(), polMenulist);
									for(EgovMap pol:polList){
										subMenu += String.format("<li><a class=\"btn_pol_status\" style=\"cursor:pointer;\" %s>%s</a></li>"
												, String.format("majCode=\"%s\" minCode=\"%s\" polCode=\"%s\"  loginType=\"%s\" menutype=\"P\" buseoindc=\"%s\""
														, submenu.getDiag_majr_code()
														, submenu.getDiag_minr_code()
														, pol.get("secpolid")
														, requestType
														, pol.get("buseoindc"))
												, pol.get("poldesc"));
										
									}
								}
							}
							
							subMenu += "</ul></div>";
							str.append(String.format("<li><a class='btn_pol_status topmenu%s' %s subMenu='%s'><span>%s</span></a></li>\r\n"
													, row.getDiag_majr_code().equals(MenuInfo) ? " topmenuSel" : ""
													, String.format("majCode='%s' minCode='' polCode='' loginType='%s' menutype='P' buseoindc='%s' "
															, row.getDiag_majr_code()
															, requestType
															, row.getBuseo_indc())
													, subMenu
													, row.getDiag_desc()));
						}
					}
				}
				if(TypeList.size() > 1){
					if((!loginInfo.getTitlecode().equals("") && Integer.parseInt(loginInfo.getTitlecode()) <= 6) || !loginInfo.getRole_code().equals("3") || (loginInfo.getIsProxy().equals("Y") && loginInfo.getIsProxyDirector().equals("Y")) ){
						for(MenuItemVO row:menulist)
						{
							if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
								if(row.getBuseo_indc().equals("Y")){
									str.append(String.format("<li><a class='btn_pol_status topmenu%s' %s><span>%s</span></a></li>\r\n"
															, row.getDiag_majr_code().equals(MenuInfo) ? " topmenuSel" : ""
															, String.format("majCode='%s' minCode='' polCode='' loginType='%s' menutype='P' buseoindc='%s'"
																	, row.getDiag_majr_code()
																	, requestType
																	, row.getBuseo_indc())  
															, row.getDiag_desc()));
								}
							}
						}
					}
				}
				
				subMenu = "<div class=\"menuwarp\"><ul>";
				subMenu += "<li><span><a href=\"/report/sheetStar.do\">일간 보고서</a></span></li></li>";
				subMenu += "<li><span><a href=\"/report/monthSheetStar.do\">기간별 진단 보고서</a></span></li>";
				if(!requestType.equals("personal")){
					subMenu += "<li><span><a href=\"/report/dataSheetStar.do\">기간별 데이터 추출</a></span></li>";
				}
				subMenu += "</ul></div>";
				
				str.append(String.format("<li><a class='topmenu%s' href='/report/sheetStar.do' subMenu='%s'><span>진단보고서</span></a></li>\r\n"
						, MenuInfo == "report" ||  MenuInfo == "monthreport" ? " topmenuSel" : ""
						, subMenu));
			
			}
			
		    
			subMenu = "<div class=\"menuwarp\"><ul>";
			subMenu += "<li><span><a href=\"/man/noticelist.do\">공지사항</a></span></li>";
			subMenu += "<li><span><a href=\"/man/faqlist.do\">FAQ</a></span></li>";
			subMenu += "<li><span><a href=\"/man/qnaList.do\">Q&A</a></span></li>";
			subMenu += "</ul></div>";
			str.append(String.format("<li><a class='topmenu%s' href='/man/noticelist.do' subMenu='%s'><span>공지사항</span></a></li>\r\n"
												, MenuInfo == "notice" ? " topmenuSel" : ""
												, subMenu));
			
			subMenu = "<div class=\"menuwarp\"><ul>";
			subMenu += "<li><span><a href=\"/server/serverPolLogList.do\">서버조치이행관리</a></span></li>";
			subMenu += "</ul></div>";
			str.append(String.format("<li><a class='topmenu%s' href='/server/serverPolLogList.do' subMenu='%s'><span>서버이행관리</span></a></li>\r\n"
												, MenuInfo == "server" ? " topmenuSel" : ""
												, subMenu));
			
			if(!loginInfo.getRole_code().equals("3")){
				
				subMenu = "<div class=\"menuwarp\"><ul>";
				subMenu += "<li><span><a href=\"/man/sanctconfig.do\">제재조치 설정</a></span></li>";
				subMenu += "<li><span><a href=\"/man/sanctlist.do\">소명/제재 조치(수동)</a></span></li>";
				subMenu += "<li><span><a href=\"/man/polmanger.do\">지수화정책관리</a></span></li>";
				subMenu += "<li><span><a href=\"/pol/polanalysis.do\">정책분석</a></span></li>";
				subMenu += "<li><span><a href=\"/stat/statistpoltotallist.do\">통계분석</a></span></li>";
				subMenu += "<li><span><a href=\"/man/dignosisItemlist.do\">진단항목 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/user/exceptlist.do\">소명 예외자 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/exception/exceptionEmpNoList.do\">예외사번 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/exception/exceptionMailList.do\">메일발송 예외자 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/user/proxylist.do\">부서담담자 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/man/userlist.do\">권한 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/man/blockuserlist.do\">차단계정 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/man/codelist.do\">코드 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/securityDay/sdCheckListGroupList.do\">SecurityDay 점검표 그룹 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/securityDay/sdCheckList.do\">SecurityDay 점검표 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/group/groupInfoList.do\">그룹 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/man/mailmanagerlist.do\">메일 발송 관리</a></span></li>";
				subMenu += "<li><span><a href=\"/exception/reqExIpInfoList.do\">예외  관리</a></span></li>";
				subMenu += "<li><span><a href=\"/man/userSearchList.do\">사용자 조회</a></span></li>";
				subMenu += "<li><span><a href=\"/man/typeReassignList.do\">담당자 재 지정</a></span></li>";
				subMenu += "<li><span><a href=\"/man/statInfoList.do\">기관별 통계</a></span></li>";
				subMenu += "<li><span><a href=\"/handy/ExanalPolList.do\">수동업로드 관리</a></span></li>";
				subMenu += "</ul></div>";
				str.append(String.format("<li><a class='topmenu%s' href='/man/sanctconfig.do' subMenu='%s'><span>관리자</span></a></li>\r\n"
												, MenuInfo == "admin" ? " topmenuSel" : ""
												, subMenu));
			}
			
			
			request.setAttribute("loginName", loginInfo.getUsername());
			request.setAttribute("topMenu", str.toString());
		
			String topNotice = "* 알림공지가 없습니다.";
			NoticeVO topNoticeInfo = comService.getMainNoticeInfo();
			if(topNoticeInfo != null){
				if(topNoticeInfo.getSq_no() > 0){
					
					topNotice = String.format("<a class='top_notice_view' nseq='%s' style='cursor:pointer;'>%s</a>", topNoticeInfo.getSq_no(), topNoticeInfo.getTitle().length() > 40 ? String.format("%s ...", topNoticeInfo.getTitle().subSequence(0, 40)) : topNoticeInfo.getTitle() );
				}
			}
			
			request.setAttribute("topNotice", topNotice);
			
			/**
			 * 전체 메뉴 생성
			 * 
			 */
			/*List<EgovMap> polMenulist = comService.getPolMenuAllList();
			
			StringBuffer menuMap = new StringBuffer();
			menuMap.append("<div class=\"menuwarp\">");
			
			for(MenuItemVO menu:menulist){
				if(menu.getDiag_majr_code().equals(menu.getDiag_minr_code())){
					menuMap.append("<ul>");
					menuMap.append(String.format("<h2><a class='btn_pol_status' style='cursor:pointer;' %s>%s</a></h2>"
							, String.format("majCode='%s' minCode='' polCode='' loginType='%s' menutype='P' buseoindc='%s'"
									, menu.getDiag_majr_code()
									, requestType
									, menu.getBuseo_indc())  
							, menu.getDiag_desc()));
					for(MenuItemVO submenu:menulist){
						if(submenu.getDiag_majr_code().equals(menu.getDiag_majr_code()) && !submenu.getDiag_majr_code().equals(submenu.getDiag_minr_code())){
							menuMap.append(String.format("<li><span><a class='btn_pol_status' style='cursor:pointer;' %s>%s</a></span></li>"
									, String.format("majCode='%s' minCode='%s' polCode=''  loginType='%s' menutype='P' buseoindc='%s'"
											, submenu.getDiag_majr_code()
											, submenu.getDiag_minr_code()
											, requestType
											, submenu.getBuseo_indc())
									, submenu.getDiag_desc()));
							List<EgovMap> polList = getPolMenuList(submenu.getDiag_majr_code(), submenu.getDiag_minr_code(), polMenulist);
							for(EgovMap pol:polList){
								menuMap.append(String.format("<li><a class='btn_pol_status' style='cursor:pointer;' %s>%s</a></li>"
										, String.format("majCode='%s' minCode='%s' polCode='%s'  loginType='%s' menutype='P' buseoindc='%s'"
												, submenu.getDiag_majr_code()
												, submenu.getDiag_minr_code()
												, pol.get("secpolid")
												, requestType
												, pol.get("buseoindc"))
										, pol.get("poldesc")));
							}
						}
					}
					
					menuMap.append("</ul>");
				}
			}
			menuMap.append("<ul>");
			menuMap.append(String.format("<h2><a href=\"/report/sheetStar.do\" style='color:#5091d3;'>진단보고서</a></h2>"));
			menuMap.append(String.format("<li><span><a href='/report/sheetStar.do'>%s</a></span></li>", "일간 보고서"));
			menuMap.append(String.format("<li><span><a href='/report/monthSheetStar.do'>%s</a></span></li>", "기간별 진단 보고서"));
			if(!requestType.equals("personal")){
				menuMap.append(String.format("<li><span><a href='/report/dataSheetStar.do'>%s</a></span></li>", "기간별 데이터 추출"));
			}
			menuMap.append("</ul>");
			menuMap.append("<ul>");
			menuMap.append(String.format("<li><span><a href='/man/noticelist.do'>%s</a></span></li>"
					, "공지사항"));
			menuMap.append(String.format("<li><span><a href='/man/faqlist.do'>%s</a></span></li>"
					, "FAQ"));
			menuMap.append(String.format("<li><span><a href='/man/qnaList.do'>%s</a></span></li>"
					, "Q&A"));
			menuMap.append("</ul>");
			
			menuMap.append("</div>");
			request.setAttribute("menuMap", menuMap.toString());*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static List<EgovMap> getPolMenuList(String majcode, String mincode, List<EgovMap> polMenulist){
		List<EgovMap> polList = new ArrayList<EgovMap>();
		for(EgovMap row:polMenulist){
			if(row.get("majcode").equals(majcode) && row.get("mincode").equals(mincode)){
				polList.add(row);
			}
		}
		return polList;
	}
	
	private static HashMap<String, String> getConvertMainparamString(String userType, String org_code, String org_nm, String emp_nm){
		HashMap<String, String> retVal = new HashMap<String, String>();
		
		if(userType.equals("ADMIN")){
			retVal.put("type", "sa");
			retVal.put("typeName", "관리자 (정보보안단)");
		}else if(userType.equals("PERSONAL")){
			retVal.put("type", "personal");
			retVal.put("typeName", " 개인 ");
		}else if(userType.equals("CAPTAIN")){// || userType.equals("PROXY")){
			retVal.put("type", org_code);
			retVal.put("typeName", org_nm);
			
			
		}else if(userType.equals("PROXY")){
			retVal.put("type", org_code);
			retVal.put("typeName",String.format("%s[부]",org_nm));
		}
		
		return retVal;
	}
	
	public static List<EgovMap> getListFromPolMenuForString(List<EgovMap> contList, String majcode, String mincode)
	{
		List<EgovMap> list = new ArrayList<EgovMap>();
		for(EgovMap cont : contList)
		{
			if(cont.get("majcode").equals(majcode) && cont.get("mincode").equals(mincode))
			{
				list.add(cont);
			}
		}
		
		return list;
		
	}
	/**
	 * 정책정보 조회 메뉴
	 * @param request
	 * @param MajCode
	 * @param Mincode
	 * @param polCode
	 * @param comService
	 */
	public static void polLeftMenuToString(HttpServletRequest request, String MajCode, String Mincode, String polCode, CommonService comService){
		
		try{
			StringBuffer str = new StringBuffer();
			
			List<MenuItemVO> menulist = comService.getSolMenuList();	
			List<EgovMap> polMenulist = comService.getPolMenuAllList();
			String Notice_String = "TEST 설명 입니다.";
			String requestType = (String)request.getSession().getAttribute("loginType");
			for(MenuItemVO row:menulist){
				if(row.getDiag_majr_code().equals(MajCode) && row.getDiag_minr_code().equals(MajCode)){
					Notice_String = row.getDiag_notice().replaceAll("(\r\n|\n|\r)", "<br />");
				}
			}
			
			for(MenuItemVO row:menulist)
			{
				
				if(row.getDiag_majr_code().equals(MajCode)){
					if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
						str.append(String.format("<h3><a class='btn_pol_status %s' %s style='cursor:pointer;'>%s</a></h3>\r\n"
								, row.getDiag_minr_code().equals(Mincode) ? " on" : ""
								, String.format("majCode='%s' minCode='%s' polCode=''  loginType='%s' menutype='P' buseoindc='%s'"
										, row.getDiag_majr_code()
										, row.getDiag_minr_code()
										, requestType
										, row.getBuseo_indc()) 
								, row.getDiag_desc()));
						HashMap<String, String> hmap = new HashMap<String, String>();
						hmap.put("majcode", row.getDiag_majr_code());
						hmap.put("mincode", row.getDiag_minr_code());
						if(row.getDiag_minr_code().equals(Mincode)){
							Notice_String = row.getDiag_notice().replaceAll("(\r\n|\n|\r)", "<br />");
						}
						List<EgovMap> sList = CommonUtil.getListFromPolMenuForString(polMenulist, hmap.get("majcode"), hmap.get("mincode"));
						if(sList.size() > 0){
							str.append(String.format("<ul class='left_submenu' style='text-align:left;display:%s;'>\r\n", row.getDiag_minr_code().equals(Mincode) ? "" : "none" ));
							for(EgovMap srow:sList)
							{
								
								str.append(String.format("<li style='padding-left:20px;'><a class='btn_pol_status %s' %s style='cursor:pointer;'>%s</a></li>"
										, srow.get("secpolid").equals(polCode) ? " on" : ""
										, String.format("majCode='%s' minCode='%s' polCode='%s'  loginType='%s' menutype='P' buseoindc='%s'"
												, row.getDiag_majr_code()
												, row.getDiag_minr_code()
												, srow.get("secpolid")
												, requestType
												, srow.get("buseoindc"))
										,srow.get("poldesc")));
								if(srow.get("secpolid").equals(polCode)){
									Notice_String = srow.get("polnotice").toString().replaceAll("(\r\n|\n|\r)", "<br />");
								}
							}
							str.append("</ul>\r\n");
						}
							
						
					}
					
				}
				
			}
				
			request.setAttribute("polLeftMenu", str.toString());
			request.setAttribute("polNoticeString", Notice_String);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 관리자 메뉴 생성 - 수동제재메뉴(조직도 추가됨)
	 * @param request
	 * @param MenuInfo
	 */
	public static void adminLeftMenuToString(HttpServletRequest request, LeftMenuInfo MenuInfo, CommonService comService){
		
		try{
			StringBuffer str = new StringBuffer();
			
			str = CommonUtil.createAdminLeftMenu(request, MenuInfo, comService);
			request.setAttribute("adminLeftMenu", str.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static StringBuffer createAdminLeftMenu(HttpServletRequest request, LeftMenuInfo MenuInfo, CommonService comService){
		try{
			StringBuffer str = new StringBuffer();
						
			UrlPathHelper urlPathHelper = new UrlPathHelper(); 
			String urlstr = urlPathHelper.getOriginatingRequestUri(request); 
			
			str.append(String.format("<h3><a href='/man/sanctconfig.do' %s>제재조치 설정</a></h3>\r\n", MenuInfo == LeftMenuInfo.SANC ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/sanctlist.do' %s>소명/제재 조치(수동)</a></h3>\r\n", MenuInfo == LeftMenuInfo.SANCTION ? "class='on'" : ""));
			if(MenuInfo == LeftMenuInfo.SANCTION){
				str.append("<div class='chart'>\r\n");
				str.append(" <h3>조직 검색</h3>\r\n");
				str.append("<ul id='orgbrowser' class='filetree chart_list'>\r\n");
				str.append(CreateOrgTree(comService));
				
				str.append("<ul>\r\n");
				str.append("</div>\r\n");
			}
			
			str.append(String.format("<h3><a href='/man/polmanger.do' %s>지수화정책관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.POL ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/pol/polanalysis.do' %s>정책분석</a></h3>\r\n", MenuInfo == LeftMenuInfo.POLSTATIC ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/stat/statistpoltotallist.do' %s>통계분석</a></h3>\r\n", MenuInfo == LeftMenuInfo.STATIC ? "class='on'" : ""));
			if(MenuInfo == LeftMenuInfo.STATIC){
				str.append("<div class='chart'>\r\n");
				str.append(" <h3>조직 검색</h3>\r\n");
				str.append("<ul id='orgbrowser' class='filetree chart_list'>\r\n");
				str.append(CreateOrgTree(comService));
				str.append("<ul>\r\n");
				str.append("</div>\r\n");
			}
			//str.append(String.format("<ul class='left_submenu' style='text-align:left;display:%s;'>",MenuInfo == LeftMenuInfo.STATIC ? "" : "none" ));
			//str.append(String.format("<li style='padding-left:20px;'><a href='/stat/statistorglist.do' class='%s'>조직별 통계</a></li>", urlstr.contains("/stat/statistorglist") ? "on" : ""));
			//str.append(String.format("<li style='padding-left:20px;'><a href='/stat/statistuserlist.do' class='%s'>직원별 통계</a></li>", urlstr.contains("/stat/statistuserlist") ? "on" : ""));
			//str.append(String.format("<li style='padding-left:20px;'><a href='/stat/statistpoltotallist.do' class='%s'>정책별/조직별 통계</a></li>", urlstr.contains("/stat/statistpoltotallist") ? "on" : ""));
			//str.append("</ul>");
			
			str.append(String.format("<h3><a href='/man/dignosisItemlist.do' %s>진단항목 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.DIGNOSIS ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/user/exceptlist.do' %s>소명 예외자 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.EXCEPT ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/exception/exceptionIpList.do' %s>예외IP 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.EXCEPTIP ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/exception/exceptionEmpNoList.do' %s>예외사번 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.EXCEPTEMPNO ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/exception/exceptionMailList.do' %s>메일발송 예외자 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.EXCEPTMAIL ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/user/proxylist.do' %s>부서담당자 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.PROXY ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/userlist.do' %s>권한 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.USER ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/blockuserlist.do' %s>차단계정 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.BLOCK ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/codelist.do' %s>코드 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.CODE ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/securityDay/sdCheckListGroupList.do' %s>SecurityDay 점검표 그룹 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.SDCHECKLISTGROUP ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/securityDay/sdCheckList.do' %s>SecurityDay 점검표 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.SDCHECKLIST ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/group/groupInfoList.do' %s>그룹 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.GROUPINFO ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/mailmanagerlist.do' %s>메일 발송 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.MAILINFO ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/exception/reqExIpInfoList.do' %s>예외  관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.REQEXCEPTIP ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/userSearchList.do' %s>사용자 조회</a></h3>\r\n", MenuInfo == LeftMenuInfo.USERSEARCH ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/typeReassignList.do' %s>담당자 재지정</a></h3>\r\n", MenuInfo == LeftMenuInfo.REASSIGN ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/statInfoList.do' %s>기관별 통계</a></h3>\r\n", MenuInfo == LeftMenuInfo.STAT ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/handy/ExanalPolList.do' %s>수동업로드 관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.HANDY ? "class='on'" : ""));			
			return str;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 관리자 메뉴 생성 - 일반메뉴
	 * @param request
	 * @param MenuInfo
	 */
	public static void adminLeftMenuToString(HttpServletRequest request, LeftMenuInfo MenuInfo){
		
		try{
			
			StringBuffer str = new StringBuffer();
			str = CommonUtil.createAdminLeftMenu(request, MenuInfo, null);
			request.setAttribute("adminLeftMenu", str.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 공지사항 메뉴
	 * @param request
	 * @param MenuInfo
	 */
	public static void noticeLeftMenuToString(HttpServletRequest request, LeftMenuInfo MenuInfo){
			
		try{
			StringBuffer str = new StringBuffer();
			str.append(String.format("<h3><a href='/man/noticelist.do' %s>공지사항</a></h3>\r\n", MenuInfo == LeftMenuInfo.NOTICE ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/faqlist.do' %s>FAQ</a></h3>\r\n", MenuInfo == LeftMenuInfo.FAQ ? "class='on'" : ""));
			str.append(String.format("<h3><a href='/man/qnaList.do' %s>Q&A</a></h3>\r\n", MenuInfo == LeftMenuInfo.QNA ? "class='on'" : ""));
	
			request.setAttribute("noticeLeftMenu", str.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 보고서 외쪽메뉴
	 * @param request
	 * @param MenuInfo
	 */
	public static void reportLeftMenuToString(HttpServletRequest request, LeftMenuInfo MenuInfo){
		
		try{
			StringBuffer str = new StringBuffer();
			str.append(String.format("<h3><a href='/report/sheetStar.do' %s>일간 보고서</a></h3>\r\n", MenuInfo == LeftMenuInfo.REPORT_DAY ? "class='on'" : ""));
			if(MenuInfo == LeftMenuInfo.REPORT_DAY){
				str.append("<div class='chart'>\r\n");
				str.append(" <h3>조직 검색</h3>\r\n");
				str.append("<ul id='orgbrowser' class='filetree chart_list'>\r\n");
				str.append(request.getAttribute("orgtree"));
				str.append("<ul>\r\n");
				str.append("</div>\r\n");
			}
			str.append(String.format("<h3><a href='/report/monthSheetStar.do' %s>기간별 진단 보고서</a></h3>\r\n", MenuInfo == LeftMenuInfo.REPORT_MONTH ? "class='on'" : ""));
			if(MenuInfo == LeftMenuInfo.REPORT_MONTH){
				str.append("<div class='chart'>\r\n");
				str.append(" <h3>조직 검색</h3>\r\n");
				str.append("<ul id='orgbrowser' class='filetree chart_list'>\r\n");
				str.append(request.getAttribute("orgtree"));
				str.append("<ul>\r\n");
				str.append("</div>\r\n");
			}
			String requestType = (String)request.getSession().getAttribute("loginType");
			if(!requestType.equals("personal")){
				str.append(String.format("<h3><a href='/report/dataSheetStar.do' %s>기간별 데이터 추출</a></h3>\r\n", MenuInfo == LeftMenuInfo.REPORT_DATA ? "class='on'" : ""));
				if(MenuInfo == LeftMenuInfo.REPORT_DATA){
					str.append("<div class='chart'>\r\n");
					str.append(" <h3>조직 검색</h3>\r\n");
					str.append("<ul id='orgbrowser' class='filetree chart_list'>\r\n");
					str.append(request.getAttribute("orgtree"));
					str.append("<ul>\r\n");
					str.append("</div>\r\n");
				}
			}
			
			/*
			 <div class="chart">
			<h3>조직도 검색</h3>
				<ul id="orgbrowser" class="filetree chart_list">
				<%=request.getAttribute("orgtree") %>
				</ul>
		</div>
			 * */
			
			
			request.setAttribute("reportLeftMenu", str.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 정책정보 - 대분류-중분류-정책명
	 * */
	public static String getPolString(HttpServletRequest request, HashMap<String, String> hmap, CommonService comService){
		
		try{
			StringBuffer str = new StringBuffer();
						
			//ServletContext servletContext = request.getServletContext();
			//List<EgovMap> polMenuList = (List<EgovMap>) servletContext.getAttribute("polMenuList");
			List<EgovMap> polMenuList = comService.getPolMenuAllList();
			
			String pol1 = "";
			for(EgovMap row:polMenuList){
				if(row.get("majcode").equals(hmap.get("majcode")) && row.get("majcode").equals(row.get("mincode"))){
					pol1 = row.get("diagdesc").toString();
					break;
				}
			}
			
			str.append(String.format("%s", pol1));
			String pol2 = "";
			for(EgovMap row:polMenuList){
				
				if(row.get("majcode").equals(hmap.get("majcode")) && row.get("mincode").equals(hmap.get("mincode"))){
					pol2 = row.get("diagdesc").toString();
					break;
				}
			}
			
			str.append(String.format("%s",  pol2.length() > 0 ? " >> " + pol2 : ""));
		
			String pol3 = "";
			for(EgovMap row:polMenuList){
				if(row.get("majcode").equals(hmap.get("majcode")) && row.get("mincode").equals(hmap.get("mincode")) &&  row.get("secpolid").equals(hmap.get("polcode")) ){
					pol3 = row.get("poldesc").toString();
					break;
				}
			}
			str.append(String.format("%s",  pol3.length() > 0 ? " >> " + pol3 : ""));
			
			
			return str.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 관리자 용 조직트리
	 * @param comService
	 * @return
	 */
	public static String CreateOrgTree(CommonService comService){
		try{
			StringBuffer str = new StringBuffer();
			
			HashMap<String, Object> orgInfo = null;
			
			
			String root = MajrCodeInfo.RootOrgCode;
			
			OrgGroupVO rootInfo = comService.getOrgInfo(root);
		
			str.append(String.format("<li class='open'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
										, rootInfo.getIs_suborg().equals("Y") ? "folder" : "file"
										, rootInfo.getIs_suborg().equals("Y") ? "1" : "2"
										, rootInfo.getOrg_code()
										, "class='ON'"
										, rootInfo.getOrg_nm()));
			str.append("<ul>\r\n");
			List<OrgGroupVO> orgList = comService.getOrgInfoListForManager(rootInfo.getOrg_code());//comService.getOrgInfoList(orgInfo);
			//최상위코드 조회
		    for(OrgGroupVO org:getOrgListForUpperCode(orgList, rootInfo.getOrg_code())){
		    	str.append(String.format("<li class='%s sel_folder' uorg_code='%s'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
		    									, "closed"
		    									, org.getOrg_code()
		    									, org.getIs_suborg().equals("Y") ? "folder" : "file"
		    									, org.getIs_suborg().equals("Y") ? "1" : "2"
		    									, org.getOrg_code()
		    									, ""
		    									, org.getOrg_nm()));
		    	if(org.getIs_suborg().equals("Y")){
		    		str.append("<ul>\r\n");
			    	//setOrgCodeInfo(orgList, org.getOrg_code(), str, false);
			    	str.append("</ul>\r\n");
		    	}
		    	str.append("</li>\r\n");
		    }
		    str.append("</ul>\r\n");
	    	str.append("</li>\r\n");
			
	    	return str.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 조직트리 생성 
	 * root : 조직 Root
	 * isUser : 일반사용자 여부 : 일반 사용자일경우 자신의 조직 및 상위만조회
	 **/
	public static void getCreateOrgTree(HttpServletRequest request, CommonService comService, String root, boolean isUser, String empno, String isKpc){
		try{
			StringBuffer str = new StringBuffer();
			String userOrgIndex = "";
			String selectedOrg = "";
			boolean isfolder = true;
			
			if(root.equals(MajrCodeInfo.RootOrgCode)){
				root = "";
			}
			
			HashMap<String, Object> orgInfo = null;
			
			List<OrgGroupVO> orgList = null;
			if(isUser){
				//일반사용자일경우 자신의 조직 및 상위만 조회
				//내조직만 조회 가능
				//System.out.println(empno + ":empno");
				EgovMap userOrgInfo = comService.getOrgInfoForUser(empno);
				userOrgIndex = userOrgInfo.get("userupperorg").toString();
				//System.out.println(userOrgIndex + ":userOrgIndex");
				orgInfo = new HashMap<String, Object>();
				selectedOrg = userOrgInfo.get("orgcode").toString();
				orgInfo.put("orglist", CommonUtil.SplitToString(userOrgIndex, ","));
				orgInfo.put("isKpc",isKpc);
				root = MajrCodeInfo.RootOrgCode;
			    orgList = comService.getOrgInfoList(orgInfo);
			    isfolder = false;
			}else{
				//Root 가 포함된 모든트리 조회
				//Root 가 공백이면 KT
				if(root.equals("")){
					root = MajrCodeInfo.RootOrgCode;
					orgInfo = new HashMap<String, Object>();
					orgInfo.put("orglist", "");
					orgInfo.put("isKpc",isKpc);
					selectedOrg = root;
					orgList = comService.getOrgInfoListForManager(root);
					isfolder = true;
				}else{
					//조직장이거나 대무자일경우
					//자기부서무터 조회 가능
					selectedOrg = root;
					orgList = comService.getCapOrgInfoList(root);
					root = MajrCodeInfo.RootOrgCode;
					isfolder = false;
				}
				
				
				//select fn_get_userorgindex('391262')
			}
			//root = MajrCodeInfo.RootOrgCode;
			//System.out.println(root + ":root org...");
			
			request.setAttribute("orgroot", selectedOrg);
			request.setAttribute("orgtype", isUser ? "2" : "1");
			/*
			boolean disabled = true;
			
			if(disabled){
				disabled = selectedOrg.equals(MajrCodeInfo.RootOrgCode) ? true : false;
			}
			*/		
		
			OrgGroupVO rootInfo = comService.getOrgInfo(root);
			str.append(String.format("<li><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
										, rootInfo.getIs_suborg().equals("Y") ? "folder" : "file"
										, rootInfo.getIs_suborg().equals("Y") ? "1" : "2"
										, selectedOrg.equals(MajrCodeInfo.RootOrgCode) ? rootInfo.getOrg_code() : selectedOrg 
										, isUser ? "" : selectedOrg.equals(MajrCodeInfo.RootOrgCode) ? "class='ON'" : ""
										, rootInfo.getOrg_nm()));
			str.append("<ul>\r\n");
			
			//최상위코드 조회
		    for(OrgGroupVO org:getOrgListForUpperCode(orgList, root)){
		    	str.append(String.format("<li class='%s%s' uorg_code='%s'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
		    									, isUser ? "open" : org.getIs_orgloctype().equals("H") ? "open" : "closed"
		    									, isfolder ? isUser ? "" : " sel_folder" : ""
		    									, org.getOrg_code()
		    									, org.getIs_suborg().equals("Y") ? "folder" : "file"
		    									, org.getIs_suborg().equals("Y") ? "1" : "2"
		    									, org.getIs_orgmember().equals("N") ? selectedOrg : org.getOrg_code()
		    									, isUser ? org.getIs_suborg().equals("Y") ? "" : "class='ON'" : org.getOrg_code().equals(selectedOrg) ? "class='ON'" : ""
		    									, org.getOrg_nm()));
		    	if(org.getIs_suborg().equals("Y")){
		    		str.append("<ul>\r\n");
			    	setOrgCodeInfo(orgList, org.getOrg_code(), str, isUser, selectedOrg);
			    	str.append("</ul>\r\n");
		    	}
		    	str.append("</li>\r\n");
		    }
		    str.append("</ul>\r\n");
	    	str.append("</li>\r\n");
			
			request.setAttribute("orgtree", str.toString());
			request.setAttribute("isuser", isUser ? "1" : "0");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getCreateSubOrgNode(List<OrgGroupVO> oList, String upperCode, boolean isUser){
		StringBuffer str = new StringBuffer();
		
		setOrgCodeInfo(oList, upperCode, str, isUser, "");
		
		return str.toString();
	}
	
	private static void setOrgCodeInfo(List<OrgGroupVO> oList, String upperCode, StringBuffer str, boolean isUser, String sel_org_code){
		for(OrgGroupVO org:getOrgListForUpperCode(oList, upperCode)){
	    	str.append(String.format("<li class='%s'><span class='%s'><p class='sel_text' stype='%s' orgcode='%s'> <a %s>%s</a></p></span>\r\n"
	    									, isUser ? "open" : org.getIs_orgloctype().equals("H") ? "open" : "closed"
	    									, org.getIs_suborg().equals("Y") ? "folder" : "file"
	    									, org.getIs_suborg().equals("Y") ? "1" : "2"
	    									, org.getIs_orgmember().equals("N") ? sel_org_code : org.getOrg_code()
	    									, isUser ? org.getIs_suborg().equals("Y") ? "" : "class='ON'" : org.getOrg_code().equals(sel_org_code) ? "class='ON'" : ""
	    									, org.getOrg_nm()));
	    	if(org.getIs_suborg().equals("Y")){
	    		str.append("<ul>\r\n");
		    	setOrgCodeInfo(oList, org.getOrg_code(), str, isUser, sel_org_code);
		    	str.append("</ul>\r\n");
	    	}
	    	str.append("</li>\r\n");
	    }
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
	
	public static MemberVO getMemberInfo(){
		if (EgovUserDetailsHelper.isAuthenticated()) {
			try{
				MemberVO memVO = (MemberVO)EgovUserDetailsHelper.getAuthenticatedUser();
				return memVO;
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
			
		} else {
		    return null;
		}
	}
	
	public static MemberVO getMemberInfo(HttpServletRequest request){
		if (EgovUserDetailsHelper.isAuthenticated()) {
			try{
				MemberVO memVO = (MemberVO)EgovUserDetailsHelper.getAuthenticatedUser();
				if(!memVO.getRole_code().equals("3")){
					if(!memVO.getIp().equals(CommonUtil.getLocalIPAddress(request))){
						memVO.setRole_code("3");
					}
				}
				return memVO;
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
			
		} else {
		    return null;
		}
	}
	
	public static String convertStringToCommaString(Integer intVal){
		return String.format("%,d", intVal);
	}
	
	/**
	 * 서버이행관리 메뉴
	 * @param request
	 * @param MenuInfo
	 */
	public static void serverLeftMenuToString(HttpServletRequest request, LeftMenuInfo MenuInfo){
			
		try{
			StringBuffer str = new StringBuffer();
			str.append(String.format("<h3><a href='/server/serverPolLogList.do' %s>서버 이행관리</a></h3>\r\n", MenuInfo == LeftMenuInfo.SERVER ? "class='on'" : ""));
			
			request.setAttribute("serverLeftMenu", str.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


