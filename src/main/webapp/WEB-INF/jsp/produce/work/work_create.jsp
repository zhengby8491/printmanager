<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/common/work/workCommon.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic}/site/produce/work/work_create.js?v=${v }"></script>
<title>创建生产工单</title>
<style type="text/css">
.tab_input {
	height: 30px
}
</style>
<script type="text/javascript">
$(function(){
	//未生产订单转过来的
	if ('${productArray}')
	{
		var rows = $.parseJSON('${productArray}');
		// var rows = JSON.parse(${productArray});
		getCallInfo_productArray(rows);
	}
})

</script>
</head>

<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-生产工单-创建"></sys:nav>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
				<div class="top_nav">

					<shiro:hasPermission name="produce:work:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="produce:work:create,produce:work:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
					<input type="hidden" id="sourceWorkId" value="${sourceWorkId }">
					<input type="hidden" id="isCheck">
				</div>
			</div>
			<!--表单部分上-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">印刷类型：</label>
								<span class="ui-combo-noborder">
									<c:if test="${productType ne'ROTARY' }">
										<select class="hy_select2 input-txt input-txt_3s" name="productType">
											<option value="PACKE" <c:if test="${productType=='PACKE' }">selected="selected"</c:if>>包装印刷</option>
											<option value="BOOK" <c:if test="${productType=='BOOK' }">selected="selected"</c:if>>书刊印刷</option>
										</select>
									</c:if>
									<c:if test="${productType eq'ROTARY' }">
										<select class="hy_select2 input-txt input-txt_3s" name="productType" disabled="disabled">
											<option value="ROTARY" selected="selected">轮转印刷</option>
										</select>
									</c:if>
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">工单类型：</label>
								<span class="ui-combo-noborder">
									<phtml:list items="${billTypeList}" textProperty="text" name="billType" cssClass="hy_select2 input-txt input-txt_3s" />
								</span>
							</dd>

							<dd class="row-dd">
								<label class="form-label label_ui label_1">制 单 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${fns:getUserEmployeeName()}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">制单日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createDate" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">备 注：</label>
								<span class="form_textarea">
									<textarea class="noborder" style="width: 706px" id="memo" onKeyUp="Helper.doms.textarealength(this,1000)"><c:if test="${!empty sourceWorkId}">基于 ${order.billNo }${billType.text }</c:if></textarea>
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

						<c:if test='${fns:hasCompanyPermission("sale:order:list")}'>
							<a href="javascript:;" class="nav_btn table_nav_btn" id="select_sale_detail">
								<i class="fa fa-plus-square"></i>
								选择销售订单
							</a>
						</c:if>

						<a href="javascript:;" class="nav_btn table_nav_btn" id="select_product">
							<i class="fa fa-plus-square"></i>
							选择产品
						</a>
						<a href="javascript:;" class="nav_btn table_nav_btn" id="add_part">
							<i class="fa fa-plus-square"></i>
							添加部件/工序
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
					<c:if test="${productType == 'BOOK' }">
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" id="auto_stricker">
							自动分贴
						</a>
					</span>
					</c:if>
					<input type="hidden" id="order_billType" value="${billType }"/>
					<input type="hidden" id="order_productType" value="${productType }"/>
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
									<th name="imgUrl">产品图片</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="product" items="${order.productList}">
									<tr>
										<td class="td-manage"">
											<input name="productList.productId" type="hidden" value="${product.productId}" onmouseover="this.title=this.value" />
											<i class="delete fa fa-trash-o"></i>
										</td>
										<td>
											<input name="productList.productName" class="tab_input" type="text" readonly="readonly" value="${product.productName}" onmouseover="this.title=this.value" />
										</td>
										<td class="pru_number" style="display: none">
											<input class="tab_input" type="text" name="productList.productCode" readonly="readonly" value="${product.productCode }" />
										</td>

										<td>
											<input name="productList.customerId" class="tab_input" type="hidden" value="${product.customerId}" />
											<input name="productList.customerCode" class="tab_input" type="hidden" value="${product.customerCode}" />
											<input name="productList.customerName" class="tab_input" readonly="readonly" type="text" value="${product.customerName}" />
										</td>
										<td>
											<input name="productList.style" class="tab_input" readonly="readonly" type="text" value="${product.style}" />
										</td>
										<td>
											<input name="productList.unitId" class="tab_input" type="hidden" value="${product.unitId}" />
											<input class="tab_input" readonly="readonly" type="text" value="${product.unitName}" />
										</td>
										<td>
											<input name="productList.sourceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="0" />
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
											<input name="productList.sourceBillNo" class="tab_input" onmouseover="this.title=this.value" readonly="readonly" type="text" value="${(billType eq 'PRODUCE_SUPPLEMENT')?product.sourceBillNo:''}" />
											<input name="productList.sourceId" type="hidden" value="" />
											<input name="productList.sourceDetailId" type="hidden" value="" />
											<input name="productList.sourceBillType" type="hidden" value="" />
										</td>

										<td>
											<input name="productList.price" class="tab_input bg_color constraint_decimal_negative" type="text" value="${not empty product.price?product.price:0}" />
										</td>
										<td>
											<input name="productList.money" class="tab_input bg_color constraint_decimal_negative" type="text" value="${not empty product.money?product.money:0}" />
										</td>

										<td>
											<input name="productList.customerBillNo" class="tab_input" readonly="readonly" type="text" value="${(billType eq 'PRODUCE_SUPPLEMENT')?product.customerBillNo:''}" />
										</td>


										<!-- 分割线 -->
										<td>
											<input name="productList.customerMaterialCode" class="tab_input" readonly="readonly" type="text" value="${product.customerMaterialCode}" />
										</td>
										<td>
											<input name="productList.deliveryTime" class="tab_input bg_color" type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'createDate\')}'})" value="" />
										</td>
										<td>
											<input name="productList.customerRequire" class="tab_input bg_color" type="text" onmouseover="this.title=this.value" value="${product.customerRequire}" />
										</td>
										<td>
											<input name="productList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${product.memo}" />
										</td>
										<td>
											<c:if test="${product.imgUrl != ''}">
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
					<c:if test="${!empty sourceWorkId || !empty saleTransmit}">
						<c:forEach items="${order.partList }" var="part">
							<div class="fold_table">
								<div class="for_sel">
									<table class="work_table work_table_new">
										<thead>
											<tr>
												<th width="">操作</th>
												<th width="">部件名称</th>
												<th width="">部件数量</th>
												<th width="">倍率</th>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">P数</th>
												</c:if>
												<th width="">拼版数</th>
												<c:if test="${productType ne 'ROTARY' }">
													<th width="">上机规格</th>
												</c:if>
												<th width="">印刷方式</th>
												<th width="">正反普色</th>
												<th width="">正反专色</th>
												<c:if test="${productType ne 'ROTARY' }">
													<th width="">印张正数</th>
												</c:if>
												<c:if test="${productType eq 'ROTARY' }">
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
												<th width="">放损%</th>
												<c:if test="${order.productType eq 'BOOK' }">
													<th width="">单贴放损</th>
												</c:if>
												<th width="">放损数</th>
												<c:if test="${productType ne 'ROTARY' }">
													<th width="">总印张</th>
												</c:if>
												<c:if test="${productType eq 'ROTARY' }">
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
												<td class="use_relative" <c:if test="${productType eq 'ROTARY' }">style="display:none;"</c:if>>
													<input name="partList.style" type="text" class="tab_input bg_color" value="${part.style}" />
													<div class="style_sel_item">
														<div class="sel_ico"></div>
														<div class="hy_hide td_box" style="width: 160px;">
															<c:if test="${productType eq 'PACKE' }">
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
															<c:if test="${productType eq 'BOOK' }">
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
													<phtml:list name="partList.printType" textProperty="text" cssClass="tab_input bg_color" selected="${part.printType }" type="com.huayin.printmanager.persist.enumerate.PrintType"></phtml:list>
												</td>
												<td class="use_relative">
													<input type="text" class="tab_input bg_color" name="partList.generalColor" value="${part.generalColor}" />

													<c:choose>
														<c:when test="${part.printType eq 'BLANK' }">

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

														</c:when>
														<c:when test="${part.printType eq 'SINGLE' }">
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
												<c:if test="${productType ne 'ROTARY' }">
													<td>
														<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="${part.impressionNum}" />
													</td>
												</c:if>
												<c:if test="${productType eq 'ROTARY' }">
													<td>
														<input name="partList.gear" type="text" class="tab_input bg_color" value="${part.gear}" />
													</td>
													<td>
														<input name="partList.distance" type="text" class="tab_input bg_color" value="${part.distance}" />
													</td>
													<td>
														<input name="partList.walkDistance" type="text" class="tab_input bg_color" value="${part.walkDistance}" />
													</td>
													<td>
														<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="1" />
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
													<input name="partList.lossRate" type="text" class="constraint_decimal tab_input bg_color" value="${part.lossRate}" />
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
												<c:if test="${order.productType ne 'ROTARY' }">
													<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="${part.totalImpressionNum}" />
												</c:if>
												<c:if test="${order.productType eq 'ROTARY' }">
													<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="" />
												</c:if>
												</td>

												<td style="position: relative;">
													<input name="partList.machineName" id="" type="text" class="tab_input bg_color" value="${part.machineName}">
													<input name="partList.machineId" class="machineId" id="" type="hidden" value="${part.machineId}">
													<div class="select-btn" id="machineNameDiv" style="height: 100%; line-height: inherit">...</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="cl whole">
									<div class="l plywood_product w_left">
										<table class="makeup_table" rules="all" style="width: auto">
											<thead>
												<tr>
													<th width="40">操作</th>
													<th width="120">成品名称</th>
													<th width="80">生产数量</th>
													<c:if test="${order.productType eq 'BOOK' }">
														<th width="40">P数</th>
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
															<input name='productList.productId' type='hidden' value='${product.productId }' />
															<input name='productList.productName' class='tab_input' onmouseover='this.title=this.value' type='text' value='${product.productName }' readonly='readonly' />
														</td>
														<td>
															<input name='productList.produceQty' class='tab_input' readonly='readonly' type='text' value='${product.produceQty }' />
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
																<c:if test="${productType ne 'ROTARY' }">
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
										<c:if test="${!empty sourceWorkId || !empty saleTransmit}">
											<c:forEach var="material" items="${order.pack.materialList}">
												<div class="cl material-class">
													<div>
														<div class="inside_item">
															<label class="form-label label_ui label_1">材料名称：</label>
															<div class="inside_combo">
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
														<c:if test="${productType ne 'ROTARY' }">
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
								<th width="">部件数量</th>
								<th width="">倍率</th>
								<th width="">拼版数</th>
								<c:if test="${productType ne 'ROTARY' }">
									<th width="">上机规格</th>
								</c:if>
								<th width="">印刷方式</th>
								<th width="">正反普色</th>
								<th width="">正反专色</th>
								<c:if test="${productType ne 'ROTARY' }">
									<th width="">印张正数</th>
								</c:if>
								<c:if test="${productType eq 'ROTARY' }">
									<th width="">齿轮数</th>
									<th width="">齿间距</th>
									<th width="">走距(mm)</th>
									<th width="">标准用料(m)</th>
								</c:if>
								<th width="">印版付数</th>
								<th width="">印版张数</th>
								<th width="">放损%</th>
								<th width="">放损数</th>
								<c:if test="${productType ne 'ROTARY' }">
									<th width="">总印张</th>
								</c:if>
								<c:if test="${productType eq 'ROTARY' }">
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
									<input name="partList.partName" type="text" class="tab_input bg_color" />
									<i class="btn_toggle fa fa-minus-square-o"> </i>
								</td>
								<td>
									<input name="partList.qty" type="text" class="constraint_negative tab_input bg_color" value="0" />
								</td>
								<td>
									<input name="partList.multiple" type="text" class="constraint_negative tab_input bg_color" value="1" />
								</td>
								<td>
									<input name="partList.pieceNum" readonly="readonly" type="text" class="constraint_negative tab_input" value="1" />
								</td>
								<td class="use_relative" <c:if test="${productType eq 'ROTARY' }">style="display:none;"</c:if>>
									<input name="partList.style" type="text" class="tab_input bg_color" value="" />
									<div class="style_sel_item">
										<div class="sel_ico"></div>
										<div class="hy_hide td_box" style="width: 160px;">
											<c:if test="${productType eq 'PACKE' }">
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
											<c:if test="${productType eq 'BOOK' }">
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
								</td>
								<td class="use_relative">
									<input type="text" class="tab_input bg_color" name="partList.spotColor" value="0+0" />
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
								</td>
								<c:if test="${productType ne 'ROTARY' }">
									<td>
										<input name="partList.impressionNum" type="text" readonly="readonly" class="tab_input" value="0" />
									</td>
								</c:if>
								<c:if test="${productType eq 'ROTARY' }">
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
									<input name="partList.lossQty" type="text" class="tab_input bg_color constraint_negative" value="0" />
								</td>
								<td>
									<input name="partList.totalImpressionNum" type="text" readonly="readonly" class="tab_input" value="0" />
								</td>

								<td style="position: relative">
									<input name="partList.machineName" id="" type="text" class="tab_input bg_color" value="">
									<input name="partList.machineId" class="machineId" id="" type="hidden" value="">
									<div class="select-btn" id="machineNameDiv" style="height: 100%; line-height: inherit">...</div>
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
									<th>拼版数</th>
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
						<input type="hidden" name="materialValuationUnitId" />
						<input type="hidden" name="materialStockUnitId" />
						<input type="text" class="input-txt input-txt_14" readonly="readonly" name="materialStockUnitName" />
					</div>
				</div>
				<c:if test="${productType ne 'ROTARY' }">
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