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
<script type="text/javascript" src="/js/js/jquery.isotope.min.js"></script>
<script type="text/javascript" src="/js/js/justgage.1.0.1.js"></script>
<script type="text/javascript" src="/js/js/raphael.2.1.0.min.js"></script>
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />

<script type="text/javaScript" language="javascript">
$(function () {
	$('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	    	goPage(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});
	
	$('#selAll_checkbox').click(function(){
		var ischeck = $(this).is(':checked');
		$('input:checkbox[name=c_kpEmpNo]').prop('checked',ischeck);

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
	
		
	$(".btn_save").click(function(){
		$("#formMod").val("I");
		
		var formMod = "I";
		$.ajax({
           data: {formMod:formMod}, 
           url: "/man/getTypeReassignForm.do",
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
               	 //alert(data.popup_body);
                    $('.DialogBox').dialog({ width: 1050, height: 660 });
                    $('.DialogBox').dialog('open');
                    $('.DialogBox').data('is_save', 'N');
                    $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.tab_btn' });
                    
                    
               }
               else {
                   alert(data.MSG);
               }

            }

        });		
		
	});
	
	$('.btn_usersearch1').click(function(){
				var popcondit=$('#popcondit1').val();
				var popkeywrd=$('#popkeywrd1').val();
	         var type = '1';
				if(popkeywrd == ''){
					alert('사번또는 이름을 입력하여주세요.');
					return false;
				}
				$.ajax({
				data: {searchCondition:popcondit, searchKeyword:popkeywrd, type:type},
				url: '/man/getSearchUserList.do',
				type: 'POST',
				dataType: 'json',
				error: function (jqXHR, textStatus, errorThrown) {
						if(jqXHR.status == 401){
							alert('인증정보가 만료되었습니다.');
							location.href='/';
						}else{
							alert(textStatus + '\\r\\n' + errorThrown);
						}
				},
				success: function (data) {
					if (data.ISOK) {
	 //alert(data.body);
						$('.popup_content_body_left').empty().append(data.body);
					}else{alert(data.MSG); }
				}
			});
	});
	$('.btn_usersearch2').click(function(){
				var popcondit=$('#popcondit2').val();
				var popkeywrd=$('#popkeywrd2').val();
				var type = '2';
				if(popkeywrd == ''){
					alert('사번또는 이름을 입력하여주세요.');
					return false;
				}
				$.ajax({
				data: {searchCondition:popcondit, searchKeyword:popkeywrd, type:type},
				url: '/man/getSearchUserList.do',
				type: 'POST',
				dataType: 'json',
				error: function (jqXHR, textStatus, errorThrown) {
						if(jqXHR.status == 401){
							alert('인증정보가 만료되었습니다.');
							location.href='/';
						}else{
							alert(textStatus + '\\r\\n' + errorThrown);
						}
				},
				success: function (data) {
					if (data.ISOK) {
	// alert(data.body);
						$('.popup_content_body_right').empty().append(data.body);
					}else{alert(data.MSG); }
				}
			});
	});
	
	$.empInfo = function(empNo, empNm, type){
			if(type == '1'){
				$('#ktEmpNo').text(empNo);
				$('#ktEmpNm').text(empNm);
			}else{
				if($('#ktEmpNo').text() == empNo){
					alert('KT담당자와 동일한 사원 정보 입니다. 다시 한번 확인해주세요.');
					return false;
				}else{ 
					$('#reEmpNo').text(empNo);
					$('#reEmpNm').text(empNm);
				}
			}
		};
		
	$(".btn_search").click(function(){
		$("#formMod").val("S");
		
	    document.listForm.action = "<c:url value='/man/typeReassignList.do'/>";
	    document.listForm.submit();
	});
	
	$(".btn_view").click(function(){
		//alert("ddddd")
		$("#formMod").val("U");
		var info =$(this).attr("info");
		
		//alert(info);
		var arr = new Array();
		arr = info.split("/");
		
		
		var ktEmpNo = arr[0];
		var cType = arr[1];
		var reEmpNo = arr[2];
		
		
		var formMod = "U";
		$.ajax({
           data: {formMod:formMod,
        	      ktEmpNo:ktEmpNo,
        	      cType:cType,
        	      reEmpNo:reEmpNo}, 
           url: "/man/getTypeReassignForm.do",
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
               	 //alert(data.popup_body);
                    $('.DialogBox').dialog({ width: 1020, height: 660 });
                    $('.DialogBox').dialog('open');
                    $('.DialogBox').data('is_save', 'N');
                    $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.tab_btn' });
                    
                    
               }
               else {
                   alert(data.MSG);
               }

            }

        });		
		
	});
	
	$(".btn_delete").click(function(){
		var checked_length =  $('input:checkbox[name=c_kpEmpNo]:checked').length;
		
		
		if(checked_length <= 0){
			alert('삭제할 목록을 선택해 주세요.');
			return false;
		} else {
			var infoList = [];
			$('input:checkbox[name=c_kpEmpNo]:checked').each(function () {
				//alert($(this).val());
				infoList.push($(this).val());
			});
			var data = {
					infoList:infoList.toString()
			}
		
			 $.ajax({
				url: '/man/setTypeReassignDelete.do',
				data: data,
				type: 'POST',
				dataType: 'json',
				error: function (jqXHR, textStatus, errorThrown) {					
					if(jqXHR.status == 401){						
						alert('인증정보가 만료되었습니다.');						
						location.href='/';					
					}else{						
						alert(textStatus + '\r\n' + errorThrown);					
					}				
				},				
				success: function (data) {					
					if (data.ISOK) {
						alert("정상적으로 처리 되었습니다."); 
						$("#formMod").val("S");
						
					    document.listForm.action = "<c:url value='/man/typeReassignList.do'/>";
					    document.listForm.submit();
					}else{
						alert(data.MSG); 
						$("#formMod").val("S");
						
					    document.listForm.action = "<c:url value='/man/typeReassignList.do'/>";
					    document.listForm.submit();
					}				
				}
			});
		}
	});
		
});

	
</script>
    
