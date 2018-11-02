<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/produce/transmit/to_product_in.js?v=${v }"></script>
<title>工单转入库</title>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-转单功能-工单转入库"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:productIn:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成成品入库</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:work:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="produce:work:complete_cancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>

				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<sys:dateConfine label="制单日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_3" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="customerName" type="text" class="input-txt input-txt_9" name="customerName" value="" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" value="" name="productName" />
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
							<label class="form-label label_ui">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="sourceBillNo" name="sourceBillNo" type="text" class="input-txt input-txt_13" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="customerBillNo" name="customerBillNo" type="text" class="input-txt input-txt_3" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户料号：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="customerMaterialCode" type="text" class="input-txt input-txt_9" name="customerMaterialCode" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">产品规格：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="style" value="" name="style" />
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
			<!--查询表单End-->
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