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
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />
<style>
/*
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;}
*/
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
            url: "/man/getSelectedPollist.do",
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
	
	$('.DialogBox').dialog({
        autoOpen: false,
        modal: true,
        resizable: true,
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
	$('.DialogBox').on('click', '.btn_dialogbox_close', function () {
		$('.DialogBox').dialog('close');
	});
	
	$('.list_contents_body').on('click', '.btn_pcgact_info', function(){
		var mode=$(this).attr('mode');
		var pcgactinfo='';
		
		var empno=$(this).attr('empno');
		var pcgactid = $(this).attr('pcgactid');
		var pcgactparam = $(this).attr('pcgparam');
		var macno=$(this).attr('macno');
		var uip=$(this).attr('uip');
		var polid=$(this).attr('pol_id');
		var regdate=$(this).attr('dt_date');
		
		pcgactinfo = empno + "/" + pcgactid + "/" + macno + "/" + uip + "/" + polid + "/" + regdate + "/" + pcgactparam ;
		
		var data = {mode:mode,
				sanctlist:'',
				apprlist:'',
				pcgactinfo:pcgactinfo};
		sanctRegAllProsess(data);
	});
	
	$('.btn_appl_order, .btn_sanct_order').click(function(){
		var mode=$(this).attr('mode');
		
		var apprlist=[];
		var sanctlist=[];
		var pcgactinfo='';
		if($('.list_contents_body').find('input:checkbox[name="sel_box"]:checked').length <= 0){
	       	alert('선택된 진단내역이 없습니다.');
	       	return false;
	    }
			
		$('.list_contents_body').find('tr').each(function(){
		   	$(this).find('input:checkbox[name="sel_box"]:checked').each(function () {
		    	var empno=$(this).attr('empno');
		    	var macno=$(this).attr('macno');
	        	var uip=$(this).attr('uip');
	        	var polid=$(this).attr('pol_id');
	        	var regdate=$(this).attr('dt_date');
	        	var uuid=$(this).val();
	        	var sancid=$(this).attr('scancid');
	        	
	        	if(mode=='S'){
	        		//if(macno.length > 0){
	        			var sanctcode = $(this).closest('tr').find('td').eq(10).find('select');
			       		if(sanctcode.length > 0){
			       			sanctlist.push(empno + "/" + sancid + "/" + macno + "/" + uip + "/" + polid + "/" + regdate + "/" + sanctcode.val());
			       		}	
	        		//}
	        		
	        	}
	       		//소명
				var apprcode = $(this).closest('tr').find('td').eq(12).find('select');
				if(apprcode.length > 0){
					apprlist.push(empno + "/" + uuid + "/" + macno + "/" + uip + "/" + polid + "/" + regdate + "/" + apprcode.val());
				}
				
	       	});
		});
		
		var data = {mode:mode,
				sanctlist:sanctlist.toString(),
				apprlist:apprlist.toString(),
				pcgactinfo:pcgactinfo};
		sanctRegAllProsess(data);
	});
	
	function sanctRegAllProsess(data){
		if(data.mode != 'P'){
			if(data.sanctlist.length <= 0 && data.apprlist.length <= 0){
				alert('선택된 내역중 수행 항목이 없습니다.');
				return false;
			}
		}
		var pheight = data.mode == 'P' ? 380 : 470;
        $.ajax({
			url : '/man/setSanctAllPrecessPopup.do',
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
					 $('.DialogBox').html(data.popup_body);
                     $('.DialogBox').dialog({ width: 700, height: pheight });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
				} else {
					alert(data.MSG);
				}

			}

		});
		
	}
	
	$('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			searchList(1);
		}
	});

    $('.btn_search').click(function(){
    	searchList(1);
    });
    
    $('#begin_date, #end_date').datepicker({
        dateFormat: 'yy-mm-dd',
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        changeMonth: true,
        changeYear: true,
        showMonthAfterYear: true,
        showButtonPanel: false,
        showOn: "both",
        maxDate:"<c:out value="${nowDate}" />",
        buttonImage: "/img/icon_date.png",
        buttonImageOnly: true,
        beforeShow:function(){
        	setTimeout(function(){
        		$(".ui-datepicker").css("z-index","99999");
        	}, 0);
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 2px 5px");
    
    $('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
    
    function searchList(page)
    {
    	$('#pageIndex').val(page);
    	var begin_date=$('#begin_date').val();
    	var end_date=$('#end_date').val();
    	var org_upper=$('#org_upper').val();
    	var majCode=$('#majCode').val();
    	var minCode=$('#minCode').val();
    	var polCode=$('#polCode').val();
    	var searchCondition=$('#searchCondition').val();
    	var searchKeyword=$('#searchKeyword').val();
    	var pageSize=$('#pageSize').val();
    	var pageIndex=$('#pageIndex').val();
    	var sanctStatus=$('#sanctStatus').val();
    	var apprStatus=$('#apprStatus').val();
    	var data = {begin_date:begin_date
    			,end_date:end_date
    			,org_upper:org_upper
    			,majCode:majCode
    			,minCode:minCode
    			,polCode:polCode
    			,searchCondition:searchCondition
    			,searchKeyword:searchKeyword
    			,pageSize:pageSize
    			,pageIndex:pageIndex
    			,sanctStatus:sanctStatus
    			,apprStatus:apprStatus};
        $.ajax({
			url : '/man/getSanctionList.do',
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
					if(data.totalCnt == '0'){
						$('.btn_export_excel').hide();
					}else{
						$('.btn_export_excel').show();
					}
					$('.list_contents_body').empty().append(data.list_body);
					$('#paging').paging({
				        current: data.currentpage,
				        max: data.totalPage,
				        onclick: function (e, page) {
				            searchList(page);
				        }
				    });
				} else {
					alert(data.MSG);
				}

			},
			beforeSend: function () {
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
           	 	var padingTop = (Number(($('.list_contents_body').css('height')).replace("px","")) / 2) + 50;
                $('#loading').css('position', 'absolute');
                $('#loading').css('left', $('.list_contents_body').offset().left + ($('.list_contents_body').css('width').replace("px","") / 2) - 130);
                $('#loading').css('top', $('.list_contents_body').offset().top);
              
                $('#loading').css('padding-top', 100);
                
                $('#loading').show().fadeIn('fast');
           },
           complete: function () {
               $('#loading').fadeOut();
           }

		});
    }
    
    $('.list_contents_body').on('click', '.btn_log_view', function(){
    	//alert($(this).attr('polcd'));
    	var pcd = $(this).attr('polcd');
    	var emp = $(this).attr('empno');
    	var bdt = $(this).attr('bdt');
    	var pw = window.open('/pol/polLogdetailview.do?polcd='+ $(this).attr('polcd') +'&empno='+ emp + '&begindate='+bdt , 'logview', 'width=1100, height=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no');
        if (pw != null)
            pw.focus();
    });
    
    $('.list_contents_body').on('click','.btn_appr_info', function(){
    	var apprid=$(this).attr('apprid');
    	var pw = window.open('/pol/polapprdetailview.do?ispgm=1&apprid='+apprid, 'apprinfo', 'width=720, height=630, toolbar=no, menubar=no, scrollbars=no, resizable=yes, copyhistory=no');
        if (pw != null)
            pw.focus();
    });
    
    $('input:checkbox[name=sel_all]').click(function(){
    	var ischecked = $(this).is(':checked');
    	$('.list_contents_body').find('tr').each(function(){
    		$(this).find('input:checkbox[name=sel_box]').prop('checked', ischecked);
    	});
    //	
    });
    
    $('#orgbrowser').on('click', '.sel_folder', function(){
    	if(!$(this).hasClass('add_ON')){
    		var _this = $(this);
    		var pNode = $(this).find('ul');
        	var uCode = $(this).attr('uorg_code');
        	$.ajax({
    			url : '/pol/setAddOrgSubfolder.do',
    			data : {uorg_code:uCode},
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
    					var node_html = data.node_body;
    					var child_node = $(node_html).appendTo(pNode);
    					$(pNode).treeview({add:child_node, unique: true, animated: "fast"});
    					_this.addClass('add_ON');
    					
    				} else {
    					alert(data.MSG);
    					//history.back(-1);
    				}

    			},
				beforeSend: function () {
					$('#loading').find('img').attr('width', '100px').attr('height', '100px');
					
					var padingTop = (Number(($('#orgbrowser').css('height')).replace("px","")) / 2) + 25;
	                $('#loading').css('position', 'absolute');
	                $('#loading').css('left', $('#orgbrowser').offset().left + ($('#orgbrowser').css('width').replace("px","") / 2) - 50);
	                $('#loading').css('top', $('#orgbrowser').offset().top);
	                $('#loading').css('padding-top', 80);
	                
	                $('#loading').show().fadeIn('fast');
	            },
	            complete: function () {
	            	$('#loading').fadeOut();
	            }

    		});
        	
    	}
    	
    });
    
    $(window).bind("load", function() {
    	
    	searchList(1);
		
	});
    
    $('.btn_export_excel').click(function(){
		var params = [];
		params[0] = JSON.stringify({"inputname": 'begin_date', "inputvalue": $('#begin_date').val()});
	    params[1] = JSON.stringify({"inputname": 'end_date', "inputvalue": $('#end_date').val()});
	    params[2] = JSON.stringify({"inputname": 'org_upper', "inputvalue": $('#org_upper').val()});
	    params[3] = JSON.stringify({"inputname": 'majCode', "inputvalue": $('#majCode').val()});
	    params[4] = JSON.stringify({"inputname": 'minCode', "inputvalue": $('#minCode').val()});
	    params[5] = JSON.stringify({"inputname": 'polCode', "inputvalue": $('#polCode').val()});
	    params[6] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": $('#searchCondition').val()});
	    params[7] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": $('#searchKeyword').val()});
	    params[8] = JSON.stringify({"inputname": 'sanctStatus', "inputvalue": $('#sanctStatus').val()});
	    params[9] = JSON.stringify({"inputname": 'apprStatus', "inputvalue": $('#apprStatus').val()});
	    gopage('/man/sanctionListexportexcel.do', params, 'searchVO');
	});
    
   // $('.list_contents_body').tooltip();
    
    $('.list_contents_body').on('mouseenter', '.show_upperorgnames', function(){
    	var thisele = $(this);
    	var titleval = $.type(thisele.attr('title')) === 'undefined' ? "" : thisele.attr('title');
    	var orgcode=thisele.attr('orgcode');
    	if(titleval == ''){
    		$.ajax({
    			url : '/common/getupperorgnames.do',
    			data : {orgcode:orgcode},
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
    					thisele.attr('title', data.uppernames);	
    				} else {
    					alert(data.MSG);
    				}

    			}
    		});
    		
    		
    	}
    	
    });
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
    	<div class="subTT"><span>제재/소명 조치(수동)</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li><p>날 짜</p> <input type="text" name="begin_date" id="begin_date" readonly value="<c:out value="${getdate}" />" style="width:150px;z-indx:100000" class="srh"> 
        	~ <input type="text" name="end_date" id="end_date" readonly value="<c:out value="${getdate}" />" style="width:150px;z-indx:100001" class="srh"></li>
        	<li><p>제재상태</p>
        		<select name='sanctStatus' id='sanctStatus'>
        			<option value=''>전체</option>
        			<option value='NA'>미제재</option>
        			<option value='Y'>제재중</option>
        			<option value='N'>해제</option>
        		</select>
        	</li>
        	<li><p>소명상태</p>
        		<select name='apprStatus' id='apprStatus'>
        			<option value=''>전체</option>
        			<option value='NA'>미소명</option>
        			<c:forEach var="result" items="${apprStatusList}" varStatus="status">
						<option value='<c:out value="${result.minr_code}" />'><c:out value="${result.code_desc}" /></option>
					</c:forEach>
				</select>
			</li>
			<li><p>조회수</p>
        		<select name='pageSize' id='pageSize'>
        			<option value='10'>10</option>
        			<option value='20'>20</option>
        			<option value='50'>50</option>
        		</select>
        	</li>
	        <br/><br>
        	<li>
            	<p>선택조직</p> 
            	<input type="text" name="sel_org_name" readonly id="sel_org_name" style="width:150px" value="<c:out value="${orgRootInfo.org_nm}" />" class="srh"/><input type="hidden" name="org_upper" id="org_upper" style="width:90px" value="<c:out value="${orgRootInfo.org_code}" />" />
            </li> 
            <li>
            	<p>정책선택</p> 
            	<select class='selected_sol' name='majCode' id='majCode' style='width:150px;'>
					<option value=''>대진단 전체</option>
					<c:forEach var="result" items="${diag_majr_select}" varStatus="status">
						<option value='<c:out value="${result.diag_minr_code}" />'><c:out value="${result.diag_desc}" /></option>
					</c:forEach>
				</select>
				<select class='selected_pol' name='minCode' id='minCode' style='width:150px;'><option value=''>중진단 전체</option></select>
				<select name='polCode' id='polCode' style='width:150px;'><option value=''>지수화정책 전체</option></select>
			</li> 
        	<br /><br />
        	<li>
            	<p>검색어</p> 
            	<select  name="searchCondition" id="searchCondition" style="width:100px">
                	<option value="1" <c:if test="${searchVO.searchCondition == '1'}" >selected</c:if>>ID(사번)</option>
					<option value="2" <c:if test="${searchVO.searchCondition == '2'}" >selected</c:if>>성명</option> 
				</select>
                <input type="text" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" style="width:200px" class="srh">
                <a class="btn_black btn_search"><span>검색</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
            <table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
					<col style="width:5%">
					<col style="width:7%">
					<col style="width:7%">
					<col style="width:7%">
					<col style="width:8%">
					<col style="width:*">
					<col style="width:7%">
					<col style="width:6%">
					<col style="width:6%">
					<col style="width:6%">
					<col style="width:8%">
					<col style="width:6%">
					<col style="width:8%">
					<col style="width:6%">
					
				</colgroup>
				<tr>
					<th><input type='checkbox' name='sel_all' /></th>
					<th>날짜</th>
					<th>조직</th>
					<th>사번/이름</th>
					<th>이벤트날짜</th>
					<th>지수화정책</th>
					<th>건수</th>
					<th>점수</th>
					<th>진단내역</th>
					<th>제재상태</th>
					<th>제재유형</th>
					<th>소명상태</th>
					<th>소명신청<br>승인대상</th>
					<th>PC지키미<br>조치</th>
				</tr>
				<tbody class='list_contents_body'>
				<tr><td style='height:400px;' colspan='14'>&nbsp;</td></tr>
				</tbody>
            </table>
            <!-- E :list -->
            <!-- S : page NUM -->
            <div class="pagingArea1 pagingPadd1">
				<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
			</div>
            <!-- E : page NUM -->
            <div class="btn_borderWrite">
            	<a class='btn_black btn_export_excel' ><span>Excel</span></a>
            	<a class="btn_black btn_appl_order"  mode='A'><span>소명요청 수행</span></a>
            	<a class="btn_black btn_sanct_order" mode='S'><span>제재조치 수행</span></a>
            	<!-- <a class="btn_black btn_excel_down"><span>Excel</span></a> -->
            </div>
        </div>
        <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div class='DialogBox'></div>
    <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
</div>
<script type="text/javascript">
	$(function() {
		$("#orgbrowser").treeview({
			collapsed: false,
			animated: "fast",
			unique: true,
			persist: "location"
		});
				
		$('#orgbrowser').on('click', '.sel_text', function(){
			var orgcode = $(this).attr('orgcode');
		    var stype = $(this).attr('stype');
		    $('#sel_org_name').val($(this).text());
		    //$('#sorgcode').val(orgcode);
		    //$('#sorgtype').val(stype);
		    $('#org_upper').val(orgcode)
		    $('#orgbrowser').find('a').each(function(){
		    	if($(this).hasClass('ON')){
		    		$(this).removeClass('ON');
		    	}
		    });
		    $(this).find('a').addClass('ON');
				    	
		});
	});	
</script>
</body>
</html>