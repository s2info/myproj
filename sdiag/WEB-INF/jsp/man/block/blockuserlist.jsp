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
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
</style>
<script type="text/javascript" language="javascript">

$(function () {		
	
	$('.btn_reset_block').click(function(){
		if(!confirm('로그인실패 정보를 초기화(삭제) 하시겠습니까?')){
			return false;
		}
		
		var uid = $(this).attr('uid');
		$.ajax({
            data: {uid:uid}, 
            url: "/user/setBlockuserDelete.do",
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
                	searchList($('#pageIndex').val());
                }
                else {
                    alert(data.MSG);
                }

            }

        });		
		
	});
	
	$('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            searchList(page);
        }
    });
	
	$('.btn_search').click(function(){
		searchList(1);
	});
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});
	
	$('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": '<c:out value="${searchVO.searchCondition}" />'});
	    params[1] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": '<c:out value="${searchVO.searchKeyword}" />'});
        gopage('/man/blockuserlistexportexcel.do', params, 'searchVO');
	});
	
	function searchList(page)
	{
		
		document.listForm.pageIndex.value = page;
		document.listForm.action = "/man/blockuserlist.do";
		document.listForm.submit();
	}
	
	$('input:checkbox[name=sel_all]').click(function(){
    	var ischecked = $(this).is(':checked');
    	$('.list_contents_body').find('tr').each(function(){
    		$(this).find('input:checkbox[name=sel_box]').prop('checked', ischecked);
    	});
    });
    
	$('.btn_reset_all').click(function(){
		
		var uidlist=[];
		if($('.list_contents_body').find('input:checkbox[name="sel_box"]:checked').length <= 0){
	       	alert('초기화 계정을 선택하여 주세요.');
	       	return false;
	    }
		
		if(!confirm('선택된 전체 계정의 로그인실패 정보를 초기화(삭제) 하시겠습니까?')){
			return false;
		}
			
		$('.list_contents_body').find('tr').each(function(){
		   	$(this).find('input:checkbox[name="sel_box"]:checked').each(function () {
		    	var uid=$(this).val();
		    	uidlist.push(uid);
	       	});
		});
		
		$.ajax({
            data: {uidlist:uidlist.toString()}, 
            url: "/user/setBlockuserDeleteAll.do",
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
                	searchList($('#pageIndex').val());
                }
                else {
                    alert(data.MSG);
                }

            }

        });		
		
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
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>권한 관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:80px">
                	<option value="UP1" <c:if test="${searchVO.searchCondition == 'UP1'}" >selected</c:if>>ID(사번)</option>
					<option value="UP2" <c:if test="${searchVO.searchCondition == 'UP2'}" >selected</c:if>>성명</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:5%">
					<col style="width:15%">
					<col style="width:*">
					<col style="width:10%">
					<col style="width:13%">
					<col style="width:10%">
					<col style="width:25%">
					<col style="width:10%">
				</colgroup>
				<tr>
					<th><input type='checkbox' name='sel_all' /></th>
					<th>사번</th>
					<th>조직</th>
					<th>성명</th>
					<th>E-mail</th>
					<th>실패회수</th>
					<th>마지막 로그인 요청시간</th>
					<th>차단초기화</th>
				</tr>
				<tbody class='list_contents_body'>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;">
					<td><input type='checkbox' name='sel_box' value='<c:out value="${result.emp_no}" />'/></td>
					<td><c:out value="${result.emp_no}" /></td>
					<td><c:out value="${result.posn_nm}" /></td>
					<td><c:out value="${result.emp_nm}" /></td>
					<td><c:out value="${result.email}" /></td>
					<td><c:out value="${result.fail_count}" />/<c:out value="${result.block_count}" /></td>
					<td><c:out value="${result.updt_date}" /></td>
					<td><a class='btn_scr3 btn_reset_block' uid='<c:out value="${result.emp_no}" />'><span>초기화</span></a></td>
				</tr>
				</c:forEach>
				</tbody>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM--> 
            <div class="btn_borderWrite">
            	<a class='btn_black btn_reset_all' ><span>전체초기화</span></a>
            </div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="selectedRowId" id="selectedRowId" value="<c:out value="${searchVO.selectedRowId}" />" />
		<input type='text' style='display:none;'/>
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div class='DialogBox'></div>
</div>
</body>
</html>