<%--
  Class Name : dashboard.jsp
  Description : dashboard 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    		--------    		---------------------------
    
 
    author   : dashboard
    since    : 
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
 <script type="text/javascript" src="/js/jquery.easypiechart.js"></script>
 <script type="text/javascript" src="/js/js/Chart/gaugeSVG.js"></script>
 <style>
.score_status {
	height:20px;
	width : 30px;
	line-height:20px;
	padding:2px 0px 0px 0px;
	color:#ffffff;
	display:inline-block;
	text-align:center; 
	/* box-sizing : border-box; */
	font-weight:normal;
	border:solid 0px #388be8;
	font-size:11px;	
	border-radius:0px;
	background: #388be8;
	/* background: -moz-linear-gradient(top, #62a3ed) 0%, #388be8 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#62a3ed), color-stop(100%,#388be8));
	background: -webkit-linear-gradient(top, #62a3ed) 0%,#388be8 100%);
	background: -o-linear-gradient(top, #62a3ed) 0%,#388be8 100%);
	background: -ms-linear-gradient(top, #62a3ed 0%,#388be8 100%);
	background: linear-gradient(top, #62a3ed) 0%,#388be8 100%); */
}

#container_gaug {
			width: 250px;
			height: 200px;
			display: inline-block;
			margin-top: 0px;
		}
 /* style="border:1px solid red;width:200px;" */	
 .graph2_block1 {
 	width:${totalUserInfo.graph_panel_width}; 	
 	margin-left:10px;
 }	
 .graph2_block1 li.cell1{
 border:0px solid blue;
 } 
  .graph2_block1 li.cell2{
 border:0px solid red;
 padding-left: 0px;
 } 
 .graph2_block1 li.cell2 div{
 border:0px solid green;
 left :0px;
 width:100%px;
 
 } 
.graph2_block1 li.cell3 {
	/* position:absolute; */
	width:100%;
	margin-top:-130px;
	text-align:center;
	font:bold 18px "맑은 고딕";
	border:0px solid black;
    
}
.graph2_block1 li.cell3 span {
	font:bold 50px "맑은 고딕";
	display:block;
}		
#container{
	padding-right:200px;
	padding-left:200px;
}
.index_block1_1{
/* 	min-width:800px; */
	width:100%;
	height:220px;
	background:#f9f9f9;
	border:solid 1px #dfdfdf;
	margin:0 0 0 0;
	text-align:center;
	padding:20px 0;
	display:inline-block;
	float:left;
}
#content{
	  /* min-width:1120px;   */
} 
 </style>
 <script type="text/javaScript" language="javascript">
