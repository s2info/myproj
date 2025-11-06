package sdiag.securityDay.service;
import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.man.service.SearchVO;

public interface SecurityDayService {

	/**
	 *  점검표 LIST
	 * @param searchVO 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getSdCheckList(SdSearchVO searchVO) throws Exception;

	/**
	 *  점검표 seq 가져오기
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getSdCheckNo() throws Exception;
	
	/**
	 *  문항번호 가져오기
	 * @param sdCheckNo 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getQuestionNum(int sdCheckNo) throws Exception;

	
	/**
	 * 점검표 정보 저장
	 * @param  sdCheckListVO
	 * @param sdTargetInfoVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdCheckListInfoInsert(SdCheckListVO sdCheckListVO, SdTargetInfoVO sdTargetInfoVO) throws Exception;
	
	
	/**
	 *  문항 정보 저장
	 * @param  sdQuestionInfoVO
	 * @param sdQuestionMcVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdQuestionInfoInsert(SdQuestionInfoVO sdQuestionInfoVO, SdQuestionMcVO sdQuestionMcVO) throws Exception;

	
	/**
	 *  점검표 상세 정보
	 * @param  sdCheckListVO
	 * @return
	 * @throws Exception
	 */
	public SdCheckListVO getSdCheckListInfo(SdCheckListVO sdCheckListVO) throws Exception;

	/**
	 *  문항 리스트
	 * @param  sdQuestionInfoVO
	 * @return
	 * @throws Exception
	 */
	public List<SdQuestionInfoVO> getQustionList(SdCheckListVO sdCheckListVO) throws Exception;

	/**
	 *  문항 상세정보
	 * @param  sdQuestionInfoVO
	 * @return
	 * @throws Exception
	 */
	public SdQuestionInfoVO getQuestionInfo(SdQuestionInfoVO sdQuestionInfoVO) throws Exception;

	/**
	 *  문항 상세정보 수정
	 * @param  sdQuestionInfoVO
	 * @param sdQuestionMcVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdQuestionInfoUpdate(SdQuestionInfoVO sdQuestionInfoVO, SdQuestionMcVO sdQuestionMcVO) throws Exception;

	/**
	 *  문항 상세정보 삭제
	 * @param  sdQuestionInfoVO
	 * @return
	 * @throws Exception
	 */
	public void sdQuestionInfoDelete(SdQuestionInfoVO sdQuestionInfoVO) throws Exception;

	/**
	 * 점검표 정보 수정
	 * @param  sdCheckListVO
	 * @param sdTargetInfoVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdCheckListInfoUpdate(SdCheckListVO sdCheckListVO, SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * 점검표 정보 삭제
	 * @param  sdCheckListVO
	 * @return
	 * @throws Exception
	 */
	public void sdCheckListInfoDelete(SdCheckListVO sdCheckListVO) throws Exception;
	
	/**
	 * 점검표 문항 보기 정보 삭제
	 * @param  sdQuestionMcVO
	 * @return
	 * @throws Exception
	 */
	public void questionMcDelete(SdQuestionMcVO sdQuestionMcVO) throws Exception;

	/**
	 * 점검표 문항 보기 정보 LIST
	 * @param  sdQuestionMcVO
	 * @return
	 * @throws Exception
	 */
	public List<SdQuestionMcVO> getQuestionMcList(
			SdQuestionInfoVO sdQuestionInfoVO) throws Exception;

	
	/**
	 * 점검표 대상 LIST
	 * @param  sdQuestionMcVO
	 * @return
	 * @throws Exception
	 */
	public List<SdTargetInfoVO> getSdTargetList(SdCheckListGroupVO sdCheckListGroupVO) throws Exception;

	/**
	 * 점검표 대상 삭제
	 * @param  sdTargetInfoVO
	 * @return
	 * @throws Exception
	 */
	public void sdTargetInfoDelete(SdTargetInfoVO sdTargetInfoVO) throws Exception;
	
