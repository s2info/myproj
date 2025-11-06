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
<style>

/***** 모니터링 추가 CSS*****/
/* Start: Recommended Isotope styles */

/**** Isotope Filtering ****/

.isotope-item {
  z-index: 2;
}

.isotope-hidden.isotope-item {
  pointer-events: none;
  z-index: 1;
}

/**** Isotope CSS3 transitions ****/

.isotope,
.isotope .isotope-item {
  -webkit-transition-duration: 0.8s;
     -moz-transition-duration: 0.8s;
      -ms-transition-duration: 0.8s;
       -o-transition-duration: 0.8s;
          transition-duration: 0.8s;
}

.isotope {
  -webkit-transition-property: height, width;
     -moz-transition-property: height, width;
      -ms-transition-property: height, width;
       -o-transition-property: height, width;
          transition-property: height, width;
}

.isotope .isotope-item {
  -webkit-transition-property: -webkit-transform, opacity;
     -moz-transition-property:    -moz-transform, opacity;
      -ms-transition-property:     -ms-transform, opacity;
       -o-transition-property:      -o-transform, opacity;
          transition-property:         transform, opacity;
}

/**** disabling Isotope CSS3 transitions ****/

.isotope.no-transition,
.isotope.no-transition .isotope-item,
.isotope .isotope-item.no-transition {
  -webkit-transition-duration: 0s;
     -moz-transition-duration: 0s;
      -ms-transition-duration: 0s;
       -o-transition-duration: 0s;
          transition-duration: 0s;
}

/***** 모니터링 추가 CSS*****/

</style>
<script type="text/javascript">

