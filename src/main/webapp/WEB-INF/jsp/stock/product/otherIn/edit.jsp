<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/product.js?r="+Math.random()></script>
<meta charset="UTF-8">
<title>成品其它入库</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/otherIn/edit.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-成品品其它入库-编辑"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:productOtherin:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:productOtherin:edit,stock:productOtherin:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_stockProduct" method="post">
				<input name="id" type="hidden" class="input-txt input-txt_3" readonly="readonly" value="${order.id }" />
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--头部END-->
				<!--查询表单START-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2"
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" name="warehouseId" textProperty="name" selected="${order.warehouseId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">入库单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">入库日期：</label>
									<span class="ui-combo-wrap">
										<input name="inTime" type="text" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.inTime}' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">入 库 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="employeeId" textProperty="name" selected="${order.employeeId}"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime}' type='date' />" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 1012px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo}</textarea>
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
					<button type="button" class="nav_btn table_nav_btn product_quick_select">
						<i class="fa fa-plus-square"></i>
						新增产品
					</button>
				</div>
				<!--按钮栏End-->
				<!--表格部分START-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1250px">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="60">操作</th>
									<th width="140">产品编号</th>
									<th width="160">成品名称</th>
									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="80">入库数量</th>
									<shiro:hasPermission name="stock:productOtherin:money">
										<th width="60">成本单价</th>
										<th width="100">金额</th>
									</shiro:hasPermission>
									<th width="200">备注</th>
									<th width="60">产品图片</th>
								</tr>
								<tr></tr>
							</thead>
							<tbody id="tbody">
								<c:forEach items="${order.detailList }" var="item" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage">
											<i class="delete_row fa fa-trash-o"></i>
										</td>
										<td>
											<input name="detailList.code" class="tab_input id_${item.productId } " type="text" readonly="readonly" value="${item.code }" />
										</td>
										<td>
											<input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="${item.productName }" />
										</td>
										<td>
											<input name="detailList.specifications" class="tab_input" readonly="readonly" type="text" value="${item.specifications }" />
										</td>
										<td>
											<input name="detailList.unitName" class="tab_input" type="text" readonly="readonly" value="${fns:basicInfo('UNIT',item.unitId).name}" />
										</td>
										<td>
											<input name="detailList.qty" class="tab_input bg_color" type="text" value="${item.qty }" />
										</td>

										<td>
											<input name="detailList.price" class="tab_input bg_color" type="text" value="${item.price }" />
										</td>
										<td>
											<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="${item.money }" />
										</td>

										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${item.memo }" />
										</td>
										<td>
											<c:if test="${item.imgUrl !=''}">
												<img class="pimg" src="${item.imgUrl }" />
											</c:if>
										</td>
										<td style="display: none">
											<input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${item.id }" />
										</td>
										<td style="display: none">
											<input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="${item.productId }" />
										</td>
										<td style="display: none">
											<input name="detailList.unitId" class="tab_input" type="hidden" readonly="readonly" value="${item.unitId }" />
										</td>
										<td style="display: none">
											<input name="detailList.productClassId" class="tab_input" type="hidden" readonly="readonly" value="${item.productClassId }" />
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