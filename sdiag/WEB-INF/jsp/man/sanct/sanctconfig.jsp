<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<c:set var="newline" value="\n" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<style>
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
</style>
<script type="text/javaScript" language="javascript">
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
            	searchList();
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
	
	$('.btn_view').click(function(){
		
		var selrow = $(this).closest('tr');
		SelectedRowChange(selrow);
		var sanctno = $(this).attr('sanctno');
		$.ajax({
            data: {sanctno: sanctno}, 
            url: "/man/sanctconfigpopup.do",
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
                     $('.DialogBox').dialog({ width: 720, height: 650 });
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
   
});


	
function searchList()
{
	
	document.listForm.action = "<c:url value='/man/sanctconfig.do'/>";
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
    	<div class="subTT"><span>제재조치 설정</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>제재조치 항목 추가는 코드관리 [S01]항목에 추가하여 주세요.</p> 
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
				<col style="width:15%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:35%">
				<col style="width:*">
				</colgroup>
			<tr>
				<th>제재조치구분</th>
				<th>점수(FROM)</th>
				<th>점수(TO)</th>
				<th>제재 공지내용</th>
				<th>해제 공지내용</th>
			</tr>
			<c:forEach var="result" items="${resultList}" varStatus="status">
			<tr style="font-weight:bold;cursor:pointer;" class='btn_view' sanctno="<c:out value="${result.sanctkind}" />">
				<td><a class="btn_view" style="cursor:pointer;" sanctno="<c:out value="${result.sanctkind}" />"><c:out value="${result.sanctnm}" /></a></td>
				<td><a class="btn_view" style="cursor:pointer;" sanctno="<c:out value="${result.sanctkind}" />"><c:out value="${result.scorfrom}" /></a></td>
				<td><a class="btn_view" style="cursor:pointer;" sanctno="<c:out value="${result.sanctkind}" />"><c:out value="${result.scoreto}" /></a></td>
				<td style="text-align:left;padding-left:15px;"><a class="btn_view" style="cursor:pointer;" sanctno="<c:out value="${result.sanctkind}" />">${fn:replace(result.sanctnoti,newline,'<br />')}</a></td>
				<td style="text-align:left;padding-left:15px;"><a class="btn_view" style="cursor:pointer;" sanctno="<c:out value="${result.sanctkind}" />">${fn:replace(result.sanctsolnoti,newline,'<br />')}</a></td>
			</tr>
			</c:forEach>
            </table>
            <!-- E :list -->
        </div>

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