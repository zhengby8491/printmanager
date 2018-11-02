<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/material.js "></script>
<meta charset="UTF-8">
<title>生产补料</title>
<style type="text/css">
input.jump-to:HOVER
{
	color: red;
	text-decoration: underline;
	cursor: pointer;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/supplement/view.js?v=${v}"></script>
</head>
<body>
<input type="hidden" id="orderDetailList" value='${fns:toJson(order.detailList)}'>
	<input id="orderId" value="${order.id}" type="hidden">
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-生产补料-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isCheck eq 'NO'}">
						<shiro:hasPermission name="stock:materialSupplement:edit">
							<button class="nav_btn table_nav_btn" id="btn_edit" value="${order.id }">修改</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:materialSupplement:audit">
							<button class="nav_btn table_nav_btn" id="btn_audit" value="${order.id }">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:materialSupplement:del">
							<button class="nav_btn table_nav_btn" id="btn_del" value="${order.id }">删除</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${order.isCheck eq 'YES'}">
						<shiro:hasPermission name="stock:materialSupplement:audit_cancel">
							<button class="nav_btn table_nav_btn" id="btn_audit_cancel" value="${order.id }">反审核</button>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="stock:materialSupplement:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">仓库名称：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfo('WAREHOUSE',order.warehouseId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">生产单号：</label>
								<span class="ui-combo-noborder">
									<input type="text" class="input-txt input-txt_9 jump-to" readonly="readonly" value="${order.workBillNo }" />
									<a style="display:none;" class="jump-to" onclick="jumpTo('${pageContext.request.contextPath}/print/produce/work/toView/${order.workBillNo }','生产工单')"></a>
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">补料单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_6" readonly="readonly" value="${order.billNo }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">补料日期：</label>
								<span class="ui-combo-wrap">
									<input id="supplementTime" name="supplementTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.supplementTime}' type='date' />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">领 料 人：</label>
								<span class="ui-combo-wrap">
									<input name="receiveEmployeeId" type="text" class="input-txt input-txt_9" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.receiveEmployeeId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">发 料 人：</label>
								<span class="ui-combo-wrap">
									<input name="sendEmployeeId" type="text" class="input-txt input-txt_6" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.sendEmployeeId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">制 单 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.createName }" />
								</span>
							</dd>

							<dd class="row-dd">
								<label class="form-label label_ui label_1">制单日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" name="memo" style="width: 873px" readonly="readonly">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>

			<!--表格部分Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->

			<!--审核标签-->
			<c:if test="${order.isCheck.value==true }">
				<div class="review">
					<span class="review_font">已审核</span>
				</div>
			</c:if>

		</div>
	</div>
</body>
</html>