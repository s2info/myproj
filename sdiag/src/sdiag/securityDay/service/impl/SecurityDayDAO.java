package sdiag.securityDay.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.securityDay.service.SdCheckListGroupVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.securityDay.service.SdQuestionInfoVO;
import sdiag.securityDay.service.SdQuestionMcVO;
import sdiag.securityDay.service.SdResultInfoVO;
import sdiag.securityDay.service.SdSearchVO;
import sdiag.securityDay.service.SdTargetInfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("SecurityDayDAO")
public class SecurityDayDAO extends EgovAbstractDAO {

	/**
	 *  점검표 LIST
	 * @param searchVO 
	 */
	public List<SdCheckListVO> getSdCheckList(SdSearchVO searchVO) throws Exception{
		List<SdCheckListVO> list = (List<SdCheckListVO>)list("securityDay.getSdCheckList", searchVO);
		return list;
	}
	
	/**
	 *  점검표 totalCount
	 * @param searchVO 
	 */
	public int getSdCheckListTotalCount(SdSearchVO searchVO) throws Exception{
		return (int)select("securityDay.getSdCheckListTotalCount", searchVO);
	}

	/**
	 *  점검표 seq 가져오기
	 */
	public int getSdCheckNo() throws Exception{
		return (int)select("securityDay.getSdCheckNo");
	}
	
	
	/**
	 *  문항번호 가져오기
	 * @param sdCheckNo 
	 */
	public int getQuestionNum(int sdCheckNo) throws Exception{
		return (int)select("securityDay.getQuestionNum", sdCheckNo);
	}

	/**
	 *  점검표 정보 저장
	 */
	public void setSdCheckListInfoInsert(SdCheckListVO sdCheckListVO)  throws Exception{
		insert("securityDay.setSdCheckListInfoInsert", sdCheckListVO);
	}
	
	/**
	 *  문항 정보 저장
	 */
	public void setSdQuestionInfoInsert(SdQuestionInfoVO sdQuestionInfoVO)  throws Exception{
		insert("securityDay.setSdQuestionInfoInsert", sdQuestionInfoVO);
	}

	/**
	 *  점검표 정보
	 */
	public SdCheckListVO getSdCheckListInfo(SdCheckListVO sdCheckListVO)  throws Exception{
		return (SdCheckListVO) select("securityDay.getSdCheckListInfo", sdCheckListVO);
	}

	/**
	 *  문항 정보 리스트
	 */
	public List<SdQuestionInfoVO> getQustionList(SdCheckListVO sdCheckListVO) throws Exception{
		List<SdQuestionInfoVO> list = (List<SdQuestionInfoVO>)list("securityDay.getQustionList", sdCheckListVO);
		return list;
	}

	/**
	 *  문항 상세정보
	 */
	public SdQuestionInfoVO getQustionInfo(SdQuestionInfoVO sdQuestionInfoVO) throws Exception{
		return (SdQuestionInfoVO) select("securityDay.getQustionInfo", sdQuestionInfoVO);
	}

	/**
	 *  문항 상세정보 수정
	 */
	public void setSdQuestionInfoUpdate(SdQuestionInfoVO sdQuestionInfoVO) throws Exception{
		update("securityDay.setSdQuestionInfoUpdate", sdQuestionInfoVO);
		
	}

	/**
	 *  문항 상세정보 삭제
	 */
	public void sdQuestionInfoDelete(SdQuestionInfoVO sdQuestionInfoVO) throws Exception{
		delete("securityDay.sdQuestionInfoDelete", sdQuestionInfoVO);
	}

	/**
	 *  점검표 상세정보 수정
	 */
	public void setSdCheckListInfoUpdate(SdCheckListVO sdCheckListVO) throws Exception{
		update("securityDay.setSdCheckListInfoUpdate", sdCheckListVO);
		
	}

	/**
	 *  점검표 상세정보 삭제
	 */
	public void sdCheckListInfoDelete(SdCheckListVO sdCheckListVO) throws Exception{
		delete("securityDay.sdCheckListInfoDelete", sdCheckListVO);
		
	}

	/**
	 *  문항 보기 정보 저장
	 */
	public void setQuestionMcInsert(SdQuestionMcVO sdQuestionMcInfo) throws Exception{
		insert("securityDay.setQuestionMcInsert", sdQuestionMcInfo);
	}

