﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/deliveryClass/list.js?v=${v }"></script>
<title>送货方式</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${deliveryClassName}" placeholder="输入送货方式名称" id="deliveryClassName" name="deliveryClassName">
				<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:deliveryClass:create">
						<span>
							<a href="javascript:;" onclick="delivery_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加送货方式
							</a>
						</span>
					</shiro:hasPermission>
				</div>
				<!-- 表格内容 -->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>