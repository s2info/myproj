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

.Search { clear:both; }

.tbl { clear:both;text-align:center;border-top:2px solid #e66c6c; }
.row { clear:both; height:28px; border-bottom:#999 solid 0px;}
.col { text-align:center;  }

.hed { border-top:#999 solid 1px;color:#222222;font-weight:bold;font-size:12px; } /* 머릿글 행*/
.odd { background-color:#fff;color:#222222;font-weight:bold;font-size:12px; }     /* 홀수 행 */
.evn { background-color:#F7F7F7;color:#222222;font-weight:bold;font-size:12px;  }     /* 짝수 행 */
.cur { background-color:#eff497; cursor:pointer;  }  /* 선택 행 */
.top { background-color:#eee;}  /* 처음 행 */

.co0 { width:80px;display:inline-block;}
.co1 { width:80px;display:inline-block;}
.co2 { width:80px;display:inline-block; }
.co3 { width:270px;display:inline-block; padding-left:20px; text-align:left;word-break:break-all;}
.co4 { width:80px;display:inline-block; cursor:pointer;  }
.co5 { width:80px;display:inline-block; }
.co6 { width:120px;display:inline-block; }
.co7 { width:30px;padding-left:10px;text-align:left;display:inline-block; }
.co8 { width:30px;padding-left:10px;text-align:left;display:inline-block; }
.co9 { width:30px;padding-left:10px;text-align:left;display:inline-block; }
.mis { height:50px; }
	.mis li { }
	.mis li.btn { padding-top:10px; }


.codeHolder { clear:both; }
.blankItemCss {width:100%; height:30px; background-color:#f00;}
.jsBtn { width:28%; height:75%; border:#666 solid 1px; vertical-align:text-bottom; }
.jsTxt { width:95%; height:75%; border:#666 solid 1px; vertical-align:text-bottom; }

div.vBtns { float:right; }

.allow { }
.open { }

.aCont { clear:both; margin:20px 0; padding:30px; max-width:1000px; border:#777 solid 1px; overflow:auto; }
.aSetup { }

/*팝업배경 - 투명하게*/
.ui-widget-content {
	border: 0px solid #aaaaaa;
	background: transparent;
	color: #222222;
</style>
<script type="text/javascript" language="javascript">

$(function () {
	
	$('.btn_search').click(function(){
    	searchList();
    });

	function searchList()
	{
		
		document.listForm.action = "/man/dignosisItemlist.do";
		document.listForm.submit();
	}
	
	$(".codeHolder").sortable({
		placeholder: 'blankItemCss',
		start: function (event, ui) {
			ui.item.startPos = ui.item.index();
		},
		change: function (event, ui) {
		},
		beforeStop: function (event, ui) {
			//ui.placeholder.index()
		},
		stop: function (e, ui) {
			if(ui.item.startPos == ui.item.index())
				return;

			var minCds = [];
			ui.item.parent().children().each(function(inx){
				$(this).children().filter(':nth-child(5)').text(inx+1)
				minCds.push($(this).children().filter(':nth-child(2)').text() + "/" + $(this).children().filter(':nth-child(5)').text());
				$(this).removeClass();
				$(this).addClass('row ' + (inx%2==0?'odd':'evn')); /*첫번째(i==0) 홀수로 볼 것*/
			});
			
			onAction({"row":$('#' + ui.item.prop('id')),"min_cds":minCds,"mode":4});
		}
	}).disableSelection();
	
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
            	searchList();
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
	
	$('.btn_Create_sol').click(function(){
		var mode = $(this).attr('mode');
		var data={mode:mode,majcd:'',mincd:'',desc:''};
		onModify(data);
		
	});

	$('#code_area').on('click', '.btn_modify, .btn_add, .btn_delete', function(){
		
		var row = $(this).closest('ul');
		var majcd = row.find('li').first();
		var mincd = row.find('li').eq(1);
		var desc = row.find('li').eq(2);
		var mode = $(this).attr('mode');
		var data={mode:mode,majcd:majcd.text(),mincd:mincd.text(),desc:desc.text()};
		
		onModify(data);
	});
	
	function onModify(data){
		if(data.mode=="5"){
			if(!confirm('진단항목를 삭제 하시겠습니까?')){ 
				return false;
			}
		}
		var mode=data.mode;
		
		//var data={mode:mode,majcd:majcd.text(),mincd:mincd.text(),desc:desc.text()};
		$.ajax({
            data: data, 
            url: "/code/setDiagnosisItemModifyPopup.do",
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
                	if(mode==5){
                		searchList();
                	}else{
	                	 $('.DialogBox').html(data.popup_body);
	                	 $('.DialogBox').dialog({ width: 655, height: 520 });
	                     $('.DialogBox').dialog('open');
	                     $('.DialogBox').data('is_save', 'N');
	                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
                	}
                }
                else {
                    alert(data.MSG);
                }

            }

        });	
	}
	
	$('#code_area').accordion({
		heightStyle:'content'
		
	});
	
	$('.btn_export_excel').click(function(){
		var params = [];
        gopage('/man/diagitemexportexcel.do', params, 'form1');
	});

});


function onAction(x) {
	var maj = x.row.children().filter(':nth-child(1)').text();
	var min = x.row.children().filter(':nth-child(2)').text();
	var nam = String(x.mode) != '4' ? x.row.children().filter(':nth-child(3)').children().first().val().Trim() : x.row.children().filter(':nth-child(3)').text();
	var act = String(x.mode) != '4' ? x.row.children().filter(':nth-child(4)').children().first().val().Trim() : x.row.children().filter(':nth-child(4)').text();
	
	var arg = {};
	if (String(x.mode) == '4'){
		arg = {"mode":x.mode,"maj_cd":maj,"min_cd":x.min_cds.toString(),"name":nam,"active":act};
	}
	else{
		arg = {"mode":x.mode,"maj_cd":maj,"min_cd":min,"name":nam,"active":act};
	}

	 $.ajax({
		url : '/code/setDiagnosisItemUpdate.do',
		data : arg,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			alert(textStatus + "\r\n" + errorThrown);
		},
		success : function(data) {
			if (data.ISOK) {
				//alert('DONE');
				
			} else {
				alert(data.msg);
				//history.back(-1);
			}

		}

	});
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
    	<div class="subTT"><span>진단항목 관리</span></div>
    	<div class="pd20"></div>
 		<div class="tbl"  style="border-top: solid 2px #3c7dbe;border-left:solid 1px #dcdcdc;border-right:solid 1px #dcdcdc;">
    		<div style='width:100%;display:inline-block;background:#f3f3f3;'>
			<ul class="row hed" style='border-top:solid 1px #dcdcdc;float:left;display:inline-block;border-bottom:#eee solid 1px;padding-top:15px;'>
				<li style='width:30px;display:inline-block;'>&nbsp;</li>
				<li class="col co1">대진단</li>
				<li class="col co2">중진단</li>
				<li class="col co3">명칭</li>
				<li class="col co4">사용여부</li>
				<li class="col co5">순서</li>
				<li class="col co6">등록일</li>
				<li class="col co7"></li>
				<li class="col co8"></li>
				<li class="col co9"></li>
			</ul>
			</div>
		</div>
		<div id="code_area">
		${tblbody }
		</div>	
		<div class="btn_black2">
			
			<a class="btn_black btn_Create_sol" mode='3'><span>대진단 항목 추가</span></a>
		</div>
		<input type='text' style='display:none;'/>
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