	/**
	 *  문항 보기 정보 수정
	 */
	public void setQuestionMcUpdate(SdQuestionMcVO sdQuestionMcInfo) throws Exception{
		update("securityDay.setQuestionMcUpdate", sdQuestionMcInfo);
	}

	/**
	 *  문항 보기 정보 삭제
	 */
	public void questionMcDelete(SdQuestionMcVO sdQuestionMcVO) throws Exception{
		delete("securityDay.questionMcDelete", sdQuestionMcVO);
		
	}
	
	/**
	 *  문항 정보 리스트
	 */
	public List<SdQuestionMcVO> getQuestionMcList(SdQuestionInfoVO sdQuestionInfoVO) throws Exception{
		List<SdQuestionMcVO> list = (List<SdQuestionMcVO>)list("securityDay.getQuestionMcList", sdQuestionInfoVO);
		return list;
	}
	
	/**
	 *  점검표 대상 정보 저장
	 */
	public void setSdTargetInfoInsert(SdTargetInfoVO sdTargetInfo) throws Exception{
		insert("securityDay.setSdTargetInfoInsert", sdTargetInfo);
		
	}
	
	/**
	 *  점검표 대상 cnt
	 * @param sdTargetInfo 
	 */
	public int sdTargetInfoCheck(SdTargetInfoVO sdTargetInfo) throws Exception{
		return (int)select("securityDay.sdTargetInfoCheck", sdTargetInfo);
	}
	
	/**
	 *  점검표 대상 LIST
	 */
	public List<SdTargetInfoVO> getSdTargetInfoList(SdCheckListGroupVO sdCheckListGroupVO) throws Exception{
		List<SdTargetInfoVO> list = (List<SdTargetInfoVO>)list("securityDay.getSdTargetInfoList", sdCheckListGroupVO);
		return list;
	}
	
	/**
	 *  점검표 대상 삭제
	 */
	public void sdTargetInfoDelete(SdTargetInfoVO sdTargetInfo) throws Exception{
		delete("securityDay.sdTargetInfoDelete", sdTargetInfo);
		
	}

	/**
	 *  점검표 그룹 LIST
	 */
	public java.util.List<SdCheckListGroupVO> getSdCheckListGroupList(
			SdSearchVO searchVO) {
		List<SdCheckListGroupVO> list = (List<SdCheckListGroupVO>)list("securityDay.getSdCheckListGroupList", searchVO);
		return list;
	}

	/**
	 *  점검표 그룹 LIST 총 갯수
	 */
	public int getSdCheckListGroupTotalCount(SdSearchVO searchVO) {
		return (int)select("securityDay.getSdCheckListGroupTotalCount", searchVO);
	}

	/**
	 *  점검표 그룹 seq
	 */
	public int getClGroupNo() {
		return (int)select("securityDay.getClGroupNo");
	}

	/**
	 *  점검표 그룹 정보 저장
	 */
	public void setSdCheckListGroupInfoInsert(
			SdCheckListGroupVO sdCheckListGroupVO) {
		insert("securityDay.setSdCheckListGroupInfoInsert", sdCheckListGroupVO);
		
	}

	/**
	 *  점검표 그룹 상세정보 조회
	 */
	public SdCheckListGroupVO getSdCheckListGroupInfo(
			SdCheckListGroupVO sdCheckListGroupVO) {
		return (SdCheckListGroupVO) select("securityDay.getSdCheckListGroupInfo", sdCheckListGroupVO);
	}

	/**
	 *  점검표 그룹 정보 삭제
	 */
	public void sdCheckListGroupInfoDelete(SdCheckListGroupVO sdCheckListGroupVO) {
		delete("securityDay.sdCheckListGroupInfoDelete", sdCheckListGroupVO);
		
	}

	/**
	 *  점검표 그룹 정보 수정
	 */
	public void setSdCheckListGroupInfoUpdate(
			SdCheckListGroupVO sdCheckListGroupVO) {
		update("securityDay.setSdCheckListGroupInfoUpdate", sdCheckListGroupVO);
		
	}

