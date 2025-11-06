<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="description" content="임직원보안수준진단" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8;" >
<meta http-equiv="content-language" content="ko">
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>임직원 보안수준진단</title>

<script type="text/javascript" src="/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="/css/login_old.css" />
<style>
/*
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
*/
</style>
<script type="text/javascript">
var rMsg = '${requestScope.rMsg }';
$(document).ready(function (){
	
	$('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: true,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
        },
        open: function () {
            var $ddata = $(".DialogBox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	});
	
	
	if (rMsg != null && rMsg != ""){
		alert(rMsg);
	}
	
	$("#u1").keyup(function(e){
		if (e.keyCode == '13'){
			login();
		}
	});
	
	$("#p1").keyup(function(e){
		if (e.keyCode == '13'){
			login();
		}
	});
	
	$('.top_notice_view').click(function(){
		$.ajax({
            data: {nseq: $(this).attr('nseq')}, 
            url: "/common/topnoticeviewpopup.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + "\r\n" + errorThrown);
            },
            success: function (data) {
                if (data.ISOK) {
                	
                	 $('.DialogBox').html( data.popup_body);
                     $('.DialogBox').dialog({ width: 920, height: 680 });
                     contents();
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
                }
                else {
                    alert(data.MSG);
                }

            }
        });		
		
	});
	
	function contents() {
		
		var t = $('#contents').text();
		$('#contents').html(t.replace('&nbsp;',''));
	}
	
	$('#u1').focus();
});

//로그인
function login() {	
	if($('#u1').val() == '' || $('#p1').val() == ''){
		alert('사번 또는 비밀번호를 입력하여 주세요.');
		if($('#u1').val()==''){
			$('#u1').focus();
		}else{
			$('#p1').focus();
		}
		
	}else{
		document.frm.method = "post";
		document.frm.submit();	
	}
	
	
}

</script>


</head>
<body>

<!-- S : header -->
<div id="header">
	<div class="top_logo"><img src="/img/top_logo.png" /></div>
</div>
<!-- E : header -->
<!-- S : user info -->
<div id="user_info">
	<div class="topnotice"><img src="/img/icon_notice_top.png" /> 
	<c:choose><c:when test="${topNotice.sq_no > 0 }">	
		<a class='top_notice_view' nseq='${topNotice.sq_no }' style='cursor:pointer;'>${topNotice.title }</a> 
	</c:when>
	<c:otherwise>
	* 알림공지가 없습니다.
	</c:otherwise></c:choose>
	</div>
</div>
<!-- E : user info -->
<form name="frm" id="frm" method="post" autocomplete="off" action="/loginProcess.do">
<div id="cnt">
	<div><img src="/img/login_img_1.jpg" /></div>
    <div class="login_box">
        <ul class="blockLoginS1">
            <li class="bls1cell1">
                <p><label for="name">아 이 디</label></p><span><input type="text" id="u1" name="u1" maxlength="9" onkeyup="if(this.value.length==9)document.getElementById('p1').focus();" class="loginid" autofocus="autofocus" required value=""></span>
                <p><label for="password">비밀번호</label></p><span><input type="password" id="p1" name="p1" maxlength="20" class="loginid" value="" onkeyup="if(event.keyCode==13){login();}"></span>
            </li>
            <li class="bls1cell2"><a href='javascript:login();'><span class="btn_login">로그인</span></a></li>
        </ul>
    </div>
    <!-- <div><a href="#"><img src="/img/bnr_1.jpg" /></a></div> -->
</div>
<input type="hidden" name="returl" value="${returnUrl }" />
</form>
<!-- S : footer -->
<div id="footer">
    <div class="copyright"><img src="/img/bottom_logo.png" /> Copyright ⓒ <span>KT</span> All rights reserved.</div>
    <div class="copyright">문의처 : 1588-3391</div>
</div>
<div class='DialogBox'></div>
</body>
</html>