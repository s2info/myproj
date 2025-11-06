package sdiag.server.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import sdiag.com.service.CodeInfoVO;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.UserinfoVO;
import sdiag.server.service.ServerPolSearchVO;
import sdiag.server.service.ServerPolVO;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * 
 * 정책항목에 대한 데이터 접근 클래스를 정의한다
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
 *
 * </pre>
 */
@Repository("ServerPolAnalDAO")
public class ServerPolAnalDAO extends EgovAbstractDAO {
	
	@Resource(name="analysisSqlMapClient")
    public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSuperSqlMapClient(sqlMapClient);
    }	


	/**
	 * 정책항목 엑셀파일을 등록한다.
	 * @param searchVO 
	 * @param 
	 * @throws Exception
	 */
	public ServerPolSearchVO getServerPolList(ServerPolSearchVO searchVO) throws Exception {
		return (ServerPolSearchVO) select("serverAnal.getServerPolList", searchVO);
	}


	/**
	 * 로그 List
	 * @param searchVO 
	 * @param 
	 * @throws Exception
	 */
	public List<EgovMap> getlogList(ServerPolSearchVO searchVO) throws Exception {
		List<EgovMap> list = (List<EgovMap>)list("serverAnal.getlogList", searchVO);
		return list;
	}

	/**
	 * 컬럼 List
	 * @param searchVO 
	 * @param 
	 * @throws Exception
	 */
	public List<EgovMap> getColumnList(ServerPolSearchVO searchVO) throws Exception {
		List<EgovMap> list = (List<EgovMap>)list("serverAnal.getColumnList", searchVO);
		return list;
	}


	/**
	 * 로그 정보 업데이트
	 * @param serverPolVO 
	 * @param 
	 * @throws Exception
	 */
	public void setPolLogUpdate(ServerPolVO serverPolVO) {
		update("serverAnal.setPolLogUpdate", serverPolVO);
		
	}

	/**
	 * 로그 날짜 List
	 * @param searchVO 
	 * @param 
	 * @throws Exception
	 */
	public List<EgovMap> getDateList(ServerPolSearchVO searchVO) {
		List<EgovMap> list = (List<EgovMap>)list("serverAnal.getDateList", searchVO);
		return list;
	}


	/**
	 * 로그 정보 업데이트_관리자 모드
	 * @param serverPolInfo
	 * @return
	 * @throws Exception
	 */
	public void setPolLogUpdateAdmin(ServerPolVO serverPolInfo) {
		update("serverAnal.setPolLogUpdateAdmin", serverPolInfo);
		
	}
       
}