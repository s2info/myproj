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
	
	$('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
            var $ddata = $(".DialogBox");
            if($ddata.data('is_save') == 'Y'){
            	searchList(1);
            }
        },
        open: function () {
            var $ddata = $(".DialogBox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	});
	
	$('.btn_user_add, .btn_user_modify').click(function(){
		var uid = $(this).attr('uid');
		var widthVal = uid == '' ? 880 : 490;
		var mode = uid=='' ? 'A' : 'M';
		if(mode == 'M'){
			$('#selectedRowId').val($(this).attr('uid'));
			var selrow = $(this).closest('tr');
			SelectedRowChange(selrow);
		}
		$.ajax({
            data: {mode:mode,uid:uid}, 
            url: "/user/getManUseraddPopup.do",
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
                	 $('.DialogBox').html(data.popup_body);
                     $('.DialogBox').dialog({ width: widthVal, height: 570 });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
                     
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
            //$('#pageIndex').val(page);
            searchList(page);
            //searchList(page, $('#searchTxt').val());
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
        gopage('/man/userlistexportexcel.do', params, 'searchVO');
	});
	
	function searchList(page)
	{
		
		document.listForm.pageIndex.value = page;
		document.listForm.action = "/man/userlist.do";
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
    	<div class="subTT"><span>권한 관리</span></div>
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
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:15%">
					<col style="width:*">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th>ID(사번)</th>
					<th>조직</th>
					<th>성명</th>
					<th>권한</th>
					<th>사용여부</th>
					<th>E-mail</th>
					<th>IP</th>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;cursor:pointer" class='btn_user_modify' uid="<c:out value="${result.id}" />" <c:if test="${searchVO.selectedRowId == result.id}" >class='SelRow'</c:if>>
					<td><c:out value="${result.id}" /></td>
					<td><c:out value="${result.posn_nm}" /></td>
					<td><c:out value="${result.emp_nm}" /></td>
					<td><c:out value="${result.user_auth}" /></td>
					<td><c:out value="${result.isused}" /></td>
					<td><c:out value="${result.email}" /></td>
					<td>${result.ip}</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite">
            	<c:if test="${totalCnt > 0 }"><a class='btn_black btn_export_excel' ><span>Excel</span></a></c:if>
            	<a class="btn_black btn_user_add" uid=''><span>사용자  추가</span></a>
            </div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="selectedRowId" id="selectedRowId" value="<c:out value="${searchVO.selectedRowId}" />" />
		<input tyee='text' style='display:none;'/>
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