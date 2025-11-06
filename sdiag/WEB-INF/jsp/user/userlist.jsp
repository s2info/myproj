<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<script type="text/javaScript" language="javascript">

function expnAdd() {
	var popUrl = "/pop/expnListPopUplist.do";
	var popOption = "width=700, height=500, resizable=no, scrollbars=no, status=no;";
	
	window.open(popUrl,"",popOption);
	
//	document.expn.target = 'submitWindow';
//	document.expn.submit();
}

function expnDateDelete(){
	// 유효성 체크
	if(!$("input:checkbox[name='EXPNNO']").is(":checked")){
		alert(" 삭제처리 항목을 체크하십시오.");
		return ;
	}
	// 자료실 DB등록
	else if( confirm(" 삭제처리 하시겠습니까?" ) ){
		document.expn.action = "/service.expnDateDelete.do";
		document.expn.submit();
	}
}

/*
$(function () {
	
	$('.btn_view').click(function(){
		var sq_no = $(this).attr('sq_no');

		$('#sq_no').val(sq_no);
		document.listForm.action = "<c:url value='/user/userView.do'/>";
		document.listForm.submit();

	});
	
});
	
	*/
//	$(document).ready(function (){	
//	
//	
//	$("#expnAdd").click(function(){
//		
//			alert("예외자 추가");
//			$.ajax({
//                data: {tseq: $('#tseq').val()}, 
//                url: "/pop/expnListPopUplist.do",
//                contentType: "application/json; charset=utf-8",
//                type: "get",
//                dataType: "json",
//                error: function (jqXHR, textStatus, errorThrown) {
//                    alert(textStatus + "\r\n" + errorThrown);
//                },
//                success: function (data) {
//                    if (data.ISOK) {
//                    	$('.contentlist_body').empty().append(data.list_body);
//                    }
//                    else {
//                        alert(data.MSG);
//                    }
//                }
//            });
//
//			$('.DialogBox').dialog({
//	            autoOpen: false,
//	            modal: true,
//	            resizable: false,
//	            show: "fade",
//	            hide: "fade",
//	            close: function () { 
//	                $(this).dialog('close'); 
//	                var $ddata = $(".DialogBox");
//	                if($ddata.data('is_save') == 'Y')
//	   	            {
//	                	    reloadOrgList();
//	                }
//	            },
//	            open: function () {
//	                var $ddata = $(".DialogBox");
//
//	                $('.ui-widget-overlay').bind('click', function () {
//	                    $ddata.dialog('close');
//	                });
//	            }
//	        });
//			
//		var Url = "/pop/expnListPopUp.jsp";
//		var date= {
//				emp_no:$("#EMPNO").val(),
			//	flag:"insert"
//			};

//		$("#empPOP").load(Url, date, function () {  
//	        $("#empPOP").dialog({  
//	            autoOpen: false,  
//	            height: 310,  
//	            width: 590,  
//	            modal: true,  
//	            resizable: false,  
//	            closeOnEscape: true,
//	            open: function (event, ui) {
//	            	
//	            },  
//	            close: function () {
//	            	
//	            }	            
//	        });  
//	        $("#empPOP").dialog("open");  
//	    });
//	});

//});
//function onExpnAdd() {
//	 if (wPers == null) {
//	        wPers = window.open('/Pop/person.aspx', 'FindPersonPopup', 'width=400,height=550');
//	    }
//
//	    wPers.focus();
//	}	
		
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
			
			<div class="WC_right">
				<div class="S_tit">
					<ul>
						<li><img src="/img/dot2.png" alt="타이틀"></li>
						<li class="ST_txt">예외자 관리</li>
					</ul>
				</div>
				<div class="search1">
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>검색어</span></li>
						<li>
							<select  name="search" >
								<option value="1">사번</option>
								<option value="2">이름</option> 
							</select>
						</li>
						<li><input type="text" style="width:90px"></li>
						<li><div class="btn1 btn_search"><a style="cursor:pointer;">검색</a></div></li>
					</ul>
				</div>
				
				<div class="marT10"></div>
				<div class="WCR_box1" style='padding:0 0 0;width:100%;' >
					<table border="0" class="TBS3" cellpadding=0 cellspacing=0>

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
						<c:forEach var="result" items="${resultList}" varStatus="status">
							<tr style="font-weight:bold;">
								<td style="text-align:left;padding-left:10px;"><a class="btn_view" style="cursor:pointer;" sq_no="<c:out value="${result.org_nm_2}" />"><c:out value="${result.org_nm_2}" /></a></td>
								<td><c:out value="${result.proxyEmpNo }" /></td>
								<td><c:out value="${result.level_nm }" /></td>
								<td><input type="checkbox" name="EXPNNO" value="${result.emp_no }"/></td>
							</tr>
						</c:forEach>
					</table>
				</div>		
				<div class="marT10"></div>
				<div class="number1">
					<ul id="paging"></ul>
				</div>
				<div class="F_right"><div class="btn3"><a href="#">예외자 추가</a></div><div class="btn3"><a href="#">예외자 삭제</a></div></div>
			</div>
		</form:form>
		<input type="hidden" name="expn" value="expnAdd"/>
		<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
<!-- E : contents -->
<div id="empPOP"></div>
	
</body>
</html>