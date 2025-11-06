package sdiag.sanct.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface SanctionService {
	public List<EgovMap> getSanctConfigList() throws Exception;
	public EgovMap getSanctConfigInfo(String sanctno) throws Exception;
	public boolean setModifySanctConfigInfo(HashMap<String, Object> params) throws Exception;
	public boolean setSanctAllRegister(HashMap<String, Object> hMap) throws Exception;
	/**
	 * PC지킴이 파라메터 정보 조회
	 * @param pcgactparam
	 * @return
	 * @throws Exception
	 */
	public String getPcgActParamInfo(String pcgactparam) throws Exception;
	/**
	 * 제재 / PC지킴이 조치 처리
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setSanctInfoRegister(HashMap<String, Object> hMap, String mode) throws Exception;
	/**
	 * 소명 정보 Insert
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setApprInfoInsert(HashMap<String, Object> hMap) throws Exception;
	/**
	 * 소명 코드 업데이트
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setApprInfoRegister(HashMap<String, Object> hMap) throws Exception;
	/**
	 * 제재처리를 위한 소명코드 조회 : 소명코드 없을경우 제재 하지 않음...
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public String getApprCodeInfo(HashMap<String, Object> hMap) throws Exception;
	/**
	 * 소명정보 Roll-back
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setApprInfoRollback(HashMap<String, Object> hMap) throws Exception;
}
