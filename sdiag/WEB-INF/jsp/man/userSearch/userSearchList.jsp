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
 <style>
/*팝업창배경투명*/
.popTT {padding:30px 10px 0 10px;font:bold 13px '돋움';}
</style>
<script type="text/javascript" language="javascript">

$(function () {		
	
	$.polEx = function(empNo, secPolId){
		$.ajax({
            data: {empNo:empNo,secPolId:secPolId}, 
            url: "/man/polEx.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
            	if(jqXHR.status == 401){
					alert("인증정보가 만료되었습니다.");
					location.href="/";
				}else{
					alert(textStatus + "\r\n" + errorThrown);	
				}
            },
            success: function (data) {
                if (data.ISOK) {
                	searchList();
                }
                else {
                    alert("처리중 오류가 발생하였습니다.");
                }

            }

        });		
		
	};
	
	$.polExDelete = function(empNo, secPolId){
		$.ajax({
            data: {empNo:empNo,secPolId:secPolId}, 
            url: "/man/polExDelete.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
            	if(jqXHR.status == 401){
					alert("인증정보가 만료되었습니다.");
					location.href="/";
				}else{
					alert(textStatus + "\r\n" + errorThrown);	
				}
            },
            success: function (data) {
                if (data.ISOK) {
                	searchList();
                }
                else {
                    alert("처리중 오류가 발생하였습니다.");
                }

            }

        });		
		
	};
	
	$.groupDelete = function(orgInfo, groupCode){
		$.ajax({
            data: {orgInfo:orgInfo, groupCode:groupCode},
            url: "/man/groupDelete.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
            	if(jqXHR.status == 401){
					alert("인증정보가 만료되었습니다.");
					location.href="/";
				}else{
					alert(textStatus + "\r\n" + errorThrown);	
				}
            },
            success: function (data) {
                if (data.ISOK) {
                	searchList();
                }
                else {
                    alert("처리중 오류가 발생하였습니다.");
                }

            }

        });		
		
	};
	

	$('.btn_search').click(function(){
		searchList();
	});
	
	$(".btn_view").click(function(){
		$("#searchKeyword").val($(this).attr('empNo'));
		$("#searchCondition").val("1");
		searchList();
	});
	
	
	function searchList()
	{
		
		document.listForm.action = "/man/userSearchList.do";
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
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>사용자 정보 조회</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:80px">
                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>ID(사번)</option>
					<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>성명</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        
        <c:if test="${searchVO.searchCondition eq '2'}">
        <div class='popTT'><img src='/img/icon_arw4.jpg' /> 사용자 목록</div>
        <p><span> ※ 상세 조회 할 사용자의 사번을 클릭 하세요. ※</span></p>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:15%">
					<col style="width:15%">
					<col style="width:*">
				</colgroup>
				<tr>
					<th>ID(사번)</th>
					<th>성명</th>
					<th>조직</th>
				</tr>
				<c:forEach var="result" items="${userList}" varStatus="status">
				<tr>
					<td style="cursor:pointer" class='btn_view' empNo="<c:out value="${result.empNo}" />"><c:out value="${result.empNo}" /></td>
					<td><c:out value="${result.empNm}" /></td>
					<td><c:out value="${result.posnNm}" /></td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
        </div>
        </c:if>
        
        <c:if test="${searchVO.searchCondition eq '1'}">
        <!-- E :search -->
         <div class='popTT'><img src='/img/icon_arw4.jpg' /> 사용자 대상 정책</div>
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:*">
					<col style="width:20%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th>ID(사번)</th>
					<th>성명</th>
					<th>조직</th>
					<th>정책명</th>
					<th>정책 예외</th>
				</tr>
				<c:if test="${empty userPolList}">
               		<tr style="height:100px;">
               			<td style="text-align: center;" colspan="5">
               			 대상 정책이 없습니다.
               			</td>
               		</tr>
		        </c:if>
				<c:forEach var="userPolList" items="${userPolList}" varStatus="status">
				<tr>
					<td><c:out value="${userPolList.empNo}" /></td>
					<td><c:out value="${userPolList.empNm}" /></td>
					<td><c:out value="${userPolList.posnNm}" /></td>
					<td><c:out value="${userPolList.secPolDesc}" /></td>
					<td><a onclick="$.polEx('${userPolList.empNo}', '${userPolList.secPolId}')"    class="btn_black"><span>정책 예외</span></a></td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
        </div>
        
        <div class='popTT'><img src='/img/icon_arw4.jpg' /> 사용자 예외 정책</div>
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:*">
					<col style="width:20%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th>ID(사번)</th>
					<th>성명</th>
					<th>조직</th>
					<th>정책명</th>
					<th>정책 예외 해제</th>
				</tr>
				<c:if test="${empty userExPolList}">
               		<tr style="height:100px;">
               			<td style="text-align: center;" colspan="5">
               			 예외 정책이 없습니다.
               			</td>
               		</tr>
		        </c:if>
				<c:forEach var="userExPolList" items="${userExPolList}" varStatus="status">
				<tr>
					<td><c:out value="${userExPolList.empNo}" /></td>
					<td><c:out value="${userExPolList.empNm}" /></td>
					<td><c:out value="${userExPolList.posnNm}" /></td>
					<td><c:out value="${userExPolList.secPolDesc}" /></td>
					<td><a onclick="$.polExDelete('${userExPolList.empNo}', '${userExPolList.secPolId}')"    class="btn_black"><span>정책 예외 해제</span></a></td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
        </div>
        
         <div class='popTT'><img src='/img/icon_arw4.jpg' /> 사용자 포함 그룹</div>
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:*">
					<col style="width:20%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th>그룹 명</th>
					<th>대상자 코드</th>
					<th>대상자 명</th>
					<th>대상 구분</th>
					<th>그룹 제외</th>
				</tr>
				<c:if test="${empty userGroupList}">
               		<tr style="height:100px;">
               			<td style="text-align: center;" colspan="5">
               			포함된 그룹이 없습니다.
               			</td>
               		</tr>
		        </c:if>
				<c:forEach var="userGroupList" items="${userGroupList}" varStatus="status">
				<tr>
					<td><c:out value="${userGroupList.groupNm}" /></td>
					<td><c:out value="${userGroupList.orgInfo}" /></td>
					<td><c:out value="${userGroupList.orgNm}" /></td>
					<td><c:out value="${userGroupList.orgType}" /></td>
					<td>
					<c:if test="${userGroupList.orgType eq '개인'}">
						<a onclick="$.groupDelete('${userGroupList.orgInfo}', '${userGroupList.groupCode}')"    class="btn_black"><span>그룹 제외</span></a>
					</c:if>
					</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
        </div>
        </c:if>
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>