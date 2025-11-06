package sdiag.man.service;

import java.util.HashMap;
import java.util.List;

import sdiag.pol.service.PolicySearchVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface SecPolService {
	public HashMap<String,Object> getSecPolList(PolicySearchVO searchVO) throws Exception;
	public  HashMap<String,Object> getPolIdxInfo(String sec_pol_id) throws Exception;
	public int setModifyPolInfo(SecPolInfoVO secPolInfoVO, PolConVO polConInfo ,PolTargetInfoVO polTargetInfoVO) throws Exception;
	/**
	 * 지수정책 로그조회 테이블
	 * @param nspname
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolSourceLogTables(String nspname) throws Exception;
	public List<EgovMap> getPolSourceLogTableColumns(String tname) throws Exception;
	/**
	 * 정책정보 리스트 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSecPolListForExportExcel(PolicySearchVO searchVO) throws Exception;
	
	/**
	 * 정책 대상 삭제
	 * @param polTargetInfoVO
	 * @return
	 * @throws Exception
	 */
	public void polTargetInfoDelete(PolTargetInfoVO polTargetInfoVO) throws Exception;
}
