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
	
	$('#checkStartDay, #checkEndDay, #idxStartDay, #idxEndDay').datepicker({
        dateFormat: 'yy-mm-dd',
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        changeMonth: true,
        changeYear: true,
        showMonthAfterYear: true,
        showButtonPanel: false,
        showOn: "both",
        //maxDate:"<c:out value="${nowDate}" />",
        buttonImage: "/img/icon_date.png",
        buttonImageOnly: true,
        beforeShow:function(){
        	setTimeout(function(){
        		$(".ui-datepicker").css("z-index","99999");
        	}, 0);
        },
        onSelect: function (dateText, inst) {
	 		var dateSearch1 = $("#checkStartDay").val();
			var dateSearch2 = $("#checkEndDay").val();
			if(dateSearch1 == "" || dateSearch1.length < 1){
				alert("시작 날짜부터 입력해주세요");
				$("#checkEndDay").val("");
			}
			if((dateSearch2 != "" && dateSearch2.length > 0) && dateSearch1 > dateSearch2){
				alert("시작 날짜가 종료 날짜보다 큽니다 . 선택을 다시해주세요");
				if (inst.id == "sBeginDate"){
					$("#checkStartDay").val("");
					$("#checkStartDay").focus();
				}
				else{
					$("#checkEndDay").val("");
					$("#checkEndDay").focus();
				}
			}
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
    
    
    $("#btn_add").click(function(){
		$("#qFormMod").val("I");
        //alert($("#qFormMod").val());
    	document.saveForm.action = "<c:url value='/securityDay/sdQuestionForm.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	});
    
    $(".btn_M").click(function(){
		$("#qFormMod").val("U");
		$("#questionNum").val($(this).attr('sQuestionNum'));
        //alert($("#qFormMod").val());
    	document.saveForm.action = "<c:url value='/securityDay/sdQuestionForm.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	});
    
    
    $(".btn_del").click(function(){
		
    	$("#questionNum").val($(this).attr('sQuestionNumDel'));
		var formData = $("#saveForm").serialize();
		
		 $.ajax({
			url: '/securityDay/sdQuestionInfoDelete.do',
			data: formData,
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
					goReload();
				}else{alert(data.MSG); }				
			}
		});
    
	});
    
	$(".btn_delete").click(function(){
		
		//var formData = $("#saveForm").serialize();
		var sdCheckNo = $("#sdCheckNo").val();
		var data = {
				sdCheckNoList:sdCheckNo
		}
		
		if(!confirm('점검표 결과가 존재 할 시,\n점검 결과 및 증적 파일도 같이 삭제 됩니다.\n삭제 하시겠습니까?')){
			return false;
		}
		 $.ajax({
			url: '/securityDay/sdCheckListInfoDelete.do',
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
					goPage();
				}else{alert(data.MSG); }				
			}
		});
    
	});
    
    
    
	$(".btn_save").click(function(){
		
		if($("#checklistNm").val() ==""){
			alert("점검표명을 입력하세요.");
			$("#checklistNm").focus();
			return;
		}
		
		
		if($("#checkStartDay").val() ==""){
			alert("활성화 시작일을 선택하세요.");
			$("#checkStartDay").focus();
			return;
		}
		
		if($("#checkEndDay").val() ==""){
			alert("활성화 종료일을 선택하세요.");
			$("#checkEndDay").focus();
			return;
		}
		
		if($("#useYn").val() ==""){
			alert("사용여부를 선택하세요.");
			$("#useYn").focus();
			return;
		}
		
		var formData = $("#saveForm").serialize();
		
		
		 $.ajax({
			url: '/securityDay/sdCheckListInfoSave.do',
			data: formData,
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
					$("#sdCheckNo").val(data.sdCheckNo);
					goReload();	
				}else{alert(data.MSG); }				
			}
		});
    
	});
	
	$.targetAdd = function(reqCode){
    	var pw = window.open('/group/popOrgInfoSearch.do?reqCode='+reqCode,'orgInfo_popup','width=1120, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
	};
	
	$.addRow = function(type, orgInfo, orgType, orgNm){
		//var orgInfo = $(this).attr('orgInfo');
		//var orgType = $(this).attr('orgType');
		//var orgNm = $(this).attr('orgNm');
		
		
		var trAdd = "<tr>";
		if(orgType=="1") {
			trAdd +="<td>개인</td>";
		} else if(orgType=="2") {
			trAdd +="<td>조직</td>";
		}else if(orgType=="3") {
			trAdd +="<td>그룹</td>";
		}
		
		var gubun
		
		if(type == "3")
			gubun = "1";
		else 
			gubun = "2";
		
		trAdd +="<td>"+orgNm+"</td>";
		trAdd +="<td>"+orgInfo+"</td>";
		trAdd +='<td><a href="javascript:void(0);" onclick="$.btn_del(\'\', this)" style="cursor:pointer;" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a></td>';
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer; ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		trAdd +="<input type='hidden' id='gubun' name='gubun' value='"+gubun+"' />";
		trAdd +="<input type='hidden' id='targetType' name='targetType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='targetNm' name='targetNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='tartgetCode' name='targetCode' value='"+orgInfo+"' />";
		trAdd +="</tr>";
		
		if(gubun =="1")
			$("#targetInfoTb").append(trAdd);
		else
			$("#targetExInfoTb").append(trAdd);
		
		//self.close();
	};
	
	
	$.btn_del = function(targetCode,gubun, obj){ 
		//alert("ddd");
		
		var targetCode = targetCode;
		var sdCheckNo = $("#sdCheckNo").val();
		var gubun = gubun;
		
		//alert("targetInfo ="+targetInfo );
		
		if(targetCode == ""){
			//alert("ee");
			$(obj).parent().parent().remove();
		}
		else{
			var data = {
					targetCode : targetCode,
					sdCheckNo : sdCheckNo,
					gubun : gubun
			}
			
			
			$.ajax({
				url: '/securityDay/sdTargetInfoDelete.do',
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
						//$("#sdCheckNo").val(data.sdCheckNo);
						$(obj).parent().parent().remove();
						
					}else{alert(data.MSG); }				
				}
			});
		}
    
	};
	
	function goReload(){
		$("#formMod").val("U");
		
		document.saveForm.action = "<c:url value='/securityDay/sdCheckListForm.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	}
	
	function goPage() {

		document.saveForm.pageIndex.value = 1;
	    document.saveForm.action = "<c:url value='/securityDay/sdCheckList.do'/>";
	    document.saveForm.method = 'post';
	    document.saveForm.submit();
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
    <form:form id="saveForm" name="saveForm" method="post" action="/securityDay/sdCheckListForm.do">
    	<div class="subTT"><span>Security Day 점검표 상세 정보</span></div>
    	<div class='popTT'><img src='/img/icon_arw4.jpg' /> 점검표 정보</div>
    	<div class="sch_view" style="margin-top: 5px;">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:35%;" />
                    <col style="width:15%;" />
                    <col style="width:35%;" />
                </colgroup>
                <tr> 
                    <th>점검표 명</th>
                    <td>
                    	<input type="text"  class="srh" id="checklistNm" name="checklistNm" value="${sdCheckListInfo.checklistNm }" maxlength="200" class="srh" style="width:98%">
                    </td>
                    <th>사용 여부</th>
					<td>
						<select name='useYn' id='useYn'>
		        			<option value='' <c:if test="${sdCheckListInfo.useYn == ''}" >selected</c:if>>전체</option>
		        			<option value='Y' <c:if test="${sdCheckListInfo.useYn == 'Y'}" >selected</c:if>>사용</option>
		        			<option value='N' <c:if test="${sdCheckListInfo.useYn == 'N'}" >selected</c:if>>미사용</option>
		        		</select>
		        	</td>
				</tr>
			</table>
        </div>
        
        
		<div <c:if test="${searchVO.formMod =='I'}"> Style='display:none;' </c:if>>
	        <div class='popTT' style="padding-top: 30px;">
	        	<img src='/img/icon_arw4.jpg' /> 문항 정보
	        	<a style="float: right;" class="btn_black" id="btn_add"><span>문항 추가</span></a>
	        </div>
	    	<div class="sch_view" style="margin-top: 5px;">
	            <table cellpadding="0" cellspacing="0" class="tbl_list1">
	                <colgroup>
						<col style="width:10%">
						<col style="width:40">
						<col style="width:10">
						<col style="width:20%">
					</colgroup>
					<tr>
						<th>순서</th>
						<th>문항 제목</th>
						<th>문제 유무</th>
						<th>수정/삭제</th>
					</tr>
					<c:forEach var="result" items="${resultList}" varStatus="status">
					<tr style="font-weight:bold;" >
						<td>${result.ordr }</td>
						<td>${result.questionNm }</td>
					<c:choose>
						<c:when test="${not empty result.question }">
						<td>Y</td>
						</c:when>
						<c:otherwise>
						<td>N</td>
						</c:otherwise>	
					</c:choose>
						<td>
							<li>
							<a class='btn_M'style='cursor:pointer;padding-right: 15px;'  sQuestionNum=<c:out value="${result.questionNum }" />><img src='/img/btn_modify.png' alt='modify' title='modify' /></a>
		                    <a class='btn_del' style='cursor:pointer;' sQuestionNumDel=<c:out value="${result.questionNum }" />><img src='/img/btn_delete.png' alt='delete' title='delete' /></a>
		                    </li>
						</td>
					</tr>
					</c:forEach>
	            </table> 
	            
	        </div>
	       	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }" />
	       	<input type="hidden" id="qFormMod" name="qFormMod" value="" />
	       	<input type="hidden" id="sdCheckNo" name="sdCheckNo" value="${sdCheckListInfo.sdCheckNo }" />
	       	<input type="hidden" id="sdCheckNoList" name="sdCheckNoList" value="" />
	       	<input type="hidden" id="questionNum" name="questionNum" value="0" />
	       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
       	</div>
       	
       	<div class="btn_black2">
       	<c:if test='${searchVO.formMod eq "U"}'>
       		<a class="btn_black btn_delete"><span>삭제</span></a>
       	</c:if>
       		<a class="btn_black btn_save"><span>저장</span></a>
	        <a class="btn_black" href="/securityDay/sdCheckList.do"><span>취소</span></a> 
	    </div>
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>