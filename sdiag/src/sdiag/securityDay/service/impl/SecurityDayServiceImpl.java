package sdiag.securityDay.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.man.service.PolTargetInfoVO;
import sdiag.securityDay.service.SdCheckListGroupVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.securityDay.service.SdQuestionInfoVO;
import sdiag.securityDay.service.SdQuestionMcVO;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.securityDay.service.SdSearchVO;
import sdiag.securityDay.service.SdTargetInfoVO;
import sdiag.securityDay.service.SecurityDayService;


@Service("SecurityDayService")
public class SecurityDayServiceImpl implements SecurityDayService{
	@Resource(name = "SecurityDayDAO")
	private SecurityDayDAO securityDayDAO;

	/**
	 *  점검표 LIST
	 */
	public HashMap<String, Object> getSdCheckList(SdSearchVO searchVO) throws Exception {
		List<SdCheckListVO> List = securityDayDAO.getSdCheckList(searchVO);
		int TotalCount = securityDayDAO.getSdCheckListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  점검표 seq 가져오기
	 */
	public int getSdCheckNo() throws Exception {
		return securityDayDAO.getSdCheckNo();
	}

	/**
	 *  문항번호 가져오기
	 */
	public int getQuestionNum(int sdCheckNo) throws Exception {
		return securityDayDAO.getQuestionNum(sdCheckNo);
	}

	/**
	 *  점검표 정보 저장
	 */
	public void setSdCheckListInfoInsert(SdCheckListVO sdCheckListVO,  SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.setSdCheckListInfoInsert(sdCheckListVO);
	}
	
	
	/**
	 *  문항 정보 저장
	 */
	public void setSdQuestionInfoInsert(SdQuestionInfoVO sdQuestionInfoVO, SdQuestionMcVO sdQuestionMcVO)
			throws Exception {
		securityDayDAO.setSdQuestionInfoInsert(sdQuestionInfoVO);
		setQuestionMcInsert(sdQuestionInfoVO, sdQuestionMcVO);
	}

	/**
	 *  점거표 정보 가져오키
	 */
	public SdCheckListVO getSdCheckListInfo(SdCheckListVO sdCheckListVO)
			throws Exception {
		return securityDayDAO.getSdCheckListInfo(sdCheckListVO);
	}

	/**
	 *  문항 정보 리스트
	 */
	public List<SdQuestionInfoVO> getQustionList(SdCheckListVO sdCheckListVO)
			throws Exception {
		return securityDayDAO.getQustionList(sdCheckListVO);
	}

	/**
	 *  문항 상세정보
	 */
	public SdQuestionInfoVO getQuestionInfo(SdQuestionInfoVO sdQuestionInfoVO)
			throws Exception {
		return securityDayDAO.getQustionInfo(sdQuestionInfoVO);
	}

	/**
	 *  문항 상세정보 수정
	 */
	public void setSdQuestionInfoUpdate(SdQuestionInfoVO sdQuestionInfoVO, SdQuestionMcVO sdQuestionMcVO)
			throws Exception {
		securityDayDAO.setSdQuestionInfoUpdate(sdQuestionInfoVO);
		setQuestionMcInsert(sdQuestionInfoVO, sdQuestionMcVO);
		if(sdQuestionMcVO.getU_exNum() !=null && sdQuestionMcVO.getU_exNum() !="" && sdQuestionMcVO.getU_exText() !=null && sdQuestionMcVO.getU_exText() !=""){
			String[] u_exNumArr = sdQuestionMcVO.getU_exNum().split(","); 
			String[] u_exTextArr = sdQuestionMcVO.getU_exText().split(",");
			for (int i=0; i<u_exNumArr.length; i++){
				SdQuestionMcVO sdQuestionMcInfo = new SdQuestionMcVO();
				
				sdQuestionMcInfo.setSdCheckNo(sdQuestionInfoVO.getSdCheckNo());
				sdQuestionMcInfo.setQuestionNum(sdQuestionInfoVO.getQuestionNum());
				sdQuestionMcInfo.setRgdtEmpNo(sdQuestionInfoVO.getRgdtEmpNo());
				sdQuestionMcInfo.setU_exNum(u_exNumArr[i]);
				sdQuestionMcInfo.setU_exText(u_exTextArr[i]);
				
				securityDayDAO.setQuestionMcUpdate(sdQuestionMcInfo);
				
			}
		}
	}

	/**
	 *  문항 상세정보 삭제
	 */
	public void sdQuestionInfoDelete(SdQuestionInfoVO sdQuestionInfoVO) 
			throws Exception {
		securityDayDAO.sdQuestionInfoDelete(sdQuestionInfoVO);
	}

	/**
	 *  점검표 상세정보 수정
	 */
	public void setSdCheckListInfoUpdate(SdCheckListVO sdCheckListVO,  SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.setSdCheckListInfoUpdate(sdCheckListVO);
	}

	/**
	 *  점검표 상세정보 삭제
	 */
	public void sdCheckListInfoDelete(SdCheckListVO sdCheckListVO)
			throws Exception {
		securityDayDAO.sdCheckListInfoDelete(sdCheckListVO);
	}
	
	/**
	 *  점검표 문항 보기 정보 저장
	 */
	public void setQuestionMcInsert(SdQuestionInfoVO sdQuestionInfoVO, SdQuestionMcVO sdQuestionMcVO)
			throws Exception {
		
		if(sdQuestionMcVO.getExNum() !=null && sdQuestionMcVO.getExNum() !="" && sdQuestionMcVO.getExText() !=null && sdQuestionMcVO.getExText() !=""){
			String[] exNumArr = sdQuestionMcVO.getExNum().split(","); 
			String[] exTextArr = sdQuestionMcVO.getExText().split(",");
			if(exNumArr.length > 0 && exTextArr.length > 0){
				for (int i=0; i<exNumArr.length; i++){
					SdQuestionMcVO sdQuestionMcInfo = new SdQuestionMcVO();
					
					sdQuestionMcInfo.setSdCheckNo(sdQuestionInfoVO.getSdCheckNo());
					sdQuestionMcInfo.setQuestionNum(sdQuestionInfoVO.getQuestionNum());
					sdQuestionMcInfo.setRgdtEmpNo(sdQuestionInfoVO.getRgdtEmpNo());
					sdQuestionMcInfo.setExNum(exNumArr[i]);
					sdQuestionMcInfo.setExText(exTextArr[i]);
					
					securityDayDAO.setQuestionMcInsert(sdQuestionMcInfo);
					
				}
			}
		}
	}
	
	/**
	 *  문항 보기 정보 삭제
	 */
	public void questionMcDelete(SdQuestionMcVO sdQuestionMcVO)
			throws Exception {
		securityDayDAO.questionMcDelete(sdQuestionMcVO);
	}

	/**
	 *  문항 보기 정보 LIST
	 */
	public List<SdQuestionMcVO> getQuestionMcList(
			SdQuestionInfoVO sdQuestionInfoVO) throws Exception {
		return securityDayDAO.getQuestionMcList(sdQuestionInfoVO);
	}
	
	/**
	 *  점검표 대상 저장
	 */
	private void setSdTargetInfoInsert(SdTargetInfoVO sdTargetInfoVO) throws Exception{
		int cnt =0;
		
		if(sdTargetInfoVO.getTargetCode()!=null && sdTargetInfoVO.getTargetCode() !=""){
		
			String[] sdCheckNoArr = sdTargetInfoVO.getSdCheckNoList().split(",");
			String[] gubunArr = sdTargetInfoVO.getGubun().split(",");
			String[] targetCodeArr = sdTargetInfoVO.getTargetCode().split(",");
			String[] targetTypeArr = sdTargetInfoVO.getTargetType().split(",");
			String[] targetNmArr = sdTargetInfoVO.getTargetNm().split(",");
			
			
			for (int i=0; i<targetCodeArr.length; i++){
				SdTargetInfoVO sdTargetInfo = new SdTargetInfoVO();
				sdTargetInfo.setClGroupNo(sdTargetInfoVO.getClGroupNo());
				sdTargetInfo.setSdCheckNo(Integer.parseInt(sdCheckNoArr[i]));
				sdTargetInfo.setGubun(gubunArr[i]);
				sdTargetInfo.setTargetCode(targetCodeArr[i]);
				sdTargetInfo.setTargetType(targetTypeArr[i]);
				sdTargetInfo.setTargetNm(targetNmArr[i]);
				/* 그룹 상세 여부 존재 여부 체크  - 해당 정보가 존재 하지 않으면 저장*/
				cnt = securityDayDAO.sdTargetInfoCheck(sdTargetInfo);
				
				if(cnt==0)
					securityDayDAO.setSdTargetInfoInsert(sdTargetInfo);
				
				cnt=0;
			}
		}
	}

	/**
	 *  점검표 대상 LIST
	 */
	public List<SdTargetInfoVO> getSdTargetList(SdCheckListGroupVO sdCheckListGroupVO) throws Exception {
		return securityDayDAO.getSdTargetInfoList(sdCheckListGroupVO);
	}

	/**
	 *  점검표 대상 삭제
	 */
	public void sdTargetInfoDelete(SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.sdTargetInfoDelete(sdTargetInfoVO);
	}

	/**
	 *  점검표 그룹 LIST
	 */
	public HashMap<String, Object> getSdCheckListGroupList(SdSearchVO searchVO)
			throws Exception {
		List<SdCheckListGroupVO> List = securityDayDAO.getSdCheckListGroupList(searchVO);
		int TotalCount = securityDayDAO.getSdCheckListGroupTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  점검표 그룹 seq 조회
	 */
	public int getClGroupNo() throws Exception {
		return securityDayDAO.getClGroupNo();
	}

	/**
	 *  점검표 그룹 정보 저장
	 */
	public void setSdCheckListInfoGroupInsert(
			SdCheckListGroupVO sdCheckListGroupVO, SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.setSdCheckListGroupInfoInsert(sdCheckListGroupVO);
		setClGroupInfoInsert(sdCheckListGroupVO);		
		setSdTargetInfoInsert(sdTargetInfoVO);
		
	}

	/**
	 *  점검표 그룹 상세정보 조회
	 */
	public SdCheckListGroupVO getSdCheckListGroupInfo(
			SdCheckListGroupVO sdCheckListGroupVO) throws Exception {
		return securityDayDAO.getSdCheckListGroupInfo(sdCheckListGroupVO);
	}

	/**
	 *  점검표 그룹 정보 삭제
	 */
	public void sdCheckListGroupInfoDelete(SdCheckListGroupVO sdCheckListGroupVO)
			throws Exception {
		securityDayDAO.sdCheckListGroupInfoDelete(sdCheckListGroupVO);
	}

	/**
	 *  점검표 그룹 정보 수정
	 */
	public void setSdCheckListGroupInfoUpdate(
			SdCheckListGroupVO sdCheckListGroupVO, SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.setSdCheckListGroupInfoUpdate(sdCheckListGroupVO);
		setClGroupInfoInsert(sdCheckListGroupVO);		
		setSdTargetInfoInsert(sdTargetInfoVO);
		
	}

	/**
	 *  그룹 대상 점검표 LIST
	 */
	public List<SdTargetInfoVO> getSdClGroupMappingList(
			SdCheckListGroupVO sdCheckListGroupVO) throws Exception {
		return securityDayDAO.getSdClGroupMappingList(sdCheckListGroupVO);
	}

	/**
	 *  점검표 LIST
	 */
	public List<SdCheckListVO> getClList() throws Exception{
		return securityDayDAO.getClList();
	}
	
	/**
	 *  그룹 대상 점검표 저장
	 */
	private void setClGroupInfoInsert(SdCheckListGroupVO sdCheckListGroupVO) throws Exception{
		int cnt =0;
		
		if(sdCheckListGroupVO.getM_sdCheckNo()!=null && sdCheckListGroupVO.getM_sdCheckNo() !=""){
		
			String[] sdCheckNoArr = sdCheckListGroupVO.getM_sdCheckNo().split(",");
			
			
			for (int i=0; i<sdCheckNoArr.length; i++){
				SdTargetInfoVO sdTargetInfo = new SdTargetInfoVO();
				sdTargetInfo.setSdCheckNo(Integer.parseInt(sdCheckNoArr[i]));
				sdTargetInfo.setClGroupNo(sdCheckListGroupVO.getClGroupNo());
				/* 그룹 상세 여부 존재 여부 체크  - 해당 정보가 존재 하지 않으면 저장*/
				cnt = securityDayDAO.sdClGroupMappinfInfoCheck(sdTargetInfo);
				
				if(cnt==0)
					securityDayDAO.setClGroupInfoInsert(sdTargetInfo);
				
				cnt=0;
			}
		}
	}

	/**
	 *  그룹 대상 점검표 삭제
	 */
	public void sdclGroupMappingInfoDelete(SdTargetInfoVO sdTargetInfoVO)  throws Exception{
		securityDayDAO.sdClGroupMappingInfoDelete(sdTargetInfoVO);
	}

	/**
	 *  점검 대상여부 확인
	 */
	public SdResultInfoVO getSdYn(String empNo) {
		SdResultInfoVO sdUseYn = securityDayDAO.getSdYn(empNo);
		
		if(sdUseYn.getQuestionNum()>0){
			sdUseYn.setResultYn("Y");
			if(sdUseYn.getRowNum() >0){
				sdUseYn.setCheckYn("N");
			}else{
				sdUseYn.setCheckYn("Y");
			}
		}else{
			sdUseYn.setResultYn("N");
		}
		return sdUseYn;
	}

	/**
	 *  점검 문항 정보 조회
	 */
	public SdResultInfoVO getSdInfo(SdResultInfoVO sdResultInfoVO) {
		return securityDayDAO.getSdInfo(sdResultInfoVO);
	}

	/**
	 *  점검 문항 결과 정보 저장
	 */
	public void setSdResultInfoSave(SdResultInfoVO sdResultInfoVO) throws Exception{
		securityDayDAO.setSdResultInfoSave(sdResultInfoVO);
	}

	/**
	 *  점검 결과 저장
	 */
	public void setSdResultSave(SdResultInfoVO sdResultInfoVO) {
		securityDayDAO.setSdResultSave(sdResultInfoVO);
	}

	/**
	 *  점검 결과 저장
	 */
	public SdResultInfoVO getSdPreView(SdSearchVO searchVO) {
		return securityDayDAO.getSdPreView(searchVO);
	}

	/**
	 *  점검 상세 대상 삭제
	 */
	public void sdTargetDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.sdTargetDatailInfoDelete(sdTargetInfoVO);
		
	}

	/**
	 *  점검 결과 삭제
	 */
	public void sdResultInfoDelete(SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.sdResultInfoDelete(sdTargetInfoVO);
		
	}

	/**
	 *  점검 상세 결과 삭제
	 */
	public void sdResultDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO)
			throws Exception {
		securityDayDAO.sdResultDatailInfoDelete(sdTargetInfoVO);
		
	}

