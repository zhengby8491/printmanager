<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=${v}"></script>
<meta charset="UTF-8">
<title>材料分切</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/split/create.js?v=${v}"></script>
</head>
<body>
<div style="display: none;" id="phtml">
<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="tab_input input-txt" name="detailList.warehouseId" textProperty="name" ></phtml:list>
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料分切-创建"></sys:nav>
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
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓 库：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" 
											valueProperty="id" cssClass="input-txt input-txt_9 hy_select2" name="warehouseId" textProperty="name"></phtml:list>
									</span>
									<input type="hidden" id="warehouseName" name="warehouseName" />
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="splitType" textProperty="text" cssClass="hy_select2 input-txt_1" type="com.huayin.printmanager.persist.enumerate.SplitType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" class="input-txt input-txt_1" readonly="readonly" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">分切日期：</label>
									<span class="ui-combo-wrap">
										<input id="splitTime" type="text" name="splitTime" class="input-txt input-txt_1 Wdate" onfocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">规格：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="specifications" name="specifications" class="input-txt input-txt_1" readonly="readonly" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">材料名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_6" readonly="readonly" id="materialName" name="materialName" />
										<input type="hidden" id="materialId" name="materialId" />
										<input type="hidden" id="stockMaterialId" name="stockMaterialId" />
										<input type="hidden" id="code" name="code" />
										<input type="hidden" id="materialClassId" name="materialClassId" />
										<input type="hidden" id="weight" name="weight" />
										<div class="select-btn" id="selectMaterial">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">单位：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="stockUnitName" class="input-txt input-txt_1" readonly="readonly" />
										<input type="hidden" id="stockUnitId" name="stockUnitId" />
										<input type="hidden" id="price" name="price" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">数量：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="qty" name="qty" class="input-txt input-txt_1" />
										<input type="hidden" id="money" name="money" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">计价单位：</label>
									<span class="ui-combo-wrap">
										<input id="valuationUnitName" type="text" class="input-txt input-txt_1" readonly="readonly" />
										<input type="hidden" id="valuationUnitId" name="valuationUnitId" />
										<input type="hidden" id="valuationPrice" name="valuationPrice" />
										<input type="hidden" id="valuationUnitAccuracy" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">计价数量：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="valuationQty" name="valuationQty" class="input-txt input-txt_1" readonly="readonly" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" class="noborder" style="width: 836px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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