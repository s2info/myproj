package sdiag.man.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.ReassignInfoVO;
import sdiag.man.service.ReassignSearchVO;
import sdiag.man.service.UserSearchInfoVO;
import sdiag.man.service.UserinfoVO;
import sdiag.man.service.SearchVO;
import sdiag.securityDay.service.SdCheckListVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("ReassignDAO")
public class ReassignDAO extends EgovAbstractDAO{

	//담당자 조회
	public List<EgovMap> getSearchUserList(HashMap<String, String> hSearch) throws Exception{
		return  (List<EgovMap>)list("reassign.getSearchUserList",hSearch);
	}

	//협력사 리스트 조회
	public List<ReassignInfoVO> getKpSerachList(ReassignSearchVO searchVO) throws Exception{
		return (List<ReassignInfoVO>)list("reassign.getKpSerachList",searchVO);
	}

	//협력사별 담담자 재지정
	public void setProxyEmpNoSave(ReassignInfoVO reassignInfo) throws Exception{
		update("reassign.setProxyEmpNoSave", reassignInfo);
	}

	//협력사별 담담자 초기화
	public void setProxyEmpNoDelete(ReassignInfoVO reassignInfo) throws Exception{
		update("reassign.setProxyEmpNoDelete", reassignInfo);
	}

	//유형별 담당자 재 지정 정보 조회
	public ReassignInfoVO getReassignInfo(ReassignInfoVO reassignInfoVO) throws Exception{
		return (ReassignInfoVO)select("reassign.getReassignInfo",reassignInfoVO);
	}

	//유형별 담당자 재 지정 정보 저장
	public void setTypeReassignInsert(ReassignInfoVO reassignInfoVO) throws Exception{
		insert("reassign.setTypeReassignInsert", reassignInfoVO);
	}

	//유형별 담당자 재 지정 정보 조회
	public List<ReassignInfoVO> getTypeReassignList(
			ReassignSearchVO searchVO) throws Exception{
		List<ReassignInfoVO> list = (List<ReassignInfoVO>)list("reassign.getTypeReassignList", searchVO);
		return list;
	}
	//유형별 담당자 재 지정 정보 리스트 cnt 
	public int getTypeReassignListTotalCount(ReassignSearchVO searchVO) throws Exception{
		return (int)select("reassign.getTypeReassignListTotalCount",searchVO);
	}

	//유형별 담당자 재 지정 정보 cnt 
	public int getTypeReassignInfoCount(ReassignInfoVO reassignInfoVO) throws Exception{
		return  (int)select("reassign.getTypeReassignInfoCount",reassignInfoVO);
	}

	//유형별 담당자 재 지정 정보 수정
	public void setTypeReassignUpdate(ReassignInfoVO reassignInfoVO) throws Exception{
		update("reassign.setTypeReassignUpdate", reassignInfoVO);
	}

	//유형별 담당자 재 지정 삭제
	public void setTypeReassignDelete(ReassignInfoVO reassignInfo) throws Exception{
		delete("reassign.setTypeReassignDelete", reassignInfo);
	}
}