$(function () {	
	$('.list_contents_body').on('click', '.btn_pol_status', function(){
		$('#majCode').val($(this).attr('majCode'));
		$('#minCode').val($(this).attr('minCode'));
		$('#polCode').val($(this).attr('polCode'));
		
		goPage("/pol/policystatus.do");
	});
	
	
	$('.diag_majr').click(function(){
			
		//$('#majrCode').val($(this).attr('diagcode'));
		//$('#majrName').val($(this).attr('diagDesc'));
		//$('#buseoIndc').val($(this).attr('buseoindc'));
		
		//$(this).find('a').addClass('ON');
		
		searchList($(this));	
		
	});	


   function searchList(selectdiv)
   {
	   	selectdiv.siblings().removeClass('on');
	   	selectdiv.addClass('on');
	   	
	   	var majrCode = selectdiv.attr('diagcode');
	   	var buseoIndc = selectdiv.attr('buseoindc');
	   	var majrName=selectdiv.attr('diagDesc');
	   	var org_code = selectdiv.attr('org_code');
	   	var is_sub_org = selectdiv.attr('is_sub_org');
	   	var cap_emp_no = $('#cap_emp_no').val();
	   	var auth=$('#auth').val();
	   	
	   	var data = {
	   			majrCode:majrCode,
	   			buseoIndc:buseoIndc,
	   			majrName:majrName,
	   			org_code:org_code,
	   			searchType:auth,
	   			emp_no:cap_emp_no,
	   			isSubOrg:is_sub_org};
	   	$.ajax({
			url : '/dash/policyResultForAjax.do',
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
					$('.result_title').empty().append(data.result_title);
					$('.result_status').empty().append(data.result_status);
					$('.list_contents_body').empty().append(data.tablebody);
				} else {
					alert("오류"+data.MSG);
				}
	
			}/* ,
			beforeSend: function () {
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
	          	 	var padingTop = (Number(($('.list_contents_body').css('height')).replace("px","")) / 2) + 50;
	               $('#loading').css('position', 'absolute');
	               $('#loading').css('left', $('.list_contents_body').offset().left + ($('.list_contents_body').css('width').replace("px","") / 2) - 130);
	               $('#loading').css('top', $('.list_contents_body').offset().top);
	             
	               $('#loading').css('padding-top', 100);
	               
	               $('#loading').show().fadeIn('fast');
	          },
	          complete: function () {
	              $('#loading').hide().fadeOut(1000);
	          }
 			*/
		});
   		//alert(selectdiv.attr('org_code'));
	//    $(this).siblings().removeClass('on');
	//	$(this).addClass('on');
	/*   
   	var majrCode=$('#majrCode').val();
   	var buseoIndc=$('#buseoIndc').val();
   	var majrName=$('#majrName').val();
   	var data = {
   			majrCode:majrCode,
   			buseoIndc:buseoIndc,
   			majrName:majrName};
       $.ajax({
			url : '/report/diagMajrAjax.do',
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
					$('.list_contents_body').empty().append(data.strList);
				} else {
					alert("오류"+data.MSG);
				}
	
			},
			beforeSend: function () {
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
	          	 	var padingTop = (Number(($('.list_contents_body').css('height')).replace("px","")) / 2) + 50;
	               $('#loading').css('position', 'absolute');
	               $('#loading').css('left', $('.list_contents_body').offset().left + ($('.list_contents_body').css('width').replace("px","") / 2) - 130);
	               $('#loading').css('top', $('.list_contents_body').offset().top);
	             
	               $('#loading').css('padding-top', 100);
	               
	               $('#loading').show().fadeIn('fast');
	          },
	          complete: function () {
	              $('#loading').hide().fadeOut(1000);
	          }

		});*/
   }    

  
   
   $(window).bind("load", function() {
	   	var firstdiv = $('.result_list').find('div').eq(0);
	   	//$('#majrCode').val(firstdiv.attr('diagcode'));
		//$('#buseoIndc').val(firstdiv.attr('buseoindc'));
		//$('#majrName').val(firstdiv.attr('diagDesc'));
	   	searchList(firstdiv);
	   	//$('.graph4').find('div').eq(0).addClass('on');
	   	/*****************/
	   	/** 오른쪽메뉴 ***/
	   	/*****************/
	   	var floatPostition = parseInt($('.aside').css('top'));
	   	$(window).scroll(function(){
	   		var scrollTop = $(window).scrollTop();
	   		var newPosition = scrollTop + floatPostition + 'px';
	   		$('.aside').stop().animate({'top':newPosition},500);
	   	}).scroll();
	});
   
	// 전사 평균 챠트 생성
	var ckvalue = ${totalUserInfo.avg_val };
	var danger = ${gauagValue.danger};
	var warning = ${gauagValue.warning};
	if (ckvalue <= 0){ckvalue = 0;} // display value 0 : 50 -- 1
	var gauge = new GaugeSVG({
		id: "container_gaug",
		value: ckvalue, 
		canvasBackColor: "#ffffff",
		title: "전사 점수",
		label: "SCORE",
		labelColor: "#2C2C2C",
		labelScale: 3,
		gaugeWidthScale: 1.0,
		borderColor: "#ffffff",
		min: 0,
		max: 100,
		minmaxColor: "#2C2C2C",
	  	lowerActionLimit:danger,
		lowerWarningLimit:warning,
	  	upperWarningLimit:100,
		upperActionLimit:100,
		showMinMax: true,
		needleColor: "#080808",
		optimumRangeColor: "${scoreColor.GOOD}", //양호
		warningRangeColor: "${scoreColor.CAUTION}", //주의
		actionRangeColor: "${scoreColor.WARNING}",  //취약
		gaugeBackColor: "#eee"
	});
	
	var chart_size=150;
	var chart_lineWidth=5;
	$('.chart1').easyPieChart({
		animate:false,
		//trackColor:false,
		barColor: '${totalUserInfo.emp_scorestat_color }',
		trackColor: '#f1f1f1',
		lineWidth :chart_lineWidth,
		size : chart_size,
		scaleColor:false,
		
	});
	$('.chart2').easyPieChart({
		animate:false,
		//trackColor:false,
		barColor: '${totalUserInfo.u_scorestat_color }',
		trackColor: '#f1f1f1',
		lineWidth :chart_lineWidth,
		size : chart_size,
		scaleColor:false
	});
	$('.chart3').easyPieChart({
		animate:false,
		//trackColor:false,
		barColor: '${totalUserInfo.upper_scorestat_color }',
		trackColor: '#f1f1f1',
		lineWidth :chart_lineWidth,
		size : chart_size,
		scaleColor:false
	});
	$('.chart4').easyPieChart({
		animate:false,
		//trackColor:false,
		barColor: '${totalUserInfo.collabo_scorestat_color }',
		trackColor: '#f1f1f1',
		lineWidth :chart_lineWidth,
		size : chart_size,
		scaleColor:false
	});
	$('.list_contents_body').on('click', '.btn_policy_view' ,function(){
		
        var secPolId = $(this).attr('secPolId');
        var emp_no = $(this).attr('emp_no');
        var searchDate = $(this).attr('searchDate');
    	var search =  {sec_pol_id: secPolId,
    			emp_no: emp_no,
    			searchDate: searchDate};
    	var param = escape(encodeURIComponent(JSON.stringify(search)));
        
		var pw = window.open('/main/userPolIdxInfoPopupEx.do?param='+param,'','width=1390, height=920,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
    		
		
	});
	//btn_totallog_view
	$('.list_contents_body').on('click', '.btn_totallog_view' ,function(){
		
        var majrCode = $(this).attr('diagMajrCode');
        var org_code = $(this).attr('org_code');
        var searchDate = $(this).attr('searchDate');
        var isSubOrg = $(this).attr('is_sub_org');
        var emp_no = $(this).attr('emp_no');
    	var search =  {majrCode: majrCode,
    			org_code: org_code,
    			emp_no: emp_no,
    			searchDate: searchDate,
    			buseoType: isSubOrg};
    	
    	var param = escape(encodeURIComponent(JSON.stringify(search)));
        
		var dpw = window.open('/dash/detailLogViewPopup.do?param='+param,'','width=1390, height=750,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
    	if(dpw != null){
    		dpw.focus();
    	}
		
	});
	
	$('.result_status').on('click', '.btn_totallog_excel' ,function(){
		
        var majrCode = $(this).attr('majrCode');
        var org_code = $(this).attr('org_code');
        var searchDate = $(this).attr('searchDate');
        var isSubOrg = $(this).attr('isSubOrg');;
        var emp_no = $(this).attr('emp_no');
    	var search =  {majrCode: majrCode,
    			org_code: org_code,
    			emp_no: emp_no,
    			searchDate: searchDate,
    			buseoType: isSubOrg};
    	
    	var param = encodeURIComponent(JSON.stringify(search));
    	$('#param').val(param);
    	document.forms["listForm"].action = "/dash/detailLogViewPopupExportExcel.do";
		document.forms["listForm"].submit();
		
		
	});
	
});

