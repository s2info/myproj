<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script type="text/javascript" language="javascript">
$(function () {		
	
	$.logListSearch = function(){
		
		if($("#polId").val() == ""){
			alert("서버 이행관리 정책을 선택하세요.");
			return false;
		}
		
		if($("#role_code").val() == "1" && ($("#s_EmpNo").val()=="" && $("#s_EmpNm").val()=="")){
			alert("사번 또는 이름 정보를 입력하세요.");
			return false;
		}
		
		goPage(1);
		
	};
	
/* 	$('#selAll_checkbox').click(function(){
		var ischeck = $(this).is(':checked');
		$('input:checkbox[name=c_seq]').prop('checked',ischeck);

	}); */
	
	function goPage(page) {

		document.listForm.pageIndex.value = page;
	    document.listForm.action = "<c:url value='/server/serverPolLogList.do'/>";
	    document.listForm.submit();
	}
	 
	$.setLogInfo = function (rowSeq){
		var rgdtDate = $("#rgdtDate"+rowSeq).val();
		var seq = $("#seq"+rowSeq).val();
		var actionDate = $("#actionDate"+rowSeq).val();
		var actionYn = $("#actionYn"+rowSeq).val();
		var bigo = $("#bigo"+rowSeq).val();
		var tbnm = $("#tbnm").val();
		var sldmEmpNo = $("#sldm_empno"+rowSeq).val();
		var empNo = $("#empno"+rowSeq).val();
		var role_code = $("#role_code").val();
		
		//alert(tbnm);
		
		var data = {
			sldmOrgLogDate:rgdtDate,
			seq:seq,
			actionDate:actionDate,
			actionYn:actionYn,
			bigo:bigo,
			src_table:tbnm,
			sldmEmpNo:sldmEmpNo,
			empNo:empNo,
			role_code:role_code
		}
	
		 $.ajax({
			url: '/server/setPolLogUpdate.do',
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
					$.logListSearch();
				}else{
					alert(data.MSG);
				}				
			}
		});
		
	};
	
	$.getDateList = function(){
		var tbnm = $("#polInfo").val();
		
		
			
		//$("#rgdtDate  option").remove();
		
		var data = {
			tbnm:tbnm,
			role_code : $("#role_code").val(),
			s_EmpNo : $("#s_EmpNo").val(),
			s_actionYn: $("#s_actionYn").val()
		}
	
		 $.ajax({
			url: '/server/getDateList.do',
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
					//$("#s_rgdtDate").append("<option value=''>선택하세요.</option>");
					for(var i=0; i<data.dateList.length; i++){
						$("#s_rgdtDate").append("<option value='"+data.dateList[i].rgdtDate+"' >"+data.dateList[i].rgdtDate+"</option>");
					}
				}else{
					$.logListSearch();
				}				
			}
		});
	};
	
	$(".setAllLogUpdate").click(function (){
		var rgdtDateList = [];
		var seqList = [];
		var actionDateList = [];
		var actionYnList = [];
		var bigoList = [];
		var sldmEmpNoList = [];
		var empNoList = [];
		
		$('input:checkbox[name=c_seq]:checked').each(function () {
		
			var rowSeq = $(this).val();
			
			//alert(rowSeq);
			
			
			rgdtDateList.push($("#rgdtDate"+rowSeq).val()==''?'null':$("#rgdtDate"+rowSeq).val());
			seqList.push($("#seq"+rowSeq).val()==''?'null':$("#seq"+rowSeq).val());
			
			actionDateList.push($("#actionDate"+rowSeq).val()==''?'null':$("#actionDate"+rowSeq).val());
			actionYnList.push($("#actionYn"+rowSeq).val()==''?'null':$("#actionYn"+rowSeq).val());
			bigoList.push($("#bigo"+rowSeq).val()==''?'null':$("#bigo"+rowSeq).val());
			sldmEmpNoList.push($("#sldm_empno"+rowSeq).val()==''?'null':$("#sldm_empno"+rowSeq).val());
			empNoList.push($("#empno"+rowSeq).val()==''?'null':$("#empno"+rowSeq).val());
			
			console.log(seqList);
			console.log(bigoList);
		
		});
		
		var tbnm = $("#tbnm").val();
		var data = {
			sldmOrgLogDate:rgdtDateList.toString(),
			seq:seqList.toString(),
			actionDate:actionDateList.toString(),
			actionYn:actionYnList.toString(),
			bigo:bigoList.toString(),
			src_table:tbnm,
			sldmEmpNo:sldmEmpNoList.toString(),
			empNo:empNoList.toString(),
			role_code:$("#role_code").val()
		}
	
	
		 $.ajax({
			url: '/server/setAllLogUpdate.do',
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
					$.logListSearch();
				}else{
					alert(data.MSG);
				}				
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
	<%@ include file="/WEB-INF/jsp/cmm/serverLeftMenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" id="listForm" name="listForm" method="post">
    <input type="hidden" name="role_code" id="role_code" value="${searchVO.role_code }" />
    	<div class="subTT"><span>서버 이행관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
        		<p>이행 관리 정책</p> 
            	<select  name="polId" id="polId" >
            		<option value="">선택하세요.</option>
            		<c:forEach var="result" items="${serverPolIdList}" varStatus="status">
                		<option value="${result.polId }" <c:if test='${searchVO.polId == result.polId}' >selected</c:if>>${result.policyName }</option>
                	</c:forEach>
				</select>
				<c:if test="${searchVO.role_code eq '1'}">
        		<p>사번</p> 
                <input type="text" name="s_EmpNo" id="s_EmpNo" value="${searchVO.s_EmpNo }" style="width:150px" class="srh">
        		<p>이름</p> 
                <input type="text" name="s_EmpNm" id="s_EmpNm" value="${searchVO.s_EmpNm }" style="width:150px" class="srh">
        		</c:if>
        		<p>조치 여부</p> 
            	<select  name="s_actionYn" id="s_actionYn" >
            		<option value="">선택하세요.</option>
                	<option value="미완료" <c:if test='${searchVO.s_actionYn == "미완료"}' >selected</c:if>>미완료</option>
                	<option value="조치중" <c:if test='${searchVO.s_actionYn == "조치중"}' >selected</c:if>>조치중</option>
                	<option value="완료" <c:if test='${searchVO.s_actionYn == "완료"}' >selected</c:if>>완료</option>
				</select>
				<a class="btn_black" onclick="$.logListSearch();"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
            	${strList }
            </table>
            <!-- E :list -->
            <div class="btn_borderWrite" style="top:20px;">
            	<a class="btn_black setAllLogUpdate" ><span>선택 저장</span></a>
            </div>
        </div>
       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
       	<input type="hidden" name="tbnm" id="tbnm" value="<c:out value="${searchVO.tbnm}" />" />
       	
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