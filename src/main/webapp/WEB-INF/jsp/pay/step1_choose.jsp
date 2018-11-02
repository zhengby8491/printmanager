<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/layout/css/pay/pay_style.css?v=${v }" />
<title>选择购买产品</title>
<style type="text/css">
.pc_firstPay:: {
	background: #999;
	opacity: 0.5;
}

#payServe .cursorNo {
	cursor: no-drop;
	background: #999;
	pointer-events: none;
}

.pointerEvenNone {
	pointer-events: none;
	color: #999;
}
</style>
<script type="text/javascript">
//to server
var productMenuMap = JSON.parse('${productMenuMap}');
$(function() {
	function choose() {
		var $that = $(this);
		var $id = $that.data('id');
		var $data = productMenuMap[$id];
		var inputlist = $('input[name="moduleCheck"]')
		//点击版本选择相应的模块
		$('.pc_moduleDiv ul li').removeClass('active');
		$that.addClass('active').siblings().removeClass('active');
		inputlist.prop('checked', false);
		$.each(inputlist, function(index, obj) {
			var  $mid = Number($(this).data('id'));
			if($.inArray($mid, $data) != -1) {
				$(obj).prop('checked', true);
			}
		});
		if('${type}'==1){
			// 第一次购买价格动态显示
			$("#price").text($that.data('price'));
			$("#oprice").text($that.data('oprice'));
			$("#pc_goPay").attr("productId",$id);
		}else{
			// 服务升级价格动态显示
			$("#price2").text($that.data('price'));
			$("#oprice2").text($that.data('oprice'));
			$("#pc_goPay2").attr("productId",$id);
		}
	}
	//立即购买
	$("#pc_goPay,#pc_goPay2").on("click",function(){
		var id = $(this).attr("productId");
		location.href = Helper.basePath + "/pay/step2/"+id;
	});
	$(document).on('click', '.pc_moduleDiv ul li', choose);
	//根据用户是否首次购买，页面做相应展示
	if('${type}'==1){
		$('.pc_choosePay2').addClass('cursorNo').text('需要先购买产品才可以升级');
		$('.pc_choosePay1').removeClass('cursorNo');;
		$('.pc_serveList li').addClass('pointerEvenNone');
		$('.pc_firstPriceList li').removeClass('pointerEvenNone');
		$('.pc_servePrice').css("visibility",'hidden');
		$('.pc_byPrice').css('visibility','visible');
		$('.pc_serveList .serveNameSp').removeClass('colorDanger');
		$(".pc_firstPrice ul li").eq(0).click();

	}else{

		$('.pc_serveList .serveNameSp').removeClass('colorDanger');	

		$('.pc_serveList li').each(function(index,obj){
			if($(obj).find('.pc_productName').text()=="${purchaseRecord.productName}："){
				$(".pc_servePay ul li").eq(index).click();
				$(".pc_servePay ul li").eq(index).find('.serveNameSp').addClass('colorDanger');
			}
		})
		$('.pc_choosePay1').addClass('cursorNo');
		$('.pc_firstPriceList li').addClass('pointerEvenNone');
		$('.pc_serveList li').addClass('pointerEvenNone');
		$('.pc_byPrice').css('visibility','hidden');
		$('.pc_firstPriceList .serveNameSp').removeClass('colorDanger');

	}
});
</script>
</head>
<body>
	<div id="payServe">
		<%@include file="/WEB-INF/jsp/pay/common_header.jsp"%>
		<div class="payContent">
			<div class="pc_choose">
				<div class="pc_firstPay">
					<h3>第一次购买费</h3>
					<div class="pc_payContentDiv">
						<div class="pc_payImg">
							<span class="icon icon-ygj" style="font-size: 60px; color: #02A578;"></span>
						</div>
						<div class="pc_moduleDiv pc_firstPrice">
							<ul class="pc_firstPriceList">
								<c:forEach var="list" items="${productList}" varStatus="vs">
									<c:if test="${list.type eq 1 }">
										<li data-id="${list.id }" data-price="${list.price }" data-oprice="${list.originalPrice }">
											<p class="pc_currentPrice w_260">
												<span class="pc_productName">${list.name}：</span>
												<span class="colorDanger fontStrong serveNameSp">${list.price}元</span>
											</p>
											<p class="pc_originalPrice textDcoration">
												<span>原价：</span>
												<span>${list.originalPrice}元</span>
											</p>

										</li>
									</c:if>
								</c:forEach>
							</ul>
							<div class="pc_firstPriceNum  pc_byPrice">
								<p class="pc_currentPriced fontStrong">
									<span>价格：</span>
									<span class="colorDanger" id="price"></span>
									<span>元</span>
								</p>
								<p class="pc_originalPriced textDcoration" style="margin-left: 8px;">
									原价：
									<span id="oprice"></span>
									元
								</p>
							</div>
							<div class="pc_goPay pc_choosePay1" id="pc_goPay">
								<span>立即购买</span>
							</div>
						</div>
					</div>
				</div>
				<div class="pc_firstPay ">
					<h3>服务年费</h3>
					<div class="pc_payContentDiv">
						<div class="pc_payImg">
							<span class="icon icon-ygj" style="font-size: 60px; color: #02A578;"></span>
						</div>
						<div class="pc_moduleDiv pc_servePay">
							<ul class="pc_serveList">
								<c:forEach var="list" items="${productList}" varStatus="vs">
									<c:if test="${list.type eq 2}">
										<li data-id="${list.id }" data-price="${list.price }" data-oprice="${list.originalPrice }">
											<p class="pc_currentPrice w_260">
												<span class="pc_productName">${list.name}：</span>
												<span class="colorDanger fontStrong serveNameSp">${list.price}元</span>
											</p>
											<p class="pc_originalPrice textDcoration">
												<span>原价：</span>
												<span>${list.originalPrice}元</span>
											</p>

										</li>
									</c:if>
								</c:forEach>
							</ul>
							<div class="pc_firstPriceNum pc_servePrice">
								<p class="pc_currentPriced fontStrong">
									<span>价格：</span>
									<span class="colorDanger" id="price2"></span>
									<span>元</span>
								</p>
								<p class="pc_originalPriced textDcoration" style="margin-left: 8px;">
									原价：
									<span id="oprice2"></span>
									元
								</p>
							</div>
							<div class="pc_goPay pc_choosePay2" id="pc_goPay2">
								<span>立即续费</span>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="pc_module">
			<h3>模块清单标准</h3>
			<div class="pc_moduleTableCtent">
				<ul class="cleaerFloat moduleList">
					<li class="pc_moduleLi1">
						模块
						</th>
					<li class="pc_moduleLi2 ">模块功能</li>
					<li class="pc_moduleLi3">模块清单</li>
				</ul>
				<c:forEach items="${menuList}" var="node1">
					<table class="pc_moduleTable border-table " coldrag="false">
						<tr>
							<td class="pc_moduleTd1 node1" rowspan="${fn:length(node1.childrens)+1}" width="120">
								<span>
									<input type="checkbox" name="moduleCheck" class="input_check" data-id="${node1.id} " id="${node1.id}" disabled="disabled" />${node1.name}
									<label for="${node1.id}"></label>
								</span>
						</tr>
						<c:forEach items="${node1.childrens}" var="node2">
							<tr>
								<td>
									<table class="second_table" coldrag="false" rules="all" width="100%">
										<tr>
											<td rowspan="" class="node2" width="120">
												<span>
													<input type="checkbox" name="moduleCheck" class="input_check" data-id="${node2.id} " id="${node2.id}" disabled="disabled" />${node2.name}
													<label for="${node2.id}"></label>
												</span>
											</td>
											<td align="left" class="node3">
												<c:forEach items="${node2.childrens}" var="node3">
													<span>
														<input type="checkbox" name="moduleCheck" class="input_check" disabled="disabled" data-id="${node3.id}" id="${node3.id}" />${node3.name}
														<label for="${node3.id }"></label>
													</span>
												</c:forEach>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>
			</div>
		</div>
		<div class="payFooter stepFooter">
			<h3>购买流程</h3>
			<div>
				<ul class="cleaerFloat">
					<li>
						<div>
							<div class="itemContentTitle">
								<span>1.选择服务</span>
								<i class="icon iconfont">&#xe653;</i>
							</div>
							<p class="itemContentText">选择您需要的服务</p>
						</div>

					</li>
					<li>
						<div>
							<div class="itemContentTitle">
								<span>2.确认订单</span>
								<i class="icon iconfont">&#xe653;</i>
							</div>
							<p class="itemContentText">确认您的订单信息，以方便我们为您开通服务</p>
						</div>
					</li>
					<li>
						<div>
							<div class="itemContentTitle">
								<span>3.支付</span>
								<i class="icon iconfont">&#xe653;</i>
							</div>
							<p class="itemContentText">支持：支付宝、微信等支付方式</p>
						</div>
					</li>
					<li>
						<div>
							<div class="itemContentTitle">
								<span>4.申请发票</span>
								<i class="icon iconfont">&#xe653;</i>
							</div>
							<p class="itemContentText">如果需要发票，可在线联系申请发票</p>
						</div>
					</li>
					<li>
						<div>
							<div class="itemContentTitle">
								<span>5.购买成功</span>
							</div>
							<p class="itemContentText">支付成功后将为您开通服务</p>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	>
</body>
</html>