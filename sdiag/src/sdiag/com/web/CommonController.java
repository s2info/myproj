package sdiag.com.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sdiag.com.service.CommonService;
import sdiag.com.service.FileVO;
import sdiag.com.service.SdiagProperties;
import sdiag.board.service.NoticeService;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import sdiag.pol.service.PolicyService;
import sdiag.util.CommonUtil;
import sdiag.util.FileManager;
import sdiag.util.LeftMenuInfo;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class CommonController {
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	// 파일 처리 클래스 선언
	@Resource(name = "FileManager")
	private FileManager fileManager;
	
	@Resource(name = "PolicyService")
	private PolicyService polService;
	
	@Resource(name = "NoticeService")
	private NoticeService noticeService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	/**
	 * 조직 상위 리스트 조직명 조회 
	 * @param request
	 * @param orgcode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/common/getupperorgnames.do")
	public void getupperorgnames(HttpServletRequest request,
							String orgcode,
							HttpServletResponse response) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		String  upperorgnames = "";
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			upperorgnames = comService.getUpperOrgNames(orgcode);
			
			hMap.put("ISOK", true);
			hMap.put("uppernames", upperorgnames);
			hMap.put("MSG", msg);
			
		}
		catch(Exception e){
			e.printStackTrace();
			msg = e.toString();
			hMap.put("ISOK", false);
			hMap.put("MSG", msg);
		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
	}	
	
	
	// 파일다운로드
	@RequestMapping(value="/appr/filedownload.do")
	public void apprfiledownload(HttpServletRequest request, HttpServletResponse response) throws Exception{	
		String apprid = request.getParameter("apprid") == null ? "" : request.getParameter("apprid");
		String svrFile="";
		String locFile="";
		try
		{
			if(!apprid.equals(null)){
				EgovMap apprInfo = polService.getApplInfo(apprid);
				svrFile = apprInfo.get("fileloc").toString();
				locFile = apprInfo.get("filename").toString();
			}
			
			if(svrFile.length() <= 0 || locFile.length() <= 0){
				throw new Exception();
			}
			FileVO fvo = new FileVO();
			fvo.setEvid_file_name(locFile);
			fvo.setEvid_file_loc(svrFile);
			fileManager.fileDownloadProcess(request, response, fvo, SdiagProperties.getProperty("Globals.apprfilePath"));
		
			
		}
		catch(Exception e){
			e.printStackTrace();
		
		}
	
	}
	
	@RequestMapping("/appr/fileDelete.do")
	public void fileDelete(HttpServletRequest request,
							String apprid,
							HttpServletResponse response) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		try
		{
			EgovMap apprInfo = polService.getApplInfo(apprid);
			
			FileVO fvo = new FileVO();
			fvo.setEvid_file_loc(apprInfo.get("fileloc").toString());
			fileManager.fileDelete(fvo, SdiagProperties.getProperty("Globals.apprfilePath"));
			
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    	hashMap.put("appr_id", apprid );
	    	hashMap.put("file_name", "" );
	    	hashMap.put("file_loc", "");
	    	int retVal = polService.setUpdateApprFileInfo(hashMap);
			
			hMap.put("ISOK", true);
			hMap.put("MSG", msg);
			
		}
		catch(Exception e){
			e.printStackTrace();
			msg = e.toString();
			hMap.put("ISOK", false);
			hMap.put("MSG", msg);
		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
	}	
	
	
	@RequestMapping("/common/formFileUpload.do")
	public void communityMutiFileUpload(final MultipartHttpServletRequest multiRequest,
								HttpServletResponse response,
								ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		try
		{
			List<MultipartFile> files = multiRequest.getFiles("inputFile");
			if (null != files && files.size() > 0){
				FileVO fileVo = new FileVO();
				Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
				
				for (MultipartFile multipartFile : files){
					fileMap.clear();
					fileMap.put("inputFile", multipartFile);
				
					//FileVO vo = fileManager.fileUploadProcess(multipartFile, "ATT_", fileVo);
					//System.out.println(vo.getEvid_file_name() + "][" + vo.getEvid_file_loc());
				}
			}
			
			
			hMap.put("ISOK", true);
			hMap.put("MSG", msg);
			
		}
		catch(Exception e){
			e.printStackTrace();
			msg = e.toString();
			hMap.put("ISOK", false);
			hMap.put("MSG", msg);
		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
		/*
		List<HashMap<String,String>> returnList = null; 
				
		if (null != files && files.size() > 0){
			FileVO fileVo = new FileVO();
			FileOneVO fileOneVO = new FileOneVO();
			Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
			returnList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> returnMap = null;
			
			for (MultipartFile multipartFile : files){
				returnMap = new HashMap<String,String>();
				fileMap.clear();
				fileMap.put("inputFile", multipartFile);
			
				FileVO vo = fileUtil.fileUploadProcess(fileMap, "BBS_", fileVo);
				
				if (vo != null){					
					String fno = communityService.fileReg(cno, vo);
					
					returnMap.put("fileDispName", (vo.getFileDispName())[0]);
					returnMap.put("fno", fno);
					
					returnList.add(returnMap);
				}
			}
		}
		
		map.put("returnList", returnList);
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		*/
	}

	/**
	 * 로그인 인증 정보 체크
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/com/isauthcheck.do")
	public void isauthcheck(HttpServletRequest request,
							HttpServletResponse response) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		boolean isauth = true;
		if (!EgovUserDetailsHelper.isAuthenticated()) {
    		
			msg = "로그인 인증 정보가 만료 되었습니다.";
			isauth= false;
		}
		msg = "";
		hMap.put("ISOK", isauth);
		hMap.put("MSG", msg);
	
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
	}	
	/**
	 * 알림공지 팝업창
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/common/topnoticeviewpopup.do")
	public void topnoticeviewpopup(HttpServletRequest request,
							String nseq,
							HttpServletResponse response) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		StringBuffer popupBody = new StringBuffer();
		try
		{
			NoticeVO noticeInfo = noticeService.getNoticeInfo(Integer.parseInt(nseq));
			/*
			popupBody.append("<div class='ly_block1' style='width:600px;background:#fff;padding:10px 10px 10px 10px;'>");
			popupBody.append(String.format("<div class='lyTT'><span><img src='/img/icon_ly_tt.png' /></span><p>%s</p><a class='btn_dialogbox_close' style='cursor:pointer;'><img src='/img/btn_ly_close.png' /></a></div>"
								, noticeInfo.getTitle()));
			popupBody.append(String.format("<div class='lyDate'>작성자 : %s <span>등록일 : %s</span></div>"
								, noticeInfo.getUpd_user()
								, noticeInfo.getUpd_date()));
			popupBody.append(String.format("<div class='lyView'>%s</div>"
								, noticeInfo.getContents()));
			popupBody.append("<div class='btn_black2'><a class='btn_black btn_dialogbox_close'><span>닫기</span></a></div>");
			popupBody.append("</div>");
			*/
				
			
			popupBody.append("<div class='ly_block1' style='width:100%;height:100%;background:#fff;padding:0px 0px 0px 0px;'>");
	    	popupBody.append(String.format("<div class='subTT' style='width:99%%;cursor:move;'><span><div style='width:100%%;white-space:pre-line;'>%s</div></span></div>", noticeInfo.getTitle()));
	    	popupBody.append("<div class='pd10'></div>");
	    	popupBody.append("<div id='contents' style='width:100%;height:75%;overflow:auto;border-bottom:1px solid #9e9e9e'>");
	    	popupBody.append(String.format("<span>%s</span>", noticeInfo.getContents()));
	    	popupBody.append("</div>");
	    	popupBody.append("<div class='btn_black2'>"
	    			+ "			<a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a>"
    				+ "		</div>");
	    	popupBody.append("</div>");
			
			msg = "";
			hMap.put("ISOK", true);
			hMap.put("MSG", msg);
			hMap.put("popup_body", popupBody.toString());
		}catch(Exception e){
			e.printStackTrace();
			hMap.put("ISOK", false);
			hMap.put("MSG", "공지사항 조회 오류 입니다.");
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
	}	
	/**
	 * 세션 업데이트 설정
 	 * @param request
	 * @param sessoinname
	 * @param sessionval
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/com/sessionupdate.do")
	public void sessionupdate(HttpServletRequest request,
							String sessoinname,
							String sessionval,
							HttpServletResponse response) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String,Object> hMap = new HashMap<String,Object>();
		String msg = "";
		try
		{
			
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			request.setCharacterEncoding("euc-kr");
			request.getSession().setAttribute(sessoinname, sessionval);
			
			hMap.put("ISOK", true);
			hMap.put("MSG", msg);
			
		}
		catch(Exception e){
			msg = e.toString();
			hMap.put("ISOK", false);
			hMap.put("MSG", msg);
		}
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
		
	}		
}
