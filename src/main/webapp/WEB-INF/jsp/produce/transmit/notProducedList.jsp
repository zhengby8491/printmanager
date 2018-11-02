<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/produce/transmit/notProducedList.js?v=${v }"></script>
<title>未开生产工单明细</title>
</head>
<body>
	<form id="transmitForm"></form>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-转单功能-未开生产工单明细"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="produce:transmit:notWork_create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成生产工单</button>
						<button class="nav_btn table_nav_btn" id="btn_transmit_rotary">生成轮转工单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:transmit:notWork_complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:transmit:notWork_cancel_complete">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<label class="form-label label_ui">制单日期：</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="createTime1" onfocus="WdatePicker()" />
							</span>
							<label class="label_2 align_c">至</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="createTime2" onfocus="WdatePicker()" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>

							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>

					<dl class="cl row-dl" style="display: none;" id="more_div">
						<dd class="row-dd">
							<sys:dateConfine label="交货日期" initDate="false" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="customerBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户料号：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="customerMaterialCode" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">产品规格：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="style" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div>
			</div>
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>