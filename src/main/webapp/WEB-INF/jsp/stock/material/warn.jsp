<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>材料库存预警</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/warn.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料库存-材料库存预警"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:order:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成采购单</button>
					</shiro:hasPermission>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info"></div>

			</div>
			<!--查询表单End-->
			<!--表格Start-->
			<div>
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>