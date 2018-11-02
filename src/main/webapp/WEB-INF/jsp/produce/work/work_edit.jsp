<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/common/work/workCommon.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic}/site/produce/work/work_edit.js?v=${v }"></script>
<title>编辑生产工单</title>
<style type="text/css">
.tab_input {
	height: 30px
}
</style>
</head>

<body>
	<div class="page-container">
		<div class="shadeHide" style="position: relative;">
			<div class="page-border">
				<!--导航按钮-->
				<div class="cl">
					<div class="iframe-top">
						<sys:nav struct="生产管理-生产工单-编辑"></sys:nav>
					</div>
					<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
						<div id="innerdiv" style="position: absolute;">
							<img id="bigimg" style="border: 5px solid #fff;" src="" />
						</div>
					</div>
					<div class="top_nav">

						<shiro:hasPermission name="produce:work:edit">
							<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
						</shiro:hasPermission>
						<shiro:hasManyPermissions name="produce:work:edit,produce:work:audit">
							<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
						</shiro:hasManyPermissions>
						<!-- DBG -->
						<button id="saveChange_material" class="nav_btn table_nav_btn">保存变更</button>
						<button id="saveChange_process" class="nav_btn table_nav_btn">保存变更</button>
						<button id="eaditProcessShade" class="nav_btn table_nav_btn">取消变更</button>
						<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						<input id="orderId" type="hidden" value="${order.id }">
						<input id="isCheck" type="hidden">
					</div>
				</div>
				<!--表单部分上-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">印刷类型：</label>
									<span class="ui-combo-noborder">
										<phtml:list type="com.huayin.printmanager.persist.enumerate.ProductType" selected="${order.productType }" textProperty="text" name="productType" cssClass="hy_select2 input-txt input-txt_3s" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">工单类型：</label>
									<span class="ui-combo-noborder">
										<phtml:list items="${billTypeList}" textProperty="text" name="billType" selected="${order.billType }" cssClass="hy_select2 input-txt input-txt_3s" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">工单单号：</label>
									<span class="ui-combo-noborder">
										<input type="text" class="input-txt input-txt_3" id= "billNo" readonly="readonly" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.createName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap" class="form_text">
										<input type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备 注：</label>
									<span class="form_textarea">
										<textarea onKeyUp="Helper.doms.textarealength(this,1000)" class="noborder l fold_textarea requireText" style="width: 869px" id="memo">${order.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/1000
										</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--表格部分-->
				<div class="cl mr_t">
					<div class="btn-billbar mb15">
						<span>
							<a href="javascript:;" class="nav_btn table_nav_btn" id="select_sale_detail">
								<i class="fa fa-plus-square"></i>
								选择源单
							</a>
							<a href="javascript:;" class="nav_btn table_nav_btn" id="select_product">
								<i class="fa fa-plus-square"></i>
								选择产品
							</a>
							<a href="javascript:;" class="nav_btn table_nav_btn" id="add_part">
								<i class="fa fa-plus-square"></i>
								添加部件
							</a>
						</span>
						<span class="work_checkbox">
							<label>
								<input type="checkbox" id="isOutSource" <c:if test="${order.isOutSource eq 'YES' }">checked</c:if> />
								整单发外
							</label>
							<label>
								<input type="checkbox" id="isEmergency" <c:if test="${order.isEmergency eq 'YES' }">checked</c:if> />
								急单
							</label>
						</span>
						<c:if test="${order.productType == 'BOOK' }">
						<span>
							<a href="javascript:;" class="nav_btn table_nav_btn" id="auto_stricker">
								自动分贴
							</a>
						</span>
						</c:if>
					</div>
					<!--产品信息部分Start-->
					<div class="fold_wrap" id="productList_div">
						<div class="fold_table ">
							<input type="hidden" id="product_sum_saleProduceQty" value="0" />
							<input type="hidden" id="product_sum_spareProduceQty" value="0" />
							<input type="hidden" id="product_sum_produceQty" value="0" />
							<table class="work_table product_table" id="product_table">
								<thead>
									<tr>
										<th width="40" name="operator">操作</th>
										<th name="productName">成品名称</th>
										<th name="customerName">客户名称</th>
										<th name="style">产品规格</th>
										<th name="unit">单位</th>
										<th name="sourceQty">销售数量</th>
										<th name="saleProduceQty">生产数量</th>
										<th name="spareProduceQty">备品数量</th>
										<th name="produceQty">生产数量</th>
										<th name="sourceBillNo">销售单号</th>

										<th name="price">单价</th>
										<th name="money">金额</th>

										<th name="customerBillNo">客户单号</th>
										<th name="customerMaterialCode">客户料号</th>
										<th name="deliveryTime">
											交货日期
											<i id="batch_edit_deliveryTime" class="fa fa-edit" src="batch_deliveryTime_box"></i>
											<div class="batch_box_container"></div>
										</th>
										<th name="customerRequire">客户要求</th>
										<th name="memo">备注</th>
										<th name="imgUrl">产品图片</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="product" items="${order.productList}">
										<tr>
											<td class="td-manage">
												<input name="productList.id" type="hidden" value="${product.id}" />
												<input name="productList.productId" type="hidden" value="${product.productId}" />
												<i class="delete fa fa-trash-o"></i>
											</td>
											<td>
												<input name="productList.productName" class="tab_input" type="text" onmouseover="this.title=this.value" readonly="readonly" value="${product.productName}" />
											</td>
											<td>
												<input name="productList.customerId" class="tab_input" type="hidden" value="${product.customerId}" />
												<input name="productList.customerCode" class="tab_input" type="hidden" value="${product.customerCode}" />
												<input name="productList.customerName" class="tab_input" readonly="readonly" type="text" onmouseover="this.title=this.value" value="${product.customerName}" />
											</td>
											<td>
												<input name="productList.style" class="tab_input" readonly="readonly" type="text" value="${product.style}" />
											</td>
											<td>
												<input name="productList.unitId" class="tab_input" type="hidden" value="${product.unitId}" />
												<input class="tab_input" readonly="readonly" type="text" value="${fns:basicInfo('UNIT',product.unitId).name}" />
											</td>
											<td>
												<input name="productList.sourceQty" class="constraint_negative tab_input" type="text" readonly="readonly" value="${product.sourceQty}" />
											</td>
											<td>
												<input name="productList.saleProduceQty" class="constraint_negative tab_input bg_color" type="text" value="${product.saleProduceQty}" />
											</td>
											<td>
												<input name="productList.spareProduceQty" class="constraint_negative tab_input bg_color" type="text" value="${product.spareProduceQty}" />
											</td>
											<td>
												<input name="productList.produceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="${product.produceQty}" />
											</td>
											<td>
												<input name="productList.sourceBillNo" class="tab_input" onmouseover="this.title=this.value" readonly="readonly" type="text" value="${product.sourceBillNo}" />
												<input name="productList.sourceId" type="hidden" value="${product.sourceId}" />
												<input name="productList.sourceDetailId" type="hidden" value="${product.sourceDetailId}" />
												<input name="productList.sourceBillType" type="hidden" value="${product.sourceBillType}" />
											</td>
											<%-- <shiro:hasPermission name="produce:work:money"> --%>
											<td>
												<input name="productList.price" class="tab_input bg_color  constraint_decimal_negative" type="text" value="${product.price}" />
											</td>
											<td>
												<input name="productList.money" class="tab_input bg_color  constraint_decimal_negative" type="text" value="${product.money}" />
											</td>
											<%--  </shiro:hasPermission> --%>
											<td>
												<input name="productList.customerBillNo" class="tab_input" readonly="readonly" type="text" value="${product.customerBillNo}" />
											</td>

											<td>
												<input name="productList.customerMaterialCode" class="tab_input" readonly="readonly" type="text" value="${product.customerMaterialCode}" />
											</td>
											<td>
												<input name="productList.deliveryTime" class="tab_input bg_color" type="text" onfocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" value="<fmt:formatDate value='${product.deliveryTime}' type='date'/>" />
											</td>
											<td>
												<input name="productList.customerRequire" class="tab_input bg_color" type="text" value="${product.customerRequire}" onmouseover="this.title=this.value" />
											</td>
											<td>
												<input name="productList.memo" class="tab_input bg_color memo" type="text" value="${product.memo}" onmouseover="this.title=this.value" />
											</td>
											<td>
												<c:if test="${product.imgUrl !=''}">
													<img class="pimg" src="${product.imgUrl }" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<!----------------------产品信息部分End------------------------------------------>
					<!----------------------部件部分Start-------------------------------------------->
					<div class="fold_wrap" id="partList_div">
						<c:forEach items="${order.partList }" var="part">
							<div class="fold_table">
								<div class="for_sel">
									<table class="work_table resizable work_table_new">
										<thead>
											<tr>
												<th width="">操作</th>
												<th width="">部件名称</th>
												<th width="">部件数量</th>
												<th width="">倍率</th>

												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">P数</th>
												</c:if>
												<c:if test="${order.productType ne 'BOOK' }">
													<th width="">拼版数</th>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">单面P数</th>
												</c:if>
												<c:if test="${order.productType ne 'ROTARY' }">
													<th width="">上机规格</th>
												</c:if>
												<th width="">印刷方式</th>
												<th width="">正反普色</th>
												<th width="">正反专色</th>
												<c:if test="${order.productType ne 'ROTARY' }">
													<th width="">印张正数</th>
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<th width="">齿轮数</th>
													<th width="">齿间距</th>
													<th width="">走距(mm)</th>
													<th width="">标准用料(m)</th>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">贴数</th>
													<th width="">每贴正数</th>
												</c:if>
												<th width="">印版付数</th>
												<th width="">印版张数</th>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">单贴放损%</th>
												</c:if>
												<c:if test="${order.productType ne 'BOOK' }">
													<th width="">放损%</th>
												</c:if>

												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">单贴放损</th>
												</c:if>
												<th width="">放损数</th>
												<c:if test="${order.productType ne 'ROTARY' }">
													<th width="">总印张</th>
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<th width="">总用料(m)</th>
												</c:if>
												<th width="">机台名称</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<i title="删除" class="fa fa-trash-o" name="btn_part_del"></i>
													<i title="复制" class="fa fa-copy"></i>
												</td>
												<td class="pro_name fold_ico">
													<input name="partList.id" type="hidden" class="tab_input" value="${part.id}" />
													<input name="partList.partName" type="text" class="tab_input bg_color" value="${part.partName}" />
													<i class="btn_toggle fa fa-minus-square-o"> </i>
												</td>
												<td>
													<input name="partList.qty" type="text" class="constraint_negative tab_input bg_color" value="${part.qty}" />
												</td>
												<td>
													<input name="partList.multiple" type="text" class="constraint_negative tab_input bg_color" value="${part.multiple}" />
												</td>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>
														<input name="partList.pageNum" type="text" readonly="readonly" class="tab_input" value="${part.pageNum}" />
													</td>
												</c:if>
												<td>
													<input name="partList.pieceNum" readonly="readonly" type="text" class="constraint_negative tab_input" value="${part.pieceNum}" />
												</td>
												<td class="use_relative" <c:if test="${order.productType eq 'ROTARY' }">style="display:none;"</c:if>>
													<input name="partList.style" type="text" class="tab_input bg_color" value="${part.style}" />
													<div class="style_sel_item">
														<div class="sel_ico"></div>
														<div class="hy_hide td_box" style="width: 160px;">
															<c:if test="${order.productType eq 'PACKE' }">
																<div class="td_style_item">540*780</div>
																<div class="td_style_item">590*880</div>
																<div class="td_style_item">360*780</div>
																<div class="td_style_item">395*880</div>
																<div class="td_style_item">390*543</div>
																<div class="td_style_item">440*590</div>
																<div class="td_style_item">360*390</div>
																<div class="td_style_item">395*440</div>
																<div class="td_style_item">270*390</div>
																<div class="td_style_item">295*440</div>
															</c:if>
															<c:if test="${order.productType eq 'BOOK' }">
																<div class="td_style_item">540*740</div>
																<div class="td_style_item">570*840</div>
																<div class="td_style_item">370*540</div>
																<div class="td_style_item">420*570</div>
																<div class="td_style_item">360*370</div>
																<div class="td_style_item">285*420</div>
																<div class="td_style_item">185*260</div>
																<div class="td_style_item">210*285</div>
																<div class="td_style_item">184*130</div>
																<div class="td_style_item">140*210</div>
															</c:if>
														</div>
													</div>
												</td>
												<td class="use_relative">
													<phtml:list name="partList.printType" textProperty="text" cssClass="tab_input" selected="${part.printType }" type="com.huayin.printmanager.persist.enumerate.PrintType"></phtml:list>
												</td>
												<td class="use_relative">
													<input type="text" class="tab_input bg_color" name="partList.generalColor" value="${part.generalColor}" />

													<c:choose>
														<c:when test="${part.printType eq 'BLANK' }">
															<div class="color_sel_item"></div>
														</c:when>
														<c:when test="${part.printType eq 'SINGLE' }">
															<div class="color_sel_item">
																<div class="sel_ico"></div>
																<div class="hy_hide td_box" style="width: 160px;">
																	<div class="td_box_item">0+1</div>
																	<div class="td_box_item">0+2</div>
																	<div class="td_box_item">0+3</div>
																	<div class="td_box_item">0+4</div>
																	<div class="td_box_item">1+0</div>
																	<div class="td_box_item">2+0</div>
																	<div class="td_box_item">3+0</div>
																	<div class="td_box_item">4+0</div>
																</div>
															</div>
														</c:when>
														<c:otherwise>
															<div class="color_sel_item">
																<div class="sel_ico"></div>
																<div class="hy_hide td_box" style="width: 200px;">
																	<div class="td_box_item">0+0</div>
																	<div class="td_box_item">0+1</div>
																	<div class="td_box_item">0+2</div>
																	<div class="td_box_item">0+3</div>
																	<div class="td_box_item">0+4</div>
																	<div class="td_box_item">1+0</div>
																	<div class="td_box_item">1+1</div>
																	<div class="td_box_item">1+2</div>
																	<div class="td_box_item">1+3</div>
																	<div class="td_box_item">1+4</div>
																	<div class="td_box_item">2+0</div>
																	<div class="td_box_item">2+1</div>
																	<div class="td_box_item">2+2</div>
																	<div class="td_box_item">2+3</div>
																	<div class="td_box_item">2+4</div>
																	<div class="td_box_item">3+0</div>
																	<div class="td_box_item">3+1</div>
																	<div class="td_box_item">3+2</div>
																	<div class="td_box_item">3+3</div>
																	<div class="td_box_item">3+4</div>
																	<div class="td_box_item">4+0</div>
																	<div class="td_box_item">4+1</div>
																	<div class="td_box_item">4+2</div>
																	<div class="td_box_item">4+3</div>
																	<div class="td_box_item">4+4</div>
																</div>
															</div>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="use_relative">
													<input type="text" class="tab_input bg_color" name="partList.spotColor" value="${part.spotColor}" />
													<c:choose>
														<c:when test="${part.printType eq 'BLANK' }">
															<div class="color_sel_item"></div>
														</c:when>
														<c:when test="${part.printType eq 'SINGLE' }">
															<c:if test="${fn:substring(part.generalColor, 0, 1)=='0'}">
																<div class="color_sel_item">
																	<div class="sel_ico"></div>
																	<div class="hy_hide td_box" style="width: 160px;">
																		<div class="td_box_item">0+1</div>
																		<div class="td_box_item">0+2</div>
																		<div class="td_box_item">0+3</div>
																		<div class="td_box_item">0+4</div>
																		<div class="td_box_item_cancel">1+0</div>
																		<div class="td_box_item_cancel">2+0</div>
																		<div class="td_box_item_cancel">3+0</div>
																		<div class="td_box_item_cancel">4+0</div>
																	</div>
																</div>
															</c:if>
															<c:if test="${fn:substring(part.generalColor, 0, 1)!='0'}">
																<div class="color_sel_item">
																	<div class="sel_ico"></div>
																	<div class="hy_hide td_box" style="width: 160px;">
																		<div class="td_box_item_cancel">0+1</div>
																		<div class="td_box_item_cancel">0+2</div>
																		<div class="td_box_item_cancel">0+3</div>
																		<div class="td_box_item_cancel">0+4</div>
																		<div class="td_box_item">1+0</div>
																		<div class="td_box_item">2+0</div>
																		<div class="td_box_item">3+0</div>
																		<div class="td_box_item">4+0</div>
																	</div>
																</div>
															</c:if>
														</c:when>
														<c:otherwise>
															<div class="color_sel_item">
																<div class="sel_ico"></div>
																<div class="hy_hide td_box" style="width: 200px;">
																	<div class="td_box_item">0+0</div>
																	<div class="td_box_item">0+1</div>
																	<div class="td_box_item">0+2</div>
																	<div class="td_box_item">0+3</div>
																	<div class="td_box_item">0+4</div>
																	<div class="td_box_item">1+0</div>
																	<div class="td_box_item">1+1</div>
																	<div class="td_box_item">1+2</div>
																	<div class="td_box_item">1+3</div>
																	<div class="td_box_item">1+4</div>
																	<div class="td_box_item">2+0</div>
																	<div class="td_box_item">2+1</div>
																	<div class="td_box_item">2+2</div>
																	<div class="td_box_item">2+3</div>
																	<div class="td_box_item">2+4</div>
																	<div class="td_box_item">3+0</div>
																	<div class="td_box_item">3+1</div>
																	<div class="td_box_item">3+2</div>
																	<div class="td_box_item">3+3</div>
																	<div class="td_box_item">3+4</div>
																	<div class="td_box_item">4+0</div>
																	<div class="td_box_item">4+1</div>
																	<div class="td_box_item">4+2</div>
																	<div class="td_box_item">4+3</div>
																	<div class="td_box_item">4+4</div>
																</div>
															</div>
														</c:otherwise>
													</c:choose>
												</td>
												<c:if test="${order.productType ne 'ROTARY' }">
												<td>
													<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="${part.impressionNum}" />
												</td>
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<td>
														<input name="partList.gear" type="text" class="tab_input bg_color constraint_negative" value="${part.gear }" />
													</td>
													<td>
														<input name="partList.distance" type="text" class="tab_input bg_color constraint_decimal" value="${part.distance }" />
													</td>
													<td>
														<input name="partList.walkDistance" type="text" class="tab_input bg_color constraint_decimal" value="${part.walkDistance }" />
													</td>
													<td>
														<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="${part.materialNum}" />
													</td>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>
														<input name="partList.stickersNum" type="text" readonly="readonly" class="tab_input" value="${part.stickersNum}" />
													</td>
													<td>
														<input name="partList.stickersPostedNum" type="text" readonly="readonly" class="tab_input" value="${part.stickersPostedNum}" />
													</td>
												</c:if>
												<td>
													<input name="partList.plateSuitNum" type="text" readonly="readonly" class="tab_input" value="${part.plateSuitNum}" />
												</td>
												<td>
													<input name="partList.plateSheetNum" type="text" readonly="readonly" class="tab_input" value="${part.plateSheetNum}" />
												</td>
												<td>
													<input name="partList.lossRate" type="text" class="constraint_decimal tab_input bg_color" value="<fmt:formatNumber value="${part.lossRate }" type="currency" pattern="0.####"/>" />
												</td>

												<c:if test="${order.productType eq 'BOOK' }">
													<td>
														<input name="partList.stickerlossQty" type="text" class="tab_input bg_color constraint_negative" value="${part.stickerlossQty}" />
													</td>
												</c:if>
												<c:if test="${order.productType ne 'BOOK' }">
													<td>
														<input name="partList.lossQty" type="text" class="tab_input bg_color constraint_negative" value="${part.lossQty}" />
													</td>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>
														<input name="partList.lossQty" type="text" class="tab_input constraint_negative" readonly="readonly" value="${part.lossQty}" />
													</td>
												</c:if>

												<td>
													<c:if test="${order.productType eq 'ROTARY' }">
														<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="${part.totalMaterialNum}" />
													</c:if>
													<c:if test="${order.productType ne 'ROTARY' }">
														<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="${part.totalImpressionNum}" />
													</c:if>
												</td>

												<td style="position: relative">
													<input name="partList.machineName" id="" type="text" class="tab_input bg_color" value="${part.machineName}">
													<input name="partList.machineId" class="machineId" id="" type="hidden" value="${part.machineId}">
													<div class="select-btn" id="machineNameDiv" style="right: 0; height: 100%; line-height: inherit; right: 0;">...</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="cl whole">
									<div class="l plywood_product w_left">
										<table class="makeup_table" rules="all">
											<thead>
												<tr>
													<th width="40">操作</th>
													<th>成品名称</th>
													<th>生产数量</th>
													<c:if test="${order.productType eq 'BOOK' }">
														<th>P数</th>
													</c:if>
													<c:if test="${order.productType ne 'BOOK' }">
														<th width="50">拼版数</th>
													</c:if>
													<c:if test="${order.productType eq 'BOOK' }">
														<th width="50">单面P数</th>
													</c:if>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${part.productList }" var="product">
													<tr>
														<td>
															<i title='删除' class='fa fa-trash-o'></i>
														</td>
														<td>
															<input name='productList.id' type='hidden' value='${product.id }' />
															<input name='productList.productId' type='hidden' value='${product.productId }' />
															<input name='productList.productName' class='tab_input' onmouseover='this.title=this.value' type='text' value='${product.productName }' readonly="readonly" />
														</td>
														<td>
															<input name='productList.produceQty' class='tab_input' readonly="readonly" type='text' value='${product.produceQty }' />
														</td>
														<c:if test="${order.productType eq 'BOOK' }">
															<td>
																<input name='productList.pageNum' class='constraint_negative tab_input bg_color' type='text' value='${product.pageNum }' />
															</td>
														</c:if>
														<td>
															<input name='productList.pieceNum' class='constraint_negative tab_input bg_color' type='text' value='${product.pieceNum }' />
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
									<div class="r w_right">
										<!--Start-->
										<div class="inside_container">
											<div class="cl inside_box material unshadeMaterial">
												<div class="l left_path">
													<a href="javascript:;" title="添加材料" class="nav_btn table_nav_btn newadd_item_btn">
														<i class="fa fa-plus-square"></i>
													</a>
													<label class="box_title">材 料：</label>
												</div>
												<div class="l right_path">
													<c:forEach var="material" items="${part.materialList}">
														<div class="cl material-class">
															<div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料名称：</label>
																	<div class="inside_combo">
																		<input type="hidden" name="id" value="${material.id }" />
																		<input type="hidden" name="materialId" value="${material.materialId  }" />
																		<input type="hidden" name="materialCode" value="${material.materialCode  }" />
																		<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" name="materialName" value="${material.materialName }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料规格：</label>
																	<div class="inside_combo use_relative">
																		<input type="text" class="input-txt input-txt_8" name="materialStyle" value="${material.style }" />
																		<div class="style_sel_item">
																			<div class="sel_ico"></div>
																			<div class="hy_hide td_box" style="width: 160px; left: initial; right: -20px; top: -200px;">
																				<div class="td_style_item">1092*1092</div>
																				<div class="td_style_item">889*1194</div>
																				<div class="td_style_item">880*1168</div>
																				<div class="td_style_item">857*1120</div>
																				<div class="td_style_item">850*1168</div>
																				<div class="td_style_item">834*1172</div>
																				<div class="td_style_item">787*1190</div>
																				<div class="td_style_item">787*1092</div>
																				<div class="td_style_item">787*970</div>
																				<div class="td_style_item">787*787</div>
																				<div class="td_style_item">690*960</div>
																				<div class="td_style_item">686*864</div>
																				<div class="td_style_item">648*953</div>
																				<div class="td_style_item">635*1118</div>
																				<div class="td_style_item">596*834</div>
																				<div class="td_style_item">560*870</div>
																				<div class="td_style_item">559*864</div>
																				<div class="td_style_item">427*569</div>
																			</div>
																		</div>
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui">克 重：</label>
																	<div class="inside_combo">
																		<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialWeight" value="${material.weight }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui">单 位：</label>
																	<div class="inside_combo">
																		<input type="hidden" name="materialValuationUnitId" value="${material.valuationUnitId }" />
																		<input type="hidden" name="materialStockUnitId" value="${material.stockUnitId }" />
																		<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" value="${fns:basicInfo('UNIT',material.stockUnitId).name }" />
																	</div>
																</div>
																<c:if test="${order.productType ne 'ROTARY' }">
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料开数：</label>
																	<div class="inside_combo">
																		<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="${material.splitQty }" />
																	</div>
																</div>
																</c:if>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料用量：</label>
																	<div class="inside_combo">
																		<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" value="${material.qty }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">来纸：</label>
																	<div class="inside_combo">
																		<input type="checkBox" class="input-txt" name="isCustPaper" <c:if test="${material.isCustPaper=='YES' }">checked="checked"</c:if> />
																	</div>
																</div>
															</div>
															<div class="inside_item btn_item">
																<a href="javascript:;" class="nav_btn table_nav_btn remove_item_btn">
																	<i class="fa fa-minus-square"></i>
																	移除
																</a>
															</div>
														</div>
													</c:forEach>
												</div>
											</div>
											<div class="cl inside_box item_width procedure unshadeProcedure">
												<div class="l left_path">
													<label class="box_title">工 序：</label>
												</div>
												<div class="l right_path">
													<div class="cl procedure-class">
														<!--印前/印刷工序Start-->
														<div class="cl">
															<!--印前工序Start-->
															<div class="procedure_before">
																<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'BEFORE',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																	<div class="cl inside_item mar_r">
																		<div class="classify_item">
																			<div class="classify_item_title">
																				<a href="javascript:void(0)">${procedureClass_item.name }</a>
																			</div>
																			<div class="hy_hide classify_content">
																				<ul class="classify_content_list">
																					<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																						<li>
																							<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																							<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																							<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																							<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																							<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																							<span _produceType="${procedure_item.produceType }"> ${procedure_item.name } </span>
																							<i title="添加" class="fa fa-plus"></i>
																						</li>
																					</c:forEach>
																				</ul>
																			</div>
																		</div>
																	</div>
																</c:forEach>
															</div>
															<!--印前工序End-->
															<!--印刷工序Start-->

															<div class="procedure_print">
																<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'PRINT',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																	<div class="cl inside_item mar_r">
																		<div class="classify_item">
																			<div class="classify_item_title">
																				<a href="javascript:void(0)">${procedureClass_item.name }</a>
																			</div>
																			<div class="hy_hide classify_content">
																				<ul class="classify_content_list">
																					<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																						<li>
																							<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																							<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																							<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																							<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																							<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																							<span _produceType="${procedure_item.produceType }">${procedure_item.name }</span>
																							<i title="添加" class="fa fa-plus"></i>
																						</li>
																					</c:forEach>
																				</ul>
																			</div>
																		</div>
																	</div>
																</c:forEach>
															</div>
															<!--印刷工序End-->
														</div>
														<!--印前/印刷工序End-->
														<!--印后工序Start-->
														<div class="cl procedure_after" style="">
															<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'AFTER',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																<div class="cl inside_item mar_r">
																	<div class="classify_item">
																		<div class="classify_item_title">
																			<a href="javascript:void(0)">${procedureClass_item.name }</a>
																		</div>
																		<div class="hy_hide classify_content">
																			<ul class="classify_content_list">
																				<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																					<li>
																						<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																						<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																						<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																						<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																						<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																						<span _produceType="${procedure_item.produceType }">${procedure_item.name }</span>
																						<i title="添加" class="fa fa-plus"></i>
																					</li>
																				</c:forEach>
																			</ul>
																		</div>
																	</div>
																</div>
															</c:forEach>
														</div>
														<!--印后工序End-->
													</div>
												</div>
											</div>
											<div class="cl inside_box procedure-line unshadeProcedure" style="z-index: 998; padding-top: 25px;">
												<div class="l left_path pri_item">
													<label class="box_title">工艺线路：</label>
												</div>
												<div class="l right_path">
													<div class="cl procedure-line-class procedure-memo-only">
														<c:forEach items="${part.procedureList}" var="procedure" varStatus="sta">
															<div class='cl pri_item mar_r'>
																<c:if test="${!sta.first}">
																	<i class='arrow_r fa fa-long-arrow-right'></i>
																</c:if>
																<span class='show_fw'>
																	<label class='hy_hide radio_item'>
																		<input type='checkbox' title='发外' <c:if test="${procedure.isOutSource eq 'YES' }">checked</c:if> />
																		发外
																	</label>
																	<span class='removeRequierBt'>X</span>
																	<label class='hy_hide radio_item2'>要求</label>
																	<input name="procedureRefId" value="${procedure.id}" type="hidden" />
																	<input type='hidden' name='procedure_id' value='${procedure.procedureId }' />
																	<input type='hidden' name='procedure_code' value='${procedure.procedureCode }' />
																	<input type='hidden' name='procedure_name' value='${procedure.procedureName }' />
																	<input type='hidden' name='procedure_procedureType' value='${procedure.procedureType }' />
																	<input type='hidden' name='procedure_procedureClassId' value='${procedure.procedureClassId}' />
																	<input type='hidden' name='procedure_isOutSource' value='${procedure.isOutSource }' />
																	<span class='ct' data-memo="${procedure.memo }">${procedure.procedureName }</span>
																	<c:if test="${procedure.isOutSource eq 'YES' }">
																		<span class='outsource'>(发外)</span>
																	</c:if>
																</span>
																<i title='删除' class='del_ico fa fa-close'></i>
															</div>
														</c:forEach>
													</div>
												</div>
											</div>
											<div class="cl inside_box  technics-memo unshadeProcedure" style="z-index: 998;">
												<div class="l left_path">
													<label class="box_title">工艺备注：</label>
												</div>
												<div class="l right_path" style="line-height: 1.5; min-height: 22px">
													<div class="l fold_textarea requireText">${part.memo }</div>
												</div>
											</div>
										</div>
										<!--END-->
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
					<!----------------------部件部分End----------------------------------------------->

					<!----------------------成品工序Start--------------------------------------------->
					<div class="fold_wrap" id="pack_div">
						<input type="hidden" id="pack_id" value="${order.pack.id }" />
						<div class="fold_table">
							<div class="cl inside_container">
								<div class="left_path procedure_finished" style="border-right: none">成品工序</div>
								<div class="right_path bd_left1" style="border-left: 1px solid #ccc;">
									<div class="cl inside_box unshadeMaterial material" style="margin-bottom: 20px;">
										<div class="l left_path">
											<a href="javascript:;" title="添加材料" class="nav_btn table_nav_btn newadd_item_btn">
												<i class="fa fa-plus-square"></i>
											</a>
											<label class="box_title">材 料：</label>
										</div>
										<div class="l right_path">
											<c:forEach var="material" items="${order.pack.materialList}">
												<div class="cl material-class">
													<div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料名称：</label>
															<div class="inside_combo">
																<input type="hidden" name="id" value="${material.id}" />
																<input type="hidden" name="materialId" value="${material.materialId}" />
																<input type="hidden" name="materialCode" value="${material.materialCode}" />
																<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" name="materialName" value="${material.materialName }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料规格：</label>
															<div class="inside_combo use_relative">
																<input type="text" class="input-txt input-txt_8" name="materialStyle" value="${material.style }" />
																<div class="style_sel_item">
																	<div class="sel_ico"></div>
																	<div class="hy_hide td_box" style="width: 160px; top: -200px; left: initial; right: -20px;">
																		<div class="td_style_item">1092*1092</div>
																		<div class="td_style_item">889*1194</div>
																		<div class="td_style_item">880*1168</div>
																		<div class="td_style_item">857*1120</div>
																		<div class="td_style_item">850*1168</div>
																		<div class="td_style_item">834*1172</div>
																		<div class="td_style_item">787*1190</div>
																		<div class="td_style_item">787*1092</div>
																		<div class="td_style_item">787*970</div>
																		<div class="td_style_item">787*787</div>
																		<div class="td_style_item">690*960</div>
																		<div class="td_style_item">686*864</div>
																		<div class="td_style_item">648*953</div>
																		<div class="td_style_item">635*1118</div>
																		<div class="td_style_item">596*834</div>
																		<div class="td_style_item">560*870</div>
																		<div class="td_style_item">559*864</div>
																		<div class="td_style_item">427*569</div>
																	</div>
																</div>
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui">克 重：</label>
															<div class="inside_combo">
																<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialWeight" value="${material.weight }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui">单 位：</label>
															<div class="inside_combo">
																<input type="hidden" name="materialValuationUnitId" value="${material.valuationUnitId }" />
																<input type="hidden" name="materialStockUnitId" value="${material.stockUnitId }" />
																<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" value="${fns:basicInfo('UNIT',material.stockUnitId).name }" />
															</div>
														</div>
														<c:if test="${order.productType ne 'ROTARY' }">
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料开数：</label>
															<div class="inside_combo">
																<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="${material.splitQty }" />
															</div>
														</div>
														</c:if>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料用量：</label>
															<div class="inside_combo">
																<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" value="${material.qty }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">来纸：</label>
															<div class="inside_combo">
																<input type="checkBox" class="input-txt" name="isCustPaper" <c:if test="${material.isCustPaper=='YES' }">checked="checked"</c:if> />
															</div>
														</div>
													</div>
													<div class="inside_item btn_item">
														<a href="javascript:;" class="nav_btn table_nav_btn remove_item_btn">
															<i class="fa fa-minus-square"></i>
															移除
														</a>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
									<div class="cl inside_box item_width unshadeProcedure">
										<div class="l left_path">
											<label class="box_title">工 序：</label>
										</div>
										<div class="l right_path">
											<div class="cl procedure_finished_pro procedure-memo-only">
												<c:forEach items="${fns:basicListParams('PROCEDURE','procedureType','productType', 'FINISHED',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
													<div class="cl inside_item mar_r" style="width: auto;">
														<c:set var="checked" value="" />
														<c:set var="checkedOut" value="" />
														<c:set var="memo" value=""></c:set>
														<c:forEach items="${order.pack.procedureList}" var="procedure">
															<c:if test="${procedure_item.id eq procedure.procedureId }">
																<c:set var="checked" value="checked" />
																<c:set var="checkedOut" value="${procedure.isOutSource}" />
																<c:set var="memo" value="${procedure.memo }"></c:set>
																<c:set var="packProcedureRefId" value="${procedure.id }"></c:set>
															</c:if>
														</c:forEach>
														<span class="form-label label_ui radio_item">
															<label class="hy_hide isOutsource">
																<input class="" type="checkbox" <c:if test="${checkedOut=='YES' }">checked</c:if> title="发外">
																发外
															</label>
															<span class='removeRequierBt'>X</span>
															<label class='hy_hide radio_item2'>要求</label>
															<c:if test="${checkedOut=='YES' }">
																<input name="procedureRefId" value="${packProcedureRefId}" type="hidden" />
															</c:if>
															<input type="hidden" name="procedure_id" value="${procedure_item.id}" />
															<input type="hidden" name="procedure_code" value="${procedure_item.code}" />
															<input type="hidden" name="procedure_name" value="${procedure_item.name}" />
															<input type="hidden" name="procedure_procedureType" value="${procedure_item.procedureType}" />
															<input type="hidden" name="procedure_procedureClassId" value="${procedure_item.procedureClassId}" />
															<input type="hidden" name="procedure_isOutSource" value="<c:if test="${empty checkedOut}"><c:if test="${procedure_item.produceType=='INSIDE'}">NO</c:if><c:if test="${procedure_item.produceType=='EXTERNAL'}">YES</c:if></c:if><c:if test="${!empty checkedOut}">${checkedOut}</c:if>" />
															<input type="checkbox" name="check_procedure" ${checked } class="check_procedure" value="${procedure_item.id }" />
															<span class="procedure_item_name ct" data-memo="${memo }">${procedure_item.name }</span>
															<c:if test="${checkedOut=='YES' }">
																<span class='outsource'>(发外)</span>
															</c:if>
														</span>
														<i class="arrow_r fa fa-long-arrow-right" style="line-height: 24px; color: #68BCF9; cursor: default !important"></i>
													</div>
												</c:forEach>
											</div>
										</div>
									</div>
									<div class="cl inside_box item_width unshadeProcedure">
										<div class="l left_path">
											<label class="box_title">备 注：</label>
										</div>
										<div class="l right_path">
											<div class="remakes_path">
												<div class="l remakes_content" style="line-height: 1.5; min-height: 22px">
													<div class="l fold_textarea requireText" id="pack_memo">${order.pack.memo }</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!--成品工序End-->
				</div>
			</div>
		</div>
	</div>


	<div id="part_template" class="hy_hide">
		<div class="fold_table">
			<div class="for_sel">
				<table class="work_table">
					<thead>
						<tr>
							<th width="">操作</th>
							<th width="">部件名称</th>
							<th width="">部件数量</th>
							<th width="">倍率</th>
							<th width="">拼版数</th>
							<c:if test="${order.productType ne 'ROTARY' }">
								<th width="">上机规格</th>
							</c:if>
							<th width="">印刷方式</th>
							<th width="">正反普色</th>
							<th width="">正反专色</th>
							<c:if test="${order.productType ne 'ROTARY' }">
								<th width="">印张正数</th>
							</c:if>
							<c:if test="${order.productType eq 'ROTARY' }">
								<th width="">齿轮数</th>
								<th width="">齿间距</th>
								<th width="">走距(mm)</th>
								<th width="">标准用料(m)</th>
							</c:if>
							<th width="">印版付数</th>
							<th width="">印版张数</th>
							<th width="">放损%</th>
							<th width="">放损数</th>
							<c:if test="${order.productType ne 'ROTARY' }">
								<th width="">总印张</th>
							</c:if>
							<c:if test="${order.productType eq 'ROTARY' }">
								<th width="">总用料(m)</th>
							</c:if>
							<th width="">机台名称</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<i title="删除" class="fa fa-trash-o" name="btn_part_del"></i>
								<i title='复制' class='fa fa-copy'></i>
							</td>
							<td class="pro_name fold_ico">
								<input name="partList.partName" type="text" onmouseover="this.title=this.value" class="tab_input bg_color" />
								<i class="btn_toggle fa fa-minus-square-o"> </i>
							</td>
							<td>
								<input name="partList.qty" type="text" class="constraint_negative tab_input bg_color" value="0" />
							</td>
							<td>
								<input name="partList.multiple" type="text" class="constr	aint_negative tab_input bg_color" value="1" />
							</td>
							<td>
								<input name="partList.pieceNum" readonly="readonly" type="text" class="constraint_negative tab_input" value="1" />
							</td>
							<td class="use_relative" <c:if test="${order.productType eq 'ROTARY' }">style="display:none;"</c:if>>
								<input name="partList.style" type="text" class="tab_input bg_color" value="" />
								<div class="style_sel_item">
									<div class="sel_ico"></div>
									<div class="hy_hide td_box" style="width: 160px;">
										<c:if test="${order.productType eq 'PACKE' }">
											<div class="td_style_item">540*780</div>
											<div class="td_style_item">590*880</div>
											<div class="td_style_item">360*780</div>
											<div class="td_style_item">395*880</div>
											<div class="td_style_item">390*543</div>
											<div class="td_style_item">440*590</div>
											<div class="td_style_item">360*390</div>
											<div class="td_style_item">395*440</div>
											<div class="td_style_item">270*390</div>
											<div class="td_style_item">295*440</div>
										</c:if>
										<c:if test="${order.productType eq 'BOOK' }">
											<div class="td_style_item">540*740</div>
											<div class="td_style_item">570*840</div>
											<div class="td_style_item">370*540</div>
											<div class="td_style_item">420*570</div>
											<div class="td_style_item">360*370</div>
											<div class="td_style_item">285*420</div>
											<div class="td_style_item">185*260</div>
											<div class="td_style_item">210*285</div>
											<div class="td_style_item">184*130</div>
											<div class="td_style_item">140*210</div>
										</c:if>
									</div>
								</div>
							</td>
							<td class="use_relative">
								<phtml:list name="partList.printType" textProperty="text" cssClass="tab_input bg_color" type="com.huayin.printmanager.persist.enumerate.PrintType"></phtml:list>
							</td>
							<td class="use_relative">
								<input type="text" class="tab_input bg_color" name="partList.generalColor" value="1+0" />
								<div class="color_sel_item">
									<div class="sel_ico"></div>
									<div class="hy_hide td_box" style="width: 160px; height: 50px;">
										<div class="td_box_item">0+1</div>
										<div class="td_box_item">0+2</div>
										<div class="td_box_item">0+3</div>
										<div class="td_box_item">0+4</div>
										<div class="td_box_item">1+0</div>
										<div class="td_box_item">2+0</div>
										<div class="td_box_item">3+0</div>
										<div class="td_box_item">4+0</div>
									</div>
								</div>
							</td>
							<td class="use_relative">
								<input type="text" class="tab_input bg_color" name="partList.spotColor" value="0+0" />
								<div class="color_sel_item">
									<div class="sel_ico active"></div>
									<div class="hy_hide td_box" style="width: 160px; height: 50px;">
										<div class="td_box_item_cancel">0+1</div>
										<div class="td_box_item_cancel">0+2</div>
										<div class="td_box_item_cancel">0+3</div>
										<div class="td_box_item_cancel">0+4</div>
										<div class="td_box_item">1+0</div>
										<div class="td_box_item">2+0</div>
										<div class="td_box_item">3+0</div>
										<div class="td_box_item">4+0</div>
									</div>
								</div>
							</td>
							<c:if test="${order.productType ne 'ROTARY' }">
								<td>
									<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="0" />
								</td>
							</c:if>
							<c:if test="${order.productType eq 'ROTARY' }">
								<!-- 齿轮数 -->
								<td>
									<input name="partList.gear" type="text" class="tab_input bg_color constraint_negative" value="80" />
								</td>
								<!-- 齿间距 -->
								<td>
									<input name="partList.distance" type="text" class="tab_input bg_color constraint_decimal" value="3.175" />
								</td>
								<!-- 走距 -->
								<td>
									<input name="partList.walkDistance" type="text" class="tab_input bg_color constraint_decimal" value="254.00" />
								</td>
								<!-- 标准用料 -->
								<td>
									<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="" />
								</td>
							</c:if>
							<td>
								<input name="partList.plateSuitNum" type="text" readonly="readonly" class="tab_input" value="1" />
							</td>
							<td>
								<input name="partList.plateSheetNum" type="text" readonly="readonly" class="tab_input" value="1" />
							</td>
							<td>
								<input name="partList.lossRate" type="text" class="constraint_decimal tab_input bg_color" value="0" />
							</td>
							<td>
								<input name="partList.lossQty" type="text" class="tab_input constraint_negative bg_color" value="0" />
							</td>
							<td>
								<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="0" />
							</td>

							<td style="position: relative;">
								<input name="partList.machineName" id="" type="text" class="tab_input bg_color" value="">
								<input name="partList.machineId" class="machineId" id="" type="hidden" value="">
								<div class="select-btn" id="machineNameDiv" style="right: 0; height: 100%; line-height: inherit; right: 0">...</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="cl whole">
				<div class="l plywood_product w_left">
					<table class="makeup_table" rules="all">
						<thead>
							<tr>
								<th width="40">操作</th>
								<th>成品名称</th>
								<th>生产数量</th>
								<th width="50">拼版数</th>

							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
				<div class="r w_right">
					<!--Start-->
					<div class="inside_container">
						<div class="cl inside_box material">
							<div class="l left_path">
								<a href="javascript:;" title="添加材料" class="nav_btn table_nav_btn newadd_item_btn">
									<i class="fa fa-plus-square"></i>
								</a>
								<label class="box_title">材 料：</label>
							</div>
							<div class="l right_path"></div>
						</div>
						<div class="cl inside_box item_width procedure unshadeProcedure">
							<div class="l left_path">
								<label class="box_title">工 序：</label>
							</div>
							<div class="l right_path">
								<div class="cl procedure-class">
									<!--印前/印刷工序Start-->
									<div class="cl">
										<!--印前工序Start-->
										<div class="procedure_before">
											<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'BEFORE',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
												<div class="cl inside_item mar_r">
													<div class="classify_item">
														<div class="classify_item_title">
															<a href="javascript:void(0)">${procedureClass_item.name }</a>
														</div>
														<div class="hy_hide classify_content">
															<ul class="classify_content_list">
																<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																	<li>
																		<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																		<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																		<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																		<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																		<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																		<span _produceType="${procedure_item.produceType }"> ${procedure_item.name } </span>
																		<i title="添加" class="fa fa-plus"></i>
																	</li>
																</c:forEach>
															</ul>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
										<!--印前工序End-->
										<!--印刷工序Start-->

										<div class="procedure_print">
											<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'PRINT',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
												<div class="cl inside_item mar_r">
													<div class="classify_item">
														<div class="classify_item_title">
															<a href="javascript:void(0)">${procedureClass_item.name }</a>
														</div>
														<div class="hy_hide classify_content">
															<ul class="classify_content_list">
																<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																	<li>
																		<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																		<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																		<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																		<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																		<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																		<span _produceType="${procedure_item.produceType }">${procedure_item.name }</span>
																		<i title="添加" class="fa fa-plus"></i>
																	</li>
																</c:forEach>
															</ul>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
										<!--印刷工序End-->
									</div>
									<!--印前/印刷工序End-->
									<!--印后工序Start-->
									<div class="cl procedure_after" style="">
										<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'AFTER',order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
											<div class="cl inside_item mar_r">
												<div class="classify_item">
													<div class="classify_item_title">
														<a href="javascript:void(0)">${procedureClass_item.name }</a>
													</div>
													<div class="hy_hide classify_content">
														<ul class="classify_content_list">
															<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType' ,procedureClass_item.id,order.productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
																<li>
																	<input type="hidden" name="procedureList.procedureId" value="${procedure_item.id}" />
																	<input type="hidden" name="procedureList.procedureCode" value="${procedure_item.code}" />
																	<input type="hidden" name="procedureList.procedureName" value="${procedure_item.name}" />
																	<input type="hidden" name="procedureList.procedureType" value="${procedure_item.procedureType}" />
																	<input type="hidden" name="procedureList.procedureClassId" value="${procedure_item.procedureClassId}" />
																	<span _produceType="${procedure_item.produceType }">${procedure_item.name }</span>
																	<i title="添加" class="fa fa-plus"></i>
																</li>
															</c:forEach>
														</ul>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>
									<!--印后工序End-->
								</div>
							</div>
						</div>
						<div class="cl inside_box procedure-line unshadeProcedure" style="padding-top: 25px; z-index: 998">
							<div class="l left_path pri_item">
								<label class="box_title">工艺线路：</label>
							</div>
							<div class="l right_path">
								<div class="cl procedure-line-class procedure-memo-only"></div>
							</div>
						</div>
						<div class="cl inside_box  technics-memo" style="z-index: 998;">
							<div class="l left_path">
								<label class="box_title">工艺备注：</label>
							</div>
							<div class="l right_path" style="line-height: 1.5; min-height: 22px">
								<div class="l fold_textarea requireText">${part.memo }</div>
							</div>
						</div>
					</div>
					<!--END-->
				</div>
			</div>
		</div>
	</div>


	<div id="material_template" class="hy_hide">
		<div class="cl material-class">
			<div>
				<div class="inside_item">
					<label class="form-label label_ui label_1">材料名称：</label>
					<div class="inside_combo">
						<input type="hidden" name="materialId" />
						<input type="hidden" name="materialCode" />
						<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" name="materialName" />
					</div>
				</div>
				<div class="inside_item">
					<label class="form-label label_ui label_1">材料规格：</label>
					<div class="inside_combo use_relative">
						<input type="text" class="input-txt input-txt_8" name="materialStyle" />
						<div class="style_sel_item">
							<div class="sel_ico"></div>
							<div class="hy_hide td_box" style="width: 160px; top: -200px; left: initial; right: -20px;">
								<div class="td_style_item">1092*1092</div>
								<div class="td_style_item">889*1194</div>
								<div class="td_style_item">880*1168</div>
								<div class="td_style_item">857*1120</div>
								<div class="td_style_item">850*1168</div>
								<div class="td_style_item">834*1172</div>
								<div class="td_style_item">787*1190</div>
								<div class="td_style_item">787*1092</div>
								<div class="td_style_item">787*970</div>
								<div class="td_style_item">787*787</div>
								<div class="td_style_item">690*960</div>
								<div class="td_style_item">686*864</div>
								<div class="td_style_item">648*953</div>
								<div class="td_style_item">635*1118</div>
								<div class="td_style_item">596*834</div>
								<div class="td_style_item">560*870</div>
								<div class="td_style_item">559*864</div>
								<div class="td_style_item">427*569</div>
							</div>
						</div>
					</div>
				</div>
				<div class="inside_item">
					<label class="form-label label_ui">克 重：</label>
					<div class="inside_combo">
						<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialWeight" />
					</div>
				</div>
				<div class="inside_item">
					<label class="form-label label_ui">单 位：</label>
					<div class="inside_combo">
						<input type="hidden" name="materialValuationUnitId" value="${material.valuationUnitId }" />
						<input type="hidden" name="materialStockUnitId" />
						<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" />
					</div>
				</div>
				<c:if test="${order.productType ne 'ROTARY' }">
				<div class="inside_item">
					<label class="form-label label_ui label_1">材料开数：</label>
					<div class="inside_combo">
						<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="1" />
					</div>
				</div>
				</c:if>
				<div class="inside_item">
					<label class="form-label label_ui label_1">材料用量：</label>
					<div class="inside_combo">
						<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" />
					</div>
				</div>
				<div class="inside_item">
					<label class="form-label label_ui label_1">来纸：</label>
					<div class="inside_combo">
						<input type="checkBox" class="input-txt" name="isCustPaper" />
					</div>
				</div>
			</div>
			<div class="inside_item btn_item">
				<a href="javascript:;" class="nav_btn table_nav_btn remove_item_btn">
					<i class="fa fa-minus-square"></i>
					移除
				</a>
			</div>
		</div>
	</div>
	<!-- 批量悬浮窗 satrt -->
	<div id="batch_deliveryTime_box" class="batch_deliveryTime_box">
		<input type="text" class="input-txt input-txt_8 batch_deliveryTime_input" name="" value="" readonly="true" onFocus="WdatePicker({onpicked:batchEditDeliveryTime}) " />
	</div>
	<!-- 批量悬浮窗 end -->

</body>
</html>
