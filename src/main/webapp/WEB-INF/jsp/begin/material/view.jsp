<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/material.js "></script>
<script type="text/javascript" src="${ctxStatic}/site/begin/material/view.js?v=${v }"></script>
<title>材料期初</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-期初设置-材料期初"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<span id="isCheckNO" style="display: none;">
						<shiro:hasPermission name="begin:material:edit">
							<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="begin:material:audit">
							<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="begin:material:del">
							<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
						</shiro:hasPermission>
					</span>
					<span id="isCheckYES" style="display: none;">
						<shiro:hasPermission name="begin:material:auditCancel">
							<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
						</shiro:hasPermission>
					</span>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">单据编号：</label>
								<span class="ui-combo-wrap">
									<input id="billNo" type="text" name="billNo" class="input-txt input-txt_3" readonly="readonly" />
									<input id="id" type="hidden" value="${id }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">期初日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" id="beginTime" name="beginTime" class="input-txt input-txt_3 Wdate" readonly="readonly" pattern="yyyy-MM-dd" />
								</span>
							</dd>

							<dd class="row-dd">
								<label class="form-label label_ui label_1">制 单 人：</label>
								<span class="ui-combo-wrap">
									<input id="createName" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">制单日期：</label>
								<span class="ui-combo-wrap">
									<input id="createTime" type="text" class="input-txt input-txt_3" readonly="readonly" pattern="yyyy-MM-dd" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">审核日期：</label>
								<span class="ui-combo-wrap">
									<input id="checkTime" type="text" class="input-txt input-txt_3" readonly="readonly" pattern="yyyy-MM-dd" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">审 核 人：</label>
								<span class="ui-combo-wrap">
									<input id="checkUserName" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">仓库名称：</label>
								<span class="ui-combo-wrap">
									<input id="warehouseName" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea id="memo" class="noborder" name="memo" readonly="readonly" style="width: 1005px"></textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>

			<!--查询表单END-->

			<!--表格Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格End-->

			<!--审核标签-->
			<div class="review" style="display: none;">
				<span class="review_font">已审核</span>
			</div>
		</div>
	</div>
</body>
</html>