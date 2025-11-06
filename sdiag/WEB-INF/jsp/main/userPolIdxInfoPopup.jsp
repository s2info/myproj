<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>KT임직원수준진단</title>
<link rel="stylesheet" type="text/css" href="/css/common2.css" />
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<style>

.popup_ly {
	min-width:960px;
	width: 100%

}

.popup_stt li span {
	min-width:920px;
	padding:0px;
	display:inline-block;
	font:bold 18px "맑은 고딕";
}

.popup_score div li.cell1 {
	min-width:640px;
	display:inline-block;
    line-height:28px;
    width:75%;	
}
.popup_score div li.cell2 {
	width:120px;
	display:inline-block;
	text-align:center;
	margin-right: 10px;
}
.pop_score_1_text {
	width:120px;
	height:30px;
	background:#51be33;
	border:solid 1px #449f2b;
	border-radius:5px;
	font:bold 12px "돋움";
	color:#ffffff;
	line-height:34px;
	display:inline-block;
	text-align:center;
	margin:0 10px 0 0;
}
.pop_score_2 {
	width:20px;
	height:20px;
	background:#ffae3b;
	border:solid 1px #d69232;
	border-radius:30px;
	font:bold 12px "돋움";
	color:#ffffff;
	line-height:24px;
	display:inline-block;
	text-align:center;
	margin:0 10px 0 0;
}
.pop_score_3 {
	width:20px;
	height:20px;
	background:#e74b3c;
	border:solid 1px #c23f32;
	border-radius:30px;
	font:bold 12px "돋움";
	color:#ffffff;
	line-height:24px;
	display:inline-block;
	text-align:center;
	margin:0 10px 0 0;
}
.pop_score_2_text {
	width:120px;
	height:30px;
	background:#ffae3b;
	border:solid 1px #d69232;
	border-radius:5px;
	font:bold 12px "돋움";
	color:#ffffff;
	line-height:34px;
	display:inline-block;
	text-align:center;
	margin:0 10px 0 0;
}
.pop_score_3_text {
	width:120px;
	height:30px;
	background:#e74b3c;
	border:solid 1px #c23f32;
	border-radius:5px;
	font:bold 12px "돋움";
	color:#ffffff;
	line-height:34px;
	display:inline-block;
	text-align:center;
	margin:0 10px 0 0;
}

.popup_wrap {
	width:97%;
	height:auto;
	padding:15px;
	margin:0 auto;
	display:inline-block;
	background:#ffffff;
	border-top:solid 1px #e1e4e8;
	border-width: 100%
}

.scroll_box {
	min-width:908px;
	height:300px;
	padding:10px;
	color:#676767;
	background:#ffffff;	
	border:solid 1px #dbdbdb;
	overflow:scroll;
	line-height:15px;
	width:100%;
	
}
</style>
<script type="text/javaScript" language="javascript">
$(document).ready(function () {		
	$("#content").each(function(){
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	});
});
</script>
</head>

<body>
    <!-- S : 상세 내용 및 조치 방안 팝업 -->
    <div class="ly_type1">
        <div class="popup_ly">
            <div class="popup_stt"><li><span>지수화정책 상세화면</span></li></div>
			<div class="popup_score">
            	<div>
           	<c:choose>
           		<c:when test="${userPolIdxInfoVO.critical eq 'GOOD'}">
           			<li class="cell1"><span class="pop_score_1">&nbsp;</span>${userPolIdxInfoVO.sec_pol_desc}</li>
                    <li class="cell2"><span class="pop_score_1_text">${userPolIdxInfoVO.score} [양호]</span></li>
           		</c:when>
           		<c:when test="${userPolIdxInfoVO.critical eq 'WARING'}">
           			<li class="cell1"><span class="pop_score_2">&nbsp;</span>${userPolIdxInfoVO.sec_pol_desc}</li>
                    <li class="cell2"><span class="pop_score_2_text">${userPolIdxInfoVO.score} [주의]</span></li>
           		</c:when>
           		<c:otherwise>
           			<li class="cell1"><span class="pop_score_3">&nbsp;</span>${userPolIdxInfoVO.sec_pol_desc}</li>
                    <li class="cell2"><span class="pop_score_3_text">${userPolIdxInfoVO.score} [취약]</span></li>
           		</c:otherwise>
           	</c:choose>
           	<%-- <c:choose>
           		<c:when test="${userPolIdxInfoVO.appr_id eq '' || userPolIdxInfoVO.appr_id eq null}">
           			<li class="call3"><span class="calling_2">소명 비대상</span></li>
           		</c:when>
           		<c:otherwise>
           			<li class="call3"><span class="calling_1">소명 요청</span></li>
           		</c:otherwise>
           	</c:choose> --%>
                </div>
            </div>
            <div class="popup_wrap">
            	<h4><img src="/img/icon_05.png" /> 진단 결과 상세</h4>
                <div class="scroll_box" style="height: 150px;">
                <c:choose>
                	<c:when test="${fn:length(colunmList) > 0 && fn:length(valueList) > 0 }">
                    	<table cellpadding="0" cellspacing="0">
                    		<thead>
                            	<tr>
	                        	<c:forEach var="result" items="${colunmList}" varStatus="status">
	                                <th>${result.column}</th>
	                        	</c:forEach>
	                            </tr>
	                        </thead>
	                        <tbody>
	                           
	                        <c:forEach var="valueList" items="${valueList}" varStatus="status">
	                         <tr class="cell1">
	                         	<c:forEach var="valaue" items="${fn:split(valueList.value, ',')}" varStatus="status">
	                            	<td>${valaue}</td>
	                          	</c:forEach>
	                          	</tr>
	                      	</c:forEach>
                      	
                        	</tbody>                       
                    	</table>
                   </c:when>
                   <c:otherwise>
	               		<h3>
	                		검색결과가 없습니다.
	                	</h3>
		            </c:otherwise>
		        </c:choose>
                <c:if test="${fn:length(sdResultInfo) > 0  && userPolIdxInfoVO.sec_pol_id eq 'SD01'}">
                	<h3 style="margin-top: 10px;"><img src="/img/icon_05.png" /> SecurityDay 점검 항목 상세</h3>
			        <table cellpadding="0" cellspacing="0" style="margin-top: 5px;">
	                   	<thead>
	                       <tr>
	                            <th>사번</th>
	                            <th>점검명</th>
	                            <th>문항 번호</th>
	                            <th>점검 여부</th>
	                            <th>점검 일자</th>
	                       </tr>
	                    </thead>
	                    <tbody>
	                   
	                    <c:forEach var="sdResultInfo" items="${sdResultInfo}" varStatus="status">
	                    	<tr class="cell1">
	                    		<td>${sdResultInfo.empNo}</td>
	                    		<td>${sdResultInfo.clGroupNm}</td>
	                    		<td>${sdResultInfo.ordr}</td>
	                    		<td>${sdResultInfo.resultYn}</td>
	                    		<td>${sdResultInfo.checkDate}</td>
	                    	</tr>
	                  	</c:forEach>
	                    </tbody>                       
	                 </table>
	            </c:if>
		            
                </div>  
                <h4><img src="/img/icon_05.png" /> 지수화 정책 상세 내용</h4>
                <div class="scroll_box"  id="content" style="height: 100%;">
                ${userPolIdxInfoVO.detail}
                </div>           
            	          
            </div>
        </div>          
    </div>
    <!-- E : 상세 내용 및 조치 방안 팝업 -->
</body>
</html>
