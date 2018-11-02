<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择库存</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/quick_select.js?v=${v}"></script>
</head>
<body>
	<input type="hidden" id="materialClassId" name="materialClassId" />
	<input type="hidden" id="warehouseId" name="warehouseId" value="${warehouseId }" />
	<input type="hidden" id="multiple" name="multiple" value="${multiple }" />
	<input type="hidden" id="isEmptyWare" value="${isEmptyWare }" />
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<li>
						<a href="javascript:void(0);" _id="" name="materialClassId">全部</a>
					</li>
					<c:forEach items="${fns:basicList('MATERIALCLASS') }" var="item">
						<li>
							<a href="javascript:void(0);" _id="${item.id }" name="materialClassId">${item.name }</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container" style="width:880px;">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="materialName" placeholder="材料名称" name="materialName" />
							<input type="text" class="input-txt input-txt_9" id="specifications" placeholder="材料规格" name="specifications" />
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
