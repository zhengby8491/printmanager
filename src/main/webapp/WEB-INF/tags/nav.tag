<%-- 使用方法:<sys:nav/>--%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="struct" type="java.lang.String" required="false" description="导航内容，父子目录以逗号隔开"%>
<div>
	<c:if test="${not empty struct}">
		<i class="fa fa-home"></i>
		<c:forEach var="item" items="${fn:split(struct, '-')  }" varStatus="sta">
			<c:if test="${!sta.first }">
				<span>&gt; </span>
			</c:if>
				${item }
			</c:forEach>
	</c:if>
</div>