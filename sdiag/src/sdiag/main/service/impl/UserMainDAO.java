package sdiag.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.board.service.NoticeVO;
import sdiag.login.service.UserManageVO;
import sdiag.main.service.UserMainIdxInfoVO;
import sdiag.main.service.UserPolIdxInfoVO;
import sdiag.man.service.SearchVO;
import sdiag.securityDay.service.SdResultInfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("UserMainDAO")
public class UserMainDAO extends EgovAbstractDAO {
	
	/**
	 *  공지사항 LIST
	 */
	public List<NoticeVO> getNoticeList(SearchVO searchVO) throws Exception{
		List<NoticeVO> list = (List<NoticeVO>)list("userMain.getNoticeList", searchVO);
		
		return list;
	}

	/**
	 *  사용자, 사용자 팀, 사용자 상위 팀 보안 점수 조회
	 */
	public UserMainIdxInfoVO getUserIdxInfo(String userId) {
		return  (UserMainIdxInfoVO)select("userMain.getUserIdxInfo", userId);
	}

	/**
	 *  사용자 정책 별 점수
	 */
	public List<UserPolIdxInfoVO> getUserPolIdxInfoList(String userId) {
		List<UserPolIdxInfoVO> list = (List<UserPolIdxInfoVO>)list("userMain.getUserPolIdxInfoList",userId);
		return list;
	}

	/**
	 *  전사 평균
	 */
	public UserMainIdxInfoVO getTotalAvg() {
		return (UserMainIdxInfoVO)select("userMain.getTotalAvg");
	}
	
	/**
	 *  정책 점수
	 */
	public UserPolIdxInfoVO getUserPolIdxInfo(UserPolIdxInfoVO userPolIdxInfo){
		return  (UserPolIdxInfoVO)select("userMain.getUserPolIdxInfo", userPolIdxInfo);
	}

	
	/**
	 * FAQ LIST
	 */
	public List<NoticeVO> getFaqList() throws Exception{
		List<NoticeVO> list = (List<NoticeVO>)list("userMain.getFaqList");
		return list;
	}

	/**
	 *  SecurityDay 점검결과
	 */
	public List<SdResultInfoVO> getSdResultInfo(String empno) {
		List<SdResultInfoVO> list = (List<SdResultInfoVO>)list("userMain.getSdResultInfo", empno);
		return list;
	}
}