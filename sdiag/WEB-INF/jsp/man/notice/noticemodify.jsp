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
	$('.btn_notice_save').click(function(){
		var titleCheck = document.getElementById('title');
		var contentsCheck = CKEDITOR.instances['editor'].getData();
		$("#contents").val(contentsCheck);
			
		if(titleCheck.value == "" || titleCheck.value == null){
			alert(" 제목을 입력 하십시오");
			titleCheck.focus();
			 return ;
		}
		if(contentsCheck == "" || contentsCheck == null){
			alert(" 내용을 입력하세요");
			contentsCheck.focus();
			 return ;
		}
		// 공지사항 DB등록
		if( confirm(" 저장 하시겠습니까?" ) ){
			document.listForm.action = "/man/noticeRegister.do";
			document.listForm.submit();
		}
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
	<%@ include file="/WEB-INF/jsp/cmm/noticeLeftMenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form commandName="searchVO" name="listForm" method="post">
    	<div class="subTT"><span>공지사항</span></div>
    	<div class="sch_view">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:85%;" />
                </colgroup>
                <tr> 
                    <th>제 목</th>
                    <td>
                        <input type="text" name="title" id="title" style="width:98%;" value="${borderInfo.title }" maxlength="200" class="srh">                             
                    </td>
                </tr>                    
                <tr>
					<th>내용</th>
					<td style='padding:5px 0 5px 10px;'>
						<textarea name="editor" id="editor" cols="45" rows="20" class="inputarea" >${borderInfo.contents }</textarea>
						<input type="hidden" name="contents" id="contents"/>
					</td>
				</tr>
				<tr> 
                    <th>상단 공지여부</th>
                    <td>
                        <select name='is_popup' id='is_popup'>
                        	<option value='Y' <c:if test="${borderInfo.is_popup == 'Y'}" >selected</c:if> >예</option>
                        	<option value='N' <c:if test="${borderInfo.is_popup == 'N'}" >selected</c:if>>아니오</option>
                        </select> * 로그인페이지 상단, 메뉴바 알림 공지 여부 입니다.                         
                    </td>
                </tr>
			</table>
            <div class="btn_black2"><a class="btn_black btn_notice_save"><span>저장</span></a>
            <a class="btn_black" href="javascript:history.back(-1);"><span>취소</span></a> 
            </div>
        </div>
       	<input type="hidden" id="status_cd" name="status_cd" value="0001" />
		<input type="hidden" id="sq_no" name="sq_no" value="<c:out value="${noticeVO.sq_no}" />" />
		<input type='text' style='display:none;' />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>