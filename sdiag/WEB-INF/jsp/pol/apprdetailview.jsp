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
<script src='/js/jquery.MultiFile.js' type="text/javascript" language="javascript"></script>
<script type="text/javascript">

$(function () {		
	$("input[name=att_file]").MultiFile(
	{
		max: 1, 
	    accept: 'jpg|png|gif|doc|pdf|pptx|docx|xls|xlsx|vsd', //허용할 확장자(지정하지 않으면 모든 확장자 허용)
	    maxfile: 1024, //각 파일 최대 업로드 크기
	    maxsize: 3024,  //전체 파일 최대 업로드 크기
	    STRING: { //Multi-lingual support : 메시지 수정 가능
			remove : "제거", //추가한 파일 제거 문구, 이미태그를 사용하면 이미지사용가능
			duplicate : "$file 은 이미 선택된 파일입니다.", 
			denied : "$ext 는(은) 업로드 할수 없는 파일확장자입니다.",
			selected:'$file 을 선택했습니다.', 
			toomuch: "업로드할 수 있는 최대크기를 초과하였습니다.($size)", 
			toomany: "업로드할 수 있는 최대 갯수는 $max개 입니다.",
			toobig: "$file 은 크기가 매우 큽니다. (max $size)"
		},
		list:".flist" //파일목록을 출력할 요소 지정가능
	});
	
	$('.btn_appr_save').click(function () {
		if(!confirm('조치내역을 저장 하시겠습니까?')){
    		return false;
    	}
		
		//document.filePopFrm.action = "<c:url value='/pol/apprdetalreg.do'/>";
		//document.filePopFrm.submit();
		
	
    });
	
	$('.file_delete').click(function(){
		var apprid = $('#apprid').val();
		if (confirm("파일을 삭제하시겠습니까?")){
			$.ajax({
				url: '/appr/fileDelete.do',
				data: {apprid:apprid},
				type: 'POST',
				dataType: 'json',
				success: function(result){
					location.reload();
				}
			});	
		}
	});
});
</script>
    
</head>
<body>
<div class="ly_block1">
    <div class="subTT" ><span>소명신청정보</span></div>
    <div class="popTT"><img src="/img/icon_arw4.jpg" /> 소명신청 상세</div>
    <form name="filePopFrm" id="filePopFrm" method="post" enctype="multipart/form-data">
    <table class="tblInfoType" cellpadding="0" cellspacing="0" >
        <colgroup>
            <col style="width:20%;" />
            <col style="width:80%;" />
        </colgroup>
        <tr>
			<th>제목</th>
			<td> [<c:out value="${apprInfo.poldesc }" />] 소명신청</td>
		</tr>
		<tr>
			<th>지수화정책</th>
			<td>
				<c:out value="${apprInfo.soldesc }" /> - <c:out value="${apprInfo.majdesc }" /> - <c:out value="${apprInfo.mindesc }" />
			</td>
		</tr>
		<tr>
			<th>진단 결과</th>
			<td>
				건수 : <c:out value="${apprInfo.count }" />, 점수 : <c:out value="${apprInfo.score }" />
			</td>
		</tr>
		<tr>
			<th>상세 내역</th>
			<td style='padding-top:5px;padding-bottom:5px;'>
				<table border='0' class='ly_tbl' cellpadding=0 cellspacing=0>
					<colgroup>
						<col style="width:20%">
						<col style="width:20%">
						<col style="width:20%">
						<col style="width:20%">
						<col style="width:20%">
					</colgroup>
					<tr style='height:30px;'>
						<th>진단일</th>
						<th>소속</th>
						<th>이름</th>
						<th>건수</th>
						<th>점수</th>
					</tr>
					<tr style='height:30px;'>
						<td class='cell2'><c:out value="${apprInfo.rgdtdate }" /></td>
						<td class='cell2'><c:out value="${apprInfo.orgnm }" /></td>
						<td class='cell2'><c:out value="${apprInfo.empnm }" /></td>
						<td class='cell2'><c:out value="${apprInfo.count }" /></td>
						<td class='cell2'><c:out value="${apprInfo.score }" /></td>
				
					</tr>
				</table>	
			</td>
		</tr>
		<tr style='height:100px;'>
			<th>조치 내역</th>
			<td style='padding-top:5px;padding-bottom:5px;'>
				<textarea id='appldesc' name='appldesc' cols=10 rows=7 class="inputarea"><c:out value="${apprInfo.appldesc }" /></textarea>
			</td>
		</tr>
		<c:if test="${apprInfo.isattach == '0'}" >
		<tr>
			<th>증빙자료</th>
			<td><c:choose><c:when test="${apprInfo.filename == '' }"><input type="file" name='att_file' id='att_file' style="width:90px"><span class='flist'></span></c:when><c:otherwise><a href="/appr/filedownload.do?apprid=<c:out value="${apprid }" />" /><c:out value="${apprInfo.filename }" /></a> <c:if test="${ispgm != '1'}" ><a class='file_delete' style='cursor:pointer;'>삭제</a></c:if></c:otherwise></c:choose> </td>
		</tr>
		</c:if>
		<input type="hidden" name="fName" id="fName" value="<c:out value="${apprInfo.filename }" />" /><input type="hidden" name="lName" id="lName" value="<c:out value="${apprInfo.fileloc }"/>" />
		<tr>
			<th>주의사항</th>
			<td>
				${apprInfo.apprnote }
			</td>
		</tr>        
    </table> 
   
    <div class="btn_black2" style='width:99%;padding-right:20px;'><a href="javascript:self.close();" class="btn_black"><span>닫기</span></a></div> 
    <input type="hidden" name="apprid" id="apprid" value="<c:out value="${apprid }" />" />
	<input type="hidden" name="ispgm" id="ispgm" value="<c:out value="${ispgm }" />" />  
	 </form>        
</div>
</body>
</html>