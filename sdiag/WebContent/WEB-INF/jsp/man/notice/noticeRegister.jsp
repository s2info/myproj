<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javaScript" language="javascript">

</script>
<title>공지사항 등록</title>
</head>
<body>

	<form:form name="listForm" id="listForm" method="post" target="action">
		<input type="text" name="sq_no" id="sq_no" value="${borderInfo.sq_no }"/>
		<input type="text" name="flag" id="flag" value="${flag }"/>
		
		<table border="1" class="TBS1" cellpadding=0 cellspacing=0 summary="공지사항정보">
				<caption>공지사항정보</caption>
				<colgroup>
					<col style="width:20%">
					<col style="width:30%">
					<col style="width:20%">
					<col style="width:30%">
				</colgroup>
				<tr>
					<th class="align1 bg1">* 제목</th>
					<td class="align1" colspan="3"><input type="text" name="title" id="title" style="width:90%;" class="input22" value="${borderInfo.title }" maxlength="200"></td>
				</tr>
				<tr>
					<th class="align1 bg1">* 내용</th>
					<td class="align1" colspan="3" >
						<textarea name="contents" id="contents" class="textbox2" rows="10" style="width:70%; height:85%;" ><c:out value="${borderInfo.contents }"/></textarea>
					</td>
				</tr>
				<tr>
					<th class="align1 bg1">* 사용여부</th>
					<td class="align1">
						<select  name="is_popup" id="is_popup" class="choice2" style="width:100px">
							<option value="Y" ${borderInfo.is_popup eq 'Y' ? 'selected' : '' }>예</option> 
							<option value="N" ${borderInfo.is_popup eq 'N' ? 'selected' : '' }>아니요</option>
						</select>
					</td>
				</tr>
			</table>
			<div class="margin_t"></div>
			<div style="height:30px;">
					<div class="button1 right1"><a href="noticelist.do">닫기</div>
					<div class="button1 right1"><a href="javascript:onClick=save()">저장</div>
			</div>
			</form:form>
</body>
</html>
<script>
function save(){
	alert("1111");
	// 유효성 체크
	var titleCheck = document.getElementById('title');
	var contentsCheck = document.getElementById('contents');
		
	if(titleCheck.value == "" || titleCheck.value == null){
		alert(" 제목을 입력 하십시오");
		titleCheck.focus();
		 return ;
	}
	if(contentsCheck.value == "" || contentsCheck.value == null){
		alert(" 내용을 입력하세요");
		contentsCheck.focus();
		 return ;
	}
	// 자료실 DB등록
	if( confirm(" 저장 하시겠습니까?" ) ){
		document.listForm.action = "/notice/noticeRegister.do";
		document.listForm.submit();
	}
}
</script>