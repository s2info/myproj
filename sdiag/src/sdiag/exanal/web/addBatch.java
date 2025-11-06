package sdiag.exanal.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
public class addBatch {
 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
         
         /*
        Connection con = null;
        PreparedStatement pstmt = null ;
         
        String sql = "Insert Into _hart_test(uid, name, age) Values(?, ?, ?)" ;
         
         
        try{
            Class.forName("core.log.jdbc.driver.PostgresqlDriver");
            con = DriverManager.getConnection(" jdbc:postgresql://10.225.148.195:5432/diagdb", "diag", "diag");
             
             
            pstmt = con.prepareStatement(sql) ;
             
             
            for(int i=0; i < 100000; i++){
                int uid = 10000 + i ;
                String name = "홍길동_" + Integer.toString(i) ;
                int age = i ;
             
                pstmt.setInt(1, uid);
                pstmt.setString(2, name) ;
                pstmt.setInt(3, age);
                 
                // addBatch에 담기
                pstmt.addBatch();
                 
                // 파라미터 Clear
                pstmt.clearParameters() ;
                 
                 
                // OutOfMemory를 고려하여 만건 단위로 커밋
                if( (i % 10000) == 0){
                     
                    // Batch 실행
                    pstmt.executeBatch() ;
                     
                    // Batch 초기화
                    pstmt.clearBatch();
                     
                    // 커밋
                    con.commit() ;
                     
                }
            }
             
             
            // 커밋되지 못한 나머지 구문에 대하여 커밋
            pstmt.executeBatch() ;
            con.commit() ;
             
        }catch(Exception e){
            e.printStackTrace();
             
            try {
                con.rollback() ;
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
             
        }finally{
            if (pstmt != null) try {pstmt.close();pstmt = null;} catch(SQLException ex){}
            if (con != null) try {con.close();con = null;} catch(SQLException ex){}
        }
 */
    }
 
}