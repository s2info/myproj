package sdiag.board.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.com.service.CodeInfoVO;
import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.ExcePtionRulerVO;
import sdiag.board.service.NoticeService;
import sdiag.board.service.NoticeVO;
import sdiag.board.service.QnaVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("NoticeService")
public class NoticeServiceImpl implements NoticeService{
	@Resource(name = "NoticeDAO")
	private NoticeDAO noticeDao;
	
	/**
	 *  공지사항 LIST
	 */
	public HashMap<String,Object> getNoticeList(SearchVO searchVO) throws Exception{
		List<NoticeVO> List = noticeDao.getNoticeList(searchVO);
		int TotalCount = noticeDao.getNoticeTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	// 공지사항 등록
//	public boolean setNoitceInsert(NoticeVO noticeVO) throws Exception{
//		return noticeDao.setNoitceInsert(noticeVO);
//	}
	/**
	 *  공지사항 등록
	 */
	public void setNoitceInsert(NoticeVO noticeVO) throws Exception{
		//System.out.println("111111111");
		int maxsqno = noticeDao.getMaxSqno();
		noticeVO.setSq_no(maxsqno);
		noticeDao.setNoitceInsert(noticeVO);
	}
	/**
	 *  공지사항 페이지이동
	 */
	public NoticeVO getNoticeInfo(int sqno) throws Exception{
		return noticeDao.getNoticeInfo(sqno);
	}
	/**
	 *  공지사항 수정
	 */
	public void NoticeUpdate(NoticeVO noticeVO) throws Exception{
		noticeDao.NoticeUpdate(noticeVO);
	}
	/**
	 *  공지사항 삭제
	 */
	public void NoticelistDelete(NoticeVO noticeVO) throws Exception{
		 noticeDao.NoticelistDelete(noticeVO);
	}
	/**
	 *  공지사항 조회수 
	 */
	public int setCommunityReadCount(long sqno) throws Exception{
    	return noticeDao.setCommunityReadCount(sqno);
    }
	/**
	 *  예외자 관리
	 */
	public HashMap<String,Object> getExcePtionRulerList(SearchVO searchVO) throws Exception{
		List<ExcePtionRulerVO> List = noticeDao.getExcePtionRulerList(searchVO);
		
		int TotalCount = noticeDao.getExpnTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	// 예외자 추가관리 LIST
//	public HashMap<String,Object> expnListPopUplist(SearchVO searchVO) throws Exception{
//		List<ExcePtionRulerVO> List = noticeDao.expnListPopUplist(searchVO);
//		
//		int TotalCount = noticeDao.getExpnPopTotalCount(searchVO);
//		
//		HashMap<String,Object> rMap = new HashMap<String,Object>();
//		rMap.put("list", List);
//		rMap.put("totalCount", TotalCount);
//		
//		return rMap;
//	}
	
	/**
	 *  예외자 추가를 위한 조회
	 */
	public List<EgovMap> expnListPopUplist(HashMap<String, String> hMap) throws Exception{
		
		return noticeDao.expnListPopUplist(hMap);
		
	}
	/**
	 *  예외자 insert setexpnUserInsert
	 */
	public boolean setexpnUserInsert(HashMap<String, Object> hMap) throws Exception{
		try
		{
			noticeDao.setexpnUserInsert(hMap);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	// 예외자 등록
//	public void setExpnInsert(ExcePtionRulerVO excePtionRulerVO) throws Exception{
		//System.out.println("111111111");
//		int maxsqno = noticeDao.getMaxSqno();
//		noticeVO.setSq_no(maxsqno);
//		noticeDao.setExpnInsert(excePtionRulerVO);
//	}
	/**
	 *  예외자 삭제관리
	 */
	public boolean expnDateDelete(HashMap<String, Object> hMap) throws Exception{
		try
		{
			int ret = noticeDao.expnDateDelete(hMap);
			System.out.println(ret + "][");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 *  대무자 관리
	 */
	public HashMap<String,Object> getProxyRulerList(SearchVO searchVO) throws Exception{
		List<EgovMap> List = noticeDao.getProxyRulerList(searchVO);
		
		int TotalCount = noticeDao.getProxyTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	/**
	 *  대무자 삭제관리
	 */
	public boolean proxyDateDelete(HashMap<String, Object> proxyDelete) throws Exception{
		try
		{
			List<String> pList = (List<String>)proxyDelete.get("proxyList");
			for(String str:pList){
				String[] proInfo = str.split("/");
				HashMap<String, String> delInfo = new HashMap<String, String>();
				delInfo.put("emp_no", proInfo[0]);
				delInfo.put("pemp_no", proInfo[1]);
				
				noticeDao.proxyDateDelete(delInfo);
			}
			return true;
		}
		catch(Exception e){
		 	e.printStackTrace();
			return false;
		}
	}
	/**
	 * 대무자 지정을 위한 직원조회
	 */
	public List<EgovMap> getproxyAddSearchUserList(HashMap<String, String> hMap) throws Exception{
		return noticeDao.getproxyAddSearchUserList(hMap);
	}
	/**
	 * 대무자 저장
	 */
	public boolean setProxyUserInsert(HashMap<String, Object> hMap) throws Exception{
		try
		{
			noticeDao.setProxyUserInsert(hMap);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 부서장 조회
	 */
	public EgovMap getproxyAddSelectOrgCapInfo(String orgCode) throws Exception{
		return noticeDao.getproxyAddSelectOrgCapInfo(orgCode);
	}
	
	/**
	 *  진단항목 관리 LIST
	 */
	public HashMap<String,Object> getDignosisItemList(SearchVO searchVO) throws Exception{
		List<DignosisItemVO> List = noticeDao.getDignosisItemList(searchVO);
		int TotalCount = noticeDao.getDignosisItemTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	
	/**
	 * 코드 관리
	 */
	public List<CodeInfoVO> getCodeItemList(SearchVO searchVO) throws Exception{
		return noticeDao.getCodeItemList(searchVO);
	}
	/**
	 * 코드정보 
	 */
	public CodeInfoVO getCodeItemInfo(CodeInfoVO searchVO) throws Exception{
		return noticeDao.getCodeItemInfo(searchVO);
	}
	/**
	 * 코드정보 존재여부
	 */
	public int setCodeItemCheckExist(CodeInfoVO item) throws Exception{
		return noticeDao.setCodeItemCheckExist(item);
	}
	/**
	 * 코드 수정
	 */
	public boolean setCodeItemModify(HashMap<String, Object> item) throws Exception{
		try
		{
			CodeInfoVO itemInfo = (CodeInfoVO)item.get("item");	
			if(item.get("mode").equals("1")){
				noticeDao.setCodeItemInsert(itemInfo);
			}else if(item.get("mode").equals("3")){
				noticeDao.setCodeItemInsert(itemInfo);
			}else if(item.get("mode").equals("5")){
				noticeDao.setCodeItemDelete(itemInfo);
			}else{
				noticeDao.setCodeItemUpdate(itemInfo);
			}
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 진단항목 존재여부
	 */
	public int setDignosisItemCheckExist(DignosisItemVO item) throws Exception{
		return noticeDao.setDignosisItemCheckExist(item);
	}
	/**
	 * 진단항목 수정
	 */
	public boolean setDignosisItemModify(HashMap<String, Object> item) throws Exception{
		try
		{
			DignosisItemVO itemInfo = (DignosisItemVO)item.get("item");	
			if(item.get("mode").equals("1")){
				
				int maxorder = noticeDao.setDignosisItemMaxOrder(itemInfo.getDiag_majr_code());
				itemInfo.setOrdr(String.valueOf(maxorder));
								
				noticeDao.setDignosisItemInsert(itemInfo);
				
			}else if(item.get("mode").equals("3")){
				noticeDao.setDignosisItemInsert(itemInfo);
			}else{
				noticeDao.setDignosisItemUpdate(itemInfo);
			}
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 진단항목 정보 조회
	 */
	public DignosisItemVO getDignosisItemInfo(DignosisItemVO searchVO) throws Exception{
		return noticeDao.getDignosisItemInfo(searchVO);
	}
	/**
	 * 진단항목 정렬순서 업데이트
	 */
	public boolean setDignosisItemOrderUpdate(HashMap<String, Object> hMap) throws Exception{
		try
		{
			noticeDao.setDignosisItemOrderUpdate(hMap);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 진단항목 삭제
	 */
	public boolean setDignosisItemDelete(DignosisItemVO item) throws Exception{
		
		try
		{
			noticeDao.setDignosisItemDelete(item);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 *  FAQ LIST
	 */
	public HashMap<String,Object> getFaqList(SearchVO searchVO) throws Exception{
		List<NoticeVO> List = noticeDao.getFaqList(searchVO);
		int TotalCount = noticeDao.getFaqTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	/**
	 *  FAQ 페이지이동
	 */
	public NoticeVO getFaqInfo(int sqno) throws Exception{
		return noticeDao.getFaqInfo(sqno);
	}
	/**
	 *  FAQ 등록
	 */
	public void setFaqInsert(NoticeVO noticeVO) throws Exception{
		//System.out.println("111111111");
		int maxsqno = noticeDao.getMaxSqno();
		noticeVO.setSq_no(maxsqno);
		noticeDao.setFaqInsert(noticeVO);
	}
	/**
	 *  FAQ 수정
	 */
	public void FaqUpdate(NoticeVO noticeVO) throws Exception{
		noticeDao.FaqUpdate(noticeVO);
	}
	/**
	 *  FAQ 삭제
	 */
	public void FaqlistDelete(NoticeVO noticeVO) throws Exception{
		 noticeDao.FaqlistDelete(noticeVO);
	}
	
	/**
	 * 사용자관리 LIST	 
	 */
	public HashMap<String,Object> getUserList(SearchVO searchVO) throws Exception{
		List<UserinfoVO> List = noticeDao.getUserList(searchVO);
		int TotalCount = noticeDao.getUserTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	
	/**
	 * 예외자 추가관리 LIST
	 */
	public HashMap<String,Object> userListPopUplist(SearchVO searchVO) throws Exception{
		List<UserinfoVO> List = noticeDao.userListPopUplist(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		
		return rMap;
	}
	
	public List<EgovMap> expnListPopUpOrgUserTitleCodelist() throws Exception{
		return noticeDao.expnListPopUpOrgUserTitleCodelist();
	}
	
	/**
	 * 코드리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getCodeItemListForExcelExport() throws Exception{
		return noticeDao.getCodeItemListForExcelExport();
	}
	
	/**
	 * 진단항목 리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDignosisItemListForExcelExport() throws Exception{
		return noticeDao.getDignosisItemListForExcelExport();
	}
	
	/**
	 * 대무자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getProxyRulerListForExportExcel(SearchVO searchVO) throws Exception{
		return noticeDao.getProxyRulerListForExportExcel(searchVO);
	}
	
	/**
	 * 예외자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getExpnRulerListForExportExcel(SearchVO searchVO) throws Exception{
		return noticeDao.getExpnRulerListForExportExcel(searchVO);
	}
	
	/**
	 *  QnA LIST
	 */
	public HashMap<String,Object> getQnaList(SearchVO searchVO) throws Exception{
		List<QnaVO> List = noticeDao.getQnaList(searchVO);
		int TotalCount = noticeDao.getQnaTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}

	/**
	 *  QnA info
	 */
	public QnaVO getQnaInfo(int sq_no) throws Exception {
		return noticeDao.getQnaInfo(sq_no);
	}
	
	
	/**
	 *  QnA 등록
	 */
	public void qnaInfoInsert(QnaVO qnaVO) throws Exception{
		//System.out.println("111111111");
		int maxsqno = noticeDao.getQnAMaxSqno();
		qnaVO.setSq_no(maxsqno);
		noticeDao.qnaInfoInsert(qnaVO);
	}

	/**
	 * QnA 수정
	 */
	public void qnaInfoUpdate(QnaVO qnaVO) throws Exception{
		noticeDao.qnaInfoUpdate(qnaVO);
	}
	
	/**
	 * QnA 답변 수정
	 */
	public void qnaAnswerInfoUpdate(QnaVO qnaVO) throws Exception{
		noticeDao.qnaAnswerInfoUpdate(qnaVO);
	}
	/**
	 * QnA 삭제
	 */
	public void qnaListDelete(QnaVO qnaVO) throws Exception{
		 noticeDao.qnaListDelete(qnaVO);
	}
	/**
	 * Board Read Log Update
	 * @param hMap
	 * @throws Exception
	 */
	public void boardReadLogUpdate(HashMap<String, Object> hMap) throws Exception{
		try{
			int ret = noticeDao.readLogUpdate(hMap);
			if(ret == 0){
				noticeDao.readLogInsert(hMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