function goPage(url) {

    document.forms["listForm"].action = url;
    document.forms["listForm"].method = 'post';
    document.forms["listForm"].submit();
}
</script>
 
</head>

<body>

<!-- S : header -->
	<!-- Top Menu Begin -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
	<!-- Top Menu End -->
<!-- E : user info -->

<!--container -->
<div id="container">
    <!-- S : left content -->
    <div class="snb">
        <!-- S : socre -->
        <div class="block1">
            <li class="blockcell1"><img style="width:26px;height:26px;" src="/img/btn_plus.png" /></li>
            <li class="blockcell2">수집 날짜 ㅣ ${totalUserInfo.day_val }</li>
        </div>        
        <div class="block_score">
            <div style='border:0px solid red;' id="container_gaug"></div>
            <div style='border:0px solid red;margin-top:5px;' class="score_cell3">
            	<img src="/img/icon_notice_top.png" /> 현재 보안등급 <span style="color:${totalUserInfo.scorestat_color};">${totalUserInfo.scorestat_name }</span>단계입니다.
            </div>            
        </div>
        <!-- E : socre -->
        <div class="graph4 result_list">
        <c:forEach var="result" items="${policyScoreList}" varStatus="status">
        <c:choose>
        	<c:when test="${result.score eq \"-999\"}">
        		<div style="cursor:pointer;" class="graph4_date5 diag_majr" org_code="${result.org_code }" diagcode="${result.diag_majr_code}" diagDesc="${result.diag_desc}" buseoindc="${result.buseo_indc}" is_sub_org="${result.is_sub_org }">
	                <li class="dateimg"><img style="width:50px;height:50px;" src="/img/${result.diag_icon_info }" /></li>
	                <li class="datecell1"><span>${result.diag_desc }</span></li>
	                <li class="datecell2">-점 <span class="score_status" style="background:#f1f1f1;">&nbsp;&nbsp;-&nbsp;&nbsp;</span></li>
	            </div>
        	</c:when>
        	<c:otherwise>
        		<div style="cursor:pointer;" class="graph4_date5 diag_majr" org_code="${result.org_code }" diagcode="${result.diag_majr_code}" diagDesc="${result.diag_desc}" buseoindc="${result.buseo_indc}" is_sub_org="${result.is_sub_org }">
	                <li class="dateimg"><img style="width:50px;height:50px;" src="/img/${result.diag_icon_info }" /></li>
	                <li class="datecell1"><span>${result.diag_desc }</span></li>
	                <li class="datecell2">${result.score }점 <span class="score_status" style="background:${result.scorestat_color };">${result.scorestat_name }</span></li>
	            </div>
        	</c:otherwise>
        </c:choose>
            
        </c:forEach>            
        </div>        
    </div>
    <!-- E : left content -->
    <!--content -->
    <div id="content">
    	<div class="main_block1">
        	<div class="main_blockcell1">
                <div class="index_block1_1">
                    <div class="graph2_block1">
                     <c:choose>
	                    <c:when test="${totalUserInfo.score eq \"-999\"}">
			        		<li class="cell1">${totalUserInfo.emp_nm }</li>
			                <li class="cell2"><div class="chart1" data-percent="0"></div> </li>
			                <li class="cell3">score<span>-</span></li>
			        	</c:when>
			        	<c:otherwise>
			        		<li class="cell1">${totalUserInfo.emp_nm }</li>
			                <li class="cell2"><div class="chart1" data-percent="${totalUserInfo.score }"></div> </li>
			                <li class="cell3">score<span>${totalUserInfo.score }</span></li>
			        	</c:otherwise>
			        </c:choose>
                    </div>
                    <div class="graph2_block1">
                        <li class="cell1">${totalUserInfo.u_org_nm }</li>
		                <li class="cell2"><div class="chart2" data-percent="${totalUserInfo.u_avg }"></li>
		            	<li class="cell3">score<span>${totalUserInfo.u_avg }</span></li>
                    </div>
                    <div class="graph2_block1">
                        <li class="cell1">${totalUserInfo.upper_org_nm }</li>
		                <li class="cell2"><div class="chart3" data-percent="${totalUserInfo.upper_avg }"></li>
		            	<li class="cell3">score<span>${totalUserInfo.upper_avg }</span></li>
                    </div>
                    <c:if test="${totalUserInfo.iscollabor_cap eq 'Y' }" >
                    <div class="graph2_block1">
                        <li class="cell1">${totalUserInfo.collabor_org_nm }</li>
		                <li class="cell2"><div class="chart4" data-percent="${totalUserInfo.collabo_avg_val }"></li>
		            	<li class="cell3">score<span>${totalUserInfo.collabo_avg_val }</span></li>
                    </div>  
                    </c:if>                                          
                </div>            
            </div>
            <div class="main_blockcell2">
                <!-- S : notice -->
                <h4>Notice</h4>
                <div class="mnoticeTT"><span><img src="/img/icon_notice.png" /> Notice </span><a href="/man/noticelist.do">+ more</a></div>
                <ul class="mnotice">
                <c:forEach var="result" items="${noticeList}" varStatus="status">
                    <li><a style='cursor:pointer;border:0px solid red;padding-left:0px;' class='dash_notice_view' title='${result.title }' nseq='${result.sq_no}'>${fn:substring(result.title, 0, 20)}<c:if test="${fn:length(result.title) > 20}" >...</c:if></a> <span style="border:0px solid red">${result.upd_date}</span></li>
                </c:forEach>
                </ul>            
                <!-- E : notice-->            
            </div>
        </div>
        <div class="main_block2">
            <div class="index_block3cell">
                <li class="cell1 result_title"><img src="/img/icon_index.png" /> 지수점수 > PC보안</li>
            	<li class="cell2 result_status">전체 정책 <span class="txcell1">양호0개</span> <span class="txcell2">취약0개</span>
            </div>
            <div class="index_tbl list_contents_body" style="min-height:300px;overflow-y:auto;height:${totalUserInfo.scorelist_panel_height};">
                
            </div>                   
        </div>
    </div>
    <!--//content -->
    
