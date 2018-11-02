<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>自动分贴</title>
<style type="text/css">
tr.active_tr {
	background: cornflowerblue !important;
}

tr.active_tr td {
	color: white !important;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/produce/auto_stricks.js?v=${v}"></script>
</head>
<body>
	<div class="layer_container">
		<div class="cl layer_content">
			<!--表格容器左START-->
			<div class="layer_table_container" style="left: 0; width: 670px; height: 270px;">
				<div class="cl layer_top">
					<div class="row-dd" style="margin-left: 5px">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap form_text">
							<label class="form-label label_ui">总P数：</label>
							<input type="text" class="input-txt input-txt_8 constraint_negative" id="totalPnum" />
						</span>
						<span class="ui-combo-wrap form_text" style="margin-left: 5px;">
							<label class="form-label label_ui">封面P数：</label>
							<input type="text" class="input-txt input-txt_8 constraint_negative" id="coverPnum" value="4" />
						</span>
						<span class="ui-combo-wrap form_text" style="margin-left: 5px;">
							<label class="form-label label_ui">开本：</label>
							<input type="text" class="input-txt input-txt_8 constraint_negative" id="openNum" />
						</span>
						<div class="layer_btns">
							<button type="button" class="nav_btn table_nav_btn" style="margin-left: 10px;" id="btn_ok" />
							开始分贴
							</button>
							<span style="margin-left: 87px;">
								<button type="button" class="nav_btn table_nav_btn" id="btn_confirm">选择</button>
								<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">关闭</button>
							</span>
						</div>
					</div>
				</div>

				<!-- 表格 -->
				<div class="table-wrap" style="height: 210px;">
					<table class="layer_table" id="bootTable">
						<thead>
							<th width="80px;">序号</th>
							<th>分贴描述</th>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
