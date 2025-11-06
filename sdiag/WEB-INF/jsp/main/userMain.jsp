<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8;" />
<meta http-equiv="content-language" content="ko" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="description" content="임직원보안수준진단" />


<title>KT임직원수준진단</title>
<link rel="stylesheet" type="text/css" href="/css/common2.css" />
<link rel="stylesheet" type="text/css" href="/css/jquery.jqplot.css" />
<style>
		
		.user_info li.cell1 {
			width:155px;
			display:inline-block;
			padding:0 10px;
			*padding:10px 10px;
		}
		.user_info li.cell2 {
			background:#3e434a;
			display:inline-block;
			padding:13px 13px;
			font:bold 12px "돋움";
			*float:right;
			*margin-top:-37px;
			*padding:11px 10px;
		}
		
		/** notice wrap **/
		.notice_wrap h3 span {
			width:180px;
			font:bold 12px "돋움";
			line-height:30px;
			display:inline-block;
		}
		.notice_wrap h3 a {
			
			color:#585f69;
		}

		.notice_wrap ul li a {
			width: 180px;
			height:14px;
			float:left;
			display:inline-block;
			margin:10px 5px 5px 0;		
			font:normal 12px "돋움";
			color:#585f69;
			overflow:hidden;
		}
		.notice_wrap ul li span {
			width:72px;
			margin:10px 0 5px 0;
			display:inline-block;
		}


		#container_gaug {
			width: 250px;
			height: 200px;
			display: inline-block;
			margin: 1em;
			margin-top: -15px;
		}
		
		
		/**popup**/
		.popup_ly {width:860px;width:100%;}
		.popup_stt {
			width:100%;
			background:#4d5057;
			color:#fff;
			margin:0 0 0 0;
			display:inline-block;
			line-height:25px;
		}
		.popup_stt li {
			width:100%;
			height:25px;
			padding:10px;
			display:inline-block;
		}
		.popup_stt li span {
			width:400px;
			padding:0px;
			display:inline-block;
			font:bold 15px "맑은 고딕";	
		}
		.popup_stt img {
			vertical-align:middle;
			float : right;
			padding-right:20px;
		}
		.popup_stt span {
			width:400px;
			font:bold 15px "맑은 고딕";	
			padding-left:15px;
		}
		.popup_score {
			width:100%;
			height:280px;
			padding:10px;
			background:#2d303f;
			color:#ffffff;
		}
		.popup_cnt {
			width:600px;
			display:inline-block;
		}
		.popup_cnt h4 {
			color:#ffffff;
			display:block;
			padding:10px 0;
			font:bold 12px "돋움";
		}
		
		.popup_wrap {
			width:825px;
			height:370px;
			padding:15px;	
			margin:0 auto;
			display:inline-block;
		}
		.popup_wrap h4 {
			display:block;
			padding:10px 0;	
			font:bold 12px "돋움";
		}
		
		.textarea {
			height:318px;
			padding:10px;
			color:#676767;
			background:#edeef0;	
			border:solid 1px #dbdbdb;
			width:805px;
		}
		.ui-widget-content {
			border: 0px solid #aaaaaa;
			background: transparent;
			color: #222222;
		}
		.cnt_textarea {
			width:575px;
			height:228px;
			padding:10px;
			background:#fff;
			color:#676767;
			
		}
		/*2015.12.17 팝업 리스트 추가*/
		.tblInfoType4 th {background:#f1f1f1;text-align:center;border:solid 1px #e9e9e9;color:#444444;}
		.tblInfoType4 td {padding:10px;border:solid 1px #e9e9e9;line-height:18px;text-align:center;}
		.tblInfoType4 td p {display:block;padding:10px 0 5px 10px;font:normal 11px "돋움";}
		.tblInfoType4 td span.addfile {font:bold 11px "돋움";padding:0 10px 0 10px;display:inline-block;}
		.tblInfoType4 tr:hover{background:#eee;}
		.tblInfoType4 tr.SelRow{background:#eee;}
		.tblInfoType4 {
		    width: 150%;
		    position: relative;
		    margin: 0 auto 0 auto;
		    border-top: solid 2px #ed2028;
		    color: #767676;
		    border-color: #222;
		}
		
		/** graph wrap **/
		.popup_graph_block {
			min-width:200px;
			
			padding:40px 20px 20px 20px;
			float:left;
			display:inline-block;
		}
		.popup_graph_block li {
			display:block;
			text-align:center;
		}
		.popup_graph_block li.cell1 {
			font:bold 12px "돋움";
			height:30px;
			margin:25px 0 0 0;
		}
		.popup_graph_block li.cell2 {
			font:bold 12px "돋움";
		}
		.popup_graph_block li.cell3 {
			position:absolute;
			min-width:200px;
		
			margin-top:-120px;
			text-align:center;
			font:bold 18px "맑은 고딕";
			display:block;
		}
		.popup_graph_block li.cell3 span {
			font:bold 50px "맑은 고딕";
			display:block;
		}

		/** link wrap **/
		.link_wrap {
			height:216px
		}
		
		/** notice wrap **/
		.notice_wrap {
			height:140px;
			border-bottom:solid 1px #e0e0e0;
			padding:10px;
		}
		
		#cnt_right {
	width:auto;
	min-width:970px;
	margin:0 0 0 280px;
	text-align:left;
	height:auto;
	min-height:915px;
	padding:0 0 10px 0;
	background:#f1f1f1 url(../img/bg_right.jpg) no-repeat left top;	
}

.date_wrap div li.cause {
	text-align:left;
	padding:0 16px;
	line-height:20px;
	overflow:hidden;
	width:160px;
	float:left;
}

.date_wrap div {
	width:195px;
	height:160px;
	padding:10px;
	margin:10px 9px;
	background:#edeef0;
	border:solid 1px #dbdbdb;
	display:inline-block;
	*float:left;
}
</style>
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/jquery.easypiechart.js"></script>
<script type="text/javascript" src="/js/js/Chart/gaugeSVG.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="/css/jquery-ui.css" />

<script type="text/javaScript" language="javascript">
$(document).ready(function () {	
	

	
	<c:forEach var="noticeList" items="${noticePopupList}" varStatus="status">
	noticePopup('${noticeList.sq_no}');
	</c:forEach>
	
	 
	 function getCookie(name){
		 var nameOfCookie = name +"=";
		 var x = 0;
		 while(x<= document.cookie.length){
			 var y = (x+nameOfCookie.length);
			 if(document.cookie.substring(x,y) == nameOfCookie){
				 if((endOfCookie=document.cookie.indexOf(";",y)) == -1){
					 endOfCookie = document.cookie.length;
				 }
				 return unescape(document.cookie.substring(y, endOfCookie));
			 }
			 x=document.cookie.indexOf("",x)+1;
			 if(x == 0)
				 break;
		 }
	 }

	function noticePopup (seq){
		var sqNo = seq;
    	var search =  {sq_no: sqNo};
    	var param = escape(encodeURIComponent(JSON.stringify(search)));
        
    	if(getCookie("name"+seq) !="done"){
			var pw = window.open('/main/noticePopup.do?param='+param,'name'+seq,'width=980, height=700,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
    	}
	}
	
	$.noticeView = function(sqno){
		
		$('#sqno').val(sqno);
		document.listForm.action = "<c:url value='/man/noticeview.do'/>";
		document.listForm.submit();
		
	};
	
	$.faqView = function(sqno){
		
		$('#sqno').val(sqno);
		document.listForm.action = "<c:url value='/man/faqview.do'/>";
		document.listForm.submit();
		
	};
	
	
	
	
	
	// 도넛챠트 생성
	for (var i=1; i<4; i++){
		var bar_color = barColor($('.chart'+i).attr("data-percent"));
		
		//점수가 0점일 경우 빨간색으로 풀로 채우기
		if($('.chart'+i).attr("data-percent") == 0 && $('.chart'+i).attr("data-percent") !="" && $('.chart'+i).attr("data-percent") !=null){
			$('.chart'+i).attr("data-percent", "100");
		}
		
		if($('.chart'+i).attr("data-percent")==""){
			bar_color = "#f1f1f1";
		}
		
		$('.chart'+i).easyPieChart({
			animate:false,
			//trackColor:false,
			barColor: bar_color,
			trackColor: '#f1f1f1',
			lineWidth :6,
			size : 165,
			scaleColor:false
		});
	}
	
	
	// 전사 평균 챠트 생성
	var ckvalue = ${totalAvg.score};
	var danger = ${gauagValue.danger};
	var warning = ${gauagValue.warning};
	if (ckvalue <= 0){ckvalue = 0;} // display value 0 : 50 -- 1
	var gauge = new GaugeSVG({
		id: "container_gaug",
		value: ckvalue, 
		canvasBackColor: "#f0f0f0",
		title: "전사",
		label: "SCORE",
		labelColor: "#2C2C2C",
		labelScale: 3,
		gaugeWidthScale: 1.0,
		borderColor: "#ff5533",
		min: 0,
		max: 100,
		minmaxColor: "#2C2C2C",
	  	lowerActionLimit:danger,
		lowerWarningLimit:warning,
	  	upperWarningLimit:100,
		upperActionLimit:100,
		showMinMax: true,
		needleColor: "#080808",
		optimumRangeColor: "#48CB01",
		warningRangeColor: "#FEAB35",
		actionRangeColor: "#E00000",
		gaugeBackColor: "#ccc"
	});
	
	
	function barColor (value){
		var color = "";
		if(value >= 80){
			color = "#48CB01";
		}else if (value < 80 && value >=40 ){
			color = "#FEAB35";
		}else{
			color = "#E00000";
		}
		return color;
	}
	
	function content (){
		$("#content").each(function(){
			var $this = $(this);
			var t = $this.text();
			$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
		});
	}
	
	function pieChart (){
		var bar_color = barColor($('.chart').attr("data-percent"));
		
		//점수가 0점일 경우 빨간색으로 풀로 채우기
		if($('.chart').attr("data-percent") <= 0 && $('.chart').attr("data-percent") != ""){
			$('.chart').attr("data-percent", "100");
		}
		
		$('.chart').easyPieChart({
			animate:false,
			//trackColor:false,
			barColor: bar_color,
			trackColor: '#f1f1f1',
			lineWidth :6,
			size : 165,
			scaleColor:false
		});
	} 
	
	var isToggle = 1;
	$('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: true,
        show: "fade",
        hide: "fade",
        minWidth:500,
        minHeight:450,
        close: function () { 
            $(this).dialog('close'); 
        },
        open: function () {
            var $ddata = $(".DialogBox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        },
        resize:function(){
        	var $ddata = $(".DialogBox");
        	var $pop_wrap = $ddata.find('.popup_wrap');
        	var $text_area = $ddata.find('.textarea');
        	var $popup_cnt = $ddata.find('.popup_cnt');
        	var $cnt_textarea = $ddata.find('.cnt_textarea');
        	$pop_wrap.width($ddata.width() - 32);
        	$pop_wrap.height($ddata.height() - 380);
        	$text_area.width($pop_wrap.width() - 20);
        	$text_area.height($pop_wrap.height() - 52);
        	$popup_cnt.width($ddata.width() - 257);
        	$cnt_textarea.width($ddata.width() - 282);
        }
    });
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	});
	
	$('.btn_view').click(function(){
		
        var secPolId = $(this).attr('secPolId');
    	var search =  {sec_pol_id: secPolId}
    	var param = escape(encodeURIComponent(JSON.stringify(search)));
        
		var pw = window.open('/main/userPolIdxInfoPopup.do?param='+param,'','width=1390, height=920,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
    		
		
	});
	
	$('.btn_main_reload').click(function(){
		 var loginType = $(this).attr('ktmp');
		 var target = $(this).attr('url');
		 //var target='/dash/dashboard.do';
	     var formid='searchVO';
	     var params = [];
	     params[0] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'ktmp'
	       , "inputvalue": loginType
	     });
	     setsession(target, params, formid, loginType);
	     
	});
	
	function setsession(target, params, formid, loginType){
		var data={"sessoinname":"loginType", "sessionval":loginType}
	     $.ajax({
				url : '/com/sessionupdate.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					alert(textStatus + "\r\n" + errorThrown);
				},
				success : function(data) {
					if (data.ISOK) {
						gopage(target, params, formid);
					} else {
						alert(data.MSG);
					}

				}

		});
	}
	
	
	function gopage(target, params, formid) {
	    var frm = document.createElement('form');
	    frm.id=formid;
	    frm.action = target;
	    frm.method = 'post';
	    for (var i = 0 ; i < params.length; i++) {
	        var r = JSON.parse(params[i]);
	        var param = document.createElement('input');
	        param.name = r.inputname;
	        param.value = r.inputvalue;
	        frm.appendChild(param);
	    }
	    frm.style.display = 'none';
	    document.body.appendChild(frm);
	    frm.submit();
	    return frm;
	}		
	
});
</script>
</head>

<body>
<form:form id="searchVO" name="listForm" method="post">
<input type="hidden" name="sqno" id="sqno" value="0" />
</form:form>
<div class="DialogBox" style="overflow: hidden;padding: 0"></div>
<!-- E : layer -->

<!-- S : header -->
<div id="header" style="z-index: 0;">
    <h1>임직원 보안수준진단</h1>
    <div><a href="/dash/home.do">세부내역확인</a></div>
</div>
<!-- E : header -->
<div style="clear:both;"></div>
<!-- S : contents -->
<div id="cnt">
	<div id="cnt_left">
    	<div class="user_wrap">
        	<ul class="user_score">
            	<li class="cell1">
						<div id="container_gaug"></div>
                </li> 
                <!-- <li class="c]ell3">55 <span>score</span></li> -->
            </ul>
            <ul class="user_info">
            	<li class="cell1"><span class='test_input'>${userNm }</span>님 환영합니다.</li>
                <li class="cell2"><img src="/img/icon_logout.png" /> <a href="/logout.do">로그아웃</a></li>
            </ul>
        </div>
        <!-- S : notice blcok -->
        <div class="notice_wrap">
        	<h3><img src="/img/icon_02.png" /> <span>공지사항</span> <a href="/man/noticelist.do"><img src="/img/icon_more.jpg" /> more</a></h3>
        	<ul>
        	<c:forEach var="noticeList" items="${noticeList}" varStatus="status">
				<li><a href="javascript:$.noticeView('${noticeList.sq_no}')" target="_self">${fn:substring(noticeList.title, 0, 18)}<c:if test="${fn:length(noticeList.title) > 18}" >...</c:if></a><span>${noticeList.upd_date}</span></li>
			</c:forEach>
			</ul>
        </div>
        <!-- E : notice blcok -->
        <!-- S : notice blcok -->
        <div class="notice_wrap">
        	<h3><img src="/img/icon_02.png" /> <span>FAQ</span> <a href="/man/faqlist.do"><img src="/img/icon_more.jpg" /> more</a></h3>
        	<ul>
        	<c:forEach var="faqList" items="${faqList}" varStatus="status">
				<li><a href="javascript:$.faqView('${faqList.sq_no}')" target="_self">${fn:substring(faqList.title, 0, 18)}<c:if test="${fn:length(faqList.title) > 18}" >...</c:if></a><span>${faqList.upd_date}</span></li>
			</c:forEach>
			</ul>
        </div>
        <!-- E : notice blcok -->
        
         <div class="notice_wrap" style="height: 30px; padding-bottom: 10px;">

        	<h3><img src="/img/icon_02.png" /> <a href="/man/qnaList.do"> <span> Q&A 바로가기</span> </a></h3>
        </div>
        <div class="link_wrap">
        	<li style="border-top-color: rgba(184, 184, 184, 1); border-bottom-color: rgba(184, 184, 184, 1); background-color: rgba(220, 220, 220, 1);"><img src="/img/link_icon_07.png" /><a href="/main/fileDown.do" target="_self">사용자 매뉴얼</a></li>
        	<li><img src="/img/link_icon_01.png" /> <a href="http://www.kisa.or.kr" target="_blank">한국인터넷 진흥원</a></li>
            <li><img src="/img/link_icon_02.png" /> <a href="http://privacy.kisa.or.kr" target="_blank">개인정보침해 신고센터</a></li>
            <li><img src="/img/link_icon_03.png" /> <a href="http://spam.kisa.or.kr" target="_blank">불법스팸 대응센터</a></li>
            <li><img src="/img/link_icon_04.png" /> <a href="http://www.krcert.or.kr" target="_blank">인터넷침해 대응센터</a></li>
            <!-- <li><img src="/img/link_icon_05.png" /> <a href="http://www.eprivacy.go.kr" target="_blank">주민등록번호 클린센터</a></li>
            <li><img src="/img/link_icon_06.png" /> <a href="https://www.virustotal.com" target="_blank">바이러스 토탈</a></li> -->
        </div>
    </div>    
    <div id="cnt_right">
    	<!-- S : graph block -->
    	<div class="graph_wrap">
        	<ul class="graph_block">
            	<li class="cell1">${userIdxVo.emp_nm } </li>
            	<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="personal" url="/dash/home.do">
            		<li class="cell2">
            			<div style="width: 300px;" class="chart1" data-percent="${userIdxVo.score }"></div> 
                	</li>
                	<li class="cell3">score<span>${userIdxVo.score }</span></li>
                </a>
            </ul>
        	<ul class="graph_block">
            	<li class="cell1">${userIdxVo.u_org_nm }</li>
            	
           	<c:choose>
           		<c:when test="${fn:length(typeList) > 0}">
           			<c:forEach var="result" items="${typeList}" varStatus="status">
		            	<c:choose>
		            	<c:when test='${result.logintype eq "CAPTAIN" && result.org_code  eq userIdxVo.u_org_code}'>
		            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="${result.org_code}"url="/dash/home.do">
		            	</c:when>
		            	<c:otherwise>
		            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="personal" url="/dash/home.do">
		            	</c:otherwise>
		            	</c:choose>
		            </c:forEach>
           		</c:when>
           		<c:otherwise>
            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="personal" url="/dash/home.do">
            	</c:otherwise>
           	</c:choose>
           			<li class="cell2">
                		<div style="width: 300px;" class="chart2" data-percent="${userIdxVo.u_avg }"></div> 
                	
                	</li>
                	<li class="cell3">score<span>${userIdxVo.u_avg }</span></li>
                </a>
            </ul>
        	<ul class="graph_block">
            	<li class="cell1">${userIdxVo.upper_org_nm }</li>
                
          	<c:choose>
           		<c:when test="${fn:length(typeList) > 0}">
           			<c:forEach var="result" items="${typeList}" varStatus="status">
		            	<c:choose>
		            	<c:when test='${result.logintype eq "CAPTAIN" && result.org_code  eq userIdxVo.upper_org_code}'>
		            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="${result.org_code}"url="/dash/home.do">
		            	</c:when>
		            	<c:otherwise>
		            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="personal" url="/dash/home.do">
		            	</c:otherwise>
		            	</c:choose>
		            </c:forEach>
           		</c:when>
           		<c:otherwise>
            		<a class="btn_main_reload" style="cursor:pointer; color: #ffffff"  ktmp="personal" url="/dash/home.do">
            	</c:otherwise>
           	</c:choose>
           			<li class="cell2">
                		<div style="width: 300px;" class="chart3" data-percent="${userIdxVo.upper_avg }"></div> 
	                </li>
	                <li class="cell3">score<span>${userIdxVo.upper_avg }</span></li>
                </a>
            </ul>
            
            
             
            <ul class="graph_data">
            	<c:if test="${userIdxVo.year_val ne null && userIdxVo.year_val ne ''}">
            	<li><img src="/img/icon_03.png" /><span>데이터 수집날짜 ㅣ</span>${userIdxVo.year_val } - ${userIdxVo.month_val } - ${userIdxVo.day_val }</li>
            	</c:if>
            	<c:if test="${userIdxVo.year_val eq null || userIdxVo.year_val eq ''}">
            	<li><img src="/img/icon_03.png" /><span>데이터 수집날짜 ㅣ</span></li>
            	</c:if>
            </ul>          
        </div>
        <!-- E : graph block -->
        <!-- S : date block -->
        <div class="date_block">
        	<!-- S : 1줄당 4개 한세트로 복제 -->
            <div class="date_wrap">
            	<!-- S : 복제영역 4개까지만 복제 (*발생사유 없어도 li 영역 유지해야함!)-->
                <c:forEach var="result" items="${userPolIdxInfoList}" varStatus="status">
	                <div style="cursor:pointer;" class="btn_view" secPolId="${result.sec_pol_id }" >
	                    <h4>${result.sec_pol_desc } </h4>
	                    <c:if test="${result.critical eq 'GOOD'}">
	                    	<li class="result_excellent">${result.score }점 [양호]</li>
	                    </c:if>
	                    <c:if test="${result.critical eq 'WARING'}">
	                    	<li class="result_care">${result.score }점 [주의]</li>
	                    </c:if>
	                    <c:if test="${result.critical eq 'WEAK'}">
	                    	<li class="result_vulnerable">${result.score }점 [취약]</li>
	                    </c:if>
	                    <c:choose>
	                    <c:when test="${result.critical ne 'GOOD'}">
	                    	<li  class="cause" style="overflow:hidden;"><span>발생사유</span> 
		                    <textarea rows="3" disabled style="width: 165px;font:12px/normal '돋움'; overflow:auto;background:#edeef0;border:solid 0px #edeef0;">${result.reason}</textarea></li>
		                </c:when>
		                <c:otherwise>
		                	<li  class="cause" style="overflow:hidden;"><span>&nbsp;</span> 
		                	<textarea rows="3" disabled style="width: 165px;font:12px/normal '돋움'; overflow:auto;background:#edeef0;border:solid 0px #edeef0;"></textarea></li>
		                </c:otherwise>
	                    </c:choose>
	                </div>
                </c:forEach>
            </div>
        </div>
        <!-- E : date block -->
    </div>
</div> 
<!-- E : contents -->
<!-- S : footer -->
<div id="footer"> Copyright ⓒ <span>KT</span> All right reserved. </div>
<!-- E : footer --> 
</body>
</html>
