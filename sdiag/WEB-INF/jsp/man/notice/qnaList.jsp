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
	$("#searchBtn").on("click", function(){
		searchList(1);
	});
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});
	
	$('.btn_view').click(function(){
		var sqno = $(this).attr('sqno');
		
		$('#sq_no').val(sqno);
		document.listForm.action = "<c:url value='/man/qnaView.do'/>";
		document.listForm.submit();
		
	});
	
	$('.btn_update').click(function(){
		var sqno = $(this).attr('sqno');
		$('#sqno').val(sqno);
		$('#flag').val("update");
		
		document.listForm.action = "/man/faqRegisterPage.do";
		document.listForm.submit();
		
	});
	
    $('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            //$('#pageIndex').val(page);
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
    
    $('.btn_search').click(function(){
    	searchList(1);
    });
   
});
	
function searchList(page)
{
	
	document.listForm.pageIndex.value = page;
	document.listForm.action = "/man/qnaList.do";
	document.listForm.submit();
}

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
	<%@ include file="/WEB-INF/jsp/cmm/noticeLeftMenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>Q&A</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:80px">
                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>제목</option>
					<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>내용</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:10%">
					<col style="width:*">
					<col style="width:15%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th>NO.</th>
					<th>제목</th>
					<th>작성일</th>
					<th>작성자</th>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;">
					<td><c:out value="${result.sq_no}" /></td>
					<td style="text-align:left;padding-left:15px;"><a class="btn_view" style="cursor:pointer;" sqno="<c:out value="${result.sq_no}" />"><c:if test="${result.is_new eq 'Y' && result.read_count <= 0 }"><span style="line-height:50px;"><img style="vertical-align: middle;" src="/img/icon_new.png"> </span></c:if>${fn:substring(result.title, 0, 50)}<c:if test="${fn:length(result.title) > 50}" >...</c:if></a></td>
					<td><c:out value="${result.upd_date}" /></td>
					<td><c:out value="${result.upd_user}" /></td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
					<ul id="paging" class="paging"></ul>
				</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite">
            	<a class="btn_black" href='/man/qnaModify.do'><span>등록</span></a>
            </div>
        </div>
       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="sq_no" id="sq_no" value="0" />	
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