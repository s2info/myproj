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

	
	$('#begindate, #enddate').datepicker({
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
   
   $('.btn_search').click(function(){
		 searchList(1);
   });
   
   $('.btn_export_excel').click(function(){
	   
	   
		var params = [];
		params[0] = JSON.stringify({"inputname": 'begindate', "inputvalue": $('#begindate').val()});
	    params[1] = JSON.stringify({"inputname": 'enddate', "inputvalue": $('#enddate').val()});
	    params[2] = JSON.stringify({"inputname": 'policyid', "inputvalue":  $('#policyid').val()});
	    params[3] = JSON.stringify({"inputname": 'searchType', "inputvalue": 'U'});
	    params[4] = JSON.stringify({"inputname": 'upperOrgCode', "inputvalue":  $('#upperOrgCode').val()});
       gopage('/stat/statistorguserexportexcel.do', params, 'searchVO');
       
       
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
	
	function searchList(page)
	{
	   $('#pageIndex').val(page);	
	   var upperOrgCode = $('#upperOrgCode').val();
	   var begindate = $('#begindate').val();
	   var enddate = $('#enddate').val();
	   var policyid = $('#policyid').val();
	   var buseoindc = $('option:selected',$('#policyid')).attr('buseoindc');
	   var pagesize = $('#pageSize').val();
	   var pageIndex=$('#pageIndex').val();
	   var data = {begindate:begindate
	   			,enddate:enddate
	   			,upperOrgCode:upperOrgCode
	   			,policyid:policyid
	   			,buseoindc:buseoindc
	   			,pageSize:pagesize
	   			,pageIndex:pageIndex};
		   
	   $.ajax({
			url : '/stat/getstatistpolicyfactInfouserdata.do',
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
					$('.contents_body').empty().append(data.list_body);
					
					$('#paging').paging({
				        current: data.currentpage,
				        max: data.totalPage,
				        onclick: function (e, page) {
				            searchList(page);
				        }
				    });
					
					if(data.ISDATA == 'Y'){
						$('.btn_export_excel').show();
					}
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
           }
		});
		   
   }
	
	$('#paging').paging({
		current: <c:out value="${currentpage}" />,
	    max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
	
	$(window).bind("load", function() {
  		searchList(1);
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
    	<div>
    		<a href="/stat/statistpoltotallist.do" class="btn_type2"><span>정책및 조직별 종합 통계</span></a> 
    		<a class="btn_type1"><span>직원별 통계</span></a> 
    		<a href="/stat/statistorglist.do" class="btn_type2"><span>조직별 통계</span></a>
    	</div>
    	<div class="pd10"></div>
    	<div class="subTT"><span>직원별 통계</span></div>
    	<!-- S :search -->
        <div class="sch_block3">
        	<li><p>검색 상위조직</p>
        		<input type="text" name="upperOrgName" readonly id="upperOrgName" style="width:150px" value="<c:out value="${searchVO.upperOrgName}" />" class="srh"/><input type="hidden" name="upperOrgCode" id="upperOrgCode" style="width:90px" value="<c:out value="${searchVO.upperOrgCode}" />" />
        	</li>
        	<li>
        	<p>진단일자</p> <input type="text" name="begindate" id="begindate" readonly value="<c:out value="${searchVO.begindate}" />" style="width:150px;z-indx:100000" class="srh"> 
        	~ <input type="text" name="enddate" id="enddate" readonly value="<c:out value="${searchVO.enddate}" />" style="width:150px;z-indx:100001" class="srh">
        	</li> 
        	<br /><br />
        	<li><p>정책선택</p>
        		<select name='policyid' id='policyid'>
        		<c:forEach var="result" items="${polList}" varStatus="status">
					<option value='<c:out value="${result.policyid}" />' buseoindc='<c:out value="${result.buseoindc}" />' <c:if test="${searchVO.policyid == result.policyid}" >selected</c:if>><c:out value="${result.majdesc}" /> >> <c:out value="${result.mindesc}" /> >> <c:out value="${result.policyname}" /></option>
				</c:forEach>	
        		</select>
        	</li>
        	<li><p>조회수</p>
        		<select name='pageSize' id='pageSize'>
        			<option value='10' selected>10</option>
        			<option value='20'>20</option>
        			<option value='50'>50</option>
        			<option value='100'>100</option>
        		</select>
        	</li>
        	<li>
            	<a class="btn_black btn_search"><span>검색</span></a>
            	&nbsp;&nbsp;&nbsp;<a class='btn_black btn_export_excel' style="display:none;"><span>Excel</span></a>
            </li> 
        </div>
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
           	<table cellpadding="0" cellspacing="0" class="tbl_list1">
                <colgroup>
				<col style="width:*">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:7%">
				</colgroup>
			<tr>
				<th>진단기간</th>
				<th>정책명</th>
				<th>조직1</th>
				<th>조직2</th>
				<th>조직3</th>
				<th>조직4</th>
				<th>조직5</th>
				<th>조직6</th>
				<th>사번<br>성명</th>
				<th>건수</th>
			</tr>
			<tbody class="contents_body">
			<tr style="font-weight:bold;height:300px;">
				<td colspan="10">검색결과가 없습니다.</td>
			</tr>	
			</tbody>
		    </table>
           <!-- E :list --> 
        </div>
		<!-- S : page NUM -->
        <div class="pagingArea1 pagingPadd1">
			<ul id="paging" class="paging" style='float:left;padding-left:20px;'></ul>
		</div>
        <!-- E : page NUM -->
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
		    $('#upperOrgName').val($(this).text());
		    $('#upperOrgCode').val(orgcode)
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