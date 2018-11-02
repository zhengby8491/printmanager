<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/print/formatMoney.js"></script>
<title></title>
<script type="text/javascript" src="${ctxStatic}/site/sale/deliver/print.js?v=${v}"></script>
<!-- 送货单打印单 -->
</head>
<body class="deliver_body">
	<input id="urlEncode" type="hidden" value="${fns:urlEncode("/sale/deliver/viewAjax/")}"/>
	<input id="orderId" type="hidden" value="${order.id }"/>
	<button class="nav_btn btn_print" id="conform_print" style="top: 20px; right: 70px; width: auto;">打印</button>
	<button class="nav_btn btn_print" id="select_template" style="top: 20px; width: auto;">选择模板</button>
	<div class="work_content">
		<div class="deliver_content">
			<div class="deliver_title">
				<h2>${loginUser.company.name}</h2>
				<h2>销售送货单</h2>
			</div>
			<div class="deliver_head">
				<div style="width: 57%">
					<div>
						<label>客户名称：</label>
						<span>${order.customerName }</span>
					</div>
					<div>
						<label>送货地址：</label>
						<span class="deliver_address">${order.deliveryAddress }</span>
					</div>
				</div>
				<div style="width: 19%">
					<div>
						<label>联系人：</label>
						<span>${order.linkName }</span>
					</div>
					<div>
						<label style='letter-spacing: 0.4px;'>电&nbsp;&nbsp;&nbsp;话：</label>
						<span>${order.mobile}</span>
					</div>
				</div>
				<div style="float: right; width: 22%">
					<div>
						<label>送货单号：</label>
						<span> ${order.billNo} </span>
					</div>
					<div>
						<label>送货日期：</label>
						<span>
							<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />
						</span>
					</div>
				</div>
			</div>
			<table class="table deliver_table border-table">
				<thead>
					<tr>
						<th>客户单号</th>
						<th>销售单号</th>
						<th>客户料号</th>
						<th>成品名称</th>
						<th>规格</th>
						<th width="6%">数量</th>
						<th width="6%">备品</th>
						<th width="6%">单价</th>
						<th width="6%">单位</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="detail" items="${order.detailList}">
						<tr>
							<td>${detail.customerBillNo}</td>
							<td>${detail.sourceBillNo}</td>
							<td>${detail.customerMaterialCode}</td>
							<td>${detail.productName}</td>
							<td>${detail.style}</td>
							<td>${detail.qty}</td>
							<td>${detail.spareQty}</td>
							<td>${detail.price}</td>
							<td>${detail.unitName}</td>
							<td>${detail.memo}</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="6" style="text-align: left; padding: 0 10px">
							合计(大写)：
							<span id="format"></span>
						</td>
						<td colspan="4" style="text-align: left; padding: 0 10px">合计(小写)：${order.totalMoney }</td>
						<input type="hidden" id="totalMoney" value="${order.totalMoney }" />
					</tr>
				</tbody>
			</table>
			<p style="margin-bottom: 10px">备注：${order.memo}</p>
			<div class="deliver_btm1">
				<span style="width: 57%">
					打印日期：
					<fmt:formatDate pattern="yyyy-MM-dd" value='${printDate}' type='date' />
				</span>
				<span style="width: 19%">制单人：${order.createName}</span>
				<span style="float: right; width: 22%">审核：${order.checkUserName}</span>
			</div>
			<div class="deliver_btm2">
				<span>联一：财务白</span>
				<span>联二：预算红</span>
				<span>联三：客户蓝</span>
				<span style="margin: 0">联四：存跟黄</span>
			</div>
		</div>
	</div>
</body>
</html>