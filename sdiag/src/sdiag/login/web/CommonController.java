package sdiag.login.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import sdiag.login.service.UserManageVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controller Abstract 클래스
 * @author  LEECJ
 * @since 2015.10.12
 * @version 1.0
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2015.10.12  LEECJ          최초 생성 
 *  
 *  </pre>
 */
public class CommonController {
	
	private static final long serialVersionUID = 5299950880477325011L;
	
    /** Log Info */
	protected transient final Log log = LogFactory.getLog(this.getClass());
    
	
	private UserManageVO user;

	private String message = "";
	
	private String opScnId = "";
	
	private String title_code = "";
	

	

	public UserManageVO getUser() {
		return user;
	}


	public void setUser(UserManageVO user) {
		this.user = user;
	}


	public String getDateToString(Date date, String format){
        SimpleDateFormat simpleDateFormat = null;
        String returnValue = "";

        if (date != null) {
        	simpleDateFormat = new SimpleDateFormat(format);
            returnValue = simpleDateFormat.format(date);
        }

        return returnValue;		
	}
	

    /**
     * Return Message Text.
     * @return Message Text
     */
    public String getMessage() {
        return message;
    }
    /**
     * Set Message Text.
     * @param message Text
     */
    public void setMessage(String message) {    
    	this.message = message;
    }

    /**
     * 
     * @return String userId
     */
    public String getUserId(HttpServletRequest request) {
    	String userId = "";
//    	user = (UserManageVO)EgovUserDetailsHelper.getAuthenticatedUser();
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getuserid()) ){
			userId = user.getuserid();
	    }
	    else{
	    }
    	
    	return userId;
    }
    
    /**
     * 
     * @return String emp_no
     */
    public String getEmp_no(HttpServletRequest request) {
    	String emp_no = "";
//    	user = (UserManageVO)EgovUserDetailsHelper.getAuthenticatedUser();
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getEmp_no()) ){
			emp_no = user.getEmp_no();
	    }
	    else{
	    }
    	
    	return emp_no;
    } 
    
    /**
     * 
     * @return String title_code
     */
    public String getTitle_code(HttpServletRequest request) {
    	String title_code = "";
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getTitle_code()) ){
			title_code = user.getTitle_code();
	    }
	    else{
	    }
    	
    	return title_code;
    }
    
    public void setTitle_code(String title_code) {    
    	this.title_code = title_code;
    }    
    
    /**
     * 
     * @return String title_code
     */
    public String getTitle_nm(HttpServletRequest request) {
    	String title_nm = "";
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getTitle_nm()) ){
			title_nm = user.getTitle_nm();
	    }
	    else{
	    }
    	
    	return title_nm;
    }
    
    /**
     * 
     * @return String user_auth
     */
    public String getUser_auth(HttpServletRequest request) {
    	String user_auth = "";
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getuser_auth()) ){
			user_auth = user.getuser_auth();
	    }
	    else{
	    }
    	
    	return user_auth;
    } 
    
    /**
     * 
     * @return String org_code
     */
    public String getOrg_code(HttpServletRequest request) {
    	String org_code = "";
		user = (UserManageVO) request.getSession().getAttribute("LoginVO");

	    if( (user != null) && !"".equals(user.getOrg_code()) ){
			org_code = user.getOrg_code();
	    }
	    else{
	    }
    	
    	return org_code;
    } 
    
    /**
     * 
     * @return String deptName
     */
    public String getDeptName(HttpServletRequest request) {
    	String deptName = "";
		user = (UserManageVO) request.getSession().getAttribute("LoginVO3");

	    if( (user != null) && !"".equals(user.getDeptName()) ){
			deptName = user.getDeptName();
	    }
	    else{
	    }
    	
    	return deptName;
    }     
    
}


