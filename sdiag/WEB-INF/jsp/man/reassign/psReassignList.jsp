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

input::-ms-input-placeholder {
color :  gray;
}

</style>


<script type="text/javaScript" language="javascript">
$(function () {
	$('#selAll_checkbox').click(function(){
		var ischeck = $(this).is(':checked');
		$('input:checkbox[name=kpEmpNo]').prop('checked',ischeck);

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
	
	
	
	$(".btn_search").click(function(){
		$("#formMod").val("S");
		
		if($("#c_searchKeyword").val() == "" && $("#s_cType").val() == ""&& $("#kt_searchKeyword").val() == ""&& $("#re_searchKeyword").val() == ""){
			alert("검색 조건을 하나라도 선택 및 입력 하세요. \n데이터가 많아 페이지 동작이 멈출수도 있습니다.");
			return false;
		}
		
	    document.listForm.action = "<c:url value='/man/psReassignList.do'/>";
	    document.listForm.submit();
	});
	
	
	$.search = function(formMod){
		
		$("#formMod").val(formMod);
		
		$.ajax({
            data: {}, 
            url: "/man/getSearchPopup.do",
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
                     $('.DialogBox').dialog({ width: 930, height: 570 });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.tab_btn' });
                     
                     
                }
                else {
                    alert("처리중 오류가 발생하였습니다.");
                }

            }

        });		
		
	};
	
	$(".btn_save").click(function(){
		
		var checked_length =  $('input:checkbox[name=kpEmpNo]:checked').length;
		
		var re_empNo = $("#re_empNo").val();
		
		if(re_empNo == ""){
			alert('담당자를 재 지정할 KT담당자를 선택해 주세요.');
			return false;
		}
			
		
		if(checked_length <= 0){
			alert('담당자를 재 지정할 협력사를 선택해 주세요.');
			return false;
		} else {
			var kpEmpNoList = [];
			$('input:checkbox[name=kpEmpNo]:checked').each(function () {
				//alert($(this).val());
				kpEmpNoList.push($(this).val());
			});
			var data = {
					kpEmpNoList:kpEmpNoList.toString(),
					reEmpNo : re_empNo
			}
		
			 $.ajax({
				url: '/man/setProxyEmpNoSave.do',
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
						kpSearch();
					}else{
						alert("처리중 오류가 발생하였습니다."); 
						kpSearch();
					}				
				}
			});
		}
		
	});
	
	$(".btn_delete").click(function(){
		var checked_length =  $('input:checkbox[name=kpEmpNo]:checked').length;
		
		
		if(checked_length <= 0){
			alert('초기화 진행할 협력사를 선택해 주세요.');
			return false;
		} else {
			var kpEmpNoList = [];
			$('input:checkbox[name=kpEmpNo]:checked').each(function () {
				//alert($(this).val());
				kpEmpNoList.push($(this).val());
			});
			var data = {
					kpEmpNoList:kpEmpNoList.toString()
			}
		
			 $.ajax({
				url: '/man/setProxyEmpNoDelete.do',
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
						kpSearch();
					}else{
						alert("처리중 오류가 발생하였습니다."); 
						kpSearch();
					}				
				}
			});
		}
	});
	
	function kpSearch (){
		$("#formMod").val("S");
		
	    document.listForm.action = "<c:url value='/man/psReassignList.do'/>";
	    document.listForm.submit();
	};
    
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
        <div class="tab_btn" style="margin: 10px 0px 20px 0px;">
    		<a class="btn_type2" href="/man/typeReassignList.do"><span>유형별 담당자 재 지정</span></a> 
    		<a class="btn_type1" href="/man/psReassignList.do"><span>개인별 담당자 재 지정</span></a> 
    	</div>

		<form id="listForm" name="listForm" method="post">
		<table cellpadding='0' cellspacing='0' style="width: 100%">
			<colgroup>
				<col style='width:50%;' />
				<col style='width:50%;' />
			</colgroup>
			<tr>
				<td style="padding-right: 15px;">

				<div class="subTT">
				<span>협력사 정보 검색</span>
			</div>
			<!-- S :search -->
			<div class="sch_block3">
				<li>
					<p>협력사정보</p> <br/><br/>
					<select name="c_condition" id="c_condition" style="margin-left: 20px;">
						<option value="">선택하세요.</option>
						<option value="1" <c:if test='${searchVO.c_condition == "1"}' >selected</c:if>>협력사 사번</option>
						<option value="2" <c:if test='${searchVO.c_condition == "2"}' >selected</c:if>>협력사 이름</option>
					</select> 
					<input type="text" name="c_searchKeyword" id="c_searchKeyword" value="${searchVO.c_searchKeyword }" style="width: 120px" class="srh"><br/>
					<br/><p>협력사유형</p> <br/><br/>
					<select name="s_cType" id="s_cType" style="margin-left: 20px;"> 
						<option value="">선택하세요.</option>
						<c:forEach var="result" items="${cTypeList}" varStatus="status">
							<option value="${result.minr_code }" <c:if test='${searchVO.s_cType == result.minr_code}' >selected</c:if>>${result.code_desc }</option>
						</c:forEach>
					</select><br/>
					<br/><p>담당자 정보</p> <br/><br/>
					<input type="text" name="kt_searchKeyword" id="kt_searchKeyword" value="${searchVO.kt_searchKeyword }" style="width: 220px;margin-left: 20px;" class="srh" onclick="$.search('1');" readonly="readonly"><br/>
					<br/><p>재지정 담당자 정보</p> <br/><br/>
					<input type="text" name="re_searchKeyword" id="re_searchKeyword" value="${searchVO.re_searchKeyword }" style="width: 220px;margin-left: 20px;" class="srh" onclick="$.search('3');" readonly="readonly">  
					<a class="btn_black btn_search"  style="margin-right: 20px; float: right;"><span>검색</span></a>
				</li>
			</div>
			<input type="hidden" id="formMod" name="formMod" />
			</td>
			<td>
			<div class="subTT">
				<span>담당자 재 지정</span>
			</div>
		<div class="sch_block3" style="padding-bottom: 207px;">
        	<li>
				<p>재 지정 담당자 정보</p> <br/><br/>
                <input type="text" name="re_empNo" id="re_empNo" placeholder="재 지정 담담자 사번"  value="" style="background:#cfcfcf; width:150px;margin-left: 20px; " class="srh" readonly="readonly">
                <input type="text" name="re_empNm" id="re_empNm" placeholder="재 지정 담담자 이름" value="" style="background:#cfcfcf; width:150px" class="srh" readonly="readonly">
                <a class="btn_black"  onclick="$.search('2');" style="float: right;margin-right: 15px; "><span>재 지정 담당자 정보 조회</span></a>
            </li> 
        </div></td>
			</tr>
		
		</table>
			
		</form>
		
		
     
        <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:5%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th class="ck_button"><label><input type="checkbox" name="selAll_checkbox" id="selAll_checkbox" /><span></span></label></th>
					<th>협력사 사번</th>
					<th>협력사 이름</th>
					<th>KT 담당자 사번</th>
					<th>KT 담당자 이름</th>
					<th>재 지정 담당자 사번</th>
					<th>재 지정 담당자 이름</th>
					<th>회사명</th>
					<th>협력사 종류</th>
				</tr>
				<c:choose>
				<c:when test="${fn:length(resultList) > 0 }">
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr>
					<td class="ck_button"><label><input type="checkbox" name="kpEmpNo" value="<c:out value="${result.kpEmpNo}" />"><span></span></label></td>
					<td>${result.kpEmpNo }</td>
					<td>${result.kpEmpNm }</td>
					<td>${result.ktEmpNo }</td>
					<td>${result.ktEmpNm }</td>
					<td>${result.reEmpNo }</td>
					<td>${result.reEmpNm }</td>
					<td>${result.userBizNm }</td>
					<td>${result.cType}</td>
				</tr>
				</c:forEach>
				</c:when>
				<c:otherwise>
	            	<tr>
	            	<td colspan="9"> 검색결과가 없습니다.</td>
	            	</tr>
		        </c:otherwise>
				</c:choose>
            </table>
            <!-- E :list -->
            
            <div class="btn_borderWrite" style="top: 20px;">
            	<a class="btn_black btn_delete"><span>초기화</span></a>
            	<a class="btn_black btn_save" ><span>저장</span></a>
            </div>
        </div>
        
        
        	
	</div>
	
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
     <div class='DialogBox'></div>
</div>
</body>
</html>