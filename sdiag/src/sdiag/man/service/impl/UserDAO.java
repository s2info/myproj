package sdiag.man.service.impl;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sdiag.man.service.DignosisItemVO;
import sdiag.man.service.UserinfoVO;
import sdiag.man.service.SearchVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("UserDAO")
public class UserDAO extends EgovAbstractDAO{
	/**
	 * 관리자 LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<UserinfoVO> getManUserList(SearchVO searchVO) throws Exception{
		List<UserinfoVO> list = (List<UserinfoVO>)list("user.getManUserList", searchVO);
		return list;
	}
	/**
	 * 관리자 TotalCount
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getManUserListTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("user.getManUserListTotalCount", searchVO);
	}
	
	public List<UserinfoVO> getManAddUserSearchUserList(SearchVO searchVO) throws Exception{
		List<UserinfoVO> list = (List<UserinfoVO>)list("user.getManAddUserSearchUserList", searchVO);
		return list;
	}
	public UserinfoVO getManUserInfo(String uid) throws Exception{
		UserinfoVO info = (UserinfoVO)select("user.getManUserInfo", uid);
		return info;
	}
	
	public int setManUserUpdate(UserinfoVO userinfoVO) throws Exception{
		return update("user.setManUserUpdate", userinfoVO);
	}
	
	public int getManUserExistCheck(UserinfoVO userinfoVO) throws Exception{
		return (int)select("user.getManUserExistCheck", userinfoVO);
	}
	public Object setManUserInsert(UserinfoVO userinfoVO) throws Exception{
		return insert("user.setManUserInsert", userinfoVO);
	}
	/**
	 * 사용자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getManUserListForExportExcel(SearchVO searchVO) throws Exception{
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)list("user.getManUserListForExportExcel", searchVO);
		return list;
	}
	/**
	 * 사용자 삭제
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManUserDelete(UserinfoVO userinfoVO) throws Exception{
		return delete("user.setManUserDelete", userinfoVO);
	}
	
	/**
	 * Block User LIST
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getManBlockuserList(SearchVO searchVO) throws Exception{
		List<HashMap<String, String>> list = (List<HashMap<String, String>>)list("user.getManBlockUserList", searchVO);
		return list;
	}
	/**
	 * Block User TotalCount
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int getManBlockuserListTotalCount(SearchVO searchVO) throws Exception{
		return (int)select("user.getManBlockUserListTotalCount", searchVO);
	}
	
	/**
	 * Block User Delete
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManBlockUserDelete(String empno) throws Exception{
		return delete("user.setManBlockUserDelete", empno);
	}
}
