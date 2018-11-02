<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>工单转工序外发</title>
<script type="text/javascript" src="${ctxStatic}/site/outsource/transmit/to_process_procedure.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="发外管理-转单功能-工单转工序外发"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="outsource:process:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成外发加工单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:work:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:work:complete_cancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="outsource:transmit:cancelProcedureOut">
						<button class="nav_btn table_nav_btn" id="btn_cancel_out">取消发外</button>
					</shiro:hasPermission> --%>
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
								<input type="text" class="input-txt input-txt_3S" id="workBillNo" value="${workBillNo}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
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