<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

		<div class="left_menu">
			  <%=request.getAttribute("reportLeftMenu") %>
		</div>
		<script type="text/javascript">
			$(function() {
				$("#orgbrowser").treeview({
					collapsed: false,
					animated: "fast",
					unique: true,
					persist: "location"
				});
				<c:if test="${isuser == '1'}" >
				$('#orgbrowser').children().css('pointer-events', 'none');
				</c:if>
			})
			
		</script>
