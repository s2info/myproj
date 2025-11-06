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
<script src="/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<script type="text/javaScript" language="javascript">
$(function () {		
	/*$('.btn_policy_save').click(function(){
			
		
		
		if( confirm(" 저장 하시겠습니까?" ) ){
			document.listForm.action = "/man/policysave.do";
			document.listForm.submit();
		}
	});*/
	
	$('.popup_dialogbox').dialog({
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
	
	$('.btn_cancel').click(function(){
		goList();
	});
	
	function goList(){
		document.listForm.action = "<c:url value='/man/polmanger.do'/>";
		document.listForm.submit();
	}
	
	$('input:radio[name=sec_pol_cat]').click(function(){
		if($(this).val()==1){
			$('.data_type_count').show();
			$('.data_type_ox').hide();
		}else{
			$('.data_type_count').hide();
			$('.data_type_ox').show();
		}
	});
	
	$('input:radio[name=gajeomindc]').click(function(){
		if($(this).val()=='Y'){
			$('#weight_val').val(0);
			$('.is_weight').hide();
		}else{
			$('#weight_val').val();
			$('.is_weight').show();
		}
		
	});
	
	$('.btn_columns_info').click(function(e){
		var sec_pol_id=$('#sec_pol_id').val();
		var pol_table=$('#pol_table').val();
		var pol_columns=$('#pol_columns').val();
		$.ajax({
			data: {sec_pol_id:sec_pol_id,pol_table:pol_table,pol_columns:pol_columns},
			url: '/man/getPolTableNColumnInfo.do',
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
					$('.popup_dialogbox').html(data.popup_body);
					$('.popup_dialogbox').dialog({ width: 880, height: 535 });
					$('.popup_dialogbox').dialog('open');
					$('.popup_dialogbox').dialog().parent().draggable({ cancel: '', handle: '.subTT' });
				}else{alert(data.MSG); }	
			}
		});	
	});
   
	$('#con_type').change(function(e){
		var contype = $(this).val();
		$.ajax({
			data: {contype:contype},
			url: '/man/getPolConSelectType.do',
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
					$('.pol_con_body_count').empty().append(data.con_body);
				}else{alert(data.MSG); }
			}
		});
	});
	
	$('#majrcode').change(function(e){
		var buseoindc = $('option:selected',$(this)).attr('isbs');
		$('#buseoindc').val(buseoindc);
		var majcode = $(this).val();
		$.ajax({
			data: {majcode:majcode},
			url: '/man/getDiagMinrCodeList.do',
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
					$('#minrcode > option').each(function(){$(this).remove();});
					$('#minrcode').append("<option value=''>중진단선택</option>");
					for(var i=0;i<data.list.length;i++){
						$('#minrcode').append('<option value="'+ data.list[i]["diag_minr_code"] +'">' + data.list[i]["diag_desc"] +'</option>');
					}
					changeDiageMajrCode(buseoindc);
				}else{alert(data.MSG); }
			}
		});			
	});
	
	function changeDiageMajrCode(buseoindc){
		$('.TBS4').find('tr').each(function(){
			if(buseoindc=='Y'){
				if($(this).hasClass('is_notbuseo')){
					$(this).hide();
				}
				if($(this).hasClass('is_buseo')){
					$(this).show();
				}
			}else{
				if($(this).hasClass('is_notbuseo')){
					$(this).show();
				}
				if($(this).hasClass('is_buseo')){
					$(this).hide();
				}
			}
		});
	}
	
	$('.btn_policy_save').click(function(){
		var sec_pol_id=$('#sec_pol_id').val();	
		var sec_pol_desc=$('#sec_pol_desc').val();	
		var sec_sol_id=$('#sec_sol_id').val();	
		var majrcode=$('#majrcode').val();	
		var minrcode=$('#minrcode').val();
		var sec_pol_cat = $('input:radio[name=sec_pol_cat]:checked');
		//var critical_type = $('input:radio[name=critical_value_type]:checked');
		var critical_type=$('#critical_value_type');
		var critical_val=$('#critical_val').val();	
		var weight_val=$('#weight_val').val();	
		var isautosanct = $('input:radio[name=isautosanct]:checked');
		var isautoappr = $('input:radio[name=isautoappr]:checked');
		var appr_line_code=$('#appr_line_code').val();	
		var isfile = $('input:radio[name=isfile]:checked');	
		var noti=$('#noti').val();	
		var isused = $('input:radio[name=isused]:checked');
		var pol_table=$('#pol_table').val();	
		var pol_columns=$('#pol_columns').val();	
		var exe_para=$('#exe_para').val();	
		var cond_field1=$('#cond_field1').val();
		var ispcgact=$('#ispcgact').val();	
		var sec_pol_notice=$('#polnoti').val();
		//var content = CKEDITOR.instances['editor'].getData();
		//var sec_pol_notice_detail=content.replace(/(<([^>]+)>)/ig, "");
		var sec_pol_notice_detail=CKEDITOR.instances['editor'].getData();
		var reason=$('#reason').val();
		var buseoindc=$('#buseoindc').val();
		var gajeomindc=$('input:radio[name=gajeomindc]:checked');
		var collabor_indc = $('input:checkbox[name=collabor_indc]:checked');
		if(majrcode == ''){	
			alert('대진단을 선택하여 주세요.');	
			return false;	
		}	
		if(minrcode == ''){
			alert('중진단을 선택하여 주세요.');
			return false;	
		}	
		if(sec_pol_cat.length <= 0){
			alert('데이타 타입을 선택하여 주세요.');
			return false;	
		}	
		var con_cnt=sec_pol_cat.val()=='01' ? $('#con_type').val() : $('#x_con_type').val();
		var con_scor_1=sec_pol_cat.val()=='01' ? $('#con_scor_1').val() : $('#x_con_scor_1').val();
		var con_from_1=sec_pol_cat.val()=='01' ? $('#con_from_1').val() : $('#x_con_from_1').val();
		var con_to_1=sec_pol_cat.val()=='01' ? $('#con_to_1').val() : $('#x_con_to_1').val();	
		var con_scor_2=sec_pol_cat.val()=='01' ? $('#con_scor_2').val() : $('#x_con_scor_2').val();
		var con_from_2=sec_pol_cat.val()=='01' ? $('#con_from_2').val() : $('#x_con_from_2').val();
		var con_to_2=sec_pol_cat.val()=='01' ? $('#con_to_2').val() : $('#x_con_to_2').val();	
		var con_scor_3=sec_pol_cat.val()=='01' ? $('#con_scor_3').val() : $('#x_con_scor_3').val();
		var con_from_3=sec_pol_cat.val()=='01' ? $('#con_from_3').val() : $('#x_con_from_3').val();
		var con_to_3=sec_pol_cat.val()=='01' ? $('#con_to_3').val() : $('#x_con_to_3').val();	
		var con_scor_4=sec_pol_cat.val()=='01' ? $('#con_scor_4').val() : $('#x_con_scor_4').val();
		var con_from_4=sec_pol_cat.val()=='01' ? $('#con_from_4').val() : $('#x_con_from_4').val();
		var con_to_4=sec_pol_cat.val()=='01' ? $('#con_to_4').val() : $('#x_con_to_4').val();	
		var con_scor_5=sec_pol_cat.val()=='01' ? $('#con_scor_5').val() : $('#x_con_scor_5').val();
		var con_from_5=sec_pol_cat.val()=='01' ? $('#con_from_5').val() : $('#x_con_from_5').val();
		var con_to_5=sec_pol_cat.val()=='01' ? $('#con_to_5').val() : $('#x_con_to_5').val();	
		var sanc_indc=$('input:radio[name=sanc_indc]:checked');	
		// 순서추가 2017.04.27
		var ordr=$('#ordr').val();	
		
		
		if(ordr == ''){
			alert('정렬 순서를 입력하세요.');
			$("#ordr").focus();
			return false;	
		}
		
		// 홠성화 날짜 추가 2018.09.20
		var startDay = $("#startDay").val();
		var endDay = $("#endDay").val();
		
		//그룸정보 확인
		var targetType = [];
		var targetCode = [];
		var targetNm = [];
		
		$("input[name='targetType']").each(function(i){
			targetType.push($("input[name='targetType']").eq(i).val());
			console.log("targetType"+i+" == "+$("input[name='targetType']").eq(i).val());
		});
		
		$("input[name='targetCode']").each(function(i){
			targetCode.push($("input[name='targetCode']").eq(i).val());
			console.log("targetCode"+i+" == "+$("input[name='targetCode']").eq(i).val());
		});
		
		$("input[name='targetNm']").each(function(i){
			targetNm.push($("input[name='targetNm']").eq(i).val());
			console.log("targetNm"+i+" == "+$("input[name='targetNm']").eq(i).val());
		});
		
		
		//비고
		var bigo=$('#bigo').val();
		
		if(critical_type.length <= 0){	
			alert('임계치 구분을 선택하여 주세요.');	
			return false;	
		}	
		if(critical_val == ''){
			alert('임계치를 입력하여 주세요.');
			return false;	
		}	
		if(!critical_val.isNumber()){
			alert('임계치는 숫자로 입력하여 주세요.');
			return false;	
		}	
		if(!weight_val.isNumber()){
			alert('가중치는 숫자로 입력하여 주세요.');
			return false;	
		}	
		if(con_cnt =='0'){
			alert('점수분류 선택 및 값을 입력하여 주세요.');
			return false;	
		}	
		if(sec_pol_cat.val()=='01'){
			if($('#con_type').val() == ''){ 
				alert('조건카운트 조건수 선택 및 조건카운트를 입력하여 주세요.');
				return false;	
			}	
		}	
		var isnum = true;	
		$(sec_pol_cat.val()=='01' ? '.pol_con_body_count' : '.pol_con_body_ox').find('input:text').each(function(){
			if(!$(this).val().isNumber()){ isnum = false;}	
		});	
		if(!isnum){	
			alert('조건 카운트는 숫자로 입력하여 주세요.');	
			return false;	
		}	
		if(buseoindc=='N'){	
			if(isautosanct.length <= 0){
				alert('제재조치유형을 선택하여 주세요.');	
				return false;	
			}	
			if(isautoappr.length <= 0){
				alert('소명신청유형을 선택하여 주세요.');	
				return false;	
			}	
			if(appr_line_code == ''){
				alert('결재라인을 선택하여 주세요.');	
				return false;	
			}	
			if(isfile.length <= 0){
				alert('소명파일 첨부여부를 선택하여 주세요.');
				return false;	
			}	
			if(sanc_indc.length <= 0){
				alert('제재조치 대상여부를 선택하여 주세요.');
				return false;	
			}	
		}else{			
			if(gajeomindc.length <= 0){		
				alert('가점/감점여부를 선택하여 주세요.');		
				return false;				
			}		
		}			
		if(isused.length <= 0){		
			alert('사용여부를 선택하여 주세요.');		
			return false;			
		}			
		if(!confirm('지수화정책 정보를 저장 하시겠습니까?')){		
			return false;		
		}		
		var data={sec_pol_id:sec_pol_id		
				,sec_pol_desc:sec_pol_desc			
				,sec_sol_id:sec_sol_id			
				,diag_majr_code:majrcode			
				,diag_minr_code:minrcode		
				,sec_pol_cat:sec_pol_cat.val()	
				,pol_critical_value_type:critical_type.val()
				,pol_critical_vlaue:critical_val			
				,pol_weight_value:weight_val				
				,sanc_cate:buseoindc=='N' ? isautosanct.val() : '1'		
				,appr_cate:buseoindc=='N' ? isautoappr.val() : '1'		
				,appr_line_code:appr_line_code				
				,appr_attach_indc:buseoindc=='N' ? isfile.val() : '1'					
				,appr_note:noti					
				,use_indc:isused.val()					 
				,sanc_indc:buseoindc=='N' ? sanc_indc.val() == 'Y' ? true : false : false					
				,exe_para:exe_para					 
				,cond_field1:cond_field1					 
				,ispcgact:ispcgact					 
				,sec_pol_notice:sec_pol_notice	
				,sec_pol_notice_detail:sec_pol_notice_detail
				,reason:reason
				,sec_pol_table:pol_table					
				,sec_pol_table_col:pol_columns					 
				,buseo_indc:buseoindc					 
				,gajeom_indc:buseoindc=='N' ? 'N' : gajeomindc.val()					
				,con_cnt:con_cnt					
				,con_scor_1:con_scor_1					 
				,con_from_1:con_from_1					 
				,con_to_1:con_to_1					 
				,con_scor_2:con_scor_2					 
				,con_from_2:con_from_2					 
				,con_to_2:con_to_2					 
				,con_scor_3:con_scor_3					 
				,con_from_3:con_from_3					 
				,con_to_3:con_to_3					 
				,con_scor_4:con_scor_4					 
				,con_from_4:con_from_4					 
				,con_to_4:con_to_4					 
				,con_scor_5:con_scor_5					
				,con_from_5:con_from_5					
				,con_to_5:con_to_5
				,ordr:ordr
				,startDay:startDay
				,endDay:endDay
				,targetTypeArr:targetType.toString()
				,targetCodeArr:targetCode.toString()
				,targetNmArr:targetNm.toString()
				,collabor_indc:collabor_indc.val()==''? 'N' : collabor_indc.val()
				,bigo:bigo};			
		$.ajax({				
			data: data,				
			url: '/man/setPolInfoModify.do',				
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
					goList();					
				}else{alert(data.MSG); }				
			}			
		});	
	});
	
	
	CKEDITOR.replace('editor', {
		filebrowserImageUploadUrl:'/man/editorImageUpload.do',
		enterMode: '2',
		shiftEnterMode: '3',
		uiColor: '#ffffff',
		height: 350,
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
	
	$.addRow = function(orgInfo, orgType, orgNm){
		
		$(".target_list_body").find('tr').each(function(){
			if($(this).attr('is_all_send') == 'Y'){
				$(this).remove();
			}
		})
		
		var trAdd = "<tr>";
		if(orgType==1) {
			trAdd +="<td>개인</td>";
		} else if(orgType==2) {
			trAdd +="<td>조직</td>";
		}else if(orgType==3) {
			trAdd +="<td>그룹</td>";
		}
		
		trAdd +="<td>"+orgNm+"</td>";
		trAdd +="<td>"+orgInfo+"</td>";
		trAdd +='<td><a href="javascript:void(0);" onclick="$.btn_del(\'\', this)" style="cursor:pointer;" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a></td>';
		//trAdd +="<td><a id= 'btn_del' class='btn_del' href='javascript:$.btn_del();' style='cursor:pointer; ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>";
		trAdd +="<input type='hidden' id='targetType' name='targetType' value='"+orgType+"' />";
		trAdd +="<input type='hidden' id='targetNm' name='targetNm' value='"+orgNm+"' />";
		trAdd +="<input type='hidden' id='targetCode' name='targetCode' value='"+orgInfo+"' />";
		trAdd +="</tr>";
		$(".groupInfo").append(trAdd);
		
		//self.close();
	};
	
	$.btn_del = function(targetCode, obj){ 
		//alert("ddd");
		
		var targetCode = targetCode;
		var sec_pol_id = $("#sec_pol_id").val();
		
		//alert("targetInfo ="+targetInfo );
		
		if(targetCode == ""){
			//alert("ee");
			$(obj).parent().parent().remove();
		}
		else{
			var data = {
					targetCode : targetCode,
					sec_pol_id : sec_pol_id
			}
			
			
			$.ajax({
				url: '/man/polTargetInfoDelete.do',
				data: data,
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
						//$("#sdCheckNo").val(data.sdCheckNo);
						$(obj).parent().parent().remove();
						
					}else{alert(data.MSG); }				
				}
			});
		}
    
	};
	
	$('#startDay, #endDay').datepicker({
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
        },
        onSelect: function (dateText, inst) {
	 		var dateSearch1 = $("#startDay").val();
			var dateSearch2 = $("#endDay").val();
			if(dateSearch1 == "" || dateSearch1.length < 1){
				alert("시작 날짜부터 입력해주세요");
				$("#endDay").val("");
			}
			if((dateSearch2 != "" && dateSearch2.length > 0) && dateSearch1 > dateSearch2){
				alert("시작 날짜가 종료 날짜보다 큽니다 . 선택을 다시해주세요");
				if (inst.id == "sBeginDate"){
					$("#startDay").val("");
					$("#startDay").focus();
				}
				else{
					$("#endDay").val("");
					$("#endDay").focus();
				}
			}
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
	
    $(".btn_add_info").click(function(){
    	var pw = window.open('/group/popOrgInfoSearch.do?reqCode=1','orgInfo_popup','width=1000, height=700,scrollbars=yes, menubar=no, status=no, toolbar=no');
    	if (pw != null)
    		 pw.focus();
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
    	<div class="subTT"><span>지수화 정책 관리</span></div>
    	<div class="sch_view">
    		<table class='tblInfoType' cellpadding='0' cellspacing='0'>
    			<colgroup><col style='width:20%'><col style='width:30%'><col style='width:20%'><col style='width:30%'></colgroup>
    			<tr>
    				<th>지수화 정책</th>
    				<td>
    					<input type='text' class="srh" name='sec_pol_desc' id='sec_pol_desc' value='${secPolInfo.sec_pol_desc }' style='width:99%;height:22px;border:1px solid #AEAEAE;'/>
    					<input type='hidden' name='sec_pol_id' id='sec_pol_id' value='${secPolInfo.sec_pol_id }' />
    					<input type='hidden' name='sec_sol_id' id='sec_sol_id' value='${secPolInfo.sec_sol_id }' />
    				</td>
    				<th>정렬 순서</th>
    				<td><input type="text" class="srh" name='ordr' id='ordr' value='${secPolInfo.ordr }' /></td>
    			</tr>
    			<tr>
    				<th>대진단</th>
    				<td>${majPolDiagString }</td>
    				<th>중진단</th>
    				<td>${mijPolDiagString }</td>
    			</tr>
    			<tr>
    				<th>보안 정책 구분</th>
    				<td colspan='3' class="radio_button">
    					<label><input type='radio' name='sec_pol_cat' value='01' <c:if test="${secPolInfo.sec_pol_cat == '01'}" >checked</c:if> /><span>카운트 </span></label>
    					<label><input type='radio' name='sec_pol_cat' value='02' <c:if test="${secPolInfo.sec_pol_cat == '02'}" >checked</c:if> /><span>O,X</span></label>
    				</td>
    			</tr>
    			<tr class='is_buseo' style='display:<c:if test="${secPolInfo.buseo_indc != 'Y'}" >none</c:if>;'>
    				<th>가점 여부</th>
    				<td colspan='3'><input type='radio' id='gajeomindc' name='gajeomindc' value='Y' <c:if test="${secPolInfo.gajeom_indc == 'Y'}" >checked</c:if> /> 예(가점) &nbsp;&nbsp;&nbsp; <input type='radio' id='gajeomindc' name='gajeomindc' value='N' <c:if test="${secPolInfo.gajeom_indc == 'N'}" >checked</c:if> /> 아니오(감점)</td>
    			</tr>
    			<tr class='is_notbuseo is_weight' style='display:<c:if test="${secPolInfo.buseo_indc == 'Y' && secPolInfo.gajeom_indc=='Y'}" >none</c:if>;'>
    				<th>가중치</th>
    				<td colspan='3'><input type='text' class="srh" id='weight_val' name='weight_val' value='${secPolInfo.pol_weight_value }' style='width:100px;height:22px;border:1px solid #AEAEAE;' maxlength='10'  /><input type='hidden' id='critical_val' name='critical_val' value='${secPolInfo.pol_critical_vlaue }' /></td>
    			</tr>
    			<tr class='data_type_count' style='display:<c:if test="${secPolInfo.sec_pol_cat == '02'}" >none</c:if>;'>
    				<th>조건카운트 <select name='con_type' id='con_type'>
    				<c:forEach var="item" begin="1" end="5" step="1">
    					<option value='<c:if test="${item != 1}" >${item }</c:if>' <c:if test="${polConInfo.con_cnt == item}" >selected</c:if>>
    					<c:choose><c:when test="${item == 1 }">
    						조건수선택
    						</c:when>
							<c:otherwise>
							조건수 ${item }
							</c:otherwise>
						</c:choose>
    					</option>
    				</c:forEach>
    				</select></th>
    				<td colspan='3' class='pol_con_body_count'>
    					${polWeightPointHtml }
    				</td>
    			</tr>
    			<tr class='data_type_ox' style='display:<c:if test="${secPolInfo.sec_pol_cat == '01'}" >none</c:if>;'>
    				<th>조건카운트</th>
    				<td colspan='3' class='pol_con_body_ox'><input type='hidden' name='x_con_type' id='x_con_type' value='2' />
    					${polWeightPointOXHtml }
    				</td>
    			</tr>
    			<tr class='is_notbuseo' style='display:<c:if test="${secPolInfo.buseo_indc == 'Y'}" >none</c:if>;'>
    				<th>제재조치</th>
    				<td class="radio_button">
	    				<label><input type='radio' name='isautosanct' value='0' <c:if test="${secPolInfo.sanc_cate == '0'}" >checked</c:if>><span>자동</span></label>
	    				<label><input type='radio' name='isautosanct' value='1' <c:if test="${secPolInfo.sanc_cate == '1'}" >checked</c:if>><span>수동</span></label>
    				</td>
    				<th>소명신청</th>
    				<td class="radio_button">
    					<label><input type='radio' name='isautoappr' value='0' <c:if test="${secPolInfo.appr_cate == '0'}" >checked</c:if>><span>자동</span></label> 
						<label><input type='radio' name='isautoappr' value='1' <c:if test="${secPolInfo.appr_cate == '1'}" >checked</c:if>><span>수동</span></label>
					</td>
    			</tr>
    			<tr class='is_notbuseo' style='display:<c:if test="${secPolInfo.buseo_indc == 'Y'}" >none</c:if>;'>	
    				<th>소명신청 결재라인</th>
    				<td >
    					<select id='appr_line_code' name='appr_line_code'>
    					<c:forEach var="result" items="${aList}" varStatus="status">
    						<option value='${result.minr_code }' <c:if test="${result.minr_code == secPolInfo.appr_line_code}" >selected</c:if>>${result.code_desc }</option>
    					</c:forEach>	
    					</select>
    				</td>
    				<th>소명파일 첨부여부</th>
    				<td class="radio_button">
    					<label><input type='radio' name='isfile' value='0' <c:if test="${secPolInfo.appr_attach_indc == '0'}" >checked</c:if>><span>첨부활성화</span></label>
    					<label><input type='radio' name='isfile' value='1' <c:if test="${secPolInfo.appr_attach_indc == '1'}" >checked</c:if>><span>첨부미활성화</span></label>
    				</td>	
    			</tr>
    			<tr style='display:;'>	
    				<th>소명신청 주의사항</th>
    				<td colspan='3'><input type='text' class="srh" id='noti' name='noti' style='width:99%;height:22px;border:1px solid #AEAEAE;' maxlength='1000' value='${secPolInfo.appr_note }' /></td>
    			</tr>
    			<tr class='is_notbuseo' style='display:<c:if test="${secPolInfo.buseo_indc == 'Y'}" >none</c:if>;'>
    				<th>PC지킴이 조치파라미터</th>
    				<td><input type='text' class="srh" id='exe_para' name='exe_para' style='width:99%;height:22px;border:1px solid #AEAEAE;' maxlength='100' value='<c:if test="${secPolInfo.ispcgact == 'Y'}" ><c:choose><c:when test="${secPolInfo.exe_para == '' }"><c:if test="${fn:length(secPolInfo.cond_field1) > 0 }" > ${fn:substring(secPolInfo.cond_field1, 1, fn:length(secPolInfo.cond_field1)) }</c:if></c:when><c:otherwise>${secPolInfo.exe_para }</c:otherwise></c:choose></c:if>' />
    					<input type='hidden' id='cond_field1' name='cond_field1' value='<c:if test="${fn:length(secPolInfo.cond_field1) > 0 }" >${fn:substring(secPolInfo.cond_field1, 1, fn:length(secPolInfo.cond_field1)) } </c:if>' />
    					<input type='hidden' id='ispcgact' name='ispcgact' value='${secPolInfo.ispcgact }' />
    				</td>
    				<th>제재조치 대상여부</th>
    				<td class="radio_button">
    					<label><input type='radio' name='sanc_indc' value='Y' <c:if test="${secPolInfo.sanc_indc == true }" >checked</c:if>><span>대상</span></label> 
    					<label><input type='radio' name='sanc_indc' value='N' <c:if test="${secPolInfo.sanc_indc == false }" >checked</c:if>><span>미대상</span></label>
    				</td>
    			</tr>
    			<tr>
    				<th>사용여부</th>
    				<td colspan='3' class="radio_button">
    					<label><input type='radio' name='isused' value='Y' <c:if test="${secPolInfo.use_indc == 'Y' }" >checked</c:if>><span>사용</span></label> 
    					<label><input type='radio' name='isused' value='N' <c:if test="${secPolInfo.use_indc == 'N' }" >checked</c:if>><span>사용중지</span></label> 
    				</td>
    			</tr>
    			<tr>
    				<th>활성화 날짜</th>
    				<td colspan='3'>
    					<input type="text"  class="srh" id="startDay" name="startDay" value="${secPolInfo.startDay }" class="srh" style="width:40%"> ~ 
                    	<input type="text"  class="srh" id="endDay" name="endDay" value="${secPolInfo.endDay }"  class="srh" style="width:40%"> 
    				</td>
    			</tr>
    			<tr>
    				<th class="ck_button">정책 적용 대상<br/>
    				<label><input name="collabor_indc" id="collabor_indc" type="checkbox" value="Y" <c:if test="${secPolInfo.collabor_indc == 'Y' }" >checked</c:if>> <span>협력사 포합 여부</span></label>
    				<a class='btn_scr3 btn_add_info' style="margin-top: 15px;"><span>대상자 추가</span></a>
    				</th>
    				<td colspan="3">
    					<table cellpadding="0" cellspacing="0" class="groupInfo tblInfoType3" style="width:80%; float: left;">
			                <colgroup>
								<col style="width:10">
								<col style="width:20">
								<col style="width:20">
								<col style="width:20">
							</colgroup>
							<tr>
								<th>유형</th>
								<th>이름</th>
								<th>코드</th>
								<th>삭제</th>
							</tr>
							<tbody class="target_list_body">
							<c:if test="${empty polTargetList}">
		                		<tr style="height:100px;"  is_all_send='Y'>
		                			<td style="text-align: center;" colspan="4">
		                			정책 대상이 없을 경우 전사 임직원이 정책 대상자가 됩니다.
		                			</td>
		                		</tr>
		                	</c:if>
							<c:forEach var="result" items="${polTargetList}" varStatus="status">
							<tr  is_all_send='N'>
								<td>${result.targetType }</td>								
								<td>${result.targetNm }</td>
								<td>${result.targetCode }</td>
								<td><a href="javascript:void(0);" onclick="$.btn_del('${result.targetCode }', this)" style="cursor:pointer;" ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
							</tr>
							</c:forEach>
							</tbody>
			            </table>
    					
    				</td>
    			</tr>	
    			<tr>
    				<th>로그조회 정보</th>
    				<td>
    					<input type='text' class="srh" readonly id='pol_table' name='pol_table' style='width:100%;height:22px;border:1px solid #AEAEAE;' value='${secPolInfo.sec_pol_table }' />
    				</td>
    				<td colspan="2">	
    					<input type='text' class="srh" readonly id='pol_columns' name='pol_columns' style='width:350px;height:22px;border:1px solid #AEAEAE;' value='${secPolInfo.sec_pol_table_col }' />
    					<a class='btn_scr3 btn_columns_info'><span>조회컬럼설정</span></a>
    				</td>
    			</tr>
    			<tr>
    				<th>비고</th>	
    				<td style='padding:5px 0 5px 10px;' colspan='3'><textarea id='bigo' name='bigo' maxlength='2000' style='width:99%;border:1px solid #AEAEAE;' rows='3'>${secPolInfo.bigo }</textarea></td>
    			</tr>
    			<tr>
    				<th>발생사유</th>	
    				<td style='padding:5px 0 5px 10px;' colspan='3'><textarea id='reason' name='reason' maxlength='2000' style='width:99%;border:1px solid #AEAEAE;' rows='3'>${secPolInfo.reason }</textarea></td>
    			</tr>
    			<tr>
    				<th>지수화정책 설명</th>	
    				<td style='padding:5px 0 5px 10px;' colspan='3'><textarea id='polnoti' name='polnoti' maxlength='2000' style='width:99%;border:1px solid #AEAEAE;' rows='10'>${secPolInfo.sec_pol_notice }</textarea></td>
    			</tr>
    			<tr>
					<th>지수화 정책 상세 내용</th>
					<td style='padding:5px 0 5px 10px;' colspan='3'><textarea  id='editor' name='editor' style='width:95%;' >${secPolInfo.sec_pol_notice_detail }</textarea></td>
					
    			<tr>
    			
    		</table>
    		<input type='hidden' name='buseoindc' id='buseoindc' value='${secPolInfo.buseo_indc }' />
    		<input type='hidden' name='critical_value_type' id='critical_value_type' value='1' />
            <div class="btn_black2"><a class="btn_black btn_policy_save"><span>저장</span></a>
            <a class="btn_black btn_cancel" style="cursor:pointer;" ><span>취소</span></a> 
            </div>
        </div>
		<input type="hidden" id="polId" name="polId" value="${searchVO.polId }" />
		<input type="hidden" name="searchType" id="searchType" value="${searchVO.searchType }" />
		<input type="hidden" name="searchKeyword" id="searchKeyword" value="${searchVO.searchKeyword }" />
		<input type="hidden" name="selectedRowId" id="selectedRowId" value="${searchVO.selectedRowId }" />
		<input type="hidden" name="majCode" id="majCode" value="${searchVO.majCode }" />
		<input type="hidden" name="minCode" id="minCode" value="${searchVO.minCode }" />
		<input type="hidden" name="polCode" id="polCode" value="${searchVO.polCode }" />
		<input type="hidden" name="pageIndex" id="pageIndex" value="${searchVO.pageIndex }" />
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