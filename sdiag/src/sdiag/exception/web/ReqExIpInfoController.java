package sdiag.exception.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import sdiag.board.service.NoticeVO;
import sdiag.com.service.CommonService;
import sdiag.exception.service.ExceptionService;
import sdiag.exception.service.ExceptionVO;
import sdiag.exception.service.ReqExIpInfoService;
import sdiag.exception.service.ReqExIpInfoVO;
import sdiag.exception.service.ReqExIpPolicyInfoVO;
import sdiag.exception.service.ReqSearchVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import sdiag.man.vo.MailPolicyInfoVO;
import sdiag.member.service.MemberVO;
import sdiag.util.CommonUtil;
import sdiag.util.LeftMenuInfo;
import sdiag.util.MailUtil;
import sdiag.com.service.MimeBodyPartVO;

@Controller
public class ReqExIpInfoController {
	@Resource(name = "ReqExIpInfoService")
	protected ReqExIpInfoService reqExIpInfoService;
	
	@Resource(name = "ExceptionService")
	protected ExceptionService exceptionService;
	
	@Resource(name= "commonService")
	private CommonService comService;	
	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/**
	 * 예외처리IP LIST class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/reqExIpInfoList.do")
	private String reqExIpInfoList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.REQEXCEPTIP, comService);
		//searchVO.setPageIndex(2);
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		try
		{
			HashMap<String,Object> retMap = reqExIpInfoService.getReqExIpInfoList(searchVO);
			List<ReqExIpInfoVO> reqExIpInfoList = (List<ReqExIpInfoVO>)retMap.get("list");
			int totalCnt = (int)retMap.get("totalCount");
		
			model.addAttribute("resultList", reqExIpInfoList);
			int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
			model.addAttribute("totalPage", TotPage);
			model.addAttribute("currentpage", searchVO.getPageIndex());
			model.addAttribute("totalCnt", totalCnt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "exception/reqExIpInfoList";
		
	}
	
	/**
	 * 메인화면 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/reqExIpInfoForm.do")
	private String reqExIpInfoForm(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.REQEXCEPTIP, comService);
		
		ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
		
		List<ReqExIpPolicyInfoVO> reqExIpPolicyList = reqExIpInfoService.getReqExIpPolicyList(reqExIpInfoVO);
				
		model.addAttribute("reqExIpPolicyList", reqExIpPolicyList);
		
		if(searchVO.getFormMod().equals("U")){
			reqExIpInfo = reqExIpInfoService.getReqExIpInfo(reqExIpInfoVO);
		}
		
		model.addAttribute("reqExIpInfo", reqExIpInfo);
		
		
		return "exception/reqExIpInfoForm";
		
	}
	
	/**
	 * 예외IP 등록 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/reqExIpInfoSave.do")
	private void ExIpInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
	    ExceptionVO ExIpInfoInfo = new ExceptionVO();
	    
	    
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");                                                                                                                                                                                                                   
		    }
			
			
			if(searchVO.getFormMod().equals("I")){
				int seq = 0;
				seq = reqExIpInfoService.getExSeqInfo();
				reqExIpInfoVO.setExSeq(seq);
				if(!reqExIpInfoVO.getExDays().equals(99))
					reqExIpInfoVO.setIntExDays(Integer.parseInt(reqExIpInfoVO.getExDays()));
				
				
				List<ReqExIpPolicyInfoVO> reqPolicyList = new ArrayList<ReqExIpPolicyInfoVO>();
				if(!reqExIpInfoVO.getPolSelectList().equals("")){
					String[] polStrList = reqExIpInfoVO.getPolSelectList().split(",");
					for(String pol:polStrList){
						ReqExIpPolicyInfoVO polVO = new ReqExIpPolicyInfoVO();
						polVO.setSec_pol_id(pol);
						reqPolicyList.add(polVO);
					}
					reqExIpInfoVO.setReqPolicyList(reqPolicyList);
				}
				
				String sDate = reqExIpInfoVO.getExStartDate().replaceAll("-", "");
				reqExIpInfoVO.setExStartDate(sDate);
				
				
				reqExIpInfoService.reqExIpInfoSave(reqExIpInfoVO);
			
			}
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			
			//msg = ex.toString();
			msg = "저장중 오류가 발생하였습니다.";
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));

		
	}
	
	
	/**
	 * 예외 신청 IP 삭제
	 * @throws Exception 
	 */
	@RequestMapping("/exception/reqExIpInfoDelete.do")
	private void ExIpInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생 하였습니다.";
	    Boolean isOk = false;
	    int result =0;
	    
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
	    	ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
    		String reqSeqList = reqExIpInfoVO.getExSeqList();
    		for(String reqSeq:reqSeqList.split(",")){
    			reqExIpInfo.setExSeq(Integer.parseInt(reqSeq));
    			
    			reqExIpInfo = reqExIpInfoService.getReqExIpInfo(reqExIpInfo);
    			
    			if(reqExIpInfo.getGubun().equals("I")){
    				reqExIpInfoService.exceptionIpDelete(reqExIpInfo);
    			}else{
    				reqExIpInfoService.exceptionEmpnoDelete(reqExIpInfo);
    			}
    			
    			reqExIpInfoService.reqExIpInfoDelete(reqExIpInfo);
    			
	    	}
    		
