<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/countValuationQty.js?v=" +${v}></script>
<meta charset="UTF-8">
<title>材料其它出库</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/otherOut/create.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-材料其它出库-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:materialOtherout:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:materialOtherout:create,stock:materialOtherout:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_stockMaterial" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO"/>
				<!--头部END-->
				<!--查询表单START-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" name="warehouseId" 
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL')" cssClass="input-txt input-txt_1 hy_select2" selected="${warehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">出库单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_6" readonly="readonly" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">出库日期：</label>
									<span class="ui-combo-wrap">
										<input id="outTime" name="outTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">领 料 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_6 hy_select2" name="receiveEmployeeId" textProperty="name"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">发 料 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="sendEmployeeId" textProperty="name"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" />
									</span>
								</dd>

								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 812px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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

				<!--查询表单END-->

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<button id="material_quick_select" type="button" class="nav_btn table_nav_btn" value="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')[0].id}">
						<i class="fa fa-plus-square"></i>
						选择材料
					</button>
				</div>
				<!--按钮栏End-->

				<!--表格部分START-->
				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40"></th>
									<th width="140">材料编号</th>
									<th width="120">材料名称</th>
									<th width="100">材料规格</th>
									<th width="60">克重</th>
									<th width="60">单位</th>
									<th width="80">出库数量</th>
									<th width="80">计价数量</th>
									<th width="80">计价单位</th>
									<shiro:hasPermission name="stock:materialOtherout:money">
										<th width="80">计价单价</th>
										<th width="80">成本单价</th>
										<th width="100">出库金额</th>
									</shiro:hasPermission>
									<th width="200">备注</th>
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