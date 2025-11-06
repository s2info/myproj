package sdiag.exanal.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sdiag.dash.service.DashboardService;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.ExanalManageService;
import sdiag.exanal.service.Pol;
import sdiag.exanal.service.PolVO;
import sdiag.getdata.service.DBConnection;
import sdiag.getdata.service.DBConnectionAnal;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * 
 * 정책등록에 대한 서비스 구현클래스를 정의한다
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
@Service("ExanalManageService")
public class ExanalManageServiceImpl implements ExanalManageService {

    @Resource(name="PolManageDAO")
    private PolManageDAO polManageDAO;
    
	@Resource(name="PolManageAnalDAO")
	private PolManageAnalDAO polManageAnalDAO;

	private Connection conn = null;		
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 정책 엑셀파일을 등록한다.
	 * @param zip
	 * @throws Exception
	 */
	public void insertExcelPol(InputStream file) throws Exception {
		//polManageDAO.insertExcelPol(PolVO polVO);
		//excelPolService.uploadExcel("PolManageDAO.insertExcelPol", file, 2, (long) 5000); 
	}
	
	public void insertPolList(PolVO polVO) throws Exception {
		
		//polManageAnalDAO.insertPolList(polVO);
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO ");
				sql.append(polVO.getGenTableName());
				sql.append("(");
				sql.append(polVO.getColumnList());
				sql.append(") VALUES (");
				sql.append(polVO.getValueList());
				sql.append(");");
				pstmt = conn.prepareStatement(sql.toString());
				
				pstmt.executeUpdate();
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
		
	}	

	public void updateSelectUsermstr(PolVO polVO)throws Exception {		
		//polManageAnalDAO.updateSelectUsermstr(polVO);
		/*
		 * with t as (
			select   
				U.sldm_empno as sldm_empno, 
				U.sldm_ip as sldm_ip,
				U.sldm_mac as sldm_mac
			from user_mstr U
			left join $genTableName$ S on S.empno = U.sldm_empno	
		)	
		update $genTableName$ 
			set sldm_empno = t.sldm_empno, sldm_ip = t.sldm_ip, sldm_mac = t.sldm_mac
		from t
		where empno = t.sldm_empno
		 * */
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			/* 2016-12-20 jinney
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("with t as ("
						+ "select "
						+ "U.sldm_empno as sldm_empno"
						+ ", U.sldm_ip as sldm_ip"
						+ ", U.sldm_mac as sldm_mac ");
				sql.append(" from user_mstr U ");
				sql.append(" left join "+ polVO.getGenTableName() + " S on S.empno = U.sldm_empno");
				sql.append(" ) ");
				sql.append(" update "+ polVO.getGenTableName());
				sql.append(" set sldm_empno = t.sldm_empno, sldm_ip = t.sldm_ip, sldm_mac = t.sldm_mac ");
				sql.append(" from t ");
				sql.append(" where empno = t.sldm_empno;");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.executeUpdate();
			}
			*/
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("with t as ("
						+ "select "
						+ "U.emp_no as sldm_empno"
						+ ", '' as sldm_ip"
						+ ", '' as sldm_mac ");
				sql.append(" from nx_org_user U ");
				sql.append(" left join "+ polVO.getGenTableName() + " S on S.empno = U.emp_no");
				sql.append(" ) ");
				sql.append(" update "+ polVO.getGenTableName());
				sql.append(" set sldm_empno = t.sldm_empno, sldm_ip = t.sldm_ip, sldm_mac = t.sldm_mac ");
				sql.append(" from t ");
				sql.append(" where empno = t.sldm_empno;");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.executeUpdate();
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
		
	}	
	
