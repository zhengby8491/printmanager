<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择销售单</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/quick_select_forwork.js?v=${v}"></script>
</head>
<body>
	<input type="hidden" id="productType" value="${productType }">
	<div class="layer_container">
		<div class="cl layer_content">
			<!--表格容器左START-->
			<div class="layer_table_container" style="width: 945px; left: 0;">
				<!-- 查询栏 -->
				<div class="cl layer_top">
					<div class="row-dd" style="margin-left: 5px">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<sys:dateConfine label="制单日期" />
							<input type="text" class="input-txt input-txt_9" id="billNo" value="${billNo }" placeholder="销售单号" />
							<input type="text" class="input-txt input-txt_9" id="customerName" value="${customerName }" placeholder="客户名称" />
							<input type="text" class="input-txt input-txt_9" id="productName" value="${productName }" placeholder="成品名称" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="height: 390px;">
					<table class="layer_table" id="bootTable">

					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
