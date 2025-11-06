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
<script type="text/javascript" src="/js/js/jquery.isotope.min.js"></script>
<script type="text/javascript" src="/js/js/justgage.1.0.1.js"></script>
<script type="text/javascript" src="/js/js/raphael.2.1.0.min.js"></script>
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />

<script type="text/javaScript" language="javascript">
$(function () {		

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
        maxDate:"<c:out value="${searchVO.now_date }" />",
        buttonImage: "/img/icon_date.png",
        buttonImageOnly: true,
        beforeShow:function(){
        	setTimeout(function(){
        		$(".ui-datepicker").css("z-index","99999");
        	}, 0);
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 2px 5px");
   
    
    function searchList(page)
    {
    	
    	var begin_date=$('#begin_date').val();
    	var end_date=$('#end_date').val();
    	var org_upper=$('#org_upper').val();
    	var auth=$('#auth').val();
    	var data = {begin_date:begin_date
    			,end_date:end_date
    			,org_upper:org_upper
    			,auth:auth};
        $.ajax({
			url : '/report/getmonthreportcontents.do',
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
					$('.list_contents_body').empty().append(data.tableBody);
					
					
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
    
    
    $('#orgbrowser').on('click', '.sel_text', function(){
    	
    	var orgcode = $(this).attr('orgcode');
    	var stype = $(this).attr('stype');
    	$('#org_upper').val(orgcode);
	    $('#sel_org_name').val($(this).text());
    	$('#orgbrowser').find('a').each(function(){
    		if($(this).hasClass('ON')){
    			$(this).removeClass('ON');
    		}
    	});
    	$(this).find('a').addClass('ON');
    	
    	searchList(1);
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
    				alert(textStatus + "\r\n" + errorThrown);
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
    
    function getTableRowContents(tbl){
    	var contents1_item = [];
    	$(tbl).find('tr').each(function(){
		 	var row_item = ""; 
			$(this).find('td, th').each(function(){
				row_item += $(this).text().replace(/,/g,"/") + "|";
			});
			contents1_item.push(row_item);
	    });
		return contents1_item.toString();
    }
    
	$('.btn_export_excel').click(function(){
		var begin_date=$('#begin_date').val();
    	var end_date=$('#end_date').val();
    	var org_upper=$('#org_upper').val();
    	var auth=$('#auth').val();
    	
		var params = [];
		params[0] = JSON.stringify({"inputname": 'begin_date', "inputvalue": begin_date});
	    params[1] = JSON.stringify({"inputname": 'end_date', "inputvalue": end_date});
	    params[2] = JSON.stringify({"inputname": 'org_upper', "inputvalue": org_upper});
	    params[3] = JSON.stringify({"inputname": 'auth', "inputvalue": auth});
	    params[4] = JSON.stringify({"inputname": 'content_body1',"inputvalue": getTableRowContents($('.contents1'))});
        gopage('/report/reportMonthexportExcel.do', params, 'searchVO');
        
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
	<%@ include file="/WEB-INF/jsp/cmm/reportLeftMenu.jsp" %>
	</div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>기간별 진단 보고서</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>선택조직</p> 
            	<input type="text" name="sel_org_name" readonly id="sel_org_name" style="width:150px" value="${searchVO.orgName }" class="srh"/><input type="hidden" name="org_upper" id="org_upper" style="width:90px" value="${searchVO.orgCode }" />
            </li>
	        <br><br>            
        	<li><p>날 짜</p> <input type="text" name="begin_date" id="begin_date" readonly value="${searchVO.begin_date }" style="width:150px;z-indx:100000" class="srh">
        		~ <input type="text" name="end_date" id="end_date" readonly value="${searchVO.end_date }" style="width:150px;z-indx:100000" class="srh">
        		<a class="btn_black btn_search"><span>검색</span></a> 
        		&nbsp;&nbsp;<a class='btn_black btn_export_excel' ><span>Excel</span></a>
        	</li>
        </div>
        
       	<div class="sch_view nomal_list" style='display:'>
            <!-- S :list -->
            <div class='list_contents_body' style='margin-bottom:20px;'></div>
        </div>  
       	<input type="hidden" name="auth" id="auth" value="${searchVO.auth }" />
       	<input type="hidden" name="orgCode" id="orgCode" value="${searchVO.orgCode }" />
       
	</form:form>
	</div>
	
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
	<div id="mask" style="display:none;background:#eee;filter:alpha(opacity=5);" ></div>
	<div class='DialogBox'></div>
</div>
</body>
</html>