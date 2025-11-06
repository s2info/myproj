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
	        searchList(page);
	        //searchList(page, $('#searchTxt').val());
	    }
	});
	
	
	$(".btn_view").click(function(){
		$("#sec_pol_id").val($(this).attr('sec_pol_id'));
		$("#formMod").val("U");
		
		document.listForm.action = "<c:url value='/exception/exceptionIpForm.do'/>";
		document.listForm.submit();
	});
	
	$(".goForm").click(function(){
		$("#formMod").val("I");
		
		document.listForm.action = "<c:url value='/exception/exceptionIpForm.do'/>";
		document.listForm.submit();
	});
	
	$('#selAll_checkbox').click(function(){
		var ischeck = $(this).is(':checked');
		$('input:checkbox[name=cb_polId]').prop('checked',ischeck);

	});

	$('.btn_delete').click(function(){
		var checked_length =  $('input:checkbox[name=cb_polId]:checked').length;
		if(checked_length <= 0){
			alert('삭제할 정책을 선택해 주세요.');
			return false;
		}else{
			if(!confirm('삭제 하시겠습니까?')){
				return false;
			}
			
			var secPolIdList = [];
			$('input:checkbox[name=cb_polId]:checked').each(function () {
				//alert($(this).val());
				secPolIdList.push($(this).val());
			});
			var data = {
					secPolIdList:secPolIdList.toString()
			}
			
			if(secPolIdList.length > 0){
				$.ajax({
					url : '/exception/exceptionIpDelete.do',
					data : data,
					type : 'POST',
					dataType : 'json',
					error : function(jqXHR, textStatus, errorThrown) {
						if(jqXHR.status == 401){
					 		alert("인증정보가 만료되었습니다.");
							location.href="/";
						}else{
							alert(textStatus + "\r\n" + errorThrown);	
						}
					},
					success : function(data) {
						if (data.ISOK) {
							goPage("<c:url value='/exception/exceptionIpList.do'/>");
						} else {
							alert(data.MSG);
						}

					}
			});
			}
		}
	});
	
	function goPage(url) {

		document.listForm.pageIndex.value = 1;
	    document.listForm.action = url;
	    document.listForm.method = 'post';
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
    	<div class="subTT"><span>예외처리 IP</span></div>
    	<!-- S :search -->
        <%-- <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:80px">
                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>제목</option>
					<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>내용</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div> --%>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:10%">
					<col style="width:45">
					<col style="width:20">
					<col style="width:25%">
				</colgroup>
				<tr>
					<th class="ck_button"><label><input type="checkbox" name="selAll_checkbox" id="selAll_checkbox" /><span></span></label></th>
					<th>정책명</th>
					<th>IP 수</th>
					<th>작성일</th>
				</tr>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;" >
					<td class="ck_button"><label><input type="checkbox" name="cb_polId" value="<c:out value="${result.sec_pol_id}" />"><span></span></label></td>
					<td style="cursor:pointer;" class='btn_view' sec_pol_id="<c:out value="${result.sec_pol_id}" />">${result.sec_pol_desc }</td>
					<td>${result.cnt }</td>
					<td>${result.rgdt_date }</td>
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
       	<input type="hidden" name="pageIndex" id="pageIndex" value="" />
       	<input type="hidden" name="formMod" id="formMod" value="" />
       	<input type="hidden" name="sec_pol_id" id="sec_pol_id" value="" />
		<input type="hidden" name="sqno" id="sqno" value="0" />	
		<input type='text' style='display:none;' />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>