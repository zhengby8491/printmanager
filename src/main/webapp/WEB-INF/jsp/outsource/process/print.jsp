<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/print/formatMoney.js"></script>
<title></title>
<!-- 发外加工单打印单 -->
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
            Helper.popup.show('选择模板', Helper.basePath + '/common/printTemplate/selectTemplate?billType=OUTSOURCE_OP&url=${fns:urlEncode("/outsource/process/printAjax/")}${order.id }', '700', '400');
        });
    })
</script>
<body class="purch_body">
	<button class="nav_btn btn_print" id="conform_print" style="top: 20px; right: 70px; width: auto;">打印</button>
	<button class="nav_btn btn_print" id="select_template" style="top: 20px; width: auto;">选择模板</button>
	<div class="work_content">
		<div class="work_title" style="margin-bottom: 10px;">
			<h2>${loginUser.company.name }</h2>
			<h2>发外加工单</h2>
		</div>
		<div class="outsouce_top">
			<div class="order_num">
				<span>加工单号：</span>
				<span>${order.billNo}</span>
			</div>
		</div>
		<div class="outsource_head">
			<span style="margin-right: 16%">
				<strong>甲方</strong>
				<div>单位名称：${loginUser.company.name }</div>
				<div>
					地 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：
					<span class="outsource_address">${loginUser.company.address }</span>
				</div>
				<div>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：${loginUser.company.tel }</div>
				<div>
					打印日期：
					<fmt:formatDate value="${printDate}" type="date" pattern="yyyy-MM-dd" />
				</div>
			</span>
			<span>
				<strong>乙方</strong>
				<div>单位名称：${order.supplierName }</div>
				<div>
					地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：
					<span class="outsource_address">${order.supplierAddress }</span>
				</div>
				<div>联&nbsp; 系&nbsp; 人：${order.linkName }</div>
				<div>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：${order.mobile}</div>
			</span>
		</div>
		<table class="table outsource_table border-table">
			<thead>
				<tr>
					<th width="13%">生产单号</th>
					<th width="14%">部件名称</th>
					<th width="12%">产品/工序</th>
					<th width="10%">加工规格</th>
					<th width="7%">数量</th>
					<th width="7%">单价</th>
					<th width="9%">金额</th>
					<th>加工要求</th>
					<th width="12%">备注</th>

				</tr>
			</thead>
			<tbody>
				<c:forEach var="detail" items="${order.detailList}">
					<tr>
						<td>${detail.workBillNo}</td>
						<td>${detail.partName}</td>
						<td>
							<c:if test="${detail.type eq 'PRODUCT'}">
								${detail.productName}
							</c:if>
							<c:if test="${detail.type eq 'PROCESS'}">
								${detail.procedureName}
							</c:if>
						</td>
						<td>${detail.style}</td>
						<td>${detail.qty}</td>
						<td>${detail.price}</td>
						<td>${detail.money}</td>
						<td>${detail.processRequire}</td>
						<td>${detail.memo }</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="4" style="text-align: left; padding: 0 10px">
						合计：
						<span id="format"></span>
					</td>
					<td colspan="5" style="text-align: left; padding: 0 10px">合计(小写)：${order.totalMoney }</td>
					<input type="hidden" id="totalMoney" value="${order.totalMoney }" />
				</tr>
				<tr>
					<td colspan="1" style="height: 50px">质量要求</td>
					<td colspan="8"></td>
				</tr>

				<tr>
					<td colspan="1">备注</td>
					<td colspan="8" style="text-align: left; padding: 0 15px">${order.memo}</td>
				</tr>
			</tbody>
		</table>
		<div class="outsource_p">
			<p>甲乙双方就上述外协加工事宜达成如下约定条款：</p>
			<p>1. 乙方在收到此订单后需按照订单中要求的交货日期保质保量交货；</p>
			<p>2. 双方对订单中未尽事宜本着友好协商的原则协商解决；</p>
			<p>3. 本订单一式两份，甲乙双方各执一份。</p>
		</div>
		<div class="outsource_btm">
			<span style="margin-right: 16%">
				<div style='margin-bottom: 10px'>甲方代表(签字或盖章)：</div>
				<div>
					制单日期：
					<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />
				</div>
				<div>审核：${order.checkUserName}</div>
				<div>
					交货日期：
					<fmt:formatDate value="${order.deliveryTime}" type="date" pattern="yyyy-MM-dd" />
				</div>
			</span>
			<span>
				<div style='margin-bottom: 10px'>乙方代表(签字或盖章)：</div>
				<div>审核：</div>
				<div>日期：</div>
			</span>
		</div>
	</div>
</body>
</html>