<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购订单列表</title>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<script type="text/javascript" src="${ctxStatic}/site/purch/order/list.js?v=${v}"></script>
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
							<sys:dateConfine label="制单日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" value="${supplierName}" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">采购单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" type="text" class="input-txt input-txt_3" value="${billNo }" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<c:if test="${auditflag!='true' }">
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
						</c:if>
						<c:if test="${auditflag=='true' }">
							<label>
								<input type="radio" value="-1" name="auditFlag" />
								全部
							</label>
							<label>
								<input type="radio" value="YES" name="auditFlag" />
								已审核
							</label>
							<label>
								<input type="radio" value="NO" name="auditFlag" checked="checked" />
								未审核
							</label>
						</c:if>
					</div>
				</div>
			</div>
			<!--查询表单END-->
			<!-- 按钮栏Start -->
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="purch:order:create">
					<span>
						<a href="javascript:add()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							新增采购订单
						</a>
					</span>
				</shiro:hasPermission>
			</div>
			<!--按钮栏End-->
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>