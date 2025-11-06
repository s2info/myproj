<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script type="text/javascript" language="javascript">
$(function () {	
	
	
	$(".btn_view").click(function(){
		$("#op_indc").val($(this).attr('op_indc'));
		
		document.listForm.action = "<c:url value='/exception/exceptionMailForm.do'/>";
		document.listForm.submit();
	});	
	
	function goPage() {

		document.listForm.pageIndex.value = 1;
	    document.listForm.action = "<c:url value='/exception/exceptionMailList.do'/>";
	    document.listForm.method = 'post';
	    document.listForm.submit();
	}
});

</script>

 

</head>
<body>
<!-- S : header -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
<!-- E : header -->
<!-- S : contents -->
<div id="cnt">
	<!-- S : left contents -->
	<div id="cnt_L">
	<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" id="listForm"  name="listForm" method="post">
    	<div class="subTT"><span>메일발송 예외자 관리</span></div>
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:30">
					<col style="width:30">
					<col style="width:30%">
				</colgroup>
				<tr>
					<th>구분 명</th>
					<th>사번 수</th>
					<th>작성일</th>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;" >
					<td style="cursor:pointer;" class='btn_view' op_indc="<c:out value="${result.op_indc}" />">${result.sec_pol_desc }</td>
					<td>${result.cnt }</td>
					<td>${result.rgdt_date }</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
        </div>
       	<input type="hidden" name="op_indc" id="op_indc" value="" />
		<input type='text' style='display:none;' />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>