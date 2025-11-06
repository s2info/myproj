package sdiag.getdata.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface GetDataService {
	/**
	 * 정책별 상세로그 조회
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public List<LinkedHashMap<String,Object>> getPolDetailLogForDateNUser(HashMap hMap) throws Exception;
}
