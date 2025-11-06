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

/* ********************************************************
 * 엑셀등록 처리 함수 
 ******************************************************** */
// function regist_ExcelPol(){
//     location.href = "<c:url value='/handy/ExanalPolRegist.do'/>";
// }
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
	
	searchList(1);
	
});
//-->
</script>

<script type="text/javaScript" language="javascript">
<!--
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
    
    varForm.action           = "/handy/ExanalNewRegist.do";
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
		alert(pcgactinfo);
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
	        		//제재
	        	if(mode=='S'){
	        		var sanctcode = $(this).closest('tr').find('td').eq(10).find('select');
		       		if(sanctcode.length > 0){
		       			sanctlist.push(empno + "/" + uuid + "/" + macno + "/" + uip + "/" + polid + "/" + regdate + "/" + sanctcode.val());
		       		}	
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
		var pheight = data.mode == 'P' ? 440 : 470;
        $.ajax({
			url : '/man/setSanctAllPrecessPopup.do',
			data : data,
			type : 'POST',
			dataType : 'json',
			error : function(jqXHR, textStatus, errorThrown) {
				alert(textStatus + "\r\n" + errorThrown);
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
    
//     function searchList(page)
//     	$('#pageIndex').val(page);
//     	var begin_date=$('#begin_date').val();
//     	var end_date=$('#end_date').val();
//     	var org_upper=$('#org_upper').val();
//     	var majCode=$('#majCode').val();
//     	var minCode=$('#minCode').val();
//     	var polCode=$('#polCode').val();
//     	var searchCondition=$('#searchCondition').val();
//     	var searchKeyword=$('#searchKeyword').val();
//     	var pageSize=$('#pageSize').val();
//     	var pageIndex=$('#pageIndex').val();
//     	var data = {begin_date:begin_date
//     			,end_date:end_date
//     			,org_upper:org_upper
//     			,majCode:majCode
//     			,minCode:minCode
//     			,polCode:polCode
//     			,searchCondition:searchCondition
//     			,searchKeyword:searchKeyword
//     			,pageSize:pageSize
//     			,pageIndex:pageIndex};
//         $.ajax({
// 			url : '/man/getSanctionList.do',
// 			data : data,
// 			type : 'POST',
// 			dataType : 'json',
// 			error : function(jqXHR, textStatus, errorThrown) {
// 				alert(textStatus + "\r\n" + errorThrown);
// 			},
// 			success : function(data) {
// 				if (data.ISOK) {
// 					$('.list_contents_body').empty().append(data.list_body);
// 					$('#paging').paging({
// 				        current: data.currentpage,
// 				        max: data.totalPage,
// 				        onclick: function (e, page) {
// 				            searchList(page);
// 				        }
// 				    });
// 				} else {
// 					alert(data.MSG);
// 				}

// 			},
// 			beforeSend: function () {
// 				$('#loading').find('img').attr('width', '100px').attr('height', '100px');
//            	 	var padingTop = (Number(($('.list_contents_body').css('height')).replace("px","")) / 2) + 50;
//                 $('#loading').css('position', 'absolute');
//                 $('#loading').css('left', $('.list_contents_body').offset().left + ($('.list_contents_body').css('width').replace("px","") / 2) - 130);
//                 $('#loading').css('top', $('.list_contents_body').offset().top);
              
//                 $('#loading').css('padding-top', 100);
                
//                 $('#loading').show().fadeIn('fast');
//            },
//            complete: function () {
//                $('#loading').fadeOut();
//            }

// 		});
//     }
    
//     $('.list_contents_body').on('click', '.btn_log_view', function(){
//     	//alert($(this).attr('polcd'));
//     	var pcd = $(this).attr('polcd');
//     	var emp = $(this).attr('empno');
//     	var bdt = $(this).attr('bdt');
//     	var pw = window.open('/pol/polLogdetailview.do?polcd='+ $(this).attr('polcd') +'&empno='+ emp + '&begindate='+bdt , 'logview', 'width=1100, height=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no');
//         if (pw != null)
//             pw.focus();
//     });
    
//     $('.list_contents_body').on('click','.btn_appr_info', function(){
//     	var apprid=$(this).attr('apprid');
//     	var pw = window.open('/pol/polapprdetailview.do?ispgm=1&apprid='+apprid, 'apprinfo', 'width=720, height=630, toolbar=no, menubar=no, scrollbars=no, resizable=yes, copyhistory=no');
//         if (pw != null)
//             pw.focus();
//     });
    
//     $('input:checkbox[name=sel_all]').click(function(){
//     	var ischecked = $(this).is(':checked');
//     	$('.list_contents_body').find('tr').each(function(){
//     		$(this).find('input:checkbox[name=sel_box]').prop('checked', ischecked);
//     	});
//     //	
//     });
    
//     $('#orgbrowser').on('click', '.sel_folder', function(){
//     	if(!$(this).hasClass('add_ON')){
//     		var _this = $(this);
//     		var pNode = $(this).find('ul');
//         	var uCode = $(this).attr('uorg_code');
//         	$.ajax({
//     			url : '/pol/setAddOrgSubfolder.do',
//     			data : {uorg_code:uCode},
//     			type : 'POST',
//     			dataType : 'json',
//     			error : function(jqXHR, textStatus, errorThrown) {
//     				alert(textStatus + "\r\n" + errorThrown);
//     			},
//     			success : function(data) {
//     				if (data.ISOK) {
//     					var node_html = data.node_body;
//     					var child_node = $(node_html).appendTo(pNode);
//     					$(pNode).treeview({add:child_node, unique: true, animated: "fast"});
//     					_this.addClass('add_ON');
    					
//     				} else {
//     					alert(data.MSG);
//     					//history.back(-1);
//     				}

//     			},
// 				beforeSend: function () {
// 					$('#loading').find('img').attr('width', '100px').attr('height', '100px');
					
// 					var padingTop = (Number(($('#orgbrowser').css('height')).replace("px","")) / 2) + 25;
// 	                $('#loading').css('position', 'absolute');
// 	                $('#loading').css('left', $('#orgbrowser').offset().left + ($('#orgbrowser').css('width').replace("px","") / 2) - 50);
// 	                $('#loading').css('top', $('#orgbrowser').offset().top);
// 	                $('#loading').css('padding-top', 80);
	                
// 	                $('#loading').show().fadeIn('fast');
// 	            },
// 	            complete: function () {
// 	            	$('#loading').fadeOut();
// 	            }

//     		});
        	
//     	}
    	
//     });
    
//     $(window).bind("load", function() {
    	
//     	//searchList(1);
		
// 	});
    
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
<%-- 		<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %> --%>
<%-- 		<c:import url="/handy/ExanalTableList.do" /> --%>
        </div>
			
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    
    	<div class="subTT"><span>수동 업로드 관리-[신규테이블]</span></div>
        
    	<!-- S :search -->
    	<form name="Form" id="Form" action="<c:url value='/handy/ExanalNewRegist.do'/>" method="post" enctype="multipart/form-data" >
    	
        <div class="sch_block3">
        	<li>
            	<p>엑셀파일을 업로드하세요.</p> 
            </li>
            <br /><br />
            	<p>정책파일선택</p>
				<input name="fileNm" type="file" id="fileNm"/ class="srh">
			</li> 
        	<li>
				<a href="#noscript" onclick="regist_ExcelPol(); return false;" class="btn_black btn_search"><span>엑셀데이터 저장</span></a>
            </li> 
            <li>
            <p>날 짜</p><input type="text" name="begin_date" id="begin_date" readonly value="" style="width:150px;z-indx:100000" class="srh">
            </li>
        	<li>
                <a class="btn_black btn_search"><span>검색</span></a>
            </li>            
        </div>
	         <input name="cmd" type="hidden" value="ExcelPolRegist"/>

        <!-- E :search -->        

        
		<div>
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>테이블명 : <c:out value="${tableName}"/></li>
			</ul>
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>업로드파일명 : <c:out value="${filename.orgfile_name}" /></li>
			</ul>
			<ul>
				<li><img src="/img/dot1.png"/></li>
				<li>레코드수<c:out value="${totCnt}" /></li>
			</ul>
		</div>         
               
        <!-- E :search -->
    	 <div class="sch_view">
            <!-- S :list -->
				<table class="tbl_list1" border="0" cellSpacing="0" cellPadding="0" style="width: 100%;">
				<tbody class='list_contents_body'></tbody>
				</table>
            <!-- E :list -->
        </div>
        
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