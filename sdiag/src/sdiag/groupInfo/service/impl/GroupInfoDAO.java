package sdiag.groupInfo.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.com.service.CodeInfoVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.groupInfo.service.GroupInfoVO;
import sdiag.groupInfo.service.GroupSearchVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.securityDay.service.SdQuestionInfoVO;
import sdiag.securityDay.service.SdSearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("GroupInfoDAO")
public class GroupInfoDAO extends EgovAbstractDAO {

	/**
	 *  점검표 LIST
	 * @param searchVO 
	 */
	public List<GroupInfoVO> getGroupInfoList(GroupSearchVO searchVO) throws Exception{
		List<GroupInfoVO> list = (List<GroupInfoVO>)list("groupInfo.getGroupInfoList", searchVO);
		return list;
	}
	
	/**
	 *  점검표 totalCount
	 * @param searchVO 
	 */
	public int getGroupInfoTotalCount(GroupSearchVO searchVO) throws Exception{
		return (int)select("groupInfo.getGroupInfoTotalCount", searchVO);
	}

	/**
	 *  개인 정보 조회
	 */
	public List<GroupDetailInfoVO> getOrgUserInfoList(GroupSearchVO searchVO) throws Exception{
		List<GroupDetailInfoVO> list = (List<GroupDetailInfoVO>)list("groupInfo.getOrgUserInfoList", searchVO);
		return list;
	}

	/**
	 *  개인 정보 TotalCount
	 */
	public int getOrgUserInfoTotalCount(GroupSearchVO searchVO) throws Exception{
		return (int)select("groupInfo.getOrgUserInfoTotalCount", searchVO);
	}

	/**
	 *  조직 정보 조회
	 */
	public List<GroupDetailInfoVO> getOrgGroupInfoList(
			GroupSearchVO searchVO) throws Exception{
		List<GroupDetailInfoVO> list = (List<GroupDetailInfoVO>)list("groupInfo.getOrgGroupInfoList", searchVO);
		return list;
	}

	/**
	 *  조직 정보 TotalCount
	 */
	public int getOrgGroupInfoTotalCount(GroupSearchVO searchVO) throws Exception{
		return (int)select("groupInfo.getOrgGroupInfoTotalCount", searchVO);
	}

	/**
	 *  그룹 코드 중복 체크
	 */
	public int groupCodeCheck(GroupInfoVO groupInfoVO) throws Exception{
		return (int)select("groupInfo.groupCodeCheck", groupInfoVO);
	}

	/**
	 *  그룹 정보 수정
	 */
	public void setGroupInfoUpdate(GroupInfoVO groupInfoVO) throws Exception{
		update("groupInfo.setGroupInfoUpdate", groupInfoVO);
	}

	/**
	 *  그룹 정보 저장
	 */
	public void setGroupInfoInsert(GroupInfoVO groupInfoVO) throws Exception{
		insert("groupInfo.setGroupInfoInsert", groupInfoVO);
	}

	/**
	 *  그룹 상세 여부 존재 여부 체크
	 */
	public int groupDetailCheck(GroupDetailInfoVO groupDetailInfo) throws Exception{
		return (int)select("groupInfo.groupDetailCheck", groupDetailInfo);
	}

	/**
	 *  그룹 상세 정보 저장
	 */
	public void setGroupDetailInfoInsert(GroupDetailInfoVO groupDetailInfo) throws Exception{
		insert("groupInfo.setGroupDetailInfoInsert", groupDetailInfo);
		
	}

	/**
	 *  그룹 정보
	 */
	public GroupInfoVO getGroupInfo(GroupInfoVO groupInfoVO) throws Exception{
		return (GroupInfoVO) select("groupInfo.getGroupInfo", groupInfoVO);
	}

	/**
	 *  그룹 상세 정보
	 */
	public List<GroupDetailInfoVO> getGroupDetailInfoList(
			GroupInfoVO groupInfoVO)throws Exception{
		List<GroupDetailInfoVO> list = (List<GroupDetailInfoVO>)list("groupInfo.getGroupDetailInfoList", groupInfoVO);
		return list;
	}

	/**
	 *  그룹 정보 삭제
	 */
	public void groupInfoDelete(GroupInfoVO groupInfoVO) throws Exception{
		delete("groupInfo.groupInfoDelete", groupInfoVO);
		
	}

	/**
	 *  그룹 상세 정보 삭제
	 */
	public void groupDetailInfoDelete(GroupDetailInfoVO groupDetailInfoVO) throws Exception{
		delete("groupInfo.groupDetailInfoDelete", groupDetailInfoVO);
		
	}
	
	/**
	 *  팝업 그룹 조직 정보 조회
	 */
	public List<GroupDetailInfoVO> getPopGroupInfoList(
			GroupSearchVO searchVO) throws Exception{
		List<GroupDetailInfoVO> list = (List<GroupDetailInfoVO>)list("groupInfo.getPopGroupInfoList", searchVO);
		return list;
	}

	/**
	 *  팝업 그룹 정보 TotalCount
	 */
	public int getPopGroupInfoTotalCount(GroupSearchVO searchVO) throws Exception{
		return (int)select("groupInfo.getPopGroupInfoTotalCount", searchVO);
	}

	/**
	 *  그룹 코드 가져오기
	 */
	public int getGroupCode() throws Exception{
		return (int)select("groupInfo.getGroupCode");
	}

	/**
	 *  정책 LIST 가져오기
	 */
	public List<CodeInfoVO> getPolIdxList() throws Exception{
		List<CodeInfoVO> list = (List<CodeInfoVO>)list("groupInfo.getPolIdxList");
		return list;
	}

	public void setPassiveGroupInsert(GroupDetailInfoVO groupDetailInfo) {
		insert("groupInfo.setPassiveGroupInsert", groupDetailInfo);
	}

	public void setQueryStrInsert(GroupDetailInfoVO groupDetailInfoVO) {
		insert("groupInfo.setQueryStrInsert", groupDetailInfoVO);
	}

}