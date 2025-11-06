<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<c:set var="newline" value="\n" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/jquery.treeview.js"></script>
<link rel="stylesheet" href="/css/jquery.treeview.css" />
<link rel="stylesheet" href="/css/styleEx.css" />
<script type="text/javaScript" language="javascript">
$(function () {		
	
	$('#begindate1, #enddate1').datepicker({
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
   
   function searchList()
   {
	   var upperOrgCode = $('#upperOrgCode').val();
	   var begindate = $('#begindate').val();
	   var enddate = $('#enddate').val();
	   var majrCode = $('#majrCode').val();
	   var buseoindc = $('#buseoindc').val();
	   var data = {begindate:begindate
	   			,enddate:enddate
	   			,upperOrgCode:upperOrgCode
	   			,majrCode:majrCode
	   			,buseoindc:buseoindc};
	   
	   $.ajax({
			url : '/stat/getstatistpoltotaldata.do',
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
					if(data.list_width < 1350){
						$('.tblInfoType4').css('width', '100%');
					}else{
						$('.tblInfoType4').css('width', data.list_width + 'px');
					}
					
					$('.contents_head').empty().append(data.list_head);
					$('.contents_body').empty().append(data.list_body);
					var tableheight = Number(($('.tblInfoType4').css('height')).replace("px","")) + 20;
				
					if(tableheight > 800){
						$('.sch_view').css('height', '800px');
					}else{
						$('.sch_view').css('height', tableheight + 'px');
					}
					//alert(data.list_rowcount);
					//alert();
				} else {
					alert(data.MSG);
				}

			},
			beforeSend: function () {
				$('.btn_export_excel').hide();
				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
                $('#loading').css('position', 'absolute');
                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
                $('#loading').css('top', $('.sch_view').offset().top);
              
                $('#loading').css('padding-top', 100);
                
                $('#loading').show().fadeIn('fast');
           },
           complete: function () {
               $('#loading').fadeOut();
               $('.btn_export_excel').show()
           }
		});
	   
   }
   
   $(window).bind("load", function() {
  		searchList();
	});
   
   $('.contents_body').on('click', '.btn_org', function(){
	   var is_open = $(this).attr('is_open');
	   var org_code = $(this).attr('org_code');
	   var isgetdata = $(this).closest('td').attr('isgetdata');
	   var $sel_tr = $(this).closest('tr');
	   var $thisimg = $(this);
	 
	   if(isgetdata == 'N'){
		    var begindate = $('#begindate').val();
			var enddate = $('#enddate').val();
			var majrCode = $('#majrCode').val();
			var buseoindc = $('#buseoindc').val();
			var data = {begindate:begindate
			  			,enddate:enddate
			   			,upperOrgCode:org_code
			   			,majrCode:majrCode
			   			,buseoindc:buseoindc};
			   
			$.ajax({
				url : '/stat/getstatistpoltotaldata.do',
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
						$sel_tr.after(data.list_body);
						$thisimg.attr('is_open', 'Y');
						$thisimg.attr('src', '/img/folder.gif');
						$thisimg.closest('td').attr('isgetdata', 'Y');
					} else {
						alert(data.MSG);
					}

				},
				beforeSend: function () {
					$('.btn_export_excel').hide();
					$('#loading').find('img').attr('width', '100px').attr('height', '100px');
		         	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
		            $('#loading').css('position', 'absolute');
		            $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
		            $('#loading').css('top', $('.sch_view').offset().top);
		              
		            $('#loading').css('padding-top', 100);
		                
		            $('#loading').show().fadeIn('fast');
		        },
		        complete: function () {
		           $('#loading').fadeOut();
		           $('.btn_export_excel').show();
		        }
			});
	   }else{
			if(is_open == 'Y'){
				close_suborg(org_code, $(this).closest('tr'));
				$(this).closest('tbody').find('tr').each(function(){
					if(org_code == $(this).attr('name')){
						$(this).hide();
					}
					
				});
				
				$thisimg.attr('is_open', 'N');
				$thisimg.attr('src', '/img/folder-closed.gif');
			}else{
				$(this).closest('tbody').find('tr').each(function(){
					if(org_code == $(this).attr('name')){
						$(this).show();
					}
				});
				$thisimg.attr('is_open', 'Y');
				$thisimg.attr('src', '/img/folder.gif');
			}
	   }
   });
  
   function close_suborg(orgcode, $tr){
	   $tr.siblings().each(function(){
			if(orgcode==$(this).attr('name')){
				if($(this).attr('is_suborg')=='Y'){
					close_suborg($(this).find('td').eq(0).attr('org_code'), $(this));
					$(this).find('td').eq(0).find('img').attr('is_open', 'N');
					$(this).find('td').eq(0).find('img').attr('src', '/img/folder-closed.gif');
				}
				$(this).hide();
			}
	   });
	   
   }
   
   function close_org(orgcode, $tbody){
	   $tbody.find('tr').each(function(){
			if(orgcode == $(this).attr('name')){
				
				$(this).hide();
				
			}
			
		});
   }
   
   $('.contents_body').on('click', '.org_sel_area', function(){
	   SelectedRowChange($(this)); 
   });
   
   $('.btn_search').click(function(){
	    $('#upperOrgCode').val($('#upperOrgCode1').val());
	    $('#upperOrgName').val($('#upperOrgName1').val());
	    $('#begindate').val($('#begindate1').val());
	    $('#enddate').val($('#enddate1').val());
	    $('#majrCode').val($('#majrCode1').val());
	    $('#buseoindc').val($('option:selected',$('#majrCode1')).attr('buseoindc'));
	    //document.listForm.action = "/stat/statistpoltotallist.do";
		//document.listForm.submit();
	    searchList();
	});
   
   $('.btn_export_excel').click(function(){
	  var contents_item = [];
	  $('.tblInfoType4').find('tr').each(function(){
		 var row_item = ""; 
		 $(this).find('td, th').each(function(){
			 row_item += $(this).text().replace(/,/g,"/") + "|";
		 });
		 contents_item.push(row_item);
	  });
	  var params = [];
		params[0] = JSON.stringify({"inputname": 'begindate', "inputvalue": $('#begindate').val()});
	    params[1] = JSON.stringify({"inputname": 'enddate', "inputvalue": $('#enddate').val()});
	    params[2] = JSON.stringify({"inputname": 'majrCode', "inputvalue": $('#majrCode').val()});
	    params[3] = JSON.stringify({"inputname": 'upperOrgCode', "inputvalue": $('#upperOrgCode').val()});
	    params[4] = JSON.stringify({"inputname": 'bodyData', "inputvalue": contents_item.toString()});
      	gopage('/stat/statisttotalexportexcel.do', params, 'searchVO');
	  
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
    	<input type="hidden" id="upperOrgCode" name="upperOrgCode" value="<c:out value="${searchVO.upperOrgCode}"/>" />
    	<input type="hidden" id="upperOrgName" name="upperOrgName" value="<c:out value="${searchVO.upperOrgName}"/>" />
    	<input type="hidden" id="begindate" name="begindate" value="<c:out value="${searchVO.begindate}"/>" />
    	<input type="hidden" id="enddate" name="enddate" value="<c:out value="${searchVO.enddate}"/>" />
    	<input type="hidden" id="majrCode" name="majrCode" value="<c:out value="${searchVO.majrCode}"/>" />
    	<input type="hidden" id="buseoindc" name="buseoindc" value="<c:out value="${searchVO.buseoindc}"/>" />
    	<div>
    		<a class="btn_type1"><span>정책및 조직별 종합 통계</span></a> 
    		<a href="/stat/statistuserlist.do" class="btn_type2"><span>직원별 통계</span></a> 
    		<a href="/stat/statistorglist.do" class="btn_type2"><span>조직별 통계</span></a>
    	</div>
    	<div class="pd10"></div>  
    	<div class="subTT"><span>정책및 조직별 종합 통계</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li><p>검색 상위조직</p>
        		<input type="text" name="upperOrgName1" readonly id="upperOrgName1" style="width:150px" value="<c:out value="${searchVO.upperOrgName}" />" class="srh"/><input type="hidden" name="upperOrgCode1" id="upperOrgCode1" style="width:90px" value="<c:out value="${searchVO.upperOrgCode}" />" />
        	</li>
        	<li>
        	<p>진단일자</p> <input type="text" name="begindate1" id="begindate1" readonly value="<c:out value="${searchVO.begindate}" />" style="width:150px;z-indx:100000" class="srh"> 
        	~ <input type="text" name="enddate1" id="enddate1" readonly value="<c:out value="${searchVO.enddate}" />" style="width:150px;z-indx:100001" class="srh">
        	</li> 
        	<br /><br />
        	<li><p>진단선택</p>
        		<select name='majrCode1' id='majrCode1'>
        		<c:forEach var="result" items="${majrCodeList}" varStatus="status">
					<option value='<c:out value="${result.majcode}" />' buseoindc='<c:out value="${result.buseoindc}" />' <c:if test="${searchVO.majrCode == result.majcode}" >selected</c:if>><c:out value="${result.diagdesc}" /></option>
				</c:forEach>	
        		</select>
        	</li>
        	<li>
            	<a class="btn_black btn_search"><span>검색</span></a>
            	&nbsp;&nbsp;&nbsp;<a class="btn_black btn_export_excel" style="display:none;"><span>Excel</span></a>
            </li>  
        </div>
        <!-- E :search -->
    	 <div class="sch_view" style="margin-bottom:20px;overflow:auto;height:200px;">
    	 	<table cellpadding="0" cellspacing="0" class="tblInfoType4" style="font-size:11px;width:100%;">
    	 		<thead class="contents_head">
    	 		</thead>
            	<tbody class="contents_body" style="font-size:12px;">
            	</tbody>
            </table>
            
        </div>

		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
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
		    $('#upperOrgName1').val($(this).text());
		    $('#upperOrgCode1').val(orgcode)
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