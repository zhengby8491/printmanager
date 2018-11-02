
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title></title>
<!-- 生产工单打印单 -->
</head>
<script type="text/javascript" src="${ctxStatic}/site/produce/work_print.js"></script>
<body class="work_body">
	<input type="hidden" id="url" value="${fns:urlEncode('/produce/work/printAjax/')}">
	<input type="hidden" id="orderId" value="${order.id }">
	<button class="nav_btn btn_print" id="conform_print" style="top: 20px; right: 70px; width: auto;">打印</button>
	<button class="nav_btn btn_print" id="select_template" style="top: 20px; width: auto;">选择模板</button>
	<div class="work_content">
		<div class="work_title">
			<h2>${loginUser.company.name }</h2>
			<h2>生产施工单</h2>
			<c:if test="${order.isEmergency eq 'YES' }">
				<span style="color: red;">(急)</span>
			</c:if>
		</div>
		<div class="work_head">
			<div>
				<label>工单类型:</label> <span>${order.billType.text }</span>
			</div>
			<div>
				<label>工单编号:</label> <span>${order.billNo }</span>
			</div>
			<div style="text-align: right">
				<label>制单日期:</label> <span> <fmt:formatDate value="${order.createTime}" type="date" pattern="yyyy-MM-dd" />
				</span>
			</div>
		</div>

		<c:if test="${order.productList!=null&& fn:length(order.productList)>0}">
			<table style="width: 100%" class="table work-table">
				<thead>
					<tr>
						<td colspan="10" style="border-top: 1px solid #000">产品信息</td>
					</tr>
					<tr>
						<th width="13%">销售单号</th>
						<th width="13%">客户单号</th>
						<th width="12%">客户名称</th>
						<th width="12%">成品名称</th>
						<th width="8%">产品规格</th>
						<th width="8%">订单数量</th>
						<th width="8%">生产数量</th>
						<th width="11%">交货日期</th>
						<th width="">生产要求</th>
						<th width="">备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${order.productList}">
						<tr>
							<td>${product.sourceBillNo }</td>
							<td>${product.customerBillNo }</td>
							<td>${product.customerName }</td>
							<td>${product.productName }</td>
							<td>${product.style }</td>
							<td>${product.sourceQty }</td>
							<td>${product.produceQty }</td>
							<td><fmt:formatDate value="${product.deliveryTime}" type="date" pattern="yyyy-MM-dd" /></td>
							<td>${product.customerRequire }</td>
							<td>${product.memo }</td>
						</tr>
					</c:forEach>
					<tr>
						<td>备注</td>
						<td colspan="9" style="text-align: left; padding: 0 10px;">${order.memo }</td>
					</tr>
					<tr>
						<td>工艺线路</td>
						<td colspan="9" style="padding: 0 30px; text-align: left"><c:forEach items="${order.partList }" var="part">
								<div>
									<div class="l" style="margin-top: 4px;">${part.partName}&nbsp;：</div>
									<div class="cl" style="display: inline;">
										<c:forEach items="${part.procedureList }" var="procedure" varStatus="sta">
											<div class='cl pri_item mar_r'>
												<c:if test="${!sta.first}">
													<i class='arrow_r fa fa-long-arrow-right'></i>
												</c:if>
												<span class='show_fw'> <span class='ct'>${procedure.procedureName }</span> <c:if test="${procedure.isOutSource eq 'YES' }">
														<span class='outsource'>(发外)</span>
													</c:if>
												</span>
											</div>
										</c:forEach>
									</div>
								</div>
							</c:forEach></td>
					</tr>
				</tbody>
			</table>
		</c:if>
		<c:if test="${allMaterialNum>0}">
			<table class="table work-table">
				<thead>
					<tr>
						<td colspan="11">材料信息</td>
					</tr>
					<tr>
						<th width="10%">部件名称</th>
						<th width="12%">材料名称</th>
						<th width="10%">材料规格</th>
						<th width="10%">材料用量</th>
						<th width="6%">克重</th>
						<th width="10%">上机规格</th>
						<th width="6%">开数</th>
						<th width="8%">印张数</th>
						<th width="6%">损耗</th>
						<th width="8%">实际用料</th>
						<th width="">备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${order.partList }" var="part">
						<c:forEach var="material" items="${part.materialList}">
							<tr>
								<td>${part.partName }</td>
								<td>${material.materialName }</td>
								<td>${material.style }</td>
								<td><span class='qty'>${material.qty }</span>${fns:basicInfo('UNIT',material.stockUnitId).name }</td>
								<td>${material.weight }</td>
								<td>${part.style }</td>
								<td>${material.splitQty }</td>
								<td>${part.impressionNum }</td>
								<td>${part.lossQty }</td>
								<td></td>
								<td>${part.memo }</td>
							</tr>
						</c:forEach>
					</c:forEach>
					<c:forEach var="material" items="${order.pack.materialList}">
						<tr>
							<td></td>
							<td>${material.materialName }</td>
							<td>${material.style }</td>
							<td><span class='qty'>${material.qty }</span>${fns:basicInfo('UNIT',material.stockUnitId).name }</td>
							<td>${material.weight }</td>
							<td></td>
							<td>${material.splitQty }</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>

		<c:if test="${before_procedureList!=null&& fn:length(before_procedureList)>0}">
			<table class="table work-table">
				<thead>
					<tr>
						<td colspan="7">印前工序</td>
					</tr>
					<tr>
						<th width="10%">部件名称</th>
						<th width="12%">工序名称</th>
						<th width="10%">数量</th>
						<th width="10%">实际数量</th>
						<th width="10%">发外</th>
						<th width="">工序要求</th>
						<th width="">签名</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${before_procedureList}" var="procedure">
						<tr>
							<td>${procedure.workPart.partName }</td>
							<td>${procedure.procedureName }</td>
							<td>${procedure.inputQty }</td>
							<td></td>
							<td><c:if test="${procedure.isOutSource eq 'YES' }">
									<span>是</span>
								</c:if></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>

		<c:if test="${print_procedureList!=null&& fn:length(print_procedureList)>0}">
			<table class="table work-table">
				<thead>
					<tr>
						<td colspan="12">印刷工序</td>
					</tr>
					<tr>
						<c:if test="${order.productType eq 'PACKE'}">
							<th width="10%">部件名称</th>
							<th width="12%">工序名称</th>
							<th width="10%">正反色数</th>
							<th width="10%">印刷方式</th>
							<th width="6%">产出数</th>
							<th width="6%">损耗</th>
							<th width="8%">实际数量</th>
							<th width="6%">发外</th>
							<th width="">工序要求</th>
							<th width="">签名</th>
						</c:if>
						<c:if test="${order.productType eq 'BOOK'}">
							<th width="10%">部件名称</th>
							<th width="12%">工序名称</th>
							<th width="10%">正反色数</th>
							<th width="10%">印刷方式</th>
							<th width="6%">P数</th>
							<th width="6%">拼版数</th>
							<th width="8%">每贴正数</th>
							<th width="6%">贴数</th>
							<th width="6%">产出</th>
							<th width="6%">损耗</th>
							<th width="6%">发外</th>
							<th width="">签名</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:if test="${order.productType eq 'BOOK'}">
						<c:forEach items="${print_procedureList}" var="procedure">
							<tr>
								<td>${procedure.workPart.partName }</td>
								<td>${procedure.procedureName }</td>
								<td><span>${procedure.workPart.generalColor }</span> &nbsp;/&nbsp; <span>${procedure.workPart.spotColor }</span></td>
								<td>${procedure.workPart.printType.text}</td>
								<td>${procedure.workPart.pageNum }</td>
								<td>${procedure.workPart.pieceNum }</td>
								<td>${procedure.workPart.stickersPostedNum }</td>
								<td>${procedure.workPart.stickersNum}</td>
								<td>${procedure.workPart.impressionNum}</td>
								<td>${procedure.workPart.lossQty}</td>
								<td><c:if test="${procedure.isOutSource eq 'YES' }">
										<span>是</span>
									</c:if></td>
								<td></td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${order.productType eq 'PACKE'}">
						<c:forEach items="${print_procedureList}" var="procedure">
							<tr>
								<td>${procedure.workPart.partName }</td>
								<td>${procedure.procedureName }</td>
								<td><span>${procedure.workPart.generalColor }</span> &nbsp;/&nbsp; <span>${procedure.workPart.spotColor }</span></td>
								<td>${procedure.workPart.printType.text}</td>
								<td>${procedure.workPart.impressionNum }</td>
								<td>${procedure.workPart.lossQty}</td>
								<td></td>
								<td><c:if test="${procedure.isOutSource eq 'YES' }">
										<span>是</span>
									</c:if></td>
								<td></td>
								<td></td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</c:if>

		<c:if test="${after_procedureList!=null&& fn:length(after_procedureList)>0}">
			<table class="table work-table">
				<thead>
					<tr>
						<td colspan="9">印后工序</td>
					</tr>
					<c:if test="${order.productType eq 'BOOK'}">
						<tr>
							<th width="10%">部件名称</th>
							<th width="12%">工序名称</th>
							<th width="10%">产出数</th>
							<th width="10%">每贴正数</th>
							<th width="6%">贴数</th>
							<th width="10%">实际数量</th>
							<th width="12%">发外</th>
							<th width="20%">工序要求</th>
							<th width="">签名</th>
						</tr>
					</c:if>
					<c:if test="${order.productType eq 'PACKE'}">
						<tr>
							<th width="10%">部件名称</th>
							<th width="12%">工序名称</th>
							<th width="10%">产出数</th>
							<th width="10%">实际数量</th>
							<th width="12%">发外</th>
							<th width="20%">工序要求</th>
							<th width="">签名</th>
						</tr>
					</c:if>
				</thead>
				<tbody>
					<c:if test="${order.productType eq 'BOOK'}">
						<c:forEach items="${after_procedureList}" var="procedure">
							<tr>
								<td>${procedure.workPart.partName }</td>
								<td>${procedure.procedureName }</td>
								<td>${procedure.outputQty }</td>
								<td>${procedure.workPart.stickersPostedNum }</td>
								<td>${procedure.workPart.stickersNum}</td>
								<td></td>
								<td><c:if test="${procedure.isOutSource eq 'YES' }">
										<span>是</span>
									</c:if></td>
								<td></td>
								<td></td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${order.productType eq 'PACKE'}">
						<c:forEach items="${after_procedureList}" var="procedure">
							<tr>
								<td>${procedure.workPart.partName }</td>
								<td>${procedure.procedureName }</td>
								<td>${procedure.outputQty }</td>
								<td></td>
								<td><c:if test="${procedure.isOutSource eq 'YES' }">
										<span>是</span>
									</c:if></td>
								<td></td>
								<td></td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</c:if>

		<c:if test="${order.pack.procedureList!=null&& fn:length(order.pack.procedureList)>0}">
			<table class="table work-table">
				<thead>
					<tr>
						<td colspan="5">成品工序</td>
					</tr>
					<tr>
						<th width="20%">工序名称</th>
						<th width="20%">产出数</th>
						<th width="20%">实际数量</th>
						<th width="20%">工序要求</th>
						<th width="20%">签名</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${order.pack.procedureList}" var="procedure">
						<tr>
							<td>${procedure.procedureName }</td>
							<td>${procedure.outputQty }</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
					<tr>
						<td>备注</td>
						<td colspan="4" style="text-align: left; padding: 0 10px;">${order.pack.memo }</td>
					</tr>
				</tbody>
			</table>
		</c:if>
		<div class="work_btm">
			<div class="r" style="width: 40%">
				<div>
					<label>制单人：${order.createName}</label>
				</div>
				<div>
					<label>审核人：${order.checkUserName}</label>
				</div>
			</div>
		</div>
	</div>
</body>
</html>