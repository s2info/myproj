<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
	<body>
		1111
		<div id="wrap_pop">
		<!-- Contents Begin -->	
			<div class="WP_tit">
				<ul>
					<li class="WPT_ic"><img src="/img/ic_stit2.png" alt="아이콘"></li>
				<li class="WPT_txt">권한 등록</li>
				</ul>
			</div>
			
			<form name="checkFrm" id="checkFrm" method="post">
			<input />
			
			<div class="marT10" style="border:1px solid red; width: 100%">
			<table border="0" class="TBS3" cellpadding="0" cellspacing="0" summary="권한등록">
			<caption>권한등록</caption>
			<colgroup>
			<col width="20%" />
			<col width="*" />
			<col width="20%" />
			<col width="30%" />
			</colgroup>
			<tr>
				<th>사용자</th>
				<td colspan="3">
				<input name="ctl00$ctl00$body$body$tbCode" type="text" readonly="readonly" id="body_body_tbCode" class="input_txt" onkeypress="u$.OnEnterKeypress(this,null);" /> <input type="button" id="btnFindPerson" name="btnFindPerson"  class="button" value="사용자찾기" title="사용자찾기" onclick="onFindPerson();" /> </td>
				
			</tr>
			<tr>
				<th>권한</th>
				<td><select name="" id="" class="input_txt">
				<option selected="selected" value="0010">관리자</option>
				<option selected="selected" value="0020">운영자</option>
				</select></td>
				
                <th>사용여부</th>
				<td><select name="" id="" class="input_txt">
				<option selected="selected" value="0010">Y</option>
				<option selected="selected" value="0020">N</option>
				</select></td>
				
			</tr>
			<tr>
				<th>메일수신</th>
				<td><select name="" id="" class="input_txt">
				<option selected="selected" value="0010">Y</option>
				<option selected="selected" value="0020">N</option>
				</select></td>
			</tr>
			<tr>
				<th>IP</th>
				<td>
				
				</td>
			</tr>
		</table>
			  </div>
			  
			  <div class="marT10"></div>
				<div style="display:table-cell;vertical-align:middle;width:1280px;height:60px;text-align:center;">
				<div class="number1">
					<ul id="paging"></ul>
				</div>
				
				<div class="margin_t"></div>
					<div style='width:150px;margin:0 auto;'>
						<div class='btn4'><a href="/man/noticeRegisterPage.do" style="cursor:pointer;">저장</a></div>
						<div class='btn4'><a href="javascript:checkPopClose();">닫기</a></div>
					</div>
			
			</form>	
		</div>
	</body>
</html>