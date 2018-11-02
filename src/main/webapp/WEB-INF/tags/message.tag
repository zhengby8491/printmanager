<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、danger、loading"%>

<c:if test="${not empty content}">
	<c:if test="${not empty type}">
		<c:set var="ctype" value="${type}" />
	</c:if>
	<c:if test="${empty type}">
		<c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'danger'}" />
	</c:if>
	<script type="text/javascript">
    Helper.message.warn("${content}");
	</script>
</c:if>