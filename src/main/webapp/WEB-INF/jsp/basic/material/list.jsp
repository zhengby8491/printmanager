<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/material/list.js?v=${v }"></script>
<title>材料信息</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${param.code}" placeholder="材料编号" id="code" name="code">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${materialName}" placeholder="输入材料名称" id="materialName" name="materialName">
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:material:create">
						<span>
							<a href="javascript:;" onclick="material_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加材料
							</a>
							<a href="javascript:;" onclick="import_info()" class="nav_btn table_nav_btn">导入</a>
							<shiro:hasPermission name="basic:material:batchDelete">
								<a href="javascript:;" id="batch_delete" class="nav_btn table_nav_btn">批量删除</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="basic:material:updateName">
								<a href="javascript:;" onclick="material_updateName()" id="updateName" class="nav_btn table_nav_btn">材料名称变更</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="offer:order:list">
								<a href="javascript:;" onclick="materialFromOffer()" class="nav_btn table_nav_btn">
									<i class="fa fa-plus-square"></i>
									来源报价系统
								</a>
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