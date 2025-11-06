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
   
    
    function searchList(page)
    {
    	$('#pageIndex').val(page);
    	var begin_date=$('#begin_date').val();
    	var org_upper=$('#org_upper').val();
    	var data = {begin_date:begin_date
    			,org_upper:org_upper};
        $.ajax({
			url : '/report/sheetAjax.do',
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
					$('.list_contents_body').empty().append(data.strList);
					$('.buseo_contents_body').empty().append(data.strBuseoList);
					$('#sel_org_name').val(data.currOrgName);
					$('#org_upper').val(data.orgCode);
					if(data.isviewtab == 'Y'){
						$('.tab_btn').show();	
					}else{
						$('.tab_btn').hide();
					}
					
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
		
		var params = [];
		params[0] = JSON.stringify({"inputname": 'begin_date', "inputvalue": $('#begin_date').val()});
	    params[1] = JSON.stringify({"inputname": 'tit1', "inputvalue": $('.title1').text()});
	    params[2] = JSON.stringify({"inputname": 'tit1_1', "inputvalue": $('.title1_1').text()});
	    params[3] = JSON.stringify({"inputname": 'tit2', "inputvalue": $('.title2').text()});
	    params[4] = JSON.stringify({"inputname": 'conts1', "inputvalue": getTableRowContents($('.contents1'))});
	    params[5] = JSON.stringify({"inputname": 'tit3', "inputvalue": $('.title3').text()});
	    params[6] = JSON.stringify({"inputname": 'conts2', "inputvalue":  getTableRowContents($('.contents2'))});
	    params[7] = JSON.stringify({"inputname": 'btit1', "inputvalue": $('.buseotitle1').text()});
	    params[8] = JSON.stringify({"inputname": 'btit1_1', "inputvalue": $('.buseotitle1_1').text()});
	    params[9] = JSON.stringify({"inputname": 'btit2', "inputvalue": $('.buseotitle2').text()});
	    params[10] = JSON.stringify({"inputname": 'bconts1', "inputvalue": getTableRowContents($('.buseocontents1'))});
	    params[11] = JSON.stringify({"inputname": 'btit3', "inputvalue": $('.buseotitle3').text()});
	    params[12] = JSON.stringify({"inputname": 'bconts2', "inputvalue":  getTableRowContents($('.buseocontents2'))});
        gopage('/report/polReportexportexcelex.do', params, 'searchVO');
        
	}); 
	
	$('.tab_bottom').click(function(){
		var btype = $(this).attr('t');
		if(btype == 'N'){
			$('.nomal_list').show();
			$('.buseo_list').hide();
		}else{
			$('.nomal_list').hide();
			$('.buseo_list').show();
		}
		$('.tab_btn').find('a').each(function(){
			if($(this).hasClass('btn_type1')){
				$(this).removeClass('btn_type1').addClass('btn_type2');
			}
		});
		$(this).removeClass('btn_type2').addClass('btn_type1');
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
    <form id="searchForm" name="listForm" method="post">
    	<div class="subTT"><span>진단 보고서</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li>
            	<p>선택조직</p> 
            	<input type="text" name="sel_org_name" readonly id="sel_org_name" style="width:150px" value="" class="srh"/><input type="hidden" name="org_upper" id="org_upper" style="width:90px" value="" />
            </li>
	        <br><br>            
        	<li><p>날 짜</p> <input type="text" name="begin_date" id="begin_date" readonly value="<c:out value="${getdate}" />" style="width:150px;z-indx:100000" class="srh">
        		<a class="btn_black btn_search"><span>검색</span></a> 
        		&nbsp;&nbsp;<a class='btn_black btn_export_excel' ><span>Excel</span></a>
        	</li>
        </div>
      <!--   <div class="pd10"></div>
        <div class="tab_btn" style="display:none;">
    		<a class="btn_type1 tab_bottom" t='N'><span>일반진단</span></a> 
    		<a class="btn_type2 tab_bottom" t='B'><span>부서진단</span></a> 
    	</div>
    	   -->
        <!-- E :search -->
    	<div class="sch_view nomal_list" style='display:'>
           
            <!-- S :list -->
            <div class='list_contents_body' style='margin-bottom:20px;'></div>
        </div>   
        <div class="sch_view buseo_list" style='display:none;'>
            <!-- S :list -->
            <div class='buseo_contents_body' style='margin-bottom:20px;'></div>
        </div>    
       <input type="hidden" name="emp_no" value="" />
		<input type="hidden" name="dtType" id="dtType" value="" />
		<input type="hidden" name="dtSeq" id="dtSeq" value="" />
		<input type="hidden" name="stval" id="stval" value="" />
		<input type="hidden" name="seqval" id="seqval" value="" />
		<input type="hidden" name="sorgcode" id="sorgcode" value=""/>
		<input type="hidden" name="sorgtype" id="sorgtype" value="1"/>
		<input type="hidden" name="pageIndex" id="pageIndex" value="1" />
		<input type="hidden" name="mCode" id="mCode" value="A01" />
		<input type="hidden" name="nCode" id="nCode" value="" />
		<input type="hidden" name="pCode" id="pCode" value="" />
		<input type="hidden" name="lType" id="lType" value="personal" />
		</form>		
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