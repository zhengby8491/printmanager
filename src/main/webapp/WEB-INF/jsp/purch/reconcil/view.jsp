<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购对账</title>
<script type="text/javascript" src="${ctxStatic}/site/purch/purch.js "></script>
<script type="text/javascript" src="${ctxStatic}/site/purch/reconcil/view.js?v=${v}"></script>
</head>
<body>
<input type="hidden" value="${id}" id="reconcilId">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购对账-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<span id="forceCompleteNO" style="display: none;">
						<span id="isCheckNO" style="display: none;">
							<shiro:hasPermission name="purch:reconcil:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purch:reconcil:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purch:reconcil:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</span>
						<span id="isCheckYES" style="display: none;">
							<shiro:hasPermission name="purch:reconcil:audit_cancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purch:reconcil:complete">
								<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
							</shiro:hasPermission>
						</span>
					</span>
					<span id="forceCompleteYES" style="display: none;">
						<shiro:hasPermission name="purch:reconcil:complete_cancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</span>
					<shiro:hasPermission name="purch:reconcil:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
					<span class="isFlowCheckYES" id="generateTransmit" style="display: none;">
						<div class="btn-group" id="btn_transmit">
							<button class="nav_btn table_nav_btn " type="button">
								生成
								<span class="caret"></span>
							</button>
							<div class="template_div">
								<ul class="dropdown-menu" role="menu">
									<li id="btn_transmit_payment">
										<a title="生成付款单" href="javascript:;">生成付款单</a>
									</li>
								</ul>
							</div>
						</div>
					</span>
				</div>
			</div>
			<!--主表-订单表单-->
			<form id="purchStockAction" method="post">
				<input type="hidden" id="id" />
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" id="supplierName" readonly="readonly" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" id="linkName" readonly="readonly" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" id="mobile" readonly="readonly" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" id="reconcilTime" pattern="yyyy-MM-dd" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="employeeName" />

									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" readonly="readonly" id="supplierAddress" />

									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="settlementClassName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="paymentClassName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="billNo" />

									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui">税 收：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="taxRateName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" id="memo" readonly="readonly" style="width: 907px"></textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--表格部分Start-->
				<div class="table-view">
					<table class="border-table resizable" id="detailList">
					</table>
				</div>
				<!--表格部分End-->
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<shiro:hasPermission name="purch:reconcil:money">
							<dd class="row-dd">
								<label class="form-label label_ui label_7">金 额：</label>
								<span class="ui-combo-wrap">
									<input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_6" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_7">金额(不含税)：</label>
								<span class="ui-combo-wrap">
									<input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_6" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_7">税 额：</label>
								<span class="ui-combo-wrap">
									<input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_6" readonly="readonly" />
								</span>
							</dd>
						</shiro:hasPermission>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_6" readonly="readonly" id="currencyTypeText" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap">
								<input id="createName" type="text" class="input-txt input-txt_6" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap">
								<input id="createTime" type="text" class="input-txt input-txt_6" readonly="readonly" pattern="yyyy-MM-dd" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap">
								<input id="checkUserName" type="text" class="input-txt input-txt_6" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap">
								<input id="checkTime" type="text" class="input-txt input-txt_6" readonly="readonly" pattern="yyyy-MM-dd" />
							</span>
						</dd>
					</dl>
				</div>
			</form>



			<div class="review" style="display: none;">
				<span class="review_font">已审核</span>
			</div>
		</div>
	</div>
</body>
</html>

