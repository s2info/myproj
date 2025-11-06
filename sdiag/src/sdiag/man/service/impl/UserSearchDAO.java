package sdiag.man.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.UserSearchInfoVO;
import sdiag.man.service.UserinfoVO;
import sdiag.man.service.SearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("UserSearchDAO")
public class UserSearchDAO extends EgovAbstractDAO{
	
	/**
	 * 사용자 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserList(SearchVO searchVO) {
		return (List<UserSearchInfoVO>)list("userSearch.getUserList",searchVO);
	}

	/**
	 * 사용자 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserPolList(SearchVO searchVO) {
		return (List<UserSearchInfoVO>)list("userSearch.getUserPolList",searchVO);
	}

	/**
	 * 사용자 예외 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserExPolList(SearchVO searchVO) {
		return (List<UserSearchInfoVO>)list("userSearch.getUserExPolList",searchVO);
	}

	/**
	 * 사용자 그룹 조회
	 * @param pMap
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserGroupList(Map<String, Object> pMap) {
		return (List<UserSearchInfoVO>)list("userSearch.getUserGroupList",pMap);
	}

	/**
	 * 사용자 상위 그룹 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public EgovMap getUserGroup(SearchVO searchVO) {
		return (EgovMap)select("userSearch.getUserGroup",searchVO);
	}

	/**
	 * 사용자 정책 예외 정보 저장
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExInsert(UserSearchInfoVO userSeachInfoVO) {
		insert("userSearch.setPolExInsert",userSeachInfoVO);
	}

	/**
	 * 사용자 정책 예외 정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExDelete(UserSearchInfoVO userSeachInfoVO) {
		delete("userSearch.setPolExDelete",userSeachInfoVO);
	}

	/**
	 * 사용자 그룹정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupDelete(UserSearchInfoVO userSeachInfoVO) {
		delete("userSearch.setGroupDelete",userSeachInfoVO);
	}
}
