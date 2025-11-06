<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<script type="text/javascript">
/* if(${dto.result} || '${dto.result}'=='')
	window.parent.CKEDITOR.tools.callFunction(${dto.CKEditorFuncNum}, '${dto.imageUrl}', '이미지 업로드가 완료 되었습니다.');
else
	alert("허용되지 않은 파일 유형입니다.");
 */	
if('${dto.result}'=='false')
	alert("허용되지 않은 파일 유형입니다.");
else
	window.parent.CKEDITOR.tools.callFunction(${dto.CKEditorFuncNum}, '${dto.imageUrl}', '이미지 업로드가 완료 되었습니다.');
</script>