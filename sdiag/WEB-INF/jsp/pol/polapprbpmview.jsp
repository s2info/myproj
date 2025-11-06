<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.HashMap" %>
<% pageContext.setAttribute("newline", "\n"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/js/Slider/jssor.utils.js"></script>
<script type="text/javascript" src='/js/js/jquery.MultiFile.js'></script>
<script type="text/javascript">

$(function () {		
	$("input[name=att_file]").MultiFile(
	{
	    accept: '<c:out value="${fileExtend }" />', //허용할 확장자(지정하지 않으면 모든 확장자 허용)
	    maxsize: ${maxSize },  //전체 파일 최대 업로드 크기
	    max: ${fileCnt }, 
	    STRING: { //Multi-lingual support : 메시지 수정 가능
			remove : "삭제 ", //추가한 파일 제거 문구, 이미태그를 사용하면 이미지사용가능
			duplicate : "$file 은 이미 선택된 파일입니다.", 
			denied : "$ext 는(은) 업로드 할수 없는 파일입니다.",
			selected:'$file 을 선택했습니다.', 
			toomuch: "업로드할 수 있는 최대크기를 초과하였습니다.($size)", 
			toomany: "업로드할 수 있는 최대 갯수는 $max개 입니다."
		},
		list:".flist" //파일목록을 출력할 요소 지정가능
	});
	/*
	$('.btn_appr_save').click(function () {
		if(!confirm('조치내역을 저장 하시겠습니까?')){
    		return false;
    	}
		document.filePopFrm.method = 'post';
		document.filePopFrm.action = '/pol/apprdetalreg.do';
		document.filePopFrm.submit();
	});
	*/
	
	$('.btn_bpm_send').click(function(){
		if($('#appldesc').val() == ''){
			alert('소명(조치) 내용을 입력하여 주세요.');
			return false;
		}
		
		if(!confirm('소명(조치) 저장 후 결재승인을 요청 하시겠습니까?')){
    		return false;
    	}
		
		document.filePopFrm.method = 'post';
		document.filePopFrm.action = '/pol/apprdetalreg.do';
		document.filePopFrm.submit();
		
	});
	
	var formsubmit = function(){
		var frm = document.createElement('form');
	    frm.id='form1';
	    frm.action = '${bpmcall_url}';
	    frm.method = 'post';
	    frm.target="_self";
	    var param1 = document.createElement('input');
	    param1.name = 'procid';
	    param1.value ='${bpmidvlaue }';
	    frm.appendChild(param1);
	    var param2 = document.createElement('input');
	    param2.name = 'apprid';
	    param2.value ='${apprid }';
	    frm.appendChild(param2);
	    frm.style.display = 'none';
	    document.body.appendChild(frm);
	    frm.submit();
	}
	
	$(window).bind("load", function() {
		<c:if test="${issubmit == '1'}" >
		if(confirm('저장이 완료 되었습니다.\r\n\r\n결재승인을 요청 하시겠습니까?')){
			formsubmit();
		}
		</c:if>
	});
	
	/*
	var savecontnets = function(){
		var appldesc = $('#appldesc').val();
		var apprid = $('#apprid').val();
		$.ajax({
			url: '/appr/saveapprdesc.do',
			data: {apprid:apprid,appldesc:appldesc},
			type: 'POST',
			dataType: 'json',
			success: function(result){
				return true;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				return false;	
			}
		});	
	}
	*/
	$('.file_delete').click(function(){
		var p = $(this).closest('td');
		var apprid = $('#apprid').val();
		if (confirm("파일을 삭제하시겠습니까?")){
			$.ajax({
				url: '/appr/fileDelete.do',
				data: {apprid:apprid},
				type: 'POST',
				dataType: 'json',
				success: function(result){
					location.href="/pol/polapprbpmview.do?apprid=${apprid }";
					
				}
			});	
		}
	});
	
	
});
</script>
    
</head>
<body>
<div class="ly_block1" style="width:1000px;" >
    <div class="popTT"><img src="/img/icon_arw4.jpg" /> 소명신청 상세</div>
    <form name="filePopFrm" id="filePopFrm" method="post" enctype="multipart/form-data">
    <table class="tblInfoType" cellpadding="0" cellspacing="0" >
        <colgroup>
            <col style="width:20%;" />
            <col style="width:80%;" />
        </colgroup>
        <c:choose><c:when test="${reqtype == '0002' }">
        	<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">검출일</td>
				<td > <c:out value="${apprInfo.sldmorglogdate }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">경고정책명</td>
				<td ><c:out value="${apprInfo.alertpolicyname }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">조회자정보</td>
				<td >사번 : <c:out value="${apprInfo.dbuid }" />, 성명 : <c:out value="${apprInfo.empname }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">DB접속정보</td>
				<td >Client IP : <c:out value="${apprInfo.clientip }" />, DBMS IP : <c:out value="${apprInfo.serverip }" /> , DBMS Port : <c:out value="${apprInfo.serverport }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">개인정보탐지유형</td>
				<td> <c:out value="${apprInfo.abnormaldataname }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">개인정보탐지건수</td>
				<td> <fmt:formatNumber value="${apprInfo.abnormaldatacount}" pattern="#,###" /> 건 </td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">검출 쿼리정보</td>
				<td style='padding-top:5px;padding-bottom:5px;'>
					<textarea id='sqlcontent' name='sqlcontent' cols=10 rows=7 class="inputarea">${apprInfo.sqlcontent }</textarea>
				</td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">담당자정보</td>
				<td>사번 : <c:out value="${apprInfo.hptktempno }" />, 성명 : <c:out value="${apprInfo.hptktempnm }" /></td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">결재자정보</td>
				<td>
					결재자라인1 : <c:out value="${apprInfo.sign1 }" />
					<c:if test="${apprInfo.signlevel1 != ''}" >
						(<c:out value="${apprInfo.signlevel1 }" />)
					</c:if>	
					
					<c:if test="${apprInfo.sign2 != ''}" >
					, 결재자라인2 : <c:out value="${apprInfo.sign2 }" />
					<c:if test="${apprInfo.signlevel2 != ''}" >
						(<c:out value="${apprInfo.signlevel2 }" />)
					</c:if>	
					
					</c:if>
					<c:if test="${apprInfo.sign3 != ''}" >
					, 결재자라인3 : <c:out value="${apprInfo.sign3 }" />
					</c:if>
				</td>
			</tr>
			<tr style='height:100px;'>
				<th style="color:blue;">소명 내용</th>
				<td style='padding-top:5px;padding-bottom:5px;'>
					<textarea id='appldesc' name='appldesc' cols=10 rows=7 class="inputarea">${apprInfo.appldesc }</textarea>
				</td>
			</tr>
			<tr >
				<th style="color:blue;">소명증적첨부</th>
				<td><c:choose><c:when test="${apprInfo.filename == '' }"><input type="file" name='att_file' id='att_file' style="width:90px"><span class='flist'></span></c:when><c:otherwise><a href="/appr/filedownload.do?apprid=<c:out value="${apprid }" />" /><c:out value="${apprInfo.filename }" /></a> <c:if test="${apprInfo.apprstatcode == '01'}" ><a class='file_delete' style='cursor:pointer;'>삭제</a></c:if></c:otherwise></c:choose> </td>
			</tr>
			<tr >
				<td style="font:normal 12px 돋움;background:#f1f1f1;padding:15px;text-align: left;border-bottom: solid 1px #e9e9e9;color: #444444;">소명 주의사항</td>
				<td>${fn:replace(apprInfo.polnotice,newline,'<br />')}</td>
			</tr>
        </c:when><c:otherwise>
        	<tr>
				<th>제목</th>
				<td style="font-weight:bold;"> [<c:out value="${apprInfo.poldesc }" />] 소명신청</td>
			</tr>
			<tr>
				<th>지수화정책</th>
				<td style="font-weight:bold;">
					<c:out value="${apprInfo.soldesc }" /> - <c:out value="${apprInfo.majdesc }" /> - <c:out value="${apprInfo.mindesc }" />
				</td>
			</tr>
			<tr>
				<th>진단 결과</th>
				<td style="font-weight:bold;">
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
			
			<c:choose><c:when test="${apprInfo.apprstatcode == '01' }">
				<tr style='height:100px;'>
					<th>소명 내용</th>
					<td style='padding-top:5px;padding-bottom:5px;'>
						<textarea id='appldesc' name='appldesc' cols=10 rows=7 class="inputarea">${apprInfo.appldesc }</textarea>
						
					</td>
				</tr>
				<c:if test="${apprInfo.isattach == '0'}" >
				<tr>
					<th>증빙자료</th>
					<td><c:choose><c:when test="${apprInfo.filename == '' }"><input type="file" name='att_file' id='att_file' style="width:90px"><span class='flist'></span></c:when><c:otherwise><a href="/appr/filedownload.do?apprid=<c:out value="${apprid }" />" /><c:out value="${apprInfo.filename }" /></a> <c:if test="${apprInfo.apprstatcode == '01'}" ><a class='file_delete' style='cursor:pointer;'>삭제</a></c:if></c:otherwise></c:choose> </td>
				</tr>
				</c:if>
			</c:when><c:otherwise>
				<tr style='min-height:100px;'>
					<th>소명 내용</th>
					<td style='padding-top:5px;padding-bottom:5px;'>
						<c:out value="${appdesctext }" />
					</td>
				</tr>
				<c:if test="${apprInfo.isattach == '0'}" >
				<tr>
					<th>증적자료</th>
					<td><a href="/appr/filedownload.do?apprid=<c:out value="${apprid }" />" /><c:out value="${apprInfo.filename }" /></a> </td>
				</tr>
				</c:if>
			</c:otherwise></c:choose>
			<input type="hidden" name="fName" id="fName" value="<c:out value="${apprInfo.filename }" />" /><input type="hidden" name="lName" id="lName" value="<c:out value="${apprInfo.fileloc }"/>" />
			<tr>
				<th>주의사항</th>
				<td>
					${apprInfo.apprnote }
				</td>
			</tr>
        </c:otherwise>
        </c:choose>
		       
    </table> 
   	<c:if test="${apprInfo.apprstatcode == '01'}" >
	<div class="btn_black2" style='width:99%;padding-right:20px;'>
		<!-- <a class="btn_black btn_appr_save"><span>조치내역저장</span></a> -->
		<c:choose>
		<c:when test="${apprInfo.bpmkey == '' }">
			<a href="javascript:alert('BPM 연동정보가 없습니다.');" class='btn_black'><span>결재 요청</span></a>	 
		</c:when><c:otherwise>
			<a class='btn_black btn_bpm_send'><span>결재 요청</span></a>	
		</c:otherwise></c:choose>
		
	</div>
	</c:if>
	<input type="hidden" name="apprid" id="apprid" value="${apprid }" />
    <input type="hidden" name="procid" id="procid" value="${bpmidvlaue }" />
	</form>        
</div>
</body>

</html>