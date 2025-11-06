package sdiag.man.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;


public interface UserSearchService {
	/**
	 * 사용자 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserList(SearchVO searchVO) throws Exception;

	/**
	 * 사용자 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserPolList(SearchVO searchVO)throws Exception;

	/**
	 * 사용자 예외 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserExPolList(SearchVO searchVO)throws Exception;

	/**
	 * 사용자 그룹 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserGroupList(Map<String, Object> pMap) throws Exception;

	/**
	 * 사용자 상위 그룹 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public EgovMap getUserGroup(SearchVO searchVO) throws Exception;

	/**
	 * 사용자 정책 예외 정보 저장
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExInsert(UserSearchInfoVO userSeachInfoVO);

	/**
	 * 사용자 정책 예외 정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExDelete(UserSearchInfoVO userSeachInfoVO);

	/**
	 * 사용자 그룹정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupDelete(UserSearchInfoVO userSeachInfoVO);

	
}
