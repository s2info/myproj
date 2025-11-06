<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script type="text/javascript" language="javascript">

$(function () {	
	
	$('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	        searchList(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});

});

function searchList(page)
{
	
	document.checkFrm.pageIndex.value = page;
	document.checkFrm.action = "/pop/expnListPopUplist.do";
	document.checkFrm.submit();
}

function checkPopClose(){
	//$("#checkPOP").dialog("close");
	window.close();
}
</script>
</head>
	<body>
		<div id="wrap_pop">
		<!-- Contents Begin -->	
		<form name="checkFrm" id="checkFrm" method="post">
		<div id="wrap_contents">	
			<!-- 타이틀 -->
			<div class="WP_tit">
				<ul>
					<li class="WPT_ic"><img src="/img/ic_stit2.png" alt="아이콘"></li>
				<li class="WPT_txt">예외자 추가</li>
				</ul>			
			</div>
			<!-- 타이틀 end-->
			
		<input type="hidden" name="EMPNO" id="EMPNO" value="<%=request.getParameter("emp_no") %>" />
			
			<div class="marT10" style="border:1px solid red; width: 100%">
					<table border="0" class="TBS3" cellpadding="0" cellspacing="0" summary="예외자추가">
						<caption>예외자추가</caption>
						<colgroup>
						<col style="width:*">
						<col style="width:20%">
						<col style="width:10%">
						<col style="width:10%">
						</colgroup>
						<tr>
							<th>조직명</th>
							<th>사번</th>
							<th>이름</th>
							<th>선택</th>
						</tr>
						<c:forEach var="result" items="${resultList}" varStatus="status">
							<tr style="font-weight:bold;">
							<!-- 
								<td><c:out value="${result.org_nm_1 }" /></td>
								<td><c:out value="${result.org_nm_3 }" /></td>
								<td><c:out value="${result.org_nm_4 }" /></td>
							 -->
								<td style="text-align:left;padding-left:10px;"><a class="btn_view" style="cursor:pointer;" emp_no="<c:out value="${result.org_nm_2}" />"><c:out value="${result.org_nm_2}" /></a></td>
								<td><c:out value="${result.emp_no }" /></td>
								<td><c:out value="${result.emp_nm }" /></td>
								<td><input type="checkbox" name="EXPNNO" value="${result.emp_no }"/></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="marT10"></div>
				<div style="display:table-cell;vertical-align:middle;width:1280px;height:60px;text-align:center;">
				<div class="number1">
					<ul id="paging"></ul>
				</div>
				
				<div class="margin_t"></div>
					<div style='width:150px;margin:0 auto;'>
						<div class='btn4'><a href="javascript:expnPopAdd();" style="cursor:pointer;">저장</a></div>
						<div class='btn4'><a href="javascript:checkPopClose();">닫기</a></div>
					</div>
				</div>
				
		</div>
		<input type="text" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="text" name="emp_no" id="emp_no" value="0">
		</form>
		
		</div>
		
		<div id="checkPOP"></div>
	</body>
<script type="text/javascript">
function expnPopAdd(){
	
	// 유효성 체크
	if(!$("input:checkbox[name='EXPNNO']").is(":checked")){
		alert(" 저장할 항목을 체크하십시오.");
		return ;
	}
	// 자료실 DB등록
	else if( confirm(" 저장 하시겠습니까?" ) ){
		document.listForm.action = "/user/expnInsert.do";
		document.listForm.submit();
	}
}
</script>
</html>