<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购对账</title>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=${v}"></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/purch.js?v=${v}"></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/reconcil/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购对账-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:reconcil:create">
						<button id="btn_purch_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>

					<shiro:hasManyPermissions name="purch:reconcil:create,purch:reconcil:audit">
						<button id="btn_purch_save_audit" class="nav_btn table_nav_btn">保存并审核</button>
					</shiro:hasManyPermissions>

					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>

				</div>
			</div>
			<!--主表-订单表单-->
			<form id="purchReconcilAction" method="post">
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_20" readonly="readonly" name="supplierName" value="${purchReconcil.supplierName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">

										<input type="text" class="input-txt input-txt_3" name="linkName" value="${purchReconcil.linkName}" />

									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" value="${purchReconcil.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" id="reconcilTime" name="reconcilTime" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchReconcil.reconcilTime}' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="hidden" name="employeeId" id="employeeId" value="${purchReconcil.employeeId }" />
										<input type="text" class="input-txt input-txt_3" readonly="readonly" name="employeeName" id="employeeName" value="${purchReconcil.employeeName }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" value="${purchReconcil.supplierAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="settlementClassId" textProperty="name" selected="${purchReconcil.settlementClassId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="paymentClassId" textProperty="name" selected="${purchReconcil.paymentClassId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 923px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${purchReconcil.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
							<input type="hidden" name="billType" value="${purchReconcil.billType }">
							<input type="hidden" name="isCancel" value="${purchReconcil.isCancel }">
							<input type="hidden" name="isCheck" id="isCheck">
							<input type="hidden" name="supplierId" value="${purchReconcil.supplierId }">
							<input type="hidden" name="rateId" value="${purchReconcil.rateId }">
							<input type="hidden" name="printCount" value="${purchReconcil.printCount }">
						</div>
					</div>
				</div>
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" id="detailList">
							<thead>
								<tr>
									<th width="30">序号</th>
									<th width="40"></th>
									<th width="120">材料编号</th>
									<th width="120">材料名称</th>
									<th width="100">材料规格</th>
									<th width="60">克重</th>
									<th width="60">单位</th>
									<th width="80">对账数量</th>
									<th width="60">计价单位</th>
									<th width="80">计价数量</th>
									<shiro:hasPermission name="purch:reconcil:money">
										<th width="60">单价</th>
										<th width="80">金额</th>
										<th width="60">税额</th>
									</shiro:hasPermission>
									<th width="100">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="60" style="display: none">税率值</th>
									<th width="60" style="display: none">库存单价</th>
									<th width="80">源单类型</th>
									<th width="140">源单单号</th>
									<th width="100">成品名称</th>
									<th width="140" style="display: none">采购单号</th>
									<th width="140" style="display: none">生产工单号</th>
									<th width="80" style="display: none">源单数量</th>
									<th width="80" style="display: none">不含税金额</th>
									<th width="80" style="display: none">不含税单价</th>
									<th width="100">出/入库日期</th>
									<th width="200">备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody>
								<c:forEach items="${purchReconcil.detailList}" var="item" varStatus="status">
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
											<input name="detailList.specifications" readonly="readonly" class="tab_input" type="text" value="${item.specifications}" />
										</td>
										<td>
											<input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${item.weight }" />
										</td>
										<td>
											<input name="detailList.purchUnitName" class="tab_input" type="text" readonly="readonly" value="${item.purchUnitName }" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input bg_color qty constraint_decimal" type="text" value="<c:if test="${item.sourceBillType=='PURCH_PR' }">-</c:if><fmt:formatNumber value="${item.qty }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${item.valuationUnitName }" />
										</td>
										<td>
											<input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.valuationQty }" type="currency" pattern="0.####"/>" />
										</td>
										<!-- 判断是否有权限查看金额---begin  -->
										<td>
											<input name="detailList.valuationPrice" class="tab_input price constraint_decimal bg_color" type="text" value="<fmt:formatNumber value="${item.valuationPrice }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input money" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.money }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.tax" class="tab_input tax" type="text" readonly="readonly" value="${item.tax }" />
										</td>
										<!-- 判断是否有权限查看金额---end  -->
										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" textProperty="name" name="detailList.taxRateId" cssClass="tab_input" selected="${item.taxRateId }" />
										</td>
										<td style="display: none">
											<input name="detailList.percent" class="tab_input" type="text" readonly="readonly" value="${item.percent }" />
										</td>
										<td style="display: none">
											<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.price }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input class="tab_input" type="text" readonly="readonly" value="${item.sourceBillType.text }" />
										</td>
										<td>
											<input class="tab_input" name="detailList.sourceBillNo" type="text" readonly="readonly" value="${item.sourceBillNo}" />
										</td>
										<td>
											<input class="tab_input" name="detailList.productNames" type="text" readonly="readonly" value="${item.productNames}" />
										</td>
										<td style="display: none">
											<input class="tab_input" name="detailList.orderBillNo" type="text" readonly="readonly" value="${item.orderBillNo}" />
										</td>
										<td style="display: none">
											<input class="tab_input" name="detailList.workBillNo" type="text" readonly="readonly" value="${item.workBillNo }" />
										</td>
										<td style="display: none">
											<input class="tab_input" name="detailList.sourceQty" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.sourceQty }" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxMoney" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.noTaxMoney }" type="currency" pattern="0.####"/>" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxPrice" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${item.noTaxPrice }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.deliveryTime" class="tab_input" type="text" readonly="readonly" value="<fmt:formatDate value="${item.deliveryTime }" type="date" pattern="yyyy-MM-dd"/>" />
										</td>
										<td>
											<input class="bg_color tab_input memo" name="detailList.memo" type="text" value="${item.memo }" />
											<input type="hidden" name="detailList.materialId" value="${item.materialId}">
											<input type="hidden" name="detailList.materialClassId" value="${item.materialClassId}">
											<input type="hidden" name="detailList.unitId" value="${item.unitId}">
											<input type="hidden" name="detailList.valuationUnitId" value="${item.valuationUnitId}">
											<input type="hidden" name="detailList.sourceId" value="${item.sourceId}">
											<input type="hidden" name="detailList.id" value="${item.id}">
											<input type="hidden" name="detailList.masterId" value="${item.masterId}">
											<input type="hidden" name="detailList.sourceBillType" value="${item.sourceBillType}">
											<input type="hidden" name="detailList.sourceDetailId" value="${item.sourceDetailId}" />
											<input type="hidden" name="detailList.noTaxValuationPrice" value="${item.noTaxValuationPrice}" />
											<input type="hidden" name="detailList.isForceComplete" value="${item.isForceComplete}" />
											<input type="hidden" name="detailList.workBillNo" value="${item.workBillNo}" />
											<input type="hidden" name="detailList.workId" value="${item.workId}" />
											<input name="valuationUnitAccuracy" type="hidden" value="${item.valuationUnitAccuracy}" />
											<input name="stockUnitAccuracy" type="hidden" value="${item.stockUnitAccuracy}" />
											<!-- 标记来自印刷家的订单，页面无逻辑依赖 -->
											<input name="detailList.saveQty" type="hidden" value="${item.qty }"/>
											<input name="detailList.saveMoney" type="hidden" value="${item.money }"/>
											<input name="detailList.saveValuationPrice" type="hidden" value="${null == item.valuationPrice? 0: item.valuationPrice}"/>
											<input name="detailList.extOrderId"  type="hidden" value="${item.extOrderId}"/>
											<input name="detailList.extOrderDetailId"  type="hidden" value="${item.extOrderDetailId}"/>
										</td>
									</tr>

									<c:set var="i" value="${status.index+1 }"></c:set>
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
								<input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_6" readonly="readonly" value="${purchReconcil.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_6" readonly="readonly" value="${purchReconcil.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_6" readonly="readonly" value="${purchReconcil.totalTax }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" value="${purchReconcil.currencyType}" />
								<input type="text" class="input-txt input-txt_6" readonly="true" value="${purchReconcil.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_6" readonly="readonly" value="${purchReconcil.createName} " />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_6" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchReconcil.createTime}' type='date' />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_6" readonly="readonly" value="${purchReconcil.checkUserName }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_6" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${purchReconcil.checkTime}' type='date' />" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="taxRateId" selected="${purchReconcil.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>
