<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/customer/create.js?v=${v}"></script>
<title>添加客户</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							客户分类：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('CUSTOMERCLASS')}" cssClass="hy_select2" valueProperty="id" name="customerClassId" textProperty="name" selected="${customer.customerClassId}"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							客户编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" readonly="true" value="${code }" placeholder="" id="code" name="code">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							客户名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							销售员：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('EMPLOYEE')}" cssClass="hy_select2" valueProperty="id" name="employeeId" textProperty="name" selected="${customer.employeeId}"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							币种：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="currencyType" textProperty="text" cssClass="hy_select2" selected="${customer.currencyType}" type="com.huayin.printmanager.persist.enumerate.CurrencyType"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							税收：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="hy_select2" selected="${customer.taxRateId}" valueProperty="id" name="taxRateId" textProperty="name"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							送货方式：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" cssClass="hy_select2" selected="${customer.deliveryClassId }" valueProperty="id" name="deliveryClassId" textProperty="name"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							结算方式：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap error_tip">
							<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" cssClass="hy_select2" selected="${customer.settlementClassId}" valueProperty="id" name="settlementClassId" textProperty="name"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							付款方式：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" cssClass="hy_select2" selected="${customer.paymentClassId}" valueProperty="id" name="paymentClassId" textProperty="name"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否有效：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isValid" textProperty="text" cssClass="hy_select2" selected="${customer.isValid }" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 824px; resize: none">${customer.memo}</textarea>
							<p class="textarea-numberbar">
								<em>0</em>
								/100
							</p>
						</div>
					</div>
				</div>
			</div>
			<!--表单部分END-->
			<!--表格部分START-->
			<div class="info_table">
				<div class="tab_page">
					<ul class="tab_page_list">
						<li class="active">联系人信息</li>
						<li>客户收款单位</li>
					</ul>
				</div>
				<div class="info_table_container">
					<div class="info_item info_item_1">
						<div class="info_item_btn">
							<button type="button" class="table_nav_btn info_btn" id="addtr_address">
								<i class="fa fa-plus"></i>
								添加行
							</button>
						</div>
						<div class="info_item_table">
							<table class="layer_table layer_info_table" rules="all">
								<thead>
									<tr>
										<th width="60">序号</th>
										<th width="60">默认</th>
										<th width="80">联系人</th>
										<th width="300">详细地址</th>
										<th width="100">手机</th>
										<th width="160">邮箱</th>
										<th width="80">QQ</th>
										<th width="60">操作</th>
									</tr>
								</thead>
								<tbody id="customer_address">
									<c:forEach var="address" items="${customer.addressList}" varStatus="sta">
										<tr>
											<td>${sta.count}</td>
											<td>
												<input type="hidden" name="addressList.isDefault" value="${address.isDefault }" />
												<input type="radio" name="address_isDefault" <c:if test="${address.isDefault=='YES' }">checked='checked'</c:if> />
											</td>
											<td>
												<input class="tab_input" name="addressList.userName" id="userName" type="text" value="${address.userName }" />
											</td>
											<td align="left">
												<input class="tab_input" name="addressList.address" id='address' type="text" value="${address.address }" />
											</td>
											<td>
												<input class="tab_input" name="addressList.mobile" id="mobile" type="text" value="${address.mobile }" />
											</td>
											<td>
												<input class="constraint_email tab_input" name="addressList.email" id="email" type="text" value="${address.email }" />
											</td>
											<td>
												<input class="tab_input" name="addressList.qq" id="qq" type="text" value="${address.qq }" />
											</td>
											<td>
												<a title="删除" href="javascript:;" name="btn_del">
													<i class="fa fa-trash-o"></i>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<div class="info_item info_item_2">
						<div class="info_item_btn">
							<button type="button" class="table_nav_btn info_btn" id="addtr_payer">
								<i class="fa fa-plus"></i>
								添加行
							</button>
						</div>
						<div class="info_item_table">
							<table class="layer_table layer_info_table" rules="all">
								<thead>
									<tr>
										<th width="60">序号</th>
										<th width="60">默认</th>
										<th>收款单位</th>
										<th width="60">操作</th>
									</tr>
								</thead>
								<tbody id="customer_payer">
									<c:forEach var="payer" items="${customer.payerList}" varStatus="sta">
										<tr>
											<td>${sta.count }</td>
											<td>
												<input type="hidden" name="payerList.isDefault" value="${payer.isDefault }" />
												<input type="radio" name="payer_isDefault" <c:if test="${payer.isDefault=='YES' }">checked='checked'</c:if> />
											</td>
											<td>
												<input type="text" name="payerList.name" class="tab_input" value="${payer.name }" />
											</td>
											<td>
												<a title="删除" href="javascript:;" name="btn_del">
													<i class="fa fa-trash-o"></i>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!--表格部分END-->
			<div style="margin-left: 850px; margin-top: 10px">
				<input class="nav_btn table_nav_btn" type="button" id="btn_save" onclick="checkData();" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>
