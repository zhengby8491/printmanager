<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/begin/customer/create.js?v=${v }"></script>
<title>客户期初新增</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-期初设置-客户期初"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="begin:customer:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="begin:customer:create,begin:customer:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
				</div>
			</div>

			<form id="form_order" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">期初日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="beginTime" name="beginTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-noborder">
										<input readonly="readonly" type="text" class="input-txt input-txt_1" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input readonly="readonly" type="text" class="input-txt input-txt_3" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审核日期：</label>
									<span class="ui-combo-wrap">
										<input readonly="readonly" type="text" class="input-txt input-txt_3" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审 核 人：</label>
									<span class="ui-combo-noborder">
										<input readonly="readonly" type="text" class="input-txt input-txt_1" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 833px"></textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--从表-订单表格-->

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="customer_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择客户
					</a>
				</div>
				<!--按钮栏End-->

				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="100">操作</th>
									<th width="120">收款日期</th>
									<th width="120">客户编号</th>
									<th width="180">客户名称</th>
									<th width="80">币别</th>
									<th width="100">应收款</th>
									<th width="100">预收款</th>
									<th width="200">备注</th>
								</tr>
							</thead>
							<tbody id="tbody">

							</tbody>
							<tfoot class="total">
								<tr>
									<td></td>
									<td>合 计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>