<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/product.js "></script>
<meta charset="UTF-8">
<title>成品其它入库</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/otherIn/view.js?v=${v}"></script>
</head>
<body>
	<input id="orderDetailList" value='${fns:toJson(order.detailList)}' type="hidden">
	<input id="orderId" value="${order.id}" type="hidden">
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-成品品其它入库-查看"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isCheck eq 'NO'}">
						<shiro:hasPermission name="stock:productOtherin:edit">
							<button class="nav_btn table_nav_btn" id="btn_edit" value="${order.id }">修改</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:productOtherin:audit">
							<button class="nav_btn table_nav_btn" id="btn_audit" value="${order.id }">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:productOtherin:del">
							<button class="nav_btn table_nav_btn" id="btn_del" value="${order.id }">删除</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${order.isCheck eq 'YES'}">
						<shiro:hasPermission name="stock:productOtherin:audit_cancel">
							<button class="nav_btn table_nav_btn" id="btn_audit_cancel" value="${order.id }">反审核</button>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="stock:productOtherin:print">
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
									<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${fns:basicInfo('WAREHOUSE',order.warehouseId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">入库单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${order.billNo }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">入库日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1 Wdate" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.inTime}' type='date' />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">入 库 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" />
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
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime}' type='date' />" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" name="memo" readonly="readonly" style="width: 1010px">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>

			<!--查询表单END-->

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