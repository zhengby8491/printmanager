<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>发外加工明细</title>
<script type="text/javascript" src="${ctxStatic }/site/outsource/process/detail_list.js?v=${v }"></script>
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
								<input type="text" class="input-txt input-txt_3" id="supplierName" name="supplierName" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;工&nbsp;序&nbsp;名&nbsp;称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="procedureName" name="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">加工单号：</label>
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
							<label class="form-label label_ui">交货日期：</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="deliveryTimeMin" onfocus="WdatePicker()">
							</span>
							<label class="label_2 align_c">至</label>
							<span>
								<input type="text" class="input-txt input-txt_0 Wdate" id="deliveryTimeMax" onfocus="WdatePicker()">
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;发&nbsp;外&nbsp;类&nbsp;型：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_21 hy_select2" defaultValue="-1" defaultOption="全部" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OutSourceType" name="outSourceType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商分类：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_12 hy_select2" items="${fns:basicList('SUPPLIERCLASS')}" defaultValue="-1" defaultOption="全部" valueProperty="id" textProperty="name" name="supplierClassId"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_5 hy_select2" defaultValue="-1" defaultOption="全部" textProperty="text" type="com.huayin.printmanager.persist.enumerate.ProcedureType" name="procedureType"></phtml:list>
							</span>
						</dd>


					</dl>

					<dl class="cl hide_container" style="display: none;" id="more_div2">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">源单单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" name="sourceBillNo" id="sourceBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;成&nbsp;品&nbsp;名&nbsp;称：</label>
							<span class="ui-combo-wrap form_text wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" />
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