<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="formId" type="java.lang.String" required="false"%>
<%@ attribute name="tableId" type="java.lang.String" required="false"%>
<%@ attribute name="style" type="java.lang.String" required="false"%>
<%@ attribute name="useClass" type="java.lang.Boolean" required="false"  %>
<!-- 表格内容 -->
<form id="${formId }">
	<div class="<c:if test="${null == useClass || useClass == true }">fold_wrap</c:if>" style="${style}">
		<div class="<c:if test="${null == useClass || useClass == true }">fold_table</c:if> ">
			<table class="border-table resizable table-hover table-striped" id="${tableId }" rules="all">
				<jsp:doBody></jsp:doBody>
			</table>
		</div>
	</div>
</form>
