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
<style type="text/css">
.ly_box2 {
	width:940px;
	margin:0 auto;	
	text-align:center;
	padding:190px 20px 0px 20px;	
}
.ly_box2 li.ly_cell3 {
	padding:30px 0 10px 0;
	display:block;
	font-weight:bold;
	text-align:center;
	font-size:16px;
	line-height:40px;
}
.ly_box2 li.ly_cell3 span {
	font-size:16px;
	font-weight:normal;
}
.ly_box2 li.ly_cell3 p {
	font-size:16px;
}
.ly_block6 {width:982px;height:820px;display:inline-block; background:#5499de;}
.ly_block7 {width:982px;height:175px;display:inline-block;}
.security_tt {
	position:relative;
	height:300px;	
	background:url(/img/securityday_bg.png) no-repeat;
}
.security_tt span {
	position:absolute;
	bottom:30px;
	right:130px;
}
.security_tt span a {
	background:url(/img/btn_start_off.png) no-repeat;
	text-indent:-99999px;
	width:173px;
	height:53px;
	display:inline-block;
}
.security_tt span a:hover {
	background:url(/img/btn_start_on.png) no-repeat;	
}
.security_tt2 {
	position:relative;
	height:175px;	
	background:url(/img/securityday_test.png) no-repeat;
}
.security_tt2 span {
	position:absolute;
	top:20px;
	right:20px;
}
.security_tt2 span a {
	background:url(/img/btn_close.png) no-repeat;
	text-indent:-99999px;
	width:21px;
	height:21px;
	display:inline-block;
}
.security_box2 {
	width:962px;
	height:350px;
	margin:0 auto;	
	display:inline-block;
	text-align:center;
	padding:10px 0px 0px 20px;
	background:#ffffff;
}
.security_box {
	width:972px;
	height:400px;
	margin:0 auto;	
	display:inline-block;
	text-align:center;
	padding:10px 0px 0px 10px;
	background:#ffffff;
}
.security_cell1 {
	font-family:"맑은 고딕";
	font-size:30px;
	font-weight:bold;
	letter-spacing:-2px;
	color:#5499de;
	line-height:40px;
	padding:40px 0;
	display:inline-block;
}
.security_cell2 {
	width:100%;
	font-family:"맑은 고딕";
	font-size:15px;
	font-weight:bold;
	letter-spacing:-1px;
	color:#ffffff;	
	text-align:center;
	padding:35px 0 0 0;
}
.security_box table {
	width:600px;
	border:solid 1px #d7d7d7;
	margin:0 auto;
	font-family:"맑은 고딕";
	font-size:15px;
	color:#323232;
}
.security_box table th {
	border:solid 1px #d7d7d7;
	padding:20px 0;
	background:#f7f7f7;
}
.security_box table td {
	border:solid 1px #d7d7d7;
	padding:20px 20px;
	text-align:left;
}
.security_box2 li.ly_cell1 {
	padding:10px 0 0 0;
	display:block;
	font-weight:bold;
	text-align:left;
	font-size:15px;
}
.btn_black3 {position:relative;width:100%;float:right;padding:15px 0 10px 0;text-align:center;}
</style>
<script type="text/javascript" language="javascript">
$(function () {
	
	var reqCode = '${searchVO.reqCode}';
	if(reqCode == "3"){
		alert("마지막 문항정보 였습니다. 미리보기가 종료 됩니다.");
		window.self.close();
	}else if (reqCode =="1"){
		alert("포함된 점검표가 존재하지 않습니다. \n 점검표 추가후에 미로보기를 진행하세요.");
		window.self.close();
	}	
	
	$(".text_box1").each(function(){
		
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	});
	
	$(".question__box").each(function(){
		
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	});
	$("#start").click(function(){
		/* $("#reqCode").val("2");
		
		goPage("/securityDay/popSecurityDay.do");
		 */
		window.self.close();
		var pw = window.open('/securityDay/popSecurityDay.do?reqCode=2','securityDay2','width=980, height=700,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
		 
		
	});
	
	
	$(".q_next").click(function(){
		alert($("#rowNum").val());
		$("#rowNum").val((parseInt($("#rowNum").val()))+1);
		
		alert($("#rowNum").val());
		
		if($("#question").val() != ""){
			$("#explainDiv").css("display","none");
			$("#questionDiv").css("display","");
		}else{
			$("#firstIndex").val((parseInt($("#firstIndex").val()))+1);
			goPage("/securityDay/popSdPreView.do");
		}
			
	});
	
	$(".back").click(function(){
		$("#rowNum").val((parseInt($("#rowNum").val()))-1);
		goPage("/securityDay/popSdPreView.do");
	});
	
	$(".btn_cancle").click(function(){
		window.self.close();
	});
	
	
	$(".answer_ch").click(function(){
		$("#rowNum").val((parseInt($("#rowNum").val()))+1);
		goPage("/securityDay/popSdPreView.do");
	});
	
	
	
	
	
	function goPage(url) {

	    document.sdForm.action = "<c:url value='"+url+"'/>";
	    document.sdForm.method = 'post';
	    document.sdForm.submit();
	}
	
	
});



</script>

 

</head>

<body style="min-width: 980px">
<!-- S : contents -->
<div class="ly_block5">
	<!-- S : center contents -->
    <!--  <div class="subTT"><span>SECURITY DAY</span></div> -->
    <form:form  id="sdForm" name="sdForm" method="post"  enctype="multipart/form-data">
    	 <div  id="explainDiv">
    	 <div class='ly_block5' ><div class='security_tt2'><span></span></div>
	    	<div class="ly_box1" >
	    	<li>
	    		<div class="text_box1" style="height: 435px;">${ sdResultInfo.explain}</div>
	    	</li>
	    	
	    <c:choose>
	    	<c:when test="${sdResultInfo.fileYn eq 'Y' }">
		    	<li class="ly_cell1">${sdResultInfo.fileText }</br>
		    		<input type="file" type="file" id="file" name="file" maxlength="24" class="srh" style="width:200px" />  
		    	</li>
	    	</c:when>
	    	<c:otherwise>
		    	<c:if test="${sdResultInfo.question ne ''}">
			    	<li class="ly_cell1 question__box">Q.${ sdResultInfo.questionNum}  ${ sdResultInfo.question}</br></li>
			    	<c:if test="${sdResultInfo.answerType eq '1'}">
				        <li class="ly_cell1"><input type="text" maxlength="24" class="srh" style="width:928px" id="answerText" name="answerText"  /></li>
				    </c:if>
				    <c:if test="${sdResultInfo.answerType eq '2'}">
				        <li class="ly_cell1 radio_button">
				        	<c:forEach var="result" items="${mcList}" varStatus="status">
				            <label><input type="radio" id ="answerMc" name='answerMc' value='${result.exNum}'/> <span>${result.exNum}. ${result.exText}</span></label><br/>
				            </c:forEach>
				        </li>
			        </c:if>
		    	</c:if>
	    	</c:otherwise>
	    	
	    	</c:choose>
	    	</div>
	    	
	    	<div class="btn_black3">
	    	<c:if test="${sdResultInfo.rowNum >1 }">
	    		<a class='btn_black back' style='margin-right: 70px;' ><span>이전</span></a>
	    	</c:if>
	    		<a class="btn_black answer_ch"><span>다음</span></a> 
	    	</div>
	    	</div>
    	</div>
    	
    	
    	<%-- <div id="questionDiv" style="display: none" >
	    	<div class="ly_box1">
		    	<li class="ly_cell2">Q.${ sdResultInfo.questionNum}</li>
		    	<li><div class="text_box2">${ sdResultInfo.question}</div></li>
		    	<li class="ly_cell1">답을 입력하세요.</li>
		    	<c:if test="${sdResultInfo.answerType eq '1'}">
		        <li class="ly_cell1"><input type="text" maxlength="24" class="srh" style="width:928px" id="answerText" name="answerText"  /></li>
		        </c:if>
		        <c:if test="${sdResultInfo.answerType eq '2'}">
		        <li class="ly_cell1 radio_button">
		        	<c:forEach var="result" items="${mcList}" varStatus="status">
		            <label><input type="radio" id ="answerMc" name='answerMc' value='${result.exNum}'/> <span>${result.exNum}. ${result.exText}</span></label><br/>
		            </c:forEach>
		        </li>
		        </c:if>
		    </div>
	    	<div class="btn_black3">
	    		<a class="btn_black back"><span>이전</span></a> 
	    		<a class="btn_black answer_ch"><span>확인</span></a> 
	    	</div>
    	</div> --%>
    	
    	
    	<input type="hidden" id="pageIndex" name="pageIndex" value="${searchVO.pageIndex}" />
    	<input type="hidden" id="firstIndex" name="firstIndex" value="${searchVO.firstIndex}" />
    	<input type="hidden" id="reqCode" name="reqCode" value="${searchVO.reqCode}" />
    	<input type="hidden" id="q_answer" name="q_answer" value="${sdResultInfo.answer}" />
    	<input type="hidden" id="questionNum" name="questionNum" value="${sdResultInfo.questionNum}" />
    	<input type="hidden" id="question" name="question" value="${sdResultInfo.question}" />
    	<input type="hidden" id="clGroupNo" name="clGroupNo" value="${sdResultInfo.clGroupNo}" />
    	<input type="hidden" id="sdCheckNo" name="sdCheckNo" value="${sdResultInfo.sdCheckNo}" />
    	<input type="hidden" id="empNo" name="empNo" value="${sdResultInfo.empNo}" />
    	<input type="hidden" id="answerType" name="answerType" value="${sdResultInfo.answerType}" />
    	<input type="hidden" id="answer" name="answer" value="" />
    	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod}" />
    	<input type="hidden" id="fileYn" name="fileYn" value="${sdResultInfo.fileYn}" />
    	<input type="hidden" id="rowNum" name="rowNum" value="${sdResultInfo.rowNum}" />
    	
    	
		</form:form>
    </div>
    <!-- E : center contents -->
</div>
</body>
</html>