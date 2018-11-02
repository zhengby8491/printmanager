<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/bflute.js?v=${v}"></script>

<print:body>
	<!-- 导航部分 -->
	<print:nav title="报价系统-报价设置-${offerType.text}-坑纸设置" topNav="false">
	</print:nav>
	<print:listLeft>
		<%@include file="/WEB-INF/jsp/offer/setting/_menuSetting.jsp"%>
	</print:listLeft>
	<print:listRight>
		<!-- 列表按钮 -->
		<print:listBtnBar>
			<shiro:hasOfferPermissions name="bflute:create" offerType="${offerType }">
				<print:listBtn name="手动添加材料" click="bfluteCreate('${offerType }')"></print:listBtn>
				<print:listBtn name="添加选择材料" click="bfluteCreateByMaterial()"></print:listBtn>
				<print:listBtn name="手动批量添加材料" click="bfluteCreateByBatch('${offerType }')"></print:listBtn>
			</shiro:hasOfferPermissions>
			<shiro:hasOfferPermissions name="bflute:delbatch" offerType="${offerType }">
				<print:listBtn name="批量删除" click="bfluteDelByBatch()"></print:listBtn>
			</shiro:hasOfferPermissions>
			<%-- <print:listBtn name="同步到材料基础资料" click="synToBasic()"></print:listBtn> --%>
		</print:listBtnBar>

		<!-- 列表表格 -->
		<print:listTable>
			<input name="offerType" type="hidden" value="${offerType }">
		</print:listTable>
	</print:listRight>
</print:body>
</html>