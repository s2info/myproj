package sdiag.main.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserMainService;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.board.service.NoticeVO;
import sdiag.man.service.SearchVO;
import sdiag.securityDay.service.SdResultInfoVO;

@Service("UserMainService")
public class UserMainServiceImpl implements UserMainService{
	@Resource(name = "UserMainDAO")
	private UserMainDAO userMainDao;
	
	/**
	 *  공지사항 LIST
	 */
	public HashMap<String,Object> getNoticeList(SearchVO searchVO) throws Exception{
		List<NoticeVO> List = userMainDao.getNoticeList(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	/**
	 *  사용자, 사용자 팀, 사용자 상위 팀 보안 점수 조회
	 */
	public UserMainIdxInfoVO getUserIdxInfo(String userId) throws Exception {
		UserMainIdxInfoVO userIdxInfo = userMainDao.getUserIdxInfo(userId);
		
		return userIdxInfo;
	}

	/**
	 *  사용자 정책별 점수
	 */
	public HashMap<String, Object> getUserPolIdxInfoList(String userId) throws Exception {
		List<UserPolIdxInfoVO> List = userMainDao.getUserPolIdxInfoList(userId);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}

	/**
	 *  전사 평규
	 */
	public UserMainIdxInfoVO getTotalAvg() throws Exception {
		UserMainIdxInfoVO totalAvg = userMainDao.getTotalAvg();
		return totalAvg;
	}
	
	/**
	 *  정책 점수
	 */
	public UserPolIdxInfoVO getuserPolIdxInfo(UserPolIdxInfoVO userPolIdxInfo) throws Exception {
		UserPolIdxInfoVO userPolIdxInfoVO = userMainDao.getUserPolIdxInfo(userPolIdxInfo);
		
		return userPolIdxInfoVO;
	}

	/**
	 *  FAQ LIST
	 */
	public HashMap<String, Object> getFaqList() throws Exception {
		List<NoticeVO> List = userMainDao.getFaqList();
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}

	/**
	 *  SecurityDay 점검결과
	 */
	public List<SdResultInfoVO> getSdResultInfo(String empno) {
		List<SdResultInfoVO> sdResultInfo = userMainDao.getSdResultInfo(empno);
		
		return sdResultInfo;
	}
	
}