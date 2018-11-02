<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>生产任务列表</title>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/produce/task/task_list.js?v=${v }"></script>
</head>
<body>
	<form id="transmitForm"></form>
	<div class="page-container" id="productTaskId">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-生产任务列表"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="report:work:create">
						<button class="nav_btn table_nav_btn" id="btn_report">产量上报</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="reportTask:work:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="reportTask:work:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="生产日期" initDate="true"></sys:dateConfine>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap form_text">
								<input id="procedureName" type="text" class="input-txt input-txt_21" name="procedureName" value="" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" />
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

					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" id="billNo">
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_21" id="customerBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_21" id="saleBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户料号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_21" id="customerMaterialCode" />
							</span>
						</dd>
						<dd class="row-dd">
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">机台名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" id="machineName" />
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