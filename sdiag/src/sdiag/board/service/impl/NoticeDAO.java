package sdiag.board.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.com.service.CodeInfoVO;
import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.ExcePtionRulerVO;
import sdiag.board.service.NoticeVO;
import sdiag.board.service.QnaVO;
import sdiag.man.service.SearchVO;
import sdiag.man.service.UserinfoVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("NoticeDAO")
public class NoticeDAO extends EgovAbstractDAO {
	
	// 공지사항 LIST
	public List<NoticeVO> getNoticeList(SearchVO searchVO) throws Exception{
		List<NoticeVO> list = (List<NoticeVO>)list("notice.getNoticeList", searchVO);
		return list;
	}
	// 공지사항 TotalCount
	public int getNoticeTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("notice.getNoticeTotalCount", searchVO);
	}
	// 공지사항 등록
//	public boolean setNoitceInsert(NoticeVO noticeVO) throws Exception{
//		try
//		{
//			int nseq = (int)select("notice.noticemaxseq");
//			noticeVO.setSq_no(nseq);
//			noticeVO.setContents(noticeVO.getContents().replace("\r", "<br>"));
//			insert("notice.noticeinsert", noticeVO);
//			return true;
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//			return false;
//		}
//	}
	// 공지사항 등록
	public void setNoitceInsert(NoticeVO noticeVO) {
		try {
			insert("notice.noticeinsert", noticeVO);
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// 공지사항 등록시 sqno값
	public int getMaxSqno() throws Exception{
		return (int) select("notice.noticemaxseq");
	}
	// 공지사항 페이지이동
	public NoticeVO getNoticeInfo(int sqno) throws Exception{
		return (NoticeVO)select("notice.getNoticeInfo", sqno);
	}
	// 공지사항 수정
	public void NoticeUpdate(NoticeVO noticeVO) {
		try{
			update("community.noticeRegisterUpdate", noticeVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// 공지사항 삭제
	public void NoticelistDelete(NoticeVO noticeVO) {
		try{
			update("community.NoticelistDelete", noticeVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// 공지사항 조회수 
	public int setCommunityReadCount(long sqno) throws Exception{
		return update("community.setCommunityReadCount", sqno);
	}
	// 예외자 관리 List
	public List<ExcePtionRulerVO> getExcePtionRulerList(SearchVO searchVO) throws Exception{
		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)list("user.getExcePtionRulerList", searchVO);
		return list;
	}
	// 예외자 추가관리 List
//	public List<ExcePtionRulerVO> expnListPopUplist(SearchVO searchVO) throws Exception{
//		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)list("user.expnListPopUplist", searchVO);
//		return list;
//	}
	// 예외자 추가를 위한 조회
	public List<EgovMap> expnListPopUplist(HashMap<String, String> hMap) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("user.expnListPopUplist", hMap);
		return list;
	}
	// 예외자 insert setexpnUserInsert
	public Object setexpnUserInsert(HashMap<String, Object> hMap) throws Exception{
		return insert("user.setexpnUserInsert", hMap);
	}
	// 예외자 등록
//	public void setExpnInsert(ExcePtionRulerVO excePtionRulerVO) {
//		try {
//			insert("user.setExpnInsert", excePtionRulerVO);
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.getStackTrace();
//		}
//	}
	// 예외자 TotalCount 
	public int getExpnTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("user.getExceptionTotalCount", searchVO);
	}
	// 예외자PopUp TotalCount 
	public int getExpnPopTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("user.getExpnPopTotalCount", searchVO);
	}
	// 예외자 삭제관리
	public int expnDateDelete(HashMap<String, Object> hMap) throws Exception {
		return delete("user.expnDateDelete", hMap);
	}
	// 대무자 관리 List
	/*
	public List<ExcePtionRulerVO> getProxyRulerList(SearchVO searchVO) throws Exception{
		List<ExcePtionRulerVO> list = (List<ExcePtionRulerVO>)list("user.getProxyRulerList", searchVO);
		return list;
	}
	*/
	public List<EgovMap> getProxyRulerList(SearchVO searchVO) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("user.getProxyRulerList", searchVO);
		return list;
	}
	// 대무자 TotalCount 
	public int getProxyTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("user.getProxyTotalCount", searchVO);
	}
	
	// 대무자 삭제관리
	public void proxyDateDelete(HashMap<String, String> vo) throws Exception {
		update("user.proxyDateDelete", vo);
	}
	
	/**
	 * 대무자 추가를  위한 조직장 조회
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	public EgovMap getproxyAddSelectOrgCapInfo(String orgCode) throws Exception{
		EgovMap info = (EgovMap)select("user.getproxyAddSelectOrgCapInfo", orgCode);
		return info;
	}
	
	/**
	 * 대무자 추가를 위한 사원리스트 조회
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getproxyAddSearchUserList(HashMap<String, String> hMap) throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("user.getproxyAddSearchUserList", hMap);
		return list;
	}
	
	public Object setDignosisItemInsert(DignosisItemVO item) throws Exception{
		return insert("dignosis.setDignosisItemInsert", item);
	}
	
	public int setDignosisItemUpdate(DignosisItemVO item) throws Exception{
		return update("dignosis.setDignosisItemUpdate", item);
	}
	
	public int setDignosisItemDelete(DignosisItemVO item) throws Exception{
		return update("dignosis.setDignosisItemDelete", item);
	}
	
	public int setDignosisItemMaxOrder(String majCode) throws Exception{
		return (int)select("dignosis.setDignosisItemMaxOrder", majCode);
	}
	
	public int setDignosisItemCheckExist(DignosisItemVO item) throws Exception{
		return (int)select("dignosis.setDignosisItemCheckExist", item);
	}
	/**
	 * 코드리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getCodeItemListForExcelExport() throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("code.getCodeItemListForExcelExport");
		return list;
	}
	/**
	 * 진단항목 리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDignosisItemListForExcelExport() throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("dignosis.getDignosisItemListForExcelExport");
		return list;
	}
	
	/**
	 *  진단항목 관리 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<DignosisItemVO> getDignosisItemList(SearchVO searchVO) throws Exception{
		List<DignosisItemVO> list = (List<DignosisItemVO>)list("dignosis.getDignosisItemList", searchVO);
		return list;
	}
	
	public List<CodeInfoVO> getCodeItemList(SearchVO searchVO) throws Exception{
		List<CodeInfoVO> list = (List<CodeInfoVO>)list("code.getCodeItemList", searchVO);
		return list;
	}
	
	public CodeInfoVO getCodeItemInfo(CodeInfoVO searchVO) throws Exception{
		CodeInfoVO info = (CodeInfoVO)select("code.getCodeItemInfo", searchVO);
		return info;
	}
	
	public int setCodeItemCheckExist(CodeInfoVO item) throws Exception{
		return (int)select("code.setCodeItemCheckExist", item);
	}
	
	public Object setCodeItemInsert(CodeInfoVO item) throws Exception{
		return insert("code.setCodeItemInsert", item);
	}
	
	public int setCodeItemUpdate(CodeInfoVO item) throws Exception{
		return update("code.setCodeItemUpdate", item);
	}
	
	public int setCodeItemDelete(CodeInfoVO item) throws Exception{
		return delete("code.setCodeItemDelete", item);
	}
	
	/**
	 * 진단항목 정보
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public DignosisItemVO getDignosisItemInfo(DignosisItemVO searchVO) throws Exception{
		DignosisItemVO info = (DignosisItemVO)select("dignosis.getDignosisItemInfo", searchVO);
		return info;
	}
	/**
	 * 진단항목 관리 CNT
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getDignosisItemTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("dignosis.getDignosisItemTotalCount", searchVO);
	}
	
	public Object setProxyUserInsert(HashMap<String, Object> hMap) throws Exception{
		return insert("user.setProxyUserInsert", hMap);
	}
	
	/**
	 * 진단항목 순서 변경
	 * @param vo
	 * @throws Exception
	 */
	public void setDignosisItemOrderUpdate(HashMap<String, Object> hMap) throws Exception {
		update("dignosis.setDignosisItemOrderUpdate", hMap);
	}
	
	public List<EgovMap> getExpnListPopUplist(int tseq) throws Exception{
		return (List<EgovMap>) list("user.getExpnListPopUplist" , tseq);
	}
	// FAQ LIST
	public List<NoticeVO> getFaqList(SearchVO searchVO) throws Exception{
		List<NoticeVO> list = (List<NoticeVO>)list("community.getFaqList", searchVO);
		return list;
	}
	// FAQ CNT
	public int getFaqTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("community.getFaqTotalCount", searchVO);
	}
	// FAQ 페이지이동
	public NoticeVO getFaqInfo(int sqno) throws Exception{
		return (NoticeVO)select("community.getFaqInfo", sqno);
	}
	// FAQ 등록
	public void setFaqInsert(NoticeVO noticeVO) {
		try {
			insert("community.faqinsert", noticeVO);
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// FAQ 수정
	public void FaqUpdate(NoticeVO noticeVO) {
		try{
			update("community.faqRegisterUpdate", noticeVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// FAQ 삭제
	public void FaqlistDelete(NoticeVO noticeVO) {
		try{
			update("community.faqlistDelete", noticeVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	// 사용자관리 LIST
	public List<UserinfoVO> getUserList(SearchVO searchVO) throws Exception{
		List<UserinfoVO> list = (List<UserinfoVO>)list("community.getUserList", searchVO);
		return list;
	}
	// 사용자관리 CNT
	public int getUserTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("community.getUserTotalCount", searchVO);
	}
	// 사용자 권한등록
	public List<UserinfoVO> userListPopUplist(SearchVO searchVO) throws Exception{
		List<UserinfoVO> list = (List<UserinfoVO>)list("user.userListPopUplist", searchVO);
		return list;
	}
	/**
	 * 예외자 검색조건 조회(직책 리스트)
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> expnListPopUpOrgUserTitleCodelist() throws Exception{
		List<EgovMap> list = (List<EgovMap>)list("user.expnListPopUpOrgUserTitleCodelist");
		return list;
	}
	/**
	 * 대무자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getProxyRulerListForExportExcel(SearchVO searchVO) throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("user.getProxyRulerListForExportExcel", searchVO);
		return list;
	}
	
	/**
	 * 예외자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getExpnRulerListForExportExcel(SearchVO searchVO) throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("user.getExpnRulerListForExportExcel", searchVO);
		return list;
	}
	
	/**
	 * QnA List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<QnaVO> getQnaList(SearchVO searchVO) throws Exception{
		List<QnaVO> list = (List<QnaVO>)list("qna.getQnaList", searchVO);
		return list;
	}
	
	/**
	 * QnA TotalCount
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getQnaTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("qna.getQnaTotalCount", searchVO);
	}
	
	/**
	 * QnA info
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public QnaVO getQnaInfo(int sq_no) {
		return (QnaVO)select("qna.getQnaInfo", sq_no);
	}
	
	
	/**
	 * QnA 등록
	 * @param qnaVO
	 * @return
	 * @throws Exception
	 */
	public void qnaInfoInsert(QnaVO qnaVO) {
		try {
			insert("qna.qnaInfoInsert", qnaVO);
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	/**
	 * QnA 순번 조회
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int getQnAMaxSqno() throws Exception{
		return (int) select("qna.getQnAMaxSqno");
	}
	
	/**
	 * QnA 수정
	 * @param qnaVO
	 * @return
	 * @throws Exception
	 */
	public void qnaInfoUpdate(QnaVO qnaVO) {
		try{
			update("qna.qnaInfoUpdate", qnaVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	
	/**
	 * QnA 답변 수정
	 * @param qnaVO
	 * @return
	 * @throws Exception
	 */
	public void qnaAnswerInfoUpdate(QnaVO qnaVO) {
		try{
			update("qna.qnaAnswerInfoUpdate", qnaVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}

	/**
	 * QnA 삭제
	 * @param qnaVO
	 * @return
	 * @throws Exception
	 */
	public void qnaListDelete(QnaVO qnaVO) {
		try{
			update("qna.qnaListDelete", qnaVO);
		}catch(Exception e){
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	/**
	 * 게시물 Read Log 저장
	 * @param hMap
	 * @return
	 */
	public Object readLogInsert(HashMap<String, Object> hMap) {
		return insert("board.readLogInsert", hMap);
	}
	
	/**
	 * 게시물 Read Log 수정
	 * @param qnaVO
	 * @return
	 * @throws Exception
	 */
	public int readLogUpdate(HashMap<String, Object> hMap) {
		return update("board.readLogUpdate", hMap);
	}
}
