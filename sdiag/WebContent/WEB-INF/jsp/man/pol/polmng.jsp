<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script src="/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<style>
/*팝업창배경투명*/
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
</style>
<script type="text/javaScript" language="javascript">
$(function () {		
	
	$('.selected_sol, .selected_pol').change(function(){
		var id = $(this).attr('id');
		var majCode=$('#majCode').val();
		var minCode=$('#minCode').val();
    	
    	var params = {selid:id,majCode:majCode,minCode:minCode};
    	$.ajax({
            data: params,
            url: "/man/getSelectedMaunPollist.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + "\r\n" + errorThrown);
            },
            success: function (data) {
            	if(data.ISOK)
                {
            		$('#polCode > option').each(function(){
                		$(this).remove();
                	});
                	$('#polCode').append("<option value=''>지수화정책 전체</option>");
            		if(id=='majCode'){
            			$('#minCode > option').each(function(){
                			$(this).remove();
                		});
                		$('#minCode').append("<option value=''>중진단 전체</option>");
                		
                		for(var i=0;i<data.list.length;i++)
                    	{
                    		$('#minCode').append("<option value='"+ data.list[i]["code"] +"'>"+ data.list[i]["desc"] +"</option>");
                    	}
            		}else{
            			for(var i=0;i<data.list.length;i++)
                    	{
                    		$('#polCode').append("<option value='"+ data.list[i]["code"] +"'>"+ data.list[i]["desc"] +"</option>");
                    	}
            		}
            		
                }
                else
                {
                	alert(data.MSG);	
                	//history.back(-1);
                } 	

            }

        });
    	
    
    });	
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});
	
	 $('.btn_search').click(function(){
	    	searchList(1);
	 });
	
    $('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
    
    $('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
            var $ddata = $(".DialogBox");
            if($ddata.data('is_save') == 'Y'){
            	searchList($('#pageIndex').val());
            }
        },
        open: function () {
            var $ddata = $(".DialogBox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
    $('.popup_dialogbox').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
        },
        open: function () {
            var $ddata = $(".popup_dialogbox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
	$('.popup_dialogbox').on('click', '.btn_subdialogbox_close', function () {
		$('.popup_dialogbox').dialog('close');
	});
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	});
	$('.list_contents_body').on('click', '.btn_modify', function(){
		
		var polid=$(this).attr('polid');
		
		$('#polId').val(polid);
		$('#selectedRowId').val(polid);
		document.listForm.action = "<c:url value='/man/polmodify.do'/>";
		document.listForm.submit();
		/*
		
		var selrow = $(this);
		$('#selectedRowId').val($(this).attr('polid'));
		SelectedRowChange(selrow);
		var polid=$(this).attr('polid');
		$.ajax({
            data: {polid:polid}, 
            url: "/man/getPolManagerPopup.do",
            type: "POST",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
            	if(jqXHR.status == 401){
					alert("인증정보가 만료되었습니다.");
					location.href="/";
				}else{
					alert(textStatus + "\r\n" + errorThrown);	
				}
            },
            success: function (data) {
                if (data.ISOK) {
                	 $('.DialogBox').html(data.popup_body);
                     $('.DialogBox').dialog({ width: 940, height: 800 });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
                     
                     
                }
                else {
                    alert(data.MSG);
                }

            }

        });		
		*/
	});
	
	$('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'searchType', "inputvalue": '<c:out value="${searchVO.searchType}" />'});
	    params[1] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": '<c:out value="${searchVO.searchKeyword}" />'});
	    params[2] = JSON.stringify({"inputname": 'majCode', "inputvalue": '<c:out value="${searchVO.majCode}" />'});
	    params[3] = JSON.stringify({"inputname": 'minCode', "inputvalue": '<c:out value="${searchVO.minCode}" />'});
	    params[4] = JSON.stringify({"inputname": 'polCode', "inputvalue": '<c:out value="${searchVO.polCode}" />'});
        gopage('/man/pollistexportexcel.do', params, 'searchVO');
	});
    
});


	
function searchList(page)
{
	document.listForm.pageIndex.value = page;
	document.listForm.action = "<c:url value='/man/polmanger.do'/>";
	document.listForm.submit();
}
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
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>지수화 정책 관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>사용여부</p> 
            	<select  name="searchType" id="searchType" style="width:150px">
					<option value="" <c:if test="${searchVO.searchType == ''}" >selected</c:if>>전체</option> 
					<option value="Y" <c:if test="${searchVO.searchType == 'Y'}" >selected</c:if>>사용</option>
					<option value="N" <c:if test="${searchVO.searchType == 'N'}" >selected</c:if>>사용중지</option>
				</select>
                
            </li><br /><br />
            <li>
           		<p>정책선택</p>
           		${pol_selectbox }
			</li>
			<li>
				<p>정책명</p>
				<input type="text" name="searchKeyword" id="searchKeyword" style="width:150px" value="${searchVO.searchKeyword }" class="srh" /> 
           		<a class="btn_black btn_search"><span>검색</span></a>
            </li>
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
                	<col style="width:8%">
					<col style="width:11%">
					<col style="width:11%">
					<col style="width:*">
					<col style="width:8%">
					<col style="width:8%">
					
					<col style="width:8%">
					<col style="width:27%">
					<%-- <col style="width:9%">
					<col style="width:8%">
					<col style="width:8%"> --%>
				</colgroup>
				<tr>
					<th>정렬 순서</th>
					<th>대진단</th>
					<th>중진단</th>
					<th>정책</th>
					<th>구분</th>
					<th>가중치</th>
					<th>사용여부</th>
					<th>비고</th>
					<!-- <th>결재라인</th>
					<th>제재조치</th>
					<th>소명신청</th> -->
				</tr>
				<tbody class='list_contents_body'>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr style="font-weight:bold;cursor:pointer;" polid="<c:out value="${result.sec_pol_id}" />" class='btn_modify<c:if test="${searchVO.selectedRowId == result.sec_pol_id}" > SelRow</c:if>'>
					<td><c:out value="${result.ordr}" /></td>
					<td><c:out value="${result.majr_desc}" /></td>
					<td><c:out value="${result.minr_desc}" /></td>
					<td><c:out value="${result.sec_pol_desc}" /></td>
					<td><c:out value="${result.sec_pol_cat_desc}" /></td>
					<td><c:out value="${result.pol_weight_value}" /></td>
					<td><c:out value="${result.use_indc}" /></td>
					<td>${fn:substring(result.bigo, 0, 25)}<c:if test="${fn:length(result.bigo) > 25}" >...</c:if></td>
					<%-- <td><c:out value="${result.appr_line_code}" /></td>
					<td><c:out value="${result.sanc_cate}" /></td>
					<td><c:out value="${result.appr_cate}" /></td> --%>
				</tr>
				</c:forEach>
				</tbody>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
             <div class="btn_borderWrite">
               	<c:if test="${totalCnt > 0 }"><a class='btn_black btn_export_excel' ><span>Excel</span></a></c:if>
            </div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="selectedRowId" id="selectedRowId" value="<c:out value="${searchVO.selectedRowId}" />" />
		<input tyee='text' style='display:none;'/>
		<input type="hidden" name="polId" id="polId" value="" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div class='DialogBox'></div>
	<div class='popup_dialogbox' style="background:#fff"></div>
</div>
</body>
</html>