package sdiag.man.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.man.service.OrgStatisticService;
import sdiag.man.vo.OrgStatisticSearchVO;
import sdiag.man.vo.StatInfoVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.securityDay.service.SdSearchVO;

@Service("OrgStatisticService")
public class OrgStatisticServiceImpl implements OrgStatisticService{
	@Resource(name = "OrgStatisticDAO")
	private OrgStatisticDAO OrgStatDAO;
	
	/**
	 * 통계 정보 List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getStatInfoList(OrgStatisticSearchVO searchVO) throws Exception {
		List<EgovMap> List = OrgStatDAO.getStatInfoList(searchVO);
		int TotalCount = OrgStatDAO.getStatInfoListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		return rMap;
	}

	/**
	 * 정책 List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolList() throws Exception {
		return OrgStatDAO.getPolList();
	}

	public void statInfoInsert(StatInfoVO statInfoVo)
			throws Exception {
		
		
		OrgStatDAO.statInfoInsert(statInfoVo);
		
		if(statInfoVo.getStatPolList() != null){
			OrgStatDAO.statPolInfoInsert(statInfoVo);
		}
		
	}

	public int getStatSeq() throws Exception {
		return OrgStatDAO.getStatSeq();
	}
	
	
	public void statUploadSoceInsert(StatInfoVO statInfoVo)
			throws Exception {
		FileInputStream fis = null;

		for(StatInfoVO statUpPolInfo :  statInfoVo.getStatUpPolList()){
			HashMap<String,Object> rMap = new HashMap<String,Object>();
			rMap.put("polIdxId", statUpPolInfo.getPolIdxId());
			rMap.put("rgdtEmpNo", statInfoVo.getRgdtEmpNo());
			rMap.put("statSeq", statInfoVo.getStatSeq());
			
			
			
			try{
			
			// excel body 내용 메모리에 저장 BEGIN
			List<HashMap<String,Object>> scoreList = new ArrayList<HashMap<String,Object>>();     // 점수 리스트
			
			
							// itsm_order_policy_info > list
			
			try{
				fis = new FileInputStream(new File(statUpPolInfo.getExcelFile()));
			} catch(Exception ex){
				break;					// 2 for : crm excel list stop
			}
			
			Workbook wb = WorkbookFactory.create(fis);
			XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);
			
				
			XSSFRow bodyRow = null;
			XSSFCell bodyCell = null;
			
			for (int k = 1; k <= sheet.getLastRowNum(); k++){
				// 3 for : excel row list BEGIN
				HashMap<String,Object> sMap = new HashMap<String,Object>();
				bodyRow = sheet.getRow(k);
				
				for (int l = 0; l < bodyRow.getLastCellNum(); l++){															// 4 for : row cell list BEGIN
					try{
					bodyCell = bodyRow.getCell(l);
					String value = "0";
					
					
					switch (bodyCell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = bodyCell.getStringCellValue().replaceAll(" ", "");
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = String.valueOf(bodyCell.getBooleanCellValue());
						break;
					case XSSFCell.CELL_TYPE_ERROR:
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						value = bodyCell.getCellFormula();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						value = String.valueOf((int) bodyCell.getNumericCellValue()).replaceAll(" ", "");
						break;
					default:
						break;
					}
					
					if (bodyCell == null ) break; // 4 for : row cell list stop									
					
					switch (l) {
					case 0:			// 사번
						sMap.put("empno", String.valueOf(value));
						break;
					case 1:			// 점수
						sMap.put("score", Integer.parseInt(value));
						break;
											
					default : break;
					}
					}catch(NullPointerException ne){
						continue;
					}
				}																												// 4 for : row cell list END
				
				
 				scoreList.add(sMap);
				
				
			}																													// 3 for : excel row list END
			
			// excel body 내용 메모리에 저장 END
			rMap.put("scoreList", scoreList);
			
			
			} catch(Exception ex){
				ex.printStackTrace();
					
				break;									
			
			}finally{
				if(fis != null){
					fis.close();
				}	
			}
			
		}
		
	}

	/**
	 * 정책 List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getStatPolList(OrgStatisticSearchVO searchVO) {
		return OrgStatDAO.getStatPolList(searchVO);
	}
	
	/**
	 * 수동 cnt
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getUpPolCnt(OrgStatisticSearchVO searchVO) {
		return OrgStatDAO.getUpPolCnt(searchVO);
	}

	
	public void statPolInfoDelete(OrgStatisticSearchVO searchVO) {
		
		
	}
	/**
	 * 통계 정책 삭제
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public void statInfoUpdate(StatInfoVO statInfoVo)
			throws Exception {
		
		OrgStatDAO.statInfoUpdate(statInfoVo);
		
		OrgStatDAO.statPolInfoDelete(statInfoVo);
		if(statInfoVo.getStatPolList() != null){
			OrgStatDAO.statPolInfoInsert(statInfoVo);
		}
		
		
				
	}

	/**
	 * 통계 정보
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public StatInfoVO getStatInfo(OrgStatisticSearchVO searchVO)
			throws Exception {
		return OrgStatDAO.getStatInfo(searchVO);
	}

	/**
	 * 통계 정보
	 * @param statInfoVo
	 * @return
	 * @throws Exception
	 */
	public void setUploadScore(StatInfoVO statInfoVo) {
		/*List<StatInfoVO> upPolIdxList = new ArrayList<StatInfoVO>();
		OrgStatisticSearchVO searchVO = new OrgStatisticSearchVO();
		searchVO.setStatSeq(statInfoVo.getStatSeq());
		
		upPolIdxList =OrgStatDAO.getStatPolList(searchVO);
		
		for(StatInfoVO upPolInfo : upPolIdxList){
			if(upPolInfo.getGubun().equals("U")){*/
				//OrgStatDAO.setUploadScore(statInfoVo);
		OrgStatDAO.statPolSumDelete(statInfoVo);
				OrgStatDAO.setUploadScore(statInfoVo);
			/*}
		}*/
		
	}

