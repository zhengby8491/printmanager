<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看发外对账单</title>
<script type="text/javascript" src="${ctxStatic}/site/outsource/reconcil/view.js?v=${v}"></script>
</head>
<body>
<input type="hidden" value='${fns:toJson(order.detailList)}' id="orderDetailList">
<input type="hidden" value='${id}' id="reconcilId">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="发外管理-发外对账-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isForceComplete eq 'NO'}">

						<c:if test="${order.isCheck eq 'NO'}">
							<shiro:hasPermission name="outsource:reconcil:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="outsource:reconcil:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="outsource:reconcil:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${order.isCheck eq 'YES'}">
							<shiro:hasPermission name="outsource:reconcil:audit_cancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
						</c:if>
						<shiro:hasPermission name="outsource:reconcil:complete">
							<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${order.isForceComplete eq 'YES'}">
						<shiro:hasPermission name="outsource:reconcil:complete_cancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="outsource:reconcil:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
					<c:if test="${order.isForceComplete eq 'NO'}">
						<c:if test="${order.isCheck eq 'YES'}">
							<span class="isFlowCheckYES" id="generateTransmit">
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
						</c:if>
					</c:if>
				</div>
			</div>

			<input type="hidden" name="id" id="order_id" value="${order.id }" />
			<!--主表-订单表单-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">供应商名称：</label>
								<span class="ui-combo-wrap wrap-width">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" id="supplierName" name="supplierName" value="${order.supplierName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">联 系 人：</label>
								<input type="text" readonly="true" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName }" />
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">联系电话：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.mobile}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">对账日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.reconcilTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">发外员：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" />
								</span>
							</dd>

						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">供应商地址：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="true" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" value="${order.supplierAddress }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">结算方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfoFiledValue('SETTLEMENTCLASS',order.settlementClassId,'name')}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">付款方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfoFiledValue('PAYMENTCLASS',order.paymentClassId,'name')}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">对账单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.billNo}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" readonly="readonly" style="width: 907px">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->
			<!--表格Start-->
			<div class="view-boot-mar table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格End-->
			<!--表单-下Start-->
			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<shiro:hasPermission name="outsource:reconcil:money">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.totalMoney}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.noTaxTotalMoney  }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.totalTax  }" />
							</span>
						</dd>
					</shiro:hasPermission>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">币 种：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.currencyType.text  }" />
						</span>
					</dd>
				</dl>
				<dl class="cl row-dl-foot">
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制 单 人：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.createName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制单日期：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>

					<dd class="row-dd">
						<label class="form-label label_ui label_7">审 核 人：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.checkUserName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审核日期：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
				</dl>
			</div>
			<!--表单-下End-->
			<c:if test="${order.isCheck eq 'YES'}">
				<div class="review">
					<span class="review_font">已审核</span>
				</div>
			</c:if>

		</div>
	</div>
</body>
</html>