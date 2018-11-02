<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建发外退货单</title>
<script type="text/javascript" src="${ctxStatic}/site/outsource/return/create.js?v=${v}"></script>
</head>
<body>
<input type="hidden" value='${supplierJson}' id="supplierJson">
<input type="hidden" value='${detailListJson}' id="detailListJson">
<div style="display: none;" id="PRODUCT">
<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" onchange = "shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouseId" textProperty="name"></phtml:list>
</div>
<div style="display: none;" id="SEMI_PRODUCT">
<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','SEMI_PRODUCT')}" onchange = "shotCutWindow('WAREHOUSE',true,$(this),'PRODUCT')" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouseId" textProperty="name"></phtml:list>
</div>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="发外管理-发外退货-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="outsource:return:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="outsource:return:create,outsource:return:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_return">
				<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="readonly" id="supplierName" name="supplierName" />
										<input type="hidden" id="supplierId" name="supplierId" />
										<input type="hidden" id="supplierCode" name="supplierCode" />
										<div class="select-btn" id="selectSupplier">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										</select>
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货员：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="hidden" name="employeeId" id="employeeId" />
										<input type="text" class="input-txt input-txt_1" readonly name="employeeName" id="employeeName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list type="com.huayin.printmanager.persist.enumerate.ReturnGoodsType" textProperty="text" name="returnGoodsType" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">

								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" textProperty="name" name="deliveryClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" textProperty="name" name="settlementClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" textProperty="name" name="paymentClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 907px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
					<button id="supplier_quick_select" type="button" class="nav_btn table_nav_btn" value="${order.supplierId }">
						<i class="fa fa-plus-square"></i>
						选择发外到货
					</button>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<!--表格Start-->
				<div>
					<div class="table-container">
						<div id="toolbar"></div>
						<table class="border-table" style="width: 1450px" id="detailList">
							<thead>
								<tr>
									<th width="30" name="seq">序号</th>
									<th width="40" name="operator"></th>
									<th width="100" name="warehouse">仓库</th>
									<th width="100" name="productName">成品名称</th>
									<th width="100" name="procedureName">工序名称</th>
									<th width="100" name="qty">退货数量</th>
									<shiro:hasPermission name="outsource:return:money">
										<th width="100" name="price">单价</th>
										<th width="100" name="money">金额</th>
										<th width="100" name="tax">税额</th>
									</shiro:hasPermission>
									<th width="100" name="taxRate">税收</th>
									<th width="100" name="taxRatePercent" style="display: none;">税率值%</th>
									<th width="100" name="noTaxPrice" style="display: none;">不含税单价</th>
									<th width="100" name="noTaxMoney" style="display: none;">不含税金额</th>
									<th width="100" name="sourceBillType">源单类型</th>
									<th width="100" name="type">发外类型</th>
									<th width="100" name="sourceBillNo">源单单号</th>
									<th width="100" name="sourceQty">源单数量</th>
									<th width="100" name="workBillNo">生产单号</th>
									<th width="100" name="produceNum">生产数量</th>
									<th width="100" name="style">加工规格</th>
									<th width="100" name="partName">部件名称</th>
									<th width="100" name="processRequire">加工要求</th>
									<th width="250" name="memo">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input name="detailList.workId" type="hidden" value="${detail.workId }" />
										<input name="detailList.procedureId" type="hidden" value="${detail.procedureId }" />
										<input name="detailList.procedureCode" type="hidden" value="${detail.procedureCode }" />
										<input name="detailList.productType" type="hidden" value="${detail.productType }" />
										<input name="detailList.workProcedureType" type="hidden" value="${detail.workProcedureType }" />
										<input name="detailList.procedureType" type="hidden" value="${detail.procedureType }" />

										<input name="detailList.taxRateId" type="hidden" value="${detail.taxRateId }" />
										<input name="detailList.taxRateName" type="hidden" value="${detail.taxRateName }" />

										<input name="detailList.sourceBillType" type="hidden" value="${detail.sourceBillType }" />

										<input name="detailList.sourceId" type="hidden" value="${detail.sourceId }" />
										<input name="detailList.sourceDetailId" type="hidden" value="${detail.sourceDetailId }" />
										<input name="detailList.productId" type="hidden" value="${detail.productId }" />

										<input name="detailList.type" type="hidden" value="${detail.type }" />

										<td>${status.count}</td>
										<td class="td-manage">
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
										</td>
										<td>
											<c:if test="${dtail.type=='PRODUCT' }">
												<phtml:list items="${product_warehouseList}" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouse" textProperty="name"></phtml:list>
											</c:if>
											<c:if test="${dtail.type=='PROCESS' }">
												<phtml:list items="${semi_product_warehouseList}" cssClass="tab_input bg_color" valueProperty="id" name="detailList.warehouse" textProperty="name"></phtml:list>
											</c:if>
										</td>
										<td>
											<c:if test="${detail.type=='PRODUCT' }">
												<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
												<input class="tab_input" type="hidden" name="detailList.productNames" readonly="readonly" value="" />
											</c:if>
											<c:if test="${detail.type=='PROCESS' }">
												<input class="tab_input" type="hidden" name="detailList.productName" readonly="readonly" value="" />
												<input class="tab_input" type="text" name="detailList.productNames" readonly="readonly"  value="${detail.productNames }" />
											</c:if>
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" />
										</td>

										<td>
											<c:choose>
												<c:when test="${detail.productType == 'ROTARY' && detail.workProcedureType == 'PART'}">
													<input class="tab_input bg_color constraint_decimal_negative" type="text" min="1" name="detailList.qty" value="${detail.qty}" />
												</c:when>
												<c:otherwise>
													<input class="tab_input bg_color constraint_negative" type="text" min="1" name="detailList.qty" value="${fns:removeLastZero(detail.qty)}" />
												</c:otherwise>
											</c:choose>
											<input type="hidden" name="detailList.saveQty" value="${detail.qty }" />
										</td>
										<!-- 判断是否有权限查看金额 --begin  -->
										<shiro:hasPermission name="outsource:return:money">
											<td>
												<input class="tab_input" type="text" readonly="readonly" name="detailList.price" value="${detail.price }" />
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="${detail.money }" />
												<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
											</td>
											<td>
												<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
											</td>
										</shiro:hasPermission>
										<!-- 判断是否有权限查看金额 --end  -->
										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" name="detailList.taxRate" textProperty="name" selected="${detail.taxRateId }"></phtml:list>
										</td>
										<td style="display: none;">
											<input type="text" name="detailList.taxRatePercent" readonly="readonly" value="${detail.taxRatePercent }" />
										</td>
										<td style="display: none;">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none;">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>

										<td>
											<input class="tab_input" type="text" readonly="readonly" value="${detail.sourceBillType.text }" />
										</td>
										<td>
											<input class="tab_input" type="text" readonly="readonly" value="${detail.type.text }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" <c:choose>
												<c:when test="${detail.productType == 'ROTARY' && detail.workProcedureType == 'PART'}">
													value = "${detail.sourceQty}"
												</c:when>
												<c:otherwise>
													value = "${fns:removeLastZero(detail.sourceQty)}"
												</c:otherwise>
											</c:choose> />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.workBillNo" readonly="readonly" value="${detail.workBillNo }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.produceNum" readonly="readonly" value="${detail.produceNum }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="${detail.style }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="${detail.partName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.processRequire" readonly="readonly" value="${detail.processRequire }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<!--表格End-->
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" id="sum_money" name="totalMoney" readonly="readonly" value="${order.totalMoney  }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" id="sum_noTaxMoney" name="noTaxTotalMoney" readonly value="${order.noTaxTotalMoney  }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" id="sum_tax" name="totalTax" readonly="readonly" value="${order.totalTax  }" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" />
								<input id="currencyTypeText" type="text" class="input-txt input-txt_3" readonly="true" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.createName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_3" readonly value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>