	/**
	 * 통계 토탈 정보
	 * @param statInfoVo
	 * @return
	 * @throws Exception
	 */
	public void setTotalStatScore(StatInfoVO statInfoVo) {
		OrgStatDAO.setTotalStatScoreDelete(statInfoVo);
		
		OrgStatDAO.setTotalStatScore(statInfoVo);
		
	}

	/**
	 * 통계 정책 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getPolIdxList(StatInfoVO statInfoVo)
			throws Exception {
		
		return OrgStatDAO.getPolIdxList(statInfoVo);
	}

	/**
	 * 통계 수동 정책 정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<StatInfoVO> getUpPolIdxList(StatInfoVO statInfoVo)
			throws Exception {
		return OrgStatDAO.getUpPolIdxList(statInfoVo);
	}

	/**
	 * 통계  정보
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getOrgStatInfo(StatInfoVO statInfoVo) throws Exception {
		return OrgStatDAO.getOrgStatInfo(statInfoVo);
	}

	/**
	 * 통계  정보 삭제
	 * @param statInfo
	 * @return
	 * @throws Exception
	 */
	public void orgStatInfoDelete(StatInfoVO statInfo) throws Exception {
		OrgStatDAO.orgStatInfoDelete(statInfo);
	}

	/**
	 * 통계  정보 삭제
	 * @param statInfo
	 * @return
	 * @throws Exception
	 */
	public void statUploadScoreDelete(HashMap<String, Object> rMap) throws Exception {
		OrgStatDAO.statUploadScoreDelete(rMap);
		
	}

	public void statUploadScoreInsert(HashMap<String, Object> rMap) throws Exception {
		OrgStatDAO.statUploadScoreInsert(rMap);
		
	}

	public void setStatUploadScoreDelete(StatInfoVO statInfoVo)
			throws Exception {
		OrgStatDAO.setStatUploadScoreDelete(statInfoVo);
		
	}


}
