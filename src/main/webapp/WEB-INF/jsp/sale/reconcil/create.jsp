<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/common/common.js?v=" +${v}></script>
<title>创建对账单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/reconcil/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售对账-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="sale:reconcil:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="sale:reconcil:create,sale:reconcil:audit">
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
								<input type="hidden" name="customerId" id="customerId" value="${customer.id }">
								<input type="hidden" id="index" />
								<input type="hidden" name="isCheck" id="isCheck" />
								<%-- <dd class="row-dd">
								<label class="form-label label_ui label_1">客户编号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" name="customerCode" id="customerCode" value="${customer.code }"/>
									<div class="select-btn" id="selectCustomer">...</div>
								</span>
							</dd> --%>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="true" name="customerName" id="customerName" value="${customer.name }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${customer.defaultAddress.userName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" value="${customer.defaultAddress.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="reconcilTime" id="reconcilTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="hidden" id="employeeId" name="employeeId" value="${customer.employeeId}" class="input-txt input-txt_1" />
										<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" value="${customer.employeeName}" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" value="${customer.defaultAddress.address }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" name="settlementClassId" textProperty="name" cssClass="nput-txt input-txt_1 hy_select2" selected="${customer.settlementClassId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${customer.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>

								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui">税 收：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" name="rateId" textProperty="name" cssClass="input-txt input-txt_1 hy_select2" onchange="taxSelect();" selected="${customer.taxRateId }"></phtml:list>
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" style="width: 937px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<div id="toolbar"></div>
						<table class="border-table" style="width: 1400px" id="detailList">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="80">源单类型</th>
									<th width="120" style="display: none">产品编号</th>
									<th width="140">成品名称</th>
									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="120">客户料号</th>
									<th width="120">客户单号</th>
									<th width="120">销售单号</th>
									<th width="80">数量</th>
									<th width="80">源单数量</th>
									<th width="80">源单单号</th>
									<th width="80">备品数量</th>
									<shiro:hasPermission name="sale:reconcil:money">
										<th width="60">单价</th>
										<th width="100">金额</th>
										<th width="100">税额</th>
									</shiro:hasPermission>

									<th width="100">税收</th>
									<th width="80" style="display: none">税率值</th>
									<th width="100" style="display: none">不含税单价</th>
									<th width="100" style="display: none">不含税金额</th>
									<th width="100">送/退货日期</th>
									<th width="100">备注</th>
									<th width="60">产品图片</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input name="detailList.productId" type="hidden" readonly="readonly" value="${detail.productId }" />
										<td>${status.count}</td>
										<td class="td-manage">
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
										</td>
										<td class="td-manage">
											<c:if test="${detail.sourceBillType=='SALE_IV' }">送货单</c:if>
											<c:if test="${detail.sourceBillType=='SALE_IR' }">退货单</c:if>
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
											<input type="hidden" name="detailList.unitId" value="${detail.unitId }" />
											<input class="tab_input" type="text" name="detailList.unitName" readonly="readonly" value="${fns:basicInfoFiledValue('UNIT',detail.unitId,'name')}" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.customerMaterialCode" readonly="readonly" value="${detail.customerMaterialCode }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.customerBillNo" readonly="readonly" value="${detail.customerBillNo }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.saleOrderBillNo" readonly="readonly" value="${detail.saleOrderBillNo }" />
										</td>
										<td>
											<input class="tab_input bg_color <c:if test="${detail.sourceBillType=='SALE_IR' }">constraint_positive</c:if> <c:if test="${detail.sourceBillType=='SALE_IV' }">constraint_negative</c:if>" type="text" name="detailList.qty" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.qty }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="${detail.sourceQty }" />
										</td>
										<td>
											<input class="tab_input" type="hidden" name="detailList.sourceDetailId" value="${detail.sourceDetailId }" />
											<input class="tab_input" type="hidden" name="detailList.sourceId" value="${detail.sourceId }" />
											<input class="tab_input" type="hidden" name="detailList.sourceBillType" value="${detail.sourceBillType }" />
											<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.spareQty" readonly="readonly" value="${detail.spareQty }" />
										</td>

										<%-- <shiro:hasPermission name="sale:reconcil:money"> --%>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.money" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.money }" />
											<input type="hidden" name="detailList.saveMoney" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.tax }" />
										</td>
										<%-- </shiro:hasPermission> --%>

										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="<c:if test="${detail.sourceBillType=='SALE_IR' }">-</c:if>${detail.noTaxMoney }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.deliveryTime" readonly="readonly" value="<fmt:formatDate value="${detail.deliveryTime}" type="date" pattern="yyyy-MM-dd" />">
										</td>
										<td>
											<input class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" name="detailList.memo" value="${detail.memo }" />
											<input type="hidden" name="detailList.receiveMoney" value="${detail.receiveMoney }" />
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
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
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
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" value="${customer.currencyType}" />
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${customer.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