	/**
	 *  점검 상세 결과 조회
	 */
	public List<EgovMap> getSdResultInfo(SdSearchVO searchVO)
			throws Exception {
		return securityDayDAO.getSdResultInfo(searchVO);
	}

	/**
	 * 
	 *  점검 상세 결과 엑셀다운로드
	 */
	public List<HashMap<String, Object>> getExcelSdResultInfo(
			SdSearchVO searchVO) throws Exception {
		return securityDayDAO.getExcelSdResultInfo(searchVO);
	}

	public String getFileNm(SdResultInfoVO sdResultInfoVO) {
		return securityDayDAO.getFileNm(sdResultInfoVO);
	}

	public int getSdInfoTotalCnt(SdResultInfoVO sdResultInfoVO) {
		return securityDayDAO.getSdInfoTotalCnt(sdResultInfoVO);
	}

	public SdResultInfoVO getSdRDetailInfo(SdResultInfoVO sdResultInfo) {
		return securityDayDAO.getSdRDetailInfo(sdResultInfo);
	}

	public void setSdResultInfoUpdate(SdResultInfoVO sdResultInfoVO) {
		securityDayDAO.setSdResultInfoUpdate(sdResultInfoVO);
	}

	public int getSdPreViewTotalCnt(SdSearchVO searchVO) {
		return securityDayDAO.getSdPreViewTotalCnt(searchVO);
	}

	/**
	 * 지수화 기간 중복 체크
	 * @return
	 * @throws Exception
	 */
	public int getIdxDayCnt(SdCheckListGroupVO sdCheckListGroupVO) {
		return securityDayDAO.getIdxDayCnt(sdCheckListGroupVO);
	}

	/**
	 * 점검 상세 정보 삭제 
	 * @return
	 * @throws Exception
	 */
	public void deleteSdResultDatailInfo(SdResultInfoVO sdResultInfoVO) {
		securityDayDAO.deleteSdResultDatailInfo(sdResultInfoVO);
		
	}
	
}