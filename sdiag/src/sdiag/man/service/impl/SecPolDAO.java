package sdiag.man.service.impl;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.pol.service.PolicySearchVO;
import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.man.service.PolConVO;
import sdiag.man.service.PolTargetInfoVO;
import sdiag.man.service.SecPolInfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("SecPolDAO")
public class SecPolDAO extends EgovAbstractDAO{
	
	public List<SecPolInfoVO> getSecPolList(PolicySearchVO searchVO) throws Exception{
		List<SecPolInfoVO> list = (List<SecPolInfoVO>)list("pol.service.getSecPolList", searchVO);
		return list;
	}
	
	public int getSecPolListTotalCount(PolicySearchVO searchVO) throws Exception{
		return (int)select("pol.service.getSecPolListTotalCount", searchVO);
	}
	
	public SecPolInfoVO getPolIdxInfo(String sec_pol_id) throws Exception{
		SecPolInfoVO info = (SecPolInfoVO)select("pol.man.getPolIdxInfo", sec_pol_id);
		return info;
	}
	
	public PolConVO getPolConInfo(String sec_pol_id) throws Exception{
		PolConVO info = (PolConVO)select("pol.man.getPolConInfo", sec_pol_id);
		return info;
	}
	
	public int setUpdatePolInfo(SecPolInfoVO secPolInfoVO) throws Exception{
		return update("pol.man.setUpdatePolInfo", secPolInfoVO);
	}
	
	public Object setInsertPolInfo(SecPolInfoVO secPolInfoVO) throws Exception{
		return insert("pol.man.setInsertPolInfo", secPolInfoVO);
	}
	
	public List<EgovMap> getPolSourceLogTables(String nspname) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("pol.man.getPolSourceLogTables", nspname);
		return list;
	}
	
	public List<EgovMap> getPolSourceLogTableColumns(String tname) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("pol.man.getPolSourceLogTableColumns", tname);
		return list;
	}	
	
	public int setUpdatePolConInfo(PolConVO polConVO) throws Exception{
		return update("pol.man.setUpdatePolConInfo", polConVO);
	}
	
	public Object setInsertPolConInfo(PolConVO polConVO) throws Exception{
		return insert("pol.man.setInsertPolConInfo", polConVO);
	}
	/**
	 * 정책정보 리스트 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSecPolListForExportExcel(PolicySearchVO searchVO) throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("pol.service.getSecPolListForExportExcel", searchVO);
		return list;
	}

	/**
	 *  정책 대상 정보 존재 여부 체크
	 */
	public int polTargetInfoCheck(PolTargetInfoVO polTargetInfo) throws Exception{
		return (int)select("pol.man.polTargetInfoCheck", polTargetInfo);
	}
	/**
	 *  정책 대상 정보 저장
	 */
	public void setPolTargetInfoInsert(PolTargetInfoVO polTargetInfo) throws Exception{
		insert("pol.man.setPolTargetInfoInsert", polTargetInfo);
		
	}

	public List<PolTargetInfoVO> getPolTargetList(String sec_pol_id) {
		return (List<PolTargetInfoVO>)list("pol.man.getPolTargetList", sec_pol_id);
	}

	public void polTargetInfoDelete(PolTargetInfoVO polTargetInfoVO) throws Exception{
		delete("pol.man.polTargetInfoDelete", polTargetInfoVO);
		
	}
}