	/**
	 *  점검표 그룹 LIST
	 * @param searchVO 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getSdCheckListGroupList(SdSearchVO searchVO) throws Exception;

	/**
	 *  점검표 그룹 seq 가져오기
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getClGroupNo() throws Exception;
	
	/**
	 * 점검표 그룹 정보 저장
	 * @param  sdCheckListVO
	 * @param sdTargetInfoVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdCheckListInfoGroupInsert(SdCheckListGroupVO sdCheckListGroupVO, SdTargetInfoVO sdTargetInfoVO) throws Exception;
	
	/**
	 *  점검표 그룹 상세 정보
	 * @param  sdCheckListVO
	 * @return
	 * @throws Exception
	 */
	public SdCheckListGroupVO getSdCheckListGroupInfo(SdCheckListGroupVO sdCheckListGroupVO) throws Exception;
	
	/**
	 * 점검표 그룹 정보 삭제
	 * @param  sdCheckListVO
	 * @return
	 * @throws Exception
	 */
	public void sdCheckListGroupInfoDelete(SdCheckListGroupVO sdCheckListGroupVO) throws Exception;
	/**
	 * 점검표 그룹 정보 수정
	 * @param  sdCheckListVO
	 * @param sdTargetInfoVO 
	 * @return
	 * @throws Exception
	 */
	public void setSdCheckListGroupInfoUpdate(SdCheckListGroupVO sdCheckListGroupVO, SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * 점검표 그룹 정보 수정
	 * @param  sdCheckListVO
	 * @param sdTargetInfoVO 
	 * @return
	 * @throws Exception
	 */
	public List<SdTargetInfoVO> getSdClGroupMappingList(SdCheckListGroupVO sdCheckListGroupVO) throws Exception;

	
	/**
	 * 점검표 list
	 * @return
	 * @throws Exception
	 */
	public List<SdCheckListVO> getClList() throws Exception;

	/**
	 * 그룹 대상 점검표 정보 삭제
	 * @return
	 * @throws Exception
	 */
	public void sdclGroupMappingInfoDelete(SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * security 점검 대상 확인
	 * @return
	 * @throws Exception
	 */
	public SdResultInfoVO getSdYn(String empNo)throws Exception;

	/**
	 * security 점검 문항 조회
	 * @return
	 * @throws Exception
	 */
	public SdResultInfoVO getSdInfo(SdResultInfoVO sdResultInfoVO)throws Exception;
	
	
	/**
	 * security 점검표 문항 점검 결과 정보 저장
	 * @return
	 * @throws Exception
	 */
	public void setSdResultInfoSave(SdResultInfoVO sdResultInfoVO) throws Exception;
	
	/**
	 * security 점검 결과 저장
	 * @return
	 * @throws Exception
	 */
	public void setSdResultSave(SdResultInfoVO sdResultInfoVO) throws Exception;

	public SdResultInfoVO getSdPreView(SdSearchVO searchVO);
	
	/**
	 * security 점검 상세 대상 삭제
	 * @return
	 * @throws Exception
	 */

	public void sdTargetDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * security 점검 결과 삭제
	 * @return
	 * @throws Exception
	 */
	public void sdResultInfoDelete(SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * security 점검 상세 결과 삭제
	 * @return
	 * @throws Exception
	 */
	public void sdResultDatailInfoDelete(SdTargetInfoVO sdTargetInfoVO) throws Exception;

	/**
	 * security 점검 결과 상세 조회
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getSdResultInfo(SdSearchVO searchVO) throws Exception;
	
	/**
	 * security 점검 결과 상세 조회 엑셀 다운로드
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getExcelSdResultInfo(SdSearchVO searchVO) throws Exception;

	public String getFileNm(SdResultInfoVO sdResultInfoVO);

	public int getSdInfoTotalCnt(SdResultInfoVO sdResultInfoVO);

	public SdResultInfoVO getSdRDetailInfo(SdResultInfoVO sdResultInfo);

	public void setSdResultInfoUpdate(SdResultInfoVO sdResultInfoVO);

	public int getSdPreViewTotalCnt(SdSearchVO searchVO);

	/**
	 * 지수화 기간 중복 체크
	 * @return
	 * @throws Exception
	 */
	public int getIdxDayCnt(SdCheckListGroupVO sdCheckListGroupVO);

	/**
	 * 점검 상세 정보 삭제
	 * @return
	 * @throws Exception
	 */
	public void deleteSdResultDatailInfo(SdResultInfoVO sdResultInfoVO);

}
