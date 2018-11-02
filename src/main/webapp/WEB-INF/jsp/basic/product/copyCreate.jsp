<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/product/create.js?v=${v}"></script>
<title>添加产品信息 - 产品信息管理</title>
<style type="text/css">
.info_table {
	width: 620px;
	left: 8px;
	display: block;
	position: relative;
	float: left;
}

.img_table {
	margin-top: 10px;
	width: 280px;
	left: 687px;
	position: absolute;
	height: 235px;
	border: 1px solid #ccc;
	overflow: hidden
}

.product_img {
	position: absolute;
	top: 4px;
	right: 118px
}

.uploadBtn {
	position: relative;
	top: 75px;
	right: -105px
}

.delBtn {
	position: absolute;
	top: 116px;
	right: 108px
}

.container_div {
	position: relative;
	top: 20px
}

img {
	width: 100%;
	height: auto
}
</style>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							产品分类：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('PRODUCTCLASS')}" valueProperty="id" cssClass="hy_select2" selected="${product.productClassId}" name="productClassId" textProperty="name"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							客户料号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<!-- FIX BUG 2209 zhangtai ${product.customerMaterialCode} -->
							<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="customerMaterialCode" name="customerMaterialCode">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							产品编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" name="code" class="input-txt input-txt_7" readonly="true" value="${code}" placeholder="" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							成品名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<!-- FIX BUG 2209 zhangtai ${product.name} -->
							<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							产品规格：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${product.specifications}" placeholder="" id="specifications" name="specifications">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							单位：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" cssClass="hy_select2" name="unitId" textProperty="name" selected="${product.unitId}"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							单价：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="constraint_decimal_negative input-txt input-txt_7" value="${product.salePrice}" placeholder="" id="salePrice" name="salePrice">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否公共产品：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isPublic" textProperty="text" selected="${product.isPublic}" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否有效：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isValid" textProperty="text" cssClass="hy_select2" selected="${product.isValid}" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 824px; resize: none">${product.memo }</textarea>
							<p class="textarea-numberbar">
								<em>0</em>
								/100
							</p>
						</div>
					</div>
				</div>
			</div>
			<!--表单部分END-->
			<!--表格部分START-->
			<div class="info_table">
				<div class="tab_page">
					<ul class="tab_page_list">
						<li class="active">客户信息</li>
					</ul>
					<input type="hidden" name="fileName" id="fileName" />
				</div>
				<div class="info_table_container">
					<div class="info_item info_item_1">
						<div class="info_item_btn">
							<button type="button" class="table_nav_btn info_btn" id="selectCustomer">
								<i class="fa fa-plus"></i>
								选择客户
							</button>
						</div>
						<div class="info_item_table">
							<table class="layer_table layer_info_table" rules="all" id="customerList">
								<thead>
									<tr>
										<th name="seq" width="30">序号</th>
										<th name="code">客户编码</th>
										<th name="name">客户名称</th>
										<th name="operator" width="80">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="customer" items="${product.customerList}" varStatus="status">
										<tr>
											<td>${status.count }</td>
											<td>${customer.code }</td>
											<td>${customer.name }</td>
											<td>
												<input type="hidden" name="customerList.id" value="${customer.id }">
												<a title="删除" href="javascript:;" name="btn_del">
													<i class="fa fa-trash-o"></i>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="img_table">
				<div class="tab_page">
					<span class="product_img">产品图片</span>
				</div>
				<div class="info_table_container" id="containerDiv">
					<input type="file" id="upload" name="upfile" style="display: none;">
					<input type="button" class="nav_btn table_nav_btn delBtn" id="delBtn" value="删除图片" style="display: none;">
					<div id="show_img" name="productImg" class="img_div"></div>
				</div>
				<input type="button" class="nav_btn table_nav_btn uploadBtn" id="addBtn" value="上传图片" style="display: none;" />
			</div>
			<!--表格部分END-->
			<div style="margin-left: 473px; margin-top: 10px; display: block; position: relative; float: left;">
				<input class="nav_btn table_nav_btn" type="button" id="btn_save" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>