    		isOk = true;
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			//msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	
	/**
	 * 예외 신청 IP 승인
	 * @throws Exception 
	 */
	@RequestMapping("/exception/reqExIpAppUpdate.do")
	private void reqExIpAppUpdate(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생 하였습니다.";
	    Boolean isOk = false;
	    int result =0;
	    boolean ret = false;
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
	    	
			result = reqExIpInfoService.reqExIpAppUpdate(reqExIpInfoVO);
    		
			
			if(result >0){
				
				isOk = true;
				//IP 예외 테이블에 저장
				
				//reqExIpInfoService.exceptionIpSave(reqExIpInfoVO);
								
				//승인 완료 메일
				ret = this.appSendMailInfo(reqExIpInfoVO);
				
				
			}
				
    		
		    map.put("ISOK", isOk);
		    map.put("ret", ret);
		    map.put("MSG", msg);
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			//msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	

	/**
	 * 사용자 예외 IP 신청 화면
	 * @throws Exception 
	 */
	@RequestMapping("/exception/userReqExIpInfoForm.do")
	private String userReqExIpInfoForm(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		model.addAttribute("reqExIpInfo", reqExIpInfoVO);
		
		List<ReqExIpPolicyInfoVO> reqExIpPolicyList = reqExIpInfoService.getReqExIpPolicyList(reqExIpInfoVO);
		
		
		model.addAttribute("reqExIpPolicyList", reqExIpPolicyList);
		
		return "exception/userReqExIpInfoForm";

		
	}
	
	/**
	 * 사용자 예외 IP 신청 화면
	 * @throws Exception 
	 */
	@RequestMapping("/exception/userReqExIpInfoSave.do")
	private void userReqExIpInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false;
	    
		try
		{
			
			List<ReqExIpPolicyInfoVO> reqPolicyList = new ArrayList<ReqExIpPolicyInfoVO>();
			if(!reqExIpInfoVO.getPolSelectList().equals("")){
				String[] polStrList = reqExIpInfoVO.getPolSelectList().split(",");
				for(String pol:polStrList){
					ReqExIpPolicyInfoVO polVO = new ReqExIpPolicyInfoVO();
					polVO.setSec_pol_id(pol);
					reqPolicyList.add(polVO);
				}
				reqExIpInfoVO.setReqPolicyList(reqPolicyList);
			}
			
			String sDate = reqExIpInfoVO.getExStartDate().replaceAll("-", "");
			reqExIpInfoVO.setExStartDate(sDate);
			
		/*if(!reqExIpInfoVO.getExDays().equals("99")){
			Calendar cal = Calendar.getInstance();
			Date startDate = new Date();
			Date endDate = new Date();
			
			SimpleDateFormat format = new SimpleDateFormat("YYYYmmdd");
			
			startDate = format.parse(reqExIpInfoVO.getExStartDate());
			
			cal.setTime(startDate);
			
			cal.add(Calendar.DATE, Integer.parseInt(reqExIpInfoVO.getExDays()));
			
			endDate = cal.getTime();
			reqExIpInfoVO.setExEndDate(format.format(endDate));
		
		}*/
			if(!reqExIpInfoVO.getExDays().equals(99))
				reqExIpInfoVO.setIntExDays(Integer.parseInt(reqExIpInfoVO.getExDays()));
			
			reqExIpInfoService.setReqExIpInfoUpdate(reqExIpInfoVO);
	    		
			isOk = true;
			
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
		
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));

		
	}
	
	/**
	 * 예외IP 정보 조회
	 * @throws Exception 
	 */
	@RequestMapping("/exception/empNoCheck.do")
	private void ExIpInfoInfo(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "처리중 오류가 발생하였습니다.";
	    Boolean isOk = false;
	    String result ="";
	    try{
	 	    int cnt = reqExIpInfoService.empNoCheck(reqExIpInfoVO);
	 	    
	 	    if(cnt <=0){
	 	    	result="N";
	 	    }else{
	 	    	result="Y";
	 	    	ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
	 	    	reqExIpInfo = reqExIpInfoService.getReqUserInfo(reqExIpInfoVO);
	 	    	//String reqEmpNm = reqExIpInfo.getReqEmpNm();
	 	    	map.put("reqExIpInfo", reqExIpInfo);
	 	    }
	 	    
	 	    isOk = true;
	 	    map.put("ISOK", isOk);
	 	   map.put("result", result);
	 	    map.put("ISOK", isOk);
	 		map.put("MSG", msg);
	 		
	    }catch(Exception ex){
			ex.printStackTrace();
			//msg = ex.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		}
	   
	   
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
	}
	
	
	private boolean appSendMailInfo(ReqExIpInfoVO reqExIpInfoVO) throws Exception{
		
		StringBuffer mailBody = new StringBuffer();
		
		MemberVO memInfo = CommonUtil.getMemberInfo();
		ReqExIpInfoVO reqExIpInfo = new ReqExIpInfoVO();
		
		reqExIpInfo = reqExIpInfoService.getReqExIpInfo(reqExIpInfoVO);
		
		List<ReqExIpPolicyInfoVO> reqExIpPolicyList = reqExIpInfoService.getReqExIpPolicyList(reqExIpInfo);
		
		String toEmail = reqExIpInfoService.getAppEmail(reqExIpInfo.getRgdtEmpNm());
		
		//String toEmail = "91209271@ktfriend.com,91209271@ktfriend.com";
		
		mailBody.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		mailBody.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
		mailBody.append("<head>");
		mailBody.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		mailBody.append("<title>Untitled Document</title>");
		mailBody.append("</head>");
        mailBody.append("");
		mailBody.append("<body style='padding:0;margin:0 auto;'>");
		mailBody.append("<table cellpadding='0' cellspacing='0' style='width:700px;background:#efefef;color:#393939;'>");
		mailBody.append("	<tr>");
		mailBody.append("    	<td style='padding:10px;'><img src=\"cid:toplogo\"  alt='KT임직원 보안수준진단' title='KT임직원 보안수준진단' /><span style='float:right;font:bold;font-family:\"돋움\";font-size:12px;padding-top:20px'>[IP 예외 신청]승인완료</span></td>");
		mailBody.append("    </tr>");
		mailBody.append("    <tr>");
		mailBody.append("    	<td style='padding:30px 20px;background:#ffffff;font:normal;font-family:\"돋움\";font-size:12px;border-top:solid 2px #5499de;line-height:25px;'>");
		mailBody.append(String.format("    <strong>%s님 안녕하세요.</strong><br/><br/>", reqExIpInfo.getRgdtEmpNm()));
		mailBody.append("        ");
		mailBody.append("        [임직원 보안수준진단] IP 예외 신청에 대한 승인 완료 안내 메일 입니다.<br/>");
		mailBody.append("		아래의 내용 확인 부탁 드립니다.<br/>");
		mailBody.append("        ");
		mailBody.append("        <strong>감사합니다.</strong>   ");
		mailBody.append("        </td>");
		mailBody.append("    </tr>");
		mailBody.append("    <tr>");
		mailBody.append("    	<td style='padding:30px 20px 80px 20px;background:#ffffff;font:normal;font-family:\"돋움\";font-size:12px;border-bottom:solid 1px #cfcfcf;border-collapse:collapse;'>");
		mailBody.append("        	<table cellpadding='0' cellspacing='0' style='width:680px;border:solid 1px #cfcfcf;'>");
		mailBody.append("            	<tr>");
		mailBody.append("                	<td colspan='2' style='color:#ffffff;font:bold;font-family:\"돋움\";font-size:12px;text-align:center;padding:10px;background:#5499de;border-bottom:solid 1px #cfcfcf;'>IP 예외 상세정보</td>");
		mailBody.append("                </tr>");
		mailBody.append("                <tr style=''>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>예외 정책</th>");
		mailBody.append("					<td style='padding:10px;border:solid 1px #cfcfcf;'>");
		
		for(ReqExIpPolicyInfoVO policyInfo:reqExIpPolicyList){
			if(policyInfo.getIs_selected().equals("Y")){
				mailBody.append(String.format("[%s] ",policyInfo.getPolicyname()));
			}
		}
		
		mailBody.append("			</td>");
		mailBody.append("                </tr>");
		mailBody.append("                <tr>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>예외 시작일</th>");
		mailBody.append(String.format("     <td style='padding:10px;border:solid 1px #cfcfcf;'>%s</td>", reqExIpInfo.getExStartDate()));
		mailBody.append("                </tr>");
		mailBody.append("                <tr>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>예외 종료일</th>");
		mailBody.append(String.format("     <td style='padding:10px;border:solid 1px #cfcfcf;'>%s</td>", reqExIpInfo.getExEndDate()));
		mailBody.append("                </tr>");
		mailBody.append("                <tr>");
		mailBody.append("                	<th style='padding:10px;border:solid 1px #cfcfcf;background:#f7f7f7;'>예외 IP</th>");
		mailBody.append(String.format("     <td style='padding:10px;border:solid 1px #cfcfcf;'>%s</td>" , reqExIpInfo.getExInfo()));
		mailBody.append("                </tr>                              ");
		mailBody.append("            </table>");
		mailBody.append("        </td>");
		mailBody.append("    </tr> ");
		mailBody.append("	<tr>");
		mailBody.append("    	<td style='padding:15px 0px 0px 0px;text-align:center;font:normal 11px '돋움';'>");
		mailBody.append("        *본 메일은 회신할 수 없는 발신전용 메일입니다.");
		mailBody.append("        <p style='color:#b8b8b8;'>Copyright <span style='font:bold 11px '돋움';color:#e71e25'>KT</span> All right reserverd.</p>");
		mailBody.append("        </td>");
		mailBody.append("    </tr>       ");
		mailBody.append("</table>");
		mailBody.append("</body>");
		mailBody.append("</html>");

		List<MimeBodyPartVO> imageList = new ArrayList<MimeBodyPartVO>();
		MimeBodyPartVO logo = new MimeBodyPartVO();
		logo.setImageName("top_logo.png");
		logo.setContentID("<toplogo>");
		logo.setFileName("toplogo.png");
		imageList.add(logo);
		
		boolean ret = MailUtil.SendMail(memInfo.getUserid(), "[임직원 보안수준진단] 예외IP신청 승인완료 안내 메일", mailBody.toString(), toEmail.toString(), comService, imageList);
		
		return ret;
		
	}
	
	/**
	 * 예외IP 등록 class
	 * @throws Exception 
	 */
	@RequestMapping("/exception/setExInfoSave.do")
	private void setExInfoSave(HttpServletRequest request,
			HttpServletResponse response,
			ReqExIpInfoVO reqExIpInfoVO,
			@ModelAttribute("searchVO") ReqSearchVO searchVO,
			ModelMap model) throws Exception{
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    int result =0;
	    
	    ExceptionVO ExIpInfoInfo = new ExceptionVO();
	    
	    
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			// 로그이한 사용자 정보
			MemberVO loginInfo = CommonUtil.getMemberInfo();
			reqExIpInfoVO.setRgdtEmpNo(loginInfo.getUserid());
			
			List<ReqExIpPolicyInfoVO> reqPolicyList = new ArrayList<ReqExIpPolicyInfoVO>();
			if(!reqExIpInfoVO.getPolSelectList().equals("")){
				String[] polStrList = reqExIpInfoVO.getPolSelectList().split(",");
				for(String pol:polStrList){
					ReqExIpPolicyInfoVO polVO = new ReqExIpPolicyInfoVO();
					polVO.setSec_pol_id(pol);
					reqPolicyList.add(polVO);
				}
				reqExIpInfoVO.setReqPolicyList(reqPolicyList);
			}
			
			
			if(searchVO.getFormMod().equals("I")){
				int seq = 0;
				seq = reqExIpInfoService.getExSeqInfo();
				reqExIpInfoVO.setExSeq(seq);
			}
			
			reqExIpInfoService.reqExIpInfoSave(reqExIpInfoVO);
			
			
			for(ReqExIpPolicyInfoVO polInfo:reqExIpInfoVO.getReqPolicyList()){
				if(reqExIpInfoVO.getGubun().equals("I")){
					ExceptionVO exceptionIpInfo = new ExceptionVO();
					exceptionIpInfo.setSec_pol_id(polInfo.getSec_pol_id());
					exceptionIpInfo.setIp(reqExIpInfoVO.getExInfo());
					
					//exceptionService.exceptionIpDelete(exceptionIpInfo);
					if(reqExIpInfoVO.getExAction().equals("I")){
						reqExIpInfoService.exceptionIpSave(polInfo);
					}else{
						reqExIpInfoService.exceptionIpDelete(reqExIpInfoVO);
					}
					
				}else{
					ExceptionVO exceptionEmpInfo = new ExceptionVO();
					exceptionEmpInfo.setSec_pol_id(polInfo.getSec_pol_id());
					exceptionEmpInfo.setEmp_no(reqExIpInfoVO.getExInfo());
					
					//exceptionService.exceptionEmpNoDelete(exceptionEmpInfo);
					
					
					if(reqExIpInfoVO.getExAction().equals("I")){
						reqExIpInfoService.exceptionEmpNoSave(polInfo);
					}else{
						reqExIpInfoService.exceptionEmpnoDelete(reqExIpInfoVO);
					}
					
				}
			}
			
			
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		catch(Exception ex)
		{
			
			//msg = ex.toString();
			msg = "저장중 오류가 발생하였습니다.";
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		}
		
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));

		
	}
}
