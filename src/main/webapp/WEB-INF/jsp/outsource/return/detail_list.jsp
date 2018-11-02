<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>发外退货明细</title>
<script type="text/javascript" src="${ctxStatic}/site/outsource/return/detail_list.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_0" id="procedureName" name="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">退货单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_5" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui">发外类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_13 hy_select2" defaultValue="-1" defaultOption="全部" name="outSourceType" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OutSourceType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商分类：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_16 hy_select2" items="${fns:basicList('SUPPLIERCLASS')}" defaultValue="-1" defaultOption="全部" valueProperty="id" name="supplierClassId" textProperty="name"></phtml:list>

							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_5 hy_select2" defaultValue="-1" defaultOption="全部" name="procedureType" textProperty="text" type="com.huayin.printmanager.persist.enumerate.ProcedureType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">加工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_5" name="outSourceBillNo" id="outSourceBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" name="workBillNo" id="workBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;源&nbsp;单&nbsp;单&nbsp;号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_16" name="sourceBillNo" id="sourceBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap form_text wrap-width">
								<input type="text" class="input-txt input-txt_0" id="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
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