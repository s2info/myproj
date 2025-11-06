<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>샘플페이지</title>
</head>
<body>
	<div id="wrap_body">
		
		
		<div id="wrap_contents">
		<!-- Contents Begin -->
		<!-- toMailAddress -->
		
		
		<form:form commandName="searchVO" name="listForm" method="post">
			<div class="SB1_tit">
				<ul>
					<li><img src="/img/ic_tit1.png" alt="타이틀"></li>
					<li class="SB1T_txt">공지사항</li>
				</ul>
			</div>
			<div class="contents">
				
				<!-- 
				<div class="SC_box2">
					<ul>
						<li class="left1">총 <c:out value="${totCnt }"/>건</li>
						<li class="right1">
							<div class="button2 left1"><a href="#">인쇄</a></div>
							<div class="button2 left1"><a href="#">엑셀</a></div>
						</li>
					</ul>
				</div>
				 -->
				 ${bpmurl }<br><br><br><br>
				 ${returnbpm }
				<div class="SC_area">
					<table border="0" class="TBS1" cellpadding=0 cellspacing=0 summary="공지사항">
						<caption>공지사항</caption>
						<colgroup>
							<col style="width:10%">
							<col style="width:*">
							<col style="width:15%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
						</colgroup>
						<tr>
							<th>NO.</th>
							<th>제목</th>
							<th>첨부파일</th>
							<th>상태</th>
							<th>등록일</th>
							<th>조회수</th>
							<th>보기</th>
						</tr>
					
				</div>
				
				<div class="margin_t"></div>
				<div style="display:table-cell;vertical-align:middle;width:1280px;height:60px;text-align:center;">
					<div class="number1">
						<ul id="paging"></ul>
					</div>
				</div>
				
				<!-- <div class="button5 right1"><a href="/community/noticeRegisterPage.do">등록</a></div> -->
			</div>
				
		</form:form>
		
		</div>
		<!-- Contents End -->
		
		<div class='DialogBox'></div>
	</div>


	
</body>
</html>