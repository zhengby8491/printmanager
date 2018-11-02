<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购入库明细</title>
<script type="text/javascript" src="${ctxStatic}/site/purch/stock/detail.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl topPath"></div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" value="${supplierName}" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="materialName" type="text" class="input-txt input-txt_3" name="materialName" value="${materialName }" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">仓 库：</label>
							<span class="ui-combo-wrap" class="form_text">
								<phtml:list items="${fns:basicListParam('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="warehouseId" textProperty="name" defaultOption="请选择"></phtml:list>
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
					<dl class="cl" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">入库单号：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="billNo" type="text" class="input-txt input-txt_13" name="billNo" value="${billNo}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商分类：</label>
							<span class="ui-combo-wrap" class="form_text">
								<select id="supplierClassId" name="supplierClassId" class="input-txt input-txt_20 hy_select2">
									<option value="">请选择</option>
									<c:forEach items="${fns:basicList('SUPPLIERCLASS')}" var="i">
										<option value="${i.id }" <c:if test="${i.id==supplierClassId}">selected="selected"</c:if>>${i.name }</option>
									</c:forEach>
								</select>

							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料规格：</label>
							<span class="ui-combo-wrap wrap-width" class="form_text">
								<input id="style" type="text" class="input-txt input-txt_3" name="style" value="${style}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料分类：</label>
							<span class="ui-combo-wrap" class="form_text">
								<select id="materialClassId" name="materialClassId" class="input-txt input-txt_1 hy_select2">
									<option value="">请选择</option>
									<c:forEach items="${fns:basicList('MATERIALCLASS')}" var="i">
										<option value="${i.id }" <c:if test="${i.id==materialClassId}">selected="selected"</c:if>>${i.name }</option>
									</c:forEach>
								</select>

							</span>
						</dd>
					</dl>

					<dl class="cl" style="display: none;" id="more_div2">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">源单单号：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="sourceBillNo" type="text" class="input-txt input-txt_13" name="sourceBillNo" value="${sourceBillNo}" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;生&nbsp;产&nbsp;单&nbsp;号：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="workBillNo" type="text" class="input-txt input-txt_20" name="workBillNo" value="${workBillNo}" />
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
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->
		</div>

	</div>
</body>
</html>