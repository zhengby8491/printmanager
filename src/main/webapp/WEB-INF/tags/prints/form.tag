<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="cusClass" type="java.lang.String" required="false"  %>
<!-- 表单 -->
<div <c:if test="${empty cusClass }">class="form-container"</c:if>
		 <c:if test="${not empty cusClass }">class="${cusClass }"</c:if>  >
	<!-- 				<div class="form-wrap"></div> -->
	<jsp:doBody></jsp:doBody>
</div>