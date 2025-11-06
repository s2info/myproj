package sdiag.man.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.man.service.SearchVO;
import sdiag.man.service.UserSearchService;
import sdiag.man.service.UserSearchInfoVO;
import sdiag.man.service.UserinfoVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("UserSearchService")
public class UserSearchServiceImpl implements UserSearchService{

	@Resource(name = "UserSearchDAO")
	private UserSearchDAO userSearchDao;
	
	
	/**
	 * 사용자 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserList(SearchVO searchVO)
			throws Exception {
		return userSearchDao.getUserList(searchVO);
	}

	/**
	 * 사용자 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserPolList(SearchVO searchVO)
			throws Exception {
		return userSearchDao.getUserPolList(searchVO);
	}

	/**
	 * 사용자 예외 정책 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserExPolList(SearchVO searchVO)
			throws Exception {
		return userSearchDao.getUserExPolList(searchVO);
	}

	/**
	 * 사용자 그룹 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserSearchInfoVO> getUserGroupList(Map<String, Object> pMap)
			throws Exception {
		return userSearchDao.getUserGroupList(pMap);
	}

	/**
	 * 사용자 상위 그룹 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public EgovMap getUserGroup(SearchVO searchVO) throws Exception {
		return userSearchDao.getUserGroup(searchVO);
	}

	/**
	 * 사용자 정책 예외 정보 저장
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExInsert(UserSearchInfoVO userSeachInfoVO) {
		userSearchDao.setPolExInsert(userSeachInfoVO);
	}

	/**
	 * 사용자 정책 예외 정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setPolExDelete(UserSearchInfoVO userSeachInfoVO) {
		userSearchDao.setPolExDelete(userSeachInfoVO);
	}

	/**
	 * 사용자 그룹정보 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void setGroupDelete(UserSearchInfoVO userSeachInfoVO) {
		userSearchDao.setGroupDelete(userSeachInfoVO);
	}
}
