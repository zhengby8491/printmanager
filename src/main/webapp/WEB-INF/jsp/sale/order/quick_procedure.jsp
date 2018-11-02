<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/common/work/workCommon.js?v=${v }"></script>
<title>销售工序材料详情</title>
<style type="text/css">
.tab_input {
	height: 30px
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/quick_procedure.js?v=${v}"></script>
</head>

<body>
	<input type="hidden" value="${productArray}" id="productArray">
	<input type="hidden" value="${param.productArray}" id="param">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top"></div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
					<input type="hidden" id="sourceWorkId" value="${sourceWorkId }">
					<input type="hidden" id="isCheck">
				</div>
			</div>
			<!--表单部分上-->
			<div class="form-container"></div>
			<!--表格部分-->
			<div class="cl mr_t">
				<div class="btn-billbar mb15">
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" id="add_part">
							<i class="fa fa-plus-square"></i>
							添加部件/工序
						</a>
					</span>
					<!-- 					<span class="work_checkbox"> -->
					<!-- 						<label> -->
					<%-- 							<input type="checkbox" id="isOutSource" <c:if test="${order.isOutSource eq 'YES' }">checked</c:if> /> --%>
					<!-- 							整单发外 -->
					<!-- 						</label> -->
					<!-- 						<label> -->
					<%-- 							<input type="checkbox" id="isEmergency" <c:if test="${order.isEmergency eq 'YES' }">checked</c:if> /> --%>
					<!-- 							急单 -->
					<!-- 						</label> -->
					<!-- 					</span> -->
				</div>
				<!--产品信息部分Start-->
				<div class="fold_wrap" id="productList_div" style="display: none;">
					<div class="fold_table ">
						<input type="hidden" id="product_sum_saleProduceQty" value="0" />
						<input type="hidden" id="product_sum_spareProduceQty" value="0" />
						<input type="hidden" id="product_sum_produceQty" value="0" />
						<table class="work_table product_table" id="product_table">
							<thead>
								<tr>
									<th width="35" name="operator">操作</th>
									<th name="productName">成品名称</th>
									<th name="code" style="display: none">产品编号</th>
									<th name="customerName">客户名称</th>
									<th name="style">产品规格</th>
									<th name="unit">单位</th>
									<th name="sourceQty">销售数量</th>
									<th name="saleProduceQty">生产数量</th>
									<th name="spareProduceQty">备品数量</th>
									<th name="produceQty">生产总量</th>
									<th name="sourceBillNo">销售单号</th>
									<%-- <shiro:hasPermission name="produce:work:money"> --%>
									<th name="price">单价</th>
									<th name="money">金额</th>
									<%-- </shiro:hasPermission> --%>

									<th name="customerBillNo">客户单号</th>
									<th name="customerMaterialCode">客户料号</th>
									<th name="deliveryTime">
										交货日期
										<i id="batch_edit_deliveryTime" class="fa fa-edit" src="batch_deliveryTime_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th name="customerRequire">客户要求</th>
									<th name="memo">备注</th>
								</tr>
							</thead>
							<tbody>
								<%-- 								<c:forEach var="product" items="${order.productList}"> --%>
								<!-- 									<tr> -->
								<!-- 										<td class="td-manage""> -->
								<%-- 											<input name="productList.productId" type="hidden" value="${product.productId}" onmouseover="this.title=this.value" /> --%>
								<!-- 											<i class="delete fa fa-trash-o"></i> -->
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.productName" class="tab_input" type="text" readonly="readonly" value="${product.productName}" onmouseover="this.title=this.value" /> --%>
								<!-- 										</td> -->

								<!-- 										<td class="pru_number" style="display: none"> -->
								<%-- 											<input class="tab_input" type="text" name="productList.productCode" readonly="readonly" value="${product.productCode }" /> --%>
								<!-- 										</td> -->

								<!-- 										<td> -->
								<%-- 											<input name="productList.customerId" class="tab_input" type="hidden" value="${product.customerId}" /> --%>
								<%-- 											<input name="productList.customerCode" class="tab_input" type="hidden" value="${product.customerCode}" /> --%>
								<%-- 											<input name="productList.customerName" class="tab_input" readonly="readonly" type="text" value="${product.customerName}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.style" class="tab_input" readonly="readonly" type="text" value="${product.style}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.unitId" class="tab_input" type="hidden" value="${product.unitId}" /> --%>
								<%-- 											<input class="tab_input" readonly="readonly" type="text" value="${product.unitName}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<!-- 											<input name="productList.sourceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="0" /> -->
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.saleProduceQty" class="constraint_negative tab_input bg_color" type="text" value="${product.saleProduceQty}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.spareProduceQty" class="constraint_negative tab_input bg_color" type="text" value="${product.spareProduceQty}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.produceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="${product.produceQty}" /> --%>
								<!-- 										</td> -->

								<!-- 										<td> -->
								<%-- 											<input name="productList.sourceBillNo" class="tab_input" readonly="readonly" type="text" value="${(billType eq 'PRODUCE_SUPPLEMENT')?product.sourceBillNo:''}" /> --%>
								<!-- 											<input name="productList.sourceId" type="hidden" value="" /> -->
								<!-- 											<input name="productList.sourceDetailId" type="hidden" value="" /> -->
								<!-- 											<input name="productList.sourceBillType" type="hidden" value="" /> -->
								<!-- 										</td> -->

								<!-- 										<td> -->
								<%-- 											<input name="productList.price" class="tab_input bg_color constraint_decimal_negative" type="text" value="${not empty product.price?product.price:0}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.money" class="tab_input bg_color constraint_decimal_negative" type="text" value="${not empty product.money?product.money:0}" /> --%>
								<!-- 										</td> -->

								<!-- 										<td> -->
								<%-- 											<input name="productList.customerBillNo" class="tab_input" readonly="readonly" type="text" value="${(billType eq 'PRODUCE_SUPPLEMENT')?product.customerBillNo:''}" /> --%>
								<!-- 										</td> -->


								<!-- 										分割线 -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.customerMaterialCode" class="tab_input" readonly="readonly" type="text" value="${product.customerMaterialCode}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<!-- 											<input name="productList.deliveryTime" class="tab_input bg_color" type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'createDate\')}'})" value="" /> -->
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.customerRequire" class="tab_input bg_color" type="text" onmouseover="this.title=this.value" value="${product.customerRequire}" /> --%>
								<!-- 										</td> -->
								<!-- 										<td> -->
								<%-- 											<input name="productList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${product.memo}" /> --%>
								<!-- 										</td> -->
								<!-- 									</tr> -->
								<%-- 								</c:forEach> --%>
							</tbody>
						</table>
					</div>
				</div>
				<!----------------------产品信息部分End------------------------------------------>
				<!----------------------部件部分Start-------------------------------------------->
				<div class="fold_wrap" id="partList_div">
					<c:if test="${null != order}">
						<c:forEach items="${order.partList }" var="part">
							<div class="fold_table">
								<div class="for_sel">
									<table class="work_table work_table_new">
										<thead>
											<tr>
												<th width="">操作</th>
												<th width="">部件名称</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<i title="删除" class="fa fa-trash-o" name="btn_part_del"></i>
												</td>
												<td class="pro_name fold_ico">
													<input name="partList.id" type="hidden" class="tab_input" value="${part.id}" />
													<input name="partList.partName" type="text" class="tab_input bg_color" value="${part.partName}" />
													<i class="btn_toggle fa fa-minus-square-o"> </i>
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
													<th width="120">成品名称</th>
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
															<input name='productList.productName' class='tab_input' onmouseover='this.title=this.value' type='text' value='${product.productName }' readonly='readonly' />
														</td>
													</tr>
												</c:forEach>
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
												<div class="l right_path">
													<c:forEach var="material" items="${part.materialList}">
														<div class="cl material-class">

															<div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料名称：</label>
																	<div class="inside_combo">
																		<input type="hidden" name="id" value="${material.id }" />
																		<input type="hidden" name="materialId" value="${material.materialId }" />
																		<input type="hidden" name="materialCode" value="${material.materialCode }" />
																		<input type="text" onmouseover="this.title=this.value" class="input-txt input-txt_9" readonly="readonly" name="materialName" value="${material.materialName }" />
																	</div>
																</div>
																<div class="inside_item">
																	<label class="form-label label_ui label_1">材料规格：</label>
																	<div class="inside_combo use_relative">
																		<input type="text" class="input-txt input-txt_8" name="materialStyle" value="${material.style }" />
																		<div class="style_sel_item">
																			<div class="sel_ico"></div>
																			<div class="hy_hide td_box" style="width: 160px; top: 24px; left: initial; right: -20px;">
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
																		<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" value="${material.stockUnitName }" />
																	</div>
																</div>
																<!-- 																<div class="inside_item"> -->
																<!-- 																	<label class="form-label label_ui label_1">材料开数：</label> -->
																<!-- 																	<div class="inside_combo"> -->
																<%-- 																		<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="${material.splitQty }" /> --%>
																<!-- 																	</div> -->
																<!-- 																</div> -->
																<!-- 																<div class="inside_item"> -->
																<!-- 																	<label class="form-label label_ui label_1">材料用量：</label> -->
																<!-- 																	<div class="inside_combo"> -->
																<%-- 																		<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" value="${material.qty }" /> --%>
																<!-- 																	</div> -->
																<!-- 																</div> -->
																<div class="inside_item">
																	<label class="form-label label_ui label_1">来纸：</label>
																	<div class="inside_combo">
																		<input type="checkBox" class="input-txt" name="isCustPaper" <c:if test="${material.isCustPaper=='YES' }">checked="checked"</c:if> />
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
													</c:forEach>
												</div>
											</div>
											<div class="cl inside_box item_width procedure" style="margin-bottom: 20px;">
												<div class="l left_path">
													<label class="box_title">工 序：</label>
												</div>
												<div class="l right_path">
													<div class="cl procedure-class">
														<!--印前/印刷工序Start-->
														<div class="cl">
															<!--印前工序Start-->
															<div class="procedure_before">
																<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'BEFORE',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																	<div class="cl inside_item mar_r">
																		<div class="classify_item">
																			<div class="classify_item_title">
																				<a href="javascript:void(0)">${procedureClass_item.name }</a>
																			</div>
																			<div class="hy_hide classify_content">
																				<ul class="classify_content_list">
																					<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
																<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'PRINT',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																	<div class="cl inside_item mar_r">
																		<div class="classify_item">
																			<div class="classify_item_title">
																				<a href="javascript:void(0)">${procedureClass_item.name }</a>
																			</div>
																			<div class="hy_hide classify_content">
																				<ul class="classify_content_list">
																					<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
														<div class="cl procedure_after">
															<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'AFTER',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
																<div class="cl inside_item mar_r">
																	<div class="classify_item">
																		<div class="classify_item_title">
																			<a href="javascript:void(0)">${procedureClass_item.name }</a>
																		</div>
																		<div class="hy_hide classify_content">
																			<ul class="classify_content_list">
																				<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
											<div class="cl inside_box procedure-line ">
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
											<div class="cl inside_box  technics-memo">
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
					</c:if>
				</div>
				<!----------------------部件部分End----------------------------------------------->
				<!----------------------成品工序Start--------------------------------------------->
				<div class="fold_wrap" id="pack_div">
					<div class="fold_table">
						<div class="cl inside_container" style="margin: -1px 0 0 0">
							<div class="left_path procedure_finished" style="border-right: none;">成品工序</div>
							<div class="right_path" style="border-left: 1px solid #ccc;">
								<div class="cl inside_box material" style="margin-bottom: 20px;">
									<div class="l left_path">
										<a href="javascript:;" title="添加材料" class="nav_btn table_nav_btn newadd_item_btn">
											<i class="fa fa-plus-square"></i>
										</a>
										<label class="box_title">材 料：</label>
									</div>
									<div class="l right_path">
										<c:if test="${null != order}">
											<c:forEach var="material" items="${order.pack.materialList}">
												<div class="cl material-class">
													<div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料名称：</label>
															<div class="inside_combo">
																<input type="hidden" name="id" value="${material.id}" />
																<input type="hidden" name="materialId" value="${material.materialId }" />
																<input type="hidden" name="materialCode" value="${material.materialCode }" />
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
																<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" value="${material.stockUnitName }" />
															</div>
														</div>
														<!-- 														<div class="inside_item"> -->
														<!-- 															<label class="form-label label_ui label_1">材料开数：</label> -->
														<!-- 															<div class="inside_combo"> -->
														<%-- 																<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="${material.splitQty }" /> --%>
														<!-- 															</div> -->
														<!-- 														</div> -->
														<!-- 														<div class="inside_item"> -->
														<!-- 															<label class="form-label label_ui label_1">材料用量：</label> -->
														<!-- 															<div class="inside_combo"> -->
														<%-- 																<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" value="${material.qty }" /> --%>
														<!-- 															</div> -->
														<!-- 														</div> -->
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
										</c:if>
									</div>
								</div>
								<div class="cl inside_box item_width">
									<div class="l left_path">
										<label class="box_title">工序：</label>
									</div>
									<div class="l right_path">
										<div class="cl procedure_finished_pro procedure-memo-only">
											<c:forEach items="${fns:basicListParams('PROCEDURE','procedureType','productType', 'FINISHED',(productType eq 'PACKE'?'1,2':'1,3'))}" var="procedure_item">
												<div class="cl inside_item mar_r" style="width: auto;">
													<c:set var="checked" value="" />
													<c:set var="checkedOut" value="" />
													<c:set var="memo" value=""></c:set>
													<c:forEach items="${order.pack.procedureList}" var="procedure">
														<c:if test="${procedure_item.id eq procedure.procedureId }">
															<c:set var="checked" value="checked" />
															<c:set var="checkedOut" value="${procedure.isOutSource}" />
															<c:set var="memo" value="${procedure.memo }"></c:set>
														</c:if>
													</c:forEach>
													<span class="form-label label_ui radio_item">
														<label class="hy_hide isOutsource">
															<input class="" type="checkbox" <c:if test="${checkedOut=='YES' }">checked</c:if> title="发外">
															发外
														</label>
														<span class='removeRequierBt'>X</span>
														<label class='hy_hide radio_item2'>要求</label>
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
									<div class="cl inside_box item_width">
										<div class="l left_path">
											<label class="box_title">备注：</label>
										</div>
										<div class="l right_path">
											<div class="remakes_path">
												<div class="l remakes_content " sstyle="line-height: 1.5;min-height: 22px">

													<div class="l fold_textarea requireText" id="pack_memo">${order.pack.memo }</div>
												</div>
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


		<div id="part_template" class="hy_hide">
			<div class="fold_table">
				<div class="for_sel">
					<table class="work_table work_table_new">
						<thead>
							<tr>
								<th width="">操作</th>
								<th width="">部件名称</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<i title="删除" class="fa fa-trash-o" name="btn_part_del"></i>
								</td>
								<td class="pro_name fold_ico">
									<input name="partList.id" type="hidden" class="tab_input" />
									<input name="partList.partName" type="text" class="tab_input bg_color" />
									<i class="btn_toggle fa fa-minus-square-o"> </i>
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
							<div class="cl inside_box item_width procedure" style="margin-bottom: 20px;">
								<div class="l left_path">
									<label class="box_title">工 序：</label>
								</div>
								<div class="l right_path">
									<div class="cl procedure-class">
										<!--印前/印刷工序Start-->
										<div class="cl">
											<!--印前工序Start-->
											<div class="procedure_before">
												<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType' ,'BEFORE',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
													<div class="cl inside_item mar_r">
														<div class="classify_item">
															<div class="classify_item_title">
																<a href="javascript:void(0)">${procedureClass_item.name }</a>
															</div>
															<div class="hy_hide classify_content">
																<ul class="classify_content_list">
																	<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
												<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'PRINT',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
													<div class="cl inside_item mar_r">
														<div class="classify_item">
															<div class="classify_item_title">
																<a href="javascript:void(0)">${procedureClass_item.name }</a>
															</div>
															<div class="hy_hide classify_content">
																<ul class="classify_content_list">
																	<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
										<div class="cl procedure_after">
											<c:forEach items="${fns:basicListParams('PROCEDURECLASS','procedureType','productType', 'AFTER',productType eq 'PACKE'?'1,2':'1,3')}" var="procedureClass_item">
												<div class="cl inside_item mar_r">
													<div class="classify_item">
														<div class="classify_item_title">
															<a href="javascript:void(0)">${procedureClass_item.name }</a>
														</div>
														<div class="hy_hide classify_content">
															<ul class="classify_content_list">
																<c:forEach items="${fns:basicListParams('PROCEDURE','procedureClassId','productType', procedureClass_item.id,productType eq 'PACKE'?'1,2':'1,3')}" var="procedure_item">
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
							<div class="cl inside_box procedure-line">
								<div class="l left_path pri_item">
									<label class="box_title">工艺线路：</label>
								</div>
								<div class="l right_path">
									<div class="cl procedure-line-class procedure-memo-only"></div>
								</div>
							</div>
							<div class="cl inside_box  technics-memo">
								<div class="l left_path">
									<label class="box_title">工艺备注：</label>
								</div>
								<div class="l right_path" style="line-height: 1.5; min-height: 22px">
									<div class="l fold_textarea requireText"></div>
								</div>
							</div>
						</div>
						<!--END-->
					</div>
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
						<input type="hidden" name="id" />
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
							<div class="hy_hide td_box" style="width: 160px; top: 24px; left: initial; right: -20px;">
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
						<input type="hidden" name="materialValuationUnitId" />
						<input type="hidden" name="materialStockUnitId" />
						<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" />
					</div>
				</div>
				<!-- 				<div class="inside_item"> -->
				<!-- 					<label class="form-label label_ui label_1">材料开数：</label> -->
				<!-- 					<div class="inside_combo"> -->
				<!-- 						<input type="text" class="constraint_negative input-txt input-txt_14" name="materialSplitQty" value="1" /> -->
				<!-- 					</div> -->
				<!-- 				</div> -->
				<!-- 				<div class="inside_item"> -->
				<!-- 					<label class="form-label label_ui label_1">材料用量：</label> -->
				<!-- 					<div class="inside_combo"> -->
				<!-- 						<input type="text" class="constraint_decimal_negative input-txt input-txt_8" name="materialQty" /> -->
				<!-- 					</div> -->
				<!-- 				</div> -->
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