<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/js/Slider/jssor.utils.js"></script>
<script type="text/javascript">

$(function () {		
	$('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'polcode', "inputvalue": '<c:out value="${searchVO.polcode}" />'});
	    params[1] = JSON.stringify({"inputname": 'empno', "inputvalue": '<c:out value="${searchVO.empno}" />'});
	    params[2] = JSON.stringify({"inputname": 'begindate', "inputvalue": '<c:out value="${searchVO.begindate}" />'});
	    params[3] = JSON.stringify({"inputname": 'mac', "inputvalue": '<c:out value="${searchVO.mac}" />'});
        gopage('/pol/logdetailviewexcel.do', params, 'form1');
	});
});
</script>
    
</head>
<body>
<div style="width:100%">
      <div class="subTT" style="width:100%;"><span>상세로그</span></div>
      	<div class="pd10"></div>
        <div class="widelist" style="width:100%;">
    	<table border="0" class="tblInfoType4" cellpadding=0 cellspacing=0>
			${logBody }
		</table>
   		 </div>
    <div class="btn_black2" style='width:99%;padding-right:20px;'>
    	<c:if test="${logCnt > 0}" >
    	<a class='btn_black btn_export_excel' ><span>Excel</span></a>
    	</c:if>
    	<a href="javascript:self.close();" class="btn_black"><span>닫기</span></a>
    </div> 
          
</div>
</body>

</html>