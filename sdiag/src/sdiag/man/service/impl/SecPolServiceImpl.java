package sdiag.man.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.groupInfo.service.GroupDetailInfoVO;
import sdiag.man.service.PolConVO;
import sdiag.man.service.PolTargetInfoVO;
import sdiag.man.service.SecPolInfoVO;
import sdiag.man.service.SecPolService;
import sdiag.pol.service.PolicySearchVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("SecPolService")
public class SecPolServiceImpl implements SecPolService {
	@Resource(name = "SecPolDAO")
	private SecPolDAO secPolDAO;
	
	public HashMap<String,Object> getSecPolList(PolicySearchVO searchVO) throws Exception{
		List<SecPolInfoVO> List = secPolDAO.getSecPolList(searchVO);
		int TotalCount = secPolDAO.getSecPolListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	
	public  HashMap<String,Object> getPolIdxInfo(String sec_pol_id) throws Exception{
		SecPolInfoVO polInfo = secPolDAO.getPolIdxInfo(sec_pol_id);
		PolConVO conInfo = secPolDAO.getPolConInfo(sec_pol_id);
		List<PolTargetInfoVO> list = secPolDAO.getPolTargetList(sec_pol_id);
		
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("polInfo", polInfo);
		rMap.put("conInfo", conInfo);
		rMap.put("polTargetList", list);
		
		return rMap;
	}
	
	public int setModifyPolInfo(SecPolInfoVO secPolInfoVO, PolConVO polConInfo, PolTargetInfoVO polTargetInfoVO) throws Exception{
		int ret=-1;
		try
		{
			int rowcount = secPolDAO.setUpdatePolInfo(secPolInfoVO);
			if(rowcount == 0){
				//추가
				secPolDAO.setInsertPolInfo(secPolInfoVO);
			}
			
			int rowcount1 = secPolDAO.setUpdatePolConInfo(polConInfo);
			if(rowcount1 == 0){
				secPolDAO.setInsertPolConInfo(polConInfo);
			}
			
			//정책 대상자 정보 저장
			if(!polTargetInfoVO.getTargetCodeArr().isEmpty() && !polTargetInfoVO.getTargetNmArr().isEmpty() && !polTargetInfoVO.getTargetTypeArr().isEmpty()){
				setPolTargetInfoInsert(polTargetInfoVO);
			}
			
			
			ret=0;
			
		}catch(Exception e){
			e.printStackTrace();
			ret=-1;
		}
		return ret;
	}
	
	
	private void setPolTargetInfoInsert(PolTargetInfoVO polTargetInfoVO) throws Exception{
		int cnt =0;
		
		String[] targetCodeArr = polTargetInfoVO.getTargetCodeArr().split(","); 
		String[] targetTypeArr = polTargetInfoVO.getTargetTypeArr().split(",");
		String[] targetNmArr = polTargetInfoVO.getTargetNmArr().split(",");
		
		
		for (int i=0; i<targetCodeArr.length; i++){
			PolTargetInfoVO polTargetInfo = new PolTargetInfoVO();
			polTargetInfo.setSec_pol_id(polTargetInfoVO.getSec_pol_id());
			polTargetInfo.setTargetCode(targetCodeArr[i]);
			polTargetInfo.setTargetType(targetTypeArr[i]);
			polTargetInfo.setTargetNm(targetNmArr[i]);
			/* 그룹 상세 여부 존재 여부 체크  - 해당 정보가 존재 하지 않으면 저장*/
			cnt = secPolDAO.polTargetInfoCheck(polTargetInfo);
			
			if(cnt==0)
				secPolDAO.setPolTargetInfoInsert(polTargetInfo);
			
			cnt=0;
		}
	}

	/**
	 * 지수화정책 로그조회 테이블 조회
	 */
	public List<EgovMap> getPolSourceLogTables(String nspname) throws Exception{
		return secPolDAO.getPolSourceLogTables(nspname);
	}
	
	public List<EgovMap> getPolSourceLogTableColumns(String tname) throws Exception{
		return secPolDAO.getPolSourceLogTableColumns(tname);
	}
	/**
	 * 정책정보 리스트 Export Excel
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getSecPolListForExportExcel(PolicySearchVO searchVO) throws Exception{
		return secPolDAO.getSecPolListForExportExcel(searchVO);
	}

	@Override
	public void polTargetInfoDelete(PolTargetInfoVO polTargetInfoVO) throws Exception{
		secPolDAO.polTargetInfoDelete(polTargetInfoVO);
		
	}
}
