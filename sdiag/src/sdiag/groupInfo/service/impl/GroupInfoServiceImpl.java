package sdiag.groupInfo.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.com.service.CodeInfoVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoService;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.groupInfo.service.GroupSearchVO;


@Service("GroupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService{
	@Resource(name = "GroupInfoDAO")
	private GroupInfoDAO groupInfoDAO;

	/**
	 *  그룹 LIST
	 */
	public HashMap<String, Object> getGroupInfoList(GroupSearchVO searchVO) throws Exception {
		List<GroupInfoVO> List = groupInfoDAO.getGroupInfoList(searchVO);
		int TotalCount = groupInfoDAO.getGroupInfoTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  개인 정보 조회
	 */
	public HashMap<String, Object> getOrgUserInfoList(GroupSearchVO searchVO)
			throws Exception {
		List<GroupDetailInfoVO> List = groupInfoDAO.getOrgUserInfoList(searchVO);
		int TotalCount = groupInfoDAO.getOrgUserInfoTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  조직 정보 조회
	 * @throws Exception 
	 */
	public HashMap<String, Object> getOrgGroupInfoList(GroupSearchVO searchVO) throws Exception {
		List<GroupDetailInfoVO> List = groupInfoDAO.getOrgGroupInfoList(searchVO);
		int TotalCount = groupInfoDAO.getOrgGroupInfoTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 * 그룹 코드 중복 체크
	 * @throws Exception 
	 */
	public int groupCodeCheck(GroupInfoVO groupInfoVO) throws Exception {
		return  groupInfoDAO.groupCodeCheck(groupInfoVO);
	}

	/**
	 * 그룹 정보 수정
	 * @throws Exception 
	 */
	public void setGroupInfoUpdate(GroupInfoVO groupInfoVO) throws Exception {
		groupInfoDAO.setGroupInfoUpdate(groupInfoVO);
		
	}

	/**
	 * 그룹 정보 저장
	 * @throws Exception 
	 */
	public void setGroupInfoInsert(GroupInfoVO groupInfoVO) throws Exception {
		groupInfoDAO.setGroupInfoInsert(groupInfoVO);
		
	}

	/**
	 * 그룹 상세 정보 저장
	 * @throws Exception 
	 */
	public void setGroupDetailInfoInsert(GroupDetailInfoVO groupDetailInfoVO)
			throws Exception {
		int cnt =0;
		
		String[] orgInfoArr = groupDetailInfoVO.getOrgInfo().split(","); 
		String[] orgTypeArr = groupDetailInfoVO.getOrgType().split(",");
		String[] orgNmArr = groupDetailInfoVO.getOrgNm().split(",");
		
		
		for (int i=0; i<orgInfoArr.length; i++){
			GroupDetailInfoVO groupDetailInfo = new GroupDetailInfoVO();
			groupDetailInfo.setGroupCode(groupDetailInfoVO.getGroupCode());
			groupDetailInfo.setOrgInfo(orgInfoArr[i]);
			groupDetailInfo.setOrgType(orgTypeArr[i]);
			groupDetailInfo.setOrgNm(orgNmArr[i]);
			groupDetailInfo.setRgdtEmpNo(groupDetailInfoVO.getRgdtEmpNo());
			/* 그룹 상세 여부 존재 여부 체크  - 해당 정보가 존재 하지 않으면 저장*/
			cnt = groupInfoDAO.groupDetailCheck(groupDetailInfo);
			
			if(cnt==0)
				groupInfoDAO.setGroupDetailInfoInsert(groupDetailInfo);
			
			cnt=0;
		}
		
	}

	/**
	 * 그룹 정보
	 * @throws Exception 
	 */
	public GroupInfoVO getGroupInfo(GroupInfoVO groupInfoVO) throws Exception {
		return groupInfoDAO.getGroupInfo(groupInfoVO);
	}

	/**
	 * 그룹 상세 정보 
	 * @throws Exception 
	 */
	public List<GroupDetailInfoVO> getGroupDetailInfoList(
			GroupInfoVO groupInfoVO) throws Exception {
		return groupInfoDAO.getGroupDetailInfoList(groupInfoVO);
	}

	/**
	 * 그룹 정보  삭제
	 * @throws Exception 
	 */
	public void groupInfoDelete(GroupInfoVO groupInfoVO) throws Exception {
		groupInfoDAO.groupInfoDelete(groupInfoVO);
		
	}

	/**
	 * 그룹 상세 정보 삭제 
	 * @throws Exception 
	 */
	public void groupDetailInfoDelete(GroupDetailInfoVO groupDetailInfoVO)
			throws Exception {
		groupInfoDAO.groupDetailInfoDelete(groupDetailInfoVO);
		
	}
	
	/**
	 *  팝업 그룹 정보 조회
	 * @throws Exception 
	 */
	public HashMap<String, Object> getPopGroupInfoList(GroupSearchVO searchVO) throws Exception {
		List<GroupDetailInfoVO> List = groupInfoDAO.getPopGroupInfoList(searchVO);
		int TotalCount = groupInfoDAO.getPopGroupInfoTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 *  그룹 코드 가져오기
	 * @throws Exception 
	 */
	public int getGroupCode() throws Exception {
		return groupInfoDAO.getGroupCode();
	}

	/**
	 *  정책 List 가져오기
	 * @throws Exception 
	 */
	public List<CodeInfoVO> getPolIdxList() throws Exception {
		List<CodeInfoVO> list = groupInfoDAO.getPolIdxList();
		return list;
	}

	/**
	 *  수동 입력 저장
	 * @throws Exception 
	 */
	public void setPassiveGroupInsert(GroupDetailInfoVO groupDetailInfoVO) throws Exception {
		String[] empNoList =  groupDetailInfoVO.getEmpNoList().split("\n");
		int cnt =0;
		for(String empNo:empNoList){
			empNo = (empNo.replace("\r", "")).replaceAll(" ", "");;
			if(!empNo.isEmpty() && empNo !="" && empNo !=null){
				//groupDetailInfoVO.setOrgInfo(empNo);
				GroupDetailInfoVO groupDetailInfo = new GroupDetailInfoVO();
				groupDetailInfo.setGroupCode(groupDetailInfoVO.getGroupCode());
				groupDetailInfo.setOrgInfo(empNo);
				groupDetailInfo.setOrgType("1");
				groupDetailInfo.setOrgNm("");
				groupDetailInfo.setRgdtEmpNo(groupDetailInfoVO.getRgdtEmpNo());
				/* 그룹 상세 여부 존재 여부 체크  - 해당 정보가 존재 하지 않으면 저장*/
				cnt = groupInfoDAO.groupDetailCheck(groupDetailInfo);
				
				if(cnt==0)
					groupInfoDAO.setPassiveGroupInsert(groupDetailInfo);
				
				cnt=0;
			}
			
		}
	}

	/**
	 *  수동 쿼리 저장
	 * @throws Exception 
	 */
	public void setQueryStrInsert(GroupDetailInfoVO groupDetailInfoVO)
			throws Exception {
		
			groupInfoDAO.groupDetailInfoDelete(groupDetailInfoVO);
			groupInfoDAO.setQueryStrInsert(groupDetailInfoVO);
			
		
	}

}