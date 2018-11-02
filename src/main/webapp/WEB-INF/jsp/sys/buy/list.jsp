<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>产品管理</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/buy/list.js?${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<div class="cl">
				<div class="cl order-info" style="width: 1140px">
					<dl class="cl row-dl">

					</dl>

				</div>
				<div>


					<div class="btn-bar">
						<span>
							<a href="javascript:;" class="nav_btn table_nav_btn" onclick="product_create()">
								<i class="fa fa-plus-square"></i>
								新增版本
							</a>
						</span>

					</div>

					<div class="search_container">
						<!--表格-->
						<div>
							<div class="boot-mar">
								<table class="border-table" id="bootTable">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
</body>
</html>