	/**
	 * 정책 목록을 조회한다.
	 */
	public List selectPolList(PolVO polVO) throws Exception {
		//return polManageAnalDAO.selectPolList(polVO);
	
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		List<EgovMap> revalList = new ArrayList<EgovMap>();
		try{
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM ");
				sql.append(polVO.getTable_name());
				sql.append(" WHERE 1=1 ");
				if(!polVO.getBegin_date().equals("")){
					sql.append(" AND to_char(sldm_org_logdate, 'YYYYMMDD') ='"+ polVO.getBegin_date() + "' ");
				}
					
				sql.append(" ORDER BY sldm_org_logdate desc  ");
				sql.append(" LIMIT ? OFFSET ?  ");
				
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setInt(1, polVO.getRecordCountPerPage());
				pstmt.setInt(2, polVO.getFirstIndex());
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				while(rs.next()){
					EgovMap map = new EgovMap();
					for(int i=1 ;i<=rsmd.getColumnCount() ;i++){
						map.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
					}
					revalList.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        try{ 
	            if(rs!=null)	rs.close();
	            if(pstmt!=null)	pstmt.close();
	            if(stmt!=null)	stmt.close();
	            if(conn!=null)	conn.close();
	        }catch(SQLException se2){
	            se2.printStackTrace();
	        }   
		}
		
		return revalList;
	}

	/**
	 * 총 갯수를 조회한다.
	 */
	public int selectPolListTotCnt(PolVO polVO) throws Exception {
		//return polManageAnalDAO.selectPolListTotCnt(polVO);
		
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		int retval = 0;
		try{
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT COUNT(*) COUNT FROM ");
				sql.append(polVO.getTable_name());
				pstmt = conn.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				if (rs != null){
					while(rs.next()){
						retval = rs.getInt("COUNT");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        try{ 
	            if(rs!=null)	rs.close();
	            if(pstmt!=null)	pstmt.close();
	            if(stmt!=null)	stmt.close();
	            if(conn!=null)	conn.close();
	        }catch(SQLException se2){
	            se2.printStackTrace();
	        }   
		}
		
		return retval;
	}

	/**
	 * 테이블을 생성한다.
	 * @return 
	 */
	private void createAnalPolTable(PolVO polVO)throws Exception {
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("CREATE TABLE IF NOT EXISTS ");
				sql.append(polVO.getGenTableName());
				sql.append("(");
				sql.append(polVO.getColumnDescription());
				sql.append(");");
				sql.append("CREATE INDEX ");
				sql.append(polVO.getGenTableName()+"_idx1 ON " + polVO.getGenTableName());
				sql.append("(sldm_empno, sldm_ip, sldm_mac, sldm_org_logdate");
				if(polVO.getGubun().equals("G")){
					sql.append(",log_yn");
				}
				sql.append(");");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.executeUpdate();
				
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
	}
	
	private void createDiagPolTable(PolVO polVO)throws Exception {	
		try{			
			DBConnection dbConn = DBConnection.getConnect();
			conn = dbConn.getConn();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("CREATE TABLE IF NOT EXISTS ");
				sql.append(polVO.getGenTableName_diag());
				sql.append("(");
				sql.append(polVO.getColumnDescription_diag());
				sql.append(");");
				sql.append("CREATE INDEX ");
				sql.append(polVO.getGenTableName_diag()+"_idx1 ON " + polVO.getGenTableName_diag());
				sql.append("(sldm_empno, sldm_ip, sldm_mac, sldm_org_logdate,");
				if(polVO.getGubun().equals("G")){
					sql.append("log_yn,");
				}		
				sql.append("sldm_eventdate, sldm_explan_flag, sldm_extract_value);");
				sql.append("CREATE INDEX ");
				sql.append(polVO.getGenTableName_diag()+"_idx2 ON " + polVO.getGenTableName_diag());
				sql.append("(sldm_policy_id);");
				sql.append("CREATE INDEX ");
				sql.append(polVO.getGenTableName_diag()+"_idx3 ON " + polVO.getGenTableName_diag());
				sql.append("(sldm_org_logdate);");
				
				if(polVO.getGubun().equals("G")){
					sql.append("CREATE INDEX ");
					sql.append(polVO.getGenTableName_diag()+"_idx4 ON " + polVO.getGenTableName_diag());
					sql.append("(log_yn);");
				}
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.executeUpdate();
				
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
	}
	public void createPolTable(PolVO polVO)throws Exception {		
		
		/*
		try{
			polManageAnalDAO.createPolTable(polVO);
			polManageDAO.createPolTable_diag(polVO);
			
		} catch (Exception e) {
			polVO.setMsg("테이블이 생성되지 않았습니다.");
		}		
			  	
		*/
		
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		this.createAnalPolTable(polVO);
		this.createDiagPolTable(polVO);
		
	}

	@Override
	public List selectTableList(PolVO polVO) throws Exception {
		return polManageAnalDAO.selectTableList(polVO);
	}

	@Override
	public void insertPolTableInfo(PolVO polVO) throws Exception {
		polManageAnalDAO.insertPolTableInfo(polVO);
	}

	public PolVO selectFileName(int sn) throws Exception {
		return polManageAnalDAO.selectFileName(sn);
	}

	public List selectPolListNov(PolVO polVO) throws Exception {
		//return polManageAnalDAO.selectPolListNov(polVO);
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		List<EgovMap> revalList = new ArrayList<EgovMap>();
		try{
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM ");
				sql.append(polVO.getTable_name());
				sql.append(" LIMIT 50;");
			
				pstmt = conn.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				while(rs.next()){
					EgovMap map = new EgovMap();
					for(int i=1 ;i<=rsmd.getColumnCount() ;i++){
						map.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
					}
					revalList.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        try{ 
	            if(rs!=null)	rs.close();
	            if(pstmt!=null)	pstmt.close();
	            if(stmt!=null)	stmt.close();
	            if(conn!=null)	conn.close();
	        }catch(SQLException se2){
	            se2.printStackTrace();
	        }   
		}
		
		return revalList;
	}

	@Override
	public List getColumnList(PolVO polVO) throws Exception {
		//return polManageAnalDAO.getColumnList(polVO);
		
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		List<EgovMap> revalList = new ArrayList<EgovMap>();
		try{
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT column_name as columnname ");
				sql.append("FROM information_schema.columns ");
				sql.append("WHERE table_name ILIKE ?");
			
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, polVO.getTable_name());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					EgovMap map = new EgovMap();
					map.put("columnname", rs.getString("columnname"));
					revalList.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        try{ 
	            if(rs!=null)	rs.close();
	            if(pstmt!=null)	pstmt.close();
	            if(stmt!=null)	stmt.close();
	            if(conn!=null)	conn.close();
	        }catch(SQLException se2){
	            se2.printStackTrace();
	        }   
		}
		
		return revalList;
	}

	@Override
	public PolVO selectUsermstr(String emp_no) throws Exception {
		return polManageAnalDAO.selectUsermstr(emp_no);
	}	

	private void deleteAnalTable(PolVO polVO) throws Exception {
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("DROP TABLE "+ polVO.getTable_name() + ";");
				sql.append("DELETE FROM \"_HANDY_TABLE_INFO\" ");
				sql.append(" WHERE sn = ?");
				sql.append(" AND table_name = ?;");
				
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setInt(1, polVO.getSqno());
				pstmt.setString(2, polVO.getTable_name());
				pstmt.executeUpdate();
				
				
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
	}
	
	private void deleteDiageTable(PolVO polVO) throws Exception {
		try{			
			DBConnection dbConn = DBConnection.getConnect();
			conn = dbConn.getConn();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("DROP TABLE "+ polVO.getTable_name_diag() + ";");
				//sql.append("DELETE FROM \"_HANDY_TABLE_INFO\" ");
				//sql.append("WHERE sn = ? ");
				//sql.append(" AND table_name = ?;");
				
				pstmt = conn.prepareStatement(sql.toString());
				//pstmt.setInt(1, polVO.getSqno());
				//pstmt.setString(2, polVO.getTable_name_diag());
				pstmt.executeUpdate();
				
				
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
	}
	public void deleteTable(PolVO polVO) throws Exception {
		/*
		try{
			polManageAnalDAO.deleteTable(polVO);
			polManageDAO.deleteTable_diag(polVO);
			polVO.setMsg("테이블이 삭제되었습니다.");
		} catch (Exception e) {
			polVO.setMsg("테이블이 삭제되지 않았습니다.");
		}
		*/
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		this.deleteAnalTable(polVO);
		this.deleteDiageTable(polVO);
		
	}

	@Override
	public void deleteRecode(PolVO polVO) throws Exception {
		/*try{
			polManageAnalDAO.deleteRecode(polVO);
			polVO.setMsg("데이터가 삭제되었습니다.");
		} catch (Exception e) {
			polVO.setMsg("데이터가 삭제되지 않았습니다.");
		}
		
		*/
		//PolManageDAO.deleteRecode
		/**
		 * ibatis SQL Injection 취약점 진단 처리
		 */
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM " + polVO.getTable_name() + " ");
				sql.append("WHERE 1=1 ");
				if(!polVO.getBegin_date().equals("")){
					sql.append("AND to_char(sldm_org_logdate, 'YYYYMMDD')=? ");
				}
				
				pstmt = conn.prepareStatement(sql.toString());
				if(!polVO.getBegin_date().equals("")){
					pstmt.setString(1, polVO.getBegin_date());
				}
				pstmt.executeUpdate();
				
				
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
	}

	
	public int getCountTableName(PolVO polVO) throws Exception {
		return polManageAnalDAO.getCountTableName(polVO);
	}
	
	/**
	 * 테이블 구분값를 리턴한다.
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public String getGubunInfo(String table_name) throws Exception {
		return polManageAnalDAO.getGubunInfo(table_name);
	}

	/**
	 * 취약 로그 insert
	 * @param polVO
	 * @return
	 * @throws Exception
	 */
	public void insertPolWanList(PolVO polVO) throws Exception {
		try{			
			DBConnectionAnal dbConn = DBConnectionAnal.getConnect();
			conn = dbConn.getConnAnal();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO ");
				sql.append(polVO.getGenTableName());
				sql.append("(");
				sql.append(polVO.getColumnList());
				sql.append(") SELECT ");
				sql.append(polVO.getValueList());
				sql.append(" FROM nx_org_user WHERE emp_no not in (SELECT sldm_empno FROM ");
				sql.append(polVO.getGenTableName());
				sql.append(") AND stat='1' ");
				if(!polVO.getGubun2().equals("Y")){
					sql.append("AND is_collabor ='N' ");
				}
				sql.append(" AND emp_no NOT LIKE 'VR%' AND emp_no <> 'admin'");
				pstmt = conn.prepareStatement(sql.toString());
				
				pstmt.executeUpdate();
			}
		}catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ 
                if(rs!=null)	rs.close();
                if(pstmt!=null)	pstmt.close();
                if(stmt!=null)	stmt.close();
                if(conn!=null)	conn.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }            
        }
		
		
	}


}

