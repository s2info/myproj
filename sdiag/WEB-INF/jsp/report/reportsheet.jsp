<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<span>진단점수 (평균) :</span><span><c:out value="${jindanAvg }" /></span>
<br>
<span><c:out value="${personalAvg }" /></span>
<br><br>

<p>
※조직	
<span>진단점수 (평균) :</span><span><c:out value="${jindanOrgAvg }" /></span>
		
<div><c:out value="${tableTitleO }" /></div>
<table><tr>
<td>
<c:out value="${OrgPolTotcountAvg}" />
</td>
</tr>
</table>

<!-- 개인 지수항목별 점수 현황 -->			
<p>
<div><c:out value="${tableTitlePC }" /></div>
<c:forEach var="result" items="${PerPolCount}" varStatus="status">
<table><tr>
<td>
<span>${result.idxrgdtdate}</span>
</td>
<td>
<span>${result.empnm}</span>
</td>
<td>
<span>${result.mac}</span>
</td>
<td>
<span>${result.ip}</span>
</td>
<td>
<span>${result.descname}</span>
</td>
<td>
<span>${result.count}</span>
</td>
<td>
<span>${result.avg}</span>
</td>
<td>
<span>${result.idxstatus}</span>
</td>
</tr>
</table>
</c:forEach>      			

<!-- 사용자별(= 팀원) -->
<p>
<div><c:out value="${tableTitleT }" /></div>
<c:forEach var="result" items="${PerTeam}" varStatus="status">
<table><tr>
<td>
<span>${result.regdate}</span>
</td>
<td>
<span>${result.orgnm}</span>
</td>
<td>
<span>${result.empnm}</span>
</td>
<td>
<span>${result.mac}</span>
</td>
<td>
<span>${result.ip}</span>
</td>
<td>
<span>${result.count}</span>
</td>
<td>
<span>${result.avg}</span>
</td>
<td>
<span>${result.idxstatus}</span>
</td>
</tr>
</table>
</c:forEach>      
        

<p>
<div><c:out value="${tableTitleP2 }" /></div>
<c:forEach var="result" items="${OrgPolcount}" varStatus="status">
<table><tr>
<td>
<span>${result.sumrgdt}</span>
</td>
<td>
<span>${result.orgnm}</span>
</td>
<td>
<span>${result.diagdesc1}</span>
</td>
<td>
<span>${result.diagdesc2}</span>
</td>
<td>
<span>${result.diagdesc3}</span>
</td>
<td>
<span>${result.count}</span>
</td>
<td>
<span>${result.avg}</span>
</td>
<td>
<span>${result.polstat}</span>
</td>
</tr>
</table>
</c:forEach>

<p>
<div><c:out value="${tableTitleP3 }" /></div>
<c:forEach var="result" items="${OrgDiagItem}" varStatus="status">
<table><tr>
<td>
<span>${result.sumrgdtdate}</span>
</td>
<td>
<span>${result.orgnm}</span>
</td>
<td>
<span>${result.majrdiagdesc}</span>
</td>
<td>
<span>${result.minrdiagdesc}</span>
</td>
<td>
<span>${result.secpoldesc}</span>
</td>
<td>
<span>${result.totcount}</span>
</td>
<td>
<span>${result.avg}</span>
</td>
<td>
<span>${result.idxstatus}</span>
</td>
</tr>
</table>
</c:forEach>
				
</body>
</html>