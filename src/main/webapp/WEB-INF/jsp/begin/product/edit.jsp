<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/begin/product/edit.js?v=${v }"></script>
<title>产品期初</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-期初设置-产品期初"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="begin:product:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="begin:product:edit,begin:product:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
				</div>
			</div>

			<form id="form_order" method="post">
				<input type="hidden" name="id" value="${order.id }" />
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">单据编号：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" name="billNo" class="input-txt input-txt_3" value="${order.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">期初日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="beginTime" name="beginTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.beginTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审核日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审 核 人：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" 
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" name="warehouseId" cssClass="input-txt input-txt_1 hy_select2" selected="${order.warehouseId }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 1004px">${order.memo }</textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="product_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						新增产品
					</a>
				</div>
				<!--按钮栏End-->

				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="60">序号</th>
									<th width="100">操作</th>
									<th width="120">产品编号</th>
									<th width="120">客户料号</th>
									<th width="120">成品名称</th>
									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="80">期初数量</th>
									<th width="60">单价</th>
									<th width="100">金额</th>
									<th width="200">备注</th>
								</tr>
							</thead>
							<tbody id="tbody">
								<c:forEach var="item" items="${order.detailList }" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage">
											<a title="删除行" href="javascript:void(0)" class="row_delete">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td>
											<input name="detailList.productCode" class="tab_input" type="text" readonly="readonly" value="${item.productCode }" />
										</td>
										<td>
											<input name="detailList.customerMaterialCode" class="tab_input" type="text" readonly="readonly" value="${item.customerMaterialCode }" />
										</td>
										<td>
											<input name="detailList.productName" class="tab_input" type="text" value="${item.productName }" />
											<input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${item.id }" />
											<input name="detailList.productId" class="tab_input id_${item.productId }" type="hidden" value="${item.productId }" />
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input" type="text" value="${item.specifications}" />
										</td>
										<td>
											<input name="detailList.unitName" readonly="readonly" class="tab_input" type="text" value="${fns:basicInfo('UNIT',item.unit).name}" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value="${item.qty }" />
										</td>
										<td>
											<input name="detailList.price" class="tab_input constraint_decimal bg_color" type="text" value="${item.price }" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input" readonly="readonly" type="text" value="${item.money }" />
										</td>
										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" type="text" value="${item.memo }" />
											<input name="detailList.unit" type="hidden" value="${item.unit}" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>