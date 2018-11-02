<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<meta charset="UTF-8">
<title>材料库存调拨</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/transfer/edit.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料库存调拨-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:materialTransfer:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:materialTransfer:edit,stock:materialTransfer:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_transfer" method="post">
				<input type="hidden" name="id" value="${order.id }" /> <input type="hidden" name="isCheck" id="isCheck" /> <input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调出仓库：</label> <span class="ui-combo-wrap wrap-width"> <phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" name="outWarehouseId" onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="input-txt input-txt_1 hy_select2" selected="${order.outWarehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调入仓库：</label> <span class="ui-combo-wrap wrap-width"> <phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" name="inWarehouseId" onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="input-txt input-txt_1 hy_select2" selected="${order.inWarehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调拨单号：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_6" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调拨日期：</label> <span class="ui-combo-wrap"> <input name="transferTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.transferTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调 拨 人：</label> <span class="ui-combo-noborder"> <phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="employeeId" textProperty="name" selected="${order.employeeId }"></phtml:list> <input id="employeeName" name="employeeName" type="hidden" value="${fns:basicList('EMPLOYEE')[0].name}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_6" readonly="readonly" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label> <span class="form_textarea"> <textarea name="memo" class="noborder" style="width: 640px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em> /100
										</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--从表-订单表格-->
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<button id="material_quick_select" type="button" class="nav_btn table_nav_btn" value="${order.outWarehouseId }">
						<i class="fa fa-plus-square"></i> 选择材料
					</button>
				</div>
				<!--按钮栏End-->

				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="140">材料编号</th>
									<th width="120">材料名称</th>
									<th width="100">材料规格</th>
									<th width="60">克重</th>
									<th width="60">单位</th>
									<th width="80">调拨数量</th>
									<th width="80">计价单位</th>
									<th width="80">计价数量</th>
									<shiro:hasPermission name="stock:materialTransfer:money">
										<th width="80">计价单价</th>
										<th width="80">成本单价</th>
										<th width="100">调拨金额</th>
									</shiro:hasPermission>
									<th width="200">备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody id="tbody">
								<c:forEach items="${order.detailList }" var="item" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage"><i class="delete_row fa fa-trash-o"></i></td>
										<td><input name="detailList.code" class="tab_input id_${item.stockMaterialId }" type="text" readonly="readonly" value="${item.code }" /></td>
										<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${item.materialName }" />
										<td><input name="detailList.specifications" class="tab_input" readonly="readonly" type="text" value="${item.specifications }" /></td>
										<td><input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${item.weight }" /></td>
										<td><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).name}" /></td>
										<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="<fmt:formatNumber value="${item.qty }" type="currency" pattern="0.####"/>" /></td>
										<td><input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).name}" /></td>
										<td><input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.valuationQty }" type="currency" pattern="0.####"/>" /></td>

										<td><input name="detailList.valuationPrice" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.valuationPrice }" type="currency" pattern="0.####"/>" /></td>
										<td><input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.price }" type="currency" pattern="0.####"/>" /></td>
										<td><input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.money }" type="currency" pattern="0.####"/>" /></td>
										<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${item.memo }" /></td>
										<td style="display: none"><input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${item.id }" /></td>
										<td style="display: none"><input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialId }" /></td>
										<td style="display: none"><input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.valuationUnitId }" /></td>
										<td style="display: none"><input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).accuracy}" /></td>
										<td style="display: none"><input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).accuracy}" /></td>
										<td style="display: none"><input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.stockUnitId }" /></td>
										<td style="display: none"><input name="detailList.stockMaterialId" class="tab_input" type="hidden" readonly="readonly" value="${item.stockMaterialId }" /></td>
										<td style="display: none"><input name="detailList.materialClassId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialClassId }" /></td>
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