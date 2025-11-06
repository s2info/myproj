<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>

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

	
	$('.list_contents_body').on('click', '.btn_log_view', function(){
    	//alert($(this).attr('polcd'));
    	var pcd = $(this).attr('polcd');
    	var emp = $(this).attr('empno');
    	var bdt = $(this).attr('bdt');
    	var mac = $(this).attr('mac');
    	var pwlog = window.open('/pol/polLogdetailview.do?polcd='+ $(this).attr('polcd') +'&empno='+ emp + '&begindate='+ bdt +'&mac='+ mac , 'logview', 'width=1300, height=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no');
        if (pwlog != null)
            pwlog.focus();
    });
	
	$('.btn_export_excel').click(function(){
		document.listForm.action = "/dash/detailLogViewPopupExportExcel.do";
		document.listForm.submit();
	})
});

function searchList(page)
{
	
	document.listForm.pageIndex.value = page;
	document.listForm.action = "/dash/detailLogViewPopup.do";
	document.listForm.submit();
}


</script>
</head>

<body>
    <!-- S : 상세 내용 및 조치 방안 팝업 -->
   
        <div class="popup_ly" style="width:100%;min-width:600px;padding-right: 0px;">
        	<!-- S : center contents -->
		    <div id="cnt_R" style="width:98%;border:0px solid red;margin:10px 10px 10px 10px;">
		    <form:form commandName="searchVO" name="listForm" method="post">
		    	<div class="subTT"><span>지수화 정책진단 상세 - 수집건수 1건 이상 조회</span></div>
		    	<!-- S :search -->
		        <%-- <div class="sch_block3">
		        	<li>
		            	<p>검색어</p> 
		            	<select  name="searchCondition" id="searchCondition" style="width:80px">
		                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>제목</option>
							<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>내용</option> 
						</select>
		                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
		                <a class="btn_black btn_search"><span>검색</span></a>
		            </li> 
		        </div> --%>
		        <!-- E :search -->
		    	 <div class="sch_view">
		            <!-- S :list -->
		            <table cellpadding="0" cellspacing="0" class="tbl_list1">
		                <caption>TBL</caption>
						<colgroup>
							<col style="width:12%">
							<col style="width:14%">
							<col style="width:10%">
							<col style="width:12%">
							<col style="width:14%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:8%">
						</colgroup>
						<tr>
							<th>수집날짜</th>
							<th>부서명</th>
							<th>성명</th>
							<th>이벤트날짜</th>
							<th>지수화정책</th>
							<th>건수</th>
							<th>점수</th>
							<th>진단내역</th>
							<th>상세로그</th>
						</tr>
						<tbody class='list_contents_body'>
						<c:forEach var="result" items="${resultList}" varStatus="status">
							<tr >
								<td><c:out value="${result.regdate}" /></td>
								<td><c:out value="${result.orgnm}" /></td>
								<td><c:out value="${result.empnm}" /></td>
								<td><c:out value="${result.eventdate}" /></td>
								<td><c:out value="${result.polidxname}" /></td>
								<td><c:out value="${result.count}" /></td>
								<td><c:out value="${result.score}" /></td>
								<td><c:out value="${result.scorestatname}" /></td>
								<td><a style="cursor:pointer;" class='btn_details btn_log_view' polcd="<c:out value="${result.polidxid}" />" empno="<c:out value="${result.empno}" />" bdt="${searchVO.begin_date }" mac="<c:out value="${result.mac}" />" ><span>상세로그</span></a></td>
							</tr>
						</c:forEach>
						</tbody>
		            </table>
		            
		        
		            <!-- E :list -->
		            <!-- S : page NUM -->
		            <div class="pagingArea1 pagingPadd1">
							<ul id="paging" class="paging"></ul>
						</div>
		            <!-- E : page NUM -->
		            <div class="btn_borderWrite">
		            	<a class='btn_black btn_export_excel' ><span>Excel</span></a>
		            	<a class="btn_black" href='javascript:window.close(0);'><span>닫기</span></a>
		            </div>
		            
		        </div>
		      	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
				<input type="hidden" name="param" id="param" value="<%=request.getParameter("param") %>" />
				
				</form:form>
		    </div>
		    <!-- E : center contents -->      
    	</div>
    <!-- E : 상세 내용 및 조치 방안 팝업 -->
</body>
</html>
