package sdiag.man.service;
import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;


public interface UserService {
	public HashMap<String,Object> getManUserList(SearchVO searchVO) throws Exception;
	
	public List<UserinfoVO> getManAddUserSearchUserList(SearchVO searchVO) throws Exception;
	public int setManUserInsert(UserinfoVO userinfoVO) throws Exception;
	public UserinfoVO getManUserInfo(String uid) throws Exception;
	/**
	 * 사용자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getManUserListForExportExcel(SearchVO searchVO) throws Exception;
	/**
	 * 사용자 삭제
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManUserDelete(UserinfoVO userinfoVO) throws Exception;
	/**
	 * Block user List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getManBlockuserList(SearchVO searchVO) throws Exception;
	
	/**
	 * Block User Delete
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManBlockUserDelete(String empno) throws Exception;
	
}
