package sdiag.main.service;
import java.util.HashMap;
import java.util.List;

import sdiag.man.service.SearchVO;
import sdiag.securityDay.service.SdResultInfoVO;

public interface UserMainService {

	/**
	 *  공지사항 LIST
	 * @param searchVO 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getNoticeList(SearchVO searchVO) throws Exception;
	
	/**
	 *  사용자, 사용자 팀, 사용자 상위 팀 보안 점수 조회
	 * @param isEmp
	 * @return
	 * @throws Exception
	 */
	public UserMainIdxInfoVO getUserIdxInfo(String userid) throws Exception;

	/**
	 *  사용자 정책별 점수
	 * @param isEmp
	 * @param wan 
	 * @param god 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUserPolIdxInfoList(String userid) throws Exception;

	/**
	 *  전사 평균
	 *  @param 
	 * @return
	 * @throws Exception
	 */
	public UserMainIdxInfoVO getTotalAvg() throws Exception;

	/**
	 *  정책 점수
	 *  @param 
	 * @return
	 * @throws Exception
	 */
	public UserPolIdxInfoVO getuserPolIdxInfo(UserPolIdxInfoVO userPolIdxInfo) throws Exception;
	
	
	/**
	 *  FAQ LIST
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getFaqList() throws Exception;

	/**
	 * SecurityDay 점검결과
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<SdResultInfoVO> getSdResultInfo(String empno);


}
