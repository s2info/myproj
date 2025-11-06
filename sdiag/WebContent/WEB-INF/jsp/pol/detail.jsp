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

<script type="text/javascript">

$(function () {		
	

	$('#paging').paging({
        current: <c:out value="${currentpage}" />,
        max: <c:out value="${totalPage}" />,
        onclick: function (e, page) {
            //$('#pageIndex').val(page);
            searchList(page);
            //searchList(page, $('#searchTxt').val());
        }
    });
	
	function searchList(page)
	{
		
		document.listForm.pageIndex.value = page;
		document.listForm.action = "/pol/policystatusdetail.do";
		document.listForm.submit();
	}
	
	function convertSelectedTitle(idx)
	{
		if(idx == 1)
			return "부문전체";
		else if(idx == 2)
			return "본부전체";
		else if(idx == 3)
			return "담당전체";
		else if(idx == 4)
			return "팀전체";
		else 
			return "전체";
	}
	
    function set_selectboxClear(idx)
    {
    	for(var i=idx;i<=4;i++){
    		$('#org_'+i+' > option').each(function(){
    			$(this).remove();
    		});
    		$('#org_'+i).append("<option value=''>"+ convertSelectedTitle(i) +"</option>");
    	}
    }
    
    $('.selected_org').change(function(){
    	
    	var next_elm = 'org_'+ $(this).attr('norg');
    	var next_val =  $(this).attr('norg');
    	var params = {orgval:$(this).val(), selval:$(this).attr('norg')};
    	
        $.ajax({
            data: params,
            url: "/pol/getSelectedOrglist.do",
            contentType: "application/json; charset=utf-8",
            type: "get",
            dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + "\r\n" + errorThrown);
            },
            success: function (data) {
            	if(data.ISOK)
                {
            		set_selectboxClear(next_val);
            		$('#'+next_elm+' > option').each(function(){
            			$(this).remove();
            		});
            		$('#'+next_elm).append("<option value=''>"+ data.selTitle +"</option>");
                	for(var i=0;i<data.list.length;i++)
                	{
                		$('#'+next_elm).append("<option value='"+ data.list[i]["org_code"] +"'>"+ data.list[i]["org_nm"] +"</option>");
                	}
                }
                else
                {
                	alert(data.msg);	
                	//history.back(-1);
                } 	

            }

        });
    	
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
        maxDate:"<c:out value="nowDate" />",
        buttonImage: "/img/ic_date1.png",
        buttonImageOnly: true,
        beforeShow:function(){
        	setTimeout(function(){
        		$(".ui-datepicker").css("z-index","99999");
        	}, 0);
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -4px 5px");
   
	$('.btn_search').click(function(){
		$('#dtType').val('');
		 $('#dtSeq').val('');
    	search_list();
    	
    });
    
    function search_list()
    {
    	/*검색 조건 정책, 이름, 사번 추가해야함......*/
    	var data ={
    			org_bumon : $('#org_1').val(),
    			org_bonbu :  $('#org_2').val(),
    			org_damdang : $('#org_3').val(),
    			org_team : $('#org_4').val(),
    			begin_date : $('#begin_date').val(),
    			
    			dtType :  $('#dtType').val(),
    			dtSeq :  $('#dtSeq').val(),
    			searchCondition : $('#searchCondition').val(),
    			searchKeyword : $('#searchKeyword').val(),
    			auth : <c:out value="${auth}"/>
        	};
		    
		$.ajax({
				url : '/pol/getploUserResultList.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					alert(textStatus + "\r\n" + errorThrown);
				},
				success : function(data) {
					if (data.ISOK) {
						$('.contents_body').empty().append(data.body);
					} else {
						alert(data.msg);
						//history.back(-1);
					}

				}

			});
	}
    
    $('.btn_search_area').click(function(){
    	var tname=$(this).attr('tbname');
    	$('.btn_search_area').each(function(){
    		$(this).removeClass('ON');
    	});
    	if(tname=='searchText'){
    		$('.searchText').show();
    		$('.searchBox').hide();
    	}else{
    		$('.searchText').hide();
    		$('.searchBox').show();
    	}
    	$(this).addClass('ON');	
    });
    
    $(window).bind("load", function() {
		search_list();
	});
    
    
});
</script>
    