$(function () {		
	var $container = $('#container');

    $container.isotope({
    	getSortData: {
            orgname: function ($elem) {
                return $elem.find('.orgname').eq(0).text();
            },
            count: function ($elem) { 
                return parseInt($elem.find('.count').eq(0).text());
            }
        }
    });
    
    $('.btn_orderlist').click(function () {
    	$(this).closest('li').find('a').each(function () {
    		if($(this).hasClass('btn_orderlist')){
       	 		$(this).removeClass('btn_scr5').addClass('btn_scr3');
       	 	}
        });
    	items_sort($(this).attr('is_sort_type'), $(this).attr('is_asc') == 1 ? 2 : 1);
    	$(this).removeClass('btn_scr3').addClass('btn_scr5');
    	$(this).html($(this).attr('is_sort_type') == 'S' ? $(this).attr('is_asc') == 1 ? "<span>건수 ▲</span>" : "<span>건수 ▼</span>" : $(this).attr('is_asc') == 1 ? "<span>조직명 ▲</span>" : "<span>조직명 ▼</span>")
    	$(this).attr('is_asc', $(this).attr('is_asc') == 1 ? 2 : 1);
    	
     });
    
    function set_elemnet(p_element, is_large)
    {
        if(is_large)
        {
        	 p_element.closest('div').children('.B1_a').hide();
             p_element.closest('div').children('.B1_b').show();
        	
        }
        else
        {
        	p_element.closest('div').children('.B1_a').show();
        	p_element.closest('div').children('.B1_b').hide();
        }
    }
    
    $container.delegate('.btn_detail_info', 'click', function () {
    	
    	/*var item = $(this).closest('div').closest('li');
    	item.closest('div').toggleClass('large');
        set_elemnet(item, item.closest('div').hasClass('large'));*/
        var item = $(this).closest('li');
        var is_large = item.attr('is_large');
        if(is_large == 'N'){
        	/*
        	var isRowType = item.attr('isRowType');
            var orgCode = item.attr('orgcode');
            var isSub = item.attr('issub');
            var empno = item.attr('empno');
            var dateValue = item.attr('dateValue');
            var listtype=item.attr('listtype');
        
            var imgorgtitle = item.find('.img_orgtitle').attr('src');
            
        	var data={isRowType:isRowType
        			,orgCode:orgCode
        			,isSub:isSub
        			,beginDate:dateValue
        			,empno:empno
        			,mCode : '<c:out value="${polInfo.majcode}" />'
        			,nCode : '<c:out value="${polInfo.mincode}" />'
        			,pCode : '<c:out value="${polInfo.polcode}" />'
        			,listtype:listtype}
        	$.ajax({
        		 url: "/pol/getSelectResultDetailList.do",
    			data : data,
    			type : 'POST',
    			dataType : 'json',
    			error : function(jqXHR, textStatus, errorThrown) {
    				alert(textStatus + "\r\n" + errorThrown);
    			},
                success: function (data) {
                	if(data.ISOK)
                    {
                		item.closest('div').find('.box_content_list').empty().append(data.box_body);
                		item.closest('div').toggleClass('large');
                        set_elemnet(item, item.closest('div').hasClass('large'));
                        reload_container();
                    }
                    else
                    {
                    	alert(data.MSG);
                    	
                    } 	

                },
				beforeSend: function () {
					item.find('.img_orgtitle').attr('src', '/img/loading3.gif');
					//item.find('.img_orgtitle').css({'width':'40px', 'height':'40px'});
	            },
	            complete: function () {
	            	item.find('.img_orgtitle').attr('src', imgorgtitle);
	            }

            });
        	*/
        	show_large_info(item);
        	
        }else{
        	item.closest('div').toggleClass('large');
            set_elemnet(item, item.closest('div').hasClass('large'));
            reload_container();
        }
       
    });
    
    function show_large_info(item){
    	var isRowType = item.attr('isRowType');
        var orgCode = item.attr('orgcode');
        var isSub = item.attr('issub');
        var empno = item.attr('empno');
        var dateValue = item.attr('dateValue');
        var listtype=item.attr('listtype');
    	
       // var imgorgtitle = item.find('.img_orgtitle').attr('src');
        
    	var data={isRowType:isRowType
    			,orgCode:orgCode
    			,isSub:isSub
    			,beginDate:dateValue
    			,empno:empno
    			,mCode : $('#mCode').val()
    			,nCode : $('#nCode').val()
    			,pCode : $('#pCode').val()
    			,listtype:listtype
    			,buseoType:$('#buseoType').val()}
    	$.ajax({
    		 url: "/pol/getSelectResultDetailList.do",
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
            success: function (data) {
            	if(data.ISOK)
                {
            		item.closest('div').find('.box_content_list').empty().append(data.box_body);
            		item.closest('div').toggleClass('large');
                    set_elemnet(item, item.closest('div').hasClass('large'));
                    reload_container();
                }
                else
                {
                	alert(data.MSG);
                	
                } 	

            },
			beforeSend: function () {
				item.find('.img_orgtitle').attr('src', listtype == 'O' ? '/img/loading-r.gif' : '/img/loading-b.gif');
            },
            complete: function () {
            	item.find('.img_orgtitle').attr('src', listtype == 'O' ? '/img/icon_blue_2.png' : '/img/icon_blue_1.png');
            }

        });
    }
    
    function reload_container(){
    	 $container.isotope('reLayout');
    }
    
    $('.btn_stat_list').click(function () {
    	$(this).closest('li').find('a').each(function () {
       	 	if($(this).hasClass('btn_stat_list')){
       	 		$(this).removeClass('btn_scr4').addClass('btn_scr3');
       	 	}
        });
    	$(this).removeClass('btn_scr3').addClass('btn_scr4');

    	var q_type = $(this).attr('s_val');
        var f_val = '*';
        if (q_type.length != 0) 
        {
            f_val = '.STATUS_' + q_type;
        }
        $container.isotope({ filter: f_val });
       
    });
    
    $('.btn_search').click(function(){
    	
    	search_list();
    	
    });
    
    
    
    var ckLarge = 0;
    $('.btn_all_detail_view').click(function () {
    	
    	$container.find('.item').each(function () {
    		
    		var item = $(this).find('li').first();
            var is_large = item.attr('is_large');
    		alert(is_large);
    		
            if (ckLarge == 0) {
                if (!$(this).hasClass('large'))
                {
                    $(this).toggleClass('large');
                   
                }
            }
            else
            {
                if ($(this).hasClass('large')) {
                    $(this).toggleClass('large');
                    
                }
            }
           
            set_elemnet($(this), $(this).hasClass('large'));
            $container.isotope('reLayout');
        });
        
        
        if(ckLarge ==0)
        {
            
            $(this).empty().append('<span><img src="/img/icon_arw3.jpg" alt="닫기"> 닫기</span>');
            ckLarge = 1;
        }
        else
        {
        	$(this).empty().append('<span><img src="/img/icon_arw2.jpg" alt="펼침"> 펼침</span>');
            ckLarge = 0;
        }
      
    });
    
    /* 미사용
    function set_selectboxClear(idx)
    {
    	for(var i=idx;i<=4;i++){
    		$('#org_'+i+' > option').each(function(){
    			$(this).remove();
    		});
    		$('#org_'+i).append("<option value=''>"+ convertSelectedTitle(i) +"</option>");
    	}
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
    */
    /*미사용
    $('.selected_org').change(function(){
    	//alert($(this).val() + "][]"+ $(this).attr('norg'));
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
                	alert(data.MSG);
                	//history.back(-1);
                } 	

            }

        });
    	
    });
    */
    $('#search_date, #end_date').datepicker({
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
    
    
    function items_sort()
    {
    	var sort_type = arguments[0];
    	var is_asc = arguments[1];
    	$container.isotope({
       	 sortBy : sort_type == 'S' ? 'count' : 'orgname',
       	 sortAscending : is_asc != "2" ? true : false
        });
    	
    }
    
    
    $container.on('click', '.btn_detail_user_info', function () {
    	
    	var st = $(this).attr('st');
    	var seq = $(this).attr('sseq');
    	var issub=$(this).attr('issub');
    	
    	$('#stval').val(st);
    	$('#seqval').val(seq);
    	$('#issubval').val(issub);
    	$('#pageIndex').val(1);
    	detail_user_info_list(st, seq, issub);
    });
    
    function searchDetail_List(st, seq, page){
    	
    	$('#pageIndex').val(page);
		var data ={
    			
    			begin_date : $('#search_date').val(),
    			
    			mCode : $('#mCode').val(),
    			nCode : $('#nCode').val(),
    			pCode : $('#pCode').val(),
    			pageIndex : $('#pageIndex').val(),
    			buseoType : $('#buseoType').val(),
    			dtType :  st,
    			dtSeq :  seq,
    			issub : $('#issubval').val(),
    			searchCondition : $('#searchPolCondition').val(),
    			searchKeyword : $('#searchPolKeyword').val().replace(" ",""),
    			auth : <c:out value="${auth}"/>
        	};
		    
		$.ajax({
				url : '/pol/getploUserResultList.do',
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
						
						$('.list_contents_body').empty().append(data.body);
						$('.mBody').hide();
				    	$('.lBody').show();
				    	$('#paging').paging({
					        current: data.currentpage,
					        max: data.totalPage,
					        onclick: function (e, page) {
					            searchDetail_List(st, seq, page);
					        }
					    });
					} else {
						alert(data.MSG);
					}

				},
				beforeSend: function () {
					var padingTop = (Number(($('.lBody').css('height')).replace("px","")) / 2) - 150;
	                 $('#loading').css('position', 'absolute');
	                 $('#loading').css('left', $('.lBody').offset().left + ($('.lBody').css('width').replace("px","") / 2) - 130);
	                 $('#loading').css('top', $('.lBody').offset().top);
	                 $('#loading').css('padding-top', padingTop);
	                 $('#loading').show().fadeIn('fast');
	           },
	           complete: function () {
	               $('#loading').fadeOut();
	           }

		});
    }
    
    function detail_user_info_list(st, seq, issub)
    {
    	$('#stval').val(st);
    	$('#seqval').val(seq);
    	
    	var data ={
    			
    			begin_date : $('#search_date').val(),
    			
    			mCode : $('#mCode').val(),
    			nCode : $('#nCode').val(),
    			pCode : $('#pCode').val(),
    			pageIndex : $('#pageIndex').val(),
    			buseoType : $('#buseoType').val(),
    			dtType :  st,
    			dtSeq :  seq,
    			issub : issub,
    			searchCondition : $('#searchPolCondition').val(),
    			searchKeyword : $('#searchPolKeyword').val().replace(" ",""),
    			auth : <c:out value="${auth}"/>
        	};
    	$('.list_contents_body').empty().append('<tr style="font-weight:bold;height:300px;"><td colspan="20">조회 처리중 입니다.</td></tr>');
    	$('.mBody').hide();
    	$('.lBody').show();    
		$.ajax({
				url : '/pol/getploUserResultList.do',
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
						$('.list_contents_body').empty().append(data.body);
						$('.mBody').hide();
				    	$('.lBody').show();
				    	$('#paging').paging({
					        current: data.currentpage,
					        max: data.totalPage,
					        onclick: function (e, page) {
					            searchDetail_List(st, seq, page);
					        }
					    });
					} else {
						alert(data.MSG);
					}

				},
				beforeSend: function () {
					
					var padingTop = (Number(($('.lBody').css('height')).replace("px","")) / 2) - 150;
	                 $('#loading').css('position', 'absolute');
	                 $('#loading').css('left', $('.lBody').offset().left + ($('.lBody').css('width').replace("px","") / 2) - 130);
	                 $('#loading').css('top', $('.lBody').offset().top);
	                 $('#loading').css('padding-top', padingTop);
	                 $('#loading').show().fadeIn('fast');
	           },
	           complete: function () {
	               $('#loading').fadeOut();
	           }

		});
    	
    }
    
    $('.btn_close_list').click(function(){
    	$('.lBody').hide();
    	$('.mBody').show();
    	
    });
    
    $('#orgbrowser').on('click', '.sel_text', function(){
    	
    	var orgcode = $(this).attr('orgcode');
    	var stype = $(this).attr('stype');
    	$('#sorgcode').val(orgcode);
    	$('#sorgtype').val(stype);
    	$('#orgbrowser').find('a').each(function(){
    		if($(this).hasClass('ON')){
    			$(this).removeClass('ON');
    		}
    	});
    	$(this).find('a').addClass('ON');
    	
    	search_list();
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
    				}

    			},
				beforeSend: function () {
					$('#loading').find('img').attr('width', '100px').attr('height', '100px');
					
					var padingTop = (Number(($('#orgbrowser').css('height')).replace("px","")) / 2) + 25;
	                $('#loading').css('position', 'absolute');
	                $('#loading').css('left', $('#orgbrowser').offset().left + ($('#orgbrowser').css('width').replace("px","") / 2) - 50);
	                $('#loading').css('top', $('#orgbrowser').offset().top);
	                $('#loading').css('padding-top', 150);
	                $('#loading').show().fadeIn('fast');
	            },
	            complete: function () {
	            	$('#loading').fadeOut();
	            }

    		});
        	
    	}
    	
    });
    
    $('#searchKeyword').keyup(function(e){
		if (e.keyCode == '13'){
			search_list();
		}
	});
    
    function search_list()
    {
    	<c:choose><c:when test="${auth == 3 }">	
    		showResultList();
    	</c:when>
    	<c:otherwise>
    		showResultBox();
    	</c:otherwise></c:choose>
    	
	}
    
    function showResultList(){
    	var st = 'U'
    	var seq = '<c:if test="${auth == 3}" ><c:out value="${emp}" /></c:if>';
    	$('#stval').val(st);
    	$('#seqval').val(seq);
    	$('#pageIndex').val(1);
    	detail_user_info_list(st, seq, 'N');
    }
    
    function showResultBox(){
    	$('.lBody').hide();
    	$('.mBody').show();
    	/*검색 조건 정책, 이름, 사번 추가해야함......*/
    	var data ={
    			//org_bumon : $('#org_1').val(),
    			//org_bonbu :  $('#org_2').val(),
    			//org_damdang : $('#org_3').val(),
    			//org_team : $('#org_4').val(),
    			searchType : $('#sorgtype').val(),
    			org_upper : $('#sorgcode').val(),
    			begin_date : $('#search_date').val(),
    			buseoType : $('#buseoType').val(),
    			mCode : $('#mCode').val(),
    			nCode : $('#nCode').val(),
    			pCode : $('#pCode').val(),
    			
    			searchCondition : $('#searchPolCondition').val(),
    			searchKeyword : $('#searchPolKeyword').val().replace(" ",""),
    			isexpn_zero : $('#isexpn_zero').is(':checked') ? 'Y' : 'N'
        };
    	var $selitem =  $container.data('isotope').$allAtoms;
		$.ajax({
				url : '/pol/getSelectResultList.do',
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
						var $elem = $(data.items);
						$container.isotope('insert', $elem).isotope('remove', $selitem);
						$('.btn_all_detail_view').empty().append('<span><img src="/img/icon_arw2.jpg" alt="펼침"> 펼침</span>');
					} else {
						alert(data.MSG);
					}

				},
				beforeSend: function () {
					$('#orgbrowser').prop('disabled', true);
					
					//mask
					// $('#mask').css('position', 'absolute');
					// $('#mask').css('left', $('#orgbrowser').offset().left);
					// $('#mask').css('top', $('#orgbrowser').offset().top);
					// $('#mask').css('width', $('#orgbrowser').css('width'));
					// $('#mask').css('height', $('#orgbrowser').css('height'));
					// $('#mask').show();
					 
					 $('#loading').find('img').attr('width', '100px').attr('height', '100px');
	            	 var padingTop = (Number(($('.contents_body').css('height')).replace("px","")) / 2) + 50;
	                 $('#loading').css('position', 'absolute');
	                 $('#loading').css('left', $('.contents_body').offset().left + ($('.contents_body').css('width').replace("px","") / 2) - 130);
	                 $('#loading').css('top', $('.contents_body').offset().top);
	                 $('#loading').css('padding-top', 100);
	                 
	                 $('#loading').show().fadeIn('fast');
	            },
	            complete: function () {
	            	$('#loading').fadeOut();
	            	$('#orgbrowser').prop('disabled', false);
	              //  $('#mask').hide();
	               
	            }

			});
    }
    
    
    $('.list_contents_body').on('click', '.btn_log_view', function(){
    	//alert($(this).attr('polcd'));
    	var pcd = $(this).attr('polcd');
    	var emp = $(this).attr('empno');
    	var bdt = $(this).attr('bdt');
    	var mac = $(this).attr('mac');
    	var pw = window.open('/pol/polLogdetailview.do?polcd='+ $(this).attr('polcd') +'&empno='+ emp + '&begindate='+ bdt +'&mac='+ mac , 'logview', 'width=1300, height=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no');
        if (pw != null)
            pw.focus();
    });
    
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
            	searchDetail_List($('#stval').val(), $('#seqval').val(), $('#pageIndex').val());
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
	
	$('.btn_appl_order').click(function(){
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
	        	//소명
				var apprcode = $(this).closest('tr').find('td').eq(8).find('select');
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
                     $('.DialogBox').dialog({ width: 600, height: pheight });
                     $('.DialogBox').dialog('open');
                     $('.DialogBox').data('is_save', 'N');
                     $('.DialogBox').dialog().parent().draggable({ cancel: '', handle: '.WP_tit' });
				} else {
					alert(data.MSG);
				}

			}

		});
		
	}
    
    $('.list_contents_body').on('click','.btn_appr_info', function(){
    	var apprid=$(this).attr('apprid');
    	var pw = window.open('/pol/polapprdetailview.do?ispgm=1&apprid='+apprid, 'apprinfo', 'width=628, height=575, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no');
        if (pw != null)
            pw.focus();
    });
    
    $('input:checkbox[name=sel_all]').click(function(){
    	var ischecked = $(this).is(':checked');
    	$('.list_contents_body').find('tr').each(function(){
    		$(this).find('input:checkbox[name=sel_box]').prop('checked', ischecked);
    	});
    });
    
    $('.btn_export_excel').click(function(){
    	
    	var params = [];
        params[0] = JSON.stringify({"inputname": 'begin_date', "inputvalue": $('#search_date').val()});
        params[1] = JSON.stringify({"inputname": 'mCode', "inputvalue": $('#mCode').val()});
        params[2] = JSON.stringify({"inputname": 'nCode', "inputvalue": $('#nCode').val()});
        params[3] = JSON.stringify({"inputname": 'pCode', "inputvalue": $('#pCode').val()});
        params[4] = JSON.stringify({"inputname": 'pageIndex', "inputvalue": $('#pageIndex').val()});
        params[5] = JSON.stringify({"inputname": 'dtType', "inputvalue": $('#stval').val()});
        params[6] = JSON.stringify({"inputname": 'dtSeq', "inputvalue":  $('#seqval').val()});
        params[7] = JSON.stringify({"inputname": 'searchCondition', "inputvalue": $('#searchPolCondition').val()});
        params[8] = JSON.stringify({"inputname": 'searchKeyword', "inputvalue": $('#searchPolKeyword').val()});
        params[9] = JSON.stringify({"inputname": 'auth', "inputvalue": '<c:out value="${auth}"/>'});
        params[9] = JSON.stringify({"inputname": 'buseoType', "inputvalue": $('#buseoType').val()});
        params[10] = JSON.stringify({"inputname": 'issub', "inputvalue":  $('#issubval').val()});
        gopage('/pol/exportexcel.do', params, 'searchVO');
    	
    });

	$(window).bind("load", function() {
		search_list();
		items_sort('S', 2);
	});
	
	
	//$('.list_contents_body').tooltip();
    
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
	<%@ include file="/WEB-INF/jsp/cmm/polLeftMenu.jsp" %>
	</div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" name="listForm" method="post">
    	<!-- S :search -->
    	<div class="cntNavi">
        	<span><img src="/img/icon_arow1.png" /> 정책조회현황</span> : ${polStatString }
        </div>
        <div class="sch_block3">
        	<li>
            	<p>날 짜</p>
            	<input type="text" name="search_date" id="search_date" readonly value="<c:out value="${getdate}" />" style="width:100px;z-indx:100000" class="srh"/>
            	<c:choose><c:when test="${PolicySearchVO.buseoType == 'Y' }">	
					<input type="hidden" name="searchPolKeyword" id="searchPolKeyword" value="" />
				</c:when>
				<c:otherwise>
					<p>검색어</p> 
	            	<select  name="searchPolCondition" id="searchPolCondition" style="width:100px" <c:if test="${auth == 3}" >disabled</c:if>>
	                	<option value="1" <c:if test="${PolicySearchVO.searchCondition == '1'}" >selected</c:if>>ID(사번)</option>
						<option value="2" <c:if test="${PolicySearchVO.searchCondition == '2'}" >selected</c:if>>성명</option> 
					</select>
                	<input type="text" name="searchPolKeyword" id="searchPolKeyword" style="width:200px" <c:if test="${auth == 3}" >disabled</c:if> value="<c:out value="${emp}" />" class="srh">
				</c:otherwise></c:choose>	
            	<a class="btn_black btn_search"><span>검색</span></a> 
                <p>건수 0건 제외 여부</p><input type='checkbox' name='isexpn_zero' id='isexpn_zero' /> 
            </li> 
        </div>
    <c:if test="${polNoticeString != '' }">
		<div class="sch_block2" style="padding-left:25px;background:#f9f9f9;">
	        <span style="font-size:12px;line-height:18px; color:#2479C0;">${polNoticeString }</span>
	    </div>
	</c:if>
        <div class="mBody">
	        <div class="sch_block2">
	        	<li><p>정렬구분</p><a class="btn_scr5 btn_orderlist" is_sort_type="S" is_asc="2"><span> 건수 ▲ </span></a>&nbsp;
	        					   <a class="btn_scr3 btn_orderlist" is_sort_type="O" is_asc="1"><span>조직명 ▼</span></a></li>
	        	<li><p>상대구분</p> <a class="btn_scr4 btn_stat_list" s_val=""><span>전체</span></a>&nbsp;
	        						<a class="btn_scr3 btn_stat_list" s_val="A"><span>조직</span></a>&nbsp;
	        						<a class="btn_scr3 btn_stat_list" s_val="B"><span>직원</span></a></li> 
	        	<li><p>색 구분</p> 
	        		<a class="btn_scr4"><span>직원</span></a>&nbsp;
	        		<a class="btn_scr5"><span>조직</span></a>&nbsp;
	        		<!-- <a class="btn_scr6 btn_scr6Sel"><span>정책 비대상</span></a>&nbsp; -->
	        		<a class="btn_scr7"><span>일반협력사</span></a>&nbsp;
	        		<a class="btn_scr8 btn_scr8Sel"><span>대리점</span></a>&nbsp;
	        		<a class="btn_scr9"><span>상담사</span></a></li>
	           <!--  <li><p>전체상세</p> <a class="btn_scr3 btn_all_detail_view"><span><img src="/img/icon_arw2.jpg" alt="펼침"> 펼침</span></a></li> --> 
	        </div>
	        <!-- E :search -->
	    	<div class="sch_view contents_body" id="container">
	            
	        </div>
       </div>
       <div class="lBody" style='display:none;'>
       		<div class="sch_view">
	            <!-- S :list -->
	            <table cellpadding="0" cellspacing="0" class="tbl_list1">
	                <caption>TBL</caption>
					<colgroup>
					<c:choose><c:when test="${PolicySearchVO.buseoType == 'Y' }">	
						<col style="width:15%">
						<col style="width:12%">
						<col style="width:*">
						<col style="width:12%">
						<col style="width:12%">
						<col style="width:12%">
						<col style="width:12%">
					</c:when>
					<c:otherwise>
						<col style="width:15%">
						<col style="width:10%">
						<col style="width:8%">
						<col style="width:*">
						<col style="width:10%">
						<col style="width:8%">
						<col style="width:8%">
						<col style="width:8%">
						<col style="width:10%">
					</c:otherwise></c:choose>
						
					</colgroup>
					<tr>
					<c:choose><c:when test="${PolicySearchVO.buseoType == 'Y' }">	
						<th>조직명</th>
						<th>조직장명</th>
						<th>지수화정책</th>
						<th>조직 하위 총건수</th>
						<th>총점수</th>
						<th>평균점수</th>
						<th>진단내역</th>
					</c:when>
					<c:otherwise>
						<th>조직명</th>
						<th>이름</th>
						<th>이벤트날짜</th>
						<th>지수화정책</th>
						<th>건수</th>
						<th>점수</th>
						<c:choose><c:when test="${auth == 1 }">	
							<th>진단내역</th>
							<!-- <th><input type='checkbox' name='sel_all' /></th>
							<th>소명요청<br>소명조치</th> -->
						</c:when>
						<c:otherwise>
							<th>진단상태</th>
							<!-- <th><input type='checkbox' name='sel_all' /></th>
							<th>PC지키미<br />조치</th> -->
						</c:otherwise></c:choose>
					</c:otherwise></c:choose>	
					</tr>
					<tbody class='list_contents_body'>
					
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
	            <c:if test="${auth == 1 || auth == 2}" >
	            	<%-- <c:if test="${PolicySearchVO.buseoType == 'N' }">
	            	<a class="btn_black btn_appl_order" mode='A'><span>선택항목 소명 요청</span></a>
	            	</c:if> --%>
	            	<a class='btn_black btn_close_list' ><span>이전</span></a>
	             </c:if>	
	            	
	            </div>
	           
	        </div>
       </div>
       <input type="hidden" name="emp_no" value="" />
		<input type="hidden" name="dtType" id="dtType" value="" />
		<input type="hidden" name="dtSeq" id="dtSeq" value="" />
		<input type="hidden" name="stval" id="stval" value="" />
		<input type="hidden" name="seqval" id="seqval" value="" />
		<input type="hidden" name="issubval" id="issubval" value="" />
		<input type="hidden" name="sorgcode" id="sorgcode" value="<c:out value="${orgroot}"/>"/>
		<input type="hidden" name="sorgtype" id="sorgtype" value="<c:out value="${orgtype }"/>"/>
		<input type="hidden" name="pageIndex" id="pageIndex" value="1" />
		<input type="hidden" name="mCode" id="mCode" value="${PolicySearchVO.majCode }" />
		<input type="hidden" name="nCode" id="nCode" value="${PolicySearchVO.minCode }" />
		<input type="hidden" name="pCode" id="pCode" value="${PolicySearchVO.polCode }" />
		<input type="hidden" name="lType" id="lType" value="${PolicySearchVO.loginType }" />
		<input type="hidden" name="buseoType" id="buseoType" value="${PolicySearchVO.buseoType }" />
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