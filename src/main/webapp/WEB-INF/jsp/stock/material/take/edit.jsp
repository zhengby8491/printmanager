<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<meta charset="UTF-8">
<title>生产领料</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/take/edit.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-生产领料-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:materialTake:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:materialTake:edit,stock:materialTake:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
				</div>
			</div>
			<form id="form_take" method="post">
				<input name="id" type="hidden" value="${order.id }" />
				<input type="hidden" name="isCheck" id="isCheck" />
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO"/>
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')"
											cssClass="input-txt input-txt_1 hy_select2" name="warehouseId" textProperty="name" selected="${order.warehouseId}"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">领料单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" class="input-txt input-txt_3" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">发 料 人：</label>
									<span class="ui-combo-noborder">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="sendEmployeeId" textProperty="name" selected="${order.sendEmployeeId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">领料日期：</label>
									<span class="ui-combo-wrap">
										<input id="takeTime" type="text" name="takeTime" class="input-txt input-txt_1 Wdate" onfocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.takeTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">领 料 人：</label>
									<span class="ui-combo-noborder">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="receiveEmployeeId" textProperty="name" selected="${order.receiveEmployeeId }"></phtml:list>
									</span>
								</dd>

							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" class="noborder" style="width: 803px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="material_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择材料
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1300px" id="detailList">
							<thead>
								<tr>
									<th width="30">序号</th>
									<th width="40"></th>
									<th width="100">材料编号</th>
									<th width="100">材料名称</th>
									<th width="70">材料规格</th>
									<th width="40">单位</th>
									<th width="70">领料数量</th>
									<th width="70">源单数量</th>
									<shiro:hasPermission name="stock:materialTake:money">
										<th width="60">成本单价</th>
										<th width="70">成本金额</th>
									</shiro:hasPermission>
									<th width="60">源单类型</th>
									<th width="100">生产单号</th>
									<th width="80">成品名称</th>
									<th width="200">备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody id="tbody">
								<c:forEach items="${order.detailList }" var="detailList" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage">
											<i class="row_delete fa fa-trash-o"></i>
										</td>
										<td>
											<input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="${detailList.code }" />
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${detailList.materialName }" />
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input" type="text" readonly="readonly" value="${detailList.specifications }" />
										</td>
										<td>
											<input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',detailList.stockUnitId).name}" />
											<input name="detailList.stockUnitId" class="tab_input" type="hidden" value="${detailList.stockUnitId}" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="<fmt:formatNumber value="${detailList.qty }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.sourceQty" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${detailList.sourceQty }" type="currency" pattern="0.####"/>" />
										</td>
										<%-- <shiro:hasPermission name="stock:materialTake:money"> --%>
										<td>
											<input name="detailList.price" class="tab_input constraint_decimal bg_color" type="text" value="<fmt:formatNumber value="${detailList.price }" type="currency" pattern="0.####"/>" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="<fmt:formatNumber value="${detailList.money }" type="currency" pattern="0.####"/>" />
										</td>
										<%-- </shiro:hasPermission> --%>
										<td>
											<input name="detailList.sourceBillTypeName" class="tab_input" type="text" readonly="readonly" value="${detailList.sourceBillType.text }" />
										</td>
										<td>
											<input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="readonly" value="${detailList.sourceBillNo }" />
										</td>
										<td>
											<input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="${detailList.productName }" />
											<input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="${detailList.productId }" />
										</td>
										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${detailList.memo }" />
											<input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${detailList.id }" />
											<input name="detailList.weight" class="tab_input" type="hidden" readonly="readonly" value="${detailList.weight }" />
											<input name="detailList.materialId" type="hidden" readonly="readonly" value="${detailList.materialId }" />
											<input name="detailList.materialClassId" type="hidden" readonly="readonly" value="${detailList.materialClassId }" />
											<input name="detailList.sourceId" type="hidden" readonly="readonly" value="${detailList.sourceId }" />
											<input name="detailList.sourceDetailId" type="hidden" readonly="readonly" value="${detailList.sourceDetailId }" />
											<input name="detailList.valuationUnitName" type="hidden" readonly="readonly" value="${detailList.valuationUnitName }" />
											<input name="detailList.valuationQty" type="hidden" readonly="readonly" value="" />
											<input name="detailList.valuationPrice" type="hidden" readonly="readonly" value="" />
											<c:if test="${detailList.sourceBillType!=null and detailList.sourceBillType!=''  }">
												<input name="detailList.sourceBillType" class="tab_input" type="hidden" readonly="readonly" value="${detailList.sourceBillType }" />
											</c:if>
											<input name="valuationUnitAccuracy" type="hidden" value="${fns:basicInfo('UNIT',detailList.valuationUnitId).accuracy}" />
											<input name="stockUnitAccuracy" type="hidden" value="${fns:basicInfo('UNIT',detailList.stockUnitId).accuracy}" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>