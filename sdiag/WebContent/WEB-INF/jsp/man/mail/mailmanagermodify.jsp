<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script src="/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<script type="text/javaScript" language="javascript">
$(function () {		
	/*$('.btn_policy_save').click(function(){
			
		
		
		if( confirm(" 저장 하시겠습니까?" ) ){
			document.listForm.action = "/man/policysave.do";
			document.listForm.submit();
		}
	});*/
	
	/* $('.popup_dialogbox').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        show: "fade",
        hide: "fade",
        close: function () { 
            $(this).dialog('close'); 
        },
        open: function () {
            var $ddata = $(".popup_dialogbox");
            $('.ui-widget-overlay').bind('click', function () {
                $ddata.dialog('close');
            });
        }
    });
	$('.popup_dialogbox').on('click', '.btn_subdialogbox_close', function () {
		$('.popup_dialogbox').dialog('close');
	});
	 */
	$('.btn_cancel').click(function(){
		goList();
	});
	
	function goList(){
		document.listForm.action = "/man/mailmanagerlist.do";
		document.listForm.submit();
	}
	
	
	
	 $('.btn_mailsendinfo_save').click(function(){
		 var gubun = $('input:radio[name=gubun]:checked').val();
		 var polSelectList = [];
		 $("input:checkbox[name=policyid]:checked").each(function(){
			 polSelectList.push($(this).val());
		 });
		 var targetCodeList = [];
		 $('.target_list_body').find('tr').each(function(){
			 if($(this).attr('is_all_send') == 'N'){
				var targetCode = $(this).find('td').eq(0).text();
				var targetType = $(this).find('td').eq(1).text();
				var targetNm = $(this).find('td').eq(3).text();
				targetCodeList.push(targetCode + "|" +targetType + "|" +targetNm);
			 }
		 })
		
		 var send_type = $('input:radio[name=send_type]:checked').val();
		 var pol_type = $('input:radio[name=pol_type]:checked').val();
		 var send_date = $('#send_date').val();
		 var send_hour_reg = $('select[name=send_hour_reg]').val();
		 var send_minutes_reg = $('select[name=send_minutes_reg]').val();
		 var send_day_type = $('select[name=send_day_type]').val();
		 var send_day_option = $('select[name=send_day_option]').val();
		 var send_day = $('select[name=send_day]').val();
		 var send_hour_rep = $('select[name=send_hour_rep]').val();
		 var send_minutes_rep = $('select[name=send_minutes_rep]').val();
		 var schedule_expression = $('#schedule_expression').val();
		 var subject = $('#subject').val();
		 var is_used = $('input:radio[name=is_used]:checked').val();
		 var contents = CKEDITOR.instances['editor'].getData();
		 var contents_top = CKEDITOR.instances['editor1'].getData();
		 var contents_bottom = CKEDITOR.instances['editor2'].getData();
		 var mail_seq = $('#mail_seq').val();
		 var is_cap_send ="N";
		 
		 if(gubun == "C"){
			is_cap_send = $(':input:radio[name=is_cap]').is(':checked') ? 'Y' : 'N';
		 }else{
			 is_cap_send = $(':input:checkbox[name=cap_send]').is(':checked') ? 'Y' : 'N';
		 }
		
		
		 
		 if(gubun == "K" || gubun == "C"){
			 if(gubun == "K")
				 pol_type ="07";
			 else
				 pol_type = "08";
		 }
		 
		 
		 if(send_type == 'REG'){
			 if(send_date == ''){
				 alert('메일 발송일을 입력하여 주세요.');
				 return false;
			 }
		 }else if(send_type == "REP"){
			 
		 }else{
			 if(schedule_expression == ''){
				 alert('메일 발송 설정정보 입력하여 주세요.');
				 return false;
			 }
		 }
		 
		 if(gubun == 'P'){
			 if($('input:radio[name=pol_type]:checked').length <= 0){
				 alert('리포트 유형을 선택하여 주세요.');
				 return false;
			 }
			 if(polSelectList.length <= 0 && pol_type =='04'){
				 alert('메일 발송을 위한 정책을 선택하여 주세요.');
				 return false;
			 }
			 if(subject == ''){
				 alert('메일 제목을 입력하여 주세요.');
				 return false;
			 }
			 if(contents_top == ''){
				 alert('상단 내용을 입력하여 주세요.');
				 return false;
			 }
			 if(contents_bottom == ''){
				 alert('하단 내용을 입력하여 주세요.');
				 return false;
			 }
		 }else if(gubun == 'N'){
			 if(subject == ''){
				 alert('메일 제목을 입력하여 주세요.');
				 return false;
			 }
			 if(contents == ''){
				 alert('공지내용을 입력하여 주세요.');
				 return false;
			 }
		 }else{
			 if(subject == ''){
				 alert('메일 제목을 입력하여 주세요.');
				 return false;
			 }
			 if(contents_top == ''){
				 alert('상단 내용을 입력하여 주세요.');
				 return false;
			 }
			 if(contents_bottom == ''){
				 alert('하단 내용을 입력하여 주세요.');
				 return false;
			 }
		 }
		 
		 var data = {
				  gubun : gubun
				 ,polSelectList : polSelectList.toString()
				 ,targetCodeList : targetCodeList.toString()
				 ,send_type : send_type
				 ,pol_type : pol_type
				 ,send_date : send_date
				 ,send_hour_reg : send_hour_reg
				 ,send_minutes_reg : send_minutes_reg
				 ,send_day_type : send_day_type
				 ,send_day_option : send_day_option
				 ,send_day : send_day
				 ,send_hour_rep : send_hour_rep
				 ,send_minutes_rep : send_minutes_rep
				 ,schedule_expression : schedule_expression
				 ,subject : subject
				 ,is_used : is_used
				 ,contents : contents
				 ,contents_top : contents_top
				 ,contents_bottom : contents_bottom
				 ,mail_seq : mail_seq
				 ,is_cap_send : is_cap_send
		 }
		 if(targetCodeList.length <= 0){
			 if(is_cap_send == 'N'){
				 if(!confirm('메일 발송 대상자가 없을 경우 전사 발송 됩니다.\r\n\r\n전사 발송 하시겠습니까?')){		
				 	return false;
				}
			 }
		 }
		 
		// alert(JSON.stringify(data));
		 if(!confirm('메일 발송 정보를  저장 하시겠습니까?')){		
				return false;		
			}
		 $.ajax({				
				data: data,				
				url: '/man/setmailmanagerinfo.do',				
				type: 'POST',				
				dataType: 'json',				
				error: function (jqXHR, textStatus, errorThrown) {					
					if(jqXHR.status == 401){						
						alert('인증정보가 만료되었습니다.');						
						location.href='/';					
					}else{						
						alert(textStatus + '\r\n' + errorThrown);					
					}				
				},				
				success: function (data) {					
					if (data.ISOK) {
						$('#mail_seq').val(data.mailSeq);
						//goList();	
						alert('저장 완료 되었습니다.')			
					}else{alert(data.MSG); }				
				}			
			});	
	 });
	 
	CKEDITOR.replace('editor', {
		filebrowserImageUploadUrl:'/man/editorImageUpload.do',
		enterMode: '2',
		shiftEnterMode: '3',
		uiColor: '#ffffff',
		height: 400,
		toolbar: null,
		toolbarGroups: [
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'clipboard', groups: ['clipboard', 'undo'] },
		{ name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },
		{ name: 'forms', groups: ['forms'] },
		'/',
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
		{ name: 'links', groups: ['links'] },
		{ name: 'insert', groups: ['insert'] },
		'/',
		{ name: 'styles', groups: ['styles'] },
		{ name: 'colors', groups: ['colors'] },
		{ name: 'tools', groups: ['tools'] },
		{ name: 'others', groups: ['others'] },
		{ name: 'about', groups: ['about'] }
		],
		removeButtons: 'Save,NewPage,Preview,Print,Templates,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,CreateDiv,Language,Anchor,Flash,Smiley,PageBreak,Iframe,ShowBlocks,About',
		});
	CKEDITOR.replace('editor1', {
		filebrowserImageUploadUrl:'/man/editorImageUpload.do',
		enterMode: '2',
		shiftEnterMode: '3',
		uiColor: '#ffffff',
		height: 250,
		toolbar: null,
		toolbarGroups: [
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'clipboard', groups: ['clipboard', 'undo'] },
		{ name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },
		{ name: 'forms', groups: ['forms'] },
		'/',
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
		{ name: 'links', groups: ['links'] },
		{ name: 'insert', groups: ['insert'] },
		'/',
		{ name: 'styles', groups: ['styles'] },
		{ name: 'colors', groups: ['colors'] },
		{ name: 'tools', groups: ['tools'] },
		{ name: 'others', groups: ['others'] },
		{ name: 'about', groups: ['about'] }
		],
		removeButtons: 'Save,NewPage,Preview,Print,Templates,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,CreateDiv,Language,Anchor,Flash,Smiley,PageBreak,Iframe,ShowBlocks,About',
		});
	CKEDITOR.replace('editor2', {
		filebrowserImageUploadUrl:'/man/editorImageUpload.do',
		enterMode: '2',
		shiftEnterMode: '3',
		uiColor: '#ffffff',
		height: 250,
		toolbar: null,
		toolbarGroups: [
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'clipboard', groups: ['clipboard', 'undo'] },
		{ name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },
		{ name: 'forms', groups: ['forms'] },
		'/',
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
		{ name: 'links', groups: ['links'] },
		{ name: 'insert', groups: ['insert'] },
		'/',
		{ name: 'styles', groups: ['styles'] },
		{ name: 'colors', groups: ['colors'] },
		{ name: 'tools', groups: ['tools'] },
		{ name: 'others', groups: ['others'] },
		{ name: 'about', groups: ['about'] }
		],
		removeButtons: 'Save,NewPage,Preview,Print,Templates,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,CreateDiv,Language,Anchor,Flash,Smiley,PageBreak,Iframe,ShowBlocks,About',
		});
	CKEDITOR.on('dialogDefinition', function(ev){
		var dialogName=ev.data.name;
		var dialog = ev.data.definition.dialog;
		var dialogDefinition = ev.data.definition;
		if(dialogName=='image'){
			dialog.on('show', function(obj){
				this.selectPage('Upload');
			});
		}
		dialogDefinition.removeContents('advanced');
		dialogDefinition.removeContents('Link');
		
	});
	 
	$('#send_date').datepicker({
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
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
	
   /*  $(".btn_add_info").click(function(){
    	var pw = window.open('/group/popOrgInfoSearch.do?reqCode=1','orgInfo_popup','width=1000, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
	}); */
	$('input:radio[name=gubun]').click(function(){
		if($(this).val() == 'N'){
			$('.contents_policy').hide();
			$('.Cap_Row').hide();
			$('.contents_noti').show();
			CKEDITOR.instances['editor1'].setData('');
			CKEDITOR.instances['editor2'].setData('');
			$('.Pol_Row').hide();
			$('.Pol_Type_Row').hide();
			$('.target_row').show();
			$('.File_Row').hide();
		}else if($(this).val() == 'P'){
			$('.Pol_Type_Row').show();
			if($('input:radio[name=pol_type]:checked').val() == "04"){
				$('.Pol_Row').show();
			}else{
				$('.Pol_Row').hide();
			}
			$('.target_row').show();
			$('.Cap_Row').hide();
			$('.contents_policy').show();
			$('.contents_noti').hide();
			$('.File_Row').hide();
			CKEDITOR.instances['editor'].setData('');
		}else{
			
			$('.Pol_Row').hide();
			$('.Pol_Type_Row').hide();
			$('.target_row').hide();
			$('.contents_policy').show();
			$('.File_Row').show();
			$('.contents_noti').hide();
			var type ="";
			
			if($(this).val() == 'C'){
				$('.Cap_Row').show();
				type ="08";
			}else{
				$('.Cap_Row').hide();
				type ="07";
			}
			CKEDITOR.instances['editor'].setData('');
			
			
			var strAdd = "<a href='/man/fileDown.do?type="+type+"' target='_self'> 첨부파일 다운로드</a>";
			//alert(strAdd);
			$('.File_Down').find('a').remove();
			$('.File_Down').append(strAdd);
			
		}
	});
	
	$('input:radio[name=pol_type]').click(function(){
		if($(this).val() == '04'){
			$('.Pol_Row').show();
		}else{
			$('.Pol_Row').hide();
		}
		
	});
	$('input:radio[name=temp_contents]').click(function(){
		
		var pol_type=  $(this).val();
		var data = {target_type : pol_type};
		$.ajax({				
			data: data,				
			url: '/man/getmailtempstr.do',				
			type: 'POST',				
			dataType: 'json',				
			error: function (jqXHR, textStatus, errorThrown) {					
				if(jqXHR.status == 401){						
					alert('인증정보가 만료되었습니다.');						
					location.href='/';					
				}else{						
					alert(textStatus + '\r\n' + errorThrown);					
				}				
			},				
			success: function (data) {					
				if (data.ISOK) {
					CKEDITOR.instances['editor1'].setData(data.contentTemp);	
				}else{alert(data.MSG); }				
			}			
		});	
	})
	
    $('input:radio[name=send_type]').click(function(){
		if($(this).val()=='REG'){
			$('.Reg_Row').show();
			$('.Rep_Row, .Mul_Row').hide();			
		}else if($(this).val() == 'REP'){
			$('.Rep_Row').show();
			$('.Reg_Row, .Mul_Row').hide();
		}else{
			$('.Mul_Row').show();
			$('.Rep_Row, .Reg_row').hide();
		}
	});
    $('input:checkbox[name=checked_all]').click(function(){
    	var ischecked = $(this).is(':checked');
    	$(this).closest('td').find('input:checkbox[name=policyid]').each(function(){
    		$(this).prop('checked', ischecked);
    	});
    });
	var week_val = [["MON", "월요일"], ["TUE", "화요일"], ["WED", "수요일"], ["THU", "목요일"], ["FRI", "금요일"], ["SAT", "토요일"], ["SUN", "일요일"]];
	$('#send_day_type').change(function(){
		$('#send_day').find('option').each(function(){
			$(this).remove();
		});
		
		if($(this).val() == 'WEEK'){
			$('#send_day_option').hide();
			for(var i=0; i < week_val.length ;i++){
				var option = $("<option value="+ week_val[i][0] +">" + week_val[i][1] + "</option>");
				$('#send_day').append(option);
			}
		}else{
			//send_day
			$('#send_day_option').show();
			for(var i=1; i < 31 ;i++){
				var option = $("<option value="+ i +">" + i + "</option>");
				$('#send_day').append(option);
			}
			//$('#send_day').append("<option value='L'>마지막날짜</option>");
		}
	});
	$('#send_day_option').change(function(){
		$('#send_day').find('option').each(function(){
			$(this).remove();
		});
		if($(this).val() == 'D'){
			for(var i=1; i < 31 ;i++){
				var option = $("<option value="+ i +">" + i + "</option>");
				$('#send_day').append(option);
			}
			//$('#send_day').append("<option value='L'>마지막날짜</option>");
		}else{
			for(var i=0; i < week_val.length ;i++){
				var option = $("<option value="+ week_val[i][0] +">" + week_val[i][1] + "</option>");
				$('#send_day').append(option);
			}
		}
	});
	
	$(".btn_add_info").click(function(){
    	var pw = window.open('/man/mailTargetSearchPopup.do?reqCode=1','mtarget_popup','width=1000, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
	});
	
	$(".btn_freeview").click(function(){
		/* var gubun = $('input:radio[name=gubun]:checked').val();
		var pol_type = $('input:radio[name=pol_type]:checked').val();
		var contents = CKEDITOR.instances['editor'].getData();
		var contents_top = CKEDITOR.instances['editor1'].getData();
		var contents_bottom = CKEDITOR.instances['editor2'].getData();
		var param = {
				gubun : gubun,
				pol_type : pol_type,
				contents : contents,
				contents_top : contents_top,
				contents_bottom : contents_bottom
		} */
		var mail_seq = $('#mail_seq').val();
		if(mail_seq == '0'){
			alert('미리보기는 저장 후 확인 가능 합니다.');
			return false;
		}
		//escape(encodeURI(JSON.stringify(param)))
		//alert(escape(encodeURIComponent(JSON.stringify(param))));
		//var fw = window.open('/man/mainfreeviewPopup.do?param='+ escape(encodeURIComponent(JSON.stringify(param))),'mtarget_popup','width=1000, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
		var fw = window.open('/man/mainfreeviewPopup.do?mail_seq='+ mail_seq,'mtarget_popup','width=1200, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no, resizable=yes');
		if (fw != null)
   		 fw.focus();
	});
	/**
	orgInfo : 대상 코드
	orgType : 대상 유형 (1:사번/성명, 2:조직, 3:그룹 )
	*/
	$.addRow = function(orgInfo, orgType, orgNm){
		var is_exists = false;
		$(".target_list_body").find('tr').each(function(){
			if($(this).attr('is_all_send') == 'Y'){
				$(this).remove();
			}
			var gcode = $(this).find('td').eq(0).text();
			if(gcode == orgInfo){
				is_exists = true;
			}
		})
		if(is_exists){
			return false;
		}
		
		var trAdd = "<tr is_all_send='N'>";
		trAdd +="<td style='text-align: center;'>"+orgInfo+"</td>";
		trAdd +="<td style='text-align: center;'>"+orgType+"</td>";
		if(orgType==1) {
			trAdd +="<td style='text-align: center;'>개인</td>";
		} else if(orgType==2) {
			trAdd +="<td style='text-align: center;'>조직</td>";
		}else if(orgType==3) {
			trAdd +="<td style='text-align: center;'>그룹</td>";
		}
		trAdd +="<td style='text-align: center;'>"+orgNm+"</td>";
		trAdd +="<td style='text-align: center;'><a class='btn_target_delete' style='cursor:pointer;' ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer; ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
	 	/*trAdd +="<input type='hidden' id='targetType' name='targetType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='targetNm' name='targetNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='targetCode' name='targetCode' value='"+orgInfo+"' />"; */
		trAdd +="</tr>";
		$(".target_list_body").append(trAdd);
	};
	
	$('.groupInfo').on('click', '.btn_target_delete', function(){
		$(this).closest('tr').remove();
	});
	
	$(':input:radio[name=pol_type]').click(function(){
		if($(this).val() == '05'){
			$('.is_cap_send').show();
		}else{
			//$('#cap_send').prop("chekced", false);
			$('.is_cap_send').hide();
		}
	});
	$(':input:checkbox[name=cap_send]').click(function(){
		if($(this).is(':checked')){
			alert('발송 대상과 상관없이 부서장에게 발송 됩니다.');
			$('.btn_add_info').hide();
		}else{
			$('.btn_add_info').show();
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
    	<div class="subTT"><span>메일 전송정보 관리</span></div>
    	<div class="sch_view">
    		<table class='tblInfoType' cellpadding='0' cellspacing='0'>
    			<colgroup><col style='width:20%'><col style='width:80%'></colgroup>
    			<tr>
    				<th>메일 전송 구분</th>
    				<td class="radio_button">
    					<label><input type='radio' name='gubun' value='N' <c:if test="${mailSendInfo.gubun == 'N'}" >checked</c:if> /><span>공지사항 </span></label>
    					<label><input type='radio' name='gubun' value='P' <c:if test="${mailSendInfo.gubun == 'P'}" >checked</c:if> /><span>정책 진단</span></label>
    					<label><input type='radio' name='gubun' value='K' <c:if test="${mailSendInfo.gubun == 'K'}" >checked</c:if> /><span>임직원 취약자</span></label>
    					<label><input type='radio' name='gubun' value='C' <c:if test="${mailSendInfo.gubun == 'C'}" >checked</c:if> /><span>협력사 취약자</span></label>
    				</td>
    			</tr>
    			<tr class="Cap_Row" style="display: <c:if test="${mailSendInfo.gubun != 'C'}" >none</c:if>;">
    				<th>담당자 발송 여부</th>
    				<td class="radio_button">
    					<label><input type='radio' name='is_cap' value='Y' <c:if test="${mailSendInfo.is_cap_send == 'Y' }" >checked</c:if>><span>사용</span></label> 
    					<label><input type='radio' name='is_cap' value='N' <c:if test="${mailSendInfo.is_cap_send == 'N' }" >checked</c:if>><span>미 사용</span></label> 
    					 (사용 일 경우 : 협력사 메일 주소 미 존재 시 KT담당자에게 메일 발송 됩니다.)
    				</td>
    			</tr>
    			<tr class="File_Row" style="display: <c:if test="${mailSendInfo.gubun != 'C' && mailSendInfo.gubun != 'K'}" >none</c:if>;">
    				<th>조치 안내 파일 다운로드</th>
    				<td class="File_Down">
    					<a href="/man/fileDown.do?type=${mailSendInfo.pol_type}" target="_self">첨부 파일 다운로드</a>
    				</td>
    			</tr>
    			
    			<tr class="Pol_Type_Row" style="display: <c:if test="${mailSendInfo.gubun != 'P'}" >none</c:if>;">
    				<th>리포트 유형선택</th>
    				<td class="radio_button">
    					<label><input type='radio' name='pol_type' value='04' <c:if test="${mailSendInfo.pol_type == '04'}" >checked</c:if> /><span>개인 </span></label>
    					<label><input type='radio' name='pol_type' value='05' <c:if test="${mailSendInfo.pol_type == '05'}" >checked</c:if> /><span>조직</span></label>
    				</td>
    			</tr>
    			<tr class="Pol_Row" style="display: <c:if test="${mailSendInfo.pol_type != '04'}" >none</c:if>;">
    				<th>정책 선택</th>
    				<td class="ck_button">
    				<label style="min-width:230px;"><input type='checkbox' name='checked_all' value='' /><span>전체 선택</span></label>
        			<c:forEach var="result" items="${mailPolicyList}" varStatus="status">
						<label style="min-width:230px;"><input type='checkbox' name='policyid' value='${result.sec_pol_id}' <c:if test="${result.is_selected == 'Y'}" >checked</c:if> /><span>${result.policyname} </span></label>
					</c:forEach>	
    				</td>
    			</tr>
    			<tr class="target_row" style="display: <c:if test="${mailSendInfo.gubun == 'K' || mailSendInfo.gubun == 'C'}" >none</c:if>;">
    				<th>
    					발송 대상자<br><br>
    					<a class="btn_black btn_add_info" style="display:<c:if test="${mailSendInfo.is_cap_send == 'Y'}" >none</c:if>;"><span>대상자 추가</span></a><br>
    					<div class="ck_button is_cap_send" style="display:<c:if test="${mailSendInfo.pol_type == '04'}" >none</c:if>;"><label><input type="checkbox" name="cap_send" id="cap_send" <c:if test="${mailSendInfo.is_cap_send == 'Y'}" >checked</c:if> value="1" /> <span>부서장 발송</span></label></div>
    				</th>
    				<td >
    					<table cellpadding="0" cellspacing="0" class="groupInfo" style="width:100%">
			                <colgroup>
								<col style="width:20%">
								<col style="width:*">
								<col style="width:20%">
								<col style="width:20%">
							</colgroup>
							<tr>
								<th style="text-align: center">대상코드</th>
								<th style="text-align: center">구분 코드</th>
								<th style="text-align: center">대당 구분명</th>
								<th style="text-align: center">대상명</th>
								<th style="text-align: center">삭제</th>
							</tr>
							<tbody class="target_list_body">
							<c:if test="${empty mailTargetList}">
		                		<tr style="height:200px;" is_all_send='Y'>
		                			<td style="text-align: center;" colspan="9">
		                			발송 대상이 없을 경우 전사 발송 됩니다.
		                			</td>
		                		</tr>
		                	</c:if>
							<c:forEach var="result" items="${mailTargetList}" varStatus="status">
							<tr is_all_send='N'>
								<td style="text-align: center">${result.target_code }</td>
								<td style="text-align: center">${result.target_type }</td>	
								<td style="text-align: center">${result.target_type_name }</td>	
								<td style="text-align: center">${result.target_nm }</td>
								<td style="text-align: center"><a targetCode='${result.target_code }' style="cursor:pointer;" class='btn_target_delete' ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
							</tr>
							</c:forEach>
							</tbody>
			            </table>
    					
    				</td>
    			</tr>
    			
    			<tr>
    				<th>발송 예약 설정</th>
    				<td class="radio_button">
    					<label><input type='radio' name='send_type' value='REG' <c:if test="${mailSendInfo.send_type == 'REG'}" >checked</c:if> /><span>예약일 발송 </span></label>
    					<label><input type='radio' name='send_type' value='REP' <c:if test="${mailSendInfo.send_type == 'REP'}" >checked</c:if> /><span>반복 발송</span></label>
    					<label><input type='radio' name='send_type' value='MUL' <c:if test="${mailSendInfo.send_type == 'MUL'}" >checked</c:if> /><span>발송 설정정보 직접 입력</span></label>
    				</td>
    			</tr>
    			<tr class="Reg_Row" style="display:<c:if test="${mailSendInfo.send_type != 'REG'}" >none</c:if>;">
    				<th>예약일 설정</th>
    				<td >
    					<input type="text" class="srh" style="width:120px;" readonly id="send_date" name="send_date" value="${mailSendInfo.send_date }" /> 
    				 	<select id="send_hour_reg" name="send_hour_reg" style="width:80px;">
    					<c:forEach var="i" begin="0" end="23" step="1">
    						<option value="${i }" <c:if test="${mailSendInfo.send_hour_reg == i}" >selected</c:if> /><fmt:formatNumber value='${i }' pattern='00' /> 시</option>
    					</c:forEach>
    					</select>
    					
    					<select id="send_minutes_reg" name="send_minutes_reg" style="width:80px;">
    					<c:forEach var="i" begin="0" end="50" step="10">
    						<option value="${i }" <c:if test="${mailSendInfo.send_minutes_reg == i}" >selected</c:if> /><fmt:formatNumber value='${i }' pattern='00' /> 분</option>
    					</c:forEach>
    					</select>
    				</td>
    			</tr>
    			<tr class="Rep_Row" style="display:<c:if test="${mailSendInfo.send_type != 'REP'}" >none</c:if>;">
    				<th>반복 발송일 설정</th>
    				<td >
    					<select id="send_day_type" name="send_day_type" style="width:80px;">
    						<option value="MONTH" <c:if test="${mailSendInfo.send_day_type == 'MONTH'}" >selected</c:if> />매월</option>
    						<option value="WEEK" <c:if test="${mailSendInfo.send_day_type == 'WEEK'}" >selected</c:if> />매주</option>
    						
    					</select>
    					
    					<select id="send_day_option" name="send_day_option" style="width:80px;display:<c:if test="${mailSendInfo.send_day_type == 'WEEK'}" >none</c:if>;">
    						<option value="D" <c:if test="${mailSendInfo.send_day_option == 'D'}" >selected</c:if> />일별</option>
    						<option value="W1" <c:if test="${mailSendInfo.send_day_option == 'W1'}" >selected</c:if> />첫번째</option>
    						<option value="W2" <c:if test="${mailSendInfo.send_day_option == 'W2'}" >selected</c:if> />두번째</option>
    						<option value="W3" <c:if test="${mailSendInfo.send_day_option == 'W3'}" >selected</c:if> />세번째</option>
    						<option value="W4" <c:if test="${mailSendInfo.send_day_option == 'W4'}" >selected</c:if> />네번재</option>
    					</select>
    					
    					<select id="send_day" name="send_day" style="width:100px;">
    					
    					<c:forEach var="result" items="${mailSendInfo.dayOptionList}" varStatus="status">
    						<option value="${result.select_value }" <c:if test="${mailSendInfo.send_day == result.select_value}" >selected</c:if> />${result.select_text }</option>
    					</c:forEach>
    					
    					</select>
    					
    					<select id="send_hour_rep" name="send_hour_rep" style="width:80px;">
    					<c:forEach var="i" begin="0" end="23" step="1">
    						<option value="${i }" <c:if test="${mailSendInfo.send_hour_rep == i}" >selected</c:if> /><fmt:formatNumber value='${i }' pattern='00' /> 시</option>
    					</c:forEach>
    					</select>
    					
    					<select id="send_minutes_rep" name="send_minutes_rep" style="width:80px;">
    					<c:forEach var="i" begin="0" end="50" step="10">
    						<option value="${i }" <c:if test="${mailSendInfo.send_minutes_rep == i}" >selected</c:if> /><fmt:formatNumber value='${i }' pattern='00' /> 분</option>
    					</c:forEach>
    					</select>
    				</td>
    			</tr>	
    			<tr class="Mul_Row" style="display:<c:if test="${mailSendInfo.send_type != 'MUL'}" >none</c:if>;">	
    				<th>메일 발송 설정정보</th>
    				<td >
    					<input type='text' class="srh" id='schedule_expression' name='schedule_expression' style='width:300px;height:20px;border:1px solid #AEAEAE;' maxlength='20' value='${mailSendInfo.schedule_expression }' />
    					 - 분 시 일 월 요일 순으로 작성 (0 0 * * 1 : 매주 월요일 0시에 발송)
    				</td>
    			</tr>
    			<tr>	
    				<th>메일 제목</th>
    				<td ><input type='text' class="srh" id='subject' name='subject' style='width:99%;height:20px;border:1px solid #AEAEAE;' maxlength='1000' value='${mailSendInfo.subject }' /></td>
    			</tr>
    			<tr>
    				<th>사용여부</th>
    				<td  class="radio_button">
    					<label><input type='radio' name='is_used' value='Y' <c:if test="${mailSendInfo.is_used == 'Y' }" >checked</c:if>><span>사용</span></label> 
    					<label><input type='radio' name='is_used' value='N' <c:if test="${mailSendInfo.is_used == 'N' }" >checked</c:if>><span>사용중지</span></label> 
    					 (미사용 일 경우 메일 발송이 차단 됩니다.)
    				</td>
    			</tr>
    			<tr>
    				<th>예약어 목록</th>
    				<td  class="radio_button">
    					※ 예약어 입력시 메일 수진자정보로 변경되어 발송 됩니다.<br>
    					<span style="font-weight: bold;">[NAME_VALUE]</span> : 메일 수신자 성명으로 대체 됩니다.<br>
    					<span style="font-weight: bold;">[ORG_NAME]</span> : 메일 수신자 부서명으로 대체 됩니다.<br>
    					<span style="font-weight: bold;">[BEGIN_VALUE]</span> : 정책진단 메일일 경우 진단일 공지일 경우 현재 날짜 (출력 예 : 2018.01.01)<br>
    					<span style="font-weight: bold;">[YEAR_VALUE]</span> : 연도 (정책진단 메일일 경우 진단일 공지일 경우 현재 날짜) <br>
    					<span style="font-weight: bold;">[MONTH_VALUE]</span> : 월 (정책진단 메일일 경우 진단일 공지일 경우 현재 날짜) <br>
    					<span style="font-weight: bold;">[DAY_VALUE]</span> : 일 (정책진단 메일일 경우 진단일 공지일 경우 현재 날짜)
    				</td>
    			</tr>
    			<tr class="contents_noti" style="display: <c:if test="${mailSendInfo.gubun != 'N'}" >none</c:if>;">
					<th>내용</th>
					<td style='padding:5px 0 5px 10px;' ><textarea  id='editor' name='editor' style='width:95%;' >${mailSendInfo.contents }</textarea></td>
					
    			<tr>
    			<tr class="contents_policy" style="display: <c:if test="${mailSendInfo.gubun == 'N'}" >none</c:if>;">
					<th>상단 내용</th>
					<td style='padding:5px 0 5px 10px;' >
					<div class="radio_button">템플릿 적용
					 	<label><input type='radio' name='temp_contents' value='T01' ><span>개인 리포트</span></label> 
    					<label><input type='radio' name='temp_contents' value='M01'><span>조직 리포트</span></label>
					</div>
					<textarea  id='editor1' name='editor1' style='width:95%;' >${mailSendInfo.contents_top }</textarea>
					</td>
					
    			<tr>
    			<tr class="contents_policy" style="display: <c:if test="${mailSendInfo.gubun == 'N'}" >none</c:if>;">
					<th>하단 내용</th>
					<td style='padding:5px 0 5px 10px;' ><textarea  id='editor2' name='editor2' style='width:95%;' >${mailSendInfo.contents_bottom }</textarea></td>
					
    			<tr>
    		</table>
            <div class="btn_black2" style="vertical-align: text-bottom;">
            	<!-- <a class="btn_black btn_testmail_send"><span>테스트 메일 발송</span></a> -->
            	<%-- <c:if test="${mailSendInfo.mail_seq != '0'}" ><a class="btn_black btn_mailsendinfo_save"><span>메일 수동 발송</span></a></c:if> --%>
            	(미리보기는 저장 후 확인 가능 합니다.) <a class="btn_black btn_freeview"><span>미리 보기</span></a>
            	<a class="btn_black btn_mailsendinfo_save"><span>저장</span></a>
            	<a class="btn_black btn_cancel" style="cursor:pointer;" ><span>목록</span></a> 
            </div>
        </div>
		<input type="hidden" name="search_gubun" id="search_gubun" value="${searchVO.search_gubun }"/>
		<input type="hidden" name="search_is_used" id="search_is_used" value="${searchVO.search_is_used }"/>
		<input type="hidden" name="search_subject" id="search_subject" value="${searchVO.search_subject }"/>
		<input type="hidden" name="pageIndex" id="pageIndex" value="${searchVO.pageIndex }" />
		<input type="hidden" name="mail_seq" id="mail_seq" value="${searchVO.mail_seq }" />
		<input type='text' style='display:none;' />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
     <div class='popup_dialogbox'></div>
</div>
</body>
</html>