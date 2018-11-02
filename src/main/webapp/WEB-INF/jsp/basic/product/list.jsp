<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/product/list.js?v=${v }"></script>
<title>产品信息列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
				<div id="innerdiv" style="position: absolute;">
					<img id="bigimg" style="border: 5px solid #fff;" src="" />
				</div>
			</div>
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 150px; height: 27px" value="${customerName}" placeholder="输入客户名称" id="customerName" name="customerName">
				<input type="text" class="input-txt" style="width: 150px; height: 27px" value="${customerMaterialCode}" placeholder="输入客户料号" id="customerMaterialCode" name="customerMaterialCode">
				<input type="text" class="input-txt" style="width: 150px; height: 27px" value="${productCode}" placeholder="输入产品编号" id="productCode" name="productCode">
				<input type="text" class="input-txt" style="width: 150px; height: 27px" value="${productName}" placeholder="输入成品名称" id="productName" name="productName">
				<input type="text" class="input-txt" style="width: 150px; height: 27px" value="${productStyle}" placeholder="输入产品规格" id="productStyle" name="productStyle">


				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:product:create">
						<span>
							<a href="javascript:;" onclick="product_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加产品
							</a>
							<a href="javascript:;" onclick="import_info()" class="nav_btn table_nav_btn">导入</a>
							<shiro:hasPermission name="basic:product:batchDelete">
								<a href="javascript:;" id="batch_delete" class="nav_btn table_nav_btn">批量删除</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="basic:product:updateName">
								<a href="javascript:;" onclick="product_updateName()" id="updateName" class="nav_btn table_nav_btn">产品名称变更</a>
							</shiro:hasPermission>
						</span>
					</shiro:hasPermission>
				</div>
				<!--表格内容-->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>