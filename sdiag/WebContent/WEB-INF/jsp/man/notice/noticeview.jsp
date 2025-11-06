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


<script type="text/javaScript" language="javascript">
$(function () {		
	// href="/man/noticelistDelete.do?sq_no=${borderInfo.sq_no }"
   $('.btn_notice_delete').click(function(){
	   $('#sq_no').val($(this).attr('sq_no')); 
		
	  if(!confirm('삭제하시겠습니까?')){
		  return false;
		  
	  } 
	 
	 document.listForm.action = "/man/noticelistDelete.do";
	 document.listForm.submit();
	  
   });
	
	$("#content").each(function(){
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
                        ${borderInfo.title}                             
                    </td>
                </tr>                    
                <tr>
					<th>등록일</th>
					<td><c:out value="${borderInfo.upd_date}" /></td>
				</tr>
				<tr style='min-height:300px;'>
					<th>내 용</th>
					<td id="content" style='vertical-align:top;padding-top:5px;'>${borderInfo.contents}</td>
				</tr>
				<c:if test="${btn_is_show == 'T'}" >
				<tr>
					 <th>상단 공지여부</th>
					<td>
						<c:choose><c:when test="${borderInfo.is_popup == 'Y' }">	
							예
						</c:when>
						<c:otherwise>
							아니오
						</c:otherwise></c:choose>
					</td>
				</tr>
				</c:if>
			</table>
            <div class="btn_black2"><a class="btn_black" href="javascript:history.back(-1);"><span><c:choose><c:when test="${searchVO.n_type=='DASH'}">뒤로</c:when><c:otherwise>목록</c:otherwise></c:choose></span></a>
            <c:if test="${btn_is_show == 'T'}" >
            <a class="btn_black" href="/man/noticeRegisterPage.do?sq_no=${borderInfo.sq_no }&flag=update"><span>수정</span></a>
            <a class="btn_black btn_notice_delete" sq_no='${borderInfo.sq_no }' style='cursor:pointer;'><span>삭제</span></a> 
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