</head>
<body>
	<div id="wrap_body">
		<!-- Top Menu Begin -->
		<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
		<!-- Top Menu End -->
		<form:form commandName="searchVO" name="listForm" method="post">
		<div id="wrap_contents">
			<%@ include file="/WEB-INF/jsp/cmm/polLeftMenu.jsp" %>
			<div class="WC_right">
				<div class="S_tab1">
					<ul>
						<li><a class="btn_search_area ON" tbname="searchText" style="cursor:pointer;">상세 검색조건</a></li>
						<li><a class="btn_search_area" tbname="searchBox" style="cursor:pointer;">재 검색</a></li>
					</ul>
				</div>
				<div class="searchText" >
				<div class="search1" style="padding-bottom:0px;">
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>사용일자 : </span></li>
						<li></li>
						
					</ul>
				</div>
				<div class="search1">	
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>소속 : </span></li>
						<li></li>
						
					</ul>
				</div>	
				</div>
				<div class="searchBox" style="display:none;">
				<div class="search1">
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>사용일자</span></li>
						<li><input type="text" name="begin_date" id="begin_date" readonly value="<c:out value="${getdate}" />" style="width:150px"></li>
						
					</ul>
				</div>
				<div class="marT10"></div>
				<div class="search1" style="padding-bottom:5px;">
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>소속</span></li>
						${orgbody }
				</div>
				<div class="search1" style="padding-top:5px;">
					<ul>
						<li><img src="/img/dot1.png"></li>
						<li><span>검색어</span></li>
						<li>
							<select  name="searchCondition" id="searchCondition" style="width:60px">
								<option value="1">사번</option> 
								<option value="2">이름</option>
							</select>
						</li>
						<li><input type="text" name="searchKeyword" id="searchKeyword" style="width:90px" value="<c:out value="${emp}" />"></li>
						<li><div class="btn1 btn_search"><a style="cursor:pointer;">검색</a></div></li>
					</ul>
				</div>
				</div>
				
				<div class="marT10"></div>
				<div class="WCR_box1" >
					<table border="0" class="TBS3" cellpadding=0 cellspacing=0 summary="TBL">
						<caption>TBL</caption>
						<colgroup>
							<col style="width:15%">
							<col style="width:10%">
							<col style="width:*">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
						</colgroup>
						<!--  -->
						<tr>
							<th>조직</th>
							<th>사번(이름)</th>
							<th>지수화정책</th>
							<th>건수</th>
							<th>점수</th>
						<c:choose><c:when test="${auth == 3 }">	
							<th>승인상태</th>
							<th>제재여부</th>
							<th>소명조치</th>
						</c:when>
						<c:otherwise>
							<th>진단내역</th>
							<th>선택</th>
							<th>소명신청<br>승인대상</th>
						</c:otherwise></c:choose>
						</tr>
						<tbody class='contents_body'>
						
						</tbody>		
					</table>
					<div class="marT10"></div>
					<div style="width:100%" class="marT10 disp">
						<div class="F_left">
							<div class="number1" style='display:none;'>
								<ul id="paging"></ul>
							</div>
						</div>
						<c:if test="${auth == 1}" >
						<div class="btn3 F_right"><a class="btn_appl_order" style='cursor:pointer;'>선택항목 소명 신청요청</a></div>
						</c:if>
						<div class="btn3 F_right"><a href="/pol/policystatus.do?auth=<c:out value="${auth}"/>&emp=<c:out value="${emp}"/>">이전</a></div>
						
					</div>
					
				</div>
				
				
			</div>
		</div>
		<input type="hidden" name="pageIndex" id="pageIndex" value="" />
		<input type="hidden" name="dtType" id="dtType" value="${dtType }" />
		<input type="hidden" name="dtSeq" id="dtSeq" value="${dtSeq }" />
		</form:form>
	</div>
	<!-- body end -->
	<!-- footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
	<!-- footer end -->

</body>

</html>