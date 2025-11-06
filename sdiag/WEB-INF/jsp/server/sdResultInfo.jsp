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

<script type="text/javaScript" language="javascript">
$(function () {	
	/* $('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	    	searchList(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	}); */
	
	$('.btn_search').click(function(){
		searchList(1);
	});
	
	$.fileDown = function(fileNm){
		$("#fileNm").val(fileNm);
		
	};
	function searchList(page) {

		//document.listForm.pageIndex.value = page;
	    document.listForm.action = "<c:url value='/securityDay/sdResultInfo.do'/>";
	    document.listForm.submit();
	}
	
	$('.btn_export_excel').click(function(){
		 var contents_item = [];
		  $('.tbl_list1').find('tr').each(function(){
			 var row_item = ""; 
			 $(this).find('td, th').each(function(){
				 row_item += $(this).text().replace(/,/g,"/") + "|";
			 });
			 contents_item.push(row_item);
		  });
		  var params = [];
			params[0] = JSON.stringify({"inputname": 'sUseYn', "inputvalue": $('#sUseYn').val()});
		    params[1] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": $('#searchCondition').val()});
		    params[2] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": $('#searchKeyword').val()});
		    params[3] = JSON.stringify({"inputname": 's_clGroupNo', "inputvalue": $('#s_clGroupNo').val()});
		    params[4] = JSON.stringify({"inputname": 'clGroupNo', "inputvalue": $('#clGroupNo').val()});
		    params[5] = JSON.stringify({"inputname": 'bodyData', "inputvalue": contents_item.toString()});
	      	gopage('/securityDay/exportexcel.do', params, 'searchVO');
		  
	   });
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
		<form:form commandName="searchVO" id="listForm" name="listForm" method="post">
    	<div class="subTT"><span>점검 결과 상세 조회</span></div>
    	
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
        		<p>점검 여부</p> 
            	<select  name="sUseYn" id="sUseYn" >
            		<option value="">점검 여부</option>
                	<option value="Y" <c:if test='${searchVO.sUseYn == "Y"}' >selected</c:if>>점검 완료</option>
					<option value="N" <c:if test='${searchVO.sUseYn == "N"}' >selected</c:if>>미 점검</option> 
				</select>
				<p>검색어</p>
				<select  name="searchCondition" id="searchCondition" style="width:80px">
                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>사번</option>
					<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>이름</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:220px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
                &nbsp;&nbsp;&nbsp;<a class="btn_black btn_export_excel" style=""><span>Excel</span></a>
            </li> 
        </div>
        <!-- E :search -->
        <div class='popTT'><img src='/img/icon_arw4.jpg' /> ${sdCheckListGroupInfo.clGroupNm} 점검 결과 정보</div>
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:5%">
					<col style="width:5%">
					<col style="width:20%">
					<col style="width:20%">
					<col style="width:5%">
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:10%">
					<col style="width:10%">
				</colgroup>
				<tr>
					<th>사번</th>
					<th>이름</th>
					<th>부서</th>
					<th>점검표명</th>
					<th>문항 번호</th>
					<th>답번</th>
					<th>파일 명</th>
					<th>점검여부</th>
					<th>점검일</th>
				</tr>
				<c:if test="${empty sdResultList}">
               		<tr style="height:100px;">
               			<td style="text-align: center;" colspan="9">
               			점검 대상이 없습니다.
               			</td>
               		</tr>
		        </c:if>
				
				<c:forEach var="result" items="${sdResultList}" varStatus="status">
				<tr style="font-weight:bold;" >
					<td>${result.empNo }</td>
					<td>${result.empNm }</td>
					<td>${result.posnNm }</td>
					<td>${result.checklistNm }</td>
					<td>${result.questionNum }</td>
					<td>${result.answer }</td>
					<td><a href="/securityDay/fileDown.do?sdCheckNo=${result.sdCheckNo }&clGroupNo=${sdCheckListGroupInfo.clGroupNo}&questionNum=${result.questionNum}&empNo=${result.empNo}" target="_self">${result.fileNm }</a></td>
					<td>${result.checkYn }</td>
					<td>${result.checkDate }</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
             <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite" style="padding-top: 10px;">
            	<a class="btn_black" href="/securityDay/sdCheckListGroupList.do"><span>목록</span></a>
            </div>
        </div>
       	<input type="hidden" name="s_clGroupNo" id="s_clGroupNo" value="${searchVO.s_clGroupNo}" />
       	<input type="hidden" name="clGroupNo" id="clGroupNo" value="${searchVO.s_clGroupNo}" />
       	<input type="hidden" name="fileNm" id="fileNm" value="" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>