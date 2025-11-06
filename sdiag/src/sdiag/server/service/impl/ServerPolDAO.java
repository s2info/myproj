package sdiag.server.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import sdiag.com.service.CodeInfoVO;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.PolVO;
import sdiag.man.service.UserinfoVO;
import sdiag.securityDay.service.SdCheckListVO;
import sdiag.server.service.ServerPolSearchVO;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("ServerPolDAO")
public class ServerPolDAO extends EgovAbstractDAO {

	/**
	 * 서버정책관리 정책 ID 리스트
	 * @param 
	 * @throws Exception
	 */
	public List<EgovMap> getPolIdList() throws Exception {
		List<EgovMap> list = (List<EgovMap>)list("serverDiag.getPolIdList");
		return list;
	}

	

	
}