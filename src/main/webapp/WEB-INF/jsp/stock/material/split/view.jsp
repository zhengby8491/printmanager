<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/material.js "></script>
<meta charset="UTF-8">
<title>材料分切</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/split/view.js?v=${v}"></script>
</head>
<body>
<input type="hidden" id="orderDetailList" value='${fns:toJson(order.detailList)}'>
	<input id="orderId" value="${order.id}" type="hidden">
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料分切-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isCheck eq 'NO'}">
						<shiro:hasPermission name="stock:materialSplit:edit">
							<button class="nav_btn table_nav_btn" id="btn_edit" value="${order.id }">修改</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:materialSplit:audit">
							<button class="nav_btn table_nav_btn" id="btn_audit" value="${order.id }">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="stock:materialSplit:del">
							<button class="nav_btn table_nav_btn" id="btn_del" value="${order.id }">删除</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${order.isCheck eq 'YES'}">
						<shiro:hasPermission name="stock:materialSplit:audit_cancel">
							<button class="nav_btn table_nav_btn" id="btn_audit_cancel" value="${order.id }">反审核</button>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="stock:materialSplit:print">
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
			<div style="margin: 5px 0">
				<div style="border: 1px solid #e2e2e2">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">仓 库：</label>
								<span class="ui-combo-wrap">
									<input type="text" name="billNo" class="input-txt input-txt_9" readonly="readonly" value="${order.warehouseName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">分切类型：</label>
								<span class="ui-combo-wrap">
									<input type="text" name="billNo" class="input-txt input-txt_1" readonly="readonly" value="${order.splitTypeText }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">分切单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" name="billNo" class="input-txt input-txt_3" readonly="readonly" value="${order.billNo }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">分切日期：</label>
								<span class="ui-combo-wrap">
									<input id="splitTime" type="text" name="splitTime" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.splitTime }' type='date' />" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">材料名称：</label>
								<span class="ui-combo-wrap wrap-width">
									<input type="text" class="input-txt input-txt_6" readonly="readonly" id="materialName" name="materialName" value="${order.materialName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">规格：</label>
								<span class="ui-combo-wrap">
									<input type="text" id="specifications" name="specifications" class="input-txt input-txt_1" readonly="readonly" value="${order.specifications }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">单位：</label>
								<span class="ui-combo-wrap">
									<input type="text" id="stockUnitName" class="input-txt input-txt_3" readonly="readonly" value="${order.stockUnitName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">数量：</label>
								<span class="ui-combo-wrap">
									<input type="text" id="qty" name="qty" class="input-txt input-txt_1" readonly="readonly" value="${order.qty }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">制 单 人：</label>
								<span class="ui-combo-wrap" class="form_text">
									<input name="createName" type="text" class="input-txt input-txt_9" readonly="readonly" value="${order.createName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">制单日期：</label>
								<span class="ui-combo-wrap" class="form_text">
									<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">计价单位：</label>
								<span class="ui-combo-wrap">
									<input id="valuationUnitName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.valuationUnitName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">计价数量：</label>
								<span class="ui-combo-wrap">
									<input type="text" id="valuationQty" name="valuationQty" class="input-txt input-txt_1" readonly="readonly" value="${order.valuationQty }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea name="memo" class="noborder" style="width: 803px" readonly="readonly" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
									<p class="textarea-numberbar">
										<em>0</em>
										/100
									</p>
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