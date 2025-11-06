<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>KT임직원수준진단</title>
<link rel="stylesheet" type="text/css" href="/css/common2.css" />
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<style>

</style>
<script type="text/javaScript" language="javascript">
$(document).ready(function () {	
	
	$("#content").each(function(){
		
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	});
	
	function setCookie(name, value, expiredays){
		var todayDate = new Date();
		todayDate.setDate(todayDate.getDate()+expiredays);
		//alert(todayDate.toGMTString());
		document.cookie= name + "=" + escape(value) + ";path=/; expires=" + todayDate.toGMTString() +";" ;
	}
	
	$("#check").click(function(){
		if(document.noticePopup.check.checked){
			setCookie("name"+$("#sqNo").val(), "done", 1);
		}
		self.close();
	});
});
</script>
</head>

<body>
<form name="noticePopup">
    <!-- S : 상세 내용 및 조치 방안 팝업 -->
    <input id="sqNo" type="hidden" value="${borderInfo.sq_no}" />
    <div id="content" style="padding: 10px;">
         ${borderInfo.contents}
    </div>
    <!-- E : 상세 내용 및 조치 방안 팝업 -->
    <div style="padding: 10px;">
    	<input  type="checkbox" name="check" id ="check" onfocus="this.blur()"/> 오늘하루 열지 않기
    </div>
    </form>
</body>
</html>
