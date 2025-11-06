package sdiag.sanct.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.sanct.service.SanctionService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("SanctionService")
public class SanctionServiceImpl implements SanctionService{
	@Resource(name = "SanctionDAO")
	private SanctionDAO sanctDAO;
	
	public List<EgovMap> getSanctConfigList() throws Exception{
		return sanctDAO.getSanctConfigList();
	}
	
	public EgovMap getSanctConfigInfo(String sanctno) throws Exception{
		return sanctDAO.getSanctConfigInfo(sanctno);
	}
	
	public boolean setModifySanctConfigInfo(HashMap<String, Object> params) throws Exception{
		try
		{
			sanctDAO.setDeleteSanctConfig(params);
			sanctDAO.setInsertSanctConfig(params);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 제재처리 사용안함 - 건by건으로 변경
	 */
	public boolean setSanctAllRegister(HashMap<String, Object> hMap) throws Exception{
		try
		{
			/*****************************************************
			 * 
			 * 신규 신청이므로 이전값존재시 모두 삭제 후 저장
			 * 제재조치 후 메일전송해야함.......
			 * 
			 * ***************************************************/
			//
			
			
			List<String> asnctList = (List<String>)hMap.get("sanctList");
			List<String> appridList =  new ArrayList<String>();
			List<HashMap<String, String>> apprInfo = new ArrayList<HashMap<String, String>>();
			for(String appr:asnctList){
				//사번 | appr_id 분리
				String[] appInfo = appr.split("/");
				
				appridList.add(appInfo[1]);
			//	System.out.println(appInfo[0] + "][" + appInfo[1] + "][" + appInfo[2] + "][" + appInfo[3] + "]["+ appInfo[4] + "][");
				HashMap<String, String> appr_Info = new HashMap<String, String>();
				appr_Info.put("empno", appInfo[0]);
				appr_Info.put("sanctid", appInfo[1]);
				appr_Info.put("mac", appInfo[2]);
				appr_Info.put("ip", appInfo[3]);
				appr_Info.put("sancttype", appInfo[4]);
				apprInfo.add(appr_Info); 
			}
			
			//전체 삭제후 신규저장
			hMap.put("sanctlist", appridList);
			hMap.put("sanctInfo", apprInfo);
			
			sanctDAO.setDeleteSanctAllList(hMap);
			sanctDAO.setUpdatePolSanctAllList(hMap);
			sanctDAO.setInsertSanctInfoAllList(hMap);
			
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * PC지킴이 파라미터 정보 조회
	 */
	public String getPcgActParamInfo(String pcgactparam) throws Exception{
		return sanctDAO.getPcgActParamInfo(pcgactparam);
	}
	
	/**
	 * 제재 / PC지킴이 처리	 * 
	 */
	public boolean setSanctInfoRegister(HashMap<String, Object> hMap, String mode) throws Exception{
		try
		{
			/*제재 시 이전 제재 삭제후 추가*/
			
			if(mode.equals("S")){
				//제재 이면 이전값 삭제
				sanctDAO.setDeleteSanctInfo(hMap);
			}
			//setUpdateUserIdxInfoScancId, setSanctInfoInsert
			sanctDAO.setSanctInfoInsert(hMap);
			sanctDAO.setUpdateUserIdxInfoScancId(hMap);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
		
	}
	
	/**
	 * 소명 정보 Insert
	 */
	public boolean setApprInfoInsert(HashMap<String, Object> hMap) throws Exception{
		try
		{
			//setUpdateUserIdxInfoScancId, setSanctInfoInsert
			sanctDAO.setApprInfoInsert(hMap);
			//sanctDAO.setUpdateUserIdxInfoApprId(hMap);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	/**
	 * 소명코드 업데이트
	 */
	public boolean setApprInfoRegister(HashMap<String, Object> hMap) throws Exception{
		try
		{
			//setUpdateUserIdxInfoScancId, setSanctInfoInsert
			//sanctDAO.setApprInfoInsert(hMap);
			sanctDAO.setUpdateUserIdxInfoApprId(hMap);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
		
	}
	/**
	 * 소명정보 Roll-back
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setApprInfoRollback(HashMap<String, Object> hMap) throws Exception{
		try
		{
			
			sanctDAO.setUpdateUserIdxInfoApprIdDelete(hMap);
			sanctDAO.setApprInfoDelete(hMap);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 제재처리를 위한 소명코드 조회 : 소명코드 없을경우 제재 하지 않음...
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public String getApprCodeInfo(HashMap<String, Object> hMap) throws Exception{
		return sanctDAO.getApprCodeInfo(hMap);
	}
}
