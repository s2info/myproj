package sdiag.server.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import sdiag.com.service.CodeInfoVO;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.UserinfoVO;

public interface ServerPolService {
	

	/**
	 * 테이블 구분값를 리턴한다.
	 * @param searchVO 
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public ServerPolSearchVO getServerPolList(ServerPolSearchVO searchVO) throws Exception ;

	
	/**
	 * 서버이행관리 정책 ID List.
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getPolIdList() throws Exception ;


	/**
	 * 로그 List.
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getlogList(ServerPolSearchVO searchVO) throws Exception ;

	/**
	 * 컬럼 List.
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getColumnList(ServerPolSearchVO searchVO) throws Exception ;

	
	/**
	 * 로그 정보 업데이트
	 * @param serverPolVO
	 * @return
	 * @throws Exception
	 */
	public void setPolLogUpdate(ServerPolVO serverPolVO) throws Exception ;

	
	/**
	 * 로그 날짜 리스트
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */

	public List<EgovMap> getDateList(ServerPolSearchVO searchVO) throws Exception ;

	/**
	 * 로그 정보 업데이트_관리자 모드
	 * @param serverPolInfo
	 * @return
	 * @throws Exception
	 */

	public void setPolLogUpdateAdmin(ServerPolVO serverPolInfo);


}
