<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<title>알수없는 오류</title>
<link rel="stylesheet" type="text/css" href="/css/login.css">
</head>


<body>
<div id="etc_header"><h1>KT임직원 보안수준진단</h1></div>
<div class="etc_area">
    <div class="etc_block">
    	<img src="/img/img_error_1.png" >
    	<h2>알 수 없는 오류!</h2>
        <!-- S : login -->
        <div class="etc_box">
            <li>웹서버에서 알 수 없는 오류가 발생하였습니다.<br>
				<%-- 에러타입 : <%=exception.getClass().getName() %><br>
				메세지 :  <%=exception.getMessage() %> --%>
				
				<br />감사합니다.
            </li>
            <li class="cell1">
            	<a href="/" class="btn_etc"><span>홈으로</span></a> 
            	<a href="javascript:history.back(-1);" class="btn_etc"><span>이전</span></a>
            </li>
        </div>
        <!-- E : login -->
    </div>
</div>
<div class="footer">Copyright ⓒ <span>KT</span> All rights reserved.</div>
</body>
</html>

