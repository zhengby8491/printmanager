<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/product.js?r="+Math.random()></script>
<meta charset="UTF-8">
<title>成品库存调整</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/adjust/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-成品库存调整-创建"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:productAdjust:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:productAdjust:create,stock:productAdjust:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_adjust" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" name="warehouseId" 
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" cssClass="input-txt input-txt_1 hy_select2" selected="${warehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调整单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调整日期：</label>
									<span class="ui-combo-wrap">
										<input id="adjustTime" name="adjustTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调 整 人：</label>
									<span class="ui-combo-noborder">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="employeeId" textProperty="name"></phtml:list>
										<input id="employeeName" name="employeeName" type="hidden" value="${fns:basicList('EMPLOYEE')[0].name}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" class="noborder" style="width: 996px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
					<button type="button" id="product_quick_select" class="nav_btn table_nav_btn" value="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')[0].id}">
						<i class="fa fa-plus-square"></i>
						选择产品
					</button>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1250px">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="140">产品编号</th>
									<th width="160">成品名称</th>

									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="80">数量</th>
									<shiro:hasPermission name="stock:productAdjust:money">
										<th width="60">成本单价</th>
										<th width="100">金额</th>
									</shiro:hasPermission>
									<th width="200">备注</th>
									<th width="60">产品图片</th>
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
</body>
</html>