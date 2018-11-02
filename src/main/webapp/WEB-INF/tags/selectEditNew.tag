<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="id" type="java.lang.String" required="false"%>
<%@ attribute name="inputCss" type="java.lang.String" required="false"%>
<%@ attribute name="selStyle" type="java.lang.String" required="false"%>
<%@ attribute name="selCss" type="java.lang.String" required="false"%>
<%@ attribute name="name" type="java.lang.String" required="false"%>
<%@ attribute name="defaultVal" type="java.lang.String" required="false"%>
<input id="${id }" name="${name }" type="text" class="edit_sel ${inputCss }" value="${defaultVal}" />
<select <c:if test="${empty selStyle }">style="min-width: 55px;height:95%;"</c:if> <c:if test="${not empty selStyle }"> style="${selStyle }"</c:if> class="sel ${selCss }">
	<jsp:doBody></jsp:doBody>
</select>

<script type="text/javascript">
	$(function()
	{
		$(document).on("change", ".sel", function()
		{
			var selVal = $(this).val();
			$(this).prev("input").val(selVal);
		});
	})
</script>