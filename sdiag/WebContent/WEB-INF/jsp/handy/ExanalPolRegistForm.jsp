<%--
  Class Name : ExanalPolRegistForm.jsp
  Description : ExanalPolRegistFrom 처리 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2015.10.27   CJLEE              최초 생성
d 
    author   : LEE CHANG JAE
    since    : 2015.10.27
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value='/'/>css/style.css" rel="stylesheet" type="text/css" >

<title>정책 엑셀파일 등록</title>
<script type="text/javaScript" language="javascript">
<!--
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function list_Pol(){
    location.href = "<c:url value='/handy/ExanalPolList.do'/>";
}
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function regist_ExcelPol(){
    var varForm              = document.all["Form"];

    // 파일 확장명 확인
    var arrExt      = "xls";
    var objInput    = varForm.elements["fileNm"];
    var strFilePath = objInput.value;
    var arrTmp      = strFilePath.split(".");
    var strExt      = arrTmp[arrTmp.length-1].toLowerCase();

    if (arrExt != strExt) {
        alert("엑셀 파일을 첨부하지 않았습니다.\n확인후 다시 처리하십시오. ");
        abort;
    } 
    
    varForm.action           = "/handy/ExanalPolRegist.do";
    varForm.submit();

}

/* ********************************************************
* 미리보기화면
******************************************************** */

$('.DialogBox').dialog({
    autoOpen: false,
    modal: true,
    resizable: false,
    show: "fade",
    hide: "fade",
    close: function () { 
        $(this).dialog('close'); 
        var $ddata = $(".DialogBox");
        if($ddata.data('is_save') == 'Y'){
        	searchList($('#pageIndex').val());
        }
    },
    open: function () {
        var $ddata = $(".DialogBox");
        $('.ui-widget-overlay').bind('click', function () {
            $ddata.dialog('close');
        });
    }
});
$('.DialogBox').click(function () {
	$('.DialogBox').dialog('close');
});

function excelPreview(data){
	
	formData = document.getElementById("Form");
	
	formData.append("fileNm", $("input[name=fileNm]")[0].files[0]);
	
	alert(1212);
	
    $.ajax({
		url : '/handy/ExanalPolPreview.do',
		data : formData,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			alert(textStatus + "\r\n" + errorThrown);
		},
		success : function(data) {
			if (data.ISOK) {
				 $('.DialogBox').html(data.previewList);
                 $('.DialogBox').dialog({ width: 600, height: 465 });
                 $('.DialogBox').dialog('open');
                 $('.DialogBox').data('is_save', 'N');
                 $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.WP_tit' });
			} else {
				alert(data.MSG);
			}

		}

	});
	
}
//-->
</script>

</head>

<body>
<noscript>자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>

<!-- 현재위치 네비게이션 시작 -->
<div class="search1" id="item">

    <!-- 검색 필드 박스 시작 -->
    <div id="search_field">
        <div id="search_field_loc"><h2><strong>엑셀파일 등록</strong></h2></div>
    </div>
    <form name="Form" id="Form" action="<c:url value='/handy/ExanalPolRegist.do'/>" method="post" enctype="multipart/form-data" >
		<div class="modify_user" >
			<table summary="엑셀파일을 첨부할 수 있는 등록 테이블이다.">
				<tr>
				<th width="20%" height="23" class="required_text" scope="row" nowrap ><label for="fileNm">정책 엑셀파일</label></th>
				<td><input name="fileNm" type="file" id="fileNm"/></td>
				<td><div class="btn3"><a href="#noscript" class="preViewButton">데이터 미리보기</a></div></td>
				</tr>
			</table>
		</div>

        <!-- 버튼 시작(상세지정 style로 div에 지정) -->
        <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
            <!-- 목록/저장버튼  -->
            <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
              <td width="10"></td>
              <td>
                <div class="btn3 btn_stat_list" s_val=""><a href="#noscript" class="ON" onclick="regist_ExcelPol(); return false;">엑셀데이터 저장</a></div> 
              </td>
            </tr>
            </table>
        </div>
       	<!-- 버튼 끝 -->  
        
         <input name="cmd" type="hidden" value="ExcelPolRegist"/>                         

    </form>
    
    <div class='DialogBox'></div>

</div>  

<div class="marT10"/>
<!-- //content 끝 -->    
<script language="javascript">
<!--
	$('.preViewButton').click(function (){
	
	var varForm              = document.all["Form"];

	// 파일 확장명 확인
	var arrExt      = "xls";
	var objInput    = varForm.elements["fileNm"];
	var strFilePath = objInput.value;
	var arrTmp      = strFilePath.split(".");
	var strExt      = arrTmp[arrTmp.length-1].toLowerCase();
	
	if (arrExt != strExt) {
	    alert("엑셀 파일을 첨부하지 않았습니다.\n확인후 다시 처리하십시오. ");
	    abort;
	} 	
	
	var data = {};
	excelPreview(data);
	});
//-->
</script>

</body>
</html>

