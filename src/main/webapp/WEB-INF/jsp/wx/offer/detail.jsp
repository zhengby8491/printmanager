<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/common/offer.js' charset='utf-8'></script>
<title>报价单</title>
</head>

<body>
	<div class="page page-current">
		<div class="content content-padded" style="">
			<input type="hidden" id="id" value="${id}">
			<div id="offerDetail" class="offer_detail"></div>
			<div>&nbsp;&nbsp;( 更多详情请访问电脑版 )</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="offer" name="module" />
		</jsp:include>
	</div>

	<!-- 报价单模板
	<script id="" type="text/x-dot-template">
						<table class="offer_detail_table">
				    <tr><td width="50px">报价日期 </td>
				            <td>{{= it.createDateTime }}</td>
						</tr>
						<tr><td>交货日期 </td>
				            <td>{{= it.deliveryDate}}</td>
						</tr>
						<tr><td>报价单号 </td>
				            <td>{{= it.offerNo }}</td>
						</tr>
						<tr><td>客户名称 </td>
				           	<td>{{= it.customerName}}</td>
						</tr>
						<tr><td>客户地址 </td>
				           	<td>{{= it.linkAddress}}</td>
						</tr>
						<tr><td>联系人 </td>
				           	<td>{{= it.linkName}}</td>
						</tr>
						<tr><td>联系电话 </td>
				           	<td>{{= it.phone}}</td>
						</tr>
						<tr><td>成品名称 </td>
										<td>{{= it.productName}}</td>
						</tr>
						<tr><td>成品尺寸 </td>
				            <td>{{= it.specification }}</td>
						</tr>
						<tr><td>报价人 </td>
				           <td>{{= it.createName }}</td>
						</tr>
						<tr><td>成品数量 </td>
				           <td>{{= it.amount }}</td>
						</tr>
						<tr><td>印刷纸张 </td>
				           <td class="offer_result_cost">￥{{= it.paperFee }}</td>
						</tr>
						<tr><td>颜色 </td>
				            <td>{{= it.printColor }}</td>
						</tr>
						<tr><td>加工工序</td>
				            <td class="offer_result_cost">{{= it.printProcedure }}</td>
						</tr>
						<tr><td>数量 </td>
				            <td class="offer_result_cost">￥{{= it.amount }}</td>
						</tr>
						<tr><td>单价 </td>
				            <td class="offer_result_cost">￥{{= it.price }}</td>
						</tr>
						<tr><td>金额</td>
				            <td class="offer_result_cost">￥{{= it.fee}}</td>
						</tr>
						<tr><td>含税单价 </td>
				            <td class="offer_result_cost">￥{{= it.taxPrice}}</td>
						</tr>
						<tr><td>含税金额 </td>
				            <td class="offer_result_cost">￥{{= it.taxFee}}</td>
						</tr>
						
	</script> -->
	<!-- 报价单模板（原） -->
	<script id="offerDetailTmpl" type="text/x-dot-template">
						<table class="offer_detail_table">
						<tr><td width="90px">成品名称</td>
				            <td>{{= it.productName }}</td>
						</tr>
						<tr><td>客户名称</td>
				            <td>{{= it.customerName }}</td>
						</tr>
						<tr><td>报价单号</td>
				            <td>{{= it.offerNo}}</td>
						</tr>
						<tr><td>报价日期</td>
				            <td>{{= it.createDateTime }}</td>
						</tr>
						<tr><td>报价人</td>
				           	<td>{{= it.createName}}</td>
						</tr>
						<tr><td>数量</td>
				           	<td>{{= it.amount}}</td>
						</tr>
						<tr><td>含税单价 </td>
				           	<td class="offer_result_cost">￥{{= it.taxPrice}}</td>
						</tr>
						<tr><td>纸张费用  </td>
				           	<td class="offer_result_cost">￥{{= it.paperFee}}</td>
						</tr>
						<tr><td>印刷费用  </td>
				           	<td class="offer_result_cost">￥{{= it.printFee}}</td>
						</tr>
						<tr><td>工序费用 </td>
										<td class="offer_result_cost">￥{{= it.procedureFee}}</td>
						</tr>
						<tr><td>其他费用 </td>
				            <td class="offer_result_cost">￥{{= it.ohterFee }}</td>
						</tr>
						<tr><td>运费 </td>
				           <td class="offer_result_cost">￥{{= it.freightFee }}</td>
						</tr>
						<tr><td>成本金额  </td>
				           <td class="offer_result_cost">￥{{= it.costMoney }}</td>
						</tr>
						<tr><td>利润 </td>
				           <td class="offer_result_cost">￥{{= it.profit }}</td>
						</tr>
						<tr><td>未税金额  </td>
				            <td class="offer_result_cost">￥{{= it.untaxedFee }}</td>
						</tr>
						<tr><td>未税单价</td>
				           <td class="offer_result_cost">￥{{= it.untaxedPrice }}</td>
						</tr>
						<tr><td>含税金额 </td>
				           <td class="offer_result_cost">￥{{= it.taxFee }}</td>
						</tr>
						
	</script>

	<script type="text/javascript">
		$(function()
		{
			HYWX.request({
				url : "${ctx}/wx/offer/getOfferOrder?id=" + $("#id").val(),
				success : function(data)
				{
					if (data.success)
					{
						data.obj = formatResult(data.obj);
						var interText = doT.template($("#offerDetailTmpl").text());
						$("#offerDetail").html(interText(data.obj));
					} else
					{
						HYWX.message.alert(data.message);
					}
				}
			})
		})
	</script>
</body>
</html>
