package sdiag.server.service.impl;

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

import sdiag.com.service.CodeInfoVO;
import sdiag.dash.service.DashboardService;
import sdiag.dash.service.UserIdxInfoCurrVO;
import sdiag.exanal.service.ExanalManageService;
import sdiag.exanal.service.Pol;
import sdiag.exanal.service.PolVO;
import sdiag.getdata.service.DBConnection;
import sdiag.getdata.service.DBConnectionAnal;
import sdiag.server.service.ServerPolSearchVO;
import sdiag.server.service.ServerPolService;
import sdiag.server.service.ServerPolVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Service("ServerPolService")
public class ServerPolServiceImpl implements ServerPolService {

    @Resource(name="ServerPolDAO")
    private ServerPolDAO serverPolDAO;
    
	@Resource(name="ServerPolAnalDAO")
	private ServerPolAnalDAO serverPolAnalDAO;

	private Connection conn = null;		
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 서버정책관리 정책 리스트
	 * @param polIdList
	 * @throws Exception
	 */
	
	public ServerPolSearchVO getServerPolList(ServerPolSearchVO searchVO) throws Exception {
		return serverPolAnalDAO.getServerPolList(searchVO);
	}

	/**
	 * 서버이행관리 정책 ID List.
	 * @param 
	 * @throws Exception
	 */
	
	public List<EgovMap> getPolIdList() throws Exception {
		return serverPolDAO.getPolIdList();
	}

	/**
	 * 로그 List.
	 * @param 
	 * @throws Exception
	 */
	public List<EgovMap> getlogList(ServerPolSearchVO searchVO)
			throws Exception {
		return serverPolAnalDAO.getlogList(searchVO);
	}

	/**
	 * 컬럼 List.
	 * @param searchVO
	 * @throws Exception
	 */
	public List<EgovMap> getColumnList(ServerPolSearchVO searchVO)
			throws Exception {
		return serverPolAnalDAO.getColumnList(searchVO);
	}

	/**
	 * 로그 정보 업데이트
	 * @param serverPolVO
	 * @throws Exception
	 */
	public void setPolLogUpdate(ServerPolVO serverPolVO) throws Exception {
		serverPolAnalDAO.setPolLogUpdate(serverPolVO);
		
	}

	/**
	 * 로그 날짜 리스트
	 * @param searchVO
	 * @throws Exception
	 */
	public List<EgovMap> getDateList(ServerPolSearchVO searchVO)
			throws Exception {
		return serverPolAnalDAO.getDateList(searchVO);
	}

	/**
	 * 로그 정보 업데이트_관리자 모드
	 * @param serverPolInfo
	 * @return
	 * @throws Exception
	 */
	public void setPolLogUpdateAdmin(ServerPolVO serverPolInfo) {
		serverPolAnalDAO.setPolLogUpdateAdmin(serverPolInfo);
		
	}


}

