<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    

<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/jquery.paging.js"></script>

<script language="javascript">
$(function () {
	
	$('.btn_table').click(function(){
		var sqno = $(this).attr('sqno');
		$('#sqno').val(sqno);
		var tbnm = $(this).attr('tbnm');
		$('#tbnm').val(tbnm);		
		document.polForm.action = "/handy/ExanalPolList.do";
		document.polForm.submit();
		
	});
   
});

</script>

<form id="searchVO" name="polForm" action="/handy/ExanalPolList.do" method="post">

<ul class="filetree chart_list" style="display:;" id="orgbrowser">

<c:forEach var="result" items="${resultList}" varStatus="status">
<li class="collapsable lastCollapsable"><a class="btn_table" sqno="<c:out value="${result.sn}" />" style="cursor:pointer;" tbnm="<c:out value="${result.table_name}" />"><c:out value="${result.sn}" /> : <c:out value="${result.table_name}" /></a></li>
</c:forEach>

</ul>

<input type="hidden" name="sqno" id="sqno" value="0" />
<input type="hidden" name="tbnm" id="tbnm" value="" />

</form>
