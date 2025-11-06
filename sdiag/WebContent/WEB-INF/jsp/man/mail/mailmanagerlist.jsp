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
 <style>
/*팝업창배경투명*/
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
}
</style>
<script type="text/javascript" language="javascript">

$(function () {		
	
	/* $('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
            var $ddata = $(".DialogBox");
            if($ddata.data('is_save') == 'Y'){
            	searchList(1);
            }
        },
        open: function () {
            var $ddata = $(".DialogBox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	}); */
	
	/* $('.btn_user_add, .btn_user_modify').click(function(){
		var uid = $(this).attr('uid');
		var widthVal = uid == '' ? 880 : 490;
		var mode = uid=='' ? 'A' : 'M';
		if(mode == 'M'){
			$('#selectedRowId').val($(this).attr('uid'));
			var selrow = $(this).closest('tr');
			SelectedRowChange(selrow);
		}
		$.ajax({
            data: {mode:mode,uid:uid}, 
            url: "/user/getManUseraddPopup.do",
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
                     $('.DialogBox').dialog({ width: widthVal, height: 500 });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
                     
                }
                else {
                    alert(data.MSG);
                }

            }

        });		
		
	}); */
	
	$('.btn_mail_add, .btn_mail_modify').click(function(){
		var mail_seq=$(this).closest('tr').attr('mail_seq');
		
		//$('#selectedRowId').val(polid);
		$('#mail_seq').val($.type(mail_seq) === "undefined" ? "0" : mail_seq);
		document.listForm.action = "/man/mailmanagermodify.do";
		document.listForm.submit();
		
	});
	
	$('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            //$('#pageIndex').val(page);
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
	
	$('.btn_search').click(function(){
		searchList(1);
	});
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});
	
	$('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": '<c:out value="${searchVO.searchCondition}" />'});
	    params[1] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": '<c:out value="${searchVO.searchKeyword}" />'});
        gopage('/man/maillistexportexcel.do', params, 'searchVO');
	});
	
	function searchList(page)
	{
		
		document.listForm.pageIndex.value = page;
		document.listForm.action = "/man/mailmanagerlist.do";
		document.listForm.submit();
	}
	
	$('.btn_agent_reset').click(function(){
		
		$.ajax({				
			data: {},				
			url: '/man/setmailsendagentreset.do',				
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
					alert('메일 발송 스케줄 Agent가 재실행  되었습니다.');	
					searchList($('#pageIndex').val());
				}else{alert(data.MSG); }				
			},
			beforeSend: function () {
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
                $('#loading').css('position', 'absolute');
                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
                $('#loading').css('top', $('.sch_view').offset().top);
              
                $('#loading').css('padding-top', 50);
                
                $('#loading').show().fadeIn('fast');
           },
           complete: function () {
               $('#loading').fadeOut();
           }		
		});	
	})
	
	$('.mail_menual_send').click(function(){
		var mail_seq = $(this).attr('mail_seq');
		var is_used = $(this).attr('is_used');
		if(is_used != 'Y'){
			alert("사용중지 메일 입니다.");
			return false;
		}
		var data = {mail_seq : mail_seq};
		if(!confirm('메일을 수동 발송 하시겠습니까?')){		
			 	return false;
			}
		///
		$.ajax({
            data: data, 
            url: "/man/getmailtargetuserlistcount.do",
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
                	//alert(data.TargetTotalCount); 
                	var alert_msg = "발송 예상 메일수가 [" + data.TargetTotalCount +"]건 입니다. \r\n\r\n계속 진행 하시겠습니까?";
                	if(data.TargetTotalCount == 0){
                		alert_msg = "발송 예상 메일수가 [" + data.TargetTotalCount +"]건 입니다.\r\n\r\n메일전송 대상이 없을 경우 전사 발송 됩니다.\r\n\r\n계속 진행 하시겠습니까?";
                	}
                	if(!confirm(alert_msg)){		
					 	return false;
					}
                	var data1 = {mail_seq : mail_seq};
					$.ajax({				
						data: data1,				
						url: '/man/setmailsend.do',				
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
								alert('메일 수동 발송이 처리 되었습니다.');	
								searchList($('#pageIndex').val());
							}else{alert(data.MSG); }				
						},
						beforeSend: function () {
							$('#loading').find('img').attr('width', '100px').attr('height', '100px');
			           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
			                $('#loading').css('position', 'absolute');
			                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
			                $('#loading').css('top', $('.sch_view').offset().top);
			              
			                $('#loading').css('padding-top', 50);
			                
			                $('#loading').show().fadeIn('fast');
			           },
			           complete: function () {
			               $('#loading').fadeOut();
			           }			
					});	
                }
                else {
                    alert(data.MSG);
                }
            }
        });			
			
		
	})
    
   
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
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>메일 발송 관리</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>검색어</p> 
            	<select  name="search_gubun" id="search_gubun" style="width:100px">
            		<option value="" >발송 구분</option>
                	<option value="N" <c:if test="${searchVO.search_gubun == 'N'}" >selected</c:if>>공지사항</option>
					<option value="P" <c:if test="${searchVO.search_gubun == 'P'}" >selected</c:if>>정책진단</option> 
				</select>
				<select  name="search_is_used" id="search_is_used" style="width:100px">
            		<option value="" >사용여부</option>
                	<option value="Y" <c:if test="${searchVO.search_is_used == 'Y'}" >selected</c:if>>사용</option>
					<option value="N" <c:if test="${searchVO.search_is_used == 'N'}" >selected</c:if>>사용중지</option> 
				</select>
                   	메일 제목 <input type="text" name="search_subject" id="search_subject" value="${searchVO.search_subject }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <caption>TBL</caption>
				<colgroup>
					<col style="width:5%">
					<col style="width:10%">
					<col style="width:15%">
					<col style="width:*">
					<col style="width:8%">
					<col style="width:12%">
					<col style="width:10%">
					<col style="width:8%">
					<col style="width:8%">
				</colgroup>
				<tr>
					<th>번호</th>
					<th>구분</th>
					<th>발송 일정</th>
					<th>제목</th>
					<th>사용여부</th>
					<th>등록일</th>
					<th>발송횟수</th>
					<th>배치적용여부</th>
					<th>수동발송</th>
				</tr>
				<tbody class="list_contents_body">
					<c:if test="${empty resultList}">
                		<tr style="height:250px;">
                			<th colspan="9">
                			검색결과가 없습니다.
                			</th>
                		</tr>
                	</c:if>
					<c:forEach var="result" items="${resultList}" varStatus="status">
					<tr style="font-weight:bold;cursor:pointer" mail_seq="<c:out value="${result.mail_seq}" />" <c:if test="${searchVO.selectedRowId == result.mail_seq}" >class='SelRow'</c:if>>
						<td class='btn_mail_modify'><c:out value="${result.mail_seq}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.gubun_name}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.send_schedule_name}" /><br />(<c:out value="${result.schedule_expression}" />)</td>
						<td class='btn_mail_modify' style="text-align: left;"><c:out value="${result.subject}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.is_used_name}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.rgdt_date}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.sendsuccesscount}" /> / <c:out value="${result.sendcount}" /></td>
						<td class='btn_mail_modify'><c:out value="${result.is_apply_name}" /></td>
						<td><span mail_seq="${result.mail_seq}" is_used="${result.is_used}" class="scondition2 mail_menual_send">발송</span></td>
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
            	<%-- <c:if test="${totalCnt > 0 }"><a class='btn_black btn_export_excel' ><span>Excel</span></a></c:if> --%>
            	<a class="btn_black btn_mail_add" mail_seq='0'><span>메일 발송정보  추가</span></a>
            	<a class="btn_black btn_agent_reset" mail_seq='0'><span>메일 발송 Agent 재실행</span></a>
            </div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" name="selectedRowId" id="selectedRowId" value="<c:out value="${searchVO.selectedRowId}" />" />
		<input type="hidden" name="mail_seq" id="mail_seq" value="0" />
		<input tyee='text' style='display:none;'/>
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div class='DialogBox'></div>
</div>
</body>
</html>