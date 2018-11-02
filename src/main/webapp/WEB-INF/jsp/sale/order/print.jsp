<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/print/formatMoney.js"></script>
<title>销售订单打印单</title>
</head>
<script type="text/javascript">
    $(function()
    {
        //金额转大写
        $("#format").text(digitUppercase($("#totalMoney").val()));
        $("#conform_print").click(function()
        {
            window.print()
        });
        $("#select_template").click(function()
        {
            Helper.popup.show('选择模板', Helper.basePath + '/common/printTemplate/selectTemplate?billType=SALE_SO&url=${fns:urlEncode("/sale/order/viewAjax/")}${order.id }', '1000', '500');
        });
    })
</script>
<body class="purch_body">
	<button class="nav_btn btn_print" id="conform_print" style="top: 20px; right: 70px; width: auto;">打印</button>
	<button class="nav_btn btn_print" id="select_template" style="top: 20px; width: auto;">选择模板</button>
	<div class="work_content">
		<div class="work_title" style="margin-bottom: 10px;">
			<h2>${loginUser.company.name }</h2>
			<h2>销售订单</h2>
		</div>
		<div class="outsouce_top">
			<div class="order_num">
				<span>销售单号：</span>
				<span>${order.billNo }</span>
			</div>
		</div>
		<div class="outsource_head" style="margin-bottom: 15px">
			<span style="margin-right: 16%">
				<div>客户名称：${order.customerName }</div>
				<div>
					地址：
					<span class="outsource_address">${order.deliveryAddress }</span>
				</div>
				<div>电话：${order.mobile }</div>
			</span>
			<span>
				<div>供应商名称：${loginUser.company.name }</div>
				<div>
					地址：
					<span class="outsource_address">${loginUser.company.address }</span>
				</div>
				<div>电话：${loginUser.company.tel }</div>
			</span>
		</div>
		<div class="outsource_p">
			<p>经双方协商同意，按以下条款签订合同</p>
			<p>甲方委托乙方印制下列活件:</p>
		</div>
		<table class="table outsource_table border-table">
			<thead>
				<tr>
					<th width="12%">成品名称</th>
					<th width="12%">产品规格</th>
					<th width="8%">数量</th>
					<th width="8%">单位</th>
					<th width="10%">单价</th>
					<th width="10%">金额</th>
					<th width="9%">交货日期</th>
					<th width="12%">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="detail" items="${order.detailList}">
					<tr>
						<td>${detail.productName}</td>
						<td>${detail.style}</td>
						<td>${detail.qty}</td>
						<td>${detail.unitName}</td>
						<td>${detail.price}</td>
						<td>${detail.money}</td>
						<td>
							<fmt:formatDate value="${detail.deliveryTime }" type="date" pattern="yyyy-MM-dd" />
						</td>
						<td>${detail.memo}</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="4" style="text-align: left; padding: 0 10px">
						合计：
						<span id="format"></span>
					</td>
					<td colspan="4" style="text-align: left; padding: 0 10px">合计(小写)：${order.totalMoney }</td>
					<input type="hidden" id="totalMoney" value="${order.totalMoney }" />
				</tr>
				<tr>
					<td colspan="1">备注</td>
					<td colspan="7" style="text-align: left; padding: 0 15px">${order.memo}</td>
				</tr>
			</tbody>
		</table>
		<div class="outsource_btm" style="margin-bottom: 30px">
			<span style="margin-right: 16%">
				<div>交货方式：${fns:basicInfoFiledValue('DELIVERYCLASS',order.deliveryClassId,'name')}</div>
				<div>交货地点：${order.deliveryAddress }</div>
			</span>
			<span>
				<div>联系人：${order.linkName }</div>
				<div>付款方式：${fns:basicInfoFiledValue('PAYMENTCLASS',order.paymentClassId,'name')}</div>
			</span>
		</div>
		<div class="outsource_btm">
			<span style="margin-right: 16%">
				<div>甲方代表(签字或盖章)：</div>
				<div>签字：</div>
			</span>
			<span>
				<div>乙方代表(签字或盖章)：</div>
				<div>签字：</div>
			</span>
		</div>
	</div>
</body>
</html>