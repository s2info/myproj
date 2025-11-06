<%--
  Class Name : ExanalPolRegist.jsp
  Description : ExanalPolRegist 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2015.10.27   CJLEE              최초 생성
d 
    author   : LEE CHANG JAE
    since    : 2015.10.27
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url var="ImgUrl" value="/images"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Language" content="ko" >
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
//-->
</script>
</head>

<body>
<noscript>자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>    
<!-- 엑셀등록 레이어 전체 -->
<div id="wrap">

            <!-- 현재위치 네비게이션 시작 -->
            <div id="content">

                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>엑셀파일 등록</strong></h2></div>
                </div>
                <form name="Form" action="<c:url value='/handy/ExanalPolRegist.do'/>" method="post" enctype="multipart/form-data" >
                <input type="submit" id="invisible" class="invisible"/>
                    <div class="modify_user" >
                        <table summary="엑셀파일을 첨부할 수 있는 등록 테이블이다.">
						   <tr>
						    <th width="20%" height="23" class="required_text" scope="row" nowrap ><label for="fileNm">정책 엑셀파일</label></th>
						    <td><input name="fileNm" type="file" id="fileNm"/></td>
						  </tr>
                        </table>
                    </div>

                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
                        <!-- 목록/저장버튼  -->
                        <table border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr>
                        <!--  
                          <td>
                            <a href="#noscript" onclick="list_Pol(); return false;">목록</a>
                          </td>
                         -->
                          <td width="10"></td>
                          <td>
                            <a href="#noscript" onclick="regist_ExcelPol(); return false;"><spring:message code="엑셀데이터 저장" /></a> 
                          </td>
                        </tr>
                        </table>
                    </div>
                    <!-- 버튼 끝 -->  
                    
                     <input name="cmd" type="hidden" value="ExcelPolRegist"/>                         

                </form>

            </div>  
            <!-- //content 끝 -->    

</div>
<!-- //전체 레이어 끝 -->
</body>
</html>

