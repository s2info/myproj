<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<script src="/js/ckeditor/ckeditor.js" type="text/javascript"></script>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />

<script type="text/javaScript" language="javascript">
$(function () {		
	$("input[id=file]").change(function(){
		if($(this).val() !=""){
			var ext = $(this).val().split(".").pop().toLowerCase();
			if($.inArray(ext, ["jpg","jpeg","png"])== -1){
				alert("jpg, jpeg, png 파일만 업로드 해주세요.");
				$(this).val("");
				return;
			}
		}
		
	});
	
	
	
	$(".btn_save").click(function(){
		
		/* if($("#file").val() ==""){
			alert("설명 파일을 선택해 주세요.");
			$("#file").focus();
			return;
		} */
		
		var contents_explain = CKEDITOR.instances['editor'].getData();
		$("#explain").val(contents_explain);
		
		var contents_question = CKEDITOR.instances['editor2'].getData();
		$("#question").val(contents_question);
		
		
		if($("#questionNm").val() ==""){
			alert("문항 제목을 입력해 주세요.");
			$("#questionNm").focus();
			return;
		}
		
		if($("#ordr").val() ==""){
			alert("문항 순서를 입력해 주세요.");
			$("#ordr").focus();
			return;
		}
		
		
		if($("#explain").val() ==""){
			alert("문항을 입력해 주세요.");
			$("#explain").focus();
			return;
		}
		
		
		var fileYn = $('input:checkbox[name=checkFileYn]:checked').val();
		
		
		if($('input:checkbox[name=checkFileYn]:checked').length <= 0)
			fileYn = "N";
		
		
		$("#fileYn").val(fileYn);
		
		if(fileYn == "Y" && $("#question").val() !="") {
			alert("첨부파일 등록 여부 또는 문제 둘중에 하나만 선택 및 입력 하세요.");
			$("#explain").focus();
			return;	
		}
		
		
		/* if($("#question").val() !=""){
			if($("#answer").val() ==""){
				alert("답을 입력 하세요.");
				$("#answer").focus();
				return;	
			}
		} */
		
		
		$("input[name='u_exText']").each(function (i){
			var str = $("input[name='u_exText']").eq(i).val();
			//alert(str);
			var str2 = str.replace(/,/gi, "/");
			//console.log(str2);
			//alert(str2);
			if(str2.length > 0){
				$("input[name='u_exText']").eq(i).val(str2);
			}
		});
		
		$("input[name='exText']").each(function (i){
			var str = $("input[name='exText']").eq(i).attr("value");
			var str2 = str.replace(/,/gi, "/");
			if(str2.length >0 ){
				$("input[name='exText']").eq(i).val(str2);
			}
			//console.log(str2);
			
		});
		
		var answer_v =  $("#answer").val();
		var answer_r =  answer_v.replace(/ /gi, "");
		
		$("#answer").val(answer_r);
		
		var formData = $("#saveForm").serialize();
		
		
		 $.ajax({
			url: '/securityDay/sdQuestionInfoSave.do',
			data: formData,
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
					goForm("/securityDay/sdCheckListForm.do");	
				}else{alert(data.MSG); }				
			}
		});
		
	});
	
	
	$(".btn_delete").click(function(){
		
			
		var formData = $("#saveForm").serialize();
		
		if(!confirm('삭제 하시겠습니까?')){
			return false;
		}
		
		 $.ajax({
			url: '/securityDay/sdQuestionInfoDelete.do',
			data: formData,
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
					goForm("/securityDay/sdCheckListForm.do");
				}else{alert(data.MSG); }				
			}
		});
    
	});
	
	function goForm(url) {

	    document.saveForm.action = "<c:url value='"+url+"'/>";
	    document.saveForm.method = 'post';
	    document.saveForm.submit();
	}
	
	CKEDITOR.replace('editor', {
		filebrowserImageUploadUrl:'/securityDay/editorImageUpload.do',
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
	
	CKEDITOR.replace('editor2', {
		filebrowserImageUploadUrl:'/securityDay/editorImageUpload.do',
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
	
	$.btn_add  = function(){
		var trAdd ="";
		var exNum = (parseInt($("#exNumCnt").val()))+1;
		
		$("#exNumCnt").val(exNum);
		trAdd +="<tr>";
		trAdd +="<td><input type='text' class='srh'  style='width: 50px;' name='exNum' id='exNum' value='"+exNum+"'/></td>";
		trAdd +="<td><input type='text' class='srh'  style='width: 350px;'name='exText' id='exText' value=''/></td>";
		trAdd +='<td><a href="javascript:void(0);" onclick="$.btn_del(\'\',\'\', this)" style="cursor:pointer;" ><img src="/img/btn_delete.png" alt="delete" title="delete" /></a></td>';
		trAdd +="</tr>";
		
		$(".exInfo").append(trAdd);
	};
	
	$.answerType = function(){
		var answerType = $("#answerType").val();
		
		if(answerType == "2"){
			$("#type2").css("display", "");
			$.btn_add();
			$.btn_add();
		}else{
			$("#type2").css("display", "none");
		}
	};
	
	$.btn_del = function(exNum,type, obj){ 
		//alert("ddd");
		
		var exNum = exNum;
		var questionNum = $("#questionNum").val();
		var sdCheckNo = $("#sdCheckNo").val();
		
		//alert("orgInfo ="+orgInfo );
		
		if(type == "D"){
			var data = {
					sdCheckNo : sdCheckNo,
					questionNum : questionNum,
					exNum : exNum
			}
			
			
			 $.ajax({
				url: '/securityDay/sdQuestionMcDelete.do',
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
						$("#exNumCnt").val($("#exNumCnt").val()-1);
					}else{alert(data.MSG); }				
				}
			});
		}
		else{
			//alert("ee");
			$(obj).parent().parent().remove();
			$("#exNumCnt").val($("#exNumCnt").val()-1);
		}
    
	};
	
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
    <form:form id="saveForm" name="saveForm" method="post" enctype="multipart/form-data">
    	<div class="subTT"><span>Security Day 점검표 문항 상세 정보</span></div>
    	<div class="sch_view" style="margin-top: 5px;">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:35%;" />
                    <col style="width:15%;" />
                    <col style="width:35%;" />
                </colgroup>
                <tr>
					<th>문항 제목</th>
					<td>
						<input type='text' class ="srh" style='width:98%' id='questionNm' name='questionNm' value='${sdQuestionInfo.questionNm }' />
		        	</td>
		        	<th>순서</th>
					<td>
						<input type='text' class ="srh" style='width:98%' id='ordr' name='ordr' value='${sdQuestionInfo.ordr }' />
		        	</td>
				</tr>
                <tr> 
                    <th>문항</th>
                    <td colspan="3" style='padding:5px 0 5px 10px;'>
                    	<textarea name="editor" id="editor" cols="45" style='width:98%;' rows="20" class="inputarea" >${sdQuestionInfo.explain }</textarea>
						<input type="hidden" name="explain" id="explain"/>
                    </td>
                </tr> 
                <tr> 
                    <th>첨부파일 안내문구</th>
					<td>
						<input type='text' class ="srh" style='width:98%' id='fileText' name='fileText' maxlength="200" value='${sdQuestionInfo.fileText }' />
		        	</td>
		        	<th>첨부파일 등록 여부</th>
		        	<td class="ck_button">
		        		<label>
		        			<input name="checkFileYn" id="checkFileYn" type="checkbox" value="Y" <c:if test="${sdQuestionInfo.fileYn == 'Y' }" >checked</c:if>> 
		        			<span></span>
		        		</label>
		        	</td>
                </tr>                   
                <tr>
					<th>문제</th>
                     <td colspan="3" style='padding:5px 0 5px 10px;'>
                    	<textarea name="editor2" id="editor2" cols="45" style='width:98%;' rows="3" class="inputarea" >${sdQuestionInfo.question }</textarea>
						<input type="hidden" name="question" id="question"/>
                    </td>
				</tr>
				<tr>
					<th>답 유형</th>
					<td>
						<select  name="answerType" id="answerType" onchange="javascript:$.answerType()">
		            		<option value="">선택</option>
		                	<option value="1" <c:if test='${sdQuestionInfo.answerType == "1"}' >selected</c:if>>주관식</option>
							<option value="2" <c:if test='${sdQuestionInfo.answerType == "2"}' >selected</c:if>>객관식</option> 
						</select>
		        	</td>
					<th>답</th>
					<td>
						<input type='text' class ="srh" style='width:98%' id='answer' name='answer' value='${sdQuestionInfo.answer }' />
		        	</td>
				</tr>
				<tr id="type2" style="display: <c:if test="${(searchVO.qFormMod == 'U' and sdQuestionInfo.answerType == '1') or (searchVO.qFormMod == 'I' or sdQuestionInfo.answerType =='')}" > none</c:if>;">
					<th>보기 <br /><br />
						<a onclick="$.btn_add();"  class="btn_black"><span>추가</span></a>
					</th>
					<td colspan="3">
						
						<table cellpadding="0" cellspacing="0" class="tblInfoType3 exInfo" style="width:60%; float: left;">
			            	<colgroup>
			                	<col style="width:10%;" />
			                    <col style="width:75%;" />
			                    <col style="width:15%;" />
			                </colgroup>
			                <tr> 
			                    <th>번호</th>
								<th>내용</th>
								<th>삭제</th>
			               	</tr>
			               	<c:forEach var="result" items="${sdQuestionMcList}" varStatus="status">
							<tr>
								 <td><input type="text" class="srh"  style="width: 50px;" name="u_exNum" id="u_exNum" value="${result.exNum }" readonly="readonly"/></td>
			                    <td><input type="text" class="srh"  style="width: 350px;" name="u_exText" id="u_exText" value='${fn:replace(result.exText,"&",",") }'/></td>
								<td><a href="javascript:void(0);" onclick="$.btn_del('${result.exNum }','D', this)" style="cursor:pointer;" ><img src='/img/btn_delete.png' alt='delete' title='delete' /></a></td>
							</tr>
							</c:forEach>
			            </table>
			        </td>
                </tr>
				<%-- <tr>
					<th>답</th>
					<td>
						<input type='text' class ="srh" style='width:98%' id='answer' name='answer' value='${sdQuestionInfo.answer }' />
		        	</td>
				</tr> --%>
			</table>
			
			<div class="btn_black2">
			<c:if test='${searchVO.qFormMod eq "U"}'>
				<a class="btn_black btn_delete"><span>삭제</span></a>
			</c:if>
				<a class="btn_black btn_save"><span>저장</span></a>
				<a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
			
        </div>
        
       	<input type="hidden" id="sdCheckNo" name="sdCheckNo" value="${sdQuestionInfo.sdCheckNo }" />
       	<input type="hidden" id="questionNum" name="questionNum" value="${sdQuestionInfo.questionNum }" />
       	<input type="hidden" id="qFormMod" name="qFormMod" value="${searchVO.qFormMod }" />
       	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }" />
       <input type="hidden" id="exNumCnt" name="exNumCnt" value="${exNumCnt }" />
       <input type="hidden" id="fileYn" name="fileYn" value="${sdQuestionInfo.fileYn}" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>