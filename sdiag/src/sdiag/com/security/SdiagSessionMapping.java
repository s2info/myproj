package sdiag.com.security;

import egovframework.rte.fdl.security.userdetails.jdbc.EgovUsersByUsernameMapping;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import egovframework.rte.fdl.security.userdetails.EgovUserDetails;
import sdiag.member.service.MemberVO;

public class SdiagSessionMapping extends EgovUsersByUsernameMapping{
	public SdiagSessionMapping(DataSource ds, String usersByUsernameQuery) {
		super(ds, usersByUsernameQuery);
    }
	
	@Override
    protected EgovUserDetails mapRow(ResultSet rs, int rownum) throws SQLException {
    	logger.debug("## Sdiag UsersByUsernameMapping mapRow ##");
    	
        boolean strEnabled  = rs.getBoolean("ENABLED");
       
        String strUsrId = rs.getString("USER_ID");
        String strUsrNm    = rs.getString("USER_NM");
        String strPassword = rs.getString("PASSWORD");
        String strUsrEmail = rs.getString("USER_EMAIL");
        String strTelNo = rs.getString("TEL_NO");
       // String strMobileNo = rs.getString("MOBLPHON_NO");
       // String strUsrZip = rs.getString("USER_ZIP");
       // String strUsrAdres = rs.getString("USER_ADRES");
       // String strDetailAdres = rs.getString("USER_DETAIL_ADRES");
        String strAuthorCode= rs.getString("AUTHOR_CODE");
        String strAuthNum = rs.getString("AUTHOR_CD");
        String strTitleCode = rs.getString("TITLE_CODE");
        String strOrgCode = rs.getString("ORG_CODE");
        String strIp = rs.getString("IP");
        String strMac = rs.getString("MAC");
        String strTitleName = rs.getString("TITLE_NAME");
        String strLevelCode = rs.getString("LEVEL_CODE");
        String strLevelName = rs.getString("LEVEL_NAME");
        String strIsEmail = rs.getString("IS_EMAIL");
        String strEmp = rs.getString("IS_EMP");
        // 세션 항목 설정
        String strIsProxy = rs.getString("IS_PROXY");
        String strIsproxyDirector = rs.getString("IS_PROXY_DIRECTOR");
        MemberVO loginVO = new MemberVO();

        loginVO.setUserid(strUsrId);
        loginVO.setPassword(strPassword);
        loginVO.setUsername(strUsrNm);
        loginVO.setEmail(strUsrEmail);
        loginVO.setTelno(strTelNo);
        loginVO.setRole_info(strAuthorCode);
        loginVO.setRole_code(strAuthNum);
        loginVO.setTitlecode(strTitleCode);
        loginVO.setOrgcode(strOrgCode);
        loginVO.setIp(strIp);
        loginVO.setMac(strMac);
        loginVO.setTitlename(strTitleName);
        loginVO.setLevelcode(strLevelCode);
        loginVO.setLevelname(strLevelName);
        loginVO.setIsmail(strIsEmail);
        loginVO.setIsEmp(strEmp);
        loginVO.setIsProxy(strIsProxy);
        loginVO.setIsProxyDirector(strIsproxyDirector);
        return  new EgovUserDetails(strUsrId, strPassword, strEnabled, loginVO);
        
    }
}
