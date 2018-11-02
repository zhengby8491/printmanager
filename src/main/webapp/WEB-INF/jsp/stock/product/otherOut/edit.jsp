<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/stock/product.js?v=" +${v}></script>
<meta charset="UTF-8">
<title>成品其它出库</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/otherOut/edit.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="库存管理-成品其它出库-编辑"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="stock:productOtherout:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="stock:productOtherout:edit,stock:productOtherout:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_stockProduct" method="post">
				<input name="id" type="hidden" class="input-txt input-txt_3" readonly="readonly" value="${order.id }" />
				<input type="hidden" name="isCheck" id="isCheck" />
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<!--头部END-->
				<!--查询表单START-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">仓库名称：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" name="warehouseId"
											onchange="shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" cssClass="input-txt input-txt_1 hy_select2" selected="${order.warehouseId }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">出库单号：</label>
									<span class="ui-combo-wrap">
										<input name="billNo" type="text" class="input-txt input-txt_3s" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">出库日期：</label>
									<span class="ui-combo-wrap">
										<input name="outTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.outTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">出 库 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="employeeId" textProperty="name" selected="${order.employeeId }"></phtml:list>
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
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 994px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
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
					<button type="button" id="product_quick_select" class="nav_btn table_nav_btn" value="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')[0].id}">
						<i class="fa fa-plus-square"></i>
						选择产品
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
									<th width="40"></th>
									<th width="140">产品编号</th>
									<th width="160">成品名称</th>
									<th width="100">产品规格</th>
									<th width="60">单位</th>
									<th width="80">数量</th>
									<shiro:hasPermission name="stock:productOtherout:money">
										<th width="80">成本单价</th>
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
											<input name="detailList.code" class="tab_input id_${item.stockProductId }" type="text" readonly="readonly" value="${item.code }" />
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
											<input name="detailList.price" class="tab_input" readonly="readonly" type="text" value="${item.price }" />
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
											<input name="detailList.stockProductId" class="tab_input" type="hidden" readonly="readonly" value="${item.stockProductId }" />
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