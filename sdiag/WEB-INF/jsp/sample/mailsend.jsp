<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />
<script type="text/javascript" language="javascript">

$(function () {
	
	
	$('.btn_send_mail').click(function()
	{
		if($('input:text[name=tomail]').val()==''){
			alert('수신자를 입력하여 주세요.');
			return false;
		}
		
		if($('input:text[name=subject]').val()==''){
			alert('제목을 입력하여 주세요.');
			return false;
		}
		
		if($('#contents').val()==''){
			alert('수신자를 입력하여 주세요.');
			return false;
		}
		
		document.listForm.action = "<c:url value='/sample/sendmailprocess.do'/>";
		document.listForm.submit();
	});
});
</script>
<body>
	<div id="wrap_body">
		
		
		<div id="wrap_contents">
		<!-- Contents Begin -->
		<form:form commandName="mailInfoVO" name="listForm" method="post">
			<div class="SB1_tit">
				<ul>
					<li><img src="/img/ic_tit1.png" alt="타이틀"></li>
					<li class="SB1T_txt">메일 전송 </li>
				</ul>
			</div>
			<div class="contents">
				
				<div class="SC_area">
					<table border="0" class="TBS4" cellpadding=0 cellspacing=0 summary="공지사항">
						<caption>공지사항</caption>
						<colgroup>
							<col style="width:20%">
							<col style="width:*">
						</colgroup>
						<tr>
							<th>사번</th>
							<td><input type='text' name='emp_no' style='width:300px;' value='admin' /></td>
						</tr>
						<tr>
							<th>수신자</th>
							<td><input type='text' name='toMailAddress' style='width:300px;' value='91117655@ktfriend.com' /></td>
						</tr>
						<tr>
							<th>제목</th>
							<td><input type='text' name='subject' style='width:300px;'/></td>
						</tr>
						<tr>
							<th>내용</th>
							<td><textarea name='contents' id='contents' style='width:300px;height:300px;'></textarea> </td>
						</tr>
								
					</table>
				</div>
				
				
				<div class="btn3"><a class='btn_send_mail'>보내기</a></div>
			</div>
						
		</form:form>
		
		</div>
		<!-- Contents End -->
		
		<div class='DialogBox'></div>
	</div>


	
</body>
</html>