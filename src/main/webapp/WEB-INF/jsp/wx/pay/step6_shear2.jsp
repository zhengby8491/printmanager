<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>分享页面</title>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<style type="text/css">
/**
 * 印管家logo样式
 **/
@font-face {
  font-family: 'iconhyui';
  src:  url('/static/hyui/fonts/iconhyui.eot?i4o94o');
  src:  url('/static/hyui/fonts/iconhyui.eot?i4o94o#iefix') format('embedded-opentype'),
    url('/static/hyui/fonts/iconhyui.ttf?i4o94o') format('truetype'),
    url('/static/hyui/fonts/iconhyui.woff?i4o94o') format('woff'),
    url('/static/hyui/fonts/iconhyui.svg?i4o94o#iconhyui') format('svg');
  font-weight: normal;
  font-style: normal;
}

[class^="icon-"], [class*=" icon-"] {
  /* use !important to prevent issues with browser extensions that change fonts */
  font-family: 'iconhyui' !important;
  speak: none;
  font-style: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  line-height: 1;

  /* Better Font Rendering =========== */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-ygj:before {
  content: "\e900";
}
</style>
</head>
<div id="pay_shear" class="content">
	<div class="sp_publicityContent">
		<div class="ps_logo">
			<span class="icon icon-ygj" style="font-size: 80px; color: #02A578;"></span>
		</div>
		<div class="ps_publicityText">
			<ul>
				<li>
					我是
					<span class="ps_linkName">${userVo.linkName }</span>
					我为印管家代言
				</li>
				<li>印管家让印刷老板</li>
				<li>放心管理，安心度假</li>
				<li>中小印刷企业 管理好帮手</li>
				<li>
					懂办公软件只需
					<span>2</span>
					小时学会
				</li>
			</ul>
		</div>
	</div>
	<div class="ps_QR">
		<div class="ps_title">
			<p>长按识别图中二维码或者微信扫一扫</p>
			<p>
				<i></i>
				<a href="${ctx}/wx/pay/view/step1/choose">立即购买</a>
			</p>
		</div>
		<div class="ps_QRimg">
			<div class="ps_imgCodeWx">
				<p>印管家微信端</p>
				<img src="${ctxStatic}/wx/img/pay/wxQR.png">
			</div>
		</div>

	</div>
</div>
<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
	<jsp:param value="check" name="module" />
</jsp:include>
</body>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm.min.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm-extend.min.js' charset='utf-8'></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script type="text/javascript">
		// url 参数解析
		function getQueryString(name) {
			var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
			var r = decodeURI(window.location.search).substr(1).match(reg);
			if (r != null) {
				return unescape(r[2]);
			}
			return null;
		};
		var linkName1 = getQueryString('linkName');
		$('.ps_linkName').text(linkName1);
		$.ajax({
			url : '${ctx}/wx/pay/getConfigInfo?linkName='+linkName1,
			type : 'get',
			success : function(data) {
				data = JSON.parse(data).obj;
				wx.config({
					debug :false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
					appId : data.appid, // 必填，公众号的唯一标识  
					timestamp : data.timestamp, // 必填，生成签名的时间戳  
					nonceStr : data.nonceStr, // 必填，生成签名的随机串  
					signature : data.signature,// 必填，签名，见附录1  
					jsApiList : [// 所有要调用的 API 都要加到这个列表中  
					'checkJsApi',  
					'onMenuShareTimeline',  
					'onMenuShareAppMessage',  
					'onMenuShareQQ',  
					'onMenuShareWeibo',  
					'onMenuShareQZone'  
					]
				});
			}
		})
		wx.ready(function () {  
		  //分享给朋友  
		  wx.onMenuShareAppMessage({  
             title: '印管家微信端', // 分享标题  
             desc: '印管家，中小印刷企业管理好帮手',  
             link:window.location.href,  
             imgUrl: "http://wx4.sinaimg.cn/mw690/005ylMJXgy1fhbifi481rj306703sjry.jpg", // 分享图标  
             trigger: function (res) {  
                 // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回 
             },  
             success: function (res) {  
         		// 分享成功执行此回调函数  
                 // alert('已分享');  
             },  
             cancel: function (res) {  
                 // alert('已取消');  
             },  
             fail: function (res) {  
                 // alert(JSON.stringify(res));  
             }  
         });  
         //分享到朋友圈  
        	 wx.onMenuShareTimeline({  
	            title: '印管家，中小印刷企业管理好帮手', // 分享标题  
	            link:window.location.href,  
	            imgUrl: "http://wx4.sinaimg.cn/mw690/005ylMJXgy1fhbifi481rj306703sjry.jpg", // 分享图标  
	            success: function () {  
	       			// 分享成功执行此回调函数  
	       			},  
       			cancel: function () {  
       			}  
       		}); 
     	});  

     </script>
</html>