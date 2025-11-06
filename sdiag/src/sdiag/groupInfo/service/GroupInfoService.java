package sdiag.groupInfo.service;
import java.util.HashMap;
import java.util.List;

import sdiag.com.service.CodeInfoVO;


public interface GroupInfoService {

	/**
	 *  점검표 LIST
	 * @param searchVO 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getGroupInfoList(GroupSearchVO searchVO) throws Exception;

	/**
	 * 개인 정보 조회
	 * @param  searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getOrgUserInfoList(GroupSearchVO searchVO) throws Exception;
	
	/**
	 * 조직 정보 조회
	 * @param  searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getOrgGroupInfoList(GroupSearchVO searchVO) throws Exception;

	/**
	 * 그룹 코드 중복 체크
	 * @param  groupInfoVO
	 * @return
	 * @throws Exception
	 */
	public int groupCodeCheck(GroupInfoVO groupInfoVO)  throws Exception;

	/**
	 * 그룹 정보 수정
	 * @param  groupInfoVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupInfoUpdate(GroupInfoVO groupInfoVO) throws Exception;

	/**
	 * 그룹 정보 저장
	 * @param  groupInfoVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupInfoInsert(GroupInfoVO groupInfoVO) throws Exception;

	/**
	 * 그룹 상세 정보 저장
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupDetailInfoInsert(GroupDetailInfoVO groupDetailInfoVO) throws Exception;

	/**
	 * 그룹  정보  
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public GroupInfoVO getGroupInfo(GroupInfoVO groupInfoVO) throws Exception;

	/**
	 * 그룹 상세 정보
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public List<GroupDetailInfoVO> getGroupDetailInfoList(GroupInfoVO groupInfoVO) throws Exception;

	/**
	 * 그룹 상세 정보 삭제
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public void groupInfoDelete(GroupInfoVO groupInfoVO) throws Exception; 
	
	/**
	 * 그룹 정보 삭제
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public void groupDetailInfoDelete(GroupDetailInfoVO groupDetailInfoVO) throws Exception;

	/**
	 * 팝업 그룹 정보 조회
	 * @param  groupDetailInfoVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPopGroupInfoList(GroupSearchVO searchVO) throws Exception;
	/**
	 * 그룹 코드 가져오기
	 * @param  groupInfoVO
	 * @return
	 * @throws Exception
	 */
	public int getGroupCode() throws Exception;

	/**
	 * 그룹 코드 가져오기
	 * @param  
	 * @return
	 * @throws Exception
	 */
	public List<CodeInfoVO> getPolIdxList() throws Exception;

	/**
	 * 수동그룹 insert
	 * @param  
	 * @return
	 * @throws Exception
	 */
	public void setPassiveGroupInsert(GroupDetailInfoVO groupDetailInfoVO) throws Exception;

	/**
	 * 수동 쿼리
	 * @param  
	 * @return
	 * @throws Exception
	 */
	public void setQueryStrInsert(GroupDetailInfoVO groupDetailInfoVO) throws Exception;
}
