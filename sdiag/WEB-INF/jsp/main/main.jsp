<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/js/Slider/jssor.utils.js"></script>
<script type="text/javascript">

$(function () {		
   
});
</script>
    
</head>

<body>
	<!-- body -->
	<div id="wrap_body">
		<!-- head & menu -->
		<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
		
		<div id="wrap_main">
			<div class="WM_line1">
				<div class="L1_box1">
					<ul>
						<li><img src="/img/img_db1.png"></li>
						<li class="L1B1_txt1">기획본부의 보안 점수 : 주의</li>
						<li class="L1B1_txt2">보안기획 본부장 : 홍길동 전무</li>
					</ul>
				</div>
				<div class="L1_box2">
					<ul>
						<li class="WM_tit1">
							<div><img src="/img/dot2.png"></div>
							<div>보안수준진단 추이</div>
						</li>
						<li><img src="/img/img_graph1.png"></li>
					</ul>
				</div>
			</div>
			<div class="WM_line2">
				<div class="L2_box1">
					<ul>
						<li class="WM_tit1">
							<div><img src="/img/dot2.png"></div>
							<div>진단항목별 발생건수</div>
						</li>
						<li><img src="/img/img_graph2.png"></li>
					</ul>
				</div>
				<div class="L2_box2">
					<ul>
						<li class="WM_tit1">
							<div><img src="/img/dot2.png"></div>
							<div>조직별 지수점수</div>
						</li>
						<li>
							<table border="0" class="TBS2" cellpadding=0 cellspacing=0>
								<colgroup>
									<col style="width:50%">
									<col style="width:25%">
									<col style="width:25%">
								</colgroup>
								<tr>
									<th>조직</th>
									<th>점수</th>
									<th>상태</th>
								</tr>
								<tr>
									<td>기획1담당<br>(담당 : 박길동 상무)</td>
									<td>30</td>
									<td>취약</td>
								</tr>
								<tr>
									<td>기획2담당<br>(담당 : 김길동 상무)</td>
									<td>15</td>
									<td>취약</td>
								</tr>
								<tr>
									<td>기획3담당<br>(담당 : 이길동 상무)</td>
									<td>5</td>
									<td>양호</td>
								</tr>
								<tr>
									<td>기획4담당<br>(담당 : 고길동 상무)</td>
									<td>0</td>
									<td>양호</td>
								</tr>
							</table>
						</li>
					</ul>
				</div>
			</div>
		</div>
	
	</div>
	<!-- body end -->
	<!-- footer -->
	<div id="wrap_footer">
		<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
	</div>
	<!-- footer end -->

</body>

</html>