	/**
	 *  그룹 대상 점검표 LIST
	 */
	public List<SdTargetInfoVO> getSdClGroupMappingList(
			SdCheckListGroupVO sdCheckListGroupVO) {
		List<SdTargetInfoVO> list = (List<SdTargetInfoVO>)list("securityDay.getSdClGroupMappingList", sdCheckListGroupVO);
		return list;
	}
	/**
	 *  점검표 LIST
	 */
	public List<SdCheckListVO> getClList() {
		List<SdCheckListVO> list = (List<SdCheckListVO>)list("securityDay.getClList");
		return list;
	}

	public int sdClGroupMappinfInfoCheck(SdTargetInfoVO sdTargetInfo) {
		return  (int)select("securityDay.sdClGroupMappinfInfoCheck", sdTargetInfo);
	}

	public void setClGroupInfoInsert(SdTargetInfoVO sdTargetInfo) {
		insert("securityDay.setClGroupInfoInsert", sdTargetInfo);
	}

	public void sdClGroupMappingInfoDelete(SdTargetInfoVO sdTargetInfoVO) {
		delete("securityDay.sdClGroupMappingInfoDelete", sdTargetInfoVO);
		
	}
	/**
	 *  점검 대상 여부 확인
	 * @param empNo 
	 */
	public SdResultInfoVO getSdYn(String empNo) {
		return  (SdResultInfoVO) select("securityDay.getSdYn", empNo);
	}
		
	
	
	/**
	 *  점검 문항 정보 조회
	 * @param sdResultInfoVO 
	 */
	public SdResultInfoVO getSdInfo(SdResultInfoVO sdResultInfoVO) {
		return (SdResultInfoVO) select("securityDay.getSdInfo", sdResultInfoVO);
	}
	
	/**
	 *  점검표 문항 결과 정보 저장
	 * @param sdResultInfoVO 
	 */
	public void setSdResultInfoSave(SdResultInfoVO sdResultInfoVO) {
		insert("securityDay.setSdResultInfoSave", sdResultInfoVO);
	}
	
	
	/**
	 *  점검표  결과 저장
	 * @param sdResultInfoVO 
	 */
	public void setSdResultSave(SdResultInfoVO sdResultInfoVO) {
		insert("securityDay.setSdResultSave", sdResultInfoVO);
		
	}

	public SdResultInfoVO getSdPreView(SdSearchVO searchVO) {
		return (SdResultInfoVO) select("securityDay.getSdPreView", searchVO);
	}

	public void sdResultDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO) {
		delete("securityDay.sdResultDatailInfoDelete", sdTargetInfoVO);
		
	}

	public void sdResultInfoDelete(SdTargetInfoVO sdTargetInfoVO) {
		delete("securityDay.sdResultInfoDelete", sdTargetInfoVO);
		
	}

	public void sdTargetDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO) {
		delete("securityDay.sdTargetDatailInfoDelete", sdTargetInfoVO);
		
	}

	public List<EgovMap> getSdResultInfo(SdSearchVO searchVO) {
		return (List<EgovMap>)list("securityDay.getSdResultInfo", searchVO);
	}

	public List<HashMap<String, Object>> getExcelSdResultInfo(
			SdSearchVO searchVO) {
		return (List<HashMap<String, Object>>)list("securityDay.getExcelSdResultInfo", searchVO);
	}

	public String getFileNm(SdResultInfoVO sdResultInfoVO) {
		return (String)select("securityDay.getFileNm", sdResultInfoVO);
	}

	public int getSdInfoTotalCnt(SdResultInfoVO sdResultInfoVO) {
		return (int)select("securityDay.getSdInfoTotalCnt", sdResultInfoVO);
	}

	public SdResultInfoVO getSdRDetailInfo(SdResultInfoVO sdResultInfo) {
		return (SdResultInfoVO)select("securityDay.getSdRDetailInfo", sdResultInfo);
	}

	public void setSdResultInfoUpdate(SdResultInfoVO sdResultInfoVO) {
		update("securityDay.setSdResultInfoUpdate", sdResultInfoVO);
		
	}

	public int getSdPreViewTotalCnt(SdSearchVO searchVO) {
		return (int)select("securityDay.getSdPreViewTotalCnt", searchVO);
	}

	public int getIdxDayCnt(SdCheckListGroupVO sdCheckListGroupVO) {
		return (int)select("securityDay.getIdxDayCnt", sdCheckListGroupVO);
	}

	public void deleteSdResultDatailInfo(SdResultInfoVO sdResultInfoVO) {
		delete("securityDay.deleteSdResultDatailInfo", sdResultInfoVO);
		
	}

}