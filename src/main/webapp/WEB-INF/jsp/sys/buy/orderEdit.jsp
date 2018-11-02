<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>创建购买版本</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/buy/orderEdit.js?${v}"></script>
<style type="text/css">
.cl {
	height: 36px;
}
</style>
</head>
<body>
	<input type="hidden" value="${purchaseRecord.invoiceInfor}" id="purchaseRecordInvoiceInfor">
	<input type="hidden" value="${purchaseRecord.orderState}" id="purchaseRecordOrderState">
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${purchaseRecord.id }">
			<input type="hidden" id="companyId" name="companyId" value="${purchaseRecord.companyId}">
			<input type="hidden" id="productId" name="productId" value="${purchaseRecord.productId}">
			<input type="hidden" id="billNo" name="billNo" value="${purchaseRecord.billNo}">
			<input type="hidden" id="type" name="type" value="${purchaseRecord.type}">
			<input type="hidden" id="orderType" name="orderType" value="${purchaseRecord.orderType}">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							公司名称：
						</label>

						<div class="ui-combo-wrap form_text">
							<input id="companyName" name="companyName" htmlEscape="false" value="${purchaseRecord.companyName }" class="input-txt input-txt_7" readonly="readonly" />
							<div class="select-btn" id="selectCompany">...</div>
						</div>

					</div>

					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							产品名称：
						</label>
						<div class="ui-combo-wrap form_text">
							<input id="productName" name="productName" htmlEscape="false" value="${purchaseRecord.productName}" class="input-txt input-txt_7" readonly="readonly" />
							<div class="select-btn" id="selectProduct">...</div>
						</div>
					</div>

					<!-- 					<div class="row-div"> -->
					<!-- 						<label class="form-label label_ui label_4mar"><span -->
					<!-- 							class="c-red">*</span>服务名称：</label> -->
					<!-- 						<div class="ui-combo-wrap form_text div_select_wrap"> -->
					<%-- 							<phtml:list items="${buy}" selected="${purchaseRecord.productId }" --%>
					<%-- 								valueProperty="id" cssClass="hy_select2" name="productName" --%>
					<%-- 								textProperty="name" defaultValue="-1" defaultOption="--请选择--"></phtml:list> --%>
					<!-- 						</div> -->
					<!-- 					</div> -->

					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							购买日期：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'payTime\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate" id="payTime" name="payTime" value="<fmt:formatDate value="${purchaseRecord.payTime}" type="date" pattern="yyyy-MM-dd" />">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_7">用户名：</label>
						<div class="ui-combo-wrap form_text">
							<input id="userName" name="userName" htmlEscape="false" value="${purchaseRecord.userName }" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">金额：</label>
						<div class="ui-combo-wrap form_text">
							<input id="price" name="price" htmlEscape="false" value="${purchaseRecord.price }" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							开票信息：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<select class="hy_select2" id="invoiceInfor" name="invoiceInfor">
								<option value="2">增值税发票17%</option>
								<option value="1">增值税发票6%</option>
								<option value="0">不要发票</option>
							</select>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_7">
							</span>
							联系人：
						</label>
						<div class="ui-combo-wrap form_text">
							<input id="linkMan" name="linkMan" htmlEscape="false" value="${purchaseRecord.linkMan }" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">税额：</label>
						<div class="ui-combo-wrap form_text">
							<input id="tax" name="tax" htmlEscape="false" value="${purchaseRecord.tax }" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">邀请人：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input id="inviter" name="inviter" htmlEscape="false" value="${purchaseRecord.inviter }" class="input-txt input-txt_7" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_7">联系电话：</label>
						<div class="ui-combo-wrap form_text">
							<input id="telephone" name="telephone" htmlEscape="false" value="${purchaseRecord.telephone }" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">奖金金额：</label>
						<div class="ui-combo-wrap form_text">
							<input id="bonus" name="bonus" htmlEscape="false" value="${purchaseRecord.bonus}" class="input-txt input-txt_7" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">邀请人电话：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input id="inviterPhone" name="inviterPhone" htmlEscape="false" value="${purchaseRecord.inviterPhone }" class="input-txt input-txt_7" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							是否已支付：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isPay" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue" selected="${purchaseRecord.isPay }"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							支付金额：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input id="payPrice" name="payPrice" htmlEscape="false" class="input-txt input-txt_7" value="${purchaseRecord.payPrice }" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_7">
							<span class="c-red">*</span>
							订单状态：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<select class="hy_select2" id="orderState" name="orderState">
								<option value="2">已完成</option>
								<option value="1">待支付</option>
								<option value="0">已取消</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" value="1" name="orderFrom">
			<div style="margin-left: 138px; margin-top: 10px">
				<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>