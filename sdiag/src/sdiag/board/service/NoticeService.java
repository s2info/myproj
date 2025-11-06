package sdiag.board.service;
import java.util.HashMap;
import java.util.List;

import sdiag.com.service.CodeInfoVO;
import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.SearchVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface NoticeService {

	/**
	 *  공지사항 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getNoticeList(SearchVO searchVO) throws Exception;
//	public boolean setNoitceInsert(NoticeVO noticeVO) throws Exception;
	public NoticeVO getNoticeInfo(int sqno) throws Exception;
	
	/**
	 *  조회수 
	 * @param sqno
	 * @return
	 * @throws Exception
	 */
	int setCommunityReadCount(long sqno) throws Exception;
	
	/**
	 *  공지사항 등록
	 * @param noticeVO
	 * @throws Exception
	 */
	void setNoitceInsert(NoticeVO noticeVO) throws Exception;
	
	/**
	 * 공지사항 수정
	 * @param noticeVO
	 * @throws Exception
	 */
	void NoticeUpdate(NoticeVO noticeVO) throws Exception;
	
	/**
	 * 공지사항 삭제
	 * @param noticeVO
	 * @throws Exception
	 */
	void NoticelistDelete(NoticeVO noticeVO) throws Exception;
	
	/**
	 * 예외자 관리 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getExcePtionRulerList(SearchVO searchVO) throws Exception;

	/**
	 * 예외자 추가를 위한 조회
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> expnListPopUplist(HashMap<String, String> hMap) throws Exception;
	
	/**
	 * 예외자 등록
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setexpnUserInsert(HashMap<String, Object> hMap) throws Exception;
	
	/**
	 * 예외자 등록
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
//	void setExpnInsert(ExcePtionRulerVO excePtionRulerVO) throws Exception;
	
	/**
	 * 예외자 삭제관리
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	boolean expnDateDelete(HashMap<String, Object> hMap) throws Exception;

	/**
	 * 대무자 관리 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getProxyRulerList(SearchVO searchVO) throws Exception;

	/**
	 * 대무자 삭제관리
	 * @param proxyDelete
	 * @return
	 * @throws Exception
	 */
	public boolean proxyDateDelete(HashMap<String, Object> proxyDelete) throws Exception;
	/**
	 * 대무자 추가를 위한 조직장 조회
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	public EgovMap getproxyAddSelectOrgCapInfo(String orgCode) throws Exception;
	
	public List<EgovMap> getproxyAddSearchUserList(HashMap<String, String> hMap) throws Exception;
	
	public boolean setProxyUserInsert(HashMap<String, Object> hMap) throws Exception;
	
	/**
	 *  진단항목 관리 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getDignosisItemList(SearchVO searchVO) throws Exception;
	/**
	 * 코드 정보 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public CodeInfoVO getCodeItemInfo(CodeInfoVO searchVO) throws Exception;
	/**
	 * 코드 관리
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<CodeInfoVO> getCodeItemList(SearchVO searchVO) throws Exception;
	public int setCodeItemCheckExist(CodeInfoVO item) throws Exception;
	public boolean setCodeItemModify(HashMap<String, Object> item) throws Exception;
	
	public int setDignosisItemCheckExist(DignosisItemVO item) throws Exception;
	
	public boolean setDignosisItemModify(HashMap<String, Object> item) throws Exception;
	/**
	 * 진단항목 삭제처리
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public boolean setDignosisItemDelete(DignosisItemVO item) throws Exception;
	
	public DignosisItemVO getDignosisItemInfo(DignosisItemVO searchVO) throws Exception;
	/**
	 * 진단항목 Order 업데이트
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public boolean setDignosisItemOrderUpdate(HashMap<String, Object> hMap) throws Exception;
	
	// FAQ LIST
	public HashMap<String,Object> getFaqList(SearchVO searchVO) throws Exception;
	
	// FAQ 페이지 이동
	public NoticeVO getFaqInfo(int sqno) throws Exception;
	
	// FAQ 등록
	void setFaqInsert(NoticeVO noticeVO) throws Exception;
	
	// FAQ 수정
	void FaqUpdate(NoticeVO noticeVO) throws Exception;
	
	// FAQ 삭제
	void FaqlistDelete(NoticeVO noticeVO) throws Exception;
	
	// 사용자관리 LIST
	public HashMap<String,Object> getUserList(SearchVO searchVO) throws Exception;
	
	// 사용자권한 관리
	public HashMap<String,Object> userListPopUplist(SearchVO searchVO) throws Exception;
	/**
	 * 예외자 검색조건 조회 - 직책 리스트
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> expnListPopUpOrgUserTitleCodelist() throws Exception;
	/**
	 * 코드리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getCodeItemListForExcelExport() throws Exception;
	/**
	 * 진단항목 리스트 Export Excel
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getDignosisItemListForExcelExport() throws Exception;
	/**
	 * 대무자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getProxyRulerListForExportExcel(SearchVO searchVO) throws Exception;
	/**
	 * 예외자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getExpnRulerListForExportExcel(SearchVO searchVO) throws Exception;
	
	/**
	 * QnA List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getQnaList(SearchVO searchVO) throws Exception;
	
	/**
	 * QnA 정보
	 * @param sq_no
	 * @return
	 * @throws Exception
	 */
	public QnaVO getQnaInfo(int sq_no) throws Exception;
	
	/**
	 * QnA 정보 수정
	 * @param sq_no
	 * @return
	 * @throws Exception
	 */
	public void qnaInfoUpdate(QnaVO qnaVO) throws Exception;
	
	/**
	 * QnA 정보 등록
	 * @param sq_no
	 * @return
	 * @throws Exception
	 */
	public void qnaInfoInsert(QnaVO qnaVO) throws Exception;
	
	/**
	 * QnA 답변 정보 저장
	 * @param sq_no
	 * @return
	 * @throws Exception
	 */
	public void qnaAnswerInfoUpdate(QnaVO qnaVO) throws Exception;
	
	/**
	 * QnA 삭제
	 * @param sq_no
	 * @return
	 * @throws Exception
	 */
	public void qnaListDelete(QnaVO qnaVO) throws Exception;
	/**
	 * Board Read Log Update
	 * @param hMap
	 * @throws Exception
	 */
	public void boardReadLogUpdate(HashMap<String, Object> hMap) throws Exception;
}
