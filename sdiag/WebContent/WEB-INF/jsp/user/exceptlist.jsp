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
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />
<style>
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
</style>
<script type="text/javaScript" language="javascript">

$(function () {
		
	$('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	        searchList(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});
	
	$('.btn_search').click(function(){
    	searchList(1);
    });
	
	$('.btn_expnDateDelete').click(function(){
		var checklist = [];
		$('.contents_body').find('tr').each(function(){
			$(this).find('input:checkbox[name="selno"]:checked').each(function () {
				checklist.push($(this).val());
			});
		});
		if(checklist.toString() == ''){
			return false;
		}
		if(!confirm('예외자를 삭제 하시겠습니까?')){
			return false;
		}
			
		var data = {checklist: checklist.toString()};
        $.ajax({
			url : '/user/setExpnDeleteUser.do',
			data : data,
			type : 'POST',
			dataType : 'json',
			error : function(jqXHR, textStatus, errorThrown) {
				if(jqXHR.status == 401){
					alert("인증정보가 만료되었습니다.");
					location.href="/";
				}else{
					alert(textStatus + "\r\n" + errorThrown);	
				}
			},
			success : function(data) {
				if (data.ISOK) {
					searchList($('#pageIndex').val());
				} else {
					alert(data.msg);
				}

			}

		});
	});
	
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
	
	$('.btn_expnAdd').click(function(){
		
		$.ajax({
            data: {}, 
            url: "/user/getExpnaddPopup.do",
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
                     $('.DialogBox').dialog({ width: 700, height: 620 });
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
	
	$('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": '<c:out value="${searchVO.searchCondition}" />'});
	    params[1] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": '<c:out value="${searchVO.searchKeyword}" />'});
        gopage('/user/expnlistexportexcel.do', params, 'searchVO');
	});
	
});


function searchList(page)
{
	
	document.listForm.pageIndex.value = page;
	document.listForm.action = "<c:url value='/user/exceptlist.do'/>";
	//document.listForm.action = "/user/exceptlist.do";
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
	<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>예외자 관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:80px">
                   <option value="3" <c:if test="${searchVO.searchCondition == '3'}" >selected</c:if>>사번</option>
								<option value="4" <c:if test="${searchVO.searchCondition == '4'}" >selected</c:if>>이름</option> 
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
					<col style="width:*">
					<col style="width:20%">
					<col style="width:15%">
				<col style="width:10%">
				</colgroup>
				<tr>
					<th>조직</th>
					<th>사번/이름</th>
					<th>직책</th>
					<th>선택</th>
				</tr>
                <tbody class='contents_body'>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;">
					<td><c:out value="${result.org_nm_2}" /></td>
					<td><c:out value="${result.proxyEmpNo }" /></td>
					<td><c:out value="${result.level_nm }" /></td>
					<td><input type="checkbox" name='selno' value="${result.emp_no }"/></td>
				</tr>
				</c:forEach>
				</tbody>    
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite">
            	<c:if test="${totalCnt > 0 }"><a class='btn_black btn_export_excel' ><span>Excel</span></a></c:if>
            	<a class="btn_black btn_expnAdd"><span>예외자 추가</span></a> 
            	<a class="btn_black btn_expnDateDelete"><span>예외자 삭제</span></a>
            </div>
        </div>
       
		<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="empno" id="empno" value="0">
		<input type="hidden" name="expn" value="expnAdd"/>
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