package sdiag.man.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.man.vo.OrgStatisticSearchVO;
import sdiag.man.vo.StatInfoVO;
import sdiag.pol.service.PolicySearchVO;
import sdiag.stat.service.StatisticResultVO;
import sdiag.stat.service.StatisticSearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("OrgStatisticDAO")
public class OrgStatisticDAO extends EgovAbstractDAO{

	/**
	 * 통계 정보 List count
	 * @param searchVO
	 * @return int
	 * @throws Exception
	 */
	public int getStatInfoListTotalCount(OrgStatisticSearchVO searchVO)  throws Exception {
		return (int) select ("orgStat.getStatInfoListTotalCount", searchVO);
	}

	/**
	 * 통계 정보 List
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public List<EgovMap> getStatInfoList(
			OrgStatisticSearchVO searchVO)  throws Exception {
		return (List<EgovMap>)list("orgStat.getStatInfoList",searchVO);
	}

	/**
	 * 정책 List
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public List<EgovMap> getPolList()  throws Exception {
		return  (List<EgovMap>)list("orgStat.getPolList");
	}

	/**
	 * 통계 seq
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public int getStatSeq() throws Exception {
		return (int) select ("orgStat.getStatSeq");
	}
	
	/**
	 * 통계 정보 insert
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public void statInfoInsert(StatInfoVO statInfoVo) throws Exception {
		insert("orgStat.statInfoInsert",statInfoVo);
	}

	/**
	 * 통계 정책 정보 insert
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public void statPolInfoInsert(StatInfoVO statInfoVo)throws Exception {
		insert("orgStat.statPolInfoInsert",statInfoVo);
	}
	
	
	/**
	 * 통계 정책 수동 점수 정보 insert
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public void statUploadScoreInsert(HashMap<String, Object> rMap) throws Exception {
		insert("orgStat.statUploadScoreInsert",rMap);
	}

	/**
	 * 통계 정책 List
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public List<StatInfoVO> getStatPolList(OrgStatisticSearchVO searchVO) {
		return (List<StatInfoVO>)list("orgStat.getStatPolList", searchVO);
	}

	/**
	 * 수동 정책 cnt
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public int getUpPolCnt(OrgStatisticSearchVO searchVO) {
		return  (int) select ("orgStat.getUpPolCnt", searchVO);
	}

	/**
	 * 정책 정보 delete
	 * @param searchVO
	 * @return List
	 * @throws Exception
	 */
	public void statPolInfoDelete(StatInfoVO statInfoVo) {
		delete("orgStat.statPolInfoDelete", statInfoVo);
	}

	public void statInfoUpdate(StatInfoVO statInfoVo) {
		update("orgStat.statInfoUpdate", statInfoVo);
	}

	public void statUploadScoreDelete(HashMap<String, Object> rMap) {
		delete("orgStat.statUploadScoreDelete", rMap);
	}

	/**
	 * 통계 정보
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public StatInfoVO getStatInfo(OrgStatisticSearchVO searchVO) throws Exception {
		return  (StatInfoVO) select ("orgStat.getStatInfo", searchVO);
	}

	public void setUploadScore(StatInfoVO statInfoVo) {
		insert("orgStat.setUploadScore",statInfoVo);
	}

	/**
	 * 통계 정책 정보 List
	 * @param statInfoVo
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getPolIdxList(StatInfoVO statInfoVo) {
		return  (List<StatInfoVO>)list("orgStat.getPolIdxList", statInfoVo);
	}

	/**
	 * 통계 수동 정책 정보 List
	 * @param statInfoVo
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getUpPolIdxList(StatInfoVO statInfoVo) {
		return  (List<StatInfoVO>)list("orgStat.getUpPolIdxList", statInfoVo);
	}

	/**
	 * 통계 토탈 정보
	 * @param statInfoVo
	 * @return
	 * @throws Exception
	 */
	public void setTotalStatScore(StatInfoVO statInfoVo) {
		insert("orgStat.setTotalStatScore",statInfoVo);
	}

	public void statPolSumInfoDelete(StatInfoVO statInfoVo) {
		delete("orgStat.statPolSumInfoDelete", statInfoVo);
		
	}

	public void setTotalStatScoreDelete(StatInfoVO statInfoVo) {
		delete("orgStat.setTotalStatScoreDelete", statInfoVo);
		
	}

	/**
	 * 통계  정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgStatInfo(StatInfoVO statInfoVo) {
		return (List<EgovMap>)list("orgStat.getOrgStatInfo", statInfoVo);
	}

	/**
	 * 통계  정보 삭제
	 * @param statInfo
	 * @return
	 * @throws Exception
	 */
	public void orgStatInfoDelete(StatInfoVO statInfo) {
		delete("orgStat.orgStatInfoDelete", statInfo);
		
	}

	public void statPolSumDelete(StatInfoVO statInfoVo) {
		delete("orgStat.statPolSumDelete", statInfoVo);
	}

	public void setStatUploadScoreDelete(StatInfoVO statInfoVo) {
		delete("orgStat.setStatUploadScoreDelete", statInfoVo);
	}


}
