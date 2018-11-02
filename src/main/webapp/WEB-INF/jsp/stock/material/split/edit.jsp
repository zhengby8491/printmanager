<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=${v}"></script>
<meta charset="UTF-8">
<title>材料分切</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/split/edit.js?v=${v}"></script>
</head>
<body>
<div style="display: none;" id="phtml">
<phtml:list items="${fns:basicListParam2(\'WAREHOUSE\',\'warehouseType\',\'MATERIAL\')}" valueProperty="id"  onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="tab_input input-txt" name="detailList.warehouseId" textProperty="name" ></phtml:list>
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料分切-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:materialSplit:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:materialSplit:create,stock:materialSplit:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_split" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<input type="hidden" name="id" value="${order.id }" />
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓 库：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" cssClass="input-txt input-txt_9 hy_select2" 
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" name="warehouseId" textProperty="name" selected="${order.warehouseId }"></phtml:list>
									</span>
									<input type="hidden" id="warehouseName" name="warehouseName" />
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="splitType" textProperty="text" cssClass="hy_select2 input-txt_1" type="com.huayin.printmanager.persist.enumerate.SplitType" selected="${order.splitType }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" class="input-txt input-txt_1" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切日期：</label>
									<span class="ui-combo-wrap">
										<input id="splitTime" type="text" name="splitTime" class="input-txt input-txt_1 Wdate" onfocus="WdatePicker({lang:'zh-cn'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.splitTime }' type='date' />" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">材料名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_6" readonly="readonly" id="materialName" name="materialName" value="${order.materialName }" />
										<input type="hidden" id="materialId" name="materialId" value="${order.materialId }" />
										<input type="hidden" id="stockMaterialId" name="stockMaterialId" value="${order.stockMaterialId }" />
										<input type="hidden" id="code" name="code" value="${order.material.code }" />
										<input type="hidden" id="weight" name="weight" value="${order.material.weight }" />
										<div class="select-btn" id="selectMaterial">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">规格：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="specifications" name="specifications" class="input-txt input-txt_1" readonly="readonly" value="${order.specifications }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">单位：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="stockUnitName" class="input-txt input-txt_1" readonly="readonly" value="${order.stockUnitName }" />
										<input type="hidden" id="stockUnitId" name="stockUnitId" value="${order.stockUnitId }" />
										<input type="hidden" id="price" name="price" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">数量：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="qty" name="qty" class="input-txt input-txt_1" value="${order.qty }" />
										<input type="hidden" id="money" name="money" value="0" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">创 建 人：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input name="createName" type="text" class="input-txt input-txt_9" readonly="readonly" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">创建日期：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">计价单位：</label>
									<span class="ui-combo-wrap">
										<input id="valuationUnitName" type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.valuationUnitName }" />
										<input type="hidden" id="valuationUnitId" name="valuationUnitId" value="${order.valuationUnitId }" />
										<input type="hidden" id="valuationPrice" name="valuationPrice" value="${order.valuationPrice }" />
										<input type="hidden" id="valuationUnitAccuracy" value="${fns:basicInfo('UNIT',order.valuationUnitId).accuracy}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">计价数量：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="valuationQty" name="valuationQty" class="input-txt input-txt_1" readonly="readonly" value="${order.valuationQty }" />
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
					<a id="add_row" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						增加材料
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1200px" id="detailList">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="80">操作</th>
									<th width="120">材料编号</th>
									<th width="120">材料名称</th>
									<th width="100">分切规格</th>
									<th width="60">克重</th>
									<th width="60">单位</th>
									<th width="80">数量</th>
									<th width="60">计价单位</th>
									<th width="80">计价数量</th>
									<th width="80">
										入库仓
										<i id="batch_edit_wareHouse" class="fa fa-edit" src="batch_wareHouse_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<shiro:hasPermission name="stock:materialSplit:money">
										<th width="80">成本单价</th>
										<th width="100">成本金额</th>
									</shiro:hasPermission>
									<th>备注</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody id="tbody">
								<c:forEach items="${order.detailList }" var="detailList" varStatus="status">
									<tr>
										<td name="rowIndex">1</td>
										<td class="td-manage">
											<i class="delete_row fa fa-trash-o"></i>
										</td>
										<td>
											<input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="${detailList.code }">
											<input name="detailList.id" type="hidden" value="${detailList.id}">
										</td>
										<td>
											<input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="${detailList.materialName }">
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input bg_color" type="text" value="${detailList.specifications }">
										</td>
										<td>
											<input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="${detailList.weight }">
										</td>
										<td>
											<input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="${detailList.stockUnitName }">
											<input name="detailList.stockUnitId" type="hidden" value="${detailList.stockUnitId}">
										</td>
										<td>
											<input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="${detailList.qty }">
										</td>
										<td>
											<input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="${detailList.valuationUnitName }">
											<input name="detailList.valuationUnitId" type="hidden" value="${detailList.valuationUnitId}">
										</td>
										<td>
											<input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="${detailList.valuationQty }">
											<input name="valuationUnitAccuracy" type="hidden" value="${fns:basicInfo('UNIT',detailList.valuationUnitId).accuracy}">
										</td>
										<td>
											<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" cssClass="tab_input input-txt"
												onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" name="detailList.warehouseId" textProperty="name" selected="${detailList.warehouseId }"></phtml:list>
										</td>

										<td>
											<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="${detailList.price }">
											<input name="detailList.valuationPrice" type="hidden" value="${detailList.valuationPrice }">
										</td>
										<td>
											<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="${detailList.money }">
										</td>

										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${detailList.memo }">
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
	<div id="batch_wareHouse_box" class="batch_wareHouse_box">
		<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" cssClass="tab_input bg_color input-txt_3 batch_wareHouse_select" valueProperty="id" name="detailList.warehouseId" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
	</div>
</body>
</html>