<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购退货</title>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/purch.js?v=" +${v}></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/refund/edit.js?v=${v}"></script>
</head>
<body>
<div style="display:none;" id="phtml">
<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouseId" textProperty="name"></phtml:list><input name="detailList.warehouse" value="' + _THIS.warehouseId + '">
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购退货-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:refund:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>

					<shiro:hasManyPermissions name="purch:refund:edit,purch:refund:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>

					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>

				</div>
			</div>
			<!--主表-订单表单-->
			<form id="form_order" method="post">
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" readonly="readonly" value="${purchRefund.supplierName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="linkName" id="linkName" class="input-txt input-txt_3" value="${purchRefund.linkName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input id="mobile" type="text" class="input-txt input-txt_1" name="mobile" value="${purchRefund.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="returnGoodsType" selected="${purchRefund.returnGoodsType}" cssClass="input-txt input-txt_1 hy_select2 " textProperty="text" type="com.huayin.printmanager.persist.enumerate.ReturnGoodsType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="hidden" name="employeeId" id="employeeId" value="${purchRefund.employeeId }" />
										<input type="text" class="input-txt input-txt_1" readonly="readonly" name="employeeName" id="employeeName" value="${purchRefund.employeeName }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" value="${purchRefund.supplierAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2 " name="settlementClassId" textProperty="name" selected="${purchRefund.settlementClassId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2 " name="paymentClassId" textProperty="name" selected="${purchRefund.paymentClassId }"></phtml:list>
									</span>
								</dd>


								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货单号：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备 注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 907px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${purchRefund.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
							<input type="hidden" name="billType" value="${purchRefund.billType }">
							<input type="hidden" name="isCancel" value="${purchRefund.isCancel }">
							<input type="hidden" name="isCheck" id="isCheck">
							<input type="hidden" name="supplierId" value="${purchRefund.supplierId }">
							<input type="hidden" name="rateId" value="${purchRefund.rateId }">
							<input id="id" type="hidden" name="id" value="${purchRefund.id }">
							<input type="hidden" name="billNo" value="${purchRefund.billNo }">
							<input type="hidden" name="companyId" value="${purchRefund.companyId }">
							<input type="hidden" name="printCount" value="${purchRefund.printCount }">
						</div>
					</div>
				</div>
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<button id="stock_quick_select" type="button" class="nav_btn table_nav_btn" value="${purchRefund.supplierId }">来源采购入库</button>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1600px" id="detailList">
							<thead>
								<tr>
									<th width="30" name="seq">序号</th>
									<th width="40" name="operator"></th>
									<th width="100" name="code">材料编号</th>
									<th width="160" name="materialName">材料名称</th>
									<th width="100" name="specifications">材料规格</th>
									<th width="60" name="weight">克重</th>
									<th width="60" name="purchUnitName">采购单位</th>
									<th width="80" name="qty">退货数量</th>
									<th width="100" name="reconcilQty" style="display: none">已对账数量</th>
									<th width="80" name="valuationUnitName">计价单位</th>
									<th width="80" name="valuationQty">计价数量</th>
									<shiro:hasPermission name="purch:refund:money">
										<th width="60" name="valuationPrice">单价</th>
										<th width="100" width="" name="money">金额</th>
										<th width="80" name="tax">税额</th>
									</shiro:hasPermission>
									<th width="120" name="taxRateId">
										税收
										<!--<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i> -->
										<!--<div class="batch_box_container"></div> -->
									</th>
									<th width="80" name="percent" style="display: none">税率值</th>
									<th width="80" name="warehouseId" width="60">
										仓库
										<i id="batch_edit_wareHouse" class="fa fa-edit" src="batch_wareHouse_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" name="price" style="display: none">库存单价</th>
									<th width="80" name="sourceBillType">源单类型</th>
									<th width="100" name="sourceBillNo">源单单号</th>
									<th width="100" name="productNames">成品名称</th>
									<th width="80" name="sourceQty" style="display: none">源单数量</th>
									<th width="100" name="noTaxMoney" style="display: none">不含税金额</th>
									<th width="100" name="noTaxPrice" style="display: none">不含税单价</th>
									<th width="100" name="orderBillNo" style="display: none">采购单号</th>
									<th width="200" name="memo">备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody>
								<c:forEach items="${purchRefund.detailList}" var="item" varStatus="status">
									<tr>
										<td>${status.index+1 }</td>
										<td class="td-manage">
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
										</td>
										<td class="pru_number">
											<input name="detailList.code" class="tab_input code" type="text" readonly="readonly" value="${item.code}" />
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${item.materialName }" />
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input" type="text" readonly="readonly" value="${item.specifications}" />
										</td>
										<td>
											<input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${item.weight }" />
										</td>
										<td>
											<input name="detailList.purchUnitName" class="tab_input" type="text" readonly="readonly" value="${item.purchUnitName }" />
										</td>
										<td>
											<input name="detailList.qty" class="bg_color tab_input qty constraint_decimal" type="text" value="<fmt:formatNumber value="${item.qty }" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.reconcilQty" readonly="readonly" class="tab_input" type="text" value="<fmt:formatNumber value="${item.reconcilQty}" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${item.valuationUnitName }" />
										</td>
										<td>
											<input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.valuationQty }" type="currency" pattern="0.####"/>" />
										</td>

										<td>
											<input name="detailList.valuationPrice" class="bg_color tab_input price constraint_decimal" type="text" value="<fmt:formatNumber value="${item.valuationPrice }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input money" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.money }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.tax" class="tab_input tax" type="text" readonly="readonly" value="${item.tax }" />
										</td>

										<td>
											<input name="detailList.taxRateName" readonly="readonly" class="tab_input" type="text" value="${fns:basicInfo('TAXRATE',item.taxRateId).name}" />
											<input name="detailList.taxRateId" type="hidden" value="${item.taxRateId }">
										</td>
										<td style="display: none">
											<input name="detailList.percent" class="tab_input" type="text" readonly="readonly" value="${item.percent }" />
										</td>
										<td>
											<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" name="detailList.warehouseId" textProperty="name" selected="${item.warehouseId}" cssClass="bg_color tab_input"></phtml:list>
										</td>
										<td style="display: none">
											<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.price }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<c:if test="${!empty item.sourceBillType}">
												<input class="tab_input" type="text" readonly="readonly" value="${item.sourceBillType.text }" />
											</c:if>
										</td>
										<td>
											<input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="readonly" value="${item.sourceBillNo}" />
										</td>
										<td>
											<input name="detailList.productNames" class="tab_input" type="text" readonly="readonly" value="${item.productNames}" />
										</td>
										<td style="display: none">
											<input name="detailList.sourceQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.sourceQty}" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxMoney" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.noTaxMoney }" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxPrice" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.noTaxPrice }" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.orderBillNo" class="tab_input" type="text" readonly="readonly" value="${item.orderBillNo}" />
											<input name="detailList.workBillNo" type="hidden" value="${item.workBillNo }"/>
											<input name="detailList.workId" type="hidden" value="${item.workId }"/></td>
										</td>
										<td>
											<input name="detailList.memo" onmouseover="this.title=this.value" class="bg_color tab_input memo" type="text" value="${item.memo }" />
											<input type="hidden" name="detailList.materialId" value="${item.materialId}">
											<input type="hidden" name="detailList.unitId" value="${item.unitId}">
											<input type="hidden" name="detailList.valuationUnitId" value="${item.valuationUnitId}">
											<input type="hidden" name="detailList.sourceId" value="${item.sourceId}">
											<input type="hidden" name="detailList.sourceBillType" value="${item.sourceBillType}">
											<input type="hidden" name="detailList.sourceDetailId" value="${item.sourceDetailId}" />
											<input type="hidden" name="detailList.id" value="${item.id}" />
											<input type="hidden" name="detailList.noTaxValuationPrice" value="${item.noTaxValuationPrice}" />
											<input type="hidden" name="detailList.masterId" value="${item.masterId}">
											<input type="hidden" name="detailList.isForceComplete" value="${item.isForceComplete}" />
											<input type="hidden" name="detailList.materialClassId" value="${item.materialClassId}" />
											<input name="valuationUnitAccuracy" type="hidden" value="${item.valuationUnitAccuracy}" />
											<input name="stockUnitAccuracy" type="hidden" value="${item.stockUnitAccuracy}" />
										</td>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.totalTax }" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.createName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchRefund.createTime}' type='date' />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${purchRefund.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchRefund.checkTime}' type='date' />" />
							</span>
						</dd>
					</dl>
				</div>
			</form>
		</div>
	</div>
	<div id="batch_wareHouse_box" class="batch_wareHouse_box">
		<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" cssClass="tab_input bg_color input-txt_3 hy_select2 batch_wareHouse_select" valueProperty="id" name="detailList.warehouseId" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
	</div>
	<div id="batch_taxRate_box" class="batch_taxRate_box">
		<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 batch_taxRate_select" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" onchange="batchEditTaxRate()" />
	</div>
</body>
</html>

