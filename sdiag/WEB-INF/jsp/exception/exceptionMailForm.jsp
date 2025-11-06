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
	$(".btn_save").click(function(){
		
		if($("#op_indc").val() ==""){
			alert("구분을 선택하세요.");
			$("#op_indc").focus();
			return;
		}
		
		var chekEmpNO = [];
		
		var failEmpNO = "";
		var failCnt = 0;
			
		chekEmpNO = $("#emp_no").val().split("\n");
		
		for(var i=0; i <chekEmpNO.length ; i++){
			var str = chekEmpNO[i].replace(/ /gi, "");
			if(str !=""){
				if(str.length !=8 ||  str.match(/[^0-9]/) != null){
					if(failCnt >0){
						failEmpNO = failEmpNO +" , "+str;
					}else {
						failEmpNO = str;
					}
					failCnt = failCnt +1;
				}
				
			}else{
				chekEmpNO.splice(i,1);
			} 
		}
		
		
		if(failCnt>0){
			alert(failEmpNO + "\n" + "해당 사번을 확인하세요.");
			$("#emp_no").focus();
			return;
		}
		
		
		if(chekEmpNO.length<=0){
			alert("사번을 입력하세요.");
			$("#emp_no").focus();
			return;
		} 
		
		
		
		
		if(!confirm('저장 하시겠습니까?')){
			return;
		}
		$("#op_indc").attr("disabled",false);
		var formData = $("#saveForm").serialize();
		
		
		 $.ajax({
			url: '/exception/exceptionMailSave.do',
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
					goList();					
				}else{alert(data.MSG); }				
			},
			beforeSend: function () {
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
                $('#loading').css('position', 'absolute');
                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
                $('#loading').css('top', $('.sch_view').offset().top);
                $('#loading').css('padding-top', 100);
                $('#loading').show().fadeIn('fast');
           },
           complete: function () {
               $('#loading').fadeOut();
           }
		});
		
	});
	
	function goList(){
		document.saveForm.action = "<c:url value='/exception/exceptionMailList.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	}
	
	$.empNoInfo = function(){
		var opIndc = $("#op_indc").val();
		var params = {op_indc:opIndc};
		
		 $.ajax({
			url: '/exception/exceptionMailInfo.do',
			data: params,
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
					$("#emp_no").val(data.empNoInfo);				
				}else{alert(data.MSG); }				
			}			
		});
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
    <form:form id="saveForm" name="saveForm" method="post">
    	<div class="subTT"><span>메일발송 예외자 관리</span></div>
    	<div class="sch_view">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:85%;" />
                </colgroup>
                <tr> 
                    <th>정책1</th>
                    <td>
                    	<select id="op_indc" name="op_indc" disabled="disabled">
                    	<option value="" >선택 ${exceptionMailInfo.op_indc} </option>
    						<option value='04' <c:if test="${exceptionMailInfo.op_indc == '04'}" >selected</c:if> >주간 보고서</option>
    						<option value='05' <c:if test="${exceptionMailInfo.op_indc == '05'}" >selected</c:if> >월간 보고서</option>
    						<option value='06' <c:if test="${exceptionMailInfo.op_indc == '06'}" >selected</c:if> >공지사항</option>
    					</select>
                    </td>
                </tr>                    
                <tr>
					<th>내용</th>
					<td style='padding:5px 0 5px 10px;'>
						<textarea name="emp_no" id="emp_no" cols="45" rows="20" style="overflow:auto;" class="inputarea" >${exceptionMailInfo.emp_no}</textarea>
					</td>
				</tr>
			</table>
            <div class="btn_black2"><a class="btn_black btn_save"><span>저장</span></a>
            <a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
        </div>
		<input type='text' style='display:none;' />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
     <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>