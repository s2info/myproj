<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
.topnotice {float:left;width:auto;font:bold 11px '돋움';padding:2px 0 0 0;}
</style>
<script type="text/javascript">

$(function () {	
	function setsession(target, params, formid, loginType){
		var data={"sessoinname":"loginType", "sessionval":loginType}
	     $.ajax({
				url : '/com/sessionupdate.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					if(jqXHR.status == 401){						
						alert('인증정보가 만료되었습니다.');						
						location.href='/';					
					}else{
						alert(textStatus + "\r\n" + errorThrown);	
					}
					
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
	
	$('.top_notice_view, .dash_notice_view, .dash_faq_view').click(function(){
		 var target='/man/noticeview.do';
		 if($(this).attr('class') == 'dash_faq_view'){
			 target='/man/faqview.do';
		 }
	     var formid='searchVO';
	     var params = [];
	     params[0] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'sqno'
	       , "inputvalue": $(this).attr('nseq')
	     });
	     params[1] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'n_type'
	       , "inputvalue": 'DASH'
	     });
	     gopage(target, params, formid);
	});

	$('.btn_pol_status').click(function(){
		pol_status_location($(this)); 
	});
	$('.all_menu').on('click', '.btn_pol_status', function(){
		pol_status_location($(this));
	});
	
	var pol_status_location = function(element){
		var majCode = element.attr('majCode');
		var minCode = element.attr('minCode');
		var polCode = element.attr('polCode');
		var loginType = element.attr('loginType');
		var mtype = element.attr('menutype');
		var buseoType = element.attr('buseoindc');
		var search_date = $.type($('#search_date').val()) === "undefined" ? "" : $('#search_date').val();
			var searchPolCondition = $.type($('#searchPolCondition').val()) === "undefined" ? "" : $('#searchPolCondition').val();
			var searchPolKeyword = $.type($('#searchPolKeyword').val()) === "undefined" ? "" : $('#searchPolKeyword').val();
		 if(mtype == 'T'){
			//메뉴이면
			majCode = $('#mCode').val();
			minCode = $('#nCode').val();
			polCode = $('#pCode').val();
			buseoType = $('#buseoType').val();
			searchPolCondition = "1";
			searchPolKeyword = "";
		 }
		 
		 var target='/pol/policystatus.do';
	     var formid='searchVO';
	     var params = [];
	     params[0] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'majCode'
	       , "inputvalue": majCode
	     });
	     params[1] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'minCode'
	       , "inputvalue": minCode
	     });
	     params[2] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'polCode'
	       , "inputvalue": polCode
	     });
	     params[3] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'begin_date'
	       , "inputvalue": search_date
	     });
	     params[4] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'searchCondition'
	       , "inputvalue": searchPolCondition
	     });
	     params[5] = JSON.stringify({
	         "inputtype": 'input'
	       , "inputname": 'searchKeyword'
	       , "inputvalue": searchPolKeyword
	     });
	     params[6] = JSON.stringify({
	    	 "inputtype": 'input'
	  	   , "inputname": 'buseoType'
	  	   , "inputvalue": buseoType
	     })
	     setsession(target, params, formid, loginType);
	}
	
	$('.topmenu').hover(
		function(){
			//$(this).css("border", "solid 1px red");
			var obj = $(this).offset();
			var left_val = obj.left;
			if($(this).text() == '관리자'){
				left_val = obj.left - 60;
				$('.all_menu').css("min-width", "170px");
			}else{
				$('.all_menu').css("min-width", "100px");
			}
			$('.all_menu').css("left", left_val + "px");
			$('.all_menu').html($(this).attr('subMenu'));
			$('.all_menu').slideDown(100);
		}/* ,
		function(){
			$('.all_menu').slideUp(100);
		} */
	);
	$('#container, #cnt').hover(function(){
		$('.all_menu').slideUp(100);
	});
	
});

</script>

<div class="dropdown">
<div id="header">
    <a href="/dash/dashboard.do"><h1>임직원 보안수준</h1></a>
    <h4>local navigation</h4>
    <ul class="lnb_menu">
        <%=request.getAttribute("topMenu") %>
    </ul>
</div>

<!-- S : All menu -->
	<%-- <%=request.getAttribute("menuMap") %> --%>
	<div class="all_menu"><!-- <div class="menuwarp">
		<ul>
			<li> PC 조회</li>
		</ul>
	</div> --></div>
</div>
<!-- E : All menu -->
<div id="user_info">
	<div class="topnotice"> 
		<!-- 2018.09.13 수정(디자인변경)  -->
		<%-- <%=request.getAttribute("topNotice") %> --%>
		<%=request.getAttribute("BtnLoginUserType") %>
	</div>
    <div class="block_user">
    	<img src="/img/icon_user.png" /> <span><%=request.getAttribute("loginName") %></span>님 안녕하세요.
    	<!-- 2018.09.13 수정(디자인변경)  -->
    	<%-- <%=request.getAttribute("BtnLoginUserType") %> --%>
    	<a href="/logout.do" class="btn_logout">로그아웃</a></div>
</div>