package sdiag.getdata.service.impl;

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


import org.springframework.stereotype.Service;

import sdiag.getdata.service.DBConnection;
import sdiag.getdata.service.GetDataService;

@Service("GetDataService")
public class PolServiceImpl implements GetDataService {
	private Connection conn = null;		
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 정책별 상세로그 조회
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public List<LinkedHashMap<String,Object>> getPolDetailLogForDateNUser(HashMap hMap) throws Exception{
		
		List<LinkedHashMap<String, Object>> revalList = new ArrayList<LinkedHashMap<String, Object>>();
		try{
			DBConnection dbConn = DBConnection.getConnect();
			conn = dbConn.getConn();
			
			if (conn != null){
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT T.sldm_empno, U.emp_nm, G.org_nm,");
				sql.append(hMap.get("columnsname"));
				sql.append(" FROM " + hMap.get("tblname") + " T ");
				sql.append("LEFT JOIN nx_org_user U ON T.sldm_empno=U.emp_no ");
				sql.append("LEFT JOIN nx_org_group G ON U.org_code=G.org_code ");
				sql.append("WHERE 1=1 ");
				sql.append("AND sldm_policy_id=? ");
				sql.append("AND sldm_empno=? ");
				sql.append("AND to_char(sldm_org_logdate, 'YYYYMMDD')=? ");
				if(!hMap.get("mac").equals("")){
					sql.append(" AND sldm_mac=?");	
				}
				if(!hMap.get("dFlag").equals("")){
					sql.append(hMap.get("dFlag"));
				}
				
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, hMap.get("polcode").toString());
				pstmt.setString(2, hMap.get("empno").toString());
				pstmt.setString(3, hMap.get("begindate").toString());
				if(!hMap.get("mac").equals("")){
					pstmt.setString(4, hMap.get("mac").toString());
				}
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				while(rs.next()){
					LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
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
	
}
