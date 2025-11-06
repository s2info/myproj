package sdiag.man.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import sdiag.man.vo.OrgStatisticSearchVO;
import sdiag.man.vo.StatInfoVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface OrgStatisticService {

	/**
	 * 통계 정보 List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getStatInfoList(OrgStatisticSearchVO searchVO) throws Exception;

	/**
	 * 정책 List
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolList() throws Exception;

	public void statInfoInsert(StatInfoVO statInfoVo) throws Exception;

	public int getStatSeq() throws Exception;

	
	/**
	 * 통계 정책 List
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getStatPolList(OrgStatisticSearchVO searchVO) throws Exception;

	/**
	 * 수동 정책 Cnt
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getUpPolCnt(OrgStatisticSearchVO searchVO) throws Exception;

	

	public void statInfoUpdate(StatInfoVO statInfoVo) throws Exception;

	/**
	 * 통계 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public StatInfoVO getStatInfo(OrgStatisticSearchVO searchVO) throws Exception;

	
	/**
	 * upload 통계 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void setUploadScore(StatInfoVO statInfoVo) throws Exception;

	/**
	 * upload 통계 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void setTotalStatScore(StatInfoVO statInfoVo) throws Exception;

	/**
	 * 통계 정책 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getPolIdxList(StatInfoVO statInfoVo) throws Exception;

	/**
	 * 통계 수동 정책 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getUpPolIdxList(StatInfoVO statInfoVo) throws Exception;

	/**
	 * 통계  정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgStatInfo(StatInfoVO statInfoVo) throws Exception;

	/**
	 * 통계  정보 삭제
	 * @param statInfo
	 * @return
	 * @throws Exception
	 */
	public void orgStatInfoDelete(StatInfoVO statInfo)  throws Exception;

	public void statUploadScoreDelete(HashMap<String, Object> rMap) throws Exception;

	public void statUploadScoreInsert(HashMap<String, Object> rMap) throws Exception;

	public void setStatUploadScoreDelete(StatInfoVO statInfoVo) throws Exception;
}
