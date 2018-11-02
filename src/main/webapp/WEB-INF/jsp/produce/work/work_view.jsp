<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/produce/work/work_view.js?v=${v }"></script>
<title>生产工单信息</title>
</head>

<body>
	<div class="page-container">
		<div class="shadeHide" style="position: relative;">
			<div class="page-border work_view">
				<!--导航按钮-->
				<div class="cl">
					<div class="iframe-top">
						<sys:nav struct="生产管理-生产工单-查看"></sys:nav>
					</div>
					<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
						<div id="innerdiv" style="position: absolute;">
							<img id="bigimg" style="border: 5px solid #fff;" src="" />
						</div>
					</div>
					<div class="top_nav">
						<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
						<c:if test="${order.isForceComplete eq 'NO'}">

							<c:if test="${order.isCheck eq 'NO'}">
								<shiro:hasPermission name="produce:work:edit">
									<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="produce:work:audit">
									<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="produce:work:del">
									<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
								</shiro:hasPermission>
							</c:if>
							<!-- 如果已经审核 -->
							<c:if test="${order.isCheck eq 'YES'}">
								<shiro:hasPermission name="produce:work:audit_cancel">
									<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
								</shiro:hasPermission>

								<shiro:hasPermission name="produce:work:updateMateria">
									<div class="btn-group mp_changeDive">
										<button id="materialPrducChang" class="nav_btn table_nav_btn">
											工单变更
											<span class="caret"></span>
										</button>
										<div class="mp_change">
											<ul>
												<li data-state="change_material">材料变更</li>
												<li data-state="change_procedure">工序变更</li>
											</ul>
										</div>
									</div>
								</shiro:hasPermission>
							</c:if>

							<shiro:hasPermission name="produce:work:complete">
								<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${order.isForceComplete eq 'YES'}">
							<shiro:hasPermission name="produce:work:complete_cancel">
								<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
							</shiro:hasPermission>
						</c:if>
						<shiro:hasPermission name="produce:work:create">
							<button class="nav_btn table_nav_btn" id="btn_turning">翻单</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="produce:work:create">
							<button class="nav_btn table_nav_btn" id="btn_supplement">补单</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="produce:work:print">
							<div class="btn-group" id="btn_print">
								<button class="nav_btn table_nav_btn " type="button">
									模板打印
									<span class="caret"></span>
								</button>
								<div class="template_div"></div>
							</div>
						</shiro:hasPermission>
						<c:if test="${order.isForceComplete eq 'NO'}">
							<c:if test="${order.isCheck eq 'YES'}">
								<span class="isFlowCheckYES" id="generateTransmit">
									<div class="btn-group" id="btn_transmit">
										<button class="nav_btn table_nav_btn " type="button">
											生成
											<span class="caret"></span>
										</button>
										<div class="template_div">
											<ul class='dropdown-menu' role='menu'>
												<c:if test="${1 != loginUser.company.systemVersion }">
													<li>
														<a title='生成采购订单' id="transmitToPuch">生成采购订单</a>
													</li>
													<li>
														<a title='生成生产领料' id="transmitToTakeMaterial">生成生产领料</a>
													</li>
													<li>
														<a title='生成成品入库' id="transmitToProductIn">生成成品入库</a>
													</li>
												</c:if>
												<li>
													<a title='生成销售送货单' id="transmitToDeliver">生成销售送货</a>
												</li>

											</ul>
										</div>
									</div>
								</span>
							</c:if>
						</c:if>
						<input id="order_id" value="${order.id }" type="hidden" />
						<input id="order_billType" value="${order.billType }" type="hidden" />
						<input id="order_isOutSource" value="${order.isOutSource }" type="hidden">
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
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${order.productType.text }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">工单类型：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${order.billType.text }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">工单单号：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" id="billNo" class="input-txt input-txt_3s" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${order.createName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="<fmt:formatDate
										value="${order.createTime}" type="date" pattern="yyyy-MM-dd" /> " />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备 注：</label>
									<span class="form_textarea">
										<textarea class="noborder" style="width: 878px" readonly="readonly">${order.memo }</textarea>
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
							<label class="form-label label_ui label_3">
								<input type="checkbox" disabled="disabled" <c:if test="${order.isOutSource eq 'YES' }">checked</c:if> />
								整单发外
							</label>
							<label class="form-label label_ui label_1">
								<input type="checkbox" disabled="disabled" <c:if test="${order.isEmergency eq 'YES' }">checked</c:if> />
								急单
							</label>
						</span>
						<span class="work_checkbox">
							<label class="form-label label_ui label_3">
								翻单次数：
								<span> ${order.turningCount } </span>
							</label>
							<label class="form-label label_ui label_3">
								补单次数：
								<span> ${order.supplementCount } </span>
							</label>
						</span>
					</div>
					<!--产品信息部分Start-->
					<div class="fold_wrap" id="productList_div">
						<div class="fold_table ">
							<input type="hidden" id="product_sum_saleProduceQty" value="0" />
							<input type="hidden" id="product_sum_spareProduceQty" value="0" />
							<input type="hidden" id="product_sum_produceQty" value="0" />
							<table class="work_table resizable product_table" id="product_table">
								<thead>
									<tr>
										<th name="productName">成品名称</th>
										<th name="customerName">客户名称</th>
										<th name="style">产品规格</th>
										<th name="unit">单位</th>
										<th name="sourceQty">销售数量</th>
										<th name="saleProduceQty">生产数量</th>
										<th name="spareProduceQty">备品数量</th>
										<th name="produceQty">生产总量</th>
										<th name="sourceBillNo" width="80px">销售单号</th>
										<shiro:hasPermission name="produce:work:money">
											<c:if test="${not empty order.productList[0].price}">
												<th name="price">单价</th>
												<th name="money">金额</th>
											</c:if>
										</shiro:hasPermission>
										<th name="customerBillNo">客户单号</th>
										<th name="customerMaterialCode">客户料号</th>
										<th name="deliveryTime">交货日期</th>
										<th name="customerRequire">客户要求</th>
										<th name="memo">备注</th>
										<th name="imgUrl">产品图片</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="product" items="${order.productList}">
										<tr>
											<td onmouseover="this.title=this.innerHTML">${product.productName }</td>
											<td>
												<input onmouseover="this.title=this.value" type="text" class="tab_input" name="customerName" readonly="readonly" value="${product.customerName }" />
												<input type="hidden" name="customerId" readonly="readonly" value="${product.customerId }"
											</td>
											<td>${product.style }</td>
											<td>${fns:basicInfo("UNIT",product.unitId).name }</td>
											<td>${product.sourceQty }</td>
											<td>
												<input type="text" class="tab_input" name="saleProduceQty" readonly="readonly" value="${product.saleProduceQty }" />
											</td>
											<td>${product.spareProduceQty }</td>
											<td>
												<input type="text" class="tab_input" name="produceQty" readonly="readonly" value="${product.produceQty }" />
											</td>
											<td>
												<c:if test="${not empty product.sourceBillNo }">
													<a class="jump-to" onclick="jumpTo('${pageContext.request.contextPath}/print/sale/order/view/${product.sourceBillNo}','销售订单')">${product.sourceBillNo }</a>
												</c:if>
												<input type="hidden" name="sourceBillNo" value="${product.sourceBillNo }">
												<input type="hidden" name="productId" value="${product.id }" />
												<input type="hidden" name="deliverQty" value="${product.deliverQty }" />
												<input type="hidden" name="inStockQty" value="${product.inStockQty }" />
											</td>
											<shiro:hasPermission name="produce:work:money">
												<c:if test="${not empty product.price}">
													<td>${product.price }</td>
													<td>${product.money }</td>
												</c:if>
											</shiro:hasPermission>
											<td>${product.customerBillNo }</td>
											<td>${product.customerMaterialCode }</td>
											<td>
												<fmt:formatDate value="${product.deliveryTime}" type="date" pattern="yyyy-MM-dd" />
											</td>
											<td>${product.customerRequire }</td>
											<td onmouseover="this.title=this.innerHTML" class="memoView">${product.memo }</td>
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
					<!--产品信息部分End-->
					<!--部件部分Start-->
					<div class="fold_wrap" id="partList_div">
						<c:forEach items="${order.partList }" var="part">
							<div class="fold_table">
								<div class="for_sel">
									<table class="work_table resizable">
										<thead>
											<tr>
												<th width="">部件名称</th>
												<th width="">部件数量</th>
												<th width="">倍率</th>
												<c:if test="${order.productType ne 'BOOK' }">
													<th width="">拼版数</th>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">单面P数</th>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">P数</th>
												</c:if>
												<c:if test="${order.productType ne 'ROTARY' }">
													<th width="">上机规格</th>
												</c:if>
												<th width="">
													印刷方式
													</lth>
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
												<td class="pro_name fold_ico">${part.partName }<i class="btn_toggle fa fa-minus-square-o"> </i>
												</td>
												<td>${part.qty }</td>
												<td>${part.multiple }</td>
												<td>${part.pieceNum }</td>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>${part.pageNum }</td>
												</c:if>
												<c:if test="${order.productType ne 'ROTARY' }">
													<td>${part.style }</td>
												</c:if>
												<td>${part.printType.text }</td>
												<td>${part.generalColor }</td>
												<td>${part.spotColor }</td>
												<c:if test="${order.productType ne 'ROTARY' }">
													<td>${part.impressionNum }</td>
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<td>${part.gear }</td>
													<td>${part.distance }</td>
													<td>${part.walkDistance }</td>
													<td>${part.materialNum }</td>
												</c:if>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>${part.stickersNum }</td>
													<td>${part.stickersPostedNum }</td>
												</c:if>
												<td>${part.plateSuitNum }</td>
												<td>${part.plateSheetNum }</td>
												<td>
													<fmt:formatNumber value="${part.lossRate }" type="currency" pattern="0.####" />
												</td>
												<c:if test="${order.productType eq 'BOOK' }">
													<td>${part.stickerlossQty }</td>
												</c:if>
												<td>${part.lossQty }</td>
												<c:if test="${order.productType ne 'ROTARY' }">
													<td>${part.totalImpressionNum }</td>
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<td>${part.totalMaterialNum }</td>
												</c:if>
												<td>${part.machineName }</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="cl whole">
									<div class="l plywood_product w_left">
										<table class="makeup_table" rules="all">
											<thead>
												<tr>
													<th onmouseover="this.title=this.innerHTML">成品名称</th>
													<th>生产数量</th>
													<c:if test="${order.productType eq 'BOOK' }">
														<th>P数</th>
													</c:if>
													<c:if test="${order.productType ne 'BOOK' }">
														<th>拼版数</th>
													</c:if>
													<c:if test="${order.productType eq 'BOOK' }">
														<th>单面P数</th>
													</c:if>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${part.productList }" var="product">
													<tr>
														<td onmouseover="this.title=this.innerHTML">${product.productName }</td>
														<td>${product.produceQty }</td>
														<c:if test="${order.productType eq 'BOOK' }">
															<td>${product.pageNum }</td>
														</c:if>
														<td>${product.pieceNum }</td>
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
													<label class="box_title">材料：</label>
												</div>
												<div class="l right_path">
													<c:forEach var="material" items="${part.materialList}">
														<div class="cl material-class">
															<div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料名称：</label>
																	<div class="inside_combo">
																		<input type="hidden" name="materialId" value="${material.materialId }" />
																		<input type="hidden" name="materId" value="${material.id }" />
																		<input type="hidden" name="materialCode" />
																		<input type="hidden" name="materialTakeQty" value="${material.takeQty }" />
																		<input type="hidden" name="materialPurchQty" value="${material.purchQty }" />
																		<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" value="${material.materialName }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料规格：</label>
																	<div class="inside_combo">
																		<input type="text" class="input-txt input-txt_8" readonly="readonly" value="${material.style }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui">克 重：</label>
																	<div class="inside_combo">
																		<input type="text" class="input-txt input-txt_14" readonly="readonly" value="${material.weight }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui">单 位：</label>
																	<div class="inside_combo">
																		<input type="hidden" name="stockUnitId" />
																		<input type="text" class="input-txt input-txt_14" readonly="readonly" value="${fns:basicInfo('UNIT',material.stockUnitId).name }" />
																	</div>
																</div>
																<c:if test="${order.productType ne 'ROTARY' }">
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料开数：</label>
																	<div class="inside_combo">
																		<input type="text" class="constraint_negative input-txt input-txt_14" readonly="readonly" value="${material.splitQty }" />
																	</div>
																</div>
																</c:if>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料用量：</label>
																	<div class="inside_combo">
																		<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="material.qty" readonly="readonly" value="${material.qty }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">来纸：</label>
																	<div class="inside_combo">
																		<input type="checkBox" class="input-txt" disabled="disabled" name="isCustPaper" <c:if test="${material.isCustPaper=='YES' }">checked="checked"</c:if> />
																	</div>
																</div>
															</div>
														</div>

													</c:forEach>
												</div>
											</div>
											<div class="cl inside_box procedure-line unshadeProcedure" style="padding-top: 25px; z-index: 99998">
												<div class="l left_path pri_part">
													<label class="box_title">工艺线路：</label>
												</div>
												<div class="l right_path">
													<div class="cl">

														<c:forEach items="${part.procedureList }" var="procedure" varStatus="sta">
															<div class='cl pri_item mar_r'>
																<c:if test="${!sta.first}">
																	<i class='arrow_r fa fa-long-arrow-right'></i>
																</c:if>
																<span class='show_fw'>
																	<span class='ct'>${procedure.procedureName }</span>
																	<c:if test="${procedure.isOutSource eq 'YES' }">
																		<span class='outsource'>(发外)</span>
																	</c:if>
																</span>
															</div>
														</c:forEach>
													</div>
												</div>
											</div>
											<div class="cl inside_box  technics-memo" style="z-index: 998;">
												<div class="l left_path">
													<label class="box_title">工艺备注：</label>
												</div>
												<div class="l right_path" style="min-height: 22px; line-height: 1.5;">
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



					<!--部件部分End-->
					<!--成品工序Start-->
					<div class="fold_wrap" id="pack_div">
						<div class="fold_table">
							<div class="cl inside_container" style="margin: -1px 0 0 0">
								<div class="left_path procedure_finished" style="border-right: none">成品工序</div>
								<div class="right_path bd_left1 procedure_finished_pro">
									<div class="cl inside_box unshadeMaterial ">
										<div class="l left_path">
											<label class="box_title">材 料：</label>
										</div>
										<div class="l right_path">
											<c:forEach var="material" items="${order.pack.materialList}">
												<div class="cl material-class">
													<div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料名称：</label>
															<div class="inside_combo">
																<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" value="${material.materialName }" />
																<input type="hidden" name="materialId" value="${material.materialId }" />
																<input type="hidden" name="materId" value="${material.id }" />
																<input type="hidden" name="materialCode" />
																<input type="hidden" name="materialTakeQty" value="${material.takeQty }" />
																<input type="hidden" name="materialPurchQty" value="${material.purchQty }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料规格：</label>
															<div class="inside_combo">
																<input type="text" class="input-txt input-txt_8" readonly="readonly" value="${material.style }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui">克 重：</label>
															<div class="inside_combo">
																<input type="text" class="input-txt input-txt_14" readonly="readonly" value="${material.weight }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui">单 位：</label>
															<div class="inside_combo">
																<input type="text" class="input-txt input-txt_14" readonly="readonly" value="${fns:basicInfo('UNIT',material.stockUnitId).name }" />
															</div>
														</div>
														<c:if test="${order.productType ne 'ROTARY' }">
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料开数：</label>
															<div class="inside_combo">
																<input type="text" class="constraint_negative input-txt input-txt_14" readonly="readonly" value="${material.splitQty }" />
															</div>
														</div>
														</c:if>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料用量：</label>
															<div class="inside_combo">
																<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="material.qty" readonly="readonly" value="${material.qty }" />
															</div>
														</div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">来纸：</label>
															<div class="inside_combo">
																<input type="checkBox" class="input-txt" disabled="disabled" name="isCustPaper" <c:if test="${material.isCustPaper=='YES' }">checked="checked"</c:if> />
															</div>
														</div>
													</div>
												</div>

											</c:forEach>

										</div>
									</div>
									<div class="cl inside_box item_width unshadeProcedure" style="padding-top: 25px; z-index: 99998">
										<div class="l left_path">
											<label class="box_title">工 序：</label>
										</div>
										<div class="l right_path">
											<div class="cl cp_procedure">
												<c:forEach items="${order.pack.procedureList}" var="procedure">
													<span>${procedure.procedureName }<c:if test="${procedure.isOutSource=='YES' }">
															<span class='outsource'>(发外)</span>
														</c:if>
														<i class="arrow_r fa fa-long-arrow-right" style="margin-left: 10px; line-height: 24px; color: #68BCF9; cursor: default !important"></i>
													</span>
												</c:forEach>
											</div>
										</div>
									</div>
									<div class="cl inside_box item_width" style="z-index: 998;">
										<div class="l left_path">
											<label class="box_title">备 注：</label>
										</div>
										<div class="l right_path">
											<div class="remakes_path">
												<div class="l remakes_content" style="min-height: 22px; line-height: 1.5;">

													<div class="l fold_textarea requireText">${order.pack.memo }</div>
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
				<!--审核标签-->
				<c:if test="${order.isCheck eq 'YES'}">
					<div class="review">
						<span class="review_font">已审核</span>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>