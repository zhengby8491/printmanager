<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/machine/list.js?v=${v }"></script>
<title>机台信息</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${machineName}" placeholder="输入机台名称" id="machineName" name="machineName">
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:machine:create">
						<span>
							<a href="javascript:;" onclick="machine_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加机台信息
							</a>
							<shiro:hasPermission name="basic:machine:batchDelete">
								<a href="javascript:;" id="batch_delete" class="nav_btn table_nav_btn">批量删除</a>
							</shiro:hasPermission>
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