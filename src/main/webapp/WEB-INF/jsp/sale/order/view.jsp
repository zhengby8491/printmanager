<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看销售单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/view.js?v=${v}"></script>

</head>
<body>
	<input type="hidden" value="${id}" id="orderId">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售订单-查看"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<shiro:hasPermission name="sale:order:historyPrice">
						<button class="nav_btn table_nav_btn" id="historyPrice">历史单价</button>
					</shiro:hasPermission>
					<span id="forceCompleteNO" style="display: none;">
						<span id="isCheckNO" style="display: none;">
							<shiro:hasPermission name="sale:order:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="sale:order:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="sale:order:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</span>
						<span id="isCheckYES" style="display: none;">
							<shiro:hasPermission name="sale:order:changePrice">
								<button class="nav_btn table_nav_btn" id="priceChange">单价变更</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="sale:order:audit_cancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
						</span>
						<shiro:hasPermission name="sale:order:complete">
							<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
						</shiro:hasPermission>
					</span>
					<span id="forceCompleteYES" style="display: none;">
						<shiro:hasPermission name="sale:order:complete_cancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</span>
					<shiro:hasPermission name="sale:order:create">
						<button class="nav_btn table_nav_btn" id="btn_copy">复制</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sale:order:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
					<span class="isFlowCheckYES" style="display: none;" id="generateTransmit">
						<div class="btn-group" id="btn_transmit">
							<button class="nav_btn table_nav_btn " type="button">
								生成
								<span class="caret"></span>
							</button>
							<div class="template_div">
								<ul class='dropdown-menu' role='menu'>
									<li>
										<a title='生成生产工单' id="transmitToProduce">生成生产工单</a>
									</li>
									<li>
										<a title='生成销售送货单' id="transmitToDeliver">生成销售送货</a>
									</li>
								</ul>
							</div>
						</div>
					</span>
				</div>
			</div>

			<input type="hidden" name="id" id="id" />
			<!--主表-订单表单-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">客户名称：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" id="customerName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">联 系 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="linkName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">联系电话：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="mobile" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">订单类型：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="orderTypeText" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">销售员：</label>
								<span class="ui-combo-wrap">
									<input type="hidden" id="employeeId" name="employeeId" class="input-txt input-txt_1" />
									<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" value="${order.employeeId}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">送货地址：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_11" readonly="readonly" id="deliveryAddress" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">送货方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="deliveryClassName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">付款方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="paymentClassName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">客户单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="customerBillNo" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">销售单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" id="billNo" />
								</span>
							</dd>
							<dd class="row-dd" style="display: none">
								<label class="form-label label_ui">税 收：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" id="rateName" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" readonly="readonly" style="width: 1100px" id="memo"></textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->
			<!--表格Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList"></table>
			</div>
			<!--表格End-->
			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<shiro:hasPermission name="sale:order:money">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="totalMoney" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="noTaxTotalMoney" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="totalTax" />
							</span>
						</dd>
					</shiro:hasPermission>

					<dd class="row-dd">
						<label class="form-label label_ui label_7">币 种：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="currencyTypeText" />
						</span>
					</dd>
				</dl>
				<dl class="cl row-dl-foot">
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制 单 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createName" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制单日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createTime" pattern="yyyy-MM-dd" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审 核 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="checkUserName" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审核日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="checkTime" pattern="yyyy-MM-dd" />
						</span>
					</dd>

				</dl>
			</div>
			<div class="review" style="display: none;">
				<span class="review_font">已审核</span>
			</div>

		</div>
	</div>
</body>
</html>