</head>
<body>
<!-- S : header -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
<!-- E : header -->
<!-- S : contents -->
		<div id="cnt" class='ptop'>
			<!-- S : left contents -->
			<div id="cnt_L">
				<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp"%>
			</div>
			<!-- E : left contents -->


			<!-- S : center contents -->
			<div id="cnt_R">
				<div class="tab_btn" style="margin: 10px 0px 20px 0px;">
					<a class="btn_type1" href="/man/typeReassignList.do"><span>유형별
							담당자 재 지정</span></a> <a class="btn_type2" href="/man/psReassignList.do"><span>개인별
							담당자 재 지정</span></a>
				</div>
				<form id="searchForm" name="listForm" method="post" action="/man/typeReassignList.do">
					<input id="formMod" type="hidden" />
					<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
					<div class="subTT">
						<span>유형별 담당자 재지정</span>
					</div>
					<!-- S :search -->
					<div class="sch_block3">
						<li>
							<p>검색 조건</p> <select name="c_condition" id="c_condition">
								<option value="">선택하세요.</option>
								<option value="1"
									<c:if test='${searchVO.c_condition == "1"}' >selected</c:if>>KT
									담당자 명</option>
								<option value="2"
									<c:if test='${searchVO.c_condition == "2"}' >selected</c:if>>KT
									담당자 사번</option>
								<option value="3"
									<c:if test='${searchVO.c_condition == "2"}' >selected</c:if>>지정
									담당자 명</option>
								<option value="4"
									<c:if test='${searchVO.c_condition == "2"}' >selected</c:if>>지정
									담당자 사번</option>
						</select> <input type="text" name="c_searchKeyword" id="c_searchKeyword"
							value="${searchVO.c_searchKeyword }" style="width: 120px"
							class="srh"> <a class="btn_black btn_search"><span>검색</span></a>
						</li>
					</div>
				</form>
				<div class="sch_view">
					<!-- S :list -->
					<table cellpadding="0" cellspacing="0" class="tbl_list1">
						<colgroup>
							<col style="width: 5%">
							<col style="width: 10%">
							<col style="width: 10%">
							<col style="width: *">
							<col style="width: 15%">
							<col style="width: 10%">
							<col style="width: 10%">
							<col style="width: *">
						</colgroup>
						<tr>
							<th class="ck_button"><label><input type="checkbox"
									name="selAll_checkbox" id="selAll_checkbox" /><span></span></label></th>
							<th>KT 담당자 사번</th>
							<th>KT 담당자 이름</th>
							<th>KT 담당자 조직</th>
							<th>협력사 유형</th>
							<th>지정 담당자 사번</th>
							<th>지정 담당자 이름</th>
							<th>지정 담당자 조직</th>

						</tr>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0 }">
								<c:forEach var="result" items="${resultList}" varStatus="status">
									<tr>
										<td class="ck_button"><label><input
												type="checkbox" name="c_kpEmpNo"
												value="<c:out value="${result.ktEmpNo}/${result.collaborCode}/${result.reEmpNo }" />"><span></span></label></td>
										<td style="cursor: pointer; " class='btn_view' info="<c:out value="${result.ktEmpNo}/${result.collaborCode}/${result.reEmpNo }" />">${result.ktEmpNm }</td>
										<td>${result.ktEmpNo }</td>
										<td>${result.ktPosnNm}</td>
										<td>${result.cType}</td>
										<td>${result.reEmpNo }</td>
										<td>${result.reEmpNm }</td>
										<td>${result.rePosnNm }</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="8">검색결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</table>
					<!-- E :list -->
		            <!-- S : page NUM -->
		            <div class="pagingArea1 pagingPadd1">
						<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
					</div>

					<div class="btn_borderWrite" style="top: 20px;">
						<a class="btn_black btn_save"><span>대규모 재지정</span></a> <a
							class="btn_black btn_delete"><span>삭제</span></a>
					</div>
				</div>
			</div>

			<!-- E : center contents -->
			<!-- S : footer -->
			<%@ include file="/WEB-INF/jsp/cmm/footer.jsp"%>
			<!-- E : footer -->
			
			<div class='DialogBox' style="height: auto;"></div>
		</div>
</body>
</html>