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
<script type="text/javascript" src="${ctxStatic}/site/purch/refund/create.js?v=${v}"></script>
</head>
<body>
<input type="hidden" value='${supplierJson}' id="supplierJson">
<input type="hidden" value='${detailListJson}' id="detailListJson">
<div style="display: none;" id="phtml">
<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouseId" textProperty="name"></phtml:list><input name="detailList.warehouse" value="' + _THIS.warehouseId + '">
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购退货-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:refund:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="purch:refund:create,purch:refund:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
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
										<input type="text" class="input-txt input-txt_7" name="supplierName" id="supplierName" readonly="readonly" value="${purchRefund.supplierName}" />
										<div class="select-btn" id="selectSupplier">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="linkName" Class="input-txt input-txt_3" id="linkName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="hidden" name="employeeId" id="employeeId" />
										<input type="text" class="input-txt input-txt_1" readonly="readonly" name="employeeName" id="employeeName" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="returnGoodsType" selected="${purchRefund.returnGoodsType}" textProperty="text" cssClass="input-txt input-txt_1 hy_select2" type="com.huayin.printmanager.persist.enumerate.ReturnGoodsType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" textProperty="name" name="settlementClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" textProperty="name" name="paymentClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 907px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
							<input id="supplierId" type="hidden" name="supplierId" value="${purchRefund.supplierId }">
							<input type="hidden" name="rateId" value="${purchRefund.rateId }">
							<input type="hidden" name="taxRateId" value="${purchRefund.taxRateId }">
							<input type="hidden" name="isCheck" id="isCheck">
						</div>
					</div>
				</div>
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<button id="stock_quick_select" type="button" class="nav_btn table_nav_btn" value="${purchRefund.supplierId }">选择采购入库</button>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 2200px" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
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
										<!-- <i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i> -->
										<!-- <div class="batch_box_container"></div> -->
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

							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<!-- 判断是否有权限查看金额 ---begin -->
						<dd class="row-dd">
							<label class="form-label label_ui label_1">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.totalTax }" />
							</span>
						</dd>
						<!-- 判断是否有权限查看金额 ---end -->
						<dd class="row-dd">
							<label class="form-label label_ui label_1">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" />
								<input id="currencyTypeText" type="text" class="input-txt input-txt_10" readonly="true" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.createName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchRefund.createTime}' type='date' />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_1" readonly="readonly" value="${purchRefund.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_10" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchRefund.checkTime}' type='date' />" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_wareHouse_box" class="batch_wareHouse_box">
					<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" cssClass="tab_input bg_color input-txt_3 hy_select2 batch_wareHouse_select" valueProperty="id" name="" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>

