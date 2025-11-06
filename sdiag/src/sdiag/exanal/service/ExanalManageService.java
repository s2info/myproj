package sdiag.exanal.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.UserinfoVO;

/**
 * 
 * 엑셀파일 등록에 관한 서비스 인터페이스 클래스를 정의한다
 * @author LEE CHANG JAE
 * @since 2015.10.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.10.27  CJLee          최초 생성
 *
 * </pre>
 */

public interface ExanalManageService {
	
	/**
	 * 엑셀파일을 등록한다.
	 * @param file
	 * @throws Exception
	 */
	void insertExcelPol(InputStream file) throws Exception;
	
	/**
	 * 엑셀파일리스트를 등록한다.
	 * @param polVO
	 * @throws Exception
	 */
	void insertPolList(PolVO polVO) throws Exception;	
	
	/**
	 * 목록을 조회한다.
	 * @param polVO
	 * @return List(정책로그 목록)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	List selectPolList(PolVO polVO) throws Exception;

    /**
	 * 총 갯수를 조회한다.
     * @param polVO
     * @return int(총 갯수)
     */
    int selectPolListTotCnt(PolVO polVO) throws Exception;
    
	/**
	 * 정책 항목을 수정한다.
	 * @param pol
	 * @throws Exception
	 */
	//void updatePol(Pol pol) throws Exception;
	
	/**
	 * 테이블을 생성한다.
	 * @param pol
	 * @throws Exception
	 */
	void createPolTable(PolVO polVO) throws Exception;
	void updateSelectUsermstr(PolVO polVO) throws Exception;

	/**
	 * 테이블목록을 조회한다.
	 * @param polVO
	 * @return List(테이블 목록)
	 * @throws Exception
	 */
	List selectTableList(PolVO polVO) throws Exception;

	/**
	 * 테이블 정보를 입력한다.
	 * @param pol
	 * @throws Exception
	 */
	void insertPolTableInfo(PolVO polVO) throws Exception;

	/**
	 * 테이블 이름을 가져온다.
	 * @param pol
	 * @throws Exception
	 */	
	public PolVO selectFileName(int sn) throws Exception;

	/**
	 * 컬럼명을 가져온다.
	 * @param pol
	 * @throws Exception
	 */	
	List<EgovMap> getColumnList(PolVO polVO) throws Exception;

	public PolVO selectUsermstr(String emp_no) throws Exception;
	
	void deleteTable(PolVO polVO) throws Exception;

	void deleteRecode(PolVO polVO) throws Exception;
	
	/**
	 * 테이블 카운트를 리턴한다.
	 * @param polVO
	 * @return
	 * @throws Exception
	 */
	int getCountTableName(PolVO polVO) throws Exception;

	
	/**
	 * 테이블 구분값를 리턴한다.
	 * @param String
	 * @return
	 * @throws Exception
	 */
	String getGubunInfo(String table_name) throws Exception;

	void insertPolWanList(PolVO polVO) throws Exception;


}
