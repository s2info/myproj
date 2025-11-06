<%--
  Class Name : ExanalPolList.jsp
  Description : ExanalPolList 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2015.10.27   CJLEE              최초 생성
 
    author   : LEE CHANG JAE
    since    : 2015.10.27
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<title>수동 업로드 관리</title>
<script type="text/javaScript" language="javascript">
<!--
/* ********************************************************
 * 페이징 처리 함수
 ******************************************************** */
function fn_egov_pageview(pageNo){
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/sym/ccm/zip/EgovCcmZipList.do'/>";
    document.listForm.submit();
}
//-->
</script>

<script type="text/javascript" language="javascript">
<!--
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
	var pageSize=$('#pageSize').val();
	var pageIndex=$('#pageIndex').val();
	var begin_date=$('#begin_date').val();
	var data = {
			sqno:${sqno}
			,pageSize:pageSize
			,pageIndex:pageIndex
			,begin_date:begin_date
			};
    $.ajax({
		url : '/handy/ExanalPolGrid.do',
		data : data,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			alert(textStatus + "\r\n" + errorThrown);
		},
		success : function(data) {
			if (data.ISOK) {
				$('.list_contents_body').empty().append(data.strList);
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


$(window).bind("load", function() {
	
	<c:if test="${totCnt > 0}">
	searchList(1);
	</c:if>
	
});
//-->
</script>

<script type="text/javaScript" language="javascript">
<!--
var msg = '${MSG}';
if(msg != ''){
	alert(msg);
}

/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function list_Pol(){
    location.href = "<c:url value='/handy/ExanalPolList.do'/>";
}
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function regist_ExcelPol(){
	//alert("dddd");
    var varForm              = document.all["Form"];

    // 파일 확장명 확인
    var arrExt      = "xlsx";
    var objInput    = varForm.elements["fileNm"];
    var strFilePath = objInput.value;
    var arrTmp      = strFilePath.split(".");
    var strExt      = arrTmp[arrTmp.length-1].toLowerCase();
    
    if(confirm(" 데이터를 저장하시겠습니까?")){
    	
    }else{
		return;
	}
    
	//if (arrExt != strExt) {
	if (!(arrExt.indexOf(strExt) > -1) || strExt == "") {   
        alert("엑셀 파일을 첨부하지 않았습니다.\n확인후 다시 처리하십시오. ");
        abort;
    } 
    
    varForm.action           = "/handy/ExanalPolRegist.do";
    varForm.submit();

}

/*********************************************************
* 미리보기화면
******************************************************** */

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
$('.DialogBox').click(function () {
	$('.DialogBox').dialog('close');
});

$("#Form").submit(function(e){alert(2323);
	var postData = $(this).serializeArray();
	$.ajax({

		url : '/handy/ExanalPolPreview.do',
		data : postData,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			alert(textStatus + "\r\n" + errorThrown);
		},
		success : function(data) {
			if (data.ISOK) {
				 $('.DialogBox').html(data.previewList);
                 $('.DialogBox').dialog({ width: 600, height: 465 });
                 $('.DialogBox').dialog('open');
                 $('.DialogBox').data('is_save', 'N');
                 $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.WP_tit' });
			} else {
				alert(data.MSG);
			}

		}		
		
	})
});

function excelPreview(data){
	
	//var varForm              = document.all["Form"];
	
	var vardata = new FormData();
	
	varForm.append("fileNm", $("input[name=fileNm]")[0].files[0]);
	
	//var objInput    = varForm.elements["fileNm"];

    $.ajax({
		url : '/handy/ExanalPolPreview.do',
		data : varForm,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			alert(textStatus + "\r\n" + errorThrown);
		},
		success : function(data) {
			if (data.ISOK) {
				 $('.DialogBox').html(data.previewList);
                 $('.DialogBox').dialog({ width: 600, height: 465 });
                 $('.DialogBox').dialog('open');
                 $('.DialogBox').data('is_save', 'N');
                 $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.WP_tit' });
			} else {
				alert(data.MSG);
			}

		}

	});
	
}
//-->
</script>

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
	
	$('.btn_newinsert').click(function(){
		document.location.href="./ExanalPolList.do";
	});
	
	$('.btn_delRecode').click(function(){

		if(confirm($('#fileName').val() + " 해당 데이터를 삭제하시겠습니까?")){
			var cmd='DelRecode';			
			var sqno = $('#sqno').val();
			var begin_date=$('#begin_date').val();
			var fileName = $('#fileName').val();
			
			var data = {
					sqno:${sqno}
					,cmd:cmd
					,table_name:fileName
					,begin_date:begin_date
			};
		    $.ajax({
				url : '/handy/ExanalDelRecode.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					alert(textStatus + "\r\n" + errorThrown);
				},
				success : function(data) {
					if (data.ISOK) {
						$('.list_contents_body').empty().append(data.strList);
						$('#totcnt').html(data.totCnt);
						searchList(1);
						$('#paging').paging({
					        current: data.currentpage,
					        max: data.totalPage,
					        onclick: function (e, page) {
					            searchList(page);
					        }
					    });
					} else {
						alert(data.MSG);
						searchList(1);
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
			
			
			
		}else{
			return;
		}
		
	});	
	
	$('.btn_deltable').click(function(){
		if(confirm($('#fileName').val() + " 테이블을 삭제하시겠습니까?")){
			var cmd='DelTable';			
			var sqno = $('#sqno').val();
			var cmd = $('#cmd').val();
			var fileName = $('#fileName').val();
			
			var data = {
					sqno:${sqno}
					,cmd:cmd
					,fileName:fileName
					};
		    $.ajax({
				url : '/handy/ExanalDelTable.do',
				data : data,
				type : 'POST',
				dataType : 'json',
				error : function(jqXHR, textStatus, errorThrown) {
					alert(textStatus + "\r\n" + errorThrown);
				},
				success : function(data) {
					if (data.ISOK) {
						$('.list_contents_body').empty().append(data.strList);
					} else {
						alert(data.MSG);
					}
					document.location.href="/handy/ExanalPolList.do";

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
			
			
			
		}else{
			return;
		}

		//document.polForm.action = "/handy/ExanalPolList.do";
		//document.polForm.submit();		
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
        //maxDate:"<c:out value="${nowDate}" />",
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
    $("input:radio[name=gubun]").click(function(){
    	var gubun = $(this).val();
    	
    	if(gubun == "G"){
    		$(".good_row").show();
    	}else{
    		$(".good_row").hide();
    	}
    });
    
    
});

	
</script>

</head>
 
<body>
<!-- S : header -->
		<!-- Top Menu Begin -->
		<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
		<!-- Top Menu End -->
<!-- E : header -->
<!-- S : contents -->
<div id="cnt">
	<!-- S : left contents -->
	<div id="cnt_L">
	



 		<div class="left_menu">
		<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %>
		<c:import url="/handy/ExanalTableList.do" />
        </div>
			
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    
    	<div class="subTT"><span>수동 업로드 관리</span></div>
        
    	<!-- S :search -->
    	<form name="Form" id="Form" action="<c:url value='/handy/ExanalPolRegist.do'/>" method="post" enctype="multipart/form-data" >
        <div class="sch_block3">
        	<li>
            	<p>엑셀파일을 업로드하세요.</p> 
            </li>
            <br /><br />
            	
            <div>
            <li class="radio_button">
            	<p>로그 유형</p>
				<label style="margin-left: 22px;"><input type='radio' id='gubun' name='gubun' value='' <c:if test="${gubun == ''}" >checked</c:if> /><span> 취약 </span></label>
				<label ><input type='radio' id='gubun' name='gubun' value='G' <c:if test="${gubun == 'G'}" >checked</c:if> /><span> 양호(취약로그 자동생성)</span></label>
				<label><input type='radio' id='gubun' name='gubun' value='S' <c:if test="${gubun == 'S'}" >checked</c:if> /><span>서버 이행관리</span></label>
				
				
			</li>
			<li class="ck_button good_row" style="padding-left: 20px;display: <c:if test="${gubun != 'G'}" >none</c:if>;" >
				<label><input type="checkbox" name="gubun2" id="gubun2" <c:if test="${gubun2 == 'Y'}" >checked</c:if> value="Y" /> <span>협력사 포함</span></label>
			</li>
			 <br /><br />
			<li>
            	<p>정책파일선택</p>
				<input name="fileNm" type="file" id="fileNm"/ class="srh">
			</li> 
        	
				<a href="#noscript" onclick="regist_ExcelPol(); return false;" class="btn_black"><span>엑셀데이터 저장</span></a>
            </li> 
            <li>
            <p>날 짜</p><input type="text" name="begin_date" id="begin_date" readonly value="" style="width:150px;z-indx:100000" class="srh">
            </li>
        	<li>
                <a class="btn_black btn_search"><span>검색</span></a>
            </li>
            <li>
            <c:if test="${sqno > 0 }">
                <a class="btn_black btn_delRecode"><span>삭제</span></a>
            </c:if>
            </li>
            </div>
            
            <br><br>
            <div>
            
            <li>
            <c:if test="${sqno > 0 }">
                <a class="btn_newinsert btn_scr3" ><span>신규테이블등록</span></a>
            </c:if>
            <c:if test="${sqno == 0 }">
				<a class="btn_newinsert btn_scr4" ><span>신규테이블등록</span></a>            
            </c:if>
            </li>
            
            
            <li>
            <c:if test="${sqno > 0 }">
                <a class="btn_deltable btn_scr5"><span>등록테이블삭제</span></a>
            </c:if>
            <c:if test="${sqno == 0 }">
                <a class="btn_deltable btn_scr3"><span>등록테이블삭제</span></a>
            </c:if>            
            </li>
            </div>
            
            
        </div>
	         <input name="cmd" type="hidden" value="ExcelPolRegist"/>

        <!-- E :search -->        
		<c:if test="${MSG != null }">
		<div class="sch_block3">
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li><c:out value="${MSG}" /></li>
				<li><c:out value="${MSG_DETAIL}" /></li>
			</ul>	        
        </div>
        </c:if>
        
        <c:if test="${sqno > 0 }">
        
		<div class="sch_block3">
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>테이블명 : <b><c:out value="${tableName}"/></b></li>
			</ul>
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>업로드파일명 : <c:out value="${filename}" /></li>
			</ul>
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>레코드수 : <b><span id="totcnt"><c:out value="${totCnt}" /></span></b></li>
			</ul>
		</div> 
		
		</c:if>        
               
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
				<table class="tbl_list1" border="0" cellSpacing="0" cellPadding="0" style="width: 100%;">
				<tbody class='list_contents_body'></tbody>
				</table>
            <!-- E :list -->
        </div>
        
        <input type="hidden" name="sqno" id="sn" value="<c:out value="${sqno}"/>" />
        <input type="hidden" name="fileName" id="fileName" value="<c:out value="${tableName}"/>" />
        
	    <input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />

		<div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>


                <!-- 페이지 네비게이션 시작 -->
                
				<div style="display:table-cell;vertical-align:middle;width:1280px;height:60px;text-align:center;">
				<div class="pagingArea1 pagingPadd1">
					<ul id="paging" class="paging"></ul>
				</div>
				</div>                
                <!-- //페이지 네비게이션 끝 -->          

		</form>
    </div>
    <!-- E : center contents -->
    
	<script language="javascript">
	<!--
		$('.preViewButton').click(function (){
		
		var varForm              = document.all["Form"];
	
		// 파일 확장명 확인
		var arrExt      = "xls";
		var objInput    = varForm.elements["fileNm"];
		var strFilePath = objInput.value;
		var arrTmp      = strFilePath.split(".");
		var strExt      = arrTmp[arrTmp.length-1].toLowerCase();
		
		if (arrExt != strExt) {
		    alert("엑셀 파일을 첨부하지 않았습니다.\n확인후 다시 처리하십시오. ");
		    abort;
		} 	
		
		var data = {};
		excelPreview(data);
		});
	//-->
	</script>	    
    
    <!-- S : footer -->
<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
    <div class='DialogBox'></div>
</div>
</body> 
</html>