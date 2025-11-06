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
	
    $(".btn_add").click(function(){
    	var pw = window.open('/group/popOrgInfoSearch.do?reqCode=2','orgInfo_popup','width=800, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
	});
       
    
	$(".btn_save").click(function(){
		
		$("#groupType").val($("#s_groupType").val());
		
		if($("#groupNm").val() ==""){
			alert("그룹 명을 입력 하세요.");
			$("#groupNm").focus();
			return;
		}
		var formData = $("#saveForm").serialize();
		
		
		 $.ajax({
			url: '/group/groupInfoSave.do',
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
					//$("#sdCheckNo").val(data.sdCheckNo);
					goPage();	
				}else{alert(data.MSG); }				
			}
		});
    
	});
	
	$(".code_check").click(function(){
		
    	var groupCode = $("#groupCode").val();
    	var data = {
    			groupCode:groupCode
		}
		
		 $.ajax({
			url: '/group/groupCodeCheck.do',
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
					if(data.result){
						alert("사용 하셔도 되는 코드 입니다.");
						$("#checkYn").val("Y");
					}else{
						alert("이미 사용 되고 있는 코드 입니다.");
						$("#checkYn").val("N");
					}
					
				}else{alert(data.MSG); }		
			}
		});
    
	});
	
	
	$.btn_del = function(orgInfo, obj){ 
		//alert("ddd");
		
		var orgInfo = orgInfo;
		var groupCode = $("#groupCode").val();
		
		//alert("orgInfo ="+orgInfo );
		
		if(orgInfo == ""){
			//alert("ee");
			$(obj).parent().parent().remove();
		}
		else{
			var data = {
					orgInfo : orgInfo,
					groupCode : groupCode
			}
			
			
			 $.ajax({
				url: '/group/groupDetailInfoDelete.do',
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
	
	$(".btn_delete").click(function(){
		
		//var orgInfo = $(this).attr('orgInfo');
		var groupCode = $("#groupCode").val();
		
		
	
		var data = {
				groupCodeList : groupCode
		}
			
		
		 $.ajax({
			url: '/group/groupInfoDelete.do',
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
	function goPage() {

		document.saveForm.pageIndex.value = 1;
	    document.saveForm.action = "<c:url value='/group/groupInfoList.do'/>";
	    document.saveForm.method = 'post';
	    document.saveForm.submit();
	}
	
	
	$.groupType = function(){
		var type = $("#s_groupType").val();
		
		if(type =="2" || type =="3" || type =="4"){
			$("#groupType1").css("display", "none");
			$("#groupInfoTr").css("display", "");
			$("#groupInfo option").remove();
			$("#groupType5").css("display", "none");
			/* if($("#formMod").val() == "I"){
				$("#groupInfoTb > tbody").empty();
			} */
			
			var data = {
					groupType : type
			}
			
			 $.ajax({
					url: '/group/getSelectBoxList.do',
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
							$("#groupInfo").append("<option value=''>선택</option>");
							if(type == '3'){
								$("#groupInfo").append("<option value='00/기타 협력사'>기타 협력사</option> ");
							}
							for(var i=0; i<data.selecBoxList.length; i++){
								
								$("#groupInfo").append("<option value='"+data.selecBoxList[i].minr_code+"/"+data.selecBoxList[i].code_desc+"' >"+data.selecBoxList[i].code_desc+"</option>");
							}
							
							
						}else{alert(data.MSG); }				
					}
				});
			
			
			$("#box").css("display", "");
			$("#groupInfoTb> tbody").empty();
		}else if(type == 1){
			$("#groupType1").css("display", "");
			$("#groupInfoTr").css("display", "");
			$("#groupType5").css("display", "none");
			$("#box").css("display", "none");
			$("#groupInfoTb> tbody").empty();
		}else{
			$("#groupType"+type).css("display", "");
			$("#box").css("display", "none");
			$("#groupInfoTr").css("display", "none");
			$("#groupInfoTb> tbody").empty();
		}
	};
	
	$.groupInfoAdd = function(){
		var groupInfo = $("#groupInfo").val();
		
		var arr = new Array();
		arr = groupInfo.split("/");
		
		var orgInfo = arr[0];
		var orgNm = arr[1];
		
		var type = parseInt($("#s_groupType").val());
		
		var orgType =type+1;
		
		$.addRow(orgInfo, orgType, orgNm);
		
	};
	
	$.addRow = function(orgInfo, orgType, orgNm){
		//var orgInfo = $(this).attr('orgInfo');
		//var orgType = $(this).attr('orgType');
		//var orgNm = $(this).attr('orgNm');
		 //alert(orgType);
		
		var trAdd = "<tr>";
		if(orgType=="1") {
			trAdd +="<td>개인</td>";
		} else if(orgType=="2") {
			trAdd +="<td>조직</td>";
		}else if(orgType=="3") {
			trAdd +="<td>취약자</td>";
		}else if(orgType=="4") {
			trAdd +="<td>협력사</td>";
		}else if(orgType=="5") {
			trAdd +="<td>복무정보</td>";
		}
		
		trAdd +="<td>"+orgNm+"</td>";
		trAdd +="<td>"+orgInfo+"</td>";
		trAdd +='<td><a href="javascript:void(0);" onclick="$.btn_del(\'\', this)" style="cursor:pointer;" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a></td>';
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer; ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		trAdd +="<input type='hidden' id='orgType' name='orgType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='orgNm' name='orgNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='orgInfo' name='orgInfo' value='"+orgInfo+"' />";
		trAdd +="</tr>";
		$("#groupInfoTb").append(trAdd);
		
		//self.close();
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
    <form:form id="saveForm" name="saveForm" method="post" >
    	<div class="subTT"><span>그룹 관리 상세 정보</span></div>
    	<div class='popTT'><img src='/img/icon_arw4.jpg' /> 그룹 정보</div>
    	<div class="sch_view" style="margin-top: 5px;">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:30%;" />
                    <col style="width:15%;" />
                    <col style="width:30%;" />
                </colgroup>
                <tr> 
                    <th>그룹 유형</th>
                    <td>
	                    <select id='s_groupType' name='s_groupType' <c:if test='${searchVO.formMod  eq "U"}'> disabled="disabled" </c:if>  onchange="javascript:$.groupType()">
	                    	<option value="" >선택</option>
	   						<option value='1' <c:if test="${groupInfo.groupType == '1'}" >selected</c:if> >조직/개인</option>
	   						<option value='2' <c:if test="${groupInfo.groupType == '2'}" >selected</c:if> >취약자</option>
	   						<option value='3' <c:if test="${groupInfo.groupType == '3'}" >selected</c:if> >협력사</option>
	   						<option value='4' <c:if test="${groupInfo.groupType == '4'}" >selected</c:if> >복무정보</option>
	   						<option value='5' <c:if test="${groupInfo.groupType == '5'}" >selected</c:if> >수동 입력</option>
	   						<%-- <option value='6' <c:if test="${groupInfo.groupType == '6'}" >selected</c:if> >수동 업로드</option>
	   						<option value='7' <c:if test="${groupInfo.groupType == '7'}" >selected</c:if> >수동 쿼리</option> --%>
	    				</select> 
	    				<input type="hidden" id="groupType" name="groupType" >
                    </td>
                    <th>그룹 명</th>
					<td>
                    	<input type="text"  class="srh" id="groupNm" name="groupNm" value="${groupInfo.groupNm }" class="srh" style="width:98%">
                    </td>
                </tr>                    
                <tr>
					<th>그룹 설명</th>
					<td colspan="3">
                    	<textarea  id='groupExt' rows='8' name='groupExt' style='border: 1px solid rgb(174, 174, 174); border-image: none; width: 99%;' >${groupInfo.groupExt }</textarea> 
                    </td>
				</tr>
				
				<tr id="box" <c:if test="${groupInfo.groupType == '1' or groupInfo.groupType == '5' or empty groupInfo.groupType  }" > Style='display:none;' </c:if>> 
                    <th>그룹 대상 선택</th>
                    <td>
                    	<select id='groupInfo' name='groupInfo' onchange="javascript:$.groupInfoAdd()">
                    	<option value="" >선택</option>
                    	<c:if test="${groupInfo.groupType == '3'}">
   							<option value='00/기타 협력사' >기타 협력사</option>
   						</c:if> 
    					<c:forEach var="result" items="${selecBoxList}" varStatus="status">
    						<option value='${result.minr_code }/${result.code_desc }' >${result.code_desc }</option>
    					</c:forEach>	
    					</select>
                    </td>
                </tr>
                
                <tr id="groupType5" <c:if test="${groupInfo.groupType != '5' or empty groupInfo.groupType  }" > Style='display:none;' </c:if>> 
                    <th>그룹 대상 입력</th>
                    <td colspan="3" style='padding:5px 0 5px 10px;'>
						<textarea name="empNoList" id="empNoList" cols="45" rows="20" style="overflow:auto;" class="inputarea" placeholder="여러개의 사번은  엔터키를 처서 한줄에 하나의 사번으로 입력하세요. 
ex)
12345566
12345656 "></textarea>

 <tr id="groupType7" <c:if test="${groupInfo.groupType != '7' or empty groupInfo.groupType  }" > Style='display:none;' </c:if>> 
                    <th>그룹 대상 입력</th>
                    <td colspan="3" style='padding:5px 0 5px 10px;'>
						<textarea name="queryStr" id="queryStr" cols="45" rows="20" style="overflow:auto;" class="inputarea" ></textarea>
					</td>
                </tr>
               <%--  <tr id="groupType6" <c:if test="${groupInfo.groupType != '6' or empty groupInfo.groupType  }" > Style='display:none;' </c:if>> 
                    <th>그룹 대상 파일 업로드</th>
                    <td colspan="3" style='padding:5px 0 5px 10px;'>
						<input type="file" type="file" id="file" name="file" maxlength="24" class="srh" style="width:200px" />
					</td>
                </tr> --%>
                <tr id="groupInfoTr">
					<th>그룹 대상<br/><br/>
					<a id="groupType1" style="display: <c:if test="${groupInfo.groupType != '1'}" > none</c:if>;" class="btn_black btn_add"><span>대상자 추가</span></a>
					</th>
					<td colspan="3">
						
						<table cellpadding="0" cellspacing="0" class = "tblInfoType3" id="groupInfoTb" style="width:60% float: left;">
			                <colgroup>
								<col style="width:10">
								<col style="width:20">
								<col style="width:20">
								<col style="width:20">
							</colgroup>
							<thead>
								<th>유형</th>
								<th>이름</th>
								<th>코드</th>
								<th>삭제</th>
							</thead>
							<tbody>
							<c:forEach var="result" items="${resultList}" varStatus="status">
							<tr>
								<td>${result.orgType }</td>								
								<td>${result.orgNm }</td>
								<td>${result.orgInfo }</td>
								<td><a href="javascript:void(0);" onclick="$.btn_del('${result.orgInfo }', this)" style="cursor:pointer;" ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
							</tr>
							</c:forEach>
							</tbody>
			            </table>
		        	</td>
				</tr>
				
			</table>
        </div>
       	<div class="btn_black2">
       	<c:if test='${searchVO.formMod eq "U"}'>
       		<a class="btn_black btn_delete"><span>삭제</span></a>
       	</c:if>
       		<a class="btn_black btn_save"><span>저장</span></a>
	        <a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
	    </div>
	    
	    <input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }" />
	    <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
	    <input type="hidden" name="groupCode" id="groupCode" value="<c:out value="${groupInfo.groupCode}" />" />
	</form:form>
    </div>
    <input type="hidden" id="checkYn" value="N">
    
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>