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
	
	 $('input:checkbox[name=checked_all]').click(function(){
    	var ischecked = $(this).is(':checked');
    	$(this).closest('td').find('input:checkbox[name=sec_pol_id]').each(function(){
    		$(this).prop('checked', ischecked);
    	});
    });
	
	
	$(".btn_save").click(function(){
		
		var chekIp = [];
		
		var failIp = "";
		var failCnt = 0;
			
		chekIp = $("#ip").val().split(",");
		
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
		
		var polSelectList = [];
		 $("input:checkbox[name=sec_pol_id]:checked").each(function(){
			 polSelectList.push($(this).val());
		 });
		 
		 $("#polSelectList").val(polSelectList.toString());
		 
		var exDays = $("input:radio[name=r_exDays]:checked").val();
		
		$("#exDays").val(exDays);
		
		//$("#sec_pol_id").attr("disabled",false);
		
		var formData = $("#saveForm").serialize();
		
		 $.ajax({
			url: '/exception/userReqExIpInfoSave.do',
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
					alert("예외 신청이 정상적으로 등록 되었습니다. \n 해당 창을 닫겠습니다.");
			 		window.self.close();
					
				}else{alert(data.MSG); }				
			}		
		});
		
	});
	
	function goList(){
		document.saveForm.action = "<c:url value='/exception/reqExIpInfoList.do'/>";
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
	
	
	$.empNoCheck = function(){
		var reqSeq = $("#reqSeq").val();
		var reqEmpNo = $("#reqEmpNo").val();
		var params = {
				reqSeq:reqSeq
				,reqEmpNo:reqEmpNo};
		
		 $.ajax({
			url: '/exception/empNoCheck.do',
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
					if(data.result == "Y"){
						$("#reqEmpNm").val(data.reqExIpInfo.reqEmpNm);
						$("#ip").val(data.reqExIpInfo.ip);
						$("#empNocheck").val(data.result);
						alert("예외 신청을 진행 하세요.");
					}else{
						$("#empNocheck").val(data.result);
						alert("관리자에게 예외 신청 후 진행 해주세요.");
					}
				}else{alert(data.MSG); }				
			}			
		});
	};
	$('#exStartDate').datepicker({
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
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
	
});
</script>
</head>
<body>
<!-- S : contents -->
<div id="ly_block1"  style="margin: 15px; max-width: 1280px;">
	<!-- S : center contents -->
    <form:form id="saveForm" name="saveForm" method="post">
    	<div class="subTT"><span>IP 예외처리 신청</span></div>
    	<div class="popTT">
    	 ◎ IP 예외 신청 안내<br />
    	  - 사번 입력 후 유효성 검사 진행 후 진행 하셔야 합니다.<br /><br />
    	 ◎ IP 등록 예제<br />
    	 EX) 10.0.0.1,10.0.0.2<br />
    	      ※ 등록 하려는 IP는 ","로 구분해서 입력해 주세요.
    	</div>
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:35%;" />
                    <col style="width:15%;" />
                    <col style="width:35%;" />
                </colgroup>
                <tr> 
                    <th>사번<br/><br/>
						<a href="javascript:$.empNoCheck()" class="btn_black"><span>유효성 검사</span></a>
					</th>
                    <td>
                    	<input type="text" class="srh" name="reqEmpNo" id="reqEmpNo">
                    </td>
                    <th>이름</th>
                    <td>
                    	<input type="text" class="srh" name="reqEmpNm" id="reqEmpNm" readonly="readonly">
                    </td>
                </tr>
                <tr class="Pol_Row">
    				<th>정책 선택</th>
    				<td class="ck_button" colspan="3">
    				<label style="min-width:230px;"><input type='checkbox' name='checked_all' value='' /><span>전체 선택</span></label>
        			<c:forEach var="result" items="${reqExIpPolicyList}" varStatus="status">
						<label style="min-width:230px;"><input type='checkbox' name='sec_pol_id' value='${result.sec_pol_id}'><span>${result.policyname} </span></label>
					</c:forEach>	
    				</td>
    			</tr>
    			<tr>
					<th>예외 시작일</th>
					<td>
						<input type="text"  class="srh" id="exStartDate" name="exStartDate" value="" class="srh" style="width:40%">
					</td>
					<th>예외 기간</th>
					<td>
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='30'> 30일 &nbsp;&nbsp;&nbsp;
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='90'> 90일 &nbsp;&nbsp;&nbsp;
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='99'> 무제한 &nbsp;&nbsp;&nbsp;
    				</td>
				</tr> 
				<tr> 
					<th>ITMS ID</th>
                    <td>
                    	<input type="text" class="srh" name="itmsId" id="itmsId">
                    </td>
                    <th>신청 이유</th>
                    <td>
                    	<textarea name="reason" id="reason" cols="15" rows="4" style="overflow:auto;" class="inputarea" ></textarea>
                    </td>
                </tr>
                <tr>
					<th>IP</th>
					<td colspan="3">
						<textarea name="ip" id="ip" cols="45" rows="5" style="overflow:auto;" class="inputarea" ></textarea>
					</td>
				</tr>
			</table>
            <div class="btn_black2"><a class="btn_black btn_save"><span>저장</span></a>
            <a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
       	<input type="hidden" id="reqSeq" name="reqSeq" value="${reqExIpInfo.reqSeq }" />
       	<input type="hidden" id="polSelectList" name="polSelectList"  />
       	<input type="hidden" id="exDays" name="exDays"  />
       	<input type="hidden" id="empNocheck" name="empNocheck" value="N" />
		</form:form>
    <!-- E : center contents -->
    </div>
</body>
</html>