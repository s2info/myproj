<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- <%@ page import="nets.websso.ssoclient.authcheck.*" %>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.ui.ModelMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="sdiag.login.service.UserManageVO" %>
<%@ page import="sdiag.member.service.MemberVO"%>
<%@ page import="sdiag.util.CommonUtil"%>
<%@ page import="sdiag.util.RequestWrapperForSecurity"%>
<%@ page import="egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper"%>
<%@ page import="sdiag.com.service.CommonService" %>
<%

	SSOConfig.request = request;
    AuthCheck auth = new AuthCheck(request, response);
    AuthStatus status = auth.CheckLogon(AuthCheckLevel.Medium);
    
    // 인증이 안되었을때, 이동시킬 SSO 로그인 URL (추후, URL 변경될 수 있음)
    String loginUrl =  "http://sldm.kt.com/"; //개발
    //String loginUrl =  "http://kate.kt.com/?" + SSOConfig.ReturnURLTagName() + "=" + Util.URLEncode(auth.ThisURL(), "UTF8"); //운영
    if(status == AuthStatus.SSOFirstAccess) //SSO 인증검사 : 최초접근 (멀티도메인시에도 체크됨)
    {
        auth.TrySSO();
    }
    else if(status == AuthStatus.SSOFail)   // SSO 인증검사: 실패
    {
        if(auth.ErrorNumber() != ErrorCode.NO_ERR)
        {
            String errMsg = ErrorMessage.GetMessage(auth.ErrorNumber());
            System.out.println(" NSSO Error Message :: "  + errMsg);
            response.sendRedirect(loginUrl);
        }
        else
        {
            response.sendRedirect(loginUrl);
        }
    }
    else if(status == AuthStatus.SSOSuccess)    //SSO 인증검사 : 성공
    {
        // 인증된 사용자 ID 받기
        String struid = auth.UserID();
       // response.getWriter().write("사용자 ID: " + struid + "<br>");   // ID받기 테스트
        
        response.getWriter().write("<form name=\"frm\" id=\"frm\" method=\"post\" autocomplete=\"off\" action=\"/nossloginProcess.do\">");
        response.getWriter().write(String.format("<input type=\"hidden\" id=\"u1\" name=\"u1\" value=\"%s\" />",struid));
        response.getWriter().write("<input type=\"hidden\" id=\"p1\" name=\"p1\" value=\"12345\" />");
        response.getWriter().write("</form>");
        response.getWriter().write("<script type=\"text/javascript\">");
        response.getWriter().write("document.frm.submit();");
        response.getWriter().write("</script>");
        response.getWriter().write("");
      
    }
    else if(status == AuthStatus.SSOUnAvaliable)    //SSO 인증검사 : SSO서버 중지
    {
        System.out.println(" NSSO UnAvailable :: " + status);
        response.sendRedirect(loginUrl);
    }
%> --%>
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
<link rel="stylesheet" type="text/css" href="css/login_red.css" />
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

<div id="etc_header"><h1>KT임직원 보안수준진단</h1></div>
<form name="frm" id="frm" method="post" autocomplete="off" action="/loginProcess.do">
<div class="etc_area">
	<div class="login_img"><img src="/img/login_img.png" /></div>
    <!-- S : login -->
    <div class="etc_box">
        <ul class="blockLoginS1">
            <li class="bls1cell1">
                <p><input type="text" id="u1" name="u1" maxlength="9" onkeyup="if(this.value.length==9)document.getElementById('p1').focus();" class="loginid" autofocus="autofocus" required value="" title="UserID" /></p>
                <p><input type="password" id="p1" name="p1" maxlength="20" class="loginpw" onkeyup="if(event.keyCode==13){login();}" title="password" class="loginpw" onfocus="inputSelect(this);"  /></p><br />
            </li>
            <li class="bls1cell2"><a href='javascript:login();' class="btn_login"><span>LOGIN</span></a></li>
        </ul>
    </div>
    <!-- E : login -->
</div>
<input type="hidden" name="returl" value="${returnUrl }" />
</form>
<div class="footer">Copyright ⓒ <span>KT</span> All rights reserved.</div>
<div class='DialogBox'></div>
</body>
</html>