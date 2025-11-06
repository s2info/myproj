package sdiag.man.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.man.service.ReassignInfoVO;
import sdiag.man.service.ReassignSearchVO;
import sdiag.man.service.ReassignService;
import sdiag.man.service.UserinfoVO;
import sdiag.securityDay.service.SdCheckListVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("ReassignService")
public class ReassignServiceImpl implements ReassignService{

	@Resource(name = "ReassignDAO")
	private ReassignDAO reassignDAO;

	//담당자 조회
	public List<EgovMap> getSearchUserList(HashMap<String, String> hSearch) throws Exception{
		return reassignDAO.getSearchUserList(hSearch);
	}

	//협력사 리스트 조회
	public List<ReassignInfoVO> getKpSerachList(ReassignSearchVO searchVO) throws Exception{
		return reassignDAO.getKpSerachList(searchVO);
	}

	//협력사별 담담자 재지정
	public void setProxyEmpNoSave(ReassignInfoVO reassignInfo) throws Exception {
		reassignDAO.setProxyEmpNoSave(reassignInfo);
	}

	//협력사별 담담자 초기화
	public void setProxyEmpNoDelete(ReassignInfoVO reassignInfo) throws Exception{
		reassignDAO.setProxyEmpNoDelete(reassignInfo);
	}

	// 유형별 담당자 정보 조회
	public ReassignInfoVO getReassignInfo(ReassignInfoVO reassignInfoVO)
			throws Exception {
		return reassignDAO.getReassignInfo(reassignInfoVO);
	}

	// 유형별 담당자 정보  저장
	public void setTypeReassignInsert(ReassignInfoVO reassignInfoVO)
			throws Exception {
		reassignDAO.setTypeReassignInsert(reassignInfoVO);
	}

	// 유형별 담당자 정보  조회
	public HashMap<String, Object> getTypeReassignList(ReassignSearchVO searchVO)
			throws Exception {
		List<ReassignInfoVO> List = reassignDAO.getTypeReassignList(searchVO);
		int TotalCount = reassignDAO.getTypeReassignListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	// 유형별 담당자 정보 count 조회
	public int getTypeReassignInfoCount(ReassignInfoVO reassignInfoVO)
			throws Exception {
		return reassignDAO.getTypeReassignInfoCount(reassignInfoVO);
	}

	// 유형별 담당자 정보 수정
	public void setTypeReassignUpdate(ReassignInfoVO reassignInfoVO)
			throws Exception {
		reassignDAO.setTypeReassignUpdate(reassignInfoVO);
	}

	// 유형별 담당자 정보  tkrwp
	public void setTypeReassignDelete(ReassignInfoVO reassignInfo)
			throws Exception {
		reassignDAO.setTypeReassignDelete(reassignInfo);
	}
	
	
}
