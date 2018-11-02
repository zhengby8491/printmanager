<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>公众号支付</title>

</head>
<body style="height: 0;">
	<!-- 	<button onclick="">立马支付</button> -->
	<script src="https://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
	<script type="text/javascript">
		function onBridgeReady() {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : "${appId}", //公众号名称，由商户传入     
				"timeStamp" : "${timeStamp}", //时间戳，自1970年以来的秒数     
				"nonceStr" : "${nonceStr}", //随机串     
				"package" : "prepay_id=${prepay_id}",
				"signType" : "MD5", //微信签名方式：     
				"paySign" : "${paySign}" //微信签名 
			}, function(res) {
				// 		    	   alert('"appid:"+${appId}');
				// 		    	   alert('"timeStamp:"+${timeStamp}');
				// 		    	   alert('"nonceStr:"+${nonceStr}');
				// 		    	   alert('"package:"+${prepay_id}');
				// 		    	   alert('${paySign}');
				// 		    	   for(var p in res) {
				// 		    		   alert(res[p]);
				// 		    	   }
				// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
				if (res.err_msg == "get_brand_wcpay_request:ok") {
					window.location = HYWX.basePath + "/wx/pay/view/step4/pay/0/${billNo}";
				}
				else {
					window.location = HYWX.basePath + "/wx/pay/view/step4/pay/1/${billNo}";
				}
			});
		}
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
						false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		} else {
			onBridgeReady();
		}
	</script>
</body>

</html>