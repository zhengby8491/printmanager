<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>模块选择</title>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<style type="text/css">
#pay_module .cursorNo {
	cursor: no-drop;
	background: #e6e6e6;
	pointer-events: none;
	color: #999;
}
</style>
<script type="text/javascript">
	// to server;判断是否有未支付订单
	/* $.ajax({
			url:HYWX.basePath+"/sys/buy/isPay",
			type:'get',
			async:false,
			success:function(data){
				data = JSON.parse(data)
				if(data.obj==false){
			    $.alert('您有未支付订单，请前往购买信息继续支付',function(){
						return false;
					});
				}
			}
		}); */
	
	var productMenuMap = JSON.parse('${productMenuMap}');
	$(function() {
		
		function choose() {
			var $that = $(this);
			var $id = $that.data('id');
			var $data = productMenuMap[$id];
			var inputlist = $('input[name="moduleCheck"]');
			$('.pm_product li').removeClass('pm_active');
			$that.addClass('pm_active').siblings().removeClass('pm_active');
			$('input[type="checkbox"]').prop('checked', false);
			$.each(inputlist, function(index, obj) {
				var  $mid = $(this).data('id');
				if($.inArray($mid, $data) != -1) {
					$(obj).prop('checked', true);
				}
			});
			// 价格动态显示
			$(".pm_price .price").html($that.data('price')+'.00');
			$(".pm_price .oprice").html($that.data('oprice')+'.00');
			
			// 选择版本
			$(".ms_save").attr("href", HYWX.basePath + "/wx/pay/view/step2/order/" + $type + "/" + $id);
		}
		// 购买类型
		var $type = 1;
		$('.buttons-row a').click(
			function() {
				$(this).addClass('active').siblings().removeClass('active');
				var indexNum = $(this).index();
				if(${type}==1){	
					if($(this).data('type')==1){
						$('.ms_save').removeClass('cursorNo');
						$('.pm_price').css('visibility','visible')
					}else{
						$('.ms_save').addClass('cursorNo');
						$('.pm_price').css('visibility','hidden')
					}
				}else{
					$('.upgradeBuy li').each(function(index,obj){

						if($(obj).find('.pc_serveName').text()=="${purchaseRecord.productName}："){
							$(".upgradeBuy li").eq(index).css({
								'background':'#fff',
								'color':'#000'
							})
							$(".upgradeBuy li").eq(index).click();
						}
						$('.upgradeBuy li').addClass('cursorNo');
					})			
					if($(this).data('type')==1){
						$('.ms_save').addClass('cursorNo').text('立即购买');
						$('.pm_price').css('visibility','hidden')
					}else{
						$('.ms_save').removeClass('cursorNo').text('立即续费');
						$('.pm_price').css('visibility','visible')
					}
				}
				$('.pm_chooseProduct ul').eq(indexNum).addClass('showList').siblings().removeClass('showList');
				$type = $(this).data('type');
			});
		setTimeout(function(){
			$('.buttons-row a').eq(${type}-1).click()
		},10)
		
		$(document).on('click', '.pm_product li', choose);
		if(${type}==1)
		{
			$('.upgradeBuy li').addClass('cursorNo');
			// 默认选中第一条
			$(".firstBuy li").eq(0).click();
		}else{
			$('.firstBuy li').addClass('cursorNo');
			// $(".upgradeBuy li").eq(0).click();
		}
	});
</script>
</head>
<body>
	<div id="pay_module" class="content">
		<div class="pm_choose">
			<div class="pm_chooseFn">
				<div class="content-block">
					<p class="buttons-row">
						<a href="javascript:;" class="button active" data-type="1">第一次购买</a>
						<a href="javascript:;" class="button" data-type="2">服务年费</a>
					</p>
				</div>
			</div>
			<div class="pm_chooseProduct">
				<ul class="showList pm_product cleaerFloat firstBuy">
					<c:forEach var="list" items="${productList}" varStatus="vs">
						<c:if test="${list.type == 1 }">
							<li data-id="${list.id }" data-price="${list.price }" data-oprice="${list.originalPrice }">
								<p class="pc_currentPrice">
									<span>${list.name }：</span>
									<span>${list.price }</span>
									元
								</p>
								<p>&nbsp;</p>
								<p class="pc_originalPrice textDcoration">
									<span>原价：</span>
									<span>${list.originalPrice }</span>
									元
								</p>
							</li>
						</c:if>
					</c:forEach>
				</ul>
				<ul class="pm_product  cleaerFloat upgradeBuy">
					<c:forEach var="list" items="${productList}" varStatus="vs">
						<c:if test="${list.type == 2 }">
							<li data-id="${list.id }" data-price="${list.price }" data-oprice="${list.originalPrice }">
								<p class="pc_currentPrice">
									<span class="pc_serveName">${list.name }：</span>
									<span>${list.price }</span>
									元
								</p>
								<p>&nbsp;</p>
								<p class="pc_originalPrice textDcoration">
									<span>原价：</span>
									<span>${list.originalPrice }</span>
									元
								</p>
							</li>
						</c:if>
					</c:forEach>
				</ul>
				<div class="pm_price">
					<p>
						价格
						<span class="price">0</span>
						元
					</p>
					<p>
						原价
						<span class="oprice">0</span>
						元
					</p>
				</div>
			</div>
			<a class="button button-fill button-warning ms_save cetnerBt" href="##">立即购买</a>
		</div>
		<div class="pm_module">
			<table>
				<tr>
					<th class="pm_moduleTh">模块</th>
					<th>模块功能</th>
				</tr>
				<c:forEach var="list" items="${menuList }" varStatus="vs">
					<c:if test="${list.type=='MENU' }">
						<tr>
							<td>
								<span>
									<input type="checkbox" data-id="${list.id }" class="input_check" disabled="disabled" name="moduleCheck" id="pm_check1">
									<label for="pm_check1"></label>
								</span>
								<span>${list.name }</span>
							</td>
							<td>
								<c:forEach items="${list.childrens}" var="list2">
									<p class="pm_contentP">
										<input type="checkbox" class="input_check" disabled="disabled" data-id="${list2.id }" name="moduleCheck" id="pm_check2">
										<label for="pm_check2"></label>
										</span>
										<span>${list2.name }
									</p>
								</c:forEach>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
		<jsp:param value="check" name="module" />
	</jsp:include>
</body>
<script type="text/javascript">
	$(function() {
		$('.buttons-row a').click(
			function() {
					$(this).addClass('active').siblings().removeClass('active')
					var indexNum = $(this).index();
					$('.pm_chooseProduct ul').eq(indexNum).addClass('showList')
					.siblings().removeClass('showList')
				})
	})
</script>
</html>