package sdiag.sanct.service.impl;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("SanctionDAO")
public class SanctionDAO extends EgovAbstractDAO {
	
	public List<EgovMap> getSanctConfigList() throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("sanct.getSanctConfigList");
		return list;
	}
	
	public EgovMap getSanctConfigInfo(String sanctno) throws Exception{
		EgovMap list = (EgovMap)select("sanct.getSanctConfigInfo", sanctno);
		return list;
	}
	
	public int setDeleteSanctConfig(HashMap<String, Object> params) throws Exception{
		return delete("sanct.setDeleteSanctConfig", params);
	}
	
	public Object setInsertSanctConfig(HashMap<String, Object> params) throws Exception{
		return insert("sanct.setInsertSanctConfig", params);
	}
	
	/*제재신청 - 사용안함*/
	public int setDeleteSanctAllList(HashMap<String, Object> hMap) throws Exception{
		return delete("sanct.setDeleteSanctAllList", hMap);
	}
	
	public int setUpdatePolSanctAllList(HashMap<String, Object> hMap) throws Exception{
		return update("sanct.setUpdatePolSanctAllList", hMap);
	}
	
	public Object setInsertSanctInfoAllList(HashMap<String, Object> hMap) throws Exception{
		return insert("sanct.setInsertSanctInfoAllList", hMap);
	}
	/*제재신청끝*/
	
	/*제재처리*/
	/**
	 * 이전 제재 삭제(존재할경우)
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public int setDeleteSanctInfo(HashMap<String, Object> hMap) throws Exception{
		return delete("sanct.setDeleteSanctInfo", hMap);
	}
	/**
	 * 제재 / PC지킴이 저장
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateUserIdxInfoScancId(HashMap<String, Object> hMap) throws Exception{
		return update("sanct.setUpdateUserIdxInfoScancId", hMap);
	}
	/**
	 * User_idx_info 업데이트
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public Object setSanctInfoInsert(HashMap<String, Object> hMap) throws Exception{
		return insert("sanct.setSanctInfoInsert", hMap);
	}
	
	/**
	 * 소명 코드 업데이트- 소명코드 저장
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateUserIdxInfoApprId(HashMap<String, Object> hMap) throws Exception{
		return update("sanct.setUpdateUserIdxInfoApprId", hMap);
	}
	/**
	 * 소명정보 Insert
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public Object setApprInfoInsert(HashMap<String, Object> hMap) throws Exception{
		return insert("sanct.setApprInfoInsert", hMap);
	}
	
	/**
	 * 소명 코드 업데이트 - 소명코드 제거
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public int setUpdateUserIdxInfoApprIdDelete(HashMap<String, Object> hMap) throws Exception{
		return update("sanct.setUpdateUserIdxInfoApprIdDelete", hMap);
	}
	/**
	 * 소명정보 Delete
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public int setApprInfoDelete(HashMap<String, Object> hMap) throws Exception{
		return delete("sanct.setApprInfoDelete", hMap);
	}
	
	/**
	 * PC지킴이 파라메터 정보 조회
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public String getPcgActParamInfo(String pcgactparam) throws Exception{
		return (String)select("sanct.getPcgActParamInfo", pcgactparam);
	}
	/**
	 * 제재처리를 위한 소명코드 조회 : 소명코드 없을경우 제재 하지 않음...
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public String getApprCodeInfo(HashMap<String, Object> hMap) throws Exception{
		return (String)select("sanct.getApprCodeInfo", hMap);
	}
}