</div>
	<!--aside -->
    <div class="aside">
    	<ul>
        	<li><a href="/man/noticelist.do"><img src="/img/icon_right_1.png" /><span style="color:#fff;">공지사항</span></a></li>
            <li><a href="/man/qnaList.do"><img src="/img/icon_right_2.png" /><span style="color:#fff;">Q&amp;A</span></a></li>
            <li><a href="/man/faqlist.do"><img src="/img/icon_right_3.png" /><span style="color:#fff;">FAQ</span></a></li>
            <li class="cell1"><img src="/img/icon_right_4.png" />Security<br/>Day</li>
        </ul>
    </div>	
<!--//container -->
    <!-- E : center contents -->
    <div style="padding:35px 0 0 0;clear:both;"></div>
	<form id="searchVO" name="listForm" action="asdf" method="post">
		<input type="hidden" name="majrCode" id="majrCode" value="" />
        <input type="hidden" name="buseoIndc" id="buseoIndc" value="" />
        <input type="hidden" name="majrName" id="majrName" value="" />
        <input type="hidden" name="majCode" id="majCode" value="" />
        <input type="hidden" name="minCode" id="minCode" value="" />
        <input type="hidden" name="polCode" id="polCode" value="" />
        <input type="hidden" name="buseoType" id="buseoType" value="N" />
        <input type="hidden" name="auth" id="auth" value="${auth }" />
        <input type="hidden" name="cap_emp_no" id="cap_emp_no" value="${searchVO.emp_no }" />
        <input type="hidden" name="param" id="param" value="" />
    </form>
	
    <!-- S : footer -->
	<!-- footer -->
	<div id="wrap_footer">
		<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
	</div>
	<!-- footer end -->
    <!-- E : footer -->
    <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
	<div class='DialogBox'>
	
	</div>    
</div>
<!-- E : contents -->

</body>

</html>