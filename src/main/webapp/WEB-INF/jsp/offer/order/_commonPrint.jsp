<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!---------------------------------------- 对外报价 --------------------------------------------->
<div id="print_content" class="visible-print-block">
	<table border="0" class="border-table1 offer_detail_table">
		<thead>
			<td colspan="12" style="background: #e2e9ef;">
				<span>${offerBean.productName }</span>
				- 报价单
			</td>
		</thead>
		<tbody>
			<tr>
				<td rowspan="2" width="8%">成品信息</td>
				<td>客户名称</td>
				<td>客户地址</td>
				<td>联系电话</td>
				<td colspan="2">成品名称</td>
				<td>成品规格</td>
				<td>报价数量</td>
				<td>报价单号</td>
				<td>交货日期</td>
				<td>报价人</td>
				<td>报价日期</td>
			</tr>
			<tr class="whiteBg">
				<td>${offerBean.customerName }</td>
				<td>${offerBean.linkAddress}</td>
				<td>${offerBean.linkName}${offerBean.phone}</td>
				<td colspan="2">${offerBean.productName}</td>
				<td>${offerBean.specification}</td>
				<td>${offerBean.amount}</td>
				<td>${offerBean.offerNo}</td>
				<td>${offerBean.deliveryDateStr}</td>
				<td>${offerBean.createName}</td>
				<td>${offerBean.createDateTimeStr}</td>
			</tr>
			<c:forEach items="${offerBean.offerPartList }" var="partList">
				<tr>
					<td <c:choose>
									<c:when test="${offerBean.offerType != 'CARTONBOX'}">rowspan="5"</c:when>
									<c:otherwise>rowspan="6"</c:otherwise>
								</c:choose>>${partList.partName}</td>
					<td>印刷机</td>
					<td>上机规格</td>
					<td>印刷方式</td>
					<td style="width: 50px">正反普色</td>
					<td style="width: 50px">正反专色</td>
					<td>印版付数</td>
					<td>印版张数</td>
					<td>拼版数</td>
					<td>印张正数</td>
					<td>放损数</td>
					<td>总印张</td>
				</tr>
				<tr class="whiteBg">
					<td>${partList.machineName}</td>
					<td>${partList.machineSpec}</td>
					<td>${partList.offerPrintStyleType.text}</td>
					<td>${partList.prosConsColor}</td>
					<td>${partList.prosConsSpot}</td>
					<td>${partList.offerPrintStyleType.value}</td>
					<td>${partList.sheetZQ}</td>
					<td>${partList.sheetNum}</td>
					<td>${partList.impositionNum}</td>
					<td>${partList.waste}</td>
					<td>${partList.paperTotal}</td>
				</tr>
				<tr>
					<td>材料分类</td>
					<td>材料名称</td>
					<td>材料规格</td>
					<td colspan="2">克重</td>
					<td>上机数量</td>
					<td>材料开数</td>
					<td>材料数量</td>
					<td>计价数量</td>
					<td>材料单价</td>
					<td>材料金额</td>
				</tr>
				<tr class="whiteBg">
					<td>${partList.paperName}</td>
					<td>${partList.paperName}</td>
					<td>${partList.paperType.style}</td>
					<td colspan="2">${partList.paperWeight}</td>
					<td>${partList.paperTotal}</td>
					<td>${partList.materialOpening}</td>
					<td>${partList.materialAmount}</td>
					<td>${partList.calNum}</td>
					<td class="tofix">${partList.paperTonPrice}</td>
					<td class="tofix">${partList.paperTonPrice * partList.calNum}</td>
				</tr>
				<c:if test="${partList.containBflute == 'YES'}">
					<tr class="whiteBg">
						<td>${partList.bflutePit}</td>
						<td>${partList.bflutePaperQuality}</td>
						<td>${partList.machineSpec}</td>
						<td colspan="2"></td>
						<td>${partList.bfluteNum}</td>
						<td>1</td>
						<td>${partList.bfluteNum}</td>
						<td>${partList.bfluteCalNum}</td>
						<td class="tofix">${partList.bflutePrice}</td>
						<td class="tofix">${partList.bflutePrice * partList.bfluteCalNum}</td>
					</tr>
				</c:if>
				<c:if test="${partList.containBflute != 'YES' && offerBean.offerType == 'CARTONBOX' }">
					<tr class="whiteBg">
						<td></td>
						<td></td>
						<td></td>
						<td colspan="2"></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
				</c:if>
				</tr>
				<tr class="whiteBg">
					<td>后道工序</td>
					<td colspan="10" style="text-align: left; word-wrap: break-word; white-space: normal;">${partList.partProcedureStr}</td>
				</tr>
			</c:forEach>
			<c:if test="${offerBean.offerType == 'CARTONBOX' }">
				<tr class="whiteBg">
					<td style="background: #F1F1F1">成品工序</td>
					<td>成品工序</td>
					<td colspan="10" style="text-align: left; word-wrap: break-word; white-space: normal;">${offerBean.productProcedureStr }</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<table border="0" class="border-table1 offer_detail_table" style="width: 90%">
		<tbody>
			<tr>
				<td rowspan="${offerBean.ladderCol+1 }" width="8.35%;">阶梯数据</td>
				<td>数量</td>
				<td>纸张费</td>
				<td>印刷费</td>
				<td>工序费</td>
				<td>其他费</td>
				<td>运费</td>
				<td>成本金额</td>
				<td>利润</td>
				<td>未税金额</td>
				<td>未税单价</td>
				<td>含税金额</td>
				<td>含税单价</td>
			</tr>
			<c:forEach items="${offerBean.offerOrderQuoteInnerList }" var="quoteList">
				<tr class="whiteBg">
					<td>${quoteList.amount }</td>
					<td class="tofix">${quoteList.paperFee }</td>
					<td class="tofix">${quoteList.printFee }</td>
					<td class="tofix">${quoteList.procedureFee }</td>
					<td class="tofix">${quoteList.ohterFee }</td>
					<td class="tofix">${quoteList.freightFee }</td>
					<td class="tofix">${quoteList.costMoney }</td>
					<td class="tofix">${quoteList.profitFee }</td>
					<td class="tofix">${quoteList.untaxedFee }</td>
					<td class="tofix">${quoteList.untaxedPrice }</td>
					<td class="tofix">${quoteList.taxFee }</td>
					<td class="tofix">${quoteList.taxPrice }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div id="print_content_dw" class="visible-print-block">
	<table id="dyTable" class="border-table1 offer_detail_table">
		<tbody>
			<tr>
				<td>报价日期</td>
				<td colspan="2">${offerBean.createDateTimeStr }</td>
				<td width="120px;">交货日期</td>
				<td width="120px;">${offerBean.deliveryDateStr }</td>
				<td>报价单号</td>
				<td colspan="2">${offerBean.offerNo }</td>
			</tr>
			<tr>
				<td>客户名称</td>
				<td colspan="2">${offerBean.customerName }</td>
				<td colspan="2" rowspan="3" style="font-size: 30px; font-family: 'STHeiti'">报价单</td>
				<td>客户地址</td>
				<td colspan="2">${offerBean.linkAddress }</td>
			</tr>
			<tr>
				<td>联系人</td>
				<td colspan="2">${offerBean.linkName }</td>
				<td>联系电话</td>
				<td colspan="2">${offerBean.linkName}${offerBean.phone }</td>
			</tr>
			<tr>
				<td>成品名称</td>
				<td colspan="2">${offerBean.productName }</td>
				<td>成品尺寸（mm）</td>
				<td colspan="2">${offerBean.specification }</td>
			</tr>
			<c:forEach items="${offerBean.offerOrderQuoteOutList }" var="quoteOut" varStatus="status">
				<c:if test="${status.count==1 }">
					<tr>
						<td>印刷纸张</td>
						<td colspan="7" style="text-align: left;">${quoteOut.printName }</td>
					</tr>
					<tr>
						<td>印刷颜色</td>
						<td colspan="7" style="text-align: left;">${quoteOut.printColor }</td>
					</tr>
					<tr>
						<td>加工工序</td>
						<td colspan="7" style="text-align: left;">${quoteOut.printProcedure }</td>
					</tr>
				</c:if>
			</c:forEach>
			<c:forEach items="${offerBean.offerOrderQuoteOutList }" var="quoteOut" varStatus="status">
				<c:if test="${status.count == 1 }">
					<tr style="font-size: 16px; font-weight: bold;">
						<td>数量</td>
						<td colspan="2">未税单价</td>
						<td colspan="2">未税金额</td>
						<td>含税单价</td>
						<td colspan="2">含税金额</td>
					</tr>
				</c:if>
				<tr>
					<td>${quoteOut.amount }</td>
					<td colspan="2" class="tofix4">${quoteOut.price }</td>
					<td colspan="2" class="tofix">${quoteOut.fee }</td>
					<td class="tofix4">${quoteOut.taxPrice }</td>
					<td colspan="2" class="tofix">${quoteOut.taxFee }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>