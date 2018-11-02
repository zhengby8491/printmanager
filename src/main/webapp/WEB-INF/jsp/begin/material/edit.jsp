<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<script type="text/javascript" src="${ctxStatic}/site/stock/material.js "></script>
<script type="text/javascript" src="${ctxStatic}/site/begin/material/edit.js?v=${v }"></script>
<title>材料期初</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-期初设置-材料期初"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="begin:material:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="begin:material:edit,begin:material:audit">
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
										<input type="text" name="billNo" readonly="readonly" class="input-txt input-txt_3" value="${order.billNo }" />
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
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3" value="${order.createName }" />
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
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" 
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" name="warehouseId" selected="${order.warehouseId }" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 1005px">${order.memo }</textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="material_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						新增材料
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
									<th width="120">材料编号</th>
									<th width="120">材料名称</th>
									<th width="100">材料规格</th>
									<th width="60">克重</th>
									<th width="80">库存单位</th>
									<th width="80">期初数量</th>
									<th width="100">计价单价</th>
									<th width="80">计价单位</th>
									<th width="80">计价数量</th>
									<th width="80">库存单价</th>
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
											<input name="detailList.materialCode" class="tab_input id_${item.materialId }" type="text" readonly="readonly" value="${item.materialCode }" />
											<input name="detailList.id" class="tab_input" type="text" readonly="readonly" value="${item.id }" />
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${item.materialName }" />
											<input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialId }" />
											<input name="detailList.materialClassId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialClassId }" />
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input bg_color" type="text" value="${item.specifications }" />
										</td>
										<td>
											<input name="detailList.weight" readonly="readonly" class="tab_input" type="text" value="${item.weight }" />
										</td>
										<td>
											<input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.stockUnitId }" />
											<input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).name}" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="${item.qty }" />
										</td>
										<td>
											<input name="detailList.valuationPrice" class="tab_input constraint_decimal bg_color" type="text" value="${item.valuationPrice }" />
										</td>
										<td>
											<input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.valuationUnitId }" />
											<input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).name}" />
										</td>
										<td>
											<input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="${item.valuationQty }" />
										</td>
										<td>
											<input name="detailList.price" class="tab_input bg_color" type="text" value="${item.price }" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="${item.money }" />
											<input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).accuracy}" />
											<input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).accuracy}" />
										</td>
										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${item.memo }" />
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