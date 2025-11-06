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
	
	$('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	        goPage(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});
	
	$(".btn_view").click(function(){
		var orgInfo = $(this).attr('orgInfo');
		var orgType = $(this).attr('orgType');
		var orgNm = $(this).attr('orgNm');
		
		/* var trAdd = "<tr>";
		if(orgType==1) {
			trAdd +="<td>개인</td>";
		} else {
			trAdd +="<td>조직</td>";
		}
		
		trAdd +="<td>"+orgNm+"</td>";
		trAdd +="<td>"+orgInfo+"</td>";
		trAdd +="<td><a id= 'btn_del' class='btn_del' style='cursor:pointer;' ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer;' ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		trAdd +="<input type='hidden' id='orgType' name='orgType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='orgNm' name='orgNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='orgInfo' name='orgInfo' value='"+orgInfo+"' />";
		trAdd +="</tr>";
		$(".groupInfo", opener.document).append(trAdd); */
		
		var reqCode = $("#reqCode").val();
		var gubun = $("#formMod").val();
		var sdCheckNo = $("#qFormMod").val();
		
		opener.$.addRow(orgInfo, orgType, orgNm);
		
		//$(opener.location).attr("href", "javascript:addRow('"+orgInfo+"', '"+orgType+"', '"+orgNm+"');");
		
		/* if(parent.addRow){
			alert("함수");
		}else{
			alert("짜증나!!!!!!!!!!!!!!");
		} */
		 
		//self.close();
	});
	
	
	
	$('.btn_search').click(function(){
		goPage(1);
	});
	
	function goPage(page) {

		document.listForm.pageIndex.value = page;
	    document.listForm.action = "<c:url value='/man/mailTargetSearchPopup.do'/>";
	    document.listForm.method = 'post';
	    document.listForm.submit();
	}
	
	
});



</script>

 

</head>

<body >
<!-- S : contents -->
<div id="cnt" >
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" id="listForm" name="listForm" method="post">
    	<div class="subTT"><span>메일 발송 대상자 조회</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<select  name="searchCondition" id="searchCondition" >
            		<option value="5" <c:if test='${searchVO.searchCondition == "5"}' >selected</c:if>>그룹 명</option>
            		<option value="1" <c:if test='${searchVO.searchCondition == "1"}' >selected</c:if>>사번</option>
					<option value="2" <c:if test='${searchVO.searchCondition == "2"}' >selected</c:if>>성명</option>
					<option value="3" <c:if test='${searchVO.searchCondition == "3"}' >selected</c:if>>조직 명</option>
					<option value="4" <c:if test='${searchVO.searchCondition == "4"}' >selected</c:if>>조직 코드</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:220px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:30%">
					<col style="width:40">
					<col style="width:30">
				</colgroup>
				<tr>
				<c:if test='${searchVO.searchCondition == "1" or searchVO.searchCondition == "2"}' >
					<th>사번</th>
					<th>이름</th>
					<th>부서</th>
				</c:if>
				<c:if test='${searchVO.searchCondition == "3" or searchVO.searchCondition == "4"}' >
					<th>조직 코드</th>
					<th>조직 명</th>
					<th>부서</th>
				</c:if>
				<c:if test='${searchVO.searchCondition == "5"}' >
					<th>그룹 코드</th>
					<th>그룹 명</th>
					<th>그룹 설명</th>
				</c:if>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;" >
					<td>${result.orgInfo }</td>
					<td style="cursor:pointer;" class='btn_view' orgInfo="<c:out value="${result.orgInfo}" />" orgType="<c:out value="${result.orgType}"/>" orgNm="<c:out value="${result.orgNm}" />">${result.orgNm }</td>
					<td>${result.orgPosition }</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
        </div>
       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
       	<input type="hidden" name="formMod" id="formMod" value="${searchVO.formMod }" />
       	<input type="hidden" name="qFormMod" id="qFormMod" value="${searchVO.qFormMod }" />
       	<input type="hidden" name="groupCode" id="groupCode" value="" />
       	<input type="hidden" name="reqCode" id="reqCode" value="${searchVO.reqCode}" />
		</form:form>
    </div>
    <!-- E : center contents -->
</div>
</body>
</html>