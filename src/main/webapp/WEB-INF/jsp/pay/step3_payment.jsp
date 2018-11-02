<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/layout/css/pay/pay_style.css?${v }" />
<title>选择支付方式</title>
<style type="text/css">
body {
	font-family: -webkit-body;
	color: #474747;
}

.hideItime {
	display: none;
}

.boderControll .tableShow {
	display: block;
}

.tableItime {
	border-bottom: 2px solid #C1E1D6;
	padding-left: 60px;
}

.tableItime li {
	float: left;
	width: 90px;
	padding: 4px 20px;
	text-align: center;
	position: relative;
	bottom: -2px;
	cursor: pointer;
}

.tableItime li img {
	width: 30px;
	height: 24px;
	display: inline-block;
}

.itimeThis {
	background-color: #fff;
	border: 2px solid #C1E1D6;
	border-bottom: #fff;
	font-weight: bold;
	color: #15a67f;
}

.itimeThis:before {
	height: 1px;
	background-color: red;
	position: relative;
	z-index: 3;
	bottom: -1px;
}

.wxImg img {
	display: inline-block;
	width: 100%;
	height: 10px;
}

.weixinP p {
	font-size: 12px;
}

.dangerSpan {
	font-size: 14px;
	color: #FF5722;
	font-weight: bold;
}
</style>
</head>
<body>
	<div id="payFn">
		<%@include file="/WEB-INF/jsp/pay/common_header.jsp"%>
		<div class="fnContent">
			<div class="common_stepImgNav">
				<img src="${ctxStatic}/layout/images/pay/setpImg.png">
			</div>
			<div class="fc_text">
				<p>订单已提交，请尽快付款</p>
				<p>
					订单编号是：
					<span class="orderNo" data-orderid="${purchaseRecord.id}">${purchaseRecord.billNo}</span>
				</p>
			</div>
			<div class="fc_chooseFn">
				<div class="table">
					<ul class="tableItime cleaerFloat">
						<li class="itimeThis">
							<img src="${ctxStatic}/layout/images/pay/zhihubao.png">
							<span>支付宝</span>
						</li>
						<li>
							<img src="${ctxStatic}/layout/images/pay/weixinlogo.png">
							<span>微信</span>
						</li>
					</ul>
					<div class="boderControll">
						<div class="tableShow hideItime">
							<div style="padding: 10px 109px;">
								您的订单金额为：￥
								<span class="dangerSpan">${purchaseRecord.price+purchaseRecord.tax}</span>
								元
							</div>
							<div class="zhifubaoP">
								<div class="zfbImg">
									<img src="${ctxStatic}/layout/images/pay/payicon_alipay.png" style="width: 100%">
								</div>
								<a class="payBtn bgcolor_danger confirmAlipay" href="${ctx}/pay/alipay/pc/step1jump/${purchaseRecord.productId}/${purchaseRecord.id}" target="_block">使用支付宝支付</a>
							</div>
						</div>
						<div class="hideItime cleaerFloat">
							<div style="padding: 10px 109px; float: left">
								<img src="https://sp.jd.com/payment/2.0.0/css/i/phone-bg.png" style="width: 200px; height: 240px;">
							</div>
							<div class="weixinP" style="float: left">
								<p>
									您的订单金额为：￥
									<span class="dangerSpan">${purchaseRecord.price+purchaseRecord.tax}</span>
									元,请打开手机微信的'扫一扫'扫描下方二维码进行支付
								</p>
								<div class="wxImg">
									<img alt="" src="${ctxStatic}/layout/images/pay/wxPayQR.png">
								</div>
								<p>
									<button class="payBtn bgcolor_danger confirmWx" disabled="disabled">
										距离二维码失效还有
										<strong>120</strong>
										秒
									</button>
								</p>
								<div></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%@include file="/WEB-INF/jsp/pay/common_footer.jsp"%>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		var statPay = true;
		$(document).on('click','.tableItime li',function(){
			$(this).addClass('itimeThis').siblings().removeClass('itimeThis')
			var indexNm = $(this).index()
			$(this).parent().next('div').children('div').eq(indexNm).addClass('tableShow').siblings().removeClass('tableShow');
			if(statPay){
				countDown();
			}
		});
		function updateQR(intTime){
			$.ajax({
				url:'${ctx}/pay/findOrdrInfo',
				type:'get',
				data:{'orderId':$('.orderNo').attr('data-orderid')},
				success:function(data){
					if(typeof(data)=='string'){
						data = JSON.parse(data)
					}
					var dataObj = data.obj;
					if(dataObj.orderState==0){
						layer.alert('订单已取消')
						$('.confirmWx').text('您的订单已取消');
						clearInterval(intTime);
						$('.weixinP .wxImg img').attr('src','${ctxStatic}/layout/images/pay/wxPayQR.png')
						return false;
					}else if(dataObj.orderState==2){
						if(dataObj.isPay=='YES'){
							clearInterval(intTime);
							location.href="${ctx}/pay/step4?orderNo="+$('.orderNo').attr('data-orderid');
							return false;
						}
					}
				}
			})
		}

		function countDown(){
			$('.weixinP .wxImg img').attr('src','${ctx}/pay/weixin/pc?orderNo=${purchaseRecord.id}&productId=${purchaseRecord.productId}');
			var timeNo =120;
			statPay = false;
			$('.confirmWx').attr('disabled','disabled')
			var intTime = setInterval(function(){
				timeNo--;
				$('.confirmWx').text('距离二维码失效还有'+timeNo+'秒');
				if(timeNo%2==0){
					updateQR(intTime);
				}
				if(timeNo <=0){
					statPay = true;
					$('.weixinP .wxImg img').attr('src','${ctxStatic}/layout/images/pay/wxPayQR.png')
					clearInterval(intTime);
					$('.confirmWx').removeAttr("disabled").text('二维码已失效，请点击刷新二维码');
				}
			},1000)
		}
		$(document).on('click','.confirmWx',function(){
			if(statPay){
				countDown();
			}
		})

		function lay_modul(){
			var pay_newHtml = '<div style="overflow: hidden;\
			width: 420px;\
			margin: 0 auto;\
			margin-top: 20px;">\
			<div style="float:left">\
			</div>\
			<div style=" float: left;\
			margin-left: 20px;\
			margin-top: 12px;">\
			<p>支付完成前，请不要关闭此支付验证窗口。</p>\
			<p>支付完成后，请根据您支付的情况点击下面按钮。</p>\
		</div>\
	</div>'
	layer.open({
		type: 1,
		skin: '', 
		area: ['600px', '200px'], 
		content: pay_newHtml,
		title:['支付提示','background-color:#14A67E'],
		btn:['支付遇到问题咨询客服','支付完成前往首页'],
		btn1:function(){
			window.location.href ='http://wpa.qq.com/msgrd?v=3&uin=2880157226&site=qq&menu=yes'
		},
		btn2:function(){
			window.location.href="/";
		}
	})
}
$(document).on('click','.confirmAlipay',function(){
	lay_modul();
})
})
</script>
</html>