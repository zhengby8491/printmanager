<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/supplier/list.js?v=${v }"></script>
<title>供应商信息</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<%-- <sys:dateConfine  label="日期区间"/> --%>
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${code}" placeholder="输入供应商编号" id="code" name="code">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${supplierName}" placeholder="输入供应商名称" id="supplierName" name="supplierName">
				<label for="isOem">
					<input type="checkbox" placeholder="代工平台" id="isOem" name="isOem">
					代工平台
				</label>
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<div class="btn-bar">
				<shiro:hasPermission name="basic:supplier:create">
					<span>
						<a href="javascript:;" onclick="supplier_add()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							添加供应商
						</a>
						<a href="javascript:;" onclick="import_info()" class="nav_btn table_nav_btn">导入</a>
						<shiro:hasPermission name="basic:supplier:batchDelete">
							<a href="javascript:;" id="batch_delete" class="nav_btn table_nav_btn">批量删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="basic:supplier:updateName">
							<a href="javascript:;" onclick="supplier_updateName()" id="updateName" class="nav_btn table_nav_btn">供应商名称变更</a>
						</shiro:hasPermission>
					</span>
				</shiro:hasPermission>
			</div>
			<div>
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