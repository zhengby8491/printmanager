<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="permission" type="java.lang.String" required="false" description="权限字符串"%>
<%@ attribute name="icon" type="java.lang.String" required="false" description="按钮icon"%>
<%@ attribute name="click" type="java.lang.String" required="false" description="按钮事件"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="按钮名称"%>
<!-- 按钮 -->
<c:if test="${not empty permission }">
	<shiro:hasPermission name="${permission }">
		<span>
			<a href="javascript:;" class="nav_btn table_nav_btn" onclick="${click}">
				<i class="fa ${icon }"></i>
				${name }
			</a>
		</span>
	</shiro:hasPermission>
</c:if>
<c:if test="${empty permission }">
	<span>
		<a href="javascript:;" class="nav_btn table_nav_btn" onclick="${click}">
			<i class="fa ${icon }"></i>
			${name }
		</a>
	</span>
</c:if>
