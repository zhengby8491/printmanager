<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>订单</title>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
</head>
<body>
	<form id="payOderForm">
		<div id="pay_order" class="content">
			<div class="po_header">确认订单</div>
			<table>
				<tr class="po_companyMessge">
					<td>1.公司信息</td>
					<td>
						<ul>
							<li>
								<span>公司名称: </span>
								<input type="text" name="companyName" value="${userVo.companyName }" readonly="true">
							</li>
							<li>
								<span>联系人: </span>
								<input type="text" name="userName" value="${userVo.linkName }" readonly="true">
							</li>
							<li>
								<span>联系电话: </span>
								<input type="text" name="telephone" value="${userVo.mobile }" readonly="true">
							</li>
							<li>
								<span>邀请人: </span>
								<input type="text" name="inviter" id="inviter">
							</li>
							<li>
								<span>邀请人电话: </span>
								<input type="text" name="inviterPhone" id="inviterPhone">
							</li>
						</ul>
					</td>
				</tr>
				<tr class="po_invoice">
					<td>2.发票信息</td>
					<td>
						<ul>
							<li>
								<span>
									<input type="radio" checked="checked" class="input_check" id="po_check1" name="invoice" value="0">
									<label for="po_check1"></label>
								</span>
								<label for="po_check1">不需要发票</label>
							</li>
							<li>
								<span>
									<input type="radio" class="input_check" id="po_check2" data-taxMoney="0.06" name="invoice" value="1">
									<label for="po_check2"></label>
								</span>
								<label for="po_check2">增值税发票(6%)</label>
							</li>
							<li>
								<span>
									<input type="radio" class="input_check" data-taxMoney="0.1" id="po_check3" name="invoice" value="2">
									<label for="po_check3"></label>
								</span>
								<label for="po_check3">增值税发票(17%)</label>
							</li>
						</ul>
					</td>
				</tr>
				<tr class="po_serve">
					<td>3.服务清单</td>
					<td>
						<p class="po_serveTitle">
							服务名称:
							<span>${product.name}</span>
						</p>
						<ul>
							<li class="composing_4">
								<span>原价</span>
								<span>折扣</span>
								<span>优惠价</span>
							</li>
							<li class="composing_4">
								<span>${product.originalPrice}元</span>
								<span>${product.originalPrice-product.price}元</span>
								<span>${product.price}元</span>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td class="po_orderMoney">4.订单结算</td>
					<td>
						<ul>
							<li class="po_orderMoney">
								<span>折后总金额</span>
								<span>税额</span>
								<span>需支付额</span>
							</li>
							<li class="po_orderMoney">
								<span>${product.price}元</span>
								<span class="po_tax">0元</span>
								<span class="po_lastMoney" style="color: #ff6600">${product.price}元</span>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td>5.服务合同</td>
					<td style="height: 4rem;">
						<div>
							<a href="http://pan.baidu.com/s/1geFhoyR " target="_block" class="button-fill button button-warning po_download po_contracter">下载合同</a>
						</div>
					</td>
				</tr>
			</table>
			<a class="button button-fill button-warning submOder" href="javascript:void(0)">提交订单</a>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="check" name="module" />
		</jsp:include>
	</form>
</body>
<script type="text/javascript">
	$('.po_invoice ul li').each(function(index,obj){
		$(obj).find('input').change(function(){
			if($(obj).find('input').attr('checked')&& $(obj).find('input').attr('data-taxMoney')!=null){				
				$('.po_contracter').attr('href','http://pan.baidu.com/s/1hsCD5x6');
				var taxNm = Number($(obj).find('input').attr('data-taxMoney'))*${product.price};
					//税额
					$('.po_tax').text(taxNm.toFixed(2)+'元' );
					//最终付款金额
					$('.po_lastMoney').text(${product.price}+Number(taxNm.toFixed(2))+'元' );
				}
				if($(obj).find('input').attr('data-taxMoney')==null){
					$('.po_tax').text(0+'元');
					$('.po_contracter').attr('href','http://pan.baidu.com/s/1geFhoyR');
					$('.po_lastMoney').text(${product.price}.toFixed(2)+'元');
				}
			});
	});
	$(function() {
		//获取提交数据
		function getformJson(){
			var formJson = {};
			formJson.productId = '${product.id}';
			formJson.productName= '${product.name}';
			formJson.price = '${product.price}';
			// TODO 税额
			formJson.userName = '${userVo.userName }';
			formJson.companyName = '${userVo.companyName }';
			formJson.linkMan = '${userVo.linkName }';
			formJson.telephone = '${userVo.mobile }';
			formJson.inviter = $.trim($("#inviter").val());
			formJson.inviterPhone = $.trim($("#inviterPhone").val());
			formJson.invoiceInfor = $("input[name='invoice']:checked").val();
			formJson.orderFrom = 2;
			return formJson;
		}
		
		$(".submOder").on("click", function() {
			var orderStatBl = true;
			$.ajax({
				url:HYWX.basePath+"/sys/buy/isPay",
				type:'get',
				async:false,
				success:function(data){
					data = JSON.parse(data);
					if(!data.obj){
						orderStatBl = false;
					}
				}
			});
			if(!orderStatBl){
				HYWX.message.alert('您有未支付订单，请到购买信息里面查看继续购买');
				return false;
			}
			var formJson = getformJson();
			HYWX.requestByObj({
				url : HYWX.basePath + "/wx/pay/step2/order/save",
				data : formJson,
				beforeSend : function() {
					HYWX.message.loading();
				},
				complete : function() {
					HYWX.message.hideLoading();
				},
				success : function(data) {

					if (data.success) {
						window.location = HYWX.basePath + "/wx/pay/view/step3/choose/pay/"+data.message;
					} else {
						HYWX.message.alert(data.message);
					}
				}
			});
		});
	});
</script>
</html>