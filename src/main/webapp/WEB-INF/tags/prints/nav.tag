<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="title" type="java.lang.String" required="false"%>
<%@ attribute name="topNav" type="java.lang.Boolean" required="false"%>
<!-- 导航按钮 -->
<div class="cl">
	<div class="iframe-top">
		<sys:nav struct="${title }"></sys:nav>
	</div>
	<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
		<div id="innerdiv" style="position: absolute;">
			<img id="bigimg" style="border: 5px solid #fff;" src="" />
		</div>
	</div>
	<c:if test="${topNav != false }">
		<div class="top_nav">
			<jsp:doBody></jsp:doBody>
		</div>
	</c:if>
	
</div>