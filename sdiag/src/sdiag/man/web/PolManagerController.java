package sdiag.man.web;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sdiag.board.service.NoticeVO;
import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.ExcelInitVO;
import sdiag.com.service.FileUploadDto;
import sdiag.com.service.FileVO;
import sdiag.com.service.MenuItemVO;
import sdiag.com.service.SdiagProperties;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupSearchVO;
import sdiag.man.service.PolConVO;
import sdiag.man.service.PolTargetInfoVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.SecPolInfoVO;
import sdiag.man.service.SecPolService;
import sdiag.pol.service.OrgGroupVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.pol.service.PolicyService;
import sdiag.util.ExcelUtil;
import sdiag.util.FileManager;
import sdiag.util.LeftMenuInfo;
import sdiag.util.CommonUtil;
import sdiag.util.MajrCodeInfo;
import sdiag.util.StringUtil;

import com.google.gson.Gson;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class PolManagerController {
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "SecPolService")
	private SecPolService secPolService;
	
	@Resource(name= "commonService")
	private CommonService comService;
	
	// 파일 처리 클래스 선언
	@Resource(name = "FileManager")
	private FileManager fileManager;
	
	
	private List<MenuItemVO> getMinPolList(List<MenuItemVO> polList, String majCode){
		List<MenuItemVO> list = new ArrayList<MenuItemVO>();
		for(MenuItemVO row:polList){
			if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
				if(row.getDiag_majr_code().equals(majCode)){
					list.add(row);
				}
			}
		}
		return list;
	}
	private StringBuffer createPolComboBox(PolicySearchVO searchVO) throws Exception{
		StringBuffer str = new StringBuffer();
		
		List<MenuItemVO> polList = comService.getSolAllMenuList();	
		
		str.append("<select class='selected_sol' name='majCode' id='majCode' style='width:150px;'>");
		str.append("<option value=''>대진단 전체</option>");
		for(MenuItemVO row:polList){
			if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
				str.append(String.format("<option value='%s' %s>%s</option>"
										, row.getDiag_majr_code()
										, row.getDiag_minr_code().equals(searchVO.getMajCode()) ? "selected" : ""
										, row.getDiag_desc()));
			}
		}
		str.append("</select>");
		
		
		str.append(" <select class='selected_pol' name='minCode' id='minCode' style='width:150px;'><option value=''>중진단 전체</option>");
		if(!searchVO.getMajCode().equals(""))
		{
			for(MenuItemVO row:getMinPolList(polList, searchVO.getMajCode())){
						str.append(String.format("<option value='%s' %s>%s</option>"
								, row.getDiag_minr_code()
								, row.getDiag_minr_code().equals(searchVO.getMinCode()) ? "selected" : ""
								, row.getDiag_desc()));


			}

		}
		str.append("</select>");
		
		
		
		str.append(" <select name='polCode' id='polCode' style='width:150px;'><option value=''>지수화정책 전체</option>");
		if(!searchVO.getMinCode().equals(""))
		{
			List<EgovMap> selList = comService.getPolMenuAllList();
			for(EgovMap row:selList){
				if(row.get("majcode").equals(searchVO.getMajCode()) && row.get("mincode").equals(searchVO.getMinCode()) ){
					str.append(String.format("<option value='%s' %s>%s</option>"
							, row.get("secpolid")
							, row.get("secpolid").equals(searchVO.getPolCode()) ? "selected" : ""
							, row.get("poldesc")));
				}
				
			}
			
		}
		str.append("</select>");
		
		return str;
	}
	
	/**
	 * 정책관리
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/polmanger.do")
	public String polmanger(HttpServletRequest request,  @ModelAttribute("searchVO") PolicySearchVO searchVO, ModelMap model) throws Exception{	

		
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.POL);
		
		/*진단 항목 조회설정
		 * private String majCode = "";
	private String minCode = "";
	private String polCode = "";
		 * */
		StringBuffer str = new StringBuffer();
		str = createPolComboBox(searchVO);
		
		model.addAttribute("pol_selectbox", str.toString());
		
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		
		paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
		 
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		HashMap<String,Object> retMap = secPolService.getSecPolList(searchVO);
		List<SecPolInfoVO> list = (List<SecPolInfoVO>)retMap.get("list");
		int totalCnt = (int)retMap.get("totalCount");
		
		model.addAttribute("resultList", list);
		int TotPage =  (totalCnt -1) / searchVO.getPageSize() + 1; 
		model.addAttribute("totalPage", TotPage);
		model.addAttribute("currentpage", searchVO.getPageIndex());
		model.addAttribute("totalCnt", totalCnt);
		
		return "man/pol/polmng";

	}
	/**
	 * EDITOR 파일 업로드 처리
	 * @param request
	 * @param dto
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/editorImageUpload.do")
	public String editorImageUpload(HttpServletRequest request, FileUploadDto dto, ModelMap model, HttpServletResponse response) throws Exception{
		
		//System.out.println("파일 업로드][");
		//System.out.println(dto.getCKEditorFuncNum() + "][ CKEditorFuncNum");
		//System.out.println(dto.getImageUrl() + "][ ImageUrl");
		//System.out.println(dto.getNewFilename() + "][ NewFilename");
		//System.out.println(dto.getUpload().getName() + "][ UPload filename");
		String contextPath = request.getSession().getServletContext().getContextPath();
		try
		{
		Map<String, MultipartFile> files = new HashMap<String, MultipartFile>();//request.getFileMap();
		files.put("ckupload", dto.getUpload());
		List<FileVO> fileVo = new ArrayList<FileVO>();
		FileVO fInfo = new FileVO();
		
		String regEx = "(jpg|jpeg|png|gif|bmp|JPG|JPEG|PNG|GIF|BPM)";
	
		
		if(files.size() > 0){
			List<FileVO> retvo = fileManager.fileUploadProcess(files, "ATT_", SdiagProperties.getProperty("Globals.fileStorePath"), fileVo);
			
			
		
			
			for(FileVO vo:retvo){
				System.out.println(vo.getEvid_file_name() + "][" + vo.getEvid_file_loc());
			}
			
			int index =retvo.get(0).getEvid_file_name().lastIndexOf(".");
			//String randomString = StringUtil.getRandomStr('a', 'g') + StringUtil.getRandomStr('a', 'g') + StringUtil.getRandomStr('a', 'g');
			String fileExt = retvo.get(0).getEvid_file_name().substring(index + 1);
			
			if(fileExt.matches(regEx)){
				if(retvo.size() > 0 ){
					fInfo = retvo.get(0);
					
					dto.setNewFilename(fInfo.getEvid_file_name());
					dto.setImageUrl(contextPath + "/" + "attachfile/" + fInfo.getEvid_file_loc());
					dto.setResult("true");
				}else{
					fInfo.setEvid_file_name("");
					fInfo.setEvid_file_loc("");
				}
			}else{
				dto.setResult("false");
			}
		
		}
		
		//PrintWriter printWriter = response.getWriter();
		//printWriter.println("<script type='text/javascript'>window.parent.CHEDITOR.tools.callFunction("+ dto.getCKEditorFuncNum() + "," + dto.getImageUrl() + "," + "'이미지업로드  완료....'" +")</script>");
		//printWriter.flush();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(dto.getNewFilename());
		//System.out.println(dto.getImageUrl());
		
		model.addAttribute("dto", dto);
		
		return "cmm/editorImageUpload";
	}

		
	
	
	/**
	 * 정책정보 수정
	 * @param request
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/man/polmodify.do")
	public String polmodify(HttpServletRequest request, @ModelAttribute("searchVO") PolicySearchVO searchVO, ModelMap model) throws Exception{	

		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.POL);
		
		System.out.println(searchVO.getPolId() + "][POL ID");
		
		HashMap<String,Object> Info = secPolService.getPolIdxInfo(searchVO.getPolId());
		SecPolInfoVO secPolInfo = (SecPolInfoVO)Info.get("polInfo");
		PolConVO polConInfo = (PolConVO)Info.get("conInfo");
		List<PolTargetInfoVO> polTargetList= (List<PolTargetInfoVO>)Info.get("polTargetList");
		
		//model.addAttribute("polInfo", Info);
		model.addAttribute("secPolInfo", secPolInfo);
		model.addAttribute("polConInfo", polConInfo);
		model.addAttribute("polTargetList", polTargetList);
		
		//대진단
		List<MenuItemVO> polList = comService.getSolAllMenuList();
		String MajPolDiag = CreatePolDiagString(polList, secPolInfo.getDiag_majr_code(), "", "L");
		String MinPolDiag = CreatePolDiagString(polList, secPolInfo.getDiag_majr_code(), secPolInfo.getDiag_minr_code(), "M");
		model.addAttribute("majPolDiagString", MajPolDiag);
		model.addAttribute("mijPolDiagString", MinPolDiag);
		
		String PolWeightPointHtml = getPolWeightPointTableHtml(String.valueOf(Math.round(polConInfo.getCon_cnt())), polConInfo);
		model.addAttribute("polWeightPointHtml", PolWeightPointHtml);
		String PolWeightPointOXHtml = getPolWeightPointOXHtml(polConInfo);
		model.addAttribute("polWeightPointOXHtml", PolWeightPointOXHtml);
		
		List<CodeInfoVO> aList = comService.getCodeInfoList(MajrCodeInfo.AgreeCode);
		model.addAttribute("aList", aList);
		
		return "man/pol/polmodify";

	}
	
	@RequestMapping(value="/man/policysave.do")
	public String policysave(HttpServletRequest request, @ModelAttribute("searchVO") PolicySearchVO searchVO, ModelMap model) throws Exception{
		//System.out.println(noticeVO.getSq_no() + "][");
		//noticeService.NoticelistDelete(noticeVO);
		
		System.out.println("정책정보 수정");
		
		return "forward:/man/polmanger.do";
	}
	/**
	 * 가중치 html정보
	 * @param polConInfo
	 * @return
	 */
	private String getPolWeightPointOXHtml(PolConVO polConInfo)
	{
		StringBuffer str = new StringBuffer();
		str.append(String.format("<table border='0' class='TBS5' cellpadding=0 cellspacing=0>"
				+ "		<colgroup><col style='width:20%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'></colgroup>"
				+ "		<tr>"
				+ "			<th>점수기준</th>"
				+ "			<td><input type='text' id='x_con_scor_1' name='x_con_scor_1' style='width:40px' value='%s'>점</td>"
				+ "			<td><input type='text' id='x_con_from_1' name='x_con_from_1' style='width:60px' value='%s'></td>"
				+ "			<td colspan='3'>같다(=)</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<td rowspan='2'></td>"
				+ "			<td><input type='text' id='x_con_scor_2' name='x_con_scor_2' style='width:40px' value='%s'>점</td>"
				+ "			<td><input type='text' id='x_con_from_2' name='x_con_from_2' style='width:60px' value='%s'></td>"
				+ "			<td colspan='3'>같다(=)"
				+ "			<input type='hidden' id='x_con_to_1' name='x_con_to_1' value=''>"
				+ "			<input type='hidden' id='x_con_to_2' name='x_con_to_2' value=''>"
				+ "			<input type='hidden' id='x_con_scor_3' name='x_con_scor_3' value='0'>"
				+ "			<input type='hidden' id='x_con_from_3' name='x_con_from_3' value=''>"
				+ "			<input type='hidden' id='x_con_to_3' name='x_con_to_3' value=''>"
				+ "			<input type='hidden' id='x_con_scor_4' name='x_con_scor_4' value='0'>"
				+ "			<input type='hidden' id='x_con_from_4' name='x_con_from_4' value=''>"
				+ "			<input type='hidden' id='x_con_to_4' name='x_con_to_4' value=''>"
				+ "			<input type='hidden' id='x_con_scor_5' name='x_con_scor_5' value='0'>"
				+ "			<input type='hidden' id='x_con_from_5' name='x_con_from_5' value=''>"
				+ "			<input type='hidden' id='x_con_to_5' name='x_con_to_5' value=''>"
				+ "			</td>"
				+ "		</tr>"
				+ "	</table>"
				, Math.round(polConInfo.getCon_scor_1())
				, polConInfo.getCon_from_1()
				, Math.round(polConInfo.getCon_scor_2())
				, polConInfo.getCon_from_2()));
		
		return str.toString();
	}
	/**
	 * 가중치 테이블 Html정보
	 * @param wType
	 * @param polConInfo
	 * @return
	 */
	private String getPolWeightPointTableHtml(String wType, PolConVO polConInfo)
	{
		StringBuffer str = new StringBuffer();
		if(wType.equals("2")){
			str.append(String.format("<table border='0' class='TBS5' cellpadding=0 cellspacing=0>"
					+ "		<colgroup><col style='width:20%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'></colgroup>"
					+ "		<tr>"
					+ "			<th>점수기준</th>"
					+ "			<td><input type='text' id='con_scor_1' name='con_scor_1' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_1' name='con_from_1' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_1' name='con_to_1' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td rowspan='2'></td>"
					+ "			<td><input type='text' id='con_scor_2' name='con_scor_2' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_2' name='con_from_2' style='width:60px' value='%s'></td>"
					+ "			<td colspan='2'><input type='hidden' id='con_to_2' name='con_to_2' value='%s'>"
					+ "			<input type='hidden' id='con_scor_3' name='con_scor_3' value='0'>"
					+ "			<input type='hidden' id='con_from_3' name='con_from_3' value=''>"
					+ "			<input type='hidden' id='con_to_3' name='con_to_3' value=''>"
					+ "			<input type='hidden' id='con_scor_4' name='con_scor_4' value='0'>"
					+ "			<input type='hidden' id='con_from_4' name='con_from_4' value=''>"
					+ "			<input type='hidden' id='con_to_4' name='con_to_4' value=''>"
					+ "			<input type='hidden' id='con_scor_5' name='con_scor_5' value='0'>"
					+ "			<input type='hidden' id='con_from_5' name='con_from_5' value=''>"
					+ "			<input type='hidden' id='con_to_5' name='con_to_5' value=''>"
					+ "			</td>"
					+ "			<td>크다(>)</td>"
					+ "		</tr>"
					+ "	</table>"
					, Math.round(polConInfo.getCon_scor_1())
					, polConInfo.getCon_from_1()
					, polConInfo.getCon_to_1()
					, Math.round(polConInfo.getCon_scor_2())
					, polConInfo.getCon_from_2()
					, polConInfo.getCon_to_2()));
		}else if(wType.equals("3")){
			str.append(String.format("<table border='0' class='TBS5' cellpadding=0 cellspacing=0>"
					+ "		<colgroup><col style='width:20%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'></colgroup>"
					+ "		<tr>"
					+ "			<th>점수기준</th>"
					+ "			<td><input type='text' id='con_scor_1' name='con_scor_1' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_1' name='con_from_1' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_1' name='con_to_1' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td rowspan='3'></td>"
					+ "			<td><input type='text' id='con_scor_2' name='con_scor_2' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_2' name='con_from_2' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_2' name='con_to_2' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td><input type='text' id='con_scor_3' name='con_scor_3' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_3' name='con_from_3' style='width:60px' value='%s'></td>"
					+ "			<td colspan='2'><input type='hidden' id='con_to_3' name='con_to_3' value='%s'></td>"
					+ "			<td>크다(>=)"
					+ "			<input type='hidden' id='con_scor_4' name='con_scor_4' value='0'>"
					+ "			<input type='hidden' id='con_from_4' name='con_from_4' value=''>"
					+ "			<input type='hidden' id='con_to_4' name='con_to_4' value=''>"
					+ "			<input type='hidden' id='con_scor_5' name='con_scor_5' value='0'>"
					+ "			<input type='hidden' id='con_from_5' name='con_from_5' value=''>"
					+ "			<input type='hidden' id='con_to_5' name='con_to_5' value=''>"
					+ "			</td>"
					+ "		</tr>"
					+ "	</table>"
					, Math.round(polConInfo.getCon_scor_1())
					, polConInfo.getCon_from_1()
					, polConInfo.getCon_to_1()
					, Math.round(polConInfo.getCon_scor_2())
					, polConInfo.getCon_from_2()
					, polConInfo.getCon_to_2()
					, Math.round(polConInfo.getCon_scor_3())
					, polConInfo.getCon_from_3()
					, polConInfo.getCon_to_3()));
		}else if(wType.equals("4")){
			str.append(String.format("<table border='0' class='TBS5' cellpadding=0 cellspacing=0>"
					+ "		<colgroup><col style='width:20%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'></colgroup>"
					+ "		<tr>"
					+ "			<th>점수기준</th>"
					+ "			<td><input type='text' id='con_scor_1' name='con_scor_1' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_1' name='con_from_1' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_1' name='con_to_1' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td rowspan='4'></td>"
					+ "			<td><input type='text' id='con_scor_2' name='con_scor_2' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_2' name='con_from_2' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_2' name='con_to_2' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td><input type='text' id='con_scor_3' name='con_scor_3' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_3' name='con_from_3' style='width:60px' value='%s'></td>"
					+ "			<td>크다(>=)</td>"
					+ "			<td><input type='text' id='con_to_3' name='con_to_3' style='width:60px' value='%s'></td>"
					+ "			<td>작다(<=)</td>"
					+ "		</tr>"
					+ "		<tr>"
					+ "			<td><input type='text' id='con_scor_4' name='con_scor_4' style='width:40px' value='%s'>점</td>"
					+ "			<td><input type='text' id='con_from_4' name='con_from_4' style='width:60px' value='%s'></td>"
					+ "			<td colspan='2'><input type='hidden' id='con_to_4' name='con_to_4' value='%s'></td>"
					+ "			<td>크다(>=)"
					+ "			<input type='hidden' id='con_scor_5' name='con_scor_5' value='0'>"
					+ "			<input type='hidden' id='con_from_5' name='con_from_5' value=''>"
					+ "			<input type='hidden' id='con_to_5' name='con_to_5' value=''>"
					+ "			</td>"
					+ "		</tr>"
					+ "	</table>"
					, Math.round(polConInfo.getCon_scor_1())
					, polConInfo.getCon_from_1()
					, polConInfo.getCon_to_1()
					, Math.round(polConInfo.getCon_scor_2())
					, polConInfo.getCon_from_2()
					, polConInfo.getCon_to_2()
					, Math.round(polConInfo.getCon_scor_3())
					, polConInfo.getCon_from_3()
					, polConInfo.getCon_to_3()
					, Math.round(polConInfo.getCon_scor_4())
					, polConInfo.getCon_from_4()
					, polConInfo.getCon_to_4()));
		}else if(wType.equals("5")){
			str.append(String.format("<table border='0' class='TBS5' cellpadding=0 cellspacing=0>"
				+ "		<colgroup><col style='width:20%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'><col style='width:15%%'></colgroup>"
				+ "		<tr>"
				+ "			<th>점수기준</th>"
				+ "			<td><input type='text' id='con_scor_1' name='con_scor_1' style='width:30px' value='%s'>점</td>"
				+ "			<td><input type='text' id='con_from_1' name='con_from_1' style='width:60px' value='%s'></td>"
				+ "			<td>크다(>=)</td>"
				+ "			<td><input type='text' id='con_to_1' name='con_to_1' style='width:60px' value='%s'></td>"
				+ "			<td>작다(<=)</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<td rowspan='5'></td>"
				+ "			<td><input type='text' id='con_scor_2' name='con_scor_2' style='width:30px' value='%s'>점</td>"
				+ "			<td><input type='text' id='con_from_2' name='con_from_2' style='width:60px' value='%s'></td>"
				+ "			<td>크다(>=)</td>"
				+ "			<td><input type='text' id='con_to_2' name='con_to_2' style='width:60px' value='%s'></td>"
				+ "			<td>작다(<=)</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<td><input type='text' id='con_scor_3' name='con_scor_3' style='width:30px' value='%s'>점</td>"
				+ "			<td><input type='text' id='con_from_3' name='con_from_3' style='width:60px' value='%s'></td>"
				+ "			<td>크다(>=)</td>"
				+ "			<td><input type='text' id='con_to_3' name='con_to_3' style='width:60px' value='%s'></td>"
				+ "			<td>작다(<=)</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<td><input type='text' id='con_scor_4' name='con_scor_4' style='width:30px' value='%s'>점</td>"
				+ "			<td><input type='text' id='con_from_4' name='con_from_4' style='width:60px' value='%s'></td>"
				+ "			<td>크다(>=)</td>"
				+ "			<td><input type='text' id='con_to_4' name='con_to_4' style='width:60px' value='%s'></td>"
				+ "			<td>작다(<=)</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<td><input type='text' id='con_scor_5' name='con_scor_5' style='width:30px' value='%s'>점</td>"
				+ "			<td><input type='text' id='con_from_5' name='con_from_5' style='width:60px' value='%s'></td>"
				+ "			<td colspan='2'><input type='hidden' id='con_to_5' name='con_to_5' value='%s'></td>"
				+ "			<td>크다(>=)</td>"
				+ "		</tr>"
				+ "	</table>"
				, Math.round(polConInfo.getCon_scor_1())
				, polConInfo.getCon_from_1()
				, polConInfo.getCon_to_1()
				, Math.round(polConInfo.getCon_scor_2())
				, polConInfo.getCon_from_2()
				, polConInfo.getCon_to_2()
				, Math.round(polConInfo.getCon_scor_3())
				, polConInfo.getCon_from_3()
				, polConInfo.getCon_to_3()
				, Math.round(polConInfo.getCon_scor_4())
				, polConInfo.getCon_from_4()
				, polConInfo.getCon_to_4()
				, Math.round(polConInfo.getCon_scor_5())
				, polConInfo.getCon_from_5()
				, polConInfo.getCon_to_5()));
		}else{
			str.append("");
		}
		return str.toString();	
	}
	/**
	 * 진단메뉴 생성
	 * @param polList
	 * @param majrCode
	 * @param minrCode
	 * @param type
	 * @return
	 */
	private String CreatePolDiagString(List<MenuItemVO> polList, String majrCode, String minrCode, String type){
		StringBuffer str = new StringBuffer();
		if(type.equals("L")){
			str.append("<select name='majrcode' id='majrcode' style='width:200px;'><option value=''>대진단선택</option>");
			for(MenuItemVO row:polList){
				if(row.getDiag_majr_code().equals(row.getDiag_minr_code())){
					str.append(String.format("<option value='%s' %s isbs='%s'>%s</option>"
							, row.getDiag_minr_code()
							, row.getDiag_minr_code().equals(majrCode) ? "selected" : ""
							, row.getBuseo_indc()	
							, row.getDiag_desc()));	
				}
			}
			str.append("</select>");
		}else{
			str.append("<select name='minrcode' id='minrcode' style='width:200px;'><option value=''>중진단선택</option>");
			for(MenuItemVO row:polList){
				if(row.getDiag_majr_code().equals(majrCode)){
					if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
						str.append(String.format("<option value='%s' %s>%s</option>"
								, row.getDiag_minr_code()
								, row.getDiag_minr_code().equals(minrCode) ? "selected" : ""
								, row.getDiag_desc()));	
					}
				}
				
			}
			str.append("</select>");
		}
		
		return str.toString();
	}
	/**
	 * 메뉴및 정책리스트 조회
	 * @param request
	 * @param selid
	 * @param majCode
	 * @param minCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getSelectedMaunPollist.do")
	public void getSelectedMaunPollist(HttpServletRequest request, String selid, String majCode, String minCode, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		
		String msg = "Error!!";
	    Boolean isOk = false; 
		
	    //ServletContext servletContext = request.getServletContext();
		List<MenuItemVO> sollist =  comService.getSolMenuList(); //(List<MenuItemVO>) servletContext.getAttribute("SolMenuList");
		List<EgovMap> pollist = comService.getPolMenuAllList();//(List<EgovMap>) servletContext.getAttribute("polMenuList");
	    
	    HashMap<String,Object> hMap = new HashMap<String,Object>();
	    try
		{
	    	List<HashMap<String, String>> polList = new ArrayList<HashMap<String, String>>();
	    	if(selid.equals("majCode")){
	    		//중진단 조회
	    		for(MenuItemVO row:sollist){
					if(row.getDiag_majr_code().equals(majCode)){
						if(!row.getDiag_majr_code().equals(row.getDiag_minr_code())){
							HashMap<String, String> polInfo = new HashMap<String, String>();
							polInfo.put("code", row.getDiag_minr_code());
							polInfo.put("desc", row.getDiag_desc());
							polList.add(polInfo);
						}
					}
				}
	    	}else{
	    		//정책
	    		List<EgovMap> sList = CommonUtil.getListFromPolMenuForString(pollist, majCode, minCode);
				if(sList.size() > 0){
					for(EgovMap srow:sList)
					{
						HashMap<String, String> polInfo = new HashMap<String, String>();
						polInfo.put("code", srow.get("secpolid").toString());
						polInfo.put("desc", srow.get("poldesc").toString());
						polList.add(polInfo);
					}
				}
	    	}
	    	
	    	isOk = true;
	    	hMap.put("list", polList);
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
		  
		}
		catch(Exception ex)
		{
			msg = ex.toString();
		    hMap.put("ISOK", isOk);
		    hMap.put("MSG", msg);
			
		}
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));
	   
	}	
	
	/**
	 * 정책관리 팝업
	 * @param request
	 * @param polid
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getPolManagerPopup.do")
	public void getPolManagerPopup(HttpServletRequest request, String polid, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
			
		String msg = "Error!!";
		Boolean isOk = false; 

		StringBuffer popupBody = new StringBuffer();

		HashMap<String,Object> hMap = new HashMap<String,Object>();
		try
		{
			Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
			HashMap<String,Object> Info = secPolService.getPolIdxInfo(polid);
			SecPolInfoVO secPolInfo = (SecPolInfoVO)Info.get("polInfo");
			PolConVO polConInfo = (PolConVO)Info.get("conInfo");
			
			popupBody.append("<div class='ly_block1' style='width:880px;background:#fff;padding:10px 15px 10px 10px;border:1px solid #eee;'>");
	    	popupBody.append(String.format("<div class='subTT' style='width:100%%;cursor:move;'><span>%s</span></div>", "지수화 정책관리"));
	    	popupBody.append("<div class='pd10'></div>");
	    	popupBody.append("<div class='configbody' style='height:500px;overflow-y:auto;'>");
	    	
	    	popupBody.append("<table class='TBS4' cellpadding='0' cellspacing='0'>");
    		popupBody.append(" <colgroup><col style='width:20%'><col style='width:30%'><col style='width:20%'><col style='width:30%'></colgroup>");
    		popupBody.append("		<tr>"
					+ "					<th>지수화 정책</th>");
			popupBody.append(String.format("<td colspan='3'>%s<input type='hidden' name='sec_pol_id' id='sec_pol_id' value='%s' /><input type='hidden' name='sec_pol_desc' id='sec_pol_desc' value='%s' /><input type='hidden' name='sec_sol_id' id='sec_sol_id' value='%s' /></td>", secPolInfo.getSec_pol_desc(), secPolInfo.getSec_pol_id(), secPolInfo.getSec_pol_desc(), secPolInfo.getSec_sol_id()));
			popupBody.append("		</tr>"
					+ "				<tr>"
					+ "					<th>대진단</th>"
					+ "					<td>");
			List<MenuItemVO> polList = comService.getSolAllMenuList();
			popupBody.append(CreatePolDiagString(polList, secPolInfo.getDiag_majr_code(), "", "L"));
			
			popupBody.append("			</td>"
					+ "					<th>중진단</th>"
					+ "					<td>");
			popupBody.append(CreatePolDiagString(polList, secPolInfo.getDiag_majr_code(), secPolInfo.getDiag_minr_code(), "M"));
			popupBody.append("			</td>"
					+ "				</tr>");
			popupBody.append("		<tr>");
			
			/*
			popupBody.append("			<th>임계치 구분</th>");
			popupBody.append(String.format("<td><input type='radio' name='critical_value_type' value='1' %s> 일 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='critical_value_type' value='2' %s> 월 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='critical_value_type' value='3' %s> 기간</td>"
											, secPolInfo.getPol_critical_value_type().equals("1") ? "checked" : ""
											, secPolInfo.getPol_critical_value_type().equals("2") ? "checked" : ""
											, secPolInfo.getPol_critical_value_type().equals("3") ? "checked" : ""));
			*/								
			popupBody.append("			<th>보안 정책 구분</th>");
			popupBody.append(String.format("<td colspan='3'><input type='radio' name='sec_pol_cat' value='01' %s> 카운트 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='sec_pol_cat' value='02' %s> O,X</td>"
											, secPolInfo.getSec_pol_cat().equals("01") ? "checked" : ""
											, secPolInfo.getSec_pol_cat().equals("02") ? "checked" : ""));
			popupBody.append("		</tr>");
			
			popupBody.append(String.format("<tr class='is_buseo' style='display:%s;'>", secPolInfo.getBuseo_indc().equals("Y") ? "" : "none"));
			popupBody.append("			<th>가점 여부</th>");
			popupBody.append(String.format("<td colspan='3'><input type='radio' id='gajeomindc' name='gajeomindc' value='Y' %s/> 예(가점) &nbsp;&nbsp;&nbsp; <input type='radio' id='gajeomindc' name='gajeomindc' value='N' %s/> 아니오(감점)</td>"
											, secPolInfo.getGajeom_indc().equals("Y") ? "checked" : ""
											, secPolInfo.getGajeom_indc().equals("N") ? "checked" : ""));
			popupBody.append("		</tr>");
			popupBody.append(String.format("<tr class='is_notbuseo is_weight' style='display:%s;'>"
											, secPolInfo.getBuseo_indc().equals("Y") ? secPolInfo.getGajeom_indc().equals("Y") ? "none" : "" : ""));
			popupBody.append("			<th>가중치</th>");
			popupBody.append(String.format("<td colspan='3'><input type='text' id='weight_val' name='weight_val' value='%s' style='width:100px;' maxlength='10'  /><input type='hidden' id='critical_val' name='critical_val' value='%s' /></td>"
											, secPolInfo.getPol_weight_value()
											, secPolInfo.getPol_critical_vlaue()));
			popupBody.append("		</tr>");
			
			popupBody.append(String.format("<tr class='data_type_count' style='display:%s;'>", secPolInfo.getSec_pol_cat().equals("01") ? "" : "none"));
			popupBody.append("			<th>조건카운트");
			popupBody.append("<select name='con_type' id='con_type'>");
			for(int i=1;i<=5;i++){
				popupBody.append(String.format("<option value='%s' %s>%s</option>", i==1 ? "" : i, Math.round(polConInfo.getCon_cnt()) == i ? "selected" : "" ,i==1 ? "조건수선택" : "조건수 " + i));
			}
			popupBody.append("</select>");
			popupBody.append("			</th>"
					+ "					<td colspan='3' class='pol_con_body_count'>");
			popupBody.append(getPolWeightPointTableHtml(String.valueOf(Math.round(polConInfo.getCon_cnt())), polConInfo));
			popupBody.append("			</td>"
					+ "				</tr>");
			
			popupBody.append(String.format("<tr class='data_type_ox' style='display:%s;'>", secPolInfo.getSec_pol_cat().equals("02") ? "" : "none"));
			popupBody.append("			<th>조건카운트</th>"
					+ "					<td colspan='3' class='pol_con_body_ox'><input type='hidden' name='x_con_type' id='x_con_type' value='2' />");
			popupBody.append(getPolWeightPointOXHtml(polConInfo));
			popupBody.append("			</td>"
					+ "				</tr>");
			
			popupBody.append(String.format("<tr class='is_notbuseo' style='display:%s;'>", secPolInfo.getBuseo_indc().equals("Y") ? "none" : ""));
			popupBody.append("					<th>제재조치</th>");
			popupBody.append(String.format("<td><input type='radio' name='isautosanct' value='0' %s> 자동 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='isautosanct' value='1' %s> 수동</td>"
											, secPolInfo.getSanc_cate().equals("0") ? "checked" : ""
											, secPolInfo.getSanc_cate().equals("1") ? "checked" : ""));
			popupBody.append("			<th>소명신청</th>");
			popupBody.append(String.format("<td><input type='radio' name='isautoappr' value='0' %s> 자동 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='isautoappr' value='1' %s> 수동</td>"
											, secPolInfo.getAppr_cate().equals("0") ? "checked" : ""
											, secPolInfo.getAppr_cate().equals("1") ? "checked" : ""));
			popupBody.append("		</tr>");
			popupBody.append(String.format("<tr class='is_notbuseo' style='display:%s;'>", secPolInfo.getBuseo_indc().equals("Y") ? "none" : ""));
			popupBody.append("			<th>소명신청 결재라인</th>"
					+ "					<td >");
		    List<CodeInfoVO> aList = comService.getCodeInfoList(MajrCodeInfo.AgreeCode);
			popupBody.append(			"<select id='appr_line_code' name='appr_line_code'>");
			for(CodeInfoVO row:aList){
				popupBody.append(String.format("<option value='%s' %s>%s</option>", row.getMinr_code()
																					, row.getMinr_code().equals(secPolInfo.getAppr_line_code()) ? "selected" : ""
																					, row.getCode_desc()));
			}
			popupBody.append(			"</select>");
			popupBody.append("			</td>"
					+ "					<th>소명파일 첨부여부</th>");
			popupBody.append(String.format("			<td><input type='radio' name='isfile' value='0' %s> 첨부활성화   &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='isfile' value='1' %s> 첨부미활성화</td></td>"
											, secPolInfo.getAppr_attach_indc().equals("0") ? "checked" : ""
											, secPolInfo.getAppr_attach_indc().equals("1") ? "checked" : ""));
			popupBody.append("				</tr>");
			popupBody.append(String.format("<tr style='display:%s;'>", secPolInfo.getBuseo_indc().equals("Y") ? "none" : ""));
			popupBody.append("					<th>소명신청 주의사항</th>");
			popupBody.append(String.format("			<td colspan='3'><input type='text' id='noti' name='noti' style='width:600px;' maxlength='1000' value='%s' /></td>", secPolInfo.getAppr_note()));
			popupBody.append("		</tr>");
			popupBody.append(String.format("<tr class='is_notbuseo' style='display:%s;'>", secPolInfo.getBuseo_indc().equals("Y") ? "none" : ""));
			popupBody.append("					<th>PC지킴이<br /> 조치파라미터</th>");
			popupBody.append(String.format("<td><input type='text' id='exe_para' name='exe_para' style='width:200px;' maxlength='100' value='%s' /><input type='hidden' id='cond_field1' name='cond_field1' value='%s' /><input type='hidden' id='ispcgact' name='ispcgact' value='%s' /></td>"
											, secPolInfo.getIspcgact().equals("Y") ? secPolInfo.getExe_para().equals("") ? secPolInfo.getCond_field1().length() > 0 ? secPolInfo.getCond_field1().subSequence(1, secPolInfo.getCond_field1().length()) : "" : secPolInfo.getExe_para() : ""
											, secPolInfo.getCond_field1().length() > 0 ? secPolInfo.getCond_field1().subSequence(1, secPolInfo.getCond_field1().length()) : ""
											, secPolInfo.getIspcgact()));
			popupBody.append("			<th>제재조치 대상여부</th>");
			popupBody.append(String.format("<td><input type='radio' name='sanc_indc' value='Y' %s> 대상 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='sanc_indc' value='N' %s> 미대상</td>"
											, secPolInfo.isSanc_indc() ? "checked" : ""
											, !secPolInfo.isSanc_indc() ? "checked" : ""));
			popupBody.append("		</tr>");
			
			popupBody.append("		<tr>"
					+ "					<th>사용여부</th>");
			popupBody.append(String.format("<td colspan='3'><input type='radio' name='isused' value='Y' %s> 사용 &nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='isused' value='N' %s> 사용중지</td>"
											, secPolInfo.getUse_indc().equals("Y") ? "checked" : ""
											, secPolInfo.getUse_indc().equals("N") ? "checked" : ""));
			
			popupBody.append("		</tr>");
			popupBody.append(String.format("</tr>"
					+ "				<tr>"
					+ "					<th>로그조회 정보</th>"
					+ "					<td colspan='3'><input type='text' readonly id='pol_table' name='pol_table' style='width:150px;' maxlength='50' value='%s' />"
					+ "						<input type='text' readonly id='pol_columns' name='pol_columns' style='width:350px;' maxlength='3000' value='%s' />"
					+ "						<a class='btn_scr3 btn_columns_info'><span>조회컬럼설정</span></a>"
					+ "					</td>"
					+ "				</tr>", secPolInfo.getSec_pol_table(), secPolInfo.getSec_pol_table_col()));
			popupBody.append("		<tr>");	
			popupBody.append("			<th>지수화정책 상세</th>");
			popupBody.append(String.format("<td style='padding:5px 0 5px 10px;' colspan='3'><textarea id='polnoti' name='polnoti' maxlength='2000' style='width:98%%' rows='10'>%s</textarea></td>", secPolInfo.getSec_pol_notice()));
			popupBody.append("		</tr>");	
			popupBody.append("		<tr>");	
			popupBody.append("			<th>조치방안 상세</th>");
			popupBody.append(String.format("<td style='padding:5px 0 5px 10px;' colspan='3'><textarea  id='editor' name='editor' style='width:95%%;' >%s</textarea></td>", secPolInfo.getSec_pol_notice_detail()));
			popupBody.append("		</tr>"); //<textarea id='polnotidetail' name='polnotidetail' maxlength='5000' style='width:98%%' rows='20'>%s</textarea>
	    	popupBody.append("	</table>");
	    	popupBody.append("	</div>");
	    	popupBody.append(String.format("<input type='hidden' name='buseoindc' id='buseoindc' value='%s' />", secPolInfo.getBuseo_indc()));
	    	popupBody.append(String.format("<input type='hidden' name='critical_value_type' id='critical_value_type' value='1' />"));
	    	popupBody.append("	<div class='btn_black2'>"
	    			+ "			<a class='btn_black btn_popup_save'><span style='color:#ffffff'>저장</span></a> "
    				+ "			<a class='btn_black btn_dialogbox_close'><span style='color:#ffffff'>닫기</span></a>"
    				+ "			</div>");
	    	popupBody.append("</div>");
	
			StringBuffer script = new StringBuffer();
			script.append("<script type='text/javascript'>");
			script.append("$(function () {"
					+ "			CKEDITOR.replace('editor', {"
					+ "					enterMode: '2',"
					+ "					shiftEnterMode: '3',"
					+ "					uiColor: '#ffffff',"
					+ " 				height: 350,"
					+ "					toolbar: null,"
					+ "					toolbarGroups: ["
					+ "						{ name: 'document', groups: ['mode', 'document', 'doctools'] },"
					+ "						{ name: 'clipboard', groups: ['clipboard', 'undo'] },"
					+ "						{ name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },"
					+ "						{ name: 'forms', groups: ['forms'] },"
					+ "						'/',"
					+ "						{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },"
					+ "						{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },"
					+ "						{ name: 'links', groups: ['links'] },"
					+ "						{ name: 'insert', groups: ['insert'] },"
					+ "						'/',"
					+ "						{ name: 'styles', groups: ['styles'] },"
					+ "						{ name: 'colors', groups: ['colors'] },"
					+ "						{ name: 'tools', groups: ['tools'] },"
					+ "						{ name: 'others', groups: ['others'] },"
					+ "						{ name: 'about', groups: ['about'] }"
					+ "					],"
					+ "					removeButtons: 'Save,NewPage,Preview,Print,Templates,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,CreateDiv,Language,Anchor,Flash,Smiley,PageBreak,Iframe,ShowBlocks,About',"
					+ "				});"
					+ "");
			script.append("$('input:radio[name=sec_pol_cat]').click(function(){"
					+ "			if($(this).val()==1){"
					+ "				$('.data_type_count').show();"
					+ "				$('.data_type_ox').hide();"
					+ "			}else{"
					+ "				$('.data_type_count').hide();"
					+ "				$('.data_type_ox').show();"
					+ "			}"
					+ "		});");
			script.append("$('input:radio[name=gajeomindc]').click(function(){"
					+ "			if($(this).val()=='Y'){"
					+ "				$('#weight_val').val(0);"
					+ "				$('.is_weight').hide();"
					+ "			}else{"
					+ "				$('#weight_val').val();"
					+ "				$('.is_weight').show();"
					+ "			}"
					+ "		});");
			script.append("$('.btn_columns_info').click(function(e){"
					+ "			var sec_pol_id=$('#sec_pol_id').val();"
					+ "			var pol_table=$('#pol_table').val();"
					+ "			var pol_columns=$('#pol_columns').val();"
					+ "			$.ajax({"
					+ "				data: {sec_pol_id:sec_pol_id,pol_table:pol_table,pol_columns:pol_columns},"
					+ "				url: '/man/getPolTableNColumnInfo.do',"
					+ "				type: 'POST',"
					+ "				dataType: 'json',"
					+ "				error: function (jqXHR, textStatus, errorThrown) {"
					+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
					+ "				},"
					+ "				success: function (data) {"
					+ "					if (data.ISOK) {"
					+ "						$('.popup_dialogbox').html(data.popup_body);"
					
					+ "						$('.popup_dialogbox').dialog({ width: 880, height: 535 });"
					+ "						$('.popup_dialogbox').dialog('open');"
					+ "						$('.popup_dialogbox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });"
				
					+ "					}else{alert(data.MSG); }"
					+ "				}"
					+ "			});"
					+ "		});");
			script.append("$('#con_type').change(function(e){"
					+ "			var contype = $(this).val();"
					+ "			$.ajax({"
					+ "				data: {contype:contype},"
					+ "				url: '/man/getPolConSelectType.do',"
					+ "				type: 'POST',"
					+ "				dataType: 'json',"
					+ "				error: function (jqXHR, textStatus, errorThrown) {"
					+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
					+ "				},"
					+ "				success: function (data) {"
					+ "					if (data.ISOK) {"
					+ "						$('.pol_con_body_count').empty().append(data.con_body);"
					+ "					}else{alert(data.MSG); }"
					+ "				}"
					+ "			});"
					+ "		});");	
			script.append("$('#majrcode').change(function(e){"
					+ "			var buseoindc = $('option:selected',$(this)).attr('isbs');"
					+ "			$('#buseoindc').val(buseoindc);"
					+ "			var majcode = $(this).val();"
					+ "			$.ajax({"
					+ "				data: {majcode:majcode},"
					+ "				url: '/man/getDiagMinrCodeList.do',"
					+ "				type: 'POST',"
					+ "				dataType: 'json',"
					+ "				error: function (jqXHR, textStatus, errorThrown) {"
					+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
					+ "				},"
					+ "				success: function (data) {"
					+ "					if (data.ISOK) {"
					+ "						$('#minrcode > option').each(function(){$(this).remove();});"
					+ "						$('#minrcode').append(\"<option value=''>중진단선택</option>\");"
					+ "						for(var i=0;i<data.list.length;i++){"
					+ "							$('#minrcode').append('<option value=\"'+ data.list[i][\"diag_minr_code\"] +'\">' + data.list[i][\"diag_desc\"] +'</option>');"
					+ "						}"
					+ "						changeDiageMajrCode(buseoindc);"
					+ "					}else{alert(data.MSG); }"
					+ "				}"
					+ "			});"
					+ "		});");
			script.append("function changeDiageMajrCode(buseoindc){"
					+ "		$('.TBS4').find('tr').each(function(){"
					+ "			if(buseoindc=='Y'){"
					+ "				if($(this).hasClass('is_notbuseo')){"
					+ "					$(this).hide();"
					+ "				}"
					+ "				if($(this).hasClass('is_buseo')){"
					+ "					$(this).show();"
					+ "				}"
					+ "			}else{"
					+ "				if($(this).hasClass('is_notbuseo')){"
					+ "					$(this).show();"
					+ "				}"
					+ "				if($(this).hasClass('is_buseo')){"
					+ "					$(this).hide();"
					+ "				}"
					+ "			}"
					+ "		});"
					
					+ "}");	
			script.append("$('.btn_popup_save').click(function(){"
					+ "			var sec_pol_id=$('#sec_pol_id').val();"
					+ "			var sec_pol_desc=$('#sec_pol_desc').val();"
					+ "			var sec_sol_id=$('#sec_sol_id').val();"
					+ "			var majrcode=$('#majrcode').val();"
					+ "			var minrcode=$('#minrcode').val();"
					+ "			var sec_pol_cat = $('input:radio[name=sec_pol_cat]:checked');"
					//+ "			var critical_type = $('input:radio[name=critical_value_type]:checked');"
					+ "			var critical_type=$('#critical_value_type');"
					+ "			var critical_val=$('#critical_val').val();"
					+ "			var weight_val=$('#weight_val').val();"
					+ "			var isautosanct = $('input:radio[name=isautosanct]:checked');"
					+ "			var isautoappr = $('input:radio[name=isautoappr]:checked');"
					+ "			var appr_line_code=$('#appr_line_code').val();"
					+ "			var isfile = $('input:radio[name=isfile]:checked');"
					+ "			var noti=$('#noti').val();"
					+ "			var isused = $('input:radio[name=isused]:checked');"
					+ "			var pol_table=$('#pol_table').val();"
					+ "			var pol_columns=$('#pol_columns').val();"
					+ "			var exe_para=$('#exe_para').val();"
					+ "			var cond_field1=$('#cond_field1').val();"
					+ "			var ispcgact=$('#ispcgact').val();"
					+ "			var sec_pol_notice=$('#polnoti').val();"
					+ "			var content = CKEDITOR.instances['editor'].getData();"
					+ "			var sec_pol_notice_detail=content.replace(/(<([^>]+)>)/ig, \"\");"
					+ "			var buseoindc=$('#buseoindc').val();"
					+ "			var gajeomindc=$('input:radio[name=gajeomindc]:checked');"
					+ "			if(majrcode == ''){"
					+ "				alert('대진단을 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(minrcode == ''){"
					+ "				alert('중진단을 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(sec_pol_cat.length <= 0){"
					+ "				alert('데이타 타입을 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			var con_cnt=sec_pol_cat.val()=='01' ? $('#con_type').val() : $('#x_con_type').val();"
					+ "			var con_scor_1=sec_pol_cat.val()=='01' ? $('#con_scor_1').val() : $('#x_con_scor_1').val();"
					+ "			var con_from_1=sec_pol_cat.val()=='01' ? $('#con_from_1').val() : $('#x_con_from_1').val();"
					+ "			var con_to_1=sec_pol_cat.val()=='01' ? $('#con_to_1').val() : $('#x_con_to_1').val();"
					+ "			var con_scor_2=sec_pol_cat.val()=='01' ? $('#con_scor_2').val() : $('#x_con_scor_2').val();"
					+ "			var con_from_2=sec_pol_cat.val()=='01' ? $('#con_from_2').val() : $('#x_con_from_2').val();"
					+ "			var con_to_2=sec_pol_cat.val()=='01' ? $('#con_to_2').val() : $('#x_con_to_2').val();"
					+ "			var con_scor_3=sec_pol_cat.val()=='01' ? $('#con_scor_3').val() : $('#x_con_scor_3').val();"
					+ "			var con_from_3=sec_pol_cat.val()=='01' ? $('#con_from_3').val() : $('#x_con_from_3').val();"
					+ "			var con_to_3=sec_pol_cat.val()=='01' ? $('#con_to_3').val() : $('#x_con_to_3').val();"
					+ "			var con_scor_4=sec_pol_cat.val()=='01' ? $('#con_scor_4').val() : $('#x_con_scor_4').val();"
					+ "			var con_from_4=sec_pol_cat.val()=='01' ? $('#con_from_4').val() : $('#x_con_from_4').val();"
					+ "			var con_to_4=sec_pol_cat.val()=='01' ? $('#con_to_4').val() : $('#x_con_to_4').val();"
					+ "			var con_scor_5=sec_pol_cat.val()=='01' ? $('#con_scor_5').val() : $('#x_con_scor_5').val();"
					+ "			var con_from_5=sec_pol_cat.val()=='01' ? $('#con_from_5').val() : $('#x_con_from_5').val();"
					+ "			var con_to_5=sec_pol_cat.val()=='01' ? $('#con_to_5').val() : $('#x_con_to_5').val();"
					+ "			var sanc_indc=$('input:radio[name=sanc_indc]:checked');"
					
					+ "			if(critical_val == ''){"
					+ "				alert('임계치를 입력하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(!critical_val.isNumber()){"
					+ "				alert('임계치는 숫자로 입력하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(!weight_val.isNumber()){"
					+ "				alert('가중치는 숫자로 입력하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(con_cnt =='0'){"
					+ "				alert('점수분류 선택 및 값을 입력하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(sec_pol_cat.val()=='01'){"
					+ "				if($('#con_type').val() == ''){ "
					+ "					alert('조건카운트 조건수 선택 및 조건카운트를 입력하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "			}"
					+ "			var isnum = true;"
					+ "			$(sec_pol_cat.val()=='01' ? '.pol_con_body_count' : '.pol_con_body_ox').find('input:text').each(function(){"
					+ "				if(!$(this).val().isNumber()){ isnum = false;}"
					+ "			});"
					+ "			if(!isnum){"
					+ "				alert('조건 카운트는 숫자로 입력하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(buseoindc=='N'){"
					+ "				if(isautosanct.length <= 0){"
					+ "					alert('제재조치유형을 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "				if(isautoappr.length <= 0){"
					+ "					alert('소명신청유형을 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "				if(appr_line_code == ''){"
					+ "					alert('결재라인을 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "				if(isfile.length <= 0){"
					+ "					alert('소명파일 첨부여부를 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "				if(sanc_indc.length <= 0){"
					+ "					alert('제재조치 대상여부를 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "			}else{"
					+ "				if(gajeomindc.length <= 0){"
					+ "					alert('가점/감점여부를 선택하여 주세요.');"
					+ "					return false;"
					+ "				}"
					+ "			}	"
					+ "			if(isused.length <= 0){"
					+ "				alert('사용여부를 선택하여 주세요.');"
					+ "				return false;"
					+ "			}"
					+ "			if(!confirm('지수화정책 정보를 저장 하시겠습니까?')){"
					+ "				return false;"
					+ "			}"

					+ "			var data={sec_pol_id:sec_pol_id"
					+ "					 ,sec_pol_desc:sec_pol_desc"
					+ "					 ,sec_sol_id:sec_sol_id"
					+ "					 ,diag_majr_code:majrcode"
					+ "					 ,diag_minr_code:minrcode"
					+ "					 ,sec_pol_cat:sec_pol_cat.val()"
					+ "					 ,pol_critical_value_type:critical_type.val()"
					+ "					 ,pol_critical_vlaue:critical_val"
					+ "					 ,pol_weight_value:weight_val"
					+ "					 ,sanc_cate:buseoindc=='N' ? isautosanct.val() : '1'"
					+ "					 ,appr_cate:buseoindc=='N' ? isautoappr.val() : '1'"
					+ "					 ,appr_line_code:appr_line_code"
					+ "					 ,appr_attach_indc:buseoindc=='N' ? isfile.val() : '1'"
					+ "					 ,appr_note:noti"
					+ "					 ,use_indc:isused.val()"
					+ "					 ,sanc_indc:buseoindc=='N' ? sanc_indc.val() == 'Y' ? true : false : false"
					+ "					 ,exe_para:exe_para"
					+ "					 ,cond_field1:cond_field1"
					+ "					 ,ispcgact:ispcgact"
					+ "					 ,sec_pol_notice:sec_pol_notice"
					+ "					 ,sec_pol_notice_detail:sec_pol_notice_detail"
					+ "					 ,sec_pol_table:pol_table"
					+ "					 ,sec_pol_table_col:pol_columns"
					+ "					 ,buseo_indc:buseoindc"
					+ "					 ,gajeom_indc:buseoindc=='N' ? 'N' : gajeomindc.val()"
					+ "					 ,con_cnt:con_cnt"
					+ "					 ,con_scor_1:con_scor_1"
					+ "					 ,con_from_1:con_from_1"
					+ "					 ,con_to_1:con_to_1"
					+ "					 ,con_scor_2:con_scor_2"
					+ "					 ,con_from_2:con_from_2"
					+ "					 ,con_to_2:con_to_2"
					+ "					 ,con_scor_3:con_scor_3"
					+ "					 ,con_from_3:con_from_3"
					+ "					 ,con_to_3:con_to_3"
					+ "					 ,con_scor_4:con_scor_4"
					+ "					 ,con_from_4:con_from_4"
					+ "					 ,con_to_4:con_to_4"
					+ "					 ,con_scor_5:con_scor_5"
					+ "					 ,con_from_5:con_from_5"
					+ "					 ,con_to_5:con_to_5};"
					+ "			$.ajax({"
					+ "				data: data,"
					+ "				url: '/man/setPolInfoModify.do',"
					+ "				type: 'POST',"
					+ "				dataType: 'json',"
					+ "				error: function (jqXHR, textStatus, errorThrown) {"
					+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
					+ "				},"
					+ "				success: function (data) {"
					+ "					if (data.ISOK) {"
					+ "						$('.DialogBox').data('is_save', 'Y');"
					+ "						$('.DialogBox').dialog('close');"
					+ "					}else{alert(data.MSG); }"
					+ "				}"
					+ "			});"
					+ "	});");
			
			script.append("});");
			script.append("</script>");
			popupBody.append(script.toString());
			
			isOk = true;

			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);
			hMap.put("popup_body", popupBody.toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			msg = ex.toString();
			hMap.put("ISOK", isOk);
			hMap.put("MSG", msg);

		}

		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(hMap));

	}	

	@RequestMapping(value="/man/getPolConSelectType.do")
	public void getPolConSelectType(HttpServletRequest request, String contype, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	PolConVO polConVO = new PolConVO();
	    	String str = getPolWeightPointTableHtml(contype, polConVO);
	    	
	    	isOk = true;
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("con_body", str);
		}
		catch(Exception ex)
		{
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
	 * 정책상세 테이블 컬럼 리스트 조회
	 * @param request
	 * @param sec_pol_id
	 * @param pol_table
	 * @param pol_columns
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getPolTableNColumnInfo.do")
	public void getPolTableNColumnInfo(HttpServletRequest request
			, String sec_pol_id
			, String pol_table
			, String pol_columns
			, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	List<EgovMap> tables = secPolService.getPolSourceLogTables("public");
	    	StringBuffer popupBody = new StringBuffer();
	    	
	    	popupBody.append("<div class='ly_block2' style='width:840px;'>");
	    	popupBody.append(" 	<div class='subTT'><span>로그테이블 및 컬럼 선택</span></div>");
	    	popupBody.append("	<div class='ly_left' style='width:410px'>");
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' />로그테이블을 선택하여 주세요.</div>");
	    	popupBody.append("		 <div style='width:100%;height:300px;overflow-y:auto;border-bottom:1px solid #9e9e9e' class='pd10'>"
					+ "					<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
					+ "						<colgroup>"
					+ "						<col style='width:40px'>"
					+ "						<col style='width:200px'>"
					+ "						<col style='width:*'>"
					+ "						</colgroup>"
					+ "						<tr>"
					+ "							<th>선택</th>"
					+ "							<th>테이블명</th>"
					+ "							<th>테이블설명</th>"
					+ "						</tr>"
					+ "						<tbody class='table_body'>");
					for(EgovMap row:tables){
						popupBody.append("<tr>");
						popupBody.append(String.format("<td>%s</td>", String.format("<input type='radio' name='sel_table' value='%s' %s/>"
																		, row.get("tablename")
																		, row.get("tablename").equals(pol_table) ? "checked" : "")));
						popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("tablename")));
						popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("tabledesc")));
						popupBody.append("</tr>");
					}
			popupBody.append("				</tbody>"
					+ "					</table>"
					+ "				</div>");
	    	popupBody.append("	</div>");
	    	popupBody.append("	<div class='ly_right' style='width:410px'>");
	    	popupBody.append("		 <div class='popTT'><img src='/img/icon_arw4.jpg' /> 로그테이블 컬럼내역</div>");
	    	popupBody.append("  	 <div style='width:100%;height:300px;overflow-y:auto;border-bottom:1px solid #9e9e9e' class='marT10'>"
					+ "					<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
					+ "					<colgroup>"
					+ "					<col style='width:40px'>"
					+ "					<col style='width:200px'>"
					+ "					<col style='width:*'>"
					+ "					</colgroup>"
					+ "					<tr>"
					+ "						<th>선택</th>"
					+ "						<th>컬럼명</th>"
					+ "						<th>컬럼설명</th>"
					+ "					</tr>"
					+ "					<tbody class='columns_body'>");
			if(!pol_table.equals("")){
				String[] col_list = null;
				if(!pol_columns.trim().equals("")){
					col_list = pol_columns.trim().split(",");
				}
				List<EgovMap> columns = secPolService.getPolSourceLogTableColumns(pol_table);
		    	for(EgovMap row:columns){
		    		boolean ischecked = false;
		    		if(col_list != null){
			    		for(String col:col_list){
			    			if(col.trim().equals(row.get("columnname").toString().trim())){
				    			ischecked = true;
				    			break;
				    		}
				    	}
		    		}
		    	
		    		if(row.get("columnname").toString().length() > 5){
		    			if(!row.get("columnname").toString().subSequence(0, 5).equals("sldm_")){
		    				popupBody.append("<tr>");
		    				popupBody.append(String.format("<td>%s</td>", String.format("<input type='checkbox' name='sel_colmun' value='%s' %s/>"
		    																, row.get("columnname"), ischecked ? "checked" : "")));
		    				popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columnname")));
		    				popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columndesc")));
		    				popupBody.append("</tr>");
			    		}
		    		}else{
		    			popupBody.append("<tr>");
		    			popupBody.append(String.format("<td>%s</td>", String.format("<input type='checkbox' name='sel_colmun' value='%s' %s/>"
		    																, row.get("columnname"), ischecked ? "checked" : "")));
		    			popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columnname")));
		    			popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columndesc")));
		    			popupBody.append("</tr>");
		    		}
		    		
		    	}
			}
	    	popupBody.append("		</tbody>");
	    	popupBody.append("		</tbody></table>"
					+ "				</div>");	
	    	popupBody.append("	</div>");
	    	popupBody.append("	<div class='btn_black2'><a class='btn_black btn_table_popup_save'><span style='color:#ffffff'>저장</span></a> <a class='btn_black btn_subdialogbox_close'><span style='color:#ffffff'>닫기</span></a></div>");
	    	popupBody.append(String.format("</div>"
	    			+ "			<input type='hidden' name='sel_table_name' id='sel_table_name' value='%s' />", pol_table));
	    
	    	/*
	    	
	    	popupBody.append("<div id='wrap_pop' style='background: #fff'>");
			popupBody.append("	<div class='WP_tit' style='cursor:move;'>");
			popupBody.append("		<ul>");
			popupBody.append("		<li class='WPT_ic'><img src='/img/ic_stit2.png' alt='아이콘'></li>");
			popupBody.append(String.format("<li class='WPT_txt'>%s</li>","로그테이블 및 컬럼 선택"));
			popupBody.append("		<li class='WPT_btn'><div class='del2'><a class='btn_subdialogbox_close' style='cursor:pointer;'><span class='blind'>닫기</span></a></div></li>");
			popupBody.append("		</ul>");
			popupBody.append("	</div>");
			popupBody.append("	<div class='WP_con'>");
			popupBody.append("	<div>");
					+ "					<div class='S_tit2'>"
					+ "						<ul>"
					+ "						<li><img src='/img/dot3.png' alt='타이틀'></li>"
					+ "						<li class='ST_txt'>로그테이블을 선택하여 주세요.</li>"
					+ "						</ul>"
					+ "					</div>"
					+ "					<div style='width:100%;height:300px;overflow-y:auto;border-bottom:1px solid #9e9e9e' class='marT10'>"
					+ "						<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
					+ "						<colgroup>"
					+ "						<col style='width:40px'>"
					+ "						<col style='width:180px'>"
					+ "						<col style='width:*'>"
					+ "						</colgroup>"
					+ "						<tr>"
					+ "							<th>선택</th>"
					+ "							<th>테이블명</th>"
					+ "							<th>테이블설명</th>"
					+ "						</tr>"
					+ "						<tbody class='table_body'>");
					for(EgovMap row:tables){
						popupBody.append("<tr>");
						popupBody.append(String.format("<td>%s</td>", String.format("<input type='radio' name='sel_table' value='%s' />", row.get("tablename"))));
						popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("tablename")));
						popupBody.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("tabledesc")));
						popupBody.append("</tr>");
					}
			popupBody.append("				</tbody>"
					+ "						</table>"
					+ "					</div>"
					+ "				</div>");
			popupBody.append("<div class='WC_part2' style='width:400px;'>");
			popupBody.append("					<div class='S_tit2'>"
					+ "						<ul>"
					+ "						<li><img src='/img/dot3.png' alt='타이틀'></li>"
					+ "						<li class='ST_txt'>로그테이블 컬럼내역</li>"
					+ "						</ul>"
					+ "					</div>"
					+ "					<div style='width:100%;height:300px;overflow-y:auto;border-bottom:1px solid #9e9e9e' class='marT10'>"
					+ "					<table border='0' class='TBS3' cellpadding=0 cellspacing=0>"
					+ "					<colgroup>"
					+ "					<col style='width:40px'>"
					+ "					<col style='width:180px'>"
					+ "					<col style='width:*'>"
					+ "					</colgroup>"
					+ "					<tr>"
					+ "						<th>선택</th>"
					+ "						<th>컬럼명</th>"
					+ "						<th>컬럼설명</th>"
					+ "					</tr>"
					+ "					<tbody class='columns_body'></tbody>"
					+ "					</table>"
					+ "					</div>"
					+ "				</div>"
					+ "");
			popupBody.append("		<div style='width:95%' class='marT10 disp'>");
			popupBody.append("			<div class='btn3 F_right'><a class='btn_subdialogbox_close' style='cursor:pointer;'>닫기</a></div>");
			popupBody.append("			<div class='btn3 F_right'><a class='btn_table_popup_save' style='cursor:pointer;'>저장</a></div>");
			popupBody.append("		</div>");
			popupBody.append("	</div>");
			popupBody.append("</div>"
					+ "		  <input type='hidden' name='sel_table_name' id='sel_table_name' value='' />");
			*/
	    	
	    	
			StringBuffer script = new StringBuffer();
			script.append("<script type='text/javascript'>");
			script.append("$(function () {");
			script.append("$('input:radio[name=sel_table]').click(function(){"
					+ "		var tname=$(this).val();"
					+ "		$('#sel_table_name').val(tname);"
					+ "		$.ajax({"
					+ "			data: {tname:tname},"
					+ "			url: '/man/getPolTableColumnsList.do',"
					+ "			type: 'POST',"
					+ "			dataType: 'json',"
					+ "			error: function (jqXHR, textStatus, errorThrown) {"
					+ "					if(jqXHR.status == 401){"
					+ "						alert('인증정보가 만료되었습니다.');"
					+ "						location.href='/';"
					+ "					}else{"
					+ "						alert(textStatus + '\\r\\n' + errorThrown);"
					+ "					}"
					+ "			},"
					+ "			success: function (data) {"
					+ "				if (data.ISOK) {"
					+ "					$('.columns_body').empty().append(data.list_body);"
					+ "				}else{alert(data.MSG); }"
					+ "			}"
					+ "		});"
					+ "	});");
			script.append("$('.btn_table_popup_save').click(function(){"
					
					+ "		var col_val=[];"
					+ "		$('.columns_body').find('input:checkbox[name=sel_colmun]').each(function(){"
					+ "			if($(this).is(':checked')){ col_val.push($(this).val());}"
					+ "		});"
					+ "		if(col_val.toString()==''){return false;}"
					+ "		$('#pol_table').val($('#sel_table_name').val());"
					+ "		$('#pol_columns').val(col_val.toString());"
					+ "		$('.popup_dialogbox').dialog('close');"
					+ "});");

			script.append("});");
			script.append("</script>");
			popupBody.append(script.toString());
	    	
	    	
		    isOk = true;
				
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("popup_body", popupBody.toString());
		}
		catch(Exception ex)
		{
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
	 * 정책리스트 컬럼리스트 UI 정보 조회
	 * @param request
	 * @param tname
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getPolTableColumnsList.do")
	public void getPolTableColumnsList(HttpServletRequest request, String tname, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	StringBuffer str = new StringBuffer();
	    	List<EgovMap> columns = secPolService.getPolSourceLogTableColumns(tname);
	    	for(EgovMap row:columns){
	    		if(row.get("columnname").toString().length() > 5){
	    			if(!row.get("columnname").toString().subSequence(0, 5).equals("sldm_")){
		    			str.append("<tr>");
			    		str.append(String.format("<td>%s</td>", String.format("<input type='checkbox' name='sel_colmun' value='%s' />", row.get("columnname")) ));
			    		str.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columnname")));
			    		str.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columndesc")));
			    		str.append("</tr>");
		    		}
	    		}else{
	    			str.append("<tr>");
		    		str.append(String.format("<td>%s</td>", String.format("<input type='checkbox' name='sel_colmun' value='%s' />", row.get("columnname")) ));
		    		str.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columnname")));
		    		str.append(String.format("<td style='text-align:left;padding-left:10px;'>%s</td>", row.get("columndesc")));
		    		str.append("</tr>");
	    		}
	    			
	    		
	    		
	    	}
	    	
	    	isOk = true;
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("list_body", str.toString());
		}
		catch(Exception ex)
		{
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
	 * 정책정보 수정
	 * @param request
	 * @param secPolInfo
	 * @param polConInfo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/setPolInfoModify.do")
	public void setPolInfoModify(HttpServletRequest request, SecPolInfoVO secPolInfo, PolConVO polConInfo, HttpServletResponse response, PolTargetInfoVO polTargetInfoVO) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	if(secPolInfo.getIspcgact().equals("Y") && !secPolInfo.getCond_field1().equals(""))
	    	{
	    		secPolInfo.setPcg_indc(true);
	    		secPolInfo.setExe_para(secPolInfo.getCond_field1());
	    	}else{
	    		secPolInfo.setPcg_indc(false);
	    		secPolInfo.setExe_para("");
	    	}
	    	
	    	int ret = secPolService.setModifyPolInfo(secPolInfo, polConInfo, polTargetInfoVO);
		    isOk = ret == 0;
			if(ret == -1){
				msg = "지수정책 저장중 오류가 발생하였습니다.";
			}
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		}
		catch(Exception ex)
		{
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
	 * 진단정책 코드 정보 조회
	 * @param request
	 * @param majcode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/getDiagMinrCodeList.do")
	public void getSelectedOrglist(HttpServletRequest request, String majcode, HttpServletResponse response) throws Exception {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
	    try
		{
	    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
			if (!isAuthenticated) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				throw new Exception("AUTH ERROR!");
		    }
			
	    	List<MenuItemVO> menuList =  comService.getSolMenuList();	
	    	
	    	List<MenuItemVO> diagList = new ArrayList<MenuItemVO>();
	    	for(MenuItemVO row:menuList){
	    		if(row.getDiag_majr_code().equals(majcode)){
	    			if(!row.getDiag_majr_code().equals((row.getDiag_minr_code()))){
	    				diagList.add(row);
	    			}
	    		}
	    	}
		    isOk = true;
				
		    map.put("ISOK", isOk);
		    map.put("MSG", msg);
		    map.put("list", diagList);
		}
		catch(Exception ex)
		{
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
	 * 지수화정책 리스트 Export Excel
	 * @param request
	 * @param searchVO
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/man/pollistexportexcel.do")
	public void pollistexportexcel(HttpServletRequest request
								, @ModelAttribute("searchVO") PolicySearchVO searchVO
								, HttpServletResponse response) throws Exception {
		
		String fileName = "지수화정책_리스트";
		
		ExcelInitVO excelVO = new ExcelInitVO();
		excelVO.setFileName(fileName);
		excelVO.setSheetName("지수화정책현황");
		excelVO.setTitle("지수화정책현황");
		excelVO.setHeadVal("");
		excelVO.setType("xls");
		List<HashMap<String, Object>> list = secPolService.getSecPolListForExportExcel(searchVO);
		List<String> head = new ArrayList<String>();
		HashMap<String, Object> log = (HashMap<String, Object>)list.get(0);
	
		for(Object key:log.keySet())
		{
			head.add(ConvertToColumnName(key.toString()));
		}
		excelVO.setHead(head);
		List<EgovMap> excellist = new ArrayList<EgovMap>();
		for(HashMap<?,?> row:list)
		{
			EgovMap map = new EgovMap();
			for(Object key:row.keySet()){
				map.put(key, row.get(key).toString());
			}
			excellist.add(map);
		}
		
		ExcelUtil.hssExcelDown(response, excelVO, excellist);
		
	}
	
	private String ConvertToColumnName(String column){
		String ColumnName = column;
		switch(column){
		case "sec_pol_id" : ColumnName = "정책코드";break;
		case "idx_indc" : ColumnName = "이메일";break;
		case "majr_desc" : ColumnName = "대진단명";break;
		case "minr_desc" : ColumnName = "중진단명";break;
		case "sec_pol_desc" : ColumnName = "정책명";break;
		case "sec_pol_cat_desc" : ColumnName = "구분";break;
		case "pol_weight_value" : ColumnName = "가중치";break;
		case "use_indc" : ColumnName = "사용여부";break;
		/*case "appr_line_code" : ColumnName = "소명결재라인";break;
		case "appr_cate" : ColumnName = "소명신청";break;
		case "sanc_cate" : ColumnName = "제재조치";break;*/
		case "bigo" : ColumnName = "비고";break;
		default : ColumnName = column;break;
		}
		
		return ColumnName;
	}
	/**
	 * 정책분석 - 분석웹 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pol/polanalysis.do")
	public String polanalysis(HttpServletRequest request,
							HttpServletResponse response,
							ModelMap model) throws Exception{
		CommonUtil.topMenuToString(request, "admin", comService);
		CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.POLSTATIC);
	
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("majCode", MajrCodeInfo.AnalysisWebCode);
		hmap.put("minCode", "URL");
		CodeInfoVO urlInfo = comService.getCodeInfo(hmap);
		model.addAttribute("analysisurl", String.format("%s/polanalysis/policyinfolist.do", urlInfo.getCode_desc()));
		//model.addAttribute("analysisurl", String.format("http://localhost:8088/polanalysis/policyinfolist.do", urlInfo.getCode_desc()));
		
		return "man/pol/polanalysis";
		
	}
	
	/**
	 * 정책 대상 정보 삭제
	 * @throws Exception 
	 */
	@RequestMapping(value = "/man/polTargetInfoDelete.do") 
	public void polTargetInfoDelete(HttpServletRequest request,
			HttpServletResponse response,
			PolTargetInfoVO polTargetInfoVO,
			ModelMap model) throws Exception{
	
		//CommonUtil.topMenuToString(request, "admin", comService);
		//CommonUtil.adminLeftMenuToString(request, LeftMenuInfo.SDCHECKLIST);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String,Object>();
		String msg = "Error!!";
	    Boolean isOk = false; 
		
		try {
			secPolService.polTargetInfoDelete(polTargetInfoVO);
			
			isOk = true;
			map.put("ISOK", isOk);
			map.put("MSG", msg);
			
		} catch (Exception e) { 
			e.printStackTrace();
			msg = e.toString();
			map.put("ISOK", isOk);
			map.put("MSG", msg);
		} 
		
		response.setContentType("application/json");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(gson.toJson(map));
		
	}
}
