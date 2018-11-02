<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="tableId" type="java.lang.String" required="false"%>
<!-- 列表表格-->
<div class="boot-mar">
	<table class="border-table resizable" id="<c:if test="${empty tableId }">bootTable</c:if><c:if test="${not empty tableId }">${tableId }</c:if>">
		<jsp:doBody></jsp:doBody>
	</table>
</div>
