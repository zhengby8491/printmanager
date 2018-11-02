<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看对账单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/reconcil/view.js?v=${v}"></script>
</head>
<body>
	<input type="hidden" value="${id}" id="reconcilId">
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
					<sys:nav struct="销售管理-销售对账-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<span id="forceCompleteNO" style="display: none;">
						<span id="isCheckNO" style="display: none;">
							<shiro:hasPermission name="sale:reconcil:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="sale:reconcil:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="sale:reconcil:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</span>
						<span id="isCheckYES" style="display: none;">
							<shiro:hasPermission name="sale:reconcil:audit_cancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
						</span>
						<shiro:hasPermission name="sale:reconcil:complete">
							<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
						</shiro:hasPermission>
					</span>
					<span id="forceCompleteYES" style="display: none;">
						<shiro:hasPermission name="sale:reconcil:complete_cancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</span>
					<shiro:hasPermission name="sale:reconcil:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
					<span>
						<span class="isFlowCheckYES" style="display: none;">
							<div class="btn-group" id="btn_transmit">
								<button class="nav_btn table_nav_btn " type="button">
									生成
									<span class="caret"></span>
								</button>
								<div class="template_div">
									<ul class='dropdown-menu' role='menu'>
										<li>
											<a title='生成收款单' id="transmitToReceive">生成收款单</a>
										</li>
									</ul>
								</div>
							</div>
						</span>
				</div>
			</div>

			<input type="hidden" name="id" id="id" value="${params.id }" />
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
								<label class="form-label label_ui label_1">对账日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="reconcilTime" pattern="yyyy-MM-dd" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">销 售 员：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="employeeName">
								</span>
							</dd>

						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">送货地址：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt" readonly="readonly" style="width: 367px" id="deliveryAddress" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">结算方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="settlementClassName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">付款方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="paymentClassName" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">对账单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="billNo" />
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
									<textarea class="noborder" readonly="readonly" style="width: 928px" id="memo"></textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->
			<!--表格Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格End-->
			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<shiro:hasPermission name="sale:reconcil:money">
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
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createName">
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
							'
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