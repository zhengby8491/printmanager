<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>材料分切</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/split/list.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>

			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="分切日期" />
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">分切单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">领料仓：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_3 hy_select2" items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" defaultValue="" defaultOption="请选择" name="warehouseId"></phtml:list>
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui">材料名称：</label>
							<span class="ui-combo-wrap form_text">
								<input id="materialName" type="text" class="input-txt input-txt_13" name="materialName" value="${materialName }" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">规格：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" name="specifications" id="specifications" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">分切类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list name="splitType" textProperty="text" cssClass="hy_select2 input-txt_3" type="com.huayin.printmanager.persist.enumerate.SplitType" defaultValue="-1" defaultOption="请选择"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
						</label>
					</div>
				</div>
			</div>
			<!-- 按钮栏Start -->
			<div class="btn-bar" id="toolbar" style="margin-bottom: 0">
				<shiro:hasPermission name="stock:materialSplit:create">
					<a href="javascript:;" class="nav_btn table_nav_btn" onclick="toCreate()">
						<i class="fa fa-plus-square"></i>
						新增材料分切
					</a>
				</shiro:hasPermission>
			</div>
			<!--按钮栏End-->
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>