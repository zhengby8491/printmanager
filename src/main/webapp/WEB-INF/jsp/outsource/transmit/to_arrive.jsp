<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>发外加工转发外到货</title>
<script type="text/javascript" src="${ctxStatic}/site/outsource/transmit/to_arrive.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="发外管理-转单功能-发外加工转发外到货"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="outsource:arrive:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成外发到货单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="outsource:arrive:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="outsource:arrive:complete_cancel">
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
							<label class="form-label label_ui">加工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_0" id="billNo" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_0" id="supplierName" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt" id="procedureName" style="width: 78px" />
								<div class="select-btn" id="procedure_quick_select">...</div>
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

					<dl class="cl row-dl" style="display: none;" id="more_div2">
						<dd class="row-dd">
							<label class="form-label label_ui">交货日期：</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="deliverDateMin" onfocus="WdatePicker()">
							</span>
							<label class="label_2 align_c">至</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="deliverDateMax" onfocus="WdatePicker()">
							</span>
						</dd>



						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_0" id="workBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;成&nbsp;品&nbsp;名&nbsp;称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_0" id="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
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