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
		
		var gubun = $('input:radio[name=gubun]:checked').val();
		
		
		var chekInfo = [];
		
		var failInfo = "";
		var failCnt = 0;
		var url = '';
		chekInfo = $("#exInfo").val().split("\n");
		
		if(gubun =='I'){

			for(var i=0; i <chekInfo.length ; i++){
				var str = chekInfo[i].replace(/ /gi, "");
				if(str !=""){
					if(!str.match(ip) && !str.match(rangeIp) && !str.match(bitIp)){
						if(failCnt >0){
							failInfo = failInfo +" , "+str;
						}else {
							failInfo = str;
						}
						failCnt = failCnt +1;
					}else{
						if(str.match(rangeIp)){
							if(!rangeIpCheck(str)){
								if(failCnt >0){
									failInfo = failInfo +" , "+str;
								}else {
									failInfo = str;
								}
								failCnt = failCnt +1;
							}
						}
					}
				}else{
					chekInfo.splice(i,1);
				} 
			}
			
			if(failCnt>0){
				alert(failInfo + "\n" + "해당 아이피를 확인하세요.");
				$("#exInfo").focus();
				return false;
			}
		
		}else{
			for(var i=0; i <chekInfo.length ; i++){
				var str = chekInfo[i].replace(/ /gi, "");
				if(str.length <0 && str.length >8){
					if(failCnt >0){
						failInfo = failInfo +" , "+str;
					}else {
						failInfo = str;
					}
					failCnt = failCnt +1;
				}
			}
			
			if(failCnt>0){
				alert(failInfo + "\n" + "해당 사번을 확인하세요.");
				$("#exInfo").focus();
				return false;
			}
		}
		
		
		if(chekInfo.length<=0){
			alert("예외 정보를 입력하세요.");
			$("#exInfo").focus();
			return;
		}
		
		var polSelectList = [];
		 $("input:checkbox[name=sec_pol_id]:checked").each(function(){
			 polSelectList.push($(this).val());
		 });
		 
		 $("#polSelectList").val(polSelectList.toString());
		 	
		
		if(polSelectList=="" || polSelectList == null){
			alert("예외 정책을 선택하세요.");
			return;
		}
		

		
		
		
		//$("#sec_pol_id").attr("disabled",false);
		
		var formData = $("#saveForm").serialize();
		
		 $.ajax({
			url: '/exception/setExInfoSave.do',
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
			}	 ,
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
	
	$(".btn_app").click(function(){
		
		var reqSeq = $("#reqSeq").val();
		
		var data = {
				reqSeq:reqSeq
		}
		
		 $.ajax({
			url: '/exception/reqExIpAppUpdate.do',
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
					alert("승인이 완료 되었습니다.");
					goList();					
				}else{alert(data.MSG); }				
			}			
		});
	});
	
	$('.btn_delete').click(function(){
		if(!confirm('삭제 하시겠습니까?')){
			return false;
		}
		
		var reqSeq = $("#reqSeq").val();
		
		
		var data = {
				reqSeqList:reqSeq
		}
		
			$.ajax({
				url : '/exception/reqExIpInfoDelete.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					if(jqXHR.status == 401){
				 		alert("인증정보가 만료되었습니다.");
						location.href="/";
					}else{
						alert(textStatus + "\r\n" + errorThrown);	
					}
				},
				success : function(data) {
					if (data.ISOK) {
						goList();
					} else {
						alert(data.MSG);
					}

				}
		});
		
		
	});
	
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
    	<div class="subTT"><span>예외처리 정보</span></div>
    	<!-- <div class="popTT">
    	 IP 등록 예제<br />
    	 EX) 10.0.0.1
    	     10.0.0.2<br />
    	      ※ 등록 하려는 IP는 ","로 구분해서 입력해 주세요.
    	</div> -->
    	<div class="sch_view">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:35%;" />
                    <col style="width:15%;" />
                    <col style="width:35%;" />
                </colgroup>
                 <tr> 
                    <th>제목
                    </th>
                    <td class="radio_button" colspan="3" >
    					<input type='text' class="srh" id='subject' name='subject' style='width:99%;height:20px;border:1px solid #AEAEAE;' maxlength='1000' value='${reqExIpInfo.subject }' />
    				</td>
                </tr>  
                <tr> 
                    <th>구분
                    </th>
                    <td class="radio_button" >
    					<label><input type='radio' name='exAction' value='I' <c:if test="${reqExIpInfo.exAction == 'I'}" >checked</c:if> /><span>추가 </span></label>
    					<label><input type='radio' name='exAction' value='D' <c:if test="${reqExIpInfo.exAction == 'D'}" >checked</c:if> /><span>삭제</span></label>
    				</td>
    				<th>예외
                    </th>
                    <td class="radio_button" >
    					<label><input type='radio' name='gubun' value='I' <c:if test="${reqExIpInfo.gubun == 'I'}" >checked</c:if> /><span>IP </span></label>
    					<label><input type='radio' name='gubun' value='E' <c:if test="${reqExIpInfo.gubun == 'E'}" >checked</c:if> /><span>사번</span></label>
    				</td>
                </tr>               
                <tr class="Pol_Row">
    				<th>정책 선택</th>
    				<td class="ck_button" colspan="3" >
    				<label style="min-width:230px;"><input type='checkbox' name='checked_all' value='' /><span>전체 선택</span></label>
        			<c:forEach var="result" items="${reqExIpPolicyList}" varStatus="status">
						<label style="min-width:230px;"><input type='checkbox' name='sec_pol_id' value='${result.sec_pol_id}' <c:if test="${result.is_selected == 'Y'}" >checked</c:if> /><span>${result.policyname} </span></label>
					</c:forEach>
    				</td>
    			</tr>
    			<%-- <tr>
					<th>예외 시작일</th>
					<td>
						<input type="text"  class="srh" id="exStartDate" name="exStartDate" class="srh" style="width:40%" value=${reqExIpInfo.exStartDate }>
					</td>
					<th>예외 기간</th>
					<td>
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='30' <c:if test="${reqExIpInfo.exDays == '30'}" >checked</c:if>> 30일 &nbsp;&nbsp;&nbsp;
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='90' <c:if test="${reqExIpInfo.exDays == '90'}" >checked</c:if>> 90일 &nbsp;&nbsp;&nbsp;
						<input type='radio' class="input_check" name='r_exDays' id="r_exDays" value='99' <c:if test="${reqExIpInfo.exDays == '99'}" >checked</c:if>> 무제한 &nbsp;&nbsp;&nbsp;
    				</td>
				</tr>  --%>
				<tr> 
                    <th>신청 이유</th>
                    <td colspan="3">
                    	<textarea name="reason" id="reason" cols="15" rows="4" style="overflow:auto;" class="inputarea" >${reqExIpInfo.reason }</textarea>
                    </td>
                </tr>                    
                <tr>
					<th>예외 정보</th>
					<td style='padding:5px 0 5px 10px;' colspan="3">
						<textarea name="exInfo" id="exInfo" cols="45" rows="20" style="overflow:auto;" class="inputarea" >${reqExIpInfo.exInfo }</textarea>
					</td>
				</tr>
			</table>
            <div class="btn_black2">
            	<a class="btn_black btn_save"><span>저장</span></a>
            	<a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
        </div>
       	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }" />
		<input type="hidden" id="exSeq" name="exSeq" value="${reqExIpInfo.exSeq }" />
		<input type="hidden" id="polSelectList" name="polSelectList"  />
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