<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<script type="text/javascript" src="/js/jquery.form.js"></script>s
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
 <style type="text/css">
 .view_right ul {width:100%;margin:10px 10px;}
 .input.srh {width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;}
 .view_left {width:30%;height:550px;display:inline-block;float:left;background:#f3f3f3;border:solid 1px #dcdcdc;padding:10px;}
 .view_center {width:15%;display:inline-block;float:left;text-align:center;padding:230px 0 0 20px;margin:0 auto;}
 .view_right {position:relative;width:50%;height:570px;display:inline-block;background:#ffffff;float:right;border:solid 1px #dcdcdc;}
 .view_right ul.inputcell {width:100%;}
</style>
<script type="text/javaScript" language="javascript">
$(function () {	
	$.statInfo = function(){
		 document.listForm.action = "<c:url value='/man/orgStatView.do'/>";
		 document.listForm.submit();
	};
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
    <form:form id="listForm" name="listForm" method="post" >
    	<input type="hidden" id="polCnt" value="${polCnt }"/>
    	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }"/>
    	<input type="hidden" id="statSeq" name="statSeq" value="${searchVO.statSeq }"/>
    	<input type="hidden" id="polIdxList" name="polIdxList" value=""/>
    	<input type="hidden" id="upPolList" name="upPolList" value=""/>
    	<div class="subTT"><span>${statInfo.subject } </span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
				<p>조직 레벨</p> 
                <select  name="orgLevel" id="orgLevel" style="width:100px;" onchange="$.statInfo();">
            		<option value="2" <c:if test='${statInfo.orgLevel == "2"}' >selected</c:if>>2레벨</option>
            		<option value="3" <c:if test='${statInfo.orgLevel == "3"}' >selected</c:if>>3레벨</option>
            		<option value="4" <c:if test='${statInfo.orgLevel == "4"}' >selected</c:if>>4레벨</option>
				</select>
            </li> 
        </div>
        <!-- E :search -->
        <div class="sch_view" style="overflow-x:auto">
            <!-- S :list -->
            <div style="width: ${width}%;min-width: 100%;">
            	<div style="height: auto; -ms-overflow-y: scroll;">
		            <table class="tbl_list1" style="margin-right: 10px;" cellspacing="0" cellpadding="0">
		                <colgroup>
		                	<col style="width:15%;" />
		                    <col style="width:5%;" />
		                    <col style="width:5%;" />
		                    <col style="width:*%;" />
		                    <%-- <c:forEach var="polIdxList" items="${polIdxList}" varStatus="status">
		                    	<col style="width:10%;" />
		                    </c:forEach>
		                    <c:forEach var="upPolIdxList" items="${upPolIdxList}" varStatus="status">
		                    	<col style="width:10%;" />
		                    </c:forEach> --%>
		                </colgroup>
		                <thead>
	                        <tr>
	                            <th>기관</th>
	                            <th>합계</th>
	                             <th>평균</th>
	                            <c:forEach var="polIdxList" items="${polIdxList}" varStatus="status">
	                            	<fmt:parseDate var="parseRegDate" value="${polIdxList.sumRgdtDate }" pattern="yyyyMMdd" />
	                            	<fmt:formatDate var="resultRegDate" value="${parseRegDate }" pattern="yyyy-MM-dd" />
			                    	<th>${polIdxList.polIdxNm }<br /> 기준일 : ${resultRegDate }<br /> 가중치 : ${polIdxList.polWeightValue }</th>
			                    </c:forEach>
			                    <c:forEach var="upPolIdxList" items="${upPolIdxList}" varStatus="status">
			                    	<fmt:parseDate var="parseRegDate2" value="${upPolIdxList.sumRgdtDate }" pattern="yyyyMMdd" />
	                            	<fmt:formatDate var="resultRegDate2" value="${parseRegDate2 }" pattern="yyyy-MM-dd" />
			                    	<th>${upPolIdxList.polIdxNm }<br /> 기준일 : ${resultRegDate2 }<br /> 가중치 : ${upPolIdxList.polWeightValue }</th>
			                    </c:forEach>
	                        </tr>
                    	</thead>
		            </table>
            	</div>
            	
            	<div  style="height:637px;overflow-y:scroll;">
	                <table cellpadding="0" cellspacing="0" class="tbl_list1">
	                    <colgroup>
			                	<col style="width:15%;" />
			                    <col style="width:5%;" />
			                     <col style="width:5%;" />
			                    <col style="width:*%;" />
			                    
			                    <%-- <c:forEach var="polIdxList" items="${polIdxList}" varStatus="status">
			                    	<col style="width:10%;" />
			                    </c:forEach>
			                    <c:forEach var="upPolIdxList" items="${upPolIdxList}" varStatus="status">
			                    	<col style="width:10%;" />
			                    </c:forEach> --%>
			                </colgroup>
	                    <tbody>
	                         ${trList }
	                    </tbody>
                    </table>
                </div>
                </div>
            </div>
            <!-- E :list -->
            <div class="pd10"></div>
            <div class="btn_black2"><a href="/man/statInfoList.do" target="_self" class="btn_black"><span>목록</span></a></div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
        <input type="hidden" name="searchKeyword" id="searchKeyword" value="<c:out value="${searchVO.searchKeyword}" />" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>