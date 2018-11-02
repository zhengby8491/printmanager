<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/basic/product/work_barcode_report.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/basic/product/initreport.js?v=${v }"></script>
<title>条码上报</title>
<style type="text/css">
.border-table tbody tr {
	background-color: #f1f1f1 !important;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/produce/work_barcode_report.js"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-生产列表-条码上报"></sys:nav>
				</div>
				<div class="top_nav">

					<shiro:hasPermission name="report:work:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="report:work:create,report:work:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<!-- 	<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button> -->
					<input type="hidden" id="sourceWorkId" value="${sourceWorkId }">
					<input type="hidden" id="isCheck">
				</div>
			</div>
			<div class="search_container">
				<dl class="cl row-dl">
					<dd class="row-dd">
						<label class="form-label label_ui">条码扫码：</label>
						<span class="ui-combo-wrap form_text">
							<input id="billNo" onkeydown="if(event.keyCode==13){inputBillNo(this);return false;}" name="billNo" placeholder="支持扫工单条码/工序条码" type="text" class="input-txt input-txt_13" value="" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui">员工信息：</label>
						<span class="ui-combo-wrap form_text">
							<input id="employee" name="employee" placeholder="" type="text" class="input-txt input-txt_13" value="" />
							<div class="select-btn" id="employee_quick_select">...</div>
							<input type="hidden"id="employeeId" name="employeeId">
						</span>
					</dd>
				</dl>

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