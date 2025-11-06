<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script type="text/javascript" language="javascript">
$(function () {		
	
	$('#paging').paging({
	    current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
	    onclick: function (e, page) {
	        goPage(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});
	
	$(".btn_view").click(function(){
		$("#sdCheckNo").val($(this).attr('sdCheckNo'));
		$("#formMod").val("U");
		
		document.listForm.action = "<c:url value='/securityDay/sdCheckListForm.do'/>";
		document.listForm.method="post";
		document.listForm.submit();
	});
	
	$(".goForm").click(function(){
		$("#formMod").val("I");
		
		document.listForm.action = "<c:url value='/securityDay/sdCheckListForm.do'/>";
		document.listForm.method="post";
		document.listForm.submit();
	});
	
	
	$('#selAll_checkbox').click(function(){
		var ischeck = $(this).is(':checked');
		$('input:checkbox[name=c_sdChekNo]').prop('checked',ischeck);

	});
	
	
$(".btn_delete").click(function(){
		
		//var formData = $("#saveForm").serialize();
		
		var checked_length =  $('input:checkbox[name=c_sdChekNo]:checked').length;
		if(checked_length <= 0){
			alert('삭제할 정책을 선택해 주세요.');
			return false;
		}else{
			if(!confirm('삭제 하시겠습니까?')){
				return false;
			}
			
			var sdCheckNoList = [];
			$('input:checkbox[name=c_sdChekNo]:checked').each(function () {
				//alert($(this).val());
				sdCheckNoList.push($(this).val());
			});
			var data = {
					sdCheckNoList:sdCheckNoList.toString()
			}
		
			 $.ajax({
				url: '/securityDay/sdCheckListInfoDelete.do',
				data: data,
				type: 'POST',
				dataType: 'json',
				error: function (jqXHR, textStatus, errorThrown) {					
					if(jqXHR.status == 401){						
						alert('인증정보가 만료되었습니다.');						
						location.href='/';					
					}else{						
						alert(textStatus + '\r\n' + errorThrown);					
					}				
				},				
				success: function (data) {					
					if (data.ISOK) {
						goPage(1);
					}else{
						alert(data.MSG); 
						goPage(1);
					}				
				}
			});
		}
    
	});
	
	
	$('.btn_search').click(function(){
		goPage(1);
	});
	
	function goPage(page) {

		document.listForm.pageIndex.value = page;
	    document.listForm.action = "<c:url value='/securityDay/sdCheckList.do'/>";
	    document.listForm.submit();
	}
});



</script>

 

</head>
<body>
<!-- S : header -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
<!-- E : header -->
<!-- S : contents -->
<div id="cnt">
	<!-- S : left contents -->
	<div id="cnt_L">
	<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" id="listForm" name="listForm" method="post">
    	<div class="subTT"><span>SecurityDay 점검표 관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
        		<p>사용 여부</p> 
            	<select  name="sUseYn" id="sUseYn" >
            		<option value="">사용 여부</option>
                	<option value="Y" <c:if test='${searchVO.sUseYn == "Y"}' >selected</c:if>>사용</option>
					<option value="N" <c:if test='${searchVO.sUseYn == "N"}' >selected</c:if>>미사용</option> 
				</select>
				<p>점검표 명</p> 
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:220px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:10%">
					<col style="width:30%">
					<col style="width:20%">
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:15%">
				</colgroup>
				<tr>
					<th class="ck_button"><label><input type="checkbox" name="selAll_checkbox" id="selAll_checkbox" /><span></span></label></th>
					<th>점검표 명</th>
					<th>등록자</th>
					<th>등록일</th>
					<th>사용여부</th>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold; cursor: pointer; " class='btn_view' sdCheckNo="<c:out value="${result.sdCheckNo}" />">
					<td class="ck_button"><label><input type="checkbox" name="c_sdChekNo" value="<c:out value="${result.sdCheckNo}" />"><span></span></label></td>
					<td style="text-align: left;">${result.checklistNm }</td>
					<td>${result.rgdtEmpNm }</td>
					<td>${result.rgdtDate }</td>
					<td>${result.useYn }</td>
				</tr>
				</c:forEach>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite">
            	<a class="btn_black btn_delete"><span>삭제</span></a>
            	<a class="btn_black goForm" ><span>등록</span></a>
            </div>
        </div>
       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
       	<input type="hidden" name="formMod" id="formMod" value="" />
       	<input type="hidden" name="sdCheckNo" id="sdCheckNo" value="0" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>