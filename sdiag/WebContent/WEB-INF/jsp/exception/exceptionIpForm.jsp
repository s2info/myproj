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
	var ip = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;				// 단일 ip
	var rangeIp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\~(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;  // range ip
	var bitIp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\/([8,9]|1[0-9]|2[0-9]|3[0-2])$/;		// bit ip	
	
	$(".btn_save").click(function(){
		
		if($("#sec_pol_id").val() ==""){
			alert("정책을 선택하세요.");
			$("#sec_pol_id").focus();
			return;
		}
		
		var chekIp = [];
		
		var failIp = "";
		var failCnt = 0;
			
		chekIp = $("#ip").val().split("\n");
		
		for(var i=0; i <chekIp.length ; i++){
			var str = chekIp[i].replace(/ /gi, "");
			if(str !=""){
				if(!str.match(ip) && !str.match(rangeIp) && !str.match(bitIp)){
					if(failCnt >0){
						failIp = failIp +" , "+str;
					}else {
						failIp = str;
					}
					failCnt = failCnt +1;
				}else{
					if(str.match(rangeIp)){
						if(!rangeIpCheck(str)){
							if(failCnt >0){
								failIp = failIp +" , "+str;
							}else {
								failIp = str;
							}
							failCnt = failCnt +1;
						}
					}
				}
			}else{
				chekIp.splice(i,1);
			} 
		}
		
		
		if(failCnt>0){
			alert(failIp + "\n" + "해당 아이피를 확인하세요.");
			$("#ip").focus();
			return;
		}
		
		if(chekIp.length<=0){
			alert("아이피를 입력하세요.");
			$("#ip").focus();
			return;
		}
		
		
		if(!confirm('저장 하시겠습니까?')){
			return;
		}
		
		$("#sec_pol_id").attr("disabled",false);
		var formData = $("#saveForm").serialize();
		
		 $.ajax({
			url: '/exception/exceptionIpSave.do',
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
		document.saveForm.action = "<c:url value='/exception/exceptionIpList.do'/>";
		document.saveForm.method = "post";
		document.saveForm.submit();
	}
	
	function rangeIpCheck(ip){
		var result = true;
		var range_ip = [];
		var arr_ip = [];
		
		range_ip = ip.split("~");
		arr_ip = range_ip[0].split(".");
		var ip1 = arr_ip[0]+arr_ip[1]+arr_ip[2];
		
		arr_ip = [];
		arr_ip = range_ip[1].split(".");
		var ip2 = arr_ip[0]+arr_ip[1]+arr_ip[2];
		//alert(ip1 + " ===== " + ip2);
		if(ip1 != ip2){
			result = false;
		}
		
		return result;
	}
	
	
	$.ipInfo = function(){
		var polId = $("#sec_pol_id").val();
		var params = {sec_pol_id:polId};
		
		 $.ajax({
			url: '/exception/exceptionIpInfo.do',
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
					$("#ip").val(data.ipInfo);
					if($("#ip").val() !="" && $("#ip").val() !=null ){
						$("#formMod").val("U");
					}else{
						$("#formMod").val("");
					}
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
    	<div class="subTT"><span>예외처리 IP</span></div>
    	<div class="popTT">
    	 IP 등록 예제<br />
    	 EX) 단일 IP : 10.0.0.1<br />
    	            범위 IP : 10.0.0.1~10.0.0.100 (아이피와 ~ 사이에 공백 없이 입력하세요.)<br />
    	            비트 IP : 10.0.0.1/32
    	</div>
    	<div class="sch_view">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:85%;" />
                </colgroup>
                <tr> 
                    <th>정책</th>
                    <td>
                    	<select id='sec_pol_id' name='sec_pol_id' <c:if test='${exceptionIpInfo.formMod  eq "U"}'> disabled="disabled" </c:if>  onchange="javascript:$.ipInfo()">
                    	<option value="" >선택</option>
    					<c:forEach var="result" items="${polIdxList}" varStatus="status">
    						<option value='${result.sec_pol_id }' <c:if test="${result.sec_pol_id == exceptionIpInfo.sec_pol_id}" >selected</c:if> >${result.sec_pol_desc }</option>
    					</c:forEach>	
    					</select>
                    </td>
                </tr>                    
                <tr>
					<th>내용</th>
					<td style='padding:5px 0 5px 10px;'>
						<textarea name="ip" id="ip" cols="45" rows="20" style="overflow:auto;" class="inputarea" >${exceptionIpInfo.ip }</textarea>
					</td>
				</tr>
			</table>
            <div class="btn_black2"><a class="btn_black btn_save"><span>저장</span></a>
            <a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
        </div>
       	<input type="hidden" id="formMod" name="formMod" value="${exceptionIpInfo.formMod }" />
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