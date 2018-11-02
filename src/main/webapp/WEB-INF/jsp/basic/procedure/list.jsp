<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/procedure/list.js?v=${v }"></script>
<title>工序信息</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${procedureName}" placeholder="输入工序名称" id="procedureName" name="procedureName">
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--按钮栏-->
			<div class="btn-bar">
				<shiro:hasPermission name="basic:procedure:create">
					<span>
						<a href="javascript:;" onclick="procedure_add()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							添加工序
						</a>
						<a href="javascript:;" onclick="import_info()" class="nav_btn table_nav_btn">导入</a>
						<shiro:hasPermission name="basic:procedure:batchDelete">
							<a href="javascript:;" id="batch_delete" class="nav_btn table_nav_btn">批量删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="basic:procedure:updateName">
							<a href="javascript:;" onclick="procedure_updateName()" id="updateName" class="nav_btn table_nav_btn">工序名称变更</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="offer:order:list">
							<a href="javascript:;" onclick="procedureFromOffer()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								来源报价系统
							</a>
						</shiro:hasPermission>
					</span>
				</shiro:hasPermission>
			</div>
			<!--表格-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
		</div>
	</div>
</body>
</html>