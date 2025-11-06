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
	// href="/man/noticelistDelete.do?sq_no=${borderInfo.sq_no }"
   $('.btn_faq_delete').click(function(){
	   $('#sq_no').val($(this).attr('sq_no')); 

	  if(!confirm('삭제하시겠습니까?')){
		  return false;
		  
	  } 
	 
	 document.listForm.action = "/man/qnaListDelete.do";
	 document.listForm.submit();
	  
   });
   $.answerSave = function(sqNo){
	   $('#sq_no').val(sqNo); 
	   
	   $("#answer_contents").val(CKEDITOR.instances['editor'].getData());
	   
	   if($("#answer_contents").val() == "" || $("#answer_contents").val() == null){
			alert(" 내용을 입력하세요");
			contentsCheck.focus();
			 return ;
		}
		// FAQ DB등록
		if( confirm(" 저장 하시겠습니까?" ) ){
			 document.listForm.action = "/man/qnaAnswerInfoUpdate.do";
			 document.listForm.submit();
		}
	   
	  
	   
   };
   var editor = '${btn_answer_show}';
   if(editor == "T"){
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
   }
	
 	$("#content").each(function(){
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	});
	
	$("#answerContents").each(function(){
		var $this = $(this);
		var t = $this.text();
		$this.html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
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
    	<div class="subTT"><span>Q&A</span></div>
    	<div class="sch_view">
            <table class="tblInfoType" cellpadding="0" cellspacing="0">
            	<colgroup>
                	<col style="width:15%;" />
                    <col style="width:85%;" />
                </colgroup>
                <tr>
					<th>제목</th>
					<td>${qnaInfo.title}</td>
				</tr>
				<tr>
					<th>등록일</th>
					<td><c:out value="${qnaInfo.upd_date}" /></td>
				</tr>
				
				<tr style='min-height:300px;'>
					<th>내용</th>
					<td id="content" style='vertical-align:top;padding-top:5px;'>${qnaInfo.contents}</td>
				</tr>
				<c:if test="${btn_answer_show == 'T'}" >
				<tr>
					<th>답변 내용</th>
					<td style='padding:5px 0 5px 10px;'>
						<textarea name="editor" id="editor" cols="45" rows="20" class="inputarea" >${qnaInfo.answer_contents }</textarea>
						<input type="hidden" name="answer_contents" id="answer_contents"/>
					</td>
				</tr>
				</c:if>
				<c:if test="${btn_is_show == 'T' and not empty qnaInfo.answer_contents and btn_answer_show != 'T'}" >
				<tr>
					<th>답변 내용</th>
					<td  id="answerContents" style='vertical-align:top;padding-top:5px;'>${qnaInfo.answer_contents }</td>
				</tr>
				</c:if>
			</table>
            <div class="btn_black2"><a class="btn_black" href="javascript:history.back(-1);"><span><c:choose><c:when test="${searchVO.n_type=='DASH'}">뒤로</c:when><c:otherwise>목록</c:otherwise></c:choose></span></a>
            <c:if test="${btn_is_show == 'T' and empty qnaInfo.answer_contents}" >
            <a class="btn_black" href="/man/qnaModify.do?sq_no=${qnaInfo.sq_no }"><span>수정</span></a>
            </c:if>
            <c:if test="${btn_answer_show == 'T'}" >
            <a class="btn_black" href="javascript:$.answerSave('${qnaInfo.sq_no }')"><span>답변 저장</span></a>
            </c:if>
            <c:if test="${btn_is_show == 'T' or btn_answer_show == 'T' }" >
            <a class="btn_black btn_faq_delete" sq_no='${qnaInfo.sq_no }'><span>삭제</span></a> 
            </c:if>
            </div>
        </div>
       	<input type="hidden" name="pageIndex" id="pageIndex" value="<c:out value="${searchVO.pageIndex}" />" />
		<input type="hidden" id="sq_no" name="sq_no" value="0" />
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>