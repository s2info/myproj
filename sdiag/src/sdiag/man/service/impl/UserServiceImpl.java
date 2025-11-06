package sdiag.man.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.man.service.SearchVO;
import sdiag.man.service.UserService;
import sdiag.man.service.UserinfoVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("UserService")
public class UserServiceImpl implements UserService{

	@Resource(name = "UserDAO")
	private UserDAO userDao;
	
	/**
	 * 관리계정 조회
	 */
	public HashMap<String,Object> getManUserList(SearchVO searchVO) throws Exception{
		List<UserinfoVO> List = userDao.getManUserList(searchVO);
		int TotalCount = userDao.getManUserListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	
	public List<UserinfoVO> getManAddUserSearchUserList(SearchVO searchVO) throws Exception{
		return userDao.getManAddUserSearchUserList(searchVO);
	}
	
	public UserinfoVO getManUserInfo(String uid) throws Exception{
		return userDao.getManUserInfo(uid);
	}
	
	public int setManUserInsert(UserinfoVO userinfoVO) throws Exception{
		try
		{
			if(userinfoVO.getStat().equals("A")){
				int existCnt = userDao.getManUserExistCheck(userinfoVO);
				
				if(existCnt > 0)
				{
					return 1;
				}
				Object ret = userDao.setManUserInsert(userinfoVO);
			}else{
				int retval = userDao.setManUserUpdate(userinfoVO);
			}
			
			return 0;
			
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 사용자 리스트 Export Excel List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getManUserListForExportExcel(SearchVO searchVO) throws Exception{
		return userDao.getManUserListForExportExcel(searchVO);
	}
	
	/**
	 * 사용자 삭제
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManUserDelete(UserinfoVO userinfoVO) throws Exception{
		return userDao.setManUserDelete(userinfoVO);
	}
	/**
	 * Block user List
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getManBlockuserList(SearchVO searchVO) throws Exception{
		List<HashMap<String, String>> List = userDao.getManBlockuserList(searchVO);
		int TotalCount = userDao.getManBlockuserListTotalCount(searchVO);
		
		HashMap<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("list", List);
		rMap.put("totalCount", TotalCount);
		
		return rMap;
	}
	
	/**
	 * Block User Delete
	 * @param userinfoVO
	 * @return
	 * @throws Exception
	 */
	public int setManBlockUserDelete(String empno) throws Exception{
		return userDao.setManBlockUserDelete(empno);
	}
}
