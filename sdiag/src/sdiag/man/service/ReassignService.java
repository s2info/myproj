package sdiag.man.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ReassignService  {

	//담당자 검색
	public List<EgovMap> getSearchUserList(HashMap<String, String> hSearch) throws Exception;

	//협력사 리스트 조회
	public List<ReassignInfoVO> getKpSerachList(ReassignSearchVO searchVO) throws Exception;

	//협력사별 담담자 재지정
	public void setProxyEmpNoSave(ReassignInfoVO reassignInfo) throws Exception;

	//협력사별 담담자 초기화
	public void setProxyEmpNoDelete(ReassignInfoVO reassignInfo) throws Exception;

	// 유형별 담당자 재지정 정보 조회
	public ReassignInfoVO getReassignInfo(ReassignInfoVO reassignInfoVO)throws Exception;

	// 유형별 담당자 재지정 정보 저장
	public void setTypeReassignInsert(ReassignInfoVO reassignInfoVO) throws Exception;
	
	// 유형별 담당자 재지정 정보 조회
	public HashMap<String, Object> getTypeReassignList(ReassignSearchVO searchVO) throws Exception;

	// 유형별 담당자 재지정 정보 count 조회
	public int getTypeReassignInfoCount(ReassignInfoVO reassignInfoVO) throws Exception;

	// 유형별 담당자 재지정 정보 수정
	public void setTypeReassignUpdate(ReassignInfoVO reassignInfoVO) throws Exception;

	// 유형별 담당자 재지정 삭제
	public void setTypeReassignDelete(ReassignInfoVO reassignInfo) throws Exception;

}
