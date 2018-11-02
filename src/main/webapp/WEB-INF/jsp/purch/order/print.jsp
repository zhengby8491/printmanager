<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/print/formatMoney.js"></script>
<meta charset="UTF-8">
<title></title>
<!-- 采购订单打印单 -->
</head>

<script>
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
            Helper.popup.show('选择模板', Helper.basePath + '/common/printTemplate/selectTemplate?billType=PURCH_PO&url=${fns:urlEncode("/purch/order/printAjax/")}${order.id }', '700', '400');
        });
    })
</script>

<body class="purch_body">
	<button class="nav_btn btn_print" id="conform_print" style="top: 20px; right: 70px; width: auto;">打印</button>
	<button class="nav_btn btn_print" id="select_template" style="top: 20px; width: auto;">选择模板</button>
	<div class="work_content">
		<div class="deliver_title">
			<h2>${loginUser.company.name}</h2>
			<h2>采购订单</h2>
		</div>
		<div class="purch_head_left">
			<div>
				<label>甲&nbsp;&nbsp;&nbsp;&nbsp;方&nbsp;：&nbsp;</label>
				<span>${loginUser.company.name }</span>
			</div>
			<div>
				<label>乙&nbsp;&nbsp;&nbsp;&nbsp;方&nbsp;：&nbsp;</label>
				<span>${order.supplierName}</span>
			</div>
		</div>
		<div class="purch_head_right">
			<div>
				<label>采&nbsp;购&nbsp;单&nbsp;号：&nbsp;</label>
				<span>${order.billNo }</span>
			</div>
			<div>
				<label>制&nbsp;单&nbsp;日&nbsp;期：&nbsp;</label>
				<span>
					<fmt:formatDate pattern="yyyy-MM-dd" value='${order.createTime}' type='date' />
				</span>
			</div>
		</div>
		<ul class="item_ul">
			<li>
				<p>经甲,乙双方本着平等友好进行协商，就产品及配件的采购事宜达成如下协议：</p>
				<p>一.乙方需制作甲方订购下列规格的型号产品,及双方商定的价格如下:名称,数量,金额,交货日期(以实际送货数量为准!)</p>
				<table class=" border-table purch_table">
					<thead>
						<tr>
							<th width="6%">序号</th>
							<th width="15%">材料名称</th>
							<th width="10%">规格</th>
							<th width="12%">计价数量</th>
							<th width="7%">数量</th>
							<th width="8%">单价</th>
							<th width="8%">计价单价</th>
							<th width="9%">金额</th>
							<th width="11%">交货日期</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="detail" varStatus="status" items="${order.detailList}">
							<tr>
								<td>${status.index+1}</td>
								<td>${detail.materialName}</td>
								<td>${detail.specifications}</td>
								<td>
									<fmt:formatNumber value="${detail.valuationQty}" type="currency" pattern="0.####" />
								</td>
								<td>
									<fmt:formatNumber value="${detail.qty}" type="currency" pattern="0.####" />
								</td>
								<td>${detail.price}</td>
								<td>${detail.valuationPrice}</td>
								<td>${detail.money}</td>
								<td>
									<fmt:formatDate pattern="yyyy-MM-dd" value='${detail.deliveryTime}' type='date' />
								</td>
								<td></td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="6" style="text-align: left; padding-left: 10px">
								合计(大写)：
								<span id="format"></span>
							</td>
							<td colspan="4" style="text-align: left; padding-left: 10px">合计(小写)：${order.totalMoney }</td>
							<input type="hidden" id="totalMoney" value="${order.totalMoney }" />
						</tr>
						<tr style="padding-left: 10px">
							<td colspan="10" style="text-align: left; padding-left: 10px">备注：</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li>
				<p>二.质量要求：</p>
				<p>1.乙方根据甲方提供的技术标准,质量检验标准等要求组织生产，并向甲方提供合格产品的检验报告(质量保证书)。</p>
				<p>2.乙方向甲方承诺质量保证期限为2年，如发现质量问题则乙方须负责赔偿责任。</p>
				<p>3.包装方式：按甲方要求执行，详见《技术协议》。</p>
			</li>
			<li>
				<p>三.交货方式:乙方将甲方订购的产品按照双方商定的交期,并在送货单上明确填写订单号.零件编号,产品品名及数量。</p>
				<p style="text-indent: 20px">送货地点：${loginUser.company.address}</p>
			</li>
			<li>
				<p>四.验收方式及标准：</p>
				<p>由甲方按技术标准要求，在5至10个工作日内对乙方的产品进行验收，如有不符合要求，甲方有权拒绝接受该批产品，并将不合格的产品退回乙方，且由乙方按照甲方要求的交期准时向甲方交付合格产品。</p>
			</li>
			<li>
				<p>五.付款方式：合约经双方签字或盖章后即生效，付款方式：${fns:basicInfo('PAYMENTCLASS',order.paymentClassId).name}</p>
			</li>
			<li>
				<p>六.合同纠纷的解决方式：</p>
				<p>1.本合同一式两份，甲乙双方各执一份，本合同自双方签字盖章之日起生效。</p>
				<p>2.本合同生效后双方不得任意解除或变更，如一方发生违约，另一方有权要求违约方赔偿由此造成的一切损失。</p>
				<p>3.双方本着友好协商方式解决，协商不成，向乙方所在地区人民法院提起诉讼进行裁决。</p>
			</li>
		</ul>
		<table class="table border-table purch_info_table">
			<tbody>
				<tr>
					<td>
						<div>
							<label>乙&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：</label>
							<span>${order.supplierName}</span>
						</div>
						<div>
							<label>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</label>
							<span>${order.supplierAddress }</span>
						</div>
						<div>
							<label>联&nbsp;系&nbsp;人：</label>
							<span>${order.linkName }</span>
						</div>
						<div>
							<label>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</label>
							<span>${order.mobile }</span>
						</div>
					</td>
					<td>
						<div>
							<label>甲&nbsp;&nbsp;方：</label>
							<span>${loginUser.company.name}</span>
						</div>
						<div>
							<label>地&nbsp;&nbsp;址：</label>
							<span>${loginUser.company.address}</span>
						</div>
						<div>
							<label>采&nbsp;&nbsp;购：</label>
							<span>${order.employeeName }</span>
						</div>
						<div>
							<label>电&nbsp;&nbsp;话：</label>
							<span>${loginUser.company.tel}</span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="sign">
			<div>
				<label>供应商回签：</label>
				<span></span>
			</div>
			<div>
				<label>制单：${order.createName}</label>
				<span></span>
			</div>
			<div style="float: right; width: 20%;">
				<label>审核：${order.checkUserName}</label>
				<span></span>
			</div>
		</div>
	</div>
</body>
</html>

