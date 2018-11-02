<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建送货单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/deliver/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售送货-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="sale:deliver:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="sale:deliver:create,sale:deliver:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
			</div>
			<form id="form_payment">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="true" name="customerName" id="customerName" value="${customer.name }" />
										<input type="hidden" name="customerId" id="customerId" value="${customer.id }">
										<input type="hidden" id="index" />
										<input type="hidden" name="isCheck" id="isCheck" />
										<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
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
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">交货日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="deliveryTime" id="deliveryTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn' })" onchange="selectTime();" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="hidden" id="employeeId" name="employeeId" value="${order.employeeId}" class="input-txt input-txt_1" />
										<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" value="${order.employeeName}" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" value="${order.deliveryAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" 
											selected="${customer.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" 
											selected="${customer.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" class="noborder" style="width: 952px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
									<th width="100" style="display: none">产品编号</th>
									<th width="140">成品名称</th>
									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="80">数量</th>
									<th width="80" style="display: none">已退货数量</th>
									<th width="80" style="display: none">已对账数量</th>
									<th width="80">备品数量</th>
									<th width="80">源单数量</th>
									<th width="120">源单单号</th>
									<th width="120">客户料号</th>
									<th width="120">客户单号</th>
									<%-- <shiro:hasPermission name="sale:deliver:money"> --%>
									<th width="60" name="salePrice">单价</th>
									<th width="100" name="money">金额</th>
									<th width="80" name="tax">税额</th>
									<%-- </shiro:hasPermission> --%>
									<th width="113">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" style="display: none">税率值</th>
									<th width="100">
										仓库
										<i id="batch_edit_wareHouse" class="fa fa-edit" src="batch_wareHouse_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" style="display: none">不含税单价</th>
									<th width="80" style="display: none">不含税金额</th>
									<th width="100">备注</th>
									<th width="60">产品图片</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input type="hidden" name="detailList.saleOrderBillNo" value="${detail.saleOrderBillNo }" />
										<input name="detailList.productId" type="hidden" readonly="readonly" value="${detail.productId }" />
										<td>${status.count}</td>
										<td class="td-manage">
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
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
											<input class="tab_input bg_color constraint_negative" type="text" name="detailList.qty" value="${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="${detail.qty }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.returnedQty" readonly="readonly" value="0" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.reconcilQty" readonly="readonly" value="0" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_negative" type="text" name="detailList.spareQty" value="${detail.spareQty }" />
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
											<input class="tab_input" type="text" name="detailList.customerMaterialCode" readonly="readonly" value="${detail.customerMaterialCode }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.customerBillNo" readonly="readonly" value="${detail.customerBillNo }" />
										</td>
										<!-- 判断是否有权限看到金额---begin↓  -->
										<%-- <shiro:hasPermission name="sale:deliver:money"> --%>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.money" value="${detail.money }" />
											<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>
										<%-- </shiro:hasPermission> --%>
										<!-- 判断是否有权限看到金额---end↑ -->
										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>

										<td>
											<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" onchange="shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" name="detailList.warehouseId" cssClass="tab_input bg_color"></phtml:list>
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
										</td>
										<td>
											<c:if test="${detail.imgUrl !=''}">
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
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
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
									<td></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<!-- 判断是否有查看金额权限---begin -->
						<%--   <shiro:hasPermission name="sale:deliver:money"> --%>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<%-- </shiro:hasPermission> --%>
						<!-- 判断是否有查看金额权限---end -->
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
								<input name="createName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${loginUser.realName}" />
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
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_wareHouse_box" class="batch_wareHouse_box">
					<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','PRODUCT')}" cssClass="tab_input bg_color input-txt_3 batch_wareHouse_select hy_select2" valueProperty="id" name="" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 batch_taxRate_select hy_select2" valueProperty="id" textProperty="name" name="rateId" selected="${customer.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>