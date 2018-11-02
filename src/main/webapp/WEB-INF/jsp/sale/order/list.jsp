<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>销售订单表</title>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/list.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="customerName" name="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="customerBillNo" id="customerBillNo" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="billNo" id="billNo" />
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
			<!-- 按钮栏Start -->
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="sale:order:create">
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" onclick="create()">
							<i class="fa fa-plus-square"></i>
							新增销售订单
						</a>
					</span>
				</shiro:hasPermission>
			</div>
			<!--按钮栏End-->

			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable"></table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>