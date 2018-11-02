<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑销售单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/edit.js?v=${v}"></script>
</head>
<body>
	<div id="phtml" style="display:none;">
		<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" />
	</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售订单-编辑"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="sale:order:historyPrice">
						<button class="nav_btn table_nav_btn" id="historyPrice">历史单价</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sale:order:edit">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="sale:order:edit,sale:order:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_order">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_7" name="customerName" id="customerName" value="${order.customerName }" readonly="true" />
										<input type="hidden" name="customerCode" value="${order.customerCode }">
										<input type="hidden" name="customerId" id="customerId" value="${order.customerId }">
										<input type="hidden" name="employeeId" id="employeeId" value="${order.employeeId }">
										<input type="hidden" name="id" id="order_id" value="${order.id }">
										<%--<input type="hidden" name="billNo" id="billNo" value="${order.billNo }" /> --%>
										<input type="hidden" name="billType" value="${order.billType }">
										<input type="hidden" id="index" />
										<input type="hidden" name="isCheck" id="isCheck" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">订单类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="orderType" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" selected="${order.orderType }" type="com.huayin.printmanager.persist.enumerate.OrderType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" readonly="true" value="${order.employeeName}" />
									</span>
								</dd>
								<dd class="row-dd" style="display: none;">
									<label class="form-label label_ui label_1">交货日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="deliveryTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" onchange="selectTime();" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap wrap-width_slt">
										<input type="text" class="input-txt input-txt_11" name="deliveryAddress" id="deliveryAddress" value="${order.deliveryAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户单号：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="text" name="customerBillNo" id="customerBillNo" class="input-txt input-txt_3" value="${order.customerBillNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" readonly="readonly" class="input-txt input-txt_1" value="${order.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" class="noborder" style="width: 1100px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
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
					<a id="select_product" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择产品
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1400px" id="detailList">
							<thead>
								<tr>
									<th width="20" name="radio"></th>
									<th width="20" name="seq">序号</th>
									<th width="20" name="operator">操作</th>
									<!-- <th width="140" name="code">产品编号</th> -->
									<th width="60" name="name">成品名称</th>
									<th width="60" name="specifications">产品规格</th>
									<th width="60" name="customerMaterialCode">客户料号</th>
									<th width="20" name="unitId">单位</th>
									<th width="30" name="qty">数量</th>
									<th width="30" name="spareQty">备品数量</th>
									<th width="100" name="deliverSpareedQty" style="display: none;">已送备品数量</th>
									<th width="30" name="salePrice">单价</th>
									<th width="30" name="money">金额</th>
									<th width="30" name="tax">税额</th>
									<th width="60" name="taxRateId">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="70" name="percent" style="display: none">税率值</th>
									<th width="40" name="deliveryTime">
										交货日期
										<i id="batch_edit_deliveryTime" class="fa fa-edit" src="batch_deliveryTime_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="30" name="procedureMaterial">工序材料</th>
									<th width="100" name="deliverQty" style="display: none;">已送货数量</th>
									<th width="100" name="noTaxPrice" style="display: none;">不含税单价</th>
									<th width="100" name="noTaxMoney" style="display: none;">不含税金额</th>
									<th width="60 " name="offerId" style="display: none">报价单id</th>
									<th width="60 " name="offerNo">报价单号</th>
									<th width="60 " name="memo">备注</th>
									<th width="60" name="imgUrl">产品图片</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input type="hidden" name="detailList.id" value="${detail.id }">
										<input type="hidden" name="detailList.saleOrderBillNo" value="${detail.saleOrderBillNo }" />
										<td>
											<input type="radio" name="radio">
										</td>
										<td>${status.count}</td>
										<td class="td-manage">

											<input name="detailList.productId" type="hidden" readonly="readonly" value="${detail.productId }" />
											<input name="detailList.productType" type="hidden" readonly="readonly" value="${detail.productType }" />
											<a title="删除行" href="javascript:void(0)" name="btn_del">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td class="pru_number" style="display: none">
											<input class="tab_input" type="text" name="detailList.productCode" readonly="readonly" value="${detail.productCode }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="${detail.style }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.customerMaterialCode" readonly="readonly" value="${detail.customerMaterialCode }" />
										</td>
										<td>
											<input class="tab_input" type="hidden" name="detailList.unitId" readonly="readonly" value="${detail.unitId }" />
											<input class="tab_input" type="text" name="detailList.unitName" readonly="readonly" value="${fns:basicInfoFiledValue('UNIT',detail.unitId,'name')}" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_negative" type="text" name="detailList.qty" value="${detail.qty }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_negative" type="text" name="detailList.spareQty" value="${detail.spareQty }" />
										</td>
										<td style="display: none">
											<input class="tab_input constraint_negative" readonly="readonly" type="text" name="detailList.deliverSpareedQty" value="${detail.deliverSpareedQty }" />
										</td>

										<td>
											<input class="tab_input bg_color constraint_decimal" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal" type="text" name="detailList.money" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>

										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>
										<td>
											<input class="tab_input bg_color" type="text" readonly="readonly" name="detailList.deliveryTime" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" value="<fmt:formatDate value="${detail.deliveryTime}" type="date" pattern="yyyy-MM-dd" />" />
										</td>
										<td class="td-manage" style="background-color: #fff;">
											<%-- 											<input name="detailList.partList" type="hidden" readonly="readonly" value="${detail.partList }" /> --%>
											<a title="工序材料详情" href="javascript:void(0)" name="btn_procedure">
												<i class="delete fa fa-info-circle"></i>
											</a>
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.deliverQty" id="deliverQty" readonly="readonly" value="${detail.deliverQty }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.offerId" readonly="readonly" value="${detail.offerId }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.offerNo" readonly="readonly" value="${detail.offerNo }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
										</td>
										<td>
											<c:if test="${detail.imgUrl !='' }">
												<img class="pimg" src="${detail.imgUrl }" />
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot style="display: none">
								<tr>
									<td>合 计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
									<td>
										<input class="tab_input" id="spareQty" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="money" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" type="text" id="tax" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" type="text" id="noTaxMoney" readonly="readonly" />
									</td>
									<td></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<shiro:hasPermission name="sale:order:money">
							<dd class="row-dd">
								<label class="form-label label_ui label_7">金 额：</label>
								<span class="ui-combo-wrap" class="form_text">
									<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly value="${order.totalMoney }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_7">金额(不含税)：</label>
								<span class="ui-combo-wrap" class="form_text">
									<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly value="${order.noTaxTotalMoney }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_7">税 额：</label>
								<span class="ui-combo-wrap" class="form_text">
									<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly="true" value="${order.totalTax }" />
								</span>
							</dd>
						</shiro:hasPermission>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="true" value="${customer.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly value="${order.createName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly value="${order.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>

					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_deliveryTime_box" class="batch_deliveryTime_box">
					<input type="text" class="input-txt input-txt_8 batch_deliveryTime_input" name="" value="" readonly="true" onFocus="WdatePicker({ minDate:'%y-%M-%d',onpicked:batchEditDeliveryTime}) " />
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 batch_taxRate_select" valueProperty="id" textProperty="name" name="rateId" selected="${order.rateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>