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
			var dateSearch3 = $("#idxStartDay").val();
			var dateSearch4 = $("#idxEndDay").val();
			if(dateSearch1 == "" || dateSearch1.length < 1){
				alert("시작 날짜부터 입력해주세요");
				$("#checkEndDay").val("");
			}
			if((dateSearch2 != "" && dateSearch2.length > 0) && dateSearch1 > dateSearch2){
				alert("점검 시작 날짜가 점검 종료 날짜보다 큽니다 . 선택을 다시해주세요");
				if (inst.id == "checkStartDay"){
					$("#checkStartDay").val("");
					$("#checkStartDay").focus();
				}
				else{
					$("#checkEndDay").val("");
					$("#checkEndDay").focus();
				}
			}
			//alert(dateSearch4);
			if((dateSearch4 != "" && dateSearch4.length > 0) && dateSearch3 > dateSearch4){
				alert("지수화 시작 날짜가 종료 날짜보다 큽니다 . 선택을 다시해주세요");
				if (inst.id == "idxStartDay"){
					$("#idxStartDay").val("");
					$("#idxStartDay").focus();
				}
				else{
					$("#idxEndDay").val("");
					$("#idxEndDay").focus();
				}
			}
			
			if((dateSearch3 != "" && dateSearch3.length > 0) && dateSearch1 > dateSearch3){
				alert("지수화 시작 날짜가 점검 시작 날짜보다 작습니다 . 선택을 다시해주세요");
				if (inst.id == "checkStartDay"){
					$("#checkStartDay").val("");
					$("#checkStartDay").focus();
				}
				else{
					$("#idxStartDay").val("");
					$("#idxStartDay").focus();
				}
			}
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
    
    
  
    
    $.clInfo_del= function(clGroupNo, sdCheckNo, obj){
		
    	var clGroupNo = clGroupNo;
    	var sdCheckNo = sdCheckNo;
    	
    	if(clGroupNo == ""){
			//alert("ee");
			$(obj).parent().parent().remove();
		}
		else{
	    	var data = {
					clGroupNo:clGroupNo,
					sdCheckNo:sdCheckNo
			}
	    	
			 $.ajax({
				url: '/securityDay/GroupClInfoDelete.do',
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
						goReload();
					}else{alert(data.MSG); }				
				}
			});
		}
    
	};
    
	$(".btn_delete").click(function(){
		
		//var formData = $("#saveForm").serialize();
		var clGroupNo = $("#clGroupNo").val();
		var data = {
				clGroupNoList:clGroupNo
		}
		
		if(!confirm('점검표 결과가 존재 할 시,\n점검 결과 및 증적 파일도 같이 삭제 됩니다.\n삭제 하시겠습니까?')){
			return false;
		}
		 $.ajax({
			url: '/securityDay/sdCheckListGroupInfoDelete.do',
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
		
		if($("#clGroupNm").val() ==""){
			alert("점검표명을 입력하세요.");
			$("#checklistNm").focus();
			return;
		}
		
		
		var formData = $("#saveForm").serialize();
		
		
		 $.ajax({
			url: '/securityDay/sdCheckListGroupInfoSave.do',
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
					if($("#formMod").val()=="I"){
						$("#clGroupNo").val(data.clGroupNo);
						$("#formMod").val("U");
					}
					
					goReload();	
				}else{alert(data.MSG); }				
			}
		});
    
	});
	
	$(".preView").click(function(){
		var pw = window.open('/securityDay/popSdPreView.do?pageIndex='+$("#clGroupNo").val()+'&firstIndex=0','orgInfo_popup','width=980, height=885,scrollbars=yes, menubar=no, status=no, toolbar=no');
	});
	
	
	$.targetAdd = function(reqCode, gubun, sdCheckNo){
    	var pw = window.open('/group/popOrgInfoSearch.do?reqCode='+reqCode+'&formMod='+gubun+'&qFormMod='+sdCheckNo,'orgInfo_popup','width=800, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
	};
	
	$.addRow = function(type,gubun,sdCheckNo, orgInfo, orgType, orgNm){
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
		
	
		trAdd +="<td>"+orgNm+"</td>";
		trAdd +="<td>"+orgInfo+"</td>";
		trAdd +='<td><a href="javascript:void(0);" onclick="$.btn_del(\'\',\'\',\'\',\''+gubun+'\', this)" style="cursor:pointer;" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a></td>';
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer; ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		trAdd +="<input type='hidden' id='sdCheckNoList' name='sdCheckNoList' value='"+sdCheckNo+"' />";
		trAdd +="<input type='hidden' id='gubun' name='gubun' value='"+gubun+"' />";
		trAdd +="<input type='hidden' id='targetType' name='targetType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='targetNm' name='targetNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='tartgetCode' name='targetCode' value='"+orgInfo+"' />";
		trAdd +="</tr>";
		
		if(gubun =="1")
			$("#targetInfoTb"+sdCheckNo).append(trAdd);
		else
			$("#targetExInfoTb").append(trAdd);
		
		//self.close();
	};
	
	
	$.btn_del = function(clGroupNo, sdCheckNo, targetCode, gubun, obj){ 
		//alert("ddd");
		
		var targetCode = targetCode;
		var clGroupNo = $("#clGroupNo").val();
		var sdCheckNo = sdCheckNo;
		var gubun = gubun;
		
		//alert("targetInfo ="+targetInfo );
		
		if(targetCode == ""){
			//alert("ee");
			if(gubun == "1")
				$(obj).parent().parent().remove();
			else
				$(obj).parent().parent().remove();
		}
		else{
			var data = {
					targetCode : targetCode,
					clGroupNo : clGroupNo,
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
						goReload();						
					}else{alert(data.MSG); }				
				}
			});
		}
    
	};
	
	$.clAdd = function(){
		var clInfo = $("#clInfo").val();
		
		var arr = new Array();
		arr = clInfo.split("/");
		
		var sdCheckNo = arr[0];
		var sdCheckNm = arr[1];
		
		if($("#targetInfoTb"+sdCheckNo).length > 0){
			
			alert("이미 추가된 점검표 입니다.");
			return false;
		
		}else{
			var trAdd = "<tr>";
			trAdd +="<th>점검표명</th>";
			trAdd +="<td>"+sdCheckNm;
			trAdd +="<input type='hidden' id='m_sdCheckNo' name='m_sdCheckNo' value='"+sdCheckNo+"' /></td>";
			trAdd +="<th>대상자<br/>";
			trAdd +='<a href="javascript:$.targetAdd(\'3\', \'1\', \''+sdCheckNo+'\')" class="btn_black" ><span>대상자 추가</span></a>';
			trAdd +="</th>";
			trAdd +="<td>";
	        trAdd +="	<table cellpadding='0' cellspacing='0' id='targetInfoTb"+sdCheckNo+"' class='tblInfoType3' style='width:98%; float: left;' >";
	        trAdd +="        <colgroup>";
			trAdd +="			<col style='width:10%'>";
			trAdd +="			<col style='width:45%'>";
			trAdd +="			<col style='width:10%'>";
			trAdd +="			<col style='width:15%'>";
			trAdd +="		</colgroup> ";
			trAdd +="		<tr> ";
			trAdd +="			<th style='padding: 8px; height: 10px;'>유형</th>";
			trAdd +="			<th style='padding: 8px; height: 10px;'>이름</th>";
			trAdd +="			<th style='padding: 8px; height: 10px;'>코드</th>";
			trAdd +="			<th style='padding: 8px; height: 10px;'>대상자 삭제</th>";
			trAdd +="		</tr>";
	        trAdd +="    </table>";
			trAdd +="</td>";
			trAdd +="<th>점검표 삭제</th>";
			trAdd +="<td>";
	        trAdd +='    <a class="btn_del" style="cursor:pointer;" onClick="$.clInfo_del(\'\',\'\',this)" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a>';
			trAdd +="</td>";
			trAdd +="</tr>";
			
			$("#clTb").append(trAdd);
		}
	};
	
	function goReload(){
		document.saveForm.action = "<c:url value='/securityDay/sdCheckListGroupForm.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	}
	
	function goPage() {

		document.saveForm.pageIndex.value = 1;
	    document.saveForm.action = "<c:url value='/securityDay/sdCheckListGroupList.do'/>";
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
    	<div class="subTT"><span>Security Day 점검표 그룹 상세 정보</span></div>
    	<div class='popTT'><img src='/img/icon_arw4.jpg' /> 점검표 그룹 정보</div>
    	<div class="sch_view" style="margin-top: 5px;">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:85%;" />
                </colgroup>
                <tr> 
                    <th>점검표 그룹 명</th>
                    <td>
                    	<input type="text"  class="srh" id="clGroupNm" name="clGroupNm" value="${sdCheckListGroupInfo.clGroupNm }" maxlength="200" class="srh" style="width:98%">
                    </td>
				</tr>                    
                <tr>
					<th>점검 활성화 기간</th>
					<td>
                    	<input type="text"  class="srh" id="checkStartDay" name="checkStartDay" value="${sdCheckListGroupInfo.checkStartDay }" class="srh" style="width:40%"> ~ 
                    	<input type="text"  class="srh" id="checkEndDay" name="checkEndDay" value="${sdCheckListGroupInfo.checkEndDay }"  class="srh" style="width:40%">
                    </td>
				</tr>
				<tr>
					<th>지수화 기간</th>
					<td colspan="3">
                    	<input type="text"  class="srh" id="idxStartDay" name="idxStartDay" value="${sdCheckListGroupInfo.idxStartDay }" class="srh" style="width:40%"> ~ 
                    	<input type="text"  class="srh" id="idxEndDay" name="idxEndDay" value="${sdCheckListGroupInfo.idxEndDay }"  class="srh" style="width:40%">
                    </td>
				</tr>
				<tr>
					<th>예외자<br/><br/>
					<a href="javascript:$.targetAdd('3','2','0')" class="btn_black"><span>예외자 추가</span></a>
					</th>
					<td colspan="3">
                    	<table cellpadding="0" cellspacing="0" id="targetExInfoTb" class="tblInfoType3" style="width:98%; float: left;" >
			                <colgroup>
								<col style="width:10%">
								<col style="width:45%">
								<col style="width:10%">
								<col style="width:15%">
							</colgroup>
							<tr>
								<th style="padding: 8px; height: 10px;">유형</th>
								<th style="padding: 8px; height: 10px;">이름</th>
								<th style="padding: 8px; height: 10px;">코드</th>
								<th style="padding: 8px; height: 10px;">대상자 삭제</th>
							</tr>
							<tbody>
							<c:forEach var="result" items="${targetList}" varStatus="status">
							<c:if test="${result.gubun eq '2'}">
								<tr>
									<td>${result.targetType }</td>								
									<td>${result.targetNm }</td>
									<td>${result.targetCode }</td>
									<td><a href="javascript:void(0);" onclick="$.btn_del('${result.clGroupNo }','${result.sdCheckNo }','${result.targetCode }','${result.gubun}', this)" style="cursor:pointer;" ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
								</tr>
							</c:if>
							</c:forEach>
							</tbody>
			            </table>
                    </td>
				</tr>
				<tr> 
                    <th>점검표 추가</th>
                    <td>
                    	<select id='clInfo' name='clInfo' onchange="javascript:$.clAdd()">
                    	<option value="" >추가 할 점검표 선택</option>
    					<c:forEach var="result" items="${sdCheckList}" varStatus="status">
    						<option value='${result.sdCheckNo }/${result.checklistNm }' >${result.checklistNm }</option>
    					</c:forEach>	
    					</select>
                    </td>
                </tr>
			</table>
        </div>
        
        
		<div>
	        <div class='popTT' style="padding-top: 30px;">
	        	<img src='/img/icon_arw4.jpg' /> 점검표 정보
	        </div>
	    	<div class="sch_view" style="margin-top: 5px;">
	            <table cellpadding="0" cellspacing="0" class="tblInfoType" id="clTb">
	                <colgroup>
						<col style="width:10%">
						<col style="width:15%">
						<col style="width:10%">
						<col style="width:50%">
						<col style="width:10%">
						<col style="width:5%">
					</colgroup>
					<c:forEach var="result" items="${sdClGroupMappingList}" varStatus="status">
					<tr>
						<th>점검표명</th>
						<td>${result.checklistNm}</td>
						<th>대상자<br/><br/>
						<a href="javascript:$.targetAdd('3', '1', '${result.sdCheckNo}')" class="btn_black" ><span>대상자 추가</span></a>
						</th>
						<td>
	                    	<table cellpadding="0" cellspacing="0" id="targetInfoTb${result.sdCheckNo}" class="tblInfoType3" style="width:98%;float: left;" >
				                <colgroup>
									<col style="width:10%">
									<col style="width:45%">
									<col style="width:10%">
									<col style="width:15%">
								</colgroup>
								<tr>
									<th style="padding: 8px; height: 10px;">유형</th>
									<th style="padding: 8px; height: 10px;">이름</th>
									<th style="padding: 8px; height: 10px;">코드</th>
									<th style="padding: 8px; height: 10px;">대상자 삭제</th>
								</tr>
								<tbody>
								<c:forEach var="targetList" items="${targetList}" varStatus="status">
								<c:if test="${targetList.gubun eq '1' and result.sdCheckNo eq targetList.sdCheckNo}">
									<tr>
										<td>${targetList.targetType }</td>								
										<td>${targetList.targetNm }</td>
										<td>${targetList.targetCode }</td>
										<td><a href="javascript:void(0);" onclick="$.btn_del('${targetList.clGroupNo }','${targetList.sdCheckNo }','${targetList.targetCode }','${targetList.gubun}', this)" style="cursor:pointer;" ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
									</tr>
								</c:if>
								</c:forEach>
								</tbody>
				            </table>
						</td>
						<th>점검표 삭제</th>
						<td>
							<li>
		                    <a class='btn_del' style='cursor:pointer;' onClick="$.clInfo_del('${result.clGroupNo }','${result.sdCheckNo }',this)"><img src='/img/btn_delete.png' alt='delete' title='delete' /></a>
		                    </li>
						</td>
					</tr>
					</c:forEach>
	            </table> 
	        </div>
	       	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }" />
	       	<input type="hidden" id="clGroupNo" name="clGroupNo" value="${sdCheckListGroupInfo.clGroupNo }" />
	       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
       	</div>
       	
       	<div class="btn_black2">
       	<c:if test='${searchVO.formMod eq "U"}'>
       		(미리보기는 저장 후 확인 가능 합니다.)<a class="btn_black preView"><span>미리보기</span></a>
       		<a class="btn_black btn_delete"><span>삭제</span></a>
       	</c:if>
       		<a class="btn_black btn_save"><span>저장</span></a>
	        <a class="btn_black" href="/securityDay/sdCheckListGroupList.do"><span>취소</span></a> 
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