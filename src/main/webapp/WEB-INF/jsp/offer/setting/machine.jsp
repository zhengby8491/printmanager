<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/machine.js?v=${v}"></script>

<print:body>
	<!-- 导航部分 -->
	<print:nav title="报价系统-报价设置-${offerType.text}-机台设置" topNav="false">
	</print:nav>
	<print:listLeft>
		<%@include file="/WEB-INF/jsp/offer/setting/_menuSetting.jsp"%>
	</print:listLeft>

	<print:listRight>
		<!-- 列表查询 -->
		<print:listSearchCenter>
			<input type="text" class="input-txt" style="width: 250px; height: 27px" placeholder="输入机台名称" id="machineName" name="machineName">
		</print:listSearchCenter>

		<print:table>
			<!-- 列表按钮 -->
			<print:listBtnBar>
				<shiro:hasOfferPermissions name="machine:create" offerType="${offerType }">
					<print:listBtn name="新增机台" icon="fa-plus-square" click="machineCreate('${offerType}')"></print:listBtn>
				</shiro:hasOfferPermissions>
			</print:listBtnBar>

			<!-- 列表表格 -->
			<print:listTable></print:listTable>
		</print:table>
	</print:listRight>
</print:body>
</html>