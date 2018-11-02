<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=${v}"></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/purch.js?v=${v}"></script>
<meta charset="UTF-8">
<title>采购订单</title>
<script type="text/javascript" src="${ctxStatic}/site/purch/order/create.js?v=${v}"></script>
</head>
<body data-iscopy="${empty isCopy ? false : true }">
<div style="display: none;" id="taxRateId">
<phtml:list name="detailList.taxRateId" items="${fns:basicList2('TAXRATE')}" valueProperty="id" textProperty="name" cssClass="bg_color input-txt tab_input " />
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购订单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:order:create">
						<button id="btn_purch_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>

					<shiro:hasManyPermissions name="purch:order:create,purch:order:audit">
						<button class="nav_btn table_nav_btn" id="btn_purch_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>

					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
				</div>
			</div>
			<form action="${ctx}/purch/order/create" id="form_order" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="readonly" id="supplierName" name="supplierName" value="${order.supplierName }" />
										<input type="hidden" id="supplierId" name="supplierId" value="${order.supplierId }" />
										<div class="select-btn" id="selectSupplier">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui label_1">交货日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1 Wdate" readonly="readonly" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' ,onpicked:function(dp){$('input[name=\'detailList.deliveryTime\']').val($(this).val())}})" name="purchTime" id="purchTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="hidden" name="employeeId" id="employeeId" value="${order.employeeId }" />
										<input type="text" class="input-txt input-txt_3" readonly="readonly" name="employeeName" id="employeeName" value="${order.employeeName }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" value="${order.supplierAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" textProperty="name" selected="${order.deliveryClassId }" name="deliveryClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" textProperty="name" selected="${order.settlementClassId }" name="settlementClassId" cssClass="input-txt input-txt_3 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" textProperty="name" selected="${order.paymentClassId }" name="paymentClassId" cssClass="input-txt input-txt_3 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 937px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
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

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="material_quick_select" href="javascript:;" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择材料
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" id="detailList" style="width:900px">
							<thead>
								<tr>
									<th width="30" name="seq">序号</th>
									<th width="30" name="operator"></th>
									<th width="70" name="code">材料编号</th>
									<th width="120" name="materialName">材料名称</th>
									<th width="100" name="specifications">材料规格</th>
									<th width="50" name="weight">克重</th>
									<th width="50" name="purchUnitName">采购单位</th>
									<th width="80" name="qty">采购数量</th>
									<th width="50" name="valuationUnitName">计价单位</th>
									<th width="80" name="valuationQty">计价数量</th>

									<shiro:hasPermission name="purch:order:money">
										<th width="60" name="valuationPrice">单价</th>
										<th width="80" name="money">金额</th>
										<th width="80" name="tax">税额</th>
									</shiro:hasPermission>

									<th width="100" name="taxRateId">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" name="percent" style="display: none">税率值</th>
									<th width="70" name="price" style="display: none">库存单价</th>
									<th width="80" name="deliveryTime">
										交货日期
										<i id="batch_edit_deliveryTime" class="fa fa-edit" src="batch_deliveryTime_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" name="sourceBillType">源单类型</th>
									<th width="100" name="sourceBillNo">生产单号</th>
									<th width="80" name="sourceQty" style="display: none">源单数量</th>
									<th width="100" name="storageQty" style="display: none">已入库数量</th>
									<th width="100" name="noTaxMoney" style="display: none">不含税金额</th>
									<th width="100" name="noTaxPrice" style="display: none">不含税单价</th>
									<th width="100" name="productNames">成品名称</th>
									<th width="200" name="memo">备注</th>
								</tr>

							</thead>
							<tbody id="tbody">
								<c:forEach items="${detailList}" var="item" varStatus="status">
									<tr name="tr">
										<td>${status.index+1 }</td>
										<td class="td-manage">
											<input name="detailList.parentId" type="hidden" value="${item.parentId}" />
											<input name="detailList.workMaterialType" type="hidden" value="${item.workMaterialType}" />
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
										</td>
										<td>
											<input name="detailList.code" class="tab_input code" type="text" readonly="readonly" value="${item.code}" />
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${item.materialName }" />
										</td>
										<td>
											<input name="detailList.specifications" class="bg_color tab_input" type="text" value="${item.specifications}" />
										</td>
										<td>
											<input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${item.weight }" />
										</td>
										<td>
											<input name="detailList.purchUnitName" class="tab_input" type="text" readonly="readonly" value="${item.purchUnitName }" />
										</td>
										<td>
											<input name="detailList.qty" class="bg_color tab_input constraint_decimal" type="text" value="<fmt:formatNumber value="${item.qty }" type="currency" pattern="0.####"/>" />
											<input name="detailList.saveQty" type="hidden" value="${item.qty }"/>
										</td>
										<td>
											<input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${item.valuationUnitName }" />
										</td>
										<td>
											<input name="detailList.valuationQty" class="tab_input valuationQty" type="text" readonly="readonly" />
										</td>

										<%-- 	<shiro:hasPermission name="purch:order:money">
 --%>
										<td>
											<input name="detailList.valuationPrice" class="bg_color tab_input constraint_decimal" type="text" value="${null == item.valuationPrice? 0: item.valuationPrice}" />
											<input name="detailList.saveValuationPrice" type="hidden" value="${null == item.valuationPrice? 0: item.valuationPrice}"/>
										</td>
										<td>
											<input name="detailList.money" class="tab_input money" type="text" readonly="readonly" value="0"/>
											<input name="detailList.saveMoney" type="hidden" value="${item.money }"/>
										</td>
										<td>
											<input name="detailList.tax" class="tab_input tax" type="text" readonly="readonly" />
										</td>
										<%-- </shiro:hasPermission> --%>

										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${item.taxRateId }" cssClass="bg_color input-txt tab_input" />
										</td>
										<td style="display: none">
											<input name="detailList.percent" class="tab_input" type="text" value="${item.percent }" readonly="readonly" />
										</td>
										<td style="display: none">
											<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="0"/>
										</td>
										<td>
											<input name="detailList.deliveryTime" class="bg_color tab_input" type="text" readonly="true" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" />
										</td>
										<td>
											<c:if test="${!empty item.sourceBillType}">
												<input class="tab_input" type="text" readonly="readonly" value="${!empty isCopy ? '' : item.sourceBillType.text }" />
											</c:if>
										</td>
										<td>
											<input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="readonly" value="${!empty isCopy ? '' : item.sourceBillNo}" />
										</td>
										<td style="display: none">
											<input name="detailList.sourceQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value='${item.sourceQty}' type='currency' pattern='0.####'/>" />
										</td>
										<td style="display: none">
											<input name="detailList.storageQty" class="tab_input" type="text" readonly="readonly" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxMoney" class="tab_input" type="text" readonly="readonly" />
										</td>
										<td style="display: none">
											<input name="detailList.noTaxPrice" class="tab_input" type="text" readonly="readonly" />
										</td>
										<td>
											<input name="detailList.productNames" class="tab_input" type="text" readonly="readonly" value="${item.productNames }" />
										</td>
										<td>
											<input name="detailList.memo" class="bg_color tab_input memo" type="text" value="${item.memo }" />
											<input type="hidden" name="detailList.noTaxValuationPrice" />
											<input type="hidden" name="detailList.sourceDetailId" value="${!empty isCopy ? '' : item.sourceDetailId}" />
											<input type="hidden" name="detailList.materialId" value="${item.materialId}" />
											<input type="hidden" name="detailList.materialClassId" value="${item.materialClassId}" />
											<input type="hidden" name="detailList.unitId" value="${item.unitId}" />
											<input type="hidden" name="detailList.valuationUnitId" value="${item.valuationUnitId}" />
											<c:if test="${!empty item.sourceBillType}">
												<input type="hidden" name="detailList.sourceBillType" value="${!empty isCopy ? '' : item.sourceBillType}" />
												<input type="hidden" name="detailList.sourceId" value="${!empty isCopy ? '' : item.sourceId}" />
											</c:if>
											<input name="valuationUnitAccuracy" type="hidden" value="${item.valuationUnitAccuracy}" />
											<input name="stockUnitAccuracy" type="hidden" value="${item.stockUnitAccuracy}" />
											<!-- 标记来自印刷家的订单，页面无逻辑依赖 -->
											<input name="detailList.extOrderId"  type="hidden" value="${item.extOrderId}"/>
											<input name="detailList.extOrderDetailId"  type="hidden" value="${item.extOrderDetailId}"/>
										</td>
										<c:set var="i" value="${status.index+1 }"></c:set>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<!-- 判断是否有查看金额的权限----begin  -->

						<dd class="row-dd">
							<label class="form-label label_ui label_3">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.totalTax }" />
							</span>
						</dd>

						<!-- 判断是否有查看金额的权限----end  -->
						<dd class="row-dd">
							<label class="form-label label_ui label_3">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" value="${order.currencyType }" />
								<input id="currencyTypeText" type="text" class="input-txt input-txt_10" value="${order.currencyTypeText }" readonly="true" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_3">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_10" readonly="readonly" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_deliveryTime_box" class="batch_deliveryTime_box">
					<input type="text" class="input-txt input-txt_8 batch_deliveryTime_input" name="" value="" readonly="true" onfocus="WdatePicker({onpicked:batchEditDeliveryTime}) " />
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="taxRateId" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>

