<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<meta charset="UTF-8">
<title>生产补料</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/supplement/edit.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-生产补料-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:materialSupplement:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:materialSupplement:edit,stock:materialSupplement:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_stockMaterial" method="post">
				<input name="id" type="hidden" readonly="readonly" value="${order.id }" />
				<input name="workId" id="workId" type="hidden" value="${order.workId }" />
				<input name="workBillType" id="workBillType" type="hidden" value="${order.workBillType }" />
				<input name="billType" id="billType" type="hidden" value="${order.billType }">
				<input type="hidden" name="isCheck" id="isCheck" />
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO"/>
				<!--头部END-->
				<!--查询表单START-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" name="warehouseId"
										  onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="input-txt input-txt_1 hy_select2" selected="${order.warehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_6">生产单号：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly="readonly" class="input-txt input-txt_9" id="workBillNo" name="workBillNo" value="${order.workBillNo }" />
										<div class="select-btn" id="workBillNo_quick_select">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">补料单号：</label>
									<span class="ui-combo-wrap">
										<input name="billNo" type="text" class="input-txt input-txt_6" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">补料日期：</label>
									<span class="ui-combo-wrap">
										<input id="supplementTime" name="supplementTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.supplementTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_6">领 料 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_12 hy_select2" name="receiveEmployeeId" textProperty="name" selected="${order.receiveEmployeeId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">发 料 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_6 hy_select2" name="sendEmployeeId" textProperty="name" selected="${order.sendEmployeeId }"></phtml:list>
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
										<textarea class="noborder" name="memo" style="width: 895px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
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

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<button id="material_quick_select" type="button" class="nav_btn table_nav_btn" value="${order.warehouseId}">
						<i class="fa fa-plus-square"></i>
						选择材料
					</button>
				</div>
				<!--按钮栏End-->

				<!--表格部分START-->
				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="60"></th>
									<th width="140">生产单号</th>
									<th width="160">材料编号</th>
									<th width="140">材料名称</th>
									<th width="100">材料规格</th>
									<th width="60">克重</th>
									<th width="60">单位</th>
									<th width="80">补料数量</th>
									<shiro:hasPermission name="stock:materialSupplement:money">
										<th width="80">成本单价</th>
										<th width="100">金额</th>
									</shiro:hasPermission>
									<th>备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody id="tbody">
								<c:forEach items="${order.detailList }" var="item" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage">
											<i class="delete fa fa-trash-o"></i>
										</td>
										<td>
											<input name="detailList.workBillNo" class="tab_input" type="text" readonly="readonly" value="${item.workBillNo }" />
										</td>
										<td>
											<input name="detailList.code" class="tab_input id_${item.id }" type="text" readonly="readonly" value="${item.code }" />
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${item.materialName }" />
										<td>
											<input name="detailList.specifications" class="tab_input" readonly="readonly" type="text" value="${item.specifications }" />
										</td>
										<td>
											<input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${item.weight }" />
										</td>
										<td>
											<input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).name}" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="<fmt:formatNumber value="${item.qty }" type="currency" pattern="0.####"/>" />
										</td>

										<td>
											<input name="detailList.price" class="tab_input bg_color" type="text" value="<fmt:formatNumber value="${item.price }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.money }" type="currency" pattern="0.####"/>" />
										</td>

										<td>
											<input name="detailList.memo" onmouseover="this.title=this.value" class="tab_input bg_color memo" type="text" value="${item.memo }" />
											<input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${item.id }" />
											<input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialId }" />
											<input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.valuationUnitId }" />
											<input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).accuracy}" />
											<input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).accuracy}" />
											<input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.stockUnitId).accuracy}" />
											<input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="${item.stockUnitId }" />
											<input name="detailList.materialClassId" class="tab_input" type="hidden" readonly="readonly" value="${item.materialClassId }" />
											<input name="detailList.valuationQty" class="tab_input" type="hidden" readonly="readonly" value="${item.valuationQty }" />
											<input name="detailList.valuationUnitName" class="tab_input" type="hidden" readonly="readonly" value="${fns:basicInfo('UNIT',item.valuationUnitId).name}" />
											<input name="detailList.valuationPrice" class="tab_input" type="hidden" value="${item.valuationPrice }" />
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