package sdiag.exanal.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.UserinfoVO;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
@Repository("PolManageDAO")
public class PolManageDAO extends EgovAbstractDAO {

	/**
	 * 정책항목 엑셀파일을 등록한다.
	 * @param pol
	 * @throws Exception
	 */
	public void insertExcelPol(PolVO polVO) throws Exception {
        insert("PolManageDAO.insertExcelPol", polVO);
	}

    /**
	 * 정책 목록을 조회한다.
     * @param polVO
     * @return List(정책 목록)
     * @throws Exception
     */
	public List selectPolList(PolVO polVO) throws Exception {
        return list("PolManageDAO.selectPolList", polVO);
    }

    /**
	 * 총 갯수를 조회한다.
     * @param polVO
     * @return int(총 갯수)
     */
    public int selectPolListTotCnt(PolVO polVO) throws Exception {
        return (Integer)getSqlMapClientTemplate().queryForObject("PolManageDAO.selectPolListTotCnt", polVO);
    }
    
    /**
     * 테이블을 생성한다 anal db.
     */
	public void createPolTable(PolVO polVO) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().queryForObject("PolManageDAO.createPolTable", polVO);
	}
	
    /**
     * 테이블을 생성한다 diag db.
     */
	public void createPolTable_diag(PolVO polVO) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().queryForObject("PolManageDAO.createPolTable_diag", polVO);
	}	

	public void insertPolList(PolVO polVO) {
		insert("PolManageDAO.insertPolList", polVO);
		
	}

	public List selectTableList(PolVO polVO) {
		return list("PolManageDAO.selectTableList", polVO);
	}

	public void insertPolTableInfo(PolVO polVO) {
		insert("PolManageDAO.insertPolTableInfo", polVO);
		
	}

	public PolVO selectFileName(int sn) throws Exception {
		return (PolVO)select("PolManageDAO.selectFileName", sn);
	}	
		
	public List selectPolListNov(PolVO polVO) throws Exception {
        return list("PolManageDAO.selectPolListNov", polVO);
    }

	public List getColumnList(PolVO polVO) throws Exception{
		// TODO Auto-generated method stub
		return list("PolManageDAO.getColumnList", polVO);
	}

	public PolVO selectUsermstr(String emp_no) throws Exception{
		return (PolVO)select("PolManageDAO.selectUsermstr", emp_no);
	}
	
	public void deleteTable(PolVO polVO) throws Exception{
		getSqlMapClientTemplate().queryForObject("PolManageDAO.deleteTable", polVO);
		
	}
	
	public void deleteTable_diag(PolVO polVO) throws Exception{
		getSqlMapClientTemplate().queryForObject("PolManageDAO.deleteTable_diag", polVO);
		
	}	

	public void deleteRecode(PolVO polVO) {
		getSqlMapClientTemplate().queryForObject("PolManageDAO.deleteRecode", polVO);
	}

	public int getCountTableName(PolVO polVO) {
        return (Integer)getSqlMapClientTemplate().queryForObject("PolManageDAO.getCountTableName", polVO);
		
	}
	
}