package sdiag.sample.dao;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("sampleOtherDAO")
public class SampleOtherDAO extends EgovAbstractDAO{

//	@Resource(name="OtherSqlMapClient")
//    public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
//        super.setSuperSqlMapClient(sqlMapClient);
//    }
//	
//	public int test(){
//		return (int) select("otherSample.test");
//	}
}
