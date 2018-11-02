<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建退货单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/return/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<input id="customerJSON" type="hidden" value='${customerJSON}'/>
			<input id="deliverDetailJSON" type="hidden" value='${deliverDetailJSON}'/>
			<input id="basicListParam2" type="hidden" value='${fns:basicListParam2('WAREHOUSE', 'warehouseType','PRODUCT')}'/>
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售退货-创建"></sys:nav>
					<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
						<div id="innerdiv" style="position: absolute;">
							<img id="bigimg" style="border: 5px solid #fff;" src="" />
						</div>
					</div>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="sale:return:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="sale:return:create,sale:return:audit">
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
								<input type="hidden" name="customerId" id="customerId">
								<input type="hidden" id="index" />
								<input type="hidden" name="isCheck" id="isCheck" />
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="true" name="customerName" id="customerName" />
										<div class="select-btn" id="selectCustomer">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="returnType" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.ReturnGoodsType"></phtml:list>
									</span>
								</dd>

							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${customer.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="nput-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${customer.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="hidden" id="employeeId" name="employeeId" class="input-txt input-txt_1" />
										<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备 注：</label>
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
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="select_deliver" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择送货单
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1600px" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
									<th width="60" name="operator">操作</th>
									<th width="120" name="productCode" style="display: none">产品编号</th>
									<th width="160" name="productName">成品名称</th>
									<th width="100" name="style">产品规格</th>
									<th width="60" name="unitId">单位</th>
									<th width="80" name="sourceQty">源单数量</th>
									<th width="120" name="billNo">源单单号</th>
									<th width="120" name="saleOrderBillNo">销售单号</th>
									<th width="120" name="customerMaterialCode">客户料号</th>
									<th width="120" name="customerBillNo">客户单号</th>
									<th width="100" name="qty">换/退货数量</th>
									<th width="80" name="reconcilQty" style="display: none">已对账数量</th>
									<th width="80" name="warehouseId">
										仓库
										<i id="batch_edit_wareHouse" class="fa fa-edit" src="batch_wareHouse_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<!-- 判断是否有权限查看金额---begin  -->

									<th width="60" name="price">单价</th>
									<th width="100" name="money">金额</th>
									<th width="80" name="tax">税额</th>

									<!-- 判断是否有权限查看金额---end  -->
									<th width="100" name="taxRateId">
										税收
										<!-- <i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i> -->
										<!-- <div class="batch_box_container"></div>  -->
									</th>
									<th width="80" name="percent" style="display: none">税率值</th>
									<th width="80" name="noTaxPrice" style="display: none">不含税单价</th>
									<th width="100" name="noTaxMoney" style="display: none">不含税金额</th>

									<th width="100" name="memo">备注</th>
									<th width="60" name="imgUrl">产品图片</th>
								</tr>
							</thead>
							<tbody>
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
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
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
						<!-- 判断是否有权限查看金额---begin  -->

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

						<!-- 判断是否有权限查看金额---end  -->
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" />
								<input id="currencyTypeText" type="text" class="input-txt input-txt_3" readonly="readonly" />
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
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_wareHouse_box" class="batch_wareHouse_box">
					<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','PRODUCT')}" cssClass="tab_input bg_color input-txt_3 hy_select2 batch_wareHouse_select" valueProperty="id" name="" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="rateId" selected="${customer.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>